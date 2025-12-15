package server.game;

import common.GameOverDTO;
import common.QuestionDTO;
import common.ScoreboardDTO;
import concurrency.Barrier;
import concurrency.CountDownLatch;
import server.net.ClientHandler;

import java.io.IOException;
import java.util.ArrayList;
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
    private final Map<String, ClientHandler> playersHandlers;
    private final Map<String, Integer> currentRoundPoints; // username --> points
    private final Map<String, Integer> pendingTeamRoundPoints; // teamID--> pending points
    private Barrier barrier;
    private final CountDownLatch waitForPlayerLatch;
    private CountDownLatch roundLatch;
    private CountDownLatch timer;
    private int currentQuestionIndex;
    private Question currentQuestion;
    private ScoreboardDTO scoreboard;
    private final int roundTime = 30000;
    private List<Question> gameQuestions;


    public GameState(String roomCode, int numTeams, int numPlayersPerTeam, Quiz quiz) {
        this.roomCode = roomCode;
        this.maxPlayers = numTeams * numPlayersPerTeam;
        this.maxTeams = numTeams;
        this.numPlayersPerTeam = numPlayersPerTeam;
        this.quiz = quiz;

        this.players = new ConcurrentHashMap<>();
        this.teamScores = new ConcurrentHashMap<>();
        this.playersHandlers = new ConcurrentHashMap<>();
        this.currentRoundPoints = new ConcurrentHashMap<>();
        this.pendingTeamRoundPoints = new ConcurrentHashMap<>();

        this.waitForPlayerLatch = new CountDownLatch(1, 0, 1000000, maxPlayers);
        this.barrier = new Barrier(maxPlayers, roundTime, () -> {
            processTeamRoundPoints();
        });

        this.currentQuestionIndex = -1;
        this.gameQuestions = new ArrayList<>(quiz.getQuestions());
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

    public Question requestNextQuestion() {
//        currentQuestionIndex++;
//        if(currentQuestionIndex >= quiz.getQuestions().size()) {
//            System.out.println("Quiz has finished");
//            return null;
//        }
//        return quiz.getQuestions().get(currentQuestionIndex);
        if(gameQuestions.isEmpty()){
            return null;
        }
        currentQuestionIndex = (int) (Math.random() * gameQuestions.size());
        Question nextQuestion = gameQuestions.get(currentQuestionIndex);
        gameQuestions.remove(currentQuestionIndex);
        return nextQuestion;
    }

    public void sendQuestion(QuestionDTO question) {
        for (ClientHandler playerHandler : playersHandlers.values()) {
            try {
                playerHandler.send(question);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void submitAnswer(String username, int answerIndex) throws InterruptedException, IOException {
        if(currentQuestion == null)
            return;
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
        } else {
            if(isCorrectAnswer) {
                pointsEarned = currentQuestion.getPoints();
                pendingTeamRoundPoints.merge(player.getTeamId(), pointsEarned, Integer::sum);
            }
        }
        currentRoundPoints.put(username, pointsEarned);

        if(!currentQuestion.isTeam()) {
            timer.countDown();
            roundLatch.await();
        } else  {
            try {
                barrier.await();
            } finally {
                timer.countDown();
            }
        }
    }

    public boolean isCorrectAnswer(int answerIndex) {
        return answerIndex == currentQuestion.getCorrect();
    }

    public void updateScoreboard() throws IOException {
        for(String username: players.keySet()) {
            Player player = players.get(username);
            String teamId = player.getTeamId();
            int totalTeamPoints = teamScores.get(teamId);
            String[][] leaderboard = generateLeaderboard();
            String position = getTeamPosition(teamId, leaderboard);
            int pointsEarned = currentRoundPoints.getOrDefault(username, 0);
            ScoreboardDTO scoreboard = new ScoreboardDTO(position, String.valueOf(pointsEarned), String.valueOf(totalTeamPoints), leaderboard);
            playersHandlers.get(username).send(scoreboard);
        }
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
        waitForPlayerLatch.await();

        System.out.println("All players are ready. Starting...");
        // startRound();o
        startGame();
    }

    private void startGame() throws IOException, InterruptedException {
        while(true) {
            currentQuestion = requestNextQuestion();
            if(currentQuestion == null) {
                System.out.println("Quiz ended");
                break;
            }
            barrier = new Barrier(maxPlayers, roundTime, this::processTeamRoundPoints);
            currentRoundPoints.clear();
            pendingTeamRoundPoints.clear();

            roundLatch = new CountDownLatch(2, 2, roundTime, maxPlayers);
            timer = new CountDownLatch(1, 0, roundTime, maxPlayers);

            QuestionDTO nextQuestionDTO = new QuestionDTO(currentQuestion.getQuestion(), currentQuestion.getOptions());
            sendQuestion(nextQuestionDTO);

            timer.await();

            updateScoreboard();

            Thread.sleep(5000); // só para dar um tempo para visualizar o scoreboard
        }
        finishGame();
    }

    private void finishGame() throws IOException {
        String winningTeamId = "";
        int maxScore = 0;
        for(Map.Entry<String, Integer> entry : teamScores.entrySet()) {
            int currentScore =  entry.getValue();
            if(currentScore > maxScore) {
                maxScore = currentScore;
                winningTeamId = entry.getKey();
            }
        }
        GameOverDTO gameOver = new GameOverDTO(winningTeamId);
        for(String username: players.keySet()) {
            playersHandlers.get(username).send(gameOver);
        }
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
                    } else { // if(teamPoints == correctPoints * numPlayersPerTeam) {
                        teamScores.merge(teamId, correctPoints * 2, Integer::sum);
                    }
                }
            }
        }
    }
}
