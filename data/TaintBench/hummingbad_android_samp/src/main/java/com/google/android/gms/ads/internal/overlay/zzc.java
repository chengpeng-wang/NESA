package com.google.android.gms.ads.internal.overlay;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.os.Build.VERSION;
import android.support.v4.widget.ExploreByTouchHelper;
import android.view.Surface;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View.MeasureSpec;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.internal.zzgk;
import com.google.android.gms.internal.zzhu;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@zzgk
public class zzc extends zzi implements OnAudioFocusChangeListener, OnBufferingUpdateListener, OnCompletionListener, OnErrorListener, OnInfoListener, OnPreparedListener, OnVideoSizeChangedListener, SurfaceTextureListener {
    private static final Map<Integer, String> zzAb = new HashMap();
    private final zzp zzAc;
    private int zzAd = 0;
    private int zzAe = 0;
    private MediaPlayer zzAf;
    private Uri zzAg;
    private int zzAh;
    private int zzAi;
    private int zzAj;
    private int zzAk;
    private int zzAl;
    private float zzAm = 1.0f;
    private boolean zzAn;
    private boolean zzAo;
    private int zzAp;
    /* access modifiers changed from: private */
    public zzh zzAq;

    static {
        zzAb.put(Integer.valueOf(-1004), "MEDIA_ERROR_IO");
        zzAb.put(Integer.valueOf(-1007), "MEDIA_ERROR_MALFORMED");
        zzAb.put(Integer.valueOf(-1010), "MEDIA_ERROR_UNSUPPORTED");
        zzAb.put(Integer.valueOf(-110), "MEDIA_ERROR_TIMED_OUT");
        zzAb.put(Integer.valueOf(100), "MEDIA_ERROR_SERVER_DIED");
        zzAb.put(Integer.valueOf(1), "MEDIA_ERROR_UNKNOWN");
        zzAb.put(Integer.valueOf(1), "MEDIA_INFO_UNKNOWN");
        zzAb.put(Integer.valueOf(700), "MEDIA_INFO_VIDEO_TRACK_LAGGING");
        zzAb.put(Integer.valueOf(3), "MEDIA_INFO_VIDEO_RENDERING_START");
        zzAb.put(Integer.valueOf(701), "MEDIA_INFO_BUFFERING_START");
        zzAb.put(Integer.valueOf(702), "MEDIA_INFO_BUFFERING_END");
        zzAb.put(Integer.valueOf(800), "MEDIA_INFO_BAD_INTERLEAVING");
        zzAb.put(Integer.valueOf(801), "MEDIA_INFO_NOT_SEEKABLE");
        zzAb.put(Integer.valueOf(802), "MEDIA_INFO_METADATA_UPDATE");
        zzAb.put(Integer.valueOf(901), "MEDIA_INFO_UNSUPPORTED_SUBTITLE");
        zzAb.put(Integer.valueOf(902), "MEDIA_INFO_SUBTITLE_TIMED_OUT");
    }

    public zzc(Context context, zzp zzp) {
        super(context);
        setSurfaceTextureListener(this);
        this.zzAc = zzp;
        this.zzAc.zza((zzi) this);
    }

    private void zzb(float f) {
        if (this.zzAf != null) {
            try {
                this.zzAf.setVolume(f, f);
                return;
            } catch (IllegalStateException e) {
                return;
            }
        }
        zzb.zzaE("AdVideoView setMediaPlayerVolume() called before onPrepared().");
    }

