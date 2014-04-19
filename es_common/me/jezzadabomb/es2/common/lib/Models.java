package me.jezzadabomb.es2.common.lib;

import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Models {
    public static final ResourceLocation ATOMIC_CATALYST_MAIN = getModelLocation("AtomicCatalystCube");
    public static final ResourceLocation ATOMIC_CATALYST_ELECTRON1 = ATOMIC_CATALYST_MAIN;
    public static final ResourceLocation ATOMIC_CATALYST_ELECTRON2 = ATOMIC_CATALYST_MAIN;
    public static final ResourceLocation ATOMIC_CATALYST_ELECTRON3 = ATOMIC_CATALYST_MAIN;
    public static final ResourceLocation INVENTORY_SCANNER = getModelLocation("inventoryScanner");
    public static final ResourceLocation SOLAR_LENS = getModelLocation("solarLens");
    public static final ResourceLocation SOLAR_LENS_LOOP = getModelLocation("solarLoop");
    public static final ResourceLocation DRONE_BAY = getModelLocation("droneBay");
    public static final ResourceLocation CONSTRUCTOR_DRONE = getModelLocation("constructorDrone");
    public static final ResourceLocation ATOMIC_SHREDDER = getModelLocation("atomicShredder");

    private static ResourceLocation getModelLocation(String name) {
        return new ResourceLocation(Reference.MOD_ID.toLowerCase(), "models/" + name + ".obj");
    }

}
