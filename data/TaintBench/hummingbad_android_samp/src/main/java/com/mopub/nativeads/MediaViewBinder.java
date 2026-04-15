package com.mopub.nativeads;

import android.support.annotation.NonNull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MediaViewBinder {
    final int callToActionId;
    @NonNull
    final Map<String, Integer> extras;
    final int iconImageId;
    final int layoutId;
    final int mediaLayoutId;
    final int privacyInformationIconImageId;
    final int textId;
    final int titleId;

    public static final class Builder {
        /* access modifiers changed from: private */
        public int callToActionId;
        /* access modifiers changed from: private */
        @NonNull
        public Map<String, Integer> extras = Collections.emptyMap();
        /* access modifiers changed from: private */
        public int iconImageId;
        /* access modifiers changed from: private|final */
        public final int layoutId;
        /* access modifiers changed from: private */
        public int mediaLayoutId;
        /* access modifiers changed from: private */
        public int privacyInformationIconImageId;
        /* access modifiers changed from: private */
        public int textId;
        /* access modifiers changed from: private */
        public int titleId;

        public Builder(int layoutId) {
            this.layoutId = layoutId;
            this.extras = new HashMap();
        }

        @NonNull
        public final Builder mediaLayoutId(int mediaLayoutId) {
            this.mediaLayoutId = mediaLayoutId;
            return this;
        }

        @NonNull
        public final Builder titleId(int titlteId) {
            this.titleId = titlteId;
            return this;
        }

        @NonNull
        public final Builder textId(int textId) {
            this.textId = textId;
            return this;
        }

        @NonNull
        public final Builder iconImageId(int iconImageId) {
            this.iconImageId = iconImageId;
            return this;
        }

        @NonNull
        public final Builder callToActionId(int callToActionId) {
            this.callToActionId = callToActionId;
            return this;
        }

        @NonNull
        public final Builder privacyInformationIconImageId(int privacyInformationIconImageId) {
            this.privacyInformationIconImageId = privacyInformationIconImageId;
            return this;
        }

        @NonNull
        public final Builder addExtras(Map<String, Integer> resourceIds) {
            this.extras = new HashMap(resourceIds);
            return this;
        }

        @NonNull
        public final Builder addExtra(String key, int resourceId) {
            this.extras.put(key, Integer.valueOf(resourceId));
            return this;
        }

        @NonNull
        public final MediaViewBinder build() {
            return new MediaViewBinder(this);
        }
    }

    private MediaViewBinder(@NonNull Builder builder) {
        this.layoutId = builder.layoutId;
        this.mediaLayoutId = builder.mediaLayoutId;
        this.titleId = builder.titleId;
        this.textId = builder.textId;
        this.callToActionId = builder.callToActionId;
        this.iconImageId = builder.iconImageId;
        this.privacyInformationIconImageId = builder.privacyInformationIconImageId;
        this.extras = builder.extras;
    }
}
