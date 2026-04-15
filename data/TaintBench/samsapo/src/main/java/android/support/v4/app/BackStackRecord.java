package android.support.v4.app;

import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.util.LogWriter;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;

final class BackStackRecord extends FragmentTransaction implements BackStackEntry, Runnable {
    static final int OP_ADD = 1;
    static final int OP_ATTACH = 7;
    static final int OP_DETACH = 6;
    static final int OP_HIDE = 4;
    static final int OP_NULL = 0;
    static final int OP_REMOVE = 3;
    static final int OP_REPLACE = 2;
    static final int OP_SHOW = 5;
    static final String TAG = "FragmentManager";
    boolean mAddToBackStack;
    boolean mAllowAddToBackStack = true;
    int mBreadCrumbShortTitleRes;
    CharSequence mBreadCrumbShortTitleText;
    int mBreadCrumbTitleRes;
    CharSequence mBreadCrumbTitleText;
    boolean mCommitted;
    int mEnterAnim;
    int mExitAnim;
    Op mHead;
    int mIndex = -1;
    final FragmentManagerImpl mManager;
    String mName;
    int mNumOp;
    int mPopEnterAnim;
    int mPopExitAnim;
    Op mTail;
    int mTransition;
    int mTransitionStyle;

    static final class Op {
        int cmd;
        int enterAnim;
        int exitAnim;
        Fragment fragment;
        Op next;
        int popEnterAnim;
        int popExitAnim;
        Op prev;
        ArrayList<Fragment> removed;

        Op() {
        }
    }

