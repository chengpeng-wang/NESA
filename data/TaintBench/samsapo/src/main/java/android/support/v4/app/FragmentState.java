package android.support.v4.app;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;

/* compiled from: Fragment */
final class FragmentState implements Parcelable {
    public static final Creator<FragmentState> CREATOR;
    final Bundle mArguments;
    final String mClassName;
    final int mContainerId;
    final boolean mDetached;
    final int mFragmentId;
    final boolean mFromLayout;
    final int mIndex;
    Fragment mInstance;
    final boolean mRetainInstance;
    Bundle mSavedFragmentState;
    final String mTag;

    public FragmentState(Fragment fragment) {
        Fragment fragment2 = fragment;
        this.mClassName = fragment2.getClass().getName();
        this.mIndex = fragment2.mIndex;
        this.mFromLayout = fragment2.mFromLayout;
        this.mFragmentId = fragment2.mFragmentId;
        this.mContainerId = fragment2.mContainerId;
        this.mTag = fragment2.mTag;
        this.mRetainInstance = fragment2.mRetainInstance;
        this.mDetached = fragment2.mDetached;
        this.mArguments = fragment2.mArguments;
    }

    public FragmentState(Parcel parcel) {
        Parcel parcel2 = parcel;
        this.mClassName = parcel2.readString();
        this.mIndex = parcel2.readInt();
        this.mFromLayout = parcel2.readInt() != 0;
        this.mFragmentId = parcel2.readInt();
        this.mContainerId = parcel2.readInt();
        this.mTag = parcel2.readString();
        this.mRetainInstance = parcel2.readInt() != 0;
        this.mDetached = parcel2.readInt() != 0;
        this.mArguments = parcel2.readBundle();
        this.mSavedFragmentState = parcel2.readBundle();
    }

    public Fragment instantiate(FragmentActivity fragmentActivity, Fragment fragment) {
        Context context = fragmentActivity;
        Fragment fragment2 = fragment;
        if (this.mInstance != null) {
            return this.mInstance;
        }
        if (this.mArguments != null) {
            this.mArguments.setClassLoader(context.getClassLoader());
        }
        this.mInstance = Fragment.instantiate(context, this.mClassName, this.mArguments);
        if (this.mSavedFragmentState != null) {
            this.mSavedFragmentState.setClassLoader(context.getClassLoader());
            this.mInstance.mSavedFragmentState = this.mSavedFragmentState;
        }
        this.mInstance.setIndex(this.mIndex, fragment2);
        this.mInstance.mFromLayout = this.mFromLayout;
        this.mInstance.mRestored = true;
        this.mInstance.mFragmentId = this.mFragmentId;
        this.mInstance.mContainerId = this.mContainerId;
        this.mInstance.mTag = this.mTag;
        this.mInstance.mRetainInstance = this.mRetainInstance;
        this.mInstance.mDetached = this.mDetached;
        this.mInstance.mFragmentManager = context.mFragments;
        if (FragmentManagerImpl.DEBUG) {
            StringBuilder stringBuilder = r7;
            StringBuilder stringBuilder2 = new StringBuilder();
            int v = Log.v("FragmentManager", stringBuilder.append("Instantiated fragment ").append(this.mInstance).toString());
        }
        return this.mInstance;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        Parcel parcel2 = parcel;
        int i2 = i;
        parcel2.writeString(this.mClassName);
        parcel2.writeInt(this.mIndex);
        parcel2.writeInt(this.mFromLayout ? 1 : 0);
        parcel2.writeInt(this.mFragmentId);
        parcel2.writeInt(this.mContainerId);
        parcel2.writeString(this.mTag);
        parcel2.writeInt(this.mRetainInstance ? 1 : 0);
        parcel2.writeInt(this.mDetached ? 1 : 0);
        parcel2.writeBundle(this.mArguments);
        parcel2.writeBundle(this.mSavedFragmentState);
    }

    static {
        AnonymousClass1 anonymousClass1 = r2;
        AnonymousClass1 anonymousClass12 = new Creator<FragmentState>() {
            public FragmentState createFromParcel(Parcel parcel) {
                FragmentState fragmentState = r5;
                FragmentState fragmentState2 = new FragmentState(parcel);
                return fragmentState;
            }

            public FragmentState[] newArray(int i) {
                return new FragmentState[i];
            }
        };
        CREATOR = anonymousClass1;
    }
}
