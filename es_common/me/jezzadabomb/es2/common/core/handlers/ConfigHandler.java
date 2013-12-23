package me.jezzadabomb.es2.common.core.handlers;

import java.io.File;
import java.util.ArrayList;
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
	private static int configNum = 9;

	public static void init(String file) {
		config = new Configuration(new File(file + Reference.MOD_ID + ".cfg"));

		try {
			config.load();
			info("Starting config handler");
			// Blocks
			BlockIds.INVENTORY_SCANNER = config.getBlock(Strings.BLOCK_TEST, BlockIds.INVENTORY_SCANNER_DEFAULT).getInt();

			// Items
			ItemIds.ATOMIC_CATALYST = config.getItem(Strings.ATOMIC_CATALYST, ItemIds.ATOMIC_CATALYST_DEFAULT).getInt();
			ItemIds.GLASSES = config.getItem(Strings.GLASSES, ItemIds.GLASSES_DEFAULT).getInt();
			ItemIds.DEBUG_TOOL = config.getItem(Strings.DEBUG_TOOL, ItemIds.DEBUG_TOOL_DEFAULT).getInt();
			ItemIds.HOVER_BOOTS = config.getItem(Strings.HOVER_BOOTS, ItemIds.HOVER_BOOTS_DEFAULT).getInt();
			ItemIds.IRON_BAR_RECIPE = config.getItem(Strings.IRON_BAR_RECIPE, ItemIds.IRON_BAR_RECIPE_DEFAULT).getInt();

			// Reference values
			Reference.GLASSES_WAIT_TIMER = config.get(glassesVar, Strings.PACKET_TIMING, Reference.GLASSES_WAIT_TIMER_DEFAULT, "Wait time for sending packets, defaults to 10").getInt();

			// Blacklist
			BlackList.putValues(config.get(var, Strings.BLACKLIST_DEFAULT, BlackList.blackListDefault, "These are the default block ids, and metas that can't be destroyed by the catalyst, easily configurable. Just id:meta. :)").getString());

			// Boolean
			Reference.HUD_VERTICAL_ROTATION = config.get(glassesVar, Strings.HUD_PITCH, false, "If this is set to true, then the glasses HUD follow you along your y-axis (rotation along the y axis)").getBoolean(false);
			Reference.DRAW_TEXTURED_SLOTS = config.get(glassesVar, Strings.DRAW_TEXTURED_SLOTS, true, "This oversees the slot texture being drawn on the glasses HUD").getBoolean(true);

			info("Finished loading: " + configNum + " configs");
		} catch (Exception e) {
			FMLLog.log(Level.SEVERE, e, Reference.MOD_NAME + " has had a problem loading its block configuration");
		} finally {
			config.save();
		}
	}

	private static void info(Object object) {
		ESLogger.info(object);
	}
}
