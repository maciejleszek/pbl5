package Leszek_Maciej.Java.Zad2_3.z23;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa reprezentująca naukową prawdę, dziedziczy po klasie Truth.
 * Dodaje właściwości specyficzne dla naukowych prawd, takie jak lista dowodów
 * oraz operacje związane z ich zarządzaniem.
 */
public class ScientificTruth extends Truth {
    private List<String> evidence; // Lista dowodów wspierających naukową prawdę
    private String field; // Dziedzina nauki (np. fizyka, biologia)

    /**
     * Konstruktor domyślny inicjalizujący listę dowodów.
     */
    public ScientificTruth() {
        this.evidence = new ArrayList<>();
        this.field = "unspecified";
    }

    /**
     * Konstruktor z parametrami.
     * @param type Typ prawdy.
     * @param universal Czy prawda jest uniwersalna.
     * @param certaintyLevel Poziom pewności.
     * @param field Dziedzina nauki.
     */
    public ScientificTruth(String type, boolean universal, double certaintyLevel, String field) {
        super.setType(type);
        super.setUniversal(universal);
        super.setCertaintyLevel(certaintyLevel);
        this.evidence = new ArrayList<>();
        this.field = field;
    }

    /**
     * Dodaje dowód wspierający prawdę.
     * @param evidence Dowód do dodania.
     */
    public void addEvidence(String evidence) {
        this.evidence.add(evidence);
        System.out.println("Dodano dowód: " + evidence);
    }

    /**
     * Usuwa dowód.
     * @param evidence Dowód do usunięcia.
     * @return true, jeśli usunięto dowód, false w przeciwnym wypadku.
     */
    public boolean removeEvidence(String evidence) {
        boolean removed = this.evidence.remove(evidence);
        if (removed) {
            System.out.println("Usunięto dowód: " + evidence);
        } else {
            System.out.println("Nie znaleziono dowodu do usunięcia: " + evidence);
        }
        return removed;
    }

    /**
     * Zwraca listę dowodów.
     * @return Lista dowodów wspierających prawdę.
     */
    public List<String> getEvidence() {
        return new ArrayList<>(this.evidence); // Zwracamy kopię listy, aby zachować jej bezpieczeństwo.
    }

    /**
     * Zwraca dziedzinę nauki.
     * @return Dziedzina nauki.
     */
    public String getField() {
        return field;
    }

    /**
     * Ustawia dziedzinę nauki.
     * @param field Dziedzina nauki.
     */
    public void setField(String field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", dziedzina nauki: " + field +
                ", dowody: " + evidence;
    }

    /**
     * Testuje klasę ScientificTruth.
     * @param args Argumenty wejściowe (nieużywane).
     */
    public static void main(String[] args) {
        ScientificTruth sciTruth = new ScientificTruth("scientific", false, 0.85, "Physics");
        sciTruth.addEvidence("Einstein's theory of relativity");
        sciTruth.addEvidence("Double-slit experiment");

        System.out.println(sciTruth);

        sciTruth.removeEvidence("Double-slit experiment");
        sciTruth.setField("Quantum Physics");

        System.out.println("Aktualna dziedzina: " + sciTruth.getField());
        System.out.println("Lista dowodów: " + sciTruth.getEvidence());

        sciTruth.validate();
    }
}
