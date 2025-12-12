package common;

import java.io.Serializable;
import java.util.List;

public class QuestionDTO implements Serializable {

    private String question;
    private String[] options;

    public QuestionDTO(String question, String[] options) {
        this.question = question;
        this.options = options;
    }

    public String getQuestion() {
        return question;
    }
    public String[] getOptions() {
        return options;
    }
}
