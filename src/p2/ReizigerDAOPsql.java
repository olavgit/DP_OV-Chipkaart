package p2;

import java.sql.*;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    String url = "jdbc:postgresql://localhost:5432/ovchip";
    Connection conn = DriverManager.getConnection(url, "postgres", "Jopie06!");

    public ReizigerDAOPsql(Connection conn) throws SQLException {
        this.conn = conn;
    }

    @Override
    public boolean save(Reiziger reiziger) {
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM reiziger");
            System.out.println("Alle reizigers:");

            rs.close();
            st.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Reiziger reiziger) {
        return false;
    }

    @Override
    public boolean delete(Reiziger reiziger) {
        return false;
    }

    @Override
    public Reiziger findById(int id) {
        return null;
    }

    @Override
    public List<Reiziger> findByGbDatum(String date) {
        return null;
    }

    @Override
    public List<Reiziger> findAll() {
        return null;
    }
}
