package defpackage;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build.VERSION;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/* renamed from: LogCatBroadcaster */
public class LogCatBroadcaster implements Runnable {
    private static boolean started = false;
    private Context context;

    private LogCatBroadcaster(Context context) {
        this.context = context;
    }

    public static synchronized void start(Context context) {
        Context context2 = context;
        synchronized (LogCatBroadcaster.class) {
            if (!started) {
                started = true;
                if (VERSION.SDK_INT >= 16) {
                    if ((0 != (context2.getApplicationInfo().flags & 2) ? 1 : null) != null) {
                        try {
                            PackageInfo packageInfo = context2.getPackageManager().getPackageInfo("com.aide.ui", 128);
                            Thread thread = r10;
                            LogCatBroadcaster logCatBroadcaster = r10;
                            LogCatBroadcaster logCatBroadcaster2 = new LogCatBroadcaster(context2);
                            Thread thread2 = new Thread(logCatBroadcaster);
                            thread.start();
                        } catch (NameNotFoundException e) {
                            NameNotFoundException nameNotFoundException = e;
                        }
                    }
                }
            }
        }
    }

    public void run() {
        try {
            BufferedReader bufferedReader = r11;
            Reader reader = r11;
            Reader inputStreamReader = new InputStreamReader(Runtime.getRuntime().exec("logcat -v threadtime").getInputStream());
            BufferedReader bufferedReader2 = new BufferedReader(reader, 20);
            BufferedReader bufferedReader3 = bufferedReader;
            String str = "";
            while (true) {
                String readLine = bufferedReader3.readLine();
                str = readLine;
                if (readLine != null) {
                    Intent intent = r11;
                    Intent intent2 = new Intent();
                    Intent intent3 = intent;
                    intent = intent3.setPackage("com.aide.ui");
                    intent = intent3.setAction("com.aide.runtime.VIEW_LOGCAT_ENTRY");
                    String[] strArr = new String[1];
                    String[] strArr2 = strArr;
                    strArr[0] = str;
                    intent = intent3.putExtra("lines", strArr2);
                    this.context.sendBroadcast(intent3);
                } else {
                    return;
                }
            }
        } catch (IOException e) {
            IOException iOException = e;
        }
    }
}
