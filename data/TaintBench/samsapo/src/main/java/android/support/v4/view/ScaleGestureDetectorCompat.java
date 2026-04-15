package android.support.v4.view;

import android.os.Build.VERSION;

public class ScaleGestureDetectorCompat {
    static final ScaleGestureDetectorImpl IMPL;

    private static class BaseScaleGestureDetectorImpl implements ScaleGestureDetectorImpl {
        private BaseScaleGestureDetectorImpl() {
        }

        /* synthetic */ BaseScaleGestureDetectorImpl(AnonymousClass1 anonymousClass1) {
            AnonymousClass1 anonymousClass12 = anonymousClass1;
            this();
        }

        public void setQuickScaleEnabled(Object obj, boolean z) {
        }

        public boolean isQuickScaleEnabled(Object obj) {
            Object obj2 = obj;
            return false;
        }
    }

    private static class ScaleGestureDetectorCompatKitKatImpl implements ScaleGestureDetectorImpl {
        private ScaleGestureDetectorCompatKitKatImpl() {
        }

        /* synthetic */ ScaleGestureDetectorCompatKitKatImpl(AnonymousClass1 anonymousClass1) {
            AnonymousClass1 anonymousClass12 = anonymousClass1;
            this();
        }

        public void setQuickScaleEnabled(Object obj, boolean z) {
            ScaleGestureDetectorCompatKitKat.setQuickScaleEnabled(obj, z);
        }

        public boolean isQuickScaleEnabled(Object obj) {
            return ScaleGestureDetectorCompatKitKat.isQuickScaleEnabled(obj);
        }
    }

    interface ScaleGestureDetectorImpl {
        boolean isQuickScaleEnabled(Object obj);

        void setQuickScaleEnabled(Object obj, boolean z);
    }

    static {
        if (VERSION.SDK_INT >= 19) {
            ScaleGestureDetectorCompatKitKatImpl scaleGestureDetectorCompatKitKatImpl = r4;
            ScaleGestureDetectorCompatKitKatImpl scaleGestureDetectorCompatKitKatImpl2 = new ScaleGestureDetectorCompatKitKatImpl();
            IMPL = scaleGestureDetectorCompatKitKatImpl;
            return;
        }
        BaseScaleGestureDetectorImpl baseScaleGestureDetectorImpl = r4;
        BaseScaleGestureDetectorImpl baseScaleGestureDetectorImpl2 = new BaseScaleGestureDetectorImpl();
        IMPL = baseScaleGestureDetectorImpl;
    }

    private ScaleGestureDetectorCompat() {
    }

    public static void setQuickScaleEnabled(Object obj, boolean z) {
        IMPL.setQuickScaleEnabled(obj, z);
    }

    public static boolean isQuickScaleEnabled(Object obj) {
        return IMPL.isQuickScaleEnabled(obj);
    }
}
