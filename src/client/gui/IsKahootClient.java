package client.gui;

import client.net.ServerConnection;
import common.*;
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
    public static final String GAMEOVER_PANEL = "GAMEOVER";

    private QuestionPanel questionPanel;
    private ScoreboardPanel scoreboardPanel;

    private ServerConnection connection;

    //ATRIBUTOS TESTE PRIMEIRA DEMONSTRACAO (APAGAR DEPOIS)

    // private List<Question> allQuestions;
    // private int currentQuestionIndex = -1;

    public IsKahootClient(String ip, String port, String room, String team, String username) {
        
        frame = new JFrame("IsKahoot - " + username);
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
            connection = new ServerConnection(ip, Integer.parseInt(port), this);
            boolean successfulLogin = connection.login(username, team, room);
            if(successfulLogin){
                cardLayout.show(mainPanel, WAITING_PANEL);
            }else {
                showError("Login failed or room does not exist");
            }
        } catch (IOException | ClassNotFoundException e) {
            showError("Can't connect to server");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void start() {
        frame.setVisible(true);
    }

    public void showQuestion(QuestionDTO question) {
        questionPanel.setQuestion(question.getQuestion(), question.getOptions());
        cardLayout.show(mainPanel, QUESTION_PANEL);
    }

    private void showError(String message){
        JOptionPane.showMessageDialog(frame, message, "Connection Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showScoreboard(ScoreboardDTO scoreboard) {
        scoreboardPanel.updateScores(scoreboard.getPosition(), scoreboard.getRoundPoints(), scoreboard.getTotalPoints(), scoreboard.getLeaderboard());
        cardLayout.show(mainPanel, SCOREBOARD_PANEL);
    }

    public void submitAnswer(int answerIndex) {
        if(connection != null){
            connection.sendAnswer(answerIndex);
        }
    }

    public void finishGame(GameOverDTO gameOver){
        GameOverPanel gameOverPanel = new GameOverPanel(gameOver.getWinnerTeamId());
        mainPanel.add(gameOverPanel, GAMEOVER_PANEL);
        cardLayout.show(mainPanel, GAMEOVER_PANEL);
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
