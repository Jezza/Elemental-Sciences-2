package me.jezzadabomb.es2.common.core;

import java.util.Locale;

import me.jezzadabomb.es2.common.core.utils.UtilMethods;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import cpw.mods.fml.relauncher.Side;

public class ESLogger {

    // Take on the appearance of FML's logger, because why not?
    public static ESLogger log = new ESLogger();
    static Side side;

    private Logger esLogger;

    public ESLogger() {
    }

    public static void init() {
        log.esLogger = LogManager.getLogger("ES2");
    }

    public static void log(Level logLevel, Object object) {
        if (object == null)
            object = "null";
        log.esLogger.log(logLevel, object.toString());
    }

    public static void info(Object object) {
        log(Level.INFO, "[INFO] " + object);
    }

    public static void debug(Object object) {
        log(Level.WARN, "[DEBUG] " + object);
    }

    public static void debug(Object object, int mode) {
        if (UtilMethods.canShowDebugHUD() && UtilMethods.correctMode(mode))
            log(Level.WARN, "[DEBUG] " + object);
    }

    public static void debugFlood(Object object) {
        if (UtilMethods.canShowDebugHUD() && UtilMethods.canFlood())
            log(Level.WARN, "[DEBUG] " + object);
    }

    public static void severe(Object object) {
        log(Level.FATAL, object);
    }

    public static Logger getLogger() {
        return log.esLogger;
    }
}
