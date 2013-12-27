package me.jezzadabomb.es2.common.core;

import java.util.logging.Level;
import java.util.logging.Logger;

import cpw.mods.fml.common.FMLLog;

import me.jezzadabomb.es2.common.core.utils.UtilHelpers;
import me.jezzadabomb.es2.common.lib.Reference;

public class ESLogger {
	// Logger thanks to Pahimar. :)
	private static Logger esLogger = Logger.getLogger(Reference.MOD_ID);

	public static void init() {
		esLogger.setParent(FMLLog.getLogger());
	}

	public static void log(Level logLevel, Object object) {
		esLogger.log(logLevel, object.toString());
	}

	public static void severe(Object object) {

		log(Level.SEVERE, object.toString());
	}

	public static void debug(Object object) {
		if (UtilHelpers.canShowDebugHUD())
			log(Level.WARNING, "[DEBUG] " + object.toString());
	}
	
	//Possible flood case.
	public static void debugFlood(Object object) {
		if (UtilHelpers.canShowDebugHUD() && UtilHelpers.canFlood())
			log(Level.WARNING, "[DEBUG] " + object.toString());
	}
	
	public static void debug(Object object, int mode) {
        if (UtilHelpers.canShowDebugHUD() && UtilHelpers.correctMode(mode))
            log(Level.WARNING, "[DEBUG] " + object.toString());
    }

	public static void warning(Object object) {

		log(Level.WARNING, object.toString());
	}

	public static void info(Object object) {

		log(Level.INFO, object.toString());
	}

	public static void config(Object object) {

		log(Level.CONFIG, object.toString());
	}

	public static void fine(Object object) {

		log(Level.FINE, object.toString());
	}

	public static void finer(Object object) {

		log(Level.FINER, object.toString());
	}

	public static void finest(Object object) {

		log(Level.FINEST, object.toString());
	}
}
