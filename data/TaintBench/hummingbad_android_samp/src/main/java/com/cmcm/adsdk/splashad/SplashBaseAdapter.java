package com.cmcm.adsdk.splashad;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import com.cmcm.adsdk.CMAdManager;
import com.cmcm.adsdk.requestconfig.data.PosBean.Colums;
import com.cmcm.adsdk.splashad.listener.ImageLoadListener;
import com.moceanmobile.mast.MASTNativeAdConstants;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class SplashBaseAdapter {
    protected static int a;

    public interface OnSplashAdapterResultListener {
        void onAdDismissed(String str);

        void onAdFailed(String str, String str2);

        void onAdPresent(String str);

        void onClicked(String str);
    }

    public abstract String a();

    public abstract void a(@NonNull Activity activity, @NonNull OnSplashAdapterResultListener onSplashAdapterResultListener, @NonNull ViewGroup viewGroup);

    public abstract void a(ImageLoadListener imageLoadListener);

    public abstract void b();

    public static void a(String str) {
        if (CMAdManager.getReportProxy() != null) {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put(Colums.REQUEST_ADTYPE_COLUMN, str);
            } catch (JSONException e) {
            }
            CMAdManager.getReportProxy().reportOther(String.valueOf(a), 1, jSONObject.toString());
        }
    }

    public static void b(String str) {
        if (CMAdManager.getReportProxy() != null) {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put(Colums.REQUEST_ADTYPE_COLUMN, str);
            } catch (JSONException e) {
            }
            CMAdManager.getReportProxy().reportOther(String.valueOf(a), 2, jSONObject.toString());
        }
    }

    public static void a(String str, String str2) {
        if (CMAdManager.getReportProxy() != null) {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put(Colums.REQUEST_ADTYPE_COLUMN, str);
                jSONObject.put(MASTNativeAdConstants.RESPONSE_ERROR, str2);
            } catch (JSONException e) {
            }
            CMAdManager.getReportProxy().reportOther(String.valueOf(a), 3, jSONObject.toString());
        }
    }
}
