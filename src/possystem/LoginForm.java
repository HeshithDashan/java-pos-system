package possystem;

import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnExit;

    public LoginForm() {
        setTitle("POS System Login");
        setSize(400, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        getRootPane().setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setLayout(new BorderLayout(10, 10));

        JLabel lblHeader = new JLabel("POS SYSTEM", SwingConstants.CENTER);

        lblHeader.putClientProperty("FlatLaf.styleClass", "h1"); 
        add(lblHeader, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        
        formPanel.add(new JLabel("Username"));
        txtUsername = new JTextField();
        txtUsername.putClientProperty("JTextField.placeholderText", "Enter your username");
        formPanel.add(txtUsername);

        formPanel.add(new JLabel("Password"));
        txtPassword = new JPasswordField();
        txtPassword.putClientProperty("JTextField.placeholderText", "Enter your password");
        txtPassword.putClientProperty("JComponent.showRevealButton", true); 
        formPanel.add(txtPassword);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnLogin = new JButton("Login");
        btnLogin.putClientProperty("JButton.buttonType", "roundRect"); 
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        btnExit = new JButton("Exit");
        btnExit.putClientProperty("JButton.buttonType", "roundRect");

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnExit);

        add(buttonPanel, BorderLayout.SOUTH);

        // --- Logic එක වෙනස් කළා ---
        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            if (username.equals("admin") && password.equals("123")) {
                JOptionPane.showMessageDialog(this, "Login Successful! Welcome Sago!");
                
                this.dispose(); // Login එක වහන්න
                new Dashboard().setVisible(true); // Dashboard එක ඕපන් කරන්න
                
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Details!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnExit.addActionListener(e -> System.exit(0));
    }
}