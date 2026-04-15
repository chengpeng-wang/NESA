package com.mopub.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import com.mopub.common.util.Dips;
import com.mopub.common.util.Drawables;

public class CloseableLayout extends FrameLayout {
    @VisibleForTesting
    static final float CLOSE_BUTTON_PADDING_DP = 8.0f;
    @VisibleForTesting
    static final float CLOSE_BUTTON_SIZE_DP = 30.0f;
    static final float CLOSE_REGION_SIZE_DP = 50.0f;
    private final Rect mClosableLayoutRect = new Rect();
    private boolean mCloseBoundChanged;
    private final Rect mCloseButtonBounds = new Rect();
    private final int mCloseButtonPadding;
    private final int mCloseButtonSize;
    @NonNull
    private final StateListDrawable mCloseDrawable = new StateListDrawable();
    @NonNull
    private ClosePosition mClosePosition = ClosePosition.TOP_RIGHT;
    private final Rect mCloseRegionBounds = new Rect();
    private final int mCloseRegionSize;
    private final Rect mInsetCloseRegionBounds = new Rect();
    @Nullable
    private OnCloseListener mOnCloseListener;
    private final int mTouchSlop;
    @Nullable
    private UnsetPressedState mUnsetPressedState;

    public enum ClosePosition {
        TOP_LEFT(51),
        TOP_CENTER(49),
        TOP_RIGHT(53),
        CENTER(17),
        BOTTOM_LEFT(83),
        BOTTOM_CENTER(81),
        BOTTOM_RIGHT(85);
        
        private final int mGravity;

        private ClosePosition(int mGravity) {
            this.mGravity = mGravity;
        }

        /* access modifiers changed from: final */
        public final int getGravity() {
            return this.mGravity;
        }
    }

    public interface OnCloseListener {
        void onClose();
    }

    private final class UnsetPressedState implements Runnable {
        private UnsetPressedState() {
        }

        public final void run() {
            CloseableLayout.this.setClosePressed(false);
        }
    }

    public CloseableLayout(@NonNull Context context) {
        super(context);
        this.mCloseDrawable.addState(SELECTED_STATE_SET, Drawables.INTERSTITIAL_CLOSE_BUTTON_PRESSED.createDrawable(context));
        this.mCloseDrawable.addState(EMPTY_STATE_SET, Drawables.INTERSTITIAL_CLOSE_BUTTON_NORMAL.createDrawable(context));
        this.mCloseDrawable.setState(EMPTY_STATE_SET);
        this.mCloseDrawable.setCallback(this);
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.mCloseRegionSize = Dips.asIntPixels(CLOSE_REGION_SIZE_DP, context);
        this.mCloseButtonSize = Dips.asIntPixels(CLOSE_BUTTON_SIZE_DP, context);
        this.mCloseButtonPadding = Dips.asIntPixels(8.0f, context);
        setWillNotDraw(false);
    }

    public void setOnCloseListener(@Nullable OnCloseListener onCloseListener) {
        this.mOnCloseListener = onCloseListener;
    }

    public void setClosePosition(@NonNull ClosePosition closePosition) {
        Preconditions.checkNotNull(closePosition);
        this.mClosePosition = closePosition;
        this.mCloseBoundChanged = true;
        invalidate();
    }

    public void setCloseVisible(boolean visible) {
        if (this.mCloseDrawable.setVisible(visible, false)) {
            invalidate(this.mCloseRegionBounds);
        }
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        this.mCloseBoundChanged = true;
    }

    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);
        if (this.mCloseBoundChanged) {
            this.mCloseBoundChanged = false;
            this.mClosableLayoutRect.set(0, 0, getWidth(), getHeight());
            applyCloseRegionBounds(this.mClosePosition, this.mClosableLayoutRect, this.mCloseRegionBounds);
            this.mInsetCloseRegionBounds.set(this.mCloseRegionBounds);
            this.mInsetCloseRegionBounds.inset(this.mCloseButtonPadding, this.mCloseButtonPadding);
            applyCloseButtonBounds(this.mClosePosition, this.mInsetCloseRegionBounds, this.mCloseButtonBounds);
            this.mCloseDrawable.setBounds(this.mCloseButtonBounds);
        }
        if (this.mCloseDrawable.isVisible()) {
            this.mCloseDrawable.draw(canvas);
        }
    }

    public void applyCloseRegionBounds(ClosePosition closePosition, Rect bounds, Rect closeBounds) {
        applyCloseBoundsWithSize(closePosition, this.mCloseRegionSize, bounds, closeBounds);
    }

    private void applyCloseButtonBounds(ClosePosition closePosition, Rect bounds, Rect outBounds) {
        applyCloseBoundsWithSize(closePosition, this.mCloseButtonSize, bounds, outBounds);
    }

    private void applyCloseBoundsWithSize(ClosePosition closePosition, int size, Rect bounds, Rect outBounds) {
        Gravity.apply(closePosition.getGravity(), size, size, bounds, outBounds);
    }

    public boolean onInterceptTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() != 0) {
            return false;
        }
        return pointInCloseBounds((int) event.getX(), (int) event.getY(), 0);
    }

    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (pointInCloseBounds((int) event.getX(), (int) event.getY(), this.mTouchSlop)) {
            switch (event.getAction()) {
                case 0:
                    setClosePressed(true);
                    break;
                case 1:
                    if (isClosePressed()) {
                        if (this.mUnsetPressedState == null) {
                            this.mUnsetPressedState = new UnsetPressedState();
                        }
                        postDelayed(this.mUnsetPressedState, (long) ViewConfiguration.getPressedStateDuration());
                        performClose();
                        break;
                    }
                    break;
                case 3:
                    setClosePressed(false);
                    break;
            }
            return true;
        }
        setClosePressed(false);
        super.onTouchEvent(event);
        return false;
    }

    /* access modifiers changed from: private */
    public void setClosePressed(boolean pressed) {
        if (pressed != isClosePressed()) {
            this.mCloseDrawable.setState(pressed ? SELECTED_STATE_SET : EMPTY_STATE_SET);
            invalidate(this.mCloseRegionBounds);
        }
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public boolean isClosePressed() {
        return this.mCloseDrawable.getState() == SELECTED_STATE_SET;
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public boolean pointInCloseBounds(int x, int y, int slop) {
        return x >= this.mCloseRegionBounds.left - slop && y >= this.mCloseRegionBounds.top - slop && x < this.mCloseRegionBounds.right + slop && y < this.mCloseRegionBounds.bottom + slop;
    }

    private void performClose() {
        playSoundEffect(0);
        if (this.mOnCloseListener != null) {
            this.mOnCloseListener.onClose();
        }
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void setCloseBounds(Rect closeBounds) {
        this.mCloseRegionBounds.set(closeBounds);
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public Rect getCloseBounds() {
        return this.mCloseRegionBounds;
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void setCloseBoundChanged(boolean changed) {
        this.mCloseBoundChanged = changed;
    }

    @VisibleForTesting
    public boolean isCloseVisible() {
        return this.mCloseDrawable.isVisible();
    }
}
