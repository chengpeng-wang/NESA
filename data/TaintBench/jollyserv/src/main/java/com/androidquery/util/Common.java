package com.androidquery.util;

import android.app.Activity;
import android.app.Dialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Gallery;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import com.androidquery.AQuery;
import com.androidquery.callback.BitmapAjaxCallback;
import java.io.File;
import java.util.Comparator;

public class Common implements Comparator<File>, Runnable, OnClickListener, OnLongClickListener, OnItemClickListener, OnScrollListener, OnItemSelectedListener, TextWatcher {
    protected static final int CLEAN_CACHE = 2;
    protected static final int STORE_FILE = 1;
    private boolean fallback;
    private boolean galleryListen = false;
    private OnItemSelectedListener galleryListener;
    private Object handler;
    private int lastBottom;
    private String method;
    private int methodId;
    private OnScrollListener osl;
    private Object[] params;
    private int scrollState = 0;
    private Class<?>[] sig;

    public Common forward(Object handler, String callback, boolean fallback, Class<?>[] sig) {
        this.handler = handler;
        this.method = callback;
        this.fallback = fallback;
        this.sig = sig;
        return this;
    }

    public Common method(int methodId, Object... params) {
        this.methodId = methodId;
        this.params = params;
        return this;
    }

    private Object invoke(Object... args) {
        if (this.method != null) {
            Object[] input = args;
            if (this.params != null) {
                input = this.params;
            }
            Object cbo = this.handler;
            if (cbo == null) {
                cbo = this;
            }
            return AQUtility.invokeHandler(cbo, this.method, this.fallback, true, this.sig, input);
        }
        if (this.methodId != 0) {
            switch (this.methodId) {
                case 1:
                    AQUtility.store((File) this.params[0], (byte[]) this.params[1]);
                    break;
                case 2:
                    AQUtility.cleanCache((File) this.params[0], ((Long) this.params[1]).longValue(), ((Long) this.params[2]).longValue());
                    break;
            }
        }
        return null;
    }

    public int compare(File f1, File f2) {
        long m1 = f1.lastModified();
        long m2 = f2.lastModified();
        if (m2 > m1) {
            return 1;
        }
        if (m2 == m1) {
            return 0;
        }
        return -1;
    }

    public void run() {
        invoke(new Object[0]);
    }

    public void onClick(View v) {
        invoke(v);
    }

    public boolean onLongClick(View v) {
        Object result = invoke(v);
        if (result instanceof Boolean) {
            return ((Boolean) result).booleanValue();
        }
        return false;
    }

