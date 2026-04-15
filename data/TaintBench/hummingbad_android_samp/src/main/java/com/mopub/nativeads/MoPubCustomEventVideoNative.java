package com.mopub.nativeads;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.SurfaceTexture;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.view.View.OnClickListener;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.mopub.common.DataKeys;
import com.mopub.common.Preconditions;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.event.BaseEvent.Category;
import com.mopub.common.event.BaseEvent.Name;
import com.mopub.common.event.BaseEvent.SamplingRate;
import com.mopub.common.event.Event;
import com.mopub.common.event.EventDetails;
import com.mopub.common.event.MoPubEvents;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.Utils;
import com.mopub.mobileads.VastManager;
import com.mopub.mobileads.VastManager.VastManagerListener;
import com.mopub.mobileads.VastTracker;
import com.mopub.mobileads.VastVideoConfig;
import com.mopub.mobileads.VideoViewabilityTracker;
import com.mopub.mobileads.factories.VastManagerFactory;
import com.mopub.nativeads.CustomEventNative.CustomEventNativeListener;
import com.mopub.nativeads.MediaLayout.Mode;
import com.mopub.nativeads.MediaLayout.MuteState;
import com.mopub.nativeads.NativeImageHelper.ImageListener;
import com.mopub.nativeads.NativeVideoController.NativeVideoProgressRunnable.ProgressListener;
import com.mopub.network.TrackingRequest;
import java.lang.ref.WeakReference;
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

@TargetApi(16)
public class MoPubCustomEventVideoNative extends CustomEventNative {

    @TargetApi(16)
    @VisibleForTesting
    static class NativeVideoControllerFactory {
        NativeVideoControllerFactory() {
        }

        public NativeVideoController createForId(long id, @NonNull Context context, @NonNull List<VisibilityTrackingEvent> visibilityTrackingEvents, @NonNull VastVideoConfig vastVideoConfig, @Nullable EventDetails eventDetails) {
            return NativeVideoController.createForId(id, context, visibilityTrackingEvents, vastVideoConfig, eventDetails);
        }
    }

    @TargetApi(16)
    @VisibleForTesting
    static class VideoResponseHeaders {
        private boolean mHeadersAreValid;
        private int mImpressionMinVisiblePercent;
        private int mImpressionVisibleMs;
        private int mMaxBufferMs;
        private int mPauseVisiblePercent;
        private int mPlayVisiblePercent;

