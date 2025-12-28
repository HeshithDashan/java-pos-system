package possystem;

import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;
import javax.swing.UIManager;

public class PosSystem {

    public static void main(String[] args) {
        try {

            FlatDarkLaf.setup();

            InputStream fontStream = PosSystem.class.getResourceAsStream("/fonts/Poppins-Regular.ttf");
            
            if (fontStream != null) {

                Font poppins = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(14f); 
                
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(poppins);
                
                UIManager.put("defaultFont", poppins);
            } else {
                System.out.println("⚠️ Font file not found!");
            }
            
            new LoginForm().setVisible(true);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}