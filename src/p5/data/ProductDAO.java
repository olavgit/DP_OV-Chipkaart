package p5.data;

import p5.domain.OVChipkaart;
import p5.domain.Product;

import java.util.List;

public interface ProductDAO {
    boolean save(Product product);
    boolean update(Product product);
    boolean delete(Product product);
    List<Product> findAll();
    List<Product> findByOVChipkaart(OVChipkaart ovChipkaart);
    Product findById(int id);

}
