package p3;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPsql implements AdresDAO {
    private Connection conn;
    private ReizigerDAO rdao;

    public AdresDAOPsql(Connection conn, ReizigerDAO rdao) throws SQLException {
        this.conn = conn;
        this.rdao = rdao;
    }

    @Override
    public boolean save(Adres adres) {
        // Insert het adres met alle gegevens van het meegegeven object
        try {
            PreparedStatement pst = conn.prepareStatement("INSERT INTO adres(adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) VALUES (?, ?, ?, ?, ?, ?)");
            pst.setInt(1, adres.getId());
            pst.setString(2, adres.getPostcode());
            pst.setString(3, adres.getHuisnummer());
            pst.setString(4, adres.getStraat());
            pst.setString(5, adres.getWoonplaats());
            pst.setInt(6, adres.getReiziger().getId());
            pst.executeUpdate();
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
    public boolean update(Adres adres) {
        // Update het adres met de id uit het meegegeven object
        try {
            PreparedStatement pst = conn.prepareStatement("UPDATE adres SET adres_id = ?, postcode = ?, huisnummer = ?, straat = ?, woonplaats = ?, reiziger_id = ? WHERE adres_id = ?");
            pst.setInt(1, adres.getId());
            pst.setString(2, adres.getPostcode());
            pst.setString(3, adres.getHuisnummer());
            pst.setString(4, adres.getStraat());
            pst.setString(5, adres.getWoonplaats());
            pst.setInt(6, adres.getReiziger().getId());
            pst.setInt(7, adres.getId());
            pst.executeUpdate();
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
    public boolean delete(Adres adres) {
        // Delete het adres met de id uit het meegegeven object
        try {
            PreparedStatement pst = conn.prepareStatement("DELETE FROM adres WHERE adres_id = ?");
            pst.setInt(1, adres.getId());
            pst.executeUpdate();
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
    public List<Adres> findByReiziger(Reiziger reiziger) {
        // Selecteer het adres met de reiziger_id uit het meegegeven object
        try {
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM adres WHERE reiziger_id = ?");
            pst.setInt(1, reiziger.getId());
            ResultSet rs = pst.executeQuery();
            List rzAdressen = new ArrayList();

            while (rs.next()) {
                Adres a = new Adres(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rdao.findById(rs.getInt(6)));
                rzAdressen.add(a);
            }

            rs.close();
            pst.close();
            return rzAdressen;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Adres> findAll() {
        // Selecteer alle adressen
        try {
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM adres");
            ResultSet rs = pst.executeQuery();
            List alleAdressen = new ArrayList();

            while (rs.next()) {
                Adres a = new Adres(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rdao.findById(rs.getInt(6)));
                alleAdressen.add(a);
            }

            rs.close();
            pst.close();
            return alleAdressen;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
