package com.baidu.android.pushservice.a;

import android.content.Context;
import com.baidu.android.common.logging.Log;
import com.baidu.android.pushservice.y;

public abstract class c extends a {
    public c(l lVar, Context context) {
        super(lVar, context);
    }

    public boolean b() {
        Log.i("BaseApiProcessor", "networkConnect");
        this.c += y.a().c();
        return super.b();
    }
}
