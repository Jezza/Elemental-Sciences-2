package me.jezzadabomb.es2.common.core.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;

import me.jezzadabomb.es2.common.lib.BlackList;
import me.jezzadabomb.es2.common.lib.BlockIds;
import me.jezzadabomb.es2.common.lib.ItemIds;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.lib.Strings;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.FMLLog;

public class ConfigHandler {
    private static Configuration config;
    private static String var = "Misc";
    
    public static void init(String file) {
        config = new Configuration(new File(file + Reference.MOD_ID + ".cfg"));

        try {
            config.load();
            
            //Blocks
            BlockIds.BLOCK_TEST = config.getBlock(Strings.BLOCK_TEST, BlockIds.BLOCK_TEST_DEFAULT).getInt();
            
            //Items
            ItemIds.ATOMIC_CATALYST = config.getItem(Strings.ATOMIC_CATALYST, ItemIds.ATOMIC_CATALYST_DEFAULT).getInt();
            ItemIds.GLASSES = config.getItem(Strings.GLASSES, ItemIds.GLASSES_DEFAULT).getInt();
            ItemIds.DEBUG_TOOL = config.getItem(Strings.DEBUG_TOOL, ItemIds.DEBUG_TOOL_DEFAULT).getInt();
            
            //Reference values
            Reference.GLASSES_WAIT_TIMER = config.get(var, Strings.PACKET_TIMING, Reference.GLASSES_WAIT_TIMER_DEFAULT, "").getInt();
            
            //Blacklist
            BlackList.putValues(config.get(var, Strings.BLACKLIST_DEFAULT, BlackList.blackListDefault, "These are the default block ids, and metas that can't be destroyed by the catalyst, easily configurable. Just id:meta. :)").getString());
        
            //Boolean
            //The default for the hud rotation is false.
            Reference.HUD_VERTICAL_ROTATION = config.get(var, Strings.HUD_PITCH, false, "If this is set to true, then the glasses HUD follow you along your y-axis (rotation along the y axis)").getBoolean(false);
            
        } catch (Exception e) {
            FMLLog.log(Level.SEVERE, e, Reference.MOD_NAME + " has had a problem loading its block configuration");
        } finally {
            config.save();
        }
    }
}

