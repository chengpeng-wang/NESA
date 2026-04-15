package android.support.v4.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class DialogFragment extends Fragment implements OnCancelListener, OnDismissListener {
    private static final String SAVED_BACK_STACK_ID = "android:backStackId";
    private static final String SAVED_CANCELABLE = "android:cancelable";
    private static final String SAVED_DIALOG_STATE_TAG = "android:savedDialogState";
    private static final String SAVED_SHOWS_DIALOG = "android:showsDialog";
    private static final String SAVED_STYLE = "android:style";
    private static final String SAVED_THEME = "android:theme";
    public static final int STYLE_NORMAL = 0;
    public static final int STYLE_NO_FRAME = 2;
    public static final int STYLE_NO_INPUT = 3;
    public static final int STYLE_NO_TITLE = 1;
    int mBackStackId = -1;
    boolean mCancelable = true;
    Dialog mDialog;
    boolean mDismissed;
    boolean mShownByMe;
    boolean mShowsDialog = true;
    int mStyle = 0;
    int mTheme = 0;
    boolean mViewDestroyed;

    public DialogFragment() {
    }

    public void setStyle(int i, int i2) {
        int i3 = i2;
        this.mStyle = i;
        if (this.mStyle == 2 || this.mStyle == 3) {
            this.mTheme = 16973913;
        }
        if (i3 != 0) {
            this.mTheme = i3;
        }
    }

    public void show(FragmentManager fragmentManager, String str) {
        FragmentManager fragmentManager2 = fragmentManager;
        String str2 = str;
        this.mDismissed = false;
        this.mShownByMe = true;
        FragmentTransaction beginTransaction = fragmentManager2.beginTransaction();
        FragmentTransaction add = beginTransaction.add(this, str2);
        int commit = beginTransaction.commit();
    }

    public int show(FragmentTransaction fragmentTransaction, String str) {
        FragmentTransaction fragmentTransaction2 = fragmentTransaction;
        String str2 = str;
        this.mDismissed = false;
        this.mShownByMe = true;
        FragmentTransaction add = fragmentTransaction2.add(this, str2);
        this.mViewDestroyed = false;
        this.mBackStackId = fragmentTransaction2.commit();
        return this.mBackStackId;
    }

    public void dismiss() {
        dismissInternal(false);
    }

    public void dismissAllowingStateLoss() {
        dismissInternal(true);
    }

    /* access modifiers changed from: 0000 */
    public void dismissInternal(boolean z) {
        boolean z2 = z;
        if (!this.mDismissed) {
            this.mDismissed = true;
            this.mShownByMe = false;
            if (this.mDialog != null) {
                this.mDialog.dismiss();
                this.mDialog = null;
            }
            this.mViewDestroyed = true;
            if (this.mBackStackId >= 0) {
                getFragmentManager().popBackStack(this.mBackStackId, 1);
                this.mBackStackId = -1;
                return;
            }
            FragmentTransaction beginTransaction = getFragmentManager().beginTransaction();
            FragmentTransaction remove = beginTransaction.remove(this);
            int commitAllowingStateLoss;
            if (z2) {
                commitAllowingStateLoss = beginTransaction.commitAllowingStateLoss();
            } else {
                commitAllowingStateLoss = beginTransaction.commit();
            }
        }
    }

    public Dialog getDialog() {
        return this.mDialog;
    }

    public int getTheme() {
        return this.mTheme;
    }

    public void setCancelable(boolean z) {
        boolean z2 = z;
        this.mCancelable = z2;
        if (this.mDialog != null) {
            this.mDialog.setCancelable(z2);
        }
    }

    public boolean isCancelable() {
        return this.mCancelable;
    }

    public void setShowsDialog(boolean z) {
        this.mShowsDialog = z;
    }

    public boolean getShowsDialog() {
        return this.mShowsDialog;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!this.mShownByMe) {
            this.mDismissed = false;
        }
    }

    public void onDetach() {
        super.onDetach();
        if (!this.mShownByMe && !this.mDismissed) {
            this.mDismissed = true;
        }
    }

    public void onCreate(Bundle bundle) {
        Bundle bundle2 = bundle;
        super.onCreate(bundle2);
        this.mShowsDialog = this.mContainerId == 0;
        if (bundle2 != null) {
            this.mStyle = bundle2.getInt(SAVED_STYLE, 0);
            this.mTheme = bundle2.getInt(SAVED_THEME, 0);
            this.mCancelable = bundle2.getBoolean(SAVED_CANCELABLE, true);
            this.mShowsDialog = bundle2.getBoolean(SAVED_SHOWS_DIALOG, this.mShowsDialog);
            this.mBackStackId = bundle2.getInt(SAVED_BACK_STACK_ID, -1);
        }
    }

    public LayoutInflater getLayoutInflater(Bundle bundle) {
        Bundle bundle2 = bundle;
        if (!this.mShowsDialog) {
            return super.getLayoutInflater(bundle2);
        }
        this.mDialog = onCreateDialog(bundle2);
        switch (this.mStyle) {
            case 1:
            case 2:
                break;
            case 3:
                this.mDialog.getWindow().addFlags(24);
                break;
        }
        boolean requestWindowFeature = this.mDialog.requestWindowFeature(1);
        if (this.mDialog != null) {
            return (LayoutInflater) this.mDialog.getContext().getSystemService("layout_inflater");
        }
        return (LayoutInflater) this.mActivity.getSystemService("layout_inflater");
    }

    public Dialog onCreateDialog(Bundle bundle) {
        Bundle bundle2 = bundle;
        Dialog dialog = r6;
        Dialog dialog2 = new Dialog(getActivity(), getTheme());
        return dialog;
    }

    public void onCancel(DialogInterface dialogInterface) {
    }

    public void onDismiss(DialogInterface dialogInterface) {
        DialogInterface dialogInterface2 = dialogInterface;
        if (!this.mViewDestroyed) {
            dismissInternal(true);
        }
    }

    public void onActivityCreated(Bundle bundle) {
        Bundle bundle2 = bundle;
        super.onActivityCreated(bundle2);
        if (this.mShowsDialog) {
            View view = getView();
            if (view != null) {
                if (view.getParent() != null) {
                    IllegalStateException illegalStateException = r7;
                    IllegalStateException illegalStateException2 = new IllegalStateException("DialogFragment can not be attached to a container view");
                    throw illegalStateException;
                }
                this.mDialog.setContentView(view);
            }
            this.mDialog.setOwnerActivity(getActivity());
            this.mDialog.setCancelable(this.mCancelable);
            this.mDialog.setOnCancelListener(this);
            this.mDialog.setOnDismissListener(this);
            if (bundle2 != null) {
                Bundle bundle3 = bundle2.getBundle(SAVED_DIALOG_STATE_TAG);
                if (bundle3 != null) {
                    this.mDialog.onRestoreInstanceState(bundle3);
                }
            }
        }
    }

    public void onStart() {
        super.onStart();
        if (this.mDialog != null) {
            this.mViewDestroyed = false;
            this.mDialog.show();
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        Bundle bundle2 = bundle;
        super.onSaveInstanceState(bundle2);
        if (this.mDialog != null) {
            Bundle onSaveInstanceState = this.mDialog.onSaveInstanceState();
            if (onSaveInstanceState != null) {
                bundle2.putBundle(SAVED_DIALOG_STATE_TAG, onSaveInstanceState);
            }
        }
        if (this.mStyle != 0) {
            bundle2.putInt(SAVED_STYLE, this.mStyle);
        }
        if (this.mTheme != 0) {
            bundle2.putInt(SAVED_THEME, this.mTheme);
        }
        if (!this.mCancelable) {
            bundle2.putBoolean(SAVED_CANCELABLE, this.mCancelable);
        }
        if (!this.mShowsDialog) {
            bundle2.putBoolean(SAVED_SHOWS_DIALOG, this.mShowsDialog);
        }
        if (this.mBackStackId != -1) {
            bundle2.putInt(SAVED_BACK_STACK_ID, this.mBackStackId);
        }
    }

    public void onStop() {
        super.onStop();
        if (this.mDialog != null) {
            this.mDialog.hide();
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        if (this.mDialog != null) {
            this.mViewDestroyed = true;
            this.mDialog.dismiss();
            this.mDialog = null;
        }
    }
}
