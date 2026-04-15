package com.splunk.mint;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

abstract class BaseDTO {
    protected static final String UNKNOWN = "NA";
    protected String apiKey;
    protected String appVersionCode;
    protected String appVersionName;
    protected int batteryLevel;
    protected String carrier;
    protected String connection;
    protected String currentView;
    protected HashMap<String, Object> customData;
    protected String device;
    protected ExtraData extraData;
    protected String isFSEncrypted;
    protected String locale;
    protected String osVersion;
    protected String packageName;
    protected String platform = "Android";
    protected String remoteIP;
    protected Boolean rooted;
    protected String screenOrientation;
    protected String sdkVersion = "4.2.1";
    protected String state;
    protected Long timestampMilis = Long.valueOf(System.currentTimeMillis());
    protected EnumActionType type;
    protected String userIdentifier;
    protected String uuid;

    public BaseDTO(EnumActionType dataType, HashMap<String, Object> customData_) {
        this.type = dataType;
        this.apiKey = Properties.API_KEY;
        this.device = (Properties.PHONE_BRAND != null ? Properties.PHONE_BRAND + " " : "") + Properties.PHONE_MODEL;
        this.osVersion = Properties.OS_VERSION;
        this.appVersionCode = Properties.APP_VERSIONCODE;
        this.appVersionName = Properties.APP_VERSIONNAME;
        this.packageName = Properties.APP_PACKAGE;
        this.locale = Properties.LOCALE;
        this.rooted = Boolean.valueOf(Properties.HAS_ROOT);
        this.uuid = Properties.UID;
        this.userIdentifier = Properties.userIdentifier;
        this.carrier = Properties.CARRIER;
        this.remoteIP = "{%#@@#%}";
        this.connection = Properties.CONNECTION;
        this.state = Properties.STATE;
        this.extraData = Properties.extraData;
        this.screenOrientation = Properties.SCREEN_ORIENTATION;
        this.customData = customData_;
        this.isFSEncrypted = Properties.ISFSENCRYPTED;
        this.batteryLevel = Properties.BATTERY_LEVEL;
        this.currentView = Properties.lastView;
    }

    public synchronized JSONObject getBasicDataFixtureJson() {
        JSONObject json;
        json = new JSONObject();
        try {
            Iterator i$;
            json.put("sdkVersion", this.sdkVersion);
            json.put("apiKey", this.apiKey);
            json.put("platform", this.platform);
            json.put("device", this.device);
            json.put("osVersion", this.osVersion);
            json.put("locale", this.locale);
            json.put("uuid", this.uuid);
            json.put("userIdentifier", this.userIdentifier);
            json.put("batteryLevel", this.batteryLevel);
            json.put("carrier", this.carrier);
            json.put("remoteIP", this.remoteIP);
            json.put("appVersionCode", this.appVersionCode);
            json.put("appVersionName", this.appVersionName);
            json.put("packageName", this.packageName);
            json.put("connection", this.connection);
            json.put("state", this.state);
            json.put("currentView", this.currentView);
            json.put("screenOrientation", this.screenOrientation);
            JSONObject extraDataJson = new JSONObject();
            if (!(this.extraData == null || this.extraData.isEmpty())) {
                for (Entry<String, Object> extra : this.extraData.entrySet()) {
                    if (extra.getValue() == null) {
                        extraDataJson.put((String) extra.getKey(), "null");
                    } else {
                        extraDataJson.put((String) extra.getKey(), extra.getValue());
                    }
                }
            }
            if (!(this.customData == null || this.customData.isEmpty())) {
                for (Entry<String, Object> extra2 : this.customData.entrySet()) {
                    if (extra2.getValue() == null) {
                        extraDataJson.put((String) extra2.getKey(), "null");
                    } else {
                        extraDataJson.put((String) extra2.getKey(), extra2.getValue());
                    }
                }
            }
            json.put("extraData", extraDataJson);
            JSONArray transactions = new JSONArray();
            if (Properties.transactions != null) {
                i$ = Properties.transactions.iterator();
                while (i$.hasNext()) {
                    transactions.put((String) i$.next());
                }
            }
            json.put("transactions", transactions);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
