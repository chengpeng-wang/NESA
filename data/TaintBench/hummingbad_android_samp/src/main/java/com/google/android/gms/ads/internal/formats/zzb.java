package com.google.android.gms.ads.internal.formats;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.google.android.gms.ads.internal.client.zzk;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.common.internal.zzx;

class zzb extends RelativeLayout {
    private static final float[] zzvH = new float[]{5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f};
    private final RelativeLayout zzvI;

    public zzb(Context context, zza zza) {
        super(context);
        zzx.zzv(zza);
        LayoutParams layoutParams = new LayoutParams(-2, -2);
        layoutParams.addRule(10);
        layoutParams.addRule(11);
        Drawable shapeDrawable = new ShapeDrawable(new RoundRectShape(zzvH, null, null));
        shapeDrawable.getPaint().setColor(zza.getBackgroundColor());
        this.zzvI = new RelativeLayout(context);
        this.zzvI.setLayoutParams(layoutParams);
        zzp.zzbz().zza(this.zzvI, shapeDrawable);
        layoutParams = new LayoutParams(-2, -2);
        TextView textView = new TextView(context);
        textView.setLayoutParams(layoutParams);
        textView.setId(1195835393);
        textView.setTypeface(Typeface.DEFAULT);
        textView.setText(zza.getText());
        textView.setTextColor(zza.getTextColor());
        textView.setTextSize((float) zza.getTextSize());
        textView.setPadding(zzk.zzcE().zzb(context, 4), 0, zzk.zzcE().zzb(context, 4), 0);
        layoutParams = new LayoutParams(-2, -2);
        layoutParams.addRule(1, textView.getId());
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(layoutParams);
        imageView.setId(1195835394);
        imageView.setImageDrawable(zza.getIcon());
        this.zzvI.addView(textView);
        this.zzvI.addView(imageView);
        addView(this.zzvI);
    }

    public ViewGroup zzdq() {
        return this.zzvI;
    }
}
