package com.cmcm.adsdk.nativead;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import com.cleanmaster.ui.app.market.Ad;
import com.cmcm.adsdk.Const;
import com.cmcm.adsdk.utils.FaceBookInfomation;
import com.cmcm.adsdk.utils.b;
import com.cmcm.baseapi.ads.INativeAd;
import com.cmcm.baseapi.ads.INativeAd.IClickPreHanleListener;
import com.cmcm.baseapi.ads.INativeAd.ImpressionListener;
import com.cmcm.baseapi.ads.INativeAd.InnerClickListener;
import com.facebook.ads.NativeAd;
import com.picksinit.c;
import java.util.List;

public abstract class CMNativeAd implements INativeAd {
    public static final int DEFAULT_LOAD_SIZE = 1;
    public static final String KEY_APP_ID = "appid";
    public static final String KEY_CACHE_TIME = "cache_time";
    public static final String KEY_JUHE_POSID = "juhe_posid";
    public static final String KEY_LOAD_SIZE = "load_size";
    public static final String KEY_PLACEMENT_ID = "placementid";
    public static final String KEY_REPORT_PKGNAME = "report_pkg_name";
    public static final String KEY_REPORT_RES = "report_res";
    @Nullable
    private String mAdDescription;
    @Nullable
    private String mAdSocialContext;
    @Nullable
    private double mAdStartRate;
    @NonNull
    private long mCacheTime;
    @Nullable
    private String mCallToAction;
    private long mCreateTime = System.currentTimeMillis();
    private List<String> mExtPicks;
    private String mFBPlacementId;
    protected boolean mHasReportShow = false;
    @Nullable
    protected IClickPreHanleListener mIClickPreHanlerListener;
    @NonNull
    private String mIconImageUrl;
    @Nullable
    protected ImpressionListener mImpressionListener;
    /* access modifiers changed from: protected */
    @Nullable
    public InnerClickListener mInnerClickListener;
    @Nullable
    private Boolean mIsDownloadApp;
    @Nullable
    private boolean mIsPriority;
    @NonNull
    private String mMainImageUrl;
    @NonNull
    private String mPosid;
    private String mReportPkgName;
    private int mReportRes;
    @NonNull
    private String mTitle;
    @Nullable
    public OpenDegBrowserListener openDegBrowserListener;
    private String renderTime;
    private int renderType = -1;

    public interface GpsDialogPressInterface {
        void onCancelDownload();

        void onNeedDownload();
    }

    public interface OpenDegBrowserListener {
        void toGetADUrl(String str);
    }

    public void recordClick() {
        String adTypeName = getAdTypeName();
        if (Const.KEY_CM.equals(adTypeName) || Const.KEY_FB.equals(adTypeName)) {
            h.a();
            String juhePosid = getJuhePosid();
            int i = this.mReportRes;
            String str = this.mReportPkgName;
            Object adObject = getAdObject();
            String str2 = this.mFBPlacementId;
            if (adObject instanceof NativeAd) {
                c.getInstance().doFaceBookClickReport(str2, FaceBookInfomation.getRawJson(2, (NativeAd) adObject), str, juhePosid, i);
                return;
            }
            return;
        }
        h.a();
        adTypeName = getJuhePosid();
        int i2 = this.mReportRes;
        c.getInstance().doRecommendAdClickReport(this.mReportPkgName, adTypeName, i2);
    }

    public void recordImpression() {
        if (!this.mHasReportShow) {
            String adTypeName = getAdTypeName();
            if (Const.KEY_CM.equals(adTypeName) || Const.KEY_FB.equals(adTypeName)) {
                h.a();
                String juhePosid = getJuhePosid();
                int i = this.mReportRes;
                String str = this.mReportPkgName;
                Object adObject = getAdObject();
                String str2 = this.mFBPlacementId;
                if (adObject instanceof Ad) {
                    c.getInstance().showReport(c.getInstance().getContext(), juhePosid, (Ad) adObject);
                } else if (adObject instanceof NativeAd) {
                    c.getInstance().doFaceBookViewReport(str2, FaceBookInfomation.getRawJson(1, (NativeAd) adObject), str, juhePosid, i);
                }
            } else {
                h.a();
                adTypeName = getJuhePosid();
                int i2 = this.mReportRes;
                c.getInstance().doRecommendAdViewReport(this.mReportPkgName, adTypeName, i2);
            }
            this.mHasReportShow = true;
        }
        if (this.mImpressionListener != null) {
            this.mImpressionListener.onLoggingImpression();
        }
    }

    public void setReUseAd() {
        this.mHasReportShow = false;
    }

    public void setInnerClickListener(@Nullable InnerClickListener listener) {
        this.mInnerClickListener = listener;
    }

    public InnerClickListener getInnerClickListener() {
        return this.mInnerClickListener;
    }

    public void setImpressionListener(@Nullable ImpressionListener impressionListener) {
        this.mImpressionListener = impressionListener;
    }

