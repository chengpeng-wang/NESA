package com.baidu.android.pushservice.message;

import com.baidu.android.common.logging.Log;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.b;
import org.json.JSONException;
import org.json.JSONObject;

public final class i {
    public static PublicMsg a(String str, String str2, byte[] bArr) {
        boolean z = true;
        PublicMsg publicMsg = new PublicMsg();
        publicMsg.a = str;
        publicMsg.b = str2;
        try {
            JSONObject jSONObject = new JSONObject(new String(bArr));
            if (!jSONObject.isNull("title")) {
                publicMsg.c = jSONObject.getString("title");
            }
            if (!jSONObject.isNull("description")) {
                publicMsg.d = jSONObject.getString("description");
            }
            if (!jSONObject.isNull("url")) {
                publicMsg.e = jSONObject.getString("url");
            }
            if (!jSONObject.isNull("notification_builder_id")) {
                publicMsg.j = jSONObject.getInt("notification_builder_id");
            }
            if (!jSONObject.isNull(PushConstants.EXTRA_OPENTYPE)) {
                publicMsg.k = jSONObject.getInt(PushConstants.EXTRA_OPENTYPE);
            }
            if (!jSONObject.isNull("user_confirm")) {
                publicMsg.l = jSONObject.getInt("user_confirm");
            }
            if (!jSONObject.isNull("notification_basic_style")) {
                publicMsg.m = jSONObject.getInt("notification_basic_style");
            }
            if (!jSONObject.isNull("custom_content")) {
                String string = jSONObject.getString("custom_content");
                Log.d("PublicMsgBuilder", "custom_content=" + string);
                publicMsg.n = string;
            }
            if (!jSONObject.isNull("net_support")) {
                publicMsg.i = jSONObject.getInt("net_support");
            }
            if (!jSONObject.isNull("app_situation")) {
                JSONObject jSONObject2 = jSONObject.getJSONObject("app_situation");
                if (jSONObject2.getInt("as_is_support") != 1) {
                    z = false;
                }
                publicMsg.p = z;
                publicMsg.o = jSONObject2.getString("as_pkg_name");
            }
            if (!jSONObject.isNull(PushConstants.PACKAGE_NAME)) {
                publicMsg.f = jSONObject.getString(PushConstants.PACKAGE_NAME);
            }
            if (!jSONObject.isNull("pkg_vercode")) {
                publicMsg.g = jSONObject.getInt("pkg_vercode");
            }
            if (jSONObject.isNull("pkg_content")) {
                return publicMsg;
            }
            publicMsg.h = jSONObject.getString("pkg_content");
            return publicMsg;
        } catch (JSONException e) {
            if (b.a()) {
                Log.e("PublicMsgBuilder", "Public Message Parsing Fail:\r\n" + e.getMessage());
            }
            return null;
        }
    }
}
