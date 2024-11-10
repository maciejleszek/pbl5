package z4;

/**
 * Produkt w koszyku.
 * Ma pole mówiące o liczbie sztuk produktu w koszyku.
 *
 * @author Radosław Ślęzak
 */
public class ProductInCart extends Product {

    /**
     * Liczba sztuk produktu w koszyku.
     */
    private int amountInCart;

    /**
     * Zwraca liczbę sztuk produktu w koszyku.
     *
     * @return Liczba sztuk produktu w koszyku
     */
    public int getAmountInCart() {
        return amountInCart;
    }

    /**
     * Ustawia liczbę sztuk produktu w koszyku.
     *
     * @param amountInCart Liczba sztuk, która zostanie ustawiona
     */
    public void setAmountInCart(int amountInCart) {
        this.amountInCart = amountInCart;
    }

    /**
     * Konstruktor bezparametrowy.
     */
    public ProductInCart() {
    }

    /**
     * Konstruktor przyjmujący nazwę, cenę i liczbę sztuk.
     *
     * @param name         Nazwa produktu
     * @param price        Cena produktu
     * @param amountInCart Liczba sztuk w koszyku
     */
    public ProductInCart(String name, float price, int amountInCart) {
        super(name, price);
        setAmountInCart(amountInCart);
    }

    /**
     * Konstruktor przyjmujący produkt i liczbę sztuk.
     *
     * @param product      Produkt
     * @param amountInCart Liczba sztuk w koszyku
     */
    public ProductInCart(Product product, int amountInCart) {
        super(product.getName(), product.getPrice());
        setAmountInCart(amountInCart);
    }

    /**
     * Zwraca reprezentację obiektu produktu w koszyku jako String.
     *
     * @return Reprezentacja obiektu produktu jako String.
     */
    public String toString() {
//        return getClass().getSimpleName() + " "
//               + "[nazwa=\"" + getNazwa() + "\"] "
//               + "[cena=" + getCena() + "] "
//               + "[liczbaSztukProduktu=" + getLiczbaSztukProduktu() + "]";
        return getAmountInCart() + " * " + getName() + " @ " + String.format("%.2f", getPrice())
                + "/szt. = " + String.format("%.2f", getAmountInCart() * getPrice());
    }
}
