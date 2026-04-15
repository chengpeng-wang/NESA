package com.mopub.common.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;

public class MoPubCollections {
    public static <T> void addAllNonNull(@NonNull Collection<? super T> c, @Nullable T... a) {
        Collections.addAll(c, a);
        c.removeAll(Collections.singleton(null));
    }
}
