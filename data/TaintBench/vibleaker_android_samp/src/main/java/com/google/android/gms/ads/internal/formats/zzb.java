package com.google.android.gms.ads.internal.formats;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.google.android.gms.ads.internal.client.zzn;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzhb;
import java.util.List;

@zzhb
class zzb extends RelativeLayout {
    private static final float[] zzxR = new float[]{5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f};
    private final RelativeLayout zzxS;
    private AnimationDrawable zzxT;

    public zzb(Context context, zza zza) {
        super(context);
        zzx.zzz(zza);
        LayoutParams layoutParams = new LayoutParams(-2, -2);
        layoutParams.addRule(10);
        layoutParams.addRule(11);
        Drawable shapeDrawable = new ShapeDrawable(new RoundRectShape(zzxR, null, null));
        shapeDrawable.getPaint().setColor(zza.getBackgroundColor());
        this.zzxS = new RelativeLayout(context);
        this.zzxS.setLayoutParams(layoutParams);
        zzr.zzbE().zza(this.zzxS, shapeDrawable);
        layoutParams = new LayoutParams(-2, -2);
        if (!TextUtils.isEmpty(zza.getText())) {
            LayoutParams layoutParams2 = new LayoutParams(-2, -2);
            TextView textView = new TextView(context);
            textView.setLayoutParams(layoutParams2);
            textView.setId(1195835393);
            textView.setTypeface(Typeface.DEFAULT);
            textView.setText(zza.getText());
            textView.setTextColor(zza.getTextColor());
            textView.setTextSize((float) zza.getTextSize());
            textView.setPadding(zzn.zzcS().zzb(context, 4), 0, zzn.zzcS().zzb(context, 4), 0);
            this.zzxS.addView(textView);
            layoutParams.addRule(1, textView.getId());
        }
        View imageView = new ImageView(context);
        imageView.setLayoutParams(layoutParams);
        imageView.setId(1195835394);
        List<Drawable> zzdG = zza.zzdG();
        if (zzdG.size() > 1) {
            this.zzxT = new AnimationDrawable();
            for (Drawable addFrame : zzdG) {
                this.zzxT.addFrame(addFrame, zza.zzdH());
            }
            zzr.zzbE().zza(imageView, this.zzxT);
        } else if (zzdG.size() == 1) {
            imageView.setImageDrawable((Drawable) zzdG.get(0));
        }
        this.zzxS.addView(imageView);
        addView(this.zzxS);
    }

    public void onAttachedToWindow() {
        if (this.zzxT != null) {
            this.zzxT.start();
        }
        super.onAttachedToWindow();
    }

    public ViewGroup zzdI() {
        return this.zzxS;
    }
}
