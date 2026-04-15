package android.support.v4.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.View;

public class ActivityOptionsCompat {

    private static class ActivityOptionsImplJB extends ActivityOptionsCompat {
        private final ActivityOptionsCompatJB mImpl;

        ActivityOptionsImplJB(ActivityOptionsCompatJB activityOptionsCompatJB) {
            this.mImpl = activityOptionsCompatJB;
        }

        public Bundle toBundle() {
            return this.mImpl.toBundle();
        }

        public void update(ActivityOptionsCompat activityOptionsCompat) {
            ActivityOptionsCompat activityOptionsCompat2 = activityOptionsCompat;
            if (activityOptionsCompat2 instanceof ActivityOptionsImplJB) {
                this.mImpl.update(((ActivityOptionsImplJB) activityOptionsCompat2).mImpl);
            }
        }
    }

    public static ActivityOptionsCompat makeCustomAnimation(Context context, int i, int i2) {
        Context context2 = context;
        int i3 = i;
        int i4 = i2;
        ActivityOptionsCompat activityOptionsCompat;
        ActivityOptionsCompat activityOptionsImplJB;
        if (VERSION.SDK_INT >= 16) {
            activityOptionsCompat = r8;
            activityOptionsImplJB = new ActivityOptionsImplJB(ActivityOptionsCompatJB.makeCustomAnimation(context2, i3, i4));
            return activityOptionsCompat;
        }
        activityOptionsCompat = r8;
        activityOptionsImplJB = new ActivityOptionsCompat();
        return activityOptionsCompat;
    }

    public static ActivityOptionsCompat makeScaleUpAnimation(View view, int i, int i2, int i3, int i4) {
        View view2 = view;
        int i5 = i;
        int i6 = i2;
        int i7 = i3;
        int i8 = i4;
        ActivityOptionsCompat activityOptionsCompat;
        ActivityOptionsCompat activityOptionsImplJB;
        if (VERSION.SDK_INT >= 16) {
            activityOptionsCompat = r12;
            activityOptionsImplJB = new ActivityOptionsImplJB(ActivityOptionsCompatJB.makeScaleUpAnimation(view2, i5, i6, i7, i8));
            return activityOptionsCompat;
        }
        activityOptionsCompat = r12;
        activityOptionsImplJB = new ActivityOptionsCompat();
        return activityOptionsCompat;
    }

    public static ActivityOptionsCompat makeThumbnailScaleUpAnimation(View view, Bitmap bitmap, int i, int i2) {
        View view2 = view;
        Bitmap bitmap2 = bitmap;
        int i3 = i;
        int i4 = i2;
        ActivityOptionsCompat activityOptionsCompat;
        ActivityOptionsCompat activityOptionsImplJB;
        if (VERSION.SDK_INT >= 16) {
            activityOptionsCompat = r10;
            activityOptionsImplJB = new ActivityOptionsImplJB(ActivityOptionsCompatJB.makeThumbnailScaleUpAnimation(view2, bitmap2, i3, i4));
            return activityOptionsCompat;
        }
        activityOptionsCompat = r10;
        activityOptionsImplJB = new ActivityOptionsCompat();
        return activityOptionsCompat;
    }

    protected ActivityOptionsCompat() {
    }

    public Bundle toBundle() {
        return null;
    }

    public void update(ActivityOptionsCompat activityOptionsCompat) {
    }
}