    private void zzel() {
        zzb.v("AdVideoView init MediaPlayer");
        SurfaceTexture surfaceTexture = getSurfaceTexture();
        if (this.zzAg != null && surfaceTexture != null) {
            zzt(false);
            try {
                this.zzAf = new MediaPlayer();
                this.zzAf.setOnBufferingUpdateListener(this);
                this.zzAf.setOnCompletionListener(this);
                this.zzAf.setOnErrorListener(this);
                this.zzAf.setOnInfoListener(this);
                this.zzAf.setOnPreparedListener(this);
                this.zzAf.setOnVideoSizeChangedListener(this);
                this.zzAj = 0;
                this.zzAf.setDataSource(getContext(), this.zzAg);
                this.zzAf.setSurface(new Surface(surfaceTexture));
                this.zzAf.setAudioStreamType(3);
                this.zzAf.setScreenOnWhilePlaying(true);
                this.zzAf.prepareAsync();
                zzt(1);
            } catch (IOException | IllegalArgumentException e) {
                zzb.zzd("Failed to initialize MediaPlayer at " + this.zzAg, e);
                onError(this.zzAf, 1, 0);
            }
        }
    }

    private void zzem() {
        if (zzep() && this.zzAf.getCurrentPosition() > 0 && this.zzAe != 3) {
            zzb.v("AdVideoView nudging MediaPlayer");
            zzb(0.0f);
            this.zzAf.start();
            int currentPosition = this.zzAf.getCurrentPosition();
            long currentTimeMillis = zzp.zzbB().currentTimeMillis();
            while (zzep() && this.zzAf.getCurrentPosition() == currentPosition) {
                if (zzp.zzbB().currentTimeMillis() - currentTimeMillis > 250) {
                    break;
                }
            }
            this.zzAf.pause();
            zzeu();
        }
    }

    private void zzen() {
        AudioManager zzev = zzev();
        if (zzev != null && !this.zzAo) {
            if (zzev.requestAudioFocus(this, 3, 2) == 1) {
                zzes();
            } else {
                zzb.zzaE("AdVideoView audio focus request failed");
            }
        }
    }

    private void zzeo() {
        zzb.v("AdVideoView abandon audio focus");
        AudioManager zzev = zzev();
        if (zzev != null && this.zzAo) {
            if (zzev.abandonAudioFocus(this) == 1) {
                this.zzAo = false;
            } else {
                zzb.zzaE("AdVideoView abandon audio focus failed");
            }
        }
    }

    private boolean zzep() {
        return (this.zzAf == null || this.zzAd == -1 || this.zzAd == 0 || this.zzAd == 1) ? false : true;
    }

    private void zzes() {
        zzb.v("AdVideoView audio focus gained");
        this.zzAo = true;
        zzeu();
    }

    private void zzet() {
        zzb.v("AdVideoView audio focus lost");
        this.zzAo = false;
        zzeu();
    }

    private void zzeu() {
        if (this.zzAn || !this.zzAo) {
            zzb(0.0f);
        } else {
            zzb(this.zzAm);
        }
    }

    private AudioManager zzev() {
        return (AudioManager) getContext().getSystemService("audio");
    }

    private void zzt(int i) {
        if (i == 3) {
            this.zzAc.zzeY();
        } else if (this.zzAd == 3 && i != 3) {
            this.zzAc.zzeZ();
        }
        this.zzAd = i;
    }

    private void zzt(boolean z) {
        zzb.v("AdVideoView release");
        if (this.zzAf != null) {
            this.zzAf.reset();
            this.zzAf.release();
            this.zzAf = null;
            zzt(0);
            if (z) {
                this.zzAe = 0;
                zzu(0);
            }
            zzeo();
        }
    }

    private void zzu(int i) {
        this.zzAe = i;
    }

    public int getCurrentPosition() {
        return zzep() ? this.zzAf.getCurrentPosition() : 0;
    }

    public int getDuration() {
        return zzep() ? this.zzAf.getDuration() : -1;
    }

    public int getVideoHeight() {
        return this.zzAf != null ? this.zzAf.getVideoHeight() : 0;
    }

    public int getVideoWidth() {
        return this.zzAf != null ? this.zzAf.getVideoWidth() : 0;
    }

