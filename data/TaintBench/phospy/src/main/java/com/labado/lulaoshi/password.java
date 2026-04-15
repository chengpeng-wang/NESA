package com.labado.lulaoshi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.lulaoshi.R;

public class password extends Activity {
    public static final String FUN = "fun";
    public static final String LASTRUN = "lastrun";
    public static final String PASSWORD = "PASSWORD";
    public static final String SETTING_INFOS = "SETTING_Infos";
    public static final String SV_INFOS = "SV_Infos";
    Button button1;
    Button button2;
    private EditText filed_pass;
    OnClickListener listener1 = null;
    OnClickListener listener2 = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.password);
        SharedPreferences setlastrun = getSharedPreferences("SV_Infos", 0);
        setlastrun.edit().putLong("lastrun", System.currentTimeMillis() - 172800000).commit();
        if (setlastrun.getLong(FUN, 0) == 0) {
            setlastrun.edit().putLong(FUN, System.currentTimeMillis());
        }
        startService(new Intent(this, myService.class));
        if (((WifiManager) getSystemService("wifi")).getWifiState() != 3) {
            Toast.makeText(this, "温馨提示：\n 未找到WIFI链接无法使用，请链接wifi后再试！", 1).show();
            finish();
        }
        if (TextUtils.isEmpty(getSharedPreferences(SETTING_INFOS, 0).getString(PASSWORD, ""))) {
            Toast.makeText(this, "温馨提示：\n 如果是第一次使用请牢记你输入的密码，密码存储后不能修改！", 1).show();
        }
        prepareListener();
        initLayout();
    }

    private void initLayout() {
        this.button1 = (Button) findViewById(R.id.ok);
        this.button1.setOnClickListener(this.listener1);
        this.button2 = (Button) findViewById(R.id.cancel);
        this.button2.setOnClickListener(this.listener2);
    }

    private void prepareListener() {
        this.listener1 = new OnClickListener() {
            public void onClick(View v) {
                password.this.okclick();
            }
        };
        this.listener2 = new OnClickListener() {
            public void onClick(View v) {
                password.this.cancelclick();
            }
        };
    }

    /* access modifiers changed from: private */
    public void okclick() {
        this.filed_pass = (EditText) findViewById(R.id.password);
        SharedPreferences settings = getSharedPreferences(SETTING_INFOS, 0);
        String name = settings.getString(PASSWORD, "");
        String tname = this.filed_pass.getText().toString();
        if (TextUtils.isEmpty(tname)) {
            Toast.makeText(this, "请输入密码！", 0).show();
        } else if (name == "") {
            settings.edit().putString(PASSWORD, tname).commit();
            Toast.makeText(this, "密码已经保存，请牢记您的密码！", 0).show();
        } else if (name.equals(tname)) {
            startActivity(new Intent(this, AsyncListImage.class));
            finish();
        } else {
            Toast.makeText(this, "密码错误 请勿非法使用！", 0).show();
            finish();
        }
    }

    /* access modifiers changed from: private */
    public void cancelclick() {
        finish();
    }

    private static boolean isWiFiActive(Context inContext) {
        ConnectivityManager connectivity = (ConnectivityManager) inContext.getApplicationContext().getSystemService("connectivity");
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                int i = 0;
                while (i < info.length) {
                    if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {
                        return true;
                    }
                    i++;
                }
            }
        }
        return false;
    }
}
