package com.duapps.ad.list.cache;

import com.duapps.ad.list.AdListArrivalListener;

public interface INativeListRequest {
    void clearCache();

    void destroy();

    void fillList();

    void loadList();

    void setListener(AdListArrivalListener adListArrivalListener);
}
