package server.game;

public class Player {
    String username;
    String teamId;
    String roomId;
    
    public Player(String username, String teamId, String roomId) {
        this.username = username;
        this.teamId = teamId;
        this.roomId = roomId;
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
}
