package possystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnExit;

    public LoginForm() {

        setTitle("POS System Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setLayout(new BorderLayout()); 

        JLabel lblHeader = new JLabel("Please Login", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 20));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // වටේට ඉඩ තියන්න
        add(lblHeader, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); // ෆෝම් එක වටේට ඉඩ

        formPanel.add(new JLabel("Username:"));
        txtUsername = new JTextField();
        formPanel.add(txtUsername);

        formPanel.add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        formPanel.add(txtPassword);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnLogin = new JButton("Login");
        btnExit = new JButton("Exit");

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnExit);

        add(buttonPanel, BorderLayout.SOUTH);
        
        btnLogin.addActionListener(e -> {

            JOptionPane.showMessageDialog(this, "Login Button Clicked!");
        });

        btnExit.addActionListener(e -> System.exit(0)); 
    }
}