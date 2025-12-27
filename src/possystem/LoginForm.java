package possystem;

import javax.swing.*;
import java.awt.*;

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

        JLabel lblHeader = new JLabel(" SMART POS", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 30)); 
        lblHeader.setForeground(new Color(33, 150, 243)); 
        lblHeader.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        
        add(lblHeader, BorderLayout.NORTH);

        JPanel mainContentPanel = new JPanel(new BorderLayout(10, 10));
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 30, 40)); 

        JPanel formPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        
        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 16)); 
        formPanel.add(lblUser);

        txtUsername = new JTextField();
        txtUsername.putClientProperty("JTextField.placeholderText", "Enter your username");
        txtUsername.putClientProperty("JComponent.roundRect", true); 
        txtUsername.setFont(new Font("Segoe UI", Font.BOLD, 14)); 
        txtUsername.setPreferredSize(new Dimension(100, 40)); 
        formPanel.add(txtUsername);

        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formPanel.add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.putClientProperty("JTextField.placeholderText", "Enter your password");
        txtPassword.putClientProperty("JComponent.showRevealButton", true);
        txtPassword.putClientProperty("JComponent.roundRect", true); 
        txtPassword.setFont(new Font("Segoe UI", Font.BOLD, 14)); 
        txtPassword.setPreferredSize(new Dimension(100, 40));
        formPanel.add(txtPassword);

        mainContentPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setBackground(new Color(33, 150, 243)); 
        btnLogin.setForeground(Color.WHITE); 
        btnLogin.setPreferredSize(new Dimension(130, 45)); 
        btnLogin.putClientProperty("JButton.buttonType", "roundRect"); 
        
        btnExit = new JButton("Exit");
        btnExit.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnExit.setBackground(new Color(244, 67, 54)); 
        btnExit.setForeground(Color.WHITE);
        btnExit.setPreferredSize(new Dimension(110, 45));
        btnExit.putClientProperty("JButton.buttonType", "roundRect");

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnExit);

        mainContentPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainContentPanel, BorderLayout.CENTER);

        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            if (username.equals("admin") && password.equals("123")) {
                this.dispose(); 
                new Dashboard().setVisible(true); 
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Details!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnExit.addActionListener(e -> System.exit(0));
    }
}