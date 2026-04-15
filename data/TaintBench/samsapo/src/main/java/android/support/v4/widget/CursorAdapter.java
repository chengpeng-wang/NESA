package android.support.v4.widget;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.FilterQueryProvider;
import android.widget.Filterable;

public abstract class CursorAdapter extends BaseAdapter implements Filterable, CursorFilterClient {
    @Deprecated
    public static final int FLAG_AUTO_REQUERY = 1;
    public static final int FLAG_REGISTER_CONTENT_OBSERVER = 2;
    protected boolean mAutoRequery;
    protected ChangeObserver mChangeObserver;
    protected Context mContext;
    protected Cursor mCursor;
    protected CursorFilter mCursorFilter;
    protected DataSetObserver mDataSetObserver;
    protected boolean mDataValid;
    protected FilterQueryProvider mFilterQueryProvider;
    protected int mRowIDColumn;

    private class ChangeObserver extends ContentObserver {
        final /* synthetic */ CursorAdapter this$0;

        public ChangeObserver(CursorAdapter cursorAdapter) {
            this.this$0 = cursorAdapter;
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

    private class MyDataSetObserver extends DataSetObserver {
        final /* synthetic */ CursorAdapter this$0;

        private MyDataSetObserver(CursorAdapter cursorAdapter) {
            this.this$0 = cursorAdapter;
        }

        /* synthetic */ MyDataSetObserver(CursorAdapter cursorAdapter, AnonymousClass1 anonymousClass1) {
            AnonymousClass1 anonymousClass12 = anonymousClass1;
            this(cursorAdapter);
        }

        public void onChanged() {
            this.this$0.mDataValid = true;
            this.this$0.notifyDataSetChanged();
        }

        public void onInvalidated() {
            this.this$0.mDataValid = false;
            this.this$0.notifyDataSetInvalidated();
        }
    }

    public abstract void bindView(View view, Context context, Cursor cursor);

    public abstract View newView(Context context, Cursor cursor, ViewGroup viewGroup);

    @Deprecated
    public CursorAdapter(Context context, Cursor cursor) {
        init(context, cursor, 1);
    }

    public CursorAdapter(Context context, Cursor cursor, boolean z) {
        init(context, cursor, z ? 1 : 2);
    }

    public CursorAdapter(Context context, Cursor cursor, int i) {
        init(context, cursor, i);
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public void init(Context context, Cursor cursor, boolean z) {
        init(context, cursor, z ? 1 : 2);
    }

    /* access modifiers changed from: 0000 */
    public void init(Context context, Cursor cursor, int i) {
        Context context2 = context;
        Cursor cursor2 = cursor;
        int i2 = i;
        if ((i2 & 1) == 1) {
            i2 |= 2;
            this.mAutoRequery = true;
        } else {
            this.mAutoRequery = false;
        }
        boolean z = cursor2 != null;
        this.mCursor = cursor2;
        this.mDataValid = z;
        this.mContext = context2;
        this.mRowIDColumn = z ? cursor2.getColumnIndexOrThrow("_id") : -1;
        if ((i2 & 2) == 2) {
            ChangeObserver changeObserver = r10;
            ChangeObserver changeObserver2 = new ChangeObserver(this);
            this.mChangeObserver = changeObserver;
            DataSetObserver dataSetObserver = r10;
            DataSetObserver myDataSetObserver = new MyDataSetObserver(this, null);
            this.mDataSetObserver = dataSetObserver;
        } else {
            this.mChangeObserver = null;
            this.mDataSetObserver = null;
        }
        if (z) {
            if (this.mChangeObserver != null) {
                cursor2.registerContentObserver(this.mChangeObserver);
            }
            if (this.mDataSetObserver != null) {
                cursor2.registerDataSetObserver(this.mDataSetObserver);
            }
        }
    }

    public Cursor getCursor() {
        return this.mCursor;
    }

    public int getCount() {
        if (!this.mDataValid || this.mCursor == null) {
            return 0;
        }
        return this.mCursor.getCount();
    }

    public Object getItem(int i) {
        int i2 = i;
        if (!this.mDataValid || this.mCursor == null) {
            return null;
        }
        boolean moveToPosition = this.mCursor.moveToPosition(i2);
        return this.mCursor;
    }

    public long getItemId(int i) {
        int i2 = i;
        if (!this.mDataValid || this.mCursor == null) {
            return 0;
        }
        if (this.mCursor.moveToPosition(i2)) {
            return this.mCursor.getLong(this.mRowIDColumn);
        }
        return 0;
    }

    public boolean hasStableIds() {
        return true;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        int i2 = i;
        View view2 = view;
        ViewGroup viewGroup2 = viewGroup;
        IllegalStateException illegalStateException;
        IllegalStateException illegalStateException2;
        if (!this.mDataValid) {
            illegalStateException = r9;
            illegalStateException2 = new IllegalStateException("this should only be called when the cursor is valid");
            throw illegalStateException;
        } else if (this.mCursor.moveToPosition(i2)) {
            View newView;
            if (view2 == null) {
                newView = newView(this.mContext, this.mCursor, viewGroup2);
            } else {
                newView = view2;
            }
            bindView(newView, this.mContext, this.mCursor);
            return newView;
        } else {
            illegalStateException = r9;
            StringBuilder stringBuilder = r9;
            StringBuilder stringBuilder2 = new StringBuilder();
            illegalStateException2 = new IllegalStateException(stringBuilder.append("couldn't move cursor to position ").append(i2).toString());
            throw illegalStateException;
        }
    }

    public View getDropDownView(int i, View view, ViewGroup viewGroup) {
        int i2 = i;
        View view2 = view;
        ViewGroup viewGroup2 = viewGroup;
        if (!this.mDataValid) {
            return null;
        }
        View newDropDownView;
        boolean moveToPosition = this.mCursor.moveToPosition(i2);
        if (view2 == null) {
            newDropDownView = newDropDownView(this.mContext, this.mCursor, viewGroup2);
        } else {
            newDropDownView = view2;
        }
        bindView(newDropDownView, this.mContext, this.mCursor);
        return newDropDownView;
    }

    public View newDropDownView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return newView(context, cursor, viewGroup);
    }

    public void changeCursor(Cursor cursor) {
        Cursor swapCursor = swapCursor(cursor);
        if (swapCursor != null) {
            swapCursor.close();
        }
    }

    public Cursor swapCursor(Cursor cursor) {
        Cursor cursor2 = cursor;
        if (cursor2 == this.mCursor) {
            return null;
        }
        Cursor cursor3 = this.mCursor;
        if (cursor3 != null) {
            if (this.mChangeObserver != null) {
                cursor3.unregisterContentObserver(this.mChangeObserver);
            }
            if (this.mDataSetObserver != null) {
                cursor3.unregisterDataSetObserver(this.mDataSetObserver);
            }
        }
        this.mCursor = cursor2;
        if (cursor2 != null) {
            if (this.mChangeObserver != null) {
                cursor2.registerContentObserver(this.mChangeObserver);
            }
            if (this.mDataSetObserver != null) {
                cursor2.registerDataSetObserver(this.mDataSetObserver);
            }
            this.mRowIDColumn = cursor2.getColumnIndexOrThrow("_id");
            this.mDataValid = true;
            notifyDataSetChanged();
        } else {
            this.mRowIDColumn = -1;
            this.mDataValid = false;
            notifyDataSetInvalidated();
        }
        return cursor3;
    }

    public CharSequence convertToString(Cursor cursor) {
        Cursor cursor2 = cursor;
        return cursor2 == null ? "" : cursor2.toString();
    }

    public Cursor runQueryOnBackgroundThread(CharSequence charSequence) {
        CharSequence charSequence2 = charSequence;
        if (this.mFilterQueryProvider != null) {
            return this.mFilterQueryProvider.runQuery(charSequence2);
        }
        return this.mCursor;
    }

    public Filter getFilter() {
        if (this.mCursorFilter == null) {
            CursorFilter cursorFilter = r5;
            CursorFilter cursorFilter2 = new CursorFilter(this);
            this.mCursorFilter = cursorFilter;
        }
        return this.mCursorFilter;
    }

    public FilterQueryProvider getFilterQueryProvider() {
        return this.mFilterQueryProvider;
    }

    public void setFilterQueryProvider(FilterQueryProvider filterQueryProvider) {
        this.mFilterQueryProvider = filterQueryProvider;
    }

    /* access modifiers changed from: protected */
    public void onContentChanged() {
        if (this.mAutoRequery && this.mCursor != null && !this.mCursor.isClosed()) {
            this.mDataValid = this.mCursor.requery();
        }
    }
}
