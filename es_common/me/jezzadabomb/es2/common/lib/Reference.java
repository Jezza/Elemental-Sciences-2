package me.jezzadabomb.es2.common.lib;

public class Reference {

    // Naming Constants
    public static final String MOD_ID = "ElementalSciences2";
    public static final String MOD_NAME = "Elemental Sciences 2";
    public static final String VERSION = "@VERSION@";
    public static final String CHANNEL_NAME = "ES2";
    public static final String MOD_IDENTIFIER = MOD_ID + ":";

    // Class Constants
    public static final String SERVER_PROXY_CLASS = "me.jezzadabomb.es2.CommonProxy";
    public static final String CLIENT_PROXY_CLASS = "me.jezzadabomb.es2.client.ClientProxy";

    // Timing constants
    public static int GLASSES_WAIT_TIMER_DEFAULT = 10;
    public static int QUANTUM_STATE_DISRUPTER_WAIT_TIMER_DEFAULT = 320 * 20;

    public static int GLASSES_WAIT_TIMER;
    public static int QUANTUM_STATE_DISRUPTER_WAIT_TIMER;

    // Boolean constants
    public static boolean HUD_VERTICAL_ROTATION;
    public static boolean DRAW_TEXTURED_SLOTS;
    public static boolean isDebugMode;

    // Numerical constants
    public static int HUD_BLOCK_RANGE = 6;
    public static int PYLON_POWER_RANGE = 16;
    public static int DRONE_BAY_DOOR_TYPE;
}
