package com.mopub.nativeads;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.mopub.common.DataKeys;
import com.mopub.common.VisibleForTesting;
import com.mopub.nativeads.CustomEventNative.CustomEventNativeListener;
import com.mopub.nativeads.NativeImageHelper.ImageListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;

public class MoPubCustomEventNative extends CustomEventNative {

    public static class MoPubStaticNativeAd extends StaticNativeAd {
        @VisibleForTesting
        static final String PRIVACY_INFORMATION_CLICKTHROUGH_URL = "https://www.mopub.com/optout";
        @NonNull
        private final Context mContext;
        /* access modifiers changed from: private|final */
        @NonNull
        public final CustomEventNativeListener mCustomEventNativeListener;
        @NonNull
        private ImpressionTracker mImpressionTracker;
        @NonNull
        private final JSONObject mJsonObject;
        @NonNull
        private final NativeClickHandler mNativeClickHandler;

        enum Parameter {
            IMPRESSION_TRACKER("imptracker", true),
            CLICK_TRACKER("clktracker", true),
            TITLE("title", false),
            TEXT(MASTNativeAdConstants.RESPONSE_TEXT, false),
            MAIN_IMAGE("mainimage", false),
            ICON_IMAGE("iconimage", false),
            CLICK_DESTINATION("clk", false),
            FALLBACK(MASTNativeAdConstants.RESPONSE_FALLBACK, false),
            CALL_TO_ACTION("ctatext", false),
            STAR_RATING("starrating", false);
            
            @NonNull
            @VisibleForTesting
            static final Set<String> requiredKeys = null;
            @NonNull
            final String name;
            final boolean required;

            static {
                requiredKeys = new HashSet();
                Parameter[] values = values();
                int length = values.length;
                int i;
                while (i < length) {
                    Parameter parameter = values[i];
                    if (parameter.required) {
                        requiredKeys.add(parameter.name);
                    }
                    i++;
                }
            }

            private Parameter(String name, boolean required) {
                this.name = name;
                this.required = required;
            }

            @Nullable
            static Parameter from(@NonNull String name) {
                for (Parameter parameter : values()) {
                    if (parameter.name.equals(name)) {
                        return parameter;
                    }
                }
                return null;
            }
        }

        public MoPubStaticNativeAd(@NonNull Context context, @NonNull JSONObject jsonBody, @NonNull ImpressionTracker impressionTracker, @NonNull NativeClickHandler nativeClickHandler, @NonNull CustomEventNativeListener customEventNativeListener) {
            this.mJsonObject = jsonBody;
            this.mContext = context.getApplicationContext();
            this.mImpressionTracker = impressionTracker;
            this.mNativeClickHandler = nativeClickHandler;
            this.mCustomEventNativeListener = customEventNativeListener;
        }

        public MoPubStaticNativeAd(@NonNull Context context, @NonNull JSONObject jsonBody, @NonNull NativeClickHandler nativeClickHandler) {
            this.mJsonObject = jsonBody;
            this.mContext = context.getApplicationContext();
            this.mNativeClickHandler = nativeClickHandler;
            this.mCustomEventNativeListener = null;
            if (containsRequiredKeys(this.mJsonObject)) {
                Iterator keys = this.mJsonObject.keys();
                while (keys.hasNext()) {
                    String str = (String) keys.next();
                    Parameter from = Parameter.from(str);
                    if (from != null) {
                        try {
                            addInstanceVariable(from, this.mJsonObject.opt(str));
                        } catch (ClassCastException e) {
                            throw new IllegalArgumentException("JSONObject key (" + str + ") contained unexpected value.");
                        }
                    }
                    addExtra(str, this.mJsonObject.opt(str));
                }
                setPrivacyInformationIconClickThroughUrl(PRIVACY_INFORMATION_CLICKTHROUGH_URL);
                return;
            }
            throw new IllegalArgumentException("JSONObject did not contain required keys.");
        }

