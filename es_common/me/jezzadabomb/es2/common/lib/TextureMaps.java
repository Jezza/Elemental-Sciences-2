package me.jezzadabomb.es2.common.lib;

import net.minecraft.util.ResourceLocation;

public class TextureMaps {
    
    // Texture Constants
    public static final String MODEL_SHEET_LOCATION = "textures/models/";
    
    //String Location Constants
    public static final String GLASSES_LOCATION = Reference.MOD_ID + ":" + MODEL_SHEET_LOCATION + "glasses.png";
    
    //Resource Location Constants
    public static final ResourceLocation MODEL_ATOMIC_CATALYST_MAIN = getResource("mainCube.png");
    public static final ResourceLocation MODEL_ATOMIC_CATALYST_ELECTRON_1 = getResource("electronCube1.png");
    public static final ResourceLocation MODEL_ATOMIC_CATALYST_ELECTRON_2 = getResource("electronCube2.png");
    public static final ResourceLocation MODEL_ATOMIC_CATALYST_ELECTRON_3 = getResource("electronCube3.png");
    public static final ResourceLocation HUD_INVENTORY = getResource("inventoryRender.png");

    private static ResourceLocation getResource(String loc){
        return new ResourceLocation(Reference.MOD_ID.toLowerCase(), MODEL_SHEET_LOCATION + loc);
    }
}