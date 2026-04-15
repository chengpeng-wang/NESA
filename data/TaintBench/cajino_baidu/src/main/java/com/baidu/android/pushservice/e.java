package com.baidu.android.pushservice;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings.System;
import com.baidu.android.common.logging.Log;
import com.baidu.android.pushservice.b.i;
import com.baidu.android.pushservice.b.m;
import com.baidu.android.pushservice.b.s;
import com.baidu.android.pushservice.jni.PushSocket;
import com.baidu.android.pushservice.message.a;
import com.baidu.android.pushservice.message.b;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import org.apache.http.HttpStatus;

public final class e {
    static int a = -1;
    /* access modifiers changed from: private|static */
    public static Boolean e = Boolean.valueOf(false);
    private static volatile e p;
    private final int A = 7200;
    Handler b = new Handler();
    a c;
    /* access modifiers changed from: private */
    public boolean d = false;
    /* access modifiers changed from: private */
    public boolean f = false;
    /* access modifiers changed from: private */
    public Socket g;
    /* access modifiers changed from: private */
    public InputStream h;
    /* access modifiers changed from: private */
    public OutputStream i;
    /* access modifiers changed from: private */
    public LinkedList j = new LinkedList();
    /* access modifiers changed from: private */
    public j k;
    /* access modifiers changed from: private */
    public i l;
    private boolean m = false;
    /* access modifiers changed from: private */
    public int n = 0;
    /* access modifiers changed from: private */
    public Context o;
    private HashSet q = new HashSet();
    /* access modifiers changed from: private */
    public boolean r;
    private Runnable s = new g(this);
    /* access modifiers changed from: private */
    public Runnable t = new h(this);
    private long u = 0;
    private int[] v = new int[]{HttpStatus.SC_MULTIPLE_CHOICES, HttpStatus.SC_METHOD_FAILURE, 540, 600};
    private int[] w = new int[]{0, 0, 0, 0};
    private final int x = 2;
    private int y = 2;
    private int z = 0;

    private e(Context context) {
        this.o = context;
        int i = i();
        if (i != 0) {
            this.y = i;
        }
        j();
        this.q = e();
        PushSDK.getInstantce(this.o).setAlarmTimeout(this.v[this.y]);
    }

    public static e a(Context context) {
        if (p == null) {
            p = new e(context);
        }
        return p;
    }

    private void a(int i) {
        PushSettings.a(i);
    }

