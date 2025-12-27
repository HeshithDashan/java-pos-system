package possystem;

import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {

    public Dashboard() {
        setTitle("Smart POS - Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

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

        JPanel menuPanel = new JPanel(new GridLayout(2, 3, 20, 20)); 
        menuPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50)); 

        menuPanel.add(createMenuButton("💰 New Sale (POS)"));
        menuPanel.add(createMenuButton("📦 Manage Products"));
        menuPanel.add(createMenuButton("👥 Customers"));
        menuPanel.add(createMenuButton("📊 Reports"));
        menuPanel.add(createMenuButton("⚙️ Settings"));
        menuPanel.add(createMenuButton("👤 User Management"));

        add(menuPanel, BorderLayout.CENTER);

        btnLogout.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                this.dispose(); 
                new LoginForm().setVisible(true); 
            }
        });
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btn.setFocusPainted(false);
        btn.putClientProperty("JButton.buttonType", "roundRect"); 
        return btn;
    }
}
