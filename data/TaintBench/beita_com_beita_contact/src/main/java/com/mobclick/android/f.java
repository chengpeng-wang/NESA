package com.mobclick.android;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import java.io.File;

class f implements OnClickListener {
    private final /* synthetic */ Context a;
    private final /* synthetic */ File b;

    f(Context context, File file) {
        this.a = context;
        this.b = file;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        MobclickAgent.c(this.a, this.b);
    }
}
