package p3;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Main {
    private static Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/ovchip";
        return DriverManager.getConnection(url, "postgres", "Jopie06!");
    }

    private static void closeConnection() throws SQLException {
        getConnection().close();
    }

    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(6, "S", "", "Boers", Date.valueOf(gbdatum));
        System.out.print("[Test] ReizigerDAO.save()\nEerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // Update reiziger
        System.out.println("[Test] ReizigerDAO.update()\nData voor update: " + rdao.findById(6));
        Reiziger sietskeUpdate = new Reiziger(6, "S", "", "Schaaps", Date.valueOf(gbdatum));
        rdao.update(sietskeUpdate);
        System.out.println("Data na update: " + rdao.findById(6) + "\n");

        // Verwijder een reiziger
        System.out.print("[Test] ReizigerDAO.delete()\nEerst " + reizigers.size() + " reizigers, na ReizigerDAO.delete() ");
        rdao.delete(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // Zoek een reiziger op ID
        System.out.println("[Test] ReizigerDAO.findById()\nVerwachte uitkomst: Reiziger{id=1, voorletters='G', tussenvoegsel='van', achternaam='Rijn', geboortedatum=2002-09-17}");
        System.out.println("Echte uitkomst: " + rdao.findById(1) + "\n");

        // Zoek een reiziger op geboortedatum
        System.out.println("[Test] ReizigerDAO.findByGbDatum()\nVerwachte uitkomst: Reiziger{id=2, voorletters='B', tussenvoegsel='van', achternaam='Rijn', geboortedatum=2002-10-22}");
        System.out.println("Echte uitkomst: " + rdao.findByGbDatum("2002-10-22") + "\n");
    }

    private static void testAdresDAO(AdresDAO adao, ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test AdresDAO -------------");

        // Haal alle adressen op uit de database
        List<Adres> adressen = adao.findAll();
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende reizigers:");
        for (Adres a : adressen) {
            System.out.println(a);
        }
        System.out.println();

        // Maak een nieuw adres aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(6, "S", "", "Boers", Date.valueOf(gbdatum));
        Adres a = new Adres(6, "4212CX", "63", "Spijkse Kweldijk", "Spijk", sietske);

    }

    public static void main(String[] args) throws SQLException {
        Connection conn = getConnection();
        ReizigerDAOPsql rdao = new ReizigerDAOPsql(conn);
        AdresDAOPsql adao = new AdresDAOPsql(conn, rdao);
        testReizigerDAO(rdao);
        testAdresDAO(adao, rdao);
        closeConnection();
    }
}
