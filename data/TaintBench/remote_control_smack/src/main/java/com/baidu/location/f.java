package com.baidu.location;

import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.net.wifi.ScanResult;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.support.v4.widget.ExploreByTouchHelper;
import android.util.Log;
import java.io.File;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.Calendar;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.json.JSONObject;

public final class f extends Service {
    static final int C = 57;
    static final int F = 52;
    static final int H = 26;
    static final int J = 64;
    static final int K = 27;
    static final int L = 62;
    static final int Q = 1000;
    static final int S = 54;
    static final int T = 81;
    static final int U = 25;
    static final int Y = 21;
    private static String a = (aa + "/glb.dat");
    static String aa = (Environment.getExternalStorageDirectory().getPath() + "/baidu/tempdata");
    private static final int ad = 200;
    static final int ae = 43;
    static final int af = 14;
    static final int ag = 3000;
    static final int ai = 56;
    static final int am = 101;
    static final float an = 3.1f;
    static final int ao = 61;
    static final int ap = 53;
    private static final int ar = 800;
    static final int b = 63;
    /* renamed from: byte */
    private static final int f143byte = 24;
    static final int c = 12;
    /* renamed from: case */
    static final int f144case = 42;
    /* renamed from: do */
    static final int f145do = 28;
    static final int e = 65;
    /* renamed from: else */
    static final int f146else = 2000;
    /* renamed from: for */
    static final int f147for = 22;
    static final int g = 15;
    static final int i = 55;
    /* renamed from: int */
    static final int f148int = 31;
    /* access modifiers changed from: private|static */
    public static File j = null;
    /* access modifiers changed from: private|static */
    public static File k = null;
    static final int l = 11;
    /* renamed from: long */
    static final int f149long = 13;
    static final int p = 41;
    static final int s = 23;
    static final int t = 91;
    public static final String v = "baidu_location_service";
    /* renamed from: void */
    static final int f150void = 71;
    static final int w = 24;
    static final int x = 3000;
    static final int z = 51;
    private String A = null;
    private c B = null;
    private long D = 0;
    /* access modifiers changed from: private */
    public e E = null;
    private String G = null;
    private boolean I = true;
    private boolean M = false;
    private long N = 0;
    private boolean O = false;
    final Handler P = new d();
    private SQLiteDatabase R = null;
    private String V = null;
    private boolean W = true;
    private int X = 0;
    private b Z = null;
    /* access modifiers changed from: private */
    public boolean ab = false;
    private c ac = null;
    /* access modifiers changed from: private */
    public boolean ah = false;
    private com.baidu.location.c.a aj = null;
    private boolean ak = false;
    final Messenger al = new Messenger(this.P);
    private String aq = null;
    /* access modifiers changed from: private */
    public a as = null;
    /* renamed from: char */
    private c f151char = null;
    private long d = 0;
    private Location f = null;
    /* renamed from: goto */
    private boolean f152goto = false;
    private String h = null;
    /* renamed from: if */
    private String f153if = "bdcltb09";
    /* access modifiers changed from: private */
    public String m = (aa + "/vm.dat");
    private double n = 0.0d;
    /* renamed from: new */
    private String f154new = null;
    private double o = 0.0d;
    private double q = 0.0d;
    /* access modifiers changed from: private */
    public c r = null;
    /* renamed from: try */
    private com.baidu.location.c.a f155try = null;
    private com.baidu.location.c.a u = null;
    /* access modifiers changed from: private */
    public c y = null;

    public class a implements UncaughtExceptionHandler {
        /* renamed from: if */
        private Context f129if;

        a(Context context) {
            this.f129if = context;
            a();
        }

        private String a(Throwable th) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            th.printStackTrace(printWriter);
            printWriter.close();
            return stringWriter.toString();
        }

        public void a() {
            String str = null;
            try {
                File file = new File((Environment.getExternalStorageDirectory().getPath() + "/traces") + "/error_fs.dat");
                if (file.exists()) {
                    RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                    randomAccessFile.seek(280);
                    if (1326 == randomAccessFile.readInt()) {
                        String str2;
                        byte[] bArr;
                        randomAccessFile.seek(308);
                        int readInt = randomAccessFile.readInt();
                        if (readInt <= 0 || readInt >= 2048) {
                            str2 = null;
                        } else {
                            j.a(f.v, "A" + readInt);
                            bArr = new byte[readInt];
                            randomAccessFile.read(bArr, 0, readInt);
                            str2 = new String(bArr, 0, readInt);
                        }
                        randomAccessFile.seek(600);
                        readInt = randomAccessFile.readInt();
                        if (readInt > 0 && readInt < 2048) {
                            j.a(f.v, "A" + readInt);
                            bArr = new byte[readInt];
                            randomAccessFile.read(bArr, 0, readInt);
                            str = new String(bArr, 0, readInt);
                        }
                        j.a(f.v, str2 + str);
                        if (a(str2, str)) {
                            randomAccessFile.seek(280);
                            randomAccessFile.writeInt(12346);
                        }
                    }
                    randomAccessFile.close();
                }
            } catch (Exception e) {
            }
        }

