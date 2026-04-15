package com.splunk.mint;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.splunk.mint.Properties.RemoteSettingsProps;
import org.json.JSONException;
import org.json.JSONObject;

class RemoteSettings {
    private static final String DEVSETTINGS = "devSettings";
    private static final String EVENTLEVEL = "eventLevel";
    private static final String HASHCODE = "hashCode";
    private static final String LOGLEVEL = "logLevel";
    private static final String NETWORKMONITORING = "netMonitoring";
    private static final String REMOTESETTINGS_API = "1";
    private static final String REMOTESETTINGS_NAME = "remSetVer";
    private static final String SESSIONTIME = "sessionTime";
    private static final String SHARED_PREFERENSES_NAME = "REMOTESETTINGSSETTINGS";

    RemoteSettings() {
    }

    protected static final RemoteSettingsData convertJsonToRemoteSettings(String jsonData) {
        if (jsonData == null || jsonData.length() < 1) {
            return null;
        }
        RemoteSettingsData rsd = new RemoteSettingsData();
        try {
            JSONObject settings = new JSONObject(jsonData).optJSONObject("remSetVer1");
            if (settings == null) {
                return rsd;
            }
            rsd.logLevel = Integer.valueOf(settings.optInt(LOGLEVEL));
            rsd.eventLevel = Integer.valueOf(settings.getInt(EVENTLEVEL));
            rsd.netMonitoring = Boolean.valueOf(settings.optBoolean(NETWORKMONITORING));
            rsd.sessionTime = Integer.valueOf(settings.optInt(SESSIONTIME));
            rsd.devSettings = settings.optJSONObject(DEVSETTINGS).toString();
            rsd.hashCode = settings.optString("hash");
            return rsd;
        } catch (JSONException e) {
            Logger.logError("Could not convert json to remote data");
            return rsd;
        }
    }

    protected static final boolean saveAndLoadRemoteSettings(Context ctx, RemoteSettingsData rsd) {
        SharedPreferences preferences = ctx.getSharedPreferences(SHARED_PREFERENSES_NAME, 0);
        if (preferences == null) {
            return false;
        }
        Editor editor = preferences.edit();
        if (editor == null) {
            return false;
        }
        if (rsd.logLevel != null && rsd.logLevel.intValue() > 0) {
            editor.putInt(LOGLEVEL, rsd.logLevel.intValue());
            RemoteSettingsProps.logLevel = rsd.logLevel;
        }
        if (rsd.eventLevel != null && rsd.eventLevel.intValue() > 0) {
            editor.putInt(EVENTLEVEL, rsd.eventLevel.intValue());
            RemoteSettingsProps.eventLevel = rsd.eventLevel;
        }
        if (rsd.netMonitoring != null) {
            editor.putBoolean(NETWORKMONITORING, rsd.netMonitoring.booleanValue());
            RemoteSettingsProps.netMonitoringEnabled = rsd.netMonitoring;
        }
        if (rsd.sessionTime != null && rsd.sessionTime.intValue() > 0) {
            editor.putInt(SESSIONTIME, rsd.sessionTime.intValue());
            RemoteSettingsProps.sessionTime = rsd.sessionTime;
        }
        if (rsd.devSettings != null) {
            editor.putString(DEVSETTINGS, rsd.devSettings);
            try {
                RemoteSettingsProps.devSettings = new JSONObject(rsd.devSettings);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (rsd.hashCode != null && rsd.hashCode.length() > 1) {
            editor.putString(HASHCODE, rsd.hashCode);
            RemoteSettingsProps.hashCode = rsd.hashCode;
        }
        return editor.commit();
    }

    protected static final RemoteSettingsData loadRemoteSettings(Context ctx) {
        RemoteSettingsData rsd = new RemoteSettingsData();
        SharedPreferences preferences = ctx.getSharedPreferences(SHARED_PREFERENSES_NAME, 0);
        if (preferences == null) {
            return null;
        }
        rsd.logLevel = Integer.valueOf(preferences.getInt(LOGLEVEL, RemoteSettingsProps.logLevel.intValue()));
        rsd.eventLevel = Integer.valueOf(preferences.getInt(EVENTLEVEL, RemoteSettingsProps.eventLevel.intValue()));
        rsd.netMonitoring = Boolean.valueOf(preferences.getBoolean(NETWORKMONITORING, RemoteSettingsProps.netMonitoringEnabled.booleanValue()));
        rsd.sessionTime = Integer.valueOf(preferences.getInt(SESSIONTIME, RemoteSettingsProps.sessionTime.intValue()));
        rsd.devSettings = preferences.getString(DEVSETTINGS, RemoteSettingsProps.devSettings.toString());
        rsd.hashCode = preferences.getString(HASHCODE, RemoteSettingsProps.hashCode);
        return rsd;
    }
}
