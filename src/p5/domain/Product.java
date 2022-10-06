package p5.domain;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private int id;
    private String naam;
    private String beschrijving;
    private Double prijs;
    private List<OVChipkaart> ovChipkaartList;

    public Product(int id, String naam, String beschrijving, Double prijs) {
        this.id = id;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
    }

    public Product(int id, String naam, String beschrijving, Double prijs, List<OVChipkaart> ovChipkaartList) {
        this.id = id;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
        this.ovChipkaartList = ovChipkaartList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public Double getPrijs() {
        return prijs;
    }

    public void setPrijs(Double prijs) {
        this.prijs = prijs;
    }

    public List<OVChipkaart> getOvChipkaartList() {
        return ovChipkaartList;
    }

    public void setOvChipkaartList(List<OVChipkaart> ovChipkaartList) {
        this.ovChipkaartList = ovChipkaartList;
    }

    public void addChipkaart(OVChipkaart ovChipkaart) {
        if (ovChipkaartList == null) {
            this.ovChipkaartList = new ArrayList<>();
        }
        this.ovChipkaartList.add(ovChipkaart);
    }

    public void removeChipkaart(OVChipkaart ovChipkaart) {
        this.ovChipkaartList.remove(ovChipkaart);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (ovChipkaartList.size() > 1) {
            for (OVChipkaart o : ovChipkaartList) {
                stringBuilder.append(o);
                stringBuilder.append(", ");
            }
        }
        for (OVChipkaart o : ovChipkaartList) {
            stringBuilder.append(o);
        }
        String ovChipkaarten = stringBuilder.toString();
        return String.format("Product {%s, %s, %s, €%s, %s}", id, naam, beschrijving, prijs, ovChipkaarten);
    }
}
