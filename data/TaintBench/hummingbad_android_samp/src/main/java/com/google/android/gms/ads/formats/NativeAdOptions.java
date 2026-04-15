package com.google.android.gms.ads.formats;

public final class NativeAdOptions {
    public static final int ORIENTATION_ANY = 0;
    public static final int ORIENTATION_LANDSCAPE = 2;
    public static final int ORIENTATION_PORTRAIT = 1;
    private final boolean zznV;
    private final int zznW;
    private final boolean zznX;

    public static final class Builder {
        /* access modifiers changed from: private */
        public boolean zznV = false;
        /* access modifiers changed from: private */
        public int zznW = 0;
        /* access modifiers changed from: private */
        public boolean zznX = false;

        public NativeAdOptions build() {
            return new NativeAdOptions(this);
        }

        public Builder setImageOrientation(int orientation) {
            this.zznW = orientation;
            return this;
        }

        public Builder setRequestMultipleImages(boolean shouldRequestMultipleImages) {
            this.zznX = shouldRequestMultipleImages;
            return this;
        }

        public Builder setReturnUrlsForImageAssets(boolean shouldReturnUrls) {
            this.zznV = shouldReturnUrls;
            return this;
        }
    }

    private NativeAdOptions(Builder builder) {
        this.zznV = builder.zznV;
        this.zznW = builder.zznW;
        this.zznX = builder.zznX;
    }

    public int getImageOrientation() {
        return this.zznW;
    }

    public boolean shouldRequestMultipleImages() {
        return this.zznX;
    }

    public boolean shouldReturnUrlsForImageAssets() {
        return this.zznV;
    }
}
