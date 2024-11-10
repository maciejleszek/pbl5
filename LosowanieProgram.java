package z6;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeSet;

public class LosowanieProgram {
    public static void main(String[] args) {
        // Inicjalizacja obiektu Scanner do pobierania danych od użytkownika
        Scanner scanner = new Scanner(System.in);

        // Pobieranie wartości MAX, N, S od użytkownika
        System.out.print("Podaj wartość MAX (górna granica zakresu losowania): ");
        int MAX = scanner.nextInt();

        System.out.print("Podaj wartość N (liczba pierwszych i ostatnich liczb do wyświetlenia): ");
        int N = scanner.nextInt();

        System.out.print("Podaj wartość S (liczba kończąca losowanie): ");
        int S = scanner.nextInt();

        // Inicjalizacja generatora losowego z losowym ziarnem (bieżący czas systemowy)
        Random random = new Random(System.currentTimeMillis());
        List<Integer> wylosowaneLiczby = new ArrayList<>();

        // Generowanie liczb pseudolosowych do momentu trafienia na wartość S
        int liczba;
        do {
            liczba = random.nextInt(MAX);
            wylosowaneLiczby.add(liczba);
        } while (liczba != S);

        // Wypisanie pierwszych N liczb
        System.out.println("Pierwsze " + Math.min(N, wylosowaneLiczby.size()) + " liczby:");
        for (int i = 0; i < Math.min(N, wylosowaneLiczby.size()); i++) {
            System.out.print(wylosowaneLiczby.get(i) + " ");
        }
        System.out.println();

        // Wypisanie ostatnich N liczb w odwrotnej kolejności
        System.out.println("Ostatnie " + Math.min(N, wylosowaneLiczby.size()) + " liczby (od końca):");
        for (int i = wylosowaneLiczby.size() - 1; i >= Math.max(0, wylosowaneLiczby.size() - N); i--) {
            System.out.print(wylosowaneLiczby.get(i) + " ");
        }
        System.out.println();

        // Usunięcie duplikatów i sortowanie liczb
        TreeSet<Integer> unikalneLiczby = new TreeSet<>(wylosowaneLiczby);
        System.out.println("Unikalne liczby w kolejności rosnącej:");
        for (Integer liczbaUnikalna : unikalneLiczby) {
            System.out.print(liczbaUnikalna + " ");
        }
        System.out.println();

        // Zamknięcie obiektu Scanner
        scanner.close();
    }
}

