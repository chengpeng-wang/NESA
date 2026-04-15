package android.support.v4.view;

import android.os.Build.VERSION;
import android.view.MotionEvent;

public class MotionEventCompat {
    public static final int ACTION_HOVER_ENTER = 9;
    public static final int ACTION_HOVER_EXIT = 10;
    public static final int ACTION_HOVER_MOVE = 7;
    public static final int ACTION_MASK = 255;
    public static final int ACTION_POINTER_DOWN = 5;
    public static final int ACTION_POINTER_INDEX_MASK = 65280;
    public static final int ACTION_POINTER_INDEX_SHIFT = 8;
    public static final int ACTION_POINTER_UP = 6;
    public static final int ACTION_SCROLL = 8;
    static final MotionEventVersionImpl IMPL;

    static class BaseMotionEventVersionImpl implements MotionEventVersionImpl {
        BaseMotionEventVersionImpl() {
        }

        public int findPointerIndex(MotionEvent motionEvent, int i) {
            MotionEvent motionEvent2 = motionEvent;
            if (i == 0) {
                return 0;
            }
            return -1;
        }

        public int getPointerId(MotionEvent motionEvent, int i) {
            MotionEvent motionEvent2 = motionEvent;
            if (i == 0) {
                return 0;
            }
            IndexOutOfBoundsException indexOutOfBoundsException = r6;
            IndexOutOfBoundsException indexOutOfBoundsException2 = new IndexOutOfBoundsException("Pre-Eclair does not support multiple pointers");
            throw indexOutOfBoundsException;
        }

        public float getX(MotionEvent motionEvent, int i) {
            MotionEvent motionEvent2 = motionEvent;
            if (i == 0) {
                return motionEvent2.getX();
            }
            IndexOutOfBoundsException indexOutOfBoundsException = r6;
            IndexOutOfBoundsException indexOutOfBoundsException2 = new IndexOutOfBoundsException("Pre-Eclair does not support multiple pointers");
            throw indexOutOfBoundsException;
        }

        public float getY(MotionEvent motionEvent, int i) {
            MotionEvent motionEvent2 = motionEvent;
            if (i == 0) {
                return motionEvent2.getY();
            }
            IndexOutOfBoundsException indexOutOfBoundsException = r6;
            IndexOutOfBoundsException indexOutOfBoundsException2 = new IndexOutOfBoundsException("Pre-Eclair does not support multiple pointers");
            throw indexOutOfBoundsException;
        }

        public int getPointerCount(MotionEvent motionEvent) {
            MotionEvent motionEvent2 = motionEvent;
            return 1;
        }
    }

    static class EclairMotionEventVersionImpl implements MotionEventVersionImpl {
        EclairMotionEventVersionImpl() {
        }

        public int findPointerIndex(MotionEvent motionEvent, int i) {
            return MotionEventCompatEclair.findPointerIndex(motionEvent, i);
        }

        public int getPointerId(MotionEvent motionEvent, int i) {
            return MotionEventCompatEclair.getPointerId(motionEvent, i);
        }

        public float getX(MotionEvent motionEvent, int i) {
            return MotionEventCompatEclair.getX(motionEvent, i);
        }

        public float getY(MotionEvent motionEvent, int i) {
            return MotionEventCompatEclair.getY(motionEvent, i);
        }

        public int getPointerCount(MotionEvent motionEvent) {
            return MotionEventCompatEclair.getPointerCount(motionEvent);
        }
    }

    interface MotionEventVersionImpl {
        int findPointerIndex(MotionEvent motionEvent, int i);

        int getPointerCount(MotionEvent motionEvent);

        int getPointerId(MotionEvent motionEvent, int i);

        float getX(MotionEvent motionEvent, int i);

        float getY(MotionEvent motionEvent, int i);
    }

    public MotionEventCompat() {
    }

    static {
        if (VERSION.SDK_INT >= 5) {
            EclairMotionEventVersionImpl eclairMotionEventVersionImpl = r2;
            EclairMotionEventVersionImpl eclairMotionEventVersionImpl2 = new EclairMotionEventVersionImpl();
            IMPL = eclairMotionEventVersionImpl;
            return;
        }
        BaseMotionEventVersionImpl baseMotionEventVersionImpl = r2;
        BaseMotionEventVersionImpl baseMotionEventVersionImpl2 = new BaseMotionEventVersionImpl();
        IMPL = baseMotionEventVersionImpl;
    }

    public static int getActionMasked(MotionEvent motionEvent) {
        return motionEvent.getAction() & ACTION_MASK;
    }

    public static int getActionIndex(MotionEvent motionEvent) {
        return (motionEvent.getAction() & ACTION_POINTER_INDEX_MASK) >> 8;
    }

    public static int findPointerIndex(MotionEvent motionEvent, int i) {
        return IMPL.findPointerIndex(motionEvent, i);
    }

    public static int getPointerId(MotionEvent motionEvent, int i) {
        return IMPL.getPointerId(motionEvent, i);
    }

    public static float getX(MotionEvent motionEvent, int i) {
        return IMPL.getX(motionEvent, i);
    }

    public static float getY(MotionEvent motionEvent, int i) {
        return IMPL.getY(motionEvent, i);
    }

    public static int getPointerCount(MotionEvent motionEvent) {
        return IMPL.getPointerCount(motionEvent);
    }
}
