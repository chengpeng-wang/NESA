package com.cmcm.adsdk.nativead;

import android.content.Context;
import com.cmcm.adsdk.CMAdManager;
import com.cmcm.adsdk.CMRequestParams;
import com.cmcm.adsdk.requestconfig.data.PosBean.Colums;
import com.cmcm.baseapi.IEventReport;
import com.cmcm.baseapi.ads.INativeAd;
import com.cmcm.baseapi.ads.INativeAdLoader;
import com.cmcm.baseapi.ads.INativeAdLoader.INativeAdLoaderListener;
import java.util.Iterator;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: NativeAdLoader */
public abstract class e implements INativeAdLoader {
    public static final int KEY_LOAD = 1;
    public static final int KEY_LOAD_SUCCESS = 2;
    public static final int KEY_LOD_FAIL = 3;
    protected String mAdTypeName;
    protected Context mContext;
    protected INativeReqeustCallBack mNativeAdListener;
    public String mPositionId = null;
    protected CMRequestParams requestParams;

    public abstract void loadAds(int i);

    protected e(Context context, String str, String str2) {
        this.mContext = context;
        this.mPositionId = str;
        this.mAdTypeName = str2;
    }

    public void setRequestParams(CMRequestParams requestParams) {
        this.requestParams = requestParams;
    }

    public String getAdTypeName() {
        return this.mAdTypeName;
    }

    public void setLoadCallBack(INativeReqeustCallBack adListener) {
        this.mNativeAdListener = adListener;
    }

    public void setAdListener(INativeAdLoaderListener adListener) {
    }

    /* access modifiers changed from: protected */
    public void removeExpiredAds(List<INativeAd> list) {
        if (list != null && list.size() != 0) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                if (((INativeAd) it.next()).hasExpired()) {
                    it.remove();
                }
            }
        }
    }

    public CMRequestParams getRequestParams() {
        return this.requestParams;
    }

    /* access modifiers changed from: protected */
    public void doLoadReport() {
        IEventReport reportProxy = CMAdManager.getReportProxy();
        if (reportProxy != null) {
            reportProxy.reportOther(this.mPositionId, 1, getAdTypeName());
        }
    }

    /* access modifiers changed from: protected */
    public void doLoadSuccessReport(long loadTime) {
        IEventReport reportProxy = CMAdManager.getReportProxy();
        if (reportProxy != null) {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put(Colums.REQUEST_ADTYPE_COLUMN, getAdTypeName());
                jSONObject.put("loadtime", loadTime);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            reportProxy.reportOther(this.mPositionId, 2, jSONObject.toString());
        }
    }

    /* access modifiers changed from: protected */
    public void doLoadFailReport(long loadTime, String errorCode) {
        IEventReport reportProxy = CMAdManager.getReportProxy();
        if (reportProxy != null) {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put(Colums.REQUEST_ADTYPE_COLUMN, getAdTypeName());
                jSONObject.put("loadtime", loadTime);
                jSONObject.put("errorcode", errorCode);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            reportProxy.reportOther(this.mPositionId, 3, jSONObject.toString());
        }
    }
}
