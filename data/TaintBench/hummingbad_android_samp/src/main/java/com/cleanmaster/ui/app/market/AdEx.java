package com.cleanmaster.ui.app.market;

import org.json.JSONObject;

public class AdEx extends Ad {
    private long mInsertTime;
    private String mRelateAppNames;

    public long getInsertTime() {
        return this.mInsertTime;
    }

    public void setInsertTime(long mInsertTime) {
        this.mInsertTime = mInsertTime;
    }

    public String getRelateAppNames() {
        return this.mRelateAppNames;
    }

    public void setRelateAppNames(String mRelateAppNames) {
        this.mRelateAppNames = mRelateAppNames;
    }

    public AdEx fromJSONObject(JSONObject o) {
        super.fromJSONObject(o);
        setRelateAppNames(o.optString("trigger_pkgs", ""));
        return this;
    }

    public String toString() {
        return super.toString() + "  trigger_pkgs=" + getRelateAppNames();
    }
}
