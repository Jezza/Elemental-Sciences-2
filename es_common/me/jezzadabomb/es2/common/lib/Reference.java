package me.jezzadabomb.es2.common.lib;

import java.util.ArrayList;

import me.jezzadabomb.es2.common.core.utils.UtilMethods;

public class Reference {
    
    //Naming Constants
    public static final String MOD_ID = "ElementalSciences2";
    public static final String MOD_NAME = "Elemental Sciences 2";
    public static final String VERSION = "@VERSION@";
    public static final String CHANNEL_NAME = "ES2";
    
    //Class Constants
    public static final String SERVER_PROXY_CLASS = "me.jezzadabomb.es2.CommonProxy";
    public static final String CLIENT_PROXY_CLASS = "me.jezzadabomb.es2.client.ClientProxy";
    
    //Timing constants
    public static int GLASSES_WAIT_TIMER_DEFAULT = 10;
    public static int GLASSES_WAIT_TIMER;
    public static int QUANTUM_STATE_DISRUPTER_WAIT_TIMER_DEFAULT = UtilMethods.getTicksFromSeconds(320);
    public static int QUANTUM_STATE_DISRUPTER_WAIT_TIMER;
    
    //Boolean constants
    public static boolean HUD_VERTICAL_ROTATION;
    public static boolean DRAW_TEXTURED_SLOTS;
    public static boolean CAN_DEBUG;
    
    //Numerical constants
    public static int HUD_BLOCK_RANGE = 6;
}
