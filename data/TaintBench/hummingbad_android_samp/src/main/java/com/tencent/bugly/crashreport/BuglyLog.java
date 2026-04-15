package com.tencent.bugly.crashreport;

import android.util.Log;
import com.tencent.bugly.proguard.ab;

/* compiled from: BUGLY */
public class BuglyLog {
    public static void v(String tag, String content) {
        if (tag == null) {
            tag = "";
        }
        if (content == null) {
            content = "null";
        }
        if (ab.a) {
            Log.v(tag, content);
        }
        ab.b("V", tag, content);
    }

    public static void d(String tag, String content) {
        if (tag == null) {
            tag = "";
        }
        if (content == null) {
            content = "null";
        }
        if (ab.a) {
            Log.d(tag, content);
        }
        ab.b("D", tag, content);
    }

    public static void i(String tag, String content) {
        if (tag == null) {
            tag = "";
        }
        if (content == null) {
            content = "null";
        }
        if (ab.a) {
            Log.i(tag, content);
        }
        ab.b("I", tag, content);
    }

    public static void w(String tag, String content) {
        if (tag == null) {
            tag = "";
        }
        if (content == null) {
            content = "null";
        }
        if (ab.a) {
            Log.w(tag, content);
        }
        ab.b("W", tag, content);
    }

    public static void e(String tag, String content) {
        if (tag == null) {
            tag = "";
        }
        if (content == null) {
            content = "null";
        }
        if (ab.a) {
            Log.e(tag, content);
        }
        ab.b("E", tag, content);
    }

    public static void e(String tag, String content, Throwable throwable) {
        if (tag == null) {
            tag = "";
        }
        if (content == null) {
            content = "null";
        }
        if (ab.a) {
            Log.e(tag, content, throwable);
        }
        ab.a("E", tag, throwable);
    }

    public static void setCache(int size) {
        ab.a(size);
    }
}
