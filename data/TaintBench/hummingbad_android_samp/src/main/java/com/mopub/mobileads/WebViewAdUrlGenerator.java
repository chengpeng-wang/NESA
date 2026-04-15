package com.mopub.mobileads;

import android.content.Context;
import com.cleanmaster.ui.app.market.transport.CmMarketHttpClient.MarketRequestBuilder;
import com.mopub.common.AdUrlGenerator;
import com.mopub.common.ClientMetadata;
import com.mopub.common.Constants;

public class WebViewAdUrlGenerator extends AdUrlGenerator {
    private final boolean mIsStorePictureSupported;

    public WebViewAdUrlGenerator(Context context, boolean isStorePictureSupported) {
        super(context);
        this.mIsStorePictureSupported = isStorePictureSupported;
    }

    public String generateUrlString(String serverHostname) {
        initUrlString(serverHostname, Constants.AD_HANDLER);
        setApiVersion(MarketRequestBuilder.REQUEST_HOT_APPS_FROM_GIFTBOX);
        addBaseParams(ClientMetadata.getInstance(this.mContext));
        setMraidFlag(true);
        setExternalStoragePermission(this.mIsStorePictureSupported);
        return getFinalUrlString();
    }
}
