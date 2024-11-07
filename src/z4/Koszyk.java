package z4;

import java.util.ArrayDeque;
import java.util.NoSuchElementException;

public class Koszyk extends ArrayDeque<ProduktWKoszyku> {

    private float wartoscKoszyka;

    public float getWartoscKoszyka() {
        return wartoscKoszyka;
    }

    public void push(ProduktWKoszyku produkt) {
        wartoscKoszyka += produkt.getCena();
        super.addFirst(produkt);
    }

    public ProduktWKoszyku pop(){
        ProduktWKoszyku p = peek();
        if (p == null) {
            throw new NoSuchElementException();
        }
        wartoscKoszyka -= p.getCena();
        return super.removeFirst();
    }

    public ProduktWKoszyku peek(){
        return super.getFirst();
    }

    public String toString() {
        String retString = "";
        retString += getClass().getSimpleName() + " [wartoscKoszyka=" + wartoscKoszyka + "] ";
//        retString += " [produkty=";
//        Iterator<ProduktWKoszyku> iter = queue.iterator();
//        while (iter.hasNext()) {
//            retString += "\n\t" + iter.next().toString() + " ";
//        }
//        retString += "\n]";
        return retString;
    }
}
