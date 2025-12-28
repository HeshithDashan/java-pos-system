package possystem;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.*;

public class SettingsPanel extends JPanel {

    private JTextField txtShopName;
    private JTextField txtAddress;
    private JTextField txtPhone;
    private JTextField txtCurrency;

    public SettingsPanel(ActionListener onBack) {
        setLayout(new BorderLayout());
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        Color headerBlue = new Color(0, 132, 255); 
        headerPanel.setBackground(headerBlue);
        headerPanel.setPreferredSize(new Dimension(800, 70)); 
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); 
        
        JPanel titleContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        titleContainer.setOpaque(false);

        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnBack.setForeground(headerBlue); 
        btnBack.setBackground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setBorderPainted(false);
        btnBack.putClientProperty(FlatClientProperties.STYLE, "arc: 20; margin: 5,15,5,15;");
        btnBack.addActionListener(onBack);

        JLabel lblTitle = new JLabel("SETTINGS"); 
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        
        try {
            URL iconURL = getClass().getResource("/icons/settings.png");
            if (iconURL != null) {
                ImageIcon icon = new ImageIcon(iconURL);
                Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                lblTitle.setIcon(new ImageIcon(img));
                lblTitle.setIconTextGap(15); 
            }
        } catch (Exception e) {
            System.out.println("Settings icon error: " + e.getMessage());
        }
        
        titleContainer.add(btnBack);
        titleContainer.add(lblTitle);
        
        headerPanel.add(titleContainer, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        addFormItem(contentPanel, gbc, 0, "Shop Name:", txtShopName = new JTextField(20));
        addFormItem(contentPanel, gbc, 1, "Address:", txtAddress = new JTextField(20));
        addFormItem(contentPanel, gbc, 2, "Phone Number:", txtPhone = new JTextField(20));
        addFormItem(contentPanel, gbc, 3, "Currency Symbol:", txtCurrency = new JTextField("Rs.", 20));

        JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerWrapper.add(contentPanel);
        add(centerWrapper, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));

        JButton btnSave = new JButton("Save Changes");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnSave.setBackground(new Color(0, 200, 83));
        btnSave.setForeground(Color.WHITE);
        btnSave.setPreferredSize(new Dimension(150, 45));
        btnSave.putClientProperty(FlatClientProperties.STYLE, "arc: 15");
        
        btnSave.addActionListener(e -> saveSettings());
        
        footerPanel.add(btnSave);
        add(footerPanel, BorderLayout.SOUTH);

        loadSettings();
    }

    private void addFormItem(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 17)); 
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        field.setPreferredSize(new Dimension(350, 40)); 
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.putClientProperty(FlatClientProperties.STYLE, "arc: 10; margin: 5,10,5,10;");
        panel.add(field, gbc);
    }


    private void loadSettings() {
        try {

            Connection conn = DBConnection.connect(); 
            String sql = "SELECT * FROM settings WHERE id = 1";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                txtShopName.setText(rs.getString("shop_name"));
                txtAddress.setText(rs.getString("address"));
                txtPhone.setText(rs.getString("phone"));
                txtCurrency.setText(rs.getString("currency"));
            }
            conn.close();
        } catch (Exception e) {
            System.out.println("Load Error: " + e.getMessage());
        }
    }

    private void saveSettings() {
        try {

            Connection conn = DBConnection.connect();
            String sql = "UPDATE settings SET shop_name=?, address=?, phone=?, currency=? WHERE id=1";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, txtShopName.getText());
            pstmt.setString(2, txtAddress.getText());
            pstmt.setString(3, txtPhone.getText());
            pstmt.setString(4, txtCurrency.getText());
            
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Settings Saved Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving settings: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}