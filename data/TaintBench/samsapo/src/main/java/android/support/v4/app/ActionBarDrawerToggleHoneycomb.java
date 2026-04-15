package android.support.v4.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.lang.reflect.Method;

class ActionBarDrawerToggleHoneycomb {
    private static final String TAG = "ActionBarDrawerToggleHoneycomb";
    private static final int[] THEME_ATTRS;

    private static class SetIndicatorInfo {
        public Method setHomeActionContentDescription;
        public Method setHomeAsUpIndicator;
        public ImageView upIndicatorView;

        SetIndicatorInfo(Activity activity) {
            Activity activity2 = activity;
            try {
                Class[] clsArr = new Class[1];
                Class[] clsArr2 = clsArr;
                clsArr[0] = Drawable.class;
                this.setHomeAsUpIndicator = ActionBar.class.getDeclaredMethod("setHomeAsUpIndicator", clsArr2);
                clsArr = new Class[1];
                clsArr2 = clsArr;
                clsArr[0] = Integer.TYPE;
                this.setHomeActionContentDescription = ActionBar.class.getDeclaredMethod("setHomeActionContentDescription", clsArr2);
            } catch (NoSuchMethodException e) {
                NoSuchMethodException noSuchMethodException = e;
                View findViewById = activity2.findViewById(16908332);
                if (findViewById != null) {
                    ViewGroup viewGroup = (ViewGroup) findViewById.getParent();
                    if (viewGroup.getChildCount() == 2) {
                        View childAt = viewGroup.getChildAt(0);
                        View childAt2 = childAt.getId() == 16908332 ? viewGroup.getChildAt(1) : childAt;
                        if (childAt2 instanceof ImageView) {
                            this.upIndicatorView = (ImageView) childAt2;
                        }
                    }
                }
            }
        }
    }

    ActionBarDrawerToggleHoneycomb() {
    }

    static {
        int[] iArr = new int[1];
        int[] iArr2 = iArr;
        iArr[0] = 16843531;
        THEME_ATTRS = iArr2;
    }

    public static Object setActionBarUpIndicator(Object obj, Activity activity, Drawable drawable, int i) {
        int w;
        Object obj2 = obj;
        Activity activity2 = activity;
        Drawable drawable2 = drawable;
        int i2 = i;
        if (obj2 == null) {
            SetIndicatorInfo setIndicatorInfo = r12;
            SetIndicatorInfo setIndicatorInfo2 = new SetIndicatorInfo(activity2);
            obj2 = setIndicatorInfo;
        }
        SetIndicatorInfo setIndicatorInfo3 = (SetIndicatorInfo) obj2;
        if (setIndicatorInfo3.setHomeAsUpIndicator != null) {
            try {
                ActionBar actionBar = activity2.getActionBar();
                Method method = setIndicatorInfo3.setHomeAsUpIndicator;
                ActionBar actionBar2 = actionBar;
                Object[] objArr = new Object[1];
                Object[] objArr2 = objArr;
                objArr[0] = drawable2;
                Object invoke = method.invoke(actionBar2, objArr2);
                method = setIndicatorInfo3.setHomeActionContentDescription;
                actionBar2 = actionBar;
                Integer[] numArr = new Object[1];
                Integer[] numArr2 = numArr;
                numArr[0] = Integer.valueOf(i2);
                invoke = method.invoke(actionBar2, numArr2);
            } catch (Exception e) {
                w = Log.w(TAG, "Couldn't set home-as-up indicator via JB-MR2 API", e);
            }
        } else if (setIndicatorInfo3.upIndicatorView != null) {
            setIndicatorInfo3.upIndicatorView.setImageDrawable(drawable2);
        } else {
            w = Log.w(TAG, "Couldn't set home-as-up indicator");
        }
        return obj2;
    }

    public static Object setActionBarDescription(Object obj, Activity activity, int i) {
        Object obj2 = obj;
        Activity activity2 = activity;
        int i2 = i;
        if (obj2 == null) {
            SetIndicatorInfo setIndicatorInfo = r11;
            SetIndicatorInfo setIndicatorInfo2 = new SetIndicatorInfo(activity2);
            obj2 = setIndicatorInfo;
        }
        SetIndicatorInfo setIndicatorInfo3 = (SetIndicatorInfo) obj2;
        if (setIndicatorInfo3.setHomeAsUpIndicator != null) {
            try {
                ActionBar actionBar = activity2.getActionBar();
                Method method = setIndicatorInfo3.setHomeActionContentDescription;
                ActionBar actionBar2 = actionBar;
                Integer[] numArr = new Object[1];
                Integer[] numArr2 = numArr;
                numArr[0] = Integer.valueOf(i2);
                Object invoke = method.invoke(actionBar2, numArr2);
            } catch (Exception e) {
                int w = Log.w(TAG, "Couldn't set content description via JB-MR2 API", e);
            }
        }
        return obj2;
    }

    public static Drawable getThemeUpIndicator(Activity activity) {
        TypedArray obtainStyledAttributes = activity.obtainStyledAttributes(THEME_ATTRS);
        Drawable drawable = obtainStyledAttributes.getDrawable(0);
        obtainStyledAttributes.recycle();
        return drawable;
    }
}
