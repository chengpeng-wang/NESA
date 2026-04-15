package com.umeng.analytics.onlineconfig;

import com.umeng.analytics.a;
import java.util.Locale;
import org.json.JSONObject;
import u.aly.aj;
import u.aly.aq;

/* compiled from: OnlineConfigResponse */
public class b extends aq {
    public JSONObject a = null;
    boolean b = false;
    int c = -1;
    int d = -1;
    String e;
    private final String f = "config_update";
    private final String g = "report_policy";
    private final String h = "online_params";
    private final String i = "last_config_time";
    private final String j = "report_interval";

    public b(JSONObject jSONObject) {
        super(jSONObject);
        if (jSONObject != null) {
            a(jSONObject);
            a();
        }
    }

    private void a(JSONObject jSONObject) {
        try {
            if (jSONObject.has("config_update") && !jSONObject.getString("config_update").toLowerCase(Locale.US).equals("no")) {
                if (jSONObject.has("report_policy")) {
                    this.c = jSONObject.getInt("report_policy");
                    this.d = jSONObject.optInt("report_interval") * 1000;
                    this.e = jSONObject.optString("last_config_time");
                } else {
                    aj.e(a.e, " online config fetch no report policy");
                }
                this.a = jSONObject.optJSONObject("online_params");
                this.b = true;
            }
        } catch (Exception e) {
            aj.e(a.e, "fail to parce online config response", e);
        }
    }

    private void a() {
        if (this.c < 0 || this.c > 6) {
            this.c = 1;
        }
    }
}
