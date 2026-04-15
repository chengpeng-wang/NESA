package android.support.v4.app;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.graphics.drawable.LevelListDrawable;
import android.os.Build.VERSION;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.MenuItem;
import android.view.View;

public class ActionBarDrawerToggle implements DrawerListener {
    private static final int ID_HOME = 16908332;
    private static final ActionBarDrawerToggleImpl IMPL;
    private static final float TOGGLE_DRAWABLE_OFFSET = 0.33333334f;
    /* access modifiers changed from: private|final */
    public final Activity mActivity;
    private final Delegate mActivityImpl;
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

    private static class ActionBarDrawerToggleImplBase implements ActionBarDrawerToggleImpl {
        private ActionBarDrawerToggleImplBase() {
        }

        /* synthetic */ ActionBarDrawerToggleImplBase(AnonymousClass1 anonymousClass1) {
            AnonymousClass1 anonymousClass12 = anonymousClass1;
            this();
        }

        public Drawable getThemeUpIndicator(Activity activity) {
            Activity activity2 = activity;
            return null;
        }

        public Object setActionBarUpIndicator(Object obj, Activity activity, Drawable drawable, int i) {
            Activity activity2 = activity;
            Drawable drawable2 = drawable;
            int i2 = i;
            return obj;
        }

        public Object setActionBarDescription(Object obj, Activity activity, int i) {
            Activity activity2 = activity;
            int i2 = i;
            return obj;
        }
    }

    private static class ActionBarDrawerToggleImplHC implements ActionBarDrawerToggleImpl {
        private ActionBarDrawerToggleImplHC() {
        }

        /* synthetic */ ActionBarDrawerToggleImplHC(AnonymousClass1 anonymousClass1) {
            AnonymousClass1 anonymousClass12 = anonymousClass1;
            this();
        }

        public Drawable getThemeUpIndicator(Activity activity) {
            return ActionBarDrawerToggleHoneycomb.getThemeUpIndicator(activity);
        }

        public Object setActionBarUpIndicator(Object obj, Activity activity, Drawable drawable, int i) {
            return ActionBarDrawerToggleHoneycomb.setActionBarUpIndicator(obj, activity, drawable, i);
        }

        public Object setActionBarDescription(Object obj, Activity activity, int i) {
            return ActionBarDrawerToggleHoneycomb.setActionBarDescription(obj, activity, i);
        }
    }

    public interface Delegate {
        Drawable getThemeUpIndicator();

        void setActionBarDescription(int i);

        void setActionBarUpIndicator(Drawable drawable, int i);
    }

    public interface DelegateProvider {
        Delegate getDrawerToggleDelegate();
    }

    private class SlideDrawable extends LevelListDrawable implements Callback {
        private final boolean mHasMirroring;
        private float mOffset;
        private float mPosition;
        private final Rect mTmpRect;
        final /* synthetic */ ActionBarDrawerToggle this$0;

        /* synthetic */ SlideDrawable(ActionBarDrawerToggle actionBarDrawerToggle, Drawable drawable, AnonymousClass1 anonymousClass1) {
            AnonymousClass1 anonymousClass12 = anonymousClass1;
            this(actionBarDrawerToggle, drawable);
        }

        private SlideDrawable(ActionBarDrawerToggle actionBarDrawerToggle, Drawable drawable) {
            Drawable drawable2 = drawable;
            this.this$0 = actionBarDrawerToggle;
            this.mHasMirroring = VERSION.SDK_INT > 18;
            Rect rect = r7;
            Rect rect2 = new Rect();
            this.mTmpRect = rect;
            if (DrawableCompat.isAutoMirrored(drawable2)) {
                DrawableCompat.setAutoMirrored(this, true);
            }
            addLevel(0, 0, drawable2);
        }

        public void setPosition(float f) {
            this.mPosition = f;
            invalidateSelf();
        }

        public float getPosition() {
            return this.mPosition;
        }

        public void setOffset(float f) {
            this.mOffset = f;
            invalidateSelf();
        }

        public void draw(Canvas canvas) {
            Canvas canvas2 = canvas;
            copyBounds(this.mTmpRect);
            int save = canvas2.save();
            Object obj = ViewCompat.getLayoutDirection(this.this$0.mActivity.getWindow().getDecorView()) == 1 ? 1 : null;
            int i = obj != null ? -1 : 1;
            int width = this.mTmpRect.width();
            canvas2.translate((((-this.mOffset) * ((float) width)) * this.mPosition) * ((float) i), 0.0f);
            if (!(obj == null || this.mHasMirroring)) {
                canvas2.translate((float) width, 0.0f);
                canvas2.scale(-1.0f, 1.0f);
            }
            super.draw(canvas2);
            canvas2.restore();
        }
    }

    static {
        if (VERSION.SDK_INT >= 11) {
            ActionBarDrawerToggleImplHC actionBarDrawerToggleImplHC = r4;
            ActionBarDrawerToggleImplHC actionBarDrawerToggleImplHC2 = new ActionBarDrawerToggleImplHC();
            IMPL = actionBarDrawerToggleImplHC;
            return;
        }
        ActionBarDrawerToggleImplBase actionBarDrawerToggleImplBase = r4;
        ActionBarDrawerToggleImplBase actionBarDrawerToggleImplBase2 = new ActionBarDrawerToggleImplBase();
        IMPL = actionBarDrawerToggleImplBase;
    }

