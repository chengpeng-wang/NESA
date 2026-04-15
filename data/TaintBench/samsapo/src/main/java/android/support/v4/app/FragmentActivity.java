package android.support.v4.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.util.SimpleArrayMap;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FragmentActivity extends Activity {
    static final String FRAGMENTS_TAG = "android:support:fragments";
    private static final int HONEYCOMB = 11;
    static final int MSG_REALLY_STOPPED = 1;
    static final int MSG_RESUME_PENDING = 2;
    private static final String TAG = "FragmentActivity";
    SimpleArrayMap<String, LoaderManagerImpl> mAllLoaderManagers;
    boolean mCheckedForLoaderManager;
    final FragmentContainer mContainer;
    boolean mCreated;
    final FragmentManagerImpl mFragments;
    final Handler mHandler;
    LoaderManagerImpl mLoaderManager;
    boolean mLoadersStarted;
    boolean mOptionsMenuInvalidated;
    boolean mReallyStopped;
    boolean mResumed;
    boolean mRetaining;
    boolean mStopped;

    static class FragmentTag {
        public static final int[] Fragment = new int[]{16842755, 16842960, 16842961};
        public static final int Fragment_id = 1;
        public static final int Fragment_name = 0;
        public static final int Fragment_tag = 2;

        FragmentTag() {
        }
    }

    static final class NonConfigurationInstances {
        Object activity;
        SimpleArrayMap<String, Object> children;
        Object custom;
        ArrayList<Fragment> fragments;
        SimpleArrayMap<String, LoaderManagerImpl> loaders;

        NonConfigurationInstances() {
        }
    }

    public FragmentActivity() {
        Handler handler = r5;
        Handler anonymousClass1 = new Handler(this) {
            final /* synthetic */ FragmentActivity this$0;

            {
                this.this$0 = r5;
            }

            public void handleMessage(Message message) {
                Message message2 = message;
                switch (message2.what) {
                    case 1:
                        if (this.this$0.mStopped) {
                            this.this$0.doReallyStop(false);
                            return;
                        }
                        return;
                    case 2:
                        this.this$0.onResumeFragments();
                        boolean execPendingActions = this.this$0.mFragments.execPendingActions();
                        return;
                    default:
                        super.handleMessage(message2);
                        return;
                }
            }
        };
        this.mHandler = handler;
        FragmentManagerImpl fragmentManagerImpl = r5;
        FragmentManagerImpl fragmentManagerImpl2 = new FragmentManagerImpl();
        this.mFragments = fragmentManagerImpl;
        AnonymousClass2 anonymousClass2 = r5;
        AnonymousClass2 anonymousClass22 = new FragmentContainer(this) {
            final /* synthetic */ FragmentActivity this$0;

            {
                this.this$0 = r5;
            }

            public View findViewById(int i) {
                return this.this$0.findViewById(i);
            }
        };
        this.mContainer = anonymousClass2;
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        int i3 = i;
        int i4 = i2;
        Intent intent2 = intent;
        this.mFragments.noteStateNotSaved();
        int i5 = i3 >> 16;
        if (i5 != 0) {
            i5--;
            String str;
            StringBuilder stringBuilder;
            StringBuilder stringBuilder2;
            int w;
            if (this.mFragments.mActive == null || i5 < 0 || i5 >= this.mFragments.mActive.size()) {
                str = TAG;
                stringBuilder = r10;
                stringBuilder2 = new StringBuilder();
                w = Log.w(str, stringBuilder.append("Activity result fragment index out of range: 0x").append(Integer.toHexString(i3)).toString());
                return;
            }
            Fragment fragment = (Fragment) this.mFragments.mActive.get(i5);
            if (fragment == null) {
                str = TAG;
                stringBuilder = r10;
                stringBuilder2 = new StringBuilder();
                w = Log.w(str, stringBuilder.append("Activity result no fragment exists for index: 0x").append(Integer.toHexString(i3)).toString());
                return;
            }
            fragment.onActivityResult(i3 & SupportMenu.USER_MASK, i4, intent2);
            return;
        }
        super.onActivityResult(i3, i4, intent2);
    }

    public void onBackPressed() {
        if (!this.mFragments.popBackStackImmediate()) {
            finish();
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        Configuration configuration2 = configuration;
        super.onConfigurationChanged(configuration2);
        this.mFragments.dispatchConfigurationChanged(configuration2);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        Bundle bundle2 = bundle;
        this.mFragments.attachActivity(this, this.mContainer, null);
        if (getLayoutInflater().getFactory() == null) {
            getLayoutInflater().setFactory(this);
        }
        super.onCreate(bundle2);
        NonConfigurationInstances nonConfigurationInstances = (NonConfigurationInstances) getLastNonConfigurationInstance();
        if (nonConfigurationInstances != null) {
            this.mAllLoaderManagers = nonConfigurationInstances.loaders;
        }
        if (bundle2 != null) {
            this.mFragments.restoreAllState(bundle2.getParcelable(FRAGMENTS_TAG), nonConfigurationInstances != null ? nonConfigurationInstances.fragments : null);
        }
        this.mFragments.dispatchCreate();
    }

    public boolean onCreatePanelMenu(int i, Menu menu) {
        int i2 = i;
        Menu menu2 = menu;
        if (i2 != 0) {
            return super.onCreatePanelMenu(i2, menu2);
        }
        boolean onCreatePanelMenu = super.onCreatePanelMenu(i2, menu2) | this.mFragments.dispatchCreateOptionsMenu(menu2, getMenuInflater());
        if (VERSION.SDK_INT >= HONEYCOMB) {
            return onCreatePanelMenu;
        }
        return true;
    }

    public View onCreateView(String str, Context context, AttributeSet attributeSet) {
        String str2 = str;
        Context context2 = context;
        AttributeSet attributeSet2 = attributeSet;
        if (!"fragment".equals(str2)) {
            return super.onCreateView(str2, context2, attributeSet2);
        }
        String attributeValue = attributeSet2.getAttributeValue(null, "class");
        TypedArray obtainStyledAttributes = context2.obtainStyledAttributes(attributeSet2, FragmentTag.Fragment);
        if (attributeValue == null) {
            attributeValue = obtainStyledAttributes.getString(0);
        }
        int resourceId = obtainStyledAttributes.getResourceId(1, -1);
        String string = obtainStyledAttributes.getString(2);
        obtainStyledAttributes.recycle();
        if (!Fragment.isSupportFragmentClass(this, attributeValue)) {
            return super.onCreateView(str2, context2, attributeSet2);
        }
        View view = null;
        int id = view != null ? view.getId() : 0;
        IllegalArgumentException illegalArgumentException;
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        IllegalArgumentException illegalArgumentException2;
        if (id == -1 && resourceId == -1 && string == null) {
            illegalArgumentException = r15;
            stringBuilder = r15;
            stringBuilder2 = new StringBuilder();
            illegalArgumentException2 = new IllegalArgumentException(stringBuilder.append(attributeSet2.getPositionDescription()).append(": Must specify unique android:id, android:tag, or have a parent with an id for ").append(attributeValue).toString());
            throw illegalArgumentException;
        }
        Fragment findFragmentById = resourceId != -1 ? this.mFragments.findFragmentById(resourceId) : null;
        if (findFragmentById == null && string != null) {
            findFragmentById = this.mFragments.findFragmentByTag(string);
        }
        if (findFragmentById == null && id != -1) {
            findFragmentById = this.mFragments.findFragmentById(id);
        }
        if (FragmentManagerImpl.DEBUG) {
            String str3 = TAG;
            StringBuilder stringBuilder3 = r15;
            stringBuilder = new StringBuilder();
            int v = Log.v(str3, stringBuilder3.append("onCreateView: id=0x").append(Integer.toHexString(resourceId)).append(" fname=").append(attributeValue).append(" existing=").append(findFragmentById).toString());
        }
        if (findFragmentById == null) {
            findFragmentById = Fragment.instantiate(this, attributeValue);
            findFragmentById.mFromLayout = true;
            findFragmentById.mFragmentId = resourceId != 0 ? resourceId : id;
            findFragmentById.mContainerId = id;
            findFragmentById.mTag = string;
            findFragmentById.mInLayout = true;
            findFragmentById.mFragmentManager = this.mFragments;
            findFragmentById.onInflate(this, attributeSet2, findFragmentById.mSavedFragmentState);
            this.mFragments.addFragment(findFragmentById, true);
        } else if (findFragmentById.mInLayout) {
            illegalArgumentException = r15;
            stringBuilder = r15;
            stringBuilder2 = new StringBuilder();
            illegalArgumentException2 = new IllegalArgumentException(stringBuilder.append(attributeSet2.getPositionDescription()).append(": Duplicate id 0x").append(Integer.toHexString(resourceId)).append(", tag ").append(string).append(", or parent id 0x").append(Integer.toHexString(id)).append(" with another fragment for ").append(attributeValue).toString());
            throw illegalArgumentException;
        } else {
            findFragmentById.mInLayout = true;
            if (!findFragmentById.mRetaining) {
                findFragmentById.onInflate(this, attributeSet2, findFragmentById.mSavedFragmentState);
            }
            this.mFragments.moveToState(findFragmentById);
        }
        if (findFragmentById.mView == null) {
            IllegalStateException illegalStateException = r15;
            stringBuilder = r15;
            stringBuilder2 = new StringBuilder();
            IllegalStateException illegalStateException2 = new IllegalStateException(stringBuilder.append("Fragment ").append(attributeValue).append(" did not create a view.").toString());
            throw illegalStateException;
        }
        if (resourceId != 0) {
            findFragmentById.mView.setId(resourceId);
        }
        if (findFragmentById.mView.getTag() == null) {
            findFragmentById.mView.setTag(string);
        }
        return findFragmentById.mView;
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        doReallyStop(false);
        this.mFragments.dispatchDestroy();
        if (this.mLoaderManager != null) {
            this.mLoaderManager.doDestroy();
        }
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        int i2 = i;
        KeyEvent keyEvent2 = keyEvent;
        if (VERSION.SDK_INT >= 5 || i2 != 4 || keyEvent2.getRepeatCount() != 0) {
            return super.onKeyDown(i2, keyEvent2);
        }
        onBackPressed();
        return true;
    }

    public void onLowMemory() {
        super.onLowMemory();
        this.mFragments.dispatchLowMemory();
    }

    public boolean onMenuItemSelected(int i, MenuItem menuItem) {
        int i2 = i;
        MenuItem menuItem2 = menuItem;
        if (super.onMenuItemSelected(i2, menuItem2)) {
            return true;
        }
        switch (i2) {
            case 0:
                return this.mFragments.dispatchOptionsItemSelected(menuItem2);
            case 6:
                return this.mFragments.dispatchContextItemSelected(menuItem2);
            default:
                return false;
        }
    }

    public void onPanelClosed(int i, Menu menu) {
        int i2 = i;
        Menu menu2 = menu;
        switch (i2) {
            case 0:
                this.mFragments.dispatchOptionsMenuClosed(menu2);
                break;
        }
        super.onPanelClosed(i2, menu2);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.mResumed = false;
        if (this.mHandler.hasMessages(2)) {
            this.mHandler.removeMessages(2);
            onResumeFragments();
        }
        this.mFragments.dispatchPause();
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.mFragments.noteStateNotSaved();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        boolean sendEmptyMessage = this.mHandler.sendEmptyMessage(2);
        this.mResumed = true;
        sendEmptyMessage = this.mFragments.execPendingActions();
    }

    /* access modifiers changed from: protected */
    public void onPostResume() {
        super.onPostResume();
        this.mHandler.removeMessages(2);
        onResumeFragments();
        boolean execPendingActions = this.mFragments.execPendingActions();
    }

    /* access modifiers changed from: protected */
    public void onResumeFragments() {
        this.mFragments.dispatchResume();
    }

    public boolean onPreparePanel(int i, View view, Menu menu) {
        int i2 = i;
        View view2 = view;
        Menu menu2 = menu;
        if (i2 != 0 || menu2 == null) {
            return super.onPreparePanel(i2, view2, menu2);
        }
        if (this.mOptionsMenuInvalidated) {
            this.mOptionsMenuInvalidated = false;
            menu2.clear();
            boolean onCreatePanelMenu = onCreatePanelMenu(i2, menu2);
        }
        return onPrepareOptionsPanel(view2, menu2) | this.mFragments.dispatchPrepareOptionsMenu(menu2);
    }

    /* access modifiers changed from: protected */
    public boolean onPrepareOptionsPanel(View view, Menu menu) {
        return super.onPreparePanel(0, view, menu);
    }

    public final Object onRetainNonConfigurationInstance() {
        if (this.mStopped) {
            doReallyStop(true);
        }
        Object onRetainCustomNonConfigurationInstance = onRetainCustomNonConfigurationInstance();
        ArrayList retainNonConfig = this.mFragments.retainNonConfig();
        Object obj = null;
        if (this.mAllLoaderManagers != null) {
            int i;
            int size = this.mAllLoaderManagers.size();
            LoaderManagerImpl[] loaderManagerImplArr = new LoaderManagerImpl[size];
            for (i = size - 1; i >= 0; i--) {
                loaderManagerImplArr[i] = (LoaderManagerImpl) this.mAllLoaderManagers.valueAt(i);
            }
            for (i = 0; i < size; i++) {
                LoaderManagerImpl loaderManagerImpl = loaderManagerImplArr[i];
                if (loaderManagerImpl.mRetaining) {
                    obj = 1;
                } else {
                    loaderManagerImpl.doDestroy();
                    Object remove = this.mAllLoaderManagers.remove(loaderManagerImpl.mWho);
                }
            }
        }
        if (retainNonConfig == null && obj == null && onRetainCustomNonConfigurationInstance == null) {
            return null;
        }
        NonConfigurationInstances nonConfigurationInstances = r12;
        NonConfigurationInstances nonConfigurationInstances2 = new NonConfigurationInstances();
        NonConfigurationInstances nonConfigurationInstances3 = nonConfigurationInstances;
        nonConfigurationInstances3.activity = null;
        nonConfigurationInstances3.custom = onRetainCustomNonConfigurationInstance;
        nonConfigurationInstances3.children = null;
        nonConfigurationInstances3.fragments = retainNonConfig;
        nonConfigurationInstances3.loaders = this.mAllLoaderManagers;
        return nonConfigurationInstances3;
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        Bundle bundle2 = bundle;
        super.onSaveInstanceState(bundle2);
        Parcelable saveAllState = this.mFragments.saveAllState();
        if (saveAllState != null) {
            bundle2.putParcelable(FRAGMENTS_TAG, saveAllState);
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        this.mStopped = false;
        this.mReallyStopped = false;
        this.mHandler.removeMessages(1);
        if (!this.mCreated) {
            this.mCreated = true;
            this.mFragments.dispatchActivityCreated();
        }
        this.mFragments.noteStateNotSaved();
        boolean execPendingActions = this.mFragments.execPendingActions();
        if (!this.mLoadersStarted) {
            this.mLoadersStarted = true;
            if (this.mLoaderManager != null) {
                this.mLoaderManager.doStart();
            } else if (!this.mCheckedForLoaderManager) {
                this.mLoaderManager = getLoaderManager("(root)", this.mLoadersStarted, false);
                if (!(this.mLoaderManager == null || this.mLoaderManager.mStarted)) {
                    this.mLoaderManager.doStart();
                }
            }
            this.mCheckedForLoaderManager = true;
        }
        this.mFragments.dispatchStart();
        if (this.mAllLoaderManagers != null) {
            int i;
            int size = this.mAllLoaderManagers.size();
            LoaderManagerImpl[] loaderManagerImplArr = new LoaderManagerImpl[size];
            for (i = size - 1; i >= 0; i--) {
                loaderManagerImplArr[i] = (LoaderManagerImpl) this.mAllLoaderManagers.valueAt(i);
            }
            for (i = 0; i < size; i++) {
                LoaderManagerImpl loaderManagerImpl = loaderManagerImplArr[i];
                loaderManagerImpl.finishRetain();
                loaderManagerImpl.doReportStart();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        this.mStopped = true;
        boolean sendEmptyMessage = this.mHandler.sendEmptyMessage(1);
        this.mFragments.dispatchStop();
    }

    public Object onRetainCustomNonConfigurationInstance() {
        return null;
    }

    public Object getLastCustomNonConfigurationInstance() {
        NonConfigurationInstances nonConfigurationInstances = (NonConfigurationInstances) getLastNonConfigurationInstance();
        return nonConfigurationInstances != null ? nonConfigurationInstances.custom : null;
    }

    public void supportInvalidateOptionsMenu() {
        if (VERSION.SDK_INT >= HONEYCOMB) {
            ActivityCompatHoneycomb.invalidateOptionsMenu(this);
        } else {
            this.mOptionsMenuInvalidated = true;
        }
    }

    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        StringBuilder stringBuilder;
        String str2 = str;
        FileDescriptor fileDescriptor2 = fileDescriptor;
        PrintWriter printWriter2 = printWriter;
        String[] strArr2 = strArr;
        if (VERSION.SDK_INT >= HONEYCOMB) {
        }
        printWriter2.print(str2);
        printWriter2.print("Local FragmentActivity ");
        printWriter2.print(Integer.toHexString(System.identityHashCode(this)));
        printWriter2.println(" State:");
        StringBuilder stringBuilder2 = r11;
        StringBuilder stringBuilder3 = new StringBuilder();
        String stringBuilder4 = stringBuilder2.append(str2).append("  ").toString();
        printWriter2.print(stringBuilder4);
        printWriter2.print("mCreated=");
        printWriter2.print(this.mCreated);
        printWriter2.print("mResumed=");
        printWriter2.print(this.mResumed);
        printWriter2.print(" mStopped=");
        printWriter2.print(this.mStopped);
        printWriter2.print(" mReallyStopped=");
        printWriter2.println(this.mReallyStopped);
        printWriter2.print(stringBuilder4);
        printWriter2.print("mLoadersStarted=");
        printWriter2.println(this.mLoadersStarted);
        if (this.mLoaderManager != null) {
            printWriter2.print(str2);
            printWriter2.print("Loader Manager ");
            printWriter2.print(Integer.toHexString(System.identityHashCode(this.mLoaderManager)));
            printWriter2.println(":");
            LoaderManagerImpl loaderManagerImpl = this.mLoaderManager;
            stringBuilder3 = r11;
            stringBuilder = new StringBuilder();
            loaderManagerImpl.dump(stringBuilder3.append(str2).append("  ").toString(), fileDescriptor2, printWriter2, strArr2);
        }
        this.mFragments.dump(str2, fileDescriptor2, printWriter2, strArr2);
        printWriter2.print(str2);
        printWriter2.println("View Hierarchy:");
        stringBuilder3 = r11;
        stringBuilder = new StringBuilder();
        dumpViewHierarchy(stringBuilder3.append(str2).append("  ").toString(), printWriter2, getWindow().getDecorView());
    }

    private static String viewToString(View view) {
        View view2 = view;
        StringBuilder stringBuilder = r10;
        StringBuilder stringBuilder2 = new StringBuilder(128);
        StringBuilder stringBuilder3 = stringBuilder;
        stringBuilder = stringBuilder3.append(view2.getClass().getName());
        stringBuilder = stringBuilder3.append('{');
        stringBuilder = stringBuilder3.append(Integer.toHexString(System.identityHashCode(view2)));
        stringBuilder = stringBuilder3.append(' ');
        switch (view2.getVisibility()) {
            case 0:
                stringBuilder = stringBuilder3.append('V');
                break;
            case 4:
                stringBuilder = stringBuilder3.append('I');
                break;
            case 8:
                stringBuilder = stringBuilder3.append('G');
                break;
            default:
                stringBuilder = stringBuilder3.append('.');
                break;
        }
        stringBuilder = stringBuilder3.append(view2.isFocusable() ? 'F' : '.');
        stringBuilder = stringBuilder3.append(view2.isEnabled() ? 'E' : '.');
        stringBuilder = stringBuilder3.append(view2.willNotDraw() ? '.' : 'D');
        stringBuilder = stringBuilder3.append(view2.isHorizontalScrollBarEnabled() ? 'H' : '.');
        stringBuilder = stringBuilder3.append(view2.isVerticalScrollBarEnabled() ? 'V' : '.');
        stringBuilder = stringBuilder3.append(view2.isClickable() ? 'C' : '.');
        stringBuilder = stringBuilder3.append(view2.isLongClickable() ? 'L' : '.');
        stringBuilder = stringBuilder3.append(' ');
        stringBuilder = stringBuilder3.append(view2.isFocused() ? 'F' : '.');
        stringBuilder = stringBuilder3.append(view2.isSelected() ? 'S' : '.');
        stringBuilder = stringBuilder3.append(view2.isPressed() ? 'P' : '.');
        stringBuilder = stringBuilder3.append(' ');
        stringBuilder = stringBuilder3.append(view2.getLeft());
        stringBuilder = stringBuilder3.append(',');
        stringBuilder = stringBuilder3.append(view2.getTop());
        stringBuilder = stringBuilder3.append('-');
        stringBuilder = stringBuilder3.append(view2.getRight());
        stringBuilder = stringBuilder3.append(',');
        stringBuilder = stringBuilder3.append(view2.getBottom());
        int id = view2.getId();
        if (id != -1) {
            stringBuilder = stringBuilder3.append(" #");
            stringBuilder = stringBuilder3.append(Integer.toHexString(id));
            Resources resources = view2.getResources();
            if (!(id == 0 || resources == null)) {
                String str;
                switch (id & ViewCompat.MEASURED_STATE_MASK) {
                    case ViewCompat.MEASURED_STATE_TOO_SMALL /*16777216*/:
                        str = "android";
                        break;
                    case 2130706432:
                        str = "app";
                        break;
                    default:
                        try {
                            str = resources.getResourcePackageName(id);
                            break;
                        } catch (NotFoundException e) {
                            NotFoundException notFoundException = e;
                            break;
                        }
                }
                String resourceTypeName = resources.getResourceTypeName(id);
                String resourceEntryName = resources.getResourceEntryName(id);
                stringBuilder = stringBuilder3.append(" ");
                stringBuilder = stringBuilder3.append(str);
                stringBuilder = stringBuilder3.append(":");
                stringBuilder = stringBuilder3.append(resourceTypeName);
                stringBuilder = stringBuilder3.append("/");
                stringBuilder = stringBuilder3.append(resourceEntryName);
            }
        }
        stringBuilder = stringBuilder3.append("}");
        return stringBuilder3.toString();
    }

    private void dumpViewHierarchy(String str, PrintWriter printWriter, View view) {
        String str2 = str;
        PrintWriter printWriter2 = printWriter;
        View view2 = view;
        printWriter2.print(str2);
        if (view2 == null) {
            printWriter2.println("null");
            return;
        }
        printWriter2.println(viewToString(view2));
        if (view2 instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view2;
            int childCount = viewGroup.getChildCount();
            if (childCount > 0) {
                StringBuilder stringBuilder = r12;
                StringBuilder stringBuilder2 = new StringBuilder();
                str2 = stringBuilder.append(str2).append("  ").toString();
                for (int i = 0; i < childCount; i++) {
                    dumpViewHierarchy(str2, printWriter2, viewGroup.getChildAt(i));
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void doReallyStop(boolean z) {
        boolean z2 = z;
        if (!this.mReallyStopped) {
            this.mReallyStopped = true;
            this.mRetaining = z2;
            this.mHandler.removeMessages(1);
            onReallyStop();
        }
    }

    /* access modifiers changed from: 0000 */
    public void onReallyStop() {
        if (this.mLoadersStarted) {
            this.mLoadersStarted = false;
            if (this.mLoaderManager != null) {
                if (this.mRetaining) {
                    this.mLoaderManager.doRetain();
                } else {
                    this.mLoaderManager.doStop();
                }
            }
        }
        this.mFragments.dispatchReallyStop();
    }

    public void onAttachFragment(Fragment fragment) {
    }

    public FragmentManager getSupportFragmentManager() {
        return this.mFragments;
    }

    public void startActivityForResult(Intent intent, int i) {
        Intent intent2 = intent;
        int i2 = i;
        if (i2 == -1 || (i2 & SupportMenu.CATEGORY_MASK) == 0) {
            super.startActivityForResult(intent2, i2);
            return;
        }
        IllegalArgumentException illegalArgumentException = r6;
        IllegalArgumentException illegalArgumentException2 = new IllegalArgumentException("Can only use lower 16 bits for requestCode");
        throw illegalArgumentException;
    }

    public void startActivityFromFragment(Fragment fragment, Intent intent, int i) {
        Fragment fragment2 = fragment;
        Intent intent2 = intent;
        int i2 = i;
        if (i2 == -1) {
            super.startActivityForResult(intent2, -1);
        } else if ((i2 & SupportMenu.CATEGORY_MASK) != 0) {
            IllegalArgumentException illegalArgumentException = r9;
            IllegalArgumentException illegalArgumentException2 = new IllegalArgumentException("Can only use lower 16 bits for requestCode");
            throw illegalArgumentException;
        } else {
            super.startActivityForResult(intent2, ((fragment2.mIndex + 1) << 16) + (i2 & SupportMenu.USER_MASK));
        }
    }

    /* access modifiers changed from: 0000 */
    public void invalidateSupportFragment(String str) {
        String str2 = str;
        if (this.mAllLoaderManagers != null) {
            LoaderManagerImpl loaderManagerImpl = (LoaderManagerImpl) this.mAllLoaderManagers.get(str2);
            if (loaderManagerImpl != null && !loaderManagerImpl.mRetaining) {
                loaderManagerImpl.doDestroy();
                Object remove = this.mAllLoaderManagers.remove(str2);
            }
        }
    }

    public LoaderManager getSupportLoaderManager() {
        if (this.mLoaderManager != null) {
            return this.mLoaderManager;
        }
        this.mCheckedForLoaderManager = true;
        this.mLoaderManager = getLoaderManager("(root)", this.mLoadersStarted, true);
        return this.mLoaderManager;
    }

    /* access modifiers changed from: 0000 */
    public LoaderManagerImpl getLoaderManager(String str, boolean z, boolean z2) {
        String str2 = str;
        boolean z3 = z;
        boolean z4 = z2;
        if (this.mAllLoaderManagers == null) {
            SimpleArrayMap simpleArrayMap = r10;
            SimpleArrayMap simpleArrayMap2 = new SimpleArrayMap();
            this.mAllLoaderManagers = simpleArrayMap;
        }
        LoaderManagerImpl loaderManagerImpl = (LoaderManagerImpl) this.mAllLoaderManagers.get(str2);
        if (loaderManagerImpl != null) {
            loaderManagerImpl.updateActivity(this);
        } else if (z4) {
            LoaderManagerImpl loaderManagerImpl2 = r10;
            LoaderManagerImpl loaderManagerImpl3 = new LoaderManagerImpl(str2, this, z3);
            loaderManagerImpl = loaderManagerImpl2;
            Object put = this.mAllLoaderManagers.put(str2, loaderManagerImpl);
        }
        return loaderManagerImpl;
    }
}
