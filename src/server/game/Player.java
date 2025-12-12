package server.game;

public class Player {
    String username;
    String teamId;
    String roomId;
    int pointsEarned;
    
    public Player(String username, String teamId, String roomId) {
        this.username = username;
        this.teamId = teamId;
        this.roomId = roomId;
        this.pointsEarned = 0;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getTeamId() {
        return teamId;
    }
    
    public String getRoomId() {
        return roomId;
    }
    public int getPointsEarned() {
        return pointsEarned;
    }

    public void addPoints(int pointsEarned){
        this.pointsEarned += pointsEarned;
    }
}
