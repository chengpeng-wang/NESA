package android.support.v4.app;

import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.content.Loader.OnLoadCompleteListener;
import android.support.v4.util.DebugUtils;
import android.support.v4.util.SparseArrayCompat;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;

/* compiled from: LoaderManager */
class LoaderManagerImpl extends LoaderManager {
    static boolean DEBUG = false;
    static final String TAG = "LoaderManager";
    FragmentActivity mActivity;
    boolean mCreatingLoader;
    final SparseArrayCompat<LoaderInfo> mInactiveLoaders;
    final SparseArrayCompat<LoaderInfo> mLoaders;
    boolean mRetaining;
    boolean mRetainingStarted;
    boolean mStarted;
    final String mWho;

    /* compiled from: LoaderManager */
    final class LoaderInfo implements OnLoadCompleteListener<Object> {
        final Bundle mArgs;
        LoaderCallbacks<Object> mCallbacks;
        Object mData;
        boolean mDeliveredData;
        boolean mDestroyed;
        boolean mHaveData;
        final int mId;
        boolean mListenerRegistered;
        Loader<Object> mLoader;
        LoaderInfo mPendingLoader;
        boolean mReportNextStart;
        boolean mRetaining;
        boolean mRetainingStarted;
        boolean mStarted;
        final /* synthetic */ LoaderManagerImpl this$0;

        public LoaderInfo(LoaderManagerImpl loaderManagerImpl, int i, Bundle bundle, LoaderCallbacks<Object> loaderCallbacks) {
            int i2 = i;
            Bundle bundle2 = bundle;
            LoaderCallbacks<Object> loaderCallbacks2 = loaderCallbacks;
            this.this$0 = loaderManagerImpl;
            this.mId = i2;
            this.mArgs = bundle2;
            this.mCallbacks = loaderCallbacks2;
        }

        /* access modifiers changed from: 0000 */
        public void start() {
            if (this.mRetaining && this.mRetainingStarted) {
                this.mStarted = true;
            } else if (!this.mStarted) {
                StringBuilder stringBuilder;
                this.mStarted = true;
                if (LoaderManagerImpl.DEBUG) {
                    String str = LoaderManagerImpl.TAG;
                    StringBuilder stringBuilder2 = r5;
                    stringBuilder = new StringBuilder();
                    int v = Log.v(str, stringBuilder2.append("  Starting: ").append(this).toString());
                }
                if (this.mLoader == null && this.mCallbacks != null) {
                    this.mLoader = this.mCallbacks.onCreateLoader(this.mId, this.mArgs);
                }
                if (this.mLoader == null) {
                    return;
                }
                if (!this.mLoader.getClass().isMemberClass() || Modifier.isStatic(this.mLoader.getClass().getModifiers())) {
                    if (!this.mListenerRegistered) {
                        this.mLoader.registerListener(this.mId, this);
                        this.mListenerRegistered = true;
                    }
                    this.mLoader.startLoading();
                    return;
                }
                IllegalArgumentException illegalArgumentException = r5;
                stringBuilder = r5;
                StringBuilder stringBuilder3 = new StringBuilder();
                IllegalArgumentException illegalArgumentException2 = new IllegalArgumentException(stringBuilder.append("Object returned from onCreateLoader must not be a non-static inner member class: ").append(this.mLoader).toString());
                throw illegalArgumentException;
            }
        }

        /* access modifiers changed from: 0000 */
        public void retain() {
            if (LoaderManagerImpl.DEBUG) {
                String str = LoaderManagerImpl.TAG;
                StringBuilder stringBuilder = r4;
                StringBuilder stringBuilder2 = new StringBuilder();
                int v = Log.v(str, stringBuilder.append("  Retaining: ").append(this).toString());
            }
            this.mRetaining = true;
            this.mRetainingStarted = this.mStarted;
            this.mStarted = false;
            this.mCallbacks = null;
        }

