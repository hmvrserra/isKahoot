package client.gui;

import client.net.ServerConnection;
import common.LoginRequest;
import common.LoginResponse;
import server.game.Question;
import server.game.Quiz;
import server.game.QuizFile;
import server.game.QuizLoader;
import server.net.Server;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

import static java.lang.Integer.parseInt;

public class IsKahootClient {

    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public static final String WAITING_PANEL = "WAITING";
    public static final String QUESTION_PANEL = "QUESTION";
    public static final String SCOREBOARD_PANEL = "SCOREBOARD";

    private QuestionPanel questionPanel;
    private ScoreboardPanel scoreboardPanel;

    private ServerConnection connection;

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

        try{
            this.connection = new ServerConnection(ip, Integer.parseInt(port));
            LoginRequest loginRequest = new LoginRequest(username, team, room);
            connection.sendLogin(loginRequest);
            System.out.println("Sent login request");
            try{
                LoginResponse loginResponse = connection.getLoginResponse();
                if(loginResponse.isSuccess()){
                    System.out.println("Login successful: " +  loginResponse.getMessage());
                    cardLayout.show(mainPanel, WAITING_PANEL);
                } else  {
                    System.out.println("Login failed: " +  loginResponse.getMessage());
                    System.exit(1);
                }
            } catch (ClassNotFoundException e) {
                System.out.println("Error reading response from server");
                e.printStackTrace();
            }
        } catch (IOException e) {
            showError("Can't connect to server");
            e.printStackTrace();
            System.exit(1);
        }

        // PRIMEIRA DEMONSTRACAO (UTILIZAR DEPOIS TALVEZ)
//        loadQuiz();
//
//        if(allQuestions != null && !allQuestions.isEmpty()) {
//            requestNextQuestion();
//        } else {
//            System.err.println("No questions found!");
//            cardLayout.show(mainPanel, WAITING_PANEL);
//        }

    }

    public void start() {
        frame.setVisible(true);
    }

    public void showQuestion(String questionText, String[] options) {
        questionPanel.setQuestion(questionText, options);
        cardLayout.show(mainPanel, QUESTION_PANEL);
    }

    private void showError(String message){
        JOptionPane.showMessageDialog(frame, message, "Connection Error", JOptionPane.ERROR_MESSAGE);
    }

//    public void showScoreboard(String pos, String change, String total, Object[][] data) {
//        ScoreboardPanel sp = new ScoreboardPanel();
//        sp.updateScores(pos, change, total, data);
//        mainPanel.add(sp, SCOREBOARD_PANEL);
//        cardLayout.show(mainPanel, SCOREBOARD_PANEL);
//    }

    //TESTES PARA PRIMEIRA DEMONSTRACAO (UTILIZAR DEPOIS TALVEZ)

//    private void loadQuiz(){
//        QuizFile quizFile = QuizLoader.loadQuizzes();
//        if (quizFile != null && !quizFile.getQuizzes().isEmpty()) {
//            Quiz firstQuiz = quizFile.getQuizzes().getFirst();
//            allQuestions = firstQuiz.getQuestions();
//        }
//    }
//
//    public void requestNextQuestion() {
//        currentQuestionIndex++;
//        if(allQuestions != null && currentQuestionIndex < allQuestions.size()) {
//            Question nextQuestion = allQuestions.get(currentQuestionIndex);
//            String questionText = nextQuestion.getQuestion();
//            String[] options = nextQuestion.getOptions().toArray(new String[0]);
//            showQuestion(questionText, options);
//        } else {
//            System.out.println("Fim do quiz");
//            cardLayout.show(mainPanel, SCOREBOARD_PANEL);
//        }
//    }


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
