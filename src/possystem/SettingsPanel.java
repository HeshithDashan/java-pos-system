package possystem;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;

public class SettingsPanel extends JPanel {

    private JTextField txtShopName;
    private JTextField txtAddress;
    private JTextField txtPhone;
    private JTextField txtCurrency;

    public SettingsPanel(ActionListener onBack) {
        setLayout(new BorderLayout());
        
        // 1. Header Area
        JPanel headerPanel = new JPanel(new BorderLayout());
        Color headerBlue = new Color(0, 132, 255); // Header එකේ නිල් පාට variable එකකට ගත්තා
        headerPanel.setBackground(headerBlue);
        headerPanel.setPreferredSize(new Dimension(800, 70)); // උස පොඩ්ඩක් වැඩි කළා
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding හැදුවා
        
        // --- Back Button & Title Container ---
        JPanel titleContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        titleContainer.setOpaque(false);

        // --- BACK BUTTON (Colors Changed) ---
        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 15));
        // අලුත් පාට: සුදු පසුබිම, නිල් අකුරු (හොඳට කැපිලා පේන්න)
        btnBack.setForeground(headerBlue); 
        btnBack.setBackground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setBorderPainted(false);
         // FlatLaf style: රවුම්, සහ bold අකුරු
        btnBack.putClientProperty(FlatClientProperties.STYLE, "arc: 20; margin: 5,15,5,15;");
        btnBack.addActionListener(onBack);

        // --- TITLE WITH ICON ---
        JLabel lblTitle = new JLabel("SETTINGS"); // Emoji එක අයින් කළා
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        
        // Icon එක add කිරීම
        try {
             // Dashboard එකේ පාවිච්චි කරන settings icon එකම ගන්නවා
            URL iconURL = getClass().getResource("/icons/settings.png");
            if (iconURL != null) {
                ImageIcon icon = new ImageIcon(iconURL);
                // අයිකන් එක ලස්සනට පේන්න සයිස් කරනවා (40x40)
                Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                lblTitle.setIcon(new ImageIcon(img));
                lblTitle.setIconTextGap(15); // Icon සහ Text අතර පරතරය
            }
        } catch (Exception e) {
            System.out.println("Settings icon error: " + e.getMessage());
        }
        
        titleContainer.add(btnBack);
        titleContainer.add(lblTitle);
        
        headerPanel.add(titleContainer, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // 2. Form Content Area (මේ ටිකේ වෙනසක් නෑ)
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10); // පරතරය ටිකක් වැඩි කළා
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
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));

        JButton btnSave = new JButton("Save Changes");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnSave.setBackground(new Color(0, 200, 83));
        btnSave.setForeground(Color.WHITE);
        btnSave.setPreferredSize(new Dimension(150, 45));
        btnSave.putClientProperty(FlatClientProperties.STYLE, "arc: 15");
        
        footerPanel.add(btnSave);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void addFormItem(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 17)); // Font size ටිකක් වැඩි කළා
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        field.setPreferredSize(new Dimension(350, 40)); // Input field එක ටිකක් ලොකු කළා
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.putClientProperty(FlatClientProperties.STYLE, "arc: 10; margin: 5,10,5,10;");
        panel.add(field, gbc);
    }
}