package common;

import java.io.Serializable;

public class LoginRequest implements Serializable {

    private String username;
    private String team;
    private String room;

    public LoginRequest(String username, String team, String room) {
        this.username = username;
        this.team = team;
        this.room = room;
    }

    public String getUsername() {
        return username;
    }

    public String getTeam() {
        return team;
    }

    public String getRoom() {
        return room;
    }

    @Override
    public String toString() {
        return "Login: " + username + " da equipa " + team + " na " + room;
    }
}
