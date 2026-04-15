package android.support.v4.app;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.Build.VERSION;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.MenuItem;
import android.view.View;

public class ActionBarDrawerToggle implements DrawerListener {
    private static final int ID_HOME = 16908332;
    private static final ActionBarDrawerToggleImpl IMPL;
    private final Activity mActivity;
    private final int mCloseDrawerContentDescRes;
    private Drawable mDrawerImage;
    private final int mDrawerImageResource;
    private boolean mDrawerIndicatorEnabled = true;
    private final DrawerLayout mDrawerLayout;
    private final int mOpenDrawerContentDescRes;
    private Object mSetIndicatorInfo;
    private SlideDrawable mSlider;
    private Drawable mThemeImage;

    private interface ActionBarDrawerToggleImpl {
        Drawable getThemeUpIndicator(Activity activity);

        Object setActionBarDescription(Object obj, Activity activity, int i);

        Object setActionBarUpIndicator(Object obj, Activity activity, Drawable drawable, int i);
    }

    private static class SlideDrawable extends Drawable implements Callback {
        private float mOffset;
        private float mOffsetBy;
        private final Rect mTmpRect = new Rect();
        private Drawable mWrapped;

        public SlideDrawable(Drawable wrapped) {
            this.mWrapped = wrapped;
        }

        public void setOffset(float offset) {
            this.mOffset = offset;
            invalidateSelf();
        }

        public float getOffset() {
            return this.mOffset;
        }

        public void setOffsetBy(float offsetBy) {
            this.mOffsetBy = offsetBy;
            invalidateSelf();
        }

        public void draw(Canvas canvas) {
            this.mWrapped.copyBounds(this.mTmpRect);
            canvas.save();
            canvas.translate((this.mOffsetBy * ((float) this.mTmpRect.width())) * (-this.mOffset), 0.0f);
            this.mWrapped.draw(canvas);
            canvas.restore();
        }

        public void setChangingConfigurations(int configs) {
            this.mWrapped.setChangingConfigurations(configs);
        }

        public int getChangingConfigurations() {
            return this.mWrapped.getChangingConfigurations();
        }

        public void setDither(boolean dither) {
            this.mWrapped.setDither(dither);
        }

        public void setFilterBitmap(boolean filter) {
            this.mWrapped.setFilterBitmap(filter);
        }

        public void setAlpha(int alpha) {
            this.mWrapped.setAlpha(alpha);
        }

        public void setColorFilter(ColorFilter cf) {
            this.mWrapped.setColorFilter(cf);
        }

        public void setColorFilter(int color, Mode mode) {
            this.mWrapped.setColorFilter(color, mode);
        }

        public void clearColorFilter() {
            this.mWrapped.clearColorFilter();
        }

        public boolean isStateful() {
            return this.mWrapped.isStateful();
        }

        public boolean setState(int[] stateSet) {
            return this.mWrapped.setState(stateSet);
        }

        public int[] getState() {
            return this.mWrapped.getState();
        }

        public Drawable getCurrent() {
            return this.mWrapped.getCurrent();
        }

        public boolean setVisible(boolean visible, boolean restart) {
            return super.setVisible(visible, restart);
        }

        public int getOpacity() {
            return this.mWrapped.getOpacity();
        }

        public Region getTransparentRegion() {
            return this.mWrapped.getTransparentRegion();
        }

        /* access modifiers changed from: protected */
        public boolean onStateChange(int[] state) {
            this.mWrapped.setState(state);
            return super.onStateChange(state);
        }

        /* access modifiers changed from: protected */
        public void onBoundsChange(Rect bounds) {
            super.onBoundsChange(bounds);
            this.mWrapped.setBounds(bounds);
        }

        public int getIntrinsicWidth() {
            return this.mWrapped.getIntrinsicWidth();
        }

        public int getIntrinsicHeight() {
            return this.mWrapped.getIntrinsicHeight();
        }

        public int getMinimumWidth() {
            return this.mWrapped.getMinimumWidth();
        }

        public int getMinimumHeight() {
            return this.mWrapped.getMinimumHeight();
        }

        public boolean getPadding(Rect padding) {
            return this.mWrapped.getPadding(padding);
        }

        public ConstantState getConstantState() {
            return super.getConstantState();
        }

        public void invalidateDrawable(Drawable who) {
            if (who == this.mWrapped) {
                invalidateSelf();
            }
        }

        public void scheduleDrawable(Drawable who, Runnable what, long when) {
            if (who == this.mWrapped) {
                scheduleSelf(what, when);
            }
        }

        public void unscheduleDrawable(Drawable who, Runnable what) {
            if (who == this.mWrapped) {
                unscheduleSelf(what);
            }
        }
    }

    private static class ActionBarDrawerToggleImplBase implements ActionBarDrawerToggleImpl {
        private ActionBarDrawerToggleImplBase() {
        }

        public Drawable getThemeUpIndicator(Activity activity) {
            return null;
        }

        public Object setActionBarUpIndicator(Object info, Activity activity, Drawable themeImage, int contentDescRes) {
            return info;
        }