        /* access modifiers changed from: 0000 */
        public void loadAd() throws IllegalArgumentException {
            if (containsRequiredKeys(this.mJsonObject)) {
                Iterator keys = this.mJsonObject.keys();
                while (keys.hasNext()) {
                    String str = (String) keys.next();
                    Parameter from = Parameter.from(str);
                    if (from != null) {
                        try {
                            addInstanceVariable(from, this.mJsonObject.opt(str));
                        } catch (ClassCastException e) {
                            throw new IllegalArgumentException("JSONObject key (" + str + ") contained unexpected value.");
                        }
                    }
                    addExtra(str, this.mJsonObject.opt(str));
                }
                setPrivacyInformationIconClickThroughUrl(PRIVACY_INFORMATION_CLICKTHROUGH_URL);
                NativeImageHelper.preCacheImages(this.mContext, getAllImageUrls(), new ImageListener() {
                    public void onImagesCached() {
                        if (MoPubStaticNativeAd.this.mCustomEventNativeListener != null) {
                            MoPubStaticNativeAd.this.mCustomEventNativeListener.onNativeAdLoaded(MoPubStaticNativeAd.this);
                        }
                    }

                    public void onImagesFailedToCache(NativeErrorCode errorCode) {
                        if (MoPubStaticNativeAd.this.mCustomEventNativeListener != null) {
                            MoPubStaticNativeAd.this.mCustomEventNativeListener.onNativeAdFailed(errorCode);
                        }
                    }
                });
                return;
            }
            throw new IllegalArgumentException("JSONObject did not contain required keys.");
        }

        private boolean containsRequiredKeys(@NonNull JSONObject jsonObject) {
            if (jsonObject == null) {
                return false;
            }
            HashSet hashSet = new HashSet();
            Iterator keys = jsonObject.keys();
            while (keys.hasNext()) {
                hashSet.add(keys.next());
            }
            return hashSet.containsAll(Parameter.requiredKeys);
        }

