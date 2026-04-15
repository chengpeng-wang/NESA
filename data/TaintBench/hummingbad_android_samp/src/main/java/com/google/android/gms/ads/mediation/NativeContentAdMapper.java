package com.google.android.gms.ads.mediation;

import com.google.android.gms.ads.formats.NativeAd.Image;
import java.util.List;

public abstract class NativeContentAdMapper extends NativeAdMapper {
    private Image zzJF;
    private String zzvK;
    private List<Image> zzvL;
    private String zzvM;
    private String zzvO;
    private String zzvV;

    public final String getAdvertiser() {
        return this.zzvV;
    }

    public final String getBody() {
        return this.zzvM;
    }

    public final String getCallToAction() {
        return this.zzvO;
    }

    public final String getHeadline() {
        return this.zzvK;
    }

    public final List<Image> getImages() {
        return this.zzvL;
    }

    public final Image getLogo() {
        return this.zzJF;
    }

    public final void setAdvertiser(String advertiser) {
        this.zzvV = advertiser;
    }

    public final void setBody(String body) {
        this.zzvM = body;
    }

    public final void setCallToAction(String callToAction) {
        this.zzvO = callToAction;
    }

    public final void setHeadline(String headline) {
        this.zzvK = headline;
    }

    public final void setImages(List<Image> images) {
        this.zzvL = images;
    }

    public final void setLogo(Image logo) {
        this.zzJF = logo;
    }
}
