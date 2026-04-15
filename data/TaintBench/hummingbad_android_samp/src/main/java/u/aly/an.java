package u.aly;

import com.mb.bdapp.db.DuAd;
import org.json.JSONObject;

/* compiled from: ReportResponse */
public class an extends aq {
    public a a;

    /* compiled from: ReportResponse */
    public enum a {
        SUCCESS,
        FAIL
    }

    public an(JSONObject jSONObject) {
        super(jSONObject);
        if ("ok".equalsIgnoreCase(jSONObject.optString(DuAd.DB_STATUS)) || "ok".equalsIgnoreCase(jSONObject.optString("success"))) {
            this.a = a.SUCCESS;
        } else {
            this.a = a.FAIL;
        }
    }
}
