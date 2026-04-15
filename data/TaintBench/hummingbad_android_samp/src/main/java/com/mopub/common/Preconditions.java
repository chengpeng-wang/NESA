package com.mopub.common;

import android.os.Looper;
import com.mopub.common.logging.MoPubLog;
import java.util.IllegalFormatException;

public final class Preconditions {
    public static final String EMPTY_ARGUMENTS = "";

    public static final class NoThrow {
        private static volatile boolean sStrictMode = false;

        public static void setStrictMode(boolean strictMode) {
            sStrictMode = strictMode;
        }

        public static boolean checkArgument(boolean expression) {
            return Preconditions.checkArgumentInternal(expression, sStrictMode, "Illegal argument", "");
        }

        public static boolean checkArgument(boolean expression, String errorMessage) {
            return Preconditions.checkArgumentInternal(expression, sStrictMode, errorMessage, "");
        }

        public static boolean checkArgument(boolean expression, String errorMessageTemplate, Object... errorMessageArgs) {
            return Preconditions.checkArgumentInternal(expression, sStrictMode, errorMessageTemplate, errorMessageArgs);
        }

        public static boolean checkState(boolean expression) {
            return Preconditions.checkStateInternal(expression, sStrictMode, "Illegal state.", "");
        }

        public static boolean checkState(boolean expression, String errorMessage) {
            return Preconditions.checkStateInternal(expression, sStrictMode, errorMessage, "");
        }

        public static boolean checkState(boolean expression, String errorMessageTemplate, Object... errorMessageArgs) {
            return Preconditions.checkStateInternal(expression, sStrictMode, errorMessageTemplate, errorMessageArgs);
        }

        public static boolean checkNotNull(Object reference) {
            return Preconditions.checkNotNullInternal(reference, sStrictMode, "Object can not be null.", "");
        }

        public static boolean checkNotNull(Object reference, String errorMessage) {
            return Preconditions.checkNotNullInternal(reference, sStrictMode, errorMessage, "");
        }

        public static boolean checkNotNull(Object reference, String errorMessageTemplate, Object... errorMessageArgs) {
            return Preconditions.checkNotNullInternal(reference, sStrictMode, errorMessageTemplate, errorMessageArgs);
        }

        public static boolean checkUiThread() {
            return Preconditions.checkUiThreadInternal(sStrictMode, "This method must be called from the UI thread.", "");
        }

        public static boolean checkUiThread(String errorMessage) {
            return Preconditions.checkUiThreadInternal(sStrictMode, errorMessage, "");
        }

        public static boolean checkUiThread(String errorMessageTemplate, Object... errorMessageArgs) {
            return Preconditions.checkUiThreadInternal(false, errorMessageTemplate, errorMessageArgs);
        }
    }

    private Preconditions() {
    }

    public static void checkArgument(boolean expression) {
        checkArgumentInternal(expression, true, "Illegal argument.", "");
    }

    public static void checkArgument(boolean expression, String errorMessage) {
        checkArgumentInternal(expression, true, errorMessage, "");
    }

    public static void checkArgument(boolean expression, String errorMessageTemplate, Object... errorMessageArgs) {
        checkArgumentInternal(expression, true, errorMessageTemplate, errorMessageArgs);
    }

    public static void checkState(boolean expression) {
        checkStateInternal(expression, true, "Illegal state.", "");
    }

    public static void checkState(boolean expression, String errorMessage) {
        checkStateInternal(expression, true, errorMessage, "");
    }

    public static void checkState(boolean expression, String errorMessageTemplate, Object... errorMessageArgs) {
        checkStateInternal(expression, true, errorMessageTemplate, errorMessageArgs);
    }

    public static void checkNotNull(Object reference) {
        checkNotNullInternal(reference, true, "Object can not be null.", "");
    }

    public static void checkNotNull(Object reference, String errorMessage) {
        checkNotNullInternal(reference, true, errorMessage, "");
    }

    public static void checkNotNull(Object reference, String errorMessageTemplate, Object... errorMessageArgs) {
        checkNotNullInternal(reference, true, errorMessageTemplate, errorMessageArgs);
    }

    public static void checkUiThread() {
        checkUiThreadInternal(true, "This method must be called from the UI thread.", "");
    }

    public static void checkUiThread(String errorMessage) {
        checkUiThreadInternal(true, errorMessage, "");
    }

    public static void checkUiThread(String errorMessageTemplate, Object... errorMessageArgs) {
        checkUiThreadInternal(true, errorMessageTemplate, errorMessageArgs);
    }

    /* access modifiers changed from: private|static|varargs */
    public static boolean checkArgumentInternal(boolean expression, boolean allowThrow, String errorMessageTemplate, Object... errorMessageArgs) {
        if (expression) {
            return true;
        }
        String format = format(errorMessageTemplate, errorMessageArgs);
        if (allowThrow) {
            throw new IllegalArgumentException(format);
        }
        MoPubLog.e(format);
        return false;
    }

    /* access modifiers changed from: private|static|varargs */
    public static boolean checkStateInternal(boolean expression, boolean allowThrow, String errorMessageTemplate, Object... errorMessageArgs) {
        if (expression) {
            return true;
        }
        String format = format(errorMessageTemplate, errorMessageArgs);
        if (allowThrow) {
            throw new IllegalStateException(format);
        }
        MoPubLog.e(format);
        return false;
    }

    /* access modifiers changed from: private|static|varargs */
    public static boolean checkNotNullInternal(Object reference, boolean allowThrow, String errorMessageTemplate, Object... errorMessageArgs) {
        if (reference != null) {
            return true;
        }
        String format = format(errorMessageTemplate, errorMessageArgs);
        if (allowThrow) {
            throw new NullPointerException(format);
        }
        MoPubLog.e(format);
        return false;
    }

    /* access modifiers changed from: private|static|varargs */
    public static boolean checkUiThreadInternal(boolean allowThrow, String errorMessageTemplate, Object... errorMessageArgs) {
        if (Looper.getMainLooper().equals(Looper.myLooper())) {
            return true;
        }
        String format = format(errorMessageTemplate, errorMessageArgs);
        if (allowThrow) {
            throw new IllegalStateException(format);
        }
        MoPubLog.e(format);
        return false;
    }

    private static String format(String template, Object... args) {
        template = String.valueOf(template);
        try {
            return String.format(template, args);
        } catch (IllegalFormatException e) {
            MoPubLog.e("MoPub preconditions had a format exception: " + e.getMessage());
            return template;
        }
    }
}
