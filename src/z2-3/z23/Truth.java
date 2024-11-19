package Leszek_Maciej.Java.Zad2.z23;

import java.util.Objects;

/**
 * Klasa reprezentująca pojęcie prawdy z atrybutami opisującymi jej rodzaj, uniwersalność i poziom pewności.
 */
public class Truth {
    private String type = "philosophical"; // Rodzaj prawdy
    private boolean universal = false; // Czy prawda jest uniwersalna
    private double certaintyLevel = 0.75; // Poziom pewności (od 0.0 do 1.0)

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isUniversal() {
        return universal;
    }

    public void setUniversal(boolean universal) {
        this.universal = universal;
    }

    public double getCertaintyLevel() {
        return certaintyLevel;
    }

    public void setCertaintyLevel(double certaintyLevel) {
        this.certaintyLevel = certaintyLevel;
    }

    /**
     * Waliduje prawdę (symulacja procesu).
     */
    public void validate() {
        System.out.println("Walidacja prawdy: " + this);
    }

    /**
     * Kwestionuje prawdę i obniża poziom jej pewności.
     */
    public void challenge() {
        certaintyLevel = Math.max(0.0, certaintyLevel - 0.1);
        System.out.println("Prawda została zakwestionowana. Nowy poziom pewności: " + certaintyLevel);
    }

    /**
     * Akceptuje prawdę jako uniwersalną.
     */
    public void acceptAsUniversal() {
        universal = true;
        System.out.println("Prawda została uznana za uniwersalną.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Truth)) return false;
        Truth truth = (Truth) o;
        return universal == truth.universal &&
                Double.compare(truth.certaintyLevel, certaintyLevel) == 0 &&
                Objects.equals(type, truth.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, universal, certaintyLevel);
    }

    @Override
    public String toString() {
        return "[" + getClass().getName() +
                " typu '" + type + "', uniwersalna: " + universal +
                ", poziom pewności: " + certaintyLevel + "]";
    }

    /**
     * Główna metoda testowa klasy.
     *
     * @param args nieużywane.
     */
    public static void main(String[] args) {
        Truth truth1 = new Truth();
        truth1.setType("scientific");
        truth1.setUniversal(false);
        truth1.setCertaintyLevel(0.9);

        System.out.println(truth1);

        truth1.validate();
        truth1.challenge();
        truth1.acceptAsUniversal();

        Truth truth2 = new Truth();
        System.out.println(truth1.equals(truth2));
        System.out.println(truth1.hashCode());
    }
}

