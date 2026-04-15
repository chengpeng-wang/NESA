package android.support.v4.view;

import android.os.Build.VERSION;
import android.view.ViewConfiguration;

public class ViewConfigurationCompat {
    static final ViewConfigurationVersionImpl IMPL;

    static class BaseViewConfigurationVersionImpl implements ViewConfigurationVersionImpl {
        BaseViewConfigurationVersionImpl() {
        }

        public int getScaledPagingTouchSlop(ViewConfiguration viewConfiguration) {
            return viewConfiguration.getScaledTouchSlop();
        }
    }

    static class FroyoViewConfigurationVersionImpl implements ViewConfigurationVersionImpl {
        FroyoViewConfigurationVersionImpl() {
        }

        public int getScaledPagingTouchSlop(ViewConfiguration viewConfiguration) {
            return ViewConfigurationCompatFroyo.getScaledPagingTouchSlop(viewConfiguration);
        }
    }

    interface ViewConfigurationVersionImpl {
        int getScaledPagingTouchSlop(ViewConfiguration viewConfiguration);
    }

    public ViewConfigurationCompat() {
    }

    static {
        if (VERSION.SDK_INT >= 11) {
            FroyoViewConfigurationVersionImpl froyoViewConfigurationVersionImpl = r2;
            FroyoViewConfigurationVersionImpl froyoViewConfigurationVersionImpl2 = new FroyoViewConfigurationVersionImpl();
            IMPL = froyoViewConfigurationVersionImpl;
            return;
        }
        BaseViewConfigurationVersionImpl baseViewConfigurationVersionImpl = r2;
        BaseViewConfigurationVersionImpl baseViewConfigurationVersionImpl2 = new BaseViewConfigurationVersionImpl();
        IMPL = baseViewConfigurationVersionImpl;
    }

    public static int getScaledPagingTouchSlop(ViewConfiguration viewConfiguration) {
        return IMPL.getScaledPagingTouchSlop(viewConfiguration);
    }
}
