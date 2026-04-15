package android.support.v4.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class ContentLoadingProgressBar extends ProgressBar {
    private static final int MIN_DELAY = 500;
    private static final int MIN_SHOW_TIME = 500;
    private final Runnable mDelayedHide;
    private final Runnable mDelayedShow;
    /* access modifiers changed from: private */
    public boolean mDismissed;
    private boolean mPostedHide;
    private boolean mPostedShow;
    private long mStartTime;

    static /* synthetic */ boolean access$002(ContentLoadingProgressBar contentLoadingProgressBar, boolean z) {
        boolean z2 = z;
        boolean z3 = z2;
        boolean z4 = z2;
        contentLoadingProgressBar.mPostedHide = z4;
        return z3;
    }

    static /* synthetic */ long access$102(ContentLoadingProgressBar contentLoadingProgressBar, long j) {
        long j2 = j;
        long j3 = j2;
        long j4 = j2;
        contentLoadingProgressBar.mStartTime = j4;
        return j3;
    }

    static /* synthetic */ boolean access$202(ContentLoadingProgressBar contentLoadingProgressBar, boolean z) {
        boolean z2 = z;
        boolean z3 = z2;
        boolean z4 = z2;
        contentLoadingProgressBar.mPostedShow = z4;
        return z3;
    }

    public ContentLoadingProgressBar(Context context) {
        this(context, null);
    }

    public ContentLoadingProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0);
        this.mStartTime = -1;
        this.mPostedHide = false;
        this.mPostedShow = false;
        this.mDismissed = false;
        AnonymousClass1 anonymousClass1 = r7;
        AnonymousClass1 anonymousClass12 = new Runnable(this) {
            final /* synthetic */ ContentLoadingProgressBar this$0;

            {
                this.this$0 = r5;
            }

            public void run() {
                boolean access$002 = ContentLoadingProgressBar.access$002(this.this$0, false);
                long access$102 = ContentLoadingProgressBar.access$102(this.this$0, -1);
                this.this$0.setVisibility(8);
            }
        };
        this.mDelayedHide = anonymousClass1;
        AnonymousClass2 anonymousClass2 = r7;
        AnonymousClass2 anonymousClass22 = new Runnable(this) {
            final /* synthetic */ ContentLoadingProgressBar this$0;

            {
                this.this$0 = r5;
            }

            public void run() {
                boolean access$202 = ContentLoadingProgressBar.access$202(this.this$0, false);
                if (!this.this$0.mDismissed) {
                    long access$102 = ContentLoadingProgressBar.access$102(this.this$0, System.currentTimeMillis());
                    this.this$0.setVisibility(0);
                }
            }
        };
        this.mDelayedShow = anonymousClass2;
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        removeCallbacks();
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks();
    }

    private void removeCallbacks() {
        boolean removeCallbacks = removeCallbacks(this.mDelayedHide);
        removeCallbacks = removeCallbacks(this.mDelayedShow);
    }

    public void hide() {
        this.mDismissed = true;
        boolean removeCallbacks = removeCallbacks(this.mDelayedShow);
        long currentTimeMillis = System.currentTimeMillis() - this.mStartTime;
        if (currentTimeMillis >= 500 || this.mStartTime == -1) {
            setVisibility(8);
        } else if (!this.mPostedHide) {
            removeCallbacks = postDelayed(this.mDelayedHide, 500 - currentTimeMillis);
            this.mPostedHide = true;
        }
    }

    public void show() {
        this.mStartTime = -1;
        this.mDismissed = false;
        boolean removeCallbacks = removeCallbacks(this.mDelayedHide);
        if (!this.mPostedShow) {
            removeCallbacks = postDelayed(this.mDelayedShow, 500);
            this.mPostedShow = true;
        }
    }
}
