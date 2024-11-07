package z4;

public class Klient {

    private final Koszyk koszyk = new Koszyk();

    public Koszyk getKoszyk() {
        return koszyk;
    }

    private String identyfikator;

    public String getIdentyfikator() {
        return identyfikator;
    }

    public void setIdentyfikator(String identyfikator) {
        this.identyfikator = identyfikator;
    }

    public void addProductToCart(Produkt produkt, int liczba) {
        getKoszyk().push(new ProduktWKoszyku(produkt, liczba));
    }

    public void serve() {
        float wartoscKoszyka = 0;
        System.out.println("Obsługuję klienta " + getIdentyfikator() + ":");
        while(true) {
            try {
                ProduktWKoszyku p = getKoszyk().pop();
                wartoscKoszyka += p.getCena() * p.getLiczbaSztukProduktu();
                System.out.println("\t" + p);
            } catch (Exception e) {
                System.out.println("\t" + "Łączna wartość produktów w koszyku: " + wartoscKoszyka);
                return;
            }
        }
    }

    public String toString() {
        return getClass().getSimpleName()
               + " [identyfikator=" + identyfikator + "] "
               + " [koszyk=" + koszyk + "]";
    }

    Klient() {
    }

    Klient(String identyfikator) {
        setIdentyfikator(identyfikator);
    }
}
