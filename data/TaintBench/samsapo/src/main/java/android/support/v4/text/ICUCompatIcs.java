package android.support.v4.text;

import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ICUCompatIcs {
    private static final String TAG = "ICUCompatIcs";
    private static Method sAddLikelySubtagsMethod;
    private static Method sGetScriptMethod;

    ICUCompatIcs() {
    }

    static {
        try {
            Class cls = Class.forName("libcore.icu.ICU");
            if (cls != null) {
                Class[] clsArr = new Class[1];
                Class[] clsArr2 = clsArr;
                clsArr[0] = String.class;
                sGetScriptMethod = cls.getMethod("getScript", clsArr2);
                clsArr = new Class[1];
                clsArr2 = clsArr;
                clsArr[0] = String.class;
                sAddLikelySubtagsMethod = cls.getMethod("addLikelySubtags", clsArr2);
            }
        } catch (Exception e) {
            int w = Log.w(TAG, e);
        }
    }

    public static String getScript(String str) {
        int w;
        String str2 = str;
        try {
            if (sGetScriptMethod != null) {
                Object[] objArr = new Object[1];
                Object[] objArr2 = objArr;
                objArr[0] = str2;
                return (String) sGetScriptMethod.invoke(null, objArr2);
            }
        } catch (IllegalAccessException e) {
            w = Log.w(TAG, e);
        } catch (InvocationTargetException e2) {
            w = Log.w(TAG, e2);
        }
        return null;
    }

    public static String addLikelySubtags(String str) {
        int w;
        String str2 = str;
        try {
            if (sAddLikelySubtagsMethod != null) {
                Object[] objArr = new Object[1];
                Object[] objArr2 = objArr;
                objArr[0] = str2;
                return (String) sAddLikelySubtagsMethod.invoke(null, objArr2);
            }
        } catch (IllegalAccessException e) {
            w = Log.w(TAG, e);
        } catch (InvocationTargetException e2) {
            w = Log.w(TAG, e2);
        }
        return str2;
    }
}
