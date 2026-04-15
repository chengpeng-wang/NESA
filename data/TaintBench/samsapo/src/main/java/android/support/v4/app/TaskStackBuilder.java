package android.support.v4.app;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;

public class TaskStackBuilder implements Iterable<Intent> {
    private static final TaskStackBuilderImpl IMPL;
    private static final String TAG = "TaskStackBuilder";
    private final ArrayList<Intent> mIntents;
    private final Context mSourceContext;

    public interface SupportParentable {
        Intent getSupportParentActivityIntent();
    }

    interface TaskStackBuilderImpl {
        PendingIntent getPendingIntent(Context context, Intent[] intentArr, int i, int i2, Bundle bundle);
    }

    static class TaskStackBuilderImplBase implements TaskStackBuilderImpl {
        TaskStackBuilderImplBase() {
        }

        public PendingIntent getPendingIntent(Context context, Intent[] intentArr, int i, int i2, Bundle bundle) {
            Context context2 = context;
            Intent[] intentArr2 = intentArr;
            int i3 = i;
            int i4 = i2;
            Bundle bundle2 = bundle;
            Intent intent = r12;
            Intent intent2 = new Intent(intentArr2[intentArr2.length - 1]);
            Intent intent3 = intent;
            intent = intent3.addFlags(268435456);
            return PendingIntent.getActivity(context2, i3, intent3, i4);
        }
    }

    static class TaskStackBuilderImplHoneycomb implements TaskStackBuilderImpl {
        TaskStackBuilderImplHoneycomb() {
        }

        public PendingIntent getPendingIntent(Context context, Intent[] intentArr, int i, int i2, Bundle bundle) {
            Context context2 = context;
            Intent[] intentArr2 = intentArr;
            int i3 = i;
            int i4 = i2;
            Bundle bundle2 = bundle;
            Intent[] intentArr3 = intentArr2;
            Intent intent = r12;
            Intent intent2 = new Intent(intentArr2[0]);
            intentArr3[0] = intent.addFlags(268484608);
            return TaskStackBuilderHoneycomb.getActivitiesPendingIntent(context2, i3, intentArr2, i4);
        }
    }

    static class TaskStackBuilderImplJellybean implements TaskStackBuilderImpl {
        TaskStackBuilderImplJellybean() {
        }

        public PendingIntent getPendingIntent(Context context, Intent[] intentArr, int i, int i2, Bundle bundle) {
            Context context2 = context;
            Intent[] intentArr2 = intentArr;
            int i3 = i;
            int i4 = i2;
            Bundle bundle2 = bundle;
            Intent[] intentArr3 = intentArr2;
            Intent intent = r12;
            Intent intent2 = new Intent(intentArr2[0]);
            intentArr3[0] = intent.addFlags(268484608);
            return TaskStackBuilderJellybean.getActivitiesPendingIntent(context2, i3, intentArr2, i4, bundle2);
        }
    }

    static {
        if (VERSION.SDK_INT >= 11) {
            TaskStackBuilderImplHoneycomb taskStackBuilderImplHoneycomb = r2;
            TaskStackBuilderImplHoneycomb taskStackBuilderImplHoneycomb2 = new TaskStackBuilderImplHoneycomb();
            IMPL = taskStackBuilderImplHoneycomb;
            return;
        }
        TaskStackBuilderImplBase taskStackBuilderImplBase = r2;
        TaskStackBuilderImplBase taskStackBuilderImplBase2 = new TaskStackBuilderImplBase();
        IMPL = taskStackBuilderImplBase;
    }

    private TaskStackBuilder(Context context) {
        Context context2 = context;
        ArrayList arrayList = r5;
        ArrayList arrayList2 = new ArrayList();
        this.mIntents = arrayList;
        this.mSourceContext = context2;
    }

    public static TaskStackBuilder create(Context context) {
        TaskStackBuilder taskStackBuilder = r4;
        TaskStackBuilder taskStackBuilder2 = new TaskStackBuilder(context);
        return taskStackBuilder;
    }

    public static TaskStackBuilder from(Context context) {
        return create(context);
    }

    public TaskStackBuilder addNextIntent(Intent intent) {
        boolean add = this.mIntents.add(intent);
        return this;
    }

    public TaskStackBuilder addNextIntentWithParentStack(Intent intent) {
        TaskStackBuilder addParentStack;
        Intent intent2 = intent;
        ComponentName component = intent2.getComponent();
        if (component == null) {
            component = intent2.resolveActivity(this.mSourceContext.getPackageManager());
        }
        if (component != null) {
            addParentStack = addParentStack(component);
        }
        addParentStack = addNextIntent(intent2);
        return this;
    }

