package android.support.v4.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build.VERSION;
import android.support.v4.content.IntentCompat;
import android.util.Log;

public class NavUtils {
    private static final NavUtilsImpl IMPL;
    public static final String PARENT_ACTIVITY = "android.support.PARENT_ACTIVITY";
    private static final String TAG = "NavUtils";

    interface NavUtilsImpl {
        Intent getParentActivityIntent(Activity activity);

        String getParentActivityName(Context context, ActivityInfo activityInfo);

        void navigateUpTo(Activity activity, Intent intent);

        boolean shouldUpRecreateTask(Activity activity, Intent intent);
    }

    static class NavUtilsImplBase implements NavUtilsImpl {
        NavUtilsImplBase() {
        }

        public Intent getParentActivityIntent(Activity activity) {
            Context context = activity;
            String parentActivityName = NavUtils.getParentActivityName(context);
            if (parentActivityName == null) {
                return null;
            }
            ComponentName componentName = r10;
            ComponentName componentName2 = new ComponentName(context, parentActivityName);
            ComponentName componentName3 = componentName;
            try {
                Intent makeMainActivity;
                if (NavUtils.getParentActivityName(context, componentName3) == null) {
                    makeMainActivity = IntentCompat.makeMainActivity(componentName3);
                } else {
                    makeMainActivity = r10;
                    Intent intent = new Intent();
                    makeMainActivity = makeMainActivity.setComponent(componentName3);
                }
                return makeMainActivity;
            } catch (NameNotFoundException e) {
                NameNotFoundException nameNotFoundException = e;
                String str = NavUtils.TAG;
                StringBuilder stringBuilder = r10;
                StringBuilder stringBuilder2 = new StringBuilder();
                int e2 = Log.e(str, stringBuilder.append("getParentActivityIntent: bad parentActivityName '").append(parentActivityName).append("' in manifest").toString());
                return null;
            }
        }

        public boolean shouldUpRecreateTask(Activity activity, Intent intent) {
            Intent intent2 = intent;
            String action = activity.getIntent().getAction();
            boolean z = (action == null || action.equals("android.intent.action.MAIN")) ? false : true;
            return z;
        }

        public void navigateUpTo(Activity activity, Intent intent) {
            Activity activity2 = activity;
            Intent intent2 = intent;
            Intent addFlags = intent2.addFlags(67108864);
            activity2.startActivity(intent2);
            activity2.finish();
        }

        public String getParentActivityName(Context context, ActivityInfo activityInfo) {
            Context context2 = context;
            ActivityInfo activityInfo2 = activityInfo;
            if (activityInfo2.metaData == null) {
                return null;
            }
            String string = activityInfo2.metaData.getString(NavUtils.PARENT_ACTIVITY);
            if (string == null) {
                return null;
            }
            if (string.charAt(0) == '.') {
                StringBuilder stringBuilder = r6;
                StringBuilder stringBuilder2 = new StringBuilder();
                string = stringBuilder.append(context2.getPackageName()).append(string).toString();
            }
            return string;
        }
    }

    static class NavUtilsImplJB extends NavUtilsImplBase {
        NavUtilsImplJB() {
        }

        public Intent getParentActivityIntent(Activity activity) {
            Activity activity2 = activity;
            Intent parentActivityIntent = NavUtilsJB.getParentActivityIntent(activity2);
            if (parentActivityIntent == null) {
                parentActivityIntent = superGetParentActivityIntent(activity2);
            }
            return parentActivityIntent;
        }

        /* access modifiers changed from: 0000 */
        public Intent superGetParentActivityIntent(Activity activity) {
            return super.getParentActivityIntent(activity);
        }

        public boolean shouldUpRecreateTask(Activity activity, Intent intent) {
            return NavUtilsJB.shouldUpRecreateTask(activity, intent);
        }

        public void navigateUpTo(Activity activity, Intent intent) {
            NavUtilsJB.navigateUpTo(activity, intent);
        }

        public String getParentActivityName(Context context, ActivityInfo activityInfo) {
            Context context2 = context;
            ActivityInfo activityInfo2 = activityInfo;
            String parentActivityName = NavUtilsJB.getParentActivityName(activityInfo2);
            if (parentActivityName == null) {
                parentActivityName = super.getParentActivityName(context2, activityInfo2);
            }
            return parentActivityName;
        }
    }

