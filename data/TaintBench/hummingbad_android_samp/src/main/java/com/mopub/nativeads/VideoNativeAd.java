package com.mopub.nativeads;

import android.annotation.TargetApi;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import com.mopub.common.Preconditions.NoThrow;
import com.mopub.nativeads.NativeVideoController.Listener;
import java.util.HashMap;
import java.util.Map;

@TargetApi(16)
public abstract class VideoNativeAd extends BaseNativeAd implements Listener {
    @Nullable
    private String mCallToAction;
    @Nullable
    private String mClickDestinationUrl;
    @NonNull
    private final Map<String, Object> mExtras = new HashMap();
    @Nullable
    private String mIconImageUrl;
    @Nullable
    private String mMainImageUrl;
    @Nullable
    private String mPrivacyInformationIconClickThroughUrl;
    @Nullable
    private String mText;
    @Nullable
    private String mTitle;
    @Nullable
    private String mVastVideo;

    @Nullable
    public String getTitle() {
        return this.mTitle;
    }

    @Nullable
    public String getText() {
        return this.mText;
    }

    @Nullable
    public String getMainImageUrl() {
        return this.mMainImageUrl;
    }

    @Nullable
    public String getIconImageUrl() {
        return this.mIconImageUrl;
    }

    @Nullable
    public String getClickDestinationUrl() {
        return this.mClickDestinationUrl;
    }

    @Nullable
    public String getVastVideo() {
        return this.mVastVideo;
    }

    @Nullable
    public String getCallToAction() {
        return this.mCallToAction;
    }

    @Nullable
    public String getPrivacyInformationIconClickThroughUrl() {
        return this.mPrivacyInformationIconClickThroughUrl;
    }

    @Nullable
    public final Object getExtra(@NonNull String key) {
        if (NoThrow.checkNotNull(key, "getExtra key is not allowed to be null")) {
            return this.mExtras.get(key);
        }
        return null;
    }

    public final Map<String, Object> getExtras() {
        return this.mExtras;
    }

    public void setTitle(@Nullable String title) {
        this.mTitle = title;
    }

    public void setText(@Nullable String text) {
        this.mText = text;
    }

    public void setMainImageUrl(@Nullable String mainImageUrl) {
        this.mMainImageUrl = mainImageUrl;
    }

    public void setIconImageUrl(@Nullable String iconImageUrl) {
        this.mIconImageUrl = iconImageUrl;
    }

    public void setClickDestinationUrl(@Nullable String clickDestinationUrl) {
        this.mClickDestinationUrl = clickDestinationUrl;
    }

    public void setVastVideo(String vastVideo) {
        this.mVastVideo = vastVideo;
    }

    public void setCallToAction(@Nullable String callToAction) {
        this.mCallToAction = callToAction;
    }

    public void setPrivacyInformationIconClickThroughUrl(@Nullable String privacyInformationIconClickThroughUrl) {
        this.mPrivacyInformationIconClickThroughUrl = privacyInformationIconClickThroughUrl;
    }

    public final void addExtra(@NonNull String key, @Nullable Object value) {
        if (NoThrow.checkNotNull(key, "addExtra key is not allowed to be null")) {
            this.mExtras.put(key, value);
        }
    }

    public void prepare(@NonNull View view) {
    }

    public void clear(@NonNull View view) {
    }

    public void destroy() {
    }

    public void render(@NonNull MediaLayout mediaLayout) {
    }
}