        VideoResponseHeaders(@NonNull Map<String, String> serverExtras) {
            try {
                this.mPlayVisiblePercent = Integer.parseInt((String) serverExtras.get(DataKeys.PLAY_VISIBLE_PERCENT));
                this.mPauseVisiblePercent = Integer.parseInt((String) serverExtras.get(DataKeys.PAUSE_VISIBLE_PERCENT));
                this.mImpressionMinVisiblePercent = Integer.parseInt((String) serverExtras.get(DataKeys.IMPRESSION_MIN_VISIBLE_PERCENT));
                this.mImpressionVisibleMs = Integer.parseInt((String) serverExtras.get(DataKeys.IMPRESSION_VISIBLE_MS));
                this.mMaxBufferMs = Integer.parseInt((String) serverExtras.get(DataKeys.MAX_BUFFER_MS));
                this.mHeadersAreValid = true;
            } catch (NumberFormatException e) {
                this.mHeadersAreValid = false;
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean hasValidHeaders() {
            return this.mHeadersAreValid;
        }

        /* access modifiers changed from: 0000 */
        public int getPlayVisiblePercent() {
            return this.mPlayVisiblePercent;
        }

        /* access modifiers changed from: 0000 */
        public int getPauseVisiblePercent() {
            return this.mPauseVisiblePercent;
        }

        /* access modifiers changed from: 0000 */
        public int getImpressionMinVisiblePercent() {
            return this.mImpressionMinVisiblePercent;
        }

        /* access modifiers changed from: 0000 */
        public int getImpressionVisibleMs() {
            return this.mImpressionVisibleMs;
        }

        /* access modifiers changed from: 0000 */
        public int getMaxBufferMs() {
            return this.mMaxBufferMs;
        }
    }

    @TargetApi(16)
    @VisibleForTesting
    static class HeaderVisibilityStrategy implements OnTrackedStrategy {
        @NonNull
        private final WeakReference<MoPubVideoNativeAd> mMoPubVideoNativeAd;

        HeaderVisibilityStrategy(@NonNull MoPubVideoNativeAd moPubVideoNativeAd) {
            this.mMoPubVideoNativeAd = new WeakReference(moPubVideoNativeAd);
        }

        public void execute() {
            MoPubVideoNativeAd moPubVideoNativeAd = (MoPubVideoNativeAd) this.mMoPubVideoNativeAd.get();
            if (moPubVideoNativeAd != null) {
                moPubVideoNativeAd.notifyAdImpressed();
            }
        }
    }

    @TargetApi(16)
    @VisibleForTesting
    static class PayloadVisibilityStrategy implements OnTrackedStrategy {
        @NonNull
        private final Context mContext;
        @NonNull
        private final String mUrl;

        PayloadVisibilityStrategy(@NonNull Context context, @NonNull String url) {
            this.mContext = context.getApplicationContext();
            this.mUrl = url;
        }

        public void execute() {
            TrackingRequest.makeTrackingHttpRequest(this.mUrl, this.mContext);
        }
    }

    @TargetApi(16)
    public static class MoPubVideoNativeAd extends VideoNativeAd implements OnAudioFocusChangeListener, VastManagerListener, ProgressListener {
        static final String PRIVACY_INFORMATION_CLICKTHROUGH_URL = "https://www.mopub.com/optout/";
        /* access modifiers changed from: private|final */
        @NonNull
        public final Context mContext;
        /* access modifiers changed from: private|final */
        @NonNull
        public final CustomEventNativeListener mCustomEventNativeListener;
        /* access modifiers changed from: private */
        public boolean mEnded;
        private boolean mError;
        @Nullable
        private final EventDetails mEventDetails;
        private final long mId;
        @NonNull
        private final JSONObject mJsonObject;
        /* access modifiers changed from: private */
        public int mLatestVideoControllerState;
        /* access modifiers changed from: private */
        public boolean mLatestVisibility;
        /* access modifiers changed from: private */
        @Nullable
        public MediaLayout mMediaLayout;
        @NonNull
        private final String mMoPubClickTrackingUrl;
        /* access modifiers changed from: private */
        public boolean mMuted;
        /* access modifiers changed from: private */
        @Nullable
        public NativeVideoController mNativeVideoController;
        @NonNull
        private final NativeVideoControllerFactory mNativeVideoControllerFactory;
        /* access modifiers changed from: private */
        public boolean mNeedsPrepare;
        /* access modifiers changed from: private */
        public boolean mNeedsSeek;
        @Nullable
        private View mRootView;
        /* access modifiers changed from: private|final */
        @NonNull
        public final VastManager mVastManager;
        @Nullable
        VastVideoConfig mVastVideoConfig;
        @NonNull
        private final VideoResponseHeaders mVideoResponseHeaders;
        /* access modifiers changed from: private */
        @NonNull
        public VideoState mVideoState;
        @NonNull
        private final VisibilityTracker mVideoVisibleTracking;
        ScreenBroadcastReceiver screenBroadcastReceiver;

        enum Parameter {
            IMPRESSION_TRACKER("imptracker", true),
            CLICK_TRACKER("clktracker", true),
            TITLE("title", false),
            TEXT(MASTNativeAdConstants.RESPONSE_TEXT, false),
            IMAGE_URL("mainimage", false),
            ICON_URL("iconimage", false),
            CLICK_DESTINATION("clk", false),
            FALLBACK(MASTNativeAdConstants.RESPONSE_FALLBACK, false),
            CALL_TO_ACTION("ctatext", false),
            VAST_VIDEO("video", false);
            
            @NonNull
            @VisibleForTesting
            static final Set<String> requiredKeys = null;
            @NonNull
            final String mName;
            final boolean mRequired;

            static {
                requiredKeys = new HashSet();
                Parameter[] values = values();
                int length = values.length;
                int i;
                while (i < length) {
                    Parameter parameter = values[i];
                    if (parameter.mRequired) {
                        requiredKeys.add(parameter.mName);
                    }
                    i++;
                }
            }

            private Parameter(String name, boolean required) {
                Preconditions.checkNotNull(name);
                this.mName = name;
                this.mRequired = required;
            }

            @Nullable
            static Parameter from(@NonNull String name) {
                Preconditions.checkNotNull(name);
                for (Parameter parameter : values()) {
                    if (parameter.mName.equals(name)) {
                        return parameter;
                    }
                }
                return null;
            }
        }

        private class ScreenBroadcastReceiver extends BroadcastReceiver {
            private ScreenBroadcastReceiver() {
            }

            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() != "android.intent.action.SCREEN_OFF") {
                    intent.getAction();
                } else if (MoPubVideoNativeAd.this.mVideoState == VideoState.PLAYING_MUTED || MoPubVideoNativeAd.this.mVideoState == VideoState.PLAYING) {
                    MoPubVideoNativeAd.this.applyState(VideoState.PAUSED);
                }
            }
        }

