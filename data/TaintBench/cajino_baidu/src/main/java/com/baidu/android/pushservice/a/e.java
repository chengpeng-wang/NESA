package com.baidu.android.pushservice.a;

import android.content.Context;
import com.baidu.android.common.logging.Log;

public abstract class e extends a {
    public e(l lVar, Context context) {
        super(lVar, context);
    }

    public boolean b() {
        Log.i("SendApiProcessor", "networkConnect");
        this.c += "channel";
        return super.b();
    }
}
