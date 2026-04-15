package com.mb.bdapp.api;

import android.content.Context;
import com.mb.bdapp.net.AsyncHttpRunner;
import com.mb.bdapp.net.HttpParameters;
import com.mb.bdapp.net.RequestListener;
import com.mb.bdapp.util.AppInfoUtils;
import com.mb.bdapp.util.MobileInfoUtils;

public abstract class AbsAPI {
    private static final String API = "api";
    protected static final String HTTPMETHOD_GET = "GET";
    protected static final String HTTPMETHOD_POST = "POST";
    private static final String IMSI = "imsi";
    private static final String LANGUAGE = "language";
    private static final String MID = "mid";
    private static final String MODEL = "model";
    private static final String NETWORK_NAME_TYPE = "networktype";
    private static final String PLATFORM = "platform";
    private static final String PNAME = "pname";
    private static final String VERSION = "version";
    private Context mContext;

    public AbsAPI(Context mContext) {
        this.mContext = mContext;
    }

    /* access modifiers changed from: protected */
    public void request(String url, HttpParameters params, String httpMethod, RequestListener listener) {
        if (params == null) {
            params = new HttpParameters();
        }
        addNeedParameters(params);
        AsyncHttpRunner.request(url, params, httpMethod, listener);
    }

    private void addNeedParameters(HttpParameters params) {
        params.add("mid", getMID(this.mContext));
        params.add("pname", AppInfoUtils.getPackageName(this.mContext));
        params.add("version", AppInfoUtils.getVersion(this.mContext));
        params.add(MODEL, MobileInfoUtils.getModel());
        params.add(API, MobileInfoUtils.getAndroidVersion());
        params.add(LANGUAGE, MobileInfoUtils.getLanguage(this.mContext));
        params.add(IMSI, getIMSI(this.mContext));
        params.add(PLATFORM, "baidu");
        params.add(NETWORK_NAME_TYPE, MobileInfoUtils.getCurrentNetType(this.mContext));
    }

    private static String getMID(Context context) {
        String mMid = MobileInfoUtils.getDeviceId(context);
        if (mMid == null || mMid.trim().length() == 0 || mMid.matches("0+")) {
            return "000000000000000";
        }
        return mMid;
    }

    private static String getIMSI(Context context) {
        String mIMSI = MobileInfoUtils.getSubscriberId(context);
        if (mIMSI == null || mIMSI.trim().length() == 0 || mIMSI.matches("0+")) {
            return "000000000000000";
        }
        return mIMSI;
    }
}
