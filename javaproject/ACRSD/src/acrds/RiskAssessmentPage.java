package acrds;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class RiskAssessmentPage extends JFrame {
    private HomePage homePage;
    private JTable table;
    private DefaultTableModel model;

    public RiskAssessmentPage(HomePage homePage) {
        this.homePage = homePage;
        setTitle("Risk Assessment Management");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
        loadData();
    }

    private void initUI() {
        JPanel main = new JPanel(new BorderLayout());

        // Top header
        JPanel header = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Risk Assessment Management", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        JButton back = new JButton("Back");
        back.addActionListener(e -> {
            homePage.setVisible(true);
            dispose();
        });
        header.add(title, BorderLayout.CENTER);
        header.add(back, BorderLayout.WEST);
        main.add(header, BorderLayout.NORTH);

        // Table
        String[] cols = {"Assessment ID", "Risk Level", "Operation ID"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(model);
        main.add(new JScrollPane(table), BorderLayout.CENTER);

        // Buttons
        JPanel buttons = new JPanel(new FlowLayout());
        JButton add = new JButton("Add");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");
        add.addActionListener(e -> showAddDialog());
        update.addActionListener(e -> showUpdateDialog());
        delete.addActionListener(e -> deleteSelected());
        buttons.add(add);
        buttons.add(update);
        buttons.add(delete);
        main.add(buttons, BorderLayout.SOUTH);

        add(main);
    }

    private void loadData() {
        model.setRowCount(0);
        try (Connection conn = DBManager.getConnection();
             ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Risk_Assessment")) {
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("Assessment_ID"),
                    rs.getString("Risk_Level"),
                    rs.getInt("Operation_ID")
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAddDialog() {
        JTextField id = new JTextField(5);
        JTextField riskLevel = new JTextField(15);
        JTextField operationId = new JTextField(5);

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Assessment ID:")); panel.add(id);
        panel.add(new JLabel("Risk Level:")); panel.add(riskLevel);
        panel.add(new JLabel("Operation ID:")); panel.add(operationId);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Risk Assessment", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = DBManager.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                         "INSERT INTO Risk_Assessment (Assessment_ID, Risk_Level, Operation_ID) VALUES (?, ?, ?)")) {
                ps.setInt(1, Integer.parseInt(id.getText()));
                ps.setString(2, riskLevel.getText());
                ps.setInt(3, Integer.parseInt(operationId.getText()));
                ps.executeUpdate();
                loadData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void showUpdateDialog() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to update.");
            return;
        }

        int assessmentId = (int) model.getValueAt(row, 0);
        String currentRiskLevel = (String) model.getValueAt(row, 1);
        int currentOperationId = (int) model.getValueAt(row, 2);

        JTextField riskLevel = new JTextField(currentRiskLevel, 15);
        JTextField operationId = new JTextField(String.valueOf(currentOperationId), 5);

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Risk Level:")); panel.add(riskLevel);
        panel.add(new JLabel("Operation ID:")); panel.add(operationId);

        int result = JOptionPane.showConfirmDialog(this, panel, "Update Risk Assessment", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = DBManager.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                         "UPDATE Risk_Assessment SET Risk_Level=?, Operation_ID=? WHERE Assessment_ID=?")) {
                ps.setString(1, riskLevel.getText());
                ps.setInt(2, Integer.parseInt(operationId.getText()));
                ps.setInt(3, assessmentId);
                ps.executeUpdate();
                loadData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to delete.");
            return;
        }

        int assessmentId = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete Risk Assessment ID " + assessmentId + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBManager.getConnection();
                 PreparedStatement ps = conn.prepareStatement("DELETE FROM Risk_Assessment WHERE Assessment_ID = ?")) {
                ps.setInt(1, assessmentId);
                ps.executeUpdate();
                loadData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