        public enum VideoState {
            CREATED,
            LOADING,
            BUFFERING,
            PAUSED,
            PLAYING,
            PLAYING_MUTED,
            ENDED,
            FAILED_LOAD
        }

        public MoPubVideoNativeAd(@NonNull Activity activity, @NonNull JSONObject jsonObject, @NonNull CustomEventNativeListener customEventNativeListener, @NonNull VideoResponseHeaders videoResponseHeaders, @Nullable EventDetails eventDetails, @NonNull String clickTrackingUrl) {
            this(activity, jsonObject, customEventNativeListener, videoResponseHeaders, new VisibilityTracker(activity), new NativeVideoControllerFactory(), eventDetails, clickTrackingUrl, VastManagerFactory.create(activity.getApplicationContext(), false));
        }

        @VisibleForTesting
        MoPubVideoNativeAd(@NonNull Activity activity, @NonNull JSONObject jsonObject, @NonNull CustomEventNativeListener customEventNativeListener, @NonNull VideoResponseHeaders videoResponseHeaders, @NonNull VisibilityTracker visibilityTracker, @NonNull NativeVideoControllerFactory nativeVideoControllerFactory, @Nullable EventDetails eventDetails, @NonNull String clickTrackingUrl, @NonNull VastManager vastManager) {
            Preconditions.checkNotNull(activity);
            Preconditions.checkNotNull(jsonObject);
            Preconditions.checkNotNull(customEventNativeListener);
            Preconditions.checkNotNull(videoResponseHeaders);
            Preconditions.checkNotNull(visibilityTracker);
            Preconditions.checkNotNull(nativeVideoControllerFactory);
            Preconditions.checkNotNull(clickTrackingUrl);
            Preconditions.checkNotNull(vastManager);
            this.mContext = activity.getApplicationContext();
            this.mJsonObject = jsonObject;
            this.mCustomEventNativeListener = customEventNativeListener;
            this.mVideoResponseHeaders = videoResponseHeaders;
            this.mNativeVideoControllerFactory = nativeVideoControllerFactory;
            this.mMoPubClickTrackingUrl = clickTrackingUrl;
            this.mEventDetails = eventDetails;
            this.mId = Utils.generateUniqueId();
            this.mNeedsSeek = true;
            this.mVideoState = VideoState.CREATED;
            this.mNeedsPrepare = true;
            this.mLatestVideoControllerState = 1;
            this.mMuted = true;
            this.mVideoVisibleTracking = visibilityTracker;
            this.mVideoVisibleTracking.setVisibilityTrackerListener(new VisibilityTrackerListener() {
                public void onVisibilityChanged(List<View> visibleViews, List<View> invisibleViews) {
                    if (!visibleViews.isEmpty() && !MoPubVideoNativeAd.this.mLatestVisibility) {
                        MoPubVideoNativeAd.this.mLatestVisibility = true;
                        MoPubVideoNativeAd.this.maybeChangeState();
                    } else if (!invisibleViews.isEmpty() && MoPubVideoNativeAd.this.mLatestVisibility) {
                        MoPubVideoNativeAd.this.mLatestVisibility = false;
                        MoPubVideoNativeAd.this.maybeChangeState();
                    }
                }
            });
            this.mVastManager = vastManager;
            this.screenBroadcastReceiver = new ScreenBroadcastReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
            this.mContext.registerReceiver(this.screenBroadcastReceiver, intentFilter);
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
                        MoPubVideoNativeAd.this.mVastManager.prepareVastVideoConfiguration(MoPubVideoNativeAd.this.getVastVideo(), MoPubVideoNativeAd.this, MoPubVideoNativeAd.this.mContext);
                    }

