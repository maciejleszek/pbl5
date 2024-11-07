package z4;

public class Produkt {
    private String nazwa;

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    private float cena;

    public float getCena() {
        return cena;
    }

    public void setCena(float cena) {
        this.cena = cena;
    }

    public Produkt() {

    }

    public Produkt(String nazwa) {
        setNazwa(nazwa);
    }

    public Produkt(String nazwa, float cena) {
        setNazwa(nazwa);
        setCena(cena);
    }
}
