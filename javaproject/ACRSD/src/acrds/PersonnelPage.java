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

public class PersonnelPage extends JFrame {
    private HomePage homePage;
    private JTable personnelTable;
    private DefaultTableModel tableModel;

    public PersonnelPage(HomePage homePage) {
        this.homePage = homePage;
        setTitle("Personnel Management");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
        loadPersonnelFromDatabase();
    }

    private void initUI() {
        JPanel main = new JPanel(new BorderLayout());
        JPanel header = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Personnel Management", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        header.add(title, BorderLayout.CENTER);
        JButton back = new JButton("Back");
        back.addActionListener(e -> {
            homePage.setVisible(true);
            dispose();
        });
        header.add(back, BorderLayout.WEST);
        main.add(header, BorderLayout.NORTH);

        String[] cols = {"Personnel ID", "Name", "Rank", "Team ID"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        personnelTable = new JTable(tableModel);
        main.add(new JScrollPane(personnelTable), BorderLayout.CENTER);

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

    private void loadPersonnelFromDatabase() {
        tableModel.setRowCount(0);
        try (Connection conn = DBManager.getConnection();
             ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Personnel")) {
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("Personnel_Id"),
                    rs.getString("Name"),
                    rs.getString("Personnel_Rank"),
                    rs.getInt("Team_Id")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAddDialog() {
        javax.swing.JTextField idField = new javax.swing.JTextField(5);
        javax.swing.JTextField nameField = new javax.swing.JTextField(15);
        javax.swing.JTextField rankField = new javax.swing.JTextField(15);
        javax.swing.JTextField teamIdField = new javax.swing.JTextField(5);

        JPanel panel = new JPanel(new java.awt.GridLayout(0, 2));
        panel.add(new JLabel("Personnel ID:"));
        panel.add(idField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Rank:"));
        panel.add(rankField);
        panel.add(new JLabel("Team ID:"));
        panel.add(teamIdField);

        int result = javax.swing.JOptionPane.showConfirmDialog(this, panel, "Add Personnel", javax.swing.JOptionPane.OK_CANCEL_OPTION);
        if (result == javax.swing.JOptionPane.OK_OPTION) {
            try (Connection conn = DBManager.getConnection();
                 java.sql.PreparedStatement ps = conn.prepareStatement("INSERT INTO Personnel (Personnel_Id, Name, Personnel_Rank, Team_Id) VALUES (?, ?, ?, ?)")) {
                ps.setInt(1, Integer.parseInt(idField.getText()));
                ps.setString(2, nameField.getText());
                ps.setString(3, rankField.getText());
                ps.setInt(4, Integer.parseInt(teamIdField.getText()));
                ps.executeUpdate();
                loadPersonnelFromDatabase();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void showUpdateDialog() {
        int selectedRow = personnelTable.getSelectedRow();
        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Select a row to update.");
            return;
        }

        int personnelId = (int) tableModel.getValueAt(selectedRow, 0);
        String currentRank = (String) tableModel.getValueAt(selectedRow, 2);
        int currentTeamId = (int) tableModel.getValueAt(selectedRow, 3);

        javax.swing.JTextField rankField = new javax.swing.JTextField(currentRank, 15);
        javax.swing.JTextField teamIdField = new javax.swing.JTextField(String.valueOf(currentTeamId), 5);

        JPanel panel = new JPanel(new java.awt.GridLayout(0, 2));
        panel.add(new JLabel("Rank:"));
        panel.add(rankField);
        panel.add(new JLabel("Team ID:"));
        panel.add(teamIdField);

        int result = javax.swing.JOptionPane.showConfirmDialog(this, panel, "Update Personnel", javax.swing.JOptionPane.OK_CANCEL_OPTION);
        if (result == javax.swing.JOptionPane.OK_OPTION) {
            try (Connection conn = DBManager.getConnection();
                 java.sql.PreparedStatement ps = conn.prepareStatement("UPDATE Personnel SET Personnel_Rank = ?, Team_Id = ? WHERE Personnel_Id = ?")) {
                ps.setString(1, rankField.getText());
                ps.setInt(2, Integer.parseInt(teamIdField.getText()));
                ps.setInt(3, personnelId);
                ps.executeUpdate();
                loadPersonnelFromDatabase();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void deleteSelected() {
        int selectedRow = personnelTable.getSelectedRow();
        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Select a row to delete.");
            return;
        }

        int personnelId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = javax.swing.JOptionPane.showConfirmDialog(this, "Are you sure you want to delete Personnel ID " + personnelId + "?", "Confirm Delete", javax.swing.JOptionPane.YES_NO_OPTION);
        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            try (Connection conn = DBManager.getConnection();
                 java.sql.PreparedStatement ps = conn.prepareStatement("DELETE FROM Personnel WHERE Personnel_Id = ?")) {
                ps.setInt(1, personnelId);
                ps.executeUpdate();
                loadPersonnelFromDatabase();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
