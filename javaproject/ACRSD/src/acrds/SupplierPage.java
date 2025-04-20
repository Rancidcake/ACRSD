package acrds;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class SupplierPage extends JFrame {
    private JTable supplierTable;
    private DefaultTableModel tableModel;
    private HomePage homePage;

    public SupplierPage(HomePage homePage) {
        this.homePage = homePage;
        setTitle("Suppliers");
        setSize(900, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
        loadSuppliers();
    }

    private void initUI() {
        JPanel main = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Supplier Management", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        JButton back = new JButton("Back");
        back.addActionListener(e -> {
            homePage.setVisible(true);
            dispose();
        });

        JPanel header = new JPanel(new BorderLayout());
        header.add(back, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);

        main.add(header, BorderLayout.NORTH);

        String[] columns = {
            "Supplier ID", "Supplier Name", "Location", "Reliability Score", 
            "Contract Duration (months)", "Source Type", "Inventory ID"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        supplierTable = new JTable(tableModel);
        main.add(new JScrollPane(supplierTable), BorderLayout.CENTER);

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

    private void loadSuppliers() {
        tableModel.setRowCount(0);
        try (Connection conn = DBManager.getConnection();
             ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Supplier")) {
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("Supplier_ID"),
                    rs.getString("Supplier_Name"),
                    rs.getString("Location"),
                    rs.getDouble("Reliability_Score"),
                    rs.getInt("Contract_Duration"),
                    rs.getString("Source_Type"),
                    rs.getInt("Inventory_ID")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAddDialog() {
        JTextField id = new JTextField(5);
        JTextField name = new JTextField(15);
        JTextField loc = new JTextField(15);
        JTextField score = new JTextField(5);
        JTextField duration = new JTextField(5);
        JTextField source = new JTextField(10);
        JTextField invId = new JTextField(5);

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Supplier ID:"));
        panel.add(id);
        panel.add(new JLabel("Supplier Name:"));
        panel.add(name);
        panel.add(new JLabel("Location:"));
        panel.add(loc);
        panel.add(new JLabel("Reliability Score:"));
        panel.add(score);
        panel.add(new JLabel("Contract Duration (months):"));
        panel.add(duration);
        panel.add(new JLabel("Source Type:"));
        panel.add(source);
        panel.add(new JLabel("Inventory ID:"));
        panel.add(invId);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Supplier", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = DBManager.getConnection();
                 PreparedStatement ps = conn.prepareStatement("INSERT INTO Supplier VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                ps.setInt(1, Integer.parseInt(id.getText()));
                ps.setString(2, name.getText());
                ps.setString(3, loc.getText());
                ps.setDouble(4, Double.parseDouble(score.getText()));
                ps.setInt(5, Integer.parseInt(duration.getText()));
                ps.setString(6, source.getText());
                ps.setInt(7, Integer.parseInt(invId.getText()));
                ps.executeUpdate();
                loadSuppliers();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void showUpdateDialog() {
        int selectedRow = supplierTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to update.");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);

        JTextField name = new JTextField((String) tableModel.getValueAt(selectedRow, 1));
        JTextField loc = new JTextField((String) tableModel.getValueAt(selectedRow, 2));
        JTextField score = new JTextField(tableModel.getValueAt(selectedRow, 3).toString());
        JTextField duration = new JTextField(tableModel.getValueAt(selectedRow, 4).toString());
        JTextField source = new JTextField((String) tableModel.getValueAt(selectedRow, 5));
        JTextField invId = new JTextField(tableModel.getValueAt(selectedRow, 6).toString());

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Supplier Name:"));
        panel.add(name);
        panel.add(new JLabel("Location:"));
        panel.add(loc);
        panel.add(new JLabel("Reliability Score:"));
        panel.add(score);
        panel.add(new JLabel("Contract Duration (months):"));
        panel.add(duration);
        panel.add(new JLabel("Source Type:"));
        panel.add(source);
        panel.add(new JLabel("Inventory ID:"));
        panel.add(invId);

        int result = JOptionPane.showConfirmDialog(this, panel, "Update Supplier", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = DBManager.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                     "UPDATE Supplier SET Supplier_Name=?, Location=?, Reliability_Score=?, Contract_Duration=?, Source_Type=?, Inventory_ID=? WHERE Supplier_ID=?")) {
                ps.setString(1, name.getText());
                ps.setString(2, loc.getText());
                ps.setDouble(3, Double.parseDouble(score.getText()));
                ps.setInt(4, Integer.parseInt(duration.getText()));
                ps.setString(5, source.getText());
                ps.setInt(6, Integer.parseInt(invId.getText()));
                ps.setInt(7, id);
                ps.executeUpdate();
                loadSuppliers();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void deleteSelected() {
        int selectedRow = supplierTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to delete.");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete Supplier ID " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBManager.getConnection();
                 PreparedStatement ps = conn.prepareStatement("DELETE FROM Supplier WHERE Supplier_ID=?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
                loadSuppliers();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
