package possystem;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Message {

    public static void showSuccess(Component parent, String message) {
        showCustomDialog(parent, message, "Success", "check.png", new Color(46, 204, 113)); 
    }

    public static void showError(Component parent, String message) {
        showCustomDialog(parent, message, "Error", "warning.png", new Color(231, 76, 60)); 
    }

    private static void showCustomDialog(Component parent, String msg, String title, String iconName, Color titleColor) {
        
        JLabel lblMessage = new JLabel("<html><body style='width: 250px; text-align: center;'>" + msg + "</body></html>");
        lblMessage.setFont(new Font("Segoe UI", Font.BOLD, 16)); 
        lblMessage.setForeground(Color.WHITE); 
        lblMessage.setHorizontalAlignment(SwingConstants.CENTER);

        ImageIcon icon = null;
        try {
            URL iconURL = Message.class.getResource("/icons/" + iconName);
            if (iconURL != null) {

                Image img = new ImageIcon(iconURL).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                icon = new ImageIcon(img);
            }
        } catch (Exception e) {
            System.out.println("Error loading icon: " + iconName);
        }

        JOptionPane.showMessageDialog(parent, 
                lblMessage, 
                title, 
                JOptionPane.PLAIN_MESSAGE, 
                icon);
    }
}