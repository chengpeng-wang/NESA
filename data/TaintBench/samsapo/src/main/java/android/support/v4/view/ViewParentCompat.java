package android.support.v4.view;

import android.os.Build.VERSION;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

public class ViewParentCompat {
    static final ViewParentCompatImpl IMPL;

    static class ViewParentCompatStubImpl implements ViewParentCompatImpl {
        ViewParentCompatStubImpl() {
        }

        public boolean requestSendAccessibilityEvent(ViewParent viewParent, View view, AccessibilityEvent accessibilityEvent) {
            ViewParent viewParent2 = viewParent;
            View view2 = view;
            AccessibilityEvent accessibilityEvent2 = accessibilityEvent;
            if (view2 == null) {
                return false;
            }
            ((AccessibilityManager) view2.getContext().getSystemService("accessibility")).sendAccessibilityEvent(accessibilityEvent2);
            return true;
        }
    }

    static class ViewParentCompatICSImpl extends ViewParentCompatStubImpl {
        ViewParentCompatICSImpl() {
        }

        public boolean requestSendAccessibilityEvent(ViewParent viewParent, View view, AccessibilityEvent accessibilityEvent) {
            return ViewParentCompatICS.requestSendAccessibilityEvent(viewParent, view, accessibilityEvent);
        }
    }

    interface ViewParentCompatImpl {
        boolean requestSendAccessibilityEvent(ViewParent viewParent, View view, AccessibilityEvent accessibilityEvent);
    }

    static {
        if (VERSION.SDK_INT >= 14) {
            ViewParentCompatICSImpl viewParentCompatICSImpl = r3;
            ViewParentCompatICSImpl viewParentCompatICSImpl2 = new ViewParentCompatICSImpl();
            IMPL = viewParentCompatICSImpl;
            return;
        }
        ViewParentCompatStubImpl viewParentCompatStubImpl = r3;
        ViewParentCompatStubImpl viewParentCompatStubImpl2 = new ViewParentCompatStubImpl();
        IMPL = viewParentCompatStubImpl;
    }

    private ViewParentCompat() {
    }

    public static boolean requestSendAccessibilityEvent(ViewParent viewParent, View view, AccessibilityEvent accessibilityEvent) {
        return IMPL.requestSendAccessibilityEvent(viewParent, view, accessibilityEvent);
    }
}
