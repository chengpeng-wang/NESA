package com.androidquery;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import com.androidquery.auth.AccountHandle;
import com.androidquery.callback.AbstractAjaxCallback;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.BitmapAjaxCallback;
import com.androidquery.callback.ImageOptions;
import com.androidquery.callback.Transformer;
import com.androidquery.util.AQUtility;
import com.androidquery.util.Common;
import com.androidquery.util.Constants;
import com.androidquery.util.WebImage;
import java.io.File;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;

public abstract class AbstractAQuery<T extends AbstractAQuery<T>> implements Constants {
    private static Class<?>[] LAYER_TYPE_SIG = new Class[]{Integer.TYPE, Paint.class};
    private static final Class<?>[] ON_CLICK_SIG = new Class[]{View.class};
    private static Class<?>[] ON_ITEM_SIG = new Class[]{AdapterView.class, View.class, Integer.TYPE, Long.TYPE};
    private static Class<?>[] ON_SCROLLED_STATE_SIG = new Class[]{AbsListView.class, Integer.TYPE};
    private static final Class<?>[] OVER_SCROLL_SIG = new Class[]{Integer.TYPE};
    private static Class<?>[] PENDING_TRANSITION_SIG = new Class[]{Integer.TYPE, Integer.TYPE};
    private static final Class<?>[] TEXT_CHANGE_SIG = new Class[]{CharSequence.class, Integer.TYPE, Integer.TYPE, Integer.TYPE};
    private static WeakHashMap<Dialog, Void> dialogs = new WeakHashMap();
    private Activity act;
    protected AccountHandle ah;
    private Constructor<T> constructor;
    private Context context;
    private int policy = 0;
    protected Object progress;
    private HttpHost proxy;
    private View root;
    private Transformer trans;
    protected View view;

