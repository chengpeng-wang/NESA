package com.cmcm.baseapi;

import android.util.Log;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class CMLog {
    private static final Logger LOGGER = Logger.getLogger("com.cm");
    private static final CMLogHandler LOG_HANDLER = new CMLogHandler();
    private static final String STATIC_LOGTAG = "CMLog";
    private final String LOGTAG;
    public boolean isDebug = true;

    private static final class CMLogHandler extends Handler {
        private static final Map<Level, Integer> LEVEL_TO_LOG;

        private CMLogHandler() {
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
                Log.println(intValue, CMLog.STATIC_LOGTAG, str);
            }
        }

        public final void close() {
        }

        public final void flush() {
        }
    }

    static {
        LogManager.getLogManager().addLogger(LOGGER);
        LOGGER.addHandler(LOG_HANDLER);
        LOGGER.setLevel(Level.FINE);
    }

    private CMLog(String logTag) {
        this.LOGTAG = logTag;
        this.isDebug = Log.isLoggable(this.LOGTAG, 2);
    }

    public void v(String message) {
        if (this.isDebug) {
            v(message, null);
        }
    }

    public void d(String message) {
        if (this.isDebug) {
            d(message, null);
        }
    }

    public void i(String message) {
        if (this.isDebug) {
            i(message, null);
        }
    }

    public void w(String message) {
        if (this.isDebug) {
            w(message, null);
        }
    }

    public void e(String message) {
        if (this.isDebug) {
            e(message, null);
        }
    }

    public void v(String message, Throwable throwable) {
        if (this.isDebug) {
            LOGGER.log(Level.FINE, message, throwable);
        }
    }

    public void d(String message, Throwable throwable) {
        if (this.isDebug) {
            LOGGER.log(Level.CONFIG, message, throwable);
        }
    }

    public void i(String message, Throwable throwable) {
        if (this.isDebug) {
            LOGGER.log(Level.INFO, message, throwable);
        }
    }

    public void w(String message, Throwable throwable) {
        if (this.isDebug) {
            LOGGER.log(Level.WARNING, message, throwable);
        }
    }

    public void e(String message, Throwable throwable) {
        if (this.isDebug) {
            LOGGER.log(Level.SEVERE, message, throwable);
        }
    }
}
