package com.baidu.mapapi.search;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.baidu.mapapi.search.a.a;
import com.baidu.platform.comapi.d.c;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import org.jivesoftware.smackx.Form;

public class PlaceCaterActivity extends Activity implements a {
    static ImageView c;
    static boolean d;
    static DisplayMetrics o;
    static Hashtable<Integer, View> q = new Hashtable();
    static Handler r = new f();
    private static int s = -2;
    private static int t = -1;
    private static int u = 10;
    private static int v = 5;
    private static int w = 1;
    private static int x = -7566196;
    private static int y = -12487463;
    private static int z = -1710619;
    TextView a;
    TextView b;
    LinearLayout e;
    TextView f;
    TextView g;
    TextView h;
    TextView i;
    TextView j;
    TextView k;
    TextView l;
    TextView m;
    LinearLayout n;
    e p = new e();

    private Bitmap a(String str) {
        try {
            return BitmapFactory.decodeStream(getAssets().open(str));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void a(LinearLayout linearLayout, List<d> list) {
        if (list != null && list.size() > 0) {
            this.n.removeAllViews();
            q.clear();
            int size = list.size();
            int i = (size / 2) + (size % 2);
            for (int i2 = 0; i2 < i; i2++) {
                LinearLayout linearLayout2 = new LinearLayout(this);
                linearLayout2.setLayoutParams(new LayoutParams(t, s));
                linearLayout.addView(linearLayout2);
                LinearLayout linearLayout3 = new LinearLayout(this);
                linearLayout3.setOrientation(0);
                linearLayout3.setLayoutParams(new LayoutParams(t, s));
                linearLayout3.setPadding(20, 5, 5, 5);
                linearLayout2.addView(linearLayout3);
                ((LinearLayout.LayoutParams) linearLayout3.getLayoutParams()).weight = 1.0f;
                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(new LayoutParams((int) (22.0f * o.density), (int) (22.0f * o.density)));
                imageView.setTag(Integer.valueOf(i2 * 2));
                a.a(linearLayout.hashCode(), (i2 * 2) + 1, d.a.replaceAll("#replace#", ((d) list.get(i2 * 2)).d), this);
                q.put(Integer.valueOf((i2 * 2) + 1), imageView);
                linearLayout3.addView(imageView);
                ((LinearLayout.LayoutParams) imageView.getLayoutParams()).gravity = 17;
                TextView textView = new TextView(this);
                textView.setTag(list.get(i2 * 2));
                textView.setPadding(u, u, u, u);
                textView.setLayoutParams(new LayoutParams(s, s));
                textView.setClickable(true);
                textView.setText(((d) list.get(i2 * 2)).b);
                textView.setTextColor(y);
                textView.setOnClickListener(new h(this));
                linearLayout3.addView(textView);
                ((LinearLayout.LayoutParams) textView.getLayoutParams()).gravity = 17;
                if ((i2 * 2) + 1 < size) {
                    linearLayout3 = new LinearLayout(this);
                    linearLayout3.setPadding(20, 5, 5, 5);
                    linearLayout3.setLayoutParams(new LayoutParams(t, s));
                    linearLayout2.addView(linearLayout3);
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linearLayout3.getLayoutParams();
                    layoutParams.weight = 1.0f;
                    layoutParams.gravity = 17;
                    ImageView imageView2 = new ImageView(this);
                    imageView2.setLayoutParams(new LayoutParams((int) (22.0f * o.density), (int) (22.0f * o.density)));
                    list.get((i2 * 2) + 1);
                    a.a(linearLayout.hashCode(), ((i2 * 2) + 1) + 1, d.a.replaceAll("#replace#", ((d) list.get((i2 * 2) + 1)).d), this);
                    q.put(Integer.valueOf(((i2 * 2) + 1) + 1), imageView2);
                    linearLayout3.addView(imageView2);
                    ((LinearLayout.LayoutParams) imageView2.getLayoutParams()).gravity = 16;
                    TextView textView2 = new TextView(this);
                    textView2.setTag(list.get((i2 * 2) + 1));
                    textView2.setPadding(u, u, u, u);
                    textView2.setClickable(true);
                    textView2.setTextColor(y);
                    textView2.setText(((d) list.get((i2 * 2) + 1)).b);
                    textView2.setOnClickListener(new i(this));
                    linearLayout3.addView(textView2);
                    ((LinearLayout.LayoutParams) textView2.getLayoutParams()).gravity = 17;
                }
            }
        }
    }

    public static boolean isShow() {
        return d;
    }

    /* access modifiers changed from: 0000 */
    public void a(float f) {
        if (this.e != null) {
            this.e.removeAllViews();
            int i = (int) f;
            for (int i2 = 0; i2 < 5; i2++) {
                ImageView imageView;
                if (i2 < i) {
                    imageView = new ImageView(this);
                    imageView.setImageBitmap(a("place/star_light.png"));
                    imageView.setLayoutParams(new LayoutParams((int) (o.density * 20.0f), (int) (o.density * 20.0f)));
                    imageView.setPadding(1, 1, 1, 1);
                    this.e.addView(imageView);
                } else {
                    imageView = new ImageView(this);
                    imageView.setImageBitmap(a("place/star_gray.png"));
                    imageView.setLayoutParams(new LayoutParams((int) (o.density * 20.0f), (int) (o.density * 20.0f)));
                    imageView.setPadding(1, 1, 1, 1);
                    this.e.addView(imageView);
                }
            }
            TextView textView = new TextView(this);
            textView.setLayoutParams(new LayoutParams(s, s));
            textView.setText(Float.toString(f));
            textView.setPadding(10, 0, 10, 0);
            textView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
            this.e.addView(textView);
        }
    }

    /* access modifiers changed from: 0000 */
    public void a(DisplayMetrics displayMetrics) {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LayoutParams(-1, -1));
        linearLayout.setOrientation(1);
        linearLayout.setBackgroundColor(-3355444);
        linearLayout.setPadding(1, 1, 1, 1);
        LinearLayout linearLayout2 = new LinearLayout(this);
        linearLayout2.setPadding(1, 1, 1, 1);
        linearLayout2.setBackgroundColor(-1);
        linearLayout2.setLayoutParams(new LayoutParams(t, s));
        linearLayout2.setOrientation(1);
        linearLayout.addView(linearLayout2);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linearLayout2.getLayoutParams();
        layoutParams.rightMargin = w;
        layoutParams.bottomMargin = w;
        layoutParams.topMargin = w;
        layoutParams.leftMargin = w;
        this.a = new TextView(this);
        this.a.setLayoutParams(new LayoutParams(t, s));
        this.a.setTextSize(18.0f);
        this.a.setText("");
        this.a.setPadding(v, v, v, v);
        this.a.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        this.a.setTypeface(Typeface.DEFAULT, 1);
        linearLayout2.addView(this.a);
        ((LinearLayout.LayoutParams) this.a.getLayoutParams()).leftMargin = 1;
        this.b = new TextView(this);
        this.b.setLayoutParams(new LayoutParams(t, s));
        this.b.setTextSize(16.0f);
        this.b.setPadding(u, u, u, u);
        this.b.setTextColor(x);
        linearLayout2.addView(this.b);
        linearLayout2 = new LinearLayout(this);
        linearLayout2.setBackgroundColor(-1);
        linearLayout2.setLayoutParams(new LayoutParams(-1, -2));
        linearLayout.addView(linearLayout2);
        layoutParams = (LinearLayout.LayoutParams) linearLayout2.getLayoutParams();
        layoutParams.topMargin = w;
        layoutParams.rightMargin = w;
        layoutParams.bottomMargin = w;
        layoutParams.leftMargin = w;
        c = new ImageView(this);
        c.setPadding(5, 5, 5, 5);
        c.setLayoutParams(new LayoutParams((int) (120.0f * displayMetrics.density), (int) (90.0f * displayMetrics.density)));
        linearLayout2.addView(c);
        LinearLayout linearLayout3 = new LinearLayout(this);
        linearLayout3.setOrientation(1);
        linearLayout3.setLayoutParams(new LayoutParams(t, s));
        linearLayout3.setPadding(u, u, u, u);
        linearLayout2.addView(linearLayout3);
        ((LinearLayout.LayoutParams) linearLayout3.getLayoutParams()).gravity = 16;
        this.e = new LinearLayout(this);
        this.e.setPadding(2, 2, 2, 2);
        this.e.setOrientation(0);
        linearLayout3.addView(this.e);
        LinearLayout linearLayout4 = new LinearLayout(this);
        linearLayout4.setPadding(2, 2, 2, 2);
        linearLayout4.setLayoutParams(new LayoutParams(s, s));
        linearLayout3.addView(linearLayout4);
        TextView textView = new TextView(this);
        textView.setTextColor(x);
        textView.setTextSize(16.0f);
        textView.setText("参考价：");
        linearLayout4.addView(textView);
        this.f = new TextView(this);
        this.f.setTextColor(-4712681);
        this.f.setTextSize(16.0f);
        linearLayout4.addView(this.f);
        linearLayout4 = new LinearLayout(this);
        linearLayout4.setPadding(2, 2, 2, 2);
        linearLayout3.addView(linearLayout4);
        this.g = new TextView(this);
        this.g.setPadding(0, 0, 5, 0);
        this.g.setText("口味:3.0");
        this.g.setTextColor(x);
        this.g.setTextSize(12.0f);
        linearLayout4.addView(this.g);
        this.h = new TextView(this);
        this.h.setPadding(0, 0, 5, 0);
        this.h.setText("服务:3.0");
        this.h.setTextColor(x);
        this.h.setTextSize(12.0f);
        linearLayout4.addView(this.h);
        this.i = new TextView(this);
        this.i.setPadding(0, 0, 5, 0);
        this.i.setText("环境:3.0");
        this.i.setTextColor(x);
        this.i.setTextSize(12.0f);
        linearLayout4.addView(this.i);
        linearLayout2 = new LinearLayout(this);
        linearLayout2.setBackgroundColor(-1);
        linearLayout2.setPadding(5, 5, 5, 5);
        linearLayout2.setLayoutParams(new LayoutParams(-1, -2));
        linearLayout2.setOrientation(0);
        linearLayout.addView(linearLayout2);
        layoutParams = (LinearLayout.LayoutParams) linearLayout2.getLayoutParams();
        layoutParams.topMargin = w;
        layoutParams.rightMargin = w;
        layoutParams.bottomMargin = w;
        layoutParams.leftMargin = w;
        linearLayout2.setOnClickListener(new g(this));
        ImageView imageView = new ImageView(this);
        imageView.setPadding(5, 5, 5, 5);
        imageView.setLayoutParams(new LayoutParams((int) (35.0f * displayMetrics.density), (int) (35.0f * displayMetrics.density)));
        imageView.setImageBitmap(a("place/iconphone.png"));
        linearLayout2.addView(imageView);
        ((LinearLayout.LayoutParams) imageView.getLayoutParams()).gravity = 16;
        this.j = new TextView(this);
        this.j.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        this.j.setText("(010)4343243");
        this.j.setPadding(5, 5, 5, 5);
        this.j.setTextSize(16.0f);
        this.j.setLayoutParams(new LayoutParams(s, s));
        linearLayout2.addView(this.j);
        layoutParams = (LinearLayout.LayoutParams) this.j.getLayoutParams();
        layoutParams.weight = 1.0f;
        layoutParams.gravity = 16;
        imageView = new ImageView(this);
        imageView.setLayoutParams(new LayoutParams(s, s));
        imageView.setImageBitmap(a("place/arrow.png"));
        imageView.setPadding(5, 5, 5, 10);
        linearLayout2.addView(imageView);
        ((LinearLayout.LayoutParams) imageView.getLayoutParams()).gravity = 16;
        linearLayout2 = new LinearLayout(this);
        linearLayout2.setBackgroundColor(z);
        linearLayout2.setLayoutParams(new LayoutParams(t, s));
        linearLayout2.setOrientation(1);
        linearLayout.addView(linearLayout2);
        layoutParams = (LinearLayout.LayoutParams) linearLayout2.getLayoutParams();
        layoutParams.topMargin = w;
        layoutParams.rightMargin = w;
        layoutParams.bottomMargin = w;
        layoutParams.leftMargin = w;
        TextView textView2 = new TextView(this);
        textView2.setTextSize(18.0f);
        textView2.setText("商户简介");
        textView2.setPadding(v, v, v, v);
        textView2.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        textView2.setLayoutParams(new LayoutParams(s, s));
        linearLayout2.addView(textView2);
        this.k = new TextView(this);
        this.k.setBackgroundColor(-1);
        this.k.setTextColor(x);
        this.k.setPadding(u, u, u, u);
        this.k.setTextSize(16.0f);
        this.k.setLayoutParams(new LayoutParams(t, s));
        linearLayout2.addView(this.k);
        this.l = new TextView(this);
        this.l.setBackgroundColor(-1);
        this.l.setTextColor(x);
        this.l.setPadding(u, u, u, u);
        this.l.setTextSize(16.0f);
        this.l.setLayoutParams(new LayoutParams(t, s));
        linearLayout2.addView(this.l);
        linearLayout2 = new LinearLayout(this);
        linearLayout2.setBackgroundColor(z);
        linearLayout2.setOrientation(1);
        linearLayout2.setLayoutParams(new LayoutParams(t, s));
        linearLayout.addView(linearLayout2);
        layoutParams = (LinearLayout.LayoutParams) linearLayout2.getLayoutParams();
        layoutParams.topMargin = w;
        layoutParams.rightMargin = w;
        layoutParams.bottomMargin = w;
        layoutParams.leftMargin = w;
        textView2 = new TextView(this);
        textView2.setLayoutParams(new LayoutParams(t, s));
        textView2.setText("评论信息");
        textView2.setPadding(v, v, v, v);
        textView2.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        textView2.setTextSize(18.0f);
        linearLayout2.addView(textView2);
        this.m = new TextView(this);
        this.m.setPadding(u, u, u, u);
        this.m.setBackgroundColor(-1);
        this.m.setLayoutParams(new LayoutParams(t, s));
        this.m.setTextSize(16.0f);
        this.m.setTextColor(x);
        linearLayout2.addView(this.m);
        linearLayout2 = new LinearLayout(this);
        linearLayout2.setBackgroundColor(z);
        linearLayout2.setOrientation(1);
        linearLayout2.setLayoutParams(new LayoutParams(t, s));
        linearLayout.addView(linearLayout2);
        layoutParams = (LinearLayout.LayoutParams) linearLayout2.getLayoutParams();
        layoutParams.topMargin = w;
        layoutParams.rightMargin = w;
        layoutParams.bottomMargin = w;
        layoutParams.leftMargin = w;
        textView2 = new TextView(this);
        textView2.setLayoutParams(new LayoutParams(s, s));
        textView2.setTextSize(18.0f);
        textView2.setPadding(v, v, v, v);
        textView2.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        textView2.setText("查看更多");
        linearLayout2.addView(textView2);
        this.n = new LinearLayout(this);
        this.n.setOrientation(1);
        this.n.setBackgroundColor(-1);
        this.n.setLayoutParams(new LayoutParams(t, s));
        linearLayout2.addView(this.n);
        ScrollView scrollView = new ScrollView(this);
        scrollView.setPadding(5, 5, 0, 5);
        scrollView.setLayoutParams(new LayoutParams(t, t));
        scrollView.setBackgroundColor(-526345);
        scrollView.addView(linearLayout);
        ((FrameLayout.LayoutParams) linearLayout.getLayoutParams()).rightMargin = 5;
        setContentView(scrollView);
    }

    /* access modifiers changed from: 0000 */
    public void a(e eVar) {
        float floatValue;
        this.a.setText(eVar.a);
        this.b.setText("地址：" + eVar.b);
        this.f.setText("￥" + eVar.g);
        this.g.setText("口味:" + eVar.h);
        this.h.setText("服务:" + eVar.j);
        this.i.setText("环境:" + eVar.i);
        this.j.setText(eVar.c);
        if (eVar.l == null || "".equals(eVar.l)) {
            this.k.setVisibility(8);
        } else {
            this.k.setVisibility(0);
            this.k.setText("推荐菜：" + eVar.l);
        }
        if (eVar.k == null || "".equals(eVar.k)) {
            this.l.setVisibility(8);
        } else {
            this.l.setVisibility(0);
            this.l.setText("商户描述：" + eVar.k);
        }
        if (eVar.m == null || "".equals(eVar.m)) {
            this.m.setVisibility(8);
        } else {
            this.m.setVisibility(0);
            this.m.setText(eVar.m);
        }
        if (eVar.e != null) {
            a.a(c.hashCode(), 0, eVar.e, this);
        }
        try {
            floatValue = Float.valueOf(eVar.f).floatValue();
        } catch (NumberFormatException e) {
            NumberFormatException numberFormatException = e;
            floatValue = 0.0f;
            numberFormatException.printStackTrace();
        }
        a(floatValue);
        a(this.n, eVar.o);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        d = true;
        Bundle extras = getIntent().getExtras();
        if (!c.a(extras.getString(Form.TYPE_RESULT), this.p)) {
            this.p.a = extras.getString("name");
            this.p.b = extras.getString("addr");
            this.p.c = extras.getString("tel");
            this.p.d = extras.getString("uid");
            this.p.e = extras.getString("image");
            this.p.f = extras.getString("overall_rating");
            this.p.g = extras.getString("price");
            this.p.h = extras.getString("taste_rating");
            this.p.i = extras.getString("enviroment_raing");
            this.p.j = extras.getString("service_rating");
            this.p.k = extras.getString("description");
            this.p.l = extras.getString("recommendation");
            this.p.m = extras.getString("review");
            this.p.n = extras.getString("user_logo");
            String[] stringArray = extras.getStringArray("aryMoreLinkName");
            String[] stringArray2 = extras.getStringArray("aryMoreLinkUrl");
            String[] stringArray3 = extras.getStringArray("aryMoreLinkCnName");
            if (!(stringArray == null || stringArray2 == null)) {
                for (int i = 0; i < stringArray2.length; i++) {
                    if (!"dianping".equals(stringArray[i])) {
                        d dVar = new d();
                        dVar.d = stringArray[i];
                        dVar.c = stringArray2[i];
                        dVar.b = stringArray3[i];
                        this.p.o.add(dVar);
                    }
                }
            }
        }
        com.baidu.platform.comapi.c.a.a().c();
        o = getResources().getDisplayMetrics();
        a(o);
        a(this.p);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        if (q != null) {
            q.clear();
        }
        o = null;
        c = null;
        d = false;
        com.baidu.platform.comapi.c.a.b();
        super.onDestroy();
    }

    public void onError(int i, int i2, String str, Object obj) {
    }

    public void onOk(int i, int i2, String str, Object obj) {
        Message obtainMessage;
        if (i == c.hashCode()) {
            obtainMessage = r.obtainMessage(1);
            obtainMessage.obj = obj;
            obtainMessage.sendToTarget();
        } else if (i == this.n.hashCode()) {
            obtainMessage = r.obtainMessage(2);
            obtainMessage.obj = obj;
            obtainMessage.arg1 = i2;
            obtainMessage.sendToTarget();
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        com.baidu.platform.comapi.c.a.a().d();
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        if (this.p.c == null || "".equals(this.p.c)) {
            com.baidu.platform.comapi.c.a.a().a("pkgname", c.p());
            com.baidu.platform.comapi.c.a.a().a("place_notel_show");
        } else {
            com.baidu.platform.comapi.c.a.a().a("pkgname", c.p());
            com.baidu.platform.comapi.c.a.a().a("place_tel_show");
        }
        super.onResume();
    }
}
