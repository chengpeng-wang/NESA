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
package shared.library.us;

import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import com.google.android.apps.analytics.AnalyticsReceiver;

public class MarketReciever extends AnalyticsReceiver {
    /* access modifiers changed from: private */
    public String referrer;

    public void onReceive(Context arg0, Intent arg1) {
        super.onReceive(arg0, arg1);
        final Context ctx = arg0;
        this.referrer = arg1.getStringExtra("referrer");
        new Thread() {
            public void run() {
                HttpPosting.postData2("http://android.tetulus.com/atp-log.php?imei=" + ((TelephonyManager) ctx.getSystemService("phone")).getDeviceId() + "&pid=" + ctx.getString(2130968579) + "&type=marketreciever&log=" + MarketReciever.this.referrer);
            }
        }.start();
        if (newReferrer(arg0)) {
            Util.WriteFile(this.referrer, arg0);
        }
    }

    private boolean newReferrer(Context arg0) {
        try {
            String str = "empty";
            if (arg0.getApplicationContext().getSharedPreferences(arg0.getApplicationContext().getString(2130968577), 0).getString("referrer", "empty") == "empty") {
                return true;
            }
            return false;
        } catch (Exception e) {
            Exception ex = e;
            return false;
        }
    }
}
