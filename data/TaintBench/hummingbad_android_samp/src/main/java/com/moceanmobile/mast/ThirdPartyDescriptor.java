package com.moceanmobile.mast;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;

public class ThirdPartyDescriptor {
    private Map<String, String> params = new HashMap();
    private Map<String, String> properties = new HashMap();

    public static ThirdPartyDescriptor getDescriptor(NativeAdDescriptor nativeAdDescriptor) throws JSONException {
        if (nativeAdDescriptor == null) {
            return null;
        }
        ThirdPartyDescriptor thirdPartyDescriptor = new ThirdPartyDescriptor();
        HashMap hashMap = new HashMap();
        HashMap hashMap2 = new HashMap();
        hashMap.put(MASTNativeAdConstants.RESPONSE_MEDIATION_ADID, nativeAdDescriptor.getAdUnitId());
        thirdPartyDescriptor.params = hashMap;
        thirdPartyDescriptor.properties = hashMap2;
        return thirdPartyDescriptor;
    }

    private ThirdPartyDescriptor() {
    }

    public Map<String, String> getProperties() {
        return this.properties;
    }

    public Map<String, String> getParams() {
        return this.params;
    }
}
