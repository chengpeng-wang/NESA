package android.support.v4.widget;

import android.os.Build.VERSION;
import android.view.View.OnTouchListener;

public class PopupMenuCompat {
    static final PopupMenuImpl IMPL;

    static class BasePopupMenuImpl implements PopupMenuImpl {
        BasePopupMenuImpl() {
        }

        public OnTouchListener getDragToOpenListener(Object obj) {
            Object obj2 = obj;
            return null;
        }
    }

    static class KitKatPopupMenuImpl extends BasePopupMenuImpl {
        KitKatPopupMenuImpl() {
        }

        public OnTouchListener getDragToOpenListener(Object obj) {
            return PopupMenuCompatKitKat.getDragToOpenListener(obj);
        }
    }

    interface PopupMenuImpl {
        OnTouchListener getDragToOpenListener(Object obj);
    }

    static {
        if (VERSION.SDK_INT >= 19) {
            KitKatPopupMenuImpl kitKatPopupMenuImpl = r3;
            KitKatPopupMenuImpl kitKatPopupMenuImpl2 = new KitKatPopupMenuImpl();
            IMPL = kitKatPopupMenuImpl;
            return;
        }
        BasePopupMenuImpl basePopupMenuImpl = r3;
        BasePopupMenuImpl basePopupMenuImpl2 = new BasePopupMenuImpl();
        IMPL = basePopupMenuImpl;
    }

    private PopupMenuCompat() {
    }

    public static OnTouchListener getDragToOpenListener(Object obj) {
        return IMPL.getDragToOpenListener(obj);
    }
}
