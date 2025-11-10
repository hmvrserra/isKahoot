package client.gui;

import server.game.Question;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class QuestionPanel extends JPanel {  // 1. extends JPanel

    private static final Color[] ANSWER_COLORS = {
            Color.decode("#ff5733"), // Vermelho
            Color.decode("#33ff57"), // Verde
            Color.decode("#3357ff"), // Azul
            Color.decode("#f1c40f")  // Amarelo
    };

    public QuestionPanel(Question question) {
        setLayout(new BorderLayout());
        setBackground(Color.decode("#46178f"));

        // Pergunta
        JLabel questionLabel = new JLabel(question.getQuestion(), SwingConstants.CENTER);
        questionLabel.setForeground(Color.WHITE);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 32));
        add(questionLabel, BorderLayout.CENTER);

        // Botões
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 30, 0));
        buttonPanel.setBackground(Color.decode("#46178f"));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 40, 50));

        List<String> options = question.getOptions();
        for (int i = 0; i < 4; i++) {
            JButton btn = new JButton(options.get(i));
            btn.setForeground(Color.WHITE);
            btn.setBackground(ANSWER_COLORS[i]);
            btn.setOpaque(true);
            btn.setBorderPainted(false);
            buttonPanel.add(btn);
        }

        add(buttonPanel, BorderLayout.PAGE_END);
    }
}