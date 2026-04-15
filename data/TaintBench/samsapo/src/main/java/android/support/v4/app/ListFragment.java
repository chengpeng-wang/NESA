package android.support.v4.app;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ListFragment extends Fragment {
    static final int INTERNAL_EMPTY_ID = 16711681;
    static final int INTERNAL_LIST_CONTAINER_ID = 16711683;
    static final int INTERNAL_PROGRESS_CONTAINER_ID = 16711682;
    ListAdapter mAdapter;
    CharSequence mEmptyText;
    View mEmptyView;
    private final Handler mHandler;
    ListView mList;
    View mListContainer;
    boolean mListShown;
    private final OnItemClickListener mOnClickListener;
    View mProgressContainer;
    private final Runnable mRequestFocus;
    TextView mStandardEmptyView;

    public ListFragment() {
        Handler handler = r5;
        Handler handler2 = new Handler();
        this.mHandler = handler;
        AnonymousClass1 anonymousClass1 = r5;
        AnonymousClass1 anonymousClass12 = new Runnable(this) {
            final /* synthetic */ ListFragment this$0;

            {
                this.this$0 = r5;
            }

            public void run() {
                this.this$0.mList.focusableViewAvailable(this.this$0.mList);
            }
        };
        this.mRequestFocus = anonymousClass1;
        AnonymousClass2 anonymousClass2 = r5;
        AnonymousClass2 anonymousClass22 = new OnItemClickListener(this) {
            final /* synthetic */ ListFragment this$0;

            {
                this.this$0 = r5;
            }

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                View view2 = view;
                int i2 = i;
                long j2 = j;
                this.this$0.onListItemClick((ListView) adapterView, view2, i2, j2);
            }
        };
        this.mOnClickListener = anonymousClass2;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        LayoutInflater layoutInflater2 = layoutInflater;
        ViewGroup viewGroup2 = viewGroup;
        Bundle bundle2 = bundle;
        Context activity = getActivity();
        View view = r17;
        View frameLayout = new FrameLayout(activity);
        View view2 = view;
        view = r17;
        frameLayout = new LinearLayout(activity);
        View view3 = view;
        view3.setId(INTERNAL_PROGRESS_CONTAINER_ID);
        view3.setOrientation(1);
        view3.setVisibility(8);
        view3.setGravity(17);
        view = r17;
        frameLayout = new ProgressBar(activity, null, 16842874);
        View view4 = view;
        view = view3;
        frameLayout = view4;
        LayoutParams layoutParams = r17;
        LayoutParams layoutParams2 = new FrameLayout.LayoutParams(-2, -2);
        view.addView(frameLayout, layoutParams);
        view = view2;
        frameLayout = view3;
        layoutParams = r17;
        layoutParams2 = new FrameLayout.LayoutParams(-1, -1);
        view.addView(frameLayout, layoutParams);
        view = r17;
        frameLayout = new FrameLayout(activity);
        View view5 = view;
        view5.setId(INTERNAL_LIST_CONTAINER_ID);
        view = r17;
        frameLayout = new TextView(getActivity());
        View view6 = view;
        view6.setId(INTERNAL_EMPTY_ID);
        view6.setGravity(17);
        view = view5;
        frameLayout = view6;
        layoutParams = r17;
        layoutParams2 = new FrameLayout.LayoutParams(-1, -1);
        view.addView(frameLayout, layoutParams);
        view = r17;
        frameLayout = new ListView(getActivity());
        View view7 = view;
        view7.setId(16908298);
        view7.setDrawSelectorOnTop(false);
        view = view5;
        frameLayout = view7;
        layoutParams = r17;
        layoutParams2 = new FrameLayout.LayoutParams(-1, -1);
        view.addView(frameLayout, layoutParams);
        view = view2;
        frameLayout = view5;
        layoutParams = r17;
        layoutParams2 = new FrameLayout.LayoutParams(-1, -1);
        view.addView(frameLayout, layoutParams);
        view = view2;
        LayoutParams layoutParams3 = r17;
        layoutParams = new FrameLayout.LayoutParams(-1, -1);
        view.setLayoutParams(layoutParams3);
        return view2;
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ensureList();
    }

    public void onDestroyView() {
        this.mHandler.removeCallbacks(this.mRequestFocus);
        this.mList = null;
        this.mListShown = false;
        View view = null;
        View view2 = view;
        View view3 = view;
        this.mListContainer = view3;
        view = view2;
        View view4 = view;
        this.mProgressContainer = view;
        this.mEmptyView = view4;
        this.mStandardEmptyView = null;
        super.onDestroyView();
    }

    public void onListItemClick(ListView listView, View view, int i, long j) {
    }

    public void setListAdapter(ListAdapter listAdapter) {
        ListAdapter listAdapter2 = listAdapter;
        Object obj = this.mAdapter != null ? 1 : null;
        this.mAdapter = listAdapter2;
        if (this.mList != null) {
            this.mList.setAdapter(listAdapter2);
            if (!this.mListShown && obj == null) {
                setListShown(true, getView().getWindowToken() != null);
            }
        }
    }

    public void setSelection(int i) {
        int i2 = i;
        ensureList();
        this.mList.setSelection(i2);
    }

    public int getSelectedItemPosition() {
        ensureList();
        return this.mList.getSelectedItemPosition();
    }

    public long getSelectedItemId() {
        ensureList();
        return this.mList.getSelectedItemId();
    }

    public ListView getListView() {
        ensureList();
        return this.mList;
    }

    public void setEmptyText(CharSequence charSequence) {
        CharSequence charSequence2 = charSequence;
        ensureList();
        if (this.mStandardEmptyView == null) {
            IllegalStateException illegalStateException = r5;
            IllegalStateException illegalStateException2 = new IllegalStateException("Can't be used with a custom content view");
            throw illegalStateException;
        }
        this.mStandardEmptyView.setText(charSequence2);
        if (this.mEmptyText == null) {
            this.mList.setEmptyView(this.mStandardEmptyView);
        }
        this.mEmptyText = charSequence2;
    }

    public void setListShown(boolean z) {
        setListShown(z, true);
    }

    public void setListShownNoAnimation(boolean z) {
        setListShown(z, false);
    }

    private void setListShown(boolean z, boolean z2) {
        boolean z3 = z;
        boolean z4 = z2;
        ensureList();
        if (this.mProgressContainer == null) {
            IllegalStateException illegalStateException = r6;
            IllegalStateException illegalStateException2 = new IllegalStateException("Can't be used with a custom content view");
            throw illegalStateException;
        } else if (this.mListShown != z3) {
            this.mListShown = z3;
            if (z3) {
                if (z4) {
                    this.mProgressContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), 17432577));
                    this.mListContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), 17432576));
                } else {
                    this.mProgressContainer.clearAnimation();
                    this.mListContainer.clearAnimation();
                }
                this.mProgressContainer.setVisibility(8);
                this.mListContainer.setVisibility(0);
                return;
            }
            if (z4) {
                this.mProgressContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), 17432576));
                this.mListContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), 17432577));
            } else {
                this.mProgressContainer.clearAnimation();
                this.mListContainer.clearAnimation();
            }
            this.mProgressContainer.setVisibility(0);
            this.mListContainer.setVisibility(8);
        }
    }

    public ListAdapter getListAdapter() {
        return this.mAdapter;
    }

    private void ensureList() {
        if (this.mList == null) {
            View view = getView();
            if (view == null) {
                IllegalStateException illegalStateException = r6;
                IllegalStateException illegalStateException2 = new IllegalStateException("Content view not yet created");
                throw illegalStateException;
            }
            if (view instanceof ListView) {
                this.mList = (ListView) view;
            } else {
                this.mStandardEmptyView = (TextView) view.findViewById(INTERNAL_EMPTY_ID);
                if (this.mStandardEmptyView == null) {
                    this.mEmptyView = view.findViewById(16908292);
                } else {
                    this.mStandardEmptyView.setVisibility(8);
                }
                this.mProgressContainer = view.findViewById(INTERNAL_PROGRESS_CONTAINER_ID);
                this.mListContainer = view.findViewById(INTERNAL_LIST_CONTAINER_ID);
                View findViewById = view.findViewById(16908298);
                RuntimeException runtimeException;
                RuntimeException runtimeException2;
                if (findViewById instanceof ListView) {
                    this.mList = (ListView) findViewById;
                    if (this.mEmptyView != null) {
                        this.mList.setEmptyView(this.mEmptyView);
                    } else if (this.mEmptyText != null) {
                        this.mStandardEmptyView.setText(this.mEmptyText);
                        this.mList.setEmptyView(this.mStandardEmptyView);
                    }
                } else if (findViewById == null) {
                    runtimeException = r6;
                    runtimeException2 = new RuntimeException("Your content must have a ListView whose id attribute is 'android.R.id.list'");
                    throw runtimeException;
                } else {
                    runtimeException = r6;
                    runtimeException2 = new RuntimeException("Content has view with id attribute 'android.R.id.list' that is not a ListView class");
                    throw runtimeException;
                }
            }
            this.mListShown = true;
            this.mList.setOnItemClickListener(this.mOnClickListener);
            if (this.mAdapter != null) {
                ListAdapter listAdapter = this.mAdapter;
                this.mAdapter = null;
                setListAdapter(listAdapter);
            } else if (this.mProgressContainer != null) {
                setListShown(false, false);
            }
            boolean post = this.mHandler.post(this.mRequestFocus);
        }
    }
}
