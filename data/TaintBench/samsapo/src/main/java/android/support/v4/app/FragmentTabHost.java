package android.support.v4.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import java.util.ArrayList;

public class FragmentTabHost extends TabHost implements OnTabChangeListener {
    private boolean mAttached;
    private int mContainerId;
    private Context mContext;
    private FragmentManager mFragmentManager;
    private TabInfo mLastTab;
    private OnTabChangeListener mOnTabChangeListener;
    private FrameLayout mRealTabContent;
    private final ArrayList<TabInfo> mTabs;

    static class DummyTabFactory implements TabContentFactory {
        private final Context mContext;

        public DummyTabFactory(Context context) {
            this.mContext = context;
        }

        public View createTabContent(String str) {
            String str2 = str;
            View view = r6;
            View view2 = new View(this.mContext);
            View view3 = view;
            view3.setMinimumWidth(0);
            view3.setMinimumHeight(0);
            return view3;
        }
    }

    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR;
        String curTab;

        /* synthetic */ SavedState(Parcel parcel, AnonymousClass1 anonymousClass1) {
            AnonymousClass1 anonymousClass12 = anonymousClass1;
            this(parcel);
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private SavedState(Parcel parcel) {
            Parcel parcel2 = parcel;
            super(parcel2);
            this.curTab = parcel2.readString();
        }

        public void writeToParcel(Parcel parcel, int i) {
            Parcel parcel2 = parcel;
            super.writeToParcel(parcel2, i);
            parcel2.writeString(this.curTab);
        }

        public String toString() {
            StringBuilder stringBuilder = r3;
            StringBuilder stringBuilder2 = new StringBuilder();
            return stringBuilder.append("FragmentTabHost.SavedState{").append(Integer.toHexString(System.identityHashCode(this))).append(" curTab=").append(this.curTab).append("}").toString();
        }

        static {
            AnonymousClass1 anonymousClass1 = r2;
            AnonymousClass1 anonymousClass12 = new Creator<SavedState>() {
                public SavedState createFromParcel(Parcel parcel) {
                    SavedState savedState = r6;
                    SavedState savedState2 = new SavedState(parcel, null);
                    return savedState;
                }

                public SavedState[] newArray(int i) {
                    return new SavedState[i];
                }
            };
            CREATOR = anonymousClass1;
        }
    }

    static final class TabInfo {
        /* access modifiers changed from: private|final */
        public final Bundle args;
        /* access modifiers changed from: private|final */
        public final Class<?> clss;
        /* access modifiers changed from: private */
        public Fragment fragment;
        /* access modifiers changed from: private|final */
        public final String tag;

        static /* synthetic */ Fragment access$102(TabInfo tabInfo, Fragment fragment) {
            Fragment fragment2 = fragment;
            Fragment fragment3 = fragment2;
            Fragment fragment4 = fragment2;
            tabInfo.fragment = fragment4;
            return fragment3;
        }

        TabInfo(String str, Class<?> cls, Bundle bundle) {
            Class<?> cls2 = cls;
            Bundle bundle2 = bundle;
            this.tag = str;
            this.clss = cls2;
            this.args = bundle2;
        }
    }

    public FragmentTabHost(Context context) {
        Context context2 = context;
        super(context2, null);
        ArrayList arrayList = r5;
        ArrayList arrayList2 = new ArrayList();
        this.mTabs = arrayList;
        initFragmentTabHost(context2, null);
    }

    public FragmentTabHost(Context context, AttributeSet attributeSet) {
        Context context2 = context;
        AttributeSet attributeSet2 = attributeSet;
        super(context2, attributeSet2);
        ArrayList arrayList = r6;
        ArrayList arrayList2 = new ArrayList();
        this.mTabs = arrayList;
        initFragmentTabHost(context2, attributeSet2);
    }

    private void initFragmentTabHost(Context context, AttributeSet attributeSet) {
        Context context2 = context;
        AttributeSet attributeSet2 = attributeSet;
        int[] iArr = new int[1];
        int[] iArr2 = iArr;
        iArr[0] = 16842995;
        TypedArray obtainStyledAttributes = context2.obtainStyledAttributes(attributeSet2, iArr2, 0, 0);
        this.mContainerId = obtainStyledAttributes.getResourceId(0, 0);
        obtainStyledAttributes.recycle();
        super.setOnTabChangedListener(this);
    }

