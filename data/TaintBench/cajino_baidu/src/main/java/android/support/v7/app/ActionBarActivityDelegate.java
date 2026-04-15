package android.support.v7.app;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle.Delegate;
import android.support.v4.app.NavUtils;
import android.support.v7.appcompat.R;
import android.support.v7.internal.view.SupportMenuInflater;
import android.support.v7.view.ActionMode;
import android.support.v7.view.ActionMode.Callback;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

abstract class ActionBarActivityDelegate {
    static final String METADATA_UI_OPTIONS = "android.support.UI_OPTIONS";
    private static final String TAG = "ActionBarActivityDelegate";
    static final String UIOPTION_SPLIT_ACTION_BAR_WHEN_NARROW = "splitActionBarWhenNarrow";
    private ActionBar mActionBar;
    final ActionBarActivity mActivity;
    private boolean mEnableDefaultActionBarUp;
    boolean mHasActionBar;
    private MenuInflater mMenuInflater;
    boolean mOverlayActionBar;

    private class ActionBarDrawableToggleImpl implements Delegate {
        private ActionBarDrawableToggleImpl() {
        }

        public Drawable getThemeUpIndicator() {
            TypedArray a = ActionBarActivityDelegate.this.mActivity.obtainStyledAttributes(new int[]{ActionBarActivityDelegate.this.getHomeAsUpIndicatorAttrId()});
            Drawable result = a.getDrawable(0);
            a.recycle();
            return result;
        }

        public void setActionBarUpIndicator(Drawable upDrawable, int contentDescRes) {
            ActionBar ab = ActionBarActivityDelegate.this.getSupportActionBar();
            if (ab != null) {
                ab.setHomeAsUpIndicator(upDrawable);
                ab.setHomeActionContentDescription(contentDescRes);
            }
        }

        public void setActionBarDescription(int contentDescRes) {
            ActionBar ab = ActionBarActivityDelegate.this.getSupportActionBar();
            if (ab != null) {
                ab.setHomeActionContentDescription(contentDescRes);
            }
        }
    }

    public abstract void addContentView(View view, LayoutParams layoutParams);

    public abstract ActionBar createSupportActionBar();

    public abstract int getHomeAsUpIndicatorAttrId();

    public abstract boolean onBackPressed();

    public abstract void onConfigurationChanged(Configuration configuration);

    public abstract void onContentChanged();

    public abstract boolean onCreatePanelMenu(int i, Menu menu);

    public abstract View onCreatePanelView(int i);

    public abstract boolean onMenuItemSelected(int i, MenuItem menuItem);

    public abstract void onPostResume();

    public abstract boolean onPreparePanel(int i, View view, Menu menu);

    public abstract void onStop();

    public abstract void onTitleChanged(CharSequence charSequence);

    public abstract void setContentView(int i);

    public abstract void setContentView(View view);

    public abstract void setContentView(View view, LayoutParams layoutParams);

    public abstract void setSupportProgress(int i);

    public abstract void setSupportProgressBarIndeterminate(boolean z);

    public abstract void setSupportProgressBarIndeterminateVisibility(boolean z);

    public abstract void setSupportProgressBarVisibility(boolean z);

    public abstract ActionMode startSupportActionMode(Callback callback);

    public abstract void supportInvalidateOptionsMenu();

    public abstract boolean supportRequestWindowFeature(int i);

    static ActionBarActivityDelegate createDelegate(ActionBarActivity activity) {
        if (VERSION.SDK_INT >= 20) {
            return new ActionBarActivityDelegateApi20(activity);
        }
        if (VERSION.SDK_INT >= 18) {
            return new ActionBarActivityDelegateJBMR2(activity);
        }
        if (VERSION.SDK_INT >= 16) {
            return new ActionBarActivityDelegateJB(activity);
        }
        if (VERSION.SDK_INT >= 14) {
            return new ActionBarActivityDelegateICS(activity);
        }
        if (VERSION.SDK_INT >= 11) {
            return new ActionBarActivityDelegateHC(activity);
        }
        return new ActionBarActivityDelegateBase(activity);
    }

    ActionBarActivityDelegate(ActionBarActivity activity) {
        this.mActivity = activity;
    }

    /* access modifiers changed from: final */
    public final ActionBar getSupportActionBar() {
        if (!this.mHasActionBar && !this.mOverlayActionBar) {
            this.mActionBar = null;
        } else if (this.mActionBar == null) {
            this.mActionBar = createSupportActionBar();
            if (this.mEnableDefaultActionBarUp) {
                this.mActionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
        return this.mActionBar;
    }

    /* access modifiers changed from: 0000 */
    public MenuInflater getMenuInflater() {
        if (this.mMenuInflater == null) {
            this.mMenuInflater = new SupportMenuInflater(getActionBarThemedContext());
        }
        return this.mMenuInflater;
    }

    /* access modifiers changed from: 0000 */
    public void onCreate(Bundle savedInstanceState) {
        TypedArray a = this.mActivity.obtainStyledAttributes(R.styleable.ActionBarWindow);
        if (a.hasValue(0)) {
            this.mHasActionBar = a.getBoolean(0, false);
            this.mOverlayActionBar = a.getBoolean(1, false);
            a.recycle();
            if (NavUtils.getParentActivityName(this.mActivity) == null) {
                return;
            }
            if (this.mActionBar == null) {
                this.mEnableDefaultActionBarUp = true;
                return;
            } else {
                this.mActionBar.setDisplayHomeAsUpEnabled(true);
                return;
            }
        }
        a.recycle();
        throw new IllegalStateException("You need to use a Theme.AppCompat theme (or descendant) with this activity.");
    }

    /* access modifiers changed from: 0000 */
    public boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (VERSION.SDK_INT < 16) {
            return this.mActivity.onPrepareOptionsMenu(menu);
        }
        return this.mActivity.superOnPrepareOptionsPanel(view, menu);
    }

    /* access modifiers changed from: final */
    public final Delegate getDrawerToggleDelegate() {
        return new ActionBarDrawableToggleImpl();
    }

    /* access modifiers changed from: protected|final */
    public final String getUiOptionsFromMetadata() {
        try {
            ActivityInfo info = this.mActivity.getPackageManager().getActivityInfo(this.mActivity.getComponentName(), 128);
            if (info.metaData != null) {
                return info.metaData.getString(METADATA_UI_OPTIONS);
            }
            return null;
        } catch (NameNotFoundException e) {
            Log.e(TAG, "getUiOptionsFromMetadata: Activity '" + this.mActivity.getClass().getSimpleName() + "' not in manifest");
            return null;
        }
    }

    /* access modifiers changed from: protected|final */
    public final Context getActionBarThemedContext() {
        Context context = this.mActivity;
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            return ab.getThemedContext();
        }
        return context;
    }
}
