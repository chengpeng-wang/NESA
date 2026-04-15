package exts.whats;

import android.app.Service;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.RelativeLayout;

public class OverlayView extends RelativeLayout {
    protected LayoutParams layoutParams;
    private int layoutResId;

    public OverlayView(Service service, int layoutResId) {
        super(service);
        this.layoutResId = layoutResId;
        setLongClickable(true);
        setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View v) {
                return OverlayView.this.onTouchEvent_LongPress();
            }
        });
        load();
    }

    public int getLayoutGravity() {
        return 17;
    }

    private void setupLayoutParams() {
        this.layoutParams = new LayoutParams(-1, -1, 2010, 262440, -3);
        this.layoutParams.gravity = getLayoutGravity();
        onSetupLayoutParams();
    }

    /* access modifiers changed from: protected */
    public void onSetupLayoutParams() {
    }

    private void inflateView() {
        ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(this.layoutResId, this);
        onInflateView();
    }

    /* access modifiers changed from: protected */
    public void onInflateView() {
    }

    public boolean isVisible() {
        return true;
    }

    public void refreshLayout() {
        if (isVisible()) {
            removeAllViews();
            inflateView();
            onSetupLayoutParams();
            ((WindowManager) getContext().getSystemService("window")).updateViewLayout(this, this.layoutParams);
            refresh();
        }
    }

    /* access modifiers changed from: protected */
    public void addView() {
        setupLayoutParams();
        ((WindowManager) getContext().getSystemService("window")).addView(this, this.layoutParams);
        super.setVisibility(8);
    }

    /* access modifiers changed from: protected */
    public void load() {
        inflateView();
        addView();
        refresh();
    }

    /* access modifiers changed from: protected */
    public void unload() {
        ((WindowManager) getContext().getSystemService("window")).removeView(this);
        removeAllViews();
    }

    /* access modifiers changed from: protected */
    public void reload() {
        unload();
        load();
    }

    public void destroy() {
        ((WindowManager) getContext().getSystemService("window")).removeView(this);
    }

    public void refresh() {
        if (isVisible()) {
            setVisibility(0);
            refreshViews();
            return;
        }
        setVisibility(8);
    }

    /* access modifiers changed from: protected */
    public void refreshViews() {
    }

    /* access modifiers changed from: protected */
    public boolean showNotificationHidden() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean onVisibilityToChange(int visibility) {
        return true;
    }

    /* access modifiers changed from: protected */
    public View animationView() {
        return this;
    }

    /* access modifiers changed from: protected */
    public void hide() {
        super.setVisibility(8);
    }

    /* access modifiers changed from: protected */
    public void show() {
        super.setVisibility(0);
    }

    /* access modifiers changed from: protected */
    public int getLeftOnScreen() {
        int[] location = new int[2];
        getLocationOnScreen(location);
        return location[0];
    }

    /* access modifiers changed from: protected */
    public int getTopOnScreen() {
        int[] location = new int[2];
        getLocationOnScreen(location);
        return location[1];
    }

    /* access modifiers changed from: protected */
    public boolean isInside(View view, int x, int y) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        if (x < location[0] || x > location[0] + view.getWidth() || y < location[1] || y > location[1] + view.getHeight()) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onTouchEvent_Up(MotionEvent event) {
    }

    /* access modifiers changed from: protected */
    public void onTouchEvent_Move(MotionEvent event) {
    }

    /* access modifiers changed from: protected */
    public void onTouchEvent_Press(MotionEvent event) {
    }

    public boolean onTouchEvent_LongPress() {
        return false;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == 0) {
            onTouchEvent_Press(event);
        } else if (event.getActionMasked() == 1) {
            onTouchEvent_Up(event);
        } else if (event.getActionMasked() == 2) {
            onTouchEvent_Move(event);
        }
        return super.onTouchEvent(event);
    }
}