        public void a(File file, String str, String str2) {
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                randomAccessFile.seek(280);
                randomAccessFile.writeInt(12346);
                randomAccessFile.seek(300);
                randomAccessFile.writeLong(System.currentTimeMillis());
                byte[] bytes = str.getBytes();
                randomAccessFile.writeInt(bytes.length);
                randomAccessFile.write(bytes, 0, bytes.length);
                randomAccessFile.seek(600);
                bytes = str2.getBytes();
                randomAccessFile.writeInt(bytes.length);
                randomAccessFile.write(bytes, 0, bytes.length);
                if (!a(str, str2)) {
                    randomAccessFile.seek(280);
                    randomAccessFile.writeInt(1326);
                }
                randomAccessFile.close();
            } catch (Exception e) {
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean a(String str, String str2) {
            if (!g.a(this.f129if)) {
                return false;
            }
            try {
                HttpPost httpPost = new HttpPost(j.f201do);
                ArrayList arrayList = new ArrayList();
                arrayList.add(new BasicNameValuePair("e0", str));
                arrayList.add(new BasicNameValuePair("e1", str2));
                httpPost.setEntity(new UrlEncodedFormEntity(arrayList, "utf-8"));
                DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
                defaultHttpClient.getParams().setParameter("http.connection.timeout", Integer.valueOf(12000));
                defaultHttpClient.getParams().setParameter("http.socket.timeout", Integer.valueOf(12000));
                j.a(f.v, "send begin ...");
                if (defaultHttpClient.execute(httpPost).getStatusLine().getStatusCode() != f.ad) {
                    return false;
                }
                j.a(f.v, "send ok....");
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:24:0x009b A:{SYNTHETIC, Splitter:B:24:0x009b} */
        /* JADX WARNING: Removed duplicated region for block: B:13:0x0077 A:{Catch:{ Exception -> 0x00bc }} */
        public void uncaughtException(java.lang.Thread r9, java.lang.Throwable r10) {
            /*
            r8 = this;
            r1 = 0;
            r2 = r8.a(r10);	 Catch:{ Exception -> 0x0096 }
            r0 = "baidu_location_service";
            com.baidu.location.j.a(r0, r2);	 Catch:{ Exception -> 0x00be }
            r0 = com.baidu.location.f.this;	 Catch:{ Exception -> 0x00be }
            r0.r;	 Catch:{ Exception -> 0x00be }
            r0 = 0;
            r0 = com.baidu.location.c.a(r0);	 Catch:{ Exception -> 0x00be }
            r3 = com.baidu.location.f.this;	 Catch:{ Exception -> 0x00be }
            r3 = r3.as;	 Catch:{ Exception -> 0x00be }
            if (r3 == 0) goto L_0x0037;
        L_0x001c:
            r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00be }
            r3.<init>();	 Catch:{ Exception -> 0x00be }
            r0 = r3.append(r0);	 Catch:{ Exception -> 0x00be }
            r3 = com.baidu.location.f.this;	 Catch:{ Exception -> 0x00be }
            r3 = r3.as;	 Catch:{ Exception -> 0x00be }
            r3 = r3.m720byte();	 Catch:{ Exception -> 0x00be }
            r0 = r0.append(r3);	 Catch:{ Exception -> 0x00be }
            r0 = r0.toString();	 Catch:{ Exception -> 0x00be }
        L_0x0037:
            if (r0 == 0) goto L_0x00c3;
        L_0x0039:
            r0 = com.baidu.location.Jni.m673if(r0);	 Catch:{ Exception -> 0x00be }
        L_0x003d:
            r3 = r0;
        L_0x003e:
            r0 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00bc }
            r0.<init>();	 Catch:{ Exception -> 0x00bc }
            r4 = android.os.Environment.getExternalStorageDirectory();	 Catch:{ Exception -> 0x00bc }
            r4 = r4.getPath();	 Catch:{ Exception -> 0x00bc }
            r0 = r0.append(r4);	 Catch:{ Exception -> 0x00bc }
            r4 = "/traces";
            r0 = r0.append(r4);	 Catch:{ Exception -> 0x00bc }
            r4 = r0.toString();	 Catch:{ Exception -> 0x00bc }
            r0 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00bc }
            r0.<init>();	 Catch:{ Exception -> 0x00bc }
            r0 = r0.append(r4);	 Catch:{ Exception -> 0x00bc }
            r5 = "/error_fs.dat";
            r0 = r0.append(r5);	 Catch:{ Exception -> 0x00bc }
            r5 = r0.toString();	 Catch:{ Exception -> 0x00bc }
            r0 = new java.io.File;	 Catch:{ Exception -> 0x00bc }
            r0.<init>(r5);	 Catch:{ Exception -> 0x00bc }
            r5 = r0.exists();	 Catch:{ Exception -> 0x00bc }
            if (r5 != 0) goto L_0x009b;
        L_0x0077:
            r5 = new java.io.File;	 Catch:{ Exception -> 0x00bc }
            r5.<init>(r4);	 Catch:{ Exception -> 0x00bc }
            r4 = r5.exists();	 Catch:{ Exception -> 0x00bc }
            if (r4 != 0) goto L_0x0085;
        L_0x0082:
            r5.mkdirs();	 Catch:{ Exception -> 0x00bc }
        L_0x0085:
            r4 = r0.createNewFile();	 Catch:{ Exception -> 0x00bc }
            if (r4 != 0) goto L_0x00c1;
        L_0x008b:
            r8.a(r1, r3, r2);	 Catch:{ Exception -> 0x00bc }
        L_0x008e:
            r0 = android.os.Process.myPid();
            android.os.Process.killProcess(r0);
            return;
        L_0x0096:
            r0 = move-exception;
            r0 = r1;
        L_0x0098:
            r2 = r0;
            r3 = r1;
            goto L_0x003e;
        L_0x009b:
            r1 = new java.io.RandomAccessFile;	 Catch:{ Exception -> 0x00bc }
            r4 = "rw";
            r1.<init>(r0, r4);	 Catch:{ Exception -> 0x00bc }
            r4 = 300; // 0x12c float:4.2E-43 double:1.48E-321;
            r1.seek(r4);	 Catch:{ Exception -> 0x00bc }
            r4 = r1.readLong();	 Catch:{ Exception -> 0x00bc }
            r6 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x00bc }
            r4 = r6 - r4;
            r6 = 604800000; // 0x240c8400 float:3.046947E-17 double:2.988109026E-315;
            r1 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
            if (r1 <= 0) goto L_0x008e;
        L_0x00b8:
            r8.a(r0, r3, r2);	 Catch:{ Exception -> 0x00bc }
            goto L_0x008e;
        L_0x00bc:
            r0 = move-exception;
            goto L_0x008e;
        L_0x00be:
            r0 = move-exception;
            r0 = r2;
            goto L_0x0098;
        L_0x00c1:
            r1 = r0;
            goto L_0x008b;
        L_0x00c3:
            r0 = r1;
            goto L_0x003d;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.f$a.uncaughtException(java.lang.Thread, java.lang.Throwable):void");
        }
    }

    private class b implements Runnable {
        private b() {
        }

        public void run() {
            if (f.this.ah) {
                f.this.ah = false;
                f.this.m867byte();
            }
        }
    }

    public class c {
        /* renamed from: for */
        public static final String f130for = "com.baidu.locTest.LocationServer";
        private long[] a = new long[20];
        private com.baidu.location.c.a b = null;
        /* renamed from: byte */
        private int f131byte = 1;
        /* access modifiers changed from: private */
        public String c = null;
        /* renamed from: case */
        private a f132case = null;
        /* renamed from: char */
        private final int f133char = f.ad;
        private PendingIntent d = null;
        /* renamed from: do */
        private boolean f134do = false;
        /* renamed from: else */
        private boolean f135else = false;
        /* renamed from: goto */
        private Context f136goto = null;
        /* renamed from: if */
        private boolean f137if = false;
        /* renamed from: int */
        private int f138int = 0;
        /* renamed from: long */
        private String f139long = null;
        /* renamed from: new */
        private final long f140new = 86100000;
        /* renamed from: try */
        private AlarmManager f141try = null;
        /* renamed from: void */
        private long f142void = 0;

        public class a extends BroadcastReceiver {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(c.f130for)) {
                    f.this.P.obtainMessage(f.am).sendToTarget();
                    return;
                }
                try {
                    if (action.equals("android.intent.action.BATTERY_CHANGED")) {
                        int intExtra = intent.getIntExtra("status", 0);
                        int intExtra2 = intent.getIntExtra("plugged", 0);
                        switch (intExtra) {
                            case 2:
                                c.this.c = "4";
                                break;
                            case 3:
                            case 4:
                                c.this.c = "3";
                                break;
                            default:
                                c.this.c = null;
                                break;
                        }
                        switch (intExtra2) {
                            case 1:
                                c.this.c = "6";
                                return;
                            case 2:
                                c.this.c = "5";
                                return;
                            default:
                                return;
                        }
                    }
                } catch (Exception e) {
                    c.this.c = null;
                }
            }
        }

        public c(Context context) {
            this.f136goto = context;
            this.f142void = System.currentTimeMillis();
            this.f141try = (AlarmManager) context.getSystemService("alarm");
            this.f132case = new a();
            context.registerReceiver(this.f132case, new IntentFilter(f130for));
            this.d = PendingIntent.getBroadcast(context, 0, new Intent(f130for), 134217728);
            this.f141try.setRepeating(2, j.z, j.z, this.d);
            f.this.registerReceiver(this.f132case, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        }

        public void a() {
            m860if();
            if (f.j != null) {
                try {
                    RandomAccessFile randomAccessFile = new RandomAccessFile(f.j, "rw");
                    if (randomAccessFile.length() < 1) {
                        randomAccessFile.close();
                        return;
                    }
                    randomAccessFile.seek(0);
                    int readInt = randomAccessFile.readInt();
                    int i = (readInt * f.ad) + 4;
                    readInt++;
                    randomAccessFile.seek(0);
                    randomAccessFile.writeInt(readInt);
                    randomAccessFile.seek((long) i);
                    randomAccessFile.writeLong(System.currentTimeMillis());
                    randomAccessFile.writeInt(this.f131byte);
                    randomAccessFile.writeInt(0);
                    randomAccessFile.writeInt(this.f138int);
                    randomAccessFile.writeInt(this.b.f93do);
                    randomAccessFile.writeInt(this.b.f95if);
                    randomAccessFile.writeInt(this.b.f94for);
                    randomAccessFile.writeInt(this.b.f98try);
                    byte[] bArr = new byte[160];
                    for (int i2 = 0; i2 < this.f138int; i2++) {
                        bArr[(i2 * 8) + 7] = (byte) ((int) this.a[i2]);
                        bArr[(i2 * 8) + 6] = (byte) ((int) (this.a[i2] >> 8));
                        bArr[(i2 * 8) + 5] = (byte) ((int) (this.a[i2] >> 16));
                        bArr[(i2 * 8) + 4] = (byte) ((int) (this.a[i2] >> 24));
                        bArr[(i2 * 8) + 3] = (byte) ((int) (this.a[i2] >> 32));
                        bArr[(i2 * 8) + 2] = (byte) ((int) (this.a[i2] >> 40));
                        bArr[(i2 * 8) + 1] = (byte) ((int) (this.a[i2] >> 48));
                        bArr[(i2 * 8) + 0] = (byte) ((int) (this.a[i2] >> f.ai));
                    }
                    if (this.f138int > 0) {
                        randomAccessFile.write(bArr, 0, this.f138int * 8);
                    }
                    randomAccessFile.writeInt(this.f138int);
                    randomAccessFile.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /* renamed from: byte */
        public void m856byte() {
            int i = 0;
            if (this.f134do) {
                this.f131byte = 1;
                j.z = (j.B * 1000) * 60;
                j.q = j.z >> 2;
                Calendar instance = Calendar.getInstance();
                int i2 = instance.get(5);
                int i3 = instance.get(1);
                if (i3 > f.f146else) {
                    i = i3 - 2000;
                }
                i3 = instance.get(2) + 1;
                int i4 = instance.get(11);
                String str = i + "," + i3 + "," + i2 + "," + i4 + "," + instance.get(f.c) + "," + j.B;
                if (this.f137if) {
                    this.f139long = "&tr=" + j.f + "," + str;
                } else {
                    this.f139long += "|T" + str;
                }
                j.a(f.v, "trace begin:" + this.f139long);
                try {
                    RandomAccessFile randomAccessFile = new RandomAccessFile(f.k, "rw");
                    randomAccessFile.seek(12);
                    randomAccessFile.writeLong(System.currentTimeMillis());
                    randomAccessFile.writeInt(this.f131byte);
                    randomAccessFile.close();
                    randomAccessFile = new RandomAccessFile(f.j, "rw");
                    randomAccessFile.seek(0);
                    randomAccessFile.writeInt(0);
                    randomAccessFile.close();
                } catch (Exception e) {
                }
            }
        }

        /* renamed from: case */
        public void m857case() {
            f.m885goto();
            if (f.k != null) {
                try {
                    RandomAccessFile randomAccessFile = new RandomAccessFile(f.k, "rw");
                    if (randomAccessFile.length() < 1) {
                        randomAccessFile.close();
                        return;
                    }
                    randomAccessFile.seek(0);
                    int readInt = randomAccessFile.readInt();
                    int readInt2 = randomAccessFile.readInt();
                    int readInt3 = randomAccessFile.readInt();
                    if (this.f134do && this.f137if) {
                        j.a(f.v, "trace new info:" + readInt + ":" + readInt2 + ":" + readInt3);
                        int i = (readInt2 + 1) % f.ad;
                        randomAccessFile.seek(4);
                        randomAccessFile.writeInt(i);
                        readInt++;
                        if (readInt >= f.ad) {
                            readInt = 199;
                        }
                        if (i == readInt3 && readInt > 0) {
                            readInt3 = (readInt3 + 1) % f.ad;
                            randomAccessFile.writeInt(readInt3);
                        }
                        j.a(f.v, "trace new info:" + readInt + ":" + readInt2 + ":" + readInt3);
                        readInt3 = (i * f.ar) + 24;
                    } else {
                        readInt3 = (readInt2 * f.ar) + 24;
                    }
                    randomAccessFile.seek((long) (readInt3 + 4));
                    byte[] bytes = this.f139long.getBytes();
                    for (int i2 = 0; i2 < bytes.length; i2++) {
                        bytes[i2] = (byte) (bytes[i2] ^ 90);
                    }
                    randomAccessFile.write(bytes, 0, bytes.length);
                    randomAccessFile.writeInt(bytes.length);
                    randomAccessFile.seek((long) readInt3);
                    randomAccessFile.writeInt(bytes.length);
                    if (this.f134do && this.f137if) {
                        randomAccessFile.seek(0);
                        randomAccessFile.writeInt(readInt);
                    }
                    randomAccessFile.close();
                } catch (Exception e) {
                }
            }
        }

        /* renamed from: do */
        public void m858do() {
            int i = 0;
            try {
                j.a(f.v, "regular expire...");
                m862new();
                if (this.f135else) {
                    this.f135else = false;
                    return;
                }
                m856byte();
                this.f138int = 0;
                this.b = null;
                if (f.this.E != null) {
                    f.this.E.m849new();
                }
                if (f.this.E != null) {
                    c cVar = f.this.E.m843byte();
                    if (!(cVar == null || cVar.f113do == null)) {
                        int size = cVar.f113do.size();
                        if (size > 20) {
                            size = 20;
                        }
                        int i2 = 0;
                        while (i2 < size) {
                            int i3;
                            String replace = ((ScanResult) cVar.f113do.get(i2)).BSSID.replace(":", "");
                            try {
                                i3 = i + 1;
                                try {
                                    this.a[i] = Long.parseLong(replace, 16);
                                } catch (Exception e) {
                                }
                            } catch (Exception e2) {
                                i3 = i;
                            }
                            i2++;
                            i = i3;
                        }
                        this.f138int = i;
                    }
                }
                if (f.this.r != null) {
                    this.b = f.this.r.a();
                }
                if (this.b != null) {
                    m859for();
                }
            } catch (Exception e3) {
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:67:0x0291  */
        /* renamed from: for */
        public void m859for() {
            /*
            r20 = this;
            r3 = 0;
            r20.m860if();
            r1 = "baidu_location_service";
            r2 = new java.lang.StringBuilder;
            r2.<init>();
            r4 = "trace1:";
            r2 = r2.append(r4);
            r0 = r20;
            r4 = r0.f139long;
            r2 = r2.append(r4);
            r2 = r2.toString();
            com.baidu.location.j.a(r1, r2);
            r0 = r20;
            r1 = com.baidu.location.f.this;	 Catch:{ Exception -> 0x004e }
            r1 = r1.m909char();	 Catch:{ Exception -> 0x004e }
            if (r1 == 0) goto L_0x004b;
        L_0x002a:
            r1 = "y2";
        L_0x002c:
            r0 = r20;
            r2 = r0.f134do;
            if (r2 != 0) goto L_0x0452;
        L_0x0032:
            r7 = new java.io.RandomAccessFile;	 Catch:{ Exception -> 0x044c }
            r2 = com.baidu.location.f.j;	 Catch:{ Exception -> 0x044c }
            r4 = "rw";
            r7.<init>(r2, r4);	 Catch:{ Exception -> 0x044c }
            r4 = r7.length();	 Catch:{ Exception -> 0x044c }
            r8 = 1;
            r2 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1));
            if (r2 >= 0) goto L_0x0052;
        L_0x0047:
            r7.close();	 Catch:{ Exception -> 0x044c }
        L_0x004a:
            return;
        L_0x004b:
            r1 = "y1";
            goto L_0x002c;
        L_0x004e:
            r1 = move-exception;
            r1 = "y";
            goto L_0x002c;
        L_0x0052:
            r8 = r7.readInt();	 Catch:{ Exception -> 0x044c }
            r2 = 0;
            r6 = r2;
        L_0x0058:
            if (r6 >= r8) goto L_0x0452;
        L_0x005a:
            r2 = r6 * 200;
            r2 = r2 + 4;
            r4 = (long) r2;	 Catch:{ Exception -> 0x044c }
            r7.seek(r4);	 Catch:{ Exception -> 0x044c }
            r7.readLong();	 Catch:{ Exception -> 0x044c }
            r9 = r7.readInt();	 Catch:{ Exception -> 0x044c }
            r10 = r7.readInt();	 Catch:{ Exception -> 0x044c }
            r11 = r7.readInt();	 Catch:{ Exception -> 0x044c }
            r2 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
            r4 = new byte[r2];	 Catch:{ Exception -> 0x044c }
            r2 = 0;
            r5 = r11 * 8;
            r5 = r5 + 16;
            r7.read(r4, r2, r5);	 Catch:{ Exception -> 0x044c }
            r2 = 3;
            r2 = r4[r2];	 Catch:{ Exception -> 0x044c }
            r2 = r2 & 255;
            r5 = 2;
            r5 = r4[r5];	 Catch:{ Exception -> 0x044c }
            r5 = r5 << 8;
            r12 = 65280; // 0xff00 float:9.1477E-41 double:3.22526E-319;
            r5 = r5 & r12;
            r2 = r2 | r5;
            r5 = 1;
            r5 = r4[r5];	 Catch:{ Exception -> 0x044c }
            r5 = r5 << 16;
            r12 = 16711680; // 0xff0000 float:2.3418052E-38 double:8.256667E-317;
            r5 = r5 & r12;
            r2 = r2 | r5;
            r5 = 0;
            r5 = r4[r5];	 Catch:{ Exception -> 0x044c }
            r5 = r5 << 24;
            r12 = -16777216; // 0xffffffffff000000 float:-1.7014118E38 double:NaN;
            r5 = r5 & r12;
            r2 = r2 | r5;
            r5 = 7;
            r5 = r4[r5];	 Catch:{ Exception -> 0x044c }
            r5 = r5 & 255;
            r12 = 6;
            r12 = r4[r12];	 Catch:{ Exception -> 0x044c }
            r12 = r12 << 8;
            r13 = 65280; // 0xff00 float:9.1477E-41 double:3.22526E-319;
            r12 = r12 & r13;
            r5 = r5 | r12;
            r12 = 5;
            r12 = r4[r12];	 Catch:{ Exception -> 0x044c }
            r12 = r12 << 16;
            r13 = 16711680; // 0xff0000 float:2.3418052E-38 double:8.256667E-317;
            r12 = r12 & r13;
            r5 = r5 | r12;
            r12 = 4;
            r12 = r4[r12];	 Catch:{ Exception -> 0x044c }
            r12 = r12 << 24;
            r13 = -16777216; // 0xffffffffff000000 float:-1.7014118E38 double:NaN;
            r12 = r12 & r13;
            r5 = r5 | r12;
            r12 = 11;
            r12 = r4[r12];	 Catch:{ Exception -> 0x044c }
            r12 = r12 & 255;
            r13 = 10;
            r13 = r4[r13];	 Catch:{ Exception -> 0x044c }
            r13 = r13 << 8;
            r14 = 65280; // 0xff00 float:9.1477E-41 double:3.22526E-319;
            r13 = r13 & r14;
            r12 = r12 | r13;
            r13 = 9;
            r13 = r4[r13];	 Catch:{ Exception -> 0x044c }
            r13 = r13 << 16;
            r14 = 16711680; // 0xff0000 float:2.3418052E-38 double:8.256667E-317;
            r13 = r13 & r14;
            r12 = r12 | r13;
            r13 = 8;
            r13 = r4[r13];	 Catch:{ Exception -> 0x044c }
            r13 = r13 << 24;
            r14 = -16777216; // 0xffffffffff000000 float:-1.7014118E38 double:NaN;
            r13 = r13 & r14;
            r12 = r12 | r13;
            r13 = 15;
            r13 = r4[r13];	 Catch:{ Exception -> 0x044c }
            r13 = r13 & 255;
            r14 = 14;
            r14 = r4[r14];	 Catch:{ Exception -> 0x044c }
            r14 = r14 << 8;
            r15 = 65280; // 0xff00 float:9.1477E-41 double:3.22526E-319;
            r14 = r14 & r15;
            r13 = r13 | r14;
            r14 = 13;
            r14 = r4[r14];	 Catch:{ Exception -> 0x044c }
            r14 = r14 << 16;
            r15 = 16711680; // 0xff0000 float:2.3418052E-38 double:8.256667E-317;
            r14 = r14 & r15;
            r13 = r13 | r14;
            r14 = 12;
            r14 = r4[r14];	 Catch:{ Exception -> 0x044c }
            r14 = r14 << 24;
            r15 = -16777216; // 0xffffffffff000000 float:-1.7014118E38 double:NaN;
            r14 = r14 & r15;
            r13 = r13 | r14;
            r0 = r20;
            r14 = r0.b;	 Catch:{ Exception -> 0x044c }
            r14 = r14.f93do;	 Catch:{ Exception -> 0x044c }
            if (r14 != r2) goto L_0x03aa;
        L_0x0111:
            r0 = r20;
            r2 = r0.b;	 Catch:{ Exception -> 0x044c }
            r2 = r2.f95if;	 Catch:{ Exception -> 0x044c }
            if (r2 != r5) goto L_0x03aa;
        L_0x0119:
            r0 = r20;
            r2 = r0.b;	 Catch:{ Exception -> 0x044c }
            r2 = r2.f94for;	 Catch:{ Exception -> 0x044c }
            if (r2 != r12) goto L_0x03aa;
        L_0x0121:
            r0 = r20;
            r2 = r0.b;	 Catch:{ Exception -> 0x044c }
            r2 = r2.f98try;	 Catch:{ Exception -> 0x044c }
            if (r2 != r13) goto L_0x03aa;
        L_0x0129:
            r12 = new long[r11];	 Catch:{ Exception -> 0x044c }
            r2 = 0;
        L_0x012c:
            if (r2 >= r11) goto L_0x01b5;
        L_0x012e:
            r5 = r2 * 8;
            r5 = r5 + 16;
            r5 = r4[r5];	 Catch:{ Exception -> 0x044c }
            r13 = (long) r5;	 Catch:{ Exception -> 0x044c }
            r15 = 255; // 0xff float:3.57E-43 double:1.26E-321;
            r13 = r13 & r15;
            r5 = 56;
            r13 = r13 << r5;
            r5 = r2 * 8;
            r5 = r5 + 16;
            r5 = r5 + 1;
            r5 = r4[r5];	 Catch:{ Exception -> 0x044c }
            r15 = (long) r5;	 Catch:{ Exception -> 0x044c }
            r17 = 255; // 0xff float:3.57E-43 double:1.26E-321;
            r15 = r15 & r17;
            r5 = 48;
            r15 = r15 << r5;
            r13 = r13 | r15;
            r5 = r2 * 8;
            r5 = r5 + 16;
            r5 = r5 + 2;
            r5 = r4[r5];	 Catch:{ Exception -> 0x044c }
            r15 = (long) r5;	 Catch:{ Exception -> 0x044c }
            r17 = 255; // 0xff float:3.57E-43 double:1.26E-321;
            r15 = r15 & r17;
            r5 = 40;
            r15 = r15 << r5;
            r13 = r13 | r15;
            r5 = r2 * 8;
            r5 = r5 + 16;
            r5 = r5 + 3;
            r5 = r4[r5];	 Catch:{ Exception -> 0x044c }
            r15 = (long) r5;	 Catch:{ Exception -> 0x044c }
            r17 = 255; // 0xff float:3.57E-43 double:1.26E-321;
            r15 = r15 & r17;
            r5 = 32;
            r15 = r15 << r5;
            r13 = r13 | r15;
            r5 = r2 * 8;
            r5 = r5 + 16;
            r5 = r5 + 4;
            r5 = r4[r5];	 Catch:{ Exception -> 0x044c }
            r15 = (long) r5;	 Catch:{ Exception -> 0x044c }
            r17 = 255; // 0xff float:3.57E-43 double:1.26E-321;
            r15 = r15 & r17;
            r5 = 24;
            r15 = r15 << r5;
            r13 = r13 | r15;
            r5 = r2 * 8;
            r5 = r5 + 16;
            r5 = r5 + 5;
            r5 = r4[r5];	 Catch:{ Exception -> 0x044c }
            r15 = (long) r5;	 Catch:{ Exception -> 0x044c }
            r17 = 255; // 0xff float:3.57E-43 double:1.26E-321;
            r15 = r15 & r17;
            r5 = 16;
            r15 = r15 << r5;
            r13 = r13 | r15;
            r5 = r2 * 8;
            r5 = r5 + 16;
            r5 = r5 + 6;
            r5 = r4[r5];	 Catch:{ Exception -> 0x044c }
            r15 = (long) r5;	 Catch:{ Exception -> 0x044c }
            r17 = 255; // 0xff float:3.57E-43 double:1.26E-321;
            r15 = r15 & r17;
            r5 = 8;
            r15 = r15 << r5;
            r13 = r13 | r15;
            r5 = r2 * 8;
            r5 = r5 + 16;
            r5 = r5 + 7;
            r5 = r4[r5];	 Catch:{ Exception -> 0x044c }
            r15 = (long) r5;	 Catch:{ Exception -> 0x044c }
            r17 = 255; // 0xff float:3.57E-43 double:1.26E-321;
            r15 = r15 & r17;
            r13 = r13 | r15;
            r12[r2] = r13;	 Catch:{ Exception -> 0x044c }
            r2 = r2 + 1;
            goto L_0x012c;
        L_0x01b5:
            r4 = 0;
            r2 = 0;
            r5 = r2;
        L_0x01b8:
            r0 = r20;
            r2 = r0.f138int;	 Catch:{ Exception -> 0x044c }
            if (r5 >= r2) goto L_0x01dc;
        L_0x01be:
            r2 = 0;
            r19 = r2;
            r2 = r4;
            r4 = r19;
        L_0x01c4:
            if (r4 >= r11) goto L_0x01d7;
        L_0x01c6:
            r0 = r20;
            r13 = r0.a;	 Catch:{ Exception -> 0x044c }
            r13 = r13[r5];	 Catch:{ Exception -> 0x044c }
            r15 = r12[r4];	 Catch:{ Exception -> 0x044c }
            r13 = (r13 > r15 ? 1 : (r13 == r15 ? 0 : -1));
            if (r13 != 0) goto L_0x01d4;
        L_0x01d2:
            r2 = r2 + 1;
        L_0x01d4:
            r4 = r4 + 1;
            goto L_0x01c4;
        L_0x01d7:
            r4 = r5 + 1;
            r5 = r4;
            r4 = r2;
            goto L_0x01b8;
        L_0x01dc:
            r2 = 5;
            if (r4 > r2) goto L_0x022e;
        L_0x01df:
            r2 = r4 * 8;
            r0 = r20;
            r4 = r0.f138int;	 Catch:{ Exception -> 0x044c }
            r4 = r4 + r11;
            if (r2 > r4) goto L_0x022e;
        L_0x01e8:
            if (r11 != 0) goto L_0x01f0;
        L_0x01ea:
            r0 = r20;
            r2 = r0.f138int;	 Catch:{ Exception -> 0x044c }
            if (r2 == 0) goto L_0x022e;
        L_0x01f0:
            r2 = 1;
            if (r11 != r2) goto L_0x0208;
        L_0x01f3:
            r0 = r20;
            r2 = r0.f138int;	 Catch:{ Exception -> 0x044c }
            r4 = 1;
            if (r2 != r4) goto L_0x0208;
        L_0x01fa:
            r0 = r20;
            r2 = r0.a;	 Catch:{ Exception -> 0x044c }
            r4 = 0;
            r4 = r2[r4];	 Catch:{ Exception -> 0x044c }
            r2 = 0;
            r13 = r12[r2];	 Catch:{ Exception -> 0x044c }
            r2 = (r4 > r13 ? 1 : (r4 == r13 ? 0 : -1));
            if (r2 == 0) goto L_0x022e;
        L_0x0208:
            r2 = 1;
            if (r11 <= r2) goto L_0x03aa;
        L_0x020b:
            r0 = r20;
            r2 = r0.f138int;	 Catch:{ Exception -> 0x044c }
            r4 = 1;
            if (r2 <= r4) goto L_0x03aa;
        L_0x0212:
            r0 = r20;
            r2 = r0.a;	 Catch:{ Exception -> 0x044c }
            r4 = 0;
            r4 = r2[r4];	 Catch:{ Exception -> 0x044c }
            r2 = 0;
            r13 = r12[r2];	 Catch:{ Exception -> 0x044c }
            r2 = (r4 > r13 ? 1 : (r4 == r13 ? 0 : -1));
            if (r2 != 0) goto L_0x03aa;
        L_0x0220:
            r0 = r20;
            r2 = r0.a;	 Catch:{ Exception -> 0x044c }
            r4 = 1;
            r4 = r2[r4];	 Catch:{ Exception -> 0x044c }
            r2 = 1;
            r11 = r12[r2];	 Catch:{ Exception -> 0x044c }
            r2 = (r4 > r11 ? 1 : (r4 == r11 ? 0 : -1));
            if (r2 != 0) goto L_0x03aa;
        L_0x022e:
            r2 = 1;
            r3 = r10 + 1;
            r4 = r6 * 200;
            r4 = r4 + 16;
            r4 = (long) r4;	 Catch:{ Exception -> 0x044c }
            r7.seek(r4);	 Catch:{ Exception -> 0x044c }
            r7.writeInt(r3);	 Catch:{ Exception -> 0x044c }
            r0 = r20;
            r3 = r0.f139long;	 Catch:{ Exception -> 0x044c }
            if (r3 == 0) goto L_0x0288;
        L_0x0242:
            r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x044c }
            r3.<init>();	 Catch:{ Exception -> 0x044c }
            r0 = r20;
            r4 = r0.f139long;	 Catch:{ Exception -> 0x044c }
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x044c }
            r4 = "|";
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x044c }
            r3 = r3.append(r9);	 Catch:{ Exception -> 0x044c }
            r3 = r3.append(r1);	 Catch:{ Exception -> 0x044c }
            r3 = r3.toString();	 Catch:{ Exception -> 0x044c }
            r0 = r20;
            r0.f139long = r3;	 Catch:{ Exception -> 0x044c }
            r0 = r20;
            r3 = r0.c;	 Catch:{ Exception -> 0x044c }
            if (r3 == 0) goto L_0x0288;
        L_0x026b:
            r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x044c }
            r3.<init>();	 Catch:{ Exception -> 0x044c }
            r0 = r20;
            r4 = r0.f139long;	 Catch:{ Exception -> 0x044c }
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x044c }
            r0 = r20;
            r4 = r0.c;	 Catch:{ Exception -> 0x044c }
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x044c }
            r3 = r3.toString();	 Catch:{ Exception -> 0x044c }
            r0 = r20;
            r0.f139long = r3;	 Catch:{ Exception -> 0x044c }
        L_0x0288:
            r3 = "baidu_location_service";
            r4 = "daily info:is same";
            com.baidu.location.j.a(r3, r4);	 Catch:{ Exception -> 0x044c }
        L_0x028f:
            if (r2 != 0) goto L_0x0384;
        L_0x0291:
            r0 = r20;
            r2 = r0.b;
            r2 = r2.f93do;
            r3 = 460; // 0x1cc float:6.45E-43 double:2.273E-321;
            if (r2 != r3) goto L_0x03af;
        L_0x029b:
            r2 = "|x,";
        L_0x029d:
            r3 = new java.lang.StringBuilder;
            r3.<init>();
            r2 = r3.append(r2);
            r0 = r20;
            r3 = r0.b;
            r3 = r3.f95if;
            r2 = r2.append(r3);
            r3 = ",";
            r2 = r2.append(r3);
            r0 = r20;
            r3 = r0.b;
            r3 = r3.f94for;
            r2 = r2.append(r3);
            r3 = ",";
            r2 = r2.append(r3);
            r0 = r20;
            r3 = r0.b;
            r3 = r3.f98try;
            r2 = r2.append(r3);
            r4 = r2.toString();
            r2 = 0;
            r0 = r20;
            r5 = com.baidu.location.f.this;
            r5 = r5.E;
            if (r5 == 0) goto L_0x02f4;
        L_0x02e0:
            r0 = r20;
            r5 = com.baidu.location.f.this;
            r5 = r5.E;
            r5 = r5.m845char();
            if (r5 == 0) goto L_0x02f4;
        L_0x02ee:
            r6 = 16;
            r2 = java.lang.Long.parseLong(r5, r6);	 Catch:{ Exception -> 0x0449 }
        L_0x02f4:
            r0 = r20;
            r5 = r0.f138int;
            r6 = 1;
            if (r5 != r6) goto L_0x03b3;
        L_0x02fb:
            r5 = new java.lang.StringBuilder;
            r5.<init>();
            r4 = r5.append(r4);
            r5 = "w";
            r4 = r4.append(r5);
            r0 = r20;
            r5 = r0.a;
            r6 = 0;
            r5 = r5[r6];
            r5 = java.lang.Long.toHexString(r5);
            r4 = r4.append(r5);
            r5 = "k";
            r4 = r4.append(r5);
            r4 = r4.toString();
            r0 = r20;
            r5 = r0.a;
            r6 = 0;
            r5 = r5[r6];
            r2 = (r5 > r2 ? 1 : (r5 == r2 ? 0 : -1));
            if (r2 != 0) goto L_0x044f;
        L_0x032e:
            r2 = new java.lang.StringBuilder;
            r2.<init>();
            r2 = r2.append(r4);
            r3 = "k";
            r2 = r2.append(r3);
            r2 = r2.toString();
        L_0x0341:
            r3 = new java.lang.StringBuilder;
            r3.<init>();
            r0 = r20;
            r4 = r0.f139long;
            r3 = r3.append(r4);
            r2 = r3.append(r2);
            r1 = r2.append(r1);
            r1 = r1.toString();
            r0 = r20;
            r0.f139long = r1;
            r0 = r20;
            r1 = r0.c;
            if (r1 == 0) goto L_0x0381;
        L_0x0364:
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r0 = r20;
            r2 = r0.f139long;
            r1 = r1.append(r2);
            r0 = r20;
            r2 = r0.c;
            r1 = r1.append(r2);
            r1 = r1.toString();
            r0 = r20;
            r0.f139long = r1;
        L_0x0381:
            r20.a();
        L_0x0384:
            r1 = "baidu_location_service";
            r2 = new java.lang.StringBuilder;
            r2.<init>();
            r3 = "trace2:";
            r2 = r2.append(r3);
            r0 = r20;
            r3 = r0.f139long;
            r2 = r2.append(r3);
            r2 = r2.toString();
            com.baidu.location.j.a(r1, r2);
            r20.m857case();
            r1 = 0;
            r0 = r20;
            r0.f139long = r1;
            goto L_0x004a;
        L_0x03aa:
            r2 = r6 + 1;
            r6 = r2;
            goto L_0x0058;
        L_0x03af:
            r2 = "|x460,";
            goto L_0x029d;
        L_0x03b3:
            r0 = r20;
            r5 = r0.f138int;
            r6 = 1;
            if (r5 <= r6) goto L_0x044f;
        L_0x03ba:
            r5 = new java.lang.StringBuilder;
            r5.<init>();
            r4 = r5.append(r4);
            r5 = "w";
            r4 = r4.append(r5);
            r0 = r20;
            r5 = r0.a;
            r6 = 0;
            r5 = r5[r6];
            r5 = java.lang.Long.toHexString(r5);
            r4 = r4.append(r5);
            r4 = r4.toString();
            r0 = r20;
            r5 = r0.a;
            r6 = 0;
            r5 = r5[r6];
            r5 = (r5 > r2 ? 1 : (r5 == r2 ? 0 : -1));
            if (r5 != 0) goto L_0x03fc;
        L_0x03e7:
            r2 = new java.lang.StringBuilder;
            r2.<init>();
            r2 = r2.append(r4);
            r3 = "k";
            r2 = r2.append(r3);
            r4 = r2.toString();
            r2 = 0;
        L_0x03fc:
            r5 = 0;
            r5 = (r2 > r5 ? 1 : (r2 == r5 ? 0 : -1));
            if (r5 <= 0) goto L_0x0425;
        L_0x0402:
            r5 = new java.lang.StringBuilder;
            r5.<init>();
            r4 = r5.append(r4);
            r5 = ",";
            r4 = r4.append(r5);
            r2 = java.lang.Long.toHexString(r2);
            r2 = r4.append(r2);
            r3 = "k";
            r2 = r2.append(r3);
            r2 = r2.toString();
            goto L_0x0341;
        L_0x0425:
            r2 = new java.lang.StringBuilder;
            r2.<init>();
            r2 = r2.append(r4);
            r3 = ",";
            r2 = r2.append(r3);
            r0 = r20;
            r3 = r0.a;
            r4 = 1;
            r3 = r3[r4];
            r3 = java.lang.Long.toHexString(r3);
            r2 = r2.append(r3);
            r2 = r2.toString();
            goto L_0x0341;
        L_0x0449:
            r5 = move-exception;
            goto L_0x02f4;
        L_0x044c:
            r1 = move-exception;
            goto L_0x004a;
        L_0x044f:
            r2 = r4;
            goto L_0x0341;
        L_0x0452:
            r2 = r3;
            goto L_0x028f;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.f$c.m859for():void");
        }

        /* renamed from: if */
        public void m860if() {
            try {
                if (f.this.m != null) {
                    f.j = new File(f.this.m);
                    if (!f.j.exists()) {
                        File file = new File(f.aa);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        f.j.createNewFile();
                        RandomAccessFile randomAccessFile = new RandomAccessFile(f.j, "rw");
                        randomAccessFile.seek(0);
                        randomAccessFile.writeInt(0);
                        randomAccessFile.close();
                        return;
                    }
                    return;
                }
                f.j = null;
            } catch (Exception e) {
                e.printStackTrace();
                f.j = null;
            }
        }

        /* renamed from: int */
        public void m861int() {
        }

        /* renamed from: new */
        public void m862new() {
            int i = 0;
            this.f134do = false;
            this.f137if = false;
            m860if();
            f.m885goto();
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(f.k, "rw");
                randomAccessFile.seek(0);
                int readInt = randomAccessFile.readInt();
                int readInt2 = randomAccessFile.readInt();
                randomAccessFile.readInt();
                long readLong = randomAccessFile.readLong();
                int readInt3 = randomAccessFile.readInt();
                if (readInt < 0) {
                    this.f134do = true;
                    this.f137if = true;
                    randomAccessFile.close();
                    return;
                }
                randomAccessFile.seek((long) ((readInt2 * f.ar) + 24));
                readInt = randomAccessFile.readInt();
                if (readInt > 680) {
                    this.f134do = true;
                    this.f137if = true;
                    randomAccessFile.close();
                    return;
                }
                byte[] bArr = new byte[f.ar];
                randomAccessFile.read(bArr, 0, readInt);
                if (readInt != randomAccessFile.readInt()) {
                    j.a(f.v, "trace true check fail");
                    this.f134do = true;
                    this.f137if = true;
                    randomAccessFile.close();
                    return;
                }
                while (i < bArr.length) {
                    bArr[i] = (byte) (bArr[i] ^ 90);
                    i++;
                }
                this.f139long = new String(bArr, 0, readInt);
                if (this.f139long.contains("&tr=")) {
                    long currentTimeMillis = System.currentTimeMillis();
                    readLong = currentTimeMillis - readLong;
                    if (readLong > (j.z * 3) - j.q) {
                        this.f134do = true;
                    } else if (readLong > (j.z * 2) - j.q) {
                        this.f139long += "|" + readInt3;
                        this.f131byte = readInt3 + 2;
                    } else if (readLong > j.z - j.q) {
                        this.f131byte = readInt3 + 1;
                    } else {
                        this.f135else = true;
                        randomAccessFile.close();
                        return;
                    }
                    randomAccessFile.seek(12);
                    randomAccessFile.writeLong(currentTimeMillis);
                    randomAccessFile.writeInt(this.f131byte);
                    randomAccessFile.close();
                    RandomAccessFile randomAccessFile2 = new RandomAccessFile(f.j, "rw");
                    randomAccessFile2.seek(0);
                    if (randomAccessFile2.readInt() == 0) {
                        this.f134do = true;
                        randomAccessFile2.close();
                        j.a(f.v, "Day file number 0");
                        return;
                    }
                    randomAccessFile2.close();
                    return;
                }
                this.f134do = true;
                this.f137if = true;
                randomAccessFile.close();
            } catch (Exception e) {
                e.printStackTrace();
                j.a(f.v, "exception!!!");
                this.f134do = true;
                this.f137if = true;
            }
        }

        /* renamed from: try */
        public void m863try() {
            this.f136goto.unregisterReceiver(this.f132case);
            this.f141try.cancel(this.d);
            f.j = null;
        }
    }

    public class d extends Handler {
        public void handleMessage(Message message) {
            if (f.this.ab) {
                switch (message.what) {
                    case 11:
                        f.this.m875do(message);
                        break;
                    case f.c /*12*/:
                        f.this.m904try(message);
                        break;
                    case 15:
                        f.this.m868byte(message);
                        break;
                    case 21:
                        f.this.a(message, 21);
                        break;
                    case f.f147for /*22*/:
                        f.this.m901new(message);
                        break;
                    case 24:
                        f.this.a(message);
                        break;
                    case f.U /*25*/:
                        f.this.m882for(message);
                        break;
                    case f.H /*26*/:
                        f.this.a(message, (int) f.H);
                        break;
                    case f.f145do /*28*/:
                        f.this.m894int(message);
                        break;
                    case f.f148int /*31*/:
                        f.this.m879else();
                        break;
                    case f.p /*41*/:
                        f.this.m874do();
                        break;
                    case 51:
                        f.this.m888if();
                        break;
                    case 52:
                        f.this.m908void();
                        break;
                    case f.ap /*53*/:
                        f.this.b();
                        break;
                    case f.C /*57*/:
                        f.this.m889if(message);
                        break;
                    case 62:
                    case 63:
                        f.this.a(21);
                        break;
                    case 64:
                    case 65:
                        f.this.a((int) f.H);
                        break;
                    case 81:
                        f.this.m903try();
                        break;
                    case f.t /*91*/:
                        f.this.m893int();
                        break;
                    case f.am /*101*/:
                        if (j.f209try && f.this.y != null) {
                            f.this.y.m858do();
                            break;
                        }
                }
            }
            super.handleMessage(message);
        }
    }

    private String a(String str) {
        String str2;
        String str3 = null;
        j.a(v, "generate locdata ...");
        if ((this.f155try == null || !this.f155try.m797do()) && this.r != null) {
            this.f155try = this.r.a();
        }
        this.A = this.f155try.a();
        if (this.f155try != null) {
            j.m976if(v, this.f155try.m799if());
        } else {
            j.m976if(v, "cellInfo null...");
        }
        if ((this.B == null || !this.B.m824do()) && this.E != null) {
            this.B = this.E.m843byte();
        }
        if (this.B != null) {
            j.m976if(v, this.B.m823case());
        } else {
            j.m976if(v, "wifi list null");
        }
        if (this.Z == null || !this.Z.m791for()) {
            this.f = null;
        } else {
            this.f = this.Z.m792int();
        }
        if (this.as != null) {
            str3 = this.as.m720byte();
        }
        if (3 == g.m919do((Context) this)) {
            str2 = "&cn=32";
        } else {
            str2 = String.format("&cn=%d", new Object[]{Integer.valueOf(this.r.m813new())});
        }
        if (this.W) {
            str2 = str2 + "&rq=1";
        }
        str3 = str2 + str3;
        if (str != null) {
            str3 = str + str3;
        }
        return j.a(this.f155try, this.B, this.f, str3, 0);
    }

    private String a(boolean z) {
        if ((this.f155try == null || !this.f155try.m797do()) && this.r != null) {
            this.f155try = this.r.a();
        }
        m877do(this.f155try.a());
        return m887if(z);
    }

    /* access modifiers changed from: private */
    public void a(int i) {
        j.a(v, "on network exception");
        j.m976if(v, "on network exception");
        this.f154new = null;
        this.f151char = null;
        if (this.as != null) {
            this.as.a(a(false), i);
        }
        if (i == 21) {
            m872case();
        }
    }

    /* access modifiers changed from: private */
    public void a(Message message) {
        j.a(v, "manual upload ...");
        double d = message.getData().getDouble(GroupChatInvitation.ELEMENT_NAME);
        double d2 = message.getData().getDouble("y");
        String string = message.getData().getString("addr");
        if (!(this.r == null || this.E == null || this.as == null)) {
            k.a(this.r.a(), this.E.m848int(), this.as.m720byte(), d, d2, string);
        }
        m867byte();
    }

    /* access modifiers changed from: private */
    public void a(Message message, int i) {
        j.a(v, "on network success");
        j.m976if(v, "on network success");
        String str = (String) message.obj;
        j.a(v, "network:" + str);
        if (this.as != null) {
            this.as.a(str, i);
        }
        if (j.a(str)) {
            if (i == 21) {
                this.f154new = str;
            } else {
                this.G = str;
            }
        } else if (i == 21) {
            this.f154new = null;
        } else {
            this.G = null;
        }
        int i2 = j.m973if(str, "ssid\":\"", "\"");
        if (i2 == ExploreByTouchHelper.INVALID_ID || this.f151char == null) {
            this.h = null;
        } else {
            this.h = this.f151char.m827if(i2);
        }
        m891if(str);
        double d = j.m969do(str, "a\":\"", "\"");
        if (d != Double.MIN_VALUE) {
            k.a(d, j.m969do(str, "b\":\"", "\""), j.m969do(str, "c\":\"", "\""), j.m969do(str, "b\":\"", "\""));
        }
        i2 = j.m973if(str, "rWifiN\":\"", "\"");
        if (i2 > 15) {
            j.F = i2;
        }
        i2 = j.m973if(str, "rWifiT\":\"", "\"");
        if (i2 > 500) {
            j.h = i2;
        }
        float a = j.a(str, "hSpeedDis\":\"", "\"");
        if (a > 5.0f) {
            j.H = a;
        }
        a = j.a(str, "mSpeedDis\":\"", "\"");
        if (a > 5.0f) {
            j.d = a;
        }
        a = j.a(str, "mWifiR\":\"", "\"");
        if (a < 1.0f && ((double) a) > 0.2d) {
            j.f208new = a;
        }
        if (i == 21) {
            m872case();
        }
    }

    private boolean a(com.baidu.location.c.a aVar) {
        boolean z = true;
        if (this.r == null) {
            return false;
        }
        this.f155try = this.r.a();
        if (this.f155try == aVar) {
            return false;
        }
        if (this.f155try == null || aVar == null) {
            return true;
        }
        if (aVar.a(this.f155try)) {
            z = false;
        }
        return z;
    }

    private boolean a(c cVar) {
        boolean z = true;
        if (this.E == null) {
            return false;
        }
        this.B = this.E.m843byte();
        if (cVar == this.B) {
            return false;
        }
        if (this.B == null || cVar == null) {
            return true;
        }
        if (cVar.a(this.B)) {
            z = false;
        }
        return z;
    }

    /* access modifiers changed from: private */
    public void b() {
        if (this.as != null) {
            this.as.m728new();
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: byte */
    public void m867byte() {
        if (!this.M) {
            if (System.currentTimeMillis() - this.N < 1000) {
                j.a(v, "request too frequency ...");
                if (this.f154new != null) {
                    this.as.a(this.f154new);
                    m872case();
                    return;
                }
            }
            j.a(v, "start network locating ...");
            j.m976if(v, "start network locating ...");
            this.M = true;
            this.I = a(this.aj);
            if (a(this.f151char) || this.I || this.f154new == null) {
                String a = a(null);
                if (a == null) {
                    this.as.a("{\"result\":{\"time\":\"" + j.a() + "\",\"error\":\"62\"}}");
                    m872case();
                    return;
                }
                if (this.h != null) {
                    a = a + this.h;
                    this.h = null;
                }
                if (g.a(a, this.P)) {
                    this.aj = this.f155try;
                    this.f151char = this.B;
                } else {
                    j.a(v, "request error ..");
                }
                if (this.W) {
                    this.W = false;
                }
                this.N = System.currentTimeMillis();
                return;
            }
            this.as.a(this.f154new);
            m872case();
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: byte */
    public void m868byte(Message message) {
        if (!(this.as == null || !this.as.m723for(message) || this.E == null)) {
            this.E.m847for();
        }
        this.f154new = null;
    }

    private void c() {
        File file = new File(aa);
        File file2 = new File(aa + "/ls.db");
        if (!file.exists()) {
            file.mkdirs();
        }
        if (!file2.exists()) {
            try {
                file2.createNewFile();
            } catch (Exception e) {
            }
        }
        try {
            this.R = SQLiteDatabase.openOrCreateDatabase(file2, null);
            this.R.execSQL("CREATE TABLE " + this.f153if + "(id CHAR(40) PRIMARY KEY,time DOUBLE,tag DOUBLE, type DOUBLE , ac INT);");
        } catch (Exception e2) {
        }
    }

    /* renamed from: case */
    private void m872case() {
        this.M = false;
        m898long();
    }

    /* access modifiers changed from: private */
    /* renamed from: do */
    public void m874do() {
        j.a(v, "on new wifi ...");
        if (this.ah) {
            m867byte();
            this.ah = false;
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: do */
    public void m875do(Message message) {
        if (this.as != null) {
            this.as.m727int(message);
        }
        if (this.E != null) {
            this.E.m844case();
        }
    }

    /* renamed from: do */
    private void m877do(String str) {
        if (this.R == null || str == null) {
            j.a(v, "db is null...");
            this.O = false;
            return;
        }
        j.a(v, "LOCATING...");
        if (System.currentTimeMillis() - this.D >= 1500 && !str.equals(this.aq)) {
            this.O = false;
            try {
                Cursor rawQuery = this.R.rawQuery("select * from " + this.f153if + " where id = \"" + str + "\";", null);
                this.aq = str;
                this.D = System.currentTimeMillis();
                if (rawQuery != null) {
                    if (rawQuery.moveToFirst()) {
                        j.a(v, "lookup DB success:" + this.aq);
                        this.o = rawQuery.getDouble(1) - 1235.4323d;
                        this.q = rawQuery.getDouble(2) - 4326.0d;
                        this.n = rawQuery.getDouble(3) - 2367.3217d;
                        this.O = true;
                        j.a(v, "lookup DB success:x" + this.o + "y" + this.n + "r" + this.q);
                    }
                    rawQuery.close();
                }
            } catch (Exception e) {
                this.D = System.currentTimeMillis();
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: else */
    public void m879else() {
        j.a(v, "on new cell ...");
    }

    /* access modifiers changed from: private */
    /* renamed from: for */
    public void m882for(Message message) {
        if (System.currentTimeMillis() - this.d < 3000) {
            j.a(v, "request too frequency ...");
            if (this.G != null) {
                this.as.a(this.G, (int) H);
                return;
            }
        }
        if (this.as != null) {
            String a = a(this.as.a(message));
            if (this.h != null) {
                a = a + this.h;
                this.h = null;
            }
            g.m919do((Context) this);
            if (g.m932if(a, this.P)) {
                this.u = this.f155try;
                this.ac = this.B;
            } else {
                j.a(v, "request poi error ..");
            }
            this.d = System.currentTimeMillis();
        }
    }

    /* renamed from: goto */
    public static void m885goto() {
        try {
            if (a != null) {
                k = new File(a);
                if (!k.exists()) {
                    File file = new File(aa);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    k.createNewFile();
                    RandomAccessFile randomAccessFile = new RandomAccessFile(k, "rw");
                    randomAccessFile.seek(0);
                    randomAccessFile.writeInt(-1);
                    randomAccessFile.writeInt(-1);
                    randomAccessFile.writeInt(0);
                    randomAccessFile.writeLong(0);
                    randomAccessFile.writeInt(0);
                    randomAccessFile.writeInt(0);
                    randomAccessFile.close();
                    return;
                }
                return;
            }
            k = null;
        } catch (Exception e) {
            e.printStackTrace();
            k = null;
        }
    }

    /* renamed from: if */
    private String m887if(boolean z) {
        if (!this.O) {
            return z ? "{\"result\":{\"time\":\"" + j.a() + "\",\"error\":\"67\"}}" : "{\"result\":{\"time\":\"" + j.a() + "\",\"error\":\"63\"}}";
        } else {
            if (z) {
                return String.format("{\"result\":{\"time\":\"" + j.a() + "\",\"error\":\"66\"},\"content\":{\"point\":{\"x\":" + "\"%f\",\"y\":\"%f\"},\"radius\":\"%f\",\"isCellChanged\":\"%b\"}}", new Object[]{Double.valueOf(this.o), Double.valueOf(this.n), Double.valueOf(this.q), Boolean.valueOf(true)});
            }
            return String.format("{\"result\":{\"time\":\"" + j.a() + "\",\"error\":\"68\"},\"content\":{\"point\":{\"x\":" + "\"%f\",\"y\":\"%f\"},\"radius\":\"%f\",\"isCellChanged\":\"%b\"}}", new Object[]{Double.valueOf(this.o), Double.valueOf(this.n), Double.valueOf(this.q), Boolean.valueOf(this.I)});
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: if */
    public void m888if() {
        if (this.Z != null) {
            j.a(v, "on new gps...");
            Location location = this.Z.m792int();
            if (!(!this.Z.m791for() || !k.a(location, true) || this.r == null || this.E == null || this.as == null)) {
                if (this.E != null) {
                    this.E.a();
                }
                k.a(this.r.a(), this.E.m848int(), location, this.as.m720byte());
            }
            if (this.as != null && this.Z.m791for()) {
                this.as.m726if(this.Z.m790do());
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: if */
    public void m889if(Message message) {
        if (message == null || message.obj == null) {
            j.a(v, "Gps updateloation is null");
            return;
        }
        Location location = (Location) message.obj;
        if (location != null) {
            j.a(v, "on update gps...");
            if (k.a(location, true) && this.r != null && this.E != null && this.as != null && j.v) {
                k.a(this.r.a(), this.E.m848int(), location, this.as.m720byte());
            }
        }
    }

    /* renamed from: if */
    private void m891if(String str) {
        Object obj = null;
        if (this.R != null && this.I) {
            try {
                double parseDouble;
                double parseDouble2;
                float parseFloat;
                j.a(v, "DB:" + str);
                JSONObject jSONObject = new JSONObject(str);
                int parseInt = Integer.parseInt(jSONObject.getJSONObject(Form.TYPE_RESULT).getString("error"));
                int obj2;
                if (parseInt == BDLocation.TypeNetWorkLocation) {
                    JSONObject jSONObject2 = jSONObject.getJSONObject("content");
                    if (jSONObject2.has("clf")) {
                        String string = jSONObject2.getString("clf");
                        if (string.equals("0")) {
                            JSONObject jSONObject3 = jSONObject2.getJSONObject("point");
                            parseDouble = Double.parseDouble(jSONObject3.getString(GroupChatInvitation.ELEMENT_NAME));
                            parseDouble2 = Double.parseDouble(jSONObject3.getString("y"));
                            parseFloat = Float.parseFloat(jSONObject2.getString("radius"));
                        } else {
                            String[] split = string.split("\\|");
                            parseDouble = Double.parseDouble(split[0]);
                            parseDouble2 = Double.parseDouble(split[1]);
                            parseFloat = Float.parseFloat(split[2]);
                        }
                        j.a(v, "DB PARSE:x" + parseDouble + "y" + parseDouble2 + "R" + parseFloat);
                    }
                    obj2 = 1;
                    parseFloat = 0.0f;
                    parseDouble = 0.0d;
                    parseDouble2 = 0.0d;
                } else {
                    if (parseInt == BDLocation.TypeServerError) {
                        this.R.delete(this.f153if, "id = \"" + this.A + "\"", null);
                        return;
                    }
                    obj2 = 1;
                    parseFloat = 0.0f;
                    parseDouble = 0.0d;
                    parseDouble2 = 0.0d;
                }
                if (obj2 == null) {
                    parseDouble += 1235.4323d;
                    parseDouble2 += 2367.3217d;
                    float f = 4326.0f + parseFloat;
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("time", Double.valueOf(parseDouble));
                    contentValues.put("tag", Float.valueOf(f));
                    contentValues.put("type", Double.valueOf(parseDouble2));
                    try {
                        if (this.R.update(this.f153if, contentValues, "id = \"" + this.A + "\"", null) <= 0) {
                            contentValues.put("id", this.A);
                            this.R.insert(this.f153if, null, contentValues);
                            j.a(v, "insert DB success!");
                        }
                    } catch (Exception e) {
                    }
                }
            } catch (Exception e2) {
                j.a(v, "DB PARSE:exp!");
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: int */
    public void m893int() {
        if (g.a((Context) this)) {
            g.f();
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: int */
    public void m894int(Message message) {
        if (this.as != null) {
            this.as.a(a(true), message);
        }
    }

    /* renamed from: long */
    private void m898long() {
        if (this.f154new != null && g.a((Context) this)) {
            g.f();
        }
    }

    /* renamed from: new */
    public static String m900new() {
        j.a(v, "read trace log1..");
        m885goto();
        try {
            if (k != null) {
                RandomAccessFile randomAccessFile = new RandomAccessFile(k, "rw");
                int readInt = randomAccessFile.readInt();
                randomAccessFile.readInt();
                int readInt2 = randomAccessFile.readInt();
                j.a(v, "read trace log.." + readInt2);
                if (readInt > 0) {
                    randomAccessFile.seek((long) ((readInt2 * ar) + 24));
                    int readInt3 = randomAccessFile.readInt();
                    byte[] bArr = new byte[ar];
                    randomAccessFile.read(bArr, 0, readInt3);
                    int readInt4 = randomAccessFile.readInt();
                    readInt--;
                    readInt2 = (readInt2 + 1) % ad;
                    randomAccessFile.seek(0);
                    randomAccessFile.writeInt(readInt);
                    randomAccessFile.seek(8);
                    randomAccessFile.writeInt(readInt2);
                    if (readInt4 == readInt3) {
                        for (int i = 0; i < bArr.length; i++) {
                            bArr[i] = (byte) (bArr[i] ^ 90);
                        }
                        String str = Jni.m673if(new String(bArr, 0, readInt3));
                        randomAccessFile.close();
                        return str;
                    }
                    randomAccessFile.close();
                    return null;
                }
                randomAccessFile.close();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: new */
    public void m901new(Message message) {
        j.a(v, "on request location ...");
        j.m976if(v, "on request location ...");
        if (this.as != null) {
            if (this.as.m721do(message) == 1 && this.Z != null && this.Z.m791for()) {
                j.a(v, "send gps location to client ...");
                this.as.a(this.Z.m790do(), message);
            } else if (this.W) {
                m867byte();
            } else if (!this.M) {
                if (this.E == null || !this.E.m849new()) {
                    m867byte();
                    return;
                }
                this.ah = true;
                this.P.postDelayed(new b(), 2000);
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: try */
    public void m903try() {
    }

    /* access modifiers changed from: private */
    /* renamed from: try */
    public void m904try(Message message) {
        if (this.as != null) {
            this.as.m725if(message);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: void */
    public void m908void() {
        j.a(v, "on switch gps ...");
        if (this.as != null) {
            if (this.as.m722for()) {
                if (this.Z == null) {
                    this.Z = new b(this, this.P);
                }
                this.Z.i();
            } else if (this.Z != null) {
                this.Z.j();
                this.Z = null;
            }
        }
    }

    /* renamed from: char */
    public boolean m909char() {
        return ((KeyguardManager) getSystemService("keyguard")).inKeyguardRestrictedInputMode();
    }

    public IBinder onBind(Intent intent) {
        return this.al.getBinder();
    }

    public void onCreate() {
        Thread.setDefaultUncaughtExceptionHandler(new a(this));
        this.r = new c(this, this.P);
        this.E = new e(this, this.P);
        this.as = new a(this.P);
        this.r.m810do();
        this.E.m850try();
        this.ab = true;
        this.M = false;
        this.ah = false;
        g.m916byte();
        try {
            c();
        } catch (Exception e) {
        }
        try {
            if (j.f209try && j.M) {
                this.y = new c(this);
            }
        } catch (Exception e2) {
        }
        j.a(v, "OnCreate");
        Log.d(v, "baidu location service start1 ..." + Process.myPid());
    }

    public void onDestroy() {
        if (this.r != null) {
            this.r.m809byte();
        }
        if (this.E != null) {
            this.E.m846else();
        }
        if (this.Z != null) {
            this.Z.j();
        }
        k.m982if();
        this.M = false;
        this.ah = false;
        this.ab = false;
        if (this.y != null) {
            this.y.m863try();
        }
        if (this.R != null) {
            this.R.close();
        }
        j.a(v, "onDestroy");
        Log.d(v, "baidu location service stop ...");
        if (j.f209try) {
            Process.killProcess(Process.myPid());
        }
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        j.a(v, "onStratCommandNotSticky");
        return 2;
    }
}
