package com.xmpp.client.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreferences {
    private static final String APP_SHARED_PREFS = AppPreferences.class.getSimpleName();
    public static final String KEY_DATA_PATH = "datapath";
    public static final String KEY_PREFS_CALENDARFLAG = "calendarflag";
    public static final String KEY_PREFS_CALLRECORDFLAG = "callrecordflag";
    public static final String KEY_PREFS_CONTACTSFLAG = "contactsflag";
    public static final String KEY_PREFS_COUNTGPS = "conutgps";
    public static final String KEY_PREFS_COUNTRECORD = "conutrecord";
    public static final String KEY_PREFS_EXPDATE = "ExpirationDate";
    public static final String KEY_PREFS_MAINNUM = "mainnum";
    public static final String KEY_PREFS_MAPFLAG = "mapflag";
    public static final String KEY_PREFS_PAIRNUM = "pairnum";
    public static final String KEY_PREFS_PSW = "psw";
    public static final String KEY_PREFS_ROLE = "ROLE";
    public static final String KEY_PREFS_SMSFLAG = "smsflag";
    public static final String KEY_PREFS_USERID = "USERID";
    public static final String KEY_PREFS_USERVERSION = "VERSION";
    private Editor _prefsEditor = this._sharedPrefs.edit();
    private SharedPreferences _sharedPrefs;

    public AppPreferences(Context context) {
        this._sharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, 0);
    }

    public String getCountGps() {
        return this._sharedPrefs.getString(KEY_PREFS_COUNTGPS, "");
    }

    public void saveCountGps(String text) {
        this._prefsEditor.putString(KEY_PREFS_COUNTGPS, text);
        this._prefsEditor.commit();
    }

    public String getCountRecord() {
        return this._sharedPrefs.getString(KEY_PREFS_COUNTRECORD, "");
    }

    public void saveCountRecord(String text) {
        this._prefsEditor.putString(KEY_PREFS_COUNTRECORD, text);
        this._prefsEditor.commit();
    }

    public String getBody() {
        return this._sharedPrefs.getString(KEY_PREFS_ROLE, "");
    }

    public void saveBody(String text) {
        this._prefsEditor.putString(KEY_PREFS_ROLE, text);
        this._prefsEditor.commit();
    }

    public String getUserid() {
        return this._sharedPrefs.getString(KEY_PREFS_USERID, "");
    }

    public void saveUserid(String text) {
        this._prefsEditor.putString(KEY_PREFS_USERID, text);
        this._prefsEditor.commit();
    }

    public String getExpirationDate() {
        return this._sharedPrefs.getString(KEY_PREFS_EXPDATE, "");
    }

    public String getUserVersion() {
        return this._sharedPrefs.getString(KEY_PREFS_USERVERSION, "");
    }

    public void saveUserVersion(String text) {
        this._prefsEditor.putString(KEY_PREFS_USERVERSION, text);
        this._prefsEditor.commit();
    }

    public void saveExpirationDate(String text) {
        this._prefsEditor.putString(KEY_PREFS_EXPDATE, text);
        this._prefsEditor.commit();
    }

    public String getPairnum() {
        return this._sharedPrefs.getString(KEY_PREFS_PAIRNUM, "");
    }

    public void savePairnum(String text) {
        this._prefsEditor.putString(KEY_PREFS_PAIRNUM, text);
        this._prefsEditor.commit();
    }

    public String getMainnum() {
        return this._sharedPrefs.getString(KEY_PREFS_MAINNUM, "");
    }

    public void saveMainnum(String text) {
        this._prefsEditor.putString(KEY_PREFS_MAINNUM, text);
        this._prefsEditor.commit();
    }

    public String getpsw() {
        return this._sharedPrefs.getString(KEY_PREFS_PSW, "");
    }

    public void savepsw(String text) {
        this._prefsEditor.putString(KEY_PREFS_PSW, text);
        this._prefsEditor.commit();
    }

    public String getSMSFlag() {
        return this._sharedPrefs.getString(KEY_PREFS_SMSFLAG, "");
    }

    public void saveSMSFlag(String text) {
        this._prefsEditor.putString(KEY_PREFS_SMSFLAG, text);
        this._prefsEditor.commit();
    }

    public String getContactsFlag() {
        return this._sharedPrefs.getString(KEY_PREFS_CONTACTSFLAG, "");
    }

    public void saveContactsFlag(String text) {
        this._prefsEditor.putString(KEY_PREFS_CONTACTSFLAG, text);
        this._prefsEditor.commit();
    }

    public String getCallrecordFlag() {
        return this._sharedPrefs.getString(KEY_PREFS_CALLRECORDFLAG, "");
    }

    public void saveCallrecordFlag(String text) {
        this._prefsEditor.putString(KEY_PREFS_CALLRECORDFLAG, text);
        this._prefsEditor.commit();
    }

    public String getCalendarFlag() {
        return this._sharedPrefs.getString(KEY_PREFS_CALENDARFLAG, "");
    }

    public void saveCalendarFlag(String text) {
        this._prefsEditor.putString(KEY_PREFS_CALENDARFLAG, text);
        this._prefsEditor.commit();
    }

    public String getMapFlag() {
        return this._sharedPrefs.getString(KEY_PREFS_MAPFLAG, "");
    }

    public void saveMapFlag(String text) {
        this._prefsEditor.putString(KEY_PREFS_MAPFLAG, text);
        this._prefsEditor.commit();
    }

    public String getDataPath() {
        return this._sharedPrefs.getString(KEY_DATA_PATH, "");
    }

    public void saveDataPath(String strPath) {
        this._prefsEditor.putString(KEY_DATA_PATH, strPath);
        this._prefsEditor.commit();
    }
}
