package android.support.v4.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

/* compiled from: FragmentManager */
final class FragmentManagerState implements Parcelable {
    public static final Creator<FragmentManagerState> CREATOR;
    FragmentState[] mActive;
    int[] mAdded;
    BackStackState[] mBackStack;

    public FragmentManagerState() {
    }

    public FragmentManagerState(Parcel parcel) {
        Parcel parcel2 = parcel;
        this.mActive = (FragmentState[]) parcel2.createTypedArray(FragmentState.CREATOR);
        this.mAdded = parcel2.createIntArray();
        this.mBackStack = (BackStackState[]) parcel2.createTypedArray(BackStackState.CREATOR);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        Parcel parcel2 = parcel;
        int i2 = i;
        parcel2.writeTypedArray(this.mActive, i2);
        parcel2.writeIntArray(this.mAdded);
        parcel2.writeTypedArray(this.mBackStack, i2);
    }

    static {
        AnonymousClass1 anonymousClass1 = r2;
        AnonymousClass1 anonymousClass12 = new Creator<FragmentManagerState>() {
            public FragmentManagerState createFromParcel(Parcel parcel) {
                FragmentManagerState fragmentManagerState = r5;
                FragmentManagerState fragmentManagerState2 = new FragmentManagerState(parcel);
                return fragmentManagerState;
            }

            public FragmentManagerState[] newArray(int i) {
                return new FragmentManagerState[i];
            }
        };
        CREATOR = anonymousClass1;
    }
}
