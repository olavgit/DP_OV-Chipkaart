package p2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    Connection conn;

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
            pst.setDate(4, reiziger.getGeboortedatum());
            pst.executeUpdate();
            pst.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Reiziger reiziger) {
        try {
            PreparedStatement pst = conn.prepareStatement()
        }
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
        try {
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM reiziger WHERE geboortedatum = ?");
            pst.setDate(1, Date.valueOf(date));
            ResultSet rs = pst.executeQuery();
            List gbReizigers = new ArrayList();

            while (rs.next()) {
                Reiziger r = new Reiziger(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDate(5));
                gbReizigers.add(r);
            }

            rs.close();
            pst.close();
            return gbReizigers;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Reiziger> findAll() {
        try {
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM reiziger");
            ResultSet rs = pst.executeQuery();
            List alleReizigers = new ArrayList();

            while (rs.next()) {
                Reiziger r = new Reiziger(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDate(5));
                alleReizigers.add(r);
            }

            rs.close();
            pst.close();
            return alleReizigers;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
