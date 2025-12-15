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
        setBackground(Color.decode("#46178f"));

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(Color.decode("#46178f"));
        JLabel titleLabel = new JLabel("Fim da ronda", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        northPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        statsPanel.setBackground(Color.decode("#46178f"));
        positionLabel = new JLabel("Position", SwingConstants.CENTER);
        positionLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        positionLabel.setForeground(Color.WHITE);

        roundPointsLabel = new JLabel("Round Points", SwingConstants.CENTER);
        roundPointsLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        roundPointsLabel.setForeground(Color.WHITE);

        totalPointsLabel = new JLabel("Total Points", SwingConstants.CENTER);
        totalPointsLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        totalPointsLabel.setForeground(Color.WHITE);

        statsPanel.add(positionLabel);
        statsPanel.add(roundPointsLabel);
        statsPanel.add(totalPointsLabel);

        northPanel.add(statsPanel, BorderLayout.CENTER);
        add(northPanel, BorderLayout.NORTH);

        // PAINEL CENTRAL
        String[] columnNames = {"Equipa", "Pontuação"};
        leaderBoardModel = new DefaultTableModel(columnNames, 0);

        leaderBoard = new JTable(leaderBoardModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        leaderBoard.setFont(new Font("Arial", Font.PLAIN, 18));
        leaderBoard.setRowHeight(40);
        leaderBoard.setForeground(Color.WHITE);
        leaderBoard.setBackground(Color.decode("#46178f"));
        leaderBoard.setGridColor(Color.decode("#5a2a8a"));
        leaderBoard.setSelectionBackground(Color.decode("#5a2a8a"));

        leaderBoard.getTableHeader().setFont(new Font("Arial", Font.BOLD, 20));
        leaderBoard.getTableHeader().setForeground(Color.WHITE);
        leaderBoard.getTableHeader().setBackground(Color.decode("#5a2a8a"));

        JScrollPane scrollPane = new JScrollPane(leaderBoard);
        scrollPane.setBackground(Color.decode("#46178f"));
        scrollPane.getViewport().setBackground(Color.decode("#46178f"));
        add(scrollPane, BorderLayout.CENTER);
    }

    public void updateScores(String position, String roundPoints, String totalPoints, String[][] leaderboard) {
        positionLabel.setText("Posição: " + position);
        roundPointsLabel.setText("Ronda: " + roundPoints);
        totalPointsLabel.setText("Total: " + totalPoints);

        leaderBoardModel.setRowCount(0);

        for(String[] row : leaderboard){
            leaderBoardModel.addRow(row);
        }
    }
}
