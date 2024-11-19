package z4;

import java.util.*;

/**
 * Sklep.
 *
 * @author Radosław Ślęzak
 */
public class Store {
    /**
     * Inicjuje listę produktów, listę klientów i kolejkę do kasy,
     * a następnie obsługuje wszystkich klientów.
     *
     * @param args Nieużyte
     */
    public static void main(String[] args) {
        // Lista produktów
        List<Product> productList = new ArrayList<>();

        // Lista produktów została wygenerowana przez sztuczną inteligencję
        // Nie gwarantuję realistycznych cen
        productList.add(new Product("Mleko", 3.50f));
        productList.add(new Product("Chleb", 5.00f));
        productList.add(new Product("Jajka", 4.20f));
        productList.add(new Product("Ziemniaki", 2.90f));
        productList.add(new Product("Banany", 1.60f));
        productList.add(new Product("Sok owocowy", 8.50f));
        productList.add(new Product("Kawa", 10.00f));
        productList.add(new Product("Cukier", 3.20f));
        productList.add(new Product("Masło", 5.80f));
        productList.add(new Product("Jogurt", 4.90f));
        productList.add(new Product("Płatki owsiane", 6.00f));
        productList.add(new Product("Jabłko", 2.50f));
        productList.add(new Product("Gruszka", 2.50f));

        // Lista klientów
        List<Client> clientList = new ArrayList<>();

        clientList.add(new Client("Abacki"));
        clientList.add(new Client("Babacki"));
        clientList.add(new Client("Cabacki"));
        clientList.add(new Client("Dabacki"));
        clientList.add(new Client("Ebacki"));
        clientList.add(new Client("Fabacki"));
        clientList.add(new Client("Gabacki"));
        clientList.add(new Client("Habacki"));
        clientList.add(new Client("Ibacki"));
        clientList.add(new Client("Jabacki"));
        clientList.add(new Client("Kabacki"));
        clientList.add(new Client("Labacki"));

        // Dodaj produkty do koszyków klientów
        for (int i = 0; i < clientList.size(); ++i) {
            for (int j = 0; j < 3; ++j) {
                clientList.get(i)
                        .addProductToCart(
                                productList.get((i + j) % productList.size()),
                                j + 1
                        );
            }
        }

        // Kolejka klientów
        Queue<Client> queue = new LinkedList<>();

        // Dodaj klientów w losowej kolejności
        Collections.shuffle(clientList);
        queue.addAll(clientList);

        // Obsłuż klientów
        while (!queue.isEmpty()) {
            Client k = queue.poll();
            k.serve();
            System.out.println();
        }
    }
}
