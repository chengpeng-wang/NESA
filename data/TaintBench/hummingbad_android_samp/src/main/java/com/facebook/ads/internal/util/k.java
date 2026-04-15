package com.facebook.ads.internal.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import com.facebook.ads.internal.thirdparty.http.a;
import com.facebook.ads.internal.view.e;

public class k extends AsyncTask<String, Void, Bitmap[]> {
    private static final String a = k.class.getSimpleName();
    private final Context b;
    private final ImageView c;
    private final e d;
    private l e;

    public k(Context context) {
        this.b = context;
        this.d = null;
        this.c = null;
    }

    public k(ImageView imageView) {
        this.b = imageView.getContext();
        this.d = null;
        this.c = imageView;
    }

    public k(e eVar) {
        this.b = eVar.getContext();
        this.d = eVar;
        this.c = null;
    }

    public k a(l lVar) {
        this.e = lVar;
        return this;
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onPostExecute(Bitmap[] bitmapArr) {
        if (this.c != null) {
            this.c.setImageBitmap(bitmapArr[0]);
        }
        if (this.d != null) {
            this.d.a(bitmapArr[0], bitmapArr[1]);
        }
        if (this.e != null) {
            this.e.a();
        }
    }

    /* access modifiers changed from: protected|varargs */
    /* renamed from: a */
    public Bitmap[] doInBackground(String... strArr) {
        Throwable th;
        Object obj;
        Object obj2;
        Bitmap obj3;
        String obj22 = null;
        String str = strArr[0];
        Bitmap a;
        try {
            a = m.a(this.b, str);
            if (a == null) {
                try {
                    byte[] d = new a(this.b).a(str, null).d();
                    a = BitmapFactory.decodeByteArray(d, 0, d.length);
                    m.a(this.b, str, a);
                } catch (Throwable th2) {
                    th = th2;
                    obj3 = obj22;
                    Log.e(a, "Error downloading image: " + str, th);
                    c.a(b.a(th, obj22));
                    obj22 = obj3;
                    obj3 = a;
                    return new Bitmap[]{obj3, obj22};
                }
            }
            obj3 = a;
            try {
                if (!(this.d == null || obj3 == null)) {
                    try {
                        q qVar = new q(obj3);
                        qVar.a(Math.round(((float) obj3.getWidth()) / 40.0f));
                        obj22 = qVar.a();
                    } catch (Throwable th3) {
                        th = th3;
                        a = obj3;
                    }
                }
            } catch (Throwable th32) {
                th = th32;
                a = obj3;
                obj3 = obj22;
                Log.e(a, "Error downloading image: " + str, th);
                c.a(b.a(th, obj22));
                obj22 = obj3;
                obj3 = a;
                return new Bitmap[]{obj3, obj22};
            }
        } catch (Throwable th22) {
            th = th22;
            a = obj22;
            obj3 = obj22;
        }
        return new Bitmap[]{obj3, obj22};
    }
}
