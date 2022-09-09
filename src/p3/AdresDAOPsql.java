package p3;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPsql implements AdresDAO {
    private Connection conn;

    private ReizigerDAOPsql rdao;

    public AdresDAOPsql(Connection conn, ReizigerDAOPsql reizigerDAOPsql) throws SQLException {
        this.conn = conn;
        this.rdao = reizigerDAOPsql;
    }

    @Override
    public boolean save(Adres adres) {
        try {
            // Insert het adres met alle gegevens van het meegegeven object
            PreparedStatement pst = conn.prepareStatement("INSERT INTO adres (adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) VALUES (?, ?, ?, ?, ?, ?)");
            pst.setInt(1, adres.getId());
            pst.setString(2, adres.getPostcode());
            pst.setString(3, adres.getHuisnummer());
            pst.setString(4, adres.getStraat());
            pst.setString(5, adres.getWoonplaats());
            pst.setInt(6, adres.getReiziger_id().getId());
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
        try {
            // Update het adres met de id uit het meegegeven object
            PreparedStatement pst = conn.prepareStatement("UPDATE adres SET adres_id = ?, postcode = ?, huisnummer = ?, straat = ?, woonplaats = ?, reiziger_id = ? WHERE adres_id = ?");
            pst.setInt(1, adres.getId());
            pst.setString(2, adres.getPostcode());
            pst.setString(3, adres.getHuisnummer());
            pst.setString(4, adres.getStraat());
            pst.setString(5, adres.getWoonplaats());
            pst.setInt(6, adres.getReiziger_id().getId());
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
        try {
            // Delete het adres met de id uit het meegegeven object
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
    public Adres findByReiziger(Reiziger reiziger) {
        try {
            // Selecteer het adres met de reiziger_id van het meegegeven reiziger object
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM adres WHERE reiziger_id = ?");
            pst.setInt(1, reiziger.getId());
            ResultSet rs = pst.executeQuery();
            Adres a = null;

            // Maak een object van het geselecteerde adres
            while (rs.next()) {
                a = new Adres(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rdao.findById(rs.getInt(6)));
            }

            rs.close();
            pst.close();
            return a;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Adres> findAll() {
        try {
            // Selecteer alle adressen
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM adres");
            ResultSet rs = pst.executeQuery();
            List alleAdressen = new ArrayList();

            // Voor elk geselecteerd adres, maak een object en voeg deze toe aan een lijst
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