        /* access modifiers changed from: 0000 */
        public void finishRetain() {
            if (this.mRetaining) {
                if (LoaderManagerImpl.DEBUG) {
                    String str = LoaderManagerImpl.TAG;
                    StringBuilder stringBuilder = r4;
                    StringBuilder stringBuilder2 = new StringBuilder();
                    int v = Log.v(str, stringBuilder.append("  Finished Retaining: ").append(this).toString());
                }
                this.mRetaining = false;
                if (!(this.mStarted == this.mRetainingStarted || this.mStarted)) {
                    stop();
                }
            }
            if (this.mStarted && this.mHaveData && !this.mReportNextStart) {
                callOnLoadFinished(this.mLoader, this.mData);
            }
        }

        /* access modifiers changed from: 0000 */
        public void reportStart() {
            if (this.mStarted && this.mReportNextStart) {
                this.mReportNextStart = false;
                if (this.mHaveData) {
                    callOnLoadFinished(this.mLoader, this.mData);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void stop() {
            if (LoaderManagerImpl.DEBUG) {
                String str = LoaderManagerImpl.TAG;
                StringBuilder stringBuilder = r4;
                StringBuilder stringBuilder2 = new StringBuilder();
                int v = Log.v(str, stringBuilder.append("  Stopping: ").append(this).toString());
            }
            this.mStarted = false;
            if (!this.mRetaining && this.mLoader != null && this.mListenerRegistered) {
                this.mListenerRegistered = false;
                this.mLoader.unregisterListener(this);
                this.mLoader.stopLoading();
            }
        }

        /* access modifiers changed from: 0000 */
        public void destroy() {
            String str;
            StringBuilder stringBuilder;
            StringBuilder stringBuilder2;
            int v;
            if (LoaderManagerImpl.DEBUG) {
                str = LoaderManagerImpl.TAG;
                stringBuilder = r7;
                stringBuilder2 = new StringBuilder();
                v = Log.v(str, stringBuilder.append("  Destroying: ").append(this).toString());
            }
            this.mDestroyed = true;
            boolean z = this.mDeliveredData;
            this.mDeliveredData = false;
            if (this.mCallbacks != null && this.mLoader != null && this.mHaveData && z) {
                if (LoaderManagerImpl.DEBUG) {
                    str = LoaderManagerImpl.TAG;
                    stringBuilder = r7;
                    stringBuilder2 = new StringBuilder();
                    v = Log.v(str, stringBuilder.append("  Reseting: ").append(this).toString());
                }
                String str2 = null;
                if (this.this$0.mActivity != null) {
                    str2 = this.this$0.mActivity.mFragments.mNoTransactionsBecause;
                    this.this$0.mActivity.mFragments.mNoTransactionsBecause = "onLoaderReset";
                }
                try {
                    this.mCallbacks.onLoaderReset(this.mLoader);
                    if (this.this$0.mActivity != null) {
                        this.this$0.mActivity.mFragments.mNoTransactionsBecause = str2;
                    }
                } catch (Throwable th) {
                    Throwable th2 = th;
                    if (this.this$0.mActivity != null) {
                        this.this$0.mActivity.mFragments.mNoTransactionsBecause = str2;
                    }
                    Throwable th3 = th2;
                }
            }
            this.mCallbacks = null;
            this.mData = null;
            this.mHaveData = false;
            if (this.mLoader != null) {
                if (this.mListenerRegistered) {
                    this.mListenerRegistered = false;
                    this.mLoader.unregisterListener(this);
                }
                this.mLoader.reset();
            }
            if (this.mPendingLoader != null) {
                this.mPendingLoader.destroy();
            }
        }

        public void onLoadComplete(Loader<Object> loader, Object obj) {
            String str;
            StringBuilder stringBuilder;
            StringBuilder stringBuilder2;
            int v;
            Loader<Object> loader2 = loader;
            Object obj2 = obj;
            if (LoaderManagerImpl.DEBUG) {
                str = LoaderManagerImpl.TAG;
                stringBuilder = r8;
                stringBuilder2 = new StringBuilder();
                v = Log.v(str, stringBuilder.append("onLoadComplete: ").append(this).toString());
            }
            if (this.mDestroyed) {
                if (LoaderManagerImpl.DEBUG) {
                    v = Log.v(LoaderManagerImpl.TAG, "  Ignoring load complete -- destroyed");
                }
            } else if (this.this$0.mLoaders.get(this.mId) == this) {
                LoaderInfo loaderInfo = this.mPendingLoader;
                if (loaderInfo != null) {
                    if (LoaderManagerImpl.DEBUG) {
                        str = LoaderManagerImpl.TAG;
                        stringBuilder = r8;
                        stringBuilder2 = new StringBuilder();
                        v = Log.v(str, stringBuilder.append("  Switching to pending loader: ").append(loaderInfo).toString());
                    }
                    this.mPendingLoader = null;
                    this.this$0.mLoaders.put(this.mId, null);
                    destroy();
                    this.this$0.installLoader(loaderInfo);
                    return;
                }
                if (!(this.mData == obj2 && this.mHaveData)) {
                    this.mData = obj2;
                    this.mHaveData = true;
                    if (this.mStarted) {
                        callOnLoadFinished(loader2, obj2);
                    }
                }
                LoaderInfo loaderInfo2 = (LoaderInfo) this.this$0.mInactiveLoaders.get(this.mId);
                if (!(loaderInfo2 == null || loaderInfo2 == this)) {
                    loaderInfo2.mDeliveredData = false;
                    loaderInfo2.destroy();
                    this.this$0.mInactiveLoaders.remove(this.mId);
                }
                if (this.this$0.mActivity != null && !this.this$0.hasRunningLoaders()) {
                    this.this$0.mActivity.mFragments.startPendingDeferredFragments();
                }
            } else if (LoaderManagerImpl.DEBUG) {
                v = Log.v(LoaderManagerImpl.TAG, "  Ignoring load complete -- not active");
            }
        }

        /* access modifiers changed from: 0000 */
        public void callOnLoadFinished(Loader<Object> loader, Object obj) {
            Loader<Object> loader2 = loader;
            Object obj2 = obj;
            if (this.mCallbacks != null) {
                String str = null;
                if (this.this$0.mActivity != null) {
                    str = this.this$0.mActivity.mFragments.mNoTransactionsBecause;
                    this.this$0.mActivity.mFragments.mNoTransactionsBecause = "onLoadFinished";
                }
                try {
                    if (LoaderManagerImpl.DEBUG) {
                        String str2 = LoaderManagerImpl.TAG;
                        StringBuilder stringBuilder = r9;
                        StringBuilder stringBuilder2 = new StringBuilder();
                        int v = Log.v(str2, stringBuilder.append("  onLoadFinished in ").append(loader2).append(": ").append(loader2.dataToString(obj2)).toString());
                    }
                    this.mCallbacks.onLoadFinished(loader2, obj2);
                    if (this.this$0.mActivity != null) {
                        this.this$0.mActivity.mFragments.mNoTransactionsBecause = str;
                    }
                    this.mDeliveredData = true;
                } catch (Throwable th) {
                    Throwable th2 = th;
                    if (this.this$0.mActivity != null) {
                        this.this$0.mActivity.mFragments.mNoTransactionsBecause = str;
                    }
                    Throwable th3 = th2;
                }
            }
        }

        public String toString() {
            StringBuilder stringBuilder = r5;
            StringBuilder stringBuilder2 = new StringBuilder(64);
            StringBuilder stringBuilder3 = stringBuilder;
            stringBuilder = stringBuilder3.append("LoaderInfo{");
            stringBuilder = stringBuilder3.append(Integer.toHexString(System.identityHashCode(this)));
            stringBuilder = stringBuilder3.append(" #");
            stringBuilder = stringBuilder3.append(this.mId);
            stringBuilder = stringBuilder3.append(" : ");
            DebugUtils.buildShortClassTag(this.mLoader, stringBuilder3);
            stringBuilder = stringBuilder3.append("}}");
            return stringBuilder3.toString();
        }

        public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            StringBuilder stringBuilder;
            StringBuilder stringBuilder2;
            String str2 = str;
            FileDescriptor fileDescriptor2 = fileDescriptor;
            PrintWriter printWriter2 = printWriter;
            String[] strArr2 = strArr;
            printWriter2.print(str2);
            printWriter2.print("mId=");
            printWriter2.print(this.mId);
            printWriter2.print(" mArgs=");
            printWriter2.println(this.mArgs);
            printWriter2.print(str2);
            printWriter2.print("mCallbacks=");
            printWriter2.println(this.mCallbacks);
            printWriter2.print(str2);
            printWriter2.print("mLoader=");
            printWriter2.println(this.mLoader);
            if (this.mLoader != null) {
                Loader loader = this.mLoader;
                stringBuilder = r10;
                stringBuilder2 = new StringBuilder();
                loader.dump(stringBuilder.append(str2).append("  ").toString(), fileDescriptor2, printWriter2, strArr2);
            }
            if (this.mHaveData || this.mDeliveredData) {
                printWriter2.print(str2);
                printWriter2.print("mHaveData=");
                printWriter2.print(this.mHaveData);
                printWriter2.print("  mDeliveredData=");
                printWriter2.println(this.mDeliveredData);
                printWriter2.print(str2);
                printWriter2.print("mData=");
                printWriter2.println(this.mData);
            }
            printWriter2.print(str2);
            printWriter2.print("mStarted=");
            printWriter2.print(this.mStarted);
            printWriter2.print(" mReportNextStart=");
            printWriter2.print(this.mReportNextStart);
            printWriter2.print(" mDestroyed=");
            printWriter2.println(this.mDestroyed);
            printWriter2.print(str2);
            printWriter2.print("mRetaining=");
            printWriter2.print(this.mRetaining);
            printWriter2.print(" mRetainingStarted=");
            printWriter2.print(this.mRetainingStarted);
            printWriter2.print(" mListenerRegistered=");
            printWriter2.println(this.mListenerRegistered);
            if (this.mPendingLoader != null) {
                printWriter2.print(str2);
                printWriter2.println("Pending Loader ");
                printWriter2.print(this.mPendingLoader);
                printWriter2.println(":");
                LoaderInfo loaderInfo = this.mPendingLoader;
                stringBuilder = r10;
                stringBuilder2 = new StringBuilder();
                loaderInfo.dump(stringBuilder.append(str2).append("  ").toString(), fileDescriptor2, printWriter2, strArr2);
            }
        }
    }

    LoaderManagerImpl(String str, FragmentActivity fragmentActivity, boolean z) {
        String str2 = str;
        FragmentActivity fragmentActivity2 = fragmentActivity;
        boolean z2 = z;
        SparseArrayCompat sparseArrayCompat = r7;
        SparseArrayCompat sparseArrayCompat2 = new SparseArrayCompat();
        this.mLoaders = sparseArrayCompat;
        sparseArrayCompat = r7;
        sparseArrayCompat2 = new SparseArrayCompat();
        this.mInactiveLoaders = sparseArrayCompat;
        this.mWho = str2;
        this.mActivity = fragmentActivity2;
        this.mStarted = z2;
    }

    /* access modifiers changed from: 0000 */
    public void updateActivity(FragmentActivity fragmentActivity) {
        this.mActivity = fragmentActivity;
    }

    private LoaderInfo createLoader(int i, Bundle bundle, LoaderCallbacks<Object> loaderCallbacks) {
        int i2 = i;
        Bundle bundle2 = bundle;
        LoaderCallbacks<Object> loaderCallbacks2 = loaderCallbacks;
        LoaderInfo loaderInfo = r12;
        LoaderInfo loaderInfo2 = new LoaderInfo(this, i2, bundle2, loaderCallbacks2);
        LoaderInfo loaderInfo3 = loaderInfo;
        loaderInfo3.mLoader = loaderCallbacks2.onCreateLoader(i2, bundle2);
        return loaderInfo3;
    }

    private LoaderInfo createAndInstallLoader(int i, Bundle bundle, LoaderCallbacks<Object> loaderCallbacks) {
        LoaderInfo loaderInfo;
        int i2 = i;
        Bundle bundle2 = bundle;
        LoaderCallbacks<Object> loaderCallbacks2 = loaderCallbacks;
        try {
            this.mCreatingLoader = true;
            LoaderInfo createLoader = createLoader(i2, bundle2, loaderCallbacks2);
            installLoader(createLoader);
            loaderInfo = createLoader;
            return this;
        } finally {
            LoaderInfo loaderInfo2 = loaderInfo;
            this.mCreatingLoader = false;
            loaderInfo = loaderInfo2;
        }
    }

    /* access modifiers changed from: 0000 */
    public void installLoader(LoaderInfo loaderInfo) {
        LoaderInfo loaderInfo2 = loaderInfo;
        this.mLoaders.put(loaderInfo2.mId, loaderInfo2);
        if (this.mStarted) {
            loaderInfo2.start();
        }
    }

    public <D> Loader<D> initLoader(int i, Bundle bundle, LoaderCallbacks<D> loaderCallbacks) {
        int i2 = i;
        Bundle bundle2 = bundle;
        LoaderCallbacks<D> loaderCallbacks2 = loaderCallbacks;
        if (this.mCreatingLoader) {
            IllegalStateException illegalStateException = r9;
            IllegalStateException illegalStateException2 = new IllegalStateException("Called while creating a loader");
            throw illegalStateException;
        }
        String str;
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        int v;
        LoaderInfo loaderInfo = (LoaderInfo) this.mLoaders.get(i2);
        if (DEBUG) {
            str = TAG;
            stringBuilder = r9;
            stringBuilder2 = new StringBuilder();
            v = Log.v(str, stringBuilder.append("initLoader in ").append(this).append(": args=").append(bundle2).toString());
        }
        if (loaderInfo == null) {
            loaderInfo = createAndInstallLoader(i2, bundle2, loaderCallbacks2);
            if (DEBUG) {
                str = TAG;
                stringBuilder = r9;
                stringBuilder2 = new StringBuilder();
                v = Log.v(str, stringBuilder.append("  Created new loader ").append(loaderInfo).toString());
            }
        } else {
            if (DEBUG) {
                str = TAG;
                stringBuilder = r9;
                stringBuilder2 = new StringBuilder();
                v = Log.v(str, stringBuilder.append("  Re-using existing loader ").append(loaderInfo).toString());
            }
            loaderInfo.mCallbacks = loaderCallbacks2;
        }
        if (loaderInfo.mHaveData && this.mStarted) {
            loaderInfo.callOnLoadFinished(loaderInfo.mLoader, loaderInfo.mData);
        }
        return loaderInfo.mLoader;
    }

    public <D> Loader<D> restartLoader(int i, Bundle bundle, LoaderCallbacks<D> loaderCallbacks) {
        int i2 = i;
        Bundle bundle2 = bundle;
        LoaderCallbacks<D> loaderCallbacks2 = loaderCallbacks;
        if (this.mCreatingLoader) {
            IllegalStateException illegalStateException = r11;
            IllegalStateException illegalStateException2 = new IllegalStateException("Called while creating a loader");
            throw illegalStateException;
        }
        String str;
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        int v;
        LoaderInfo loaderInfo = (LoaderInfo) this.mLoaders.get(i2);
        if (DEBUG) {
            str = TAG;
            stringBuilder = r11;
            stringBuilder2 = new StringBuilder();
            v = Log.v(str, stringBuilder.append("restartLoader in ").append(this).append(": args=").append(bundle2).toString());
        }
        if (loaderInfo != null) {
            LoaderInfo loaderInfo2 = (LoaderInfo) this.mInactiveLoaders.get(i2);
            if (loaderInfo2 == null) {
                if (DEBUG) {
                    str = TAG;
                    stringBuilder = r11;
                    stringBuilder2 = new StringBuilder();
                    v = Log.v(str, stringBuilder.append("  Making last loader inactive: ").append(loaderInfo).toString());
                }
                loaderInfo.mLoader.abandon();
                this.mInactiveLoaders.put(i2, loaderInfo);
            } else if (loaderInfo.mHaveData) {
                if (DEBUG) {
                    str = TAG;
                    stringBuilder = r11;
                    stringBuilder2 = new StringBuilder();
                    v = Log.v(str, stringBuilder.append("  Removing last inactive loader: ").append(loaderInfo).toString());
                }
                loaderInfo2.mDeliveredData = false;
                loaderInfo2.destroy();
                loaderInfo.mLoader.abandon();
                this.mInactiveLoaders.put(i2, loaderInfo);
            } else if (loaderInfo.mStarted) {
                if (loaderInfo.mPendingLoader != null) {
                    if (DEBUG) {
                        str = TAG;
                        stringBuilder = r11;
                        stringBuilder2 = new StringBuilder();
                        v = Log.v(str, stringBuilder.append("  Removing pending loader: ").append(loaderInfo.mPendingLoader).toString());
                    }
                    loaderInfo.mPendingLoader.destroy();
                    loaderInfo.mPendingLoader = null;
                }
                if (DEBUG) {
                    v = Log.v(TAG, "  Enqueuing as new pending loader");
                }
                loaderInfo.mPendingLoader = createLoader(i2, bundle2, loaderCallbacks2);
                return loaderInfo.mPendingLoader.mLoader;
            } else {
                if (DEBUG) {
                    v = Log.v(TAG, "  Current loader is stopped; replacing");
                }
                this.mLoaders.put(i2, null);
                loaderInfo.destroy();
            }
        }
        return createAndInstallLoader(i2, bundle2, loaderCallbacks2).mLoader;
    }

    public void destroyLoader(int i) {
        int i2 = i;
        if (this.mCreatingLoader) {
            IllegalStateException illegalStateException = r7;
            IllegalStateException illegalStateException2 = new IllegalStateException("Called while creating a loader");
            throw illegalStateException;
        }
        LoaderInfo loaderInfo;
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = r7;
            StringBuilder stringBuilder2 = new StringBuilder();
            int v = Log.v(str, stringBuilder.append("destroyLoader in ").append(this).append(" of ").append(i2).toString());
        }
        int indexOfKey = this.mLoaders.indexOfKey(i2);
        if (indexOfKey >= 0) {
            loaderInfo = (LoaderInfo) this.mLoaders.valueAt(indexOfKey);
            this.mLoaders.removeAt(indexOfKey);
            loaderInfo.destroy();
        }
        indexOfKey = this.mInactiveLoaders.indexOfKey(i2);
        if (indexOfKey >= 0) {
            loaderInfo = (LoaderInfo) this.mInactiveLoaders.valueAt(indexOfKey);
            this.mInactiveLoaders.removeAt(indexOfKey);
            loaderInfo.destroy();
        }
        if (this.mActivity != null && !hasRunningLoaders()) {
            this.mActivity.mFragments.startPendingDeferredFragments();
        }
    }

