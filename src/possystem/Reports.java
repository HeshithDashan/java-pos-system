package possystem;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class Reports extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblTotalSales, lblTotalRevenue;

    public Reports(ActionListener onBack) {
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

        JLabel lblTitle = new JLabel("SALES REPORTS");
        lblTitle.setFont(new Font("Poppins", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        
        titleContainer.add(btnBack);
        titleContainer.add(lblTitle);
        headerPanel.add(titleContainer, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel summaryPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        
        JPanel cardSales = createCard("Today's Sales Count", "0", new Color(40, 167, 69));
        lblTotalSales = (JLabel) cardSales.getComponent(1);
        
        JPanel cardRevenue = createCard("Today's Revenue", "Rs. 0.00", new Color(255, 193, 7));
        lblTotalRevenue = (JLabel) cardRevenue.getComponent(1);
        
        summaryPanel.add(cardSales);
        summaryPanel.add(cardRevenue);
        
        contentPanel.add(summaryPanel, BorderLayout.NORTH);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(" Recent Transactions "));

        String[] columns = {"Invoice ID", "Customer", "Date & Time", "Total Amount"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Poppins", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        
        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        JLabel lblHint = new JLabel("* Click on a row to view bill items");
        lblHint.setFont(new Font("Poppins", Font.ITALIC, 12));
        lblHint.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        tablePanel.add(lblHint, BorderLayout.SOUTH);

        contentPanel.add(tablePanel, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);

        loadSalesData();
        loadSummary();

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        int saleId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                        showBillDetails(saleId);
                    }
                }
            }
        });
    }

    private JPanel createCard(String title, String value, Color color) {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setBackground(color);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc: 20");

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Poppins", Font.BOLD, 16));
        lblTitle.setForeground(Color.WHITE);
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Poppins", Font.BOLD, 28));
        lblValue.setForeground(Color.WHITE);
        
        panel.add(lblTitle);
        panel.add(lblValue);
        return panel;
    }

    private void loadSalesData() {
        tableModel.setRowCount(0);
        try {
            Connection conn = DBConnection.connect();
            String sql = "SELECT s.id, c.name, s.sale_date, s.total_price " +
                         "FROM sales s LEFT JOIN customers c ON s.customer_id = c.id " +
                         "ORDER BY s.sale_date DESC";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("sale_date"),
                    rs.getDouble("total_price")
                });
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSummary() {
        try {
            Connection conn = DBConnection.connect();
            String sql = "SELECT COUNT(*), SUM(total_price) FROM sales WHERE DATE(sale_date) = CURDATE()";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                lblTotalSales.setText(String.valueOf(rs.getInt(1)));
                double revenue = rs.getDouble(2);
                lblTotalRevenue.setText("Rs. " + String.format("%.2f", revenue));
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showBillDetails(int saleId) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Invoice Details #" + saleId, true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        String[] cols = {"Product", "Price", "Qty", "Subtotal"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable itemTable = new JTable(model);
        itemTable.setRowHeight(30);
        
        try {
            Connection conn = DBConnection.connect();
            String sql = "SELECT p.name, si.unit_price, si.quantity, si.subtotal " +
                         "FROM sales_items si JOIN products p ON si.product_id = p.id " +
                         "WHERE si.sale_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, saleId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("name"),
                    rs.getDouble("unit_price"),
                    rs.getInt("quantity"),
                    rs.getDouble("subtotal")
                });
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        dialog.add(new JScrollPane(itemTable), BorderLayout.CENTER);
        
        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> dialog.dispose());
        JPanel btnPanel = new JPanel();
        btnPanel.add(btnClose);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
}