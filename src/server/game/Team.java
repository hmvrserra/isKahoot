package server.game;

public class Team {
    
    public String teamId;
    public String teamName;
    public int teamScore;
    
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
}
