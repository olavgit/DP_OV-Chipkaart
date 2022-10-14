package p5.data;

import p5.domain.OVChipkaart;
import p5.domain.Product;
import p5.domain.Reiziger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {
    private Connection conn;
    private ReizigerDAO rdao;
    private ProductDAO pdao;

    public OVChipkaartDAOPsql(Connection conn, ReizigerDAO rdao) {
        this.conn = conn;
        this.rdao = rdao;
        this.pdao = new ProductDAOPsql(conn, this);
    }

    @Override
    public boolean save(OVChipkaart ovChipkaart) {
        try {
            PreparedStatement pst = conn.prepareStatement("INSERT INTO ov_chipkaart(geldig_tot, klasse, saldo, reiziger_id, kaart_nummer) VALUES(?, ?, ?, ?, ?)");
            pst.setDate(1, ovChipkaart.getGeldig_tot());
            pst.setInt(2, ovChipkaart.getKlasse());
            pst.setDouble(3, ovChipkaart.getSaldo());
            pst.setInt(4, ovChipkaart.getReiziger().getId());
            pst.setInt(5, ovChipkaart.getKaart_nummer());
            pst.executeUpdate();
            pst.close();

            List<Product> pl = ovChipkaart.getProductList();
            if (pl != null) {
                for (Product p : pl) {
                    PreparedStatement is = conn.prepareStatement("INSERT INTO ov_chipkaart_product(kaart_nummer, product_nummer) VALUES(?, ?)");
                    is.setInt(1, ovChipkaart.getKaart_nummer());
                    is.setInt(2, p.getId());
                    is.executeUpdate();
                    is.close();
                }
            }
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(OVChipkaart ovChipkaart) {
        try {
            PreparedStatement pst = conn.prepareStatement("UPDATE ov_chipkaart SET geldig_tot = ?, klasse = ?, saldo = ?, reiziger_id = ? WHERE kaart_nummer = ?");
            pst.setDate(1, ovChipkaart.getGeldig_tot());
            pst.setInt(2, ovChipkaart.getKlasse());
            pst.setDouble(3, ovChipkaart.getSaldo());
            pst.setInt(4, ovChipkaart.getReiziger().getId());
            pst.setInt(5, ovChipkaart.getKaart_nummer());
            pst.executeUpdate();
            pst.close();
            PreparedStatement dst = conn.prepareStatement("DELETE FROM ov_chipkaart_product WHERE kaart_nummer = ?");
            dst.setInt(1, ovChipkaart.getKaart_nummer());
            dst.executeUpdate();
            dst.close();

            List<Product> pl = ovChipkaart.getProductList();
            if (pl != null) {
                for (Product p : pl) {
                    PreparedStatement is = conn.prepareStatement("INSERT INTO ov_chipkaart_product(kaart_nummer, product_nummer) VALUES(?, ?)");
                    is.setInt(1, ovChipkaart.getKaart_nummer());
                    is.setInt(2, p.getId());
                    is.executeUpdate();
                    is.close();
                }
            }
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) {
        try {
            PreparedStatement pst = conn.prepareStatement("DELETE FROM ov_chipkaart WHERE kaart_nummer = ?");
            pst.setInt(1, ovChipkaart.getKaart_nummer());

            PreparedStatement dst = conn.prepareStatement("DELETE FROM ov_chipkaart_product WHERE kaart_nummer = ?");
            dst.setInt(1, ovChipkaart.getKaart_nummer());
            dst.executeUpdate();
            dst.close();

            pst.executeUpdate();
            pst.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) {
        try {
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM ov_chipkaart WHERE reiziger_id = ?");
            pst.setInt(1, reiziger.getId());
            ResultSet rs = pst.executeQuery();
            List<OVChipkaart> ovchipkaarten = new ArrayList();

            while (rs.next()) {
                OVChipkaart o = new OVChipkaart(
                        rs.getInt(1),
                        rs.getDate(2),
                        rs.getInt(3),
                        rs.getDouble(4),
                        reiziger);
                ovchipkaarten.add(o);
            }

            rs.close();
            pst.close();
            return ovchipkaarten;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public OVChipkaart findById(int id) {
        try {
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM ov_chipkaart WHERE kaart_nummer = ?");
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            OVChipkaart ovChipkaart = null;

            while (rs.next()) {
                ovChipkaart = new OVChipkaart(
                        rs.getInt("kaart_nummer"),
                        rs.getDate("geldig_tot"),
                        rs.getInt("klasse"),
                        rs.getDouble("saldo"),
                        rdao.findById(rs.getInt("reiziger_id")));
                ovChipkaart.setProductList(pdao.findByOVChipkaart(ovChipkaart));
            }

            rs.close();
            pst.close();
            return ovChipkaart;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<OVChipkaart> findAll() {
        try {
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM ov_chipkaart");
            ResultSet rs = pst.executeQuery();
            List<OVChipkaart> alleOVChipkaarten = new ArrayList<>();

            while (rs.next()) {
                OVChipkaart o = new OVChipkaart(
                        rs.getInt("kaart_nummer"),
                        rs.getDate("geldig_tot"),
                        rs.getInt("klasse"),
                        rs.getDouble("saldo"),
                        rdao.findById(rs.getInt("reiziger_id")));
                o.setProductList(pdao.findByOVChipkaart(o));
                alleOVChipkaarten.add(o);
            }

            rs.close();
            pst.close();
            return alleOVChipkaarten;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<OVChipkaart> findByProduct(Product product) {
        try {
            PreparedStatement pst = conn.prepareStatement("SELECT o.kaart_nummer, o.geldig_tot, o.klasse, o.saldo, o.reiziger_id FROM ov_chipkaart_product AS ovcp JOIN ov_chipkaart AS o ON ovcp.kaart_nummer = o.kaart_nummer WHERE product_nummer = ?");
            pst.setInt(1, product.getId());
            ResultSet rs = pst.executeQuery();
            List<OVChipkaart> ovChipkaarten = new ArrayList<>();

            while (rs.next()) {
                OVChipkaart o = new OVChipkaart(
                        rs.getInt("kaart_nummer"),
                        rs.getDate("geldig_tot"),
                        rs.getInt("klasse"),
                        rs.getDouble("saldo"),
                        rdao.findById(rs.getInt("reiziger_id")));
                ovChipkaarten.add(o);
            }

            pst.close();
            rs.close();
            return ovChipkaarten;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
