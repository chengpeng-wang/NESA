package com.facebook.ads.internal.view.video;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.duapps.ad.AdError;
import com.facebook.ads.InterstitialAdActivity;
import com.facebook.ads.InterstitialAdActivity.Type;
import com.facebook.ads.NativeAdVideoActivity;
import com.facebook.ads.internal.adapters.d;
import com.facebook.ads.internal.util.o;
import com.facebook.ads.internal.util.t;
import java.util.HashMap;

public class a extends FrameLayout {
    /* access modifiers changed from: private */
    public com.facebook.ads.internal.view.video.support.a a;
    private d b;
    /* access modifiers changed from: private */
    public c c;
    private String d;
    private String e;
    private String f;
    private boolean g;
    private boolean h;
    /* access modifiers changed from: private */
    public int i;
    /* access modifiers changed from: private */
    public Handler j;
    /* access modifiers changed from: private */
    public Handler k;
    private Runnable l;
    private Runnable m;
    private float n;

    class c extends Button {
        private Paint b;
        private Path c;
        private Path d;
        private Path e;
        private int f;
        private boolean g;

        public c(Context context) {
            super(context);
            a();
        }

        private void a() {
            this.f = 60;
            this.c = new Path();
            this.d = new Path();
            this.e = new Path();
            this.b = new Paint() {
                {
                    setStyle(Style.FILL_AND_STROKE);
                    setStrokeCap(Cap.ROUND);
                    setStrokeWidth(3.0f);
                    setAntiAlias(true);
                    setColor(-1);
                }
            };
            b();
            setClickable(true);
            setBackgroundColor(0);
        }

        /* access modifiers changed from: private */
        public void a(boolean z) {
            this.g = z;
            refreshDrawableState();
            invalidate();
        }

        private void b() {
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            LayoutParams layoutParams = new LayoutParams((int) (((float) this.f) * displayMetrics.density), (int) (displayMetrics.density * ((float) this.f)));
            layoutParams.addRule(9);
            layoutParams.addRule(12);
            setLayoutParams(layoutParams);
        }

        /* access modifiers changed from: private */
        public boolean c() {
            return this.g;
        }

        /* access modifiers changed from: protected */
        public void onDraw(Canvas canvas) {
            if (canvas.isHardwareAccelerated() && VERSION.SDK_INT < 17) {
                setLayerType(1, null);
            }
            float max = ((float) Math.max(canvas.getWidth(), canvas.getHeight())) / 100.0f;
            if (c()) {
                this.e.rewind();
                this.e.moveTo(26.5f * max, 15.5f * max);
                this.e.lineTo(26.5f * max, 84.5f * max);
                this.e.lineTo(82.5f * max, 50.5f * max);
                this.e.lineTo(26.5f * max, max * 15.5f);
                this.e.close();
                canvas.drawPath(this.e, this.b);
            } else {
                this.c.rewind();
                this.c.moveTo(29.0f * max, 21.0f * max);
                this.c.lineTo(29.0f * max, 79.0f * max);
                this.c.lineTo(45.0f * max, 79.0f * max);
                this.c.lineTo(45.0f * max, 21.0f * max);
                this.c.lineTo(29.0f * max, 21.0f * max);
                this.c.close();
                this.d.rewind();
                this.d.moveTo(55.0f * max, 21.0f * max);
                this.d.lineTo(55.0f * max, 79.0f * max);
                this.d.lineTo(71.0f * max, 79.0f * max);
                this.d.lineTo(71.0f * max, 21.0f * max);
                this.d.lineTo(55.0f * max, max * 21.0f);
                this.d.close();
                canvas.drawPath(this.c, this.b);
                canvas.drawPath(this.d, this.b);
            }
            super.onDraw(canvas);
        }
    }

    static final class a extends t<a> {
        public a(a aVar) {
            super(aVar);
        }

        public final void run() {
            a aVar = (a) a();
            if (aVar != null) {
                if (aVar.a.getCurrentPosition() > AdError.TIME_OUT_CODE) {
                    new o().execute(new String[]{aVar.getVideoPlayReportURI()});
                    return;
                }
                aVar.j.postDelayed(this, 250);
            }
        }
    }

    static final class b extends t<a> {
        public b(a aVar) {
            super(aVar);
        }

        public final void run() {
            a aVar = (a) a();
            if (aVar != null && aVar != null) {
                int currentPosition = aVar.a.getCurrentPosition();
                if (currentPosition > aVar.i) {
                    aVar.i = currentPosition;
                }
                aVar.k.postDelayed(this, 250);
            }
        }
    }

    public a(Context context) {
        super(context);
        e();
    }

    private void a(Context context, Intent intent) {
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(new DisplayMetrics());
        intent.putExtra("useNativeCloseButton", true);
        intent.putExtra(InterstitialAdActivity.VIEW_TYPE, Type.VIDEO);
        intent.putExtra(InterstitialAdActivity.VIDEO_URL, getVideoURI());
        intent.putExtra(InterstitialAdActivity.VIDEO_PLAY_REPORT_URL, getVideoPlayReportURI());
        intent.putExtra(InterstitialAdActivity.VIDEO_TIME_REPORT_URL, getVideoTimeReportURI());
        intent.putExtra(InterstitialAdActivity.PREDEFINED_ORIENTATION_KEY, 13);
        intent.addFlags(268435456);
    }

