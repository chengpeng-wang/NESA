package android.support.v4.content;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.support.v4.util.DebugUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class Loader<D> {
    boolean mAbandoned = false;
    boolean mContentChanged = false;
    Context mContext;
    int mId;
    OnLoadCompleteListener<D> mListener;
    boolean mProcessingChange = false;
    boolean mReset = true;
    boolean mStarted = false;

    public final class ForceLoadContentObserver extends ContentObserver {
        final /* synthetic */ Loader this$0;

        public ForceLoadContentObserver(Loader loader) {
            this.this$0 = loader;
            Handler handler = r5;
            Handler handler2 = new Handler();
            super(handler);
        }

        public boolean deliverSelfNotifications() {
            return true;
        }

        public void onChange(boolean z) {
            boolean z2 = z;
            this.this$0.onContentChanged();
        }
    }

    public interface OnLoadCompleteListener<D> {
        void onLoadComplete(Loader<D> loader, D d);
    }

    public Loader(Context context) {
        Context context2 = context;
        this.mContext = context2.getApplicationContext();
    }

    public void deliverResult(D d) {
        D d2 = d;
        if (this.mListener != null) {
            this.mListener.onLoadComplete(this, d2);
        }
    }

    public Context getContext() {
        return this.mContext;
    }

    public int getId() {
        return this.mId;
    }

    public void registerListener(int i, OnLoadCompleteListener<D> onLoadCompleteListener) {
        int i2 = i;
        OnLoadCompleteListener<D> onLoadCompleteListener2 = onLoadCompleteListener;
        if (this.mListener != null) {
            IllegalStateException illegalStateException = r6;
            IllegalStateException illegalStateException2 = new IllegalStateException("There is already a listener registered");
            throw illegalStateException;
        }
        this.mListener = onLoadCompleteListener2;
        this.mId = i2;
    }

    public void unregisterListener(OnLoadCompleteListener<D> onLoadCompleteListener) {
        OnLoadCompleteListener<D> onLoadCompleteListener2 = onLoadCompleteListener;
        if (this.mListener == null) {
            IllegalStateException illegalStateException = r5;
            IllegalStateException illegalStateException2 = new IllegalStateException("No listener register");
            throw illegalStateException;
        } else if (this.mListener != onLoadCompleteListener2) {
            IllegalArgumentException illegalArgumentException = r5;
            IllegalArgumentException illegalArgumentException2 = new IllegalArgumentException("Attempting to unregister the wrong listener");
            throw illegalArgumentException;
        } else {
            this.mListener = null;
        }
    }

    public boolean isStarted() {
        return this.mStarted;
    }

    public boolean isAbandoned() {
        return this.mAbandoned;
    }

    public boolean isReset() {
        return this.mReset;
    }

    public final void startLoading() {
        this.mStarted = true;
        this.mReset = false;
        this.mAbandoned = false;
        onStartLoading();
    }

    /* access modifiers changed from: protected */
    public void onStartLoading() {
    }

    public void forceLoad() {
        onForceLoad();
    }

    /* access modifiers changed from: protected */
    public void onForceLoad() {
    }

    public void stopLoading() {
        this.mStarted = false;
        onStopLoading();
    }

    /* access modifiers changed from: protected */
    public void onStopLoading() {
    }

    public void abandon() {
        this.mAbandoned = true;
        onAbandon();
    }

    /* access modifiers changed from: protected */
    public void onAbandon() {
    }

    public void reset() {
        onReset();
        this.mReset = true;
        this.mStarted = false;
        this.mAbandoned = false;
        this.mContentChanged = false;
        this.mProcessingChange = false;
    }

    /* access modifiers changed from: protected */
    public void onReset() {
    }

    public boolean takeContentChanged() {
        boolean z = this.mContentChanged;
        this.mContentChanged = false;
        this.mProcessingChange |= z;
        return z;
    }

    public void commitContentChanged() {
        this.mProcessingChange = false;
    }

    public void rollbackContentChanged() {
        if (this.mProcessingChange) {
            this.mContentChanged = true;
        }
    }

    public void onContentChanged() {
        if (this.mStarted) {
            forceLoad();
        } else {
            this.mContentChanged = true;
        }
    }

    public String dataToString(D d) {
        D d2 = d;
        StringBuilder stringBuilder = r6;
        StringBuilder stringBuilder2 = new StringBuilder(64);
        StringBuilder stringBuilder3 = stringBuilder;
        DebugUtils.buildShortClassTag(d2, stringBuilder3);
        stringBuilder = stringBuilder3.append("}");
        return stringBuilder3.toString();
    }

    public String toString() {
        StringBuilder stringBuilder = r5;
        StringBuilder stringBuilder2 = new StringBuilder(64);
        StringBuilder stringBuilder3 = stringBuilder;
        DebugUtils.buildShortClassTag(this, stringBuilder3);
        stringBuilder = stringBuilder3.append(" id=");
        stringBuilder = stringBuilder3.append(this.mId);
        stringBuilder = stringBuilder3.append("}");
        return stringBuilder3.toString();
    }

    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        String str2 = str;
        FileDescriptor fileDescriptor2 = fileDescriptor;
        PrintWriter printWriter2 = printWriter;
        String[] strArr2 = strArr;
        printWriter2.print(str2);
        printWriter2.print("mId=");
        printWriter2.print(this.mId);
        printWriter2.print(" mListener=");
        printWriter2.println(this.mListener);
        if (this.mStarted || this.mContentChanged || this.mProcessingChange) {
            printWriter2.print(str2);
            printWriter2.print("mStarted=");
            printWriter2.print(this.mStarted);
            printWriter2.print(" mContentChanged=");
            printWriter2.print(this.mContentChanged);
            printWriter2.print(" mProcessingChange=");
            printWriter2.println(this.mProcessingChange);
        }
        if (this.mAbandoned || this.mReset) {
            printWriter2.print(str2);
            printWriter2.print("mAbandoned=");
            printWriter2.print(this.mAbandoned);
            printWriter2.print(" mReset=");
            printWriter2.println(this.mReset);
        }
    }
}
