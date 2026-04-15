package android.support.v4.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

public class ActivityCompat extends ContextCompat {
    public ActivityCompat() {
    }

    public static boolean invalidateOptionsMenu(Activity activity) {
        Activity activity2 = activity;
        if (VERSION.SDK_INT < 11) {
            return false;
        }
        ActivityCompatHoneycomb.invalidateOptionsMenu(activity2);
        return true;
    }

    public static void startActivity(Activity activity, Intent intent, Bundle bundle) {
        Context context = activity;
        Intent intent2 = intent;
        Bundle bundle2 = bundle;
        if (VERSION.SDK_INT >= 16) {
            ActivityCompatJB.startActivity(context, intent2, bundle2);
        } else {
            context.startActivity(intent2);
        }
    }

    public static void startActivityForResult(Activity activity, Intent intent, int i, Bundle bundle) {
        Activity activity2 = activity;
        Intent intent2 = intent;
        int i2 = i;
        Bundle bundle2 = bundle;
        if (VERSION.SDK_INT >= 16) {
            ActivityCompatJB.startActivityForResult(activity2, intent2, i2, bundle2);
        } else {
            activity2.startActivityForResult(intent2, i2);
        }
    }

    public static void finishAffinity(Activity activity) {
        Activity activity2 = activity;
        if (VERSION.SDK_INT >= 16) {
            ActivityCompatJB.finishAffinity(activity2);
        } else {
            activity2.finish();
        }
    }
}
