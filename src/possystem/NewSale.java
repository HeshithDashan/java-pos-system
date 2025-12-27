package possystem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class NewSale extends JFrame {

    private JTextField txtProductID, txtQty, txtCash;
    private JLabel lblTotal, lblBalance;
    private JTable cartTable;
    private DefaultTableModel cartModel;
    private double netTotal = 0.0; 

    public NewSale() {
        setTitle("New Sale (Billing)");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        getContentPane().setBackground(new Color(24, 24, 24));
        setLayout(new BorderLayout());

        // --- 1. HEADER ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(33, 150, 243)); 
        headerPanel.setPreferredSize(new Dimension(800, 70));
        headerPanel.setBorder(new EmptyBorder(10, 30, 10, 30));

        JLabel lblTitle = new JLabel("🛒 BILLING TERMINAL");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        JLabel lblDate = new JLabel(java.time.LocalDate.now().toString()); 
        lblDate.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblDate.setForeground(new Color(240, 240, 240));
        headerPanel.add(lblDate, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // --- 2. LEFT PANEL (Cart & Inputs) ---
        JPanel leftPanel = new JPanel(new BorderLayout(20, 20));
        leftPanel.setBackground(new Color(24, 24, 24));
        leftPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Input Bar
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        inputPanel.setBackground(new Color(45, 45, 45)); 
        inputPanel.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 1));
        inputPanel.setPreferredSize(new Dimension(0, 80));

        inputPanel.add(createLabel("Product ID / Name:"));
        txtProductID = createTextField(20);
        inputPanel.add(txtProductID);

        inputPanel.add(createLabel("Qty:"));
        txtQty = createTextField(5);
        inputPanel.add(txtQty);

        JButton btnAdd = new JButton("ADD ITEM");
        btnAdd.setBackground(new Color(46, 204, 113)); 
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAdd.setPreferredSize(new Dimension(120, 40));
        btnAdd.setFocusPainted(false);
        btnAdd.putClientProperty("JButton.buttonType", "roundRect");
        inputPanel.add(btnAdd);

        leftPanel.add(inputPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Product Name", "Price", "Qty", "Total"};
        cartModel = new DefaultTableModel(columns, 0);
        cartTable = new JTable(cartModel);
        cartTable.setRowHeight(40); 
        cartTable.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        cartTable.setShowVerticalLines(false); 
        cartTable.setIntercellSpacing(new Dimension(0, 0));
        
        // Table Header
        JTableHeader header = cartTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setBackground(new Color(60, 60, 60));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 40));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        cartTable.setDefaultRenderer(Object.class, centerRenderer);

        JScrollPane scrollPane = new JScrollPane(cartTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        add(leftPanel, BorderLayout.CENTER);

        // --- 3. RIGHT PANEL (Payment) ---
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(400, 0));
        rightPanel.setBackground(new Color(30, 30, 30));
        rightPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(60, 60, 60)));

        // Total Display
        JPanel totalDisplayPanel = new JPanel(new GridLayout(2, 1));
        totalDisplayPanel.setBackground(new Color(30, 30, 30));
        totalDisplayPanel.setBorder(new EmptyBorder(30, 20, 30, 20));

        JLabel lblNetTotalTitle = new JLabel("NET TOTAL", SwingConstants.RIGHT);
        lblNetTotalTitle.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblNetTotalTitle.setForeground(Color.GRAY);
        totalDisplayPanel.add(lblNetTotalTitle);

        lblTotal = new JLabel("Rs. 0.00", SwingConstants.RIGHT);
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 48)); 
        lblTotal.setForeground(new Color(46, 204, 113)); 
        totalDisplayPanel.add(lblTotal);

        rightPanel.add(totalDisplayPanel, BorderLayout.NORTH);

        // Payment Inputs
        JPanel paymentInputPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        paymentInputPanel.setBackground(new Color(30, 30, 30));
        paymentInputPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        paymentInputPanel.add(createLabel("Cash Received (Rs):"));
        txtCash = createTextField(10);
        txtCash.setFont(new Font("Segoe UI", Font.BOLD, 24)); 
        txtCash.setHorizontalAlignment(JTextField.RIGHT);
        txtCash.setForeground(new Color(255, 193, 7)); 
        paymentInputPanel.add(txtCash);

        paymentInputPanel.add(createLabel("Balance (Rs):"));
        lblBalance = new JLabel("0.00", SwingConstants.RIGHT);
        lblBalance.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblBalance.setForeground(new Color(231, 76, 60)); 
        paymentInputPanel.add(lblBalance);

        rightPanel.add(paymentInputPanel, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        btnPanel.setBackground(new Color(30, 30, 30));
        btnPanel.setBorder(new EmptyBorder(20, 30, 30, 30));

        JButton btnRemove = new JButton("REMOVE ITEM");
        btnRemove.setBackground(new Color(231, 76, 60));
        btnRemove.setForeground(Color.WHITE);
        btnRemove.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnRemove.setPreferredSize(new Dimension(0, 50));
        btnRemove.putClientProperty("JButton.buttonType", "roundRect");
        btnPanel.add(btnRemove);

        JButton btnPay = new JButton("PAY & PRINT");
        btnPay.setBackground(new Color(33, 150, 243));
        btnPay.setForeground(Color.WHITE);
        btnPay.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnPay.setPreferredSize(new Dimension(0, 60));
        btnPay.putClientProperty("JButton.buttonType", "roundRect");
        btnPanel.add(btnPay);

        rightPanel.add(btnPanel, BorderLayout.SOUTH);

        add(rightPanel, BorderLayout.EAST);

        // 🔥 --- FUNCTIONALITY --- 🔥

        // 1. ADD Item Logic
        btnAdd.addActionListener(e -> addItemToCart());

        // 2. Remove Item Logic
        btnRemove.addActionListener(e -> {
            int selectedRow = cartTable.getSelectedRow();
            if (selectedRow != -1) {
                cartModel.removeRow(selectedRow);
                updateNetTotal(); 
                calculateBalance(); // බඩු අයින් කරාමත් Balance එක වෙනස් වෙනවා
            } else {
                Message.showError(this, "Please select an item to remove!");
            }
        });

        // 3. Shortcuts (Enter Key)
        txtProductID.addActionListener(e -> txtQty.requestFocus()); 
        txtQty.addActionListener(e -> addItemToCart()); 

        // 4. 🔥 Auto Balance Calculation (සල්ලි ගහනකොටම Balance එක හැදෙනවා)
        txtCash.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                calculateBalance();
            }
        });

        // 5. 🔥 PAY Logic (Stock Update)
        btnPay.addActionListener(e -> payAndPrint());
    }

    // --- LOGIC METHODS ---

    private void addItemToCart() {
        String productId = txtProductID.getText();
        String qtyStr = txtQty.getText();

        if (productId.isEmpty() || qtyStr.isEmpty()) {
            Message.showError(this, "Please enter Product ID/Name and Quantity!");
            return;
        }

        try {
            Connection conn = DBConnection.connect();
            String sql = "SELECT * FROM products WHERE id = ? OR name = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            
            try {
                int id = Integer.parseInt(productId);
                pst.setInt(1, id);
                pst.setString(2, ""); 
            } catch (NumberFormatException e) {
                pst.setInt(1, 0); 
                pst.setString(2, productId); 
            }

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int stock = rs.getInt("stock");
                int qty = Integer.parseInt(qtyStr);

                if (stock >= qty) {
                    double lineTotal = price * qty;
                    Object[] row = { rs.getInt("id"), name, price, qty, lineTotal };
                    cartModel.addRow(row);
                    updateNetTotal(); 
                    
                    txtProductID.setText("");
                    txtQty.setText("");
                    txtProductID.requestFocus(); 
                } else {
                    Message.showError(this, "Not enough stock! Available: " + stock);
                }
            } else {
                Message.showError(this, "Product not found!");
            }
            conn.close();
        } catch (Exception e) {
            Message.showError(this, "Error: " + e.getMessage());
        }
    }

    // Total එක හදන මැෂින් එක
    private void updateNetTotal() {
        netTotal = 0.0;
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            netTotal += (double) cartModel.getValueAt(i, 4); 
        }
        lblTotal.setText(String.format("Rs. %.2f", netTotal));
        calculateBalance(); // Total වෙනස් වුනාම Balance එකත් වෙනස් වෙනවා
    }

    // 🔥 Balance එක හදන මැෂින් එක
    private void calculateBalance() {
        try {
            double cash = Double.parseDouble(txtCash.getText());
            double balance = cash - netTotal;
            lblBalance.setText(String.format("%.2f", balance));
            
            if (balance < 0) {
                lblBalance.setForeground(new Color(231, 76, 60)); // මදි නම් රතු පාට
            } else {
                lblBalance.setForeground(new Color(46, 204, 113)); // ඉතුරු නම් කොළ පාට
            }
        } catch (NumberFormatException e) {
            lblBalance.setText("0.00");
        }
    }

    // 🔥 සල්ලි ගෙවලා බඩු අඩු කරන එක
    private void payAndPrint() {
        if (cartModel.getRowCount() == 0) {
            Message.showError(this, "Cart is empty!");
            return;
        }

        try {
            double cash = Double.parseDouble(txtCash.getText());
            if (cash < netTotal) {
                Message.showError(this, "Insufficient Cash!");
                return;
            }

            // Database Stock Update Loop
            Connection conn = DBConnection.connect();
            String updateSql = "UPDATE products SET stock = stock - ? WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(updateSql);

            for (int i = 0; i < cartModel.getRowCount(); i++) {
                int id = (int) cartModel.getValueAt(i, 0); // ID
                int qty = (int) cartModel.getValueAt(i, 3); // Qty

                pst.setInt(1, qty);
                pst.setInt(2, id);
                pst.executeUpdate(); // Update Stock
            }
            
            conn.close();

            // Success Message
            Message.showSuccess(this, "Bill Paid & Printed Successfully! ✅");
            
            // Clear Everything
            cartModel.setRowCount(0);
            updateNetTotal();
            txtCash.setText("");
            lblBalance.setText("0.00");

        } catch (NumberFormatException e) {
            Message.showError(this, "Please enter valid cash amount!");
        } catch (Exception e) {
            Message.showError(this, "Error processing sale: " + e.getMessage());
        }
    }

    // UI Helpers
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lbl.setForeground(Color.LIGHT_GRAY);
        return lbl;
    }

    private JTextField createTextField(int columns) {
        JTextField txt = new JTextField(columns);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txt.putClientProperty("JComponent.roundRect", true);
        return txt;
    }
}