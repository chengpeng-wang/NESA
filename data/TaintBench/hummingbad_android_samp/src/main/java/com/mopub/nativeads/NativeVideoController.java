package com.mopub.nativeads;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.view.Surface;
import android.view.TextureView;
import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.ExoPlayer.Factory;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.extractor.Extractor;
import com.google.android.exoplayer.extractor.ExtractorSampleSource;
import com.google.android.exoplayer.extractor.mp4.Mp4Extractor;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.mopub.common.Preconditions;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.event.BaseEvent.Category;
import com.mopub.common.event.BaseEvent.Name;
import com.mopub.common.event.BaseEvent.SamplingRate;
import com.mopub.common.event.Event;
import com.mopub.common.event.EventDetails;
import com.mopub.common.event.MoPubEvents;
import com.mopub.mobileads.RepeatingHandlerRunnable;
import com.mopub.mobileads.VastTracker;
import com.mopub.mobileads.VastVideoConfig;
import com.mopub.network.TrackingRequest;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@TargetApi(16)
public class NativeVideoController implements OnAudioFocusChangeListener, com.google.android.exoplayer.ExoPlayer.Listener {
    private static final int BUFFER_SEGMENT_COUNT = 32;
    private static final int BUFFER_SEGMENT_SIZE = 65536;
    public static final long RESUME_FINISHED_THRESHOLD = 750;
    public static final int STATE_BUFFERING = 3;
    public static final int STATE_CLEARED = 6;
    public static final int STATE_ENDED = 5;
    public static final int STATE_IDLE = 1;
    public static final int STATE_PREPARING = 2;
    public static final int STATE_READY = 4;
    @NonNull
    private static final Map<Long, NativeVideoController> sManagerMap = new HashMap(4);
    private boolean mAppAudioEnabled;
    private boolean mAudioEnabled;
    @NonNull
    private AudioManager mAudioManager;
    @Nullable
    private MediaCodecAudioTrackRenderer mAudioTrackRenderer;
    @NonNull
    private final Context mContext;
    @Nullable
    private EventDetails mEventDetails;
    @Nullable
    private volatile ExoPlayer mExoPlayer;
    @NonNull
    private final ExoPlayerFactory mExoPlayerFactory;
    private boolean mExoPlayerStateStartedFromIdle;
    @Nullable
    private BitmapDrawable mFinalFrame;
    @NonNull
    private final Handler mHandler;
    @Nullable
    private Listener mListener;
    @NonNull
    private NativeVideoProgressRunnable mNativeVideoProgressRunnable;
    @Nullable
    private OnAudioFocusChangeListener mOnAudioFocusChangeListener;
    @Nullable
    private WeakReference<Object> mOwnerRef;
    private boolean mPlayWhenReady;
    private int mPreviousExoPlayerState;
    @Nullable
    private Surface mSurface;
    @Nullable
    private TextureView mTextureView;
    @NonNull
    private VastVideoConfig mVastVideoConfig;
    @Nullable
    private MediaCodecVideoTrackRenderer mVideoTrackRenderer;

    @VisibleForTesting
    static class ExoPlayerFactory {
        ExoPlayerFactory() {
        }

        public ExoPlayer newInstance(int rendererCount, int minBufferMs, int minRebufferMs) {
            return Factory.newInstance(rendererCount, minBufferMs, minRebufferMs);
        }
    }

    public interface Listener {
        void onError(Exception exception);

        void onStateChanged(boolean z, int i);
    }

    static class VisibilityTrackingEvent {
        boolean isTracked;
        int minimumPercentageVisible;
        OnTrackedStrategy strategy;
        int totalQualifiedPlayCounter;
        int totalRequiredPlayTimeMs;

        interface OnTrackedStrategy {
            void execute();
        }

        VisibilityTrackingEvent() {
        }
    }

