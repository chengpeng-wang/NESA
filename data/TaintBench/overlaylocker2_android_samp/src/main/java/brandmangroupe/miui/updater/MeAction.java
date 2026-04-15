package brandmangroupe.miui.updater;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MeAction {
    Context mContext;

    MeAction(Context c) {
        this.mContext = c;
    }

    public void SetBoot(String cmd) {
        Set(cmd, "1", "Boot_conf");
    }

    public String ListBoot() throws JSONException {
        return List("Boot_conf");
    }

    public void SetNetwork(String cmd) {
        Set(cmd, "1", "Network_conf");
    }

    public String ListNetwork() throws JSONException {
        return List("Network_conf");
    }

    public void SetPower(String cmd) {
        Set(cmd, "1", "Power_conf");
    }

    public String ListPower() throws JSONException {
        return List("Power_conf");
    }

    public void SetCall(String cmd) {
        Set(cmd, "1", "Call_conf");
    }

    public String ListCall() throws JSONException {
        return List("Call_conf");
    }

    public void SetSMS(String cmd) {
        Set(cmd, "1", "SMS_conf");
    }

    public String ListSMS() throws JSONException {
        return List("SMS_conf");
    }

    public void SetStart(String cmd, String packagename) {
        Set(cmd, packagename, "Start_conf");
    }

    public String ListStart() throws JSONException {
        return List("Start_conf");
    }

    public void SetTimeout(String cmd, String time) {
        Set(cmd, time, "Timeout_conf");
    }

    public void SetTimer(String cmd, String time) {
        Set(cmd, time, "Timers_conf");
    }

    public void SetCmd(String cmd, String string) {
        Set(cmd, string, "Cmd_conf");
    }

    public String ListCmd() throws JSONException {
        return List("Cmd_conf");
    }

    public String ListTimeout() throws JSONException {
        return List("Timeout_conf");
    }

    public String ListTimers() throws JSONException {
        return List("Timers_conf");
    }

    public void Set(String cmd, String string, String ff) {
        Editor editor = this.mContext.getSharedPreferences(ff, 0).edit();
        editor.putString(cmd, string);
        editor.commit();
    }

    public void Del(String cmd, String ff) {
        Editor editor = this.mContext.getSharedPreferences(ff, 0).edit();
        editor.remove(cmd);
        editor.commit();
    }

    public String List(String ff) throws JSONException {
        SharedPreferences settings = this.mContext.getSharedPreferences(ff, 0);
        JSONObject jsonObj = new JSONObject();
        JSONArray jsonArr = new JSONArray();
        Map<String, ?> items = settings.getAll();
        if (items.size() > 0) {
            for (String s : items.keySet()) {
                JSONObject pnObj = new JSONObject();
                pnObj.put(s, items.get(s));
                jsonArr.put(pnObj);
            }
        }
        jsonObj.put("result", jsonArr);
        return jsonObj.toString();
    }
}
