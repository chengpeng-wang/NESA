package com.baidu.location;

import android.content.Context;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.media.session.PlaybackStateCompat;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

class g {
    /* access modifiers changed from: private|static */
    public static int a = 12000;
    /* access modifiers changed from: private|static */
    public static boolean b = false;
    /* renamed from: byte */
    private static final int f156byte = 1;
    private static int c = 2048;
    /* renamed from: case */
    private static final int f157case = 2;
    /* access modifiers changed from: private|static */
    /* renamed from: char */
    public static boolean f158char = false;
    /* access modifiers changed from: private|static */
    public static String d = null;
    /* access modifiers changed from: private|static */
    /* renamed from: do */
    public static String f159do = null;
    /* access modifiers changed from: private|static */
    public static ArrayList e = null;
    /* access modifiers changed from: private|static */
    /* renamed from: else */
    public static boolean f160else = false;
    /* access modifiers changed from: private|static */
    public static int f = 80;
    /* access modifiers changed from: private|static */
    /* renamed from: for */
    public static String f161for = null;
    /* access modifiers changed from: private|static */
    public static boolean g = false;
    /* access modifiers changed from: private|static */
    /* renamed from: goto */
    public static int f162goto = 0;
    private static final int h = 4;
    /* access modifiers changed from: private|static */
    public static boolean i = false;
    /* renamed from: if */
    private static Uri f163if = null;
    /* access modifiers changed from: private|static */
    /* renamed from: int */
    public static Handler f164int = null;
    /* access modifiers changed from: private|static */
    public static String j = "10.0.0.172";
    /* access modifiers changed from: private|static */
    public static String k = null;
    /* access modifiers changed from: private|static */
    public static Handler l = null;
    /* access modifiers changed from: private|static */
    /* renamed from: long */
    public static String f165long = null;
    public static final int m = 3;
    /* access modifiers changed from: private|static */
    /* renamed from: new */
    public static String f166new = f.v;
    /* access modifiers changed from: private|static */
    /* renamed from: try */
    public static int f167try = 4;
    /* access modifiers changed from: private|static */
    /* renamed from: void */
    public static int f168void = 3;

    g() {
    }

    private static int a(Context context, NetworkInfo networkInfo) {
        String toLowerCase;
        if (!(networkInfo == null || networkInfo.getExtraInfo() == null)) {
            toLowerCase = networkInfo.getExtraInfo().toLowerCase();
            if (toLowerCase != null) {
                String defaultHost;
                if (toLowerCase.startsWith("cmwap") || toLowerCase.startsWith("uniwap") || toLowerCase.startsWith("3gwap")) {
                    defaultHost = Proxy.getDefaultHost();
                    if (defaultHost == null || defaultHost.equals("") || defaultHost.equals("null")) {
                        defaultHost = "10.0.0.172";
                    }
                    j = defaultHost;
                    return 1;
                } else if (toLowerCase.startsWith("ctwap")) {
                    defaultHost = Proxy.getDefaultHost();
                    if (defaultHost == null || defaultHost.equals("") || defaultHost.equals("null")) {
                        defaultHost = "10.0.0.200";
                    }
                    j = defaultHost;
                    return 1;
                } else if (toLowerCase.startsWith("cmnet") || toLowerCase.startsWith("uninet") || toLowerCase.startsWith("ctnet") || toLowerCase.startsWith("3gnet")) {
                    return 2;
                }
            }
        }
        toLowerCase = Proxy.getDefaultHost();
        if (toLowerCase == null || toLowerCase.length() <= 0) {
            return 2;
        }
        if ("10.0.0.172".equals(toLowerCase.trim())) {
            j = "10.0.0.172";
            return 1;
        } else if (!"10.0.0.200".equals(toLowerCase.trim())) {
            return 2;
        } else {
            j = "10.0.0.200";
            return 1;
        }
    }

