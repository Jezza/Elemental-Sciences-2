package me.jezzadabomb.es2.common.core.config;

import java.io.File;
import java.util.logging.Level;

import me.jezzadabomb.es2.common.core.ESLogger;
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
	private static String glassesVar = "glassesVar";

	public static void init(String file) {
		config = new Configuration(new File(file + Reference.MOD_ID + ".cfg"));

		try {
			config.load();
			ESLogger.info("Starting to load configuration file.");

			// Blocks
			BlockIds.INVENTORY_SCANNER = config.getBlock(Strings.INVENTORY_SCANNER, BlockIds.INVENTORY_SCANNER_DEFAULT).getInt();

			// Items
			ItemIds.ATOMIC_CATALYST = config.getItem(Strings.ATOMIC_CATALYST, ItemIds.ATOMIC_CATALYST_DEFAULT).getInt();
			ItemIds.GLASSES = config.getItem(Strings.GLASSES, ItemIds.GLASSES_DEFAULT).getInt();
			ItemIds.DEBUG_TOOL = config.getItem(Strings.DEBUG_TOOL, ItemIds.DEBUG_TOOL_DEFAULT).getInt();
			ItemIds.HOVER_BOOTS = config.getItem(Strings.HOVER_BOOTS, ItemIds.HOVER_BOOTS_DEFAULT).getInt();
			ItemIds.QUANTUM_STATE_DISRUPTER = config.getItem(Strings.QUANTUM_STATE_DISRUPTER, ItemIds.QUANTUM_STATE_DISRUPTER_DEFAULT).getInt();
			ItemIds.LIFE_COIN = config.getItem(Strings.LIFE_COIN, ItemIds.LIFE_COIN_DEFAULT).getInt();

			// Reference values
			Reference.GLASSES_WAIT_TIMER = config.get(glassesVar, Strings.PACKET_TIMING, Reference.GLASSES_WAIT_TIMER_DEFAULT, "Wait time for sending packets, defaults to 10").getInt();
Reference.QUANTUM_STATE_DISRUPTER_WAIT_TIMER =  config.get(var, Strings.QUANTUM_WAIT_TIMING, Reference.QUANTUM_STATE_DISRUPTER_WAIT_TIMER_DEFAULT, "The time in seconds that determines the time the server waits before the state disrupter detonates.").getInt();
			// Catalyst Blacklist
			BlackList.putValues(config.get(var, Strings.BLACKLIST_DEFAULT, BlackList.blackListDefault, "These are the default block ids, and metas that can't be destroyed by the catalyst, easily configurable. Just id:meta. :)").getString());

			// Reference Booleans
			Reference.HUD_VERTICAL_ROTATION = config.get(glassesVar, Strings.HUD_PITCH, false, "If this is set to true, then the glasses HUD follow you along your y-axis (rotation along the y axis)").getBoolean(false);
			Reference.DRAW_TEXTURED_SLOTS = config.get(glassesVar, Strings.DRAW_TEXTURED_SLOTS, true, "This oversees the slot texture being drawn on the glasses HUD").getBoolean(true);
			Reference.CAN_DEBUG = config.get(var, Strings.CAN_DEBUG, true, "This puts the mod in debug mode, beware, can flood.").getBoolean(true);

			ESLogger.info("Loaded successful.");
		} catch (Exception e) {
			ESLogger.severe("Failed loading item and block configuration.");
		} finally {
			config.save();
		}
	}
}
