package android.support.v4.widget;

import android.database.Cursor;
import android.widget.Filter;
import android.widget.Filter.FilterResults;

class CursorFilter extends Filter {
    CursorFilterClient mClient;

    interface CursorFilterClient {
        void changeCursor(Cursor cursor);

        CharSequence convertToString(Cursor cursor);

        Cursor getCursor();

        Cursor runQueryOnBackgroundThread(CharSequence charSequence);
    }

    CursorFilter(CursorFilterClient cursorFilterClient) {
        this.mClient = cursorFilterClient;
    }

    public CharSequence convertResultToString(Object obj) {
        return this.mClient.convertToString((Cursor) obj);
    }

    /* access modifiers changed from: protected */
    public FilterResults performFiltering(CharSequence charSequence) {
        Cursor runQueryOnBackgroundThread = this.mClient.runQueryOnBackgroundThread(charSequence);
        FilterResults filterResults = r6;
        FilterResults filterResults2 = new FilterResults();
        FilterResults filterResults3 = filterResults;
        if (runQueryOnBackgroundThread != null) {
            filterResults3.count = runQueryOnBackgroundThread.getCount();
            filterResults3.values = runQueryOnBackgroundThread;
        } else {
            filterResults3.count = 0;
            filterResults3.values = null;
        }
        return filterResults3;
    }

    /* access modifiers changed from: protected */
    public void publishResults(CharSequence charSequence, FilterResults filterResults) {
        CharSequence charSequence2 = charSequence;
        FilterResults filterResults2 = filterResults;
        Cursor cursor = this.mClient.getCursor();
        if (filterResults2.values != null && filterResults2.values != cursor) {
            this.mClient.changeCursor((Cursor) filterResults2.values);
        }
    }
}
