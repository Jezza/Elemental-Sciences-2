package me.jezzadabomb.es2.common.core.config;

import java.io.File;

import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.lib.BlackList;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.lib.Strings;
import me.jezzadabomb.es2.common.tickers.QuantumBombTicker;
import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {

    private static Configuration config;
    private static String var = "Gameplay";

    // Local dev environment. Shh, I know this is a stupid way to do it, but it's also simple... :P
    private static final String debugString = "G:\\Minecraft\\forge\\eclipse\\config\\elementalsciences2.cfg";

    public static void init(File file) {
        config = new Configuration(file);

        try {
            ESLogger.info("Starting to load configuration file.");
            config.load();
            Reference.isDebugMode = file.toString().equals(debugString);

            getStrings();
            getBooleanValues();
            getReferenceConstants();

            ESLogger.info("Loaded successful.");
        } catch (Exception e) {
            ESLogger.severe("Failed loading configurations.");
        } finally {
            config.save();
        }
    }

    public static void getStrings() {
        BlackList.putValues(getString(Strings.BLACKLIST_DEFAULT, BlackList.blackListDefault, "The blacklist on what blocks the atomic catalyst can break.\nNote: Some blocks you cant break on purpose, such as bedrock.\nTo add to it simply put a comma then follow it with the id:meta of the block you want to protect.\n-Serverside."));
        QuantumBombTicker.parseTime(getString(Strings.QUANTUM_WAIT_TIMING, QuantumBombTicker.DEFAULT_TIMING, "The time it takes for the Quantum State Disrupter to explode.\nFormat: <hours>,<minutes>,<seconds>,<ticks>\neg, 0,2,5,0 would be 0 hours, 2 minutes, 5 seconds, and 0 ticks.\nWrite default for random intervals between 5 and 20 minutes.\n-Serverside"));
    }

    public static void getBooleanValues() {
        Reference.HUD_VERTICAL_ROTATION = getBoolean(Strings.HUD_PITCH, false, "This determines whether or not to rotate the inventory HUD along your y-axis.\n-Clientside");
        Reference.DRAW_TEXTURED_SLOTS = getBoolean(Strings.DRAW_TEXTURED_SLOTS, true, "Some people thought this looked better, so here it is.\nDisables the inventory slot texture when drawing the HUD.\nSo, it's just a smooth texture.\n-Clientside");
    }

    public static void getReferenceConstants() {
        Reference.GLASSES_WAIT_TIMER = getConstant(Strings.PACKET_TIMING, Reference.GLASSES_WAIT_TIMER_DEFAULT, "How many ticks it waits before sending an update of an inventory.\n-Clientside");
        Reference.DRONE_BAY_DOOR_TYPE = getConstant(Strings.DRONE_BAY_DOOR_TYPE, 0, "The way the drone bay opens.\n0: Default,Opens in a circluar direction.\n1:Which just slides back.\n2: A cool look animation that looks cool... :P Try it and find out. \n-Clientside");
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
