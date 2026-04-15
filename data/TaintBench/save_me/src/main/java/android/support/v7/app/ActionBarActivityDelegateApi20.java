package android.support.v7.app;

import android.view.Window.Callback;

class ActionBarActivityDelegateApi20 extends ActionBarActivityDelegateJBMR2 {

    class WindowCallbackWrapperApi20 extends WindowCallbackWrapper {
        WindowCallbackWrapperApi20(Callback wrapped) {
            super(wrapped);
        }
    }

    ActionBarActivityDelegateApi20(ActionBarActivity activity) {
        super(activity);
    }

    /* access modifiers changed from: 0000 */
    public Callback createWindowCallbackWrapper(Callback cb) {
        return new WindowCallbackWrapperApi20(cb);
    }
}
