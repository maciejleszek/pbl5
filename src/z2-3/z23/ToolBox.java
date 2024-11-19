package Leszek_Maciej.Java.Zad2.z23;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A general ToolBox that can be opened and closed. The ToolBox is always lockable, and is locked after
 * closing. A tool can be put to the ToolBox and removed if necessary.
 * @author kmi
 * @version 1.0.0
 */
public class ToolBox extends Box {
    /**
     * The list of names of the tools put into this ToolBox. There should be a real class named Tool created, instead
     * of using String as a tool name only...
     */
    private final ArrayList<String> toolNames = new ArrayList<>();

    /**
     * Adds the tool to this ToolBox. The weight of this ToolBox is increased by the weight of the added tool.
     * @param toolName the name of the to be added.
     */
    public void addTool(String toolName) {
        if (toolNames.add(toolName)) {
            setWeight(getWeight() + toolName.length());
        }
    }

    /**
     * Removes the tool from this ToolBox. The weight of this ToolBox is decreased by the weight of the tool being removed.
     * @param toolName the name of the tool to be removed.
     * @return {@code true} if the tool was present in the ToolBox and was really removed, {@code false} otherwise.
     */
    public boolean removeTool(String toolName) {
        if (toolNames.remove(toolName)) {
            setWeight(getWeight() - toolName.length());
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<String> getToolNames() {
        return toolNames;
    }

    private boolean opened = false;
    private boolean locked = true;

    public boolean isOpened() {
        return opened;
    }

    public boolean isLocked() {
        return locked;
    }

    public void open() {
        if (isLocked()) {
            throw new RuntimeException("Cannot open locked " + getClass().getSimpleName() +
                    ". Try to unlock it first.");
        }
        opened = true;
    }

    public void close() {
        opened = false;
    }

    public void lock() {
        if (isLockable()) {
            locked = true;
        } else {
            throw new RuntimeException("Cannot lock not lockable " + getClass().getSimpleName());
        }
    }

    public void unlock() {
        locked = false;
    }

    public boolean contains(String toolName) {
        if (isOpened()) {
            return toolNames.contains(toolName);
        } else {
            throw new RuntimeException("Cannot look for a tool while the " + getClass().getSimpleName() +
                    " is closed. Try to open it first.");
        }
    }

    @Override
    public String toString() {
        return super.toString() + " This " + getClass().getSimpleName() + " currently is" +
                (isOpened() ?
                     // (" opened and " + (toolNames.size() == 0 ? "empty" : ("contains: " + toolNames))) :
                        (" opened and " + (toolNames.isEmpty()   ? "empty" : ("contains: " + toolNames))) :
                        (" closed and " + (isLocked() ? "" : "un") + "locked")) + ".";
    }

    @Override
    public void setLockable(boolean lockable) {
        if (!lockable) {
            throw new RuntimeException("Well, well, you cannot disassemble the lock for the " + getClass().getSimpleName());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ToolBox toolBox)) return false;
        //if (!(o instanceof ToolBox)) return false;
        //ToolBox toolBox = (ToolBox) o;
        if (!super.equals(o)) return false;
        return isOpened() == toolBox.isOpened() && isLocked() == toolBox.isLocked() && getToolNames().equals(toolBox.getToolNames());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getToolNames(), isOpened(), isLocked());
    }

    public ToolBox() {
        super(true, 3.5);
        setMaterial("steel");
    }

    /**
     * A simple test for the class.
     * @param args not used.
     */
    public static void main(String... args) {
        Box b = new Box();
        Box tb = new ToolBox();
        ToolBox t = new ToolBox();
        t.setSize("huge and roomy");
        t.setWeight(7);

        System.out.println(b);
        System.out.println(tb);// a "very" polymorphic call of toString!
        System.out.println(t);

        t.unlock();
        t.open();
        System.out.println(t);
        t.addTool("drill");
        System.out.println(t);
        t.addTool("hammer");
        System.out.println(t);
        t.close();
        t.addTool("panzerfaust");
        System.out.println(t);
        System.out.printf("t = [%s], t.hashCode() = %d%n", b, b.hashCode());
        t.setLockable(false);
    }
}