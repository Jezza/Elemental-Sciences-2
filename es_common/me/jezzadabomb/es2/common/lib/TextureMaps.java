package me.jezzadabomb.es2.common.lib;

import net.minecraft.util.ResourceLocation;

public class TextureMaps {

    // Texture Constants
    public static final String MODEL_SHEET_LOCATION = "textures/models/";

    // String Location Constants
    public static final String GLASSES_LOCATION = getFullLocation(Strings.GLASSES);
    public static final String HOVER_BOOTS_LOCATION = getFullLocation(Strings.HOVER_BOOTS);

    // Resource Location Constants
    public static final ResourceLocation MODEL_ATOMIC_CATALYST_MAIN = getResource("mainCube");
    public static final ResourceLocation MODEL_ATOMIC_CATALYST_ELECTRON_1 = getResource("electronCube1");
    public static final ResourceLocation MODEL_ATOMIC_CATALYST_ELECTRON_2 = getResource("electronCube2");
    public static final ResourceLocation MODEL_ATOMIC_CATALYST_ELECTRON_3 = getResource("electronCube3");
    public static final ResourceLocation HUD_INVENTORY = getResource("inventoryRender");
    public static final ResourceLocation HOVER_TEXTURE = getResource("hoverGlow");
    public static final ResourceLocation ATOMIC_CONSTRUCTOR = getResource("atomicConstructor");
    public static final ResourceLocation CONSTRUCTOR_DRONE = getResource("constructorDrone");
    public static final ResourceLocation BLANK_PIXEL = getResource("blankPixel");
    public static final ResourceLocation SOLAR_LENS = getResource("solarLens");
    public static final ResourceLocation SOLAR_LOOP = getResource("solarLoop");
    public static final ResourceLocation INVENTORY_SCANNER = getResource("inventoryScanner");
    public static final ResourceLocation CONSOLE_BASE = getResource("consolePlate");
    public static final ResourceLocation CONSOLE_SCREEN = getResource("consoleScreen");
    public static final ResourceLocation BLANK_SCREEN = getResource("blankScreen");
    public static final ResourceLocation CABLE_PIXEL = getResource("cable");

    private static String getFullLocation(String name) {
        return Reference.MOD_ID + ":" + MODEL_SHEET_LOCATION + name + ".png";
    }

    private static ResourceLocation getResource(String loc) {
        return new ResourceLocation(Reference.MOD_ID.toLowerCase(), MODEL_SHEET_LOCATION + loc + ".png");
    }
}