    /* access modifiers changed from: protected */
    public T create(View view) {
        T result = null;
        try {
            result = (AbstractAQuery) getConstructor().newInstance(new Object[]{view});
            result.act = this.act;
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
    }

    private Constructor<T> getConstructor() {
        if (this.constructor == null) {
            try {
                this.constructor = getClass().getConstructor(new Class[]{View.class});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.constructor;
    }

    public AbstractAQuery(Activity act) {
        this.act = act;
    }

    public AbstractAQuery(View root) {
        this.root = root;
        this.view = root;
    }

    public AbstractAQuery(Activity act, View root) {
        this.root = root;
        this.view = root;
        this.act = act;
    }

    public AbstractAQuery(Context context) {
        this.context = context;
    }

    private View findView(int id) {
        if (this.root != null) {
            return this.root.findViewById(id);
        }
        if (this.act != null) {
            return this.act.findViewById(id);
        }
        return null;
    }

    private View findView(String tag) {
        if (this.root != null) {
            return this.root.findViewWithTag(tag);
        }
        if (this.act == null) {
            return null;
        }
        View top = ((ViewGroup) this.act.findViewById(16908290)).getChildAt(0);
        if (top != null) {
            return top.findViewWithTag(tag);
        }
        return null;
    }

    private View findView(int... path) {
        View result = findView(path[0]);
        for (int i = 1; i < path.length && result != null; i++) {
            result = result.findViewById(path[i]);
        }
        return result;
    }

    public T find(int id) {
        return create(findView(id));
    }

    public T parent(int id) {
        View node = this.view;
        View result = null;
        while (node != null) {
            if (node.getId() != id) {
                ViewParent p = node.getParent();
                if (!(p instanceof View)) {
                    break;
                }
                node = (View) p;
            } else {
                result = node;
                break;
            }
        }
        return create(result);
    }

    public T recycle(View root) {
        this.root = root;
        this.view = root;
        reset();
        this.context = null;
        return self();
    }

    private T self() {
        return this;
    }

    public View getView() {
        return this.view;
    }

    public T id(int id) {
        return id(findView(id));
    }

    public T id(View view) {
        this.view = view;
        reset();
        return self();
    }

    public T id(String tag) {
        return id(findView(tag));
    }

    public T id(int... path) {
        return id(findView(path));
    }

    public T progress(int id) {
        this.progress = findView(id);
        return self();
    }

    public T progress(Object view) {
        this.progress = view;
        return self();
    }

    public T progress(Dialog dialog) {
        this.progress = dialog;
        return self();
    }

    public T auth(AccountHandle handle) {
        this.ah = handle;
        return self();
    }

    public T transformer(Transformer transformer) {
        this.trans = transformer;
        return self();
    }

    public T policy(int cachePolicy) {
        this.policy = cachePolicy;
        return self();
    }

    public T proxy(String host, int port) {
        this.proxy = new HttpHost(host, port);
        return self();
    }

    public T rating(float rating) {
        if (this.view instanceof RatingBar) {
            this.view.setRating(rating);
        }
        return self();
    }

    public T text(int resid) {
        if (this.view instanceof TextView) {
            this.view.setText(resid);
        }
        return self();
    }

    public T text(int resid, Object... formatArgs) {
        Context context = getContext();
        if (context != null) {
            text(context.getString(resid, formatArgs));
        }
        return self();
    }

    public T text(CharSequence text) {
        if (this.view instanceof TextView) {
            this.view.setText(text);
        }
        return self();
    }

    public T text(CharSequence text, boolean goneIfEmpty) {
        if (goneIfEmpty && (text == null || text.length() == 0)) {
            return gone();
        }
        return text(text);
    }

    public T text(Spanned text) {
        if (this.view instanceof TextView) {
            this.view.setText(text);
        }
        return self();
    }

    public T textColor(int color) {
        if (this.view instanceof TextView) {
            this.view.setTextColor(color);
        }
        return self();
    }

    public T typeface(Typeface tf) {
        if (this.view instanceof TextView) {
            this.view.setTypeface(tf);
        }
        return self();
    }

    public T textSize(float size) {
        if (this.view instanceof TextView) {
            this.view.setTextSize(size);
        }
        return self();
    }

    public T adapter(Adapter adapter) {
        if (this.view instanceof AdapterView) {
            this.view.setAdapter(adapter);
        }
        return self();
    }

    public T adapter(ExpandableListAdapter adapter) {
        if (this.view instanceof ExpandableListView) {
            this.view.setAdapter(adapter);
        }
        return self();
    }

    public T image(int resid) {
        if (this.view instanceof ImageView) {
            ImageView iv = this.view;
            iv.setTag(Constants.TAG_URL, null);
            if (resid == 0) {
                iv.setImageBitmap(null);
            } else {
                iv.setImageResource(resid);
            }
        }
        return self();
    }

    public T image(Drawable drawable) {
        if (this.view instanceof ImageView) {
            ImageView iv = this.view;
            iv.setTag(Constants.TAG_URL, null);
            iv.setImageDrawable(drawable);
        }
        return self();
    }

    public T image(Bitmap bm) {
        if (this.view instanceof ImageView) {
            ImageView iv = this.view;
            iv.setTag(Constants.TAG_URL, null);
            iv.setImageBitmap(bm);
        }
        return self();
    }

    public T image(String url) {
        return image(url, true, true, 0, 0);
    }

    public T image(String url, boolean memCache, boolean fileCache) {
        return image(url, memCache, fileCache, 0, 0);
    }

    public T image(String url, boolean memCache, boolean fileCache, int targetWidth, int fallbackId) {
        return image(url, memCache, fileCache, targetWidth, fallbackId, null, 0);
    }

    public T image(String url, boolean memCache, boolean fileCache, int targetWidth, int fallbackId, Bitmap preset, int animId) {
        return image(url, memCache, fileCache, targetWidth, fallbackId, preset, animId, 0.0f);
    }

    public T image(String url, boolean memCache, boolean fileCache, int targetWidth, int fallbackId, Bitmap preset, int animId, float ratio) {
        return image(url, memCache, fileCache, targetWidth, fallbackId, preset, animId, ratio, 0, null);
    }

    /* access modifiers changed from: protected */
    public T image(String url, boolean memCache, boolean fileCache, int targetWidth, int fallbackId, Bitmap preset, int animId, float ratio, int round, String networkUrl) {
        if (this.view instanceof ImageView) {
            BitmapAjaxCallback.async(this.act, getContext(), (ImageView) this.view, url, memCache, fileCache, targetWidth, fallbackId, preset, animId, ratio, Float.MAX_VALUE, this.progress, this.ah, this.policy, round, this.proxy, networkUrl);
            reset();
        }
        return self();
    }

    public T image(String url, ImageOptions options) {
        return image(url, options, null);
    }

    /* access modifiers changed from: protected */
    public T image(String url, ImageOptions options, String networkUrl) {
        if (this.view instanceof ImageView) {
            BitmapAjaxCallback.async(this.act, getContext(), (ImageView) this.view, url, this.progress, this.ah, options, this.proxy, networkUrl);
            reset();
        }
        return self();
    }

    public T image(BitmapAjaxCallback callback) {
        if (this.view instanceof ImageView) {
            callback.imageView((ImageView) this.view);
            invoke(callback);
        }
        return self();
    }

    public T image(String url, boolean memCache, boolean fileCache, int targetWidth, int resId, BitmapAjaxCallback callback) {
        ((BitmapAjaxCallback) ((BitmapAjaxCallback) callback.targetWidth(targetWidth).fallback(resId).url(url)).memCache(memCache)).fileCache(fileCache);
        return image(callback);
    }

    public T image(File file, int targetWidth) {
        return image(file, true, targetWidth, null);
    }

    public T image(File file, boolean memCache, int targetWidth, BitmapAjaxCallback callback) {
        if (callback == null) {
            callback = new BitmapAjaxCallback();
        }
        callback.file(file);
        String url = null;
        if (file != null) {
            url = file.getAbsolutePath();
        }
        return image(url, memCache, true, targetWidth, 0, callback);
    }

    public T image(Bitmap bm, float ratio) {
        BitmapAjaxCallback cb = new BitmapAjaxCallback();
        cb.ratio(ratio).bitmap(bm);
        return image(cb);
    }

    public T tag(Object tag) {
        if (this.view != null) {
            this.view.setTag(tag);
        }
        return self();
    }

    public T tag(int key, Object tag) {
        if (this.view != null) {
            this.view.setTag(key, tag);
        }
        return self();
    }

    public T transparent(boolean transparent) {
        if (this.view != null) {
            AQUtility.transparent(this.view, transparent);
        }
        return self();
    }

    public T enabled(boolean enabled) {
        if (this.view != null) {
            this.view.setEnabled(enabled);
        }
        return self();
    }

    public T checked(boolean checked) {
        if (this.view instanceof CompoundButton) {
            this.view.setChecked(checked);
        }
        return self();
    }

    public boolean isChecked() {
        if (this.view instanceof CompoundButton) {
            return this.view.isChecked();
        }
        return false;
    }

    public T clickable(boolean clickable) {
        if (this.view != null) {
            this.view.setClickable(clickable);
        }
        return self();
    }

    public T gone() {
        if (!(this.view == null || this.view.getVisibility() == 8)) {
            this.view.setVisibility(8);
        }
        return self();
    }

    public T invisible() {
        if (!(this.view == null || this.view.getVisibility() == 4)) {
            this.view.setVisibility(4);
        }
        return self();
    }

    public T visible() {
        if (!(this.view == null || this.view.getVisibility() == 0)) {
            this.view.setVisibility(0);
        }
        return self();
    }

    public T background(int id) {
        if (this.view != null) {
            if (id != 0) {
                this.view.setBackgroundResource(id);
            } else {
                this.view.setBackgroundDrawable(null);
            }
        }
        return self();
    }

    public T backgroundColor(int color) {
        if (this.view != null) {
            this.view.setBackgroundColor(color);
        }
        return self();
    }

    public T dataChanged() {
        if (this.view instanceof AdapterView) {
            Adapter a = this.view.getAdapter();
            if (a instanceof BaseAdapter) {
                ((BaseAdapter) a).notifyDataSetChanged();
            }
        }
        return self();
    }

    public boolean isExist() {
        return this.view != null;
    }

    public Object getTag() {
        if (this.view != null) {
            return this.view.getTag();
        }
        return null;
    }

    public Object getTag(int id) {
        if (this.view != null) {
            return this.view.getTag(id);
        }
        return null;
    }

    public ImageView getImageView() {
        return (ImageView) this.view;
    }

    public Gallery getGallery() {
        return (Gallery) this.view;
    }

    public TextView getTextView() {
        return (TextView) this.view;
    }

    public EditText getEditText() {
        return (EditText) this.view;
    }

    public ProgressBar getProgressBar() {
        return (ProgressBar) this.view;
    }

    public SeekBar getSeekBar() {
        return (SeekBar) this.view;
    }

    public Button getButton() {
        return (Button) this.view;
    }

    public CheckBox getCheckBox() {
        return (CheckBox) this.view;
    }

    public ListView getListView() {
        return (ListView) this.view;
    }

    public ExpandableListView getExpandableListView() {
        return (ExpandableListView) this.view;
    }

    public GridView getGridView() {
        return (GridView) this.view;
    }

    public RatingBar getRatingBar() {
        return (RatingBar) this.view;
    }

    public WebView getWebView() {
        return (WebView) this.view;
    }

    public Spinner getSpinner() {
        return (Spinner) this.view;
    }

    public Editable getEditable() {
        if (this.view instanceof EditText) {
            return ((EditText) this.view).getEditableText();
        }
        return null;
    }

    public CharSequence getText() {
        if (this.view instanceof TextView) {
            return ((TextView) this.view).getText();
        }
        return null;
    }

    public Object getSelectedItem() {
        if (this.view instanceof AdapterView) {
            return ((AdapterView) this.view).getSelectedItem();
        }
        return null;
    }

    public int getSelectedItemPosition() {
        if (this.view instanceof AdapterView) {
            return ((AdapterView) this.view).getSelectedItemPosition();
        }
        return -1;
    }

    public T clicked(Object handler, String method) {
        return clicked(new Common().forward(handler, method, true, ON_CLICK_SIG));
    }

    public T clicked(OnClickListener listener) {
        if (this.view != null) {
            this.view.setOnClickListener(listener);
        }
        return self();
    }

    public T longClicked(Object handler, String method) {
        return longClicked(new Common().forward(handler, method, true, ON_CLICK_SIG));
    }

    public T longClicked(OnLongClickListener listener) {
        if (this.view != null) {
            this.view.setOnLongClickListener(listener);
        }
        return self();
    }

    public T itemClicked(Object handler, String method) {
        return itemClicked(new Common().forward(handler, method, true, ON_ITEM_SIG));
    }

    public T itemClicked(OnItemClickListener listener) {
        if (this.view instanceof AdapterView) {
            this.view.setOnItemClickListener(listener);
        }
        return self();
    }

    public T itemSelected(Object handler, String method) {
        return itemSelected(new Common().forward(handler, method, true, ON_ITEM_SIG));
    }

    public T itemSelected(OnItemSelectedListener listener) {
        if (this.view instanceof AdapterView) {
            this.view.setOnItemSelectedListener(listener);
        }
        return self();
    }

    public T setSelection(int position) {
        if (this.view instanceof AdapterView) {
            this.view.setSelection(position);
        }
        return self();
    }

    public T scrolledBottom(Object handler, String method) {
        if (this.view instanceof AbsListView) {
            setScrollListener().forward(handler, method, true, ON_SCROLLED_STATE_SIG);
        }
        return self();
    }

    private Common setScrollListener() {
        AbsListView lv = this.view;
        Common common = (Common) lv.getTag(Constants.TAG_SCROLL_LISTENER);
        if (common != null) {
            return common;
        }
        common = new Common();
        lv.setOnScrollListener(common);
        lv.setTag(Constants.TAG_SCROLL_LISTENER, common);
        AQUtility.debug((Object) "set scroll listenr");
        return common;
    }

    public T scrolled(OnScrollListener listener) {
        if (this.view instanceof AbsListView) {
            setScrollListener().forward(listener);
        }
        return self();
    }

    public T textChanged(Object handler, String method) {
        if (this.view instanceof TextView) {
            this.view.addTextChangedListener(new Common().forward(handler, method, true, TEXT_CHANGE_SIG));
        }
        return self();
    }

    public T overridePendingTransition5(int enterAnim, int exitAnim) {
        if (this.act != null) {
            AQUtility.invokeHandler(this.act, "overridePendingTransition", false, false, PENDING_TRANSITION_SIG, Integer.valueOf(enterAnim), Integer.valueOf(exitAnim));
        }
        return self();
    }

    public T setOverScrollMode9(int mode) {
        if (this.view instanceof AbsListView) {
            AQUtility.invokeHandler(this.view, "setOverScrollMode", false, false, OVER_SCROLL_SIG, Integer.valueOf(mode));
        }
        return self();
    }

    public T setLayerType11(int type, Paint paint) {
        if (this.view != null) {
            AQUtility.invokeHandler(this.view, "setLayerType", false, false, LAYER_TYPE_SIG, Integer.valueOf(type), paint);
        }
        return self();
    }

    public Object invoke(String method, Class<?>[] sig, Object... params) {
        Object handler = this.view;
        if (handler == null) {
            handler = this.act;
        }
        return AQUtility.invokeHandler(handler, method, false, false, sig, params);
    }

    public T hardwareAccelerated11() {
        if (this.act != null) {
            this.act.getWindow().setFlags(Constants.FLAG_HARDWARE_ACCELERATED, Constants.FLAG_HARDWARE_ACCELERATED);
        }
        return self();
    }

    public T clear() {
        if (this.view != null) {
            if (this.view instanceof ImageView) {
                ImageView iv = this.view;
                iv.setImageBitmap(null);
                iv.setTag(Constants.TAG_URL, null);
            } else if (this.view instanceof WebView) {
                WebView wv = this.view;
                wv.stopLoading();
                wv.clearView();
                wv.setTag(Constants.TAG_URL, null);
            } else if (this.view instanceof TextView) {
                this.view.setText("");
            }
        }
        return self();
    }

    public T margin(float leftDip, float topDip, float rightDip, float bottomDip) {
        if (this.view != null) {
            LayoutParams lp = this.view.getLayoutParams();
            if (lp instanceof MarginLayoutParams) {
                Context context = getContext();
                ((MarginLayoutParams) lp).setMargins(AQUtility.dip2pixel(context, leftDip), AQUtility.dip2pixel(context, topDip), AQUtility.dip2pixel(context, rightDip), AQUtility.dip2pixel(context, bottomDip));
                this.view.setLayoutParams(lp);
            }
        }
        return self();
    }

    public T width(int dip) {
        size(true, dip, true);
        return self();
    }

    public T height(int dip) {
        size(false, dip, true);
        return self();
    }

    public T width(int width, boolean dip) {
        size(true, width, dip);
        return self();
    }

    public T height(int height, boolean dip) {
        size(false, height, dip);
        return self();
    }

    private void size(boolean width, int n, boolean dip) {
        if (this.view != null) {
            LayoutParams lp = this.view.getLayoutParams();
            Context context = getContext();
            if (n > 0 && dip) {
                n = AQUtility.dip2pixel(context, (float) n);
            }
            if (width) {
                lp.width = n;
            } else {
                lp.height = n;
            }
            this.view.setLayoutParams(lp);
        }
    }

    public Context getContext() {
        if (this.act != null) {
            return this.act;
        }
        if (this.root != null) {
            return this.root.getContext();
        }
        return this.context;
    }

    public <K> T ajax(AjaxCallback<K> callback) {
        return invoke(callback);
    }

    /* access modifiers changed from: protected */
    public <K> T invoke(AbstractAjaxCallback<?, K> cb) {
        cb.auth(this.ah);
        cb.progress(this.progress);
        cb.transformer(this.trans);
        cb.policy(this.policy);
        if (this.proxy != null) {
            cb.proxy(this.proxy.getHostName(), this.proxy.getPort());
        }
        if (this.act != null) {
            cb.async(this.act);
        } else {
            cb.async(getContext());
        }
        reset();
        return self();
    }

    /* access modifiers changed from: protected */
    public void reset() {
        this.ah = null;
        this.progress = null;
        this.trans = null;
        this.policy = 0;
        this.proxy = null;
    }

    public <K> T ajax(String url, Class<K> type, AjaxCallback<K> callback) {
        ((AjaxCallback) callback.type(type)).url(url);
        return ajax(callback);
    }

    public <K> T ajax(String url, Class<K> type, long expire, AjaxCallback<K> callback) {
        ((AjaxCallback) ((AjaxCallback) ((AjaxCallback) callback.type(type)).url(url)).fileCache(true)).expire(expire);
        return ajax(callback);
    }

    public <K> T ajax(String url, Class<K> type, Object handler, String callback) {
        AjaxCallback<K> cb = new AjaxCallback();
        ((AjaxCallback) cb.type(type)).weakHandler(handler, callback);
        return ajax(url, type, cb);
    }

    public <K> T ajax(String url, Class<K> type, long expire, Object handler, String callback) {
        AjaxCallback<K> cb = new AjaxCallback();
        ((AjaxCallback) ((AjaxCallback) ((AjaxCallback) cb.type(type)).weakHandler(handler, callback)).fileCache(true)).expire(expire);
        return ajax(url, type, cb);
    }

    public <K> T ajax(String url, Map<String, ?> params, Class<K> type, AjaxCallback<K> callback) {
        ((AjaxCallback) ((AjaxCallback) callback.type(type)).url(url)).params(params);
        return ajax(callback);
    }

    public <K> T ajax(String url, Map<String, ?> params, Class<K> type, Object handler, String callback) {
        AjaxCallback cb = new AjaxCallback();
        ((AjaxCallback) cb.type(type)).weakHandler(handler, callback);
        return ajax(url, (Map) params, (Class) type, cb);
    }

    public <K> T delete(String url, Class<K> type, AjaxCallback<K> callback) {
        ((AjaxCallback) ((AjaxCallback) callback.url(url)).type(type)).method(2);
        return ajax(callback);
    }

    public <K> T put(String url, String contentHeader, HttpEntity entity, Class<K> type, AjaxCallback<K> callback) {
        ((AjaxCallback) ((AjaxCallback) ((AjaxCallback) ((AjaxCallback) callback.url(url)).type(type)).method(3)).header("Content-Type", contentHeader)).param(Constants.POST_ENTITY, entity);
        return ajax(callback);
    }

    public <K> T delete(String url, Class<K> type, Object handler, String callback) {
        AjaxCallback<K> cb = new AjaxCallback();
        cb.weakHandler(handler, callback);
        return delete(url, type, cb);
    }

    public <K> T sync(AjaxCallback<K> callback) {
        ajax(callback);
        callback.block();
        return self();
    }

    public T cache(String url, long expire) {
        return ajax(url, byte[].class, expire, null, null);
    }

    public T ajaxCancel() {
        AbstractAjaxCallback.cancel();
        return self();
    }

    public File getCachedFile(String url) {
        File result = AQUtility.getExistedCacheByUrl(AQUtility.getCacheDir(getContext(), 1), url);
        if (result == null) {
            return AQUtility.getExistedCacheByUrl(AQUtility.getCacheDir(getContext(), 0), url);
        }
        return result;
    }

    public T invalidate(String url) {
        File file = getCachedFile(url);
        if (file != null) {
            file.delete();
        }
        return self();
    }

    public Bitmap getCachedImage(String url) {
        return getCachedImage(url, 0);
    }

    public Bitmap getCachedImage(String url, int targetWidth) {
        Bitmap result = BitmapAjaxCallback.getMemoryCached(url, targetWidth);
        if (result != null) {
            return result;
        }
        File file = getCachedFile(url);
        if (file != null) {
            return BitmapAjaxCallback.getResizedImage(file.getAbsolutePath(), null, targetWidth, true, 0);
        }
        return result;
    }

    public Bitmap getCachedImage(int resId) {
        return BitmapAjaxCallback.getMemoryCached(getContext(), resId);
    }

    @Deprecated
    public boolean shouldDelay(View convertView, ViewGroup parent, String url, float velocity) {
        return Common.shouldDelay(convertView, parent, url, velocity, true);
    }

    @Deprecated
    public boolean shouldDelay(View convertView, ViewGroup parent, String url, float velocity, boolean fileCheck) {
        return Common.shouldDelay(convertView, parent, url, velocity, fileCheck);
    }

    public boolean shouldDelay(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent, String url) {
        return Common.shouldDelay(groupPosition, -1, convertView, parent, url);
    }

    public boolean shouldDelay(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent, String url) {
        return Common.shouldDelay(groupPosition, childPosition, convertView, parent, url);
    }

    public boolean shouldDelay(int position, View convertView, ViewGroup parent, String url) {
        if (!(parent instanceof ExpandableListView)) {
            return Common.shouldDelay(position, convertView, parent, url);
        }
        throw new IllegalArgumentException("Please use the other shouldDelay methods for expandable list.");
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    public java.io.File makeSharedFile(java.lang.String r12, java.lang.String r13) {
        /*
        r11 = this;
        r8 = 0;
        r6 = r11.getCachedFile(r12);	 Catch:{ Exception -> 0x004e }
        if (r6 == 0) goto L_0x003b;
    L_0x0007:
        r10 = com.androidquery.util.AQUtility.getTempDir();	 Catch:{ Exception -> 0x004e }
        if (r10 == 0) goto L_0x003b;
    L_0x000d:
        r9 = new java.io.File;	 Catch:{ Exception -> 0x004e }
        r9.<init>(r10, r13);	 Catch:{ Exception -> 0x004e }
        r9.createNewFile();	 Catch:{ Exception -> 0x0048 }
        r1 = new java.io.FileInputStream;	 Catch:{ Exception -> 0x0048 }
        r1.<init>(r6);	 Catch:{ Exception -> 0x0048 }
        r0 = r1.getChannel();	 Catch:{ Exception -> 0x0048 }
        r1 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x0048 }
        r1.<init>(r9);	 Catch:{ Exception -> 0x0048 }
        r5 = r1.getChannel();	 Catch:{ Exception -> 0x0048 }
        r1 = 0;
        r3 = r0.size();	 Catch:{ all -> 0x003c }
        r0.transferTo(r1, r3, r5);	 Catch:{ all -> 0x003c }
        if (r0 == 0) goto L_0x0035;
    L_0x0032:
        r0.close();	 Catch:{ Exception -> 0x0048 }
    L_0x0035:
        if (r5 == 0) goto L_0x0050;
    L_0x0037:
        r5.close();	 Catch:{ Exception -> 0x0048 }
        r8 = r9;
    L_0x003b:
        return r8;
    L_0x003c:
        r1 = move-exception;
        if (r0 == 0) goto L_0x0042;
    L_0x003f:
        r0.close();	 Catch:{ Exception -> 0x0048 }
    L_0x0042:
        if (r5 == 0) goto L_0x0047;
    L_0x0044:
        r5.close();	 Catch:{ Exception -> 0x0048 }
    L_0x0047:
        throw r1;	 Catch:{ Exception -> 0x0048 }
    L_0x0048:
        r7 = move-exception;
        r8 = r9;
    L_0x004a:
        com.androidquery.util.AQUtility.debug(r7);
        goto L_0x003b;
    L_0x004e:
        r7 = move-exception;
        goto L_0x004a;
    L_0x0050:
        r8 = r9;
        goto L_0x003b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.androidquery.AbstractAQuery.makeSharedFile(java.lang.String, java.lang.String):java.io.File");
    }

    public T animate(int animId) {
        return animate(animId, null);
    }

    public T animate(int animId, AnimationListener listener) {
        Animation anim = AnimationUtils.loadAnimation(getContext(), animId);
        anim.setAnimationListener(listener);
        return animate(anim);
    }

    public T animate(Animation anim) {
        if (!(this.view == null || anim == null)) {
            this.view.startAnimation(anim);
        }
        return self();
    }

    public T click() {
        if (this.view != null) {
            this.view.performClick();
        }
        return self();
    }

    public T longClick() {
        if (this.view != null) {
            this.view.performLongClick();
        }
        return self();
    }

    public T show(Dialog dialog) {
        if (dialog != null) {
            try {
                dialog.show();
                dialogs.put(dialog, null);
            } catch (Exception e) {
            }
        }
        return self();
    }

    public T dismiss(Dialog dialog) {
        if (dialog != null) {
            try {
                dialogs.remove(dialog);
                dialog.dismiss();
            } catch (Exception e) {
            }
        }
        return self();
    }

    public T dismiss() {
        Iterator<Dialog> keys = dialogs.keySet().iterator();
        while (keys.hasNext()) {
            try {
                ((Dialog) keys.next()).dismiss();
            } catch (Exception e) {
            }
            keys.remove();
        }
        return self();
    }

    public T webImage(String url) {
        return webImage(url, true, false, -16777216);
    }

    public T webImage(String url, boolean zoom, boolean control, int color) {
        if (this.view instanceof WebView) {
            setLayerType11(1, null);
            new WebImage((WebView) this.view, url, this.progress, zoom, control, color).load();
            this.progress = null;
        }
        return self();
    }

    public View inflate(View convertView, int layoutId, ViewGroup root) {
        LayoutInflater inflater;
        if (convertView != null) {
            Integer layout = (Integer) convertView.getTag(Constants.TAG_LAYOUT);
            if (layout != null && layout.intValue() == layoutId) {
                return convertView;
            }
        }
        if (this.act != null) {
            inflater = this.act.getLayoutInflater();
        } else {
            inflater = (LayoutInflater) getContext().getSystemService("layout_inflater");
        }
        View view = inflater.inflate(layoutId, root, false);
        view.setTag(Constants.TAG_LAYOUT, Integer.valueOf(layoutId));
        return view;
    }

    public T expand(int position, boolean expand) {
        if (this.view instanceof ExpandableListView) {
            ExpandableListView elv = this.view;
            if (expand) {
                elv.expandGroup(position);
            } else {
                elv.collapseGroup(position);
            }
        }
        return self();
    }

    public T expand(boolean expand) {
        if (this.view instanceof ExpandableListView) {
            ExpandableListView elv = this.view;
            ExpandableListAdapter ela = elv.getExpandableListAdapter();
            if (ela != null) {
                int count = ela.getGroupCount();
                for (int i = 0; i < count; i++) {
                    if (expand) {
                        elv.expandGroup(i);
                    } else {
                        elv.collapseGroup(i);
                    }
                }
            }
        }
        return self();
    }

    public T download(String url, File target, AjaxCallback<File> cb) {
        ((AjaxCallback) ((AjaxCallback) cb.url(url)).type(File.class)).targetFile(target);
        return ajax(cb);
    }

    public T download(String url, File target, Object handler, String callback) {
        AjaxCallback<File> cb = new AjaxCallback();
        cb.weakHandler(handler, callback);
        return download(url, target, cb);
    }
}
