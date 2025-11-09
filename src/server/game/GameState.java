package server.game;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameState {
    
    String roomCode;
    int numTeams;
    Quiz quiz;
    
    private final Map<String, Team> teams;
    private final Map<String, Player> players;
    private final Map<String, Integer> teamScores;
    
    int currentQuestionIndex;
    private final long roundTimeMillis = 30000;
    
    
    public GameState(String roomCode, int numTeams, Quiz quiz) {
        this.roomCode = roomCode;
        this.numTeams = numTeams;
        this.quiz = quiz;
        
        this.teams = new ConcurrentHashMap<>();
        this.players = new ConcurrentHashMap<>();
        this.teamScores = new ConcurrentHashMap<>();
        
        this.currentQuestionIndex = -1;
    } 
        
    // ------------------------------------- Getters -------------------------------------
    public String getRoomCode() {
        return roomCode;
    }
    
    public int getNumTeams() {
        return numTeams;
    }
    
    public Quiz getQuiz() {
        return quiz;
    }
    
    public Map<String, Team> getTeams() {
        return teams;
    }
    
    public Map<String, Player> getPlayers() {
        return players;
    }
    
    public Map<String, Integer> getTeamScores() {
        return teamScores;
    }
    
    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }
    
    // --------------------------------- Game Logic Methods ---------------------------------
    
    public void addPlayer(Player player) {
        players.put(player.getUsername(), player);
    }
    
    public void addTeam(Team team) {
        teams.put(team.getTeamId(), team);
        teamScores.put(team.getTeamId(), 0);
    }
    
    public void nextQuestion() {
        currentQuestionIndex++;
    }
}