    public ActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, int i, int i2, int i3) {
        Activity activity2 = activity;
        DrawerLayout drawerLayout2 = drawerLayout;
        int i4 = i;
        int i5 = i2;
        int i6 = i3;
        this.mActivity = activity2;
        if (activity2 instanceof DelegateProvider) {
            this.mActivityImpl = ((DelegateProvider) activity2).getDrawerToggleDelegate();
        } else {
            this.mActivityImpl = null;
        }
        this.mDrawerLayout = drawerLayout2;
        this.mDrawerImageResource = i4;
        this.mOpenDrawerContentDescRes = i5;
        this.mCloseDrawerContentDescRes = i6;
        this.mThemeImage = getThemeUpIndicator();
        this.mDrawerImage = activity2.getResources().getDrawable(i4);
        SlideDrawable slideDrawable = r12;
        SlideDrawable slideDrawable2 = new SlideDrawable(this, this.mDrawerImage, null);
        this.mSlider = slideDrawable;
        this.mSlider.setOffset(TOGGLE_DRAWABLE_OFFSET);
    }

    public void syncState() {
        if (this.mDrawerLayout.isDrawerOpen((int) GravityCompat.START)) {
            this.mSlider.setPosition(1.0f);
        } else {
            this.mSlider.setPosition(0.0f);
        }
        if (this.mDrawerIndicatorEnabled) {
            setActionBarUpIndicator(this.mSlider, this.mDrawerLayout.isDrawerOpen((int) GravityCompat.START) ? this.mCloseDrawerContentDescRes : this.mOpenDrawerContentDescRes);
        }
    }

    public void setDrawerIndicatorEnabled(boolean z) {
        boolean z2 = z;
        if (z2 != this.mDrawerIndicatorEnabled) {
            if (z2) {
                setActionBarUpIndicator(this.mSlider, this.mDrawerLayout.isDrawerOpen((int) GravityCompat.START) ? this.mCloseDrawerContentDescRes : this.mOpenDrawerContentDescRes);
            } else {
                setActionBarUpIndicator(this.mThemeImage, 0);
            }
            this.mDrawerIndicatorEnabled = z2;
        }
    }

    public boolean isDrawerIndicatorEnabled() {
        return this.mDrawerIndicatorEnabled;
    }

    public void onConfigurationChanged(Configuration configuration) {
        Configuration configuration2 = configuration;
        this.mThemeImage = getThemeUpIndicator();
        this.mDrawerImage = this.mActivity.getResources().getDrawable(this.mDrawerImageResource);
        syncState();
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        MenuItem menuItem2 = menuItem;
        if (menuItem2 == null || menuItem2.getItemId() != ID_HOME || !this.mDrawerIndicatorEnabled) {
            return false;
        }
        if (this.mDrawerLayout.isDrawerVisible((int) GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer((int) GravityCompat.START);
        } else {
            this.mDrawerLayout.openDrawer((int) GravityCompat.START);
        }
        return true;
    }

    public void onDrawerSlide(View view, float f) {
        View view2 = view;
        float f2 = f;
        float position = this.mSlider.getPosition();
        if (f2 > 0.5f) {
            position = Math.max(position, Math.max(0.0f, f2 - 0.5f) * 2.0f);
        } else {
            position = Math.min(position, f2 * 2.0f);
        }
        this.mSlider.setPosition(position);
    }

    public void onDrawerOpened(View view) {
        View view2 = view;
        this.mSlider.setPosition(1.0f);
        if (this.mDrawerIndicatorEnabled) {
            setActionBarDescription(this.mCloseDrawerContentDescRes);
        }
    }

    public void onDrawerClosed(View view) {
        View view2 = view;
        this.mSlider.setPosition(0.0f);
        if (this.mDrawerIndicatorEnabled) {
            setActionBarDescription(this.mOpenDrawerContentDescRes);
        }
    }

    public void onDrawerStateChanged(int i) {
    }

    /* access modifiers changed from: 0000 */
    public Drawable getThemeUpIndicator() {
        if (this.mActivityImpl != null) {
            return this.mActivityImpl.getThemeUpIndicator();
        }
        return IMPL.getThemeUpIndicator(this.mActivity);
    }

    /* access modifiers changed from: 0000 */
    public void setActionBarUpIndicator(Drawable drawable, int i) {
        Drawable drawable2 = drawable;
        int i2 = i;
        if (this.mActivityImpl != null) {
            this.mActivityImpl.setActionBarUpIndicator(drawable2, i2);
            return;
        }
        this.mSetIndicatorInfo = IMPL.setActionBarUpIndicator(this.mSetIndicatorInfo, this.mActivity, drawable2, i2);
    }

    /* access modifiers changed from: 0000 */
    public void setActionBarDescription(int i) {
        int i2 = i;
        if (this.mActivityImpl != null) {
            this.mActivityImpl.setActionBarDescription(i2);
            return;
        }
        this.mSetIndicatorInfo = IMPL.setActionBarDescription(this.mSetIndicatorInfo, this.mActivity, i2);
    }
}
