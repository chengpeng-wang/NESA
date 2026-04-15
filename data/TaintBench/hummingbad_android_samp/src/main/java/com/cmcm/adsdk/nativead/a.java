package com.cmcm.adsdk.nativead;

import android.content.Context;

/* compiled from: CMPicksNativeAd */
public abstract class a extends CMNativeAd {
    public final void a(Context context) {
        if (this.mInnerClickListener == null) {
            PicksLoadingActivity.startLoadingDialog(context);
        } else {
            this.mInnerClickListener.onClickStart(true);
        }
        handleClick();
    }

    public final boolean b(Context context) {
        if (this.mInnerClickListener != null) {
            return this.mInnerClickListener.onClickFinish(isDownLoadApp().booleanValue());
        }
        PicksLoadingActivity.closeLoadingDialog(context);
        return !PicksLoadingActivity.mNeedJumpGP;
    }
}
