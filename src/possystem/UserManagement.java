package possystem;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.*;

public class UserManagement extends JPanel {

    private JTextField txtUsername;
    private JPasswordField txtPassword; // Password field for security
    private JComboBox<String> cmbRole;
    private JTable table;
    private DefaultTableModel tableModel;
    private int selectedUserId = -1; // To track selection for Update/Delete

    public UserManagement(ActionListener onBack) {
        setLayout(new BorderLayout());

        // --- 1. HEADER (Same as Settings) ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 123, 255)); // Primary Blue
        headerPanel.setPreferredSize(new Dimension(800, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel titleContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        titleContainer.setOpaque(false);

        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Poppins", Font.BOLD, 15));
        btnBack.setForeground(new Color(0, 123, 255));
        btnBack.setBackground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setBorderPainted(false);
        btnBack.putClientProperty(FlatClientProperties.STYLE, "arc: 20; margin: 5,15,5,15;");
        btnBack.addActionListener(onBack);

        JLabel lblTitle = new JLabel("USER MANAGEMENT");
        lblTitle.setFont(new Font("Poppins", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        
        // Icon (Optional)
        try {
            URL iconURL = getClass().getResource("/icons/user.png"); // Use user icon if available
            if (iconURL != null) {
                ImageIcon icon = new ImageIcon(iconURL);
                Image img = icon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
                lblTitle.setIcon(new ImageIcon(img));
                lblTitle.setIconTextGap(15);
            }
        } catch (Exception e) {}

        titleContainer.add(btnBack);
        titleContainer.add(lblTitle);
        headerPanel.add(titleContainer, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // --- 2. MAIN CONTENT (Split: Form & Table) ---
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // --- LEFT SIDE: FORM ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(" Manage User Details "));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridy = 1;
        txtUsername = new JTextField();
        txtUsername.setPreferredSize(new Dimension(200, 40));
        txtUsername.putClientProperty(FlatClientProperties.STYLE, "arc: 10; borderColor: #ced4da; borderWidth: 2");
        formPanel.add(txtUsername, gbc);

        // Password
        gbc.gridy = 2;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridy = 3;
        txtPassword = new JPasswordField(); // Changed to PasswordField
        txtPassword.setPreferredSize(new Dimension(200, 40));
        txtPassword.putClientProperty(FlatClientProperties.STYLE, "arc: 10; showRevealButton: true; borderColor: #ced4da; borderWidth: 2");
        formPanel.add(txtPassword, gbc);

        // Role
        gbc.gridy = 4;
        formPanel.add(new JLabel("Role:"), gbc);
        gbc.gridy = 5;
        cmbRole = new JComboBox<>(new String[]{"Cashier", "Admin"});
        cmbRole.setPreferredSize(new Dimension(200, 40));
        formPanel.add(cmbRole, gbc);

        // Buttons Panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        JButton btnAdd = createButton("Add", new Color(40, 167, 69)); // Green
        JButton btnUpdate = createButton("Update", new Color(255, 193, 7)); // Yellow
        JButton btnDelete = createButton("Delete", new Color(220, 53, 69)); // Red
        JButton btnClear = createButton("Clear", new Color(108, 117, 125)); // Gray

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        gbc.gridy = 6;
        gbc.insets = new Insets(30, 10, 10, 10);
        formPanel.add(btnPanel, gbc);

        contentPanel.add(formPanel);

        // --- RIGHT SIDE: TABLE ---
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(" User List "));

        String[] columns = {"ID", "Username", "Role", "Password"}; // Password column added for visibility (optional)
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Poppins", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        
        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        contentPanel.add(tablePanel);
        add(contentPanel, BorderLayout.CENTER);

        // --- ACTIONS ---
        
        // Add Button
        btnAdd.addActionListener(e -> {
            String user = txtUsername.getText();
            String pass = new String(txtPassword.getPassword());
            String role = cmbRole.getSelectedItem().toString();
            
            if(user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
                return;
            }
            
            if (addUser(user, pass, role)) {
                loadUsers();
                clearForm();
                JOptionPane.showMessageDialog(this, "User Added!");
            }
        });

        // Update Button
        btnUpdate.addActionListener(e -> {
            if(selectedUserId == -1) {
                JOptionPane.showMessageDialog(this, "Please select a user to update!");
                return;
            }
             if (updateUser(selectedUserId, txtUsername.getText(), new String(txtPassword.getPassword()), cmbRole.getSelectedItem().toString())) {
                loadUsers();
                clearForm();
                JOptionPane.showMessageDialog(this, "User Updated!");
            }
        });

        // Delete Button
        btnDelete.addActionListener(e -> {
             if(selectedUserId == -1) {
                JOptionPane.showMessageDialog(this, "Please select a user to delete!");
                return;
            }
             if(JOptionPane.showConfirmDialog(this, "Are you sure?", "Delete", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                 if(deleteUser(selectedUserId)) {
                     loadUsers();
                     clearForm();
                     JOptionPane.showMessageDialog(this, "User Deleted!");
                 }
             }
        });

        // Clear Button
        btnClear.addActionListener(e -> clearForm());

        // Table Row Click
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    selectedUserId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                    txtUsername.setText(tableModel.getValueAt(row, 1).toString());
                    cmbRole.setSelectedItem(tableModel.getValueAt(row, 2).toString());
                    txtPassword.setText(tableModel.getValueAt(row, 3).toString());
                }
            }
        });

        loadUsers(); // Load data on start
    }
    
    // --- HELPER METHODS ---
    
    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Poppins", Font.BOLD, 12));
        btn.putClientProperty(FlatClientProperties.STYLE, "arc: 10");
        return btn;
    }

    private void clearForm() {
        txtUsername.setText("");
        txtPassword.setText("");
        cmbRole.setSelectedIndex(0);
        selectedUserId = -1;
        table.clearSelection();
    }

    // --- DATABASE OPERATIONS ---

    private void loadUsers() {
        tableModel.setRowCount(0);
        try {
            Connection conn = DBConnection.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("role"),
                    rs.getString("password")
                });
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean addUser(String user, String pass, String role) {
        try {
            Connection conn = DBConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO users (username, password, role) VALUES (?, ?, ?)");
            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            pstmt.setString(3, role);
            pstmt.executeUpdate();
            conn.close();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            return false;
        }
    }
    
    private boolean updateUser(int id, String user, String pass, String role) {
        try {
            Connection conn = DBConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement("UPDATE users SET username=?, password=?, role=? WHERE id=?");
            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            pstmt.setString(3, role);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
            conn.close();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            return false;
        }
    }
    
    private boolean deleteUser(int id) {
        try {
            Connection conn = DBConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM users WHERE id=?");
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            conn.close();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            return false;
        }
    }
}