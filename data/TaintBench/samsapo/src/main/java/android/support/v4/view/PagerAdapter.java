package android.support.v4.view;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;

public abstract class PagerAdapter {
    public static final int POSITION_NONE = -2;
    public static final int POSITION_UNCHANGED = -1;
    private DataSetObservable mObservable;

    public abstract int getCount();

    public abstract boolean isViewFromObject(View view, Object obj);

    public PagerAdapter() {
        DataSetObservable dataSetObservable = r4;
        DataSetObservable dataSetObservable2 = new DataSetObservable();
        this.mObservable = dataSetObservable;
    }

    public void startUpdate(ViewGroup viewGroup) {
        startUpdate((View) viewGroup);
    }

    public Object instantiateItem(ViewGroup viewGroup, int i) {
        return instantiateItem((View) viewGroup, i);
    }

    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        destroyItem((View) viewGroup, i, obj);
    }

    public void setPrimaryItem(ViewGroup viewGroup, int i, Object obj) {
        setPrimaryItem((View) viewGroup, i, obj);
    }

    public void finishUpdate(ViewGroup viewGroup) {
        finishUpdate((View) viewGroup);
    }

    public void startUpdate(View view) {
    }

    public Object instantiateItem(View view, int i) {
        View view2 = view;
        int i2 = i;
        UnsupportedOperationException unsupportedOperationException = r6;
        UnsupportedOperationException unsupportedOperationException2 = new UnsupportedOperationException("Required method instantiateItem was not overridden");
        throw unsupportedOperationException;
    }

    public void destroyItem(View view, int i, Object obj) {
        View view2 = view;
        int i2 = i;
        Object obj2 = obj;
        UnsupportedOperationException unsupportedOperationException = r7;
        UnsupportedOperationException unsupportedOperationException2 = new UnsupportedOperationException("Required method destroyItem was not overridden");
        throw unsupportedOperationException;
    }

    public void setPrimaryItem(View view, int i, Object obj) {
    }

    public void finishUpdate(View view) {
    }

    public Parcelable saveState() {
        return null;
    }

    public void restoreState(Parcelable parcelable, ClassLoader classLoader) {
    }

    public int getItemPosition(Object obj) {
        Object obj2 = obj;
        return -1;
    }

    public void notifyDataSetChanged() {
        this.mObservable.notifyChanged();
    }

    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        this.mObservable.registerObserver(dataSetObserver);
    }

    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        this.mObservable.unregisterObserver(dataSetObserver);
    }

    public CharSequence getPageTitle(int i) {
        int i2 = i;
        return null;
    }

    public float getPageWidth(int i) {
        int i2 = i;
        return 1.0f;
    }
}
