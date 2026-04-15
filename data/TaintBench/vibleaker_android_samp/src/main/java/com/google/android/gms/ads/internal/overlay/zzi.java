package com.google.android.gms.ads.internal.overlay;

import android.annotation.TargetApi;
import android.content.Context;
import android.view.TextureView;

@TargetApi(14)
public abstract class zzi extends TextureView {
    public zzi(Context context) {
        super(context);
    }

    public abstract int getCurrentPosition();

    public abstract int getDuration();

    public abstract int getVideoHeight();

    public abstract int getVideoWidth();

    public abstract void pause();

    public abstract void play();

    public abstract void seekTo(int i);

    public abstract void setMimeType(String str);

    public abstract void setVideoPath(String str);

    public abstract void stop();

    public abstract void zza(float f);

    public abstract void zza(zzh zzh);

    public abstract String zzeZ();

    public abstract void zzff();

    public abstract void zzfg();
}
