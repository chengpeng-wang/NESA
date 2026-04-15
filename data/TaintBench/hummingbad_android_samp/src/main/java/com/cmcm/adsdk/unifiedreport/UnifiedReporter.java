package com.cmcm.adsdk.unifiedreport;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.cmcm.adsdk.CMAdManager;
import com.cmcm.adsdk.utils.a;
import com.cmcm.adsdk.utils.b;
import com.cmcm.adsdk.utils.c;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UnifiedReporter {
    private static final int AC_SHOW = 1;
    private static final int DEFAULT_TIME_OUT = 1000;
    private static final int NET_RETY_TIMES = 3;
    private static UnifiedReporter sSelf;
    private boolean DEG = false;
    private final String TAG = "UnifiedReporter";
    private String mConstantParam = "";
    /* access modifiers changed from: private */
    public Context mContext = null;
    private volatile boolean mIsInit = false;
    private String mReportUrl = "";

    public static UnifiedReporter getInstance() {
        if (sSelf == null) {
            synchronized (UnifiedReporter.class) {
                if (sSelf == null) {
                    sSelf = new UnifiedReporter();
                }
            }
        }
        return sSelf;
    }

    public UnifiedReporter() {
        if (!this.mIsInit) {
            this.mContext = CMAdManager.getContext().getApplicationContext();
            this.mReportUrl = getReportUrl();
            this.mConstantParam = getConstantParam();
            this.mIsInit = true;
        }
    }

    public void reportShow(int posid) {
        reportShow(posid, "");
    }

    public synchronized void reportShow(int posid, String extra) {
        boolean z = this.mIsInit;
        if (this.mIsInit) {
            StringBuffer stringBuffer = new StringBuffer(this.mReportUrl);
            stringBuffer.append("ac=").append(1);
            stringBuffer.append("&posid").append(MASTNativeAdConstants.EQUAL).append(posid);
            if (!TextUtils.isEmpty(extra)) {
                stringBuffer.append("&extra=").append(extra);
            }
            stringBuffer.append(MASTNativeAdConstants.AMPERSAND).append(getVariabletParam());
            stringBuffer.append(MASTNativeAdConstants.AMPERSAND).append(this.mConstantParam);
            report(stringBuffer.toString());
        }
    }

    private void report(final String url) {
        a.a.post(new Runnable() {
            public final void run() {
                if (b.b(UnifiedReporter.this.mContext)) {
                    a.a(url, 1000, 3);
                }
            }
        });
    }

    private String getReportUrl() {
        if (CMAdManager.isChinaVersion()) {
            return "http://ud.mobad.ijinshan.com/r/?";
        }
        return "http://ud.adkmob.com/r/?";
    }

    private String getConstantParam() {
        String str = Build.MODEL;
        try {
            str = URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("pid=").append(CMAdManager.getMid());
        stringBuffer.append("&intl").append("=2");
        stringBuffer.append("&aid").append(MASTNativeAdConstants.EQUAL).append(c.b(this.mContext));
        stringBuffer.append("&resolution").append(MASTNativeAdConstants.EQUAL).append(c.c(this.mContext));
        stringBuffer.append("&brand").append(MASTNativeAdConstants.EQUAL).append(Build.BRAND);
        stringBuffer.append("&model").append(MASTNativeAdConstants.EQUAL).append(str);
        stringBuffer.append("&vercode").append(MASTNativeAdConstants.EQUAL).append(c.f(this.mContext));
        stringBuffer.append("&mcc").append(MASTNativeAdConstants.EQUAL).append(c.e(this.mContext));
        stringBuffer.append("&cn").append(MASTNativeAdConstants.EQUAL).append(CMAdManager.getChannelId());
        stringBuffer.append("&os").append(MASTNativeAdConstants.EQUAL).append(VERSION.RELEASE);
        return stringBuffer.toString();
    }

    private String getVariabletParam() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("cl=").append(c.d(this.mContext));
        stringBuffer.append("&nt").append(MASTNativeAdConstants.EQUAL).append(getNetType());
        return stringBuffer.toString();
    }

    private int getNetType() {
        if (!b.b(this.mContext)) {
            return 0;
        }
        if (b.a(this.mContext)) {
            return 1;
        }
        if (b.c(this.mContext)) {
            return 2;
        }
        return 0;
    }
}
