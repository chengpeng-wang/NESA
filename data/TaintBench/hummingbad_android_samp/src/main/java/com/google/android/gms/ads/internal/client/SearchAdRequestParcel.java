package com.google.android.gms.ads.internal.client;

import android.os.Parcel;
import com.google.android.gms.ads.search.SearchAdRequest;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.zzgk;

@zzgk
public final class SearchAdRequestParcel implements SafeParcelable {
    public static final zzae CREATOR = new zzae();
    public final int backgroundColor;
    public final int versionCode;
    public final int zztA;
    public final int zztB;
    public final int zztC;
    public final int zztD;
    public final int zztE;
    public final int zztF;
    public final int zztG;
    public final String zztH;
    public final int zztI;
    public final String zztJ;
    public final int zztK;
    public final int zztL;
    public final String zztM;

    SearchAdRequestParcel(int versionCode, int anchorTextColor, int backgroundColor, int backgroundGradientBottom, int backgroundGradientTop, int borderColor, int borderThickness, int borderType, int callButtonColor, String channels, int descriptionTextColor, String fontFace, int headerTextColor, int headerTextSize, String query) {
        this.versionCode = versionCode;
        this.zztA = anchorTextColor;
        this.backgroundColor = backgroundColor;
        this.zztB = backgroundGradientBottom;
        this.zztC = backgroundGradientTop;
        this.zztD = borderColor;
        this.zztE = borderThickness;
        this.zztF = borderType;
        this.zztG = callButtonColor;
        this.zztH = channels;
        this.zztI = descriptionTextColor;
        this.zztJ = fontFace;
        this.zztK = headerTextColor;
        this.zztL = headerTextSize;
        this.zztM = query;
    }

    public SearchAdRequestParcel(SearchAdRequest searchAdRequest) {
        this.versionCode = 1;
        this.zztA = searchAdRequest.getAnchorTextColor();
        this.backgroundColor = searchAdRequest.getBackgroundColor();
        this.zztB = searchAdRequest.getBackgroundGradientBottom();
        this.zztC = searchAdRequest.getBackgroundGradientTop();
        this.zztD = searchAdRequest.getBorderColor();
        this.zztE = searchAdRequest.getBorderThickness();
        this.zztF = searchAdRequest.getBorderType();
        this.zztG = searchAdRequest.getCallButtonColor();
        this.zztH = searchAdRequest.getCustomChannels();
        this.zztI = searchAdRequest.getDescriptionTextColor();
        this.zztJ = searchAdRequest.getFontFace();
        this.zztK = searchAdRequest.getHeaderTextColor();
        this.zztL = searchAdRequest.getHeaderTextSize();
        this.zztM = searchAdRequest.getQuery();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        zzae.zza(this, out, flags);
    }
}