    private void ensureHierarchy(Context context) {
        Context context2 = context;
        if (findViewById(16908307) == null) {
            View view = r12;
            View linearLayout = new LinearLayout(context2);
            View view2 = view;
            view2.setOrientation(1);
            linearLayout = view2;
            LayoutParams layoutParams = r12;
            LayoutParams layoutParams2 = new FrameLayout.LayoutParams(-1, -1);
            addView(linearLayout, layoutParams);
            view = r12;
            linearLayout = new TabWidget(context2);
            View view3 = view;
            view3.setId(16908307);
            view3.setOrientation(0);
            view = view2;
            linearLayout = view3;
            layoutParams = r12;
            layoutParams2 = new LinearLayout.LayoutParams(-1, -2, 0.0f);
            view.addView(linearLayout, layoutParams);
            view = r12;
            linearLayout = new FrameLayout(context2);
            View view4 = view;
            view4.setId(16908305);
            view = view2;
            linearLayout = view4;
            layoutParams = r12;
            layoutParams2 = new LinearLayout.LayoutParams(0, 0, 0.0f);
            view.addView(linearLayout, layoutParams);
            linearLayout = r12;
            View frameLayout = new FrameLayout(context2);
            View view5 = linearLayout;
            view4 = view5;
            this.mRealTabContent = view5;
            this.mRealTabContent.setId(this.mContainerId);
            view = view2;
            linearLayout = view4;
            layoutParams = r12;
            layoutParams2 = new LinearLayout.LayoutParams(-1, 0, 1.0f);
            view.addView(linearLayout, layoutParams);
        }
    }

    @Deprecated
    public void setup() {
        IllegalStateException illegalStateException = r4;
        IllegalStateException illegalStateException2 = new IllegalStateException("Must call setup() that takes a Context and FragmentManager");
        throw illegalStateException;
    }

    public void setup(Context context, FragmentManager fragmentManager) {
        Context context2 = context;
        FragmentManager fragmentManager2 = fragmentManager;
        ensureHierarchy(context2);
        super.setup();
        this.mContext = context2;
        this.mFragmentManager = fragmentManager2;
        ensureContent();
    }

    public void setup(Context context, FragmentManager fragmentManager, int i) {
        Context context2 = context;
        FragmentManager fragmentManager2 = fragmentManager;
        int i2 = i;
        ensureHierarchy(context2);
        super.setup();
        this.mContext = context2;
        this.mFragmentManager = fragmentManager2;
        this.mContainerId = i2;
        ensureContent();
        this.mRealTabContent.setId(i2);
        if (getId() == -1) {
            setId(16908306);
        }
    }

    private void ensureContent() {
        if (this.mRealTabContent == null) {
            this.mRealTabContent = (FrameLayout) findViewById(this.mContainerId);
            if (this.mRealTabContent == null) {
                IllegalStateException illegalStateException = r5;
                StringBuilder stringBuilder = r5;
                StringBuilder stringBuilder2 = new StringBuilder();
                IllegalStateException illegalStateException2 = new IllegalStateException(stringBuilder.append("No tab content FrameLayout found for id ").append(this.mContainerId).toString());
                throw illegalStateException;
            }
        }
    }

    public void setOnTabChangedListener(OnTabChangeListener onTabChangeListener) {
        this.mOnTabChangeListener = onTabChangeListener;
    }

