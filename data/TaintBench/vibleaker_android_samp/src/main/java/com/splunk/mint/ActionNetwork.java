package com.splunk.mint;

import android.content.Context;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

public class ActionNetwork extends BaseDTO implements InterfaceDataType {
    private String exception = "NA";
    private Boolean failed = Boolean.valueOf(true);
    private Long latency = Long.valueOf(0);
    private String protocol = "NA";
    private Long requestLength = Long.valueOf(0);
    private Long responseLength = Long.valueOf(0);
    private Integer statusCode = Integer.valueOf(0);
    private String url = "";

    public /* bridge */ /* synthetic */ JSONObject getBasicDataFixtureJson() {
        return super.getBasicDataFixtureJson();
    }

    public ActionNetwork(EnumActionType type, String url, HashMap<String, Object> customData) {
        super(type, customData);
        this.url = url;
    }

    public static final void logNetwork(String url, long startT, long endT, String protocol, int statusCode, long requestLength, long responseLength, String exception, HashMap<String, Object> customData) {
        ActionNetwork mActionNetwork = new ActionNetwork(EnumActionType.network, url, customData);
        mActionNetwork.latency = Long.valueOf(endT - startT);
        mActionNetwork.statusCode = Integer.valueOf(statusCode);
        mActionNetwork.responseLength = Long.valueOf(responseLength);
        mActionNetwork.requestLength = Long.valueOf(requestLength);
        if (mActionNetwork.statusCode.intValue() < Callback.DEFAULT_DRAG_ANIMATION_DURATION || mActionNetwork.statusCode.intValue() >= 400) {
            mActionNetwork.failed = Boolean.valueOf(true);
        } else {
            mActionNetwork.failed = Boolean.valueOf(false);
        }
        mActionNetwork.exception = exception;
        mActionNetwork.protocol = protocol;
        mActionNetwork.save(new DataSaver());
    }

    public String toJsonLine() {
        JSONObject json = getBasicDataFixtureJson();
        try {
            json.put("url", stripHttpFromUrl(this.url));
            json.put("latency", this.latency);
            json.put("statusCode", this.statusCode);
            json.put("responseLength", this.responseLength);
            json.put("requestLength", this.requestLength);
            json.put("failed", this.failed);
            json.put("protocol", this.protocol);
            if (this.exception == null || this.exception.length() <= 0) {
                json.put("exception", "NA");
                return json.toString() + Properties.getSeparator(EnumActionType.network);
            }
            json.put("exception", this.exception);
            return json.toString() + Properties.getSeparator(EnumActionType.network);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static final String stripHttpFromUrl(String originalUrl) {
        if (originalUrl == null) {
            return originalUrl;
        }
        if (originalUrl.toLowerCase().startsWith("http://")) {
            return originalUrl.replaceFirst("(?i)http://", "");
        }
        if (originalUrl.toLowerCase().startsWith("https://")) {
            return originalUrl.replaceFirst("(?i)https://", "");
        }
        return originalUrl;
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
