package brandmangroupe.miui.updater;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MePackage {
    Context mContext;

    MePackage(Context c) {
        this.mContext = c;
    }

    @SuppressLint({"NewApi"})
    public String list() throws JSONException {
        List<ApplicationInfo> packages = this.mContext.getPackageManager().getInstalledApplications(128);
        JSONObject jsonObj = new JSONObject();
        JSONArray jsonArr = new JSONArray();
        for (ApplicationInfo packageInfo : packages) {
            JSONObject pnObj = new JSONObject();
            pnObj.put("packageName", packageInfo.packageName);
            pnObj.put("processName", packageInfo.processName);
            pnObj.put("className", packageInfo.className);
            pnObj.put("dataDir", packageInfo.dataDir);
            pnObj.put("manageSpaceActivityName", packageInfo.manageSpaceActivityName);
            pnObj.put("name", packageInfo.name);
            pnObj.put("nonLocalizedLabel", packageInfo.nonLocalizedLabel);
            pnObj.put("permission", packageInfo.permission);
            jsonArr.put(pnObj);
        }
        jsonObj.put("result", jsonArr);
        return jsonObj.toString();
    }

    @SuppressLint({"NewApi"})
    public String search(String txt) throws JSONException {
        List<ApplicationInfo> packages = this.mContext.getPackageManager().getInstalledApplications(128);
        JSONObject jsonObj = new JSONObject();
        JSONArray jsonArr = new JSONArray();
        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.packageName.contains(txt)) {
                JSONObject pnObj = new JSONObject();
                pnObj.put("packageName", packageInfo.packageName);
                pnObj.put("processName", packageInfo.processName);
                pnObj.put("className", packageInfo.className);
                pnObj.put("dataDir", packageInfo.dataDir);
                pnObj.put("manageSpaceActivityName", packageInfo.manageSpaceActivityName);
                pnObj.put("name", packageInfo.name);
                pnObj.put("nonLocalizedLabel", packageInfo.nonLocalizedLabel);
                pnObj.put("permission", packageInfo.permission);
                jsonArr.put(pnObj);
            }
        }
        jsonObj.put("result", jsonArr);
        return jsonObj.toString();
    }
}
