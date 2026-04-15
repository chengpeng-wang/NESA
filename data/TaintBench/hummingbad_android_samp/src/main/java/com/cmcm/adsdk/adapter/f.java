package com.cmcm.adsdk.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import com.cleanmaster.ui.app.market.Ad;
import com.cleanmaster.ui.app.utils.MarketContext;
import com.cmcm.adsdk.Const;
import com.cmcm.adsdk.adapter.BaseNativeAdapter.NativeAdapterListener;
import com.cmcm.adsdk.nativead.CMNativeAd;
import com.cmcm.baseapi.utils.Assure;
import com.moceanmobile.mast.MASTNativeAd;
import com.moceanmobile.mast.MASTNativeAd.NativeRequestListener;
import com.moceanmobile.mast.bean.DataAssetRequest;
import com.moceanmobile.mast.bean.DataAssetTypes;
import com.moceanmobile.mast.bean.ImageAssetRequest;
import com.moceanmobile.mast.bean.ImageAssetTypes;
import com.moceanmobile.mast.bean.TitleAssetRequest;
import java.util.ArrayList;
import java.util.Map;

/* compiled from: PubMaticNativeAdapter */
public final class f extends BaseNativeAdapter {

    /* compiled from: PubMaticNativeAdapter */
    private class a extends CMNativeAd implements OnClickListener, OnTouchListener, NativeRequestListener {
        private NativeAdapterListener b;
        private Map<String, Object> c;
        private Context d;
        /* access modifiers changed from: private */
        public MASTNativeAd e;
        private View f;
        private Handler g = null;

        public a(Context context, @NonNull NativeAdapterListener nativeAdapterListener, @Nullable Map<String, Object> map) {
            this.d = context.getApplicationContext();
            this.b = nativeAdapterListener;
            this.c = map;
            this.g = new Handler(Looper.getMainLooper());
        }

        public final void a() {
            int intValue = ((Integer) this.c.get(CMNativeAd.KEY_PLACEMENT_ID)).intValue();
            setCacheTime(((Long) this.c.get(CMNativeAd.KEY_CACHE_TIME)).longValue());
            Assure.checkNotNull(Integer.valueOf(intValue));
            setJuhePosid((String) this.c.get(CMNativeAd.KEY_JUHE_POSID));
            setReportRes(((Integer) this.c.get(CMNativeAd.KEY_REPORT_RES)).intValue());
            setReportPkgName((String) this.c.get(CMNativeAd.KEY_REPORT_PKGNAME));
            this.e = new MASTNativeAd(new MarketContext(this.d));
            this.e.setRequestListener(this);
            this.e.setZone(intValue);
            MASTNativeAd mASTNativeAd = this.e;
            ArrayList arrayList = new ArrayList();
            TitleAssetRequest titleAssetRequest = new TitleAssetRequest();
            titleAssetRequest.setAssetId(1001);
            titleAssetRequest.setLength(50);
            titleAssetRequest.setRequired(true);
            arrayList.add(titleAssetRequest);
            ImageAssetRequest imageAssetRequest = new ImageAssetRequest();
            imageAssetRequest.setAssetId(1002);
            imageAssetRequest.setImageType(ImageAssetTypes.main);
            imageAssetRequest.setRequired(true);
            arrayList.add(imageAssetRequest);
            imageAssetRequest = new ImageAssetRequest();
            imageAssetRequest.setAssetId(Ad.RECOMMEND_SHOW_TYPE_HIGH);
            imageAssetRequest.setImageType(ImageAssetTypes.icon);
            imageAssetRequest.setRequired(true);
            arrayList.add(imageAssetRequest);
            DataAssetRequest dataAssetRequest = new DataAssetRequest();
            dataAssetRequest.setAssetId(Ad.SHOW_TYPE_BIG_ICON);
            dataAssetRequest.setDataAssetType(DataAssetTypes.downloads);
            arrayList.add(dataAssetRequest);
            dataAssetRequest = new DataAssetRequest();
            dataAssetRequest.setAssetId(Ad.RECOMMEND_SHOW_TYPE_LOW);
            dataAssetRequest.setDataAssetType(DataAssetTypes.ctatext);
            arrayList.add(dataAssetRequest);
            dataAssetRequest = new DataAssetRequest();
            dataAssetRequest.setAssetId(Ad.SHOW_TYPE_ICON_LIST);
            dataAssetRequest.setDataAssetType(DataAssetTypes.desc);
            dataAssetRequest.setRequired(true);
            arrayList.add(dataAssetRequest);
            dataAssetRequest = new DataAssetRequest();
            dataAssetRequest.setAssetId(Ad.SHOW_TYPE_TWO_PIC);
            dataAssetRequest.setDataAssetType(DataAssetTypes.rating);
            arrayList.add(dataAssetRequest);
            mASTNativeAd.addNativeAssetRequestList(arrayList);
            this.e.setUseInternalBrowser(false);
            this.g.post(new Runnable() {
                public final void run() {
                    a.this.e.update();
                }
            });
        }

