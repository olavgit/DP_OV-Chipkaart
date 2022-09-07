package p1;

import java.sql.*;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/ovchip";
        try {
            Connection conn = DriverManager.getConnection(url, "postgres", "Jopie06!");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM reiziger");
            System.out.println("Alle reizigers:");

            while (rs.next()) {
                String tv = rs.getString(3);
                if (tv == null) { tv = ""; } else tv += " ";
                String id = "   #" + rs.getString(1) + ": ";
                String vn = rs.getString(2);
                String an = rs.getString(4);
                String gb = " (" + rs.getString(5) + ")";
                String out = id + vn + ". " + tv + an + gb;
                System.out.println(out);
            }

            rs.close();
            st.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}