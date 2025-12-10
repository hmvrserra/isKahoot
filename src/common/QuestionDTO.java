package common;

import java.io.Serializable;
import java.util.List;

public class QuestionDTO implements Serializable {

    private String question;
    private List<String> options;

    public QuestionDTO(String question, List<String> options) {
        this.question = question;
        this.options = options;
    }

    public String getQuestion() {
        return question;
    }
    public List<String> getOptions() {
        return options;
    }
}
