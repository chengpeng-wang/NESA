package android.support.v4.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.ActionProvider;
import android.view.MenuItem;
import android.widget.ShareActionProvider;

class ShareCompatICS {
    private static final String HISTORY_FILENAME_PREFIX = ".sharecompat_";

    ShareCompatICS() {
    }

    public static void configureMenuItem(MenuItem menuItem, Activity activity, Intent intent) {
        ActionProvider actionProvider;
        ActionProvider actionProvider2;
        MenuItem menuItem2 = menuItem;
        Context context = activity;
        Intent intent2 = intent;
        ActionProvider actionProvider3 = menuItem2.getActionProvider();
        Object obj = null;
        if (actionProvider3 instanceof ShareActionProvider) {
            actionProvider = (ShareActionProvider) actionProvider3;
        } else {
            actionProvider2 = r8;
            ActionProvider shareActionProvider = new ShareActionProvider(context);
            actionProvider = actionProvider2;
        }
        actionProvider2 = actionProvider;
        StringBuilder stringBuilder = r8;
        StringBuilder stringBuilder2 = new StringBuilder();
        actionProvider2.setShareHistoryFileName(stringBuilder.append(HISTORY_FILENAME_PREFIX).append(context.getClass().getName()).toString());
        actionProvider.setShareIntent(intent2);
        MenuItem actionProvider4 = menuItem2.setActionProvider(actionProvider);
    }
}
