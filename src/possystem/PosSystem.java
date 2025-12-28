package possystem;

import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;
import javax.swing.UIManager;

public class PosSystem {

    public static void main(String[] args) {
        try {
            // 1. Theme එක Setup කිරීම
            FlatDarkLaf.setup();

            // 2. Font එක Load කරගැනීම
            InputStream fontStream = PosSystem.class.getResourceAsStream("/fonts/Poppins-Regular.ttf");
            
            if (fontStream != null) {
                // Font එක හදලා size එක 14 දානවා
                Font poppins = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(14f); 
                
                // System එකට Register කරනවා
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(poppins);
                
                // මුළු App එකටම මේ ෆොන්ට් එක දාන්න කියනවා
                UIManager.put("defaultFont", poppins);
            } else {
                System.out.println("⚠️ Font file not found!");
            }
            
            // 3. Login Form එක Open කිරීම
            new LoginForm().setVisible(true);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}