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

public class ProductManagement extends JPanel {

    private JTextField txtName;
    private JComboBox<String> cmbCategory;
    private JTextField txtPrice;
    private JTextField txtStock;
    private JTextField txtBarcode;
    private JTable table;
    private DefaultTableModel tableModel;
    private int selectedProductId = -1;

    public ProductManagement(ActionListener onBack) {
        setLayout(new BorderLayout());

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

        JLabel lblTitle = new JLabel("PRODUCT MANAGEMENT");
        lblTitle.setFont(new Font("Poppins", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        
        try {
            URL iconURL = getClass().getResource("/icons/product.png");
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

        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(" Manage Product Details "));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Barcode:"), gbc);
        gbc.gridy = 1;
        txtBarcode = new JTextField();
        txtBarcode.setPreferredSize(new Dimension(200, 35));
        txtBarcode.putClientProperty(FlatClientProperties.STYLE, "arc: 10; borderColor: #ced4da; borderWidth: 2");
        txtBarcode.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Scan or Type Barcode");
        formPanel.add(txtBarcode, gbc);

        gbc.gridy = 2;
        formPanel.add(new JLabel("Product Name:"), gbc);
        gbc.gridy = 3;
        txtName = new JTextField();
        txtName.setPreferredSize(new Dimension(200, 35));
        txtName.putClientProperty(FlatClientProperties.STYLE, "arc: 10; borderColor: #ced4da; borderWidth: 2");
        formPanel.add(txtName, gbc);

        gbc.gridy = 4;
        formPanel.add(new JLabel("Category:"), gbc);
        gbc.gridy = 5;
        cmbCategory = new JComboBox<>(new String[]{"General", "Beverages", "Snacks", "Medicine", "Electronics"});
        cmbCategory.setPreferredSize(new Dimension(200, 35));
        formPanel.add(cmbCategory, gbc);

        gbc.gridy = 6;
        formPanel.add(new JLabel("Price (Rs.):"), gbc);
        gbc.gridy = 7;
        txtPrice = new JTextField();
        txtPrice.setPreferredSize(new Dimension(200, 35));
        txtPrice.putClientProperty(FlatClientProperties.STYLE, "arc: 10; borderColor: #ced4da; borderWidth: 2");
        formPanel.add(txtPrice, gbc);

        gbc.gridy = 8;
        formPanel.add(new JLabel("Stock Quantity:"), gbc);
        gbc.gridy = 9;
        txtStock = new JTextField();
        txtStock.setPreferredSize(new Dimension(200, 35));
        txtStock.putClientProperty(FlatClientProperties.STYLE, "arc: 10; borderColor: #ced4da; borderWidth: 2");
        formPanel.add(txtStock, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton btnAdd = createButton("Add", new Color(40, 167, 69)); 
        JButton btnUpdate = createButton("Update", new Color(255, 193, 7)); 
        JButton btnDelete = createButton("Delete", new Color(220, 53, 69)); 
        JButton btnClear = createButton("Clear", new Color(108, 117, 125)); 

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        gbc.gridy = 10;
        gbc.insets = new Insets(20, 10, 10, 10);
        formPanel.add(btnPanel, gbc);

        contentPanel.add(formPanel);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(" Product Inventory "));

        String[] columns = {"ID", "Barcode", "Name", "Category", "Price", "Stock"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Poppins", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        
        table.getColumnModel().getColumn(0).setPreferredWidth(30); 
        table.getColumnModel().getColumn(2).setPreferredWidth(150); 
        
        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        contentPanel.add(tablePanel);
        add(contentPanel, BorderLayout.CENTER);

        
        btnAdd.addActionListener(e -> {
            if(txtName.getText().isEmpty() || txtPrice.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and Price are required!");
                return;
            }
            if (addProduct()) {
                loadProducts();
                clearForm();
                JOptionPane.showMessageDialog(this, "Product Added!");
            }
        });

        btnUpdate.addActionListener(e -> {
            if(selectedProductId == -1) return;
             if (updateProduct()) {
                loadProducts();
                clearForm();
                JOptionPane.showMessageDialog(this, "Product Updated!");
            }
        });

        btnDelete.addActionListener(e -> {
             if(selectedProductId == -1) return;
             if(JOptionPane.showConfirmDialog(this, "Delete this product?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                 if(deleteProduct()) {
                     loadProducts();
                     clearForm();
                     JOptionPane.showMessageDialog(this, "Product Deleted!");
                 }
             }
        });

        btnClear.addActionListener(e -> clearForm());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    selectedProductId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                    txtBarcode.setText(tableModel.getValueAt(row, 1) != null ? tableModel.getValueAt(row, 1).toString() : "");
                    txtName.setText(tableModel.getValueAt(row, 2).toString());
                    cmbCategory.setSelectedItem(tableModel.getValueAt(row, 3).toString());
                    txtPrice.setText(tableModel.getValueAt(row, 4).toString());
                    txtStock.setText(tableModel.getValueAt(row, 5).toString());
                }
            }
        });

        loadProducts(); 
    }
        
    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Poppins", Font.BOLD, 12));
        btn.putClientProperty(FlatClientProperties.STYLE, "arc: 10");
        return btn;
    }

    private void clearForm() {
        txtBarcode.setText("");
        txtName.setText("");
        txtPrice.setText("");
        txtStock.setText("");
        cmbCategory.setSelectedIndex(0);
        selectedProductId = -1;
        table.clearSelection();
    }

    private void loadProducts() {
        tableModel.setRowCount(0);
        try {
            Connection conn = DBConnection.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM products");
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("barcode"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getDouble("price"),
                    rs.getInt("stock")
                });
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean addProduct() {
        try {
            Connection conn = DBConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO products (name, category, price, stock, barcode) VALUES (?, ?, ?, ?, ?)");
            pstmt.setString(1, txtName.getText());
            pstmt.setString(2, cmbCategory.getSelectedItem().toString());
            pstmt.setDouble(3, Double.parseDouble(txtPrice.getText()));
            pstmt.setInt(4, txtStock.getText().isEmpty() ? 0 : Integer.parseInt(txtStock.getText()));
            pstmt.setString(5, txtBarcode.getText());
            pstmt.executeUpdate();
            conn.close();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            return false;
        }
    }
    
    private boolean updateProduct() {
        try {
            Connection conn = DBConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement("UPDATE products SET name=?, category=?, price=?, stock=?, barcode=? WHERE id=?");
            pstmt.setString(1, txtName.getText());
            pstmt.setString(2, cmbCategory.getSelectedItem().toString());
            pstmt.setDouble(3, Double.parseDouble(txtPrice.getText()));
            pstmt.setInt(4, Integer.parseInt(txtStock.getText()));
            pstmt.setString(5, txtBarcode.getText());
            pstmt.setInt(6, selectedProductId);
            pstmt.executeUpdate();
            conn.close();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            return false;
        }
    }
    
    private boolean deleteProduct() {
        try {
            Connection conn = DBConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM products WHERE id=?");
            pstmt.setInt(1, selectedProductId);
            pstmt.executeUpdate();
            conn.close();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            return false;
        }
    }
}