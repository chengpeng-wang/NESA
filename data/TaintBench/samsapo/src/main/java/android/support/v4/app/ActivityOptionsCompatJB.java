package android.support.v4.app;

import android.app.ActivityOptions;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

class ActivityOptionsCompatJB {
    private final ActivityOptions mActivityOptions;

    public static ActivityOptionsCompatJB makeCustomAnimation(Context context, int i, int i2) {
        ActivityOptionsCompatJB activityOptionsCompatJB = r8;
        ActivityOptionsCompatJB activityOptionsCompatJB2 = new ActivityOptionsCompatJB(ActivityOptions.makeCustomAnimation(context, i, i2));
        return activityOptionsCompatJB;
    }

    public static ActivityOptionsCompatJB makeScaleUpAnimation(View view, int i, int i2, int i3, int i4) {
        ActivityOptionsCompatJB activityOptionsCompatJB = r12;
        ActivityOptionsCompatJB activityOptionsCompatJB2 = new ActivityOptionsCompatJB(ActivityOptions.makeScaleUpAnimation(view, i, i2, i3, i4));
        return activityOptionsCompatJB;
    }

    public static ActivityOptionsCompatJB makeThumbnailScaleUpAnimation(View view, Bitmap bitmap, int i, int i2) {
        ActivityOptionsCompatJB activityOptionsCompatJB = r10;
        ActivityOptionsCompatJB activityOptionsCompatJB2 = new ActivityOptionsCompatJB(ActivityOptions.makeThumbnailScaleUpAnimation(view, bitmap, i, i2));
        return activityOptionsCompatJB;
    }

    private ActivityOptionsCompatJB(ActivityOptions activityOptions) {
        this.mActivityOptions = activityOptions;
    }

    public Bundle toBundle() {
        return this.mActivityOptions.toBundle();
    }

    public void update(ActivityOptionsCompatJB activityOptionsCompatJB) {
        this.mActivityOptions.update(activityOptionsCompatJB.mActivityOptions);
    }
}
