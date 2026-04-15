package com.baidu.android.pushservice.message;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;

class e implements OnClickListener {
    final /* synthetic */ Context a;
    final /* synthetic */ PublicMsg b;

    e(PublicMsg publicMsg, Context context) {
        this.b = publicMsg;
        this.a = context;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(Uri.parse(this.b.e));
        intent.addFlags(268435456);
        this.a.startActivity(intent);
    }
}
