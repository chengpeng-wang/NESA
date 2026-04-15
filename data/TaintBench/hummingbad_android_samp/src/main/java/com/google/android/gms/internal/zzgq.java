package com.google.android.gms.internal;

import com.moceanmobile.mast.MASTNativeAdConstants;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@zzgk
class zzgq {
    private final String zzBm;
    private int zzCI;
    private String zzF;
    private final boolean zzFA;
    private final List<String> zzFu;
    private final List<String> zzFv;
    private final String zzFw;
    private final String zzFx;
    private final String zzFy;
    private final String zzFz;

    public zzgq(int i, Map<String, String> map) {
        this.zzF = (String) map.get(MASTNativeAdConstants.RESPONSE_URL);
        this.zzFx = (String) map.get("base_uri");
        this.zzFy = (String) map.get("post_parameters");
        this.zzFA = parseBoolean((String) map.get("drt_include"));
        this.zzFw = (String) map.get("activation_overlay_url");
        this.zzFv = zzaq((String) map.get("check_packages"));
        this.zzBm = (String) map.get("request_id");
        this.zzFz = (String) map.get("type");
        this.zzFu = zzaq((String) map.get("errors"));
        this.zzCI = i;
    }

    private static boolean parseBoolean(String bool) {
        return bool != null && (bool.equals("1") || bool.equals("true"));
    }

    private List<String> zzaq(String str) {
        return str == null ? null : Arrays.asList(str.split(","));
    }

    public int getErrorCode() {
        return this.zzCI;
    }

    public String getRequestId() {
        return this.zzBm;
    }

    public String getType() {
        return this.zzFz;
    }

    public String getUrl() {
        return this.zzF;
    }

    public void setUrl(String url) {
        this.zzF = url;
    }

    public List<String> zzfK() {
        return this.zzFu;
    }

    public String zzfL() {
        return this.zzFy;
    }

    public boolean zzfM() {
        return this.zzFA;
    }
}
