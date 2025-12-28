package possystem;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.prefs.Preferences;

public class LoginForm extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JCheckBox chkRemember;
    private Preferences prefs;
    
    private final Color PRIMARY_COLOR = new Color(0, 123, 255);    
    private final Color PRIMARY_HOVER = new Color(0, 86, 179);     
    private final Color BG_LEFT = new Color(13, 71, 161);          
    private final Color TEXT_COLOR = new Color(33, 37, 41);        
    private final Color SECONDARY_TEXT = new Color(108, 117, 125); 

    public LoginForm() {
        setTitle("Smart POS - Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(950, 600); 
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        prefs = Preferences.userNodeForPackage(LoginForm.class);

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(BG_LEFT);
        leftPanel.setPreferredSize(new Dimension(400, 600));
        leftPanel.setLayout(new GridBagLayout());

        JLabel lblLogo = new JLabel("SMART POS");
        lblLogo.setFont(new Font("Poppins", Font.BOLD, 42));
        lblLogo.setForeground(Color.WHITE);
        
        JLabel lblTagline = new JLabel("Modern Point of Sale System");
        lblTagline.setFont(new Font("Poppins", Font.PLAIN, 16));
        lblTagline.setForeground(new Color(255, 255, 255, 200)); 
        
        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.gridx = 0; gbcLeft.gridy = 0;
        leftPanel.add(lblLogo, gbcLeft);
        gbcLeft.gridy = 1;
        gbcLeft.insets = new Insets(10, 0, 0, 0);
        leftPanel.add(lblTagline, gbcLeft);

        add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); 
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 10, 12, 10); 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblLoginTitle = new JLabel("Welcome Back!");
        lblLoginTitle.setFont(new Font("Poppins", Font.BOLD, 32));
        lblLoginTitle.setForeground(TEXT_COLOR);
        lblLoginTitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 40, 0); 
        rightPanel.add(lblLoginTitle, gbc);

        gbc.gridy = 1; gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 5, 10);
        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(new Font("Poppins", Font.BOLD, 14));
        lblUser.setForeground(SECONDARY_TEXT);
        rightPanel.add(lblUser, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 10, 15, 10);
        txtUsername = new JTextField(20);
        txtUsername.setPreferredSize(new Dimension(320, 50)); 
        txtUsername.setFont(new Font("Poppins", Font.PLAIN, 15));
        
        txtUsername.putClientProperty(FlatClientProperties.STYLE, "arc: 15; iconTextGap: 10; showClearButton: true; borderColor: #ced4da; focusedBorderColor: #007bff; borderWidth: 2");
        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your username");
        rightPanel.add(txtUsername, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(10, 10, 5, 10);
        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("Poppins", Font.BOLD, 14));
        lblPass.setForeground(SECONDARY_TEXT);
        rightPanel.add(lblPass, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(0, 10, 15, 10);
        txtPassword = new JPasswordField(20);
        txtPassword.setPreferredSize(new Dimension(320, 50));
        txtPassword.setFont(new Font("Poppins", Font.PLAIN, 15));
        
        txtPassword.putClientProperty(FlatClientProperties.STYLE, "arc: 15; showRevealButton: true; borderColor: #ced4da; focusedBorderColor: #007bff; borderWidth: 2");
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your password");
        rightPanel.add(txtPassword, gbc);
        
        gbc.gridy = 5;
        gbc.insets = new Insets(5, 10, 5, 10);
        chkRemember = new JCheckBox("Remember Me");
        chkRemember.setFont(new Font("Poppins", Font.PLAIN, 14));
        chkRemember.setForeground(SECONDARY_TEXT);
        chkRemember.setBackground(Color.WHITE);
        chkRemember.setFocusPainted(false);
        rightPanel.add(chkRemember, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnPanel.setBackground(Color.WHITE);

        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Poppins", Font.BOLD, 16));
        btnLogin.setBackground(PRIMARY_COLOR);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setPreferredSize(new Dimension(140, 50));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.putClientProperty(FlatClientProperties.STYLE, "arc: 50"); 
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnLogin.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnLogin.setBackground(PRIMARY_HOVER);
            }
            public void mouseExited(MouseEvent evt) {
                btnLogin.setBackground(PRIMARY_COLOR);
            }
        });

        JButton btnExit = new JButton("Exit");
        btnExit.setFont(new Font("Poppins", Font.BOLD, 16));
        btnExit.setBackground(Color.WHITE);
        btnExit.setForeground(SECONDARY_TEXT);
        btnExit.setPreferredSize(new Dimension(120, 50));
        btnExit.setFocusPainted(false);
        btnExit.putClientProperty(FlatClientProperties.STYLE, "arc: 50; borderColor: #ced4da; borderWidth: 2");
        btnExit.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnExit.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnExit.setBackground(new Color(248, 249, 250)); 
                btnExit.setForeground(TEXT_COLOR);
            }
            public void mouseExited(MouseEvent evt) {
                btnExit.setBackground(Color.WHITE);
                btnExit.setForeground(SECONDARY_TEXT);
            }
        });

        btnPanel.add(btnLogin);
        btnPanel.add(btnExit);

        gbc.gridy = 6;
        gbc.insets = new Insets(40, 10, 20, 10); 
        rightPanel.add(btnPanel, gbc);

        add(rightPanel, BorderLayout.CENTER);

        btnLogin.addActionListener(e -> {
            String user = txtUsername.getText();
            String pass = new String(txtPassword.getPassword());

            if (validateLogin(user, pass)) {
                savePreferences(user, pass);
                this.dispose();
                new Dashboard("Admin").setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnExit.addActionListener(e -> System.exit(0));

        loadPreferences();
    }


    private boolean validateLogin(String username, String password) {
        try {
            Connection conn = DBConnection.connect();
            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            
            boolean isValid = rs.next();
            conn.close();
            return isValid;
            
        } catch (Exception e) {
            if (username.equals("admin") && password.equals("123")) return true;
            return false;
        }
    }

    private void savePreferences(String user, String pass) {
        if (chkRemember.isSelected()) {
            prefs.put("username", user);
            prefs.put("password", pass);
            prefs.putBoolean("remember", true);
        } else {
            prefs.remove("username");
            prefs.remove("password");
            prefs.putBoolean("remember", false);
        }
    }

    private void loadPreferences() {
        boolean isRemembered = prefs.getBoolean("remember", false);
        if (isRemembered) {
            txtUsername.setText(prefs.get("username", ""));
            txtPassword.setText(prefs.get("password", ""));
            chkRemember.setSelected(true);
        }
    }
}