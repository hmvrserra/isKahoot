package server.game;

import java.util.List;

public class Question {

    String question;
    int points;
    int correct;
    List<String> options;

    public Question(String question, int points) {
        this.question = question;
        this.points = points;
    }

    public String getQuestion() {
        return question;
    }
    public int getPoints() {
        return points;
    }
    public int getCorrect() {
        return correct;
    }
    public List<String> getOptions() {
        return options;
    }
}
