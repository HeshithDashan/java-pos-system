package possystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class UserManagement extends JFrame {

    private JTextField txtUser, txtPass;
    private JComboBox<String> cmbRole;
    private JTable userTable;
    private DefaultTableModel tableModel;

    public UserManagement() {
        setTitle("Manage Users");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(24, 24, 24));
        setLayout(new BorderLayout(10, 10));

        // --- Header ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(33, 150, 243));
        headerPanel.setPreferredSize(new Dimension(800, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblTitle = new JLabel("👤 USER MANAGEMENT");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        
        try {
            java.net.URL iconURL = getClass().getResource("/icons/user.png");
            if (iconURL != null) {
                ImageIcon icon = new ImageIcon(iconURL);
                Image img = icon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
                lblTitle.setIcon(new ImageIcon(img));
                lblTitle.setIconTextGap(15);
            }
        } catch (Exception e) {}

        headerPanel.add(lblTitle, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // --- Input Panel (Left) ---
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBackground(new Color(24, 24, 24));
        inputPanel.setBorder(BorderFactory.createTitledBorder(null, "Add New User", 0, 0, new Font("Segoe UI", Font.BOLD, 14), Color.WHITE));
        inputPanel.setPreferredSize(new Dimension(300, 0));

        inputPanel.add(createLabel("Username:"));
        txtUser = createTextField();
        inputPanel.add(txtUser);

        inputPanel.add(createLabel("Password:"));
        txtPass = createTextField(); // Normal text field for visibility (Change to JPasswordField if needed)
        inputPanel.add(txtPass);

        inputPanel.add(createLabel("Role:"));
        String[] roles = {"Cashier", "Admin"};
        cmbRole = new JComboBox<>(roles);
        inputPanel.add(cmbRole);

        JButton btnAdd = new JButton("Add User");
        btnAdd.setBackground(new Color(46, 204, 113));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        
        JButton btnDelete = new JButton("Delete User");
        btnDelete.setBackground(new Color(231, 76, 60));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);

        inputPanel.add(btnAdd);
        inputPanel.add(btnDelete);

        JPanel westPanel = new JPanel(new BorderLayout());
        westPanel.setBackground(new Color(24, 24, 24));
        westPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        westPanel.add(inputPanel, BorderLayout.NORTH);
        add(westPanel, BorderLayout.WEST);

        // --- Table (Center) ---
        String[] columns = {"ID", "Username", "Password", "Role"};
        tableModel = new DefaultTableModel(columns, 0);
        userTable = new JTable(tableModel);
        userTable.setRowHeight(30);
        userTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));
        add(scrollPane, BorderLayout.CENTER);

        loadUsers();

        // --- Actions ---
        btnAdd.addActionListener(e -> addUser());
        btnDelete.addActionListener(e -> deleteUser());
    }

    private void loadUsers() {
        try {
            tableModel.setRowCount(0);
            Connection conn = DBConnection.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role")
                };
                tableModel.addRow(row);
            }
            conn.close();
        } catch (Exception e) {
            Message.showError(this, "Error loading users: " + e.getMessage());
        }
    }

    private void addUser() {
        String user = txtUser.getText();
        String pass = txtPass.getText();
        String role = cmbRole.getSelectedItem().toString();

        if (user.isEmpty() || pass.isEmpty()) {
            Message.showError(this, "All fields are required!");
            return;
        }

        try {
            Connection conn = DBConnection.connect();
            String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, user);
            pst.setString(2, pass);
            pst.setString(3, role);
            
            pst.executeUpdate();
            conn.close();
            
            Message.showSuccess(this, "User Added Successfully!");
            txtUser.setText("");
            txtPass.setText("");
            loadUsers();

        } catch (Exception e) {
            Message.showError(this, "Error adding user: " + e.getMessage());
        }
    }

    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            Message.showError(this, "Please select a user to delete!");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String role = tableModel.getValueAt(selectedRow, 3).toString();

        // Admin කෙනෙක්ට තව Admin කෙනෙක්ව මකන්න දෙන එක පරිස්සමෙන්
        if (role.equalsIgnoreCase("admin")) {
             if (!Message.showConfirm(this, "Warning: You are deleting an ADMIN account. Continue?")) {
                 return;
             }
        }

        if (Message.showConfirm(this, "Are you sure you want to delete this user?")) {
            try {
                Connection conn = DBConnection.connect();
                PreparedStatement pst = conn.prepareStatement("DELETE FROM users WHERE id=?");
                pst.setInt(1, id);
                pst.executeUpdate();
                conn.close();
                
                Message.showSuccess(this, "User Deleted Successfully!");
                loadUsers();

            } catch (Exception e) {
                Message.showError(this, "Error deleting: " + e.getMessage());
            }
        }
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(Color.LIGHT_GRAY);
        return lbl;
    }

    private JTextField createTextField() {
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.putClientProperty("JComponent.roundRect", true);
        return txt;
    }
}