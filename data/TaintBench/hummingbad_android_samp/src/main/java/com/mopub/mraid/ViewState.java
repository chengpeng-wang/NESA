package com.mopub.mraid;

import java.util.Locale;

public enum ViewState {
    LOADING,
    DEFAULT,
    RESIZED,
    EXPANDED,
    HIDDEN;

    public final String toJavascriptString() {
        return toString().toLowerCase(Locale.US);
    }
}
