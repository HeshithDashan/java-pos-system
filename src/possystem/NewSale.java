package possystem;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class NewSale extends JPanel {

    private JTextField txtBarcode, txtQty, txtCashPaid;
    private JLabel lblProductName, lblPrice, lblStock, lblTotal, lblBalance;
    private JComboBox<String> cmbCustomer;
    private JTable table;
    private DefaultTableModel tableModel;
    private HashMap<String, Integer> customerMap = new HashMap<>();
    private int currentProductId = -1;

    public NewSale(ActionListener onBack) {
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
        btnBack.putClientProperty(FlatClientProperties.STYLE, "arc: 20; margin: 5,15,5,15;");
        btnBack.addActionListener(onBack);

        JLabel lblTitle = new JLabel("NEW SALE");
        lblTitle.setFont(new Font("Poppins", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        
        titleContainer.add(btnBack);
        titleContainer.add(lblTitle);
        headerPanel.add(titleContainer, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setPreferredSize(new Dimension(350, 0));
        leftPanel.setBorder(BorderFactory.createTitledBorder(" Add Item "));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0;
        leftPanel.add(new JLabel("Select Customer:"), gbc);
        gbc.gridy = 1;
        cmbCustomer = new JComboBox<>();
        cmbCustomer.setPreferredSize(new Dimension(200, 40));
        leftPanel.add(cmbCustomer, gbc);

        gbc.gridy = 2;
        leftPanel.add(new JLabel("Scan Barcode / ID:"), gbc);
        gbc.gridy = 3;
        txtBarcode = new JTextField();
        txtBarcode.setPreferredSize(new Dimension(200, 40));
        txtBarcode.putClientProperty(FlatClientProperties.STYLE, "arc: 10; borderColor: #ced4da; borderWidth: 2");
        txtBarcode.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Scan & Press Enter");
        leftPanel.add(txtBarcode, gbc);

        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        infoPanel.setBackground(new Color(245, 245, 245));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        lblProductName = new JLabel("Product: -");
        lblProductName.setFont(new Font("Poppins", Font.BOLD, 14));
        lblProductName.setForeground(new Color(0, 123, 255));
        
        lblPrice = new JLabel("Price: Rs. 0.00");
        lblPrice.setFont(new Font("Poppins", Font.BOLD, 14));
        
        lblStock = new JLabel("Stock: 0");
        lblStock.setFont(new Font("Poppins", Font.PLAIN, 13));
        lblStock.setForeground(Color.RED);

        infoPanel.add(lblProductName);
        infoPanel.add(lblPrice);
        infoPanel.add(lblStock);
        
        gbc.gridy = 4;
        leftPanel.add(infoPanel, gbc);

        gbc.gridy = 5;
        leftPanel.add(new JLabel("Quantity:"), gbc);
        gbc.gridy = 6;
        txtQty = new JTextField("1");
        txtQty.setPreferredSize(new Dimension(200, 40));
        txtQty.setHorizontalAlignment(JTextField.CENTER);
        txtQty.putClientProperty(FlatClientProperties.STYLE, "arc: 10; borderColor: #ced4da; borderWidth: 2");
        leftPanel.add(txtQty, gbc);

        gbc.gridy = 7;
        JButton btnAdd = new JButton("Add to Cart");
        btnAdd.setBackground(new Color(40, 167, 69));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("Poppins", Font.BOLD, 16));
        btnAdd.setPreferredSize(new Dimension(200, 50));
        btnAdd.putClientProperty(FlatClientProperties.STYLE, "arc: 15");
        leftPanel.add(btnAdd, gbc);

        contentPanel.add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new BorderLayout(0, 10));
        
        String[] columns = {"ID", "Product Name", "Price", "Qty", "Total"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Poppins", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        rightPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel payPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        payPanel.setBackground(new Color(240, 240, 240));
        payPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        lblTotal = new JLabel("Total: Rs. 0.00");
        lblTotal.setFont(new Font("Poppins", Font.BOLD, 20));
        
        JLabel lblCash = new JLabel("Cash Paid:");
        lblCash.setFont(new Font("Poppins", Font.BOLD, 16));
        lblCash.setHorizontalAlignment(SwingConstants.RIGHT);
        
        txtCashPaid = new JTextField();
        txtCashPaid.setFont(new Font("Poppins", Font.BOLD, 16));
        txtCashPaid.putClientProperty(FlatClientProperties.STYLE, "arc: 10; borderColor: #007bff; borderWidth: 2");

        lblBalance = new JLabel("Balance: Rs. 0.00");
        lblBalance.setFont(new Font("Poppins", Font.BOLD, 18));
        lblBalance.setForeground(new Color(0, 150, 0));

        payPanel.add(lblTotal);
        payPanel.add(new JLabel(""));
        payPanel.add(lblCash);
        payPanel.add(txtCashPaid);
        payPanel.add(lblBalance);

        JButton btnCheckout = new JButton("PAY & PRINT");
        btnCheckout.setBackground(new Color(0, 123, 255));
        btnCheckout.setForeground(Color.WHITE);
        btnCheckout.setFont(new Font("Poppins", Font.BOLD, 18));
        btnCheckout.putClientProperty(FlatClientProperties.STYLE, "arc: 15");
        
        payPanel.add(btnCheckout);
        
        rightPanel.add(payPanel, BorderLayout.SOUTH);
        contentPanel.add(rightPanel, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        loadCustomers();

        txtBarcode.addActionListener(e -> fetchProductDetails());
        btnAdd.addActionListener(e -> addToCart());
        btnCheckout.addActionListener(e -> checkout());
        
        txtCashPaid.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { calculateBalance(); }
            public void removeUpdate(DocumentEvent e) { calculateBalance(); }
            public void changedUpdate(DocumentEvent e) { calculateBalance(); }
        });
    }

    private void loadCustomers() {
        try {
            Connection conn = DBConnection.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, name FROM customers");
            while (rs.next()) {
                String name = rs.getString("name");
                int id = rs.getInt("id");
                cmbCustomer.addItem(name);
                customerMap.put(name, id);
            }
            conn.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void fetchProductDetails() {
        String barcode = txtBarcode.getText().trim();
        if (barcode.isEmpty()) return;

        try {
            Connection conn = DBConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM products WHERE barcode = ? OR id = ?");
            pstmt.setString(1, barcode);
            try { pstmt.setInt(2, Integer.parseInt(barcode)); } catch (Exception e) { pstmt.setInt(2, -1); }

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                currentProductId = rs.getInt("id");
                lblProductName.setText("Product: " + rs.getString("name"));
                lblPrice.setText("Price: Rs. " + rs.getDouble("price"));
                lblStock.setText("Stock: " + rs.getInt("stock"));
                txtQty.requestFocus();
            } else {
                JOptionPane.showMessageDialog(this, "Product not found!");
                currentProductId = -1;
            }
            conn.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void addToCart() {
        if (currentProductId == -1) return;
        try {
            int qty = Integer.parseInt(txtQty.getText());
            int stock = Integer.parseInt(lblStock.getText().replace("Stock: ", ""));
            
            if (qty > stock) {
                JOptionPane.showMessageDialog(this, "Low Stock!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double price = Double.parseDouble(lblPrice.getText().replace("Price: Rs. ", ""));
            double total = price * qty;
            String name = lblProductName.getText().replace("Product: ", "");

            tableModel.addRow(new Object[]{currentProductId, name, price, qty, total});
            updateGrandTotal();
            
            txtBarcode.setText("");
            txtQty.setText("1");
            currentProductId = -1;
            txtBarcode.requestFocus();

        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Invalid Qty"); }
    }

    private void updateGrandTotal() {
        double grandTotal = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) grandTotal += (double) tableModel.getValueAt(i, 4);
        lblTotal.setText("Total: Rs. " + String.format("%.2f", grandTotal));
        calculateBalance();
    }
    
    private void calculateBalance() {
        try {
            double total = Double.parseDouble(lblTotal.getText().replace("Total: Rs. ", ""));
            double cash = Double.parseDouble(txtCashPaid.getText());
            double balance = cash - total;
            lblBalance.setText("Balance: Rs. " + String.format("%.2f", balance));
            
            if(balance < 0) lblBalance.setForeground(Color.RED);
            else lblBalance.setForeground(new Color(0, 150, 0));
            
        } catch (Exception e) {
            lblBalance.setText("Balance: Rs. 0.00");
        }
    }

    private void checkout() {
        if (tableModel.getRowCount() == 0) return;
        
        try {
            double total = Double.parseDouble(lblTotal.getText().replace("Total: Rs. ", ""));
            double cash = txtCashPaid.getText().isEmpty() ? total : Double.parseDouble(txtCashPaid.getText());
            
            if(cash < total) {
                JOptionPane.showMessageDialog(this, "Insufficient Cash!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Connection conn = DBConnection.connect();
            conn.setAutoCommit(false);

            String custName = cmbCustomer.getSelectedItem().toString();
            int custId = customerMap.getOrDefault(custName, 0);
            
            PreparedStatement pstmtSale = conn.prepareStatement(
                "INSERT INTO sales (customer_id, total_price, cash_paid, balance) VALUES (?, ?, ?, ?)", 
                Statement.RETURN_GENERATED_KEYS
            );
            pstmtSale.setInt(1, custId);
            pstmtSale.setDouble(2, total);
            pstmtSale.setDouble(3, cash);
            pstmtSale.setDouble(4, cash - total);
            pstmtSale.executeUpdate();

            ResultSet rsKeys = pstmtSale.getGeneratedKeys();
            int saleId = 0;
            if (rsKeys.next()) saleId = rsKeys.getInt(1);

            PreparedStatement pstmtItem = conn.prepareStatement("INSERT INTO sales_items (sale_id, product_id, quantity, unit_price, subtotal) VALUES (?, ?, ?, ?, ?)");
            PreparedStatement pstmtStock = conn.prepareStatement("UPDATE products SET stock = stock - ? WHERE id = ?");

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                int pid = (int) tableModel.getValueAt(i, 0);
                int qty = (int) tableModel.getValueAt(i, 3);
                double price = (double) tableModel.getValueAt(i, 2);
                double sub = (double) tableModel.getValueAt(i, 4);

                pstmtItem.setInt(1, saleId);
                pstmtItem.setInt(2, pid);
                pstmtItem.setInt(3, qty);
                pstmtItem.setDouble(4, price);
                pstmtItem.setDouble(5, sub);
                pstmtItem.addBatch();

                pstmtStock.setInt(1, qty);
                pstmtStock.setInt(2, pid);
                pstmtStock.addBatch();
            }

            pstmtItem.executeBatch();
            pstmtStock.executeBatch();
            conn.commit();
            conn.close();
            
            printBill(saleId, custName, total, cash, cash - total);

            JOptionPane.showMessageDialog(this, "Sale Completed! Invoice #" + saleId);
            
            tableModel.setRowCount(0);
            txtCashPaid.setText("");
            lblTotal.setText("Total: Rs. 0.00");
            lblBalance.setText("Balance: Rs. 0.00");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void printBill(int invoiceId, String customer, double total, double cash, double balance) {
        try {
            JTextArea bill = new JTextArea();
            bill.setText("******************************************\n");
            bill.append("              SMART POS SYSTEM            \n");
            bill.append("               No 123, Colombo            \n");
            bill.append("              Tel: 077-1234567            \n");
            bill.append("******************************************\n");
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            bill.append(" Date: " + sdf.format(new Date()) + "\n");
            bill.append(" Invoice #: " + invoiceId + "\n");
            bill.append(" Customer: " + customer + "\n");
            bill.append("------------------------------------------\n");
            bill.append(String.format(" %-20s %-5s %-10s\n", "Item", "Qty", "Price"));
            bill.append("------------------------------------------\n");

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String name = (String) tableModel.getValueAt(i, 1);
                int qty = (int) tableModel.getValueAt(i, 3);
                double price = (double) tableModel.getValueAt(i, 2);
                
                if (name.length() > 20) name = name.substring(0, 20);
                
                bill.append(String.format(" %-20s %-5d %-10.2f\n", name, qty, price * qty));
            }

            bill.append("------------------------------------------\n");
            bill.append(String.format(" Total Amount:      Rs. %-10.2f\n", total));
            bill.append(String.format(" Cash Paid:         Rs. %-10.2f\n", cash));
            bill.append(String.format(" Balance:           Rs. %-10.2f\n", balance));
            bill.append("******************************************\n");
            bill.append("           THANK YOU COME AGAIN!          \n");
            bill.append("******************************************\n");

            bill.print(); 

        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(this, "Printing Error: " + e.getMessage());
        }
    }
}