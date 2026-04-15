package com.nicon.tool;

import android.content.Context;
import com.qq.gdt.utils.Version;
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonResolve {
    private static final String ACTION = "action";
    private static final String GID = "gid";
    private static final String PID = "pid";
    private static JsonResolve jsonResolve;

    public static JsonResolve getInstance() {
        if (jsonResolve == null) {
            synchronized (NoTools.class) {
                if (jsonResolve == null) {
                    jsonResolve = new JsonResolve();
                }
            }
        }
        return jsonResolve;
    }

    public String postAdRecord(Context context) throws Exception {
        String resoult = "";
        ArrayList<NameValuePair> entitys = new ArrayList();
        entitys.addAll(getParameter(context));
        entitys.add(new BasicNameValuePair("data", getData()));
        resoult = HttpUtils.getInstance().httpPost(VariableUtils.URL.trim(), entitys);
        if ("1".equals(resoult)) {
            PreferencesUtls.setPostStaus(context, false);
        } else {
            PreferencesUtls.setPostStaus(context, true);
        }
        return resoult;
    }

    private ArrayList<NameValuePair> getParameter(Context context) {
        ArrayList<NameValuePair> entitys = new ArrayList();
        entitys.add(new BasicNameValuePair("mid", NoTools.getInstance().getMID(context)));
        entitys.add(new BasicNameValuePair("version", Version.NOICONVERSION));
        entitys.add(new BasicNameValuePair("pname", String.valueOf(NoTools.getInstance().getPackname(context))));
        entitys.add(new BasicNameValuePair(VariableUtils.APPNAME, String.valueOf(NoTools.getInstance().getAppname(context))));
        return entitys;
    }

    private String getData() {
        JSONArray recordsArray = new JSONArray();
        JSONObject record = new JSONObject();
        try {
            record.put(ACTION, "1");
            record.put(PID, "n");
            record.put("gid", "888");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        recordsArray.put(record);
        return recordsArray.toString();
    }
}
