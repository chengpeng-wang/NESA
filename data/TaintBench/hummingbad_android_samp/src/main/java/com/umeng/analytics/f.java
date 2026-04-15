package com.umeng.analytics;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import java.io.File;
import java.io.InputStream;
import u.aly.ai;
import u.aly.aj;
import u.aly.av;

/* compiled from: StoreHelper */
public final class f {
    private static f a = new f();
    private static Context b = null;
    private static String c = null;
    private static long d = 1209600000;
    private static long e = 2097152;
    private static final String f = "age";
    private static final String g = "sex";
    private static final String h = "id";
    private static final String i = "url";
    private static final String j = "mobclick_agent_user_";
    private static final String k = "mobclick_agent_online_setting_";
    private static final String l = "mobclick_agent_header_";
    private static final String m = "mobclick_agent_update_";
    private static final String n = "mobclick_agent_state_";
    private static final String o = "mobclick_agent_cached_";
    private static final String p = "mobclick_agent_sealed_";

    public static f a(Context context) {
        if (b == null) {
            b = context.getApplicationContext();
        }
        if (c == null) {
            c = context.getPackageName();
        }
        return a;
    }

    private static boolean a(File file) {
        long length = file.length();
        if (!file.exists() || length <= e) {
            return false;
        }
        return true;
    }

    public void a(String str, String str2, int i, int i2) {
        Editor edit = k().edit();
        if (!TextUtils.isEmpty(str)) {
            edit.putString("id", str);
        }
        if (!TextUtils.isEmpty(str2)) {
            edit.putString("url", str2);
        }
        if (i > 0) {
            edit.putInt(f, i);
        }
        edit.putInt(g, i2);
        edit.commit();
    }

    public Object[] b(Context context) {
        SharedPreferences k = k();
        Object[] objArr = new Object[4];
        if (k.contains("id")) {
            objArr[0] = k.getString("id", null);
        }
        if (k.contains("url")) {
            objArr[1] = k.getString("url", null);
        }
        if (k.contains(f)) {
            objArr[2] = Integer.valueOf(k.getInt(f, -1));
        }
        if (k.contains(g)) {
            objArr[3] = Integer.valueOf(k.getInt(g, -1));
        }
        return objArr;
    }

    public int[] a() {
        SharedPreferences g = g();
        int[] iArr = new int[2];
        if (g.getInt(a.h, -1) != -1) {
            iArr[0] = g.getInt(a.h, 1);
            iArr[1] = (int) g.getLong(a.i, 0);
        } else {
            iArr[0] = g.getInt(a.k, 1);
            iArr[1] = (int) g.getLong(a.l, 0);
        }
        return iArr;
    }

    public void a(int i, int i2) {
        Editor edit = a(b).g().edit();
        edit.putInt(a.h, i);
        edit.putLong(a.i, (long) i2);
        edit.commit();
    }

    public byte[] b() {
        Exception e;
        Throwable th;
        byte[] bArr = null;
        String m = m();
        File file = new File(b.getFilesDir(), m);
        if (a(file)) {
            file.delete();
        } else if (file.exists()) {
            InputStream openFileInput;
            try {
                openFileInput = b.openFileInput(m);
                try {
                    bArr = av.b(openFileInput);
                    av.c(openFileInput);
                } catch (Exception e2) {
                    e = e2;
                    try {
                        e.printStackTrace();
                        av.c(openFileInput);
                        return bArr;
                    } catch (Throwable th2) {
                        th = th2;
                        av.c(openFileInput);
                        throw th;
                    }
                }
            } catch (Exception e3) {
                e = e3;
                openFileInput = bArr;
            } catch (Throwable th3) {
                openFileInput = bArr;
                th = th3;
                av.c(openFileInput);
                throw th;
            }
        }
        return bArr;
    }

    public void a(byte[] bArr) {
        try {
            av.a(new File(b.getFilesDir(), m()), bArr);
        } catch (Exception e) {
            aj.b(a.e, e.getMessage());
        }
    }

    public void c() {
        b.deleteFile(l());
        b.deleteFile(m());
    }

    public byte[] d() {
        Throwable th;
        String n = n();
        File file = new File(b.getFilesDir(), n);
        Exception e;
        try {
            InputStream openFileInput;
            if (!file.exists() || file.length() <= 0) {
                return null;
            }
            try {
                openFileInput = b.openFileInput(n);
                try {
                    byte[] b = av.b(openFileInput);
                    av.c(openFileInput);
                    return b;
                } catch (Exception e2) {
                    e = e2;
                }
            } catch (Exception e3) {
                e = e3;
                openFileInput = null;
            } catch (Throwable th2) {
                th = th2;
                openFileInput = null;
                av.c(openFileInput);
                throw th;
            }
            return null;
            try {
                e.printStackTrace();
                av.c(openFileInput);
                return null;
            } catch (Throwable th3) {
                th = th3;
                av.c(openFileInput);
                throw th;
            }
        } catch (Exception e4) {
            file.delete();
            e4.printStackTrace();
        }
    }

    public void b(byte[] bArr) {
        try {
            av.a(new File(b.getFilesDir(), n()), bArr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void e() {
        aj.a("--->", "delete envelope:" + b.deleteFile(n()));
    }

    public boolean f() {
        File file = new File(b.getFilesDir(), n());
        if (!file.exists() || file.length() <= 0) {
            return false;
        }
        return true;
    }

    private SharedPreferences k() {
        return b.getSharedPreferences(new StringBuilder(j).append(c).toString(), 0);
    }

    public SharedPreferences g() {
        return b.getSharedPreferences(new StringBuilder(k).append(c).toString(), 0);
    }

    public SharedPreferences h() {
        return b.getSharedPreferences(new StringBuilder(l).append(c).toString(), 0);
    }

    public SharedPreferences i() {
        return b.getSharedPreferences(new StringBuilder(m).append(c).toString(), 0);
    }

    public SharedPreferences j() {
        return b.getSharedPreferences(new StringBuilder(n).append(c).toString(), 0);
    }

    private String l() {
        return new StringBuilder(l).append(c).toString();
    }

    private String m() {
        return new StringBuilder(o).append(c).append(ai.c(b)).toString();
    }

    private String n() {
        return new StringBuilder(p).append(c).toString();
    }
}