    public void addTab(TabSpec tabSpec, Class<?> cls, Bundle bundle) {
        TabSpec tabSpec2 = tabSpec;
        Class<?> cls2 = cls;
        Bundle bundle2 = bundle;
        TabSpec tabSpec3 = tabSpec2;
        DummyTabFactory dummyTabFactory = r12;
        DummyTabFactory dummyTabFactory2 = new DummyTabFactory(this.mContext);
        tabSpec3 = tabSpec3.setContent(dummyTabFactory);
        String tag = tabSpec2.getTag();
        TabInfo tabInfo = r12;
        TabInfo tabInfo2 = new TabInfo(tag, cls2, bundle2);
        TabInfo tabInfo3 = tabInfo;
        if (this.mAttached) {
            Fragment access$102 = TabInfo.access$102(tabInfo3, this.mFragmentManager.findFragmentByTag(tag));
            if (!(tabInfo3.fragment == null || tabInfo3.fragment.isDetached())) {
                FragmentTransaction beginTransaction = this.mFragmentManager.beginTransaction();
                FragmentTransaction detach = beginTransaction.detach(tabInfo3.fragment);
                int commit = beginTransaction.commit();
            }
        }
        boolean add = this.mTabs.add(tabInfo3);
        addTab(tabSpec2);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        String currentTabTag = getCurrentTabTag();
        FragmentTransaction fragmentTransaction = null;
        for (int i = 0; i < this.mTabs.size(); i++) {
            TabInfo tabInfo = (TabInfo) this.mTabs.get(i);
            Fragment access$102 = TabInfo.access$102(tabInfo, this.mFragmentManager.findFragmentByTag(tabInfo.tag));
            if (!(tabInfo.fragment == null || tabInfo.fragment.isDetached())) {
                if (tabInfo.tag.equals(currentTabTag)) {
                    this.mLastTab = tabInfo;
                } else {
                    if (fragmentTransaction == null) {
                        fragmentTransaction = this.mFragmentManager.beginTransaction();
                    }
                    FragmentTransaction detach = fragmentTransaction.detach(tabInfo.fragment);
                }
            }
        }
        this.mAttached = true;
        fragmentTransaction = doTabChanged(currentTabTag, fragmentTransaction);
        if (fragmentTransaction != null) {
            int commit = fragmentTransaction.commit();
            boolean executePendingTransactions = this.mFragmentManager.executePendingTransactions();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mAttached = false;
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        SavedState savedState = r6;
        SavedState savedState2 = new SavedState(super.onSaveInstanceState());
        SavedState savedState3 = savedState;
        savedState3.curTab = getCurrentTabTag();
        return savedState3;
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        setCurrentTabByTag(savedState.curTab);
    }

    public void onTabChanged(String str) {
        String str2 = str;
        if (this.mAttached) {
            FragmentTransaction doTabChanged = doTabChanged(str2, null);
            if (doTabChanged != null) {
                int commit = doTabChanged.commit();
            }
        }
        if (this.mOnTabChangeListener != null) {
            this.mOnTabChangeListener.onTabChanged(str2);
        }
    }

    private FragmentTransaction doTabChanged(String str, FragmentTransaction fragmentTransaction) {
        String str2 = str;
        FragmentTransaction fragmentTransaction2 = fragmentTransaction;
        TabInfo tabInfo = null;
        for (int i = 0; i < this.mTabs.size(); i++) {
            TabInfo tabInfo2 = (TabInfo) this.mTabs.get(i);
            if (tabInfo2.tag.equals(str2)) {
                tabInfo = tabInfo2;
            }
        }
        if (tabInfo == null) {
            IllegalStateException illegalStateException = r10;
            StringBuilder stringBuilder = r10;
            StringBuilder stringBuilder2 = new StringBuilder();
            IllegalStateException illegalStateException2 = new IllegalStateException(stringBuilder.append("No tab known for tag ").append(str2).toString());
            throw illegalStateException;
        }
        if (this.mLastTab != tabInfo) {
            FragmentTransaction detach;
            if (fragmentTransaction2 == null) {
                fragmentTransaction2 = this.mFragmentManager.beginTransaction();
            }
            if (!(this.mLastTab == null || this.mLastTab.fragment == null)) {
                detach = fragmentTransaction2.detach(this.mLastTab.fragment);
            }
            if (tabInfo != null) {
                if (tabInfo.fragment == null) {
                    Fragment access$102 = TabInfo.access$102(tabInfo, Fragment.instantiate(this.mContext, tabInfo.clss.getName(), tabInfo.args));
                    detach = fragmentTransaction2.add(this.mContainerId, tabInfo.fragment, tabInfo.tag);
                } else {
                    detach = fragmentTransaction2.attach(tabInfo.fragment);
                }
            }
            this.mLastTab = tabInfo;
        }
        return fragmentTransaction2;
    }
}
