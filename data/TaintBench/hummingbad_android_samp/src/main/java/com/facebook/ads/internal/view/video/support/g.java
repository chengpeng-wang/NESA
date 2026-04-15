package com.facebook.ads.internal.view.video.support;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.view.View;
import android.widget.VideoView;

public class g extends VideoView implements OnPreparedListener, e {
    private View a;
    private Uri b;
    private b c;

    public g(Context context) {
        super(context);
    }

    public void a(View view, Uri uri) {
        this.a = view;
        this.b = uri;
        setOnPreparedListener(this);
    }

    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.setLooping(true);
        mediaPlayer.setOnInfoListener(new c(this.a));
        if (this.c != null) {
            this.c.a(mediaPlayer);
        }
    }

    public void pause() {
        this.a.setVisibility(0);
        stopPlayback();
    }

    public void setFrameVideoViewListener(b bVar) {
        this.c = bVar;
    }

    public void start() {
        setVideoURI(this.b);
        super.start();
    }
}
