package com.mobclick.android;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

class h implements OnClickListener {
    private final /* synthetic */ Context a;
    private final /* synthetic */ String b;
    private final /* synthetic */ String c;

    h(Context context, String str, String str2) {
        this.a = context;
        this.b = str;
        this.c = str2;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        new a(this.a, this.b, this.a.getString(l.a(this.a, "string", "UMAppUpdate")), this.a.getString(l.a(this.a, "string", "UMUpdatingNow")), "", this.c).c();
    }
}
