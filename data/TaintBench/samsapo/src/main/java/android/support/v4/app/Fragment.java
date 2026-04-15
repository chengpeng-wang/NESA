package android.support.v4.app;

import android.app.Activity;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.v4.util.DebugUtils;
import android.support.v4.util.SimpleArrayMap;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class Fragment implements ComponentCallbacks, OnCreateContextMenuListener {
    static final int ACTIVITY_CREATED = 2;
    static final int CREATED = 1;
    static final int INITIALIZING = 0;
    static final int RESUMED = 5;
    static final int STARTED = 4;
    static final int STOPPED = 3;
    private static final SimpleArrayMap<String, Class<?>> sClassMap;
    FragmentActivity mActivity;
    boolean mAdded;
    View mAnimatingAway;
    Bundle mArguments;
    int mBackStackNesting;
    boolean mCalled;
    boolean mCheckedForLoaderManager;
    FragmentManagerImpl mChildFragmentManager;
    ViewGroup mContainer;
    int mContainerId;
    boolean mDeferStart;
    boolean mDetached;
    int mFragmentId;
    FragmentManagerImpl mFragmentManager;
    boolean mFromLayout;
    boolean mHasMenu;
    boolean mHidden;
    boolean mInLayout;
    int mIndex = -1;
    View mInnerView;
    LoaderManagerImpl mLoaderManager;
    boolean mLoadersStarted;
    boolean mMenuVisible = true;
    int mNextAnim;
    Fragment mParentFragment;
    boolean mRemoving;
    boolean mRestored;
    boolean mResumed;
    boolean mRetainInstance;
    boolean mRetaining;
    Bundle mSavedFragmentState;
    SparseArray<Parcelable> mSavedViewState;
    int mState = 0;
    int mStateAfterAnimating;
    String mTag;
    Fragment mTarget;
    int mTargetIndex = -1;
    int mTargetRequestCode;
    boolean mUserVisibleHint = true;
    View mView;
    String mWho;

    public static class InstantiationException extends RuntimeException {
        public InstantiationException(String str, Exception exception) {
            super(str, exception);
        }
    }

    public static class SavedState implements Parcelable {
        public static final Creator<SavedState> CREATOR;
        final Bundle mState;

        SavedState(Bundle bundle) {
            this.mState = bundle;
        }

        SavedState(Parcel parcel, ClassLoader classLoader) {
            ClassLoader classLoader2 = classLoader;
            this.mState = parcel.readBundle();
            if (classLoader2 != null && this.mState != null) {
                this.mState.setClassLoader(classLoader2);
            }
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel parcel, int i) {
            int i2 = i;
            parcel.writeBundle(this.mState);
        }

        static {
            AnonymousClass1 anonymousClass1 = r2;
            AnonymousClass1 anonymousClass12 = new Creator<SavedState>() {
                public SavedState createFromParcel(Parcel parcel) {
                    SavedState savedState = r6;
                    SavedState savedState2 = new SavedState(parcel, null);
                    return savedState;
                }

                public SavedState[] newArray(int i) {
                    return new SavedState[i];
                }
            };
            CREATOR = anonymousClass1;
        }
    }

    static {
        SimpleArrayMap simpleArrayMap = r2;
        SimpleArrayMap simpleArrayMap2 = new SimpleArrayMap();
        sClassMap = simpleArrayMap;
    }

    public Fragment() {
    }

    public static Fragment instantiate(Context context, String str) {
        return instantiate(context, str, null);
    }

    public static Fragment instantiate(Context context, String str, Bundle bundle) {
        Exception exception;
        InstantiationException instantiationException;
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        InstantiationException instantiationException2;
        Context context2 = context;
        String str2 = str;
        Bundle bundle2 = bundle;
        try {
            Class cls = (Class) sClassMap.get(str2);
            if (cls == null) {
                cls = context2.getClassLoader().loadClass(str2);
                Object put = sClassMap.put(str2, cls);
            }
            Fragment fragment = (Fragment) cls.newInstance();
            if (bundle2 != null) {
                bundle2.setClassLoader(fragment.getClass().getClassLoader());
                fragment.mArguments = bundle2;
            }
            return fragment;
        } catch (ClassNotFoundException e) {
            exception = e;
            instantiationException = r9;
            stringBuilder = r9;
            stringBuilder2 = new StringBuilder();
            instantiationException2 = new InstantiationException(stringBuilder.append("Unable to instantiate fragment ").append(str2).append(": make sure class name exists, is public, and has an").append(" empty constructor that is public").toString(), exception);
            throw instantiationException;
        } catch (InstantiationException e2) {
            exception = e2;
            instantiationException = r9;
            stringBuilder = r9;
            stringBuilder2 = new StringBuilder();
            instantiationException2 = new InstantiationException(stringBuilder.append("Unable to instantiate fragment ").append(str2).append(": make sure class name exists, is public, and has an").append(" empty constructor that is public").toString(), exception);
            throw instantiationException;
        } catch (IllegalAccessException e22) {
            exception = e22;
            instantiationException = r9;
            stringBuilder = r9;
            stringBuilder2 = new StringBuilder();
            instantiationException2 = new InstantiationException(stringBuilder.append("Unable to instantiate fragment ").append(str2).append(": make sure class name exists, is public, and has an").append(" empty constructor that is public").toString(), exception);
            throw instantiationException;
        }
    }

    static boolean isSupportFragmentClass(Context context, String str) {
        Context context2 = context;
        String str2 = str;
        try {
            Class cls = (Class) sClassMap.get(str2);
            if (cls == null) {
                cls = context2.getClassLoader().loadClass(str2);
                Object put = sClassMap.put(str2, cls);
            }
            return Fragment.class.isAssignableFrom(cls);
        } catch (ClassNotFoundException e) {
            ClassNotFoundException classNotFoundException = e;
            return false;
        }
    }

    /* access modifiers changed from: final */
    public final void restoreViewState(Bundle bundle) {
        Bundle bundle2 = bundle;
        if (this.mSavedViewState != null) {
            this.mInnerView.restoreHierarchyState(this.mSavedViewState);
            this.mSavedViewState = null;
        }
        this.mCalled = false;
        onViewStateRestored(bundle2);
        if (!this.mCalled) {
            SuperNotCalledException superNotCalledException = r6;
            StringBuilder stringBuilder = r6;
            StringBuilder stringBuilder2 = new StringBuilder();
            SuperNotCalledException superNotCalledException2 = new SuperNotCalledException(stringBuilder.append("Fragment ").append(this).append(" did not call through to super.onViewStateRestored()").toString());
            throw superNotCalledException;
        }
    }

    /* access modifiers changed from: final */
    public final void setIndex(int i, Fragment fragment) {
        Fragment fragment2 = fragment;
        this.mIndex = i;
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        if (fragment2 != null) {
            stringBuilder = r6;
            stringBuilder2 = new StringBuilder();
            this.mWho = stringBuilder.append(fragment2.mWho).append(":").append(this.mIndex).toString();
            return;
        }
        stringBuilder = r6;
        stringBuilder2 = new StringBuilder();
        this.mWho = stringBuilder.append("android:fragment:").append(this.mIndex).toString();
    }

    /* access modifiers changed from: final */
    public final boolean isInBackStack() {
        return this.mBackStackNesting > 0;
    }

    public final boolean equals(Object obj) {
        return super.equals(obj);
    }

    public final int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        StringBuilder stringBuilder = r5;
        StringBuilder stringBuilder2 = new StringBuilder(128);
        StringBuilder stringBuilder3 = stringBuilder;
        DebugUtils.buildShortClassTag(this, stringBuilder3);
        if (this.mIndex >= 0) {
            stringBuilder = stringBuilder3.append(" #");
            stringBuilder = stringBuilder3.append(this.mIndex);
        }
        if (this.mFragmentId != 0) {
            stringBuilder = stringBuilder3.append(" id=0x");
            stringBuilder = stringBuilder3.append(Integer.toHexString(this.mFragmentId));
        }
        if (this.mTag != null) {
            stringBuilder = stringBuilder3.append(" ");
            stringBuilder = stringBuilder3.append(this.mTag);
        }
        stringBuilder = stringBuilder3.append('}');
        return stringBuilder3.toString();
    }

    public final int getId() {
        return this.mFragmentId;
    }

    public final String getTag() {
        return this.mTag;
    }

    public void setArguments(Bundle bundle) {
        Bundle bundle2 = bundle;
        if (this.mIndex >= 0) {
            IllegalStateException illegalStateException = r5;
            IllegalStateException illegalStateException2 = new IllegalStateException("Fragment already active");
            throw illegalStateException;
        }
        this.mArguments = bundle2;
    }

    public final Bundle getArguments() {
        return this.mArguments;
    }

    public void setInitialSavedState(SavedState savedState) {
        SavedState savedState2 = savedState;
        if (this.mIndex >= 0) {
            IllegalStateException illegalStateException = r5;
            IllegalStateException illegalStateException2 = new IllegalStateException("Fragment already active");
            throw illegalStateException;
        }
        Bundle bundle = (savedState2 == null || savedState2.mState == null) ? null : savedState2.mState;
        this.mSavedFragmentState = bundle;
    }

    public void setTargetFragment(Fragment fragment, int i) {
        int i2 = i;
        this.mTarget = fragment;
        this.mTargetRequestCode = i2;
    }

    public final Fragment getTargetFragment() {
        return this.mTarget;
    }

    public final int getTargetRequestCode() {
        return this.mTargetRequestCode;
    }

    public final FragmentActivity getActivity() {
        return this.mActivity;
    }

    public final Resources getResources() {
        if (this.mActivity != null) {
            return this.mActivity.getResources();
        }
        IllegalStateException illegalStateException = r5;
        StringBuilder stringBuilder = r5;
        StringBuilder stringBuilder2 = new StringBuilder();
        IllegalStateException illegalStateException2 = new IllegalStateException(stringBuilder.append("Fragment ").append(this).append(" not attached to Activity").toString());
        throw illegalStateException;
    }

    public final CharSequence getText(int i) {
        return getResources().getText(i);
    }

    public final String getString(int i) {
        return getResources().getString(i);
    }

    public final String getString(int i, Object... objArr) {
        return getResources().getString(i, objArr);
    }

    public final FragmentManager getFragmentManager() {
        return this.mFragmentManager;
    }

    public final FragmentManager getChildFragmentManager() {
        if (this.mChildFragmentManager == null) {
            instantiateChildFragmentManager();
            if (this.mState >= 5) {
                this.mChildFragmentManager.dispatchResume();
            } else if (this.mState >= 4) {
                this.mChildFragmentManager.dispatchStart();
            } else if (this.mState >= 2) {
                this.mChildFragmentManager.dispatchActivityCreated();
            } else if (this.mState >= 1) {
                this.mChildFragmentManager.dispatchCreate();
            }
        }
        return this.mChildFragmentManager;
    }

    public final Fragment getParentFragment() {
        return this.mParentFragment;
    }

    public final boolean isAdded() {
        boolean z = this.mActivity != null && this.mAdded;
        return z;
    }

    public final boolean isDetached() {
        return this.mDetached;
    }

    public final boolean isRemoving() {
        return this.mRemoving;
    }

    public final boolean isInLayout() {
        return this.mInLayout;
    }

    public final boolean isResumed() {
        return this.mResumed;
    }

    public final boolean isVisible() {
        boolean z = (!isAdded() || isHidden() || this.mView == null || this.mView.getWindowToken() == null || this.mView.getVisibility() != 0) ? false : true;
        return z;
    }

    public final boolean isHidden() {
        return this.mHidden;
    }

    public final boolean hasOptionsMenu() {
        return this.mHasMenu;
    }

    public final boolean isMenuVisible() {
        return this.mMenuVisible;
    }

    public void onHiddenChanged(boolean z) {
    }

    public void setRetainInstance(boolean z) {
        boolean z2 = z;
        if (!z2 || this.mParentFragment == null) {
            this.mRetainInstance = z2;
            return;
        }
        IllegalStateException illegalStateException = r5;
        IllegalStateException illegalStateException2 = new IllegalStateException("Can't retain fragements that are nested in other fragments");
        throw illegalStateException;
    }

    public final boolean getRetainInstance() {
        return this.mRetainInstance;
    }

    public void setHasOptionsMenu(boolean z) {
        boolean z2 = z;
        if (this.mHasMenu != z2) {
            this.mHasMenu = z2;
            if (isAdded() && !isHidden()) {
                this.mActivity.supportInvalidateOptionsMenu();
            }
        }
    }

    public void setMenuVisibility(boolean z) {
        boolean z2 = z;
        if (this.mMenuVisible != z2) {
            this.mMenuVisible = z2;
            if (this.mHasMenu && isAdded() && !isHidden()) {
                this.mActivity.supportInvalidateOptionsMenu();
            }
        }
    }

    public void setUserVisibleHint(boolean z) {
        boolean z2 = z;
        if (!this.mUserVisibleHint && z2 && this.mState < 4) {
            this.mFragmentManager.performPendingDeferredStart(this);
        }
        this.mUserVisibleHint = z2;
        this.mDeferStart = !z2;
    }

    public boolean getUserVisibleHint() {
        return this.mUserVisibleHint;
    }

    public LoaderManager getLoaderManager() {
        if (this.mLoaderManager != null) {
            return this.mLoaderManager;
        }
        if (this.mActivity == null) {
            IllegalStateException illegalStateException = r6;
            StringBuilder stringBuilder = r6;
            StringBuilder stringBuilder2 = new StringBuilder();
            IllegalStateException illegalStateException2 = new IllegalStateException(stringBuilder.append("Fragment ").append(this).append(" not attached to Activity").toString());
            throw illegalStateException;
        }
        this.mCheckedForLoaderManager = true;
        this.mLoaderManager = this.mActivity.getLoaderManager(this.mWho, this.mLoadersStarted, true);
        return this.mLoaderManager;
    }

    public void startActivity(Intent intent) {
        Intent intent2 = intent;
        if (this.mActivity == null) {
            IllegalStateException illegalStateException = r6;
            StringBuilder stringBuilder = r6;
            StringBuilder stringBuilder2 = new StringBuilder();
            IllegalStateException illegalStateException2 = new IllegalStateException(stringBuilder.append("Fragment ").append(this).append(" not attached to Activity").toString());
            throw illegalStateException;
        }
        this.mActivity.startActivityFromFragment(this, intent2, -1);
    }

    public void startActivityForResult(Intent intent, int i) {
        Intent intent2 = intent;
        int i2 = i;
        if (this.mActivity == null) {
            IllegalStateException illegalStateException = r7;
            StringBuilder stringBuilder = r7;
            StringBuilder stringBuilder2 = new StringBuilder();
            IllegalStateException illegalStateException2 = new IllegalStateException(stringBuilder.append("Fragment ").append(this).append(" not attached to Activity").toString());
            throw illegalStateException;
        }
        this.mActivity.startActivityFromFragment(this, intent2, i2);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
    }

    public LayoutInflater getLayoutInflater(Bundle bundle) {
        Bundle bundle2 = bundle;
        return this.mActivity.getLayoutInflater();
    }

    public void onInflate(Activity activity, AttributeSet attributeSet, Bundle bundle) {
        Activity activity2 = activity;
        AttributeSet attributeSet2 = attributeSet;
        Bundle bundle2 = bundle;
        this.mCalled = true;
    }

    public void onAttach(Activity activity) {
        Activity activity2 = activity;
        this.mCalled = true;
    }

    public Animation onCreateAnimation(int i, boolean z, int i2) {
        int i3 = i;
        boolean z2 = z;
        int i4 = i2;
        return null;
    }

    public void onCreate(Bundle bundle) {
        Bundle bundle2 = bundle;
        this.mCalled = true;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        LayoutInflater layoutInflater2 = layoutInflater;
        ViewGroup viewGroup2 = viewGroup;
        Bundle bundle2 = bundle;
        return null;
    }

    public void onViewCreated(View view, Bundle bundle) {
    }

    public View getView() {
        return this.mView;
    }

    public void onActivityCreated(Bundle bundle) {
        Bundle bundle2 = bundle;
        this.mCalled = true;
    }

    public void onViewStateRestored(Bundle bundle) {
        Bundle bundle2 = bundle;
        this.mCalled = true;
    }

    public void onStart() {
        this.mCalled = true;
        if (!this.mLoadersStarted) {
            this.mLoadersStarted = true;
            if (!this.mCheckedForLoaderManager) {
                this.mCheckedForLoaderManager = true;
                this.mLoaderManager = this.mActivity.getLoaderManager(this.mWho, this.mLoadersStarted, false);
            }
            if (this.mLoaderManager != null) {
                this.mLoaderManager.doStart();
            }
        }
    }

    public void onResume() {
        this.mCalled = true;
    }

    public void onSaveInstanceState(Bundle bundle) {
    }

    public void onConfigurationChanged(Configuration configuration) {
        Configuration configuration2 = configuration;
        this.mCalled = true;
    }

    public void onPause() {
        this.mCalled = true;
    }

    public void onStop() {
        this.mCalled = true;
    }

    public void onLowMemory() {
        this.mCalled = true;
    }

    public void onDestroyView() {
        this.mCalled = true;
    }

    public void onDestroy() {
        this.mCalled = true;
        if (!this.mCheckedForLoaderManager) {
            this.mCheckedForLoaderManager = true;
            this.mLoaderManager = this.mActivity.getLoaderManager(this.mWho, this.mLoadersStarted, false);
        }
        if (this.mLoaderManager != null) {
            this.mLoaderManager.doDestroy();
        }
    }

    /* access modifiers changed from: 0000 */
    public void initState() {
        this.mIndex = -1;
        this.mWho = null;
        this.mAdded = false;
        this.mRemoving = false;
        this.mResumed = false;
        this.mFromLayout = false;
        this.mInLayout = false;
        this.mRestored = false;
        this.mBackStackNesting = 0;
        this.mFragmentManager = null;
        this.mActivity = null;
        this.mFragmentId = 0;
        this.mContainerId = 0;
        this.mTag = null;
        this.mHidden = false;
        this.mDetached = false;
        this.mRetaining = false;
        this.mLoaderManager = null;
        this.mLoadersStarted = false;
        this.mCheckedForLoaderManager = false;
    }

    public void onDetach() {
        this.mCalled = true;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
    }

    public void onPrepareOptionsMenu(Menu menu) {
    }

    public void onDestroyOptionsMenu() {
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        MenuItem menuItem2 = menuItem;
        return false;
    }

    public void onOptionsMenuClosed(Menu menu) {
    }

    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenuInfo contextMenuInfo) {
        getActivity().onCreateContextMenu(contextMenu, view, contextMenuInfo);
    }

    public void registerForContextMenu(View view) {
        view.setOnCreateContextMenuListener(this);
    }

    public void unregisterForContextMenu(View view) {
        view.setOnCreateContextMenuListener(null);
    }

    public boolean onContextItemSelected(MenuItem menuItem) {
        MenuItem menuItem2 = menuItem;
        return false;
    }

    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        String str2 = str;
        FileDescriptor fileDescriptor2 = fileDescriptor;
        PrintWriter printWriter2 = printWriter;
        String[] strArr2 = strArr;
        printWriter2.print(str2);
        printWriter2.print("mFragmentId=#");
        printWriter2.print(Integer.toHexString(this.mFragmentId));
        printWriter2.print(" mContainerId=#");
        printWriter2.print(Integer.toHexString(this.mContainerId));
        printWriter2.print(" mTag=");
        printWriter2.println(this.mTag);
        printWriter2.print(str2);
        printWriter2.print("mState=");
        printWriter2.print(this.mState);
        printWriter2.print(" mIndex=");
        printWriter2.print(this.mIndex);
        printWriter2.print(" mWho=");
        printWriter2.print(this.mWho);
        printWriter2.print(" mBackStackNesting=");
        printWriter2.println(this.mBackStackNesting);
        printWriter2.print(str2);
        printWriter2.print("mAdded=");
        printWriter2.print(this.mAdded);
        printWriter2.print(" mRemoving=");
        printWriter2.print(this.mRemoving);
        printWriter2.print(" mResumed=");
        printWriter2.print(this.mResumed);
        printWriter2.print(" mFromLayout=");
        printWriter2.print(this.mFromLayout);
        printWriter2.print(" mInLayout=");
        printWriter2.println(this.mInLayout);
        printWriter2.print(str2);
        printWriter2.print("mHidden=");
        printWriter2.print(this.mHidden);
        printWriter2.print(" mDetached=");
        printWriter2.print(this.mDetached);
        printWriter2.print(" mMenuVisible=");
        printWriter2.print(this.mMenuVisible);
        printWriter2.print(" mHasMenu=");
        printWriter2.println(this.mHasMenu);
        printWriter2.print(str2);
        printWriter2.print("mRetainInstance=");
        printWriter2.print(this.mRetainInstance);
        printWriter2.print(" mRetaining=");
        printWriter2.print(this.mRetaining);
        printWriter2.print(" mUserVisibleHint=");
        printWriter2.println(this.mUserVisibleHint);
        if (this.mFragmentManager != null) {
            printWriter2.print(str2);
            printWriter2.print("mFragmentManager=");
            printWriter2.println(this.mFragmentManager);
        }
        if (this.mActivity != null) {
            printWriter2.print(str2);
            printWriter2.print("mActivity=");
            printWriter2.println(this.mActivity);
        }
        if (this.mParentFragment != null) {
            printWriter2.print(str2);
            printWriter2.print("mParentFragment=");
            printWriter2.println(this.mParentFragment);
        }
        if (this.mArguments != null) {
            printWriter2.print(str2);
            printWriter2.print("mArguments=");
            printWriter2.println(this.mArguments);
        }
        if (this.mSavedFragmentState != null) {
            printWriter2.print(str2);
            printWriter2.print("mSavedFragmentState=");
            printWriter2.println(this.mSavedFragmentState);
        }
        if (this.mSavedViewState != null) {
            printWriter2.print(str2);
            printWriter2.print("mSavedViewState=");
            printWriter2.println(this.mSavedViewState);
        }
        if (this.mTarget != null) {
            printWriter2.print(str2);
            printWriter2.print("mTarget=");
            printWriter2.print(this.mTarget);
            printWriter2.print(" mTargetRequestCode=");
            printWriter2.println(this.mTargetRequestCode);
        }
        if (this.mNextAnim != 0) {
            printWriter2.print(str2);
            printWriter2.print("mNextAnim=");
            printWriter2.println(this.mNextAnim);
        }
        if (this.mContainer != null) {
            printWriter2.print(str2);
            printWriter2.print("mContainer=");
            printWriter2.println(this.mContainer);
        }
        if (this.mView != null) {
            printWriter2.print(str2);
            printWriter2.print("mView=");
            printWriter2.println(this.mView);
        }
        if (this.mInnerView != null) {
            printWriter2.print(str2);
            printWriter2.print("mInnerView=");
            printWriter2.println(this.mView);
        }
        if (this.mAnimatingAway != null) {
            printWriter2.print(str2);
            printWriter2.print("mAnimatingAway=");
            printWriter2.println(this.mAnimatingAway);
            printWriter2.print(str2);
            printWriter2.print("mStateAfterAnimating=");
            printWriter2.println(this.mStateAfterAnimating);
        }
        if (this.mLoaderManager != null) {
            printWriter2.print(str2);
            printWriter2.println("Loader Manager:");
            LoaderManagerImpl loaderManagerImpl = this.mLoaderManager;
            stringBuilder = r10;
            stringBuilder2 = new StringBuilder();
            loaderManagerImpl.dump(stringBuilder.append(str2).append("  ").toString(), fileDescriptor2, printWriter2, strArr2);
        }
        if (this.mChildFragmentManager != null) {
            printWriter2.print(str2);
            PrintWriter printWriter3 = printWriter2;
            stringBuilder = r10;
            stringBuilder2 = new StringBuilder();
            printWriter3.println(stringBuilder.append("Child ").append(this.mChildFragmentManager).append(":").toString());
            FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
            stringBuilder = r10;
            stringBuilder2 = new StringBuilder();
            fragmentManagerImpl.dump(stringBuilder.append(str2).append("  ").toString(), fileDescriptor2, printWriter2, strArr2);
        }
    }

    /* access modifiers changed from: 0000 */
    public Fragment findFragmentByWho(String str) {
        String str2 = str;
        if (str2.equals(this.mWho)) {
            return this;
        }
        if (this.mChildFragmentManager != null) {
            return this.mChildFragmentManager.findFragmentByWho(str2);
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public void instantiateChildFragmentManager() {
        FragmentManagerImpl fragmentManagerImpl = r6;
        FragmentManagerImpl fragmentManagerImpl2 = new FragmentManagerImpl();
        this.mChildFragmentManager = fragmentManagerImpl;
        FragmentManagerImpl fragmentManagerImpl3 = this.mChildFragmentManager;
        FragmentActivity fragmentActivity = this.mActivity;
        AnonymousClass1 anonymousClass1 = r6;
        AnonymousClass1 anonymousClass12 = new FragmentContainer(this) {
            final /* synthetic */ Fragment this$0;

            {
                this.this$0 = r5;
            }

            public View findViewById(int i) {
                int i2 = i;
                if (this.this$0.mView != null) {
                    return this.this$0.mView.findViewById(i2);
                }
                IllegalStateException illegalStateException = r5;
                IllegalStateException illegalStateException2 = new IllegalStateException("Fragment does not have a view");
                throw illegalStateException;
            }
        };
        fragmentManagerImpl3.attachActivity(fragmentActivity, anonymousClass1, this);
    }

    /* access modifiers changed from: 0000 */
    public void performCreate(Bundle bundle) {
        Bundle bundle2 = bundle;
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.noteStateNotSaved();
        }
        this.mCalled = false;
        onCreate(bundle2);
        if (!this.mCalled) {
            SuperNotCalledException superNotCalledException = r7;
            StringBuilder stringBuilder = r7;
            StringBuilder stringBuilder2 = new StringBuilder();
            SuperNotCalledException superNotCalledException2 = new SuperNotCalledException(stringBuilder.append("Fragment ").append(this).append(" did not call through to super.onCreate()").toString());
            throw superNotCalledException;
        } else if (bundle2 != null) {
            Parcelable parcelable = bundle2.getParcelable("android:support:fragments");
            if (parcelable != null) {
                if (this.mChildFragmentManager == null) {
                    instantiateChildFragmentManager();
                }
                this.mChildFragmentManager.restoreAllState(parcelable, null);
                this.mChildFragmentManager.dispatchCreate();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public View performCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        LayoutInflater layoutInflater2 = layoutInflater;
        ViewGroup viewGroup2 = viewGroup;
        Bundle bundle2 = bundle;
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.noteStateNotSaved();
        }
        return onCreateView(layoutInflater2, viewGroup2, bundle2);
    }

    /* access modifiers changed from: 0000 */
    public void performActivityCreated(Bundle bundle) {
        Bundle bundle2 = bundle;
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.noteStateNotSaved();
        }
        this.mCalled = false;
        onActivityCreated(bundle2);
        if (!this.mCalled) {
            SuperNotCalledException superNotCalledException = r6;
            StringBuilder stringBuilder = r6;
            StringBuilder stringBuilder2 = new StringBuilder();
            SuperNotCalledException superNotCalledException2 = new SuperNotCalledException(stringBuilder.append("Fragment ").append(this).append(" did not call through to super.onActivityCreated()").toString());
            throw superNotCalledException;
        } else if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchActivityCreated();
        }
    }

    /* access modifiers changed from: 0000 */
    public void performStart() {
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.noteStateNotSaved();
            boolean execPendingActions = this.mChildFragmentManager.execPendingActions();
        }
        this.mCalled = false;
        onStart();
        if (this.mCalled) {
            if (this.mChildFragmentManager != null) {
                this.mChildFragmentManager.dispatchStart();
            }
            if (this.mLoaderManager != null) {
                this.mLoaderManager.doReportStart();
                return;
            }
            return;
        }
        SuperNotCalledException superNotCalledException = r5;
        StringBuilder stringBuilder = r5;
        StringBuilder stringBuilder2 = new StringBuilder();
        SuperNotCalledException superNotCalledException2 = new SuperNotCalledException(stringBuilder.append("Fragment ").append(this).append(" did not call through to super.onStart()").toString());
        throw superNotCalledException;
    }

    /* access modifiers changed from: 0000 */
    public void performResume() {
        boolean execPendingActions;
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.noteStateNotSaved();
            execPendingActions = this.mChildFragmentManager.execPendingActions();
        }
        this.mCalled = false;
        onResume();
        if (!this.mCalled) {
            SuperNotCalledException superNotCalledException = r5;
            StringBuilder stringBuilder = r5;
            StringBuilder stringBuilder2 = new StringBuilder();
            SuperNotCalledException superNotCalledException2 = new SuperNotCalledException(stringBuilder.append("Fragment ").append(this).append(" did not call through to super.onResume()").toString());
            throw superNotCalledException;
        } else if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchResume();
            execPendingActions = this.mChildFragmentManager.execPendingActions();
        }
    }

    /* access modifiers changed from: 0000 */
    public void performConfigurationChanged(Configuration configuration) {
        Configuration configuration2 = configuration;
        onConfigurationChanged(configuration2);
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchConfigurationChanged(configuration2);
        }
    }

    /* access modifiers changed from: 0000 */
    public void performLowMemory() {
        onLowMemory();
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchLowMemory();
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean performCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        Menu menu2 = menu;
        MenuInflater menuInflater2 = menuInflater;
        boolean z = false;
        if (!this.mHidden) {
            if (this.mHasMenu && this.mMenuVisible) {
                z = true;
                onCreateOptionsMenu(menu2, menuInflater2);
            }
            if (this.mChildFragmentManager != null) {
                z |= this.mChildFragmentManager.dispatchCreateOptionsMenu(menu2, menuInflater2);
            }
        }
        return z;
    }

    /* access modifiers changed from: 0000 */
    public boolean performPrepareOptionsMenu(Menu menu) {
        Menu menu2 = menu;
        boolean z = false;
        if (!this.mHidden) {
            if (this.mHasMenu && this.mMenuVisible) {
                z = true;
                onPrepareOptionsMenu(menu2);
            }
            if (this.mChildFragmentManager != null) {
                z |= this.mChildFragmentManager.dispatchPrepareOptionsMenu(menu2);
            }
        }
        return z;
    }

    /* access modifiers changed from: 0000 */
    public boolean performOptionsItemSelected(MenuItem menuItem) {
        MenuItem menuItem2 = menuItem;
        if (!this.mHidden) {
            if (this.mHasMenu && this.mMenuVisible && onOptionsItemSelected(menuItem2)) {
                return true;
            }
            if (this.mChildFragmentManager != null && this.mChildFragmentManager.dispatchOptionsItemSelected(menuItem2)) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: 0000 */
    public boolean performContextItemSelected(MenuItem menuItem) {
        MenuItem menuItem2 = menuItem;
        if (!this.mHidden) {
            if (onContextItemSelected(menuItem2)) {
                return true;
            }
            if (this.mChildFragmentManager != null && this.mChildFragmentManager.dispatchContextItemSelected(menuItem2)) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: 0000 */
    public void performOptionsMenuClosed(Menu menu) {
        Menu menu2 = menu;
        if (!this.mHidden) {
            if (this.mHasMenu && this.mMenuVisible) {
                onOptionsMenuClosed(menu2);
            }
            if (this.mChildFragmentManager != null) {
                this.mChildFragmentManager.dispatchOptionsMenuClosed(menu2);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void performSaveInstanceState(Bundle bundle) {
        Bundle bundle2 = bundle;
        onSaveInstanceState(bundle2);
        if (this.mChildFragmentManager != null) {
            Parcelable saveAllState = this.mChildFragmentManager.saveAllState();
            if (saveAllState != null) {
                bundle2.putParcelable("android:support:fragments", saveAllState);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void performPause() {
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchPause();
        }
        this.mCalled = false;
        onPause();
        if (!this.mCalled) {
            SuperNotCalledException superNotCalledException = r5;
            StringBuilder stringBuilder = r5;
            StringBuilder stringBuilder2 = new StringBuilder();
            SuperNotCalledException superNotCalledException2 = new SuperNotCalledException(stringBuilder.append("Fragment ").append(this).append(" did not call through to super.onPause()").toString());
            throw superNotCalledException;
        }
    }

    /* access modifiers changed from: 0000 */
    public void performStop() {
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchStop();
        }
        this.mCalled = false;
        onStop();
        if (!this.mCalled) {
            SuperNotCalledException superNotCalledException = r5;
            StringBuilder stringBuilder = r5;
            StringBuilder stringBuilder2 = new StringBuilder();
            SuperNotCalledException superNotCalledException2 = new SuperNotCalledException(stringBuilder.append("Fragment ").append(this).append(" did not call through to super.onStop()").toString());
            throw superNotCalledException;
        }
    }

    /* access modifiers changed from: 0000 */
    public void performReallyStop() {
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchReallyStop();
        }
        if (this.mLoadersStarted) {
            this.mLoadersStarted = false;
            if (!this.mCheckedForLoaderManager) {
                this.mCheckedForLoaderManager = true;
                this.mLoaderManager = this.mActivity.getLoaderManager(this.mWho, this.mLoadersStarted, false);
            }
            if (this.mLoaderManager == null) {
                return;
            }
            if (this.mActivity.mRetaining) {
                this.mLoaderManager.doRetain();
            } else {
                this.mLoaderManager.doStop();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void performDestroyView() {
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchDestroyView();
        }
        this.mCalled = false;
        onDestroyView();
        if (!this.mCalled) {
            SuperNotCalledException superNotCalledException = r5;
            StringBuilder stringBuilder = r5;
            StringBuilder stringBuilder2 = new StringBuilder();
            SuperNotCalledException superNotCalledException2 = new SuperNotCalledException(stringBuilder.append("Fragment ").append(this).append(" did not call through to super.onDestroyView()").toString());
            throw superNotCalledException;
        } else if (this.mLoaderManager != null) {
            this.mLoaderManager.doReportNextStart();
        }
    }

    /* access modifiers changed from: 0000 */
    public void performDestroy() {
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchDestroy();
        }
        this.mCalled = false;
        onDestroy();
        if (!this.mCalled) {
            SuperNotCalledException superNotCalledException = r5;
            StringBuilder stringBuilder = r5;
            StringBuilder stringBuilder2 = new StringBuilder();
            SuperNotCalledException superNotCalledException2 = new SuperNotCalledException(stringBuilder.append("Fragment ").append(this).append(" did not call through to super.onDestroy()").toString());
            throw superNotCalledException;
        }
    }
}