    public <D> Loader<D> getLoader(int i) {
        int i2 = i;
        if (this.mCreatingLoader) {
            IllegalStateException illegalStateException = r6;
            IllegalStateException illegalStateException2 = new IllegalStateException("Called while creating a loader");
            throw illegalStateException;
        }
        LoaderInfo loaderInfo = (LoaderInfo) this.mLoaders.get(i2);
        if (loaderInfo == null) {
            return null;
        }
        if (loaderInfo.mPendingLoader != null) {
            return loaderInfo.mPendingLoader.mLoader;
        }
        return loaderInfo.mLoader;
    }

    /* access modifiers changed from: 0000 */
    public void doStart() {
        String str;
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        int v;
        if (DEBUG) {
            str = TAG;
            stringBuilder = r5;
            stringBuilder2 = new StringBuilder();
            v = Log.v(str, stringBuilder.append("Starting in ").append(this).toString());
        }
        if (this.mStarted) {
            Throwable th = r5;
            Throwable runtimeException = new RuntimeException("here");
            Throwable th2 = th;
            th = th2.fillInStackTrace();
            str = TAG;
            stringBuilder = r5;
            stringBuilder2 = new StringBuilder();
            v = Log.w(str, stringBuilder.append("Called doStart when already started: ").append(this).toString(), th2);
            return;
        }
        this.mStarted = true;
        for (int size = this.mLoaders.size() - 1; size >= 0; size--) {
            ((LoaderInfo) this.mLoaders.valueAt(size)).start();
        }
    }

