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

public class CustomerManagement extends JPanel {

    private JTextField txtName;
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JTable table;
    private DefaultTableModel tableModel;
    private int selectedCustomerId = -1;

    public CustomerManagement(ActionListener onBack) {
        setLayout(new BorderLayout());

        // --- 1. HEADER ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 123, 255));
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

        JLabel lblTitle = new JLabel("CUSTOMER MANAGEMENT");
        lblTitle.setFont(new Font("Poppins", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        
        try {
            URL iconURL = getClass().getResource("/icons/customer.png");
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

        // --- 2. MAIN CONTENT ---
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // --- LEFT: FORM ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(" Manage Customer Details "));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Customer Name:"), gbc);
        gbc.gridy = 1;
        txtName = new JTextField();
        txtName.setPreferredSize(new Dimension(200, 40));
        txtName.putClientProperty(FlatClientProperties.STYLE, "arc: 10; borderColor: #ced4da; borderWidth: 2");
        formPanel.add(txtName, gbc);

        // Phone
        gbc.gridy = 2;
        formPanel.add(new JLabel("Phone Number:"), gbc);
        gbc.gridy = 3;
        txtPhone = new JTextField();
        txtPhone.setPreferredSize(new Dimension(200, 40));
        txtPhone.putClientProperty(FlatClientProperties.STYLE, "arc: 10; borderColor: #ced4da; borderWidth: 2");
        formPanel.add(txtPhone, gbc);

        // Email
        gbc.gridy = 4;
        formPanel.add(new JLabel("Email Address:"), gbc);
        gbc.gridy = 5;
        txtEmail = new JTextField();
        txtEmail.setPreferredSize(new Dimension(200, 40));
        txtEmail.putClientProperty(FlatClientProperties.STYLE, "arc: 10; borderColor: #ced4da; borderWidth: 2");
        formPanel.add(txtEmail, gbc);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton btnAdd = createButton("Add", new Color(40, 167, 69)); 
        JButton btnUpdate = createButton("Update", new Color(255, 193, 7)); 
        JButton btnDelete = createButton("Delete", new Color(220, 53, 69)); 
        JButton btnClear = createButton("Clear", new Color(108, 117, 125)); 

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        gbc.gridy = 6;
        gbc.insets = new Insets(30, 10, 10, 10);
        formPanel.add(btnPanel, gbc);

        contentPanel.add(formPanel);

        // --- RIGHT: TABLE ---
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(" Customer List "));

        String[] columns = {"ID", "Name", "Phone", "Email", "Points"};
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
        
        btnAdd.addActionListener(e -> {
            if(txtName.getText().isEmpty() || txtPhone.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and Phone are required!");
                return;
            }
            if (addCustomer(txtName.getText(), txtPhone.getText(), txtEmail.getText())) {
                loadCustomers();
                clearForm();
                JOptionPane.showMessageDialog(this, "Customer Added!");
            }
        });

        btnUpdate.addActionListener(e -> {
            if(selectedCustomerId == -1) return;
             if (updateCustomer(selectedCustomerId, txtName.getText(), txtPhone.getText(), txtEmail.getText())) {
                loadCustomers();
                clearForm();
                JOptionPane.showMessageDialog(this, "Customer Updated!");
            }
        });

        btnDelete.addActionListener(e -> {
             if(selectedCustomerId == -1) return;
             if(JOptionPane.showConfirmDialog(this, "Delete this customer?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                 if(deleteCustomer(selectedCustomerId)) {
                     loadCustomers();
                     clearForm();
                     JOptionPane.showMessageDialog(this, "Customer Deleted!");
                 }
             }
        });

        btnClear.addActionListener(e -> clearForm());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    selectedCustomerId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                    txtName.setText(tableModel.getValueAt(row, 1).toString());
                    txtPhone.setText(tableModel.getValueAt(row, 2).toString());
                    txtEmail.setText(tableModel.getValueAt(row, 3).toString());
                }
            }
        });

        loadCustomers(); 
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
        txtName.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        selectedCustomerId = -1;
        table.clearSelection();
    }

    // --- DATABASE ---

    private void loadCustomers() {
        tableModel.setRowCount(0);
        try {
            Connection conn = DBConnection.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM customers");
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getInt("points")
                });
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean addCustomer(String name, String phone, String email) {
        try {
            Connection conn = DBConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO customers (name, phone, email, points) VALUES (?, ?, ?, 0)");
            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            pstmt.setString(3, email);
            pstmt.executeUpdate();
            conn.close();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            return false;
        }
    }
    
    private boolean updateCustomer(int id, String name, String phone, String email) {
        try {
            Connection conn = DBConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement("UPDATE customers SET name=?, phone=?, email=? WHERE id=?");
            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            pstmt.setString(3, email);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
            conn.close();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            return false;
        }
    }
    
    private boolean deleteCustomer(int id) {
        try {
            Connection conn = DBConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM customers WHERE id=?");
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