    public static void a(String str, boolean z) {
        if (!f158char && str != null) {
            f165long = Jni.m673if(str);
            g = z;
            f158char = true;
            new Thread() {
                public void run() {
                    boolean z = true;
                    try {
                        HttpPost httpPost = new HttpPost(j.m970do());
                        ArrayList arrayList = new ArrayList();
                        if (g.g) {
                            arrayList.add(new BasicNameValuePair("qt", "grid"));
                        } else {
                            arrayList.add(new BasicNameValuePair("qt", "conf"));
                        }
                        arrayList.add(new BasicNameValuePair("req", g.f165long));
                        httpPost.setEntity(new UrlEncodedFormEntity(arrayList, "utf-8"));
                        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
                        defaultHttpClient.getParams().setParameter("http.connection.timeout", Integer.valueOf(g.a));
                        defaultHttpClient.getParams().setParameter("http.socket.timeout", Integer.valueOf(g.a));
                        j.a(g.f166new, "req config...");
                        HttpResponse execute = defaultHttpClient.execute(httpPost);
                        if (execute.getStatusLine().getStatusCode() == 200) {
                            if (g.g) {
                                j.a(g.f166new, "req config response...");
                                byte[] toByteArray = EntityUtils.toByteArray(execute.getEntity());
                                if (toByteArray == null) {
                                    z = false;
                                } else if (toByteArray.length < 640) {
                                    j.a(g.f166new, "req config response.<640.");
                                    j.e = false;
                                    j.f198byte = j.c + 0.025d;
                                    j.s = j.O - 0.025d;
                                } else {
                                    j.e = true;
                                    Long valueOf = Long.valueOf(((((((((((long) toByteArray[7]) & 255) << 56) | ((((long) toByteArray[6]) & 255) << 48)) | ((((long) toByteArray[5]) & 255) << 40)) | ((((long) toByteArray[4]) & 255) << 32)) | ((((long) toByteArray[3]) & 255) << 24)) | ((((long) toByteArray[2]) & 255) << 16)) | ((((long) toByteArray[1]) & 255) << 8)) | (((long) toByteArray[0]) & 255));
                                    j.a(g.f166new, "req config 1...");
                                    j.s = Double.longBitsToDouble(valueOf.longValue());
                                    j.a(g.f166new, "req config response:" + Double.longBitsToDouble(valueOf.longValue()));
                                    valueOf = Long.valueOf(((((((((((long) toByteArray[15]) & 255) << 56) | ((((long) toByteArray[14]) & 255) << 48)) | ((((long) toByteArray[13]) & 255) << 40)) | ((((long) toByteArray[12]) & 255) << 32)) | ((((long) toByteArray[11]) & 255) << 24)) | ((((long) toByteArray[10]) & 255) << 16)) | ((((long) toByteArray[9]) & 255) << 8)) | (((long) toByteArray[8]) & 255));
                                    j.f198byte = Double.longBitsToDouble(valueOf.longValue());
                                    j.o = new byte[625];
                                    j.a(g.f166new, "req config response:" + Double.longBitsToDouble(valueOf.longValue()));
                                    for (int i = 0; i < 625; i++) {
                                        j.o[i] = toByteArray[i + 16];
                                        j.a(g.f166new, "req config value:" + j.o[i]);
                                    }
                                }
                                if (z) {
                                    g.m924for();
                                }
                            } else {
                                String entityUtils = EntityUtils.toString(execute.getEntity(), "utf-8");
                                j.a(g.f166new, "req config value:" + entityUtils);
                                if (g.m931if(entityUtils)) {
                                    j.a(g.f166new, "Save to config");
                                    g.c();
                                }
                            }
                        }
                        g.f165long = null;
                        g.f158char = false;
                        g.g = false;
                    } catch (Exception e) {
                        j.a(g.f166new, "Exception!!!");
                        g.f165long = null;
                        g.f158char = false;
                        g.g = false;
                    } catch (Throwable th) {
                        g.f165long = null;
                        g.f158char = false;
                        g.g = false;
                        throw th;
                    }
                }
            }.start();
        }
    }

    public static boolean a(Context context) {
        if (context == null) {
            return false;
        }
        m919do(context);
        return f167try == 3;
    }

