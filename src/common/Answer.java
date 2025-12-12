package common;

import java.io.Serializable;

public class Answer implements Serializable {

    private int answer;

    public Answer(int answer) {
        this.answer = answer;
    }

    public int getAnswer() {
        return answer;
    }
}
