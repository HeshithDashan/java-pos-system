package possystem;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Dashboard extends JFrame {

    public Dashboard(String userRole) {
        setTitle("Smart POS - Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- 1. Header Panel ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(33, 150, 243));
        headerPanel.setPreferredSize(new Dimension(800, 90));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Title
        JLabel lblTitle = new JLabel(" SMART POS - " + userRole.toUpperCase());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);

        try {
            URL iconURL = getClass().getResource("/icons/header.png");
            if (iconURL != null) {
                ImageIcon icon = new ImageIcon(iconURL);
                Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                lblTitle.setIcon(new ImageIcon(img));
                lblTitle.setIconTextGap(15);
            }
        } catch (Exception e) {
            System.out.println("Header icon error: " + e.getMessage());
        }

        headerPanel.add(lblTitle, BorderLayout.WEST);

        // Logout Button
        JButton btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogout.putClientProperty("JButton.buttonType", "roundRect");
        btnLogout.setBackground(new Color(244, 67, 54));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setPreferredSize(new Dimension(120, 45));

        headerPanel.add(btnLogout, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // --- 2. Menu Buttons Grid ---
        JPanel menuPanel = new JPanel(new GridLayout(2, 3, 30, 30));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Common Buttons
        menuPanel.add(createMenuButton("New Sale", "sale.png"));
        menuPanel.add(createMenuButton("Products", "product.png")); 
        menuPanel.add(createMenuButton("Customers", "customer.png"));

        // Admin Only Buttons
        if (userRole.equalsIgnoreCase("admin")) {
            menuPanel.add(createMenuButton("Reports", "report.png"));
            menuPanel.add(createMenuButton("Settings", "settings.png"));
            menuPanel.add(createMenuButton("User Management", "user.png"));
        }

        add(menuPanel, BorderLayout.CENTER);

        // Logout Logic (Using our custom Message class)
        btnLogout.addActionListener(e -> {
            if (Message.showConfirm(this, "Are you sure you want to logout?")) {
                this.dispose();
                new LoginForm().setVisible(true);
            }
        });
    }

    private JButton createMenuButton(String text, String iconName) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btn.setFocusPainted(false);
        btn.putClientProperty("JButton.buttonType", "roundRect");

        // Icon Setup
        try {
            java.net.URL iconURL = getClass().getResource("/icons/" + iconName);
            if (iconURL != null) {
                ImageIcon originalIcon = new ImageIcon(iconURL);
                Image img = originalIcon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
                btn.setIcon(new ImageIcon(img));
            }
        } catch (Exception e) {
        }

        btn.setHorizontalTextPosition(SwingConstants.CENTER);
        btn.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn.setIconTextGap(15);

        // 🔥 Button Click Logic (Fixed: Removed Duplicate Listener)
        btn.addActionListener(e -> {
            if (text.equals("Products") || text.equals("Manage Products")) {
                new ProductManagement().setVisible(true);
            } 
            else if (text.contains("New Sale")) {
                new NewSale().setVisible(true);
            }
            // Future buttons can be added here with else if...
        });

        return btn;
    }
}