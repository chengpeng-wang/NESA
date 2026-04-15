package com.cmcm.baseapi.utils;

import android.text.TextUtils;
import android.util.Log;
import java.util.Collection;

public class Assure {
    private static final boolean DEBUG = false;

    public static class AssureException extends RuntimeException {
        private static final long serialVersionUID = 874757892066603343L;

        public AssureException(String msg) {
            super(msg);
        }
    }

    public static void throwMessage(String msg) {
        Log.e("ASSURE fail", msg);
    }

    public static void NOTREACHED() {
        throwMessage("NOTREACHED");
    }

    public static void NOT_IMPLEMENTED() {
        throwMessage("NOT_IMPLEMENTED");
    }

    public static void checkTrue(boolean value) {
        if (!value) {
            throwMessage("AssureTrue");
        }
    }

    public static void checkFalse(boolean value) {
        if (value) {
            throwMessage("AssureFalse");
        }
    }

    public static void checkNull(Object obj) {
        if (obj != null) {
            throwMessage("AssureNull");
        }
    }

    public static void checkNotNull(Object obj) {
        if (obj == null) {
            throwMessage("AssureNotNull");
        }
    }

    public static void checkNotEqual(int expectNot, int real) {
        if (expectNot == real) {
            throwMessage("AssureNotEqual");
        }
    }

    public static void checkNotEqual(Object expectNot, Object real) {
        if (expectNot == real) {
            throwMessage("AssureNotEqual");
        }
        if (expectNot != null && real != null && expectNot.equals(real)) {
            throwMessage("AssureNotEqual");
        }
    }

    public static void checkEqual(Object expect, Object real) {
        if (expect != real) {
            throwMessage("AssureEqual");
        }
    }

    public static void checkEqual(int expect, int real) {
        if (expect != real) {
            throwMessage("AssureEqual");
        }
    }

    public static void checkEqual(long expect, long real) {
        if (expect != real) {
            throwMessage("AssureEqual");
        }
    }

    public static void checkEqualNoCase(String expect, String real) {
        if (expect != null || real != null) {
            if (expect == null || real == null) {
                throwMessage("AssureEqual");
            }
            if (!expect.equalsIgnoreCase(real)) {
                throwMessage("AssureEqual");
            }
        }
    }

    public static void checkNotEmptyString(String webUrl) {
        if (TextUtils.isEmpty(webUrl)) {
            throwMessage("AssureNotEmptyString");
        }
    }

    public static <E> void checkNotEmptyCollection(Collection<E> collection) {
        if (collection == null || collection.isEmpty()) {
            throwMessage("AssureNotEmptyCollection");
        }
    }

    public static <E> void checkNotEmptyArray(E[] collection) {
        if (collection == null || collection.length <= 0) {
            throwMessage("checkNotEmptyArray");
        }
    }

    public static void DCHECK(boolean value) {
    }

    public static void checkRunningOnUIThread() {
        DCHECK(ThreadUtils.runningOnUiThread());
    }

    public static void checkNotOnUIThread() {
        DCHECK(!ThreadUtils.runningOnUiThread());
    }
}