    /* access modifiers changed from: 0000 */
    public void doStop() {
        String str;
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        int v;
        if (DEBUG) {
            str = TAG;
            stringBuilder = r5;
            stringBuilder2 = new StringBuilder();
            v = Log.v(str, stringBuilder.append("Stopping in ").append(this).toString());
        }
        if (this.mStarted) {
            for (int size = this.mLoaders.size() - 1; size >= 0; size--) {
                ((LoaderInfo) this.mLoaders.valueAt(size)).stop();
            }
            this.mStarted = false;
            return;
        }
        Throwable th = r5;
        Throwable runtimeException = new RuntimeException("here");
        Throwable th2 = th;
        th = th2.fillInStackTrace();
        str = TAG;
        stringBuilder = r5;
        stringBuilder2 = new StringBuilder();
        v = Log.w(str, stringBuilder.append("Called doStop when not started: ").append(this).toString(), th2);
    }

    /* access modifiers changed from: 0000 */
    public void doRetain() {
        String str;
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        int v;
        if (DEBUG) {
            str = TAG;
            stringBuilder = r5;
            stringBuilder2 = new StringBuilder();
            v = Log.v(str, stringBuilder.append("Retaining in ").append(this).toString());
        }
        if (this.mStarted) {
            this.mRetaining = true;
            this.mStarted = false;
            for (int size = this.mLoaders.size() - 1; size >= 0; size--) {
                ((LoaderInfo) this.mLoaders.valueAt(size)).retain();
            }
            return;
        }
        Throwable th = r5;
        Throwable runtimeException = new RuntimeException("here");
        Throwable th2 = th;
        th = th2.fillInStackTrace();
        str = TAG;
        stringBuilder = r5;
        stringBuilder2 = new StringBuilder();
        v = Log.w(str, stringBuilder.append("Called doRetain when not started: ").append(this).toString(), th2);
    }

