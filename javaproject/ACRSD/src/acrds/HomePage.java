package acrds;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class HomePage extends JFrame {
    private JPanel mainPanel;
    private static final Color BACKGROUND_COLOR = new Color(10, 25, 50);
    private static final Color TEXT_COLOR = new Color(220, 220, 220);
    private static final Color HEADER_COLOR = new Color(15, 35, 70);
    private static final Color BUTTON_COLOR = new Color(25, 55, 100);
    private static final Color BUTTON_TEXT_COLOR = new Color(230, 230, 230);
    private static final Color CATEGORY_COLOR = new Color(40, 80, 150);
    private static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 26);
    private static final Font CATEGORY_FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);

    public HomePage() {
        setTitle("Military Logistics Command System");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel headerLabel = new JLabel("MILITARY LOGISTICS COMMAND SYSTEM", JLabel.CENTER);
        headerLabel.setFont(HEADER_FONT);
        headerLabel.setForeground(TEXT_COLOR);
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Menu panels - organized by military logistics categories
        JPanel menuPanel = new JPanel(new GridBagLayout());
        menuPanel.setBackground(BACKGROUND_COLOR);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(12, 12, 12, 12);
        
        // Operations Category
        JLabel operationsLabel = createCategoryLabel("OPERATIONS");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        menuPanel.add(operationsLabel, gbc);
        
        JButton operationsButton = createMenuButton("Operations Control");
        operationsButton.addActionListener(e -> openOperationPage());
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        menuPanel.add(operationsButton, gbc);
        
        JButton crisisButton = createMenuButton("Crisis Management");
        crisisButton.addActionListener(e -> openCrisisPage());
        gbc.gridx = 1;
        gbc.gridy = 1;
        menuPanel.add(crisisButton, gbc);
        
        // Supply Chain Category
        JLabel supplyLabel = createCategoryLabel("SUPPLY CHAIN");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        menuPanel.add(supplyLabel, gbc);
        
        JButton supplyButton = createMenuButton("Supply Management");
        supplyButton.addActionListener(e -> openSupplierPage());
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        menuPanel.add(supplyButton, gbc);
        
        JButton ordersButton = createMenuButton("Orders Management");
        ordersButton.addActionListener(e -> openOrderPage());
        gbc.gridx = 1;
        gbc.gridy = 3;
        menuPanel.add(ordersButton, gbc);
        
        // Inventory & Storage Category
        JLabel inventoryLabel = createCategoryLabel("INVENTORY & STORAGE");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        menuPanel.add(inventoryLabel, gbc);
        
        JButton inventoryButton = createMenuButton("Inventory Control");
        inventoryButton.addActionListener(e -> openInventoryPage());
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        menuPanel.add(inventoryButton, gbc);
        
        JButton warehouseButton = createMenuButton("Warehouse Management");
        warehouseButton.addActionListener(e -> openStoragePage());
        gbc.gridx = 1;
        gbc.gridy = 5;
        menuPanel.add(warehouseButton, gbc);
        
        // Units Category
        JLabel unitsLabel = createCategoryLabel("PERSONNEL & UNITS");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        menuPanel.add(unitsLabel, gbc);
        
        JButton personnelButton = createMenuButton("Personnel Management");
        personnelButton.addActionListener(e -> openPersonnelPage());
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        menuPanel.add(personnelButton, gbc);
        
        JButton defenseButton = createMenuButton("Defense Units Management");
        defenseButton.addActionListener(e -> openDefensePage());
        gbc.gridx = 1;
        gbc.gridy = 7;
        menuPanel.add(defenseButton, gbc);
        
        // Transportation & Risk Category
        JLabel transportLabel = createCategoryLabel("TRANSPORTATION & RISK");
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        menuPanel.add(transportLabel, gbc);
        
        JButton transportButton = createMenuButton("Transportation Control");
        transportButton.addActionListener(e -> openModePage());
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 1;
        menuPanel.add(transportButton, gbc);
        
        JButton riskButton = createMenuButton("Risk Assessment");
        riskButton.addActionListener(e -> openRiskPage());
        gbc.gridx = 1;
        gbc.gridy = 9;
        menuPanel.add(riskButton, gbc);
        
        // Exit Button
        JButton exitButton = createMenuButton("Exit System");
        exitButton.setBackground(new Color(120, 30, 30));
        exitButton.setForeground(BUTTON_TEXT_COLOR);
        exitButton.addActionListener(e -> System.exit(0));
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 12, 12, 12);
        menuPanel.add(exitButton, gbc);

        // Add scroll capabilities
        JScrollPane scrollPane = new JScrollPane(menuPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(BACKGROUND_COLOR);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        
        // Center menu with scroll pane
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(HEADER_COLOR);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel footerLabel = new JLabel("©️ 2025 MILITARY LOGISTICS COMMAND SYSTEM - RESTRICTED ACCESS", JLabel.CENTER);
        footerLabel.setForeground(TEXT_COLOR);
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        footerPanel.add(footerLabel, BorderLayout.CENTER);
        
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JLabel createCategoryLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(CATEGORY_FONT);
        label.setForeground(TEXT_COLOR);
        label.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, CATEGORY_COLOR));
        return label;
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(BUTTON_COLOR);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(50, 100, 180), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        button.setPreferredSize(new Dimension(220, 50));
        return button;
    }

    private void openOrderPage() {
        OrderPage orderPage = new OrderPage(this);
        orderPage.setLocationRelativeTo(null);
        orderPage.setVisible(true);
        this.setVisible(false);
    }

    private void openPersonnelPage() {
        PersonnelPage personnelPage = new PersonnelPage(this);
        personnelPage.setLocationRelativeTo(null);
        personnelPage.setVisible(true);
        this.setVisible(false);
    }
    
    private void openCrisisPage() {
        CrisisPage crisisPage = new CrisisPage(this);
        crisisPage.setLocationRelativeTo(null);
        crisisPage.setVisible(true);
        this.setVisible(false);
    }
    
    private void openInventoryPage() {
        InventoryPage inventoryPage = new InventoryPage(this);
        inventoryPage.setLocationRelativeTo(null);
        inventoryPage.setVisible(true);
        this.setVisible(false);
    }
    
    private void openDefensePage() {
        DefenseUnitsPage defensePage = new DefenseUnitsPage(this);
        defensePage.setLocationRelativeTo(null);
        defensePage.setVisible(true);
        this.setVisible(false);
    }
    
    private void openSupplierPage() {
        SupplierPage supplierPage = new SupplierPage(this);
        supplierPage.setLocationRelativeTo(null);
        supplierPage.setVisible(true);
        this.setVisible(false);
    }
    
    private void openRiskPage() {
        RiskAssessmentPage riskPage = new RiskAssessmentPage(this);
        riskPage.setLocationRelativeTo(null);
        riskPage.setVisible(true);
        this.setVisible(false);
    }
    
    private void openStoragePage() {
        WarehousePage storePage = new WarehousePage(this);
        storePage.setLocationRelativeTo(null);
        storePage.setVisible(true);
        this.setVisible(false);
    }
    
    private void openModePage() {
        TransportationPage modePage = new TransportationPage(this);
        modePage.setLocationRelativeTo(null);
        modePage.setVisible(true);
        this.setVisible(false);
    }
    
    private void openOperationPage() {
        OperationPage opPage = new OperationPage(this);
        opPage.setLocationRelativeTo(null);
        opPage.setVisible(true);
        this.setVisible(false);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            HomePage homePage = new HomePage();
            homePage.setVisible(true);
        });
    }
}