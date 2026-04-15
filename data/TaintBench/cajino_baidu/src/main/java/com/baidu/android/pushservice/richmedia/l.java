package com.baidu.android.pushservice.richmedia;

import android.util.Log;
import java.util.Hashtable;

public class l {
    private static boolean a = true;
    private static Hashtable b = new Hashtable();
    private String c;

    private l(String str) {
        this.c = str;
    }

    public static l a(String str) {
        l lVar = (l) b.get(str);
        if (lVar != null) {
            return lVar;
        }
        lVar = new l(str);
        b.put(str, lVar);
        return lVar;
    }

    public void b(String str) {
        if (a) {
            Log.d("[Channel]", "{Thread:" + Thread.currentThread().getName() + "}" + "[" + this.c + ":] " + str);
        }
    }

    public void c(String str) {
        if (a) {
            Log.w("[Channel]", "{Thread:" + Thread.currentThread().getName() + "}" + "[" + this.c + ":] " + str);
        }
    }
}
