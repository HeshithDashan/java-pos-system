package possystem;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Message {

    // ✅ Success Message
    public static void showSuccess(Component parent, String message) {
        showCustomDialog(parent, message, "Success", "check.png", new Color(46, 204, 113));
    }

    // ❌ Error Message
    public static void showError(Component parent, String message) {
        showCustomDialog(parent, message, "Error", "warning.png", new Color(231, 76, 60));
    }

    // ❓ Confirm Message (Yes/No අහන එක) - මේක තමයි අලුත් එක
    public static boolean showConfirm(Component parent, String message) {
        // 1. Message Text Style
        JLabel lblMessage = new JLabel("<html><body style='width: 250px; text-align: center;'>" + message + "</body></html>");
        lblMessage.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblMessage.setForeground(Color.WHITE);
        lblMessage.setHorizontalAlignment(SwingConstants.CENTER);

        // 2. Icon Setup
        ImageIcon icon = null;
        try {
            URL iconURL = Message.class.getResource("/icons/question.png"); // ඔයා දාපු අලුත් icon එක
            if (iconURL != null) {
                Image img = new ImageIcon(iconURL).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                icon = new ImageIcon(img);
            }
        } catch (Exception e) {
        }

        // 3. Show Yes/No Dialog
        int result = JOptionPane.showConfirmDialog(parent, 
                lblMessage, 
                "Confirm", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE, 
                icon);

        return result == JOptionPane.YES_OPTION; // Yes එබුවොත් true යවනවා
    }

    // Private Helper Method (Alerts වලට විතරයි)
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
        }

        JOptionPane.showMessageDialog(parent, lblMessage, title, JOptionPane.PLAIN_MESSAGE, icon);
    }
}