package com.baidu.android.pushservice;

import android.app.PendingIntent;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

class s implements OnClickListener {
    final /* synthetic */ PushTestActivity a;

    s(PushTestActivity pushTestActivity) {
        this.a = pushTestActivity;
    }

    public void onClick(View view) {
        Intent intent = new Intent(PushConstants.ACTION_METHOD);
        intent.putExtra("method", PushConstants.METHOD_FETCH);
        intent.putExtra(PushConstants.EXTRA_APP, PendingIntent.getBroadcast(this.a, 0, new Intent(), 0));
        intent.putExtra(PushConstants.EXTRA_FETCH_TYPE, 1);
        intent.putExtra(PushConstants.EXTRA_FETCH_NUM, 2);
        intent.putExtra(PushConstants.EXTRA_ACCESS_TOKEN, PushConstants.rsaEncrypt(this.a.a));
        this.a.sendBroadcast(intent);
    }
}