    private boolean a(Class<?> cls) {
        try {
            Context context = getContext();
            Intent intent = new Intent(context, cls);
            a(context, intent);
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            com.facebook.ads.internal.util.c.a(com.facebook.ads.internal.util.b.a(e, "Error occurred while loading fullscreen video activity."));
            return false;
        }
    }

    private void e() {
        this.n = 0.0f;
        RelativeLayout relativeLayout = new RelativeLayout(getContext());
        LayoutParams layoutParams = new LayoutParams(-1, -1);
        relativeLayout.setGravity(17);
        relativeLayout.setLayoutParams(layoutParams);
        setBackgroundColor(0);
        Context context = getContext();
        this.a = new com.facebook.ads.internal.view.video.support.a(context);
        this.a.setBackgroundColor(0);
        LayoutParams layoutParams2 = new LayoutParams(-1, -1);
        layoutParams2.addRule(10, -1);
        layoutParams2.addRule(12, -1);
        layoutParams2.addRule(11, -1);
        layoutParams2.addRule(9, -1);
        this.a.setLayoutParams(layoutParams2);
        this.a.setFrameVideoViewListener(new com.facebook.ads.internal.view.video.support.b() {
            public void a(MediaPlayer mediaPlayer) {
                try {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = new MediaPlayer();
                    }
                    mediaPlayer.setVolume(a.this.getVolume(), a.this.getVolume());
                    mediaPlayer.setLooping(false);
                    if (this.getAutoplay()) {
                        this.c();
                    } else {
                        this.d();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        relativeLayout.addView(this.a);
        addView(relativeLayout);
        this.k = new Handler();
        this.l = new b(this);
        this.k.postDelayed(this.l, 250);
        this.j = new Handler();
        this.m = new a(this);
        this.j.postDelayed(this.m, 250);
        this.b = new d(context, this, 50, true, new com.facebook.ads.internal.adapters.d.a() {
            public void a() {
                if (this.getAutoplay()) {
                    a.this.c();
                } else {
                    a.this.d();
                }
            }

            public void b() {
                a.this.d();
            }
        });
        this.b.a(0);
        this.b.b((int) org.xbill.DNS.Type.TSIG);
        this.b.a();
        setOnTouchListenerInternal(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() != 1 || this.getVideoURI() == null) {
                    return false;
                }
                a.this.g();
                return true;
            }
        });
        setOnClickListenerInternal(new OnClickListener() {
            public void onClick(View view) {
                if (this.getVideoURI() != null) {
                    a.this.g();
                }
            }
        });
        this.c = new c(context);
        this.c.setLayoutParams(new FrameLayout.LayoutParams(100, 100, 80));
        this.c.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() != 1) {
                    return false;
                }
                if (a.this.c.c()) {
                    a.this.c();
                    return true;
                }
                a.this.d();
                return true;
            }
        });
        this.c.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (a.this.c.c()) {
                    a.this.c();
                } else {
                    a.this.d();
                }
            }
        });
        addView(this.c);
    }

    private void f() {
        if (!this.h && getVideoTimeReportURI() != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("time", Integer.toString(this.i / 1000));
            hashMap.put("inline", "1");
            new o(hashMap).execute(new String[]{getVideoTimeReportURI()});
            this.h = true;
            this.i = 0;
        }
    }

    /* access modifiers changed from: private */
    public void g() {
        if (!a(NativeAdVideoActivity.class)) {
            a(InterstitialAdActivity.class);
        }
    }

    private void setOnClickListenerInternal(OnClickListener onClickListener) {
        super.setOnClickListener(onClickListener);
    }

    private void setOnTouchListenerInternal(OnTouchListener onTouchListener) {
        super.setOnTouchListener(onTouchListener);
    }

    public void a() {
        if (this.i > 0) {
            f();
            this.i = 0;
        }
    }

    public void b() {
        this.d = null;
    }

    public void c() {
        this.c.a(false);
        this.a.a();
    }

    public void d() {
        this.c.a(true);
        this.a.b();
    }

    public boolean getAutoplay() {
        return this.g;
    }

    public String getVideoPlayReportURI() {
        return this.e;
    }

    public String getVideoTimeReportURI() {
        return this.f;
    }

    public String getVideoURI() {
        return this.d;
    }

    /* access modifiers changed from: 0000 */
    public float getVolume() {
        return this.n;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.b.a();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        f();
        this.b.b();
    }

    public void setAutoplay(boolean z) {
        this.g = z;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
    }

    public void setOnTouchListener(OnTouchListener onTouchListener) {
    }

    public void setVideoPlayReportURI(String str) {
        this.e = str;
    }

    public void setVideoTimeReportURI(String str) {
        a();
        this.f = str;
    }

    public void setVideoURI(String str) {
        this.d = str;
        if (str != null) {
            this.a.setup(Uri.parse(str));
            if (this.g) {
                this.a.a();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void setVolume(float f) {
        this.n = f;
    }
}
