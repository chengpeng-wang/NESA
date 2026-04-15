package android.support.v4.media;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build.VERSION;
import android.support.v4.view.KeyEventCompat;
import android.view.KeyEvent;
import android.view.KeyEvent.Callback;
import android.view.View;
import java.util.ArrayList;

public class TransportMediator extends TransportController {
    public static final int FLAG_KEY_MEDIA_FAST_FORWARD = 64;
    public static final int FLAG_KEY_MEDIA_NEXT = 128;
    public static final int FLAG_KEY_MEDIA_PAUSE = 16;
    public static final int FLAG_KEY_MEDIA_PLAY = 4;
    public static final int FLAG_KEY_MEDIA_PLAY_PAUSE = 8;
    public static final int FLAG_KEY_MEDIA_PREVIOUS = 1;
    public static final int FLAG_KEY_MEDIA_REWIND = 2;
    public static final int FLAG_KEY_MEDIA_STOP = 32;
    public static final int KEYCODE_MEDIA_PAUSE = 127;
    public static final int KEYCODE_MEDIA_PLAY = 126;
    public static final int KEYCODE_MEDIA_RECORD = 130;
    final AudioManager mAudioManager;
    final TransportPerformer mCallbacks;
    final Context mContext;
    final TransportMediatorJellybeanMR2 mController;
    final Object mDispatcherState;
    final Callback mKeyEventCallback;
    final ArrayList<TransportStateListener> mListeners;
    final TransportMediatorCallback mTransportKeyCallback;
    final View mView;

    static boolean isMediaKey(int i) {
        switch (i) {
            case 79:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 91:
            case KEYCODE_MEDIA_PLAY /*126*/:
            case KEYCODE_MEDIA_PAUSE /*127*/:
            case KEYCODE_MEDIA_RECORD /*130*/:
                return true;
            default:
                return false;
        }
    }

    public TransportMediator(Activity activity, TransportPerformer transportPerformer) {
        this(activity, null, transportPerformer);
    }

    public TransportMediator(View view, TransportPerformer transportPerformer) {
        this(null, view, transportPerformer);
    }

    private TransportMediator(Activity activity, View view, TransportPerformer transportPerformer) {
        Context context = activity;
        View view2 = view;
        TransportPerformer transportPerformer2 = transportPerformer;
        ArrayList arrayList = r11;
        ArrayList arrayList2 = new ArrayList();
        this.mListeners = arrayList;
        AnonymousClass1 anonymousClass1 = r11;
        AnonymousClass1 anonymousClass12 = new TransportMediatorCallback(this) {
            final /* synthetic */ TransportMediator this$0;

            {
                this.this$0 = r5;
            }

            public void handleKey(KeyEvent keyEvent) {
                boolean dispatch = keyEvent.dispatch(this.this$0.mKeyEventCallback);
            }

            public void handleAudioFocusChange(int i) {
                this.this$0.mCallbacks.onAudioFocusChange(i);
            }

            public long getPlaybackPosition() {
                return this.this$0.mCallbacks.onGetCurrentPosition();
            }

            public void playbackPositionUpdate(long j) {
                this.this$0.mCallbacks.onSeekTo(j);
            }
        };
        this.mTransportKeyCallback = anonymousClass1;
        AnonymousClass2 anonymousClass2 = r11;
        AnonymousClass2 anonymousClass22 = new Callback(this) {
            final /* synthetic */ TransportMediator this$0;

            {
                this.this$0 = r5;
            }

            public boolean onKeyDown(int i, KeyEvent keyEvent) {
                int i2 = i;
                return TransportMediator.isMediaKey(i2) ? this.this$0.mCallbacks.onMediaButtonDown(i2, keyEvent) : false;
            }

            public boolean onKeyLongPress(int i, KeyEvent keyEvent) {
                int i2 = i;
                KeyEvent keyEvent2 = keyEvent;
                return false;
            }

            public boolean onKeyUp(int i, KeyEvent keyEvent) {
                int i2 = i;
                return TransportMediator.isMediaKey(i2) ? this.this$0.mCallbacks.onMediaButtonUp(i2, keyEvent) : false;
            }

            public boolean onKeyMultiple(int i, int i2, KeyEvent keyEvent) {
                int i3 = i;
                int i4 = i2;
                KeyEvent keyEvent2 = keyEvent;
                return false;
            }
        };
        this.mKeyEventCallback = anonymousClass2;
        this.mContext = context != null ? context : view2.getContext();
        this.mCallbacks = transportPerformer2;
        this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
        this.mView = context != null ? context.getWindow().getDecorView() : view2;
        this.mDispatcherState = KeyEventCompat.getKeyDispatcherState(this.mView);
        if (VERSION.SDK_INT >= 18) {
            TransportMediatorJellybeanMR2 transportMediatorJellybeanMR2 = r11;
            TransportMediatorJellybeanMR2 transportMediatorJellybeanMR22 = new TransportMediatorJellybeanMR2(this.mContext, this.mAudioManager, this.mView, this.mTransportKeyCallback);
            this.mController = transportMediatorJellybeanMR2;
            return;
        }
        this.mController = null;
    }

