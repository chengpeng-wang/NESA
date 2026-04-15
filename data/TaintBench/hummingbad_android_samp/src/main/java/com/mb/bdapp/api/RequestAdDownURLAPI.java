package com.mb.bdapp.api;

import android.content.Context;
import android.text.TextUtils;
import com.mb.bdapp.api.resp.AdDownURLResponse;
import com.mb.bdapp.net.HttpParameters;
import com.mb.bdapp.net.RequestListener;
import com.mb.bdapp.util.LogUtil;
import java.io.IOException;
import org.json.JSONException;

public class RequestAdDownURLAPI extends AbsAPI {
    public static final String AD_PNAME = "ad_pname";
    private static final String SERVER_URL = "http://efget.hmapi.com:10091/fgeturl.do";
    /* access modifiers changed from: private|static|final */
    public static final String TAG = RequestAdDownURLAPI.class.getSimpleName();

    public interface APIListener {
        void onComplete(AdDownURLResponse adDownURLResponse);

        void onError(Exception exception);
    }

    public RequestAdDownURLAPI(Context mContext) {
        super(mContext);
    }

    public void requestAd(HttpParameters params, final APIListener listener) {
        request(SERVER_URL, params, "POST", new RequestListener() {
            public void onIOException(IOException e) {
                LogUtil.d(RequestAdDownURLAPI.TAG, "IOException=" + e.getLocalizedMessage());
                if (listener != null) {
                    listener.onError(e);
                }
            }

            public void onError(Exception e) {
                LogUtil.d(RequestAdDownURLAPI.TAG, "Exception=" + e.getLocalizedMessage());
                if (listener != null) {
                    listener.onError(e);
                }
            }

            public void onComplete(String response) {
                AdDownURLResponse adResponse = RequestAdDownURLAPI.this.parseJson(response);
                if (listener != null) {
                    listener.onComplete(adResponse);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public AdDownURLResponse parseJson(String response) {
        if (TextUtils.isEmpty(response)) {
            return null;
        }
        try {
            return new AdDownURLResponse(response);
        } catch (JSONException e) {
            LogUtil.e(TAG, e.getLocalizedMessage());
            return null;
        }
    }
}
