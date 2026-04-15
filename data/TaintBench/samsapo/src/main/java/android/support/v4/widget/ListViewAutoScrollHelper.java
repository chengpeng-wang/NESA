package android.support.v4.widget;

import android.view.View;
import android.widget.ListView;

public class ListViewAutoScrollHelper extends AutoScrollHelper {
    private final ListView mTarget;

    public ListViewAutoScrollHelper(ListView listView) {
        View view = listView;
        super(view);
        this.mTarget = view;
    }

    public void scrollTargetBy(int i, int i2) {
        int i3 = i;
        int i4 = i2;
        ListView listView = this.mTarget;
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        if (firstVisiblePosition != -1) {
            View childAt = listView.getChildAt(0);
            if (childAt != null) {
                listView.setSelectionFromTop(firstVisiblePosition, childAt.getTop() - i4);
            }
        }
    }

    public boolean canTargetScrollHorizontally(int i) {
        int i2 = i;
        return false;
    }

    public boolean canTargetScrollVertically(int i) {
        int i2 = i;
        ListView listView = this.mTarget;
        int count = listView.getCount();
        int childCount = listView.getChildCount();
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        int i3 = firstVisiblePosition + childCount;
        if (i2 > 0) {
            if (i3 >= count && listView.getChildAt(childCount - 1).getBottom() <= listView.getHeight()) {
                return false;
            }
        } else if (i2 >= 0) {
            return false;
        } else {
            if (firstVisiblePosition <= 0 && listView.getChildAt(0).getTop() >= 0) {
                return false;
            }
        }
        return true;
    }
}
