package server.game;

import java.util.List;

public class Team {
    
    private String teamId;
    private String teamName;
    private int teamScore;
    private List<Player> players;
    
    public Team(String teamId, String teamName, int teamScore) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.teamScore = teamScore;
    }
    
    public String getTeamId() {
        return teamId;
    }
    
    public String getTeamName() {
        return teamName;
    }
    
    public int getTeamScore() {
        return teamScore;
    }

    public void addScore(int pointsEarned){
        this.teamScore += pointsEarned;
    }

    public void addPlayer(Player player){
        players.add(player);
    }
}
