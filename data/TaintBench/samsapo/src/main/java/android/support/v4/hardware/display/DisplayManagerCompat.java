package android.support.v4.hardware.display;

import android.content.Context;
import android.os.Build.VERSION;
import android.view.Display;
import android.view.WindowManager;
import java.util.WeakHashMap;

public abstract class DisplayManagerCompat {
    public static final String DISPLAY_CATEGORY_PRESENTATION = "android.hardware.display.category.PRESENTATION";
    private static final WeakHashMap<Context, DisplayManagerCompat> sInstances;

    private static class JellybeanMr1Impl extends DisplayManagerCompat {
        private final Object mDisplayManagerObj;

        public JellybeanMr1Impl(Context context) {
            this.mDisplayManagerObj = DisplayManagerJellybeanMr1.getDisplayManager(context);
        }

        public Display getDisplay(int i) {
            return DisplayManagerJellybeanMr1.getDisplay(this.mDisplayManagerObj, i);
        }

        public Display[] getDisplays() {
            return DisplayManagerJellybeanMr1.getDisplays(this.mDisplayManagerObj);
        }

        public Display[] getDisplays(String str) {
            return DisplayManagerJellybeanMr1.getDisplays(this.mDisplayManagerObj, str);
        }
    }

    private static class LegacyImpl extends DisplayManagerCompat {
        private final WindowManager mWindowManager;

        public LegacyImpl(Context context) {
            this.mWindowManager = (WindowManager) context.getSystemService("window");
        }

        public Display getDisplay(int i) {
            int i2 = i;
            Display defaultDisplay = this.mWindowManager.getDefaultDisplay();
            if (defaultDisplay.getDisplayId() == i2) {
                return defaultDisplay;
            }
            return null;
        }

        public Display[] getDisplays() {
            Display[] displayArr = new Display[1];
            Display[] displayArr2 = displayArr;
            displayArr[0] = this.mWindowManager.getDefaultDisplay();
            return displayArr2;
        }

        public Display[] getDisplays(String str) {
            return str == null ? getDisplays() : new Display[0];
        }
    }

    public abstract Display getDisplay(int i);

    public abstract Display[] getDisplays();

    public abstract Display[] getDisplays(String str);

    static {
        WeakHashMap weakHashMap = r2;
        WeakHashMap weakHashMap2 = new WeakHashMap();
        sInstances = weakHashMap;
    }

    DisplayManagerCompat() {
    }

    public static DisplayManagerCompat getInstance(Context context) {
        Context context2 = context;
        WeakHashMap weakHashMap = sInstances;
        Object obj = weakHashMap;
        WeakHashMap weakHashMap2 = obj;
        synchronized (weakHashMap) {
            Object obj2;
            try {
                DisplayManagerCompat displayManagerCompat;
                obj = context2;
                DisplayManagerCompat displayManagerCompat2 = (DisplayManagerCompat) sInstances.get(obj);
                if (displayManagerCompat2 == null) {
                    obj2 = VERSION.SDK_INT;
                    DisplayManagerCompat jellybeanMr1Impl;
                    if (obj2 >= obj) {
                        displayManagerCompat = r8;
                        jellybeanMr1Impl = new JellybeanMr1Impl(context2);
                        displayManagerCompat2 = displayManagerCompat;
                    } else {
                        displayManagerCompat = r8;
                        jellybeanMr1Impl = new LegacyImpl(context2);
                        displayManagerCompat2 = displayManagerCompat;
                    }
                    obj2 = sInstances.put(context2, displayManagerCompat2);
                }
                displayManagerCompat = displayManagerCompat2;
                return displayManagerCompat;
            } finally {
                Object obj3 = obj2;
                WeakHashMap obj22 = weakHashMap2;
                obj22 = obj3;
            }
        }
    }
}