    public void setClickPreHanlerListener(@Nullable IClickPreHanleListener hanler) {
        this.mIClickPreHanlerListener = hanler;
    }

    public void setOnClickToLBListener(@Nullable OpenDegBrowserListener openDegBrowserListener) {
        this.openDegBrowserListener = openDegBrowserListener;
    }

    public boolean isNativeAd() {
        return true;
    }

    public String getAdTitle() {
        return this.mTitle;
    }

    public String getAdCoverImageUrl() {
        return this.mMainImageUrl;
    }

    public String getAdIconUrl() {
        return this.mIconImageUrl;
    }

    public String getAdSocialContext() {
        return this.mAdSocialContext;
    }

    public String getAdCallToAction() {
        return this.mCallToAction;
    }

    public boolean isNeedShowAdTag() {
        return true;
    }

    public Boolean isDownLoadApp() {
        return this.mIsDownloadApp;
    }

    public void setIsDownloadApp(@Nullable Boolean isDownloadApp) {
        this.mIsDownloadApp = isDownloadApp;
    }

    public void setTitle(@NonNull String mTitle) {
        this.mTitle = mTitle;
    }

    public void setAdCoverImageUrl(@NonNull String mainImageUrl) {
        this.mMainImageUrl = mainImageUrl;
    }

    public void setAdIconUrl(@NonNull String iconImageUrl) {
        this.mIconImageUrl = iconImageUrl;
    }

    public void setAdCallToAction(@Nullable String callToAction) {
        this.mCallToAction = callToAction;
    }

    public void setAdSocialContext(@Nullable String adSocialContext) {
        this.mAdSocialContext = adSocialContext;
    }

    public void setAdBody(@Nullable String adDescription) {
        this.mAdDescription = adDescription;
    }

    public String getAdBody() {
        return this.mAdDescription;
    }

    public void setJuhePosid(@NonNull String posid) {
        this.mPosid = posid;
    }

    public String getJuhePosid() {
        return this.mPosid;
    }

    public double getAdStarRating() {
        return this.mAdStartRate;
    }

    public void setAdStarRate(@Nullable double starRate) {
        this.mAdStartRate = starRate;
    }

    public void setCacheTime(long cacheTime) {
        this.mCacheTime = cacheTime;
    }

    public boolean isPriority() {
        return this.mIsPriority;
    }

    public void setIsPriority(@Nullable boolean isPriority) {
        this.mIsPriority = isPriority;
    }

    public void setExtPics(List<String> extPicks) {
        this.mExtPicks = extPicks;
    }

    public List<String> getExtPics() {
        return this.mExtPicks;
    }

    public boolean hasExpired() {
        return System.currentTimeMillis() - this.mCreateTime >= this.mCacheTime;
    }

    public void addClickListener(@NonNull View view, @NonNull OnClickListener onClickListener, @Nullable OnTouchListener touchListener) {
        if (view != null) {
            view.setOnClickListener(onClickListener);
            view.setOnTouchListener(touchListener);
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    View childAt = viewGroup.getChildAt(i);
                    childAt.setOnClickListener(onClickListener);
                    childAt.setOnTouchListener(touchListener);
                }
            }
        }
    }

    public void clearClickListener(@NonNull View view) {
        if (view != null) {
            view.setOnClickListener(null);
            view.setOnTouchListener(null);
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    View childAt = viewGroup.getChildAt(i);
                    childAt.setOnClickListener(null);
                    childAt.setOnTouchListener(null);
                }
            }
        }
    }

    public void setReportRes(@Nullable int res) {
        this.mReportRes = res;
    }

    public void setPlacementId(@Nullable String fBPlacementId) {
        this.mFBPlacementId = fBPlacementId;
    }

    public void setReportPkgName(@Nullable String pkgName) {
        this.mReportPkgName = pkgName;
    }

    public void showGPSDialog(Context context, final GpsDialogPressInterface gpsDialogPressInterface) {
        if (b.c(context)) {
            AlertDialog create = new Builder(context).setTitle("下载确认").setMessage("您现在是非wifi环境，是否下载：" + getAdTitle()).setPositiveButton("下载", new DialogInterface.OnClickListener() {
                public final void onClick(DialogInterface dialogInterface, int i) {
                    gpsDialogPressInterface.onNeedDownload();
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public final void onClick(DialogInterface dialog, int which) {
                    gpsDialogPressInterface.onCancelDownload();
                }
            }).create();
            if (com.cmcm.adsdk.utils.c.a() || com.cmcm.adsdk.utils.c.b() || com.cmcm.adsdk.utils.c.c()) {
                create.getWindow().setType(2005);
            } else {
                create.getWindow().setType(2003);
            }
            create.show();
            return;
        }
        gpsDialogPressInterface.onNeedDownload();
    }

    public void setRenderType(int type) {
        this.renderType = type;
    }

    public void setRenderTime(String renderTime) {
        this.renderTime = renderTime;
    }

    public String getRenderTime() {
        return this.renderTime;
    }

    public int getRenderType() {
        return this.renderType;
    }
}
