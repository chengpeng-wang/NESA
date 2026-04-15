package com.picksinit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.cleanmaster.down.a;
import com.cleanmaster.gaid.AdvertisingIdHelper;
import com.cleanmaster.ui.app.market.Ad;
import com.cleanmaster.ui.app.market.transport.b;
import com.cleanmaster.util.ReceiverUtils;
import com.cleanmaster.util.f;
import com.cleanmaster.util.p;
import com.cleanmaster.util.v;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* compiled from: PicksMobBase */
public abstract class c {
    public static final String TAG = "picks sdk";
    private static long mCacheTime = 900000;
    private static c mInstance;
    private int mAdResourceRp = 1;
    private a mBrowserConfig;
    private String mChannelId;
    private final PicksConfig mConfig = new PicksConfig();
    private Context mContext;
    private int mCver;
    private boolean mIsCnVersion;
    private boolean mIsInit = false;
    private String mMid;
    private IPicksCallBack mPicksCallBack;
    private int mSSPId = -1;

    public abstract void clearParse302Url(int i);

    public abstract void doFaceBookClickReport(String str, String str2, String str3, String str4, int i);

    public abstract void doFaceBookViewReport(String str, String str2, String str3, String str4, int i);

    public abstract void downloadSuccessReport(String str, Ad ad, String str2);

    public abstract a getExtraDownLoader();

    public abstract boolean ishaveInternalDown();

    public abstract void preLoad302Ad(Ad ad, int i);

    public abstract void reportDowned(String str);

    public abstract void reportInstall(String str);

    public abstract void setDownLoader(a aVar);

