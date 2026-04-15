package android.support.v4.app;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment.SavedState;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.util.DebugUtils;
import android.support.v4.util.LogWriter;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* compiled from: FragmentManager */
final class FragmentManagerImpl extends FragmentManager {
    static final Interpolator ACCELERATE_CUBIC;
    static final Interpolator ACCELERATE_QUINT;
    static final int ANIM_DUR = 220;
    public static final int ANIM_STYLE_CLOSE_ENTER = 3;
    public static final int ANIM_STYLE_CLOSE_EXIT = 4;
    public static final int ANIM_STYLE_FADE_ENTER = 5;
    public static final int ANIM_STYLE_FADE_EXIT = 6;
    public static final int ANIM_STYLE_OPEN_ENTER = 1;
    public static final int ANIM_STYLE_OPEN_EXIT = 2;
    static boolean DEBUG = HONEYCOMB;
    static final Interpolator DECELERATE_CUBIC;
    static final Interpolator DECELERATE_QUINT;
    static final boolean HONEYCOMB;
    static final String TAG = "FragmentManager";
    static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
    static final String TARGET_STATE_TAG = "android:target_state";
    static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
    static final String VIEW_STATE_TAG = "android:view_state";
    ArrayList<Fragment> mActive;
    FragmentActivity mActivity;
    ArrayList<Fragment> mAdded;
    ArrayList<Integer> mAvailBackStackIndices;
    ArrayList<Integer> mAvailIndices;
    ArrayList<BackStackRecord> mBackStack;
    ArrayList<OnBackStackChangedListener> mBackStackChangeListeners;
    ArrayList<BackStackRecord> mBackStackIndices;
    FragmentContainer mContainer;
    ArrayList<Fragment> mCreatedMenus;
    int mCurState = 0;
    boolean mDestroyed;
    Runnable mExecCommit;
    boolean mExecutingActions;
    boolean mHavePendingDeferredStart;
    boolean mNeedMenuInvalidate;
    String mNoTransactionsBecause;
    Fragment mParent;
    ArrayList<Runnable> mPendingActions;
    SparseArray<Parcelable> mStateArray = null;
    Bundle mStateBundle = null;
    boolean mStateSaved;
    Runnable[] mTmpActions;

    /*  JADX ERROR: JadxRuntimeException in pass: SSATransform
        jadx.core.utils.exceptions.JadxRuntimeException: Not initialized variable reg: 2, insn: 0x00b4: MOVE  (r6 ?[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r2 ?[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:40:0x00b3
        	at jadx.core.dex.visitors.ssa.SSATransform.renameVarsInBlock(SSATransform.java:157)
        	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:129)
        	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:51)
        	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:41)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.util.ArrayList.forEach(ArrayList.java:1257)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
        	at java.lang.Iterable.forEach(Iterable.java:75)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
        	at jadx.core.ProcessClass.process(ProcessClass.java:37)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:61)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    public boolean execPendingActions() {
        /*
        r9 = this;
        r0 = r9;
        r5 = r0;
        r5 = r5.mExecutingActions;
        if (r5 == 0) goto L_0x0011;
        r5 = new java.lang.IllegalStateException;
        r8 = r5;
        r5 = r8;
        r6 = r8;
        r7 = "Recursive entry to executePendingTransactions";
        r6.<init>(r7);
        throw r5;
        r5 = android.os.Looper.myLooper();
        r6 = r0;
        r6 = r6.mActivity;
        r6 = r6.mHandler;
        r6 = r6.getLooper();
        if (r5 == r6) goto L_0x002b;
        r5 = new java.lang.IllegalStateException;
        r8 = r5;
        r5 = r8;
        r6 = r8;
        r7 = "Must be called from main thread of process";
        r6.<init>(r7);
        throw r5;
        r5 = 0;
        r1 = r5;
        r5 = r0;
        r8 = r5;
        r5 = r8;
        r6 = r8;
        r3 = r6;
        monitor-enter(r5);
        r5 = r0;
        r5 = r5.mPendingActions;	 Catch:{ all -> 0x00ca }
        if (r5 == 0) goto L_0x0041;	 Catch:{ all -> 0x00ca }
        r5 = r0;	 Catch:{ all -> 0x00ca }
        r5 = r5.mPendingActions;	 Catch:{ all -> 0x00ca }
        r5 = r5.size();	 Catch:{ all -> 0x00ca }
        if (r5 != 0) goto L_0x0076;	 Catch:{ all -> 0x00ca }
        r5 = r3;	 Catch:{ all -> 0x00ca }
        monitor-exit(r5);	 Catch:{ all -> 0x00ca }
        r5 = r0;
        r5 = r5.mHavePendingDeferredStart;
        if (r5 == 0) goto L_0x00e3;
        r5 = 0;
        r2 = r5;
        r5 = 0;
        r3 = r5;
        r5 = r3;
        r6 = r0;
        r6 = r6.mActive;
        r6 = r6.size();
        if (r5 >= r6) goto L_0x00d8;
        r5 = r0;
        r5 = r5.mActive;
        r6 = r3;
        r5 = r5.get(r6);
        r5 = (android.support.v4.app.Fragment) r5;
        r4 = r5;
        r5 = r4;
        if (r5 == 0) goto L_0x0073;
        r5 = r4;
        r5 = r5.mLoaderManager;
        if (r5 == 0) goto L_0x0073;
        r5 = r2;
        r6 = r4;
        r6 = r6.mLoaderManager;
        r6 = r6.hasRunningLoaders();
        r5 = r5 | r6;
        r2 = r5;
        r3 = r3 + 1;
        goto L_0x004c;
        r5 = r0;
        r5 = r5.mPendingActions;	 Catch:{ all -> 0x00ca }
        r5 = r5.size();	 Catch:{ all -> 0x00ca }
        r2 = r5;	 Catch:{ all -> 0x00ca }
        r5 = r0;	 Catch:{ all -> 0x00ca }
        r5 = r5.mTmpActions;	 Catch:{ all -> 0x00ca }
        if (r5 == 0) goto L_0x008a;	 Catch:{ all -> 0x00ca }
        r5 = r0;	 Catch:{ all -> 0x00ca }
        r5 = r5.mTmpActions;	 Catch:{ all -> 0x00ca }
        r5 = r5.length;	 Catch:{ all -> 0x00ca }
        r6 = r2;	 Catch:{ all -> 0x00ca }
        if (r5 >= r6) goto L_0x0090;	 Catch:{ all -> 0x00ca }
        r5 = r0;	 Catch:{ all -> 0x00ca }
        r6 = r2;	 Catch:{ all -> 0x00ca }
        r6 = new java.lang.Runnable[r6];	 Catch:{ all -> 0x00ca }
        r5.mTmpActions = r6;	 Catch:{ all -> 0x00ca }
        r5 = r0;	 Catch:{ all -> 0x00ca }
        r5 = r5.mPendingActions;	 Catch:{ all -> 0x00ca }
        r6 = r0;	 Catch:{ all -> 0x00ca }
        r6 = r6.mTmpActions;	 Catch:{ all -> 0x00ca }
        r5 = r5.toArray(r6);	 Catch:{ all -> 0x00ca }
        r5 = r0;	 Catch:{ all -> 0x00ca }
        r5 = r5.mPendingActions;	 Catch:{ all -> 0x00ca }
        r5.clear();	 Catch:{ all -> 0x00ca }
        r5 = r0;	 Catch:{ all -> 0x00ca }
        r5 = r5.mActivity;	 Catch:{ all -> 0x00ca }
        r5 = r5.mHandler;	 Catch:{ all -> 0x00ca }
        r6 = r0;	 Catch:{ all -> 0x00ca }
        r6 = r6.mExecCommit;	 Catch:{ all -> 0x00ca }
        r5.removeCallbacks(r6);	 Catch:{ all -> 0x00ca }
        r5 = r3;	 Catch:{ all -> 0x00ca }
        monitor-exit(r5);	 Catch:{ all -> 0x00ca }
        r5 = r0;
        r6 = 1;
        r5.mExecutingActions = r6;
        r5 = 0;
        r3 = r5;
        r5 = r3;
        r6 = r2;
        if (r5 >= r6) goto L_0x00d0;
        r5 = r0;
        r5 = r5.mTmpActions;
        r6 = r3;
        r5 = r5[r6];
        r5.run();
        r5 = r0;
        r5 = r5.mTmpActions;
        r6 = r3;
        r7 = 0;
        r5[r6] = r7;
        r3 = r3 + 1;
        goto L_0x00b3;
        r5 = move-exception;
        r4 = r5;
        r5 = r3;
        monitor-exit(r5);	 Catch:{ all -> 0x00ca }
        r5 = r4;
        throw r5;
        r5 = r0;
        r6 = 0;
        r5.mExecutingActions = r6;
        r5 = 1;
        r1 = r5;
        goto L_0x002d;
        r5 = r2;
        if (r5 != 0) goto L_0x00e3;
        r5 = r0;
        r6 = 0;
        r5.mHavePendingDeferredStart = r6;
        r5 = r0;
        r5.startPendingDeferredFragments();
        r5 = r1;
        r0 = r5;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.app.FragmentManagerImpl.execPendingActions():boolean");
    }

    FragmentManagerImpl() {
        AnonymousClass1 anonymousClass1 = r5;
        AnonymousClass1 anonymousClass12 = new Runnable(this) {
            final /* synthetic */ FragmentManagerImpl this$0;

            {
                this.this$0 = r5;
            }

