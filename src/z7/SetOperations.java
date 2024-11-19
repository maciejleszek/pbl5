package Leszek_Maciej.Java.Zad7;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Program wykonuje operacje logiczne na dwóch zbiorach liczb nieujemnych.
 */
public class SetOperations {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // Pobierz dane wejściowe od użytkownika
            System.out.println("Podaj operację w formacie: [dane zbioru pierwszego] * | + | – [dane zbioru drugiego]");
            String input = scanner.nextLine();

            // Rozdziel dane wejściowe na elementy
            String[] parts = input.split(" \\* | \\+ | – ");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Nieprawidłowy format danych wejściowych!");
            }

            // Odczytaj operację
            String operation = input.replaceAll("[^*+–]", "").trim();
            if (!operation.matches("[*+–]")) {
                throw new IllegalArgumentException("Nieobsługiwana operacja! Dozwolone operacje to *, +, –.");
            }

            // Przetwórz zbiory wejściowe
            Set<Integer> set1 = parseSet(parts[0]);
            Set<Integer> set2 = parseSet(parts[1]);

            // Wykonaj operację na zbiorach
            Set<Integer> result = performOperation(set1, set2, operation);

            // Wyświetl wynik
            System.out.println("Wynik: " + result);

        } catch (IllegalArgumentException e) {
            System.err.println("Błąd: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.err.println("Błąd: Wprowadzone dane zawierają nieprawidłowe liczby.");
        } catch (Exception e) {
            System.err.println("Nieoczekiwany błąd: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    /**
     * Parsuje napis reprezentujący zbiór do struktury TreeSet.
     *
     * @param input Napis w formacie "[x,y,z]"
     * @return Posortowany zbiór liczb nieujemnych
     * @throws IllegalArgumentException Jeśli format jest nieprawidłowy lub zawiera liczby ujemne
     */
    private static Set<Integer> parseSet(String input) throws IllegalArgumentException {
        if (!input.matches("\\[\\d*(,\\d+)*\\]")) {
            throw new IllegalArgumentException("Nieprawidłowy format zbioru: " + input);
        }

        String[] numbers = input.substring(1, input.length() - 1).split(",");
        return Arrays.stream(numbers)
                .filter(num -> !num.isEmpty())
                .map(Integer::parseInt)
                .peek(num -> {
                    if (num < 0) throw new IllegalArgumentException("Zbiory nie mogą zawierać liczb ujemnych!");
                })
                .collect(Collectors.toCollection(TreeSet::new)); // TreeSet automatycznie sortuje elementy
    }

    /**
     * Wykonuje operację logiczną na dwóch zbiorach.
     *
     * @param set1      Pierwszy zbiór
     * @param set2      Drugi zbiór
     * @param operation Operacja (*, +, –)
     * @return Wynikowy zbiór
     */
    private static Set<Integer> performOperation(Set<Integer> set1, Set<Integer> set2, String operation) {
        Set<Integer> result = new TreeSet<>(set1); // Kopia pierwszego zbioru

        switch (operation) {
            case "*": // Część wspólna
                result.retainAll(set2);
                break;
            case "+": // Suma zbiorów
                result.addAll(set2);
                break;
            case "–": // Różnica zbiorów
                result.removeAll(set2);
                break;
            default:
                throw new IllegalArgumentException("Nieobsługiwana operacja: " + operation);
        }

        return result;
    }
}
