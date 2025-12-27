package possystem;

import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnExit;

    public LoginForm() {
        setTitle("POS System Login");
        setSize(400, 350); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        getRootPane().setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setLayout(new BorderLayout(10, 10));

        JLabel lblHeader = new JLabel("POS SYSTEM", SwingConstants.CENTER);
        lblHeader.putClientProperty("FlatLaf.styleClass", "h1"); 
        lblHeader.setForeground(new Color(33, 150, 243)); 
        add(lblHeader, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(4, 1, 8, 8)); 
        
        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 14)); 
        formPanel.add(lblUser);

        txtUsername = new JTextField();
        txtUsername.putClientProperty("JTextField.placeholderText", "Enter your username");
        txtUsername.putClientProperty("JComponent.roundRect", true); 
        txtUsername.setFont(new Font("Segoe UI", Font.BOLD, 14)); 
        formPanel.add(txtUsername);

        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.putClientProperty("JTextField.placeholderText", "Enter your password");
        txtPassword.putClientProperty("JComponent.showRevealButton", true);
        txtPassword.putClientProperty("JComponent.roundRect", true); 
        txtPassword.setFont(new Font("Segoe UI", Font.BOLD, 14)); 
        formPanel.add(txtPassword);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(new Color(33, 150, 243));
        btnLogin.setForeground(Color.WHITE); 
        btnLogin.setPreferredSize(new Dimension(120, 40)); 
        btnLogin.putClientProperty("JButton.buttonType", "roundRect"); 
        
        btnExit = new JButton("Exit");
        btnExit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnExit.setBackground(new Color(244, 67, 54)); 
        btnExit.setForeground(Color.WHITE);
        btnExit.setPreferredSize(new Dimension(100, 40));
        btnExit.putClientProperty("JButton.buttonType", "roundRect");

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnExit);

        add(buttonPanel, BorderLayout.SOUTH);

        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            if (username.equals("admin") && password.equals("123")) {
                JOptionPane.showMessageDialog(this, "Login Successful! Welcome Sago!");
                this.dispose(); 
                new Dashboard().setVisible(true); 
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Details!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnExit.addActionListener(e -> System.exit(0));
    }
}