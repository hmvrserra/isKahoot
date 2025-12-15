package common;

import java.io.Serializable;

public class GameOverDTO implements Serializable {

    private String winnerTeamId;

    public GameOverDTO(String winnerTeamId){
        this.winnerTeamId = winnerTeamId;
    }

    public String getWinnerTeamId() {
        return winnerTeamId;
    }
}