    /* access modifiers changed from: 0000 */
    public void finishRetain() {
        if (this.mRetaining) {
            if (DEBUG) {
                String str = TAG;
                StringBuilder stringBuilder = r5;
                StringBuilder stringBuilder2 = new StringBuilder();
                int v = Log.v(str, stringBuilder.append("Finished Retaining in ").append(this).toString());
            }
            this.mRetaining = false;
            for (int size = this.mLoaders.size() - 1; size >= 0; size--) {
                ((LoaderInfo) this.mLoaders.valueAt(size)).finishRetain();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void doReportNextStart() {
        for (int size = this.mLoaders.size() - 1; size >= 0; size--) {
            ((LoaderInfo) this.mLoaders.valueAt(size)).mReportNextStart = true;
        }
    }

    /* access modifiers changed from: 0000 */
    public void doReportStart() {
        for (int size = this.mLoaders.size() - 1; size >= 0; size--) {
            ((LoaderInfo) this.mLoaders.valueAt(size)).reportStart();
        }
    }

    /* access modifiers changed from: 0000 */
    public void doDestroy() {
        String str;
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        int v;
        int size;
        if (!this.mRetaining) {
            if (DEBUG) {
                str = TAG;
                stringBuilder = r5;
                stringBuilder2 = new StringBuilder();
                v = Log.v(str, stringBuilder.append("Destroying Active in ").append(this).toString());
            }
            for (size = this.mLoaders.size() - 1; size >= 0; size--) {
                ((LoaderInfo) this.mLoaders.valueAt(size)).destroy();
            }
            this.mLoaders.clear();
        }
        if (DEBUG) {
            str = TAG;
            stringBuilder = r5;
            stringBuilder2 = new StringBuilder();
            v = Log.v(str, stringBuilder.append("Destroying Inactive in ").append(this).toString());
        }
        for (size = this.mInactiveLoaders.size() - 1; size >= 0; size--) {
            ((LoaderInfo) this.mInactiveLoaders.valueAt(size)).destroy();
        }
        this.mInactiveLoaders.clear();
    }

    public String toString() {
        StringBuilder stringBuilder = r5;
        StringBuilder stringBuilder2 = new StringBuilder(128);
        StringBuilder stringBuilder3 = stringBuilder;
        stringBuilder = stringBuilder3.append("LoaderManager{");
        stringBuilder = stringBuilder3.append(Integer.toHexString(System.identityHashCode(this)));
        stringBuilder = stringBuilder3.append(" in ");
        DebugUtils.buildShortClassTag(this.mActivity, stringBuilder3);
        stringBuilder = stringBuilder3.append("}}");
        return stringBuilder3.toString();
    }

    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        String stringBuilder3;
        int i;
        LoaderInfo loaderInfo;
        String str2 = str;
        FileDescriptor fileDescriptor2 = fileDescriptor;
        PrintWriter printWriter2 = printWriter;
        String[] strArr2 = strArr;
        if (this.mLoaders.size() > 0) {
            printWriter2.print(str2);
            printWriter2.println("Active Loaders:");
            stringBuilder = r13;
            stringBuilder2 = new StringBuilder();
            stringBuilder3 = stringBuilder.append(str2).append("    ").toString();
            for (i = 0; i < this.mLoaders.size(); i++) {
                loaderInfo = (LoaderInfo) this.mLoaders.valueAt(i);
                printWriter2.print(str2);
                printWriter2.print("  #");
                printWriter2.print(this.mLoaders.keyAt(i));
                printWriter2.print(": ");
                printWriter2.println(loaderInfo.toString());
                loaderInfo.dump(stringBuilder3, fileDescriptor2, printWriter2, strArr2);
            }
        }
        if (this.mInactiveLoaders.size() > 0) {
            printWriter2.print(str2);
            printWriter2.println("Inactive Loaders:");
            stringBuilder = r13;
            stringBuilder2 = new StringBuilder();
            stringBuilder3 = stringBuilder.append(str2).append("    ").toString();
            for (i = 0; i < this.mInactiveLoaders.size(); i++) {
                loaderInfo = (LoaderInfo) this.mInactiveLoaders.valueAt(i);
                printWriter2.print(str2);
                printWriter2.print("  #");
                printWriter2.print(this.mInactiveLoaders.keyAt(i));
                printWriter2.print(": ");
                printWriter2.println(loaderInfo.toString());
                loaderInfo.dump(stringBuilder3, fileDescriptor2, printWriter2, strArr2);
            }
        }
    }

    public boolean hasRunningLoaders() {
        boolean z = false;
        int size = this.mLoaders.size();
        for (int i = 0; i < size; i++) {
            LoaderInfo loaderInfo = (LoaderInfo) this.mLoaders.valueAt(i);
            boolean z2 = z;
            int i2 = (!loaderInfo.mStarted || loaderInfo.mDeliveredData) ? 0 : 1;
            z = z2 | i2;
        }
        return z;
    }
}
