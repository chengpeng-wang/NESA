package android.support.v4.view;

import android.os.Build.VERSION;
import android.support.v4.internal.view.SupportMenuItem;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

public class MenuItemCompat {
    static final MenuVersionImpl IMPL;
    public static final int SHOW_AS_ACTION_ALWAYS = 2;
    public static final int SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW = 8;
    public static final int SHOW_AS_ACTION_IF_ROOM = 1;
    public static final int SHOW_AS_ACTION_NEVER = 0;
    public static final int SHOW_AS_ACTION_WITH_TEXT = 4;
    private static final String TAG = "MenuItemCompat";

    static class BaseMenuVersionImpl implements MenuVersionImpl {
        BaseMenuVersionImpl() {
        }

        public void setShowAsAction(MenuItem menuItem, int i) {
        }

        public MenuItem setActionView(MenuItem menuItem, View view) {
            View view2 = view;
            return menuItem;
        }

        public MenuItem setActionView(MenuItem menuItem, int i) {
            int i2 = i;
            return menuItem;
        }

        public View getActionView(MenuItem menuItem) {
            MenuItem menuItem2 = menuItem;
            return null;
        }

        public boolean expandActionView(MenuItem menuItem) {
            MenuItem menuItem2 = menuItem;
            return false;
        }

        public boolean collapseActionView(MenuItem menuItem) {
            MenuItem menuItem2 = menuItem;
            return false;
        }

        public boolean isActionViewExpanded(MenuItem menuItem) {
            MenuItem menuItem2 = menuItem;
            return false;
        }

        public MenuItem setOnActionExpandListener(MenuItem menuItem, OnActionExpandListener onActionExpandListener) {
            OnActionExpandListener onActionExpandListener2 = onActionExpandListener;
            return menuItem;
        }
    }

    static class HoneycombMenuVersionImpl implements MenuVersionImpl {
        HoneycombMenuVersionImpl() {
        }

        public void setShowAsAction(MenuItem menuItem, int i) {
            MenuItemCompatHoneycomb.setShowAsAction(menuItem, i);
        }

        public MenuItem setActionView(MenuItem menuItem, View view) {
            return MenuItemCompatHoneycomb.setActionView(menuItem, view);
        }

        public MenuItem setActionView(MenuItem menuItem, int i) {
            return MenuItemCompatHoneycomb.setActionView(menuItem, i);
        }

        public View getActionView(MenuItem menuItem) {
            return MenuItemCompatHoneycomb.getActionView(menuItem);
        }

        public boolean expandActionView(MenuItem menuItem) {
            MenuItem menuItem2 = menuItem;
            return false;
        }

        public boolean collapseActionView(MenuItem menuItem) {
            MenuItem menuItem2 = menuItem;
            return false;
        }

        public boolean isActionViewExpanded(MenuItem menuItem) {
            MenuItem menuItem2 = menuItem;
            return false;
        }

        public MenuItem setOnActionExpandListener(MenuItem menuItem, OnActionExpandListener onActionExpandListener) {
            OnActionExpandListener onActionExpandListener2 = onActionExpandListener;
            return menuItem;
        }
    }

    static class IcsMenuVersionImpl extends HoneycombMenuVersionImpl {
        IcsMenuVersionImpl() {
        }

        public boolean expandActionView(MenuItem menuItem) {
            return MenuItemCompatIcs.expandActionView(menuItem);
        }

        public boolean collapseActionView(MenuItem menuItem) {
            return MenuItemCompatIcs.collapseActionView(menuItem);
        }

        public boolean isActionViewExpanded(MenuItem menuItem) {
            return MenuItemCompatIcs.isActionViewExpanded(menuItem);
        }

