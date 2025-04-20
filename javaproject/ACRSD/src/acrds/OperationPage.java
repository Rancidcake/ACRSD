package acrds;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class OperationPage extends JFrame {
    private JTable operationTable;
    private DefaultTableModel tableModel;
    private HomePage homePage;

    public OperationPage(HomePage homePage) {
        this.homePage = homePage;
        setTitle("Operations");
        setSize(800, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
        loadOperations();
    }

    private void initUI() {
        JPanel main = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Operation Management", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));

        JButton back = new JButton("Back");
        back.addActionListener(e -> { homePage.setVisible(true); dispose(); });

        JPanel header = new JPanel(new BorderLayout());
        header.add(back, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);
        main.add(header, BorderLayout.NORTH);

        String[] cols = {"Operation ID", "Start Date", "End Date", "Outcome", "Contact Details", "Emergency ID"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        operationTable = new JTable(tableModel);
        main.add(new JScrollPane(operationTable), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton add = new JButton("Add"); add.addActionListener(e -> showAddDialog());
        JButton upd = new JButton("Update"); upd.addActionListener(e -> showUpdateDialog());
        JButton del = new JButton("Delete"); del.addActionListener(e -> deleteSelected());
        btnPanel.add(add); btnPanel.add(upd); btnPanel.add(del);
        main.add(btnPanel, BorderLayout.SOUTH);

        add(main);
    }

    private void loadOperations() {
        tableModel.setRowCount(0);
        String sql = "SELECT * FROM operations";
        try (Connection conn = DBManager.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("Operation_ID"),
                    rs.getDate("Start_Date"),
                    rs.getDate("End_Date"),
                    rs.getString("Outcome"),
                    rs.getString("Contact_Details"),
                    rs.getInt("Emergency_ID")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAddDialog() {
        JTextField idField = new JTextField(5);
        JTextField startField = new JTextField(10);
        JTextField endField = new JTextField(10);
        JTextField outcomeField = new JTextField(15);
        JTextField contactField = new JTextField(20);
        JTextField emergencyField = new JTextField(5);

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Operation ID:")); panel.add(idField);
        panel.add(new JLabel("Start Date (YYYY-MM-DD):")); panel.add(startField);
        panel.add(new JLabel("End Date (YYYY-MM-DD):")); panel.add(endField);
        panel.add(new JLabel("Outcome:")); panel.add(outcomeField);
        panel.add(new JLabel("Contact Details:")); panel.add(contactField);
        panel.add(new JLabel("Emergency ID:")); panel.add(emergencyField);

        int res = JOptionPane.showConfirmDialog(this, panel, "Add Operation", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            String sql = "INSERT INTO operations (Operation_ID, Start_Date, End_Date, Outcome, Contact_Details, Emergency_ID) VALUES (?, ?, ?, ?, ?, ?)";
            try (Connection conn = DBManager.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, Integer.parseInt(idField.getText()));
                ps.setDate(2, Date.valueOf(startField.getText()));
                ps.setDate(3, Date.valueOf(endField.getText()));
                ps.setString(4, outcomeField.getText());
                ps.setString(5, contactField.getText());
                ps.setInt(6, Integer.parseInt(emergencyField.getText()));
                ps.executeUpdate();
                loadOperations();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void showUpdateDialog() {
        int row = operationTable.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a row to update."); return; }

        int id = (int) tableModel.getValueAt(row, 0);
        JTextField startField = new JTextField(tableModel.getValueAt(row, 1).toString());
        JTextField endField = new JTextField(tableModel.getValueAt(row, 2).toString());
        JTextField outcomeField = new JTextField((String) tableModel.getValueAt(row, 3));
        JTextField contactField = new JTextField((String) tableModel.getValueAt(row, 4));
        JTextField emergencyField = new JTextField(tableModel.getValueAt(row, 5).toString());

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Start Date (YYYY-MM-DD):")); panel.add(startField);
        panel.add(new JLabel("End Date (YYYY-MM-DD):")); panel.add(endField);
        panel.add(new JLabel("Outcome:")); panel.add(outcomeField);
        panel.add(new JLabel("Contact Details:")); panel.add(contactField);
        panel.add(new JLabel("Emergency ID:")); panel.add(emergencyField);

        int res = JOptionPane.showConfirmDialog(this, panel, "Update Operation", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            String sql = "UPDATE operations SET Start_Date=?, End_Date=?, Outcome=?, Contact_Details=?, Emergency_ID=? WHERE Operation_ID=?";
            try (Connection conn = DBManager.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setDate(1, Date.valueOf(startField.getText()));
                ps.setDate(2, Date.valueOf(endField.getText()));
                ps.setString(3, outcomeField.getText());
                ps.setString(4, contactField.getText());
                ps.setInt(5, Integer.parseInt(emergencyField.getText()));
                ps.setInt(6, id);
                ps.executeUpdate();
                loadOperations();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void deleteSelected() {
        int row = operationTable.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a row to delete."); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete Operation ID " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM operations WHERE Operation_ID=?";
            try (Connection conn = DBManager.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.executeUpdate();
                loadOperations();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}