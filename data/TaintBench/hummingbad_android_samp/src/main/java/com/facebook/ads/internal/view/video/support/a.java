package com.facebook.ads.internal.view.video.support;

import android.content.Context;
import android.net.Uri;
import android.os.Build.VERSION;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

public class a extends FrameLayout {
    private e a;
    private f b;
    private View c;
    private Uri d;
    private Context e;

    public a(Context context) {
        super(context);
        this.e = context;
        this.c = b(context);
        this.a = a(context);
        addView(this.c);
    }

    private e a(Context context) {
        if (VERSION.SDK_INT >= 14) {
            this.b = f.TEXTURE_VIEW;
            d dVar = new d(context);
            dVar.a(this.c, this.d);
            addView(dVar);
            return dVar;
        }
        this.b = f.VIDEO_VIEW;
        g gVar = new g(context);
        addView(gVar);
        return gVar;
    }

    private View b(Context context) {
        View view = new View(context);
        view.setBackgroundColor(-16777216);
        view.setLayoutParams(new LayoutParams(-1, -1));
        return view;
    }

    public void a() {
        this.a.start();
    }

    public void b() {
        this.a.pause();
    }

    public int getCurrentPosition() {
        return this.a.getCurrentPosition();
    }

    public View getPlaceholderView() {
        return this.c;
    }

    public f getVideoImplType() {
        return this.b;
    }

    public void setFrameVideoViewListener(b bVar) {
        this.a.setFrameVideoViewListener(bVar);
    }

    public void setVideoImpl(f fVar) {
        removeAllViews();
        if (fVar == f.TEXTURE_VIEW && VERSION.SDK_INT < 14) {
            fVar = f.VIDEO_VIEW;
        }
        this.b = fVar;
        switch (fVar) {
            case TEXTURE_VIEW:
                d dVar = new d(this.e);
                dVar.a(this.c, this.d);
                addView(dVar);
                this.a = dVar;
                break;
            case VIDEO_VIEW:
                g gVar = new g(this.e);
                gVar.a(this.c, this.d);
                addView(gVar);
                this.a = gVar;
                break;
        }
        addView(this.c);
        a();
    }

    public void setup(Uri uri) {
        this.d = uri;
        this.a.a(this.c, uri);
    }
}
