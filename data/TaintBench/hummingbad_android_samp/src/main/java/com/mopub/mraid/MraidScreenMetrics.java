package com.mopub.mraid;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import com.mopub.common.util.Dips;

class MraidScreenMetrics {
    @NonNull
    private final Context mContext;
    @NonNull
    private final Rect mCurrentAdRect = new Rect();
    @NonNull
    private final Rect mCurrentAdRectDips = new Rect();
    @NonNull
    private final Rect mDefaultAdRect = new Rect();
    @NonNull
    private final Rect mDefaultAdRectDips = new Rect();
    private final float mDensity;
    @NonNull
    private final Rect mRootViewRect = new Rect();
    @NonNull
    private final Rect mRootViewRectDips = new Rect();
    @NonNull
    private final Rect mScreenRect = new Rect();
    @NonNull
    private final Rect mScreenRectDips = new Rect();

    MraidScreenMetrics(Context context, float density) {
        this.mContext = context.getApplicationContext();
        this.mDensity = density;
    }

    private void convertToDips(Rect sourceRect, Rect outRect) {
        outRect.set(Dips.pixelsToIntDips((float) sourceRect.left, this.mContext), Dips.pixelsToIntDips((float) sourceRect.top, this.mContext), Dips.pixelsToIntDips((float) sourceRect.right, this.mContext), Dips.pixelsToIntDips((float) sourceRect.bottom, this.mContext));
    }

    public float getDensity() {
        return this.mDensity;
    }

    /* access modifiers changed from: 0000 */
    public void setScreenSize(int width, int height) {
        this.mScreenRect.set(0, 0, width, height);
        convertToDips(this.mScreenRect, this.mScreenRectDips);
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public Rect getScreenRect() {
        return this.mScreenRect;
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public Rect getScreenRectDips() {
        return this.mScreenRectDips;
    }

    /* access modifiers changed from: 0000 */
    public void setRootViewPosition(int x, int y, int width, int height) {
        this.mRootViewRect.set(x, y, x + width, y + height);
        convertToDips(this.mRootViewRect, this.mRootViewRectDips);
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public Rect getRootViewRect() {
        return this.mRootViewRect;
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public Rect getRootViewRectDips() {
        return this.mRootViewRectDips;
    }

    /* access modifiers changed from: 0000 */
    public void setCurrentAdPosition(int x, int y, int width, int height) {
        this.mCurrentAdRect.set(x, y, x + width, y + height);
        convertToDips(this.mCurrentAdRect, this.mCurrentAdRectDips);
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public Rect getCurrentAdRect() {
        return this.mCurrentAdRect;
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public Rect getCurrentAdRectDips() {
        return this.mCurrentAdRectDips;
    }

    /* access modifiers changed from: 0000 */
    public void setDefaultAdPosition(int x, int y, int width, int height) {
        this.mDefaultAdRect.set(x, y, x + width, y + height);
        convertToDips(this.mDefaultAdRect, this.mDefaultAdRectDips);
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public Rect getDefaultAdRect() {
        return this.mDefaultAdRect;
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public Rect getDefaultAdRectDips() {
        return this.mDefaultAdRectDips;
    }
}
