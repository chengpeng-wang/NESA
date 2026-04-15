package com.splunk.mint;

import android.content.Context;
import com.splunk.mint.Properties.RemoteSettingsProps;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

class ActionEvent extends BaseDTO implements InterfaceDataType {
    protected static String savedSessionID = "";
    protected long duration = -1;
    protected Integer eventLevel = null;
    protected String eventName = "";
    private Long gnipTimestamp = null;
    protected String session_id = "";

    public ActionEvent(EnumActionType type, String eventName, Integer level, HashMap<String, Object> customData, Long gnipTimestamp) {
        super(type, customData);
        this.eventName = eventName;
        this.eventLevel = level;
        if (type == EnumActionType.ping) {
            this.session_id = Utils.getRandomSessionNumber();
            savedSessionID = this.session_id;
        } else if (type == EnumActionType.gnip) {
            this.session_id = savedSessionID;
            this.gnipTimestamp = gnipTimestamp;
        }
    }

    public static final ActionEvent createEvent(String eventName) {
        return new ActionEvent(EnumActionType.event, eventName, Integer.valueOf(Utils.convertLoggingLevelToInt(MintLogLevel.Verbose)), null, null);
    }

    public static final ActionEvent createEvent(String eventName, MintLogLevel level, HashMap<String, Object> customData) {
        return new ActionEvent(EnumActionType.event, eventName, Integer.valueOf(Utils.convertLoggingLevelToInt(level)), customData, null);
    }

    public static final ActionEvent createPing() {
        ActionEvent eventPing = new ActionEvent(EnumActionType.ping, null, null, null, null);
        Properties.lastPingTime = eventPing.timestampMilis.longValue();
        return eventPing;
    }

    public static final ActionEvent createGnip() {
        ActionEvent eventGnip = new ActionEvent(EnumActionType.gnip, null, null, null, null);
        eventGnip.duration = eventGnip.timestampMilis.longValue() - Properties.lastPingTime;
        return eventGnip;
    }

    public static final ActionEvent createGnip(Long timestamp) {
        ActionEvent eventGnip = new ActionEvent(EnumActionType.gnip, null, null, null, timestamp);
        eventGnip.duration = eventGnip.timestampMilis.longValue() - Properties.lastPingTime;
        return eventGnip;
    }

    public String toJsonLine() {
        JSONObject json = getBasicDataFixtureJson();
        try {
            if (this.duration != -1) {
                json.put("ses_duration", this.duration);
            }
            if (this.eventName != null) {
                json.put("event_name", this.eventName);
            }
            if (this.eventLevel != null) {
                json.put("level", this.eventLevel);
            }
            if (this.type != EnumActionType.event) {
                json.put("session_id", this.session_id);
            }
            if (this.type == EnumActionType.ping) {
                json.put("rooted", this.rooted);
                json.put("fsEncrypted", this.isFSEncrypted);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (this.type != EnumActionType.gnip || this.gnipTimestamp == null) {
            return json.toString() + Properties.getSeparator(this.type);
        }
        return json.toString() + Properties.getSeparator(this.type, String.valueOf(this.gnipTimestamp));
    }

    public void send(Context ctx, NetSender netSender, boolean saveOnFail) {
        if (this.type.equals(EnumActionType.ping)) {
            StringBuilder settingsUrl = new StringBuilder();
            settingsUrl.append(MintUrls.getURL(0, 1));
            settingsUrl.append("?hash=");
            settingsUrl.append(RemoteSettingsProps.hashCode);
            RemoteSettingsData remoteData = RemoteSettings.convertJsonToRemoteSettings(netSender.sendBlocking(settingsUrl.toString(), toJsonLine(), saveOnFail).getServerResponse());
            if (remoteData != null) {
                RemoteSettings.saveAndLoadRemoteSettings(ctx, remoteData);
                return;
            }
            return;
        }
        netSender.send(toJsonLine(), saveOnFail);
    }

    public void save(DataSaver dataSaver) {
        if (this.eventLevel == null) {
            dataSaver.save(toJsonLine());
        } else if (this.eventLevel.intValue() >= RemoteSettingsProps.eventLevel.intValue()) {
            dataSaver.save(toJsonLine());
        } else {
            Logger.logInfo("Event's level is lower than the minimum level from Remote Settings, event will not be saved");
        }
    }

    public void send(NetSender netSender, boolean saveOnFail) {
        netSender.send(toJsonLine(), saveOnFail);
    }
}
