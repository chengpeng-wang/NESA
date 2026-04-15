package android.support.v4.media;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.RemoteControlClient;
import android.media.RemoteControlClient.OnGetPlaybackPositionListener;
import android.media.RemoteControlClient.OnPlaybackPositionUpdateListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnWindowAttachListener;
import android.view.ViewTreeObserver.OnWindowFocusChangeListener;

class TransportMediatorJellybeanMR2 implements OnGetPlaybackPositionListener, OnPlaybackPositionUpdateListener {
    OnAudioFocusChangeListener mAudioFocusChangeListener;
    boolean mAudioFocused;
    final AudioManager mAudioManager;
    final Context mContext;
    boolean mFocused;
    final Intent mIntent;
    final BroadcastReceiver mMediaButtonReceiver;
    PendingIntent mPendingIntent;
    int mPlayState = 0;
    final String mReceiverAction;
    final IntentFilter mReceiverFilter;
    RemoteControlClient mRemoteControl;
    final View mTargetView;
    final TransportMediatorCallback mTransportCallback;
    final OnWindowAttachListener mWindowAttachListener;
    final OnWindowFocusChangeListener mWindowFocusListener;

    public TransportMediatorJellybeanMR2(Context context, AudioManager audioManager, View view, TransportMediatorCallback transportMediatorCallback) {
        Context context2 = context;
        AudioManager audioManager2 = audioManager;
        View view2 = view;
        TransportMediatorCallback transportMediatorCallback2 = transportMediatorCallback;
        AnonymousClass1 anonymousClass1 = r9;
        AnonymousClass1 anonymousClass12 = new OnWindowAttachListener(this) {
            final /* synthetic */ TransportMediatorJellybeanMR2 this$0;

            {
                this.this$0 = r5;
            }

            public void onWindowAttached() {
                this.this$0.windowAttached();
            }

            public void onWindowDetached() {
                this.this$0.windowDetached();
            }
        };
        this.mWindowAttachListener = anonymousClass1;
        AnonymousClass2 anonymousClass2 = r9;
        AnonymousClass2 anonymousClass22 = new OnWindowFocusChangeListener(this) {
            final /* synthetic */ TransportMediatorJellybeanMR2 this$0;

            {
                this.this$0 = r5;
            }

            public void onWindowFocusChanged(boolean z) {
                if (z) {
                    this.this$0.gainFocus();
                } else {
                    this.this$0.loseFocus();
                }
            }
        };
        this.mWindowFocusListener = anonymousClass2;
        BroadcastReceiver broadcastReceiver = r9;
        BroadcastReceiver anonymousClass3 = new BroadcastReceiver(this) {
            final /* synthetic */ TransportMediatorJellybeanMR2 this$0;

            {
                this.this$0 = r5;
            }

            public void onReceive(Context context, Intent intent) {
                Context context2 = context;
                try {
                    this.this$0.mTransportCallback.handleKey((KeyEvent) intent.getParcelableExtra("android.intent.extra.KEY_EVENT"));
                } catch (ClassCastException e) {
                    int w = Log.w("TransportController", e);
                }
            }
        };
        this.mMediaButtonReceiver = broadcastReceiver;
        AnonymousClass4 anonymousClass4 = r9;
        AnonymousClass4 anonymousClass42 = new OnAudioFocusChangeListener(this) {
            final /* synthetic */ TransportMediatorJellybeanMR2 this$0;

            {
                this.this$0 = r5;
            }

            public void onAudioFocusChange(int i) {
                this.this$0.mTransportCallback.handleAudioFocusChange(i);
            }
        };
        this.mAudioFocusChangeListener = anonymousClass4;
        this.mContext = context2;
        this.mAudioManager = audioManager2;
        this.mTargetView = view2;
        this.mTransportCallback = transportMediatorCallback2;
        StringBuilder stringBuilder = r9;
        StringBuilder stringBuilder2 = new StringBuilder();
        this.mReceiverAction = stringBuilder.append(context2.getPackageName()).append(":transport:").append(System.identityHashCode(this)).toString();
        Intent intent = r9;
        Intent intent2 = new Intent(this.mReceiverAction);
        this.mIntent = intent;
        Intent intent3 = this.mIntent.setPackage(context2.getPackageName());
        IntentFilter intentFilter = r9;
        IntentFilter intentFilter2 = new IntentFilter();
        this.mReceiverFilter = intentFilter;
        this.mReceiverFilter.addAction(this.mReceiverAction);
        this.mTargetView.getViewTreeObserver().addOnWindowAttachListener(this.mWindowAttachListener);
        this.mTargetView.getViewTreeObserver().addOnWindowFocusChangeListener(this.mWindowFocusListener);
    }

