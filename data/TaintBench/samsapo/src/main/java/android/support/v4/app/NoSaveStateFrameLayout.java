package android.support.v4.app;

import android.content.Context;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

class NoSaveStateFrameLayout extends FrameLayout {
    static ViewGroup wrap(View view) {
        View view2 = view;
        ViewGroup viewGroup = r8;
        ViewGroup noSaveStateFrameLayout = new NoSaveStateFrameLayout(view2.getContext());
        ViewGroup viewGroup2 = viewGroup;
        LayoutParams layoutParams = view2.getLayoutParams();
        if (layoutParams != null) {
            viewGroup2.setLayoutParams(layoutParams);
        }
        LayoutParams layoutParams2 = r8;
        LayoutParams layoutParams3 = new FrameLayout.LayoutParams(-1, -1);
        view2.setLayoutParams(layoutParams2);
        viewGroup2.addView(view2);
        return viewGroup2;
    }

    public NoSaveStateFrameLayout(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public void dispatchSaveInstanceState(SparseArray<Parcelable> sparseArray) {
        dispatchFreezeSelfOnly(sparseArray);
    }

    /* access modifiers changed from: protected */
    public void dispatchRestoreInstanceState(SparseArray<Parcelable> sparseArray) {
        dispatchThawSelfOnly(sparseArray);
    }
}
