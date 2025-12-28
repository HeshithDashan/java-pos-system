package possystem;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SettingsPanel extends JPanel {

    private JTextField txtShopName;
    private JTextField txtAddress;
    private JTextField txtPhone;
    private JTextField txtCurrency;

    // Constructor එකට 'onBack' කියලා parameter එකක් එකතු කළා
    public SettingsPanel(ActionListener onBack) {
        setLayout(new BorderLayout());
        
        // 1. Header Area
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 132, 255));
        headerPanel.setPreferredSize(new Dimension(800, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); // Padding
        
        // Back Button & Title Container (West Side)
        JPanel titleContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 12));
        titleContainer.setOpaque(false);

        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBack.setForeground(Color.WHITE);
        btnBack.setBackground(new Color(255, 255, 255, 50)); // Transparent white
        btnBack.setBorderPainted(false);
        btnBack.setFocusPainted(false);
        btnBack.putClientProperty(FlatClientProperties.STYLE, "arc: 10");
        
        // Back Button Action
        btnBack.addActionListener(onBack);

        JLabel lblTitle = new JLabel("⚙ SETTINGS");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        
        titleContainer.add(btnBack);
        titleContainer.add(lblTitle);
        
        headerPanel.add(titleContainer, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // 2. Form Content Area
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        addFormItem(contentPanel, gbc, 0, "Shop Name:", txtShopName = new JTextField(20));
        addFormItem(contentPanel, gbc, 1, "Address:", txtAddress = new JTextField(20));
        addFormItem(contentPanel, gbc, 2, "Phone Number:", txtPhone = new JTextField(20));
        addFormItem(contentPanel, gbc, 3, "Currency Symbol:", txtCurrency = new JTextField("Rs.", 20));

        JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerWrapper.add(contentPanel);
        add(centerWrapper, BorderLayout.CENTER);

        // 3. Footer (Save Button)
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JButton btnSave = new JButton("Save Changes");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setBackground(new Color(0, 200, 83));
        btnSave.setForeground(Color.WHITE);
        btnSave.putClientProperty(FlatClientProperties.STYLE, "arc: 10");
        
        footerPanel.add(btnSave);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void addFormItem(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        field.setPreferredSize(new Dimension(300, 35));
        field.putClientProperty(FlatClientProperties.STYLE, "arc: 10");
        panel.add(field, gbc);
    }
}