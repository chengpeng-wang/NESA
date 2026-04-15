package android.support.v4.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;

/* compiled from: BackStackRecord */
final class BackStackState implements Parcelable {
    public static final Creator<BackStackState> CREATOR;
    final int mBreadCrumbShortTitleRes;
    final CharSequence mBreadCrumbShortTitleText;
    final int mBreadCrumbTitleRes;
    final CharSequence mBreadCrumbTitleText;
    final int mIndex;
    final String mName;
    final int[] mOps;
    final int mTransition;
    final int mTransitionStyle;

    public BackStackState(FragmentManagerImpl fragmentManagerImpl, BackStackRecord backStackRecord) {
        Op op;
        FragmentManagerImpl fragmentManagerImpl2 = fragmentManagerImpl;
        BackStackRecord backStackRecord2 = backStackRecord;
        int i = 0;
        Op op2 = backStackRecord2.mHead;
        while (true) {
            op = op2;
            if (op == null) {
                break;
            }
            if (op.removed != null) {
                i += op.removed.size();
            }
            op2 = op.next;
        }
        this.mOps = new int[((backStackRecord2.mNumOp * 7) + i)];
        if (backStackRecord2.mAddToBackStack) {
            int i2 = 0;
            for (op = backStackRecord2.mHead; op != null; op = op.next) {
                int i3 = i2;
                i2++;
                this.mOps[i3] = op.cmd;
                i3 = i2;
                i2++;
                this.mOps[i3] = op.fragment != null ? op.fragment.mIndex : -1;
                i3 = i2;
                i2++;
                this.mOps[i3] = op.enterAnim;
                i3 = i2;
                i2++;
                this.mOps[i3] = op.exitAnim;
                i3 = i2;
                i2++;
                this.mOps[i3] = op.popEnterAnim;
                i3 = i2;
                i2++;
                this.mOps[i3] = op.popExitAnim;
                if (op.removed != null) {
                    int size = op.removed.size();
                    i3 = i2;
                    i2++;
                    this.mOps[i3] = size;
                    for (int i4 = 0; i4 < size; i4++) {
                        i3 = i2;
                        i2++;
                        this.mOps[i3] = ((Fragment) op.removed.get(i4)).mIndex;
                    }
                } else {
                    i3 = i2;
                    i2++;
                    this.mOps[i3] = 0;
                }
            }
            this.mTransition = backStackRecord2.mTransition;
            this.mTransitionStyle = backStackRecord2.mTransitionStyle;
            this.mName = backStackRecord2.mName;
            this.mIndex = backStackRecord2.mIndex;
            this.mBreadCrumbTitleRes = backStackRecord2.mBreadCrumbTitleRes;
            this.mBreadCrumbTitleText = backStackRecord2.mBreadCrumbTitleText;
            this.mBreadCrumbShortTitleRes = backStackRecord2.mBreadCrumbShortTitleRes;
            this.mBreadCrumbShortTitleText = backStackRecord2.mBreadCrumbShortTitleText;
            return;
        }
        IllegalStateException illegalStateException = r12;
        IllegalStateException illegalStateException2 = new IllegalStateException("Not on back stack");
        throw illegalStateException;
    }