    public String toString() {
        StringBuilder stringBuilder = r5;
        StringBuilder stringBuilder2 = new StringBuilder(128);
        StringBuilder stringBuilder3 = stringBuilder;
        stringBuilder = stringBuilder3.append("BackStackEntry{");
        stringBuilder = stringBuilder3.append(Integer.toHexString(System.identityHashCode(this)));
        if (this.mIndex >= 0) {
            stringBuilder = stringBuilder3.append(" #");
            stringBuilder = stringBuilder3.append(this.mIndex);
        }
        if (this.mName != null) {
            stringBuilder = stringBuilder3.append(" ");
            stringBuilder = stringBuilder3.append(this.mName);
        }
        stringBuilder = stringBuilder3.append("}");
        return stringBuilder3.toString();
    }

    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        FileDescriptor fileDescriptor2 = fileDescriptor;
        String[] strArr2 = strArr;
        dump(str, printWriter, true);
    }

    public void dump(String str, PrintWriter printWriter, boolean z) {
        String str2 = str;
        PrintWriter printWriter2 = printWriter;
        boolean z2 = z;
        if (z2) {
            printWriter2.print(str2);
            printWriter2.print("mName=");
            printWriter2.print(this.mName);
            printWriter2.print(" mIndex=");
            printWriter2.print(this.mIndex);
            printWriter2.print(" mCommitted=");
            printWriter2.println(this.mCommitted);
            if (this.mTransition != 0) {
                printWriter2.print(str2);
                printWriter2.print("mTransition=#");
                printWriter2.print(Integer.toHexString(this.mTransition));
                printWriter2.print(" mTransitionStyle=#");
                printWriter2.println(Integer.toHexString(this.mTransitionStyle));
            }
            if (!(this.mEnterAnim == 0 && this.mExitAnim == 0)) {
                printWriter2.print(str2);
                printWriter2.print("mEnterAnim=#");
                printWriter2.print(Integer.toHexString(this.mEnterAnim));
                printWriter2.print(" mExitAnim=#");
                printWriter2.println(Integer.toHexString(this.mExitAnim));
            }
            if (!(this.mPopEnterAnim == 0 && this.mPopExitAnim == 0)) {
                printWriter2.print(str2);
                printWriter2.print("mPopEnterAnim=#");
                printWriter2.print(Integer.toHexString(this.mPopEnterAnim));
                printWriter2.print(" mPopExitAnim=#");
                printWriter2.println(Integer.toHexString(this.mPopExitAnim));
            }
            if (!(this.mBreadCrumbTitleRes == 0 && this.mBreadCrumbTitleText == null)) {
                printWriter2.print(str2);
                printWriter2.print("mBreadCrumbTitleRes=#");
                printWriter2.print(Integer.toHexString(this.mBreadCrumbTitleRes));
                printWriter2.print(" mBreadCrumbTitleText=");
                printWriter2.println(this.mBreadCrumbTitleText);
            }
            if (!(this.mBreadCrumbShortTitleRes == 0 && this.mBreadCrumbShortTitleText == null)) {
                printWriter2.print(str2);
                printWriter2.print("mBreadCrumbShortTitleRes=#");
                printWriter2.print(Integer.toHexString(this.mBreadCrumbShortTitleRes));
                printWriter2.print(" mBreadCrumbShortTitleText=");
                printWriter2.println(this.mBreadCrumbShortTitleText);
            }
        }
        if (this.mHead != null) {
            printWriter2.print(str2);
            printWriter2.println("Operations:");
            StringBuilder stringBuilder = r12;
            StringBuilder stringBuilder2 = new StringBuilder();
            String stringBuilder3 = stringBuilder.append(str2).append("    ").toString();
            Op op = this.mHead;
            int i = 0;
            while (op != null) {
                String str3;
                switch (op.cmd) {
                    case 0:
                        str3 = "NULL";
                        break;
                    case 1:
                        str3 = "ADD";
                        break;
                    case 2:
                        str3 = "REPLACE";
                        break;
                    case 3:
                        str3 = "REMOVE";
                        break;
                    case 4:
                        str3 = "HIDE";
                        break;
                    case 5:
                        str3 = "SHOW";
                        break;
                    case 6:
                        str3 = "DETACH";
                        break;
                    case 7:
                        str3 = "ATTACH";
                        break;
                    default:
                        stringBuilder = r12;
                        stringBuilder2 = new StringBuilder();
                        str3 = stringBuilder.append("cmd=").append(op.cmd).toString();
                        break;
                }
                printWriter2.print(str2);
                printWriter2.print("  Op #");
                printWriter2.print(i);
                printWriter2.print(": ");
                printWriter2.print(str3);
                printWriter2.print(" ");
                printWriter2.println(op.fragment);
                if (z2) {
                    if (!(op.enterAnim == 0 && op.exitAnim == 0)) {
                        printWriter2.print(str2);
                        printWriter2.print("enterAnim=#");
                        printWriter2.print(Integer.toHexString(op.enterAnim));
                        printWriter2.print(" exitAnim=#");
                        printWriter2.println(Integer.toHexString(op.exitAnim));
                    }
                    if (!(op.popEnterAnim == 0 && op.popExitAnim == 0)) {
                        printWriter2.print(str2);
                        printWriter2.print("popEnterAnim=#");
                        printWriter2.print(Integer.toHexString(op.popEnterAnim));
                        printWriter2.print(" popExitAnim=#");
                        printWriter2.println(Integer.toHexString(op.popExitAnim));
                    }
                }
                if (op.removed != null && op.removed.size() > 0) {
                    for (int i2 = 0; i2 < op.removed.size(); i2++) {
                        printWriter2.print(stringBuilder3);
                        if (op.removed.size() == 1) {
                            printWriter2.print("Removed: ");
                        } else {
                            if (i2 == 0) {
                                printWriter2.println("Removed:");
                            }
                            printWriter2.print(stringBuilder3);
                            printWriter2.print("  #");
                            printWriter2.print(i2);
                            printWriter2.print(": ");
                        }
                        printWriter2.println(op.removed.get(i2));
                    }
                }
                op = op.next;
                i++;
            }
        }
    }

    public BackStackRecord(FragmentManagerImpl fragmentManagerImpl) {
        FragmentManagerImpl fragmentManagerImpl2 = fragmentManagerImpl;
        this.mManager = fragmentManagerImpl2;
    }

    public int getId() {
        return this.mIndex;
    }

    public int getBreadCrumbTitleRes() {
        return this.mBreadCrumbTitleRes;
    }

    public int getBreadCrumbShortTitleRes() {
        return this.mBreadCrumbShortTitleRes;
    }

    public CharSequence getBreadCrumbTitle() {
        if (this.mBreadCrumbTitleRes != 0) {
            return this.mManager.mActivity.getText(this.mBreadCrumbTitleRes);
        }
        return this.mBreadCrumbTitleText;
    }

    public CharSequence getBreadCrumbShortTitle() {
        if (this.mBreadCrumbShortTitleRes != 0) {
            return this.mManager.mActivity.getText(this.mBreadCrumbShortTitleRes);
        }
        return this.mBreadCrumbShortTitleText;
    }

    /* access modifiers changed from: 0000 */
    public void addOp(Op op) {
        Op op2 = op;
        if (this.mHead == null) {
            Op op3 = op2;
            Op op4 = op3;
            Op op5 = op3;
            this.mTail = op5;
            this.mHead = op4;
        } else {
            op2.prev = this.mTail;
            this.mTail.next = op2;
            this.mTail = op2;
        }
        op2.enterAnim = this.mEnterAnim;
        op2.exitAnim = this.mExitAnim;
        op2.popEnterAnim = this.mPopEnterAnim;
        op2.popExitAnim = this.mPopExitAnim;
        this.mNumOp++;
    }

    public FragmentTransaction add(Fragment fragment, String str) {
        doAddOp(0, fragment, str, 1);
        return this;
    }

    public FragmentTransaction add(int i, Fragment fragment) {
        doAddOp(i, fragment, null, 1);
        return this;
    }

    public FragmentTransaction add(int i, Fragment fragment, String str) {
        doAddOp(i, fragment, str, 1);
        return this;
    }

    private void doAddOp(int i, Fragment fragment, String str, int i2) {
        IllegalStateException illegalStateException;
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        IllegalStateException illegalStateException2;
        int i3 = i;
        Fragment fragment2 = fragment;
        String str2 = str;
        int i4 = i2;
        fragment2.mFragmentManager = this.mManager;
        if (str2 != null) {
            if (fragment2.mTag == null || str2.equals(fragment2.mTag)) {
                fragment2.mTag = str2;
            } else {
                illegalStateException = r10;
                stringBuilder = r10;
                stringBuilder2 = new StringBuilder();
                illegalStateException2 = new IllegalStateException(stringBuilder.append("Can't change tag of fragment ").append(fragment2).append(": was ").append(fragment2.mTag).append(" now ").append(str2).toString());
                throw illegalStateException;
            }
        }
        if (i3 != 0) {
            if (fragment2.mFragmentId == 0 || fragment2.mFragmentId == i3) {
                Fragment fragment3 = fragment2;
                int i5 = i3;
                int i6 = i5;
                int i7 = i5;
                fragment2.mFragmentId = i7;
                fragment3.mContainerId = i6;
            } else {
                illegalStateException = r10;
                stringBuilder = r10;
                stringBuilder2 = new StringBuilder();
                illegalStateException2 = new IllegalStateException(stringBuilder.append("Can't change container ID of fragment ").append(fragment2).append(": was ").append(fragment2.mFragmentId).append(" now ").append(i3).toString());
                throw illegalStateException;
            }
        }
        Op op = r10;
        Op op2 = new Op();
        Op op3 = op;
        op3.cmd = i4;
        op3.fragment = fragment2;
        addOp(op3);
    }

    public FragmentTransaction replace(int i, Fragment fragment) {
        return replace(i, fragment, null);
    }

    public FragmentTransaction replace(int i, Fragment fragment, String str) {
        int i2 = i;
        Fragment fragment2 = fragment;
        String str2 = str;
        if (i2 == 0) {
            IllegalArgumentException illegalArgumentException = r9;
            IllegalArgumentException illegalArgumentException2 = new IllegalArgumentException("Must use non-zero containerViewId");
            throw illegalArgumentException;
        }
        doAddOp(i2, fragment2, str2, 2);
        return this;
    }

    public FragmentTransaction remove(Fragment fragment) {
        Fragment fragment2 = fragment;
        Op op = r5;
        Op op2 = new Op();
        Op op3 = op;
        op3.cmd = 3;
        op3.fragment = fragment2;
        addOp(op3);
        return this;
    }

    public FragmentTransaction hide(Fragment fragment) {
        Fragment fragment2 = fragment;
        Op op = r5;
        Op op2 = new Op();
        Op op3 = op;
        op3.cmd = 4;
        op3.fragment = fragment2;
        addOp(op3);
        return this;
    }

    public FragmentTransaction show(Fragment fragment) {
        Fragment fragment2 = fragment;
        Op op = r5;
        Op op2 = new Op();
        Op op3 = op;
        op3.cmd = 5;
        op3.fragment = fragment2;
        addOp(op3);
        return this;
    }

    public FragmentTransaction detach(Fragment fragment) {
        Fragment fragment2 = fragment;
        Op op = r5;
        Op op2 = new Op();
        Op op3 = op;
        op3.cmd = 6;
        op3.fragment = fragment2;
        addOp(op3);
        return this;
    }

    public FragmentTransaction attach(Fragment fragment) {
        Fragment fragment2 = fragment;
        Op op = r5;
        Op op2 = new Op();
        Op op3 = op;
        op3.cmd = 7;
        op3.fragment = fragment2;
        addOp(op3);
        return this;
    }

    public FragmentTransaction setCustomAnimations(int i, int i2) {
        return setCustomAnimations(i, i2, 0, 0);
    }

    public FragmentTransaction setCustomAnimations(int i, int i2, int i3, int i4) {
        int i5 = i2;
        int i6 = i3;
        int i7 = i4;
        this.mEnterAnim = i;
        this.mExitAnim = i5;
        this.mPopEnterAnim = i6;
        this.mPopExitAnim = i7;
        return this;
    }

    public FragmentTransaction setTransition(int i) {
        this.mTransition = i;
        return this;
    }

    public FragmentTransaction setTransitionStyle(int i) {
        this.mTransitionStyle = i;
        return this;
    }

    public FragmentTransaction addToBackStack(String str) {
        String str2 = str;
        if (this.mAllowAddToBackStack) {
            this.mAddToBackStack = true;
            this.mName = str2;
            return this;
        }
        IllegalStateException illegalStateException = r5;
        IllegalStateException illegalStateException2 = new IllegalStateException("This FragmentTransaction is not allowed to be added to the back stack.");
        throw illegalStateException;
    }

    public boolean isAddToBackStackAllowed() {
        return this.mAllowAddToBackStack;
    }

    public FragmentTransaction disallowAddToBackStack() {
        if (this.mAddToBackStack) {
            IllegalStateException illegalStateException = r4;
            IllegalStateException illegalStateException2 = new IllegalStateException("This transaction is already being added to the back stack");
            throw illegalStateException;
        }
        this.mAllowAddToBackStack = false;
        return this;
    }

    public FragmentTransaction setBreadCrumbTitle(int i) {
        this.mBreadCrumbTitleRes = i;
        this.mBreadCrumbTitleText = null;
        return this;
    }

    public FragmentTransaction setBreadCrumbTitle(CharSequence charSequence) {
        CharSequence charSequence2 = charSequence;
        this.mBreadCrumbTitleRes = 0;
        this.mBreadCrumbTitleText = charSequence2;
        return this;
    }

    public FragmentTransaction setBreadCrumbShortTitle(int i) {
        this.mBreadCrumbShortTitleRes = i;
        this.mBreadCrumbShortTitleText = null;
        return this;
    }

    public FragmentTransaction setBreadCrumbShortTitle(CharSequence charSequence) {
        CharSequence charSequence2 = charSequence;
        this.mBreadCrumbShortTitleRes = 0;
        this.mBreadCrumbShortTitleText = charSequence2;
        return this;
    }

    /* access modifiers changed from: 0000 */
    public void bumpBackStackNesting(int i) {
        int i2 = i;
        if (this.mAddToBackStack) {
            String str;
            StringBuilder stringBuilder;
            StringBuilder stringBuilder2;
            int v;
            if (FragmentManagerImpl.DEBUG) {
                str = TAG;
                stringBuilder = r8;
                stringBuilder2 = new StringBuilder();
                v = Log.v(str, stringBuilder.append("Bump nesting in ").append(this).append(" by ").append(i2).toString());
            }
            Op op = this.mHead;
            while (true) {
                Op op2 = op;
                if (op2 != null) {
                    Fragment fragment;
                    if (op2.fragment != null) {
                        fragment = op2.fragment;
                        fragment.mBackStackNesting += i2;
                        if (FragmentManagerImpl.DEBUG) {
                            str = TAG;
                            stringBuilder = r8;
                            stringBuilder2 = new StringBuilder();
                            v = Log.v(str, stringBuilder.append("Bump nesting of ").append(op2.fragment).append(" to ").append(op2.fragment.mBackStackNesting).toString());
                        }
                    }
                    if (op2.removed != null) {
                        for (int size = op2.removed.size() - 1; size >= 0; size--) {
                            Fragment fragment2 = (Fragment) op2.removed.get(size);
                            fragment = fragment2;
                            fragment.mBackStackNesting += i2;
                            if (FragmentManagerImpl.DEBUG) {
                                str = TAG;
                                stringBuilder = r8;
                                stringBuilder2 = new StringBuilder();
                                v = Log.v(str, stringBuilder.append("Bump nesting of ").append(fragment2).append(" to ").append(fragment2.mBackStackNesting).toString());
                            }
                        }
                    }
                    op = op2.next;
                } else {
                    return;
                }
            }
        }
    }

    public int commit() {
        return commitInternal(false);
    }

    public int commitAllowingStateLoss() {
        return commitInternal(true);
    }

    /* access modifiers changed from: 0000 */
    public int commitInternal(boolean z) {
        boolean z2 = z;
        if (this.mCommitted) {
            IllegalStateException illegalStateException = r9;
            IllegalStateException illegalStateException2 = new IllegalStateException("commit already called");
            throw illegalStateException;
        }
        if (FragmentManagerImpl.DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = r9;
            StringBuilder stringBuilder2 = new StringBuilder();
            int v = Log.v(str, stringBuilder.append("Commit: ").append(this).toString());
            Writer writer = r9;
            Writer logWriter = new LogWriter(TAG);
            Writer writer2 = writer;
            PrintWriter printWriter = r9;
            PrintWriter printWriter2 = new PrintWriter(writer2);
            dump("  ", null, printWriter, null);
        }
        this.mCommitted = true;
        if (this.mAddToBackStack) {
            this.mIndex = this.mManager.allocBackStackIndex(this);
        } else {
            this.mIndex = -1;
        }
        this.mManager.enqueueAction(this, z2);
        return this.mIndex;
    }

    public void run() {
        String str;
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        int v;
        if (FragmentManagerImpl.DEBUG) {
            str = TAG;
            stringBuilder = r10;
            stringBuilder2 = new StringBuilder();
            v = Log.v(str, stringBuilder.append("Run: ").append(this).toString());
        }
        if (!this.mAddToBackStack || this.mIndex >= 0) {
            bumpBackStackNesting(1);
            Op op = this.mHead;
            while (true) {
                Op op2 = op;
                if (op2 != null) {
                    Fragment fragment;
                    switch (op2.cmd) {
                        case 1:
                            fragment = op2.fragment;
                            fragment.mNextAnim = op2.enterAnim;
                            this.mManager.addFragment(fragment, false);
                            break;
                        case 2:
                            fragment = op2.fragment;
                            if (this.mManager.mAdded != null) {
                                for (int i = 0; i < this.mManager.mAdded.size(); i++) {
                                    Fragment fragment2 = (Fragment) this.mManager.mAdded.get(i);
                                    if (FragmentManagerImpl.DEBUG) {
                                        str = TAG;
                                        stringBuilder = r10;
                                        stringBuilder2 = new StringBuilder();
                                        v = Log.v(str, stringBuilder.append("OP_REPLACE: adding=").append(fragment).append(" old=").append(fragment2).toString());
                                    }
                                    if (fragment == null || fragment2.mContainerId == fragment.mContainerId) {
                                        Fragment fragment3;
                                        if (fragment2 == fragment) {
                                            fragment3 = null;
                                            fragment = fragment3;
                                            op2.fragment = fragment3;
                                        } else {
                                            if (op2.removed == null) {
                                                op = op2;
                                                ArrayList arrayList = r10;
                                                ArrayList arrayList2 = new ArrayList();
                                                op.removed = arrayList;
                                            }
                                            boolean add = op2.removed.add(fragment2);
                                            fragment2.mNextAnim = op2.exitAnim;
                                            if (this.mAddToBackStack) {
                                                fragment3 = fragment2;
                                                fragment3.mBackStackNesting++;
                                                if (FragmentManagerImpl.DEBUG) {
                                                    str = TAG;
                                                    stringBuilder = r10;
                                                    stringBuilder2 = new StringBuilder();
                                                    v = Log.v(str, stringBuilder.append("Bump nesting of ").append(fragment2).append(" to ").append(fragment2.mBackStackNesting).toString());
                                                }
                                            }
                                            this.mManager.removeFragment(fragment2, this.mTransition, this.mTransitionStyle);
                                        }
                                    }
                                }
                            }
                            if (fragment == null) {
                                break;
                            }
                            fragment.mNextAnim = op2.enterAnim;
                            this.mManager.addFragment(fragment, false);
                            break;
                            break;
                        case 3:
                            fragment = op2.fragment;
                            fragment.mNextAnim = op2.exitAnim;
                            this.mManager.removeFragment(fragment, this.mTransition, this.mTransitionStyle);
                            break;
                        case 4:
                            fragment = op2.fragment;
                            fragment.mNextAnim = op2.exitAnim;
                            this.mManager.hideFragment(fragment, this.mTransition, this.mTransitionStyle);
                            break;
                        case 5:
                            fragment = op2.fragment;
                            fragment.mNextAnim = op2.enterAnim;
                            this.mManager.showFragment(fragment, this.mTransition, this.mTransitionStyle);
                            break;
                        case 6:
                            fragment = op2.fragment;
                            fragment.mNextAnim = op2.exitAnim;
                            this.mManager.detachFragment(fragment, this.mTransition, this.mTransitionStyle);
                            break;
                        case 7:
                            fragment = op2.fragment;
                            fragment.mNextAnim = op2.enterAnim;
                            this.mManager.attachFragment(fragment, this.mTransition, this.mTransitionStyle);
                            break;
                        default:
                            IllegalArgumentException illegalArgumentException = r10;
                            stringBuilder2 = r10;
                            StringBuilder stringBuilder3 = new StringBuilder();
                            IllegalArgumentException illegalArgumentException2 = new IllegalArgumentException(stringBuilder2.append("Unknown cmd: ").append(op2.cmd).toString());
                            throw illegalArgumentException;
                    }
                    op = op2.next;
                } else {
                    this.mManager.moveToState(this.mManager.mCurState, this.mTransition, this.mTransitionStyle, true);
                    if (this.mAddToBackStack) {
                        this.mManager.addBackStackState(this);
                        return;
                    }
                    return;
                }
            }
        }
        IllegalStateException illegalStateException = r10;
        IllegalStateException illegalStateException2 = new IllegalStateException("addToBackStack() called after commit()");
        throw illegalStateException;
    }

    public void popFromBackStack(boolean z) {
        StringBuilder stringBuilder;
        boolean z2 = z;
        if (FragmentManagerImpl.DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder2 = r11;
            stringBuilder = new StringBuilder();
            int v = Log.v(str, stringBuilder2.append("popFromBackStack: ").append(this).toString());
            Writer writer = r11;
            Writer logWriter = new LogWriter(TAG);
            Writer writer2 = writer;
            PrintWriter printWriter = r11;
            PrintWriter printWriter2 = new PrintWriter(writer2);
            dump("  ", null, printWriter, null);
        }
        bumpBackStackNesting(-1);
        Op op = this.mTail;
        while (true) {
            Op op2 = op;
            if (op2 != null) {
                Fragment fragment;
                switch (op2.cmd) {
                    case 1:
                        fragment = op2.fragment;
                        fragment.mNextAnim = op2.popExitAnim;
                        this.mManager.removeFragment(fragment, FragmentManagerImpl.reverseTransit(this.mTransition), this.mTransitionStyle);
                        break;
                    case 2:
                        fragment = op2.fragment;
                        if (fragment != null) {
                            fragment.mNextAnim = op2.popExitAnim;
                            this.mManager.removeFragment(fragment, FragmentManagerImpl.reverseTransit(this.mTransition), this.mTransitionStyle);
                        }
                        if (op2.removed == null) {
                            break;
                        }
                        for (int i = 0; i < op2.removed.size(); i++) {
                            Fragment fragment2 = (Fragment) op2.removed.get(i);
                            fragment2.mNextAnim = op2.popEnterAnim;
                            this.mManager.addFragment(fragment2, false);
                        }
                        break;
                    case 3:
                        fragment = op2.fragment;
                        fragment.mNextAnim = op2.popEnterAnim;
                        this.mManager.addFragment(fragment, false);
                        break;
                    case 4:
                        fragment = op2.fragment;
                        fragment.mNextAnim = op2.popEnterAnim;
                        this.mManager.showFragment(fragment, FragmentManagerImpl.reverseTransit(this.mTransition), this.mTransitionStyle);
                        break;
                    case 5:
                        fragment = op2.fragment;
                        fragment.mNextAnim = op2.popExitAnim;
                        this.mManager.hideFragment(fragment, FragmentManagerImpl.reverseTransit(this.mTransition), this.mTransitionStyle);
                        break;
                    case 6:
                        fragment = op2.fragment;
                        fragment.mNextAnim = op2.popEnterAnim;
                        this.mManager.attachFragment(fragment, FragmentManagerImpl.reverseTransit(this.mTransition), this.mTransitionStyle);
                        break;
                    case 7:
                        fragment = op2.fragment;
                        fragment.mNextAnim = op2.popEnterAnim;
                        this.mManager.detachFragment(fragment, FragmentManagerImpl.reverseTransit(this.mTransition), this.mTransitionStyle);
                        break;
                    default:
                        IllegalArgumentException illegalArgumentException = r11;
                        stringBuilder = r11;
                        StringBuilder stringBuilder3 = new StringBuilder();
                        IllegalArgumentException illegalArgumentException2 = new IllegalArgumentException(stringBuilder.append("Unknown cmd: ").append(op2.cmd).toString());
                        throw illegalArgumentException;
                }
                op = op2.prev;
            } else {
                if (z2) {
                    this.mManager.moveToState(this.mManager.mCurState, FragmentManagerImpl.reverseTransit(this.mTransition), this.mTransitionStyle, true);
                }
                if (this.mIndex >= 0) {
                    this.mManager.freeBackStackIndex(this.mIndex);
                    this.mIndex = -1;
                    return;
                }
                return;
            }
        }
    }

    public String getName() {
        return this.mName;
    }

    public int getTransition() {
        return this.mTransition;
    }

    public int getTransitionStyle() {
        return this.mTransitionStyle;
    }

    public boolean isEmpty() {
        return this.mNumOp == 0;
    }
}