                    public void onImagesFailedToCache(NativeErrorCode errorCode) {
                        MoPubVideoNativeAd.this.mCustomEventNativeListener.onNativeAdFailed(errorCode);
                    }
                });
                return;
            }
            throw new IllegalArgumentException("JSONObject did not contain required keys.");
        }

        public void onVastVideoConfigurationPrepared(@Nullable VastVideoConfig vastVideoConfig) {
            if (vastVideoConfig == null) {
                this.mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.EMPTY_AD_RESPONSE);
                return;
            }
            ArrayList arrayList = new ArrayList();
            VisibilityTrackingEvent visibilityTrackingEvent = new VisibilityTrackingEvent();
            visibilityTrackingEvent.strategy = new HeaderVisibilityStrategy(this);
            visibilityTrackingEvent.minimumPercentageVisible = this.mVideoResponseHeaders.getImpressionMinVisiblePercent();
            visibilityTrackingEvent.totalRequiredPlayTimeMs = this.mVideoResponseHeaders.getImpressionVisibleMs();
            arrayList.add(visibilityTrackingEvent);
            this.mVastVideoConfig = vastVideoConfig;
            VideoViewabilityTracker videoViewabilityTracker = this.mVastVideoConfig.getVideoViewabilityTracker();
            if (videoViewabilityTracker != null) {
                VisibilityTrackingEvent visibilityTrackingEvent2 = new VisibilityTrackingEvent();
                visibilityTrackingEvent2.strategy = new PayloadVisibilityStrategy(this.mContext, videoViewabilityTracker.getTrackingUrl());
                visibilityTrackingEvent2.minimumPercentageVisible = videoViewabilityTracker.getPercentViewable();
                visibilityTrackingEvent2.totalRequiredPlayTimeMs = videoViewabilityTracker.getViewablePlaytimeMS();
                arrayList.add(visibilityTrackingEvent2);
            }
            HashSet<String> hashSet = new HashSet();
            hashSet.add(this.mMoPubClickTrackingUrl);
            hashSet.addAll(getClickTrackers());
            ArrayList arrayList2 = new ArrayList();
            for (String vastTracker : hashSet) {
                arrayList2.add(new VastTracker(vastTracker, false));
            }
            this.mVastVideoConfig.addClickTrackers(arrayList2);
            this.mVastVideoConfig.setClickThroughUrl(getClickDestinationUrl());
            this.mNativeVideoController = this.mNativeVideoControllerFactory.createForId(this.mId, this.mContext, arrayList, this.mVastVideoConfig, this.mEventDetails);
            this.mCustomEventNativeListener.onNativeAdLoaded(this);
        }

        private boolean containsRequiredKeys(@NonNull JSONObject jsonObject) {
            Preconditions.checkNotNull(jsonObject);
            HashSet hashSet = new HashSet();
            Iterator keys = jsonObject.keys();
            while (keys.hasNext()) {
                hashSet.add(keys.next());
            }
            return hashSet.containsAll(Parameter.requiredKeys);
        }

        /* JADX WARNING: Removed duplicated region for block: B:19:0x0072  */
        /* JADX WARNING: Removed duplicated region for block: B:9:0x002f  */
        private void addInstanceVariable(@android.support.annotation.NonNull com.mopub.nativeads.MoPubCustomEventVideoNative.MoPubVideoNativeAd.Parameter r3, @android.support.annotation.Nullable java.lang.Object r4) throws java.lang.ClassCastException {
            /*
            r2 = this;
            com.mopub.common.Preconditions.checkNotNull(r3);
            com.mopub.common.Preconditions.checkNotNull(r4);
            r0 = com.mopub.nativeads.MoPubCustomEventVideoNative.AnonymousClass1.$SwitchMap$com$mopub$nativeads$MoPubCustomEventVideoNative$MoPubVideoNativeAd$Parameter;	 Catch:{ ClassCastException -> 0x002a }
            r1 = r3.ordinal();	 Catch:{ ClassCastException -> 0x002a }
            r0 = r0[r1];	 Catch:{ ClassCastException -> 0x002a }
            switch(r0) {
                case 1: goto L_0x0026;
                case 2: goto L_0x0044;
                case 3: goto L_0x004a;
                case 4: goto L_0x0050;
                case 5: goto L_0x0056;
                case 6: goto L_0x005c;
                case 7: goto L_0x0062;
                case 8: goto L_0x0066;
                case 9: goto L_0x006c;
                default: goto L_0x0011;
            };	 Catch:{ ClassCastException -> 0x002a }
        L_0x0011:
            r0 = new java.lang.StringBuilder;	 Catch:{ ClassCastException -> 0x002a }
            r1 = "Unable to add JSON key to internal mapping: ";
            r0.<init>(r1);	 Catch:{ ClassCastException -> 0x002a }
            r1 = r3.mName;	 Catch:{ ClassCastException -> 0x002a }
            r0 = r0.append(r1);	 Catch:{ ClassCastException -> 0x002a }
            r0 = r0.toString();	 Catch:{ ClassCastException -> 0x002a }
            com.mopub.common.logging.MoPubLog.d(r0);	 Catch:{ ClassCastException -> 0x002a }
        L_0x0025:
            return;
        L_0x0026:
            r2.addImpressionTrackers(r4);	 Catch:{ ClassCastException -> 0x002a }
            goto L_0x0025;
        L_0x002a:
            r0 = move-exception;
            r1 = r3.mRequired;
            if (r1 != 0) goto L_0x0072;
        L_0x002f:
            r0 = new java.lang.StringBuilder;
            r1 = "Ignoring class cast exception for optional key: ";
            r0.<init>(r1);
            r1 = r3.mName;
            r0 = r0.append(r1);
            r0 = r0.toString();
            com.mopub.common.logging.MoPubLog.d(r0);
            goto L_0x0025;
        L_0x0044:
            r4 = (java.lang.String) r4;	 Catch:{ ClassCastException -> 0x002a }
            r2.setTitle(r4);	 Catch:{ ClassCastException -> 0x002a }
            goto L_0x0025;
        L_0x004a:
            r4 = (java.lang.String) r4;	 Catch:{ ClassCastException -> 0x002a }
            r2.setText(r4);	 Catch:{ ClassCastException -> 0x002a }
            goto L_0x0025;
        L_0x0050:
            r4 = (java.lang.String) r4;	 Catch:{ ClassCastException -> 0x002a }
            r2.setMainImageUrl(r4);	 Catch:{ ClassCastException -> 0x002a }
            goto L_0x0025;
        L_0x0056:
            r4 = (java.lang.String) r4;	 Catch:{ ClassCastException -> 0x002a }
            r2.setIconImageUrl(r4);	 Catch:{ ClassCastException -> 0x002a }
            goto L_0x0025;
        L_0x005c:
            r4 = (java.lang.String) r4;	 Catch:{ ClassCastException -> 0x002a }
            r2.setClickDestinationUrl(r4);	 Catch:{ ClassCastException -> 0x002a }
            goto L_0x0025;
        L_0x0062:
            r2.parseClickTrackers(r4);	 Catch:{ ClassCastException -> 0x002a }
            goto L_0x0025;
        L_0x0066:
            r4 = (java.lang.String) r4;	 Catch:{ ClassCastException -> 0x002a }
            r2.setCallToAction(r4);	 Catch:{ ClassCastException -> 0x002a }
            goto L_0x0025;
        L_0x006c:
            r4 = (java.lang.String) r4;	 Catch:{ ClassCastException -> 0x002a }
            r2.setVastVideo(r4);	 Catch:{ ClassCastException -> 0x002a }
            goto L_0x0025;
        L_0x0072:
            throw r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.mopub.nativeads.MoPubCustomEventVideoNative$MoPubVideoNativeAd.addInstanceVariable(com.mopub.nativeads.MoPubCustomEventVideoNative$MoPubVideoNativeAd$Parameter, java.lang.Object):void");
        }

        private void parseClickTrackers(@NonNull Object clickTrackers) {
            if (clickTrackers instanceof JSONArray) {
                addClickTrackers(clickTrackers);
            } else {
                addClickTracker((String) clickTrackers);
            }
        }

        public void render(@NonNull MediaLayout mediaLayout) {
            Preconditions.checkNotNull(mediaLayout);
            this.mVideoVisibleTracking.addView(this.mRootView, mediaLayout, this.mVideoResponseHeaders.getPlayVisiblePercent(), this.mVideoResponseHeaders.getPauseVisiblePercent());
            this.mMediaLayout = mediaLayout;
            this.mMediaLayout.initForVideo();
            this.mMediaLayout.setSurfaceTextureListener(new SurfaceTextureListener() {
                public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                    MoPubVideoNativeAd.this.mNativeVideoController.setListener(MoPubVideoNativeAd.this);
                    MoPubVideoNativeAd.this.mNativeVideoController.setOnAudioFocusChangeListener(MoPubVideoNativeAd.this);
                    MoPubVideoNativeAd.this.mNativeVideoController.setProgressListener(MoPubVideoNativeAd.this);
                    MoPubVideoNativeAd.this.mNativeVideoController.setTextureView(MoPubVideoNativeAd.this.mMediaLayout.getTextureView());
                    MoPubVideoNativeAd.this.mMediaLayout.resetProgress();
                    long duration = MoPubVideoNativeAd.this.mNativeVideoController.getDuration();
                    long currentPosition = MoPubVideoNativeAd.this.mNativeVideoController.getCurrentPosition();
                    if (MoPubVideoNativeAd.this.mLatestVideoControllerState == 5 || (duration > 0 && duration - currentPosition < 750)) {
                        MoPubVideoNativeAd.this.mEnded = true;
                    }
                    if (MoPubVideoNativeAd.this.mNeedsPrepare) {
                        MoPubVideoNativeAd.this.mNeedsPrepare = false;
                        MoPubVideoNativeAd.this.mNativeVideoController.prepare(MoPubVideoNativeAd.this);
                    }
                    MoPubVideoNativeAd.this.mNeedsSeek = true;
                    MoPubVideoNativeAd.this.maybeChangeState();
                }

                public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                }

                public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                    MoPubVideoNativeAd.this.mNeedsPrepare = true;
                    MoPubVideoNativeAd.this.mNativeVideoController.release(MoPubVideoNativeAd.this);
                    MoPubVideoNativeAd.this.applyState(VideoState.PAUSED);
                    return true;
                }

                public void onSurfaceTextureUpdated(SurfaceTexture surface) {
                }
            });
            this.mMediaLayout.setMuteControlClickListener(new OnClickListener() {
                public void onClick(View v) {
                    MoPubVideoNativeAd.this.mMuted = !MoPubVideoNativeAd.this.mMuted;
                    MoPubVideoNativeAd.this.maybeChangeState();
                }
            });
            this.mMediaLayout.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    VideoState videoState;
                    if (MoPubVideoNativeAd.this.mVideoState == VideoState.PAUSED) {
                        MoPubVideoNativeAd moPubVideoNativeAd = MoPubVideoNativeAd.this;
                        if (MoPubVideoNativeAd.this.mMuted) {
                            videoState = VideoState.PLAYING_MUTED;
                        } else {
                            videoState = VideoState.PLAYING;
                        }
                        moPubVideoNativeAd.applyState(videoState);
                    } else if (MoPubVideoNativeAd.this.mVideoState == VideoState.ENDED) {
                        MoPubVideoNativeAd.this.mMediaLayout.resetProgress();
                        MoPubVideoNativeAd.this.mNativeVideoController.seekTo(0);
                        MoPubVideoNativeAd.this.mEnded = false;
                        MoPubVideoNativeAd.this.mNeedsSeek = false;
                        MoPubVideoNativeAd.this.applyState(MoPubVideoNativeAd.this.mMuted ? VideoState.PLAYING_MUTED : VideoState.PLAYING);
                    } else if (MoPubVideoNativeAd.this.mVideoState != VideoState.PLAYING) {
                        MoPubVideoNativeAd.this.mVideoState;
                        videoState = VideoState.PLAYING_MUTED;
                    }
                }
            });
            if (this.mNativeVideoController.getPlaybackState() == 6) {
                this.mNativeVideoController.prepare(this);
            }
            applyState(VideoState.PAUSED);
        }

        public void prepare(@NonNull View view) {
            Preconditions.checkNotNull(view);
            this.mRootView = view;
            this.mRootView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    MoPubVideoNativeAd.this.prepareToLeaveView();
                    MoPubVideoNativeAd.this.mNativeVideoController.triggerImpressionTrackers();
                    MoPubVideoNativeAd.this.mNativeVideoController.handleCtaClick(MoPubVideoNativeAd.this.mContext);
                }
            });
        }

        public void clear(@NonNull View view) {
            Preconditions.checkNotNull(view);
            this.mNativeVideoController.clear();
            cleanUpMediaLayout();
        }

        public void destroy() {
            cleanUpMediaLayout();
            this.mNativeVideoController.setPlayWhenReady(false);
            this.mNativeVideoController.release(this);
            NativeVideoController.remove(this.mId);
            this.mVideoVisibleTracking.destroy();
            if (this.screenBroadcastReceiver != null) {
                this.mContext.unregisterReceiver(this.screenBroadcastReceiver);
            }
        }

        public void onStateChanged(boolean playWhenReady, int playbackState) {
            this.mLatestVideoControllerState = playbackState;
            maybeChangeState();
        }

        public void onError(Exception e) {
            MoPubLog.w("Error playing back video.", e);
            this.mError = true;
            maybeChangeState();
        }

        public void updateProgress(int progressTenthPercent) {
            this.mMediaLayout.updateProgress(progressTenthPercent);
        }

        public void onAudioFocusChange(int focusChange) {
            if (focusChange == -1 || focusChange == -2) {
                this.mMuted = true;
                maybeChangeState();
            } else if (focusChange == -3) {
                this.mNativeVideoController.setAudioVolume(0.3f);
            } else if (focusChange == 1) {
                this.mNativeVideoController.setAudioVolume(1.0f);
                maybeChangeState();
            }
        }

        private void cleanUpMediaLayout() {
            if (this.mMediaLayout != null) {
                this.mMediaLayout.setMode(Mode.IMAGE);
                this.mMediaLayout.setSurfaceTextureListener(null);
                this.mMediaLayout.setPlayButtonClickListener(null);
                this.mMediaLayout.setMuteControlClickListener(null);
                this.mMediaLayout.setOnClickListener(null);
                this.mVideoVisibleTracking.removeView(this.mMediaLayout);
                this.mMediaLayout = null;
            }
        }

        /* access modifiers changed from: private */
        public void prepareToLeaveView() {
            this.mNeedsSeek = true;
            this.mNeedsPrepare = true;
            applyState(VideoState.PAUSED, true);
        }

        /* access modifiers changed from: private */
        public void maybeChangeState() {
            VideoState videoState = this.mVideoState;
            if (this.mError) {
                videoState = VideoState.FAILED_LOAD;
            } else if (this.mEnded) {
                videoState = VideoState.ENDED;
            } else if (this.mLatestVideoControllerState == 2 || this.mLatestVideoControllerState == 1) {
                videoState = VideoState.LOADING;
            } else if (this.mLatestVideoControllerState == 3) {
                videoState = VideoState.BUFFERING;
            } else if (this.mLatestVideoControllerState == 5) {
                this.mEnded = true;
                videoState = VideoState.ENDED;
            } else if (this.mLatestVideoControllerState == 4) {
                videoState = this.mLatestVisibility ? this.mMuted ? VideoState.PLAYING_MUTED : VideoState.PLAYING : VideoState.PAUSED;
            }
            applyState(videoState);
        }

        /* access modifiers changed from: 0000 */
        @VisibleForTesting
        public void applyState(@NonNull VideoState videoState) {
            applyState(videoState, false);
        }

        /* access modifiers changed from: 0000 */
        @VisibleForTesting
        public void applyState(@NonNull VideoState videoState, boolean transitionToFullScreen) {
            Preconditions.checkNotNull(videoState);
            if (this.mVideoState != videoState) {
                switch (videoState) {
                    case FAILED_LOAD:
                        this.mVastVideoConfig.handleError(this.mContext, null, 0);
                        this.mNativeVideoController.setAppAudioEnabled(false);
                        this.mMediaLayout.setMode(Mode.IMAGE);
                        if (!(this.mVideoState == VideoState.PLAYING || this.mVideoState == VideoState.PLAYING_MUTED)) {
                            MoPubEvents.log(Event.createEventFromDetails(Name.ERROR_FAILED_TO_PLAY, Category.NATIVE_VIDEO, SamplingRate.NATIVE_VIDEO, this.mEventDetails));
                            break;
                        }
                    case CREATED:
                    case LOADING:
                        this.mNativeVideoController.setPlayWhenReady(true);
                        this.mMediaLayout.setMode(Mode.LOADING);
                        break;
                    case BUFFERING:
                        this.mNativeVideoController.setPlayWhenReady(true);
                        this.mMediaLayout.setMode(Mode.BUFFERING);
                        break;
                    case PAUSED:
                        if (!transitionToFullScreen) {
                            this.mNativeVideoController.setAppAudioEnabled(false);
                        }
                        this.mNativeVideoController.setPlayWhenReady(false);
                        this.mMediaLayout.setMode(Mode.PAUSED);
                        break;
                    case PLAYING:
                        if (this.mNeedsSeek) {
                            this.mNeedsSeek = false;
                        }
                        this.mNativeVideoController.setPlayWhenReady(true);
                        this.mNativeVideoController.setAudioEnabled(true);
                        this.mNativeVideoController.setAppAudioEnabled(true);
                        this.mMediaLayout.setMode(Mode.PLAYING);
                        this.mMediaLayout.setMuteState(MuteState.UNMUTED);
                        break;
                    case PLAYING_MUTED:
                        if (this.mNeedsSeek) {
                            this.mNeedsSeek = false;
                        }
                        this.mNativeVideoController.setPlayWhenReady(true);
                        this.mNativeVideoController.setAudioEnabled(false);
                        this.mNativeVideoController.setAppAudioEnabled(false);
                        this.mMediaLayout.setMode(Mode.PLAYING);
                        this.mMediaLayout.setMuteState(MuteState.MUTED);
                        break;
                    case ENDED:
                        if (this.mNativeVideoController.hasFinalFrame()) {
                            this.mMediaLayout.setMainImageDrawable(this.mNativeVideoController.getFinalFrame());
                        }
                        this.mVastVideoConfig.handleComplete(this.mContext, 0);
                        this.mNativeVideoController.setAppAudioEnabled(false);
                        this.mMediaLayout.setMode(Mode.FINISHED);
                        this.mMediaLayout.updateProgress(1000);
                        break;
                }
                this.mVideoState = videoState;
            }
        }

        private boolean isImageKey(@Nullable String name) {
            return name != null && name.toLowerCase(Locale.US).endsWith("image");
        }

        @NonNull
        private List<String> getExtrasImageUrls() {
            ArrayList arrayList = new ArrayList(getExtras().size());
            for (Entry entry : getExtras().entrySet()) {
                if (isImageKey((String) entry.getKey()) && (entry.getValue() instanceof String)) {
                    arrayList.add((String) entry.getValue());
                }
            }
            return arrayList;
        }

        @NonNull
        private List<String> getAllImageUrls() {
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

        /* access modifiers changed from: 0000 */
        @Deprecated
        @VisibleForTesting
        public boolean needsPrepare() {
            return this.mNeedsPrepare;
        }

        /* access modifiers changed from: 0000 */
        @Deprecated
        @VisibleForTesting
        public boolean hasEnded() {
            return this.mEnded;
        }

        /* access modifiers changed from: 0000 */
        @Deprecated
        @VisibleForTesting
        public boolean needsSeek() {
            return this.mNeedsSeek;
        }

        /* access modifiers changed from: 0000 */
        @Deprecated
        @VisibleForTesting
        public boolean isMuted() {
            return this.mMuted;
        }

        /* access modifiers changed from: 0000 */
        @Deprecated
        @VisibleForTesting
        public long getId() {
            return this.mId;
        }

        /* access modifiers changed from: 0000 */
        @Deprecated
        @VisibleForTesting
        public VideoState getVideoState() {
            return this.mVideoState;
        }

        /* access modifiers changed from: 0000 */
        @Deprecated
        @VisibleForTesting
        public void setLatestVisibility(boolean latestVisibility) {
            this.mLatestVisibility = latestVisibility;
        }

        /* access modifiers changed from: 0000 */
        @Deprecated
        @VisibleForTesting
        public void setMuted(boolean muted) {
            this.mMuted = muted;
        }

        /* access modifiers changed from: 0000 */
        @Deprecated
        @VisibleForTesting
        public MediaLayout getMediaLayout() {
            return this.mMediaLayout;
        }
    }

    /* access modifiers changed from: protected */
    public void loadNativeAd(@NonNull Activity activity, @NonNull CustomEventNativeListener customEventNativeListener, @NonNull Map<String, Object> localExtras, @NonNull Map<String, String> serverExtras) {
        Object obj = localExtras.get(DataKeys.JSON_BODY_KEY);
        if (obj instanceof JSONObject) {
            Object obj2 = localExtras.get(DataKeys.EVENT_DETAILS);
            EventDetails eventDetails = obj2 instanceof EventDetails ? (EventDetails) obj2 : null;
            VideoResponseHeaders videoResponseHeaders = new VideoResponseHeaders(serverExtras);
            if (videoResponseHeaders.hasValidHeaders()) {
                Object obj3 = localExtras.get(DataKeys.CLICK_TRACKING_URL_KEY);
                if (!(obj3 instanceof String) || TextUtils.isEmpty((String) obj3)) {
                    customEventNativeListener.onNativeAdFailed(NativeErrorCode.UNSPECIFIED);
                    return;
                }
                try {
                    new MoPubVideoNativeAd(activity, (JSONObject) obj, customEventNativeListener, videoResponseHeaders, eventDetails, (String) obj3).loadAd();
                    return;
                } catch (IllegalArgumentException e) {
                    customEventNativeListener.onNativeAdFailed(NativeErrorCode.UNSPECIFIED);
                    return;
                }
            }
            customEventNativeListener.onNativeAdFailed(NativeErrorCode.INVALID_RESPONSE);
            return;
        }
        customEventNativeListener.onNativeAdFailed(NativeErrorCode.INVALID_RESPONSE);
    }
}