    public static boolean a(String str, Handler handler) {
        if (i || str == null) {
            return false;
        }
        i = true;
        j.a(f166new, "bloc : " + k);
        k = Jni.m673if(str);
        j.a(f166new, "NUMBER_e : " + k.length());
        f164int = handler;
        if (f159do == null) {
            f159do = k.a();
        }
        new Thread() {
            public void run() {
                int i = g.f168void;
                while (i > 0) {
                    try {
                        HttpPost httpPost = new HttpPost(j.m970do());
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(new BasicNameValuePair("bloc", g.k));
                        if (g.f159do != null) {
                            arrayList.add(new BasicNameValuePair("up", g.f159do));
                        }
                        httpPost.setEntity(new UrlEncodedFormEntity(arrayList, "utf-8"));
                        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
                        defaultHttpClient.getParams().setParameter("http.connection.timeout", Integer.valueOf(g.a));
                        defaultHttpClient.getParams().setParameter("http.socket.timeout", Integer.valueOf(g.a));
                        HttpProtocolParams.setUseExpectContinue(defaultHttpClient.getParams(), false);
                        j.a(g.f166new, "apn type : " + g.f167try);
                        if ((g.f167try == 1 || g.f167try == 4) && (g.f168void - i) % 2 == 0) {
                            j.a(g.f166new, "apn type : ADD PROXY" + g.j + g.f);
                            defaultHttpClient.getParams().setParameter("http.route.default-proxy", new HttpHost(g.j, g.f, "http"));
                        }
                        HttpResponse execute = defaultHttpClient.execute(httpPost);
                        int statusCode = execute.getStatusLine().getStatusCode();
                        j.a(g.f166new, "===status error : " + statusCode);
                        if (statusCode == 200) {
                            String entityUtils = EntityUtils.toString(execute.getEntity(), "utf-8");
                            j.a(g.f166new, "status error : " + execute.getEntity().getContentType());
                            Message obtainMessage = g.f164int.obtainMessage(21);
                            obtainMessage.obj = entityUtils;
                            obtainMessage.sendToTarget();
                            g.f159do = null;
                            break;
                        }
                        httpPost.abort();
                        Message obtainMessage2 = g.f164int.obtainMessage(63);
                        obtainMessage2.obj = "HttpStatus error";
                        obtainMessage2.sendToTarget();
                        i--;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (i <= 0 && g.f164int != null) {
                    j.a(g.f166new, "have tried 3 times...");
                    g.f164int.obtainMessage(62).sendToTarget();
                }
                g.f164int = null;
                g.i = false;
            }
        }.start();
        return true;
    }

    /* renamed from: byte */
    public static void m916byte() {
        try {
            File file = new File(f.aa + "/config.dat");
            if (file.exists()) {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                if (randomAccessFile.readBoolean()) {
                    randomAccessFile.seek(2);
                    int readInt = randomAccessFile.readInt();
                    byte[] bArr = new byte[readInt];
                    randomAccessFile.read(bArr, 0, readInt);
                    m931if(new String(bArr));
                }
                randomAccessFile.seek(1);
                if (randomAccessFile.readBoolean()) {
                    randomAccessFile.seek(PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID);
                    j.s = randomAccessFile.readDouble();
                    j.f198byte = randomAccessFile.readDouble();
                    j.e = randomAccessFile.readBoolean();
                    if (j.e) {
                        j.o = new byte[625];
                        randomAccessFile.read(j.o, 0, 625);
                    }
                }
                randomAccessFile.close();
            }
            String str = "&ver=" + j.f204goto + "&usr=" + j.f;
            j.a(f166new, str);
            a(str, false);
        } catch (Exception e) {
        }
    }

    public static void c() {
        String str = f.aa + "/config.dat";
        int i = j.m ? 1 : 0;
        int i2 = j.G ? 1 : 0;
        String format = String.format("{\"ver\":\"%d\",\"gps\":\"%.1f|%.1f|%.1f|%.1f|%d|%d|%d|%d|%d|%d|%d\",\"up\":\"%.1f|%.1f|%.1f|%.1f\",\"wf\":\"%d|%.1f|%d|%.1f\",\"ab\":\"%.2f|%.2f|%d|%d\",\"gpc\":\"%d|%d|%d|%d|%d|%d\"}", new Object[]{Integer.valueOf(j.f204goto), Float.valueOf(j.f199case), Float.valueOf(j.A), Float.valueOf(j.f200char), Float.valueOf(j.C), Integer.valueOf(j.t), Integer.valueOf(j.a), Integer.valueOf(j.u), Integer.valueOf(j.f205if), Integer.valueOf(j.f206int), Integer.valueOf(j.k), Integer.valueOf(j.K), Float.valueOf(j.T), Float.valueOf(j.Q), Float.valueOf(j.d), Float.valueOf(j.H), Integer.valueOf(j.F), Float.valueOf(j.f208new), Integer.valueOf(j.h), Float.valueOf(j.D), Float.valueOf(j.S), Float.valueOf(j.P), Integer.valueOf(j.N), Integer.valueOf(j.L), Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(j.i), Integer.valueOf(j.l), Long.valueOf(j.B), Integer.valueOf(j.E)});
        j.a(f166new, "save2Config : " + format);
        byte[] bytes = format.getBytes();
        try {
            RandomAccessFile randomAccessFile;
            File file = new File(str);
            if (!file.exists()) {
                File file2 = new File(f.aa);
                if (!file2.exists()) {
                    file2.mkdirs();
                }
                if (file.createNewFile()) {
                    j.a(f166new, "upload manager create file success");
                    randomAccessFile = new RandomAccessFile(file, "rw");
                    randomAccessFile.seek(0);
                    randomAccessFile.writeBoolean(false);
                    randomAccessFile.writeBoolean(false);
                    randomAccessFile.close();
                } else {
                    return;
                }
            }
            randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(0);
            randomAccessFile.writeBoolean(true);
            randomAccessFile.seek(2);
            randomAccessFile.writeInt(bytes.length);
            randomAccessFile.write(bytes);
            randomAccessFile.close();
        } catch (Exception e) {
        }
    }

    /* renamed from: do */
    public static int m919do(Context context) {
        f167try = m928if(context);
        return f167try;
    }

    public static void f() {
        if (!b) {
            b = true;
            if (e == null) {
                f162goto = 0;
                e = new ArrayList();
                int i = 0;
                do {
                    Object a = f162goto < 2 ? k.a() : null;
                    if (a != null || f162goto == 1) {
                        f162goto = 1;
                    } else {
                        f162goto = 2;
                        try {
                            if (j.E == 0) {
                                a = f.m900new();
                                if (a == null) {
                                    a = b.e();
                                }
                            } else if (j.E == 1) {
                                a = b.e();
                                if (a == null) {
                                    a = f.m900new();
                                }
                            }
                        } catch (Exception e) {
                            a = null;
                        }
                    }
                    if (a == null) {
                        break;
                    }
                    e.add(a);
                    i += a.length();
                    j.a(f166new, "upload data size:" + i);
                } while (i < c);
            }
            if (e == null || e.size() < 1) {
                e = null;
                b = false;
                j.a(f166new, "No upload data...");
                return;
            }
            j.a(f166new, "Beging upload data...");
            new Thread() {
                public void run() {
                    try {
                        HttpPost httpPost = new HttpPost(j.m970do());
                        ArrayList arrayList = new ArrayList();
                        for (int i = 0; i < g.e.size(); i++) {
                            if (g.f162goto == 1) {
                                arrayList.add(new BasicNameValuePair("cldc[" + i + "]", (String) g.e.get(i)));
                            } else {
                                arrayList.add(new BasicNameValuePair("cltr[" + i + "]", (String) g.e.get(i)));
                            }
                        }
                        httpPost.setEntity(new UrlEncodedFormEntity(arrayList, "utf-8"));
                        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
                        defaultHttpClient.getParams().setParameter("http.connection.timeout", Integer.valueOf(g.a));
                        defaultHttpClient.getParams().setParameter("http.socket.timeout", Integer.valueOf(g.a));
                        if (defaultHttpClient.execute(httpPost).getStatusLine().getStatusCode() == 200) {
                            j.a(g.f166new, "Status ok1...");
                            g.e.clear();
                            g.e = null;
                        } else {
                            j.a(g.f166new, "Status err1...");
                        }
                        g.b = false;
                    } catch (Exception e) {
                        g.b = false;
                    } catch (Throwable th) {
                        g.b = false;
                        throw th;
                    }
                }
            }.start();
        }
    }

    /* renamed from: for */
    public static void m924for() {
        try {
            RandomAccessFile randomAccessFile;
            File file = new File(f.aa + "/config.dat");
            if (!file.exists()) {
                File file2 = new File(f.aa);
                if (!file2.exists()) {
                    file2.mkdirs();
                }
                if (file.createNewFile()) {
                    j.a(f166new, "upload manager create file success");
                    randomAccessFile = new RandomAccessFile(file, "rw");
                    randomAccessFile.seek(0);
                    randomAccessFile.writeBoolean(false);
                    randomAccessFile.writeBoolean(false);
                    randomAccessFile.close();
                } else {
                    return;
                }
            }
            randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(1);
            randomAccessFile.writeBoolean(true);
            randomAccessFile.seek(PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID);
            randomAccessFile.writeDouble(j.s);
            randomAccessFile.writeDouble(j.f198byte);
            randomAccessFile.writeBoolean(j.e);
            if (j.e && j.o != null) {
                randomAccessFile.write(j.o);
            }
            randomAccessFile.close();
        } catch (Exception e) {
        }
    }

    /* renamed from: for */
    public static boolean m925for(Context context) {
        boolean z = true;
        if (context == null) {
            return false;
        }
        m919do(context);
        if (f167try != 1) {
            z = false;
        }
        return z;
    }

    /* JADX WARNING: Removed duplicated region for block: B:56:0x00d1 A:{ExcHandler: Exception (r0_0 'e' java.lang.Exception), Splitter:B:1:0x0003} */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:56:0x00d1, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:57:0x00d2, code skipped:
            r0.printStackTrace();
     */
    /* JADX WARNING: Missing block: B:59:0x00d9, code skipped:
            r0 = r8;
     */
    /* JADX WARNING: Missing block: B:67:?, code skipped:
            return 4;
     */
    /* renamed from: if */
    private static int m928if(android.content.Context r9) {
        /*
        r7 = 1;
        r6 = 4;
        r1 = 0;
        r0 = "connectivity";
        r0 = r9.getSystemService(r0);	 Catch:{ SecurityException -> 0x00be, Exception -> 0x00d1 }
        r0 = (android.net.ConnectivityManager) r0;	 Catch:{ SecurityException -> 0x00be, Exception -> 0x00d1 }
        if (r0 != 0) goto L_0x000f;
    L_0x000d:
        r0 = r6;
    L_0x000e:
        return r0;
    L_0x000f:
        r8 = r0.getActiveNetworkInfo();	 Catch:{ SecurityException -> 0x00be, Exception -> 0x00d1 }
        if (r8 == 0) goto L_0x001b;
    L_0x0015:
        r0 = r8.isAvailable();	 Catch:{ SecurityException -> 0x00d8, Exception -> 0x00d1 }
        if (r0 != 0) goto L_0x001d;
    L_0x001b:
        r0 = r6;
        goto L_0x000e;
    L_0x001d:
        r0 = r8.getType();	 Catch:{ SecurityException -> 0x00d8, Exception -> 0x00d1 }
        if (r0 != r7) goto L_0x0025;
    L_0x0023:
        r0 = 3;
        goto L_0x000e;
    L_0x0025:
        r0 = "content://telephony/carriers/preferapn";
        r0 = android.net.Uri.parse(r0);	 Catch:{ SecurityException -> 0x00d8, Exception -> 0x00d1 }
        f163if = r0;	 Catch:{ SecurityException -> 0x00d8, Exception -> 0x00d1 }
        r0 = r9.getContentResolver();	 Catch:{ SecurityException -> 0x00d8, Exception -> 0x00d1 }
        r1 = f163if;	 Catch:{ SecurityException -> 0x00d8, Exception -> 0x00d1 }
        r2 = 0;
        r3 = 0;
        r4 = 0;
        r5 = 0;
        r1 = r0.query(r1, r2, r3, r4, r5);	 Catch:{ SecurityException -> 0x00d8, Exception -> 0x00d1 }
        if (r1 == 0) goto L_0x00b6;
    L_0x003d:
        r0 = r1.moveToFirst();	 Catch:{ SecurityException -> 0x00d8, Exception -> 0x00d1 }
        if (r0 == 0) goto L_0x00b6;
    L_0x0043:
        r0 = "apn";
        r0 = r1.getColumnIndex(r0);	 Catch:{ SecurityException -> 0x00d8, Exception -> 0x00d1 }
        r0 = r1.getString(r0);	 Catch:{ SecurityException -> 0x00d8, Exception -> 0x00d1 }
        if (r0 == 0) goto L_0x0081;
    L_0x004f:
        r2 = r0.toLowerCase();	 Catch:{ SecurityException -> 0x00d8, Exception -> 0x00d1 }
        r3 = "ctwap";
        r2 = r2.contains(r3);	 Catch:{ SecurityException -> 0x00d8, Exception -> 0x00d1 }
        if (r2 == 0) goto L_0x0081;
    L_0x005b:
        r0 = android.net.Proxy.getDefaultHost();	 Catch:{ SecurityException -> 0x00d8, Exception -> 0x00d1 }
        if (r0 == 0) goto L_0x007e;
    L_0x0061:
        r2 = "";
        r2 = r0.equals(r2);	 Catch:{ SecurityException -> 0x00d8, Exception -> 0x00d1 }
        if (r2 != 0) goto L_0x007e;
    L_0x0069:
        r2 = "null";
        r2 = r0.equals(r2);	 Catch:{ SecurityException -> 0x00d8, Exception -> 0x00d1 }
        if (r2 != 0) goto L_0x007e;
    L_0x0071:
        j = r0;	 Catch:{ SecurityException -> 0x00d8, Exception -> 0x00d1 }
        r0 = 80;
        f = r0;	 Catch:{ SecurityException -> 0x00d8, Exception -> 0x00d1 }
        if (r1 == 0) goto L_0x007c;
    L_0x0079:
        r1.close();	 Catch:{ SecurityException -> 0x00d8, Exception -> 0x00d1 }
    L_0x007c:
        r0 = r7;
        goto L_0x000e;
    L_0x007e:
        r0 = "10.0.0.200";
        goto L_0x0071;
    L_0x0081:
        if (r0 == 0) goto L_0x00b6;
    L_0x0083:
        r0 = r0.toLowerCase();	 Catch:{ SecurityException -> 0x00d8, Exception -> 0x00d1 }
        r2 = "wap";
        r0 = r0.contains(r2);	 Catch:{ SecurityException -> 0x00d8, Exception -> 0x00d1 }
        if (r0 == 0) goto L_0x00b6;
    L_0x008f:
        r0 = android.net.Proxy.getDefaultHost();	 Catch:{ SecurityException -> 0x00d8, Exception -> 0x00d1 }
        if (r0 == 0) goto L_0x00b3;
    L_0x0095:
        r2 = "";
        r2 = r0.equals(r2);	 Catch:{ SecurityException -> 0x00d8, Exception -> 0x00d1 }
        if (r2 != 0) goto L_0x00b3;
    L_0x009d:
        r2 = "null";
        r2 = r0.equals(r2);	 Catch:{ SecurityException -> 0x00d8, Exception -> 0x00d1 }
        if (r2 != 0) goto L_0x00b3;
    L_0x00a5:
        j = r0;	 Catch:{ SecurityException -> 0x00d8, Exception -> 0x00d1 }
        r0 = 80;
        f = r0;	 Catch:{ SecurityException -> 0x00d8, Exception -> 0x00d1 }
        if (r1 == 0) goto L_0x00b0;
    L_0x00ad:
        r1.close();	 Catch:{ SecurityException -> 0x00d8, Exception -> 0x00d1 }
    L_0x00b0:
        r0 = r7;
        goto L_0x000e;
    L_0x00b3:
        r0 = "10.0.0.172";
        goto L_0x00a5;
    L_0x00b6:
        if (r1 == 0) goto L_0x00bb;
    L_0x00b8:
        r1.close();	 Catch:{ SecurityException -> 0x00d8, Exception -> 0x00d1 }
    L_0x00bb:
        r0 = 2;
        goto L_0x000e;
    L_0x00be:
        r0 = move-exception;
        r0 = r1;
    L_0x00c0:
        r1 = f166new;	 Catch:{ Exception -> 0x00cd }
        r2 = "APN security...";
        com.baidu.location.j.a(r1, r2);	 Catch:{ Exception -> 0x00cd }
        r0 = a(r9, r0);	 Catch:{ Exception -> 0x00cd }
        goto L_0x000e;
    L_0x00cd:
        r0 = move-exception;
        r0 = r6;
        goto L_0x000e;
    L_0x00d1:
        r0 = move-exception;
        r0.printStackTrace();
        r0 = r6;
        goto L_0x000e;
    L_0x00d8:
        r0 = move-exception;
        r0 = r8;
        goto L_0x00c0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.g.m928if(android.content.Context):int");
    }

    /* renamed from: if */
    public static boolean m931if(String str) {
        if (str != null) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                int parseInt = Integer.parseInt(jSONObject.getString("ver"));
                if (parseInt > j.f204goto) {
                    String[] split;
                    j.f204goto = parseInt;
                    if (jSONObject.has("gps")) {
                        j.a(f166new, "has gps...");
                        split = jSONObject.getString("gps").split("\\|");
                        if (split.length > 10) {
                            if (!(split[0] == null || split[0].equals(""))) {
                                j.f199case = Float.parseFloat(split[0]);
                            }
                            if (!(split[1] == null || split[1].equals(""))) {
                                j.A = Float.parseFloat(split[1]);
                            }
                            if (!(split[2] == null || split[2].equals(""))) {
                                j.f200char = Float.parseFloat(split[2]);
                            }
                            if (!(split[3] == null || split[3].equals(""))) {
                                j.C = Float.parseFloat(split[3]);
                            }
                            if (!(split[4] == null || split[4].equals(""))) {
                                j.t = Integer.parseInt(split[4]);
                            }
                            if (!(split[5] == null || split[5].equals(""))) {
                                j.a = Integer.parseInt(split[5]);
                            }
                            if (!(split[6] == null || split[6].equals(""))) {
                                j.u = Integer.parseInt(split[6]);
                            }
                            if (!(split[7] == null || split[7].equals(""))) {
                                j.f205if = Integer.parseInt(split[7]);
                            }
                            if (!(split[8] == null || split[8].equals(""))) {
                                j.f206int = Integer.parseInt(split[8]);
                            }
                            if (!(split[9] == null || split[9].equals(""))) {
                                j.k = Integer.parseInt(split[9]);
                            }
                            if (!(split[10] == null || split[10].equals(""))) {
                                j.K = Integer.parseInt(split[10]);
                            }
                        }
                    }
                    if (jSONObject.has("up")) {
                        j.a(f166new, "has up...");
                        split = jSONObject.getString("up").split("\\|");
                        if (split.length > 3) {
                            if (!(split[0] == null || split[0].equals(""))) {
                                j.T = Float.parseFloat(split[0]);
                            }
                            if (!(split[1] == null || split[1].equals(""))) {
                                j.Q = Float.parseFloat(split[1]);
                            }
                            if (!(split[2] == null || split[2].equals(""))) {
                                j.d = Float.parseFloat(split[2]);
                            }
                            if (!(split[3] == null || split[3].equals(""))) {
                                j.H = Float.parseFloat(split[3]);
                            }
                        }
                    }
                    if (jSONObject.has("wf")) {
                        j.a(f166new, "has wf...");
                        split = jSONObject.getString("wf").split("\\|");
                        if (split.length > 3) {
                            if (!(split[0] == null || split[0].equals(""))) {
                                j.F = Integer.parseInt(split[0]);
                            }
                            if (!(split[1] == null || split[1].equals(""))) {
                                j.f208new = Float.parseFloat(split[1]);
                            }
                            if (!(split[2] == null || split[2].equals(""))) {
                                j.h = Integer.parseInt(split[2]);
                            }
                            if (!(split[3] == null || split[3].equals(""))) {
                                j.D = Float.parseFloat(split[3]);
                            }
                        }
                    }
                    if (jSONObject.has("ab")) {
                        j.a(f166new, "has ab...");
                        split = jSONObject.getString("ab").split("\\|");
                        if (split.length > 3) {
                            if (!(split[0] == null || split[0].equals(""))) {
                                j.S = Float.parseFloat(split[0]);
                            }
                            if (!(split[1] == null || split[1].equals(""))) {
                                j.P = Float.parseFloat(split[1]);
                            }
                            if (!(split[2] == null || split[2].equals(""))) {
                                j.N = Integer.parseInt(split[2]);
                            }
                            if (!(split[3] == null || split[3].equals(""))) {
                                j.L = Integer.parseInt(split[3]);
                            }
                        }
                    }
                    if (jSONObject.has("gpc")) {
                        j.a(f166new, "has gpc...");
                        String[] split2 = jSONObject.getString("gpc").split("\\|");
                        if (split2.length > 5) {
                            if (!(split2[0] == null || split2[0].equals(""))) {
                                if (Integer.parseInt(split2[0]) > 0) {
                                    j.m = true;
                                } else {
                                    j.m = false;
                                }
                            }
                            if (!(split2[1] == null || split2[1].equals(""))) {
                                if (Integer.parseInt(split2[1]) > 0) {
                                    j.G = true;
                                } else {
                                    j.G = false;
                                }
                            }
                            if (!(split2[2] == null || split2[2].equals(""))) {
                                j.i = Integer.parseInt(split2[2]);
                            }
                            if (!(split2[3] == null || split2[3].equals(""))) {
                                j.l = Integer.parseInt(split2[3]);
                            }
                            if (!(split2[4] == null || split2[4].equals(""))) {
                                parseInt = Integer.parseInt(split2[4]);
                                if (parseInt > 0) {
                                    j.B = (long) parseInt;
                                    j.z = (j.B * 1000) * 60;
                                    j.q = j.z >> 2;
                                } else {
                                    j.M = false;
                                }
                            }
                            if (!(split2[5] == null || split2[5].equals(""))) {
                                j.E = Integer.parseInt(split2[5]);
                            }
                        }
                    }
                    try {
                        j.a(f166new, "config change true...");
                        return true;
                    } catch (Exception e) {
                        return true;
                    }
                }
            } catch (Exception e2) {
                return false;
            }
        }
        return false;
    }

    /* renamed from: if */
    public static boolean m932if(String str, Handler handler) {
        if (f160else || str == null) {
            return false;
        }
        f160else = true;
        d = Jni.m673if(str);
        j.a(f166new, "bloc : " + d);
        l = handler;
        if (f161for == null) {
            f161for = k.a();
        }
        new Thread() {
            public void run() {
                int i = g.f168void;
                while (i > 0) {
                    try {
                        HttpPost httpPost = new HttpPost(j.m970do());
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(new BasicNameValuePair("bloc", g.d));
                        if (g.f161for != null) {
                            arrayList.add(new BasicNameValuePair("up", g.f161for));
                        }
                        httpPost.setEntity(new UrlEncodedFormEntity(arrayList, "utf-8"));
                        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
                        defaultHttpClient.getParams().setParameter("http.connection.timeout", Integer.valueOf(g.a));
                        defaultHttpClient.getParams().setParameter("http.socket.timeout", Integer.valueOf(g.a));
                        HttpProtocolParams.setUseExpectContinue(defaultHttpClient.getParams(), false);
                        if (g.f167try == 1) {
                            defaultHttpClient.getParams().setParameter("http.route.default-proxy", new HttpHost(g.j, g.f, "http"));
                        }
                        HttpResponse execute = defaultHttpClient.execute(httpPost);
                        int statusCode = execute.getStatusLine().getStatusCode();
                        j.a(g.f166new, "===status error : " + statusCode);
                        if (statusCode == 200) {
                            String entityUtils = EntityUtils.toString(execute.getEntity(), "utf-8");
                            Message obtainMessage = g.l.obtainMessage(26);
                            obtainMessage.obj = entityUtils;
                            obtainMessage.sendToTarget();
                            g.f159do = null;
                            break;
                        }
                        httpPost.abort();
                        Message obtainMessage2 = g.l.obtainMessage(65);
                        obtainMessage2.obj = "HttpStatus error";
                        obtainMessage2.sendToTarget();
                        i--;
                    } catch (Exception e) {
                    }
                }
                if (i <= 0 && g.l != null) {
                    j.a(g.f166new, "have tried 3 times...");
                    g.l.obtainMessage(64).sendToTarget();
                }
                g.l = null;
                g.f160else = false;
            }
        }.start();
        return true;
    }
}
