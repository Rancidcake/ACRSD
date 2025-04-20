package acrds;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class TransportationPage extends JFrame {
    private JTable transportationTable;    // Changed modeTable to transportationTable
    private DefaultTableModel tableModel;
    private HomePage homePage;

    public TransportationPage(HomePage homePage) {
        this.homePage = homePage;
        setTitle("Transportation Modes");
        setSize(800, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
        loadTransportations();    // Changed loadModes to loadTransportations
    }

    private void initUI() {
        JPanel main = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Transportation Modes", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));

        JButton back = new JButton("Back");
        back.addActionListener(e -> { homePage.setVisible(true); dispose(); });

        JPanel header = new JPanel(new BorderLayout());
        header.add(back, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);
        main.add(header, BorderLayout.NORTH);

        String[] cols = {"Transportation ID", "Transport Type", "Capacity", "Shipment ID"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        transportationTable = new JTable(tableModel);   // Changed modeTable to transportationTable
        main.add(new JScrollPane(transportationTable), BorderLayout.CENTER);

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

    private void loadTransportations() {    // Changed loadModes to loadTransportations
        tableModel.setRowCount(0);
        String sql = "SELECT * FROM Transportation_mode";    // Changed from Mode to Transportation
        try (Connection conn = DBManager.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("Mode_ID"),    // Changed from Mode_ID to Transportation_ID
                    rs.getString("Transport_Type"),
                    rs.getInt("Capacity"),
                    rs.getInt("Shipment_ID")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAddDialog() {
        JTextField idField = new JTextField(5);
        JTextField typeField = new JTextField(10);
        JTextField capField = new JTextField(5);
        JTextField shipField = new JTextField(5);

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Transportation ID:")); panel.add(idField);
        panel.add(new JLabel("Transport Type:")); panel.add(typeField);
        panel.add(new JLabel("Capacity:")); panel.add(capField);
        panel.add(new JLabel("Shipment ID:")); panel.add(shipField);

        int res = JOptionPane.showConfirmDialog(this, panel, "Add Transportation", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            String sql = "INSERT INTO Transportation_mode (Mode_ID, Transport_Type, Capacity, Shipment_ID) VALUES (?, ?, ?, ?)";   // Changed Mode to Transportation
            try (Connection conn = DBManager.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, Integer.parseInt(idField.getText()));
                ps.setString(2, typeField.getText());
                ps.setInt(3, Integer.parseInt(capField.getText()));
                ps.setInt(4, Integer.parseInt(shipField.getText()));
                ps.executeUpdate();
                loadTransportations();    // Changed loadModes to loadTransportations
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    private void showUpdateDialog() {
        int row = transportationTable.getSelectedRow();   // Changed modeTable to transportationTable
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a row to update."); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        JTextField typeField = new JTextField((String) tableModel.getValueAt(row, 1));
        JTextField capField = new JTextField(tableModel.getValueAt(row, 2).toString());
        JTextField shipField = new JTextField(tableModel.getValueAt(row, 3).toString());

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Transport Type:")); panel.add(typeField);
        panel.add(new JLabel("Capacity:")); panel.add(capField);
        panel.add(new JLabel("Shipment ID:")); panel.add(shipField);

        int res = JOptionPane.showConfirmDialog(this, panel, "Update Transportation", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            String sql = "UPDATE Transportation_mode SET Transport_Type=?, Capacity=?, Shipment_ID=? WHERE Mode_ID=?";   // Changed Mode to Transportation
            try (Connection conn = DBManager.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, typeField.getText());
                ps.setInt(2, Integer.parseInt(capField.getText()));
                ps.setInt(3, Integer.parseInt(shipField.getText()));
                ps.setInt(4, id);
                ps.executeUpdate();
                loadTransportations();    // Changed loadModes to loadTransportations
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    private void deleteSelected() {
        int row = transportationTable.getSelectedRow();   // Changed modeTable to transportationTable
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a row to delete."); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete Transportation ID " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM Transportation_mode WHERE Mode_ID=?";   // Changed Mode to Transportation
            try (Connection conn = DBManager.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.executeUpdate();
                loadTransportations();    // Changed loadModes to loadTransportations
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
