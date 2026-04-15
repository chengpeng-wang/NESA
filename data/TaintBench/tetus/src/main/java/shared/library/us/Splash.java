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
