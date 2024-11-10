package z4;

import java.util.ArrayDeque;
import java.util.NoSuchElementException;

/**
 * Koszyk klienta.
 * Ma pole przechowujące łączną wartość koszyka.
 * Koszyk zachowuje się jak kolejka LIFO (stos).
 *
 * @author Radosław Ślęzak
 */
public class Cart extends ArrayDeque<ProductInCart> {

    /**
     * Wartość wszystkich produktów w koszyku.
     */
    private float value;

    /**
     * Zwraca wartość koszyka.
     *
     * @return Wartość koszyka
     */
    public float getValue() {
        return value;
    }

    /**
     * Dodaje produkt do koszyka.
     * Wartość koszyka jest automatycznie zaktualizowana.
     *
     * @param product Produkt, który zostanie dodany
     */
    public void push(ProductInCart product) {
        value += product.getPrice();
        super.addFirst(product);
    }

    /**
     * Usuwa pierwszy dostępny produkt z koszyka i go zwraca.
     * Koszyk działa na zasadzie kolejki LIFO (stosu), więc
     * pierwszy dostępny produkt to ten dodany najpóźniej.
     * Wartość koszyka jest automatycznie zaktualizowana.
     *
     * @return Pierwszy dostępny produkt w koszyku
     */
    public ProductInCart pop() {
        ProductInCart p = peek();
        if (p == null) {
            throw new NoSuchElementException();
        }
        value -= p.getPrice();
        return super.removeFirst();
    }

    /**
     * Zwraca pierwszy dostępny produkt z koszyka bez usuwania go.
     * Koszyk działa na zasadzie kolejki LIFO (stosu), więc
     * pierwszy dostępny produkt to ten dodany najpóźniej.
     *
     * @return Pierwszy dostępny produkt w koszyku
     */
    public ProductInCart peek() {
        return super.getFirst();
    }

    /**
     * Zwraca reprezentację obiektu koszyka jako String.
     *
     * @return reprezentacja obiektu koszyka jako String
     */
    public String toString() {
        return getClass().getSimpleName() + " [Wartość koszyka = " + getValue() + "]";
    }
}
