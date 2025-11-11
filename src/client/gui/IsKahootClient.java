package client.gui;

import server.game.Question;
import server.game.Quiz;
import server.game.QuizFile;
import server.game.QuizLoader;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class IsKahootClient {

    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public static final String WAITING_PANEL = "WAITING";
    public static final String QUESTION_PANEL = "QUESTION";
    public static final String SCOREBOARD_PANEL = "SCOREBOARD";

    private QuestionPanel questionPanel;
    private ScoreboardPanel scoreboardPanel;

    //ATRIBUTOS TESTE PRIMEIRA DEMONSTRACAO (APAGAR DEPOIS)

    private List<Question> allQuestions;
    private int currentQuestionIndex = -1;

    public IsKahootClient(String ip, String port, String room, String team, String username) {
        
        frame = new JFrame("IsKahoot");
        frame.setSize(800, 600); // Ajustar altura aqui se quisermos mudar
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        WaitingPanel waitingPanel = new WaitingPanel();
        questionPanel = new QuestionPanel(this);
        scoreboardPanel = new ScoreboardPanel();

        mainPanel.add(questionPanel, QUESTION_PANEL);
        mainPanel.add(scoreboardPanel, SCOREBOARD_PANEL);
        mainPanel.add(waitingPanel, WAITING_PANEL);

        frame.add(mainPanel);

        // PRIEMIRA DEMONSTRACAO (APAGAR DEPOIS)
        loadQuiz();

        if(allQuestions != null && !allQuestions.isEmpty()) {
            requestNextQuestion();
        } else {
            System.err.println("No questions found!");
            cardLayout.show(mainPanel, WAITING_PANEL);
        }

    }

    public void start() {
        frame.setVisible(true);
    }

    public void showQuestion(String questionText, String[] options) {
        questionPanel.setQuestion(questionText, options);
        cardLayout.show(mainPanel, QUESTION_PANEL);
    }

//    public void showScoreboard(String pos, String change, String total, Object[][] data) {
//        ScoreboardPanel sp = new ScoreboardPanel();
//        sp.updateScores(pos, change, total, data);
//        mainPanel.add(sp, SCOREBOARD_PANEL);
//        cardLayout.show(mainPanel, SCOREBOARD_PANEL);
//    }

    //TESTES PARA PRIMEIRA DEMONSTRACAO (APAGAR DEPOIS)

    private void loadQuiz(){
        QuizFile quizFile = QuizLoader.loadQuizzes();
        if (quizFile != null && !quizFile.getQuizzes().isEmpty()) {
            Quiz firstQuiz = quizFile.getQuizzes().getFirst();
            allQuestions = firstQuiz.getQuestions();
        }
    }

    public void requestNextQuestion() {
        currentQuestionIndex++;
        if(allQuestions != null && currentQuestionIndex < allQuestions.size()) {
            Question nextQuestion = allQuestions.get(currentQuestionIndex);
            String questionText = nextQuestion.getQuestion();
            String[] options = nextQuestion.getOptions().toArray(new String[0]);
            showQuestion(questionText, options);
        } else {
            System.out.println("Fim do quiz");
            cardLayout.show(mainPanel, SCOREBOARD_PANEL);
        }
    }


    public static void main(String[] args) {
        if(args.length != 5) {
            System.out.println("Arguments should be: IP PORT Room Team Username");
            return;
        }
        String ip = args[0];
        String port = args[1];
        String room = args[2];
        String team = args[3];
        String username = args[4];

        IsKahootClient client = new IsKahootClient(ip, port, room, team, username);
        client.start();
    }
}
