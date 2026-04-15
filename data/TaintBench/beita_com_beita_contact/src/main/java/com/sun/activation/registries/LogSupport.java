package com.sun.activation.registries;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogSupport {
    private static boolean debug;
    private static final Level level = Level.FINE;
    private static Logger logger = Logger.getLogger("javax.activation");

    static {
        debug = false;
        try {
            debug = Boolean.getBoolean("javax.activation.debug");
        } catch (Throwable th) {
        }
    }

    private LogSupport() {
    }

    public static void log(String msg) {
        if (debug) {
            System.out.println(msg);
        }
        logger.log(level, msg);
    }

    public static void log(String msg, Throwable t) {
        if (debug) {
            System.out.println(new StringBuilder(String.valueOf(msg)).append("; Exception: ").append(t).toString());
        }
        logger.log(level, msg, t);
    }

    public static boolean isLoggable() {
        return debug || logger.isLoggable(level);
    }
}
