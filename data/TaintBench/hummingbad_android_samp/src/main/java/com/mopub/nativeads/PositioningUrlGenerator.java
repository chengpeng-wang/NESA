package com.mopub.nativeads;

import android.content.Context;
import android.support.annotation.NonNull;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.mopub.common.BaseUrlGenerator;
import com.mopub.common.ClientMetadata;
import com.mopub.common.Constants;

class PositioningUrlGenerator extends BaseUrlGenerator {
    private static final String POSITIONING_API_VERSION = "1";
    @NonNull
    private String mAdUnitId;
    @NonNull
    private final Context mContext;

    public PositioningUrlGenerator(@NonNull Context context) {
        this.mContext = context;
    }

    @NonNull
    public PositioningUrlGenerator withAdUnitId(@NonNull String adUnitId) {
        this.mAdUnitId = adUnitId;
        return this;
    }

    public String generateUrlString(@NonNull String serverHostname) {
        initUrlString(serverHostname, Constants.POSITIONING_HANDLER);
        setAdUnitId(this.mAdUnitId);
        setApiVersion("1");
        ClientMetadata instance = ClientMetadata.getInstance(this.mContext);
        setSdkVersion(instance.getSdkVersion());
        setDeviceInfo(instance.getDeviceManufacturer(), instance.getDeviceModel(), instance.getDeviceProduct());
        setAppVersion(instance.getAppVersion());
        appendAdvertisingInfoTemplates();
        return getFinalUrlString();
    }

    private void setAdUnitId(@NonNull String adUnitId) {
        addParam(MASTNativeAdConstants.ID_STRING, adUnitId);
    }

    private void setSdkVersion(@NonNull String sdkVersion) {
        addParam("nsv", sdkVersion);
    }
}
