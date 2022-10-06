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
            List<OVChipkaart> ovChipkaartList = product.getOvChipkaartList();
            prepareProduct(product, pst);
            if (ovChipkaartList != null) {
                for (OVChipkaart o : ovChipkaartList) {
                    PreparedStatement productStatement = conn.prepareStatement("INSERT INTO ov_chipkaart_product(kaart_nummer, product_nummer) VALUES(?, ?)");
                    productStatement.setInt(1, o.getKaart_nummer());
                    productStatement.setInt(2, product.getId());
                    productStatement.executeUpdate();
                    productStatement.close();
                    odao.update(o);
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
    public boolean update(Product product) {
        try {
            PreparedStatement pst = conn.prepareStatement("UPDATE product SET naam = ?, beschrijving = ?, prijs = ? WHERE product_nummer = ?");
            prepareProduct(product, pst);
            List<OVChipkaart> ovChipkaartList = product.getOvChipkaartList();
            if (ovChipkaartList != null) {
                for (OVChipkaart o : ovChipkaartList) {
                    odao.update(o);
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
    public boolean delete(Product product) {
        try {
            PreparedStatement pst = conn.prepareStatement("DELETE FROM product WHERE product_nummer = ?");
            PreparedStatement dst = conn.prepareStatement("DELETE FROM ov_chipkaart_product WHERE product_nummer = ?");
            pst.setInt(1, product.getId());
            dst.setInt(1, product.getId());
            dst.executeUpdate();
            dst.close();
            List<OVChipkaart> ovChipkaartList = odao.findByProduct(product);
            if (ovChipkaartList != null) {
                for (OVChipkaart o : ovChipkaartList) {
                    o.removeProduct(product);
                    odao.update(o);
                }
            }
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

    private void prepareProduct(Product product, PreparedStatement pst) throws SQLException {
        pst.setString(1, product.getNaam());
        pst.setString(2, product.getBeschrijving());
        pst.setDouble(3, product.getPrijs());
        pst.setInt(4, product.getId());
        pst.executeUpdate();
        pst.close();
    }

    @Override
    public List<Product> findAll() {
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * from product");
            List<Product> productList = new ArrayList<>();
            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("product_nummer"),
                        rs.getString("naam"),
                        rs.getString("beschrijving"),
                        rs.getDouble("prijs"));
                productList.add(product);
            }

            for (Product p : productList) {
                List<OVChipkaart> ovChipkaartList = odao.findByProduct(p);
                if (ovChipkaartList != null) {
                    p.setOvChipkaartList(ovChipkaartList);
                }
            }

            st.close();
            rs.close();
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
            PreparedStatement pst = conn.prepareStatement("SELECT p.product_nummer, p.naam, p.beschrijving, p.prijs FROM ov_chipkaart_product as ovc JOIN product as p ON ovc.product_nummer = p.product_nummer WHERE kaart_nummer = ?");
            pst.setInt(1, ovChipkaart.getKaart_nummer());
            ResultSet rs = pst.executeQuery();
            List<Product> productList = new ArrayList<>();
            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("product_nummer"),
                        rs.getString("naam"),
                        rs.getString("beschrijving"),
                        rs.getDouble("prijs"));
                productList.add(product);
            }

            for (Product p : productList) {
                List<OVChipkaart> ovChipkaartList = odao.findByProduct(p);
                if (ovChipkaartList != null) {
                    p.setOvChipkaartList(ovChipkaartList);
                }
            }

            pst.close();
            rs.close();
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
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM product WHERE product_nummer  = ?");
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            Product product = null;
            while (rs.next()) {
                product = new Product(
                        rs.getInt("product_nummer"),
                        rs.getString("naam"),
                        rs.getString("beschrijving"),
                        rs.getDouble("prijs"));
            }

            List<OVChipkaart> ovChipkaartList = odao.findByProduct(product);
            if (ovChipkaartList != null) {
                product.setOvChipkaartList(ovChipkaartList);
            }

            pst.close();
            rs.close();
            return product;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