        public final String getAdTypeName() {
            return Const.KEY_PM;
        }

        public final void registerViewForInteraction(View view) {
            recordImpression();
            if (view != null && this.e != null) {
                this.e.sendImpression();
                this.e.trackViewForInteractions(view);
                this.f = view;
                addClickListener(view, this, this);
            }
        }

        public final void unregisterView() {
            if (this.e != null) {
                this.e.reset();
                this.e.destroy();
            }
            if (this.f != null) {
                clearClickListener(this.f);
                this.f = null;
            }
        }

        public final Object getAdObject() {
            return this.e;
        }

        public final void handleClick() {
        }

        public final void onNativeAdFailed(MASTNativeAd mastNativeAd, Exception e) {
            if (this.b != null) {
                this.b.onNativeAdFailed(e.getMessage());
            }
        }

        public final void onReceivedThirdPartyRequest(MASTNativeAd mastNativeAd, Map<String, String> map, Map<String, String> map2) {
        }

        public final void onNativeAdClicked(MASTNativeAd mastNativeAd) {
            if (this.b != null) {
                this.b.onNativeAdClick(this);
            }
        }

        public final void onClick(View view) {
            recordClick();
            if (this.mInnerClickListener != null) {
                this.mInnerClickListener.onClickStart(false);
                this.mInnerClickListener.onClickFinish(false);
            }
        }

        public final boolean onTouch(View view, MotionEvent motionEvent) {
            return false;
        }

