package com.cleanmaster.ui.app.splashad;

import com.mopub.mobileads.resource.DrawableConstants.CtaButton;

public class SplashAdConfig {
    public static final String CONFIGKEY_CACHETIME = "configkey_cachetime";
    public static final String CONFIGKEY_POSID = "configkey_posid";
    public static final String CONFIGKEY_SHOWTIME = "configkey_showtime";
    public static final String CONFIGKEY_WAITTIME = "configkey_waittime";
    private static final int DEFAULT_CACHE_TIME = 2;
    private static final int DEFAULT_SHOW_TIME = 3;
    private static final int DEFAULT_SHOW_TYPE = 60004;
    private static final long DEFAULT_WAIT_TIME = 200;
    public static final String ERRORCODE_AD_ALL_INVALID = "ad is invalid";
    public static final String ERRORCODE_IMAGELOADER = "Image Loader is null";
    public static final String ERRORCODE_IMAGE_NULL = "request bitmap is null";
    public static final String ERRORCODE_NO_FILL = "no fill";
    public static final String ERRORCODE_TIMEOUT = "loading timeout";
    private int mAppShowType;
    private int mCacheTime;
    private int mPosid;
    private int mShowTime;
    private long mWaitTime;

    public SplashAdConfig(int posId, int cacheTime, int ShowTime, long waitTime, int showType) {
        this.mPosid = posId;
        this.mCacheTime = cacheTime;
        this.mShowTime = ShowTime;
        this.mWaitTime = waitTime;
        this.mAppShowType = showType;
        if (this.mPosid <= 0) {
            throw new RuntimeException("Splash Ad Config is Error !");
        }
        setDefaultValueIfCan();
    }

    private void setDefaultValueIfCan() {
        if (this.mWaitTime <= 0) {
            this.mWaitTime = DEFAULT_WAIT_TIME;
        }
        if (this.mCacheTime <= 0) {
            this.mCacheTime = 2;
        }
        if (this.mShowTime <= 0) {
            this.mShowTime = 3;
        }
        if (this.mAppShowType < 0) {
            this.mAppShowType = DEFAULT_SHOW_TYPE;
        }
    }

    public void setCacheTime(Object cacheTime) {
        this.mCacheTime = parseInt(cacheTime, 2);
    }

    public void setShowTime(Object showTime) {
        this.mShowTime = parseInt(showTime, 3);
    }

    public void setPosid(Object posid) {
        this.mPosid = parseInt(posid, -1);
        if (this.mPosid == -1) {
            throw new RuntimeException("Splash Ad Config is Error !");
        }
    }

    public void setWaitTime(Object waitTime) {
        this.mWaitTime = (long) parseInt(waitTime, CtaButton.WIDTH_DIPS);
    }

    public int getCacheTime() {
        return this.mCacheTime;
    }

    public int getShowTime() {
        return this.mShowTime;
    }

    public int getPosid() {
        return this.mPosid;
    }

    public long getWaitTime() {
        return this.mWaitTime;
    }

    public int getmAppShowType() {
        return this.mAppShowType;
    }

    private int parseInt(Object val, int defaultNum) {
        try {
            return Integer.parseInt((String) val);
        } catch (Exception e) {
            return defaultNum;
        }
    }
}
