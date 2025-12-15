package client.gui;

import javax.swing.*;
import java.awt.*;

public class GameOverPanel extends JPanel {

    String winnerTeamId;

    public GameOverPanel(String winnerTeamId) {
        this.winnerTeamId = winnerTeamId;
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel waitingLabel = new JLabel("The winner is: " + winnerTeamId);
        waitingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        waitingLabel.setFont(new Font("Arial", Font.PLAIN, 24));

        add(waitingLabel, c);
    }


}
