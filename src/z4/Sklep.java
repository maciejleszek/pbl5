package z4;

import java.util.*;

public class Sklep {
    public static void main(String[] args) {
        List<Produkt> listaProduktow = new ArrayList<Produkt>();
        for (int i = 0; i < 20; i++) {
            listaProduktow.add(new Produkt("Produkt " + (i + 1), (float) i + 1.50f));
        }

        List<Klient> listaKlientow = new ArrayList<Klient>();
        Queue<Klient> kolejka = new LinkedList<Klient>();

        Scanner sc = new Scanner(System.in);
        int liczbaKlientow;
        System.out.print("Podaj liczbę klientów: ");
        liczbaKlientow = sc.nextInt();
        for (int i = 0; i < liczbaKlientow; i++) {
            System.out.print("Podaj identyfikator klienta #" + (i + 1) + ": ");
            String id = sc.next();

            Klient k = new Klient(id);
            k.addProductToCart(listaProduktow.get(2 * i), (2 * i + 1));
            k.addProductToCart(listaProduktow.get(2 * i + 2), (2 * i + 2));
            listaKlientow.add(k);
        }

        for (Klient k : listaKlientow) {
            k.serve();
        }
    }
}
