package com.mopub.mraid;

import android.support.annotation.NonNull;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import com.mopub.common.VisibleForTesting;

@VisibleForTesting
public interface MraidWebViewDebugListener {
    boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage);

    boolean onJsAlert(@NonNull String str, @NonNull JsResult jsResult);
}
