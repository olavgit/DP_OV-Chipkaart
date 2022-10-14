package p5;

import p5.data.*;
import p5.domain.Adres;
import p5.domain.OVChipkaart;
import p5.domain.Product;
import p5.domain.Reiziger;

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

        // Verwijder een reiziger
        System.out.print("[Test] ReizigerDAO.delete()\nEerst " + reizigers.size() + " reizigers, na ReizigerDAO.delete() ");
        rdao.delete(rdao.findById(6));
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

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

        // Zoek een reiziger op ID
        System.out.println("[Test] ReizigerDAO.findById()\nVerwachte uitkomst: Reiziger{id=1, voorletters='G', tussenvoegsel='van', achternaam='Rijn', geboortedatum=2002-09-17, adresStraat=Visschersplein}");
        System.out.println("Echte uitkomst: " + rdao.findById(1) + "\n");

        // Zoek een reiziger op geboortedatum
        System.out.println("[Test] ReizigerDAO.findByGbDatum()\nVerwachte uitkomst: [Reiziger{id=2, voorletters='B', tussenvoegsel='van', achternaam='Rijn', geboortedatum=2002-10-22, adresStraat=Jaarbeursplein}]");
        System.out.println("Echte uitkomst: " + rdao.findByGbDatum("2002-10-22") + "\n");
    }

    private static void testAdresDAO(AdresDAO adao, ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test AdresDAO -------------");

        // Haal alle adressen op uit de database
        List<Adres> adressen = adao.findAll();
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende adressen:");
        for (Adres a : adressen) {
            System.out.println(a);
        }
        System.out.println();

        // Maak een nieuw adres en persisteer deze in de database
        Adres a1 = new Adres(6, "4212CX", "63", "Spijkse Kweldijk", "Spijk", rdao.findById(6));
        System.out.print("[Test] AdresDAO.save()\nEerst " + adressen.size() + " adressen, na AdresDAO.save() ");
        adao.save(a1);
        adressen = adao.findAll();
        System.out.println(adressen.size() + " adressen\n");

        // Verwijder een adres
        System.out.print("[Test] AdresDAO.delete()\nEerst " + adressen.size() + " adressen, na AdresDAO.delete() ");
        adao.delete(a1);
        adressen = adao.findAll();
        System.out.println(adressen.size() + " adressen\n");

        // Zoek een adres op reiziger
        System.out.println("[Test] AdresDAO.findByReiziger()\nVerwachte uitkomst: Adres{id=1, postcode='3511LX', huisnummer='37', straat='Visschersplein', woonplaats='Utrecht', reizigerAchternaam=Rijn}");
        System.out.println("Echte uitkomst: " + adao.findByReiziger(rdao.findById(1)));
    }

    private static void testOVChipkaartDAO(OVChipkaartDAO odao, ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test OVChipkaartDAO -------------");

        // Haal alle ov chipkaarten op uit de database
        List<OVChipkaart> ovchipkaarten = odao.findAll();
        System.out.println("[Test] OVChipkaartDAO.findAll() geeft de volgende adressen:");
        for (OVChipkaart o : ovchipkaarten) {
            System.out.println(o);
        }
        System.out.println();

        // Maak een nieuwe OVChipkaart en persisteer deze in de database
        String verloopdatum = "2022-07-10";
        OVChipkaart o1 = new OVChipkaart(8, Date.valueOf(verloopdatum), 2, 5, rdao.findById(6));
        System.out.print("[Test] OVChipkaartDAO.save()\nEerst " + ovchipkaarten.size() + " ovchipkaarten, na OVChipkaartDAO.save() ");
        odao.save(o1);
        ovchipkaarten = odao.findAll();
        System.out.println(ovchipkaarten.size() + " ovchipkaarten\n");

        // Verwijder een OVChipkaart
        System.out.print("[Test] OVChipkaartDAO.delete()\nEerst " + ovchipkaarten.size() + " ovchipkaarten, na OVChipkaartDAO.delete() ");
        odao.delete(o1);
        ovchipkaarten = odao.findAll();
        System.out.println(ovchipkaarten.size() + " ovchipkaarten\n");

        // Zoek een ovchipkaart op reiziger
        System.out.println(odao.findByReiziger(rdao.findById(2)) + "\n");

        // Zoek een ovchipkaart op id
        System.out.println(odao.findById(35283));
    }

    private static void testProductDAO(ProductDAO pdao, OVChipkaartDAO odao, ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ProductDAO -------------");

        // Vind een lijst Producten op basis van een OVChipkaart
        System.out.println("[Test] ProductDAO.findByOVChipkaart() geeft de volgende producten:");
        List<OVChipkaart> ovChipkaartList = odao.findByReiziger(rdao.findById(2));
        for (OVChipkaart o : ovChipkaartList) {
            List<Product> productList = pdao.findByOVChipkaart(o);
            for (Product p : productList) {
                System.out.println(p);
            }
        }
        System.out.println();

        // Haal alle producten op uit de database
        System.out.println("[Test] ProductDAO.findAll() vindt de volgende producten:");
        List<Product> productList = pdao.findAll();
        for (Product p : productList) {
            System.out.println(p);
        }
        System.out.println();

        // Maak een nieuw product aan en persisteer deze in de database
        Product newProduct = new Product(7, "Dagkaart 1e klas", "Een hele dag onbeperkt reizen met de trein.", 75.00);
        System.out.print("[Test] Eerst " + productList.size() + " producten, na ProductDAO.save() ");
        newProduct.addChipkaart(odao.findById(57401).getKaart_nummer());
        pdao.save(newProduct);
        productList = pdao.findAll();
        System.out.println(productList.size() + " producten\n");

        // Update een product uit de database
        String beschrijvingUpdate = "Geen heerlijke reis met de trein.";
        Product updateProduct = new Product(7, "Dagkaart 1e klas", beschrijvingUpdate, 75.00);
        System.out.print("[Test] Product voor update: \n" + pdao.findById(7));
        pdao.update(updateProduct);
        System.out.println("\nNa de update: \n" + pdao.findById(7));
        System.out.println();

        // Delete een product uit de database
        System.out.print("[Test] Eerst " + productList.size() + " producten, na ProductDAO.delete() ");
        pdao.delete(pdao.findById(7));
        productList = pdao.findAll();
        System.out.println(productList.size() + " producten\n");
    }

    public static void main(String[] args) throws SQLException {
        Connection conn = getConnection();
        ReizigerDAO rdao = new ReizigerDAOPsql(conn);
        AdresDAO adao = new AdresDAOPsql(conn, rdao);
        OVChipkaartDAO odao = new OVChipkaartDAOPsql(conn, rdao);
        ProductDAO pdao = new ProductDAOPsql(conn, odao);
        testReizigerDAO(rdao);
        testAdresDAO(adao, rdao);
        testOVChipkaartDAO(odao, rdao);
        testProductDAO(pdao, odao, rdao);
        closeConnection();
    }
}
