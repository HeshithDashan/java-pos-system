package possystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class NewSale extends JFrame {

    private JTextField txtProductID, txtQty, txtCash;
    private JLabel lblTotal, lblChange, lblBalance;
    private JTable cartTable;
    private DefaultTableModel cartModel;

    public NewSale() {
        setTitle("New Sale (Billing)");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full Screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dashboard එක වැහෙන්න බෑ
        setLayout(new BorderLayout(10, 10));

        // --- 1. Header ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(33, 150, 243));
        headerPanel.setPreferredSize(new Dimension(800, 70));
        JLabel lblTitle = new JLabel("🛒 BILLING SECTION");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle);
        add(headerPanel, BorderLayout.NORTH);

        // --- 2. LEFT PANEL (Product Input & Cart) ---
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 10));
        
        // Input Area
        JPanel inputPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add Product"));
        
        inputPanel.add(new JLabel("Product ID / Name:"));
        txtProductID = new JTextField();
        txtProductID.putClientProperty("JComponent.roundRect", true);
        inputPanel.add(txtProductID);

        inputPanel.add(new JLabel("Quantity:"));
        txtQty = new JTextField();
        txtQty.putClientProperty("JComponent.roundRect", true);
        inputPanel.add(txtQty);

        JButton btnAdd = new JButton("Add to Cart");
        btnAdd.setBackground(new Color(46, 204, 113)); // Green
        btnAdd.setForeground(Color.WHITE);
        inputPanel.add(btnAdd);

        leftPanel.add(inputPanel, BorderLayout.NORTH);

        // Table (The Cart)
        String[] columns = {"ID", "Name", "Price", "Qty", "Total"};
        cartModel = new DefaultTableModel(columns, 0);
        cartTable = new JTable(cartModel);
        cartTable.setRowHeight(30);
        cartTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JScrollPane scrollPane = new JScrollPane(cartTable);
        leftPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(leftPanel, BorderLayout.CENTER);

        // --- 3. RIGHT PANEL (Payment & Totals) ---
        JPanel rightPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        rightPanel.setPreferredSize(new Dimension(350, 0)); // දකුණු පැත්තේ පළල
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));

        // Total Showing Box
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBackground(new Color(44, 62, 80)); // Dark box
        JLabel lblTotalText = new JLabel("NET TOTAL", SwingConstants.CENTER);
        lblTotalText.setForeground(Color.WHITE);
        lblTotalText.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        
        lblTotal = new JLabel("Rs. 0.00", SwingConstants.CENTER);
        lblTotal.setForeground(new Color(46, 204, 113)); // Green Text
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 40)); // Loku Font
        
        totalPanel.add(lblTotalText, BorderLayout.NORTH);
        totalPanel.add(lblTotal, BorderLayout.CENTER);
        rightPanel.add(totalPanel);

        // Cash Input
        JPanel paymentPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        paymentPanel.setBorder(BorderFactory.createTitledBorder("Payment"));
        
        paymentPanel.add(new JLabel("Cash Paid (Rs):"));
        txtCash = new JTextField();
        txtCash.setFont(new Font("Segoe UI", Font.BOLD, 18));
        paymentPanel.add(txtCash);
        
        paymentPanel.add(new JLabel("Balance (Rs):"));
        lblBalance = new JLabel("0.00");
        lblBalance.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblBalance.setForeground(Color.RED);
        paymentPanel.add(lblBalance);
        
        rightPanel.add(paymentPanel);

        // Buttons
        JButton btnPay = new JButton("PAY & PRINT");
        btnPay.setBackground(new Color(33, 150, 243));
        btnPay.setForeground(Color.WHITE);
        btnPay.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        JButton btnRemove = new JButton("Remove Item");
        btnRemove.setBackground(new Color(231, 76, 60));
        btnRemove.setForeground(Color.WHITE);

        JPanel actionPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        actionPanel.add(btnRemove);
        actionPanel.add(btnPay);
        
        rightPanel.add(actionPanel);

        add(rightPanel, BorderLayout.EAST);
    }
}