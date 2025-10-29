package client.gui;

import javax.swing.*;
import java.awt.*;

public class WaitingPanel extends JPanel {

    public WaitingPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel waitingLabel = new JLabel("Waiting for game start...");
        waitingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        waitingLabel.setFont(new Font("Arial", Font.PLAIN, 24));

        add(waitingLabel, c);
    }
}