    public Object getRemoteControlClient() {
        return this.mRemoteControl;
    }

    public void destroy() {
        windowDetached();
        this.mTargetView.getViewTreeObserver().removeOnWindowAttachListener(this.mWindowAttachListener);
        this.mTargetView.getViewTreeObserver().removeOnWindowFocusChangeListener(this.mWindowFocusListener);
    }

    /* access modifiers changed from: 0000 */
    public void windowAttached() {
        Intent registerReceiver = this.mContext.registerReceiver(this.mMediaButtonReceiver, this.mReceiverFilter);
        this.mPendingIntent = PendingIntent.getBroadcast(this.mContext, 0, this.mIntent, 268435456);
        RemoteControlClient remoteControlClient = r6;
        RemoteControlClient remoteControlClient2 = new RemoteControlClient(this.mPendingIntent);
        this.mRemoteControl = remoteControlClient;
        this.mRemoteControl.setOnGetPlaybackPositionListener(this);
        this.mRemoteControl.setPlaybackPositionUpdateListener(this);
    }

    /* access modifiers changed from: 0000 */
    public void gainFocus() {
        if (!this.mFocused) {
            this.mFocused = true;
            this.mAudioManager.registerMediaButtonEventReceiver(this.mPendingIntent);
            this.mAudioManager.registerRemoteControlClient(this.mRemoteControl);
            if (this.mPlayState == 3) {
                takeAudioFocus();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void takeAudioFocus() {
        if (!this.mAudioFocused) {
            this.mAudioFocused = true;
            int requestAudioFocus = this.mAudioManager.requestAudioFocus(this.mAudioFocusChangeListener, 3, 1);
        }
    }

    public void startPlaying() {
        if (this.mPlayState != 3) {
            this.mPlayState = 3;
            this.mRemoteControl.setPlaybackState(3);
        }
        if (this.mFocused) {
            takeAudioFocus();
        }
    }

    public long onGetPlaybackPosition() {
        return this.mTransportCallback.getPlaybackPosition();
    }

    public void onPlaybackPositionUpdate(long j) {
        this.mTransportCallback.playbackPositionUpdate(j);
    }

    public void refreshState(boolean z, long j, int i) {
        boolean z2 = z;
        long j2 = j;
        int i2 = i;
        if (this.mRemoteControl != null) {
            this.mRemoteControl.setPlaybackState(z2 ? 3 : 1, j2, z2 ? 1.0f : 0.0f);
            this.mRemoteControl.setTransportControlFlags(i2);
        }
    }

    public void pausePlaying() {
        if (this.mPlayState == 3) {
            this.mPlayState = 2;
            this.mRemoteControl.setPlaybackState(2);
        }
        dropAudioFocus();
    }

    public void stopPlaying() {
        if (this.mPlayState != 1) {
            this.mPlayState = 1;
            this.mRemoteControl.setPlaybackState(1);
        }
        dropAudioFocus();
    }

    /* access modifiers changed from: 0000 */
    public void dropAudioFocus() {
        if (this.mAudioFocused) {
            this.mAudioFocused = false;
            int abandonAudioFocus = this.mAudioManager.abandonAudioFocus(this.mAudioFocusChangeListener);
        }
    }

    /* access modifiers changed from: 0000 */
    public void loseFocus() {
        dropAudioFocus();
        if (this.mFocused) {
            this.mFocused = false;
            this.mAudioManager.unregisterRemoteControlClient(this.mRemoteControl);
            this.mAudioManager.unregisterMediaButtonEventReceiver(this.mPendingIntent);
        }
    }

    /* access modifiers changed from: 0000 */
    public void windowDetached() {
        loseFocus();
        if (this.mPendingIntent != null) {
            this.mContext.unregisterReceiver(this.mMediaButtonReceiver);
            this.mPendingIntent.cancel();
            this.mPendingIntent = null;
            this.mRemoteControl = null;
        }
    }
}
