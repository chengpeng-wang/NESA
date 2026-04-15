package com.facebook.ads;

import android.content.Context;
import android.view.View;
import com.facebook.ads.internal.view.b;
import com.picksinit.ErrorInfo;

public class NativeAdView {

    public enum Type {
        HEIGHT_100(-1, 100),
        HEIGHT_120(-1, 120),
        HEIGHT_300(-1, ErrorInfo.ERROR_CODE_NO_NETWORK),
        HEIGHT_400(-1, ErrorInfo.ERROR_CODE_NO_FILL);
        
        private final int a;
        private final int b;

        private Type(int i, int i2) {
            this.a = i;
            this.b = i2;
        }

        public final int getHeight() {
            return this.b;
        }

        public final int getValue() {
            switch (this.b) {
                case 100:
                    return 1;
                case 120:
                    return 2;
                case ErrorInfo.ERROR_CODE_NO_NETWORK /*300*/:
                    return 3;
                case ErrorInfo.ERROR_CODE_NO_FILL /*400*/:
                    return 4;
                default:
                    return -1;
            }
        }

        public final int getWidth() {
            return this.a;
        }
    }

    public static View render(Context context, NativeAd nativeAd, Type type) {
        return render(context, nativeAd, type, null);
    }

    public static View render(Context context, NativeAd nativeAd, Type type, NativeAdViewAttributes nativeAdViewAttributes) {
        if (nativeAd.isNativeConfigEnabled()) {
            nativeAdViewAttributes = nativeAd.getAdViewAttributes();
        } else if (nativeAdViewAttributes == null) {
            nativeAdViewAttributes = new NativeAdViewAttributes();
        }
        nativeAd.a(type);
        return new b(context, nativeAd, type, nativeAdViewAttributes);
    }
}
