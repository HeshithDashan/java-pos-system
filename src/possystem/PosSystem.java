package possystem;

import com.formdev.flatlaf.FlatDarkLaf; 
import javax.swing.*;

public class PosSystem {

    public static void main(String[] args) {
        
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        java.awt.EventQueue.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}