        /* JADX WARNING: Removed duplicated region for block: B:18:0x006e  */
        /* JADX WARNING: Removed duplicated region for block: B:8:0x002b  */
        private void addInstanceVariable(@android.support.annotation.NonNull com.mopub.nativeads.MoPubCustomEventNative.MoPubStaticNativeAd.Parameter r3, @android.support.annotation.Nullable java.lang.Object r4) throws java.lang.ClassCastException {
            /*
            r2 = this;
            r0 = com.mopub.nativeads.MoPubCustomEventNative.AnonymousClass1.$SwitchMap$com$mopub$nativeads$MoPubCustomEventNative$MoPubStaticNativeAd$Parameter;	 Catch:{ ClassCastException -> 0x0026 }
            r1 = r3.ordinal();	 Catch:{ ClassCastException -> 0x0026 }
            r0 = r0[r1];	 Catch:{ ClassCastException -> 0x0026 }
            switch(r0) {
                case 1: goto L_0x0020;
                case 2: goto L_0x0040;
                case 3: goto L_0x0046;
                case 4: goto L_0x004a;
                case 5: goto L_0x0050;
                case 6: goto L_0x0054;
                case 7: goto L_0x005a;
                case 8: goto L_0x0060;
                case 9: goto L_0x0066;
                default: goto L_0x000b;
            };	 Catch:{ ClassCastException -> 0x0026 }
        L_0x000b:
            r0 = new java.lang.StringBuilder;	 Catch:{ ClassCastException -> 0x0026 }
            r1 = "Unable to add JSON key to internal mapping: ";
            r0.<init>(r1);	 Catch:{ ClassCastException -> 0x0026 }
            r1 = r3.name;	 Catch:{ ClassCastException -> 0x0026 }
            r0 = r0.append(r1);	 Catch:{ ClassCastException -> 0x0026 }
            r0 = r0.toString();	 Catch:{ ClassCastException -> 0x0026 }
            com.mopub.common.logging.MoPubLog.d(r0);	 Catch:{ ClassCastException -> 0x0026 }
        L_0x001f:
            return;
        L_0x0020:
            r4 = (java.lang.String) r4;	 Catch:{ ClassCastException -> 0x0026 }
            r2.setMainImageUrl(r4);	 Catch:{ ClassCastException -> 0x0026 }
            goto L_0x001f;
        L_0x0026:
            r0 = move-exception;
            r1 = r3.required;
            if (r1 != 0) goto L_0x006e;
        L_0x002b:
            r0 = new java.lang.StringBuilder;
            r1 = "Ignoring class cast exception for optional key: ";
            r0.<init>(r1);
            r1 = r3.name;
            r0 = r0.append(r1);
            r0 = r0.toString();
            com.mopub.common.logging.MoPubLog.d(r0);
            goto L_0x001f;
        L_0x0040:
            r4 = (java.lang.String) r4;	 Catch:{ ClassCastException -> 0x0026 }
            r2.setIconImageUrl(r4);	 Catch:{ ClassCastException -> 0x0026 }
            goto L_0x001f;
        L_0x0046:
            r2.addImpressionTrackers(r4);	 Catch:{ ClassCastException -> 0x0026 }
            goto L_0x001f;
        L_0x004a:
            r4 = (java.lang.String) r4;	 Catch:{ ClassCastException -> 0x0026 }
            r2.setClickDestinationUrl(r4);	 Catch:{ ClassCastException -> 0x0026 }
            goto L_0x001f;
        L_0x0050:
            r2.parseClickTrackers(r4);	 Catch:{ ClassCastException -> 0x0026 }
            goto L_0x001f;
        L_0x0054:
            r4 = (java.lang.String) r4;	 Catch:{ ClassCastException -> 0x0026 }
            r2.setCallToAction(r4);	 Catch:{ ClassCastException -> 0x0026 }
            goto L_0x001f;
        L_0x005a:
            r4 = (java.lang.String) r4;	 Catch:{ ClassCastException -> 0x0026 }
            r2.setTitle(r4);	 Catch:{ ClassCastException -> 0x0026 }
            goto L_0x001f;
        L_0x0060:
            r4 = (java.lang.String) r4;	 Catch:{ ClassCastException -> 0x0026 }
            r2.setText(r4);	 Catch:{ ClassCastException -> 0x0026 }
            goto L_0x001f;
        L_0x0066:
            r0 = com.mopub.common.util.Numbers.parseDouble(r4);	 Catch:{ ClassCastException -> 0x0026 }
            r2.setStarRating(r0);	 Catch:{ ClassCastException -> 0x0026 }
            goto L_0x001f;
        L_0x006e:
            throw r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.mopub.nativeads.MoPubCustomEventNative$MoPubStaticNativeAd.addInstanceVariable(com.mopub.nativeads.MoPubCustomEventNative$MoPubStaticNativeAd$Parameter, java.lang.Object):void");
        }

        private void parseClickTrackers(@NonNull Object clickTrackers) {
            if (clickTrackers instanceof JSONArray) {
                addClickTrackers(clickTrackers);
            } else {
                addClickTracker((String) clickTrackers);
            }
        }

        private boolean isImageKey(@Nullable String name) {
            return name != null && name.toLowerCase(Locale.US).endsWith("image");
        }

        /* access modifiers changed from: 0000 */
        @NonNull
        public List<String> getExtrasImageUrls() {
            ArrayList arrayList = new ArrayList(getExtras().size());
            for (Entry entry : getExtras().entrySet()) {
                if (isImageKey((String) entry.getKey()) && (entry.getValue() instanceof String)) {
                    arrayList.add((String) entry.getValue());
                }
            }
            return arrayList;
        }

        /* access modifiers changed from: 0000 */
        @NonNull
        public List<String> getAllImageUrls() {
            ArrayList arrayList = new ArrayList();
            if (getMainImageUrl() != null) {
                arrayList.add(getMainImageUrl());
            }
            if (getIconImageUrl() != null) {
                arrayList.add(getIconImageUrl());
            }
            arrayList.addAll(getExtrasImageUrls());
            return arrayList;
        }

        public void setImpressionTracker(ImpressionTracker impressionTracker) {
            if (this.mImpressionTracker == null) {
                this.mImpressionTracker = impressionTracker;
            }
        }

        public void prepare(@NonNull View view) {
            if (this.mImpressionTracker != null) {
                this.mImpressionTracker.addView(view, this);
            }
            this.mNativeClickHandler.setOnClickListener(view, (ClickInterface) this);
        }

        public void clear(@NonNull View view) {
            if (this.mImpressionTracker != null) {
                this.mImpressionTracker.removeView(view);
            }
            this.mNativeClickHandler.clearOnClickListener(view);
        }

        public void destroy() {
            this.mImpressionTracker.destroy();
        }

        public void recordImpression(@NonNull View view) {
            notifyAdImpressed();
        }

        public void handleClick(@Nullable View view) {
            notifyAdClicked();
            this.mNativeClickHandler.openClickDestinationUrl(getClickDestinationUrl(), view);
        }
    }

    /* access modifiers changed from: protected */
    public void loadNativeAd(@NonNull Activity activity, @NonNull CustomEventNativeListener customEventNativeListener, @NonNull Map<String, Object> localExtras, @NonNull Map<String, String> map) {
        Object obj = localExtras.get(DataKeys.JSON_BODY_KEY);
        if (obj instanceof JSONObject) {
            try {
                new MoPubStaticNativeAd(activity, (JSONObject) obj, new ImpressionTracker(activity), new NativeClickHandler(activity), customEventNativeListener).loadAd();
                return;
            } catch (IllegalArgumentException e) {
                customEventNativeListener.onNativeAdFailed(NativeErrorCode.UNSPECIFIED);
                return;
            }
        }
        customEventNativeListener.onNativeAdFailed(NativeErrorCode.INVALID_RESPONSE);
    }
}
