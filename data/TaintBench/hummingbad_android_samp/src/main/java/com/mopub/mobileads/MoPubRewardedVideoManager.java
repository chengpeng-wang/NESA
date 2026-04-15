package com.mopub.mobileads;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mopub.common.AdFormat;
import com.mopub.common.Constants;
import com.mopub.common.DataKeys;
import com.mopub.common.LocationService;
import com.mopub.common.MediationSettings;
import com.mopub.common.MoPub;
import com.mopub.common.MoPubReward;
import com.mopub.common.Preconditions;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.MoPubCollections;
import com.mopub.common.util.Reflection;
import com.mopub.network.AdRequest;
import com.mopub.network.AdRequest.Listener;
import com.mopub.network.AdResponse;
import com.mopub.network.MoPubNetworkError;
import com.mopub.network.Networking;
import com.mopub.network.TrackingRequest;
import com.mopub.volley.NoConnectionError;
import com.mopub.volley.VolleyError;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class MoPubRewardedVideoManager {
    private static final int DEFAULT_LOAD_TIMEOUT = 30000;
    /* access modifiers changed from: private|static */
    public static MoPubRewardedVideoManager sInstance;
    /* access modifiers changed from: private|final */
    @NonNull
    public final AdRequestStatusMapping mAdRequestStatus;
    @NonNull
    private final Handler mCallbackHandler = new Handler(Looper.getMainLooper());
    /* access modifiers changed from: private|final */
    @NonNull
    public final Context mContext;
    @NonNull
    private final Handler mCustomEventTimeoutHandler;
    @NonNull
    private final Set<MediationSettings> mGlobalMediationSettings = new HashSet();
    @NonNull
    private final Map<String, Set<MediationSettings>> mInstanceMediationSettings;
    @NonNull
    private WeakReference<Activity> mMainActivity;
    /* access modifiers changed from: private|final */
    @NonNull
    public final RewardedVideoData mRewardedVideoData = new RewardedVideoData();
    @NonNull
    private final Map<String, Runnable> mTimeoutMap;
    /* access modifiers changed from: private */
    @Nullable
    public MoPubRewardedVideoListener mVideoListener;

    private static abstract class ForEachMoPubIdRunnable implements Runnable {
        @NonNull
        private final Class<? extends CustomEventRewardedVideo> mCustomEventClass;
        @NonNull
        private final String mThirdPartyId;

        public abstract void forEach(@NonNull String str);

        ForEachMoPubIdRunnable(@NonNull Class<? extends CustomEventRewardedVideo> customEventClass, @NonNull String thirdPartyId) {
            Preconditions.checkNotNull(customEventClass);
            Preconditions.checkNotNull(thirdPartyId);
            this.mCustomEventClass = customEventClass;
            this.mThirdPartyId = thirdPartyId;
        }

        public void run() {
            for (String forEach : MoPubRewardedVideoManager.sInstance.mRewardedVideoData.getMoPubIdsForAdNetwork(this.mCustomEventClass, this.mThirdPartyId)) {
                forEach(forEach);
            }
        }
    }

    public static class RewardedVideoRequestListener implements Listener {
        public final String adUnitId;
        private final MoPubRewardedVideoManager mVideoManager;

        public RewardedVideoRequestListener(MoPubRewardedVideoManager videoManager, String adUnitId) {
            this.adUnitId = adUnitId;
            this.mVideoManager = videoManager;
        }

        public void onSuccess(AdResponse response) {
            this.mVideoManager.onAdSuccess(response, this.adUnitId);
        }

        public void onErrorResponse(VolleyError volleyError) {
            this.mVideoManager.onAdError(volleyError, this.adUnitId);
        }
    }

    private MoPubRewardedVideoManager(@NonNull Activity mainActivity, MediationSettings... mediationSettings) {
        this.mMainActivity = new WeakReference(mainActivity);
        this.mContext = mainActivity.getApplicationContext();
        MoPubCollections.addAllNonNull(this.mGlobalMediationSettings, mediationSettings);
        this.mInstanceMediationSettings = new HashMap();
        this.mCustomEventTimeoutHandler = new Handler();
        this.mTimeoutMap = new HashMap();
        this.mAdRequestStatus = new AdRequestStatusMapping();
    }

    public static synchronized void init(@NonNull Activity mainActivity, MediationSettings... mediationSettings) {
        synchronized (MoPubRewardedVideoManager.class) {
            if (sInstance == null) {
                sInstance = new MoPubRewardedVideoManager(mainActivity, mediationSettings);
            } else {
                MoPubLog.e("Tried to call initializeRewardedVideo more than once. Only the first initialization call has any effect.");
            }
        }
    }

    public static void updateActivity(@NonNull Activity activity) {
        if (sInstance != null) {
            sInstance.mMainActivity = new WeakReference(activity);
            return;
        }
        logErrorNotInitialized();
    }

    @Nullable
    public static <T extends MediationSettings> T getGlobalMediationSettings(@NonNull Class<T> clazz) {
        if (sInstance == null) {
            logErrorNotInitialized();
            return null;
        }
        for (MediationSettings mediationSettings : sInstance.mGlobalMediationSettings) {
            if (clazz.equals(mediationSettings.getClass())) {
                return (MediationSettings) clazz.cast(mediationSettings);
            }
        }
        return null;
    }

    @Nullable
    public static <T extends MediationSettings> T getInstanceMediationSettings(@NonNull Class<T> clazz, @NonNull String adUnitId) {
        if (sInstance == null) {
            logErrorNotInitialized();
            return null;
        }
        Set<MediationSettings> set = (Set) sInstance.mInstanceMediationSettings.get(adUnitId);
        if (set == null) {
            return null;
        }
        for (MediationSettings mediationSettings : set) {
            if (clazz.equals(mediationSettings.getClass())) {
                return (MediationSettings) clazz.cast(mediationSettings);
            }
        }
        return null;
    }

    public static void setVideoListener(@Nullable MoPubRewardedVideoListener listener) {
        if (sInstance != null) {
            sInstance.mVideoListener = listener;
        } else {
            logErrorNotInitialized();
        }
    }

    public static void loadVideo(@NonNull String adUnitId, @Nullable MediationSettings... mediationSettings) {
        if (sInstance == null) {
            logErrorNotInitialized();
            return;
        }
        HashSet hashSet = new HashSet();
        MoPubCollections.addAllNonNull(hashSet, mediationSettings);
        sInstance.mInstanceMediationSettings.put(adUnitId, hashSet);
        loadVideo(adUnitId, new WebViewAdUrlGenerator(sInstance.mContext, false).withAdUnitId(adUnitId).withLocation(LocationService.getLastKnownLocation(sInstance.mContext, MoPub.getLocationPrecision(), MoPub.getLocationAwareness())).generateUrlString(Constants.HOST));
    }

    private static void loadVideo(@NonNull String adUnitId, @NonNull String adUrlString) {
        if (sInstance == null) {
            logErrorNotInitialized();
        } else if (sInstance.mAdRequestStatus.isLoading(adUnitId)) {
            MoPubLog.d(String.format(Locale.US, "Did not queue rewarded video request for ad unit %s. A request is already pending.", new Object[]{adUnitId}));
        } else {
            Networking.getRequestQueue(sInstance.mContext).add(new AdRequest(adUrlString, AdFormat.REWARDED_VIDEO, adUnitId, sInstance.mContext, new RewardedVideoRequestListener(sInstance, adUnitId)));
            sInstance.mAdRequestStatus.markLoading(adUnitId);
        }
    }

    public static boolean hasVideo(@NonNull String adUnitId) {
        if (sInstance != null) {
            return isPlayable(adUnitId, sInstance.mRewardedVideoData.getCustomEvent(adUnitId));
        }
        logErrorNotInitialized();
        return false;
    }

    public static void showVideo(@NonNull String adUnitId) {
        if (sInstance != null) {
            CustomEventRewardedVideo customEvent = sInstance.mRewardedVideoData.getCustomEvent(adUnitId);
            if (isPlayable(adUnitId, customEvent)) {
                sInstance.mAdRequestStatus.markPlayed(adUnitId);
                customEvent.showVideo();
                return;
            }
            sInstance.failover(adUnitId, MoPubErrorCode.VIDEO_NOT_AVAILABLE);
            return;
        }
        logErrorNotInitialized();
    }

    private static boolean isPlayable(String adUnitId, @Nullable CustomEventRewardedVideo customEvent) {
        return sInstance != null && sInstance.mAdRequestStatus.canPlay(adUnitId) && customEvent != null && customEvent.hasVideoAvailable();
    }

    /* access modifiers changed from: private */
    public void onAdSuccess(AdResponse adResponse, String adUnitId) {
        Integer valueOf;
        this.mAdRequestStatus.markLoaded(adUnitId, adResponse.getFailoverUrl(), adResponse.getImpressionTrackingUrl(), adResponse.getClickTrackingUrl());
        Integer adTimeoutMillis = adResponse.getAdTimeoutMillis();
        if (adTimeoutMillis == null || adTimeoutMillis.intValue() <= 0) {
            valueOf = Integer.valueOf(30000);
        } else {
            valueOf = adTimeoutMillis;
        }
        String customEventClassName = adResponse.getCustomEventClassName();
        if (customEventClassName == null) {
            MoPubLog.e("Couldn't create custom event, class name was null.");
            failover(adUnitId, MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        }
        try {
            final CustomEventRewardedVideo customEventRewardedVideo = (CustomEventRewardedVideo) Reflection.instantiateClassWithEmptyConstructor(customEventClassName, CustomEventRewardedVideo.class);
            TreeMap treeMap = new TreeMap();
            treeMap.put(DataKeys.AD_UNIT_ID_KEY, adUnitId);
            Activity activity = (Activity) this.mMainActivity.get();
            if (activity == null) {
                MoPubLog.d("Could not load custom event because Activity reference was null. Call MoPub#updateActivity before requesting more rewarded videos.");
                this.mAdRequestStatus.markFail(adUnitId);
                return;
            }
            AnonymousClass1 anonymousClass1 = new Runnable() {
                public void run() {
                    MoPubLog.d("Custom Event failed to load rewarded video in a timely fashion.");
                    MoPubRewardedVideoManager.onRewardedVideoLoadFailure(customEventRewardedVideo.getClass(), customEventRewardedVideo.getAdNetworkId(), MoPubErrorCode.NETWORK_TIMEOUT);
                    customEventRewardedVideo.onInvalidate();
                }
            };
            this.mCustomEventTimeoutHandler.postDelayed(anonymousClass1, (long) valueOf.intValue());
            this.mTimeoutMap.put(adUnitId, anonymousClass1);
            customEventRewardedVideo.loadCustomEvent(activity, treeMap, adResponse.getServerExtras());
            this.mRewardedVideoData.updateAdUnitCustomEventMapping(adUnitId, customEventRewardedVideo, customEventRewardedVideo.getVideoListenerForSdk(), customEventRewardedVideo.getAdNetworkId());
        } catch (Exception e) {
            MoPubLog.e(String.format(Locale.US, "Couldn't create custom event with class name %s", new Object[]{customEventClassName}));
            failover(adUnitId, MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
        }
    }

    /* access modifiers changed from: private */
    public void onAdError(@NonNull VolleyError volleyError, @NonNull String adUnitId) {
        MoPubErrorCode moPubErrorCode = MoPubErrorCode.INTERNAL_ERROR;
        if (volleyError instanceof MoPubNetworkError) {
            switch (((MoPubNetworkError) volleyError).getReason()) {
                case NO_FILL:
                case WARMING_UP:
                    moPubErrorCode = MoPubErrorCode.NO_FILL;
                    break;
                default:
                    moPubErrorCode = MoPubErrorCode.INTERNAL_ERROR;
                    break;
            }
        }
        if (volleyError instanceof NoConnectionError) {
            moPubErrorCode = MoPubErrorCode.NO_CONNECTION;
        }
        failover(adUnitId, moPubErrorCode);
    }

    /* access modifiers changed from: private */
    public void failover(@NonNull String adUnitId, @NonNull MoPubErrorCode errorCode) {
        String failoverUrl = this.mAdRequestStatus.getFailoverUrl(adUnitId);
        this.mAdRequestStatus.markFail(adUnitId);
        if (failoverUrl != null) {
            loadVideo(adUnitId, failoverUrl);
        } else if (this.mVideoListener != null) {
            this.mVideoListener.onRewardedVideoLoadFailure(adUnitId, errorCode);
        }
    }

    /* access modifiers changed from: private */
    public void cancelTimeouts(@NonNull String moPubId) {
        Runnable runnable = (Runnable) this.mTimeoutMap.remove(moPubId);
        if (runnable != null) {
            this.mCustomEventTimeoutHandler.removeCallbacks(runnable);
        }
    }

    public static <T extends CustomEventRewardedVideo> void onRewardedVideoLoadSuccess(@NonNull Class<T> customEventClass, @NonNull String thirdPartyId) {
        postToInstance(new ForEachMoPubIdRunnable(customEventClass, thirdPartyId) {
            /* access modifiers changed from: protected|final */
            public final void forEach(@NonNull String moPubId) {
                MoPubRewardedVideoManager.sInstance.cancelTimeouts(moPubId);
                if (MoPubRewardedVideoManager.sInstance.mVideoListener != null) {
                    MoPubRewardedVideoManager.sInstance.mVideoListener.onRewardedVideoLoadSuccess(moPubId);
                }
            }
        });
    }

    public static <T extends CustomEventRewardedVideo> void onRewardedVideoLoadFailure(@NonNull Class<T> customEventClass, String thirdPartyId, final MoPubErrorCode errorCode) {
        postToInstance(new ForEachMoPubIdRunnable(customEventClass, thirdPartyId) {
            /* access modifiers changed from: protected|final */
            public final void forEach(@NonNull String moPubId) {
                MoPubRewardedVideoManager.sInstance.cancelTimeouts(moPubId);
                MoPubRewardedVideoManager.sInstance.failover(moPubId, errorCode);
            }
        });
    }

    public static <T extends CustomEventRewardedVideo> void onRewardedVideoStarted(@NonNull Class<T> customEventClass, String thirdPartyId) {
        postToInstance(new ForEachMoPubIdRunnable(customEventClass, thirdPartyId) {
            /* access modifiers changed from: protected|final */
            public final void forEach(@NonNull String moPubId) {
                if (MoPubRewardedVideoManager.sInstance.mVideoListener != null) {
                    MoPubRewardedVideoManager.sInstance.mVideoListener.onRewardedVideoStarted(moPubId);
                }
                TrackingRequest.makeTrackingHttpRequest(MoPubRewardedVideoManager.sInstance.mAdRequestStatus.getImpressionTrackerUrlString(moPubId), MoPubRewardedVideoManager.sInstance.mContext);
                MoPubRewardedVideoManager.sInstance.mAdRequestStatus.clearImpressionUrl(moPubId);
            }
        });
    }

    public static <T extends CustomEventRewardedVideo> void onRewardedVideoPlaybackError(@NonNull Class<T> customEventClass, String thirdPartyId, final MoPubErrorCode errorCode) {
        postToInstance(new ForEachMoPubIdRunnable(customEventClass, thirdPartyId) {
            /* access modifiers changed from: protected|final */
            public final void forEach(@NonNull String moPubId) {
                if (MoPubRewardedVideoManager.sInstance.mVideoListener != null) {
                    MoPubRewardedVideoManager.sInstance.mVideoListener.onRewardedVideoPlaybackError(moPubId, errorCode);
                }
            }
        });
    }

    public static <T extends CustomEventRewardedVideo> void onRewardedVideoClicked(@NonNull Class<T> customEventClass, String thirdPartyId) {
        postToInstance(new ForEachMoPubIdRunnable(customEventClass, thirdPartyId) {
            /* access modifiers changed from: protected|final */
            public final void forEach(@NonNull String moPubId) {
                TrackingRequest.makeTrackingHttpRequest(MoPubRewardedVideoManager.sInstance.mAdRequestStatus.getClickTrackerUrlString(moPubId), MoPubRewardedVideoManager.sInstance.mContext);
                MoPubRewardedVideoManager.sInstance.mAdRequestStatus.clearClickUrl(moPubId);
            }
        });
    }

    public static <T extends CustomEventRewardedVideo> void onRewardedVideoClosed(@NonNull Class<T> customEventClass, String thirdPartyId) {
        postToInstance(new ForEachMoPubIdRunnable(customEventClass, thirdPartyId) {
            /* access modifiers changed from: protected|final */
            public final void forEach(@NonNull String moPubId) {
                if (MoPubRewardedVideoManager.sInstance.mVideoListener != null) {
                    MoPubRewardedVideoManager.sInstance.mVideoListener.onRewardedVideoClosed(moPubId);
                }
            }
        });
    }

    public static <T extends CustomEventRewardedVideo> void onRewardedVideoCompleted(@NonNull final Class<T> customEventClass, final String thirdPartyId, @NonNull final MoPubReward moPubReward) {
        postToInstance(new Runnable() {
            public final void run() {
                HashSet hashSet = new HashSet(MoPubRewardedVideoManager.sInstance.mRewardedVideoData.getMoPubIdsForAdNetwork(customEventClass, thirdPartyId));
                if (MoPubRewardedVideoManager.sInstance.mVideoListener != null) {
                    MoPubRewardedVideoManager.sInstance.mVideoListener.onRewardedVideoCompleted(hashSet, moPubReward);
                }
            }
        });
    }

    private static void postToInstance(@NonNull Runnable runnable) {
        if (sInstance != null) {
            sInstance.mCallbackHandler.post(runnable);
        }
    }

    private static void logErrorNotInitialized() {
        MoPubLog.e("MoPub rewarded video was not initialized. You must call MoPub.initializeRewardedVideo() before loading or attempting to play video ads.");
    }
}
