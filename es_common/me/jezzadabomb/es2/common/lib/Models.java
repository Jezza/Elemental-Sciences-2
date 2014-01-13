package me.jezzadabomb.es2.common.lib;

public class Models {

    public static final String MODEL_LOCATION = "/assets/" + Reference.MOD_ID.toLowerCase() + "/models/";

    public static final String ATOMIC_CATALYST_MAIN = getModelLocation("AtomicCatalystCube");
    public static final String ATOMIC_CATALYST_ELECTRON1 = ATOMIC_CATALYST_MAIN;
    public static final String ATOMIC_CATALYST_ELECTRON2 = ATOMIC_CATALYST_MAIN;
    public static final String ATOMIC_CATALYST_ELECTRON3 = ATOMIC_CATALYST_MAIN;
    public static final String INVENTORY_SCANNER = getModelLocation("inventoryScanner");
    public static final String CONSTRUCTOR_DRONE = getModelLocation("drone");
    public static final String SOLAR_LENS = getModelLocation("solarLens");
    public static final String SOLAR_LENS_LOOP = getModelLocation("solarLoop");

    private static String getModelLocation(String name) {
        return MODEL_LOCATION + name + ".obj";
    }

}
