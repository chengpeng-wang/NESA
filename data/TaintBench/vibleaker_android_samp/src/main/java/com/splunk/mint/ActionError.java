package com.splunk.mint;

import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import java.text.DecimalFormat;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class ActionError extends BaseDTO implements InterfaceDataType {
    private JSONArray breadcrumbs;
    private String errorHash;
    private EnumStateStatus gpsStatus;
    private Boolean handled;
    private String klass;
    private String memAppAvailable;
    private String memAppMax;
    private String memAppTotal;
    private String memSysAvailable = null;
    private String memSysLow;
    private String memSysThreshold;
    private String memSysTotal = null;
    private String message;
    private Long msFromStart;
    private String stacktrace;
    private String where;

    public ActionError(EnumActionType dataType, String stacktrace, EnumExceptionType exceptionType, HashMap<String, Object> customData) {
        super(dataType, customData);
        this.stacktrace = stacktrace;
        if (exceptionType == EnumExceptionType.HANDLED) {
            this.handled = Boolean.valueOf(true);
        } else {
            this.handled = Boolean.valueOf(false);
        }
        HashMap<String, String> stackHashMap = StacktraceHash.manipulateStacktrace(Properties.APP_PACKAGE, stacktrace);
        this.klass = (String) stackHashMap.get("klass");
        this.message = (String) stackHashMap.get("message");
        this.errorHash = (String) stackHashMap.get("errorHash");
        this.where = (String) stackHashMap.get("where");
        this.gpsStatus = Properties.IS_GPS_ON;
        this.msFromStart = Utils.getMilisFromStart();
        MemoryInfo memoryInfo = new MemoryInfo();
        Runtime rt = Runtime.getRuntime();
        if (!this.handled.booleanValue()) {
            HashMap<String, String> memInfo = Utils.getMemoryInfo();
            this.memSysTotal = (String) memInfo.get("memTotal");
            this.memSysAvailable = (String) memInfo.get("memFree");
        }
        DecimalFormat nft = new DecimalFormat("#.##");
        this.memSysThreshold = String.valueOf(nft.format(((double) memoryInfo.threshold) / 1048576.0d));
        this.memSysLow = String.valueOf(memoryInfo.lowMemory);
        this.memAppMax = String.valueOf(nft.format(((double) rt.maxMemory()) / 1048576.0d));
        this.memAppAvailable = String.valueOf(nft.format(((double) rt.freeMemory()) / 1048576.0d));
        this.memAppTotal = String.valueOf(nft.format(((double) rt.totalMemory()) / 1048576.0d));
        this.breadcrumbs = Properties.breadcrumbs.getList();
    }

    public ActionError(EnumActionType dataType, String message, String file, String line, String stacktrace, HashMap<String, Object> map, EnumExceptionType exceptionType) {
        super(dataType, map);
        this.stacktrace = stacktrace;
        if (exceptionType == EnumExceptionType.HANDLED) {
            this.handled = Boolean.valueOf(true);
        } else {
            this.handled = Boolean.valueOf(false);
        }
        this.klass = file;
        this.message = message;
        this.errorHash = StacktraceHash.getMD5ForJavascriptError(stacktrace);
        this.where = "line: " + line;
        this.gpsStatus = Properties.IS_GPS_ON;
        this.msFromStart = Utils.getMilisFromStart();
        MemoryInfo memoryInfo = new MemoryInfo();
        Runtime rt = Runtime.getRuntime();
        if (!this.handled.booleanValue()) {
            HashMap<String, String> memInfo = Utils.getMemoryInfo();
            this.memSysTotal = (String) memInfo.get("memTotal");
            this.memSysAvailable = (String) memInfo.get("memFree");
        }
        DecimalFormat nft = new DecimalFormat("#.##");
        this.memSysThreshold = String.valueOf(nft.format(((double) memoryInfo.threshold) / 1048576.0d));
        this.memSysLow = String.valueOf(memoryInfo.lowMemory);
        this.memAppMax = String.valueOf(nft.format(((double) rt.maxMemory()) / 1048576.0d));
        this.memAppAvailable = String.valueOf(nft.format(((double) rt.freeMemory()) / 1048576.0d));
        this.memAppTotal = String.valueOf(nft.format(((double) rt.totalMemory()) / 1048576.0d));
        this.breadcrumbs = Properties.breadcrumbs.getList();
    }

    /* access modifiers changed from: protected|final */
    public final String getErrorHash() {
        return this.errorHash;
    }

    public String toJsonLine() {
        JSONObject json = getBasicDataFixtureJson();
        try {
            json.put("stacktrace", this.stacktrace);
            json.put("handled", this.handled);
            json.put("klass", this.klass);
            json.put("message", this.message);
            json.put("errorHash", this.errorHash);
            json.put("where", this.where);
            json.put("rooted", this.rooted);
            json.put("gpsStatus", this.gpsStatus.toString());
            json.put("msFromStart", this.msFromStart);
            json.put("breadcrumbs", this.breadcrumbs);
            json.put("memSysLow", this.memSysLow);
            if (!this.handled.booleanValue()) {
                json.put("memSysTotal", this.memSysTotal);
                json.put("memSysAvailable", this.memSysAvailable);
            }
            json.put("memSysThreshold", this.memSysThreshold);
            json.put("memAppMax", this.memAppMax);
            json.put("memAppAvailable", this.memAppAvailable);
            json.put("memAppTotal", this.memAppTotal);
            if (Properties.SEND_LOG) {
                json.put("log", Utils.readLogs());
            } else {
                json.put("log", "NA");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString() + Properties.getSeparator(EnumActionType.error);
    }

    public void send(NetSender netSender, boolean saveOnFail) {
        netSender.send(toJsonLine(), saveOnFail);
    }

    public void save(DataSaver dataSaver) {
        new DataSaver().save(toJsonLine());
    }

    public void send(Context ctx, NetSender netSender, boolean saveOnFail) {
        netSender.send(toJsonLine(), saveOnFail);
    }
}
