package possystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ProductManagement extends JFrame {

    private JTextField txtName, txtPrice, txtStock;
    private JTable productTable;
    private DefaultTableModel tableModel;

    public ProductManagement() {
        setTitle("Manage Products");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel lblHeader = new JLabel("PRODUCT INVENTORY", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblHeader.setForeground(new Color(33, 150, 243));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(lblHeader, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add / Edit Product"));
        inputPanel.setPreferredSize(new Dimension(300, 0));

        inputPanel.add(new JLabel("Product Name:"));
        txtName = new JTextField();
        inputPanel.add(txtName);

        inputPanel.add(new JLabel("Price (Rs):"));
        txtPrice = new JTextField();
        inputPanel.add(txtPrice);

        inputPanel.add(new JLabel("Stock Qty:"));
        txtStock = new JTextField();
        inputPanel.add(txtStock);

        JButton btnAdd = new JButton("Add");
        btnAdd.setBackground(new Color(46, 204, 113));
        btnAdd.setForeground(Color.WHITE);
        
        JButton btnDelete = new JButton("Delete");
        btnDelete.setBackground(new Color(231, 76, 60));
        btnDelete.setForeground(Color.WHITE);

        inputPanel.add(btnAdd);
        inputPanel.add(btnDelete);

        JPanel westPanel = new JPanel(new BorderLayout());
        westPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        westPanel.add(inputPanel, BorderLayout.NORTH);
        add(westPanel, BorderLayout.WEST);

        String[] columns = {"ID", "Name", "Price", "Stock"};
        tableModel = new DefaultTableModel(columns, 0);
        productTable = new JTable(tableModel);
        productTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        loadProducts();

        btnAdd.addActionListener(e -> addProduct());
        btnDelete.addActionListener(e -> deleteProduct());
    }

    private void loadProducts() {
        try {
            tableModel.setRowCount(0);
            Connection conn = DBConnection.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM products");

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("stock")
                };
                tableModel.addRow(row);
            }
            conn.close();
        } catch (Exception e) {
            Message.showError(this, "Error loading data: " + e.getMessage());
        }
    }

    private void addProduct() {
        String name = txtName.getText();
        String price = txtPrice.getText();
        String stock = txtStock.getText();

        if (name.isEmpty() || price.isEmpty() || stock.isEmpty()) {
            Message.showError(this, "Please fill all fields!");
            return;
        }

        try {
            Connection conn = DBConnection.connect();
            String sql = "INSERT INTO products (name, price, stock) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, name);
            pst.setString(2, price);
            pst.setString(3, stock);
            
            pst.executeUpdate();
            conn.close();
            
            Message.showSuccess(this, "Product Added Successfully!");
            
            txtName.setText("");
            txtPrice.setText("");
            txtStock.setText("");
            
            loadProducts();

        } catch (Exception e) {
            Message.showError(this, "Error adding product: " + e.getMessage());
        }
    }

    private void deleteProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            Message.showError(this, "Please select a product to delete!");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);

        if (Message.showConfirm(this, "Are you sure you want to delete this product?")) {
            try {
                Connection conn = DBConnection.connect();
                PreparedStatement pst = conn.prepareStatement("DELETE FROM products WHERE id=?");
                pst.setInt(1, id);
                pst.executeUpdate();
                conn.close();
                
                Message.showSuccess(this, "Deleted Successfully!");
                loadProducts();

            } catch (Exception e) {
                Message.showError(this, "Error deleting: " + e.getMessage());
            }
        }
    }
}