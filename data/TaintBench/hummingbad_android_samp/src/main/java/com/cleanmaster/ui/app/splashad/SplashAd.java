package com.cleanmaster.ui.app.splashad;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.cleanmaster.common.a;
import com.cleanmaster.ui.app.market.Ad;
import com.cleanmaster.util.v;
import com.picksinit.ClickAdFinishListener;
import com.picksinit.ErrorInfo;
import com.picksinit.ICallBack;
import com.picksinit.PicksConfig;
import com.picksinit.c;
import com.picksinit.goGp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SplashAd {
    private SplashAdActionListener mActionListener;
    /* access modifiers changed from: private */
    public Ad mAd;
    /* access modifiers changed from: private */
    public Bitmap mBitmap;
    private SplashAdConfig mConfig;
    /* access modifiers changed from: private */
    public Context mContext;
    private ImageDownloadListener mImageListener;
    private boolean mIsAction = false;
    private boolean mIsOutofTime = false;
    private OnClickListener mOnclickListener;

    class MyPicksLoadCallback implements ICallBack {
        private MyPicksLoadCallback() {
        }

        /* synthetic */ MyPicksLoadCallback(SplashAd x0, AnonymousClass1 x1) {
            this();
        }

        public void onLoadSuccess(List listAd) {
            if (listAd == null || listAd.isEmpty()) {
                SplashAd.this.actionFailed(SplashAdConfig.ERRORCODE_NO_FILL);
                return;
            }
            for (Ad ad : listAd) {
                if (SplashAd.this.isAdValid(ad)) {
                    SplashAd.this.mAd = ad;
                    SplashAd.this.loadAdImage(ad);
                    if (SplashAd.this.isSendViewToOut(ad)) {
                        SplashAd.this.provideAdView();
                        return;
                    }
                    return;
                }
            }
            SplashAd.this.actionFailed(SplashAdConfig.ERRORCODE_AD_ALL_INVALID);
        }

        public void onLoadError(ErrorInfo info) {
            SplashAd.this.actionFailed(info.toString());
        }

        public void onPreExecute() {
        }
    }

    public SplashAd(SplashAdConfig config, SplashAdActionListener listener, ImageDownloadListener imageListener) {
        if (config == null || listener == null || imageListener == null) {
            throw new RuntimeException("configure cannot be null");
        }
        this.mContext = c.getInstance().getContext();
        this.mConfig = config;
        this.mActionListener = listener;
        this.mImageListener = imageListener;
        this.mOnclickListener = new OnClickListener() {
            public void onClick(View v) {
                SplashAd.this.actionClicked();
            }
        };
    }

    public void updateConfig(Map items) {
        if (items != null && !items.isEmpty()) {
            for (Entry entry : items.entrySet()) {
                String str = (String) entry.getKey();
                Object value = entry.getValue();
                if (SplashAdConfig.CONFIGKEY_POSID.equals(str)) {
                    this.mConfig.setPosid(value);
                } else if (SplashAdConfig.CONFIGKEY_CACHETIME.equals(str)) {
                    this.mConfig.setCacheTime(value);
                } else if (SplashAdConfig.CONFIGKEY_SHOWTIME.equals(str)) {
                    this.mConfig.setShowTime(value);
                } else if (SplashAdConfig.CONFIGKEY_WAITTIME.equals(str)) {
                    this.mConfig.setWaitTime(value);
                }
            }
        }
    }

    public void clean() {
        this.mAd = null;
        this.mBitmap = null;
        this.mActionListener = null;
        this.mOnclickListener = null;
        this.mImageListener = null;
    }

    public void loadSplashAd() {
        if (this.mImageListener == null) {
            actionFailed(SplashAdConfig.ERRORCODE_IMAGELOADER);
        }
        this.mIsAction = false;
        if (!isAdValid(this.mAd) || this.mBitmap == null) {
            this.mIsOutofTime = false;
            startLoadTimer();
        } else {
            provideAdView();
            this.mIsOutofTime = true;
        }
        preLoadSplashAd();
    }

    public void preLoadSplashAd() {
        updatePicksConfig();
        c.getInstance().loadad(this.mConfig.getPosid(), new MyPicksLoadCallback(this, null), 10, ((this.mConfig.getCacheTime() * 60) * 60) * 1000);
    }

    public void actionShowed() {
        if (this.mAd != null) {
            c.getInstance().showReport(this.mContext, this.mConfig.getPosid(), this.mAd);
        }
        startShowTimer();
    }

    private void updatePicksConfig() {
        HashMap hashMap = new HashMap();
        HashSet hashSet = new HashSet();
        Set set = c.getInstance().getConfig().filter_show_type;
        if (set == null) {
            hashSet.add(Integer.valueOf(this.mConfig.getmAppShowType()));
        } else {
            hashSet.addAll(set);
            hashSet.add(Integer.valueOf(this.mConfig.getmAppShowType()));
        }
        hashMap.put(PicksConfig.KEY_SHOW_TYPE, hashSet);
        c.getInstance().updateConfig(hashMap);
    }

    /* access modifiers changed from: private */
    public void loadAdImage(Ad ad) {
        if (isAdValid(ad) && this.mImageListener != null) {
            this.mImageListener.getBitmap(ad.getBackground(), new BitmapListener() {
                public void onFailed(String errorCode) {
                    SplashAd.this.actionFailed(errorCode);
                }

                public void onSuccessed(Bitmap bitmap) {
                    if (bitmap != null) {
                        SplashAd.this.mBitmap = bitmap;
                    } else {
                        SplashAd.this.actionFailed(SplashAdConfig.ERRORCODE_IMAGE_NULL);
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public boolean isSendViewToOut(Ad ad) {
        if (this.mIsOutofTime || !isAdValid(ad) || this.mBitmap == null) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void provideAdView() {
        View constructAdView = constructAdView();
        if (constructAdView != null) {
            constructAdView.setVisibility(0);
            actionLoaded(constructAdView);
        }
    }

    private View constructAdView() {
        ImageView createAdView = createAdView();
        if (this.mBitmap == null || createAdView == null) {
            return null;
        }
        double height = ((double) this.mBitmap.getHeight()) / ((double) this.mBitmap.getWidth());
        int d = a.d(this.mContext);
        FrameLayout createFramLayout = createFramLayout(d, (int) (height * ((double) d)));
        createFramLayout.addView(createAdView);
        createFramLayout.bringToFront();
        return createFramLayout;
    }

    private ImageView createAdView() {
        ImageView imageView = new ImageView(this.mContext);
        if (this.mBitmap == null) {
            return null;
        }
        imageView.setScaleType(ScaleType.FIT_XY);
        imageView.setImageBitmap(this.mBitmap);
        imageView.setOnClickListener(this.mOnclickListener);
        imageView.bringToFront();
        return imageView;
    }

    private FrameLayout createFramLayout(int width, int height) {
        FrameLayout frameLayout = new FrameLayout(this.mContext);
        LayoutParams layoutParams = frameLayout.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new LayoutParams(width, height);
        } else {
            layoutParams.width = width;
            layoutParams.height = height;
        }
        frameLayout.setLayoutParams(layoutParams);
        return frameLayout;
    }

    private void startLoadTimer() {
        v.a(new Runnable() {
            public void run() {
                SplashAd.this.actionRequestTimeout();
            }
        }, this.mConfig.getWaitTime());
    }

    private void startShowTimer() {
        v.a(new Runnable() {
            public void run() {
                SplashAd.this.actionShowedTimeout();
            }
        }, (long) (this.mConfig.getShowTime() * 1000));
    }

    /* access modifiers changed from: private */
    public void actionClicked() {
        if (this.mActionListener != null) {
            this.mActionListener.onClicked();
        }
        c.getInstance().onClickAdNoDialog(this.mContext, this.mConfig.getPosid(), this.mAd, new ClickAdFinishListener() {
            public void onClickFinish(goGp g) {
                if (g != null) {
                    g.goGp(SplashAd.this.mContext);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void actionFailed(String paramString) {
        if (this.mActionListener != null && !this.mIsAction) {
            this.mIsAction = true;
            this.mActionListener.onFailed(paramString);
        }
    }

    private void actionLoaded(View paramView) {
        if (this.mActionListener != null && !this.mIsAction) {
            this.mIsAction = true;
            this.mActionListener.onLoaded(paramView);
        }
    }

    /* access modifiers changed from: private */
    public void actionShowedTimeout() {
        if (this.mActionListener != null) {
            this.mActionListener.onShowedFinish();
        }
    }

    /* access modifiers changed from: private */
    public void actionRequestTimeout() {
        actionFailed(SplashAdConfig.ERRORCODE_TIMEOUT);
        this.mIsOutofTime = true;
    }

    /* access modifiers changed from: private */
    public boolean isAdValid(Ad ad) {
        return (ad == null || ad.getAppShowType() != this.mConfig.getmAppShowType() || !ad.isAvailAble() || ad.isShowed() || TextUtils.isEmpty(ad.getBackground())) ? false : true;
    }
}
