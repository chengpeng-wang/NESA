package android.support.v4.app;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public abstract class FragmentPagerAdapter extends PagerAdapter {
    private static final boolean DEBUG = false;
    private static final String TAG = "FragmentPagerAdapter";
    private FragmentTransaction mCurTransaction = null;
    private Fragment mCurrentPrimaryItem = null;
    private final FragmentManager mFragmentManager;

    public abstract Fragment getItem(int i);

    public FragmentPagerAdapter(FragmentManager fragmentManager) {
        FragmentManager fragmentManager2 = fragmentManager;
        this.mFragmentManager = fragmentManager2;
    }

    public void startUpdate(ViewGroup viewGroup) {
    }

    public Object instantiateItem(ViewGroup viewGroup, int i) {
        ViewGroup viewGroup2 = viewGroup;
        int i2 = i;
        if (this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }
        long itemId = getItemId(i2);
        Fragment findFragmentByTag = this.mFragmentManager.findFragmentByTag(makeFragmentName(viewGroup2.getId(), itemId));
        FragmentTransaction attach;
        if (findFragmentByTag != null) {
            attach = this.mCurTransaction.attach(findFragmentByTag);
        } else {
            findFragmentByTag = getItem(i2);
            attach = this.mCurTransaction.add(viewGroup2.getId(), findFragmentByTag, makeFragmentName(viewGroup2.getId(), itemId));
        }
        if (findFragmentByTag != this.mCurrentPrimaryItem) {
            findFragmentByTag.setMenuVisibility(DEBUG);
            findFragmentByTag.setUserVisibleHint(DEBUG);
        }
        return findFragmentByTag;
    }

    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        ViewGroup viewGroup2 = viewGroup;
        int i2 = i;
        Object obj2 = obj;
        if (this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }
        FragmentTransaction detach = this.mCurTransaction.detach((Fragment) obj2);
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
        return null;
    }

    public void restoreState(Parcelable parcelable, ClassLoader classLoader) {
    }

    public long getItemId(int i) {
        return (long) i;
    }

    private static String makeFragmentName(int i, long j) {
        int i2 = i;
        long j2 = j;
        StringBuilder stringBuilder = r6;
        StringBuilder stringBuilder2 = new StringBuilder();
        return stringBuilder.append("android:switcher:").append(i2).append(":").append(j2).toString();
    }
}
