package client.gui;

import javax.swing.*;
import java.awt.*;

public class IsKahootClient {

    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public static final String WAITING_PANEL = "WAITING";

    public IsKahootClient(String ip, String port, String room, String team, String username) {
        frame = new JFrame("IsKahoot");

        frame.setSize(800, 600); //Can be adjusted
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        WaitingPanel waitingPanel = new WaitingPanel();
        mainPanel.add(waitingPanel, WAITING_PANEL);

        frame.add(mainPanel);
        cardLayout.show(mainPanel, WAITING_PANEL);
    }

    public void start() {
        frame.setVisible(true);
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