        public final void onNativeAdReceived(com.moceanmobile.mast.MASTNativeAd r5) {
            /*
            r4 = this;
            r0 = r5.getNativeAssets();
            if (r0 == 0) goto L_0x009a;
        L_0x0006:
            r1 = r0.iterator();
        L_0x000a:
            r0 = r1.hasNext();
            if (r0 == 0) goto L_0x009a;
        L_0x0010:
            r0 = r1.next();
            r0 = (com.moceanmobile.mast.bean.AssetResponse) r0;
            r2 = r0.getAssetId();	 Catch:{ Exception -> 0x002a }
            switch(r2) {
                case 1001: goto L_0x001e;
                case 1002: goto L_0x002f;
                case 1003: goto L_0x0045;
                case 1004: goto L_0x005b;
                case 1005: goto L_0x0067;
                case 1006: goto L_0x0073;
                case 1007: goto L_0x007f;
                default: goto L_0x001d;
            };	 Catch:{ Exception -> 0x002a }
        L_0x001d:
            goto L_0x000a;
        L_0x001e:
            r0 = (com.moceanmobile.mast.bean.TitleAssetResponse) r0;	 Catch:{ Exception -> 0x002a }
            if (r0 == 0) goto L_0x000a;
        L_0x0022:
            r0 = r0.getTitleText();	 Catch:{ Exception -> 0x002a }
            r4.setTitle(r0);	 Catch:{ Exception -> 0x002a }
            goto L_0x000a;
        L_0x002a:
            r0 = move-exception;
            r0.getMessage();
            goto L_0x000a;
        L_0x002f:
            r0 = (com.moceanmobile.mast.bean.ImageAssetResponse) r0;	 Catch:{ Exception -> 0x002a }
            if (r0 == 0) goto L_0x000a;
        L_0x0033:
            r2 = r0.getImage();	 Catch:{ Exception -> 0x002a }
            if (r2 == 0) goto L_0x000a;
        L_0x0039:
            r0 = r0.getImage();	 Catch:{ Exception -> 0x002a }
            r0 = r0.getUrl();	 Catch:{ Exception -> 0x002a }
            r4.setAdCoverImageUrl(r0);	 Catch:{ Exception -> 0x002a }
            goto L_0x000a;
        L_0x0045:
            r0 = (com.moceanmobile.mast.bean.ImageAssetResponse) r0;	 Catch:{ Exception -> 0x002a }
            if (r0 == 0) goto L_0x000a;
        L_0x0049:
            r2 = r0.getImage();	 Catch:{ Exception -> 0x002a }
            if (r2 == 0) goto L_0x000a;
        L_0x004f:
            r0 = r0.getImage();	 Catch:{ Exception -> 0x002a }
            r0 = r0.getUrl();	 Catch:{ Exception -> 0x002a }
            r4.setAdIconUrl(r0);	 Catch:{ Exception -> 0x002a }
            goto L_0x000a;
        L_0x005b:
            r0 = (com.moceanmobile.mast.bean.DataAssetResponse) r0;	 Catch:{ Exception -> 0x002a }
            if (r0 == 0) goto L_0x000a;
        L_0x005f:
            r0 = r0.getValue();	 Catch:{ Exception -> 0x002a }
            r4.setAdSocialContext(r0);	 Catch:{ Exception -> 0x002a }
            goto L_0x000a;
        L_0x0067:
            r0 = (com.moceanmobile.mast.bean.DataAssetResponse) r0;	 Catch:{ Exception -> 0x002a }
            if (r0 == 0) goto L_0x000a;
        L_0x006b:
            r0 = r0.getValue();	 Catch:{ Exception -> 0x002a }
            r4.setAdCallToAction(r0);	 Catch:{ Exception -> 0x002a }
            goto L_0x000a;
        L_0x0073:
            r0 = (com.moceanmobile.mast.bean.DataAssetResponse) r0;	 Catch:{ Exception -> 0x002a }
            if (r0 == 0) goto L_0x000a;
        L_0x0077:
            r0 = r0.getValue();	 Catch:{ Exception -> 0x002a }
            r4.setAdBody(r0);	 Catch:{ Exception -> 0x002a }
            goto L_0x000a;
        L_0x007f:
            r0 = (com.moceanmobile.mast.bean.DataAssetResponse) r0;	 Catch:{ Exception -> 0x002a }
            if (r0 == 0) goto L_0x000a;
        L_0x0083:
            r2 = r0.getValue();	 Catch:{ Exception -> 0x002a }
            r2 = android.text.TextUtils.isEmpty(r2);	 Catch:{ Exception -> 0x002a }
            if (r2 != 0) goto L_0x000a;
        L_0x008d:
            r0 = r0.getValue();	 Catch:{ Exception -> 0x002a }
            r2 = java.lang.Double.parseDouble(r0);	 Catch:{ Exception -> 0x002a }
            r4.setAdStarRate(r2);	 Catch:{ Exception -> 0x002a }
            goto L_0x000a;
        L_0x009a:
            r0 = r4.b;
            if (r0 == 0) goto L_0x00a3;
        L_0x009e:
            r0 = r4.b;
            r0.onNativeAdLoaded(r4);
        L_0x00a3:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.cmcm.adsdk.adapter.f$a.onNativeAdReceived(com.moceanmobile.mast.MASTNativeAd):void");
        }
    }

    public final void a(@NonNull Context context, @NonNull NativeAdapterListener nativeAdapterListener, @NonNull Map<String, Object> map) {
        new a(context, nativeAdapterListener, map).a();
    }
}