    public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
        invoke(parent, v, Integer.valueOf(pos), Long.valueOf(id));
    }

    public void onScroll(AbsListView view, int first, int visibleItemCount, int totalItemCount) {
        checkScrolledBottom(view, this.scrollState);
        if (this.osl != null) {
            this.osl.onScroll(view, first, visibleItemCount, totalItemCount);
        }
    }

    public int getScrollState() {
        return this.scrollState;
    }

    public void forward(OnScrollListener listener) {
        this.osl = listener;
    }

    private void checkScrolledBottom(AbsListView view, int scrollState) {
        int cc = view.getCount();
        int last = view.getLastVisiblePosition();
        if (scrollState != 0 || cc != last + 1) {
            this.lastBottom = -1;
        } else if (last != this.lastBottom) {
            this.lastBottom = last;
            invoke(view, Integer.valueOf(scrollState));
        }
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.scrollState = scrollState;
        checkScrolledBottom(view, scrollState);
        if (view instanceof ExpandableListView) {
            onScrollStateChanged((ExpandableListView) view, scrollState);
        } else {
            onScrollStateChanged2(view, scrollState);
        }
        if (this.osl != null) {
            this.osl.onScrollStateChanged(view, scrollState);
        }
    }

    private void onScrollStateChanged(ExpandableListView elv, int scrollState) {
        elv.setTag(Constants.TAG_NUM, Integer.valueOf(scrollState));
        if (scrollState == 0) {
            int first = elv.getFirstVisiblePosition();
            int count = elv.getLastVisiblePosition() - first;
            ExpandableListAdapter ela = elv.getExpandableListAdapter();
            for (int i = 0; i <= count; i++) {
                long packed = elv.getExpandableListPosition(i + first);
                int group = ExpandableListView.getPackedPositionGroup(packed);
                int child = ExpandableListView.getPackedPositionChild(packed);
                if (group >= 0) {
                    View convertView = elv.getChildAt(i);
                    Long targetPacked = (Long) convertView.getTag(Constants.TAG_NUM);
                    if (targetPacked != null && targetPacked.longValue() == packed) {
                        if (child == -1) {
                            ela.getGroupView(group, elv.isGroupExpanded(group), convertView, elv);
                        } else {
                            ela.getChildView(group, child, child == ela.getChildrenCount(group) + -1, convertView, elv);
                        }
                        convertView.setTag(Constants.TAG_NUM, null);
                    }
                }
            }
        }
    }

    private void onScrollStateChanged2(AbsListView lv, int scrollState) {
        lv.setTag(Constants.TAG_NUM, Integer.valueOf(scrollState));
        if (scrollState == 0) {
            int first = lv.getFirstVisiblePosition();
            int count = lv.getLastVisiblePosition() - first;
            ListAdapter la = (ListAdapter) lv.getAdapter();
            for (int i = 0; i <= count; i++) {
                long packed = (long) (i + first);
                View convertView = lv.getChildAt(i);
                if (((Number) convertView.getTag(Constants.TAG_NUM)) != null) {
                    la.getView((int) packed, convertView, lv);
                    convertView.setTag(Constants.TAG_NUM, null);
                }
            }
        }
    }

    public static boolean shouldDelay(int groupPosition, int childPosition, View convertView, ViewGroup parent, String url) {
        if (url == null || BitmapAjaxCallback.isMemoryCached(url)) {
            return false;
        }
        AbsListView lv = (AbsListView) parent;
        if (((OnScrollListener) parent.getTag(Constants.TAG_SCROLL_LISTENER)) == null) {
            OnScrollListener sl = new Common();
            lv.setOnScrollListener(sl);
            parent.setTag(Constants.TAG_SCROLL_LISTENER, sl);
        }
        Integer scrollState = (Integer) lv.getTag(Constants.TAG_NUM);
        if (scrollState == null || scrollState.intValue() == 0 || scrollState.intValue() == 1) {
            return false;
        }
        long packed = (long) childPosition;
        if (parent instanceof ExpandableListView) {
            packed = ExpandableListView.getPackedPositionForChild(groupPosition, childPosition);
        }
        convertView.setTag(Constants.TAG_NUM, Long.valueOf(packed));
        return true;
    }

    public static boolean shouldDelay(int position, View convertView, ViewGroup parent, String url) {
        if (parent instanceof Gallery) {
            return shouldDelayGallery(position, convertView, parent, url);
        }
        return shouldDelay(-2, position, convertView, parent, url);
    }

    public static boolean shouldDelay(View convertView, ViewGroup parent, String url, float velocity, boolean fileCheck) {
        return shouldDelay(-1, convertView, parent, url);
    }

    private static boolean shouldDelayGallery(int position, View convertView, ViewGroup parent, String url) {
        if (url == null || BitmapAjaxCallback.isMemoryCached(url)) {
            return false;
        }
        Gallery gallery = (Gallery) parent;
        Integer selected = (Integer) gallery.getTag(Constants.TAG_NUM);
        if (selected == null) {
            selected = Integer.valueOf(0);
            gallery.setTag(Constants.TAG_NUM, Integer.valueOf(0));
            gallery.setCallbackDuringFling(false);
            new Common().listen(gallery);
        }
        int delta = ((gallery.getLastVisiblePosition() - gallery.getFirstVisiblePosition()) / 2) + 1;
        int from = selected.intValue() - delta;
        int to = selected.intValue() + delta;
        if (from < 0) {
            to -= from;
            from = 0;
        }
        if (position < from || position > to) {
            convertView.setTag(Constants.TAG_NUM, null);
            return true;
        }
        convertView.setTag(Constants.TAG_NUM, Integer.valueOf(position));
        return false;
    }

    public void afterTextChanged(Editable s) {
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        invoke(s, Integer.valueOf(start), Integer.valueOf(before), Integer.valueOf(count));
    }

    public void listen(Gallery gallery) {
        this.galleryListener = gallery.getOnItemSelectedListener();
        this.galleryListen = true;
        gallery.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
        invoke(parent, v, Integer.valueOf(pos), Long.valueOf(id));
        if (this.galleryListener != null) {
            this.galleryListener.onItemSelected(parent, v, pos, id);
        }
        if (this.galleryListen && ((Integer) parent.getTag(Constants.TAG_NUM)).intValue() != pos) {
            Adapter adapter = parent.getAdapter();
            parent.setTag(Constants.TAG_NUM, Integer.valueOf(pos));
            int count = parent.getChildCount();
            int first = parent.getFirstVisiblePosition();
            for (int i = 0; i < count; i++) {
                View convertView = parent.getChildAt(i);
                int drawPos = first + i;
                Integer lastDrawn = (Integer) convertView.getTag(Constants.TAG_NUM);
                if (lastDrawn == null || lastDrawn.intValue() != drawPos) {
                    adapter.getView(drawPos, convertView, parent);
                }
            }
        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        if (this.galleryListener != null) {
            this.galleryListener.onNothingSelected(arg0);
        }
    }

    public static void showProgress(Object p, String url, boolean show) {
        if (p == null) {
            return;
        }
        if (p instanceof View) {
            View pv = (View) p;
            ProgressBar pbar = null;
            if (p instanceof ProgressBar) {
                pbar = (ProgressBar) p;
            }
            if (show) {
                pv.setTag(Constants.TAG_URL, url);
                pv.setVisibility(0);
                if (pbar != null) {
                    pbar.setProgress(0);
                    pbar.setMax(100);
                    return;
                }
                return;
            }
            Object tag = pv.getTag(Constants.TAG_URL);
            if (tag == null || tag.equals(url)) {
                pv.setTag(Constants.TAG_URL, null);
                if (pbar == null || pbar.isIndeterminate()) {
                    pv.setVisibility(8);
                }
            }
        } else if (p instanceof Dialog) {
            Dialog pd = (Dialog) p;
            AQuery aq = new AQuery(pd.getContext());
            if (show) {
                aq.show(pd);
            } else {
                aq.dismiss(pd);
            }
        } else if (p instanceof Activity) {
            Activity act = (Activity) p;
            act.setProgressBarIndeterminateVisibility(show);
            act.setProgressBarVisibility(show);
            if (show) {
                act.setProgress(0);
            }
        }
    }
}
