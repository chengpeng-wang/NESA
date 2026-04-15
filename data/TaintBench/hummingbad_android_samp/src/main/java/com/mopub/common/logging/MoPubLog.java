package com.mopub.common.logging;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.util.Log;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.mopub.common.VisibleForTesting;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class MoPubLog {
    private static final Logger LOGGER = Logger.getLogger(LOGGER_NAMESPACE);
    public static final String LOGGER_NAMESPACE = "com.mopub";
    private static final String LOGTAG = "MoPub";
    private static final MoPubLogHandler LOG_HANDLER = new MoPubLogHandler();

    private static final class MoPubLogHandler extends Handler {
        private static final Map<Level, Integer> LEVEL_TO_LOG;

        private MoPubLogHandler() {
        }

        static {
            HashMap hashMap = new HashMap(7);
            LEVEL_TO_LOG = hashMap;
            hashMap.put(Level.FINEST, Integer.valueOf(2));
            LEVEL_TO_LOG.put(Level.FINER, Integer.valueOf(2));
            LEVEL_TO_LOG.put(Level.FINE, Integer.valueOf(2));
            LEVEL_TO_LOG.put(Level.CONFIG, Integer.valueOf(3));
            LEVEL_TO_LOG.put(Level.INFO, Integer.valueOf(4));
            LEVEL_TO_LOG.put(Level.WARNING, Integer.valueOf(5));
            LEVEL_TO_LOG.put(Level.SEVERE, Integer.valueOf(6));
        }

        @SuppressLint({"LogTagMismatch"})
        public final void publish(LogRecord logRecord) {
            if (isLoggable(logRecord)) {
                int intValue;
                if (LEVEL_TO_LOG.containsKey(logRecord.getLevel())) {
                    intValue = ((Integer) LEVEL_TO_LOG.get(logRecord.getLevel())).intValue();
                } else {
                    intValue = 2;
                }
                String str = logRecord.getMessage() + MASTNativeAdConstants.NEWLINE;
                Throwable thrown = logRecord.getThrown();
                if (thrown != null) {
                    str = str + Log.getStackTraceString(thrown);
                }
                Log.println(intValue, MoPubLog.LOGTAG, str);
            }
        }

        public final void close() {
        }

        public final void flush() {
        }
    }

    static {
        LOGGER.setUseParentHandlers(false);
        LOGGER.setLevel(Level.ALL);
        LOG_HANDLER.setLevel(Level.FINE);
        LogManager.getLogManager().addLogger(LOGGER);
        addHandler(LOGGER, LOG_HANDLER);
    }

    private MoPubLog() {
    }

    public static void c(String message) {
        c(message, null);
    }

    public static void v(String message) {
        v(message, null);
    }

    public static void d(String message) {
        d(message, null);
    }

    public static void i(String message) {
        i(message, null);
    }

    public static void w(String message) {
        w(message, null);
    }

    public static void e(String message) {
        e(message, null);
    }

    public static void c(String message, Throwable throwable) {
        LOGGER.log(Level.FINEST, message, throwable);
    }

    public static void v(String message, Throwable throwable) {
        LOGGER.log(Level.FINE, message, throwable);
    }

    public static void d(String message, Throwable throwable) {
        LOGGER.log(Level.CONFIG, message, throwable);
    }

    public static void i(String message, Throwable throwable) {
        LOGGER.log(Level.INFO, message, throwable);
    }

    public static void w(String message, Throwable throwable) {
        LOGGER.log(Level.WARNING, message, throwable);
    }

    public static void e(String message, Throwable throwable) {
        LOGGER.log(Level.SEVERE, message, throwable);
    }

    @VisibleForTesting
    public static void setSdkHandlerLevel(@NonNull Level level) {
        LOG_HANDLER.setLevel(level);
    }

    private static void addHandler(@NonNull Logger logger, @NonNull Handler handler) {
        Handler[] handlers = logger.getHandlers();
        int length = handlers.length;
        int i = 0;
        while (i < length) {
            if (!handlers[i].equals(handler)) {
                i++;
            } else {
                return;
            }
        }
        logger.addHandler(handler);
    }
}
