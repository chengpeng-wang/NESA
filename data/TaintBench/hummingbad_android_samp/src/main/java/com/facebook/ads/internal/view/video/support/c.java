package com.facebook.ads.internal.view.video.support;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnInfoListener;
import android.view.View;

public class c implements OnInfoListener {
    private View a;

    public c(View view) {
        this.a = view;
    }

    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i2) {
        if (i != 3) {
            return false;
        }
        this.a.setVisibility(8);
        return true;
    }
}
