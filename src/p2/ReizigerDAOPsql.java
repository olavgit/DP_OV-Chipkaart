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
            PreparedStatement pst = conn.prepareStatement("INSERT INTO reiziger VALUES(voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ?)");
            pst.setString(1, reiziger.getVoorletters());
            pst.setString(2, reiziger.getTussenvoegsel());
            pst.setString(3, reiziger.getAchternaam());
            pst.setString(4, String.valueOf(reiziger.getGeboortedatum()));
            ResultSet rs = pst.executeQuery();

            rs.close();
            pst.close();
            return true;
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