    private void a(HashSet hashSet) {
        String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(absolutePath, "baidu/pushservice/files");
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(absolutePath, "baidu/pushservice/files/mi")));
            Iterator it = hashSet.iterator();
            while (it.hasNext()) {
                bufferedWriter.write((String) it.next());
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void b(boolean z) {
        int i = this.y;
        if (z) {
            this.z++;
            this.w[this.y] = 0;
            if (this.v[this.y] * this.z >= 7200) {
                this.z = 0;
                if (this.y < this.v.length - 1 && this.w[this.y + 1] < 2) {
                    this.y++;
                    a(this.y);
                    PushSDK.getInstantce(this.o).setAlarmTimeout(this.v[this.y]);
                }
            }
            if (this.v[this.y] * this.z >= 14400) {
                i iVar = new i();
                iVar.c("030101");
                iVar.a(System.currentTimeMillis());
                iVar.d(m.d(this.o));
                iVar.b(this.v[this.y]);
                s.a(this.o, iVar);
            }
        } else {
            this.z = 0;
            int[] iArr = this.w;
            int i2 = this.y;
            iArr[i2] = iArr[i2] + 1;
            if (this.y > 0) {
                this.y--;
                a(this.y);
                PushSDK.getInstantce(this.o).setAlarmTimeout(this.v[this.y]);
            }
        }
        if (PushSettings.c()) {
            Log.d("PushConnection", "RTC stat update from " + this.v[i] + " to " + this.v[this.y]);
            com.baidu.android.pushservice.util.m.a("RTC stat update from " + this.v[i] + " to " + this.v[this.y]);
        }
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void f() {
        if (this.d || e.booleanValue()) {
            if (b.a()) {
                Log.i("PushConnection", "Connect return. mConnected:" + this.d + " mConnectting:" + e);
            }
        } else if (y.a().e()) {
            e = Boolean.valueOf(true);
            a = -1;
            Thread thread = new Thread(new f(this));
            thread.setName("PushService-PushService-connect");
            thread.start();
        } else {
            if (b.a()) {
                Log.d("PushConnection", "re-token");
            }
            PushSDK.getInstantce(this.o).sendRequestTokenIntent();
        }
    }

    /* access modifiers changed from: private */
    public void g() {
        if (b.a()) {
            Log.i("PushConnection", "disconnectedByPeer, mStoped == " + this.m);
            com.baidu.android.pushservice.util.m.a("disconnectedByPeer, mStoped == " + this.m);
        }
        if (!this.m) {
            h();
            this.n++;
            if (this.n < 3) {
                int i = ((this.n - 1) * 30) * 1000;
                if (this.n == 1) {
                    i = 0;
                }
                this.b.postDelayed(this.s, (long) i);
                if (b.a()) {
                    Log.i("PushConnection", "Schedule retry-- retry times: " + this.n + " time delay: " + i);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void h() {
        if (b.a()) {
            Log.i("PushConnection", "disconnected");
        }
        this.b.removeCallbacks(this.t);
        this.f = true;
        this.d = false;
        a(false);
        synchronized (this.j) {
            this.j.notifyAll();
        }
        try {
            if (this.g != null) {
                this.g.close();
                this.g = null;
            }
            if (this.h != null) {
                this.h.close();
                this.h = null;
            }
            if (this.i != null) {
                this.i.close();
                this.i = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (PushSocket.a) {
            PushSocket.closeSocket(a);
        }
        if (this.c != null) {
            this.c.c();
        }
    }

    private int i() {
        return PushSettings.d();
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x0071 A:{Catch:{ all -> 0x008f }} */
    /* JADX WARNING: Removed duplicated region for block: B:50:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x007a A:{SYNTHETIC, Splitter:B:31:0x007a} */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0086 A:{SYNTHETIC, Splitter:B:37:0x0086} */
    private void j() {
        /*
        r7 = this;
        r2 = 0;
        r3 = new java.io.File;
        r0 = android.os.Environment.getExternalStorageDirectory();
        r1 = "baidu/pushservice/pushservice.cfg";
        r3.<init>(r0, r1);
        r0 = r3.exists();
        if (r0 == 0) goto L_0x0063;
    L_0x0012:
        r4 = new java.util.Properties;
        r4.<init>();
        r1 = 0;
        r0 = new java.io.FileInputStream;	 Catch:{ Exception -> 0x0069, all -> 0x0083 }
        r0.<init>(r3);	 Catch:{ Exception -> 0x0069, all -> 0x0083 }
        r4.load(r0);	 Catch:{ Exception -> 0x0094 }
        r1 = "rtcseed";
        r1 = r4.getProperty(r1);	 Catch:{ Exception -> 0x0094 }
        if (r1 == 0) goto L_0x004a;
    L_0x0028:
        r3 = r1.length();	 Catch:{ Exception -> 0x0094 }
        if (r3 <= 0) goto L_0x004a;
    L_0x002e:
        r3 = new org.json.JSONArray;	 Catch:{ Exception -> 0x0094 }
        r3.<init>(r1);	 Catch:{ Exception -> 0x0094 }
        r1 = r2;
    L_0x0034:
        r2 = r3.length();	 Catch:{ Exception -> 0x0094 }
        if (r1 >= r2) goto L_0x004a;
    L_0x003a:
        r2 = r7.v;	 Catch:{ Exception -> 0x0094 }
        r5 = r3.getInt(r1);	 Catch:{ Exception -> 0x0094 }
        r2[r1] = r5;	 Catch:{ Exception -> 0x0094 }
        r2 = r7.w;	 Catch:{ Exception -> 0x0094 }
        r5 = 0;
        r2[r1] = r5;	 Catch:{ Exception -> 0x0094 }
        r1 = r1 + 1;
        goto L_0x0034;
    L_0x004a:
        r1 = "originseed";
        r1 = r4.getProperty(r1);	 Catch:{ Exception -> 0x0094 }
        if (r1 == 0) goto L_0x005e;
    L_0x0052:
        r2 = r1.length();	 Catch:{ Exception -> 0x0094 }
        if (r2 <= 0) goto L_0x005e;
    L_0x0058:
        r1 = java.lang.Integer.parseInt(r1);	 Catch:{ Exception -> 0x0094 }
        r7.y = r1;	 Catch:{ Exception -> 0x0094 }
    L_0x005e:
        if (r0 == 0) goto L_0x0063;
    L_0x0060:
        r0.close();	 Catch:{ IOException -> 0x0064 }
    L_0x0063:
        return;
    L_0x0064:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0063;
    L_0x0069:
        r0 = move-exception;
        r0 = r1;
    L_0x006b:
        r1 = com.baidu.android.pushservice.b.a();	 Catch:{ all -> 0x008f }
        if (r1 == 0) goto L_0x0078;
    L_0x0071:
        r1 = "PushConnection";
        r2 = "getTestConfig exception ";
        com.baidu.android.common.logging.Log.e(r1, r2);	 Catch:{ all -> 0x008f }
    L_0x0078:
        if (r0 == 0) goto L_0x0063;
    L_0x007a:
        r0.close();	 Catch:{ IOException -> 0x007e }
        goto L_0x0063;
    L_0x007e:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0063;
    L_0x0083:
        r0 = move-exception;
    L_0x0084:
        if (r1 == 0) goto L_0x0089;
    L_0x0086:
        r1.close();	 Catch:{ IOException -> 0x008a }
    L_0x0089:
        throw r0;
    L_0x008a:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0089;
    L_0x008f:
        r1 = move-exception;
        r6 = r1;
        r1 = r0;
        r0 = r6;
        goto L_0x0084;
    L_0x0094:
        r1 = move-exception;
        goto L_0x006b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.android.pushservice.e.j():void");
    }

    public void a(b bVar) {
        synchronized (this.j) {
            this.j.add(bVar);
            this.j.notify();
        }
    }

    public void a(boolean z) {
        if (this.o == null) {
            Log.e("PushConnection", "setConnectState, mContext == null");
            return;
        }
        int i = 0;
        if (z) {
            i = 1;
        }
        System.putInt(this.o.getContentResolver(), "com.baidu.pushservice.PushSettings.connect_state", i);
    }

    public boolean a() {
        return this.d;
    }

    public boolean a(String str) {
        boolean z = false;
        if (this.q.contains(str)) {
            this.q.remove(str);
            z = true;
        }
        if (this.q.size() >= 100) {
            this.q.clear();
        }
        this.q.add(str);
        a(this.q);
        return z;
    }

    public void b() {
        this.n = 0;
        this.m = false;
        f();
    }

    public void c() {
        if (b.a()) {
            Log.i("PushConnection", "---stop---");
        }
        this.f = true;
        this.m = true;
        this.b.removeCallbacks(this.s);
        h();
    }

    public void d() {
        if (this.c != null) {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - this.u > 180000) {
                this.c.d();
                this.u = currentTimeMillis;
                if (b.a()) {
                    Log.i("PushConnection", "sendHeartbeatMessage");
                }
            } else if (b.a()) {
                Log.i("PushConnection", "sendHeartbeatMessage ingnored， because too frequent.");
            }
        }
    }

    public HashSet e() {
        String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(absolutePath, "baidu/pushservice/files");
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(absolutePath, "baidu/pushservice/files/mi");
        HashSet hashSet = new HashSet();
        if (file.exists()) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                for (Object readLine = bufferedReader.readLine(); readLine != null; readLine = bufferedReader.readLine()) {
                    hashSet.add(readLine);
                }
                bufferedReader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return hashSet;
    }
}
