package acrds;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class OrderPage extends JFrame {
    private HomePage homePage;
    private JTable ordersTable;
    private DefaultTableModel tableModel;

    public OrderPage(HomePage homePage) {
        this.homePage = homePage;
        setTitle("Orders Management");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setupUI();
        loadOrdersFromDB();
    }

    private void setupUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Orders Management", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            homePage.setVisible(true);
            dispose();
        });

        JPanel top = new JPanel(new BorderLayout());
        top.add(backButton, BorderLayout.WEST);
        top.add(title, BorderLayout.CENTER);
        mainPanel.add(top, BorderLayout.NORTH);

        String[] cols = {"Order ID", "Quantity", "Expected Delivery", "Status", "Order Date"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        ordersTable = new JTable(tableModel);
        mainPanel.add(new JScrollPane(ordersTable), BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton add = new JButton("Add");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");

        add.addActionListener(e -> showOrderDialog(false));
        update.addActionListener(e -> showOrderDialog(true));
        delete.addActionListener(e -> deleteOrder());

        bottom.add(add);
        bottom.add(update);
        bottom.add(delete);

        mainPanel.add(bottom, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void loadOrdersFromDB() {
        tableModel.setRowCount(0);
        String query = "SELECT * FROM Orders ORDER BY Order_ID";
        try (Connection conn = DBManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("Order_ID"),
                        rs.getInt("Quantity_Ordered"),
                        rs.getDate("Expected_Delivery_Date"),
                        rs.getString("Order_Status"),
                        rs.getDate("Order_date")
                });
            }
        } catch (SQLException e) {
            showError("DB Error: " + e.getMessage());
        }
    }

    private void showOrderDialog(boolean isUpdate) {
        int row = ordersTable.getSelectedRow();
        if (isUpdate && row == -1) {
            showError("Select an order to update.");
            return;
        }

        JTextField idField = new JTextField();
        JTextField qtyField = new JTextField();
        JTextField delDateField = new JTextField("yyyy-MM-dd");
        JTextField ordDateField = new JTextField("yyyy-MM-dd");
        JComboBox<String> statusBox = new JComboBox<>(new String[]{"Pending", "Processing", "Shipped", "Delivered", "Cancelled"});

        if (isUpdate) {
            idField.setText(tableModel.getValueAt(row, 0).toString());
            idField.setEnabled(false);
            qtyField.setText(tableModel.getValueAt(row, 1).toString());
            delDateField.setText(tableModel.getValueAt(row, 2).toString());
            statusBox.setSelectedItem(tableModel.getValueAt(row, 3).toString());
            ordDateField.setText(tableModel.getValueAt(row, 4).toString());
        }

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Order ID:")); panel.add(idField);
        panel.add(new JLabel("Quantity:")); panel.add(qtyField);
        panel.add(new JLabel("Expected Delivery Date:")); panel.add(delDateField);
        panel.add(new JLabel("Order Date:")); panel.add(ordDateField);
        panel.add(new JLabel("Status:")); panel.add(statusBox);

        int result = JOptionPane.showConfirmDialog(this, panel, isUpdate ? "Update Order" : "Add Order", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int id = Integer.parseInt(idField.getText());
                int qty = Integer.parseInt(qtyField.getText());
                Date delDate = Date.valueOf(delDateField.getText());
                Date ordDate = Date.valueOf(ordDateField.getText());
                String status = (String) statusBox.getSelectedItem();

                Connection conn = DBManager.getConnection();
                if (isUpdate) {
                    PreparedStatement ps = conn.prepareStatement("UPDATE Orders SET Quantity_Ordered=?, Expected_Delivery_Date=?, Order_Status=?, Order_date=? WHERE Order_ID=?");
                    ps.setInt(1, qty);
                    ps.setDate(2, delDate);
                    ps.setString(3, status);
                    ps.setDate(4, ordDate);
                    ps.setInt(5, id);
                    ps.executeUpdate();
                } else {
                    if (orderIdExists(id)) {
                        showError("Order ID already exists.");
                        return;
                    }
                    PreparedStatement ps = conn.prepareStatement("INSERT INTO Orders VALUES (?, ?, ?, ?, ?)");
                    ps.setInt(1, id);
                    ps.setInt(2, qty);
                    ps.setDate(3, delDate);
                    ps.setString(4, status);
                    ps.setDate(5, ordDate);
                    ps.executeUpdate();
                }

                loadOrdersFromDB();
            } catch (Exception e) {
                showError("Invalid input: " + e.getMessage());
            }
        }
    }

    private void deleteOrder() {
        int row = ordersTable.getSelectedRow();
        if (row == -1) {
            showError("Select an order to delete.");
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this order?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBManager.getConnection();
                 PreparedStatement ps = conn.prepareStatement("DELETE FROM Orders WHERE Order_ID=?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
                loadOrdersFromDB();
            } catch (SQLException e) {
                showError("Error deleting order: " + e.getMessage());
            }
        }
    }

    private boolean orderIdExists(int id) {
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM Orders WHERE Order_ID=?")) {
            ps.setInt(1, id);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            return false;
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
