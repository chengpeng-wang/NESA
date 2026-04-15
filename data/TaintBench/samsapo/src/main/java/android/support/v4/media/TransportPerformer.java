package android.support.v4.media;

import android.os.SystemClock;
import android.view.KeyEvent;

public abstract class TransportPerformer {
    static final int AUDIOFOCUS_GAIN = 1;
    static final int AUDIOFOCUS_GAIN_TRANSIENT = 2;
    static final int AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK = 3;
    static final int AUDIOFOCUS_LOSS = -1;
    static final int AUDIOFOCUS_LOSS_TRANSIENT = -2;
    static final int AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK = -3;

    public abstract long onGetCurrentPosition();

    public abstract long onGetDuration();

    public abstract boolean onIsPlaying();

    public abstract void onPause();

    public abstract void onSeekTo(long j);

    public abstract void onStart();

    public abstract void onStop();

    public TransportPerformer() {
    }

    public int onGetBufferPercentage() {
        return 100;
    }

    public int onGetTransportControlFlags() {
        return 60;
    }

    public boolean onMediaButtonDown(int i, KeyEvent keyEvent) {
        KeyEvent keyEvent2 = keyEvent;
        switch (i) {
            case 79:
            case 85:
                if (!onIsPlaying()) {
                    onStart();
                    break;
                }
                onPause();
                break;
            case 86:
                onStop();
                return true;
            case TransportMediator.KEYCODE_MEDIA_PLAY /*126*/:
                onStart();
                return true;
            case TransportMediator.KEYCODE_MEDIA_PAUSE /*127*/:
                onPause();
                return true;
        }
        return true;
    }

    public boolean onMediaButtonUp(int i, KeyEvent keyEvent) {
        int i2 = i;
        KeyEvent keyEvent2 = keyEvent;
        return true;
    }

    public void onAudioFocusChange(int i) {
        int i2 = 0;
        switch (i) {
            case -1:
                i2 = 127;
                break;
        }
        if (i2 != 0) {
            long uptimeMillis = SystemClock.uptimeMillis();
            int i3 = i2;
            KeyEvent keyEvent = r16;
            KeyEvent keyEvent2 = new KeyEvent(uptimeMillis, uptimeMillis, 0, i2, 0);
            boolean onMediaButtonDown = onMediaButtonDown(i3, keyEvent);
            i3 = i2;
            keyEvent = r16;
            keyEvent2 = new KeyEvent(uptimeMillis, uptimeMillis, 1, i2, 0);
            onMediaButtonDown = onMediaButtonUp(i3, keyEvent);
        }
    }
}
