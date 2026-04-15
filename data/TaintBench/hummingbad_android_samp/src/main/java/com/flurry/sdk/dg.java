package com.flurry.sdk;

import android.support.v4.os.EnvironmentCompat;

public enum dg {
    EV_UNKNOWN(EnvironmentCompat.MEDIA_UNKNOWN),
    EV_REQUESTED("requested"),
    EV_REQUEST_AD_COMPONENTS("requestAdComponents"),
    EV_FILLED("filled"),
    EV_UNFILLED("unfilled"),
    EV_RENDERED("rendered"),
    EV_RENDER_FAILED("renderFailed"),
    EV_CLICKED("clicked"),
    EV_VIDEO_START("videoStart"),
    EV_VIDEO_COMPLETED("videoCompleted"),
    EV_VIDEO_PROGRESSED("videoProgressed"),
    EV_SENT_TO_URL("sentToUrl"),
    EV_URL_VERIFIED("urlVerified"),
    EV_URL_NOT_VERIFIED("urlNotVerified"),
    EV_PACKAGE_VERIFIED("packageVerified"),
    EV_PACKAGE_NOT_VERIFIED("packageNotVerified"),
    EV_USER_CONFIRMED("userConfirmed"),
    EV_USER_CANCELLED("userCanceled"),
    EV_AD_WILL_CLOSE("adWillClose"),
    EV_AD_CLOSED("adClosed"),
    EV_VIDEO_CLOSED("videoClosed"),
    EV_REQUEST_AD_COLLAPSE("collapse"),
    EV_CAP_EXHAUSTED("capExhausted"),
    EV_CAP_NOT_EXHAUSTED("capNotExhausted"),
    EV_VIDEO_FIRST_QUARTILE("videoFirstQuartile"),
    EV_VIDEO_MIDPOINT("videoMidpoint"),
    EV_VIDEO_VIEWED("videoView"),
    EV_REWARD_GRANTED("rewardGranted"),
    EV_SEND_URL_STATUS_RESULT("sendUrlStatusResult"),
    EV_PREPARED("prepared"),
    EV_AD_UNIT_MERGED("adunitMerged"),
    EV_PRIVACY("privacy"),
    INTERNAL_EV_AD_OPENED("adOpened"),
    INTERNAL_EV_APP_EXIT("appExit"),
    EV_NATIVE_IMPRESSION("nativeImpression");
    
    public final String J;

    private dg(String str) {
        this.J = str;
    }

    public final String toString() {
        return this.J;
    }
}
