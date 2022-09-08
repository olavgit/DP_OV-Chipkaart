package p3;

import java.sql.*
import java.util.List;

public class AdresDAOPsql implements AdresDAO {
    Connection conn;

    public AdresDAOPsql(Connection conn) throws SQLException {
        this.conn = conn;
    }

    @Override
    public boolean save(Adres adres) {
        return false;
    }

    @Override
    public boolean update(Adres adres) {
        return false;
    }

    @Override
    public boolean delete(Adres adres) {
        return false;
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) {
        return null;
    }

    @Override
    public List<Reiziger> findAll() {
        return null;
    }
}
