package z4;

/**
 * Produkt w sklepie.
 * Ma swoją nazwę i cenę za jednostkę.
 *
 * @author Radosław Ślęzak
 */
public class Product {
    /**
     * Nazwa produktu.
     */
    private String name;

    /**
     * Zwraca nazwę produktu.
     *
     * @return Nazwa produktu
     */
    public String getName() {
        return name;
    }

    /**
     * Ustawia nazwę produktu.
     *
     * @param name Nazwa, która zostanie ustawiona
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Cena produktu.
     */
    private float price;

    /**
     * Zwraca cenę produktu.
     *
     * @return Cena produktu
     */
    public float getPrice() {
        return price;
    }

    /**
     * Ustawia cenę produktu.
     *
     * @param price Cena, która zostanie ustawiona
     */
    public void setPrice(float price) {
        this.price = price;
    }

    /**
     * Konstruktor bezparametrowy.
     */
    public Product() {
    }

    /**
     * Konstruktor przyjmujący nazwę produktu.
     *
     * @param name Nazwa produktu
     */
    public Product(String name) {
        setName(name);
    }

    /**
     * Konstruktor przyjmujący nazwę i cenę produktu.
     *
     * @param name  Nazwa produktu
     * @param price Cena produktu
     */
    public Product(String name, float price) {
        setName(name);
        setPrice(price);
    }
}
