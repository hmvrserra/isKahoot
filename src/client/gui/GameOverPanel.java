package client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameOverPanel extends JPanel implements ActionListener {

    String winnerTeamId;
    private JLabel titleLabel;
    private JLabel winnerLabel;
    private Timer colorTimer;
    private Color[] celebrationColors = {
        Color.decode("#ff5733"), // Red
        Color.decode("#33ff57"), // Green
        Color.decode("#3357ff"), // Blue
        Color.decode("#f1c40f"), // Yellow
        Color.decode("#e91e63"), // Pink
        Color.decode("#9c27b0")  // Purple
    };
    private int colorIndex = 0;

    public GameOverPanel(String winnerTeamId) {
        this.winnerTeamId = winnerTeamId;
        setLayout(new GridBagLayout());
        setBackground(Color.decode("#46178f"));
        GridBagConstraints c = new GridBagConstraints();

        titleLabel = new JLabel("GAME OVER", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        c.gridy = 0;
        c.insets = new Insets(20, 0, 10, 0);
        add(titleLabel, c);

        winnerLabel = new JLabel("The winner is: " + winnerTeamId, SwingConstants.CENTER);
        winnerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        winnerLabel.setForeground(Color.YELLOW);
        c.gridy = 1;
        c.insets = new Insets(10, 0, 20, 0);
        add(winnerLabel, c);

        startCelebrationAnimation();
    }

    private void startCelebrationAnimation() {
        colorTimer = new Timer(500, this);
        colorTimer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        colorIndex = (colorIndex + 1) % celebrationColors.length;
        winnerLabel.setForeground(celebrationColors[colorIndex]);
    }

    public void stopAnimation() {
        if (colorTimer != null) {
            colorTimer.stop();
        }
    }
}
