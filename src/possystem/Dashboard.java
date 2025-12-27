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

        // --- 1. Header ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(33, 150, 243));
        headerPanel.setPreferredSize(new Dimension(800, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel("🛒 POS SYSTEM DASHBOARD");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        JButton btnLogout = new JButton("Logout");
        btnLogout.putClientProperty("JButton.buttonType", "roundRect");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 14));
        headerPanel.add(btnLogout, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // --- 2. Menu Buttons Grid ---
        JPanel menuPanel = new JPanel(new GridLayout(2, 3, 30, 30)); // පරතරය ටිකක් වැඩි කළා (30)
        menuPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // 🔥 මෙන්න මෙතන තමයි අපි අර පින්තූරවල නම් ටික දෙන්නේ
        // (Text එක, Image එකේ නම)
        menuPanel.add(createMenuButton("New Sale", "sale.png"));
        menuPanel.add(createMenuButton("Products", "product.png"));
        menuPanel.add(createMenuButton("Customers", "customer.png"));
        menuPanel.add(createMenuButton("Reports", "report.png"));
        menuPanel.add(createMenuButton("Settings", "settings.png"));
        menuPanel.add(createMenuButton("Users", "user.png"));

        add(menuPanel, BorderLayout.CENTER);

        // --- Logout Logic ---
        btnLogout.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this, "Logout?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                this.dispose();
                new LoginForm().setVisible(true);
            }
        });
    }

    // 🔥 PRO BUTTON CREATOR METHOD 🔥
    private JButton createMenuButton(String text, String iconName) {
        JButton btn = new JButton(text);
        
        // 1. අකුරු වල ස්ටයිල් එක
        btn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btn.setFocusPainted(false);
        btn.putClientProperty("JButton.buttonType", "roundRect");

        // 2. Icon එක Load කරලා Resize කරන කොටස
        try {
            // "icons" පැකේජ් එක ඇතුලේ තියෙන පින්තූරේ ගන්න
            URL iconURL = getClass().getResource("/icons/" + iconName);
            
            if (iconURL != null) {
                ImageIcon originalIcon = new ImageIcon(iconURL);
                
                // Image එක 64x64 සයිස් එකට පොඩි කරමු (Dashboard එකට ලොකු අයිකන් ලස්සනයි)
                Image img = originalIcon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
                
                btn.setIcon(new ImageIcon(img)); // බට්න් එකට අයිකන් එක දානවා
            } else {
                System.err.println("Icon not found: " + iconName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 3. Icon එක සහ Text එක තියෙන විදිය (Layout)
        // අයිකන් එක උඩින්, Text එක යටින්
        btn.setHorizontalTextPosition(SwingConstants.CENTER);
        btn.setVerticalTextPosition(SwingConstants.BOTTOM);
        
        // Icon එකයි Text එකයි අතර පරතරය
        btn.setIconTextGap(15); 

        return btn;
    }
}