    public BackStackState(Parcel parcel) {
        Parcel parcel2 = parcel;
        this.mOps = parcel2.createIntArray();
        this.mTransition = parcel2.readInt();
        this.mTransitionStyle = parcel2.readInt();
        this.mName = parcel2.readString();
        this.mIndex = parcel2.readInt();
        this.mBreadCrumbTitleRes = parcel2.readInt();
        this.mBreadCrumbTitleText = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel2);
        this.mBreadCrumbShortTitleRes = parcel2.readInt();
        this.mBreadCrumbShortTitleText = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel2);
    }

    public BackStackRecord instantiate(FragmentManagerImpl fragmentManagerImpl) {
        FragmentManagerImpl fragmentManagerImpl2 = fragmentManagerImpl;
        BackStackRecord backStackRecord = r14;
        BackStackRecord backStackRecord2 = new BackStackRecord(fragmentManagerImpl2);
        BackStackRecord backStackRecord3 = backStackRecord;
        int i = 0;
        int i2 = 0;
        while (i < this.mOps.length) {
            StringBuilder stringBuilder;
            StringBuilder stringBuilder2;
            int v;
            Op op = r14;
            Op op2 = new Op();
            Op op3 = op;
            int i3 = i;
            i++;
            op3.cmd = this.mOps[i3];
            if (FragmentManagerImpl.DEBUG) {
                stringBuilder = r14;
                stringBuilder2 = new StringBuilder();
                v = Log.v("FragmentManager", stringBuilder.append("Instantiate ").append(backStackRecord3).append(" op #").append(i2).append(" base fragment #").append(this.mOps[i]).toString());
            }
            int i4 = i;
            i++;
            int i5 = this.mOps[i4];
            if (i5 >= 0) {
                op3.fragment = (Fragment) fragmentManagerImpl2.mActive.get(i5);
            } else {
                op3.fragment = null;
            }
            i3 = i;
            i++;
            op3.enterAnim = this.mOps[i3];
            i3 = i;
            i++;
            op3.exitAnim = this.mOps[i3];
            i3 = i;
            i++;
            op3.popEnterAnim = this.mOps[i3];
            i3 = i;
            i++;
            op3.popExitAnim = this.mOps[i3];
            i4 = i;
            i++;
            int i6 = this.mOps[i4];
            if (i6 > 0) {
                op = op3;
                ArrayList arrayList = r14;
                ArrayList arrayList2 = new ArrayList(i6);
                op.removed = arrayList;
                for (int i7 = 0; i7 < i6; i7++) {
                    if (FragmentManagerImpl.DEBUG) {
                        stringBuilder = r14;
                        stringBuilder2 = new StringBuilder();
                        v = Log.v("FragmentManager", stringBuilder.append("Instantiate ").append(backStackRecord3).append(" set remove fragment #").append(this.mOps[i]).toString());
                    }
                    i3 = i;
                    i++;
                    boolean add = op3.removed.add((Fragment) fragmentManagerImpl2.mActive.get(this.mOps[i3]));
                }
            }
            backStackRecord3.addOp(op3);
            i2++;
        }
        backStackRecord3.mTransition = this.mTransition;
        backStackRecord3.mTransitionStyle = this.mTransitionStyle;
        backStackRecord3.mName = this.mName;
        backStackRecord3.mIndex = this.mIndex;
        backStackRecord3.mAddToBackStack = true;
        backStackRecord3.mBreadCrumbTitleRes = this.mBreadCrumbTitleRes;
        backStackRecord3.mBreadCrumbTitleText = this.mBreadCrumbTitleText;
        backStackRecord3.mBreadCrumbShortTitleRes = this.mBreadCrumbShortTitleRes;
        backStackRecord3.mBreadCrumbShortTitleText = this.mBreadCrumbShortTitleText;
        backStackRecord3.bumpBackStackNesting(1);
        return backStackRecord3;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        Parcel parcel2 = parcel;
        int i2 = i;
        parcel2.writeIntArray(this.mOps);
        parcel2.writeInt(this.mTransition);
        parcel2.writeInt(this.mTransitionStyle);
        parcel2.writeString(this.mName);
        parcel2.writeInt(this.mIndex);
        parcel2.writeInt(this.mBreadCrumbTitleRes);
        TextUtils.writeToParcel(this.mBreadCrumbTitleText, parcel2, 0);
        parcel2.writeInt(this.mBreadCrumbShortTitleRes);
        TextUtils.writeToParcel(this.mBreadCrumbShortTitleText, parcel2, 0);
    }

    static {
        AnonymousClass1 anonymousClass1 = r2;
        AnonymousClass1 anonymousClass12 = new Creator<BackStackState>() {
            public BackStackState createFromParcel(Parcel parcel) {
                BackStackState backStackState = r5;
                BackStackState backStackState2 = new BackStackState(parcel);
                return backStackState;
            }

            public BackStackState[] newArray(int i) {
                return new BackStackState[i];
            }
        };
        CREATOR = anonymousClass1;
    }
}