    public static c getInstance() {
        if (mInstance == null) {
            synchronized (c.class) {
                if (mInstance == null) {
                    mInstance = new PicksMob();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context, String mid, String channelId, boolean isCnVersion) {
        this.mContext = context.getApplicationContext();
        this.mChannelId = channelId;
        this.mMid = mid;
        this.mIsCnVersion = isCnVersion;
        this.mIsInit = true;
        AdvertisingIdHelper.getInstance().asyncGetGAId();
        freshConfig();
        ReceiverUtils.a(this.mContext);
    }

    public void initUserAgent() {
        if (VERSION.SDK_INT >= 10) {
            b.a();
        }
    }

    private void freshConfig() {
        if (com.cleanmaster.ui.app.market.b.a("config_last_save_time", (long) com.umeng.analytics.a.m)) {
            new Thread(new d(this)).start();
        }
    }

    public void clearCacheTime(String posid) {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences("market_config", 0);
        if (VERSION.SDK_INT >= 9) {
            sharedPreferences.edit().putLong(posid + "_cache_time", 0).apply();
        } else {
            sharedPreferences.edit().putLong(posid + "_cache_time", 0).commit();
        }
    }

    public void loadad(int posid, ICallBack cb, int adn, int width, int height, int cacheTime) {
        loadAd(0, posid, cb, adn, cacheTime, width, height, false);
    }

    public void loadad(int pageNum, int posid, ICallBack cb, int adn, int cacheTime) {
        loadAd(pageNum, posid, cb, adn, cacheTime, 0, 0, false);
    }

    public void loadad(int posid, ICallBack cb, int adn, int cacheTime) {
        loadAd(0, posid, cb, adn, cacheTime, 0, 0, false);
    }

    private void loadAd(int pageNum, int posid, ICallBack cb, int adn, int cacheTime, int width, int height, boolean isEnforceLoadFromRemote) {
        f.a(this.mIsInit, "you not init picks sdk, please excete init sdk first");
        if (adn <= 0 || adn > 30 || pageNum < 0) {
            if (cb != null) {
                cb.onLoadError(new ErrorInfo(ErrorInfo.ERROR_CODE_PARAMS, "params is error", posid));
            }
        } else if (com.cleanmaster.common.a.c(this.mContext)) {
            e eVar = new e(this, pageNum, adn, String.valueOf(posid), cb, posid, pageNum, adn, cacheTime, width, height);
            if (cacheTime > 0) {
                eVar.a(cacheTime);
            }
            if (isEnforceLoadFromRemote) {
                eVar.k();
            }
            eVar.c((Object[]) new Void[0]);
        } else if (cb != null) {
            cb.onLoadError(new ErrorInfo(ErrorInfo.ERROR_CODE_NO_NETWORK, "netWork is unable", posid));
        }
    }

    /* access modifiers changed from: private */
    public void loadFromNet(int pageNum, int posid, ICallBack cb, int adn, int cacheTime, int width, int height) {
        loadAd(pageNum, posid, cb, adn, cacheTime, width, height, true);
    }

    private void filterWebView(List list) {
        if (list != null && list.size() > 0) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                Ad ad = (Ad) it.next();
                if (ad == null || (!TextUtils.isEmpty(ad.getHtml()) && TextUtils.isEmpty(ad.getPicUrl()))) {
                    it.remove();
                }
            }
        }
    }

    private void filterShowType(List list, Set notFilter) {
        if (list != null && list.size() > 0) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                Ad ad = (Ad) it.next();
                if (ad == null) {
                    it.remove();
                } else if (notFilter != null) {
                    if (!(notFilter.contains(Integer.valueOf(ad.getAppShowType())) || ad.getAppShowType() == 50000 || ad.getAppShowType() == Ad.SHOW_TYPE_NO_PIC_SMALL_CARD || ad.getAppShowType() == 0 || ad.getAppShowType() == 1016)) {
                        it.remove();
                    }
                } else if (!(ad.getAppShowType() == 50000 || ad.getAppShowType() == Ad.SHOW_TYPE_NO_PIC_SMALL_CARD || ad.getAppShowType() == 0 || ad.getAppShowType() == 1016)) {
                    it.remove();
                }
            }
        }
    }

    private void filterMtType(List list) {
        if (list != null && list.size() > 0) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                Ad ad = (Ad) it.next();
                if (ad == null) {
                    it.remove();
                } else if (!ad.isMtTypeAvail()) {
                    it.remove();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void filter(List list) {
        filterMtType(list);
        filterShowType(list, this.mConfig.filter_show_type);
        if (!this.mConfig.enable_webview) {
            filterWebView(list);
        }
    }

    /* access modifiers changed from: private */
    public void filterShowed(List list) {
        if (list != null && list.size() > 0) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                if (((Ad) it.next()).isShowed()) {
                    it.remove();
                }
            }
        }
    }

    public void onClickAdNoDialog(Context context, String posid, Ad ad, ClickAdFinishListener listener) {
        if (!ad.isShowed()) {
            showReport(context, posid, ad);
        }
        com.cleanmaster.ui.app.market.c.a(context, posid, ad, null, true, listener);
    }

    public void showReport(Context context, String posId, Ad ad) {
        if (ad != null && !ad.isShowed()) {
            com.cleanmaster.ui.app.market.c.a(ad, posId, null);
            com.cleanmaster.ui.app.market.c.b(ad);
            ad.setShowed(true);
            v.a(new f(this, posId, ad));
        }
    }

    public void doRecommendAdClickReport(String pkgName, String posId, int res) {
        com.cleanmaster.ui.app.market.c.b(pkgName, posId, res);
    }

    public void doRecommendAdViewReport(String pkgName, String posid, int res) {
        com.cleanmaster.ui.app.market.c.a(pkgName, posid, res);
    }

    public void doInstallSuccessReport(String posid, Ad ad, String rf) {
        com.cleanmaster.ui.app.market.c.d(posid, ad, rf);
    }

    public void setSSPId(int sspId) {
        f.a(this.mIsInit, "please init picks sdk first");
        this.mSSPId = sspId;
    }

    public int getSSPId() {
        f.a(this.mIsInit, "please init picks sdk first");
        return this.mSSPId;
    }

    public String getUserAgent() {
        f.a(this.mIsInit, "please init picks sdk first");
        return b.b();
    }

    public void setUserAgent(String userAgent) {
        f.a(this.mIsInit, "please init picks sdk first");
        b.a(userAgent);
    }

    public void setCacheTime(long cacheTime) {
        f.a(this.mIsInit, "please init picks sdk first");
        mCacheTime = cacheTime;
    }

    public Context getContext() {
        f.a(this.mIsInit, "please init picks sdk first");
        return this.mContext;
    }

    public String getMid() {
        return this.mMid;
    }

    public int getIntMid() {
        try {
            return Integer.parseInt(getMid());
        } catch (Exception e) {
            return 0;
        }
    }

    public void setChannelId(String channelId) {
        this.mChannelId = channelId;
    }

    public String getChannelId() {
        return this.mChannelId;
    }

    public void updateConfig(Map items) {
        this.mConfig.updateConfig(items);
    }

    public PicksConfig getConfig() {
        return this.mConfig;
    }

    public boolean isDebug() {
        return this.mConfig.enable_debug;
    }

    public String getTestReportIp() {
        return this.mConfig.server_report;
    }

    public void setBrowserConfig(String url) {
        f.a(this.mIsInit, "please init picks sdk first");
        this.mBrowserConfig = new a(url);
    }

    public void setBrowserConfig(String url, Object js, String jsName) {
        this.mBrowserConfig = new a(url, js, jsName);
    }

    public a getBrowserConfig() {
        return this.mBrowserConfig;
    }

    public void setmAdResourceRp(int mAdResourceRp) {
    }

    public int getmAdResourceRp() {
        return this.mAdResourceRp;
    }

    public int getmCver() {
        return this.mCver;
    }

    public void setmCver(int cver) {
        this.mCver = cver;
    }

    public boolean isCnVersion() {
        return this.mIsCnVersion;
    }

    public void reportRender(Ad ad, String posid, String render, int type) {
        if (isNeedReport()) {
            com.cleanmaster.ui.app.market.c.a(ad, posid, render, type);
        }
    }

    private boolean isNeedReport() {
        if (p.a(com.cleanmaster.ui.app.market.b.g(), 10000)) {
            return true;
        }
        return false;
    }

    public void setPicksCallBack(IPicksCallBack callBack) {
        this.mPicksCallBack = callBack;
    }

    public IPicksCallBack getPicksCallBack() {
        return this.mPicksCallBack;
    }
}
