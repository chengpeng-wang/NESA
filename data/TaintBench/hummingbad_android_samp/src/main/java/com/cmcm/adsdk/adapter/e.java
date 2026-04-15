package com.cmcm.adsdk.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import com.cleanmaster.ui.app.market.Ad;
import com.cmcm.adsdk.Const;
import com.cmcm.adsdk.adapter.BaseNativeAdapter.NativeAdapterListener;
import com.cmcm.adsdk.nativead.CMNativeAd;
import com.cmcm.adsdk.nativead.PicksViewCheckHelper;
import com.cmcm.adsdk.nativead.PicksViewCheckHelper.AdImpressionListener;
import com.picksinit.ClickAdFinishListener;
import com.picksinit.ErrorInfo;
import com.picksinit.ICallBack;
import com.picksinit.c;
import com.picksinit.goGp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* compiled from: PicksNativeAdapter */
public final class e extends BaseNativeAdapter implements ICallBack {
    /* access modifiers changed from: private */
    public String a;
    /* access modifiers changed from: private */
    public NativeAdapterListener b;
    private Map<String, Object> c;
    /* access modifiers changed from: private */
    public Context d;
    private int e = 1;

    /* compiled from: PicksNativeAdapter */
    private class a extends com.cmcm.adsdk.nativead.a implements OnClickListener, OnTouchListener, AdImpressionListener {
        final /* synthetic */ e a;
        private PicksViewCheckHelper b;
        private Ad c;
        private View d;

        public a(e eVar, Ad ad) {
            boolean z = true;
            this.a = eVar;
            this.c = ad;
            if (ad.getAppShowType() == Ad.SHOW_TYPE_NEWS_SMALL_PIC || ad.getAppShowType() == Ad.SHOW_TYPE_NEWS_THREE_PIC) {
                com.cmcm.adsdk.requestconfig.log.a.b(Const.TAG, "70003|70002 pic size=" + ad.getExtPics().size());
                setExtPics(ad.getExtPics());
            }
            setJuhePosid(this.a.a);
            setTitle(ad.getTitle());
            setAdCoverImageUrl(ad.getBackground());
            setAdIconUrl(ad.getPicUrl());
            setAdCallToAction(ad.getButtonTxt());
            setAdBody(ad.getDesc());
            setAdStarRate(ad.getRating());
            setAdSocialContext(ad.getDownloadNum());
            setIsDownloadApp(Boolean.valueOf(ad.getMtType() == 8));
            if (this.c.getPriority() != 1) {
                z = false;
            }
            setIsPriority(z);
        }

        public final String getAdTypeName() {
            return Const.KEY_CM;
        }

        public final void registerViewForInteraction(View view) {
            if (view != null) {
                boolean z;
                this.d = view;
                addClickListener(view, this, this);
                Context a = this.a.d;
                if (this.c.getResType() == 56) {
                    z = true;
                } else {
                    z = false;
                }
                this.b = new PicksViewCheckHelper(a, view, this, z);
                this.b.a();
            }
        }

        public final void unregisterView() {
            clearClickListener(this.d);
            if (this.d != null) {
                this.d = null;
            }
            if (this.b != null) {
                this.b.b();
            }
        }

        public final boolean onTouch(View view, MotionEvent motionEvent) {
            return false;
        }

        public final boolean hasExpired() {
            return !this.c.isAvailAble() || this.c.isShowed();
        }

        public final Object getAdObject() {
            return this.c;
        }

        public final void onAdImpression() {
            if (!(getRenderType() == -1 || TextUtils.isEmpty(getRenderTime()))) {
                c.getInstance().reportRender(this.c, this.a.a, getRenderTime(), getRenderType());
            }
            recordImpression();
        }

        public final void handleClick() {
            c.getInstance().onClickAdNoDialog(this.a.d, this.a.a, this.c, new ClickAdFinishListener() {
                public final void onClickFinish(goGp go) {
                    boolean b = a.this.b(a.this.a.d);
                    if (go != null && !b) {
                        go.goGp(a.this.a.d);
                    }
                }
            });
        }

        public final void onClick(View view) {
            if (isDownLoadApp().booleanValue()) {
                a(this.a.d);
            } else {
                handleClick();
            }
            if (this.a.b != null) {
                this.a.b.onNativeAdClick(this);
            }
        }
    }

    public final void a(@NonNull Context context, @NonNull NativeAdapterListener nativeAdapterListener, @NonNull Map<String, Object> map) {
        this.d = context.getApplicationContext();
        this.b = nativeAdapterListener;
        this.c = map;
        this.a = (String) this.c.get(CMNativeAd.KEY_PLACEMENT_ID);
        this.e = ((Integer) this.c.get(CMNativeAd.KEY_LOAD_SIZE)).intValue();
        c.getInstance().loadad(Integer.valueOf(this.a).intValue(), this, this.e, 0);
    }

    public final void onLoadSuccess(List list) {
        if (list != null && !list.isEmpty()) {
            List arrayList = new ArrayList();
            for (Object next : list) {
                if (next instanceof Ad) {
                    arrayList.add(new a(this, (Ad) next));
                }
            }
            if (this.b != null) {
                this.b.onNativeAdLoaded(arrayList);
            }
        }
    }

    public final void onLoadError(ErrorInfo errorInfo) {
        if (this.b != null) {
            this.b.onNativeAdFailed(errorInfo.toString());
        }
    }

    public final void onPreExecute() {
    }
}
