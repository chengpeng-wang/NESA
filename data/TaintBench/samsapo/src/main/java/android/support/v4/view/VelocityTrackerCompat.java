package android.support.v4.view;

import android.os.Build.VERSION;
import android.view.VelocityTracker;

public class VelocityTrackerCompat {
    static final VelocityTrackerVersionImpl IMPL;

    static class BaseVelocityTrackerVersionImpl implements VelocityTrackerVersionImpl {
        BaseVelocityTrackerVersionImpl() {
        }

        public float getXVelocity(VelocityTracker velocityTracker, int i) {
            int i2 = i;
            return velocityTracker.getXVelocity();
        }

        public float getYVelocity(VelocityTracker velocityTracker, int i) {
            int i2 = i;
            return velocityTracker.getYVelocity();
        }
    }

    static class HoneycombVelocityTrackerVersionImpl implements VelocityTrackerVersionImpl {
        HoneycombVelocityTrackerVersionImpl() {
        }

        public float getXVelocity(VelocityTracker velocityTracker, int i) {
            return VelocityTrackerCompatHoneycomb.getXVelocity(velocityTracker, i);
        }

        public float getYVelocity(VelocityTracker velocityTracker, int i) {
            return VelocityTrackerCompatHoneycomb.getYVelocity(velocityTracker, i);
        }
    }

    interface VelocityTrackerVersionImpl {
        float getXVelocity(VelocityTracker velocityTracker, int i);

        float getYVelocity(VelocityTracker velocityTracker, int i);
    }

    public VelocityTrackerCompat() {
    }

    static {
        if (VERSION.SDK_INT >= 11) {
            HoneycombVelocityTrackerVersionImpl honeycombVelocityTrackerVersionImpl = r2;
            HoneycombVelocityTrackerVersionImpl honeycombVelocityTrackerVersionImpl2 = new HoneycombVelocityTrackerVersionImpl();
            IMPL = honeycombVelocityTrackerVersionImpl;
            return;
        }
        BaseVelocityTrackerVersionImpl baseVelocityTrackerVersionImpl = r2;
        BaseVelocityTrackerVersionImpl baseVelocityTrackerVersionImpl2 = new BaseVelocityTrackerVersionImpl();
        IMPL = baseVelocityTrackerVersionImpl;
    }

    public static float getXVelocity(VelocityTracker velocityTracker, int i) {
        return IMPL.getXVelocity(velocityTracker, i);
    }

    public static float getYVelocity(VelocityTracker velocityTracker, int i) {
        return IMPL.getYVelocity(velocityTracker, i);
    }
}
