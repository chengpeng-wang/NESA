package android.support.v4.view;

import android.os.Build.VERSION;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;

public class ViewGroupCompat {
    static final ViewGroupCompatImpl IMPL;
    public static final int LAYOUT_MODE_CLIP_BOUNDS = 0;
    public static final int LAYOUT_MODE_OPTICAL_BOUNDS = 1;

    static class ViewGroupCompatStubImpl implements ViewGroupCompatImpl {
        ViewGroupCompatStubImpl() {
        }

        public boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
            ViewGroup viewGroup2 = viewGroup;
            View view2 = view;
            AccessibilityEvent accessibilityEvent2 = accessibilityEvent;
            return true;
        }

        public void setMotionEventSplittingEnabled(ViewGroup viewGroup, boolean z) {
        }

        public int getLayoutMode(ViewGroup viewGroup) {
            ViewGroup viewGroup2 = viewGroup;
            return 0;
        }

        public void setLayoutMode(ViewGroup viewGroup, int i) {
        }
    }

    static class ViewGroupCompatHCImpl extends ViewGroupCompatStubImpl {
        ViewGroupCompatHCImpl() {
        }

        public void setMotionEventSplittingEnabled(ViewGroup viewGroup, boolean z) {
            ViewGroupCompatHC.setMotionEventSplittingEnabled(viewGroup, z);
        }
    }

    static class ViewGroupCompatIcsImpl extends ViewGroupCompatHCImpl {
        ViewGroupCompatIcsImpl() {
        }

        public boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
            return ViewGroupCompatIcs.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
        }
    }

    interface ViewGroupCompatImpl {
        int getLayoutMode(ViewGroup viewGroup);

        boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent);

        void setLayoutMode(ViewGroup viewGroup, int i);

        void setMotionEventSplittingEnabled(ViewGroup viewGroup, boolean z);
    }

    static class ViewGroupCompatJellybeanMR2Impl extends ViewGroupCompatIcsImpl {
        ViewGroupCompatJellybeanMR2Impl() {
        }

        public int getLayoutMode(ViewGroup viewGroup) {
            return ViewGroupCompatJellybeanMR2.getLayoutMode(viewGroup);
        }

        public void setLayoutMode(ViewGroup viewGroup, int i) {
            ViewGroupCompatJellybeanMR2.setLayoutMode(viewGroup, i);
        }
    }

    static {
        int i = VERSION.SDK_INT;
        if (i >= 18) {
            ViewGroupCompatJellybeanMR2Impl viewGroupCompatJellybeanMR2Impl = r3;
            ViewGroupCompatJellybeanMR2Impl viewGroupCompatJellybeanMR2Impl2 = new ViewGroupCompatJellybeanMR2Impl();
            IMPL = viewGroupCompatJellybeanMR2Impl;
        } else if (i >= 14) {
            ViewGroupCompatIcsImpl viewGroupCompatIcsImpl = r3;
            ViewGroupCompatIcsImpl viewGroupCompatIcsImpl2 = new ViewGroupCompatIcsImpl();
            IMPL = viewGroupCompatIcsImpl;
        } else if (i >= 11) {
            ViewGroupCompatHCImpl viewGroupCompatHCImpl = r3;
            ViewGroupCompatHCImpl viewGroupCompatHCImpl2 = new ViewGroupCompatHCImpl();
            IMPL = viewGroupCompatHCImpl;
        } else {
            ViewGroupCompatStubImpl viewGroupCompatStubImpl = r3;
            ViewGroupCompatStubImpl viewGroupCompatStubImpl2 = new ViewGroupCompatStubImpl();
            IMPL = viewGroupCompatStubImpl;
        }
    }

    private ViewGroupCompat() {
    }

    public static boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
        return IMPL.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
    }

    public static void setMotionEventSplittingEnabled(ViewGroup viewGroup, boolean z) {
        IMPL.setMotionEventSplittingEnabled(viewGroup, z);
    }

    public static int getLayoutMode(ViewGroup viewGroup) {
        return IMPL.getLayoutMode(viewGroup);
    }

    public static void setLayoutMode(ViewGroup viewGroup, int i) {
        IMPL.setLayoutMode(viewGroup, i);
    }
}
