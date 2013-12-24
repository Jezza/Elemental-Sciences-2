package me.jezzadabomb.es2.common.lib;

public class Models {

    public static final String MODEL_LOCATION = "/assets/" + Reference.MOD_ID.toLowerCase() + "/models/";
    
    public static final String ATOMIC_CATALYST_MAIN = getModelLocation("AtomicCatalystCube");
    public static final String ATOMIC_CATALYST_ELECTRON1 = ATOMIC_CATALYST_MAIN;
    public static final String ATOMIC_CATALYST_ELECTRON2 = ATOMIC_CATALYST_MAIN;
    public static final String ATOMIC_CATALYST_ELECTRON3 = ATOMIC_CATALYST_MAIN;
    
    public static final String INVENTORY_SCANNER_BASE_PLATE = getModelLocation("upperBasePlate");
    public static final String INVENTORY_SCANNER_SPINNER = getModelLocation("spinner");
    
    private static String getModelLocation(String name){
    	return MODEL_LOCATION + name + ".obj";
    }
    
}
