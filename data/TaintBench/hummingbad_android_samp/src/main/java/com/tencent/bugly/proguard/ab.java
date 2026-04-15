package com.tencent.bugly.proguard;

import android.content.Context;
import android.os.Process;
import android.util.Log;
import com.moceanmobile.mast.Defaults;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.bugly.crashreport.crash.jni.NativeCrashHandler;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/* compiled from: BUGLY */
public class ab {
    public static boolean a;
    private static SimpleDateFormat b = new SimpleDateFormat("MM-dd hh:mm:ss");
    /* access modifiers changed from: private|static */
    public static int c = 5120;
    private static StringBuffer d;
    /* access modifiers changed from: private|static */
    public static StringBuffer e;
    /* access modifiers changed from: private|static */
    public static a f;
    private static String g;
    private static String h;
    private static Context i;
    /* access modifiers changed from: private|static */
    public static String j;
    private static boolean k;
    private static int l;
    /* access modifiers changed from: private|static */
    public static Object m = new Object();

    /* compiled from: BUGLY */
    public static class a {
        /* access modifiers changed from: private */
        public boolean a;
        /* access modifiers changed from: private */
        public File b;
        private String c;
        private long d;
        /* access modifiers changed from: private */
        public long e = 30720;

        public a(String str) {
            if (str != null && !str.equals("")) {
                this.c = str;
                this.a = a();
            } else if (ab.a) {
                Log.w("CrashReport", "[log] file path is: " + str);
            }
        }

        /* access modifiers changed from: private|declared_synchronized */
        public synchronized boolean a() {
            boolean z = false;
            synchronized (this) {
                try {
                    this.b = new File(this.c);
                    if (!this.b.exists() || this.b.delete()) {
                        if (!this.b.createNewFile()) {
                            if (ab.a) {
                                Log.w("CrashReport", "[log] create log file error: " + this.c);
                            }
                            this.a = false;
                        }
                        if (ab.a) {
                            Log.i("CrashReport", "[log] create log file success: " + this.c);
                        }
                        z = true;
                    } else {
                        if (ab.a) {
                            Log.w("CrashReport", "[log] create log file error: " + this.c);
                        }
                        this.a = false;
                    }
                } catch (Throwable th) {
                    if (ab.a) {
                        th.printStackTrace();
                        Log.w("CrashReport", "[log] create log file error: " + this.c);
                    }
                    this.a = false;
                }
            }
            return z;
        }

        public synchronized boolean a(String str) {
            boolean z = false;
            synchronized (this) {
                if (this.a) {
                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(this.b, true);
                        byte[] bytes = str.getBytes(Defaults.ENCODING_UTF_8);
                        fileOutputStream.write(bytes);
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        this.d = ((long) bytes.length) + this.d;
                        z = true;
                    } catch (Throwable th) {
                        if (ab.a) {
                            th.printStackTrace();
                        }
                        this.a = false;
                    }
                }
            }
            return z;
        }
    }

    public static synchronized void a(Context context) {
        synchronized (ab.class) {
            if (!(k || context == null)) {
                try {
                    a = CrashReport.isDebug;
                    e = new StringBuffer(0);
                    d = new StringBuffer(0);
                    i = context;
                    com.tencent.bugly.crashreport.common.info.a a = com.tencent.bugly.crashreport.common.info.a.a(i);
                    g = a.E();
                    h = a.i();
                    j = i.getFilesDir().getPath() + "/" + "buglylog_" + g + "_" + h + ".txt";
                    l = Process.myPid();
                } catch (Throwable th) {
                    if (a) {
                        th.printStackTrace();
                    }
                }
                k = true;
            }
        }
    }

    public static void a(int i) {
        synchronized (m) {
            c = i;
            if (i < 0) {
                c = 0;
            } else if (i > 10240) {
                c = 10240;
            }
        }
        if (a) {
            Log.i("CrashReport", "[log] cache is set to " + c);
        }
    }

    public static void a(String str, String str2, String str3) {
        if (a) {
            b(str, str2, str3);
        }
    }

    public static void a(String str, String str2, Throwable th) {
        if (th != null) {
            String message = th.getMessage();
            if (message == null) {
                message = "";
            }
            b(str, str2, message + 10 + ag.b(th));
        }
    }

    public static synchronized void b(String str, String str2, String str3) {
        synchronized (ab.class) {
            if (k) {
                NativeCrashHandler instance = NativeCrashHandler.getInstance();
                if (instance != null) {
                    instance.a(str, str2, str3);
                }
                final String a = a(str, str2, str3, Process.myTid());
                e.append(a);
                if (e.length() > c) {
                    y.a().a(new Runnable() {
                        public void run() {
                            synchronized (ab.m) {
                                try {
                                    if (ab.e.length() <= ab.c) {
                                        return;
                                    }
                                    if (ab.f == null) {
                                        ab.f = new a(ab.j);
                                    } else if (ab.f.b.length() + ((long) ab.e.length()) > ab.f.e) {
                                        ab.f.a();
                                    }
                                    if (ab.f.a) {
                                        ab.f.a(ab.e.toString());
                                        ab.e.setLength(0);
                                    } else {
                                        ab.e.setLength(0);
                                        ab.e.append(a);
                                    }
                                } catch (Throwable th) {
                                    if (ab.a) {
                                        th.printStackTrace();
                                    }
                                }
                            }
                        }
                    });
                }
            }
        }
    }

    private static String a(String str, String str2, String str3, int i) {
        d.setLength(0);
        if (str3.length() > 30720) {
            str3 = str3.substring(str3.length() - 30720, str3.length() - 1);
            if (a) {
                Log.w("CrashReport", "[log] your log is too long, will be substring");
            }
        }
        d.append(b.format(new Date())).append(" ").append(l).append(" ").append(i).append(" ").append(str).append(" ").append(str2).append(": ").append(str3).append("\u0001\r\n");
        return d.toString();
    }

    public static byte[] a(boolean z) {
        byte[] bArr = null;
        synchronized (m) {
            File a;
            if (z) {
                try {
                    if (f != null && f.a) {
                        a = f.b;
                        if (e.length() == 0 || a != null) {
                            bArr = ag.a(a, e.toString());
                        }
                    }
                } catch (Throwable th) {
                    if (a) {
                        th.printStackTrace();
                    }
                }
            }
            a = bArr;
            if (e.length() == 0) {
            }
            bArr = ag.a(a, e.toString());
        }
        return bArr;
    }
}
