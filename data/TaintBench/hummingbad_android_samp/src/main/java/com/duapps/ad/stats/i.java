package com.duapps.ad.stats;

import android.widget.Toast;

/* compiled from: ToolClickHandlerBase */
final class i implements Runnable {
    private /* synthetic */ String a;
    private /* synthetic */ h b;

    i(h hVar, String str) {
        this.b = hVar;
        this.a = str;
    }

    public final void run() {
        if (this.b.e == null) {
            this.b.e = Toast.makeText(this.b.d, this.a, 0);
        }
        this.b.e.setText(this.a);
        this.b.e.show();
    }
}
