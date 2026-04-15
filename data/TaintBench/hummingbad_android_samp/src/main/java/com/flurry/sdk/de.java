package com.flurry.sdk;

import android.support.v4.os.EnvironmentCompat;

public enum de {
    AC_UNKNOWN(EnvironmentCompat.MEDIA_UNKNOWN),
    AC_NOTIFY_USER("notifyUser"),
    AC_LOG_EVENT("logEvent"),
    AC_PROCESS_REDIRECT("processRedirect"),
    AC_NEXT_FRAME("nextFrame"),
    AC_NEXT_AD_UNIT("nextAdUnit"),
    AC_CLOSE_AD("closeAd"),
    AC_VERIFY_URL("verifyUrl"),
    AC_VERIFY_PACKAGE("verifyPackage"),
    AC_SEND_URL_ASYNC("sendUrlAsync"),
    AC_SEND_AD_LOGS("sendAdLogs"),
    AC_LOAD_AD_COMPONENTS("loadAdComponents"),
    AC_DIRECT_OPEN("directOpen"),
    AC_LOAD_COMPLETE("adFullyLoaded"),
    AC_DELETE("delete"),
    AC_CHECK_CAP("checkCap"),
    AC_UPDATE_VIEW_COUNT("updateViewCount"),
    AC_LAUNCH_PACKAGE("launchPackage"),
    AC_MRAID_DO_EXPAND("doExpand");
    
    private final String t;

    private de(String str) {
        this.t = str;
    }

    public final String toString() {
        return this.t;
    }
}
