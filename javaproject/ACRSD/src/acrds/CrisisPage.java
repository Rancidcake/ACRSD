package acrds;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class CrisisPage extends JFrame {
    private HomePage homePage;
    private JTable crisisTable;
    private DefaultTableModel tableModel;

    public CrisisPage(HomePage homePage) {
        this.homePage = homePage;
        setTitle("Crisis Management");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
        loadCrisisFromDatabase();
    }

    private void initUI() {
        JPanel main = new JPanel(new BorderLayout());
        JPanel header = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Crisis Management", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        header.add(title, BorderLayout.CENTER);
        JButton back = new JButton("Back");
        back.addActionListener(e -> {
            homePage.setVisible(true);
            dispose();
        });
        header.add(back, BorderLayout.WEST);
        main.add(header, BorderLayout.NORTH);

        String[] cols = {"Crisis ID", "Crisis Type", "Severity Level", "Status", "Response ID"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        crisisTable = new JTable(tableModel);
        main.add(new JScrollPane(crisisTable), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton add = new JButton("Add");
        add.addActionListener(e -> showAddDialog());
        JButton upd = new JButton("Update");
        upd.addActionListener(e -> showUpdateDialog());
        JButton del = new JButton("Delete");
        del.addActionListener(e -> deleteSelected());
        btnPanel.add(add);
        btnPanel.add(upd);
        btnPanel.add(del);
        main.add(btnPanel, BorderLayout.SOUTH);

        add(main);
    }

    private void loadCrisisFromDatabase() {
        tableModel.setRowCount(0);
        try (Connection conn = DBManager.getConnection();
             ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Crisis_Management")) {
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("Crisis_ID"),
                    rs.getString("Crisis_Type"),
                    rs.getString("Severity_Level"),
                    rs.getString("Status"),
                    rs.getInt("Response_ID")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAddDialog() {
        javax.swing.JTextField crisisIdField = new javax.swing.JTextField(5);
        javax.swing.JTextField crisisTypeField = new javax.swing.JTextField(15);
        javax.swing.JTextField severityField = new javax.swing.JTextField(15);
        javax.swing.JTextField statusField = new javax.swing.JTextField(15);
        javax.swing.JTextField responseIdField = new javax.swing.JTextField(5);

        JPanel panel = new JPanel(new java.awt.GridLayout(0, 2));
        panel.add(new JLabel("Crisis ID:"));
        panel.add(crisisIdField);
        panel.add(new JLabel("Crisis Type:"));
        panel.add(crisisTypeField);
        panel.add(new JLabel("Severity Level:"));
        panel.add(severityField);
        panel.add(new JLabel("Status:"));
        panel.add(statusField);
        panel.add(new JLabel("Response ID:"));
        panel.add(responseIdField);

        int result = javax.swing.JOptionPane.showConfirmDialog(this, panel, "Add Crisis", javax.swing.JOptionPane.OK_CANCEL_OPTION);
        if (result == javax.swing.JOptionPane.OK_OPTION) {
            try (Connection conn = DBManager.getConnection();
                 java.sql.PreparedStatement ps = conn.prepareStatement("INSERT INTO Crisis_Management (Crisis_ID, Crisis_Type, Severity_Level, Status, Response_ID) VALUES (?, ?, ?, ?, ?)")) {
                ps.setInt(1, Integer.parseInt(crisisIdField.getText()));
                ps.setString(2, crisisTypeField.getText());
                ps.setString(3, severityField.getText());
                ps.setString(4, statusField.getText());
                ps.setInt(5, Integer.parseInt(responseIdField.getText()));
                ps.executeUpdate();
                loadCrisisFromDatabase();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void showUpdateDialog() {
        int selectedRow = crisisTable.getSelectedRow();
        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Select a row to update.");
            return;
        }

        int crisisId = (int) tableModel.getValueAt(selectedRow, 0);
        String currentType = (String) tableModel.getValueAt(selectedRow, 1);
        String currentSeverity = (String) tableModel.getValueAt(selectedRow, 2);
        String currentStatus = (String) tableModel.getValueAt(selectedRow, 3);
        int currentResponseId = (int) tableModel.getValueAt(selectedRow, 4);

        javax.swing.JTextField typeField = new javax.swing.JTextField(currentType, 15);
        javax.swing.JTextField severityField = new javax.swing.JTextField(currentSeverity, 15);
        javax.swing.JTextField statusField = new javax.swing.JTextField(currentStatus, 15);
        javax.swing.JTextField responseIdField = new javax.swing.JTextField(String.valueOf(currentResponseId), 5);

        JPanel panel = new JPanel(new java.awt.GridLayout(0, 2));
        panel.add(new JLabel("Crisis Type:"));
        panel.add(typeField);
        panel.add(new JLabel("Severity Level:"));
        panel.add(severityField);
        panel.add(new JLabel("Status:"));
        panel.add(statusField);
        panel.add(new JLabel("Response ID:"));
        panel.add(responseIdField);

        int result = javax.swing.JOptionPane.showConfirmDialog(this, panel, "Update Crisis", javax.swing.JOptionPane.OK_CANCEL_OPTION);
        if (result == javax.swing.JOptionPane.OK_OPTION) {
            try (Connection conn = DBManager.getConnection();
                 java.sql.PreparedStatement ps = conn.prepareStatement("UPDATE Crisis_Management SET Crisis_Type = ?, Severity_Level = ?, Status = ?, Response_ID = ? WHERE Crisis_ID = ?")) {
                ps.setString(1, typeField.getText());
                ps.setString(2, severityField.getText());
                ps.setString(3, statusField.getText());
                ps.setInt(4, Integer.parseInt(responseIdField.getText()));
                ps.setInt(5, crisisId);
                ps.executeUpdate();
                loadCrisisFromDatabase();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void deleteSelected() {
        int selectedRow = crisisTable.getSelectedRow();
        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Select a row to delete.");
            return;
        }

        int crisisId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = javax.swing.JOptionPane.showConfirmDialog(this, "Are you sure you want to delete Crisis ID " + crisisId + "?", "Confirm Delete", javax.swing.JOptionPane.YES_NO_OPTION);
        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            try (Connection conn = DBManager.getConnection();
                 java.sql.PreparedStatement ps = conn.prepareStatement("DELETE FROM Crisis_Management WHERE Crisis_ID = ?")) {
                ps.setInt(1, crisisId);
                ps.executeUpdate();
                loadCrisisFromDatabase();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
