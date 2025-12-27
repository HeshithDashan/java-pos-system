package possystem;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginForm extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnExit;

    public LoginForm() {
        setTitle("POS System Login");
        setSize(400, 450);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout());

        // --- Header (Blue Text) ---
        JLabel lblHeader = new JLabel("SMART POS", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblHeader.setForeground(new Color(33, 150, 243));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        add(lblHeader, BorderLayout.NORTH);

        // --- Form Panel ---
        JPanel mainContentPanel = new JPanel(new BorderLayout(10, 10));
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 30, 40));

        JPanel formPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        
        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 14)); 
        formPanel.add(lblUser);

        txtUsername = new JTextField();
        txtUsername.putClientProperty("JTextField.placeholderText", "Enter your username");
        txtUsername.putClientProperty("JComponent.roundRect", true); 
        formPanel.add(txtUsername);

        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.putClientProperty("JTextField.placeholderText", "Enter your password");
        txtPassword.putClientProperty("JComponent.showRevealButton", true);
        txtPassword.putClientProperty("JComponent.roundRect", true); 
        formPanel.add(txtPassword);

        mainContentPanel.add(formPanel, BorderLayout.CENTER);

        // --- Buttons ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        btnLogin = new JButton("Login");
        btnLogin.setBackground(new Color(33, 150, 243)); 
        btnLogin.setForeground(Color.WHITE); 
        btnLogin.setPreferredSize(new Dimension(130, 45)); 
        btnLogin.putClientProperty("JButton.buttonType", "roundRect"); 
        
        btnExit = new JButton("Exit");
        btnExit.setBackground(new Color(244, 67, 54)); 
        btnExit.setForeground(Color.WHITE);
        btnExit.setPreferredSize(new Dimension(110, 45));
        btnExit.putClientProperty("JButton.buttonType", "roundRect");

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnExit);

        mainContentPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainContentPanel, BorderLayout.CENTER);

        // --- 🔥 REAL DATABASE LOGIC ---
        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            try {
                // 1. Database එකට සම්බන්ධ වෙන්න
                Connection conn = DBConnection.connect();
                
                // 2. Query එක ලියන්න (මේ නම සහ පාස්වර්ඩ් තියෙන කෙනෙක් ඉන්නවද?)
                String sql = "SELECT * FROM users WHERE username=? AND password=?";
                PreparedStatement pst = conn.prepareStatement(sql);
                
                pst.setString(1, username); // පළවෙනි ? ලකුණට username දාන්න
                pst.setString(2, password); // දෙවෙනි ? ලකුණට password දාන්න
                
                // 3. Query එක Run කරන්න
                ResultSet rs = pst.executeQuery();
                
                if (rs.next()) {
                    // හරි නම් (Data ආවා නම්)
                    String role = rs.getString("role"); // එයාගේ තනතුර (admin/cashier)
                    
                    JOptionPane.showMessageDialog(this, "Login Successful! Welcome " + role + "!");
                    this.dispose(); 
                    new Dashboard().setVisible(true); 
                    
                } else {
                    // වැරදි නම්
                    JOptionPane.showMessageDialog(this, "Invalid Username or Password!", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
                
                conn.close(); // වැඩේ ඉවර වුනාම connection වහන්න
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
            }
        });

        btnExit.addActionListener(e -> System.exit(0));
    }
}