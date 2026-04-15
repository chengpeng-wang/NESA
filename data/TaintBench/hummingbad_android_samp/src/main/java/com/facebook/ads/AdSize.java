package com.facebook.ads;

import org.xbill.DNS.Type;

public enum AdSize {
    BANNER_320_50(320, 50),
    INTERSTITIAL(0, 0),
    BANNER_HEIGHT_50(-1, 50),
    BANNER_HEIGHT_90(-1, 90),
    RECTANGLE_HEIGHT_250(-1, Type.TSIG);
    
    private final int a;
    private final int b;

    private AdSize(int i, int i2) {
        this.a = i;
        this.b = i2;
    }

    private static boolean a(AdSize adSize, int i, int i2) {
        return adSize != null && adSize.a == i && adSize.b == i2;
    }

    public static AdSize fromWidthAndHeight(int i, int i2) {
        for (AdSize adSize : (AdSize[]) AdSize.class.getEnumConstants()) {
            if (a(adSize, i, i2)) {
                return adSize;
            }
        }
        return null;
    }

    public final int getHeight() {
        return this.b;
    }

    public final int getWidth() {
        return this.a;
    }
}
