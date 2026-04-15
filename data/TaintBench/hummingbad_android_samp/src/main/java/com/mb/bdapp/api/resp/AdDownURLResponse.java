package com.mb.bdapp.api.resp;

import com.mb.bdapp.db.DuAd;
import org.json.JSONException;
import org.json.JSONObject;

public class AdDownURLResponse {
    private String downurl;
    private String message;
    private int status = -1;

    public AdDownURLResponse(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.isNull(DuAd.DB_STATUS)) {
            this.message = "接口返回异常";
        } else {
            this.status = jsonObject.optInt(DuAd.DB_STATUS);
        }
        this.message = jsonObject.optString("msg");
        this.downurl = jsonObject.optString(DuAd.DB_DOWN_URL);
    }

    public String getDownurl() {
        return this.downurl;
    }

    public int getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }
}
