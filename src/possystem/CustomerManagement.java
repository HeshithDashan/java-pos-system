package possystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class CustomerManagement extends JFrame {

    private JTextField txtName, txtPhone, txtEmail;
    private JTable customerTable;
    private DefaultTableModel tableModel;

    public CustomerManagement() {
        setTitle("Manage Customers");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Header ---
        JLabel lblHeader = new JLabel("👥 CUSTOMER MANAGEMENT", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblHeader.setForeground(new Color(33, 150, 243));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(lblHeader, BorderLayout.NORTH);

        // --- Input Panel ---
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add New Customer"));
        inputPanel.setPreferredSize(new Dimension(300, 0));

        inputPanel.add(new JLabel("Customer Name:"));
        txtName = new JTextField();
        inputPanel.add(txtName);

        inputPanel.add(new JLabel("Phone Number:"));
        txtPhone = new JTextField();
        inputPanel.add(txtPhone);

        inputPanel.add(new JLabel("Email (Optional):"));
        txtEmail = new JTextField();
        inputPanel.add(txtEmail);

        JButton btnAdd = new JButton("Add Customer");
        btnAdd.setBackground(new Color(46, 204, 113));
        btnAdd.setForeground(Color.WHITE);
        
        JButton btnDelete = new JButton("Delete Customer");
        btnDelete.setBackground(new Color(231, 76, 60));
        btnDelete.setForeground(Color.WHITE);

        inputPanel.add(btnAdd);
        inputPanel.add(btnDelete);

        JPanel westPanel = new JPanel(new BorderLayout());
        westPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        westPanel.add(inputPanel, BorderLayout.NORTH);
        add(westPanel, BorderLayout.WEST);

        // --- Table ---
        String[] columns = {"ID", "Name", "Phone", "Email"};
        tableModel = new DefaultTableModel(columns, 0);
        customerTable = new JTable(tableModel);
        customerTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        loadCustomers();

        // --- Actions ---
        btnAdd.addActionListener(e -> addCustomer());
        btnDelete.addActionListener(e -> deleteCustomer());
    }

    private void loadCustomers() {
        try {
            tableModel.setRowCount(0);
            Connection conn = DBConnection.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM customers");

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("email")
                };
                tableModel.addRow(row);
            }
            conn.close();
        } catch (Exception e) {
            Message.showError(this, "Error loading customers: " + e.getMessage());
        }
    }

    private void addCustomer() {
        String name = txtName.getText();
        String phone = txtPhone.getText();
        String email = txtEmail.getText();

        if (name.isEmpty() || phone.isEmpty()) {
            Message.showError(this, "Name and Phone are required!");
            return;
        }

        try {
            Connection conn = DBConnection.connect();
            String sql = "INSERT INTO customers (name, phone, email) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, name);
            pst.setString(2, phone);
            pst.setString(3, email);
            
            pst.executeUpdate();
            conn.close();
            
            Message.showSuccess(this, "Customer Added Successfully!");
            
            txtName.setText("");
            txtPhone.setText("");
            txtEmail.setText("");
            
            loadCustomers();

        } catch (Exception e) {
            Message.showError(this, "Error adding customer: " + e.getMessage());
        }
    }

    private void deleteCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            Message.showError(this, "Please select a customer to delete!");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);

        if (Message.showConfirm(this, "Are you sure you want to delete this customer?")) {
            try {
                Connection conn = DBConnection.connect();
                PreparedStatement pst = conn.prepareStatement("DELETE FROM customers WHERE id=?");
                pst.setInt(1, id);
                pst.executeUpdate();
                conn.close();
                
                Message.showSuccess(this, "Customer Deleted Successfully!");
                loadCustomers();

            } catch (Exception e) {
                Message.showError(this, "Error deleting: " + e.getMessage());
            }
        }
    }
}