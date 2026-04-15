package com.duapps.ad.entity.strategy;

import android.view.View;
import com.duapps.ad.DuAdDataCallBack;
import com.duapps.ad.DuClickCallback;
import java.util.List;

public interface NativeAd {
    public static final int CHANNEL_TYPE_AM_CONTENT = 5;
    public static final int CHANNEL_TYPE_AM_INSTALL = 4;
    public static final int CHANNEL_TYPE_DL = 1;
    public static final int CHANNEL_TYPE_FB = 2;
    public static final int CHANNEL_TYPE_FL = 8;
    public static final int CHANNEL_TYPE_IM = 3;
    public static final int CHANNEL_TYPE_MP = 7;
    public static final int CHANNEL_TYPE_OL = 6;
    public static final int CHANNEL_TYPE_UNKOWN = -1;

    void destroy();

    String getAdBody();

    String getAdCallToAction();

    int getAdChannelType();

    String getAdCoverImageUrl();

    String getAdIconUrl();

    String getAdSocialContext();

    String getAdSource();

    float getAdStarRating();

    String getAdTitle();

    String getId();

    Object getRealData();

    String getSourceType();

    boolean isValid();

    void registerViewForInteraction(View view);

    void registerViewForInteraction(View view, List<View> list);

    void setMobulaAdListener(DuAdDataCallBack duAdDataCallBack);

    void setProcessClickUrlCallback(DuClickCallback duClickCallback);

    void unregisterView();
}
