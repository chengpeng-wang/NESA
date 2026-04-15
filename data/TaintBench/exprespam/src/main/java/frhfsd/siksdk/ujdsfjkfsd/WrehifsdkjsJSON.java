package frhfsd.siksdk.ujdsfjkfsd;

import org.json.JSONException;
import org.json.JSONObject;

public class WrehifsdkjsJSON {
    private boolean jsonFlag = false;
    private String jsonString = "";
    private JSONObject jsons = null;

    public JSONObject getJsons() {
        return this.jsons;
    }

    public WrehifsdkjsJSON(String json) {
        try {
            this.jsonString = json;
            this.jsons = new JSONObject(this.jsonString);
            this.jsonFlag = true;
        } catch (JSONException e) {
            this.jsonFlag = false;
        }
    }

    public boolean isJSON() {
        return this.jsonFlag;
    }
}
