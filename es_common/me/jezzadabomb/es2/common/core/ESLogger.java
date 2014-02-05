package me.jezzadabomb.es2.common.core;

import java.util.logging.Level;
import java.util.logging.Logger;

import cpw.mods.fml.common.FMLLog;

import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.lib.Reference;

public class ESLogger {
    // Logger thanks to Pahimar. :)
    private static Logger esLogger = Logger.getLogger(Reference.MOD_ID);

    public static void init() {
        esLogger.setParent(FMLLog.getLogger());
    }

    public static void log(Level logLevel, Object object) {
        if (object == null)
            object = "null";
        esLogger.log(logLevel, object.toString());
    }

    public static void severe(Object object) {
        log(Level.SEVERE, object);
    }

    public static void debug(Object object) {
        log(Level.WARNING, "[DEBUG] " + object);
    }

    // Possible flood case.
    public static void debugFlood(Object object) {
        if (UtilMethods.canShowDebugHUD() && UtilMethods.canFlood())
            log(Level.WARNING, "[DEBUG] " + object);
    }

    public static void debug(Object object, int mode) {
        if (UtilMethods.canShowDebugHUD() && UtilMethods.correctMode(mode))
            log(Level.WARNING, "[DEBUG] " + object);
    }

    public static void warning(Object object) {

        log(Level.WARNING, object);
    }

    public static void info(Object object) {

        log(Level.INFO, object);
    }

    public static void config(Object object) {

        log(Level.CONFIG, object);
    }

    public static void fine(Object object) {

        log(Level.FINE, object);
    }

    public static void finer(Object object) {

        log(Level.FINER, object);
    }

    public static void finest(Object object) {

        log(Level.FINEST, object);
    }
}
