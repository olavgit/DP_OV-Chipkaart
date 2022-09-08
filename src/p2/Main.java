package p2;

import java.sql.*;
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
        Reiziger sietske = new Reiziger(6, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        System.out.print("[Test] ReizigerDAO.save()\nEerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // Update reiziger
        System.out.println("[Test] ReizigerDAO.update()\nData voor update: " + rdao.findById(6));
        Reiziger sietskeUpdate = new Reiziger(6, "S", "", "Schaaps", java.sql.Date.valueOf(gbdatum));
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

    public static void main(String[] args) throws SQLException {
        Connection conn = getConnection();
        ReizigerDAO rdao = new ReizigerDAOPsql(conn);
        testReizigerDAO(rdao);
        closeConnection();
    }
}
