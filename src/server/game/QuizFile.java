package server.game;

import java.util.List;

public class QuizFile {

    List<Quiz> quizzes;

    public QuizFile(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }
    public List<Quiz> getQuizzes() {
        return quizzes;
    }
}
