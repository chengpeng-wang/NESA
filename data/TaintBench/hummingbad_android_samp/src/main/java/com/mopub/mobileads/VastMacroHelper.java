package com.mopub.mobileads;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.moceanmobile.mast.Defaults;
import com.mopub.common.Preconditions;
import com.mopub.common.logging.MoPubLog;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class VastMacroHelper {
    @NonNull
    private final Map<VastMacro, String> mMacroDataMap = new HashMap();
    @NonNull
    private final List<String> mOriginalUris;

    public VastMacroHelper(@NonNull List<String> uris) {
        Preconditions.checkNotNull(uris, "uris cannot be null");
        this.mOriginalUris = uris;
        this.mMacroDataMap.put(VastMacro.CACHEBUSTING, getCachebustingString());
    }

    @NonNull
    public List<String> getUris() {
        ArrayList arrayList = new ArrayList();
        for (String str : this.mOriginalUris) {
            String str2;
            if (!TextUtils.isEmpty(str2)) {
                Object obj = str2;
                for (Object obj2 : VastMacro.values()) {
                    str2 = (String) this.mMacroDataMap.get(obj2);
                    if (str2 == null) {
                        str2 = "";
                    }
                    obj = obj.replaceAll("\\[" + obj2.name() + "\\]", str2);
                }
                arrayList.add(obj);
            }
        }
        return arrayList;
    }

    @NonNull
    public VastMacroHelper withErrorCode(@Nullable VastErrorCode errorCode) {
        if (errorCode != null) {
            this.mMacroDataMap.put(VastMacro.ERRORCODE, errorCode.getErrorCode());
        }
        return this;
    }

    @NonNull
    public VastMacroHelper withContentPlayHead(@Nullable Integer contentPlayHeadMS) {
        if (contentPlayHeadMS != null) {
            String formatContentPlayHead = formatContentPlayHead(contentPlayHeadMS.intValue());
            if (!TextUtils.isEmpty(formatContentPlayHead)) {
                this.mMacroDataMap.put(VastMacro.CONTENTPLAYHEAD, formatContentPlayHead);
            }
        }
        return this;
    }

    @NonNull
    public VastMacroHelper withAssetUri(@Nullable String assetUri) {
        if (!TextUtils.isEmpty(assetUri)) {
            Object assetUri2;
            try {
                assetUri2 = URLEncoder.encode(assetUri2, Defaults.ENCODING_UTF_8);
            } catch (UnsupportedEncodingException e) {
                MoPubLog.w("Failed to encode url", e);
            }
            this.mMacroDataMap.put(VastMacro.ASSETURI, assetUri2);
        }
        return this;
    }

    @NonNull
    private String getCachebustingString() {
        return String.format(Locale.US, "%08d", new Object[]{Long.valueOf(Math.round(Math.random() * 1.0E8d))});
    }

    @NonNull
    private String formatContentPlayHead(int contentPlayHeadMS) {
        return String.format("%02d:%02d:%02d.%03d", new Object[]{Long.valueOf(TimeUnit.MILLISECONDS.toHours((long) contentPlayHeadMS)), Long.valueOf(TimeUnit.MILLISECONDS.toMinutes((long) contentPlayHeadMS) % TimeUnit.HOURS.toMinutes(1)), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds((long) contentPlayHeadMS) % TimeUnit.MINUTES.toSeconds(1)), Integer.valueOf(contentPlayHeadMS % 1000)});
    }
}
