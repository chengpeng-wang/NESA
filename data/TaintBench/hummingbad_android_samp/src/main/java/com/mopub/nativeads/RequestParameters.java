package com.mopub.nativeads;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.util.EnumSet;

public class RequestParameters {
    @Nullable
    private final EnumSet<NativeAdAsset> mDesiredAssets;
    @Nullable
    private final String mKeywords;
    @Nullable
    private final Location mLocation;

    public static final class Builder {
        /* access modifiers changed from: private */
        public EnumSet<NativeAdAsset> desiredAssets;
        /* access modifiers changed from: private */
        public String keywords;
        /* access modifiers changed from: private */
        public Location location;

        @NonNull
        public final Builder keywords(String keywords) {
            this.keywords = keywords;
            return this;
        }

        @NonNull
        public final Builder location(Location location) {
            this.location = location;
            return this;
        }

        @NonNull
        public final Builder desiredAssets(EnumSet<NativeAdAsset> desiredAssets) {
            this.desiredAssets = EnumSet.copyOf(desiredAssets);
            return this;
        }

        @NonNull
        public final RequestParameters build() {
            return new RequestParameters(this);
        }
    }

    public enum NativeAdAsset {
        TITLE("title"),
        TEXT(MASTNativeAdConstants.RESPONSE_TEXT),
        ICON_IMAGE("iconimage"),
        MAIN_IMAGE("mainimage"),
        CALL_TO_ACTION_TEXT("ctatext"),
        STAR_RATING("starrating");
        
        private final String mAssetName;

        private NativeAdAsset(String assetName) {
            this.mAssetName = assetName;
        }

        @NonNull
        public final String toString() {
            return this.mAssetName;
        }
    }

    private RequestParameters(@NonNull Builder builder) {
        this.mKeywords = builder.keywords;
        this.mLocation = builder.location;
        this.mDesiredAssets = builder.desiredAssets;
    }

    @Nullable
    public final String getKeywords() {
        return this.mKeywords;
    }

    @Nullable
    public final Location getLocation() {
        return this.mLocation;
    }

    public final String getDesiredAssets() {
        String str = "";
        if (this.mDesiredAssets != null) {
            return TextUtils.join(",", this.mDesiredAssets.toArray());
        }
        return str;
    }
}
