package android.support.v4.content;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.Loader.ForceLoadContentObserver;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Arrays;

public class CursorLoader extends AsyncTaskLoader<Cursor> {
    Cursor mCursor;
    final ForceLoadContentObserver mObserver;
    String[] mProjection;
    String mSelection;
    String[] mSelectionArgs;
    String mSortOrder;
    Uri mUri;

    public Cursor loadInBackground() {
        Cursor query = getContext().getContentResolver().query(this.mUri, this.mProjection, this.mSelection, this.mSelectionArgs, this.mSortOrder);
        if (query != null) {
            int count = query.getCount();
            query.registerContentObserver(this.mObserver);
        }
        return query;
    }

    public void deliverResult(Cursor cursor) {
        Cursor cursor2 = cursor;
        if (!isReset()) {
            Cursor cursor3 = this.mCursor;
            this.mCursor = cursor2;
            if (isStarted()) {
                super.deliverResult(cursor2);
            }
            if (cursor3 != null && cursor3 != cursor2 && !cursor3.isClosed()) {
                cursor3.close();
            }
        } else if (cursor2 != null) {
            cursor2.close();
        }
    }

    public CursorLoader(Context context) {
        super(context);
        ForceLoadContentObserver forceLoadContentObserver = r6;
        ForceLoadContentObserver forceLoadContentObserver2 = new ForceLoadContentObserver(this);
        this.mObserver = forceLoadContentObserver;
    }

    public CursorLoader(Context context, Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        Uri uri2 = uri;
        String[] strArr3 = strArr;
        String str3 = str;
        String[] strArr4 = strArr2;
        String str4 = str2;
        super(context);
        ForceLoadContentObserver forceLoadContentObserver = r11;
        ForceLoadContentObserver forceLoadContentObserver2 = new ForceLoadContentObserver(this);
        this.mObserver = forceLoadContentObserver;
        this.mUri = uri2;
        this.mProjection = strArr3;
        this.mSelection = str3;
        this.mSelectionArgs = strArr4;
        this.mSortOrder = str4;
    }

    /* access modifiers changed from: protected */
    public void onStartLoading() {
        if (this.mCursor != null) {
            deliverResult(this.mCursor);
        }
        if (takeContentChanged() || this.mCursor == null) {
            forceLoad();
        }
    }

    /* access modifiers changed from: protected */
    public void onStopLoading() {
        boolean cancelLoad = cancelLoad();
    }

    public void onCanceled(Cursor cursor) {
        Cursor cursor2 = cursor;
        if (cursor2 != null && !cursor2.isClosed()) {
            cursor2.close();
        }
    }

    /* access modifiers changed from: protected */
    public void onReset() {
        super.onReset();
        onStopLoading();
        if (!(this.mCursor == null || this.mCursor.isClosed())) {
            this.mCursor.close();
        }
        this.mCursor = null;
    }

    public Uri getUri() {
        return this.mUri;
    }

    public void setUri(Uri uri) {
        this.mUri = uri;
    }

    public String[] getProjection() {
        return this.mProjection;
    }

    public void setProjection(String[] strArr) {
        this.mProjection = strArr;
    }

    public String getSelection() {
        return this.mSelection;
    }

    public void setSelection(String str) {
        this.mSelection = str;
    }

    public String[] getSelectionArgs() {
        return this.mSelectionArgs;
    }

    public void setSelectionArgs(String[] strArr) {
        this.mSelectionArgs = strArr;
    }

    public String getSortOrder() {
        return this.mSortOrder;
    }

    public void setSortOrder(String str) {
        this.mSortOrder = str;
    }

    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        String str2 = str;
        PrintWriter printWriter2 = printWriter;
        super.dump(str2, fileDescriptor, printWriter2, strArr);
        printWriter2.print(str2);
        printWriter2.print("mUri=");
        printWriter2.println(this.mUri);
        printWriter2.print(str2);
        printWriter2.print("mProjection=");
        printWriter2.println(Arrays.toString(this.mProjection));
        printWriter2.print(str2);
        printWriter2.print("mSelection=");
        printWriter2.println(this.mSelection);
        printWriter2.print(str2);
        printWriter2.print("mSelectionArgs=");
        printWriter2.println(Arrays.toString(this.mSelectionArgs));
        printWriter2.print(str2);
        printWriter2.print("mSortOrder=");
        printWriter2.println(this.mSortOrder);
        printWriter2.print(str2);
        printWriter2.print("mCursor=");
        printWriter2.println(this.mCursor);
        printWriter2.print(str2);
        printWriter2.print("mContentChanged=");
        printWriter2.println(this.mContentChanged);
    }
}
