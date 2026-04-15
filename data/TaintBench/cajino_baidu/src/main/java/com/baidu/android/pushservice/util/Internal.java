package com.baidu.android.pushservice.util;

import android.content.Context;
import android.content.Intent;

public final class Internal implements NoProGuard {
    private Internal() {
    }

    public static Intent createBdussInent(Context context) {
        return m.e(context);
    }

    public static void disablePushConnection(Context context) {
        m.b(context, false);
    }

    public static void disablePushService(Context context) {
        m.a(context, false);
    }

    public static void enablePushConnection(Context context) {
        m.b(context, true);
    }

    public static void enablePushService(Context context) {
        m.a(context, true);
    }
}
