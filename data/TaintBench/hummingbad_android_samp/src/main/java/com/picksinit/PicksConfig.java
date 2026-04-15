package com.picksinit;

import android.os.Environment;
import com.cleanmaster.ui.app.market.b;
import com.cleanmaster.util.v;
import com.cmcm.adsdk.nativead.CMNativeAd;
import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class PicksConfig {
    public static String KEY_CACHE_TIME = CMNativeAd.KEY_CACHE_TIME;
    public static String KEY_DEBUG = "enable_debug";
    public static String KEY_MT_TYPE = "filter_mt_type";
    public static String KEY_RESUME_DOWNLOAD_SERVICE = "enable_resume_download_service";
    public static String KEY_SERVER_REPORT = "server_report";
    public static String KEY_SERVER_REQUEST = "server_request";
    public static String KEY_SHOW_TYPE = "filter_show_type";
    public static String KEY_WEBVIEW = "enable_webview";
    public int cache_time = 0;
    public boolean enable_debug = false;
    public boolean enable_download_service = false;
    public boolean enable_webview = false;
    public final String file_path = (Environment.getExternalStorageDirectory() + File.separator + "juhe.ini");
    public Set filter_mt_type = null;
    public Set filter_show_type = null;
    public String server_report = "";
    public String server_request = "";

    public void updateConfig(Map items) {
        if (items != null && !items.isEmpty()) {
            for (Entry entry : items.entrySet()) {
                String str = (String) entry.getKey();
                Object value = entry.getValue();
                if (KEY_DEBUG.equals(str)) {
                    this.enable_debug = parseBoolean(value);
                    if (this.enable_debug) {
                        freshJuheConfig();
                    }
                } else if (KEY_WEBVIEW.equals(str)) {
                    this.enable_webview = parseBoolean(value);
                } else if (KEY_SHOW_TYPE.equals(str)) {
                    if (value != null && (value instanceof Set)) {
                        this.filter_show_type = (Set) value;
                    }
                } else if (KEY_MT_TYPE.equals(str)) {
                    if (value != null && (value instanceof Set)) {
                        this.filter_mt_type = (Set) value;
                    }
                } else if (KEY_SERVER_REQUEST.equals(str)) {
                    this.server_request = parseString(value);
                } else if (KEY_SERVER_REPORT.equals(str)) {
                    this.server_report = parseString(value);
                } else if (KEY_CACHE_TIME.equals(str)) {
                    this.cache_time = parseInt(value);
                }
                if (KEY_RESUME_DOWNLOAD_SERVICE.equals(str)) {
                    this.enable_download_service = parseBoolean(value);
                    setDownloadStateState(this.enable_download_service);
                }
            }
        }
    }

    private void setDownloadStateState(boolean isResume) {
        b.a(isResume);
    }

    private void freshJuheConfig() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            v.a(new b(this));
        }
    }

    private boolean parseBoolean(Object val) {
        return Integer.parseInt(val.toString()) != 0;
    }

    private int parseInt(Object val) {
        return Integer.parseInt((String) val);
    }

    private String parseString(Object val) {
        return (String) val;
    }
}
