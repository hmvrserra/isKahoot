package client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class QuestionPanel extends JPanel {

    private JLabel questionLabel;
    private JButton[] answerButtons;
    public QuestionPanel() {
        setLayout(new BorderLayout(10, 10));
        questionLabel = new JLabel("Texto da pergunta: ", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(questionLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        answerButtons = new JButton[4];

        for(int i = 0; i < answerButtons.length; i++) {
            answerButtons[i] = new JButton("Opção " + (i + 1));
            answerButtons[i].setFont(new Font("Arial", Font.PLAIN, 20));
            //answerButtons[i].addActionListener(this);
            buttonPanel.add(answerButtons[i]);
        }
        add(buttonPanel, BorderLayout.SOUTH);

    }

    public void setQuestion(String question, String[] answers) {
        questionLabel.setText(question);
        for(int i = 0; i < answerButtons.length; i++) {
            if(i < answers.length) {
                answerButtons[i].setText(answers[i]);
                answerButtons[i].setVisible(true);
            } else {
                answerButtons[i].setVisible(false);
            }
        }
        setButtonsEnabled(true);
    }

    public void setButtonsEnabled(boolean enabled) {
        for(JButton button : answerButtons) {
            button.setEnabled(enabled);
        }
    }

}
