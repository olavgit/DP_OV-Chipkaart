package p4.data;

import p4.domain.OVChipkaart;
import p4.domain.Reiziger;

import java.util.List;

public interface OVChipkaartDAO {
    public boolean save(OVChipkaart ovChipkaart);
    public boolean update(OVChipkaart ovChipkaart);
    public boolean delete(OVChipkaart ovChipkaart);
    public List<OVChipkaart> findByReiziger(Reiziger reiziger);
    public OVChipkaart findById(int id);
    public List<OVChipkaart> findAll();
}
