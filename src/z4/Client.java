package z4;

/**
 * Klient sklepu.
 * Ma swój identyfikator i koszyk.
 *
 * @author Radosław Ślęzak
 */
public class Client {
    /**
     * Koszyk klienta.
     */
    private final Cart cart = new Cart();

    /**
     * Zwraca koszyk klienta.
     *
     * @return Koszyk klienta
     */
    public Cart getCart() {
        return cart;
    }

    /**
     * Identyfikator klienta.
     */
    private String id;

    /**
     * Zwraca identyfikator klienta.
     *
     * @return Identyfikator klienta
     */
    public String getId() {
        return id;
    }

    /**
     * Ustawia identyfikator klienta.
     *
     * @param id Identyfikator, który zostanie ustawiony
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Dodaje produkt do koszyka klienta.
     *
     * @param product Produkt, który zostanie dodany
     * @param amount  Liczba sztuk produktu
     */
    public void addProductToCart(Product product, int amount) {
        getCart().push(new ProductInCart(product, amount));
    }

    /**
     * Obsługuje klienta.
     * Produkty są wyjmowane w kolejności od ostatnio dodanego do pierwszego.
     * Dla każdego produktu wypisywane są informacje o nim i cena jego sztuk.
     * Na koniec wypisana jest cena wszystkich produktów w koszyku.
     */
    public void serve() {
        float totalValue = getCart().getValue();
        System.out.println("Obsługuję klienta " + getId() + ":");
        while (true) {
            try {
                ProductInCart p = getCart().pop();
                System.out.println("\t" + p);
            } catch (Exception e) {
                System.out.println("\t" + "Łączna wartość produktów w koszyku: "
                        + String.format("%.2f", totalValue));
                return;
            }
        }
    }

    /**
     * Zwraca reprezentację obiektu klienta jako String.
     *
     * @return reprezentacja obiektu klienta jako String
     */
    public String toString() {
        return getClass().getSimpleName()
                + " [identyfikator=" + getId() + "] "
                + " [koszyk=" + getCart() + "]";
    }

    /**
     * Konstruktor bezparametrowy.
     */
    Client() {
    }

    /**
     * Konstruktor przyjmujący identyfikator klienta.
     *
     * @param id Identyfikator klienta
     */
    Client(String id) {
        setId(id);
    }
}
