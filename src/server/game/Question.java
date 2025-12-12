package server.game;

import java.util.List;

public class Question {

    private String question;
    private int points;
    private int correct;
    private boolean team;
    private String[] options;

    public Question(String question, int points, int correct, boolean team, String[] options) {
        this.question = question;
        this.points = points;
        this.correct = correct;
        this.team = team;
        this.options = options;
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
    public String[] getOptions() {
        return options;
    }
    public boolean isTeam() {
        return team;
    }
}
