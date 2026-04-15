package com.duapps.ad.list;

import com.duapps.ad.AdError;
import com.duapps.ad.entity.strategy.NativeAd;
import java.util.List;

public interface AdListArrivalListener {
    void onAdError(AdError adError);

    void onAdLoaded(List<NativeAd> list);
}
