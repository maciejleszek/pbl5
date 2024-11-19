package Leszek_Maciej.Java.Zad2.z23;

import java.util.Objects;

/**
 * A general Box that is made of some material, can be lockable, has a volume and weight. The size of a Box can be
 * described with casual words (large, extra huge etc).
 * @author kmi
 * @version 1.0.0
 */
public class Box {
    private String material = "wood";
    private boolean lockable = false;
    private double volume = 3.0;
    private double weight = 2.4;
    private String size = "extra huge";

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public boolean isLockable() {
        return lockable;
    }

    public void setLockable(boolean lockable) {
        this.lockable = lockable;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        if (size.toLowerCase().contains("small")) {
            this.size = "small";
        } else if (size.toLowerCase().contains("medium")) {
            this.size = "medium";
        } else if (size.toLowerCase().contains("large")) {
            this.size = "large";
        } else {
            this.size = size;
        }
    }

    public Box() {
    }

    public Box(boolean lockable) {
        this.lockable = lockable;
    }

    public Box(boolean lockable, double weight) {
        this(lockable);
        this.weight = weight;
    }

    public Box(boolean lockable, double weight, double volume) {
        this(lockable, weight);
        this.volume = volume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Box theBox)) return false;
        //Box theBox = (Box) o;
        return isLockable() == theBox.isLockable() &&
                Double.compare(theBox.getVolume(), getVolume()) == 0 &&
                Double.compare(theBox.getWeight(), getWeight()) == 0 &&
                Objects.equals(getMaterial(), theBox.getMaterial()) &&
                getSize().equals(theBox.getSize());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMaterial(), isLockable(), getVolume(), getWeight(), getSize());
    }

    @Override
    public String toString() {
        return "The " + getClass().getSimpleName() + " made of " + getMaterial() +
               ", of size " + getSize() +
               ", of weight " + getWeight() +
               (getVolume() == 0.0 ? "" : (", of volume " + getVolume())) +
               " is" + (isLockable() ? "" : " not") + " lockable.";
    }

    /**
     * A short test of this class.
     * @param args not used.
     */
    public static void main(String[] args) {
        Box b = new Box();
        System.out.printf("%s%n", b);
        b.setVolume(2);
        System.out.printf("%s%n", b);
        b.setVolume(0);
        b.setLockable(true);
        System.out.printf("%s%n", b);
        b.setSize("super super big");
        System.out.printf("%s%n", b);
        b.setWeight(2.37);
        b.setSize("super super large");
        b.setLockable(false);
        System.out.printf("%s%n", b);
        Box b2 = new Box();
        System.out.println(b == b2);
        System.out.println(b.equals(b2));
        System.out.println(b2.equals(b));
        System.out.printf("b = [%s], b.hashCode() = %d%n", b, b.hashCode());
    }
}