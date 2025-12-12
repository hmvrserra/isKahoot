package common;

import java.io.Serializable;

public class ScoreboardDTO implements Serializable {

    private String position;
    private String roundPoints;
    private String totalPoints;
    private String[][] leaderboard;

    public ScoreboardDTO(String position, String roundPoints, String totalPoints, String[][] leaderboard) {
        this.position = position;
        this.roundPoints = roundPoints;
        this.totalPoints = totalPoints;
        this.leaderboard = leaderboard;
    }

    public String getPosition() {
        return position;
    }
    public String getRoundPoints() {
        return roundPoints;
    }
    public String getTotalPoints() {
        return totalPoints;
    }
    public String[][] getLeaderboard() {
        return leaderboard;
    }
}
