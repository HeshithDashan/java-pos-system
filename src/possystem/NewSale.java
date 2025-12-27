package possystem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class NewSale extends JFrame {

    private JTextField txtProductID, txtQty, txtCash;
    private JLabel lblTotal, lblBalance;
    private JTable cartTable;
    private DefaultTableModel cartModel;
    private double netTotal = 0.0; // මුළු එකතුව මතක තියාගන්න

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

        // Date Display
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

        // 🔥 --- FUNCTIONALITY (මෙතන තමයි වැඩ කෑලි) --- 🔥

        // 1. ADD Button Action
        btnAdd.addActionListener(e -> addItemToCart());

        // 2. Remove Button Action
        btnRemove.addActionListener(e -> {
            int selectedRow = cartTable.getSelectedRow();
            if (selectedRow != -1) {
                cartModel.removeRow(selectedRow);
                updateNetTotal(); // ගාන අඩු වෙන්න ඕනේ
            } else {
                Message.showError(this, "Please select an item to remove!");
            }
        });

        // 3. Enter ගැහුවම වැඩ කරන්න (Shortcuts)
        txtProductID.addActionListener(e -> txtQty.requestFocus()); // ID ගහලා Enter ගැහුවම Qty එකට යන්න
        txtQty.addActionListener(e -> addItemToCart()); // Qty ගහලා Enter ගැහුවම Cart එකට යන්න
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
            // ID එකෙන් හරි Name එකෙන් හරි Search කරනවා
            String sql = "SELECT * FROM products WHERE id = ? OR name = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            
            // ID එක නම් අංකයක් ද කියලා බලනවා, නැත්නම් නම කියලා හිතනවා
            try {
                int id = Integer.parseInt(productId);
                pst.setInt(1, id);
                pst.setString(2, ""); // නම හිස්ව තියනවා (ID මැච් වුනොත් ඇති)
            } catch (NumberFormatException e) {
                pst.setInt(1, 0); // ID එක 0 කරනවා
                pst.setString(2, productId); // නම දානවා
            }

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                // බඩු හම්බුනා!
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int stock = rs.getInt("stock");
                int qty = Integer.parseInt(qtyStr);

                // Stock තියෙනවද බලනවා
                if (stock >= qty) {
                    double lineTotal = price * qty;

                    // Table එකට දානවා
                    Object[] row = {
                        rs.getInt("id"),
                        name,
                        price,
                        qty,
                        lineTotal
                    };
                    cartModel.addRow(row);

                    updateNetTotal(); // Total එක Update කරනවා
                    
                    // Inputs Clear කරනවා ඊළඟ බඩුව ගහන්න ලේසි වෙන්න
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
            netTotal += (double) cartModel.getValueAt(i, 4); // 4 වෙනි තීරුව (Total) එකතු කරනවා
        }
        lblTotal.setText(String.format("Rs. %.2f", netTotal));
    }

    // UI Helpers (Label & TextStyles)
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