package acrds;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class InventoryPage extends JFrame {
    private HomePage homePage;
    private JTable inventoryTable;
    private DefaultTableModel tableModel;

    public InventoryPage(HomePage homePage) {
        this.homePage = homePage;
        setTitle("Inventory Management");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
        loadInventoryFromDatabase();
    }

    private void initUI() {
        JPanel main = new JPanel(new BorderLayout());
        JPanel header = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Inventory Management", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        header.add(title, BorderLayout.CENTER);

        JButton back = new JButton("Back");
        back.addActionListener(e -> {
            homePage.setVisible(true);
            dispose();
        });
        header.add(back, BorderLayout.WEST);
        main.add(header, BorderLayout.NORTH);

        String[] cols = {"Inventory ID", "Item Name", "Item Type", "Quantity", "Reorder Level", "Order ID", "Forecast ID"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        inventoryTable = new JTable(tableModel);
        main.add(new JScrollPane(inventoryTable), BorderLayout.CENTER);

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

    private void loadInventoryFromDatabase() {
        tableModel.setRowCount(0);
        try (Connection conn = DBManager.getConnection();
             ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Inventory")) {
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("Inventory_ID"),
                    rs.getString("Item_Name"),
                    rs.getString("Item_Type"),
                    rs.getInt("Quantity_Available"),
                    rs.getInt("Reorder_Level"),
                    rs.getInt("Order_ID"),
                    rs.getInt("Forecast_ID")
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
        JTextField typeField = new JTextField(15);
        JTextField quantityField = new JTextField(5);
        JTextField reorderField = new JTextField(5);
        JTextField orderIdField = new JTextField(5);
        JTextField forecastIdField = new JTextField(5);

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Inventory ID:")); panel.add(idField);
        panel.add(new JLabel("Item Name:")); panel.add(nameField);
        panel.add(new JLabel("Item Type:")); panel.add(typeField);
        panel.add(new JLabel("Quantity:")); panel.add(quantityField);
        panel.add(new JLabel("Reorder Level:")); panel.add(reorderField);
        panel.add(new JLabel("Order ID:")); panel.add(orderIdField);
        panel.add(new JLabel("Forecast ID:")); panel.add(forecastIdField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Inventory", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = DBManager.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                         "INSERT INTO Inventory (Inventory_ID, Item_Name, Item_Type, Quantity_Available, Reorder_Level, Order_ID, Forecast_ID) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                ps.setInt(1, Integer.parseInt(idField.getText()));
                ps.setString(2, nameField.getText());
                ps.setString(3, typeField.getText());
                ps.setInt(4, Integer.parseInt(quantityField.getText()));
                ps.setInt(5, Integer.parseInt(reorderField.getText()));
                ps.setInt(6, Integer.parseInt(orderIdField.getText()));
                ps.setInt(7, Integer.parseInt(forecastIdField.getText()));
                ps.executeUpdate();
                loadInventoryFromDatabase();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void showUpdateDialog() {
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to update.");
            return;
        }

        int inventoryId = (int) tableModel.getValueAt(selectedRow, 0);
        String itemName = (String) tableModel.getValueAt(selectedRow, 1);
        String itemType = (String) tableModel.getValueAt(selectedRow, 2);
        int quantity = (int) tableModel.getValueAt(selectedRow, 3);
        int reorderLevel = (int) tableModel.getValueAt(selectedRow, 4);
        int orderId = (int) tableModel.getValueAt(selectedRow, 5);
        int forecastId = (int) tableModel.getValueAt(selectedRow, 6);

        JTextField nameField = new JTextField(itemName, 15);
        JTextField typeField = new JTextField(itemType, 15);
        JTextField quantityField = new JTextField(String.valueOf(quantity), 5);
        JTextField reorderField = new JTextField(String.valueOf(reorderLevel), 5);
        JTextField orderIdField = new JTextField(String.valueOf(orderId), 5);
        JTextField forecastIdField = new JTextField(String.valueOf(forecastId), 5);

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Item Name:")); panel.add(nameField);
        panel.add(new JLabel("Item Type:")); panel.add(typeField);
        panel.add(new JLabel("Quantity:")); panel.add(quantityField);
        panel.add(new JLabel("Reorder Level:")); panel.add(reorderField);
        panel.add(new JLabel("Order ID:")); panel.add(orderIdField);
        panel.add(new JLabel("Forecast ID:")); panel.add(forecastIdField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Update Inventory", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = DBManager.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                         "UPDATE Inventory SET Item_Name=?, Item_Type=?, Quantity_Available=?, Reorder_Level=?, Order_ID=?, Forecast_ID=? WHERE Inventory_ID=?")) {
                ps.setString(1, nameField.getText());
                ps.setString(2, typeField.getText());
                ps.setInt(3, Integer.parseInt(quantityField.getText()));
                ps.setInt(4, Integer.parseInt(reorderField.getText()));
                ps.setInt(5, Integer.parseInt(orderIdField.getText()));
                ps.setInt(6, Integer.parseInt(forecastIdField.getText()));
                ps.setInt(7, inventoryId);
                ps.executeUpdate();
                loadInventoryFromDatabase();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void deleteSelected() {
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to delete.");
            return;
        }

        int inventoryId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete Inventory ID " + inventoryId + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBManager.getConnection();
                 PreparedStatement ps = conn.prepareStatement("DELETE FROM Inventory WHERE Inventory_ID = ?")) {
                ps.setInt(1, inventoryId);
                ps.executeUpdate();
                loadInventoryFromDatabase();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
