package acrds;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class DefenseUnitsPage extends JFrame {
    private HomePage homePage;
    private JTable unitTable;
    private DefaultTableModel tableModel;

    public DefenseUnitsPage(HomePage homePage) {
        this.homePage = homePage;
        setTitle("Defense Units Management");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
        loadUnitsFromDatabase();
    }

    private void initUI() {
        JPanel main = new JPanel(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Defense Units Management", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        header.add(title, BorderLayout.CENTER);

        JButton back = new JButton("Back");
        back.addActionListener(e -> {
            homePage.setVisible(true);
            dispose();
        });
        header.add(back, BorderLayout.WEST);

        main.add(header, BorderLayout.NORTH);

        String[] cols = {"Unit ID", "Unit Name", "Commander In Charge", "Org ID"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        unitTable = new JTable(tableModel);
        main.add(new JScrollPane(unitTable), BorderLayout.CENTER);

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

    private void loadUnitsFromDatabase() {
        tableModel.setRowCount(0);
        try (Connection conn = DBManager.getConnection();
             ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM defence_units")) {
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("Unit_ID"),
                    rs.getString("Unit_Name"),
                    rs.getString("Commander_In_Charge"),
                    rs.getInt("Org_ID")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAddDialog() {
        JTextField idField = new JTextField(5);
        JTextField nameField = new JTextField(15);
        JTextField commanderField = new JTextField(15);
        JTextField orgIdField = new JTextField(5);

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Unit ID:")); panel.add(idField);
        panel.add(new JLabel("Unit Name:")); panel.add(nameField);
        panel.add(new JLabel("Commander In Charge:")); panel.add(commanderField);
        panel.add(new JLabel("Org ID:")); panel.add(orgIdField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Defense Unit", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = DBManager.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                         "INSERT INTO defence_units (Unit_ID, Unit_Name, Commander_In_Charge, Org_ID) VALUES (?, ?, ?, ?)")) {
                ps.setInt(1, Integer.parseInt(idField.getText()));
                ps.setString(2, nameField.getText());
                ps.setString(3, commanderField.getText());
                ps.setInt(4, Integer.parseInt(orgIdField.getText()));
                ps.executeUpdate();
                loadUnitsFromDatabase();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void showUpdateDialog() {
        int selectedRow = unitTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to update.");
            return;
        }

        int unitId = (int) tableModel.getValueAt(selectedRow, 0);
        String unitName = (String) tableModel.getValueAt(selectedRow, 1);
        String commander = (String) tableModel.getValueAt(selectedRow, 2);
        int orgId = (int) tableModel.getValueAt(selectedRow, 3);

        JTextField nameField = new JTextField(unitName, 15);
        JTextField commanderField = new JTextField(commander, 15);
        JTextField orgIdField = new JTextField(String.valueOf(orgId), 5);

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Unit Name:")); panel.add(nameField);
        panel.add(new JLabel("Commander In Charge:")); panel.add(commanderField);
        panel.add(new JLabel("Org ID:")); panel.add(orgIdField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Update Defense Unit", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = DBManager.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                         "UPDATE defence_units SET Unit_Name=?, Commander_In_Charge=?, Org_ID=? WHERE Unit_ID=?")) {
                ps.setString(1, nameField.getText());
                ps.setString(2, commanderField.getText());
                ps.setInt(3, Integer.parseInt(orgIdField.getText()));
                ps.setInt(4, unitId);
                ps.executeUpdate();
                loadUnitsFromDatabase();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void deleteSelected() {
        int selectedRow = unitTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to delete.");
            return;
        }

        int unitId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete Unit ID " + unitId + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBManager.getConnection();
                 PreparedStatement ps = conn.prepareStatement("DELETE FROM defence_units WHERE Unit_ID = ?")) {
                ps.setInt(1, unitId);
                ps.executeUpdate();
                loadUnitsFromDatabase();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