    static {
        if (VERSION.SDK_INT >= 16) {
            NavUtilsImplJB navUtilsImplJB = r3;
            NavUtilsImplJB navUtilsImplJB2 = new NavUtilsImplJB();
            IMPL = navUtilsImplJB;
            return;
        }
        NavUtilsImplBase navUtilsImplBase = r3;
        NavUtilsImplBase navUtilsImplBase2 = new NavUtilsImplBase();
        IMPL = navUtilsImplBase;
    }

    public static boolean shouldUpRecreateTask(Activity activity, Intent intent) {
        return IMPL.shouldUpRecreateTask(activity, intent);
    }

    public static void navigateUpFromSameTask(Activity activity) {
        Activity activity2 = activity;
        Intent parentActivityIntent = getParentActivityIntent(activity2);
        if (parentActivityIntent == null) {
            IllegalArgumentException illegalArgumentException = r6;
            StringBuilder stringBuilder = r6;
            StringBuilder stringBuilder2 = new StringBuilder();
            IllegalArgumentException illegalArgumentException2 = new IllegalArgumentException(stringBuilder.append("Activity ").append(activity2.getClass().getSimpleName()).append(" does not have a parent activity name specified.").append(" (Did you forget to add the android.support.PARENT_ACTIVITY <meta-data> ").append(" element in your manifest?)").toString());
            throw illegalArgumentException;
        }
        navigateUpTo(activity2, parentActivityIntent);
    }

    public static void navigateUpTo(Activity activity, Intent intent) {
        IMPL.navigateUpTo(activity, intent);
    }

    public static Intent getParentActivityIntent(Activity activity) {
        return IMPL.getParentActivityIntent(activity);
    }

    public static Intent getParentActivityIntent(Context context, Class<?> cls) throws NameNotFoundException {
        Context context2 = context;
        Context context3 = context2;
        ComponentName componentName = r11;
        ComponentName componentName2 = new ComponentName(context2, cls);
        String parentActivityName = getParentActivityName(context3, componentName);
        if (parentActivityName == null) {
            return null;
        }
        Intent makeMainActivity;
        ComponentName componentName3 = r11;
        componentName = new ComponentName(context2, parentActivityName);
        ComponentName componentName4 = componentName3;
        if (getParentActivityName(context2, componentName4) == null) {
            makeMainActivity = IntentCompat.makeMainActivity(componentName4);
        } else {
            makeMainActivity = r11;
            Intent intent = new Intent();
            makeMainActivity = makeMainActivity.setComponent(componentName4);
        }
        return makeMainActivity;
    }

    public static Intent getParentActivityIntent(Context context, ComponentName componentName) throws NameNotFoundException {
        Context context2 = context;
        ComponentName componentName2 = componentName;
        String parentActivityName = getParentActivityName(context2, componentName2);
        if (parentActivityName == null) {
            return null;
        }
        Intent makeMainActivity;
        ComponentName componentName3 = r10;
        ComponentName componentName4 = new ComponentName(componentName2.getPackageName(), parentActivityName);
        ComponentName componentName5 = componentName3;
        if (getParentActivityName(context2, componentName5) == null) {
            makeMainActivity = IntentCompat.makeMainActivity(componentName5);
        } else {
            makeMainActivity = r10;
            Intent intent = new Intent();
            makeMainActivity = makeMainActivity.setComponent(componentName5);
        }
        return makeMainActivity;
    }

    public static String getParentActivityName(Activity activity) {
        Context context = activity;
        try {
            return getParentActivityName(context, context.getComponentName());
        } catch (NameNotFoundException e) {
            Throwable th = e;
            IllegalArgumentException illegalArgumentException = r5;
            IllegalArgumentException illegalArgumentException2 = new IllegalArgumentException(th);
            throw illegalArgumentException;
        }
    }

    public static String getParentActivityName(Context context, ComponentName componentName) throws NameNotFoundException {
        Context context2 = context;
        return IMPL.getParentActivityName(context2, context2.getPackageManager().getActivityInfo(componentName, 128));
    }

    private NavUtils() {
    }
}