    static class NativeVideoProgressRunnable extends RepeatingHandlerRunnable {
        @NonNull
        private final Context mContext;
        private long mCurrentPosition;
        private long mDuration;
        @Nullable
        private ExoPlayer mExoPlayer;
        @Nullable
        private ProgressListener mProgressListener;
        @Nullable
        private TextureView mTextureView;
        @NonNull
        private final VastVideoConfig mVastVideoConfig;
        @NonNull
        private final VisibilityChecker mVisibilityChecker;
        @NonNull
        private final List<VisibilityTrackingEvent> mVisibilityTrackingEvents;

        public interface ProgressListener {
            void updateProgress(int i);
        }

        NativeVideoProgressRunnable(@NonNull Context context, @NonNull Handler handler, @NonNull List<VisibilityTrackingEvent> visibilityTrackingEvents, @NonNull VastVideoConfig vastVideoConfig) {
            this(context, handler, visibilityTrackingEvents, new VisibilityChecker(), vastVideoConfig);
        }

        @VisibleForTesting
        NativeVideoProgressRunnable(@NonNull Context context, @NonNull Handler handler, @NonNull List<VisibilityTrackingEvent> visibilityTrackingEvents, @NonNull VisibilityChecker visibilityChecker, @NonNull VastVideoConfig vastVideoConfig) {
            super(handler);
            Preconditions.checkNotNull(context);
            Preconditions.checkNotNull(handler);
            Preconditions.checkNotNull(visibilityTrackingEvents);
            Preconditions.checkNotNull(vastVideoConfig);
            this.mContext = context.getApplicationContext();
            this.mVisibilityTrackingEvents = visibilityTrackingEvents;
            this.mVisibilityChecker = visibilityChecker;
            this.mVastVideoConfig = vastVideoConfig;
            this.mDuration = -1;
        }

        /* access modifiers changed from: 0000 */
        public void setExoPlayer(@Nullable ExoPlayer exoPlayer) {
            this.mExoPlayer = exoPlayer;
        }

        /* access modifiers changed from: 0000 */
        public void setTextureView(@Nullable TextureView textureView) {
            this.mTextureView = textureView;
        }

        /* access modifiers changed from: 0000 */
        public void setProgressListener(@Nullable ProgressListener progressListener) {
            this.mProgressListener = progressListener;
        }

        /* access modifiers changed from: 0000 */
        public void seekTo(long currentPosition) {
            this.mCurrentPosition = currentPosition;
        }

        /* access modifiers changed from: 0000 */
        public long getCurrentPosition() {
            return this.mCurrentPosition;
        }

        /* access modifiers changed from: 0000 */
        public long getDuration() {
            return this.mDuration;
        }

        /* access modifiers changed from: 0000 */
        public void checkImpressionTrackers(boolean forceTrigger) {
            for (VisibilityTrackingEvent visibilityTrackingEvent : this.mVisibilityTrackingEvents) {
                if (!visibilityTrackingEvent.isTracked && (forceTrigger || this.mVisibilityChecker.isVisible(this.mTextureView, this.mTextureView, visibilityTrackingEvent.minimumPercentageVisible))) {
                    visibilityTrackingEvent.totalQualifiedPlayCounter = (int) (((long) visibilityTrackingEvent.totalQualifiedPlayCounter) + this.mUpdateIntervalMillis);
                    if (forceTrigger || visibilityTrackingEvent.totalQualifiedPlayCounter >= visibilityTrackingEvent.totalRequiredPlayTimeMs) {
                        visibilityTrackingEvent.strategy.execute();
                        visibilityTrackingEvent.isTracked = true;
                    }
                }
            }
        }

