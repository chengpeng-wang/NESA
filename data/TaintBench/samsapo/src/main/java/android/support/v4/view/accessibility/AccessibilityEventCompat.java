package android.support.v4.view.accessibility;

import android.os.Build.VERSION;
import android.view.accessibility.AccessibilityEvent;

public class AccessibilityEventCompat {
    private static final AccessibilityEventVersionImpl IMPL;
    public static final int TYPES_ALL_MASK = -1;
    public static final int TYPE_ANNOUNCEMENT = 16384;
    public static final int TYPE_GESTURE_DETECTION_END = 524288;
    public static final int TYPE_GESTURE_DETECTION_START = 262144;
    public static final int TYPE_TOUCH_EXPLORATION_GESTURE_END = 1024;
    public static final int TYPE_TOUCH_EXPLORATION_GESTURE_START = 512;
    public static final int TYPE_TOUCH_INTERACTION_END = 2097152;
    public static final int TYPE_TOUCH_INTERACTION_START = 1048576;
    public static final int TYPE_VIEW_ACCESSIBILITY_FOCUSED = 32768;
    public static final int TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED = 65536;
    public static final int TYPE_VIEW_HOVER_ENTER = 128;
    public static final int TYPE_VIEW_HOVER_EXIT = 256;
    public static final int TYPE_VIEW_SCROLLED = 4096;
    public static final int TYPE_VIEW_TEXT_SELECTION_CHANGED = 8192;
    public static final int TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY = 131072;
    public static final int TYPE_WINDOW_CONTENT_CHANGED = 2048;

    static class AccessibilityEventStubImpl implements AccessibilityEventVersionImpl {
        AccessibilityEventStubImpl() {
        }

        public void appendRecord(AccessibilityEvent accessibilityEvent, Object obj) {
        }

        public Object getRecord(AccessibilityEvent accessibilityEvent, int i) {
            AccessibilityEvent accessibilityEvent2 = accessibilityEvent;
            int i2 = i;
            return null;
        }

        public int getRecordCount(AccessibilityEvent accessibilityEvent) {
            AccessibilityEvent accessibilityEvent2 = accessibilityEvent;
            return 0;
        }
    }

    static class AccessibilityEventIcsImpl extends AccessibilityEventStubImpl {
        AccessibilityEventIcsImpl() {
        }

        public void appendRecord(AccessibilityEvent accessibilityEvent, Object obj) {
            AccessibilityEventCompatIcs.appendRecord(accessibilityEvent, obj);
        }

        public Object getRecord(AccessibilityEvent accessibilityEvent, int i) {
            return AccessibilityEventCompatIcs.getRecord(accessibilityEvent, i);
        }

        public int getRecordCount(AccessibilityEvent accessibilityEvent) {
            return AccessibilityEventCompatIcs.getRecordCount(accessibilityEvent);
        }
    }

    interface AccessibilityEventVersionImpl {
        void appendRecord(AccessibilityEvent accessibilityEvent, Object obj);

        Object getRecord(AccessibilityEvent accessibilityEvent, int i);

        int getRecordCount(AccessibilityEvent accessibilityEvent);
    }

    static {
        if (VERSION.SDK_INT >= 14) {
            AccessibilityEventIcsImpl accessibilityEventIcsImpl = r2;
            AccessibilityEventIcsImpl accessibilityEventIcsImpl2 = new AccessibilityEventIcsImpl();
            IMPL = accessibilityEventIcsImpl;
            return;
        }
        AccessibilityEventStubImpl accessibilityEventStubImpl = r2;
        AccessibilityEventStubImpl accessibilityEventStubImpl2 = new AccessibilityEventStubImpl();
        IMPL = accessibilityEventStubImpl;
    }

    private AccessibilityEventCompat() {
    }

    public static int getRecordCount(AccessibilityEvent accessibilityEvent) {
        return IMPL.getRecordCount(accessibilityEvent);
    }

    public static void appendRecord(AccessibilityEvent accessibilityEvent, AccessibilityRecordCompat accessibilityRecordCompat) {
        IMPL.appendRecord(accessibilityEvent, accessibilityRecordCompat.getImpl());
    }

    public static AccessibilityRecordCompat getRecord(AccessibilityEvent accessibilityEvent, int i) {
        AccessibilityRecordCompat accessibilityRecordCompat = r7;
        AccessibilityRecordCompat accessibilityRecordCompat2 = new AccessibilityRecordCompat(IMPL.getRecord(accessibilityEvent, i));
        return accessibilityRecordCompat;
    }

    public static AccessibilityRecordCompat asRecord(AccessibilityEvent accessibilityEvent) {
        AccessibilityRecordCompat accessibilityRecordCompat = r4;
        AccessibilityRecordCompat accessibilityRecordCompat2 = new AccessibilityRecordCompat(accessibilityEvent);
        return accessibilityRecordCompat;
    }
}
