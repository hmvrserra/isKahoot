package client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class QuestionPanel extends JPanel implements ActionListener {

    private JLabel questionLabel;
    private JButton[] answerButtons;
    private JLabel countdownLabel;
    private Timer countdownTimer;
    private int remainingSeconds;

    private static final int ROUND_TIME_SECONDS = 30;

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

        // Countdown
        countdownLabel = new JLabel("30", SwingConstants.CENTER);
        countdownLabel.setForeground(Color.WHITE);
        countdownLabel.setFont(new Font("Arial", Font.BOLD, 48));
        add(countdownLabel, BorderLayout.NORTH);

        // Pergunta
        questionLabel = new JLabel("Loading question...", SwingConstants.CENTER);
        questionLabel.setForeground(Color.WHITE);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 32));
        add(questionLabel, BorderLayout.CENTER);

        // Botões
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 30, 30));
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
        questionLabel.setText("<html><center>" + questionText + "</center></html>");
        for(int i = 0; i < answerButtons.length; i++) {
            if(i < options.length) {
                answerButtons[i].setText("<html><center>" + options[i] + "</center></html>");
                answerButtons[i].setVisible(true);
                answerButtons[i].setEnabled(true);
            } else {
                answerButtons[i].setVisible(false);
            }
        }
        startCountdown();
    }

    private void startCountdown() {
        if (countdownTimer != null) {
            countdownTimer.stop();
        }
        remainingSeconds = ROUND_TIME_SECONDS;
        countdownLabel.setText(String.valueOf(remainingSeconds));
        countdownTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remainingSeconds--;
                countdownLabel.setText(String.valueOf(remainingSeconds));
                if (remainingSeconds <= 0) {
                    countdownTimer.stop();
                    disableButtons();
                }
            }
        });
        countdownTimer.start();
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