    public void onAudioFocusChange(int focusChange) {
        if (focusChange > 0) {
            zzes();
        } else if (focusChange < 0) {
            zzet();
        }
    }

    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        this.zzAj = percent;
    }

    public void onCompletion(MediaPlayer mp) {
        zzb.v("AdVideoView completion");
        zzt(5);
        zzu(5);
        zzhu.zzHK.post(new Runnable() {
            public void run() {
                if (zzc.this.zzAq != null) {
                    zzc.this.zzAq.zzeN();
                }
            }
        });
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        final String str = (String) zzAb.get(Integer.valueOf(what));
        final String str2 = (String) zzAb.get(Integer.valueOf(extra));
        zzb.zzaE("AdVideoView MediaPlayer error: " + str + ":" + str2);
        zzt(-1);
        zzu(-1);
        zzhu.zzHK.post(new Runnable() {
            public void run() {
                if (zzc.this.zzAq != null) {
                    zzc.this.zzAq.zzh(str, str2);
                }
            }
        });
        return true;
    }

    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        String str = (String) zzAb.get(Integer.valueOf(extra));
        zzb.v("AdVideoView MediaPlayer info: " + ((String) zzAb.get(Integer.valueOf(what))) + ":" + str);
        return true;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int defaultSize = getDefaultSize(this.zzAh, widthMeasureSpec);
        int defaultSize2 = getDefaultSize(this.zzAi, heightMeasureSpec);
        if (this.zzAh > 0 && this.zzAi > 0) {
            int mode = MeasureSpec.getMode(widthMeasureSpec);
            int size = MeasureSpec.getSize(widthMeasureSpec);
            int mode2 = MeasureSpec.getMode(heightMeasureSpec);
            defaultSize2 = MeasureSpec.getSize(heightMeasureSpec);
            if (mode == 1073741824 && mode2 == 1073741824) {
                if (this.zzAh * defaultSize2 < this.zzAi * size) {
                    defaultSize = (this.zzAh * defaultSize2) / this.zzAi;
                } else if (this.zzAh * defaultSize2 > this.zzAi * size) {
                    defaultSize2 = (this.zzAi * size) / this.zzAh;
                    defaultSize = size;
                } else {
                    defaultSize = size;
                }
            } else if (mode == 1073741824) {
                defaultSize = (this.zzAi * size) / this.zzAh;
                if (mode2 != ExploreByTouchHelper.INVALID_ID || defaultSize <= defaultSize2) {
                    defaultSize2 = defaultSize;
                    defaultSize = size;
                } else {
                    defaultSize = size;
                }
            } else if (mode2 == 1073741824) {
                defaultSize = (this.zzAh * defaultSize2) / this.zzAi;
                if (mode == ExploreByTouchHelper.INVALID_ID && defaultSize > size) {
                    defaultSize = size;
                }
            } else {
                int i = this.zzAh;
                defaultSize = this.zzAi;
                if (mode2 != ExploreByTouchHelper.INVALID_ID || defaultSize <= defaultSize2) {
                    defaultSize2 = defaultSize;
                    defaultSize = i;
                } else {
                    defaultSize = (this.zzAh * defaultSize2) / this.zzAi;
                }
                if (mode == ExploreByTouchHelper.INVALID_ID && defaultSize > size) {
                    defaultSize2 = (this.zzAi * size) / this.zzAh;
                    defaultSize = size;
                }
            }
        }
        setMeasuredDimension(defaultSize, defaultSize2);
        if (VERSION.SDK_INT == 16) {
            if ((this.zzAk > 0 && this.zzAk != defaultSize) || (this.zzAl > 0 && this.zzAl != defaultSize2)) {
                zzem();
            }
            this.zzAk = defaultSize;
            this.zzAl = defaultSize2;
        }
    }

    public void onPrepared(MediaPlayer mediaPlayer) {
        zzb.v("AdVideoView prepared");
        zzt(2);
        this.zzAc.zzeL();
        zzhu.zzHK.post(new Runnable() {
            public void run() {
                if (zzc.this.zzAq != null) {
                    zzc.this.zzAq.zzeL();
                }
            }
        });
        this.zzAh = mediaPlayer.getVideoWidth();
        this.zzAi = mediaPlayer.getVideoHeight();
        if (this.zzAp != 0) {
            seekTo(this.zzAp);
        }
        zzem();
        zzb.zzaD("AdVideoView stream dimensions: " + this.zzAh + " x " + this.zzAi);
        if (this.zzAe == 3) {
            play();
        }
        zzen();
        zzeu();
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        zzb.v("AdVideoView surface created");
        zzel();
        zzhu.zzHK.post(new Runnable() {
            public void run() {
                if (zzc.this.zzAq != null) {
                    zzc.this.zzAq.zzeK();
                }
            }
        });
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        zzb.v("AdVideoView surface destroyed");
        if (this.zzAf != null && this.zzAp == 0) {
            this.zzAp = this.zzAf.getCurrentPosition();
        }
        zzhu.zzHK.post(new Runnable() {
            public void run() {
                if (zzc.this.zzAq != null) {
                    zzc.this.zzAq.onPaused();
                    zzc.this.zzAq.zzeO();
                }
            }
        });
        zzt(true);
        return true;
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int w, int h) {
        Object obj = 1;
        zzb.v("AdVideoView surface changed");
        Object obj2 = this.zzAe == 3 ? 1 : null;
        if (!(this.zzAh == w && this.zzAi == h)) {
            obj = null;
        }
        if (this.zzAf != null && obj2 != null && obj != null) {
            if (this.zzAp != 0) {
                seekTo(this.zzAp);
            }
            play();
        }
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        this.zzAc.zzb(this);
    }

    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        zzb.v("AdVideoView size changed: " + width + " x " + height);
        this.zzAh = mp.getVideoWidth();
        this.zzAi = mp.getVideoHeight();
        if (this.zzAh != 0 && this.zzAi != 0) {
            requestLayout();
        }
    }

    public void pause() {
        zzb.v("AdVideoView pause");
        if (zzep() && this.zzAf.isPlaying()) {
            this.zzAf.pause();
            zzt(4);
            zzhu.zzHK.post(new Runnable() {
                public void run() {
                    if (zzc.this.zzAq != null) {
                        zzc.this.zzAq.onPaused();
                    }
                }
            });
        }
        zzu(4);
    }

    public void play() {
        zzb.v("AdVideoView play");
        if (zzep()) {
            this.zzAf.start();
            zzt(3);
            zzhu.zzHK.post(new Runnable() {
                public void run() {
                    if (zzc.this.zzAq != null) {
                        zzc.this.zzAq.zzeM();
                    }
                }
            });
        }
        zzu(3);
    }

    public void seekTo(int millis) {
        zzb.v("AdVideoView seek " + millis);
        if (zzep()) {
            this.zzAf.seekTo(millis);
            this.zzAp = 0;
            return;
        }
        this.zzAp = millis;
    }

    public void setMimeType(String mimeType) {
    }

    public void setVideoPath(String path) {
        setVideoURI(Uri.parse(path));
    }

    public void setVideoURI(Uri uri) {
        this.zzAg = uri;
        this.zzAp = 0;
        zzel();
        requestLayout();
        invalidate();
    }

    public void stop() {
        zzb.v("AdVideoView stop");
        if (this.zzAf != null) {
            this.zzAf.stop();
            this.zzAf.release();
            this.zzAf = null;
            zzt(0);
            zzu(0);
            zzeo();
        }
        this.zzAc.onStop();
    }

    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }

    public void zza(float f) {
        this.zzAm = f;
        zzeu();
    }

    public void zza(zzh zzh) {
        this.zzAq = zzh;
    }

    public String zzek() {
        return "MediaPlayer";
    }

    public void zzeq() {
        this.zzAn = true;
        zzeu();
    }

    public void zzer() {
        this.zzAn = false;
        zzeu();
    }
}
