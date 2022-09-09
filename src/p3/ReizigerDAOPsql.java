package p3;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    Connection conn;

    private AdresDAOPsql adao;

    public ReizigerDAOPsql(Connection conn) throws SQLException {
        this.conn = conn;
        this.adao = new AdresDAOPsql(this.conn, this);
    }

    @Override
    public boolean save(Reiziger reiziger) {
        try {
            // Insert de reiziger met alle gegevens van het meegegeven object
            PreparedStatement pst = conn.prepareStatement("INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) VALUES (?, ?, ?, ?, ?)");
            pst.setInt(1, reiziger.getId());
            pst.setString(2, reiziger.getVoorletters());
            pst.setString(3, reiziger.getTussenvoegsel());
            pst.setString(4, reiziger.getAchternaam());
            pst.setDate(5, reiziger.getGeboortedatum());
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
            // Update de reiziger met de id uit het meegegeven object
            PreparedStatement pst = conn.prepareStatement("UPDATE reiziger SET reiziger_id = ?, voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ? WHERE reiziger_id = ?");
            pst.setInt(1, reiziger.getId());
            pst.setString(2, reiziger.getVoorletters());
            pst.setString(3, reiziger.getTussenvoegsel());
            pst.setString(4, reiziger.getAchternaam());
            pst.setDate(5, reiziger.getGeboortedatum());
            pst.setInt(6, reiziger.getId());
            pst.executeUpdate();
            pst.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Reiziger reiziger) {
        try {
            // Delete de reiziger met de id uit het meegegeven object
            PreparedStatement pst = conn.prepareStatement("DELETE FROM reiziger WHERE reiziger_id = ?");
            pst.setInt(1, reiziger.getId());
            pst.executeUpdate();
            pst.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Reiziger findById(int id) {
        try {
            // Selecteer de reiziger met id 'id'
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM reiziger WHERE reiziger_id = ?");
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            Reiziger r = null;

            // Maak een object voor de geselecteerde reiziger
            while (rs.next()) {
                r = new Reiziger(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDate(5));
            }

            rs.close();
            pst.close();
            return r;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Reiziger> findByGbDatum(String date) {
        try {
            // Selecteer alle reizigers met geboortdatum 'date'
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM reiziger WHERE geboortedatum = ?");
            pst.setDate(1, Date.valueOf(date));
            ResultSet rs = pst.executeQuery();
            List gbReizigers = new ArrayList();

            // Voor elke geselecteerde reiziger, maak een object en voeg deze toe aan een lijst
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
        return null;
    }

    @Override
    public List<Reiziger> findAll() {
        try {
            // Selecteer alle reizigers
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM reiziger");
            ResultSet rs = pst.executeQuery();
            List alleReizigers = new ArrayList();

            // Voor elke geselecteerde reiziger, maak een object en voeg deze toe aan een lijst
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
        return null;
    }
}
