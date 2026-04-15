package android.support.v4.widget;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class ResourceCursorAdapter extends CursorAdapter {
    private int mDropDownLayout;
    private LayoutInflater mInflater;
    private int mLayout;

    @Deprecated
    public ResourceCursorAdapter(Context context, int i, Cursor cursor) {
        Context context2 = context;
        int i2 = i;
        super(context2, cursor);
        int i3 = i2;
        int i4 = i3;
        int i5 = i3;
        this.mDropDownLayout = i5;
        this.mLayout = i4;
        this.mInflater = (LayoutInflater) context2.getSystemService("layout_inflater");
    }

    public ResourceCursorAdapter(Context context, int i, Cursor cursor, boolean z) {
        Context context2 = context;
        int i2 = i;
        super(context2, cursor, z);
        int i3 = i2;
        int i4 = i3;
        int i5 = i3;
        this.mDropDownLayout = i5;
        this.mLayout = i4;
        this.mInflater = (LayoutInflater) context2.getSystemService("layout_inflater");
    }

    public ResourceCursorAdapter(Context context, int i, Cursor cursor, int i2) {
        Context context2 = context;
        int i3 = i;
        super(context2, cursor, i2);
        int i4 = i3;
        int i5 = i4;
        int i6 = i4;
        this.mDropDownLayout = i6;
        this.mLayout = i5;
        this.mInflater = (LayoutInflater) context2.getSystemService("layout_inflater");
    }

    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        Context context2 = context;
        Cursor cursor2 = cursor;
        return this.mInflater.inflate(this.mLayout, viewGroup, false);
    }

    public View newDropDownView(Context context, Cursor cursor, ViewGroup viewGroup) {
        Context context2 = context;
        Cursor cursor2 = cursor;
        return this.mInflater.inflate(this.mDropDownLayout, viewGroup, false);
    }

    public void setViewResource(int i) {
        this.mLayout = i;
    }

    public void setDropDownViewResource(int i) {
        this.mDropDownLayout = i;
    }
}
