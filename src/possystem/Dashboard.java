package possystem;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Dashboard extends JFrame {

    public Dashboard() {
        setTitle("Smart POS - Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full Screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- 1. Header Panel ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(33, 150, 243)); // Header Blue Color
        headerPanel.setPreferredSize(new Dimension(800, 90)); // උස චුට්ටක් වැඩි කළා අයිකන් එකට
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // 🔥 HEADER TITLE + ICON 🔥
        JLabel lblTitle = new JLabel(" SMART POS DASHBOARD"); // ඉස්සරහින් පොඩි ඉඩක් තිබ්බා
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        
        // Header Icon එක Load කිරීම සහ Resize කිරීම
        try {
            URL iconURL = getClass().getResource("/icons/header.png"); // ඔයා දාපු අලුත් පින්තූරේ නම
            if (iconURL != null) {
                ImageIcon icon = new ImageIcon(iconURL);
                // Header එකට ගැලපෙන සයිස් එකකට (40x40) පොඩි කරමු
                Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                lblTitle.setIcon(new ImageIcon(img));
                lblTitle.setIconTextGap(15); // Icon එකයි Text එකයි අතර පරතරය
            }
        } catch (Exception e) {
            System.out.println("Header icon error: " + e.getMessage());
        }
        
        headerPanel.add(lblTitle, BorderLayout.WEST);

        // 🔥 LOGOUT BUTTON (RED COLOR) 🔥
        JButton btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogout.putClientProperty("JButton.buttonType", "roundRect");
        
        // පාට දාන කොටස (Login එකේ Exit බට්න් එකේ රතු පාටම ගත්තා)
        btnLogout.setBackground(new Color(244, 67, 54)); // ලස්සන රතු පාටක්
        btnLogout.setForeground(Color.WHITE); // අකුරු සුදු පාටින්
        btnLogout.setPreferredSize(new Dimension(120, 45)); // බට්න් එකේ සයිස් එක හැදුවා

        headerPanel.add(btnLogout, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // --- 2. Menu Buttons Grid (කලින් විදියමයි) ---
        JPanel menuPanel = new JPanel(new GridLayout(2, 3, 30, 30));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        menuPanel.add(createMenuButton("New Sale", "sale.png"));
        menuPanel.add(createMenuButton("Products", "product.png"));
        menuPanel.add(createMenuButton("Customers", "customer.png"));
        menuPanel.add(createMenuButton("Reports", "report.png"));
        menuPanel.add(createMenuButton("Settings", "settings.png"));
        menuPanel.add(createMenuButton("Users", "user.png"));

        add(menuPanel, BorderLayout.CENTER);

        // --- Logout Logic ---
        btnLogout.addActionListener(e -> {
            // Logout අහන Box එකේ අයිකන් එකත් Warning විදියට වෙනස් කළා
            int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (choice == JOptionPane.YES_OPTION) {
                this.dispose();
                new LoginForm().setVisible(true);
            }
        });
    }

    // --- Button Creator Method (කලින් විදියමයි) ---
    private JButton createMenuButton(String text, String iconName) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btn.setFocusPainted(false);
        btn.putClientProperty("JButton.buttonType", "roundRect");

        try {
            URL iconURL = getClass().getResource("/icons/" + iconName);
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

        return btn;
    }
}