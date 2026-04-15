package com.google.android.gms.ads.mediation;

import com.google.android.gms.ads.formats.NativeAd.Image;
import java.util.List;

public abstract class NativeAppInstallAdMapper extends NativeAdMapper {
    private Image zzJE;
    private String zzvK;
    private List<Image> zzvL;
    private String zzvM;
    private String zzvO;
    private double zzvP;
    private String zzvQ;
    private String zzvR;

    public final String getBody() {
        return this.zzvM;
    }

    public final String getCallToAction() {
        return this.zzvO;
    }

    public final String getHeadline() {
        return this.zzvK;
    }

    public final Image getIcon() {
        return this.zzJE;
    }

    public final List<Image> getImages() {
        return this.zzvL;
    }

    public final String getPrice() {
        return this.zzvR;
    }

    public final double getStarRating() {
        return this.zzvP;
    }

    public final String getStore() {
        return this.zzvQ;
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

    public final void setIcon(Image icon) {
        this.zzJE = icon;
    }

    public final void setImages(List<Image> images) {
        this.zzvL = images;
    }

    public final void setPrice(String price) {
        this.zzvR = price;
    }

    public final void setStarRating(double starRating) {
        this.zzvP = starRating;
    }

    public final void setStore(String store) {
        this.zzvQ = store;
    }
}
