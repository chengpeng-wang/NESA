package com.mobclick.android;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;

public class a {
    private static Notification g = null;
    /* access modifiers changed from: private */
    public Context a;
    private int b = 50;
    /* access modifiers changed from: private */
    public String c;
    /* access modifiers changed from: private */
    public String d;
    private String e;
    private String f;
    private NotificationManager h;
    private int i;
    /* access modifiers changed from: private */
    public boolean j = true;
    /* access modifiers changed from: private */
    public boolean k = true;
    /* access modifiers changed from: private */
    public int l = d.a;
    private Handler m = new b(this);

    public a(Context context, String str, String str2, String str3, String str4, String str5) {
        try {
            a(context, str, str2, str3, str4, str5);
        } catch (Exception e) {
            e.printStackTrace();
            this.j = false;
        }
    }

    private int a(String str, String str2) {
        try {
            Field field = Class.forName(new StringBuilder(String.valueOf(this.a.getPackageName())).append(".R$").append(str).toString()).getField(str2);
            return Integer.parseInt(field.get(field.getName()).toString());
        } catch (Exception e) {
            Log.e("getIdByReflection error", e.getMessage());
            return 0;
        }
    }

    public static String a(String str, String str2, int i) {
        return "_" + i + "_" + str2 + ".apk";
    }

    private void a(Context context, String str, String str2, String str3, String str4, String str5) {
        this.a = context;
        this.f = str5;
        e();
        if (this.l == d.b) {
            this.j = false;
            return;
        }
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        this.c = externalStorageDirectory.getParent() + "/" + externalStorageDirectory.getName() + "/download";
        this.e = str;
        this.i = 17301633;
        g = new Notification(this.i, str2, 1);
        Notification notification = g;
        notification.flags |= 2;
        RemoteViews remoteViews = new RemoteViews(this.a.getPackageName(), a("layout", "umeng_analyse_download_notification"));
        remoteViews.setProgressBar(a("id", "umeng_analyse_progress_bar"), 100, 0, false);
        remoteViews.setTextViewText(a("id", "umeng_analyse_progress_text"), "0%");
        remoteViews.setTextViewText(a("id", "umeng_analyse_title"), str3);
        remoteViews.setTextViewText(a("id", "umeng_analyse_description"), str4);
        remoteViews.setImageViewResource(a("id", "umeng_analyse_appIcon"), this.i);
        g.contentView = remoteViews;
        Intent intent = new Intent();
        g.contentIntent = PendingIntent.getActivity(this.a, 0, intent, 134217728);
        this.h = (NotificationManager) this.a.getSystemService("notification");
    }

    /* access modifiers changed from: private */
    public void b(boolean z) {
        try {
            FileOutputStream fileOutputStream;
            int i;
            int i2;
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(this.e).openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            this.h.notify(0, g);
            byte[] bArr = new byte[4096];
            int contentLength = httpURLConnection.getContentLength();
            this.d = a(this.a.getPackageName(), this.f, contentLength);
            if (z) {
                fileOutputStream = new FileOutputStream(new File(this.c, this.d));
                i = 0;
                i2 = 0;
            } else {
                this.c = this.a.getFilesDir().getAbsolutePath();
                fileOutputStream = this.a.openFileOutput(this.d, 3);
                i = 0;
                i2 = 0;
            }
            while (true) {
                int read = inputStream.read(bArr);
                if (read <= 0) {
                    break;
                }
                fileOutputStream.write(bArr, 0, read);
                i += read;
                read = i2 + 1;
                if (i2 % this.b == 0) {
                    if (!f()) {
                        this.j = false;
                        break;
                    }
                    i2 = (int) ((((float) i) * 100.0f) / ((float) contentLength));
                    g.contentView.setProgressBar(a("id", "umeng_analyse_progress_bar"), 100, i2, false);
                    g.contentView.setTextViewText(a("id", "umeng_analyse_progress_text"), String.valueOf(i2) + "%");
                    this.h.notify(0, g);
                }
                i2 = read;
            }
            inputStream.close();
            fileOutputStream.close();
            if (this.j) {
                d();
                this.m.sendEmptyMessage(0);
                return;
            }
            d();
        } catch (Exception e) {
            e.printStackTrace();
            this.j = false;
            d();
        }
    }

    public static boolean b() {
        return g != null;
    }

    private void d() {
        this.h.cancel(0);
        g = null;
    }

    private void e() {
        if (!f()) {
            Toast.makeText(this.a, g(), 3).show();
            this.l = d.b;
        } else if (Environment.getExternalStorageState().equals("mounted")) {
            this.l = d.a;
        } else {
            this.l = d.c;
        }
    }

    private boolean f() {
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) this.a.getSystemService("connectivity")).getActiveNetworkInfo();
            return activeNetworkInfo != null ? activeNetworkInfo.isConnectedOrConnecting() : false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String g() {
        return this.a.getString(a("string", "UMBreak_Network"));
    }

    public void a(int i) {
        this.i = i;
        g.icon = i;
        g.contentView.setImageViewResource(a("id", "umeng_analyse_appIcon"), i);
    }

    public void a(String str) {
        g.contentView.setTextViewText(a("id", "umeng_analyse_title"), str);
    }

    public void a(boolean z) {
        this.k = z;
    }

    public boolean a() {
        return this.j;
    }

    public void b(String str) {
        this.c = str;
    }

    public void c() {
        if (this.j) {
            try {
                File file = new File(this.c);
                if (!file.exists()) {
                    file.mkdirs();
                }
                new c(this).start();
            } catch (Exception e) {
                Log.e(UmengConstants.LOG_TAG, e.getMessage());
            }
        }
    }
}
