package com.splunk.mint;

import android.content.Context;
import com.splunk.mint.Properties.RemoteSettingsProps;
import org.json.JSONException;
import org.json.JSONObject;

class ActionLog extends BaseDTO implements InterfaceDataType {
    public Integer eventLevel = Integer.valueOf(2);
    public String eventName = "";

    public ActionLog(EnumActionType type, String eventName, Integer level) {
        super(type, null);
        this.eventName = eventName;
        this.eventLevel = level;
    }

    public static final ActionLog createLog(String eventName, MintLogLevel level) {
        return new ActionLog(EnumActionType.log, eventName, Integer.valueOf(Utils.convertLoggingLevelToInt(level)));
    }

    public String toJsonLine() {
        JSONObject json = getBasicDataFixtureJson();
        try {
            json.put("log_name", this.eventName);
            json.put("level", this.eventLevel);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString() + Properties.getSeparator(this.type);
    }

    public void send(Context ctx, NetSender netSender, boolean saveOnFail) {
        netSender.send(toJsonLine(), saveOnFail);
    }

    public void save(DataSaver dataSaver) {
        if (this.eventLevel == null) {
            dataSaver.save(toJsonLine());
        } else if (this.eventLevel.intValue() >= RemoteSettingsProps.logLevel.intValue()) {
            dataSaver.save(toJsonLine());
        } else {
            Logger.logInfo("Logs's level is lower than the minimum level from Remote Settings, log will not be saved");
        }
    }

    public void send(NetSender netSender, boolean saveOnFail) {
        netSender.send(toJsonLine(), saveOnFail);
    }
}
