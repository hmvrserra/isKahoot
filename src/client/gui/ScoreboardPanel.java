package client.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ScoreboardPanel extends JPanel {

    private JLabel positionLabel;
    private JLabel roundPointsLabel;
    private JLabel totalPointsLabel;
    private JTable leaderBoard;
    private DefaultTableModel leaderBoardModel;
    public ScoreboardPanel() {
        setLayout(new BorderLayout(10, 10));
        JPanel northPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Fim da ronda", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        northPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        positionLabel = new JLabel("Position", SwingConstants.CENTER);
        positionLabel.setFont(new Font("Arial", Font.PLAIN, 20));

        roundPointsLabel = new JLabel("Round Points", SwingConstants.CENTER);
        roundPointsLabel.setFont(new Font("Arial", Font.PLAIN, 20));

        totalPointsLabel = new JLabel("Total Points", SwingConstants.CENTER);
        totalPointsLabel.setFont(new Font("Arial", Font.PLAIN, 20));

        statsPanel.add(positionLabel);
        statsPanel.add(roundPointsLabel);
        statsPanel.add(totalPointsLabel);

        northPanel.add(statsPanel, BorderLayout.CENTER);
        add(northPanel, BorderLayout.NORTH);

        //------------- PAINEL CENTRAL ----------------- //
        String[] columnNames = {"Equipa", "Pontuação"};
        leaderBoardModel = new DefaultTableModel(columnNames, 0);

        leaderBoard = new JTable(leaderBoardModel);
        leaderBoard.setFont(new Font("Arial", Font.PLAIN, 16));
        leaderBoard.setRowHeight(25);
        leaderBoard.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));

        JScrollPane scrollPane = new JScrollPane(leaderBoard);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void updateScores(String position, String roundPoints, String totalPoints, Object[][] teamData) {
        positionLabel.setText("Posição: " + position);
        roundPointsLabel.setText("Ronda: " + roundPoints);
        totalPointsLabel.setText("Total: " + totalPoints);

        leaderBoardModel.setRowCount(0);

        for(Object[] row : teamData){
            leaderBoardModel.addRow(row);
        }
    }
}
