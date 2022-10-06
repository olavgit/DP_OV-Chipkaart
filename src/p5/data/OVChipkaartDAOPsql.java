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
            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO ov_chipkaart(geldig_tot, klasse, saldo, reiziger_id, kaart_nummer) VALUES(?, ?, ?, ?, ?)");
            prepareovChipkaart(ovChipkaart, preparedStatement);
            List<Product> productList = ovChipkaart.getProductList();
            if (productList != null) {
                for (Product p : productList) {
                    PreparedStatement productStatement = conn.prepareStatement("INSERT INTO ov_chipkaart_product(kaart_nummer, product_nummer) VALUES(?, ?)");
                    productStatement.setInt(1, ovChipkaart.getKaart_nummer());
                    productStatement.setInt(2, p.getId());
                    productStatement.executeUpdate();
                    pdao.update(p);
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(OVChipkaart ovChipkaart) {
        try {
            PreparedStatement pst = conn.prepareStatement("UPDATE ov_chipkaart SET geldig_tot = ?, klasse = ?, saldo = ?, reiziger_id = ? WHERE kaart_nummer = ?");
            List<Product> productList = ovChipkaart.getProductList();
            if (productList != null) {
                for (Product p : productList) {
                    pdao.update(p);
                }
            }
            return prepareovChipkaart(ovChipkaart, pst);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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

            List<Product> productList = pdao.findByOVChipkaart(ovChipkaart);
            if (productList != null) {
                for (Product p : productList) {
                    p.removeChipkaart(ovChipkaart);
                    pdao.update(p);
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

    private boolean prepareovChipkaart(OVChipkaart ovChipkaart, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setDate(1, ovChipkaart.getGeldig_tot());
        preparedStatement.setInt(2, ovChipkaart.getKlasse());
        preparedStatement.setDouble(3, ovChipkaart.getSaldo());
        preparedStatement.setInt(4, ovChipkaart.getReiziger().getId());
        preparedStatement.setInt(5, ovChipkaart.getKaart_nummer());
        preparedStatement.executeUpdate();
        preparedStatement.close();
        return true;
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
                        rs.getInt(1),
                        rs.getDate(2),
                        rs.getInt(3),
                        rs.getDouble(4),
                        rdao.findById(rs.getInt(5)));
            }

            List<Product> productList = pdao.findByOVChipkaart(ovChipkaart);
            if (productList != null) {
                ovChipkaart.setProductList(productList);
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
            List<OVChipkaart> alleOVChipkaarten = new ArrayList();

            while (rs.next()) {
                OVChipkaart o = new OVChipkaart(
                        rs.getInt(1),
                        rs.getDate(2),
                        rs.getInt(3),
                        rs.getDouble(4),
                        rdao.findById(rs.getInt(5)));
                alleOVChipkaarten.add(o);
            }

            for (OVChipkaart ov : alleOVChipkaarten) {
                List<Product> productList = pdao.findByOVChipkaart(ov);
                if (productList != null) {
                    ov.setProductList(productList);
                }
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
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT o.kaart_nummer, o.geldig_tot, o.klasse, o.saldo, o.reiziger_id FROM ov_chipkaart_product as ovc JOIN ov_chipkaart as o ON ovc.kaart_nummer = o.kaart_nummer WHERE product_nummer = ?");
            preparedStatement.setInt(1, product.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            List<OVChipkaart> ovChipkaartList = new ArrayList<>();
            while (resultSet.next()) {
                OVChipkaart ovChipkaart = new OVChipkaart(
                        resultSet.getInt("kaart_nummer"),
                        resultSet.getDate("geldig_tot"),
                        resultSet.getInt("klasse"),
                        resultSet.getDouble("saldo"),
                        rdao.findById(resultSet.getInt("reiziger_id")));
                ovChipkaartList.add(ovChipkaart);
            }
            preparedStatement.close();
            resultSet.close();
            return ovChipkaartList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
