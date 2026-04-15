package com.google.android.gms.ads.internal.overlay;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import com.google.android.gms.ads.internal.client.zzk;
import com.google.android.gms.internal.zzgk;

@zzgk
public class zzm extends FrameLayout implements OnClickListener {
    private final ImageButton zzBk;
    private final zzo zzBl;

    public zzm(Context context, int i, zzo zzo) {
        super(context);
        this.zzBl = zzo;
        setOnClickListener(this);
        this.zzBk = new ImageButton(context);
        this.zzBk.setImageResource(17301527);
        this.zzBk.setBackgroundColor(0);
        this.zzBk.setOnClickListener(this);
        this.zzBk.setPadding(0, 0, 0, 0);
        this.zzBk.setContentDescription("Interstitial close button");
        int zzb = zzk.zzcE().zzb(context, i);
        addView(this.zzBk, new LayoutParams(zzb, zzb, 17));
    }

    public void onClick(View view) {
        if (this.zzBl != null) {
            this.zzBl.zzey();
        }
    }

    public void zza(boolean z, boolean z2) {
        if (!z2) {
            this.zzBk.setVisibility(0);
        } else if (z) {
            this.zzBk.setVisibility(4);
        } else {
            this.zzBk.setVisibility(8);
        }
    }
}