        public Object setActionBarDescription(Object info, Activity activity, int contentDescRes) {
            return info;
        }
    }

    private static class ActionBarDrawerToggleImplHC implements ActionBarDrawerToggleImpl {
        private ActionBarDrawerToggleImplHC() {
        }

        public Drawable getThemeUpIndicator(Activity activity) {
            return ActionBarDrawerToggleHoneycomb.getThemeUpIndicator(activity);
        }

        public Object setActionBarUpIndicator(Object info, Activity activity, Drawable themeImage, int contentDescRes) {
            return ActionBarDrawerToggleHoneycomb.setActionBarUpIndicator(info, activity, themeImage, contentDescRes);
        }

        public Object setActionBarDescription(Object info, Activity activity, int contentDescRes) {
            return ActionBarDrawerToggleHoneycomb.setActionBarDescription(info, activity, contentDescRes);
        }
    }

    static {
        if (VERSION.SDK_INT >= 11) {
            IMPL = new ActionBarDrawerToggleImplHC();
        } else {
            IMPL = new ActionBarDrawerToggleImplBase();
        }
    }

    public ActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, int drawerImageRes, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
        this.mActivity = activity;
        this.mDrawerLayout = drawerLayout;
        this.mDrawerImageResource = drawerImageRes;
        this.mOpenDrawerContentDescRes = openDrawerContentDescRes;
        this.mCloseDrawerContentDescRes = closeDrawerContentDescRes;
        this.mThemeImage = IMPL.getThemeUpIndicator(activity);
        this.mDrawerImage = activity.getResources().getDrawable(drawerImageRes);
        this.mSlider = new SlideDrawable(this.mDrawerImage);
        this.mSlider.setOffsetBy(0.33333334f);
    }

    public void syncState() {
        if (this.mDrawerLayout.isDrawerOpen((int) GravityCompat.START)) {
            this.mSlider.setOffset(1.0f);
        } else {
            this.mSlider.setOffset(0.0f);
        }
        if (this.mDrawerIndicatorEnabled) {
            this.mSetIndicatorInfo = IMPL.setActionBarUpIndicator(this.mSetIndicatorInfo, this.mActivity, this.mSlider, this.mDrawerLayout.isDrawerOpen((int) GravityCompat.START) ? this.mOpenDrawerContentDescRes : this.mCloseDrawerContentDescRes);
        }
    }

    public void setDrawerIndicatorEnabled(boolean enable) {
        if (enable != this.mDrawerIndicatorEnabled) {
            if (enable) {
                this.mSetIndicatorInfo = IMPL.setActionBarUpIndicator(this.mSetIndicatorInfo, this.mActivity, this.mSlider, this.mDrawerLayout.isDrawerOpen((int) GravityCompat.START) ? this.mOpenDrawerContentDescRes : this.mCloseDrawerContentDescRes);
            } else {
                this.mSetIndicatorInfo = IMPL.setActionBarUpIndicator(this.mSetIndicatorInfo, this.mActivity, this.mThemeImage, 0);
            }
            this.mDrawerIndicatorEnabled = enable;
        }
    }

    public boolean isDrawerIndicatorEnabled() {
        return this.mDrawerIndicatorEnabled;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        this.mThemeImage = IMPL.getThemeUpIndicator(this.mActivity);
        this.mDrawerImage = this.mActivity.getResources().getDrawable(this.mDrawerImageResource);
        syncState();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null && item.getItemId() == ID_HOME && this.mDrawerIndicatorEnabled) {
            if (this.mDrawerLayout.isDrawerVisible((int) GravityCompat.START)) {
                this.mDrawerLayout.closeDrawer((int) GravityCompat.START);
            } else {
                this.mDrawerLayout.openDrawer((int) GravityCompat.START);
            }
        }
        return false;
    }

    public void onDrawerSlide(View drawerView, float slideOffset) {
        float glyphOffset = this.mSlider.getOffset();
        if (slideOffset > 0.5f) {
            glyphOffset = Math.max(glyphOffset, Math.max(0.0f, slideOffset - 0.5f) * 2.0f);
        } else {
            glyphOffset = Math.min(glyphOffset, slideOffset * 2.0f);
        }
        this.mSlider.setOffset(glyphOffset);
    }

    public void onDrawerOpened(View drawerView) {
        this.mSlider.setOffset(1.0f);
        if (this.mDrawerIndicatorEnabled) {
            this.mSetIndicatorInfo = IMPL.setActionBarDescription(this.mSetIndicatorInfo, this.mActivity, this.mOpenDrawerContentDescRes);
        }
    }

    public void onDrawerClosed(View drawerView) {
        this.mSlider.setOffset(0.0f);
        if (this.mDrawerIndicatorEnabled) {
            this.mSetIndicatorInfo = IMPL.setActionBarDescription(this.mSetIndicatorInfo, this.mActivity, this.mCloseDrawerContentDescRes);
        }
    }

    public void onDrawerStateChanged(int newState) {
    }
}