    public Object getRemoteControlClient() {
        return this.mController != null ? this.mController.getRemoteControlClient() : null;
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return KeyEventCompat.dispatch(keyEvent, this.mKeyEventCallback, this.mDispatcherState, this);
    }

    public void registerStateListener(TransportStateListener transportStateListener) {
        boolean add = this.mListeners.add(transportStateListener);
    }

    public void unregisterStateListener(TransportStateListener transportStateListener) {
        boolean remove = this.mListeners.remove(transportStateListener);
    }

    private TransportStateListener[] getListeners() {
        if (this.mListeners.size() <= 0) {
            return null;
        }
        TransportStateListener[] transportStateListenerArr = new TransportStateListener[this.mListeners.size()];
        Object[] toArray = this.mListeners.toArray(transportStateListenerArr);
        return transportStateListenerArr;
    }

    private void reportPlayingChanged() {
        TransportStateListener[] listeners = getListeners();
        if (listeners != null) {
            for (TransportStateListener onPlayingChanged : listeners) {
                onPlayingChanged.onPlayingChanged(this);
            }
        }
    }

    private void reportTransportControlsChanged() {
        TransportStateListener[] listeners = getListeners();
        if (listeners != null) {
            for (TransportStateListener onTransportControlsChanged : listeners) {
                onTransportControlsChanged.onTransportControlsChanged(this);
            }
        }
    }

    private void pushControllerState() {
        if (this.mController != null) {
            this.mController.refreshState(this.mCallbacks.onIsPlaying(), this.mCallbacks.onGetCurrentPosition(), this.mCallbacks.onGetTransportControlFlags());
        }
    }

    public void refreshState() {
        pushControllerState();
        reportPlayingChanged();
        reportTransportControlsChanged();
    }

    public void startPlaying() {
        if (this.mController != null) {
            this.mController.startPlaying();
        }
        this.mCallbacks.onStart();
        pushControllerState();
        reportPlayingChanged();
    }

    public void pausePlaying() {
        if (this.mController != null) {
            this.mController.pausePlaying();
        }
        this.mCallbacks.onPause();
        pushControllerState();
        reportPlayingChanged();
    }

    public void stopPlaying() {
        if (this.mController != null) {
            this.mController.stopPlaying();
        }
        this.mCallbacks.onStop();
        pushControllerState();
        reportPlayingChanged();
    }

    public long getDuration() {
        return this.mCallbacks.onGetDuration();
    }

    public long getCurrentPosition() {
        return this.mCallbacks.onGetCurrentPosition();
    }

    public void seekTo(long j) {
        this.mCallbacks.onSeekTo(j);
    }

    public boolean isPlaying() {
        return this.mCallbacks.onIsPlaying();
    }

    public int getBufferPercentage() {
        return this.mCallbacks.onGetBufferPercentage();
    }

    public int getTransportControlFlags() {
        return this.mCallbacks.onGetTransportControlFlags();
    }

    public void destroy() {
        this.mController.destroy();
    }
}
