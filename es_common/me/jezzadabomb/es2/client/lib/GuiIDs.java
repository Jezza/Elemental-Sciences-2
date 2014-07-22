package me.jezzadabomb.es2.client.lib;

public class GuiIDs {

    public static int guiStartingNumber = 64;

    public static final int QUILL_AND_PAPYRUS = getNewGuiID();
    public static final int PENCIL_AND_PAPER = getNewGuiID();

    private static int getNewGuiID() {
        return guiStartingNumber++;
    }
}
