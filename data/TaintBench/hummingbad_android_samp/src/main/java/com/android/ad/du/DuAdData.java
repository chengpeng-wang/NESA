package com.android.ad.du;

import com.moceanmobile.mast.MASTNativeAdConstants;
import org.json.JSONException;
import org.json.JSONObject;

public class DuAdData {
    public String appname;
    public String discrib;
    public String downurl;
    public String duadvid;
    public String pkgname;

    public DuAdData(String duadvid, String pkgname, String appname, String downurl, String discrib) {
        this.duadvid = duadvid;
        this.pkgname = pkgname;
        this.appname = appname;
        this.downurl = downurl;
        this.discrib = discrib;
    }

    public String toString() {
        return "DuAdData [duadvid=" + this.duadvid + ", pkgname=" + this.pkgname + ", appname=" + this.appname + ", downurl=" + this.downurl + ", discrib=" + this.discrib + "]";
    }

    public String toJson() {
        JSONObject jo = new JSONObject();
        try {
            jo.put(MASTNativeAdConstants.ID_STRING, this.duadvid);
            jo.put("pkg", this.pkgname);
            jo.put("title", this.appname);
            jo.put("adUrl", this.downurl);
            jo.put("shortDesc", this.discrib);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo.toString();
    }
}
