package android.support.v4.widget;

import android.os.Build.VERSION;
import android.view.View;
import android.view.View.OnTouchListener;

public class ListPopupWindowCompat {
    static final ListPopupWindowImpl IMPL;

    static class BaseListPopupWindowImpl implements ListPopupWindowImpl {
        BaseListPopupWindowImpl() {
        }

        public OnTouchListener createDragToOpenListener(Object obj, View view) {
            Object obj2 = obj;
            View view2 = view;
            return null;
        }
    }

    static class KitKatListPopupWindowImpl extends BaseListPopupWindowImpl {
        KitKatListPopupWindowImpl() {
        }

        public OnTouchListener createDragToOpenListener(Object obj, View view) {
            return ListPopupWindowCompatKitKat.createDragToOpenListener(obj, view);
        }
    }

    interface ListPopupWindowImpl {
        OnTouchListener createDragToOpenListener(Object obj, View view);
    }

    static {
        if (VERSION.SDK_INT >= 19) {
            KitKatListPopupWindowImpl kitKatListPopupWindowImpl = r3;
            KitKatListPopupWindowImpl kitKatListPopupWindowImpl2 = new KitKatListPopupWindowImpl();
            IMPL = kitKatListPopupWindowImpl;
            return;
        }
        BaseListPopupWindowImpl baseListPopupWindowImpl = r3;
        BaseListPopupWindowImpl baseListPopupWindowImpl2 = new BaseListPopupWindowImpl();
        IMPL = baseListPopupWindowImpl;
    }

    private ListPopupWindowCompat() {
    }

    public static OnTouchListener createDragToOpenListener(Object obj, View view) {
        return IMPL.createDragToOpenListener(obj, view);
    }
}
