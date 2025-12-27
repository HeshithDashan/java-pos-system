package possystem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class NewSale extends JFrame {

    private JTextField txtProductID, txtQty, txtCash;
    private JLabel lblTotal, lblBalance;
    private JTable cartTable;
    private DefaultTableModel cartModel;

    public NewSale() {
        setTitle("New Sale (Billing)");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Main Background Dark
        getContentPane().setBackground(new Color(24, 24, 24));
        setLayout(new BorderLayout());

        // --- 1. HEADER SECTION ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(33, 150, 243)); // Pos Blue
        headerPanel.setPreferredSize(new Dimension(800, 70));
        headerPanel.setBorder(new EmptyBorder(10, 30, 10, 30));

        JLabel lblTitle = new JLabel("🛒 BILLING TERMINAL");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        // Date/Time පෙන්නන්න තැනක් (පස්සේ හදමු)
        JLabel lblDate = new JLabel("2025-12-27"); 
        lblDate.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblDate.setForeground(new Color(240, 240, 240));
        headerPanel.add(lblDate, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // --- 2. LEFT PANEL (Cart & Inputs) ---
        JPanel leftPanel = new JPanel(new BorderLayout(20, 20));
        leftPanel.setBackground(new Color(24, 24, 24));
        leftPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Top Input Bar
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        inputPanel.setBackground(new Color(45, 45, 45)); // Slightly lighter dark
        inputPanel.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 1));
        inputPanel.setPreferredSize(new Dimension(0, 80)); // Fixed height

        // Product Input
        inputPanel.add(createLabel("Product ID / Name:"));
        txtProductID = createTextField(20);
        inputPanel.add(txtProductID);

        // Qty Input
        inputPanel.add(createLabel("Qty:"));
        txtQty = createTextField(5);
        inputPanel.add(txtQty);

        // Add Button
        JButton btnAdd = new JButton("ADD ITEM");
        btnAdd.setBackground(new Color(46, 204, 113)); // Green
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAdd.setPreferredSize(new Dimension(120, 40));
        btnAdd.setFocusPainted(false);
        btnAdd.putClientProperty("JButton.buttonType", "roundRect");
        inputPanel.add(btnAdd);

        leftPanel.add(inputPanel, BorderLayout.NORTH);

        // Table Area
        String[] columns = {"ID", "Product Name", "Price", "Qty", "Total"};
        cartModel = new DefaultTableModel(columns, 0);
        
        cartTable = new JTable(cartModel);
        cartTable.setRowHeight(40); // පේළි උස වැඩි කළා
        cartTable.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        cartTable.setShowVerticalLines(false); // Grid Lines අයින් කළා
        cartTable.setIntercellSpacing(new Dimension(0, 0));
        
        // Table Header Styling
        JTableHeader header = cartTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setBackground(new Color(60, 60, 60));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 40));

        // Center Align Cells
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
        rightPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(60, 60, 60))); // වම් පැත්තෙන් ඉරක්

        // Total Display Area (Top of Right Panel)
        JPanel totalDisplayPanel = new JPanel(new GridLayout(2, 1));
        totalDisplayPanel.setBackground(new Color(30, 30, 30));
        totalDisplayPanel.setBorder(new EmptyBorder(30, 20, 30, 20));

        JLabel lblNetTotalTitle = new JLabel("NET TOTAL", SwingConstants.RIGHT);
        lblNetTotalTitle.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblNetTotalTitle.setForeground(Color.GRAY);
        totalDisplayPanel.add(lblNetTotalTitle);

        lblTotal = new JLabel("Rs. 0.00", SwingConstants.RIGHT);
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 48)); // Huge Font
        lblTotal.setForeground(new Color(46, 204, 113)); // Bright Green
        totalDisplayPanel.add(lblTotal);

        rightPanel.add(totalDisplayPanel, BorderLayout.NORTH);

        // Payment Inputs (Center of Right Panel)
        JPanel paymentInputPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        paymentInputPanel.setBackground(new Color(30, 30, 30));
        paymentInputPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        paymentInputPanel.add(createLabel("Cash Received (Rs):"));
        txtCash = createTextField(10);
        txtCash.setFont(new Font("Segoe UI", Font.BOLD, 24)); // ලොකු ඉලක්කම්
        txtCash.setHorizontalAlignment(JTextField.RIGHT);
        txtCash.setForeground(new Color(255, 193, 7)); // Gold Color Text
        paymentInputPanel.add(txtCash);

        paymentInputPanel.add(createLabel("Balance (Rs):"));
        lblBalance = new JLabel("0.00", SwingConstants.RIGHT);
        lblBalance.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblBalance.setForeground(new Color(231, 76, 60)); // Red Color
        paymentInputPanel.add(lblBalance);

        rightPanel.add(paymentInputPanel, BorderLayout.CENTER);

        // Action Buttons (Bottom of Right Panel)
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
    }

    // Helper method to create styled labels
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lbl.setForeground(Color.LIGHT_GRAY);
        return lbl;
    }

    // Helper method to create styled text fields
    private JTextField createTextField(int columns) {
        JTextField txt = new JTextField(columns);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txt.putClientProperty("JComponent.roundRect", true);
        txt.putClientProperty("JTextField.placeholderText", "Type here...");
        return txt;
    }
}