package com.flurry.sdk;

import android.text.TextUtils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class cv {
    private static final String e = cv.class.getSimpleName();
    private static final List f = Arrays.asList(new String[]{"requested", "filled", "unfilled", "rendered", "clicked", "prepared", "adunitMerged", "sendUrlStatusResult", "videoStart", "videoFirstQuartile", "videoMidpoint", "videoThirdQuartile", "videoCompleted", "videoProgressed", "videoView", "videoClosed", "sentToUrl", "adClosed", "adWillClose", "renderFailed", "requestAdComponents", "urlVerified", "capExhausted", "capNotExhausted"});
    public String a;
    public boolean b;
    public long c;
    public Map d;

    private cv() {
    }

    /* synthetic */ cv(byte b) {
        this();
    }

    public cv(String str, boolean z, long j, Map map) {
        if (!f.contains(str)) {
            iw.a(e, "AdEvent initialized with unrecognized type: " + str);
        }
        this.a = str;
        this.b = z;
        this.c = j;
        if (map == null) {
            this.d = new HashMap();
        } else {
            this.d = map;
        }
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof cv)) {
            return false;
        }
        cv cvVar = (cv) obj;
        if (TextUtils.equals(this.a, cvVar.a) && this.b == cvVar.b && this.c == cvVar.c) {
            if (this.d == cvVar.d) {
                return true;
            }
            if (this.d != null && this.d.equals(cvVar.d)) {
                return true;
            }
        }
        return false;
    }

    public final int hashCode() {
        int i = 17;
        if (this.a != null) {
            i = this.a.hashCode() ^ 17;
        }
        if (this.b) {
            i ^= 1;
        }
        i = (int) (((long) i) ^ this.c);
        return this.d != null ? i ^ this.d.hashCode() : i;
    }
}
