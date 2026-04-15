package android.support.v4.os;

import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class ParcelableCompat {

    static class CompatCreator<T> implements Creator<T> {
        final ParcelableCompatCreatorCallbacks<T> mCallbacks;

        public CompatCreator(ParcelableCompatCreatorCallbacks<T> parcelableCompatCreatorCallbacks) {
            this.mCallbacks = parcelableCompatCreatorCallbacks;
        }

        public T createFromParcel(Parcel parcel) {
            return this.mCallbacks.createFromParcel(parcel, null);
        }

        public T[] newArray(int i) {
            return this.mCallbacks.newArray(i);
        }
    }

    public ParcelableCompat() {
    }

    public static <T> Creator<T> newCreator(ParcelableCompatCreatorCallbacks<T> parcelableCompatCreatorCallbacks) {
        ParcelableCompatCreatorCallbacks<T> parcelableCompatCreatorCallbacks2 = parcelableCompatCreatorCallbacks;
        if (VERSION.SDK_INT >= 13) {
            Creator instantiate = ParcelableCompatCreatorHoneycombMR2Stub.instantiate(parcelableCompatCreatorCallbacks2);
        }
        CompatCreator compatCreator = r4;
        CompatCreator compatCreator2 = new CompatCreator(parcelableCompatCreatorCallbacks2);
        return compatCreator;
    }
}
