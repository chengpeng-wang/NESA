package android.support.v4.graphics.drawable;

import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;

public class DrawableCompat {
    static final DrawableImpl IMPL;

    static class BaseDrawableImpl implements DrawableImpl {
        BaseDrawableImpl() {
        }

        public void jumpToCurrentState(Drawable drawable) {
        }

        public void setAutoMirrored(Drawable drawable, boolean z) {
        }

        public boolean isAutoMirrored(Drawable drawable) {
            Drawable drawable2 = drawable;
            return false;
        }
    }

    interface DrawableImpl {
        boolean isAutoMirrored(Drawable drawable);

        void jumpToCurrentState(Drawable drawable);

        void setAutoMirrored(Drawable drawable, boolean z);
    }

    static class HoneycombDrawableImpl extends BaseDrawableImpl {
        HoneycombDrawableImpl() {
        }

        public void jumpToCurrentState(Drawable drawable) {
            DrawableCompatHoneycomb.jumpToCurrentState(drawable);
        }
    }

    static class KitKatDrawableImpl extends HoneycombDrawableImpl {
        KitKatDrawableImpl() {
        }

        public void setAutoMirrored(Drawable drawable, boolean z) {
            DrawableCompatKitKat.setAutoMirrored(drawable, z);
        }

        public boolean isAutoMirrored(Drawable drawable) {
            return DrawableCompatKitKat.isAutoMirrored(drawable);
        }
    }

    public DrawableCompat() {
    }

    static {
        int i = VERSION.SDK_INT;
        if (i >= 19) {
            KitKatDrawableImpl kitKatDrawableImpl = r3;
            KitKatDrawableImpl kitKatDrawableImpl2 = new KitKatDrawableImpl();
            IMPL = kitKatDrawableImpl;
        } else if (i >= 11) {
            HoneycombDrawableImpl honeycombDrawableImpl = r3;
            HoneycombDrawableImpl honeycombDrawableImpl2 = new HoneycombDrawableImpl();
            IMPL = honeycombDrawableImpl;
        } else {
            BaseDrawableImpl baseDrawableImpl = r3;
            BaseDrawableImpl baseDrawableImpl2 = new BaseDrawableImpl();
            IMPL = baseDrawableImpl;
        }
    }

    public static void jumpToCurrentState(Drawable drawable) {
        IMPL.jumpToCurrentState(drawable);
    }

    public static void setAutoMirrored(Drawable drawable, boolean z) {
        IMPL.setAutoMirrored(drawable, z);
    }

    public static boolean isAutoMirrored(Drawable drawable) {
        return IMPL.isAutoMirrored(drawable);
    }
}
