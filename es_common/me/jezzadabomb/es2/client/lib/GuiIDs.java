package me.jezzadabomb.es2.client.lib;

public class GuiIDs {
    private static final int startingNumber = 64;
    private static int currentNumber = startingNumber;

    public static final int QUILL_AND_PAPYRUS = getNewGuiID();

    public static boolean isResearchGui(int id) {
        return id >= startingNumber || id <= currentNumber;
    }

    public static Object handleResearchGui(int id) {
        return null;
    }

    private static int getNewGuiID() {
        return currentNumber++;
    }
}
