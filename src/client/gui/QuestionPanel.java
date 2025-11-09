package client.gui;
import javax.swing.*;
import java.awt.*;

public class QuestionPanel {

    private JFrame frame;
    private JPanel panel;
    private final String QUESTION_PANEL = "Quanto é 1 + 1 ?";
    private JPanel buttonPanel;

    private static final Color[] ANSWER_COLORS = {
            Color.decode("#ff5733"), // Vermelho
            Color.decode("#33ff57"), // Verde
            Color.decode("#3357ff"), // Azul
            Color.decode("#f1c40f")  // Amarelo
    };

    private void answerButtons() {
        JButton answer1 = new JButton("1");
        JButton answer2 = new JButton("2");
        JButton answer3 = new JButton("3");
        JButton answer4 = new JButton("4");

        JButton[] buttons = {answer1, answer2, answer3, answer4};
        for (int i = 0; i < 4; i++) {
            buttons[i].setPreferredSize(new Dimension(150, 80));
            buttons[i].setForeground(Color.WHITE);
            buttons[i].setBackground(ANSWER_COLORS[i]);
            buttons[i].setOpaque(true);
            buttons[i].setBorderPainted(false);
            buttons[i].setFont(new Font("Arial", Font.BOLD, 28));
            this.buttonPanel.add(buttons[i]);
        }
    }

    public QuestionPanel() {
        frame = new JFrame("Question Panel");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(new BorderLayout());                   
        panel.setBackground(Color.decode("#46178f"));

        JLabel questionLabel = new JLabel(QUESTION_PANEL, SwingConstants.CENTER);
        questionLabel.setForeground(Color.WHITE);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 32));

        this.buttonPanel = new JPanel();
        this.buttonPanel.setLayout(new GridLayout(1, 4, 30, 0));
        this.buttonPanel.setBackground(Color.decode("#46178f"));
        this.buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 40, 50));

        answerButtons();

        panel.add(questionLabel, BorderLayout.CENTER);         
        panel.add(this.buttonPanel, BorderLayout.PAGE_END);     

        frame.add(panel);
    }

    public static void main(String[] args) {
        new QuestionPanel().frame.setVisible(true);
    }
}