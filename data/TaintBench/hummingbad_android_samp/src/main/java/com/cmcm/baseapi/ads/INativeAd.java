package com.cmcm.baseapi.ads;

import android.view.View;
import java.util.List;

public interface INativeAd {

    public interface IClickPreHanleListener {
        boolean preHandle(INativeAd iNativeAd);
    }

    public interface ImpressionListener {
        void onLoggingImpression();
    }

    public interface InnerClickListener {
        boolean onClickFinish(boolean z);

        void onClickStart(boolean z);
    }

    String getAdBody();

    String getAdCallToAction();

    String getAdCoverImageUrl();

    String getAdIconUrl();

    Object getAdObject();

    String getAdSocialContext();

    double getAdStarRating();

    String getAdTitle();

    String getAdTypeName();

    List<String> getExtPics();

    InnerClickListener getInnerClickListener();

    void handleClick();

    boolean hasExpired();

    Boolean isDownLoadApp();

    boolean isNativeAd();

    boolean isNeedShowAdTag();

    boolean isPriority();

    void registerViewForInteraction(View view);

    void setClickPreHanlerListener(IClickPreHanleListener iClickPreHanleListener);

    void setImpressionListener(ImpressionListener impressionListener);

    void setInnerClickListener(InnerClickListener innerClickListener);

    void setReUseAd();

    void unregisterView();
}
