package com.facebook.ads.internal.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.support.v4.os.EnvironmentCompat;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAdView.Type;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class m extends a {
    private final n b;
    private Type c;
    private boolean d;
    private boolean e;
    private boolean f;
    private View g;
    private List<View> h;

    public m(Context context, b bVar, n nVar) {
        super(context, bVar);
        this.b = nVar;
    }

    private String b(View view) {
        try {
            return c(view).toString();
        } catch (JSONException e) {
            return "Json exception";
        }
    }

    private JSONObject c(View view) {
        boolean z = true;
        int i = 0;
        JSONObject jSONObject = new JSONObject();
        jSONObject.putOpt(MASTNativeAdConstants.ID_STRING, Integer.valueOf(view.getId()));
        jSONObject.putOpt("class", view.getClass());
        jSONObject.putOpt("origin", String.format("{x:%d, y:%d}", new Object[]{Integer.valueOf(view.getTop()), Integer.valueOf(view.getLeft())}));
        jSONObject.putOpt("size", String.format("{h:%d, w:%d}", new Object[]{Integer.valueOf(view.getHeight()), Integer.valueOf(view.getWidth())}));
        if (this.h == null || !this.h.contains(view)) {
            z = false;
        }
        jSONObject.putOpt("clickable", Boolean.valueOf(z));
        Object obj = EnvironmentCompat.MEDIA_UNKNOWN;
        if (view instanceof TextView) {
            obj = MASTNativeAdConstants.RESPONSE_TEXT;
        } else if (view instanceof Button) {
            obj = "button";
        } else if (view instanceof ImageView) {
            obj = "image";
        } else if (view instanceof MediaView) {
            obj = "mediaview";
        } else if (view instanceof ViewGroup) {
            obj = "viewgroup";
        }
        jSONObject.putOpt("type", obj);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            JSONArray jSONArray = new JSONArray();
            while (i < viewGroup.getChildCount()) {
                jSONArray.put(c(viewGroup.getChildAt(i)));
                i++;
            }
            jSONObject.putOpt("list", jSONArray);
        }
        return jSONObject;
    }

    private String d(View view) {
        if (view.getWidth() <= 0 || view.getHeight() <= 0) {
            return "";
        }
        try {
            Bitmap createBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Config.ARGB_8888);
            createBitmap.setDensity(view.getResources().getDisplayMetrics().densityDpi);
            view.draw(new Canvas(createBitmap));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            createBitmap.compress(CompressFormat.JPEG, this.b.h(), byteArrayOutputStream);
            return Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
        } catch (Exception e) {
            return "";
        }
    }

    public void a(View view) {
        this.g = view;
    }

    public void a(Type type) {
        this.c = type;
    }

    public void a(List<View> list) {
        this.h = list;
    }

    public void a(boolean z) {
        this.d = z;
    }

    /* access modifiers changed from: protected */
    public void b() {
        if (this.b != null) {
            Map hashMap = new HashMap();
            if (this.a != null) {
                hashMap.put("mil", Boolean.valueOf(this.a.a()));
                hashMap.put("eil", Boolean.valueOf(this.a.b()));
                hashMap.put("eil_source", this.a.c());
            }
            if (this.c != null) {
                hashMap.put("nti", String.valueOf(this.c.getValue()));
            }
            if (this.d) {
                hashMap.put("nhs", String.valueOf(this.d));
            }
            if (this.e) {
                hashMap.put("nmv", String.valueOf(this.e));
            }
            if (this.f) {
                hashMap.put("nmvap", String.valueOf(this.f));
            }
            if (this.g != null && this.b.e()) {
                hashMap.put("view", b(this.g));
            }
            if (this.g != null && this.b.g()) {
                hashMap.put("snapshot", d(this.g));
            }
            this.b.a(hashMap);
        }
    }

    public void b(boolean z) {
        this.e = z;
    }

    public void c(boolean z) {
        this.f = z;
    }
}
