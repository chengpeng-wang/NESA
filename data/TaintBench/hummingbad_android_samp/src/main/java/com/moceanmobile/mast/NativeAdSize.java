package com.moceanmobile.mast;

import com.picksinit.ErrorInfo;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Service;

public final class NativeAdSize {
    public static NativeAdSize ICON_IMAGE_150X150 = new NativeAdSize(150, 150);
    public static NativeAdSize ICON_IMAGE_300X300 = new NativeAdSize(ErrorInfo.ERROR_CODE_NO_NETWORK, ErrorInfo.ERROR_CODE_NO_NETWORK);
    public static NativeAdSize ICON_IMAGE_75X75 = new NativeAdSize(75, 75);
    public static NativeAdSize LOGO_IMAGE_150X150 = new NativeAdSize(150, 150);
    public static NativeAdSize LOGO_IMAGE_300X300 = new NativeAdSize(ErrorInfo.ERROR_CODE_NO_NETWORK, ErrorInfo.ERROR_CODE_NO_NETWORK);
    public static NativeAdSize LOGO_IMAGE_75X75 = new NativeAdSize(75, 75);
    public static NativeAdSize LOGO_IMAGE_80X80 = new NativeAdSize(80, 80);
    public static NativeAdSize MAIN_IMAGE_1136X640 = new NativeAdSize(1136, 640);
    public static NativeAdSize MAIN_IMAGE_1200X627 = new NativeAdSize(1200, 627);
    public static NativeAdSize MAIN_IMAGE_1200X800 = new NativeAdSize(1200, 800);
    public static NativeAdSize MAIN_IMAGE_250X300 = new NativeAdSize(Type.TSIG, ErrorInfo.ERROR_CODE_NO_NETWORK);
    public static NativeAdSize MAIN_IMAGE_256X135 = new NativeAdSize(256, Service.LOC_SRV);
    public static NativeAdSize MAIN_IMAGE_300X250 = new NativeAdSize(ErrorInfo.ERROR_CODE_NO_NETWORK, Type.TSIG);
    public static NativeAdSize MAIN_IMAGE_320X480 = new NativeAdSize(320, 480);
    public static NativeAdSize MAIN_IMAGE_320X50 = new NativeAdSize(320, 50);
    public static NativeAdSize MAIN_IMAGE_320X568 = new NativeAdSize(320, 568);
    public static NativeAdSize MAIN_IMAGE_480X320 = new NativeAdSize(480, 320);
    public static NativeAdSize MAIN_IMAGE_568X320 = new NativeAdSize(568, 320);
    public static NativeAdSize MAIN_IMAGE_600X313 = new NativeAdSize(600, 313);
    public static NativeAdSize MAIN_IMAGE_640X1136 = new NativeAdSize(640, 1136);
    public static NativeAdSize MAIN_IMAGE_640X960 = new NativeAdSize(640, 960);
    public static NativeAdSize MAIN_IMAGE_720X1280 = new NativeAdSize(720, SimpleResolver.DEFAULT_EDNS_PAYLOADSIZE);
    public static NativeAdSize MAIN_IMAGE_728X90 = new NativeAdSize(728, 90);
    public static NativeAdSize MAIN_IMAGE_800X1200 = new NativeAdSize(800, 1200);
    public static NativeAdSize MAIN_IMAGE_960X640 = new NativeAdSize(960, 640);
    private int height = 0;
    private int width = 0;

    public NativeAdSize(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Width and Height should be greater than zero");
        }
        this.width = width;
        this.height = height;
    }

    public final int getWidth() {
        return this.width;
    }

    public final int getHeight() {
        return this.height;
    }

    /* access modifiers changed from: final */
    public final String getSizeInString() {
        return this.width + "x" + this.height;
    }

    /* access modifiers changed from: final */
    public final String getAspectRatio() {
        String str = "";
        try {
            return String.valueOf(this.width / this.height);
        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }
}
