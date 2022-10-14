package p5.data;

import p5.domain.OVChipkaart;
import p5.domain.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOPsql implements ProductDAO {
    private Connection conn;
    private OVChipkaartDAO odao;

    public ProductDAOPsql(Connection conn, OVChipkaartDAO odao) {
        this.conn = conn;
        this.odao = odao;
    }

    @Override
    public boolean save(Product product) {
        try {
            PreparedStatement pst = conn.prepareStatement("INSERT INTO product(naam, beschrijving, prijs, product_nummer) VALUES(?, ?, ?, ?)");
            pst.setString(1, product.getNaam());
            pst.setString(2, product.getBeschrijving());
            pst.setDouble(3, product.getPrijs());
            pst.setInt(4, product.getId());
            pst.executeUpdate();
            pst.close();
            List<Integer> ovchipkaarten = product.getOvChipkaartList();

            for (Integer o : ovchipkaarten) {
                PreparedStatement sst = conn.prepareStatement("INSERT INTO ov_chipkaart_product(kaart_nummer, product_nummer) VALUES(?, ?)");
                sst.setInt(1, o);
                sst.setInt(2, product.getId());
                sst.executeUpdate();
                sst.close();
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
    public boolean update(Product product) {
        try {
            PreparedStatement pst = conn.prepareStatement("UPDATE product SET naam = ?, beschrijving = ?, prijs = ? WHERE product_nummer = ?");
            pst.setString(1, product.getNaam());
            pst.setString(2, product.getBeschrijving());
            pst.setDouble(3, product.getPrijs());
            pst.setInt(4, product.getId());
            pst.executeUpdate();
            pst.close();

            PreparedStatement dst = conn.prepareStatement("DELETE FROM ov_chipkaart_product WHERE product_nummer = ?");
            dst.setInt(1, product.getId());
            dst.executeUpdate();
            dst.close();
            List<Integer> ovchipkaarten = product.getOvChipkaartList();

            for (Integer o : ovchipkaarten) {
                PreparedStatement sst = conn.prepareStatement("INSERT INTO ov_chipkaart_product(kaart_nummer, product_nummer) VALUES(?, ?)");
                sst.setInt(1, o);
                sst.setInt(2, product.getId());
                sst.executeUpdate();
                sst.close();
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
    public boolean delete(Product product) {
        try {
            PreparedStatement pst = conn.prepareStatement("DELETE FROM product WHERE product_nummer = ?");
            pst.setInt(1, product.getId());
            PreparedStatement dst = conn.prepareStatement("DELETE FROM ov_chipkaart_product WHERE product_nummer = ?");
            dst.setInt(1, product.getId());
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
    public List<Product> findAll() {
        try {
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM product");
            ResultSet rs = pst.executeQuery();
            List<Product> productList = new ArrayList<>();

            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("product_nummer"),
                        rs.getString("naam"),
                        rs.getString("beschrijving"),
                        rs.getDouble("prijs"));
                PreparedStatement ovs = conn.prepareStatement("SELECT kaart_nummer FROM ov_chipkaart_product WHERE product_nummer = ?");
                ovs.setInt(1, p.getId());
                ResultSet ovrs = ovs.executeQuery();

                while (ovrs.next()) {
                    p.addChipkaart(ovrs.getInt("kaart_nummer"));
                }
                productList.add(p);
                ovrs.close();
                ovs.close();
            }
            return productList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) {
        try {
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM product WHERE product_nummer IN (SELECT product_nummer FROM ov_chipkaart_product WHERE kaart_nummer = ?)");
            pst.setInt(1, ovChipkaart.getKaart_nummer());
            ResultSet rs = pst.executeQuery();
            List<Product> productList = new ArrayList<>();

            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("product_nummer"),
                        rs.getString("naam"),
                        rs.getString("beschrijving"),
                        rs.getDouble("prijs"));
                PreparedStatement ovs = conn.prepareStatement("SELECT kaart_nummer FROM ov_chipkaart_product WHERE product_nummer = ?");
                ovs.setInt(1, p.getId());
                ResultSet ovrs = ovs.executeQuery();

                while (ovrs.next()) {
                    p.addChipkaart(ovrs.getInt("kaart_nummer"));
                }
                productList.add(p);
                ovrs.close();
                ovs.close();
            }
            return productList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Product findById(int id) {
        try {
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM product WHERE product_nummer = ?");
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            Product p = null;

            while (rs.next()) {
                p = new Product(
                        rs.getInt("product_nummer"),
                        rs.getString("naam"),
                        rs.getString("beschrijving"),
                        rs.getDouble("prijs"));
                PreparedStatement ovs = conn.prepareStatement("SELECT kaart_nummer FROM ov_chipkaart_product WHERE product_nummer = ?");
                ovs.setInt(1, p.getId());
                ResultSet ovrs = ovs.executeQuery();

                while (ovrs.next()) {
                    p.addChipkaart(ovrs.getInt("kaart_nummer"));
                }
                ovrs.close();
                ovs.close();
            }
            return p;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
