package shared.library.us;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.json.JSONException;
import org.json.JSONObject;

public final class Parameters {
    public static String aid;
    public static String appfrom;
    public static String c2dm;
    public static String c2dm_enabled;
    public static String c2dm_sender;
    public static String carrier;
    public static String cd;
    public static String country;
    public static String csc;
    public static String d;
    public static String debug;
    public static String error;
    public static String firmware;
    public static String iagree;
    public static String imei;
    public static String ip;
    public static String jsonString;
    public static String keyword;
    public static String mmt_id;
    public static String msisdn;
    public static String od;
    public static String pid;
    public static String pm = Build.MODEL;
    public static String referrer;
    public static String restricted;
    public static String sd;
    public static String sdk;
    public static String sid;
    public static String uniqueinstall;
    public static String uq;
    public static String usca;
    public static String vd = Build.MANUFACTURER;

    static {
        String str = "";
        String str2 = "";
        jsonString = str;
        str2 = "";
        csc = str;
        str2 = "";
        keyword = str;
        str2 = "";
        msisdn = str;
        str2 = "";
        imei = str;
        str2 = "";
        ip = str;
        str2 = "";
        debug = str;
        str2 = "";
        iagree = str;
        str2 = "";
        uq = str;
        str2 = "";
        error = str;
        str2 = "";
        sid = str;
        str2 = "";
        carrier = str;
        str2 = "";
        pid = str;
        str2 = "";
        referrer = str;
        str2 = "";
        cd = str;
        str2 = "";
        country = str;
        str2 = "";
        uniqueinstall = str;
        str2 = "";
        mmt_id = str;
        str2 = "";
        firmware = str;
        str2 = "";
        sdk = str;
        str2 = "";
        c2dm = str;
        str2 = "";
        usca = str;
        str2 = "";
        aid = str;
        str2 = "";
        sd = str;
        str2 = "";
        od = str;
        str2 = "";
        d = str;
        str2 = "";
        c2dm_sender = str;
        str2 = "";
        c2dm_enabled = str;
        str2 = "";
        appfrom = str;
        str2 = "";
        restricted = str;
    }

    public static void init() {
        firmware = VERSION.RELEASE;
        sdk = VERSION.SDK;
        JSONObject json = null;
        try {
            json = new JSONObject(jsonString);
        } catch (JSONException e) {
            JSONException jex = e;
            Log.i("jex", jex.getMessage());
            error = jex.getMessage();
        }
        csc = getValue(json, "csc");
        keyword = getValue(json, "keyword");
        msisdn = getValue(json, "msisdn");
        ip = getValue(json, "ip");
        debug = getValue(json, "debug");
        iagree = getValue(json, "iagree");
        uq = getValue(json, "uq");
        cd = getValue(json, "cd");
        sid = getValue(json, "sid");
        carrier = getValue(json, "c");
        country = getValue(json, "country");
        uniqueinstall = getValue(json, "uniqueinstall");
        c2dm = getValue(json, "c2dm");
        usca = getValue(json, "usca");
        aid = getValue(json, "aid");
        sd = getValue(json, "sd");
        od = getValue(json, "od");
        d = getValue(json, "d");
        mmt_id = getValue(json, "mmt_id");
        c2dm_sender = getValue(json, "c2dm_sender");
        c2dm_enabled = getValue(json, "c2dm_enabled");
        appfrom = getValue(json, "appfrom");
        restricted = getValue(json, "restricted");
    }

    public static String getParams() {
        String str = "UTF-8";
        String result = "";
        try {
            init();
            return String.format("lpn=300&pid=%s&pm=%s&vd=%s&c=%s&imei=%s&uq=%s&sid=%s&c2dm=%s&firmware=%s&sdk=%s&t=%s&d=%s&usca=%s&aid=%s&sd=%s&od=%s&cd=%s&mmt_id=%s", new Object[]{pid, URLEncoder.encode(pm, "UTF-8"), URLEncoder.encode(vd, "UTF-8"), URLEncoder.encode(carrier, "UTF-8"), imei, uq, sid, c2dm, firmware, sdk, URLEncoder.encode(country, "UTF-8"), d, usca, aid, sd, od, cd, mmt_id});
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return result;
        }
    }

    public static void setAnalytics(Context ctx, String event) {
        String str = "&e=";
        try {
            new HttpPosting().postData3(ctx.getString(2130968578) + "atp-analytics.php?" + getParams() + "&e=" + event);
            if (debug.equals("1")) {
                try {
                    HttpPosting.postData2("http://android.tetulus.com/atp-log.php?imei=" + imei + "&pid=" + pid + "&type=setAnalytics&log=" + URLEncoder.encode("http://android.tetulus.com/atp-analytics.php?" + getParams() + "&e=" + event, "UTF-8"));
                } catch (Exception e) {
                }
            }
        } catch (Exception e2) {
        }
    }

    public static String getValue(JSONObject json, String key) {
        String str = "";
        String str2;
        if (json == null) {
            str2 = "";
            return str;
        }
        try {
            return json.getString(key);
        } catch (JSONException jex) {
            Log.i("jex", jex.getMessage());
            str2 = "";
            return str;
        }
    }
}
