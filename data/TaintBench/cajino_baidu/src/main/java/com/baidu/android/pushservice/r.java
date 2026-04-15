package com.baidu.android.pushservice;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

class r implements OnClickListener {
    final /* synthetic */ PushTestActivity a;

    r(PushTestActivity pushTestActivity) {
        this.a = pushTestActivity;
    }

    public void onClick(View view) {
        Intent intent = new Intent();
        new Intent(PushConstants.ACTION_METHOD).putExtra("method", "com.baidu.android.pushservice.action.UNBINDAPP");
        intent.putExtra("package_name", this.a.getPackageName());
        intent.putExtra(PushConstants.EXTRA_APP_ID, "101962");
        intent.setClass(this.a, PushService.class);
        this.a.startService(intent);
    }
}
