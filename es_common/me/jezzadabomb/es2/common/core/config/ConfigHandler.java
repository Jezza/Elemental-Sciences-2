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
	private static String var = "Gameplay";

	public static void init(File file) {
		ESLogger.info(file);
		config = new Configuration(file);

		try {
			ESLogger.info("Starting to load configuration file.");
			config.load();

			getStrings();
			getBooleanValues();
			getReferenceConstants();
			getIDs();

			ESLogger.info("Loaded successful.");
		} catch (Exception e) {
			ESLogger.severe("Failed loading item and block configuration.");
		} finally {
			config.save();
		}
	}

	public static void getIDs() {
		BlockIds.INVENTORY_SCANNER = getID(Strings.INVENTORY_SCANNER, BlockIds.INVENTORY_SCANNER_DEFAULT);

		ItemIds.ATOMIC_CATALYST = getID(Strings.ATOMIC_CATALYST, ItemIds.ATOMIC_CATALYST_DEFAULT);
		ItemIds.GLASSES = getID(Strings.GLASSES, ItemIds.GLASSES_DEFAULT);
		ItemIds.HOVER_BOOTS = getID(Strings.HOVER_BOOTS, ItemIds.HOVER_BOOTS_DEFAULT);
		ItemIds.QUANTUM_STATE_DISRUPTER = getID(Strings.QUANTUM_STATE_DISRUPTER, ItemIds.QUANTUM_STATE_DISRUPTER_DEFAULT);
		ItemIds.LIFE_COIN = getID(Strings.LIFE_COIN, ItemIds.LIFE_COIN_DEFAULT);
		if (Reference.CAN_DEBUG) 
			ItemIds.DEBUG_TOOL = getID(Strings.DEBUG_TOOL, ItemIds.DEBUG_TOOL_DEFAULT);
	}

	public static void getStrings() {
		BlackList.putValues(getString(Strings.BLACKLIST_DEFAULT, BlackList.blackListDefault, "The blacklist on what blocks the atomic catalyst can break.\nNote: Some blocks you cant break on purpose, such as bedrock.\nTo add to it simply put a comma then follow it with the id:meta of the block you want to protect.\n-Serverside."));
	}

	public static void getBooleanValues() {
		Reference.HUD_VERTICAL_ROTATION = getBoolean(Strings.HUD_PITCH, false, "This determines whether or not to rotate the inventory HUD along your y-axis.\n-Clientside");
		Reference.DRAW_TEXTURED_SLOTS = getBoolean(Strings.DRAW_TEXTURED_SLOTS, true, "Some people thought this looked better, so here it is.\nDisables the inventory slot texture when drawing the HUD.\nSo, it's just a smooth texture.\n-Clientside");
		Reference.CAN_DEBUG = getBoolean(Strings.CAN_DEBUG, true, "Does what it says on the tin. Best to just leave this off.\n-Clientside");
	}

	public static void getReferenceConstants() {
		Reference.GLASSES_WAIT_TIMER = getConstant(Strings.PACKET_TIMING, Reference.GLASSES_WAIT_TIMER_DEFAULT, "How many ticks it waits before sending an update of an inventory.\n-Clientside");
		Reference.QUANTUM_STATE_DISRUPTER_WAIT_TIMER = getConstant(Strings.QUANTUM_WAIT_TIMING, Reference.QUANTUM_STATE_DISRUPTER_WAIT_TIMER_DEFAULT, "The time it takes for the Quantum State Disrupter to explode, in ticks.\nSo, just divide by twenty to get it in seconds.\n-Serverside");
	}

	public static int getID(String path, int defaultID) {
		return config.get("IDs", path, defaultID).getInt();
	}

	public static boolean getBoolean(String path, boolean defaultBoolean, String comment) {
		return config.get(var, path, defaultBoolean, comment).getBoolean(defaultBoolean);
	}

	public static String getString(String path, String defaultString, String comment) {
		return config.get(var, path, defaultString, comment).getString();
	}

	public static int getConstant(String path, int defaultConstant, String comment) {
		return config.get(var, path, defaultConstant, comment).getInt();
	}
}