    public TaskStackBuilder addParentStack(Activity activity) {
        Activity activity2 = activity;
        Intent intent = null;
        if (activity2 instanceof SupportParentable) {
            intent = ((SupportParentable) activity2).getSupportParentActivityIntent();
        }
        if (intent == null) {
            intent = NavUtils.getParentActivityIntent(activity2);
        }
        if (intent != null) {
            ComponentName component = intent.getComponent();
            if (component == null) {
                component = intent.resolveActivity(this.mSourceContext.getPackageManager());
            }
            TaskStackBuilder addParentStack = addParentStack(component);
            addParentStack = addNextIntent(intent);
        }
        return this;
    }

    public TaskStackBuilder addParentStack(Class<?> cls) {
        ComponentName componentName = r7;
        ComponentName componentName2 = new ComponentName(this.mSourceContext, cls);
        return addParentStack(componentName);
    }

    public TaskStackBuilder addParentStack(ComponentName componentName) {
        ComponentName componentName2 = componentName;
        int size = this.mIntents.size();
        try {
            Intent parentActivityIntent = NavUtils.getParentActivityIntent(this.mSourceContext, componentName2);
            while (true) {
                Intent intent = parentActivityIntent;
                if (intent == null) {
                    return this;
                }
                this.mIntents.add(size, intent);
                parentActivityIntent = NavUtils.getParentActivityIntent(this.mSourceContext, intent.getComponent());
            }
        } catch (NameNotFoundException e) {
            Throwable th = e;
            int e2 = Log.e(TAG, "Bad ComponentName while traversing activity parent metadata");
            IllegalArgumentException illegalArgumentException = r7;
            IllegalArgumentException illegalArgumentException2 = new IllegalArgumentException(th);
            throw illegalArgumentException;
        }
    }

    public int getIntentCount() {
        return this.mIntents.size();
    }

    public Intent getIntent(int i) {
        return editIntentAt(i);
    }

    public Intent editIntentAt(int i) {
        return (Intent) this.mIntents.get(i);
    }

    public Iterator<Intent> iterator() {
        return this.mIntents.iterator();
    }

    public void startActivities() {
        startActivities(null);
    }

    public void startActivities(Bundle bundle) {
        Bundle bundle2 = bundle;
        if (this.mIntents.isEmpty()) {
            IllegalStateException illegalStateException = r10;
            IllegalStateException illegalStateException2 = new IllegalStateException("No intents added to TaskStackBuilder; cannot startActivities");
            throw illegalStateException;
        }
        Intent[] intentArr = (Intent[]) this.mIntents.toArray(new Intent[this.mIntents.size()]);
        Intent[] intentArr2 = intentArr;
        Intent intent = r10;
        Intent intent2 = new Intent(intentArr[0]);
        intentArr2[0] = intent.addFlags(268484608);
        if (!ContextCompat.startActivities(this.mSourceContext, intentArr, bundle2)) {
            Intent intent3 = r10;
            Intent intent4 = new Intent(intentArr[intentArr.length - 1]);
            Intent intent5 = intent3;
            intent3 = intent5.addFlags(268435456);
            this.mSourceContext.startActivity(intent5);
        }
    }

    public PendingIntent getPendingIntent(int i, int i2) {
        return getPendingIntent(i, i2, null);
    }

    public PendingIntent getPendingIntent(int i, int i2, Bundle bundle) {
        int i3 = i;
        int i4 = i2;
        Bundle bundle2 = bundle;
        if (this.mIntents.isEmpty()) {
            IllegalStateException illegalStateException = r11;
            IllegalStateException illegalStateException2 = new IllegalStateException("No intents added to TaskStackBuilder; cannot getPendingIntent");
            throw illegalStateException;
        }
        Intent[] intentArr = (Intent[]) this.mIntents.toArray(new Intent[this.mIntents.size()]);
        Intent[] intentArr2 = intentArr;
        Intent intent = r11;
        Intent intent2 = new Intent(intentArr[0]);
        intentArr2[0] = intent.addFlags(268484608);
        return IMPL.getPendingIntent(this.mSourceContext, intentArr, i3, i4, bundle2);
    }

    public Intent[] getIntents() {
        Intent[] intentArr = new Intent[this.mIntents.size()];
        if (intentArr.length == 0) {
            return intentArr;
        }
        Intent[] intentArr2 = intentArr;
        Intent intent = r9;
        Intent intent2 = new Intent((Intent) this.mIntents.get(0));
        intentArr2[0] = intent.addFlags(268484608);
        for (int i = 1; i < intentArr.length; i++) {
            intentArr2 = intentArr;
            int i2 = i;
            intent = r9;
            intent2 = new Intent((Intent) this.mIntents.get(i));
            intentArr2[i2] = intent;
        }
        return intentArr;
    }
}
