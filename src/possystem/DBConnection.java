package possystem;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class DBConnection {

    public static Connection connect() {
        Connection conn = null;
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/pos_system", "root", "");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, e);
        }
        return conn;
    }

    public static void main(String[] args) {
        if (connect() != null) {
            System.out.println(" Database Connected Successfully! 🔥");
        } else {
            System.out.println(" Connection Failed!");
        }
    }
}
