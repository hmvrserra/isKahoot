package server.game;

import common.QuestionDTO;
import common.ScoreboardDTO;
import concurrency.Barrier;
import concurrency.CountDownLatch;
import server.net.ClientHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class GameState {

    private final String roomCode;
    private final int maxPlayers;
    private final int maxTeams;
    private final int numPlayersPerTeam;
    Quiz quiz;

    // private final Map<String, List<Player>> teams; // teamId --> lista de membros da equipa
    private final Map<String, Player> players; // Username --> Player
    private final Map<String, Integer> teamScores; // teamID--> points
    private final Map<String, String> currentRoundAnswers;
    private final Map<String, ClientHandler> playersHandlers;
    private final Map<String, Integer> pendingTeamRoundPoints; // teamID--> pending points
    private final Barrier barrier;
    private final CountDownLatch waitForPlayerLatch;
    private CountDownLatch roundLatch;
    private int currentQuestionIndex;
    private Question currentQuestion;
    private ScoreboardDTO scoreboard;
    private final long roundTimeMillis = 30000;
    private boolean nextRoundStarted = false;


    public GameState(String roomCode, int numTeams, int numPlayersPerTeam, Quiz quiz) {
        this.roomCode = roomCode;
        this.maxPlayers = numTeams * numPlayersPerTeam;
        this.maxTeams = numTeams;
        this.numPlayersPerTeam = numPlayersPerTeam;
        this.quiz = quiz;

//        this.teams = new ConcurrentHashMap<>();
        this.players = new ConcurrentHashMap<>();
        this.teamScores = new ConcurrentHashMap<>();
        this.currentRoundAnswers = new ConcurrentHashMap<>();
        this.playersHandlers = new ConcurrentHashMap<>();
        this.pendingTeamRoundPoints = new ConcurrentHashMap<>();

        this.waitForPlayerLatch = new CountDownLatch(maxPlayers);
        this.barrier = new Barrier(maxPlayers, () -> {
            processTeamRoundPoints();
        });

        this.currentQuestionIndex = -1;
        Collections.shuffle(quiz.questions);
    }


    public synchronized void addPlayer(Player player, ClientHandler playerHandler) throws Exception {
        boolean teamExists = teamScores.containsKey(player.getTeamId());
        if(!teamExists && teamScores.size() >= maxTeams) {
            throw new Exception("\"Can't add more teams, limit reached\"");
        }
        players.put(player.getUsername(), player);
        playersHandlers.put(player.getUsername(), playerHandler);
        teamScores.putIfAbsent(player.getTeamId(), 0);
        waitForPlayerLatch.countDown();
    }

    public void startRound() throws IOException {
        nextRoundStarted = false;
        barrier.reset();
        roundLatch = new CountDownLatch(maxPlayers);
        pendingTeamRoundPoints.clear();
        currentQuestion = requestNextQuestion();
        if(currentQuestion == null) {
            System.out.println("No more questions");
            return;
        }
        QuestionDTO nextQuestionDTO = new QuestionDTO(currentQuestion.getQuestion(), currentQuestion.getOptions());
        sendQuestion(nextQuestionDTO);
    }

    public Question requestNextQuestion() {
        currentQuestionIndex++;
        if(currentQuestionIndex >= quiz.getQuestions().size()) {
            System.out.println("Quiz has finished");
            return null;
        }
        return quiz.getQuestions().get(currentQuestionIndex);
    }

    public synchronized void sendQuestion(QuestionDTO question) throws IOException {
        for (ClientHandler playerHandler : playersHandlers.values()) {
            playerHandler.send(question);
        }
    }

    public void submitAnswer(String username, int answerIndex) throws InterruptedException, IOException {
        if (currentQuestion == null) return; // Game ended, ignore late answers
        Player player = players.get(username);
        boolean isCorrectAnswer = isCorrectAnswer(answerIndex);
        int pointsEarned = 0;
        if(!currentQuestion.isTeam()){
            int bonusMultiplier = roundLatch.countDown();
            if(isCorrectAnswer) {
                pointsEarned = currentQuestion.getPoints() *  bonusMultiplier;
                player.addPoints(pointsEarned);
                teamScores.merge(player.getTeamId(), pointsEarned, Integer::sum); // Nas perguntas individuais a classificação de uma equipa é a soma dos valores obtidos por cada jogador;
            }
            roundLatch.await();
        } else {
            if(isCorrectAnswer) {
                pointsEarned = currentQuestion.getPoints();
                pendingTeamRoundPoints.merge(player.getTeamId(), pointsEarned, Integer::sum);
            }
            barrier.await();
        }
        updateScoreboard(username, pointsEarned);
        synchronized(this) {
            if (!nextRoundStarted) {
                nextRoundStarted = true;
                startNextRound();
            }
        }
    }

    public boolean isCorrectAnswer(int answerIndex) {
        return answerIndex == currentQuestion.getCorrect();
    }

    public void updateScoreboard(String username, int pointsEarned) throws IOException {
        Player player = players.get(username);
        String teamId = player.getTeamId();
        int totalTeamPoints = teamScores.get(teamId);
        String[][] leaderboard = generateLeaderboard();

        String position = getTeamPosition(teamId, leaderboard);

        ScoreboardDTO scoreboard = new ScoreboardDTO(position, String.valueOf(pointsEarned), String.valueOf(totalTeamPoints), leaderboard);
        playersHandlers.get(username).send(scoreboard);
    }

    private String[][] generateLeaderboard() {
        List<String> sortedTeams = new ArrayList<>(teamScores.keySet());
        sortedTeams.sort((team1, team2) -> {
            int pointsTeam1 = teamScores.get(team1);
            int pointsTeam2 = teamScores.get(team2);
            return pointsTeam2 - pointsTeam1;
        });
        String[][] leaderboard = new String[sortedTeams.size()][2];
        for (int i = 0; i < sortedTeams.size(); i++) {
            String teamName = sortedTeams.get(i);
            String teamPoints = String.valueOf(teamScores.get(teamName));
            leaderboard[i][0] = teamName;
            leaderboard[i][1] = teamPoints;
        }
        return leaderboard;
    }

    private String getTeamPosition(String teamId, String[][] leaderboard) {
        for(int i = 0; i < leaderboard.length; i++) {
            if(leaderboard[i][0].equals(teamId))
                return (i + 1) + "º";
        }
        return "";
    }

    public void awaitPlayersAndStart() throws InterruptedException, IOException {
        System.out.println("Waiting for players to start");
        waitForPlayerLatch.await(1000000);

        System.out.println("All players are ready. Starting...");
        startRound();
    }

    public Map<String, Player> getPlayers() {
        return players;
    }

    private void processTeamRoundPoints() {
        if(currentQuestion.isTeam()) {
            for(String teamId: teamScores.keySet()) {
                int teamPoints = pendingTeamRoundPoints.getOrDefault(teamId, 0);
                // int teamSize = teams.get(teamId).size();
                int correctPoints = currentQuestion.getPoints();
                if(teamPoints != 0) {
                    if(teamPoints != correctPoints * numPlayersPerTeam) {
                        teamScores.merge(teamId, correctPoints, Integer::sum);
                    } else if(teamPoints == correctPoints * numPlayersPerTeam) {
                        teamScores.merge(teamId, correctPoints * 2, Integer::sum);
                    }
                }
            }
        }
        synchronized(this) {
            if (!nextRoundStarted) {
                nextRoundStarted = true;
                try {
                    startNextRound();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private synchronized void startNextRound() throws IOException {
        startRound();
    }
}
