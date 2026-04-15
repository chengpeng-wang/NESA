package com.esotericsoftware.minlog;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

public class Log {
    public static final boolean DEBUG = false;
    public static final boolean ERROR = false;
    public static final boolean INFO = false;
    public static final int LEVEL_DEBUG = 2;
    public static final int LEVEL_ERROR = 5;
    public static final int LEVEL_INFO = 3;
    public static final int LEVEL_NONE = 6;
    public static final int LEVEL_TRACE = 1;
    public static final int LEVEL_WARN = 4;
    public static final boolean TRACE = false;
    public static final boolean WARN = false;
    private static final int level = 6;
    private static Logger logger = new Logger();

    public static class Logger {
        private long firstLogTime = new Date().getTime();

        public void log(int i, String str, String str2, Throwable th) {
            StringBuilder stringBuilder = new StringBuilder(256);
            long time = new Date().getTime() - this.firstLogTime;
            long j = time / 60000;
            time = (time / 1000) % 60;
            if (j <= 9) {
                stringBuilder.append('0');
            }
            stringBuilder.append(j);
            stringBuilder.append(':');
            if (time <= 9) {
                stringBuilder.append('0');
            }
            stringBuilder.append(time);
            switch (i) {
                case 1:
                    stringBuilder.append(" TRACE: ");
                    break;
                case 2:
                    stringBuilder.append(" DEBUG: ");
                    break;
                case 3:
                    stringBuilder.append("  INFO: ");
                    break;
                case 4:
                    stringBuilder.append("  WARN: ");
                    break;
                case 5:
                    stringBuilder.append(" ERROR: ");
                    break;
            }
            if (str != null) {
                stringBuilder.append('[');
                stringBuilder.append(str);
                stringBuilder.append("] ");
            }
            stringBuilder.append(str2);
            if (th != null) {
                StringWriter stringWriter = new StringWriter(256);
                th.printStackTrace(new PrintWriter(stringWriter));
                stringBuilder.append(10);
                stringBuilder.append(stringWriter.toString().trim());
            }
            print(stringBuilder.toString());
        }

        /* access modifiers changed from: protected */
        public void print(String str) {
            System.out.println(str);
        }
    }

    public static void set(int i) {
    }

    public static void NONE() {
        set(6);
    }

    public static void ERROR() {
        set(5);
    }

    public static void WARN() {
        set(4);
    }

    public static void INFO() {
        set(3);
    }

    public static void DEBUG() {
        set(2);
    }

    public static void TRACE() {
        set(1);
    }

    public static void setLogger(Logger logger) {
        logger = logger;
    }

    public static void error(String str, Throwable th) {
    }

    public static void error(String str, String str2, Throwable th) {
    }

    public static void error(String str) {
    }

    public static void error(String str, String str2) {
    }

    public static void warn(String str, Throwable th) {
    }

    public static void warn(String str, String str2, Throwable th) {
    }

    public static void warn(String str) {
    }

    public static void warn(String str, String str2) {
    }

    public static void info(String str, Throwable th) {
    }

    public static void info(String str, String str2, Throwable th) {
    }

    public static void info(String str) {
    }

    public static void info(String str, String str2) {
    }

    public static void debug(String str, Throwable th) {
    }

    public static void debug(String str, String str2, Throwable th) {
    }

    public static void debug(String str) {
    }

    public static void debug(String str, String str2) {
    }

    public static void trace(String str, Throwable th) {
    }

    public static void trace(String str, String str2, Throwable th) {
    }

    public static void trace(String str) {
    }

    public static void trace(String str, String str2) {
    }

    private Log() {
    }
}
