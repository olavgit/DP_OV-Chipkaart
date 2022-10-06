package p5.data;

import p5.domain.OVChipkaart;
import p5.domain.Product;
import p5.domain.Reiziger;

import java.util.List;

public interface OVChipkaartDAO {
    public boolean save(OVChipkaart ovChipkaart);
    public boolean update(OVChipkaart ovChipkaart);
    public boolean delete(OVChipkaart ovChipkaart);
    public List<OVChipkaart> findByReiziger(Reiziger reiziger);
    public List<OVChipkaart> findByProduct(Product product);
    public List<OVChipkaart> findAll();
    public OVChipkaart findById(int id);
}
