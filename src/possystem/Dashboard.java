package possystem;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Dashboard extends JFrame {

    private JPanel contentPanel;
    private JPanel menuPanel;

    public Dashboard(String userRole) {
        setTitle("Smart POS - Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(33, 150, 243));
        headerPanel.setPreferredSize(new Dimension(800, 90));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel(" SMART POS - " + userRole.toUpperCase());
        lblTitle.setFont(new Font("Poppins", Font.BOLD, 28));
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
            System.out.println("Icon Error: " + e.getMessage());
        }

        headerPanel.add(lblTitle, BorderLayout.WEST);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Poppins", Font.BOLD, 16));
        btnLogout.putClientProperty("JButton.buttonType", "roundRect");
        btnLogout.setBackground(new Color(244, 67, 54));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setPreferredSize(new Dimension(120, 45));

        headerPanel.add(btnLogout, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        contentPanel = new JPanel(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);

        menuPanel = new JPanel(new GridLayout(2, 3, 30, 30));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        menuPanel.add(createMenuButton("New Sale", "sale.png"));
        menuPanel.add(createMenuButton("Products", "product.png"));
        menuPanel.add(createMenuButton("Customers", "customer.png"));

        if (userRole.equalsIgnoreCase("admin")) {
            menuPanel.add(createMenuButton("Reports", "report.png"));
            menuPanel.add(createMenuButton("Settings", "settings.png"));
            menuPanel.add(createMenuButton("User Management", "user.png"));
        }

        showMenu();

        btnLogout.addActionListener(e -> {
            if (Message.showConfirm(this, "Are you sure you want to logout?")) {
                this.dispose();
                new LoginForm().setVisible(true);
            }
        });
    }

    private void showMenu() {
        contentPanel.removeAll();
        contentPanel.add(menuPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JButton createMenuButton(String text, String iconName) {
        JButton btn = new JButton(text);

        btn.setFont(new Font("Poppins", Font.BOLD, 18));
        btn.setForeground(new Color(220, 220, 220));
        btn.setFocusPainted(false);

        btn.setBackground(new Color(60, 63, 65));

        btn.putClientProperty("JButton.buttonType", "roundRect");
        btn.putClientProperty("Component.borderColor", new Color(100, 100, 100));
        btn.putClientProperty("Component.borderWidth", 1);

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
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(80, 83, 85));
                btn.putClientProperty("Component.borderColor", new Color(33, 150, 243));
                btn.putClientProperty("Component.borderWidth", 2);
                btn.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(60, 63, 65));
                btn.putClientProperty("Component.borderColor", new Color(100, 100, 100));
                btn.putClientProperty("Component.borderWidth", 1);
                btn.repaint();
            }
        });

        btn.addActionListener(e -> {
            if (text.equals("Products") || text.equals("Manage Products")) {
                contentPanel.removeAll();
                contentPanel.add(new ProductManagement(evt -> showMenu()));
                contentPanel.revalidate();
                contentPanel.repaint();
            } else if (text.contains("New Sale")) {
                contentPanel.removeAll();
                contentPanel.add(new NewSale(evt -> showMenu()));
                contentPanel.revalidate();
                contentPanel.repaint();
            } else if (text.contains("Customers")) {
                contentPanel.removeAll();
                contentPanel.add(new CustomerManagement(evt -> showMenu()));
                contentPanel.revalidate();
                contentPanel.repaint();
            } else if (text.contains("Reports")) {
                new Reports().setVisible(true);
            } else if (text.contains("Settings")) {
                contentPanel.removeAll();
                contentPanel.add(new SettingsPanel(evt -> showMenu()));
                contentPanel.revalidate();
                contentPanel.repaint();
            } else if (text.contains("User Management") || text.contains("Users")) {
                contentPanel.removeAll();
                contentPanel.add(new UserManagement(evt -> showMenu()));
                contentPanel.revalidate();
                contentPanel.repaint();
            }
        });

        return btn;
    }
}
