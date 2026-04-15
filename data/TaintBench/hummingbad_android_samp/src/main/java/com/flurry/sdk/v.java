package com.flurry.sdk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;

public class v {
    /* access modifiers changed from: private|static|final */
    public static final String a = v.class.getSimpleName();

    public static String a(eh ehVar, int i) {
        ba baVar = n.a().g;
        File a = ba.a(i, ehVar.c);
        return a == null ? ehVar.c : "file://" + a.getAbsolutePath();
    }

    /* access modifiers changed from: private */
    public void a(ImageView imageView, Bitmap bitmap) {
        hz.a.a(new y(this, imageView, bitmap));
    }

    static /* synthetic */ void a(v vVar, eh ehVar, ImageView imageView, int i) {
        ba baVar = n.a().g;
        File a = ba.a(i, ehVar.c);
        if (a == null) {
            iw.a(3, a, "Cached asset not available for image:" + ehVar.c);
            jc jcVar = new jc();
            jcVar.e = ehVar.c;
            jcVar.j = 40000;
            jcVar.f = jj.kGet;
            jcVar.d = new gp();
            jcVar.a = new x(vVar, imageView);
            hy.a().a((Object) vVar, (ma) jcVar);
            return;
        }
        iw.a(3, a, "Cached asset present for image:" + ehVar.c);
        vVar.a(imageView, BitmapFactory.decodeFile(a.getAbsolutePath()));
    }

    public final void a(eh ehVar, View view, int i) {
        if (ehVar != null && view != null) {
            switch (ehVar.b) {
                case STRING:
                    if (ehVar != null && ei.STRING.equals(ehVar.b) && view != null) {
                        if (ehVar.a.equals("callToAction") && (view instanceof Button)) {
                            Button button = (Button) view;
                            button.setText(ehVar.c);
                            if (ehVar.a.equals("callToAction")) {
                                ij aaVar = new aa();
                                aaVar.a = button;
                                aaVar.b = i;
                                il.a().a(aaVar);
                                return;
                            }
                            return;
                        } else if (view instanceof TextView) {
                            ((TextView) view).setText(ehVar.c);
                            return;
                        } else {
                            iw.c(a, "The view must be an instance of TextView in order to load the asset");
                            return;
                        }
                    }
                    return;
                case IMAGE:
                    if (ehVar != null && !TextUtils.isEmpty(ehVar.c) && ei.IMAGE.equals(ehVar.b)) {
                        if (view == null || !(view instanceof ImageView)) {
                            iw.c(a, "The view must be an instance of ImageView in order to load the asset");
                            return;
                        } else {
                            hz.a.b(new w(this, ehVar, view, i));
                            return;
                        }
                    }
                    return;
                default:
                    return;
            }
        }
    }
}