        public MenuItem setOnActionExpandListener(MenuItem menuItem, OnActionExpandListener onActionExpandListener) {
            MenuItem menuItem2 = menuItem;
            OnActionExpandListener onActionExpandListener2 = onActionExpandListener;
            if (onActionExpandListener2 == null) {
                return MenuItemCompatIcs.setOnActionExpandListener(menuItem2, null);
            }
            MenuItem menuItem3 = menuItem2;
            AnonymousClass1 anonymousClass1 = r8;
            final OnActionExpandListener onActionExpandListener3 = onActionExpandListener2;
            AnonymousClass1 anonymousClass12 = new SupportActionExpandProxy(this) {
                final /* synthetic */ IcsMenuVersionImpl this$0;

                public boolean onMenuItemActionExpand(MenuItem menuItem) {
                    return onActionExpandListener3.onMenuItemActionExpand(menuItem);
                }

                public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                    return onActionExpandListener3.onMenuItemActionCollapse(menuItem);
                }
            };
            return MenuItemCompatIcs.setOnActionExpandListener(menuItem3, anonymousClass1);
        }
    }

    interface MenuVersionImpl {
        boolean collapseActionView(MenuItem menuItem);

        boolean expandActionView(MenuItem menuItem);

        View getActionView(MenuItem menuItem);

        boolean isActionViewExpanded(MenuItem menuItem);

        MenuItem setActionView(MenuItem menuItem, int i);

        MenuItem setActionView(MenuItem menuItem, View view);

        MenuItem setOnActionExpandListener(MenuItem menuItem, OnActionExpandListener onActionExpandListener);

        void setShowAsAction(MenuItem menuItem, int i);
    }

    public interface OnActionExpandListener {
        boolean onMenuItemActionCollapse(MenuItem menuItem);

        boolean onMenuItemActionExpand(MenuItem menuItem);
    }

    public MenuItemCompat() {
    }

    static {
        int i = VERSION.SDK_INT;
        if (i >= 14) {
            IcsMenuVersionImpl icsMenuVersionImpl = r3;
            IcsMenuVersionImpl icsMenuVersionImpl2 = new IcsMenuVersionImpl();
            IMPL = icsMenuVersionImpl;
        } else if (i >= 11) {
            HoneycombMenuVersionImpl honeycombMenuVersionImpl = r3;
            HoneycombMenuVersionImpl honeycombMenuVersionImpl2 = new HoneycombMenuVersionImpl();
            IMPL = honeycombMenuVersionImpl;
        } else {
            BaseMenuVersionImpl baseMenuVersionImpl = r3;
            BaseMenuVersionImpl baseMenuVersionImpl2 = new BaseMenuVersionImpl();
            IMPL = baseMenuVersionImpl;
        }
    }

    public static void setShowAsAction(MenuItem menuItem, int i) {
        MenuItem menuItem2 = menuItem;
        int i2 = i;
        if (menuItem2 instanceof SupportMenuItem) {
            ((SupportMenuItem) menuItem2).setShowAsAction(i2);
        } else {
            IMPL.setShowAsAction(menuItem2, i2);
        }
    }

    public static MenuItem setActionView(MenuItem menuItem, View view) {
        MenuItem menuItem2 = menuItem;
        View view2 = view;
        if (menuItem2 instanceof SupportMenuItem) {
            return ((SupportMenuItem) menuItem2).setActionView(view2);
        }
        return IMPL.setActionView(menuItem2, view2);
    }

    public static MenuItem setActionView(MenuItem menuItem, int i) {
        MenuItem menuItem2 = menuItem;
        int i2 = i;
        if (menuItem2 instanceof SupportMenuItem) {
            return ((SupportMenuItem) menuItem2).setActionView(i2);
        }
        return IMPL.setActionView(menuItem2, i2);
    }

    public static View getActionView(MenuItem menuItem) {
        MenuItem menuItem2 = menuItem;
        if (menuItem2 instanceof SupportMenuItem) {
            return ((SupportMenuItem) menuItem2).getActionView();
        }
        return IMPL.getActionView(menuItem2);
    }

    public static MenuItem setActionProvider(MenuItem menuItem, ActionProvider actionProvider) {
        MenuItem menuItem2 = menuItem;
        ActionProvider actionProvider2 = actionProvider;
        if (menuItem2 instanceof SupportMenuItem) {
            return ((SupportMenuItem) menuItem2).setSupportActionProvider(actionProvider2);
        }
        int w = Log.w(TAG, "setActionProvider: item does not implement SupportMenuItem; ignoring");
        return menuItem2;
    }

    public static ActionProvider getActionProvider(MenuItem menuItem) {
        MenuItem menuItem2 = menuItem;
        if (menuItem2 instanceof SupportMenuItem) {
            return ((SupportMenuItem) menuItem2).getSupportActionProvider();
        }
        int w = Log.w(TAG, "getActionProvider: item does not implement SupportMenuItem; returning null");
        return null;
    }

    public static boolean expandActionView(MenuItem menuItem) {
        MenuItem menuItem2 = menuItem;
        if (menuItem2 instanceof SupportMenuItem) {
            return ((SupportMenuItem) menuItem2).expandActionView();
        }
        return IMPL.expandActionView(menuItem2);
    }

    public static boolean collapseActionView(MenuItem menuItem) {
        MenuItem menuItem2 = menuItem;
        if (menuItem2 instanceof SupportMenuItem) {
            return ((SupportMenuItem) menuItem2).collapseActionView();
        }
        return IMPL.collapseActionView(menuItem2);
    }

    public static boolean isActionViewExpanded(MenuItem menuItem) {
        MenuItem menuItem2 = menuItem;
        if (menuItem2 instanceof SupportMenuItem) {
            return ((SupportMenuItem) menuItem2).isActionViewExpanded();
        }
        return IMPL.isActionViewExpanded(menuItem2);
    }

    public static MenuItem setOnActionExpandListener(MenuItem menuItem, OnActionExpandListener onActionExpandListener) {
        MenuItem menuItem2 = menuItem;
        OnActionExpandListener onActionExpandListener2 = onActionExpandListener;
        if (menuItem2 instanceof SupportMenuItem) {
            return ((SupportMenuItem) menuItem2).setSupportOnActionExpandListener(onActionExpandListener2);
        }
        return IMPL.setOnActionExpandListener(menuItem2, onActionExpandListener2);
    }
}
