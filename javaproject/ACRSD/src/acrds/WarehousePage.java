package acrds;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class WarehousePage extends JFrame {
    private JTable warehouseTable;
    private DefaultTableModel tableModel;
    private HomePage homePage;

    public WarehousePage(HomePage homePage) {
        this.homePage = homePage;
        setTitle("Warehouse Management");
        setSize(900, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
        loadWarehouses();
    }

    private void initUI() {
        JPanel main = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Warehouse Management", JLabel.CENTER);
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

        String[] columns = {"Warehouse ID", "Location", "Capacity", "Manager Name", "Org ID", "Resource ID"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        warehouseTable = new JTable(tableModel);
        main.add(new JScrollPane(warehouseTable), BorderLayout.CENTER);

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

    private void loadWarehouses() {
        tableModel.setRowCount(0);
        String query = "SELECT * FROM Warehouse";
        try (Connection conn = DBManager.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("Warehouse_ID"),
                    rs.getString("Location"),
                    rs.getInt("Capacity"),
                    rs.getString("Manager_Name"),
                    rs.getInt("Org_ID"),
                    rs.getInt("Resource_ID")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void showAddDialog() {
        JTextField idField = new JTextField(5);
        JTextField locField = new JTextField(15);
        JTextField capField = new JTextField(5);
        JTextField mgrField = new JTextField(15);
        JTextField orgField = new JTextField(5);
        JTextField resField = new JTextField(5);

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Warehouse ID:")); panel.add(idField);
        panel.add(new JLabel("Location:")); panel.add(locField);
        panel.add(new JLabel("Capacity:")); panel.add(capField);
        panel.add(new JLabel("Manager Name:")); panel.add(mgrField);
        panel.add(new JLabel("Org ID:")); panel.add(orgField);
        panel.add(new JLabel("Resource ID:")); panel.add(resField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Warehouse", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String sql = "INSERT INTO Warehouse (Warehouse_ID, Location, Capacity, Manager_Name, Org_ID, Resource_ID) VALUES (?, ?, ?, ?, ?, ?)";
            try (Connection conn = DBManager.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, Integer.parseInt(idField.getText()));
                ps.setString(2, locField.getText());
                ps.setInt(3, Integer.parseInt(capField.getText()));
                ps.setString(4, mgrField.getText());
                ps.setInt(5, Integer.parseInt(orgField.getText()));
                ps.setInt(6, Integer.parseInt(resField.getText()));
                ps.executeUpdate();
                loadWarehouses();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void showUpdateDialog() {
        int row = warehouseTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a row to update.");
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);
        JTextField locField = new JTextField((String) tableModel.getValueAt(row, 1));
        JTextField capField = new JTextField(tableModel.getValueAt(row, 2).toString());
        JTextField mgrField = new JTextField((String) tableModel.getValueAt(row, 3));
        JTextField orgField = new JTextField(tableModel.getValueAt(row, 4).toString());
        JTextField resField = new JTextField(tableModel.getValueAt(row, 5).toString());

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Location:")); panel.add(locField);
        panel.add(new JLabel("Capacity:")); panel.add(capField);
        panel.add(new JLabel("Manager Name:")); panel.add(mgrField);
        panel.add(new JLabel("Org ID:")); panel.add(orgField);
        panel.add(new JLabel("Resource ID:")); panel.add(resField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Update Warehouse", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String sql = "UPDATE Warehouse SET Location=?, Capacity=?, Manager_Name=?, Org_ID=?, Resource_ID=? WHERE Warehouse_ID=?";
            try (Connection conn = DBManager.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, locField.getText());
                ps.setInt(2, Integer.parseInt(capField.getText()));
                ps.setString(3, mgrField.getText());
                ps.setInt(4, Integer.parseInt(orgField.getText()));
                ps.setInt(5, Integer.parseInt(resField.getText()));
                ps.setInt(6, id);
                ps.executeUpdate();
                loadWarehouses();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteSelected() {
        int row = warehouseTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a row to delete.");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete Warehouse ID " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBManager.getConnection();
                 PreparedStatement ps = conn.prepareStatement("DELETE FROM Warehouse WHERE Warehouse_ID=?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
                loadWarehouses();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}