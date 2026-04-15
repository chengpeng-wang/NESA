package android.support.v4.view;

import android.os.Build.VERSION;
import android.view.KeyEvent;
import android.view.KeyEvent.Callback;
import android.view.View;

public class KeyEventCompat {
    static final KeyEventVersionImpl IMPL;

    static class BaseKeyEventVersionImpl implements KeyEventVersionImpl {
        private static final int META_ALL_MASK = 247;
        private static final int META_MODIFIER_MASK = 247;

        BaseKeyEventVersionImpl() {
        }

        private static int metaStateFilterDirectionalModifiers(int i, int i2, int i3, int i4, int i5) {
            int i6 = i;
            int i7 = i2;
            int i8 = i3;
            Object obj = (i7 & i8) != 0 ? 1 : null;
            int i9 = i4 | i5;
            Object obj2 = (i7 & i9) != 0 ? 1 : null;
            if (obj != null) {
                if (obj2 == null) {
                    return i6 & (i9 ^ -1);
                }
                IllegalArgumentException illegalArgumentException = r11;
                IllegalArgumentException illegalArgumentException2 = new IllegalArgumentException("bad arguments");
                throw illegalArgumentException;
            } else if (obj2 != null) {
                return i6 & (i8 ^ -1);
            } else {
                return i6;
            }
        }

        public int normalizeMetaState(int i) {
            int i2 = i;
            if ((i2 & 192) != 0) {
                i2 |= 1;
            }
            if ((i2 & 48) != 0) {
                i2 |= 2;
            }
            return i2 & 247;
        }

        public boolean metaStateHasModifiers(int i, int i2) {
            int i3 = i2;
            return metaStateFilterDirectionalModifiers(metaStateFilterDirectionalModifiers(normalizeMetaState(i) & 247, i3, 1, 64, 128), i3, 2, 16, 32) == i3;
        }

        public boolean metaStateHasNoModifiers(int i) {
            return (normalizeMetaState(i) & 247) == 0;
        }

        public void startTracking(KeyEvent keyEvent) {
        }

        public boolean isTracking(KeyEvent keyEvent) {
            KeyEvent keyEvent2 = keyEvent;
            return false;
        }

        public Object getKeyDispatcherState(View view) {
            View view2 = view;
            return null;
        }

        public boolean dispatch(KeyEvent keyEvent, Callback callback, Object obj, Object obj2) {
            Object obj3 = obj;
            Object obj4 = obj2;
            return keyEvent.dispatch(callback);
        }
    }

    static class EclairKeyEventVersionImpl extends BaseKeyEventVersionImpl {
        EclairKeyEventVersionImpl() {
        }

        public void startTracking(KeyEvent keyEvent) {
            KeyEventCompatEclair.startTracking(keyEvent);
        }

        public boolean isTracking(KeyEvent keyEvent) {
            return KeyEventCompatEclair.isTracking(keyEvent);
        }

        public Object getKeyDispatcherState(View view) {
            return KeyEventCompatEclair.getKeyDispatcherState(view);
        }

        public boolean dispatch(KeyEvent keyEvent, Callback callback, Object obj, Object obj2) {
            return KeyEventCompatEclair.dispatch(keyEvent, callback, obj, obj2);
        }
    }

    static class HoneycombKeyEventVersionImpl extends EclairKeyEventVersionImpl {
        HoneycombKeyEventVersionImpl() {
        }

        public int normalizeMetaState(int i) {
            return KeyEventCompatHoneycomb.normalizeMetaState(i);
        }

        public boolean metaStateHasModifiers(int i, int i2) {
            return KeyEventCompatHoneycomb.metaStateHasModifiers(i, i2);
        }

        public boolean metaStateHasNoModifiers(int i) {
            return KeyEventCompatHoneycomb.metaStateHasNoModifiers(i);
        }
    }

    interface KeyEventVersionImpl {
        boolean dispatch(KeyEvent keyEvent, Callback callback, Object obj, Object obj2);

        Object getKeyDispatcherState(View view);

        boolean isTracking(KeyEvent keyEvent);

        boolean metaStateHasModifiers(int i, int i2);

        boolean metaStateHasNoModifiers(int i);

        int normalizeMetaState(int i);

        void startTracking(KeyEvent keyEvent);
    }

    public KeyEventCompat() {
    }

    static {
        if (VERSION.SDK_INT >= 11) {
            HoneycombKeyEventVersionImpl honeycombKeyEventVersionImpl = r2;
            HoneycombKeyEventVersionImpl honeycombKeyEventVersionImpl2 = new HoneycombKeyEventVersionImpl();
            IMPL = honeycombKeyEventVersionImpl;
            return;
        }
        BaseKeyEventVersionImpl baseKeyEventVersionImpl = r2;
        BaseKeyEventVersionImpl baseKeyEventVersionImpl2 = new BaseKeyEventVersionImpl();
        IMPL = baseKeyEventVersionImpl;
    }

    public static int normalizeMetaState(int i) {
        return IMPL.normalizeMetaState(i);
    }

    public static boolean metaStateHasModifiers(int i, int i2) {
        return IMPL.metaStateHasModifiers(i, i2);
    }

    public static boolean metaStateHasNoModifiers(int i) {
        return IMPL.metaStateHasNoModifiers(i);
    }

    public static boolean hasModifiers(KeyEvent keyEvent, int i) {
        return IMPL.metaStateHasModifiers(keyEvent.getMetaState(), i);
    }

    public static boolean hasNoModifiers(KeyEvent keyEvent) {
        return IMPL.metaStateHasNoModifiers(keyEvent.getMetaState());
    }

    public static void startTracking(KeyEvent keyEvent) {
        IMPL.startTracking(keyEvent);
    }

    public static boolean isTracking(KeyEvent keyEvent) {
        return IMPL.isTracking(keyEvent);
    }

    public static Object getKeyDispatcherState(View view) {
        return IMPL.getKeyDispatcherState(view);
    }

    public static boolean dispatch(KeyEvent keyEvent, Callback callback, Object obj, Object obj2) {
        return IMPL.dispatch(keyEvent, callback, obj, obj2);
    }
}