        public void doWork() {
            if (this.mExoPlayer != null && this.mExoPlayer.getPlayWhenReady()) {
                this.mCurrentPosition = this.mExoPlayer.getCurrentPosition();
                this.mDuration = this.mExoPlayer.getDuration();
                if (this.mDuration > 0) {
                    checkImpressionTrackers(false);
                    if (this.mProgressListener != null) {
                        this.mProgressListener.updateProgress((int) ((((float) this.mCurrentPosition) / ((float) this.mDuration)) * 1000.0f));
                    }
                    List<VastTracker> untriggeredTrackersBefore = this.mVastVideoConfig.getUntriggeredTrackersBefore((int) this.mCurrentPosition, (int) this.mDuration);
                    if (!untriggeredTrackersBefore.isEmpty()) {
                        Iterable arrayList = new ArrayList();
                        for (VastTracker vastTracker : untriggeredTrackersBefore) {
                            if (!vastTracker.isTracked()) {
                                arrayList.add(vastTracker.getTrackingUrl());
                                vastTracker.setTracked();
                            }
                        }
                        TrackingRequest.makeTrackingHttpRequest(arrayList, this.mContext);
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        @Deprecated
        @VisibleForTesting
        public void setUpdateIntervalMillis(long updateIntervalMillis) {
            this.mUpdateIntervalMillis = updateIntervalMillis;
        }
    }

    @NonNull
    public static NativeVideoController createForId(long id, @NonNull Context context, @NonNull List<VisibilityTrackingEvent> visibilityTrackingEvents, @NonNull VastVideoConfig vastVideoConfig, @Nullable EventDetails eventDetails) {
        NativeVideoController nativeVideoController = new NativeVideoController(context, visibilityTrackingEvents, vastVideoConfig, eventDetails);
        sManagerMap.put(Long.valueOf(id), nativeVideoController);
        return nativeVideoController;
    }

    @NonNull
    @VisibleForTesting
    public static NativeVideoController createForId(long id, @NonNull Context context, @NonNull VastVideoConfig vastVideoConfig, @NonNull NativeVideoProgressRunnable nativeVideoProgressRunnable, @NonNull ExoPlayerFactory exoPlayerFactory, @Nullable EventDetails eventDetails, @NonNull AudioManager audioManager) {
        NativeVideoController nativeVideoController = new NativeVideoController(context, vastVideoConfig, nativeVideoProgressRunnable, exoPlayerFactory, eventDetails, audioManager);
        sManagerMap.put(Long.valueOf(id), nativeVideoController);
        return nativeVideoController;
    }

    @VisibleForTesting
    static void setForId(long id, @NonNull NativeVideoController nativeVideoController) {
        sManagerMap.put(Long.valueOf(id), nativeVideoController);
    }

    @Nullable
    public static NativeVideoController getForId(long id) {
        return (NativeVideoController) sManagerMap.get(Long.valueOf(id));
    }

    @Nullable
    public static NativeVideoController remove(long id) {
        return (NativeVideoController) sManagerMap.remove(Long.valueOf(id));
    }

    private NativeVideoController(@NonNull Context context, @NonNull List<VisibilityTrackingEvent> visibilityTrackingEvents, @NonNull VastVideoConfig vastVideoConfig, @Nullable EventDetails eventDetails) {
        this(context, vastVideoConfig, new NativeVideoProgressRunnable(context, new Handler(Looper.getMainLooper()), visibilityTrackingEvents, vastVideoConfig), new ExoPlayerFactory(), eventDetails, (AudioManager) context.getSystemService("audio"));
    }

    private NativeVideoController(@NonNull Context context, @NonNull VastVideoConfig vastVideoConfig, @NonNull NativeVideoProgressRunnable nativeVideoProgressRunnable, @NonNull ExoPlayerFactory exoPlayerFactory, @Nullable EventDetails eventDetails, @NonNull AudioManager audioManager) {
        this.mPreviousExoPlayerState = 1;
        this.mExoPlayerStateStartedFromIdle = true;
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(vastVideoConfig);
        Preconditions.checkNotNull(exoPlayerFactory);
        Preconditions.checkNotNull(audioManager);
        this.mContext = context.getApplicationContext();
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mVastVideoConfig = vastVideoConfig;
        this.mNativeVideoProgressRunnable = nativeVideoProgressRunnable;
        this.mExoPlayerFactory = exoPlayerFactory;
        this.mEventDetails = eventDetails;
        this.mAudioManager = audioManager;
    }

    public void setListener(@Nullable Listener listener) {
        this.mListener = listener;
    }

    public void setProgressListener(@Nullable ProgressListener progressListener) {
        this.mNativeVideoProgressRunnable.setProgressListener(progressListener);
    }

    public void setOnAudioFocusChangeListener(@Nullable OnAudioFocusChangeListener onAudioFocusChangeListener) {
        this.mOnAudioFocusChangeListener = onAudioFocusChangeListener;
    }

    public void setPlayWhenReady(boolean playWhenReady) {
        if (this.mPlayWhenReady != playWhenReady) {
            this.mPlayWhenReady = playWhenReady;
            setExoPlayWhenReady();
        }
    }

    public int getPlaybackState() {
        if (this.mExoPlayer == null) {
            return 6;
        }
        return this.mExoPlayer.getPlaybackState();
    }

    public void setAudioEnabled(boolean audioEnabled) {
        this.mAudioEnabled = audioEnabled;
        setExoAudio();
    }

    public void setAppAudioEnabled(boolean audioEnabled) {
        if (this.mAppAudioEnabled != audioEnabled) {
            this.mAppAudioEnabled = audioEnabled;
            if (this.mAppAudioEnabled) {
                this.mAudioManager.requestAudioFocus(this, 3, 1);
            } else {
                this.mAudioManager.abandonAudioFocus(this);
            }
        }
    }

    public void setAudioVolume(float volume) {
        if (this.mAudioEnabled) {
            setExoAudio(volume);
        }
    }

    public void onAudioFocusChange(int focusChange) {
        if (this.mOnAudioFocusChangeListener != null) {
            this.mOnAudioFocusChangeListener.onAudioFocusChange(focusChange);
        }
    }

    public void setTextureView(@NonNull TextureView textureView) {
        Preconditions.checkNotNull(textureView);
        this.mSurface = new Surface(textureView.getSurfaceTexture());
        this.mTextureView = textureView;
        this.mNativeVideoProgressRunnable.setTextureView(this.mTextureView);
        setExoSurface(this.mSurface);
    }

    public void prepare(@NonNull Object owner) {
        Preconditions.checkNotNull(owner);
        this.mOwnerRef = new WeakReference(owner);
        clearExistingPlayer();
        preparePlayer();
        setExoSurface(this.mSurface);
    }

    public void clear() {
        setPlayWhenReady(false);
        this.mSurface = null;
        clearExistingPlayer();
    }

    public void release(@NonNull Object owner) {
        Preconditions.checkNotNull(owner);
        if ((this.mOwnerRef == null ? null : this.mOwnerRef.get()) == owner) {
            clearExistingPlayer();
        }
    }

    public void onPlayerStateChanged(boolean playWhenReady, int newState) {
        startProgressRunnable(playWhenReady, newState);
        if (newState == 5 && this.mFinalFrame == null) {
            this.mFinalFrame = new BitmapDrawable(this.mContext.getResources(), this.mTextureView.getBitmap());
        }
        if (this.mPreviousExoPlayerState == 4 && newState == 3) {
            MoPubEvents.log(Event.createEventFromDetails(Name.DOWNLOAD_BUFFERING, Category.NATIVE_VIDEO, SamplingRate.NATIVE_VIDEO, this.mEventDetails));
        }
        if (this.mExoPlayerStateStartedFromIdle && this.mPreviousExoPlayerState == 3 && newState == 4) {
            MoPubEvents.log(Event.createEventFromDetails(Name.DOWNLOAD_VIDEO_READY, Category.NATIVE_VIDEO, SamplingRate.NATIVE_VIDEO, this.mEventDetails));
        }
        this.mPreviousExoPlayerState = newState;
        if (newState == 4) {
            this.mExoPlayerStateStartedFromIdle = false;
        } else if (newState == 1) {
            this.mExoPlayerStateStartedFromIdle = true;
        }
        if (this.mListener != null) {
            this.mListener.onStateChanged(playWhenReady, newState);
        }
    }

    public void seekTo(long ms) {
        if (this.mExoPlayer != null) {
            this.mExoPlayer.seekTo(ms);
            this.mNativeVideoProgressRunnable.seekTo(ms);
        }
    }

    public long getCurrentPosition() {
        return this.mNativeVideoProgressRunnable.getCurrentPosition();
    }

    public long getDuration() {
        return this.mNativeVideoProgressRunnable.getDuration();
    }

    public void onPlayWhenReadyCommitted() {
    }

    public void onPlayerError(ExoPlaybackException e) {
        if (this.mListener != null) {
            MoPubEvents.log(Event.createEventFromDetails(Name.ERROR_DURING_PLAYBACK, Category.NATIVE_VIDEO, SamplingRate.NATIVE_VIDEO, this.mEventDetails));
            this.mListener.onError(e);
        }
    }

    public void handleCtaClick(@NonNull Context context) {
        triggerImpressionTrackers();
        this.mVastVideoConfig.handleClickWithoutResult(context, 0);
    }

    public boolean hasFinalFrame() {
        return this.mFinalFrame != null;
    }

    @Nullable
    public Drawable getFinalFrame() {
        return this.mFinalFrame;
    }

    /* access modifiers changed from: 0000 */
    public void triggerImpressionTrackers() {
        this.mNativeVideoProgressRunnable.checkImpressionTrackers(true);
    }

    private void startProgressRunnable(boolean playWhenReady, int state) {
        if (playWhenReady && state == 4) {
            this.mNativeVideoProgressRunnable.startRepeating(50);
        } else {
            this.mNativeVideoProgressRunnable.stop();
        }
    }

    private void clearExistingPlayer() {
        if (this.mExoPlayer != null) {
            setExoSurface(null);
            this.mExoPlayer.stop();
            this.mExoPlayer.release();
            this.mExoPlayer = null;
            this.mNativeVideoProgressRunnable.setExoPlayer(null);
        }
    }

    private void preparePlayer() {
        if (this.mExoPlayer == null) {
            this.mExoPlayer = this.mExoPlayerFactory.newInstance(2, 1000, 5000);
            this.mNativeVideoProgressRunnable.setExoPlayer(this.mExoPlayer);
            this.mExoPlayer.addListener(this);
            DefaultAllocator defaultAllocator = new DefaultAllocator(65536);
            Mp4Extractor mp4Extractor = new Mp4Extractor();
            HttpDiskCompositeDataSource httpDiskCompositeDataSource = new HttpDiskCompositeDataSource(this.mContext, "exo_demo", this.mEventDetails);
            ExtractorSampleSource extractorSampleSource = new ExtractorSampleSource(Uri.parse(this.mVastVideoConfig.getNetworkMediaFileUrl()), httpDiskCompositeDataSource, defaultAllocator, AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_END, new Extractor[]{mp4Extractor});
            this.mVideoTrackRenderer = new MediaCodecVideoTrackRenderer(extractorSampleSource, 2, 0, this.mHandler, null, 10);
            this.mAudioTrackRenderer = new MediaCodecAudioTrackRenderer(extractorSampleSource);
            this.mExoPlayer.prepare(new TrackRenderer[]{this.mAudioTrackRenderer, this.mVideoTrackRenderer});
        }
        setExoAudio();
        setExoPlayWhenReady();
    }

    private void setExoPlayWhenReady() {
        if (this.mExoPlayer != null) {
            this.mExoPlayer.setPlayWhenReady(this.mPlayWhenReady);
            startProgressRunnable(this.mExoPlayer.getPlayWhenReady(), this.mExoPlayer.getPlaybackState());
        }
    }

    private void setExoAudio() {
        setExoAudio(this.mAudioEnabled ? 1.0f : 0.0f);
    }

    private void setExoAudio(float volume) {
        boolean z = volume >= 0.0f && volume <= 1.0f;
        Preconditions.checkArgument(z);
        if (this.mExoPlayer != null) {
            this.mExoPlayer.sendMessage(this.mAudioTrackRenderer, 1, Float.valueOf(volume));
        }
    }

    private void setExoSurface(@Nullable Surface surface) {
        if (this.mExoPlayer != null) {
            this.mExoPlayer.sendMessage(this.mVideoTrackRenderer, 1, surface);
        }
    }
}
