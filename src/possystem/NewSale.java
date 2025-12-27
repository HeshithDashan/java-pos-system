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
import java.sql.Statement;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.awt.print.Printable;
import java.awt.print.PageFormat;

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

        // --- Header ---
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

        // --- Left Panel ---
        JPanel leftPanel = new JPanel(new BorderLayout(20, 20));
        leftPanel.setBackground(new Color(24, 24, 24));
        leftPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

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

        String[] columns = {"ID", "Product Name", "Price", "Qty", "Total"};
        cartModel = new DefaultTableModel(columns, 0);
        cartTable = new JTable(cartModel);
        cartTable.setRowHeight(40); 
        cartTable.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        cartTable.setShowVerticalLines(false); 
        cartTable.setIntercellSpacing(new Dimension(0, 0));
        
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

        // --- Right Panel ---
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(400, 0));
        rightPanel.setBackground(new Color(30, 30, 30));
        rightPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(60, 60, 60)));

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

        // 🔥 මෙන්න මේ පේළිය තමයි මග හැරුනේ! 
        add(rightPanel, BorderLayout.EAST);

        // --- Actions ---
        btnAdd.addActionListener(e -> addItemToCart());

        btnRemove.addActionListener(e -> {
            int selectedRow = cartTable.getSelectedRow();
            if (selectedRow != -1) {
                cartModel.removeRow(selectedRow);
                updateNetTotal(); 
                calculateBalance(); 
            } else {
                Message.showError(this, "Please select an item to remove!");
            }
        });

        txtProductID.addActionListener(e -> txtQty.requestFocus()); 
        txtQty.addActionListener(e -> addItemToCart()); 

        txtCash.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                calculateBalance();
            }
        });

        btnPay.addActionListener(e -> payAndPrint());
    }

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

    private void updateNetTotal() {
        netTotal = 0.0;
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            netTotal += (double) cartModel.getValueAt(i, 4); 
        }
        lblTotal.setText(String.format("Rs. %.2f", netTotal));
        calculateBalance(); 
    }

    private void calculateBalance() {
        try {
            double cash = Double.parseDouble(txtCash.getText());
            double balance = cash - netTotal;
            lblBalance.setText(String.format("%.2f", balance));
            
            if (balance < 0) {
                lblBalance.setForeground(new Color(231, 76, 60)); 
            } else {
                lblBalance.setForeground(new Color(46, 204, 113)); 
            }
        } catch (NumberFormatException e) {
            lblBalance.setText("0.00");
        }
    }

    // 🔥 SAVE SALE TO DATABASE & UPDATE STOCK
    private void payAndPrint() {
        if (cartModel.getRowCount() == 0) {
            Message.showError(this, "Cart is empty!");
            return;
        }

        try {
            double cash = Double.parseDouble(txtCash.getText());
            double balance = cash - netTotal;
            
            if (cash < netTotal) {
                Message.showError(this, "Insufficient Cash!");
                return;
            }

            Connection conn = DBConnection.connect();
            
            String insertSale = "INSERT INTO sales (total_price, cash_paid, balance) VALUES (?, ?, ?)";
            PreparedStatement pstSale = conn.prepareStatement(insertSale, Statement.RETURN_GENERATED_KEYS);
            pstSale.setDouble(1, netTotal);
            pstSale.setDouble(2, cash);
            pstSale.setDouble(3, balance);
            pstSale.executeUpdate();
            
            ResultSet rs = pstSale.getGeneratedKeys();
            int saleId = 0;
            if (rs.next()) {
                saleId = rs.getInt(1);
            }

            String insertItem = "INSERT INTO sales_items (sale_id, product_name, price, qty, sub_total) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstItem = conn.prepareStatement(insertItem);
            
            String updateStock = "UPDATE products SET stock = stock - ? WHERE id = ?";
            PreparedStatement pstStock = conn.prepareStatement(updateStock);

            for (int i = 0; i < cartModel.getRowCount(); i++) {
                int prodId = (int) cartModel.getValueAt(i, 0); 
                String name = cartModel.getValueAt(i, 1).toString();
                double price = (double) cartModel.getValueAt(i, 2);
                int qty = (int) cartModel.getValueAt(i, 3); 
                double subTotal = (double) cartModel.getValueAt(i, 4);

                pstItem.setInt(1, saleId);
                pstItem.setString(2, name);
                pstItem.setDouble(3, price);
                pstItem.setInt(4, qty);
                pstItem.setDouble(5, subTotal);
                pstItem.executeUpdate();
                
                pstStock.setInt(1, qty);
                pstStock.setInt(2, prodId);
                pstStock.executeUpdate();
            }
            
            conn.close();

            Message.showSuccess(this, "Bill Saved & Printing... 🖨️");
            
            printBill(); 

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

    private void printBill() {
        PrinterJob pj = PrinterJob.getPrinterJob();
        
        pj.setPrintable(new Printable() {
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                if (pageIndex > 0) return NO_SUCH_PAGE;

                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                int y = 20; 
                int x = 10; 

                g2d.setFont(new Font("Monospaced", Font.BOLD, 12));
                g2d.drawString("      SMART POS SYSTEM      ", x, y); y += 15;
                g2d.setFont(new Font("Monospaced", Font.PLAIN, 10));
                g2d.drawString("    No 123, Galle Road,     ", x, y); y += 12;
                g2d.drawString("        Colombo 03.         ", x, y); y += 12;
                g2d.drawString("Date: " + java.time.LocalDate.now(), x, y); y += 12;
                g2d.drawString("-------------------------------------", x, y); y += 12;
                
                g2d.drawString("Item          Qty   Price   Total", x, y); y += 12;
                g2d.drawString("-------------------------------------", x, y); y += 15;

                for (int i = 0; i < cartModel.getRowCount(); i++) {
                    String name = cartModel.getValueAt(i, 1).toString();
                    String qty = cartModel.getValueAt(i, 3).toString();
                    String price = cartModel.getValueAt(i, 2).toString();
                    String total = cartModel.getValueAt(i, 4).toString();

                    if (name.length() > 12) name = name.substring(0, 12); 

                    g2d.drawString(String.format("%-12s %3s %7s %7s", name, qty, price, total), x, y);
                    y += 15;
                }

                g2d.drawString("-------------------------------------", x, y); y += 15;

                g2d.setFont(new Font("Monospaced", Font.BOLD, 12));
                g2d.drawString("Net Total :       Rs. " + lblTotal.getText().replace("Rs. ", ""), x, y); y += 15;
                g2d.drawString("Cash      :       Rs. " + txtCash.getText(), x, y); y += 15;
                g2d.drawString("Balance   :       Rs. " + lblBalance.getText(), x, y); y += 25;

                g2d.setFont(new Font("Monospaced", Font.ITALIC, 10));
                g2d.drawString("      Thank You! Come Again!     ", x, y); y += 12;

                return PAGE_EXISTS;
            }
        });

        boolean doPrint = pj.printDialog();
        if (doPrint) {
            try {
                pj.print();
            } catch (PrinterException e) {
                Message.showError(NewSale.this, "Printing Failed: " + e.getMessage());
            }
        }
    }

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