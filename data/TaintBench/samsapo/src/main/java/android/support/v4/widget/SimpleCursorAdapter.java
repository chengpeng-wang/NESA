package android.support.v4.widget;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SimpleCursorAdapter extends ResourceCursorAdapter {
    private CursorToStringConverter mCursorToStringConverter;
    protected int[] mFrom;
    String[] mOriginalFrom;
    private int mStringConversionColumn = -1;
    protected int[] mTo;
    private ViewBinder mViewBinder;

    public interface CursorToStringConverter {
        CharSequence convertToString(Cursor cursor);
    }

    public interface ViewBinder {
        boolean setViewValue(View view, Cursor cursor, int i);
    }

    @Deprecated
    public SimpleCursorAdapter(Context context, int i, Cursor cursor, String[] strArr, int[] iArr) {
        String[] strArr2 = strArr;
        int[] iArr2 = iArr;
        super(context, i, cursor);
        this.mTo = iArr2;
        this.mOriginalFrom = strArr2;
        findColumns(strArr2);
    }

    public SimpleCursorAdapter(Context context, int i, Cursor cursor, String[] strArr, int[] iArr, int i2) {
        String[] strArr2 = strArr;
        int[] iArr2 = iArr;
        super(context, i, cursor, i2);
        this.mTo = iArr2;
        this.mOriginalFrom = strArr2;
        findColumns(strArr2);
    }

    public void bindView(View view, Context context, Cursor cursor) {
        View view2 = view;
        Context context2 = context;
        Cursor cursor2 = cursor;
        ViewBinder viewBinder = this.mViewBinder;
        int length = this.mTo.length;
        int[] iArr = this.mFrom;
        int[] iArr2 = this.mTo;
        for (int i = 0; i < length; i++) {
            View findViewById = view2.findViewById(iArr2[i]);
            if (findViewById != null) {
                boolean z = false;
                if (viewBinder != null) {
                    z = viewBinder.setViewValue(findViewById, cursor2, iArr[i]);
                }
                if (z) {
                    continue;
                } else {
                    String string = cursor2.getString(iArr[i]);
                    if (string == null) {
                        string = "";
                    }
                    if (findViewById instanceof TextView) {
                        setViewText((TextView) findViewById, string);
                    } else if (findViewById instanceof ImageView) {
                        setViewImage((ImageView) findViewById, string);
                    } else {
                        IllegalStateException illegalStateException = r17;
                        StringBuilder stringBuilder = r17;
                        StringBuilder stringBuilder2 = new StringBuilder();
                        IllegalStateException illegalStateException2 = new IllegalStateException(stringBuilder.append(findViewById.getClass().getName()).append(" is not a ").append(" view that can be bounds by this SimpleCursorAdapter").toString());
                        throw illegalStateException;
                    }
                }
            }
        }
    }

    public ViewBinder getViewBinder() {
        return this.mViewBinder;
    }

    public void setViewBinder(ViewBinder viewBinder) {
        this.mViewBinder = viewBinder;
    }

    public void setViewImage(ImageView imageView, String str) {
        ImageView imageView2 = imageView;
        String str2 = str;
        try {
            imageView2.setImageResource(Integer.parseInt(str2));
        } catch (NumberFormatException e) {
            NumberFormatException numberFormatException = e;
            imageView2.setImageURI(Uri.parse(str2));
        }
    }

    public void setViewText(TextView textView, String str) {
        textView.setText(str);
    }

    public int getStringConversionColumn() {
        return this.mStringConversionColumn;
    }

    public void setStringConversionColumn(int i) {
        this.mStringConversionColumn = i;
    }

    public CursorToStringConverter getCursorToStringConverter() {
        return this.mCursorToStringConverter;
    }

    public void setCursorToStringConverter(CursorToStringConverter cursorToStringConverter) {
        this.mCursorToStringConverter = cursorToStringConverter;
    }

    public CharSequence convertToString(Cursor cursor) {
        Cursor cursor2 = cursor;
        if (this.mCursorToStringConverter != null) {
            return this.mCursorToStringConverter.convertToString(cursor2);
        }
        if (this.mStringConversionColumn > -1) {
            return cursor2.getString(this.mStringConversionColumn);
        }
        return super.convertToString(cursor2);
    }

    private void findColumns(String[] strArr) {
        String[] strArr2 = strArr;
        if (this.mCursor != null) {
            int length = strArr2.length;
            if (this.mFrom == null || this.mFrom.length != length) {
                this.mFrom = new int[length];
            }
            for (int i = 0; i < length; i++) {
                this.mFrom[i] = this.mCursor.getColumnIndexOrThrow(strArr2[i]);
            }
            return;
        }
        this.mFrom = null;
    }

    public Cursor swapCursor(Cursor cursor) {
        Cursor swapCursor = super.swapCursor(cursor);
        findColumns(this.mOriginalFrom);
        return swapCursor;
    }

    public void changeCursorAndColumns(Cursor cursor, String[] strArr, int[] iArr) {
        Cursor cursor2 = cursor;
        int[] iArr2 = iArr;
        this.mOriginalFrom = strArr;
        this.mTo = iArr2;
        super.changeCursor(cursor2);
        findColumns(this.mOriginalFrom);
    }
}
