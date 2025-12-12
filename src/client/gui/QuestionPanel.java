package client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class QuestionPanel extends JPanel implements ActionListener {

    private JLabel questionLabel;
    private JButton[] answerButtons;

    private static final Color[] ANSWER_COLORS = {
            Color.decode("#ff5733"), // Vermelho
            Color.decode("#33ff57"), // Verde
            Color.decode("#3357ff"), // Azul
            Color.decode("#f1c40f")  // Amarelo
    };

    IsKahootClient mainApp;

    public QuestionPanel(IsKahootClient mainApp) {
        this.mainApp = mainApp;

        setLayout(new BorderLayout());
        setBackground(Color.decode("#46178f"));

        // Pergunta
        questionLabel = new JLabel("Loading question...", SwingConstants.CENTER);
        questionLabel.setForeground(Color.WHITE);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 32));
        add(questionLabel, BorderLayout.CENTER);

        // Botões
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 30, 0));
        buttonPanel.setBackground(Color.decode("#46178f"));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 40, 50));

        answerButtons = new JButton[4];

        for (int i = 0; i < 4; i++) {
            answerButtons[i] = new JButton();
            answerButtons[i].setFont(new Font("Arial", Font.PLAIN, 20));
            answerButtons[i].setForeground(Color.WHITE);
            answerButtons[i].setBackground(ANSWER_COLORS[i]);
            answerButtons[i].setOpaque(true);
            answerButtons[i].setBorderPainted(false);
            answerButtons[i].addActionListener(this);
            buttonPanel.add(answerButtons[i]);
        }

        add(buttonPanel, BorderLayout.PAGE_END);
    }

    public void setQuestion(String questionText, String[] options) {
        questionLabel.setText(questionText);
        for(int i = 0; i < answerButtons.length; i++) {
            if(i < options.length) {
                answerButtons[i].setText(options[i]);
                answerButtons[i].setVisible(true);
                answerButtons[i].setEnabled(true);
            } else {
                answerButtons[i].setVisible(false);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        for(int i = 0; i < answerButtons.length; i++) {
            if(source == answerButtons[i]) {
                disableButtons();
                mainApp.submitAnswer(i);
                return;
            }
        }
    }

    private void disableButtons() {
        for(JButton button : answerButtons) {
            button.setEnabled(false);
        }
    }
}