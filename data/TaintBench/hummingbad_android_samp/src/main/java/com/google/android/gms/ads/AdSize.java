package com.google.android.gms.ads;

import android.content.Context;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.client.zzk;
import com.picksinit.ErrorInfo;
import org.xbill.DNS.Type;

public final class AdSize {
    public static final int AUTO_HEIGHT = -2;
    public static final AdSize BANNER = new AdSize(320, 50, "320x50_mb");
    public static final AdSize FULL_BANNER = new AdSize(468, 60, "468x60_as");
    public static final int FULL_WIDTH = -1;
    public static final AdSize LARGE_BANNER = new AdSize(320, 100, "320x100_as");
    public static final AdSize LEADERBOARD = new AdSize(728, 90, "728x90_as");
    public static final AdSize MEDIUM_RECTANGLE = new AdSize(ErrorInfo.ERROR_CODE_NO_NETWORK, Type.TSIG, "300x250_as");
    public static final AdSize SMART_BANNER = new AdSize(-1, -2, "smart_banner");
    public static final AdSize WIDE_SKYSCRAPER = new AdSize(160, 600, "160x600_as");
    private final int zznP;
    private final int zznQ;
    private final String zznR;

    public AdSize(int width, int height) {
        this(width, height, (width == -1 ? "FULL" : String.valueOf(width)) + "x" + (height == -2 ? "AUTO" : String.valueOf(height)) + "_as");
    }

    AdSize(int width, int height, String formatString) {
        if (width < 0 && width != -1) {
            throw new IllegalArgumentException("Invalid width for AdSize: " + width);
        } else if (height >= 0 || height == -2) {
            this.zznP = width;
            this.zznQ = height;
            this.zznR = formatString;
        } else {
            throw new IllegalArgumentException("Invalid height for AdSize: " + height);
        }
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AdSize)) {
            return false;
        }
        AdSize adSize = (AdSize) other;
        return this.zznP == adSize.zznP && this.zznQ == adSize.zznQ && this.zznR.equals(adSize.zznR);
    }

    public int getHeight() {
        return this.zznQ;
    }

    public int getHeightInPixels(Context context) {
        return this.zznQ == -2 ? AdSizeParcel.zzb(context.getResources().getDisplayMetrics()) : zzk.zzcE().zzb(context, this.zznQ);
    }

    public int getWidth() {
        return this.zznP;
    }

    public int getWidthInPixels(Context context) {
        return this.zznP == -1 ? AdSizeParcel.zza(context.getResources().getDisplayMetrics()) : zzk.zzcE().zzb(context, this.zznP);
    }

    public int hashCode() {
        return this.zznR.hashCode();
    }

    public boolean isAutoHeight() {
        return this.zznQ == -2;
    }

    public boolean isFullWidth() {
        return this.zznP == -1;
    }

    public String toString() {
        return this.zznR;
    }
}