            public void run() {
                boolean execPendingActions = this.this$0.execPendingActions();
            }
        };
        this.mExecCommit = anonymousClass1;
    }

    static {
        boolean z;
        if (VERSION.SDK_INT >= 11) {
            z = true;
        } else {
            z = HONEYCOMB;
        }
        HONEYCOMB = z;
        DecelerateInterpolator decelerateInterpolator = r3;
        DecelerateInterpolator decelerateInterpolator2 = new DecelerateInterpolator(2.5f);
        DECELERATE_QUINT = decelerateInterpolator;
        decelerateInterpolator = r3;
        decelerateInterpolator2 = new DecelerateInterpolator(1.5f);
        DECELERATE_CUBIC = decelerateInterpolator;
        AccelerateInterpolator accelerateInterpolator = r3;
        AccelerateInterpolator accelerateInterpolator2 = new AccelerateInterpolator(2.5f);
        ACCELERATE_QUINT = accelerateInterpolator;
        accelerateInterpolator = r3;
        accelerateInterpolator2 = new AccelerateInterpolator(1.5f);
        ACCELERATE_CUBIC = accelerateInterpolator;
    }

    private void throwException(RuntimeException runtimeException) {
        RuntimeException runtimeException2 = runtimeException;
        int e = Log.e(TAG, runtimeException2.getMessage());
        e = Log.e(TAG, "Activity state:");
        Writer writer = r10;
        Writer logWriter = new LogWriter(TAG);
        Writer writer2 = writer;
        PrintWriter printWriter = r10;
        PrintWriter printWriter2 = new PrintWriter(writer2);
        PrintWriter printWriter3 = printWriter;
        if (this.mActivity != null) {
            try {
                this.mActivity.dump("  ", null, printWriter3, new String[0]);
            } catch (Exception e2) {
                e = Log.e(TAG, "Failed dumping state", e2);
            }
        } else {
            try {
                dump("  ", null, printWriter3, new String[0]);
            } catch (Exception e22) {
                e = Log.e(TAG, "Failed dumping state", e22);
            }
        }
        throw runtimeException2;
    }

    public FragmentTransaction beginTransaction() {
        FragmentTransaction fragmentTransaction = r4;
        FragmentTransaction backStackRecord = new BackStackRecord(this);
        return fragmentTransaction;
    }

    public boolean executePendingTransactions() {
        return execPendingActions();
    }

    public void popBackStack() {
        AnonymousClass2 anonymousClass2 = r5;
        AnonymousClass2 anonymousClass22 = new Runnable(this) {
            final /* synthetic */ FragmentManagerImpl this$0;

            {
                this.this$0 = r5;
            }

            public void run() {
                boolean popBackStackState = this.this$0.popBackStackState(this.this$0.mActivity.mHandler, null, -1, 0);
            }
        };
        enqueueAction(anonymousClass2, HONEYCOMB);
    }

    public boolean popBackStackImmediate() {
        checkStateLoss();
        boolean executePendingTransactions = executePendingTransactions();
        return popBackStackState(this.mActivity.mHandler, null, -1, 0);
    }

    public void popBackStack(String str, int i) {
        AnonymousClass3 anonymousClass3 = r9;
        final String str2 = str;
        final int i2 = i;
        AnonymousClass3 anonymousClass32 = new Runnable(this) {
            final /* synthetic */ FragmentManagerImpl this$0;

            public void run() {
                boolean popBackStackState = this.this$0.popBackStackState(this.this$0.mActivity.mHandler, str2, -1, i2);
            }
        };
        enqueueAction(anonymousClass3, HONEYCOMB);
    }

    public boolean popBackStackImmediate(String str, int i) {
        String str2 = str;
        int i2 = i;
        checkStateLoss();
        boolean executePendingTransactions = executePendingTransactions();
        return popBackStackState(this.mActivity.mHandler, str2, -1, i2);
    }

    public void popBackStack(int i, int i2) {
        int i3 = i;
        int i4 = i2;
        if (i3 < 0) {
            IllegalArgumentException illegalArgumentException = r9;
            StringBuilder stringBuilder = r9;
            StringBuilder stringBuilder2 = new StringBuilder();
            IllegalArgumentException illegalArgumentException2 = new IllegalArgumentException(stringBuilder.append("Bad id: ").append(i3).toString());
            throw illegalArgumentException;
        }
        AnonymousClass4 anonymousClass4 = r9;
        final int i5 = i3;
        final int i6 = i4;
        AnonymousClass4 anonymousClass42 = new Runnable(this) {
            final /* synthetic */ FragmentManagerImpl this$0;

            public void run() {
                boolean popBackStackState = this.this$0.popBackStackState(this.this$0.mActivity.mHandler, null, i5, i6);
            }
        };
        enqueueAction(anonymousClass4, HONEYCOMB);
    }

    public boolean popBackStackImmediate(int i, int i2) {
        int i3 = i;
        int i4 = i2;
        checkStateLoss();
        boolean executePendingTransactions = executePendingTransactions();
        if (i3 >= 0) {
            return popBackStackState(this.mActivity.mHandler, null, i3, i4);
        }
        IllegalArgumentException illegalArgumentException = r8;
        StringBuilder stringBuilder = r8;
        StringBuilder stringBuilder2 = new StringBuilder();
        IllegalArgumentException illegalArgumentException2 = new IllegalArgumentException(stringBuilder.append("Bad id: ").append(i3).toString());
        throw illegalArgumentException;
    }

    public int getBackStackEntryCount() {
        return this.mBackStack != null ? this.mBackStack.size() : 0;
    }

    public BackStackEntry getBackStackEntryAt(int i) {
        return (BackStackEntry) this.mBackStack.get(i);
    }

    public void addOnBackStackChangedListener(OnBackStackChangedListener onBackStackChangedListener) {
        OnBackStackChangedListener onBackStackChangedListener2 = onBackStackChangedListener;
        if (this.mBackStackChangeListeners == null) {
            ArrayList arrayList = r5;
            ArrayList arrayList2 = new ArrayList();
            this.mBackStackChangeListeners = arrayList;
        }
        boolean add = this.mBackStackChangeListeners.add(onBackStackChangedListener2);
    }

    public void removeOnBackStackChangedListener(OnBackStackChangedListener onBackStackChangedListener) {
        OnBackStackChangedListener onBackStackChangedListener2 = onBackStackChangedListener;
        if (this.mBackStackChangeListeners != null) {
            boolean remove = this.mBackStackChangeListeners.remove(onBackStackChangedListener2);
        }
    }

    public void putFragment(Bundle bundle, String str, Fragment fragment) {
        Bundle bundle2 = bundle;
        String str2 = str;
        Fragment fragment2 = fragment;
        if (fragment2.mIndex < 0) {
            RuntimeException runtimeException = r9;
            StringBuilder stringBuilder = r9;
            StringBuilder stringBuilder2 = new StringBuilder();
            RuntimeException illegalStateException = new IllegalStateException(stringBuilder.append("Fragment ").append(fragment2).append(" is not currently in the FragmentManager").toString());
            throwException(runtimeException);
        }
        bundle2.putInt(str2, fragment2.mIndex);
    }

    public Fragment getFragment(Bundle bundle, String str) {
        String str2 = str;
        int i = bundle.getInt(str2, -1);
        if (i == -1) {
            return null;
        }
        RuntimeException runtimeException;
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        RuntimeException illegalStateException;
        if (i >= this.mActive.size()) {
            runtimeException = r10;
            stringBuilder = r10;
            stringBuilder2 = new StringBuilder();
            illegalStateException = new IllegalStateException(stringBuilder.append("Fragement no longer exists for key ").append(str2).append(": index ").append(i).toString());
            throwException(runtimeException);
        }
        Fragment fragment = (Fragment) this.mActive.get(i);
        if (fragment == null) {
            runtimeException = r10;
            stringBuilder = r10;
            stringBuilder2 = new StringBuilder();
            illegalStateException = new IllegalStateException(stringBuilder.append("Fragement no longer exists for key ").append(str2).append(": index ").append(i).toString());
            throwException(runtimeException);
        }
        return fragment;
    }

    public List<Fragment> getFragments() {
        return this.mActive;
    }

    public SavedState saveFragmentInstanceState(Fragment fragment) {
        Fragment fragment2 = fragment;
        if (fragment2.mIndex < 0) {
            RuntimeException runtimeException = r8;
            StringBuilder stringBuilder = r8;
            StringBuilder stringBuilder2 = new StringBuilder();
            RuntimeException illegalStateException = new IllegalStateException(stringBuilder.append("Fragment ").append(fragment2).append(" is not currently in the FragmentManager").toString());
            throwException(runtimeException);
        }
        if (fragment2.mState <= 0) {
            return null;
        }
        SavedState savedState;
        Bundle saveFragmentBasicState = saveFragmentBasicState(fragment2);
        if (saveFragmentBasicState != null) {
            savedState = r8;
            SavedState savedState2 = new SavedState(saveFragmentBasicState);
        } else {
            savedState = null;
        }
        return savedState;
    }

    public String toString() {
        StringBuilder stringBuilder = r5;
        StringBuilder stringBuilder2 = new StringBuilder(128);
        StringBuilder stringBuilder3 = stringBuilder;
        stringBuilder = stringBuilder3.append("FragmentManager{");
        stringBuilder = stringBuilder3.append(Integer.toHexString(System.identityHashCode(this)));
        stringBuilder = stringBuilder3.append(" in ");
        if (this.mParent != null) {
            DebugUtils.buildShortClassTag(this.mParent, stringBuilder3);
        } else {
            DebugUtils.buildShortClassTag(this.mActivity, stringBuilder3);
        }
        stringBuilder = stringBuilder3.append("}}");
        return stringBuilder3.toString();
    }

    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        int size;
        int i;
        Fragment fragment;
        String str2 = str;
        FileDescriptor fileDescriptor2 = fileDescriptor;
        PrintWriter printWriter2 = printWriter;
        String[] strArr2 = strArr;
        StringBuilder stringBuilder = r16;
        StringBuilder stringBuilder2 = new StringBuilder();
        String stringBuilder3 = stringBuilder.append(str2).append("    ").toString();
        if (this.mActive != null) {
            size = this.mActive.size();
            if (size > 0) {
                printWriter2.print(str2);
                printWriter2.print("Active Fragments in ");
                printWriter2.print(Integer.toHexString(System.identityHashCode(this)));
                printWriter2.println(":");
                for (i = 0; i < size; i++) {
                    fragment = (Fragment) this.mActive.get(i);
                    printWriter2.print(str2);
                    printWriter2.print("  #");
                    printWriter2.print(i);
                    printWriter2.print(": ");
                    printWriter2.println(fragment);
                    if (fragment != null) {
                        fragment.dump(stringBuilder3, fileDescriptor2, printWriter2, strArr2);
                    }
                }
            }
        }
        if (this.mAdded != null) {
            size = this.mAdded.size();
            if (size > 0) {
                printWriter2.print(str2);
                printWriter2.println("Added Fragments:");
                for (i = 0; i < size; i++) {
                    fragment = (Fragment) this.mAdded.get(i);
                    printWriter2.print(str2);
                    printWriter2.print("  #");
                    printWriter2.print(i);
                    printWriter2.print(": ");
                    printWriter2.println(fragment.toString());
                }
            }
        }
        if (this.mCreatedMenus != null) {
            size = this.mCreatedMenus.size();
            if (size > 0) {
                printWriter2.print(str2);
                printWriter2.println("Fragments Created Menus:");
                for (i = 0; i < size; i++) {
                    fragment = (Fragment) this.mCreatedMenus.get(i);
                    printWriter2.print(str2);
                    printWriter2.print("  #");
                    printWriter2.print(i);
                    printWriter2.print(": ");
                    printWriter2.println(fragment.toString());
                }
            }
        }
        if (this.mBackStack != null) {
            size = this.mBackStack.size();
            if (size > 0) {
                printWriter2.print(str2);
                printWriter2.println("Back Stack:");
                for (i = 0; i < size; i++) {
                    BackStackRecord backStackRecord = (BackStackRecord) this.mBackStack.get(i);
                    printWriter2.print(str2);
                    printWriter2.print("  #");
                    printWriter2.print(i);
                    printWriter2.print(": ");
                    printWriter2.println(backStackRecord.toString());
                    backStackRecord.dump(stringBuilder3, fileDescriptor2, printWriter2, strArr2);
                }
            }
        }
        synchronized (this) {
            FragmentManagerImpl size2;
            try {
                if (this.mBackStackIndices != null) {
                    size2 = this.mBackStackIndices.size();
                    if (size2 > null) {
                        printWriter2.print(str2);
                        printWriter2.println("Back Stack Indices:");
                        for (int i2 = 0; i2 < size; i2++) {
                            BackStackRecord backStackRecord2 = (BackStackRecord) this.mBackStackIndices.get(i2);
                            printWriter2.print(str2);
                            printWriter2.print("  #");
                            printWriter2.print(i2);
                            printWriter2.print(": ");
                            printWriter2.println(backStackRecord2);
                        }
                    }
                }
            } finally {
                FragmentManagerImpl fragmentManagerImpl = size2;
                size2 = this;
                size2 = fragmentManagerImpl;
            }
            if (this.mAvailBackStackIndices != null && this.mAvailBackStackIndices.size() > 0) {
                printWriter2.print(str2);
                printWriter2.print("mAvailBackStackIndices: ");
                printWriter2.println(Arrays.toString(this.mAvailBackStackIndices.toArray()));
            }
            if (this.mPendingActions != null) {
                size = this.mPendingActions.size();
                if (size > 0) {
                    printWriter2.print(str2);
                    printWriter2.println("Pending Actions:");
                    for (i = 0; i < size; i++) {
                        Runnable runnable = (Runnable) this.mPendingActions.get(i);
                        printWriter2.print(str2);
                        printWriter2.print("  #");
                        printWriter2.print(i);
                        printWriter2.print(": ");
                        printWriter2.println(runnable);
                    }
                }
            }
            printWriter2.print(str2);
            printWriter2.println("FragmentManager misc state:");
            printWriter2.print(str2);
            printWriter2.print("  mActivity=");
            printWriter2.println(this.mActivity);
            printWriter2.print(str2);
            printWriter2.print("  mContainer=");
            printWriter2.println(this.mContainer);
            if (this.mParent != null) {
                printWriter2.print(str2);
                printWriter2.print("  mParent=");
                printWriter2.println(this.mParent);
            }
            printWriter2.print(str2);
            printWriter2.print("  mCurState=");
            printWriter2.print(this.mCurState);
            printWriter2.print(" mStateSaved=");
            printWriter2.print(this.mStateSaved);
            printWriter2.print(" mDestroyed=");
            printWriter2.println(this.mDestroyed);
            if (this.mNeedMenuInvalidate) {
                printWriter2.print(str2);
                printWriter2.print("  mNeedMenuInvalidate=");
                printWriter2.println(this.mNeedMenuInvalidate);
            }
            if (this.mNoTransactionsBecause != null) {
                printWriter2.print(str2);
                printWriter2.print("  mNoTransactionsBecause=");
                printWriter2.println(this.mNoTransactionsBecause);
            }
            if (this.mAvailIndices != null && this.mAvailIndices.size() > 0) {
                printWriter2.print(str2);
                printWriter2.print("  mAvailIndices: ");
                printWriter2.println(Arrays.toString(this.mAvailIndices.toArray()));
            }
        }
    }

    static Animation makeOpenCloseAnimation(Context context, float f, float f2, float f3, float f4) {
        Context context2 = context;
        float f5 = f;
        float f6 = f2;
        float f7 = f3;
        float f8 = f4;
        Animation animation = r18;
        Animation animationSet = new AnimationSet(HONEYCOMB);
        Animation animation2 = animation;
        animation = r18;
        animationSet = new ScaleAnimation(f5, f6, f5, f6, 1, 0.5f, 1, 0.5f);
        Animation animation3 = animation;
        animation3.setInterpolator(DECELERATE_QUINT);
        animation3.setDuration(220);
        animation2.addAnimation(animation3);
        animation = r18;
        animationSet = new AlphaAnimation(f7, f8);
        Animation animation4 = animation;
        animation4.setInterpolator(DECELERATE_CUBIC);
        animation4.setDuration(220);
        animation2.addAnimation(animation4);
        return animation2;
    }

    static Animation makeFadeAnimation(Context context, float f, float f2) {
        Context context2 = context;
        Animation animation = r8;
        Animation alphaAnimation = new AlphaAnimation(f, f2);
        Animation animation2 = animation;
        animation2.setInterpolator(DECELERATE_CUBIC);
        animation2.setDuration(220);
        return animation2;
    }

    /* access modifiers changed from: 0000 */
    public Animation loadAnimation(Fragment fragment, int i, boolean z, int i2) {
        Fragment fragment2 = fragment;
        int i3 = i;
        boolean z2 = z;
        int i4 = i2;
        Animation onCreateAnimation = fragment2.onCreateAnimation(i3, z2, fragment2.mNextAnim);
        if (onCreateAnimation != null) {
            return onCreateAnimation;
        }
        if (fragment2.mNextAnim != 0) {
            Animation loadAnimation = AnimationUtils.loadAnimation(this.mActivity, fragment2.mNextAnim);
            if (loadAnimation != null) {
                return loadAnimation;
            }
        }
        if (i3 == 0) {
            return null;
        }
        int transitToStyleIndex = transitToStyleIndex(i3, z2);
        if (transitToStyleIndex < 0) {
            return null;
        }
        switch (transitToStyleIndex) {
            case 1:
                return makeOpenCloseAnimation(this.mActivity, 1.125f, 1.0f, 0.0f, 1.0f);
            case 2:
                return makeOpenCloseAnimation(this.mActivity, 1.0f, 0.975f, 1.0f, 0.0f);
            case 3:
                return makeOpenCloseAnimation(this.mActivity, 0.975f, 1.0f, 0.0f, 1.0f);
            case 4:
                return makeOpenCloseAnimation(this.mActivity, 1.0f, 1.075f, 1.0f, 0.0f);
            case 5:
                return makeFadeAnimation(this.mActivity, 0.0f, 1.0f);
            case 6:
                return makeFadeAnimation(this.mActivity, 1.0f, 0.0f);
            default:
                if (i4 == 0 && this.mActivity.getWindow() != null) {
                    i4 = this.mActivity.getWindow().getAttributes().windowAnimations;
                }
                if (i4 == 0) {
                    return null;
                }
                return null;
        }
    }

    public void performPendingDeferredStart(Fragment fragment) {
        Fragment fragment2 = fragment;
        if (!fragment2.mDeferStart) {
            return;
        }
        if (this.mExecutingActions) {
            this.mHavePendingDeferredStart = true;
            return;
        }
        fragment2.mDeferStart = HONEYCOMB;
        moveToState(fragment2, this.mCurState, 0, 0, HONEYCOMB);
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Missing block: B:67:0x0190, code skipped:
            if (r2 <= 1) goto L_0x0297;
     */
    /* JADX WARNING: Missing block: B:69:0x0194, code skipped:
            if (DEBUG == false) goto L_0x01b3;
     */
    /* JADX WARNING: Missing block: B:70:0x0196, code skipped:
            r8 = TAG;
            r9 = r14;
            r10 = new java.lang.StringBuilder();
            r8 = android.util.Log.v(r8, r9.append("moveto ACTIVITY_CREATED: ").append(r1).toString());
     */
    /* JADX WARNING: Missing block: B:72:0x01b6, code skipped:
            if (r1.mFromLayout != false) goto L_0x0280;
     */
    /* JADX WARNING: Missing block: B:73:0x01b8, code skipped:
            r6 = null;
     */
    /* JADX WARNING: Missing block: B:74:0x01bd, code skipped:
            if (r1.mContainerId == 0) goto L_0x021e;
     */
    /* JADX WARNING: Missing block: B:75:0x01bf, code skipped:
            r6 = (android.view.ViewGroup) r0.mContainer.findViewById(r1.mContainerId);
     */
    /* JADX WARNING: Missing block: B:76:0x01cd, code skipped:
            if (r6 != null) goto L_0x021e;
     */
    /* JADX WARNING: Missing block: B:78:0x01d2, code skipped:
            if (r1.mRestored != false) goto L_0x021e;
     */
    /* JADX WARNING: Missing block: B:79:0x01d4, code skipped:
            r8 = r0;
            r9 = r14;
            r11 = r14;
            r12 = new java.lang.StringBuilder();
            r10 = new java.lang.IllegalArgumentException(r11.append("No view found for id 0x").append(java.lang.Integer.toHexString(r1.mContainerId)).append(" (").append(r1.getResources().getResourceName(r1.mContainerId)).append(") for fragment ").append(r1).toString());
            throwException(r9);
     */
    /* JADX WARNING: Missing block: B:80:0x021e, code skipped:
            r1.mContainer = r6;
            r1.mView = r1.performCreateView(r1.getLayoutInflater(r1.mSavedFragmentState), r6, r1.mSavedFragmentState);
     */
    /* JADX WARNING: Missing block: B:81:0x0239, code skipped:
            if (r1.mView == null) goto L_0x02fd;
     */
    /* JADX WARNING: Missing block: B:82:0x023b, code skipped:
            r1.mInnerView = r1.mView;
            r1.mView = android.support.v4.app.NoSaveStateFrameLayout.wrap(r1.mView);
     */
    /* JADX WARNING: Missing block: B:83:0x024c, code skipped:
            if (r6 == null) goto L_0x0269;
     */
    /* JADX WARNING: Missing block: B:84:0x024e, code skipped:
            r7 = loadAnimation(r1, r3, true, r4);
     */
    /* JADX WARNING: Missing block: B:85:0x0259, code skipped:
            if (r7 == null) goto L_0x0262;
     */
    /* JADX WARNING: Missing block: B:86:0x025b, code skipped:
            r1.mView.startAnimation(r7);
     */
    /* JADX WARNING: Missing block: B:87:0x0262, code skipped:
            r6.addView(r1.mView);
     */
    /* JADX WARNING: Missing block: B:89:0x026c, code skipped:
            if (r1.mHidden == false) goto L_0x0276;
     */
    /* JADX WARNING: Missing block: B:90:0x026e, code skipped:
            r1.mView.setVisibility(8);
     */
    /* JADX WARNING: Missing block: B:91:0x0276, code skipped:
            r1.onViewCreated(r1.mView, r1.mSavedFragmentState);
     */
    /* JADX WARNING: Missing block: B:92:0x0280, code skipped:
            r1.performActivityCreated(r1.mSavedFragmentState);
     */
    /* JADX WARNING: Missing block: B:93:0x028a, code skipped:
            if (r1.mView == null) goto L_0x0293;
     */
    /* JADX WARNING: Missing block: B:94:0x028c, code skipped:
            r1.restoreViewState(r1.mSavedFragmentState);
     */
    /* JADX WARNING: Missing block: B:95:0x0293, code skipped:
            r1.mSavedFragmentState = null;
     */
    /* JADX WARNING: Missing block: B:97:0x0299, code skipped:
            if (r2 <= 3) goto L_0x02c0;
     */
    /* JADX WARNING: Missing block: B:99:0x029d, code skipped:
            if (DEBUG == false) goto L_0x02bc;
     */
    /* JADX WARNING: Missing block: B:100:0x029f, code skipped:
            r8 = TAG;
            r9 = r14;
            r10 = new java.lang.StringBuilder();
            r8 = android.util.Log.v(r8, r9.append("moveto STARTED: ").append(r1).toString());
     */
    /* JADX WARNING: Missing block: B:101:0x02bc, code skipped:
            r1.performStart();
     */
    /* JADX WARNING: Missing block: B:103:0x02c2, code skipped:
            if (r2 <= 4) goto L_0x0066;
     */
    /* JADX WARNING: Missing block: B:105:0x02c6, code skipped:
            if (DEBUG == false) goto L_0x02e5;
     */
    /* JADX WARNING: Missing block: B:106:0x02c8, code skipped:
            r8 = TAG;
            r9 = r14;
            r10 = new java.lang.StringBuilder();
            r8 = android.util.Log.v(r8, r9.append("moveto RESUMED: ").append(r1).toString());
     */
    /* JADX WARNING: Missing block: B:107:0x02e5, code skipped:
            r1.mResumed = true;
            r1.performResume();
            r1.mSavedFragmentState = null;
            r1.mSavedViewState = null;
     */
    /* JADX WARNING: Missing block: B:109:0x02fd, code skipped:
            r1.mInnerView = null;
     */
    /* JADX WARNING: Missing block: B:115:0x0313, code skipped:
            if (r2 >= 1) goto L_0x0066;
     */
    /* JADX WARNING: Missing block: B:117:0x0318, code skipped:
            if (r0.mDestroyed == false) goto L_0x032b;
     */
    /* JADX WARNING: Missing block: B:119:0x031d, code skipped:
            if (r1.mAnimatingAway == null) goto L_0x032b;
     */
    /* JADX WARNING: Missing block: B:120:0x031f, code skipped:
            r6 = r1.mAnimatingAway;
            r1.mAnimatingAway = null;
            r6.clearAnimation();
     */
    /* JADX WARNING: Missing block: B:122:0x032e, code skipped:
            if (r1.mAnimatingAway == null) goto L_0x0453;
     */
    /* JADX WARNING: Missing block: B:123:0x0330, code skipped:
            r1.mStateAfterAnimating = r2;
            r2 = 1;
     */
    /* JADX WARNING: Missing block: B:131:0x0367, code skipped:
            if (r2 >= 4) goto L_0x038e;
     */
    /* JADX WARNING: Missing block: B:133:0x036b, code skipped:
            if (DEBUG == false) goto L_0x038a;
     */
    /* JADX WARNING: Missing block: B:134:0x036d, code skipped:
            r8 = TAG;
            r9 = r14;
            r10 = new java.lang.StringBuilder();
            r8 = android.util.Log.v(r8, r9.append("movefrom STARTED: ").append(r1).toString());
     */
    /* JADX WARNING: Missing block: B:135:0x038a, code skipped:
            r1.performStop();
     */
    /* JADX WARNING: Missing block: B:137:0x0390, code skipped:
            if (r2 >= 3) goto L_0x03b7;
     */
    /* JADX WARNING: Missing block: B:139:0x0394, code skipped:
            if (DEBUG == false) goto L_0x03b3;
     */
    /* JADX WARNING: Missing block: B:140:0x0396, code skipped:
            r8 = TAG;
            r9 = r14;
            r10 = new java.lang.StringBuilder();
            r8 = android.util.Log.v(r8, r9.append("movefrom STOPPED: ").append(r1).toString());
     */
    /* JADX WARNING: Missing block: B:141:0x03b3, code skipped:
            r1.performReallyStop();
     */
    /* JADX WARNING: Missing block: B:143:0x03b9, code skipped:
            if (r2 >= 2) goto L_0x0311;
     */
    /* JADX WARNING: Missing block: B:145:0x03bd, code skipped:
            if (DEBUG == false) goto L_0x03dc;
     */
    /* JADX WARNING: Missing block: B:146:0x03bf, code skipped:
            r8 = TAG;
            r9 = r14;
            r10 = new java.lang.StringBuilder();
            r8 = android.util.Log.v(r8, r9.append("movefrom ACTIVITY_CREATED: ").append(r1).toString());
     */
    /* JADX WARNING: Missing block: B:148:0x03df, code skipped:
            if (r1.mView == null) goto L_0x03f4;
     */
    /* JADX WARNING: Missing block: B:150:0x03e8, code skipped:
            if (r0.mActivity.isFinishing() != false) goto L_0x03f4;
     */
    /* JADX WARNING: Missing block: B:152:0x03ed, code skipped:
            if (r1.mSavedViewState != null) goto L_0x03f4;
     */
    /* JADX WARNING: Missing block: B:153:0x03ef, code skipped:
            saveFragmentViewState(r1);
     */
    /* JADX WARNING: Missing block: B:154:0x03f4, code skipped:
            r1.performDestroyView();
     */
    /* JADX WARNING: Missing block: B:155:0x03fb, code skipped:
            if (r1.mView == null) goto L_0x0445;
     */
    /* JADX WARNING: Missing block: B:157:0x0400, code skipped:
            if (r1.mContainer == null) goto L_0x0445;
     */
    /* JADX WARNING: Missing block: B:158:0x0402, code skipped:
            r6 = null;
     */
    /* JADX WARNING: Missing block: B:159:0x0407, code skipped:
            if (r0.mCurState <= 0) goto L_0x0418;
     */
    /* JADX WARNING: Missing block: B:161:0x040c, code skipped:
            if (r0.mDestroyed != false) goto L_0x0418;
     */
    /* JADX WARNING: Missing block: B:162:0x040e, code skipped:
            r6 = loadAnimation(r1, r3, HONEYCOMB, r4);
     */
    /* JADX WARNING: Missing block: B:164:0x0419, code skipped:
            if (r6 == null) goto L_0x043c;
     */
    /* JADX WARNING: Missing block: B:165:0x041b, code skipped:
            r7 = r1;
            r1.mAnimatingAway = r1.mView;
            r1.mStateAfterAnimating = r2;
            r8 = r6;
            r9 = r14;
            r12 = r7;
            r10 = new android.support.v4.app.FragmentManagerImpl.AnonymousClass5(r0);
            r8.setAnimationListener(r9);
            r1.mView.startAnimation(r6);
     */
    /* JADX WARNING: Missing block: B:166:0x043c, code skipped:
            r1.mContainer.removeView(r1.mView);
     */
    /* JADX WARNING: Missing block: B:167:0x0445, code skipped:
            r1.mContainer = null;
            r1.mView = null;
            r1.mInnerView = null;
     */
    /* JADX WARNING: Missing block: B:169:0x0455, code skipped:
            if (DEBUG == false) goto L_0x0474;
     */
    /* JADX WARNING: Missing block: B:170:0x0457, code skipped:
            r8 = TAG;
            r9 = r14;
            r10 = new java.lang.StringBuilder();
            r8 = android.util.Log.v(r8, r9.append("movefrom CREATED: ").append(r1).toString());
     */
    /* JADX WARNING: Missing block: B:172:0x0477, code skipped:
            if (r1.mRetaining != false) goto L_0x047d;
     */
    /* JADX WARNING: Missing block: B:173:0x0479, code skipped:
            r1.performDestroy();
     */
    /* JADX WARNING: Missing block: B:174:0x047d, code skipped:
            r1.mCalled = HONEYCOMB;
            r1.onDetach();
     */
    /* JADX WARNING: Missing block: B:175:0x0488, code skipped:
            if (r1.mCalled != false) goto L_0x04b0;
     */
    /* JADX WARNING: Missing block: B:176:0x048a, code skipped:
            r8 = r14;
            r10 = r14;
            r11 = new java.lang.StringBuilder();
            r9 = new android.support.v4.app.SuperNotCalledException(r10.append("Fragment ").append(r1).append(" did not call through to super.onDetach()").toString());
     */
    /* JADX WARNING: Missing block: B:177:0x04af, code skipped:
            throw r8;
     */
    /* JADX WARNING: Missing block: B:179:0x04b1, code skipped:
            if (r5 != false) goto L_0x0066;
     */
    /* JADX WARNING: Missing block: B:181:0x04b6, code skipped:
            if (r1.mRetaining != false) goto L_0x04bf;
     */
    /* JADX WARNING: Missing block: B:182:0x04b8, code skipped:
            makeInactive(r1);
     */
    /* JADX WARNING: Missing block: B:183:0x04bf, code skipped:
            r1.mActivity = null;
            r1.mFragmentManager = null;
     */
    public void moveToState(android.support.v4.app.Fragment r16, int r17, int r18, int r19, boolean r20) {
        /*
        r15 = this;
        r0 = r15;
        r1 = r16;
        r2 = r17;
        r3 = r18;
        r4 = r19;
        r5 = r20;
        r8 = r1;
        r8 = r8.mAdded;
        if (r8 == 0) goto L_0x0015;
    L_0x0010:
        r8 = r1;
        r8 = r8.mDetached;
        if (r8 == 0) goto L_0x001b;
    L_0x0015:
        r8 = r2;
        r9 = 1;
        if (r8 <= r9) goto L_0x001b;
    L_0x0019:
        r8 = 1;
        r2 = r8;
    L_0x001b:
        r8 = r1;
        r8 = r8.mRemoving;
        if (r8 == 0) goto L_0x002a;
    L_0x0020:
        r8 = r2;
        r9 = r1;
        r9 = r9.mState;
        if (r8 <= r9) goto L_0x002a;
    L_0x0026:
        r8 = r1;
        r8 = r8.mState;
        r2 = r8;
    L_0x002a:
        r8 = r1;
        r8 = r8.mDeferStart;
        if (r8 == 0) goto L_0x003b;
    L_0x002f:
        r8 = r1;
        r8 = r8.mState;
        r9 = 4;
        if (r8 >= r9) goto L_0x003b;
    L_0x0035:
        r8 = r2;
        r9 = 3;
        if (r8 <= r9) goto L_0x003b;
    L_0x0039:
        r8 = 3;
        r2 = r8;
    L_0x003b:
        r8 = r1;
        r8 = r8.mState;
        r9 = r2;
        if (r8 >= r9) goto L_0x0303;
    L_0x0041:
        r8 = r1;
        r8 = r8.mFromLayout;
        if (r8 == 0) goto L_0x004c;
    L_0x0046:
        r8 = r1;
        r8 = r8.mInLayout;
        if (r8 != 0) goto L_0x004c;
    L_0x004b:
        return;
    L_0x004c:
        r8 = r1;
        r8 = r8.mAnimatingAway;
        if (r8 == 0) goto L_0x0060;
    L_0x0051:
        r8 = r1;
        r9 = 0;
        r8.mAnimatingAway = r9;
        r8 = r0;
        r9 = r1;
        r10 = r1;
        r10 = r10.mStateAfterAnimating;
        r11 = 0;
        r12 = 0;
        r13 = 1;
        r8.moveToState(r9, r10, r11, r12, r13);
    L_0x0060:
        r8 = r1;
        r8 = r8.mState;
        switch(r8) {
            case 0: goto L_0x006b;
            case 1: goto L_0x018e;
            case 2: goto L_0x0297;
            case 3: goto L_0x0297;
            case 4: goto L_0x02c0;
            default: goto L_0x0066;
        };
    L_0x0066:
        r8 = r1;
        r9 = r2;
        r8.mState = r9;
        goto L_0x004b;
    L_0x006b:
        r8 = DEBUG;
        if (r8 == 0) goto L_0x008c;
    L_0x006f:
        r8 = "FragmentManager";
        r9 = new java.lang.StringBuilder;
        r14 = r9;
        r9 = r14;
        r10 = r14;
        r10.<init>();
        r10 = "moveto CREATED: ";
        r9 = r9.append(r10);
        r10 = r1;
        r9 = r9.append(r10);
        r9 = r9.toString();
        r8 = android.util.Log.v(r8, r9);
    L_0x008c:
        r8 = r1;
        r8 = r8.mSavedFragmentState;
        if (r8 == 0) goto L_0x00d8;
    L_0x0091:
        r8 = r1;
        r9 = r1;
        r9 = r9.mSavedFragmentState;
        r10 = "android:view_state";
        r9 = r9.getSparseParcelableArray(r10);
        r8.mSavedViewState = r9;
        r8 = r1;
        r9 = r0;
        r10 = r1;
        r10 = r10.mSavedFragmentState;
        r11 = "android:target_state";
        r9 = r9.getFragment(r10, r11);
        r8.mTarget = r9;
        r8 = r1;
        r8 = r8.mTarget;
        if (r8 == 0) goto L_0x00bc;
    L_0x00af:
        r8 = r1;
        r9 = r1;
        r9 = r9.mSavedFragmentState;
        r10 = "android:target_req_state";
        r11 = 0;
        r9 = r9.getInt(r10, r11);
        r8.mTargetRequestCode = r9;
    L_0x00bc:
        r8 = r1;
        r9 = r1;
        r9 = r9.mSavedFragmentState;
        r10 = "android:user_visible_hint";
        r11 = 1;
        r9 = r9.getBoolean(r10, r11);
        r8.mUserVisibleHint = r9;
        r8 = r1;
        r8 = r8.mUserVisibleHint;
        if (r8 != 0) goto L_0x00d8;
    L_0x00ce:
        r8 = r1;
        r9 = 1;
        r8.mDeferStart = r9;
        r8 = r2;
        r9 = 3;
        if (r8 <= r9) goto L_0x00d8;
    L_0x00d6:
        r8 = 3;
        r2 = r8;
    L_0x00d8:
        r8 = r1;
        r9 = r0;
        r9 = r9.mActivity;
        r8.mActivity = r9;
        r8 = r1;
        r9 = r0;
        r9 = r9.mParent;
        r8.mParentFragment = r9;
        r8 = r1;
        r9 = r0;
        r9 = r9.mParent;
        if (r9 == 0) goto L_0x0127;
    L_0x00ea:
        r9 = r0;
        r9 = r9.mParent;
        r9 = r9.mChildFragmentManager;
    L_0x00ef:
        r8.mFragmentManager = r9;
        r8 = r1;
        r9 = 0;
        r8.mCalled = r9;
        r8 = r1;
        r9 = r0;
        r9 = r9.mActivity;
        r8.onAttach(r9);
        r8 = r1;
        r8 = r8.mCalled;
        if (r8 != 0) goto L_0x012d;
    L_0x0101:
        r8 = new android.support.v4.app.SuperNotCalledException;
        r14 = r8;
        r8 = r14;
        r9 = r14;
        r10 = new java.lang.StringBuilder;
        r14 = r10;
        r10 = r14;
        r11 = r14;
        r11.<init>();
        r11 = "Fragment ";
        r10 = r10.append(r11);
        r11 = r1;
        r10 = r10.append(r11);
        r11 = " did not call through to super.onAttach()";
        r10 = r10.append(r11);
        r10 = r10.toString();
        r9.m118init(r10);
        throw r8;
    L_0x0127:
        r9 = r0;
        r9 = r9.mActivity;
        r9 = r9.mFragments;
        goto L_0x00ef;
    L_0x012d:
        r8 = r1;
        r8 = r8.mParentFragment;
        if (r8 != 0) goto L_0x0139;
    L_0x0132:
        r8 = r0;
        r8 = r8.mActivity;
        r9 = r1;
        r8.onAttachFragment(r9);
    L_0x0139:
        r8 = r1;
        r8 = r8.mRetaining;
        if (r8 != 0) goto L_0x0145;
    L_0x013e:
        r8 = r1;
        r9 = r1;
        r9 = r9.mSavedFragmentState;
        r8.performCreate(r9);
    L_0x0145:
        r8 = r1;
        r9 = 0;
        r8.mRetaining = r9;
        r8 = r1;
        r8 = r8.mFromLayout;
        if (r8 == 0) goto L_0x018e;
    L_0x014e:
        r8 = r1;
        r9 = r1;
        r10 = r1;
        r11 = r1;
        r11 = r11.mSavedFragmentState;
        r10 = r10.getLayoutInflater(r11);
        r11 = 0;
        r12 = r1;
        r12 = r12.mSavedFragmentState;
        r9 = r9.performCreateView(r10, r11, r12);
        r8.mView = r9;
        r8 = r1;
        r8 = r8.mView;
        if (r8 == 0) goto L_0x02f7;
    L_0x0167:
        r8 = r1;
        r9 = r1;
        r9 = r9.mView;
        r8.mInnerView = r9;
        r8 = r1;
        r9 = r1;
        r9 = r9.mView;
        r9 = android.support.v4.app.NoSaveStateFrameLayout.wrap(r9);
        r8.mView = r9;
        r8 = r1;
        r8 = r8.mHidden;
        if (r8 == 0) goto L_0x0184;
    L_0x017c:
        r8 = r1;
        r8 = r8.mView;
        r9 = 8;
        r8.setVisibility(r9);
    L_0x0184:
        r8 = r1;
        r9 = r1;
        r9 = r9.mView;
        r10 = r1;
        r10 = r10.mSavedFragmentState;
        r8.onViewCreated(r9, r10);
    L_0x018e:
        r8 = r2;
        r9 = 1;
        if (r8 <= r9) goto L_0x0297;
    L_0x0192:
        r8 = DEBUG;
        if (r8 == 0) goto L_0x01b3;
    L_0x0196:
        r8 = "FragmentManager";
        r9 = new java.lang.StringBuilder;
        r14 = r9;
        r9 = r14;
        r10 = r14;
        r10.<init>();
        r10 = "moveto ACTIVITY_CREATED: ";
        r9 = r9.append(r10);
        r10 = r1;
        r9 = r9.append(r10);
        r9 = r9.toString();
        r8 = android.util.Log.v(r8, r9);
    L_0x01b3:
        r8 = r1;
        r8 = r8.mFromLayout;
        if (r8 != 0) goto L_0x0280;
    L_0x01b8:
        r8 = 0;
        r6 = r8;
        r8 = r1;
        r8 = r8.mContainerId;
        if (r8 == 0) goto L_0x021e;
    L_0x01bf:
        r8 = r0;
        r8 = r8.mContainer;
        r9 = r1;
        r9 = r9.mContainerId;
        r8 = r8.findViewById(r9);
        r8 = (android.view.ViewGroup) r8;
        r6 = r8;
        r8 = r6;
        if (r8 != 0) goto L_0x021e;
    L_0x01cf:
        r8 = r1;
        r8 = r8.mRestored;
        if (r8 != 0) goto L_0x021e;
    L_0x01d4:
        r8 = r0;
        r9 = new java.lang.IllegalArgumentException;
        r14 = r9;
        r9 = r14;
        r10 = r14;
        r11 = new java.lang.StringBuilder;
        r14 = r11;
        r11 = r14;
        r12 = r14;
        r12.<init>();
        r12 = "No view found for id 0x";
        r11 = r11.append(r12);
        r12 = r1;
        r12 = r12.mContainerId;
        r12 = java.lang.Integer.toHexString(r12);
        r11 = r11.append(r12);
        r12 = " (";
        r11 = r11.append(r12);
        r12 = r1;
        r12 = r12.getResources();
        r13 = r1;
        r13 = r13.mContainerId;
        r12 = r12.getResourceName(r13);
        r11 = r11.append(r12);
        r12 = ") for fragment ";
        r11 = r11.append(r12);
        r12 = r1;
        r11 = r11.append(r12);
        r11 = r11.toString();
        r10.<init>(r11);
        r8.throwException(r9);
    L_0x021e:
        r8 = r1;
        r9 = r6;
        r8.mContainer = r9;
        r8 = r1;
        r9 = r1;
        r10 = r1;
        r11 = r1;
        r11 = r11.mSavedFragmentState;
        r10 = r10.getLayoutInflater(r11);
        r11 = r6;
        r12 = r1;
        r12 = r12.mSavedFragmentState;
        r9 = r9.performCreateView(r10, r11, r12);
        r8.mView = r9;
        r8 = r1;
        r8 = r8.mView;
        if (r8 == 0) goto L_0x02fd;
    L_0x023b:
        r8 = r1;
        r9 = r1;
        r9 = r9.mView;
        r8.mInnerView = r9;
        r8 = r1;
        r9 = r1;
        r9 = r9.mView;
        r9 = android.support.v4.app.NoSaveStateFrameLayout.wrap(r9);
        r8.mView = r9;
        r8 = r6;
        if (r8 == 0) goto L_0x0269;
    L_0x024e:
        r8 = r0;
        r9 = r1;
        r10 = r3;
        r11 = 1;
        r12 = r4;
        r8 = r8.loadAnimation(r9, r10, r11, r12);
        r7 = r8;
        r8 = r7;
        if (r8 == 0) goto L_0x0262;
    L_0x025b:
        r8 = r1;
        r8 = r8.mView;
        r9 = r7;
        r8.startAnimation(r9);
    L_0x0262:
        r8 = r6;
        r9 = r1;
        r9 = r9.mView;
        r8.addView(r9);
    L_0x0269:
        r8 = r1;
        r8 = r8.mHidden;
        if (r8 == 0) goto L_0x0276;
    L_0x026e:
        r8 = r1;
        r8 = r8.mView;
        r9 = 8;
        r8.setVisibility(r9);
    L_0x0276:
        r8 = r1;
        r9 = r1;
        r9 = r9.mView;
        r10 = r1;
        r10 = r10.mSavedFragmentState;
        r8.onViewCreated(r9, r10);
    L_0x0280:
        r8 = r1;
        r9 = r1;
        r9 = r9.mSavedFragmentState;
        r8.performActivityCreated(r9);
        r8 = r1;
        r8 = r8.mView;
        if (r8 == 0) goto L_0x0293;
    L_0x028c:
        r8 = r1;
        r9 = r1;
        r9 = r9.mSavedFragmentState;
        r8.restoreViewState(r9);
    L_0x0293:
        r8 = r1;
        r9 = 0;
        r8.mSavedFragmentState = r9;
    L_0x0297:
        r8 = r2;
        r9 = 3;
        if (r8 <= r9) goto L_0x02c0;
    L_0x029b:
        r8 = DEBUG;
        if (r8 == 0) goto L_0x02bc;
    L_0x029f:
        r8 = "FragmentManager";
        r9 = new java.lang.StringBuilder;
        r14 = r9;
        r9 = r14;
        r10 = r14;
        r10.<init>();
        r10 = "moveto STARTED: ";
        r9 = r9.append(r10);
        r10 = r1;
        r9 = r9.append(r10);
        r9 = r9.toString();
        r8 = android.util.Log.v(r8, r9);
    L_0x02bc:
        r8 = r1;
        r8.performStart();
    L_0x02c0:
        r8 = r2;
        r9 = 4;
        if (r8 <= r9) goto L_0x0066;
    L_0x02c4:
        r8 = DEBUG;
        if (r8 == 0) goto L_0x02e5;
    L_0x02c8:
        r8 = "FragmentManager";
        r9 = new java.lang.StringBuilder;
        r14 = r9;
        r9 = r14;
        r10 = r14;
        r10.<init>();
        r10 = "moveto RESUMED: ";
        r9 = r9.append(r10);
        r10 = r1;
        r9 = r9.append(r10);
        r9 = r9.toString();
        r8 = android.util.Log.v(r8, r9);
    L_0x02e5:
        r8 = r1;
        r9 = 1;
        r8.mResumed = r9;
        r8 = r1;
        r8.performResume();
        r8 = r1;
        r9 = 0;
        r8.mSavedFragmentState = r9;
        r8 = r1;
        r9 = 0;
        r8.mSavedViewState = r9;
        goto L_0x0066;
    L_0x02f7:
        r8 = r1;
        r9 = 0;
        r8.mInnerView = r9;
        goto L_0x018e;
    L_0x02fd:
        r8 = r1;
        r9 = 0;
        r8.mInnerView = r9;
        goto L_0x0280;
    L_0x0303:
        r8 = r1;
        r8 = r8.mState;
        r9 = r2;
        if (r8 <= r9) goto L_0x0066;
    L_0x0309:
        r8 = r1;
        r8 = r8.mState;
        switch(r8) {
            case 1: goto L_0x0311;
            case 2: goto L_0x03b7;
            case 3: goto L_0x038e;
            case 4: goto L_0x0365;
            case 5: goto L_0x0338;
            default: goto L_0x030f;
        };
    L_0x030f:
        goto L_0x0066;
    L_0x0311:
        r8 = r2;
        r9 = 1;
        if (r8 >= r9) goto L_0x0066;
    L_0x0315:
        r8 = r0;
        r8 = r8.mDestroyed;
        if (r8 == 0) goto L_0x032b;
    L_0x031a:
        r8 = r1;
        r8 = r8.mAnimatingAway;
        if (r8 == 0) goto L_0x032b;
    L_0x031f:
        r8 = r1;
        r8 = r8.mAnimatingAway;
        r6 = r8;
        r8 = r1;
        r9 = 0;
        r8.mAnimatingAway = r9;
        r8 = r6;
        r8.clearAnimation();
    L_0x032b:
        r8 = r1;
        r8 = r8.mAnimatingAway;
        if (r8 == 0) goto L_0x0453;
    L_0x0330:
        r8 = r1;
        r9 = r2;
        r8.mStateAfterAnimating = r9;
        r8 = 1;
        r2 = r8;
        goto L_0x0066;
    L_0x0338:
        r8 = r2;
        r9 = 5;
        if (r8 >= r9) goto L_0x0365;
    L_0x033c:
        r8 = DEBUG;
        if (r8 == 0) goto L_0x035d;
    L_0x0340:
        r8 = "FragmentManager";
        r9 = new java.lang.StringBuilder;
        r14 = r9;
        r9 = r14;
        r10 = r14;
        r10.<init>();
        r10 = "movefrom RESUMED: ";
        r9 = r9.append(r10);
        r10 = r1;
        r9 = r9.append(r10);
        r9 = r9.toString();
        r8 = android.util.Log.v(r8, r9);
    L_0x035d:
        r8 = r1;
        r8.performPause();
        r8 = r1;
        r9 = 0;
        r8.mResumed = r9;
    L_0x0365:
        r8 = r2;
        r9 = 4;
        if (r8 >= r9) goto L_0x038e;
    L_0x0369:
        r8 = DEBUG;
        if (r8 == 0) goto L_0x038a;
    L_0x036d:
        r8 = "FragmentManager";
        r9 = new java.lang.StringBuilder;
        r14 = r9;
        r9 = r14;
        r10 = r14;
        r10.<init>();
        r10 = "movefrom STARTED: ";
        r9 = r9.append(r10);
        r10 = r1;
        r9 = r9.append(r10);
        r9 = r9.toString();
        r8 = android.util.Log.v(r8, r9);
    L_0x038a:
        r8 = r1;
        r8.performStop();
    L_0x038e:
        r8 = r2;
        r9 = 3;
        if (r8 >= r9) goto L_0x03b7;
    L_0x0392:
        r8 = DEBUG;
        if (r8 == 0) goto L_0x03b3;
    L_0x0396:
        r8 = "FragmentManager";
        r9 = new java.lang.StringBuilder;
        r14 = r9;
        r9 = r14;
        r10 = r14;
        r10.<init>();
        r10 = "movefrom STOPPED: ";
        r9 = r9.append(r10);
        r10 = r1;
        r9 = r9.append(r10);
        r9 = r9.toString();
        r8 = android.util.Log.v(r8, r9);
    L_0x03b3:
        r8 = r1;
        r8.performReallyStop();
    L_0x03b7:
        r8 = r2;
        r9 = 2;
        if (r8 >= r9) goto L_0x0311;
    L_0x03bb:
        r8 = DEBUG;
        if (r8 == 0) goto L_0x03dc;
    L_0x03bf:
        r8 = "FragmentManager";
        r9 = new java.lang.StringBuilder;
        r14 = r9;
        r9 = r14;
        r10 = r14;
        r10.<init>();
        r10 = "movefrom ACTIVITY_CREATED: ";
        r9 = r9.append(r10);
        r10 = r1;
        r9 = r9.append(r10);
        r9 = r9.toString();
        r8 = android.util.Log.v(r8, r9);
    L_0x03dc:
        r8 = r1;
        r8 = r8.mView;
        if (r8 == 0) goto L_0x03f4;
    L_0x03e1:
        r8 = r0;
        r8 = r8.mActivity;
        r8 = r8.isFinishing();
        if (r8 != 0) goto L_0x03f4;
    L_0x03ea:
        r8 = r1;
        r8 = r8.mSavedViewState;
        if (r8 != 0) goto L_0x03f4;
    L_0x03ef:
        r8 = r0;
        r9 = r1;
        r8.saveFragmentViewState(r9);
    L_0x03f4:
        r8 = r1;
        r8.performDestroyView();
        r8 = r1;
        r8 = r8.mView;
        if (r8 == 0) goto L_0x0445;
    L_0x03fd:
        r8 = r1;
        r8 = r8.mContainer;
        if (r8 == 0) goto L_0x0445;
    L_0x0402:
        r8 = 0;
        r6 = r8;
        r8 = r0;
        r8 = r8.mCurState;
        if (r8 <= 0) goto L_0x0418;
    L_0x0409:
        r8 = r0;
        r8 = r8.mDestroyed;
        if (r8 != 0) goto L_0x0418;
    L_0x040e:
        r8 = r0;
        r9 = r1;
        r10 = r3;
        r11 = 0;
        r12 = r4;
        r8 = r8.loadAnimation(r9, r10, r11, r12);
        r6 = r8;
    L_0x0418:
        r8 = r6;
        if (r8 == 0) goto L_0x043c;
    L_0x041b:
        r8 = r1;
        r7 = r8;
        r8 = r1;
        r9 = r1;
        r9 = r9.mView;
        r8.mAnimatingAway = r9;
        r8 = r1;
        r9 = r2;
        r8.mStateAfterAnimating = r9;
        r8 = r6;
        r9 = new android.support.v4.app.FragmentManagerImpl$5;
        r14 = r9;
        r9 = r14;
        r10 = r14;
        r11 = r0;
        r12 = r7;
        r10.m54init(r11, r12);
        r8.setAnimationListener(r9);
        r8 = r1;
        r8 = r8.mView;
        r9 = r6;
        r8.startAnimation(r9);
    L_0x043c:
        r8 = r1;
        r8 = r8.mContainer;
        r9 = r1;
        r9 = r9.mView;
        r8.removeView(r9);
    L_0x0445:
        r8 = r1;
        r9 = 0;
        r8.mContainer = r9;
        r8 = r1;
        r9 = 0;
        r8.mView = r9;
        r8 = r1;
        r9 = 0;
        r8.mInnerView = r9;
        goto L_0x0311;
    L_0x0453:
        r8 = DEBUG;
        if (r8 == 0) goto L_0x0474;
    L_0x0457:
        r8 = "FragmentManager";
        r9 = new java.lang.StringBuilder;
        r14 = r9;
        r9 = r14;
        r10 = r14;
        r10.<init>();
        r10 = "movefrom CREATED: ";
        r9 = r9.append(r10);
        r10 = r1;
        r9 = r9.append(r10);
        r9 = r9.toString();
        r8 = android.util.Log.v(r8, r9);
    L_0x0474:
        r8 = r1;
        r8 = r8.mRetaining;
        if (r8 != 0) goto L_0x047d;
    L_0x0479:
        r8 = r1;
        r8.performDestroy();
    L_0x047d:
        r8 = r1;
        r9 = 0;
        r8.mCalled = r9;
        r8 = r1;
        r8.onDetach();
        r8 = r1;
        r8 = r8.mCalled;
        if (r8 != 0) goto L_0x04b0;
    L_0x048a:
        r8 = new android.support.v4.app.SuperNotCalledException;
        r14 = r8;
        r8 = r14;
        r9 = r14;
        r10 = new java.lang.StringBuilder;
        r14 = r10;
        r10 = r14;
        r11 = r14;
        r11.<init>();
        r11 = "Fragment ";
        r10 = r10.append(r11);
        r11 = r1;
        r10 = r10.append(r11);
        r11 = " did not call through to super.onDetach()";
        r10 = r10.append(r11);
        r10 = r10.toString();
        r9.m118init(r10);
        throw r8;
    L_0x04b0:
        r8 = r5;
        if (r8 != 0) goto L_0x0066;
    L_0x04b3:
        r8 = r1;
        r8 = r8.mRetaining;
        if (r8 != 0) goto L_0x04bf;
    L_0x04b8:
        r8 = r0;
        r9 = r1;
        r8.makeInactive(r9);
        goto L_0x0066;
    L_0x04bf:
        r8 = r1;
        r9 = 0;
        r8.mActivity = r9;
        r8 = r1;
        r9 = 0;
        r8.mFragmentManager = r9;
        goto L_0x0066;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.app.FragmentManagerImpl.moveToState(android.support.v4.app.Fragment, int, int, int, boolean):void");
    }

    /* access modifiers changed from: 0000 */
    public void moveToState(Fragment fragment) {
        moveToState(fragment, this.mCurState, 0, 0, HONEYCOMB);
    }

    /* access modifiers changed from: 0000 */
    public void moveToState(int i, boolean z) {
        moveToState(i, 0, 0, z);
    }

    /* access modifiers changed from: 0000 */
    public void moveToState(int i, int i2, int i3, boolean z) {
        int i4 = i;
        int i5 = i2;
        int i6 = i3;
        boolean z2 = z;
        if (this.mActivity == null && i4 != 0) {
            IllegalStateException illegalStateException = r14;
            IllegalStateException illegalStateException2 = new IllegalStateException("No activity");
            throw illegalStateException;
        } else if (z2 || this.mCurState != i4) {
            this.mCurState = i4;
            if (this.mActive != null) {
                int i7 = 0;
                for (int i8 = 0; i8 < this.mActive.size(); i8++) {
                    Fragment fragment = (Fragment) this.mActive.get(i8);
                    if (fragment != null) {
                        moveToState(fragment, i4, i5, i6, HONEYCOMB);
                        if (fragment.mLoaderManager != null) {
                            i7 |= fragment.mLoaderManager.hasRunningLoaders();
                        }
                    }
                }
                if (i7 == 0) {
                    startPendingDeferredFragments();
                }
                if (this.mNeedMenuInvalidate && this.mActivity != null && this.mCurState == 5) {
                    this.mActivity.supportInvalidateOptionsMenu();
                    this.mNeedMenuInvalidate = HONEYCOMB;
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void startPendingDeferredFragments() {
        if (this.mActive != null) {
            for (int i = 0; i < this.mActive.size(); i++) {
                Fragment fragment = (Fragment) this.mActive.get(i);
                if (fragment != null) {
                    performPendingDeferredStart(fragment);
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void makeActive(Fragment fragment) {
        Fragment fragment2 = fragment;
        if (fragment2.mIndex < 0) {
            if (this.mAvailIndices == null || this.mAvailIndices.size() <= 0) {
                if (this.mActive == null) {
                    ArrayList arrayList = r6;
                    ArrayList arrayList2 = new ArrayList();
                    this.mActive = arrayList;
                }
                fragment2.setIndex(this.mActive.size(), this.mParent);
                boolean add = this.mActive.add(fragment2);
            } else {
                fragment2.setIndex(((Integer) this.mAvailIndices.remove(this.mAvailIndices.size() - 1)).intValue(), this.mParent);
                Object obj = this.mActive.set(fragment2.mIndex, fragment2);
            }
            if (DEBUG) {
                String str = TAG;
                StringBuilder stringBuilder = r6;
                StringBuilder stringBuilder2 = new StringBuilder();
                int v = Log.v(str, stringBuilder.append("Allocated fragment index ").append(fragment2).toString());
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void makeInactive(Fragment fragment) {
        Fragment fragment2 = fragment;
        if (fragment2.mIndex >= 0) {
            if (DEBUG) {
                String str = TAG;
                StringBuilder stringBuilder = r5;
                StringBuilder stringBuilder2 = new StringBuilder();
                int v = Log.v(str, stringBuilder.append("Freeing fragment index ").append(fragment2).toString());
            }
            Object obj = this.mActive.set(fragment2.mIndex, null);
            if (this.mAvailIndices == null) {
                ArrayList arrayList = r5;
                ArrayList arrayList2 = new ArrayList();
                this.mAvailIndices = arrayList;
            }
            boolean add = this.mAvailIndices.add(Integer.valueOf(fragment2.mIndex));
            this.mActivity.invalidateSupportFragment(fragment2.mWho);
            fragment2.initState();
        }
    }

    public void addFragment(Fragment fragment, boolean z) {
        StringBuilder stringBuilder;
        Fragment fragment2 = fragment;
        boolean z2 = z;
        if (this.mAdded == null) {
            ArrayList arrayList = r7;
            ArrayList arrayList2 = new ArrayList();
            this.mAdded = arrayList;
        }
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder2 = r7;
            stringBuilder = new StringBuilder();
            int v = Log.v(str, stringBuilder2.append("add: ").append(fragment2).toString());
        }
        makeActive(fragment2);
        if (!fragment2.mDetached) {
            if (this.mAdded.contains(fragment2)) {
                IllegalStateException illegalStateException = r7;
                stringBuilder = r7;
                StringBuilder stringBuilder3 = new StringBuilder();
                IllegalStateException illegalStateException2 = new IllegalStateException(stringBuilder.append("Fragment already added: ").append(fragment2).toString());
                throw illegalStateException;
            }
            boolean add = this.mAdded.add(fragment2);
            fragment2.mAdded = true;
            fragment2.mRemoving = HONEYCOMB;
            if (fragment2.mHasMenu && fragment2.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            if (z2) {
                moveToState(fragment2);
            }
        }
    }

    public void removeFragment(Fragment fragment, int i, int i2) {
        Fragment fragment2 = fragment;
        int i3 = i;
        int i4 = i2;
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = r11;
            StringBuilder stringBuilder2 = new StringBuilder();
            int v = Log.v(str, stringBuilder.append("remove: ").append(fragment2).append(" nesting=").append(fragment2.mBackStackNesting).toString());
        }
        Object obj = !fragment2.isInBackStack() ? 1 : null;
        if (!fragment2.mDetached || obj != null) {
            if (this.mAdded != null) {
                boolean remove = this.mAdded.remove(fragment2);
            }
            if (fragment2.mHasMenu && fragment2.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            fragment2.mAdded = HONEYCOMB;
            fragment2.mRemoving = true;
            moveToState(fragment2, obj != null ? 0 : 1, i3, i4, HONEYCOMB);
        }
    }

    public void hideFragment(Fragment fragment, int i, int i2) {
        Fragment fragment2 = fragment;
        int i3 = i;
        int i4 = i2;
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = r10;
            StringBuilder stringBuilder2 = new StringBuilder();
            int v = Log.v(str, stringBuilder.append("hide: ").append(fragment2).toString());
        }
        if (!fragment2.mHidden) {
            fragment2.mHidden = true;
            if (fragment2.mView != null) {
                Animation loadAnimation = loadAnimation(fragment2, i3, HONEYCOMB, i4);
                if (loadAnimation != null) {
                    fragment2.mView.startAnimation(loadAnimation);
                }
                fragment2.mView.setVisibility(8);
            }
            if (fragment2.mAdded && fragment2.mHasMenu && fragment2.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            fragment2.onHiddenChanged(true);
        }
    }

    public void showFragment(Fragment fragment, int i, int i2) {
        Fragment fragment2 = fragment;
        int i3 = i;
        int i4 = i2;
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = r10;
            StringBuilder stringBuilder2 = new StringBuilder();
            int v = Log.v(str, stringBuilder.append("show: ").append(fragment2).toString());
        }
        if (fragment2.mHidden) {
            fragment2.mHidden = HONEYCOMB;
            if (fragment2.mView != null) {
                Animation loadAnimation = loadAnimation(fragment2, i3, true, i4);
                if (loadAnimation != null) {
                    fragment2.mView.startAnimation(loadAnimation);
                }
                fragment2.mView.setVisibility(0);
            }
            if (fragment2.mAdded && fragment2.mHasMenu && fragment2.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            fragment2.onHiddenChanged(HONEYCOMB);
        }
    }

    public void detachFragment(Fragment fragment, int i, int i2) {
        String str;
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        int v;
        Fragment fragment2 = fragment;
        int i3 = i;
        int i4 = i2;
        if (DEBUG) {
            str = TAG;
            stringBuilder = r10;
            stringBuilder2 = new StringBuilder();
            v = Log.v(str, stringBuilder.append("detach: ").append(fragment2).toString());
        }
        if (!fragment2.mDetached) {
            fragment2.mDetached = true;
            if (fragment2.mAdded) {
                if (this.mAdded != null) {
                    if (DEBUG) {
                        str = TAG;
                        stringBuilder = r10;
                        stringBuilder2 = new StringBuilder();
                        v = Log.v(str, stringBuilder.append("remove from detach: ").append(fragment2).toString());
                    }
                    boolean remove = this.mAdded.remove(fragment2);
                }
                if (fragment2.mHasMenu && fragment2.mMenuVisible) {
                    this.mNeedMenuInvalidate = true;
                }
                fragment2.mAdded = HONEYCOMB;
                moveToState(fragment2, 1, i3, i4, HONEYCOMB);
            }
        }
    }

    public void attachFragment(Fragment fragment, int i, int i2) {
        String str;
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        int v;
        Fragment fragment2 = fragment;
        int i3 = i;
        int i4 = i2;
        if (DEBUG) {
            str = TAG;
            stringBuilder = r10;
            stringBuilder2 = new StringBuilder();
            v = Log.v(str, stringBuilder.append("attach: ").append(fragment2).toString());
        }
        if (fragment2.mDetached) {
            fragment2.mDetached = HONEYCOMB;
            if (!fragment2.mAdded) {
                if (this.mAdded == null) {
                    ArrayList arrayList = r10;
                    ArrayList arrayList2 = new ArrayList();
                    this.mAdded = arrayList;
                }
                if (this.mAdded.contains(fragment2)) {
                    IllegalStateException illegalStateException = r10;
                    stringBuilder2 = r10;
                    StringBuilder stringBuilder3 = new StringBuilder();
                    IllegalStateException illegalStateException2 = new IllegalStateException(stringBuilder2.append("Fragment already added: ").append(fragment2).toString());
                    throw illegalStateException;
                }
                if (DEBUG) {
                    str = TAG;
                    stringBuilder = r10;
                    stringBuilder2 = new StringBuilder();
                    v = Log.v(str, stringBuilder.append("add from attach: ").append(fragment2).toString());
                }
                boolean add = this.mAdded.add(fragment2);
                fragment2.mAdded = true;
                if (fragment2.mHasMenu && fragment2.mMenuVisible) {
                    this.mNeedMenuInvalidate = true;
                }
                moveToState(fragment2, this.mCurState, i3, i4, HONEYCOMB);
            }
        }
    }

    public Fragment findFragmentById(int i) {
        int size;
        Fragment fragment;
        int i2 = i;
        if (this.mAdded != null) {
            for (size = this.mAdded.size() - 1; size >= 0; size--) {
                fragment = (Fragment) this.mAdded.get(size);
                if (fragment != null && fragment.mFragmentId == i2) {
                    return fragment;
                }
            }
        }
        if (this.mActive != null) {
            for (size = this.mActive.size() - 1; size >= 0; size--) {
                fragment = (Fragment) this.mActive.get(size);
                if (fragment != null && fragment.mFragmentId == i2) {
                    return fragment;
                }
            }
        }
        return null;
    }

    public Fragment findFragmentByTag(String str) {
        int size;
        Fragment fragment;
        String str2 = str;
        if (!(this.mAdded == null || str2 == null)) {
            for (size = this.mAdded.size() - 1; size >= 0; size--) {
                fragment = (Fragment) this.mAdded.get(size);
                if (fragment != null && str2.equals(fragment.mTag)) {
                    return fragment;
                }
            }
        }
        if (!(this.mActive == null || str2 == null)) {
            for (size = this.mActive.size() - 1; size >= 0; size--) {
                fragment = (Fragment) this.mActive.get(size);
                if (fragment != null && str2.equals(fragment.mTag)) {
                    return fragment;
                }
            }
        }
        return null;
    }

    public Fragment findFragmentByWho(String str) {
        String str2 = str;
        if (!(this.mActive == null || str2 == null)) {
            for (int size = this.mActive.size() - 1; size >= 0; size--) {
                Fragment fragment = (Fragment) this.mActive.get(size);
                if (fragment != null) {
                    Fragment findFragmentByWho = fragment.findFragmentByWho(str2);
                    fragment = findFragmentByWho;
                    if (findFragmentByWho != null) {
                        return fragment;
                    }
                }
            }
        }
        return null;
    }

    private void checkStateLoss() {
        IllegalStateException illegalStateException;
        IllegalStateException illegalStateException2;
        if (this.mStateSaved) {
            illegalStateException = r5;
            illegalStateException2 = new IllegalStateException("Can not perform this action after onSaveInstanceState");
            throw illegalStateException;
        } else if (this.mNoTransactionsBecause != null) {
            illegalStateException = r5;
            StringBuilder stringBuilder = r5;
            StringBuilder stringBuilder2 = new StringBuilder();
            illegalStateException2 = new IllegalStateException(stringBuilder.append("Can not perform this action inside of ").append(this.mNoTransactionsBecause).toString());
            throw illegalStateException;
        }
    }

    public void enqueueAction(Runnable runnable, boolean z) {
        Runnable runnable2 = runnable;
        if (!z) {
            checkStateLoss();
        }
        synchronized (this) {
            try {
                if (this.mDestroyed || this.mActivity == null) {
                    IllegalStateException illegalStateException = r8;
                    IllegalStateException illegalStateException2 = new IllegalStateException("Activity has been destroyed");
                    throw illegalStateException;
                }
                if (this.mPendingActions == null) {
                    ArrayList arrayList = r8;
                    ArrayList arrayList2 = new ArrayList();
                    this.mPendingActions = arrayList;
                }
                boolean add = this.mPendingActions.add(runnable2);
                if (this.mPendingActions.size() == 1) {
                    this.mActivity.mHandler.removeCallbacks(this.mExecCommit);
                    add = this.mActivity.mHandler.post(this.mExecCommit);
                }
            } catch (Throwable th) {
                Throwable th2 = th;
                Throwable th3 = th2;
            }
        }
    }

    public int allocBackStackIndex(BackStackRecord backStackRecord) {
        FragmentManagerImpl fragmentManagerImpl;
        BackStackRecord backStackRecord2 = backStackRecord;
        synchronized (this) {
            try {
                int size;
                String str;
                StringBuilder stringBuilder;
                StringBuilder stringBuilder2;
                int v;
                if (this.mAvailBackStackIndices == null || this.mAvailBackStackIndices.size() <= 0) {
                    if (this.mBackStackIndices == null) {
                        fragmentManagerImpl = this;
                    }
                    size = this.mBackStackIndices.size();
                    if (DEBUG) {
                        str = TAG;
                        stringBuilder = r8;
                        stringBuilder2 = new StringBuilder();
                        v = Log.v(str, stringBuilder.append("Setting back stack index ").append(size).append(" to ").append(backStackRecord2).toString());
                    }
                    boolean add = this.mBackStackIndices.add(backStackRecord2);
                    v = size;
                    return v;
                }
                size = ((Integer) this.mAvailBackStackIndices.remove(this.mAvailBackStackIndices.size() - 1)).intValue();
                if (DEBUG) {
                    str = TAG;
                    stringBuilder = r8;
                    stringBuilder2 = new StringBuilder();
                    v = Log.v(str, stringBuilder.append("Adding back stack index ").append(size).append(" with ").append(backStackRecord2).toString());
                }
                Object obj = this.mBackStackIndices.set(size, backStackRecord2);
                v = size;
                return v;
            } finally {
                FragmentManagerImpl fragmentManagerImpl2 = fragmentManagerImpl;
                fragmentManagerImpl = fragmentManagerImpl2;
            }
        }
    }

    public void setBackStackIndex(int i, BackStackRecord backStackRecord) {
        int i2 = i;
        BackStackRecord backStackRecord2 = backStackRecord;
        synchronized (this) {
            FragmentManagerImpl fragmentManagerImpl;
            try {
                if (this.mBackStackIndices == null) {
                    fragmentManagerImpl = this;
                }
                int size = this.mBackStackIndices.size();
                String str;
                StringBuilder stringBuilder;
                StringBuilder stringBuilder2;
                int v;
                if (i2 < size) {
                    if (DEBUG) {
                        str = TAG;
                        stringBuilder = r9;
                        stringBuilder2 = new StringBuilder();
                        v = Log.v(str, stringBuilder.append("Setting back stack index ").append(i2).append(" to ").append(backStackRecord2).toString());
                    }
                    Object obj = this.mBackStackIndices.set(i2, backStackRecord2);
                } else {
                    boolean add;
                    while (size < i2) {
                        add = this.mBackStackIndices.add(null);
                        if (this.mAvailBackStackIndices == null) {
                            ArrayList arrayList = r9;
                            ArrayList arrayList2 = new ArrayList();
                            this.mAvailBackStackIndices = arrayList;
                        }
                        if (DEBUG) {
                            str = TAG;
                            stringBuilder = r9;
                            stringBuilder2 = new StringBuilder();
                            v = Log.v(str, stringBuilder.append("Adding available back stack index ").append(size).toString());
                        }
                        add = this.mAvailBackStackIndices.add(Integer.valueOf(size));
                        size++;
                    }
                    if (DEBUG) {
                        str = TAG;
                        stringBuilder = r9;
                        stringBuilder2 = new StringBuilder();
                        v = Log.v(str, stringBuilder.append("Adding back stack index ").append(i2).append(" with ").append(backStackRecord2).toString());
                    }
                    add = this.mBackStackIndices.add(backStackRecord2);
                }
            } finally {
                FragmentManagerImpl fragmentManagerImpl2 = fragmentManagerImpl;
                fragmentManagerImpl = fragmentManagerImpl2;
            }
        }
    }

    public void freeBackStackIndex(int i) {
        FragmentManagerImpl fragmentManagerImpl;
        int i2 = i;
        synchronized (this) {
            try {
                Object obj = this.mBackStackIndices.set(i2, null);
                if (this.mAvailBackStackIndices == null) {
                    fragmentManagerImpl = this;
                }
                if (DEBUG) {
                    String str = TAG;
                    StringBuilder stringBuilder = r7;
                    StringBuilder stringBuilder2 = new StringBuilder();
                    int v = Log.v(str, stringBuilder.append("Freeing back stack index ").append(i2).toString());
                }
                boolean add = this.mAvailBackStackIndices.add(Integer.valueOf(i2));
            } finally {
                FragmentManagerImpl fragmentManagerImpl2 = fragmentManagerImpl;
                fragmentManagerImpl = fragmentManagerImpl2;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void reportBackStackChanged() {
        if (this.mBackStackChangeListeners != null) {
            for (int i = 0; i < this.mBackStackChangeListeners.size(); i++) {
                ((OnBackStackChangedListener) this.mBackStackChangeListeners.get(i)).onBackStackChanged();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void addBackStackState(BackStackRecord backStackRecord) {
        BackStackRecord backStackRecord2 = backStackRecord;
        if (this.mBackStack == null) {
            ArrayList arrayList = r5;
            ArrayList arrayList2 = new ArrayList();
            this.mBackStack = arrayList;
        }
        boolean add = this.mBackStack.add(backStackRecord2);
        reportBackStackChanged();
    }

    /* access modifiers changed from: 0000 */
    public boolean popBackStackState(Handler handler, String str, int i, int i2) {
        Handler handler2 = handler;
        String str2 = str;
        int i3 = i;
        int i4 = i2;
        if (this.mBackStack == null) {
            return HONEYCOMB;
        }
        int size;
        if (str2 == null && i3 < 0 && (i4 & 1) == 0) {
            size = this.mBackStack.size() - 1;
            if (size < 0) {
                return HONEYCOMB;
            }
            ((BackStackRecord) this.mBackStack.remove(size)).popFromBackStack(true);
            reportBackStackChanged();
        } else {
            size = -1;
            if (str2 != null || i3 >= 0) {
                BackStackRecord backStackRecord;
                size = this.mBackStack.size() - 1;
                while (size >= 0) {
                    backStackRecord = (BackStackRecord) this.mBackStack.get(size);
                    if ((str2 != null && str2.equals(backStackRecord.getName())) || (i3 >= 0 && i3 == backStackRecord.mIndex)) {
                        break;
                    }
                    size--;
                }
                if (size < 0) {
                    return HONEYCOMB;
                }
                if ((i4 & 1) != 0) {
                    while (true) {
                        size--;
                        if (size < 0) {
                            break;
                        }
                        backStackRecord = (BackStackRecord) this.mBackStack.get(size);
                        if ((str2 == null || !str2.equals(backStackRecord.getName())) && (i3 < 0 || i3 != backStackRecord.mIndex)) {
                            break;
                        }
                    }
                }
            }
            if (size == this.mBackStack.size() - 1) {
                return HONEYCOMB;
            }
            int size2;
            ArrayList arrayList = r13;
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = arrayList;
            for (size2 = this.mBackStack.size() - 1; size2 > size; size2--) {
                boolean add = arrayList3.add(this.mBackStack.remove(size2));
            }
            size2 = arrayList3.size() - 1;
            int i5 = 0;
            while (i5 <= size2) {
                if (DEBUG) {
                    String str3 = TAG;
                    StringBuilder stringBuilder = r13;
                    StringBuilder stringBuilder2 = new StringBuilder();
                    int v = Log.v(str3, stringBuilder.append("Popping back stack state: ").append(arrayList3.get(i5)).toString());
                }
                ((BackStackRecord) arrayList3.get(i5)).popFromBackStack(i5 == size2 ? true : HONEYCOMB);
                i5++;
            }
            reportBackStackChanged();
        }
        return true;
    }

    /* access modifiers changed from: 0000 */
    public ArrayList<Fragment> retainNonConfig() {
        ArrayList<Fragment> arrayList = null;
        if (this.mActive != null) {
            for (int i = 0; i < this.mActive.size(); i++) {
                Fragment fragment = (Fragment) this.mActive.get(i);
                if (fragment != null && fragment.mRetainInstance) {
                    if (arrayList == null) {
                        ArrayList<Fragment> arrayList2 = r7;
                        ArrayList<Fragment> arrayList3 = new ArrayList();
                        arrayList = arrayList2;
                    }
                    boolean add = arrayList.add(fragment);
                    fragment.mRetaining = true;
                    fragment.mTargetIndex = fragment.mTarget != null ? fragment.mTarget.mIndex : -1;
                    if (DEBUG) {
                        String str = TAG;
                        StringBuilder stringBuilder = r7;
                        StringBuilder stringBuilder2 = new StringBuilder();
                        int v = Log.v(str, stringBuilder.append("retainNonConfig: keeping retained ").append(fragment).toString());
                    }
                }
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: 0000 */
    public void saveFragmentViewState(Fragment fragment) {
        Fragment fragment2 = fragment;
        if (fragment2.mInnerView != null) {
            if (this.mStateArray == null) {
                SparseArray sparseArray = r5;
                SparseArray sparseArray2 = new SparseArray();
                this.mStateArray = sparseArray;
            } else {
                this.mStateArray.clear();
            }
            fragment2.mInnerView.saveHierarchyState(this.mStateArray);
            if (this.mStateArray.size() > 0) {
                fragment2.mSavedViewState = this.mStateArray;
                this.mStateArray = null;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public Bundle saveFragmentBasicState(Fragment fragment) {
        Bundle bundle;
        Bundle bundle2;
        Fragment fragment2 = fragment;
        Bundle bundle3 = null;
        if (this.mStateBundle == null) {
            bundle = r6;
            Bundle bundle4 = new Bundle();
            this.mStateBundle = bundle;
        }
        fragment2.performSaveInstanceState(this.mStateBundle);
        if (!this.mStateBundle.isEmpty()) {
            bundle3 = this.mStateBundle;
            this.mStateBundle = null;
        }
        if (fragment2.mView != null) {
            saveFragmentViewState(fragment2);
        }
        if (fragment2.mSavedViewState != null) {
            if (bundle3 == null) {
                bundle2 = r6;
                bundle = new Bundle();
                bundle3 = bundle2;
            }
            bundle3.putSparseParcelableArray(VIEW_STATE_TAG, fragment2.mSavedViewState);
        }
        if (!fragment2.mUserVisibleHint) {
            if (bundle3 == null) {
                bundle2 = r6;
                bundle = new Bundle();
                bundle3 = bundle2;
            }
            bundle3.putBoolean(USER_VISIBLE_HINT_TAG, fragment2.mUserVisibleHint);
        }
        return bundle3;
    }

    /* access modifiers changed from: 0000 */
    public Parcelable saveAllState() {
        boolean execPendingActions = execPendingActions();
        if (HONEYCOMB) {
            this.mStateSaved = true;
        }
        if (this.mActive == null || this.mActive.size() <= 0) {
            return null;
        }
        RuntimeException runtimeException;
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        RuntimeException illegalStateException;
        String str;
        StringBuilder stringBuilder3;
        StringBuilder stringBuilder4;
        int v;
        int size = this.mActive.size();
        FragmentState[] fragmentStateArr = new FragmentState[size];
        Object obj = null;
        for (int i = 0; i < size; i++) {
            Fragment fragment = (Fragment) this.mActive.get(i);
            if (fragment != null) {
                if (fragment.mIndex < 0) {
                    runtimeException = r14;
                    stringBuilder = r14;
                    stringBuilder2 = new StringBuilder();
                    illegalStateException = new IllegalStateException(stringBuilder.append("Failure saving state: active ").append(fragment).append(" has cleared index: ").append(fragment.mIndex).toString());
                    throwException(runtimeException);
                }
                obj = 1;
                FragmentState fragmentState = r14;
                FragmentState fragmentState2 = new FragmentState(fragment);
                FragmentState fragmentState3 = fragmentState;
                fragmentStateArr[i] = fragmentState3;
                if (fragment.mState <= 0 || fragmentState3.mSavedFragmentState != null) {
                    fragmentState3.mSavedFragmentState = fragment.mSavedFragmentState;
                } else {
                    fragmentState3.mSavedFragmentState = saveFragmentBasicState(fragment);
                    if (fragment.mTarget != null) {
                        if (fragment.mTarget.mIndex < 0) {
                            runtimeException = r14;
                            stringBuilder = r14;
                            stringBuilder2 = new StringBuilder();
                            illegalStateException = new IllegalStateException(stringBuilder.append("Failure saving state: ").append(fragment).append(" has target not in fragment manager: ").append(fragment.mTarget).toString());
                            throwException(runtimeException);
                        }
                        if (fragmentState3.mSavedFragmentState == null) {
                            fragmentState = fragmentState3;
                            Bundle bundle = r14;
                            Bundle bundle2 = new Bundle();
                            fragmentState.mSavedFragmentState = bundle;
                        }
                        putFragment(fragmentState3.mSavedFragmentState, TARGET_STATE_TAG, fragment.mTarget);
                        if (fragment.mTargetRequestCode != 0) {
                            fragmentState3.mSavedFragmentState.putInt(TARGET_REQUEST_CODE_STATE_TAG, fragment.mTargetRequestCode);
                        }
                    }
                }
                if (DEBUG) {
                    str = TAG;
                    stringBuilder3 = r14;
                    stringBuilder4 = new StringBuilder();
                    v = Log.v(str, stringBuilder3.append("Saved state of ").append(fragment).append(": ").append(fragmentState3.mSavedFragmentState).toString());
                }
            }
        }
        if (obj == null) {
            if (DEBUG) {
                v = Log.v(TAG, "saveAllState: no fragments!");
            }
            return null;
        }
        int i2;
        int[] iArr = null;
        BackStackState[] backStackStateArr = null;
        if (this.mAdded != null) {
            size = this.mAdded.size();
            if (size > 0) {
                iArr = new int[size];
                for (i2 = 0; i2 < size; i2++) {
                    iArr[i2] = ((Fragment) this.mAdded.get(i2)).mIndex;
                    if (iArr[i2] < 0) {
                        runtimeException = r14;
                        stringBuilder = r14;
                        stringBuilder2 = new StringBuilder();
                        illegalStateException = new IllegalStateException(stringBuilder.append("Failure saving state: active ").append(this.mAdded.get(i2)).append(" has cleared index: ").append(iArr[i2]).toString());
                        throwException(runtimeException);
                    }
                    if (DEBUG) {
                        str = TAG;
                        stringBuilder3 = r14;
                        stringBuilder4 = new StringBuilder();
                        v = Log.v(str, stringBuilder3.append("saveAllState: adding fragment #").append(i2).append(": ").append(this.mAdded.get(i2)).toString());
                    }
                }
            }
        }
        if (this.mBackStack != null) {
            size = this.mBackStack.size();
            if (size > 0) {
                backStackStateArr = new BackStackState[size];
                for (i2 = 0; i2 < size; i2++) {
                    BackStackState[] backStackStateArr2 = backStackStateArr;
                    int i3 = i2;
                    BackStackState backStackState = r14;
                    BackStackState backStackState2 = new BackStackState(this, (BackStackRecord) this.mBackStack.get(i2));
                    backStackStateArr2[i3] = backStackState;
                    if (DEBUG) {
                        str = TAG;
                        stringBuilder3 = r14;
                        stringBuilder4 = new StringBuilder();
                        v = Log.v(str, stringBuilder3.append("saveAllState: adding back stack #").append(i2).append(": ").append(this.mBackStack.get(i2)).toString());
                    }
                }
            }
        }
        FragmentManagerState fragmentManagerState = r14;
        FragmentManagerState fragmentManagerState2 = new FragmentManagerState();
        FragmentManagerState fragmentManagerState3 = fragmentManagerState;
        fragmentManagerState3.mActive = fragmentStateArr;
        fragmentManagerState3.mAdded = iArr;
        fragmentManagerState3.mBackStack = backStackStateArr;
        return fragmentManagerState3;
    }

    /* access modifiers changed from: 0000 */
    public void restoreAllState(Parcelable parcelable, ArrayList<Fragment> arrayList) {
        Parcelable parcelable2 = parcelable;
        ArrayList<Fragment> arrayList2 = arrayList;
        if (parcelable2 != null) {
            FragmentManagerState fragmentManagerState = (FragmentManagerState) parcelable2;
            if (fragmentManagerState.mActive != null) {
                int i;
                Fragment fragment;
                String str;
                StringBuilder stringBuilder;
                StringBuilder stringBuilder2;
                int v;
                boolean add;
                if (arrayList2 != null) {
                    for (i = 0; i < arrayList2.size(); i++) {
                        fragment = (Fragment) arrayList2.get(i);
                        if (DEBUG) {
                            str = TAG;
                            stringBuilder = r14;
                            stringBuilder2 = new StringBuilder();
                            v = Log.v(str, stringBuilder.append("restoreAllState: re-attaching retained ").append(fragment).toString());
                        }
                        FragmentState fragmentState = fragmentManagerState.mActive[fragment.mIndex];
                        fragmentState.mInstance = fragment;
                        fragment.mSavedViewState = null;
                        fragment.mBackStackNesting = 0;
                        fragment.mInLayout = HONEYCOMB;
                        fragment.mAdded = HONEYCOMB;
                        fragment.mTarget = null;
                        if (fragmentState.mSavedFragmentState != null) {
                            fragmentState.mSavedFragmentState.setClassLoader(this.mActivity.getClassLoader());
                            fragment.mSavedViewState = fragmentState.mSavedFragmentState.getSparseParcelableArray(VIEW_STATE_TAG);
                        }
                    }
                }
                ArrayList arrayList3 = r14;
                ArrayList arrayList4 = new ArrayList(fragmentManagerState.mActive.length);
                this.mActive = arrayList3;
                if (this.mAvailIndices != null) {
                    this.mAvailIndices.clear();
                }
                for (i = 0; i < fragmentManagerState.mActive.length; i++) {
                    FragmentState fragmentState2 = fragmentManagerState.mActive[i];
                    if (fragmentState2 != null) {
                        Fragment instantiate = fragmentState2.instantiate(this.mActivity, this.mParent);
                        if (DEBUG) {
                            str = TAG;
                            stringBuilder = r14;
                            stringBuilder2 = new StringBuilder();
                            v = Log.v(str, stringBuilder.append("restoreAllState: active #").append(i).append(": ").append(instantiate).toString());
                        }
                        add = this.mActive.add(instantiate);
                        fragmentState2.mInstance = null;
                    } else {
                        add = this.mActive.add(null);
                        if (this.mAvailIndices == null) {
                            arrayList3 = r14;
                            arrayList4 = new ArrayList();
                            this.mAvailIndices = arrayList3;
                        }
                        if (DEBUG) {
                            str = TAG;
                            stringBuilder = r14;
                            stringBuilder2 = new StringBuilder();
                            v = Log.v(str, stringBuilder.append("restoreAllState: avail #").append(i).toString());
                        }
                        add = this.mAvailIndices.add(Integer.valueOf(i));
                    }
                }
                if (arrayList2 != null) {
                    for (i = 0; i < arrayList2.size(); i++) {
                        fragment = (Fragment) arrayList2.get(i);
                        if (fragment.mTargetIndex >= 0) {
                            if (fragment.mTargetIndex < this.mActive.size()) {
                                fragment.mTarget = (Fragment) this.mActive.get(fragment.mTargetIndex);
                            } else {
                                str = TAG;
                                stringBuilder = r14;
                                stringBuilder2 = new StringBuilder();
                                v = Log.w(str, stringBuilder.append("Re-attaching retained fragment ").append(fragment).append(" target no longer exists: ").append(fragment.mTargetIndex).toString());
                                fragment.mTarget = null;
                            }
                        }
                    }
                }
                if (fragmentManagerState.mAdded != null) {
                    arrayList3 = r14;
                    arrayList4 = new ArrayList(fragmentManagerState.mAdded.length);
                    this.mAdded = arrayList3;
                    for (i = 0; i < fragmentManagerState.mAdded.length; i++) {
                        fragment = (Fragment) this.mActive.get(fragmentManagerState.mAdded[i]);
                        if (fragment == null) {
                            RuntimeException runtimeException = r14;
                            StringBuilder stringBuilder3 = r14;
                            StringBuilder stringBuilder4 = new StringBuilder();
                            RuntimeException illegalStateException = new IllegalStateException(stringBuilder3.append("No instantiated fragment for index #").append(fragmentManagerState.mAdded[i]).toString());
                            throwException(runtimeException);
                        }
                        fragment.mAdded = true;
                        if (DEBUG) {
                            str = TAG;
                            stringBuilder = r14;
                            stringBuilder2 = new StringBuilder();
                            v = Log.v(str, stringBuilder.append("restoreAllState: added #").append(i).append(": ").append(fragment).toString());
                        }
                        if (this.mAdded.contains(fragment)) {
                            IllegalStateException illegalStateException2 = r14;
                            IllegalStateException illegalStateException3 = new IllegalStateException("Already added!");
                            throw illegalStateException2;
                        }
                        add = this.mAdded.add(fragment);
                    }
                } else {
                    this.mAdded = null;
                }
                if (fragmentManagerState.mBackStack != null) {
                    arrayList3 = r14;
                    arrayList4 = new ArrayList(fragmentManagerState.mBackStack.length);
                    this.mBackStack = arrayList3;
                    for (i = 0; i < fragmentManagerState.mBackStack.length; i++) {
                        BackStackRecord instantiate2 = fragmentManagerState.mBackStack[i].instantiate(this);
                        if (DEBUG) {
                            str = TAG;
                            stringBuilder = r14;
                            stringBuilder2 = new StringBuilder();
                            v = Log.v(str, stringBuilder.append("restoreAllState: back stack #").append(i).append(" (index ").append(instantiate2.mIndex).append("): ").append(instantiate2).toString());
                            Writer writer = r14;
                            Writer logWriter = new LogWriter(TAG);
                            Writer writer2 = writer;
                            PrintWriter printWriter = r14;
                            PrintWriter printWriter2 = new PrintWriter(writer2);
                            instantiate2.dump("  ", printWriter, HONEYCOMB);
                        }
                        add = this.mBackStack.add(instantiate2);
                        if (instantiate2.mIndex >= 0) {
                            setBackStackIndex(instantiate2.mIndex, instantiate2);
                        }
                    }
                    return;
                }
                this.mBackStack = null;
            }
        }
    }

    public void attachActivity(FragmentActivity fragmentActivity, FragmentContainer fragmentContainer, Fragment fragment) {
        FragmentActivity fragmentActivity2 = fragmentActivity;
        FragmentContainer fragmentContainer2 = fragmentContainer;
        Fragment fragment2 = fragment;
        if (this.mActivity != null) {
            IllegalStateException illegalStateException = r7;
            IllegalStateException illegalStateException2 = new IllegalStateException("Already attached");
            throw illegalStateException;
        }
        this.mActivity = fragmentActivity2;
        this.mContainer = fragmentContainer2;
        this.mParent = fragment2;
    }

    public void noteStateNotSaved() {
        this.mStateSaved = HONEYCOMB;
    }

    public void dispatchCreate() {
        this.mStateSaved = HONEYCOMB;
        moveToState(1, HONEYCOMB);
    }

    public void dispatchActivityCreated() {
        this.mStateSaved = HONEYCOMB;
        moveToState(2, HONEYCOMB);
    }

    public void dispatchStart() {
        this.mStateSaved = HONEYCOMB;
        moveToState(4, HONEYCOMB);
    }

    public void dispatchResume() {
        this.mStateSaved = HONEYCOMB;
        moveToState(5, HONEYCOMB);
    }

    public void dispatchPause() {
        moveToState(4, HONEYCOMB);
    }

    public void dispatchStop() {
        this.mStateSaved = true;
        moveToState(3, HONEYCOMB);
    }

    public void dispatchReallyStop() {
        moveToState(2, HONEYCOMB);
    }

    public void dispatchDestroyView() {
        moveToState(1, HONEYCOMB);
    }

    public void dispatchDestroy() {
        this.mDestroyed = true;
        boolean execPendingActions = execPendingActions();
        moveToState(0, HONEYCOMB);
        this.mActivity = null;
        this.mContainer = null;
        this.mParent = null;
    }

    public void dispatchConfigurationChanged(Configuration configuration) {
        Configuration configuration2 = configuration;
        if (this.mAdded != null) {
            for (int i = 0; i < this.mAdded.size(); i++) {
                Fragment fragment = (Fragment) this.mAdded.get(i);
                if (fragment != null) {
                    fragment.performConfigurationChanged(configuration2);
                }
            }
        }
    }

    public void dispatchLowMemory() {
        if (this.mAdded != null) {
            for (int i = 0; i < this.mAdded.size(); i++) {
                Fragment fragment = (Fragment) this.mAdded.get(i);
                if (fragment != null) {
                    fragment.performLowMemory();
                }
            }
        }
    }

    public boolean dispatchCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        int i;
        Fragment fragment;
        Menu menu2 = menu;
        MenuInflater menuInflater2 = menuInflater;
        boolean z = false;
        ArrayList arrayList = null;
        if (this.mAdded != null) {
            for (i = 0; i < this.mAdded.size(); i++) {
                fragment = (Fragment) this.mAdded.get(i);
                if (fragment != null && fragment.performCreateOptionsMenu(menu2, menuInflater2)) {
                    z = true;
                    if (arrayList == null) {
                        ArrayList arrayList2 = r10;
                        ArrayList arrayList3 = new ArrayList();
                        arrayList = arrayList2;
                    }
                    boolean add = arrayList.add(fragment);
                }
            }
        }
        if (this.mCreatedMenus != null) {
            for (i = 0; i < this.mCreatedMenus.size(); i++) {
                fragment = (Fragment) this.mCreatedMenus.get(i);
                if (arrayList == null || !arrayList.contains(fragment)) {
                    fragment.onDestroyOptionsMenu();
                }
            }
        }
        this.mCreatedMenus = arrayList;
        return z;
    }

    public boolean dispatchPrepareOptionsMenu(Menu menu) {
        Menu menu2 = menu;
        boolean z = false;
        if (this.mAdded != null) {
            for (int i = 0; i < this.mAdded.size(); i++) {
                Fragment fragment = (Fragment) this.mAdded.get(i);
                if (fragment != null && fragment.performPrepareOptionsMenu(menu2)) {
                    z = true;
                }
            }
        }
        return z;
    }

    public boolean dispatchOptionsItemSelected(MenuItem menuItem) {
        MenuItem menuItem2 = menuItem;
        if (this.mAdded != null) {
            for (int i = 0; i < this.mAdded.size(); i++) {
                Fragment fragment = (Fragment) this.mAdded.get(i);
                if (fragment != null && fragment.performOptionsItemSelected(menuItem2)) {
                    return true;
                }
            }
        }
        return HONEYCOMB;
    }

    public boolean dispatchContextItemSelected(MenuItem menuItem) {
        MenuItem menuItem2 = menuItem;
        if (this.mAdded != null) {
            for (int i = 0; i < this.mAdded.size(); i++) {
                Fragment fragment = (Fragment) this.mAdded.get(i);
                if (fragment != null && fragment.performContextItemSelected(menuItem2)) {
                    return true;
                }
            }
        }
        return HONEYCOMB;
    }

    public void dispatchOptionsMenuClosed(Menu menu) {
        Menu menu2 = menu;
        if (this.mAdded != null) {
            for (int i = 0; i < this.mAdded.size(); i++) {
                Fragment fragment = (Fragment) this.mAdded.get(i);
                if (fragment != null) {
                    fragment.performOptionsMenuClosed(menu2);
                }
            }
        }
    }

    public static int reverseTransit(int i) {
        int i2 = 0;
        switch (i) {
            case FragmentTransaction.TRANSIT_FRAGMENT_OPEN /*4097*/:
                i2 = 8194;
                break;
            case FragmentTransaction.TRANSIT_FRAGMENT_FADE /*4099*/:
                i2 = 4099;
                break;
            case FragmentTransaction.TRANSIT_FRAGMENT_CLOSE /*8194*/:
                i2 = 4097;
                break;
        }
        return i2;
    }

    public static int transitToStyleIndex(int i, boolean z) {
        boolean z2 = z;
        int i2 = -1;
        switch (i) {
            case FragmentTransaction.TRANSIT_FRAGMENT_OPEN /*4097*/:
                i2 = z2 ? 1 : 2;
                break;
            case FragmentTransaction.TRANSIT_FRAGMENT_FADE /*4099*/:
                i2 = z2 ? 5 : 6;
                break;
            case FragmentTransaction.TRANSIT_FRAGMENT_CLOSE /*8194*/:
                i2 = z2 ? 3 : 4;
                break;
        }
        return i2;
    }
}
