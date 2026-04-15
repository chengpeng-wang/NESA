package com.splunk.mint;

import android.content.Context;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

class ActionView extends BaseDTO implements InterfaceDataType {
    private Integer domProcessingTime = null;
    private Integer domainLookupTime = null;
    private Long elapsedTime = null;
    private String host = "NA";
    private String lastName = "NA";
    private Integer loadTime = null;
    private Integer serverTime = null;
    private String viewName = "NA";

    public ActionView(EnumActionType type, String viewName, Integer domainLookupTime, Integer domProcessingTime, Integer serverTime, String host, Integer loadTime, HashMap<String, Object> extraData) {
        super(type, extraData);
        this.viewName = viewName;
        this.lastName = Properties.lastView;
        this.domainLookupTime = domainLookupTime;
        this.domProcessingTime = domProcessingTime;
        this.serverTime = serverTime;
        this.host = host;
        this.loadTime = loadTime;
        Properties.lastView = viewName;
        long currentTimestamp = System.currentTimeMillis();
        if (Properties.lastViewTime == 0) {
            this.elapsedTime = Long.valueOf(0);
        } else {
            this.elapsedTime = Long.valueOf(currentTimestamp - Properties.lastViewTime);
        }
        Properties.lastViewTime = currentTimestamp;
    }

    public static final synchronized void logView(String viewName, HashMap<String, Object> extraData) {
        synchronized (ActionView.class) {
            new ActionView(EnumActionType.view, viewName, null, null, null, null, null, extraData).save(new DataSaver());
        }
    }

    public static final synchronized void logView(String viewName, Integer domainLookupTime, Integer domProcessingTime, Integer serverTime, String host, Integer loadTime, HashMap<String, Object> extraData) {
        synchronized (ActionView.class) {
            new ActionView(EnumActionType.view, viewName, domainLookupTime, domProcessingTime, domProcessingTime, host, loadTime, extraData).save(new DataSaver());
        }
    }

    public String toJsonLine() {
        JSONObject json = getBasicDataFixtureJson();
        try {
            if (this.viewName != null) {
                json.put("current", this.viewName);
                json.put("previous", this.lastName);
                json.put("domainLookupTime", this.domainLookupTime == null ? JSONObject.NULL : this.domainLookupTime);
                json.put("domProcessingTime", this.domProcessingTime == null ? JSONObject.NULL : this.domProcessingTime);
                json.put("serverTime", this.serverTime == null ? JSONObject.NULL : this.serverTime);
                json.put("host", this.host == null ? JSONObject.NULL : this.host);
                json.put("loadTime", this.loadTime == null ? JSONObject.NULL : this.loadTime);
                json.put("elapsedTime", this.elapsedTime == null ? JSONObject.NULL : this.elapsedTime);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString() + Properties.getSeparator(EnumActionType.view);
    }

    public void send(Context ctx, NetSender netSender, boolean saveOnFail) {
        netSender.send(toJsonLine(), saveOnFail);
    }

    public void save(DataSaver dataSaver) {
        dataSaver.save(toJsonLine());
    }

    public void send(NetSender netSender, boolean saveOnFail) {
        netSender.send(toJsonLine(), saveOnFail);
    }
}
