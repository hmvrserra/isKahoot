package common;

import java.io.Serializable;

public class ScoreboardDTO implements Serializable {

    private String position;
    private String roundPoints;
    private String totalPoints;

    public ScoreboardDTO(String position, String roundPoints, String totalPoints) {
        this.position = position;
        this.roundPoints = roundPoints;
        this.totalPoints = totalPoints;
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
}
