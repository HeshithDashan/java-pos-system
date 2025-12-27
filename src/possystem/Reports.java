package possystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Reports extends JFrame {

    private JTable tableSales, tableItems;
    private DefaultTableModel modelSales, modelItems;
    private JLabel lblTotalSales;

    public Reports() {
        setTitle("Sales Reports");
        setSize(1000, 700); // Window එක ටිකක් ලොකු කළා
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(24, 24, 24));
        setLayout(new BorderLayout(10, 10));

        // --- 1. Header Section ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(33, 150, 243));
        headerPanel.setPreferredSize(new Dimension(1000, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblTitle = new JLabel("📊 SALES HISTORY");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        lblTotalSales = new JLabel("Total Revenue: Rs. 0.00");
        lblTotalSales.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTotalSales.setForeground(Color.YELLOW); // කැපී පේන්න
        headerPanel.add(lblTotalSales, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // --- 2. Main Content (Split View) ---
        // උඩින් Sales Table එක, යටින් Items Table එක
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(350); // උඩ කොටසේ උස
        splitPane.setResizeWeight(0.5);
        
        // --- TOP TABLE (Sales List) ---
        JPanel panelSales = new JPanel(new BorderLayout());
        panelSales.setBorder(BorderFactory.createTitledBorder(null, "Step 1: Select a Bill", 0, 0, new Font("Segoe UI", Font.BOLD, 14), Color.WHITE));
        panelSales.setBackground(new Color(24, 24, 24));

        String[] salesCols = {"Sale ID", "Date/Time", "Total (Rs)", "Cash (Rs)", "Balance (Rs)"};
        modelSales = new DefaultTableModel(salesCols, 0);
        tableSales = new JTable(modelSales);
        tableSales.setRowHeight(30);
        tableSales.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        panelSales.add(new JScrollPane(tableSales), BorderLayout.CENTER);
        splitPane.setTopComponent(panelSales);

        // --- BOTTOM TABLE (Items List) ---
        JPanel panelItems = new JPanel(new BorderLayout());
        panelItems.setBorder(BorderFactory.createTitledBorder(null, "Step 2: Bill Items", 0, 0, new Font("Segoe UI", Font.BOLD, 14), Color.WHITE));
        panelItems.setBackground(new Color(24, 24, 24));

        String[] itemCols = {"Product Name", "Price", "Qty", "Sub Total"};
        modelItems = new DefaultTableModel(itemCols, 0);
        tableItems = new JTable(modelItems);
        tableItems.setRowHeight(30);
        tableItems.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        panelItems.add(new JScrollPane(tableItems), BorderLayout.CENTER);
        splitPane.setBottomComponent(panelItems);

        add(splitPane, BorderLayout.CENTER);

        // --- Load Data ---
        loadSales();

        // --- Event Listener (Click on Sales Table) ---
        // බිල් එකක් උඩ Click කරාම යට Table එක Update වෙනවා
        tableSales.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableSales.getSelectedRow() != -1) {
                int saleId = (int) tableSales.getValueAt(tableSales.getSelectedRow(), 0);
                loadSaleItems(saleId);
            }
        });
    }

    // 1. ඔක්කොම බිල් ටික පූරවනවා
    private void loadSales() {
        try {
            modelSales.setRowCount(0);
            Connection conn = DBConnection.connect();
            Statement stmt = conn.createStatement();
            // අලුත් බිල් උඩින්ම පේන්න (ORDER BY id DESC)
            ResultSet rs = stmt.executeQuery("SELECT * FROM sales ORDER BY id DESC");

            double totalRevenue = 0;

            while (rs.next()) {
                double total = rs.getDouble("total_price");
                totalRevenue += total;

                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("sale_date"),
                    total,
                    rs.getDouble("cash_paid"),
                    rs.getDouble("balance")
                };
                modelSales.addRow(row);
            }
            conn.close();
            
            // මුළු ආදායම උඩින් පෙන්නනවා
            lblTotalSales.setText("Total Revenue: Rs. " + String.format("%.2f", totalRevenue));

        } catch (Exception e) {
            Message.showError(this, "Error loading sales: " + e.getMessage());
        }
    }

    // 2. තෝරපු බිල් එකේ බඩු ටික පූරවනවා
    private void loadSaleItems(int saleId) {
        try {
            modelItems.setRowCount(0); // පරණ data මකනවා
            Connection conn = DBConnection.connect();
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM sales_items WHERE sale_id = ?");
            pst.setInt(1, saleId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getString("product_name"),
                    rs.getDouble("price"),
                    rs.getInt("qty"),
                    rs.getDouble("sub_total")
                };
                modelItems.addRow(row);
            }
            conn.close();

        } catch (Exception e) {
            Message.showError(this, "Error loading items: " + e.getMessage());
        }
    }
}