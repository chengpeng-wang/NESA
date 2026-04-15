package com.flurry.sdk;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;

final class if implements ActivityLifecycleCallbacks {
    final /* synthetic */ ie a;

    if(ie ieVar) {
        this.a = ieVar;
    }

    private static void a(Activity activity, id idVar) {
        ij icVar = new ic();
        icVar.a = activity;
        icVar.b = idVar;
        il.a().a(icVar);
    }

    public final void onActivityCreated(Activity activity, Bundle bundle) {
        iw.a(3, ie.c, "onActivityCreated for activity:" + activity);
        a(activity, id.kCreated);
    }

    public final void onActivityDestroyed(Activity activity) {
        iw.a(3, ie.c, "onActivityDestroyed for activity:" + activity);
        a(activity, id.kDestroyed);
    }

    public final void onActivityPaused(Activity activity) {
        iw.a(3, ie.c, "onActivityPaused for activity:" + activity);
        a(activity, id.kPaused);
    }

    public final void onActivityResumed(Activity activity) {
        iw.a(3, ie.c, "onActivityResumed for activity:" + activity);
        a(activity, id.kResumed);
    }

    public final void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        iw.a(3, ie.c, "onActivitySaveInstanceState for activity:" + activity);
        a(activity, id.kSaveState);
    }

    public final void onActivityStarted(Activity activity) {
        iw.a(3, ie.c, "onActivityStarted for activity:" + activity);
        a(activity, id.kStarted);
    }

    public final void onActivityStopped(Activity activity) {
        iw.a(3, ie.c, "onActivityStopped for activity:" + activity);
        a(activity, id.kStopped);
    }
}
