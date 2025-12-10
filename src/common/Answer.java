package common;

import java.io.Serializable;

public class Answer implements Serializable {

    private String username;
    private int answer;

    public Answer(String username, int answer) {
        this.username = username;
        this.answer = answer;
    }

    public int getAnswer() {
        return answer;
    }
}
