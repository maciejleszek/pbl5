package z4;

public class ProduktWKoszyku extends Produkt {
    private int liczbaSztukProduktu;

    public int getLiczbaSztukProduktu() {
        return liczbaSztukProduktu;
    }

    public void setLiczbaSztukProduktu(int liczbaSztukProduktu) {
        this.liczbaSztukProduktu = liczbaSztukProduktu;
    }

    public ProduktWKoszyku() {

    }

    public ProduktWKoszyku(String nazwa, float cena, int liczbaSztukProduktu) {
        super(nazwa, cena);
        setLiczbaSztukProduktu(liczbaSztukProduktu);
    }

    public ProduktWKoszyku(Produkt produkt, int liczbaSztukProduktu) {
        super(produkt.getNazwa(), produkt.getCena());
        setLiczbaSztukProduktu(liczbaSztukProduktu);
    }

    public String toString() {
//        return getClass().getSimpleName() + " "
//               + "[nazwa=\"" + getNazwa() + "\"] "
//               + "[cena=" + getCena() + "] "
//               + "[liczbaSztukProduktu=" + getLiczbaSztukProduktu() + "]";
        return getLiczbaSztukProduktu() + " * " + getNazwa() + " @ " + getCena()
               + "/szt. = " + getLiczbaSztukProduktu() * getCena();
    }
}
