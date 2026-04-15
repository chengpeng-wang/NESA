package android.support.v4.app;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment.SavedState;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

public abstract class FragmentStatePagerAdapter extends PagerAdapter {
    private static final boolean DEBUG = false;
    private static final String TAG = "FragmentStatePagerAdapter";
    private FragmentTransaction mCurTransaction = null;
    private Fragment mCurrentPrimaryItem;
    private final FragmentManager mFragmentManager;
    private ArrayList<Fragment> mFragments;
    private ArrayList<SavedState> mSavedState;

    public abstract Fragment getItem(int i);

    public FragmentStatePagerAdapter(FragmentManager fragmentManager) {
        FragmentManager fragmentManager2 = fragmentManager;
        ArrayList arrayList = r5;
        ArrayList arrayList2 = new ArrayList();
        this.mSavedState = arrayList;
        arrayList = r5;
        arrayList2 = new ArrayList();
        this.mFragments = arrayList;
        this.mCurrentPrimaryItem = null;
        this.mFragmentManager = fragmentManager2;
    }

    public void startUpdate(ViewGroup viewGroup) {
    }

    public Object instantiateItem(ViewGroup viewGroup, int i) {
        Fragment fragment;
        ViewGroup viewGroup2 = viewGroup;
        int i2 = i;
        if (this.mFragments.size() > i2) {
            fragment = (Fragment) this.mFragments.get(i2);
            if (fragment != null) {
                return fragment;
            }
        }
        if (this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }
        fragment = getItem(i2);
        if (this.mSavedState.size() > i2) {
            SavedState savedState = (SavedState) this.mSavedState.get(i2);
            if (savedState != null) {
                fragment.setInitialSavedState(savedState);
            }
        }
        while (this.mFragments.size() <= i2) {
            boolean add = this.mFragments.add(null);
        }
        fragment.setMenuVisibility(DEBUG);
        fragment.setUserVisibleHint(DEBUG);
        Object obj = this.mFragments.set(i2, fragment);
        FragmentTransaction add2 = this.mCurTransaction.add(viewGroup2.getId(), fragment);
        return fragment;
    }

    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        ViewGroup viewGroup2 = viewGroup;
        int i2 = i;
        Fragment fragment = (Fragment) obj;
        if (this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }
        while (this.mSavedState.size() <= i2) {
            boolean add = this.mSavedState.add(null);
        }
        Object obj2 = this.mSavedState.set(i2, this.mFragmentManager.saveFragmentInstanceState(fragment));
        obj2 = this.mFragments.set(i2, null);
        FragmentTransaction remove = this.mCurTransaction.remove(fragment);
    }

    public void setPrimaryItem(ViewGroup viewGroup, int i, Object obj) {
        ViewGroup viewGroup2 = viewGroup;
        int i2 = i;
        Fragment fragment = (Fragment) obj;
        if (fragment != this.mCurrentPrimaryItem) {
            if (this.mCurrentPrimaryItem != null) {
                this.mCurrentPrimaryItem.setMenuVisibility(DEBUG);
                this.mCurrentPrimaryItem.setUserVisibleHint(DEBUG);
            }
            if (fragment != null) {
                fragment.setMenuVisibility(true);
                fragment.setUserVisibleHint(true);
            }
            this.mCurrentPrimaryItem = fragment;
        }
    }

    public void finishUpdate(ViewGroup viewGroup) {
        ViewGroup viewGroup2 = viewGroup;
        if (this.mCurTransaction != null) {
            int commitAllowingStateLoss = this.mCurTransaction.commitAllowingStateLoss();
            this.mCurTransaction = null;
            boolean executePendingTransactions = this.mFragmentManager.executePendingTransactions();
        }
    }

    public boolean isViewFromObject(View view, Object obj) {
        return ((Fragment) obj).getView() == view ? true : DEBUG;
    }

    public Parcelable saveState() {
        Bundle bundle;
        Bundle bundle2;
        Bundle bundle3 = null;
        if (this.mSavedState.size() > 0) {
            bundle = r9;
            bundle2 = new Bundle();
            bundle3 = bundle;
            SavedState[] savedStateArr = new SavedState[this.mSavedState.size()];
            Object[] toArray = this.mSavedState.toArray(savedStateArr);
            bundle3.putParcelableArray("states", savedStateArr);
        }
        for (int i = 0; i < this.mFragments.size(); i++) {
            Fragment fragment = (Fragment) this.mFragments.get(i);
            if (fragment != null) {
                if (bundle3 == null) {
                    bundle = r9;
                    bundle2 = new Bundle();
                    bundle3 = bundle;
                }
                StringBuilder stringBuilder = r9;
                StringBuilder stringBuilder2 = new StringBuilder();
                this.mFragmentManager.putFragment(bundle3, stringBuilder.append("f").append(i).toString(), fragment);
            }
        }
        return bundle3;
    }

    public void restoreState(Parcelable parcelable, ClassLoader classLoader) {
        Parcelable parcelable2 = parcelable;
        ClassLoader classLoader2 = classLoader;
        if (parcelable2 != null) {
            boolean add;
            Bundle bundle = (Bundle) parcelable2;
            bundle.setClassLoader(classLoader2);
            Parcelable[] parcelableArray = bundle.getParcelableArray("states");
            this.mSavedState.clear();
            this.mFragments.clear();
            if (parcelableArray != null) {
                for (Parcelable parcelable3 : parcelableArray) {
                    add = this.mSavedState.add((SavedState) parcelable3);
                }
            }
            for (String str : bundle.keySet()) {
                if (str.startsWith("f")) {
                    int parseInt = Integer.parseInt(str.substring(1));
                    Fragment fragment = this.mFragmentManager.getFragment(bundle, str);
                    if (fragment != null) {
                        while (this.mFragments.size() <= parseInt) {
                            add = this.mFragments.add(null);
                        }
                        fragment.setMenuVisibility(DEBUG);
                        Object obj = this.mFragments.set(parseInt, fragment);
                    } else {
                        String str2 = TAG;
                        StringBuilder stringBuilder = r13;
                        StringBuilder stringBuilder2 = new StringBuilder();
                        int w = Log.w(str2, stringBuilder.append("Bad fragment at key ").append(str).toString());
                    }
                }
            }
        }
    }
}
