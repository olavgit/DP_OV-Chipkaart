package p5.data;

import p5.domain.Adres;
import p5.domain.OVChipkaart;
import p5.domain.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    private Connection conn;
    private AdresDAO adao;
    private OVChipkaartDAO odao;

    public ReizigerDAOPsql(Connection conn) throws SQLException {
        this.conn = conn;
        this.adao = new AdresDAOPsql(this.conn, this);
        this.odao = new OVChipkaartDAOPsql(this.conn, this);
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

            Adres adres = reiziger.getAdres();
            if (adres != null) {
                adao.save(adres);
            }

            List<OVChipkaart> ovChipkaartList = reiziger.getOvChipkaartList();
            if (ovChipkaartList != null) {
                for (OVChipkaart o : ovChipkaartList) {
                    odao.save(o);
                }
            }

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

            Adres adres = reiziger.getAdres();
            if (adres != null) {
                adao.update(adres);
            }

            List<OVChipkaart> ovChipkaartList = reiziger.getOvChipkaartList();
            if (ovChipkaartList != null) {
                for (OVChipkaart o : ovChipkaartList) {
                    odao.update(o);
                }
            }

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
            List<OVChipkaart> ovChipkaartList = reiziger.getOvChipkaartList();

            if (ovChipkaartList != null) {
                for (OVChipkaart o : ovChipkaartList) {
                    odao.delete(o);
                }
            }

            Adres a = reiziger.getAdres();
            if (a != null) {
                adao.delete(a);
            }
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
            PreparedStatement pst = conn.prepareStatement("SELECT * from reiziger WHERE reiziger_id = ?");
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            Reiziger r = null;
            while (rs.next()) {
                r = new Reiziger(
                        rs.getInt("reiziger_id"),
                        rs.getString("voorletters"),
                        rs.getString("tussenvoegsel"),
                        rs.getString("achternaam"),
                        rs.getDate("geboortedatum"));

                List<OVChipkaart> ovChipkaartList = odao.findByReiziger(r);
                r.setOvChipkaartList(ovChipkaartList);

                Adres a = adao.findByReiziger(r);
                if (a != null) {
                    r.setAdres(a);
                }
            }
            pst.close();
            rs.close();
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
            PreparedStatement pst = conn.prepareStatement("SELECT * from reiziger WHERE geboortedatum = ?");
            Date sqlDate = Date.valueOf(date);
            pst.setDate(1, sqlDate);
            ResultSet rs = pst.executeQuery();
            List<Reiziger> gbReizigers = new ArrayList<>();

            while (rs.next()) {
                Reiziger r = new Reiziger(
                        rs.getInt("reiziger_id"),
                        rs.getString("voorletters"),
                        rs.getString("tussenvoegsel"),
                        rs.getString("achternaam"),
                        rs.getDate("geboortedatum"));
                gbReizigers.add(r);

                List<OVChipkaart> ovChipkaartList = odao.findByReiziger(r);
                r.setOvChipkaartList(ovChipkaartList);

                Adres a = adao.findByReiziger(r);
                if (a != null) {
                    r.setAdres(a);
                }
            }
            pst.close();
            rs.close();
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
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * from reiziger");
            List<Reiziger> alleReizigers = new ArrayList<>();

            while (rs.next()) {
                Reiziger r = new Reiziger(
                        rs.getInt("reiziger_id"),
                        rs.getString("voorletters"),
                        rs.getString("tussenvoegsel"),
                        rs.getString("achternaam"),
                        rs.getDate("geboortedatum"));
                alleReizigers.add(r);

                List<OVChipkaart> ovChipkaartList = odao.findByReiziger(r);
                r.setOvChipkaartList(ovChipkaartList);

                Adres a = adao.findByReiziger(r);
                if (a != null) {
                    r.setAdres(a);
                }
            }
            st.close();
            rs.close();
            return alleReizigers;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
