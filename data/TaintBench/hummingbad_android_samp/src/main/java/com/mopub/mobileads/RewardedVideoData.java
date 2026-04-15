package com.mopub.mobileads;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

class RewardedVideoData {
    @NonNull
    private final Set<CustomEventRewardedVideoListener> mAdNetworkListeners = new HashSet();
    @NonNull
    private final Map<String, CustomEventRewardedVideo> mAdUnitToCustomEventMap = new TreeMap();
    @NonNull
    private final Map<TwoPartKey, Set<String>> mCustomEventToMoPubIdMap = new HashMap();

    private static class TwoPartKey extends Pair<Class<? extends CustomEventRewardedVideo>, String> {
        @NonNull
        final String adNetworkId;
        @NonNull
        final Class<? extends CustomEventRewardedVideo> customEventClass;

        public TwoPartKey(@NonNull Class<? extends CustomEventRewardedVideo> customEventClass, @NonNull String adNetworkId) {
            super(customEventClass, adNetworkId);
            this.customEventClass = customEventClass;
            this.adNetworkId = adNetworkId;
        }
    }

    RewardedVideoData() {
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public CustomEventRewardedVideo getCustomEvent(@NonNull String moPubId) {
        return (CustomEventRewardedVideo) this.mAdUnitToCustomEventMap.get(moPubId);
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public Set<String> getMoPubIdsForAdNetwork(@NonNull Class<? extends CustomEventRewardedVideo> customEventClass, @Nullable String adNetworkId) {
        if (adNetworkId == null) {
            HashSet hashSet = new HashSet();
            for (Entry entry : this.mCustomEventToMoPubIdMap.entrySet()) {
                if (customEventClass == ((TwoPartKey) entry.getKey()).customEventClass) {
                    hashSet.addAll((Collection) entry.getValue());
                }
            }
            return hashSet;
        }
        TwoPartKey twoPartKey = new TwoPartKey(customEventClass, adNetworkId);
        return this.mCustomEventToMoPubIdMap.containsKey(twoPartKey) ? (Set) this.mCustomEventToMoPubIdMap.get(twoPartKey) : Collections.emptySet();
    }

    /* access modifiers changed from: 0000 */
    public void updateAdUnitCustomEventMapping(@NonNull String moPubId, @NonNull CustomEventRewardedVideo customEvent, @Nullable CustomEventRewardedVideoListener listener, @NonNull String adNetworkId) {
        this.mAdUnitToCustomEventMap.put(moPubId, customEvent);
        this.mAdNetworkListeners.add(listener);
        associateCustomEventWithMoPubId(customEvent.getClass(), adNetworkId, moPubId);
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0055  */
    public void associateCustomEventWithMoPubId(@android.support.annotation.NonNull java.lang.Class<? extends com.mopub.mobileads.CustomEventRewardedVideo> r5, @android.support.annotation.NonNull java.lang.String r6, @android.support.annotation.NonNull java.lang.String r7) {
        /*
        r4 = this;
        r2 = new com.mopub.mobileads.RewardedVideoData$TwoPartKey;
        r2.m2040init(r5, r6);
        r0 = r4.mCustomEventToMoPubIdMap;
        r0 = r0.entrySet();
        r3 = r0.iterator();
    L_0x000f:
        r0 = r3.hasNext();
        if (r0 == 0) goto L_0x004b;
    L_0x0015:
        r0 = r3.next();
        r0 = (java.util.Map.Entry) r0;
        r1 = r0.getKey();
        r1 = (com.mopub.mobileads.RewardedVideoData.TwoPartKey) r1;
        r1 = r1.equals(r2);
        if (r1 != 0) goto L_0x000f;
    L_0x0027:
        r1 = r0.getValue();
        r1 = (java.util.Set) r1;
        r1 = r1.contains(r7);
        if (r1 == 0) goto L_0x000f;
    L_0x0033:
        r1 = r0.getValue();
        r1 = (java.util.Set) r1;
        r1.remove(r7);
        r0 = r0.getValue();
        r0 = (java.util.Set) r0;
        r0 = r0.isEmpty();
        if (r0 == 0) goto L_0x004b;
    L_0x0048:
        r3.remove();
    L_0x004b:
        r0 = r4.mCustomEventToMoPubIdMap;
        r0 = r0.get(r2);
        r0 = (java.util.Set) r0;
        if (r0 != 0) goto L_0x005f;
    L_0x0055:
        r0 = new java.util.HashSet;
        r0.<init>();
        r1 = r4.mCustomEventToMoPubIdMap;
        r1.put(r2, r0);
    L_0x005f:
        r0.add(r7);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mopub.mobileads.RewardedVideoData.associateCustomEventWithMoPubId(java.lang.Class, java.lang.String, java.lang.String):void");
    }
}
