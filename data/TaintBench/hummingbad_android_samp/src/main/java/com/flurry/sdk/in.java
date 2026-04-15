package com.flurry.sdk;

import com.mopub.nativeads.MoPubNativeAdPositioning.MoPubClientPositioning;
import java.util.Comparator;

public class in implements Comparator {
    private static final String a = in.class.getSimpleName();

    private static int a(Runnable runnable) {
        if (runnable == null) {
            return MoPubClientPositioning.NO_REPEAT;
        }
        if (runnable instanceof io) {
            ma maVar = (ma) ((io) runnable).a();
            return maVar != null ? maVar.j : MoPubClientPositioning.NO_REPEAT;
        } else if (runnable instanceof ma) {
            return ((ma) runnable).j;
        } else {
            iw.a(6, a, "Unknown runnable class: " + runnable.getClass().getName());
            return MoPubClientPositioning.NO_REPEAT;
        }
    }

    public /* synthetic */ int compare(Object obj, Object obj2) {
        Runnable runnable = (Runnable) obj2;
        int a = a((Runnable) obj);
        int a2 = a(runnable);
        return a < a2 ? -1 : a > a2 ? 1 : 0;
    }
}
