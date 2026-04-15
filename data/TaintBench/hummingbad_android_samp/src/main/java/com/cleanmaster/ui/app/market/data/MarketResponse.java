package com.cleanmaster.ui.app.market.data;

import android.text.TextUtils;
import com.cleanmaster.ui.app.market.Ad;
import com.cleanmaster.ui.app.market.AdEx;
import com.cleanmaster.ui.app.market.a;
import com.cleanmaster.ui.app.market.c;
import com.cleanmaster.ui.app.market.transport.CmMarketHttpClient.MarketRequestBuilder;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MarketResponse {
    private List ads = new ArrayList();
    private a mHeader = new a();

    public int code() {
        return this.mHeader.a;
    }

    public int adn() {
        return this.mHeader.b;
    }

    public int offset() {
        return this.mHeader.c;
    }

    public int getShowRating() {
        return this.mHeader.d;
    }

    public int getShowType() {
        return this.mHeader.e;
    }

    public List ads() {
        return this.ads;
    }

    public void addAd(Ad ad) {
        if (ad != null) {
            this.ads.add(ad);
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("(MarketResponse %s", new Object[]{this.mHeader}));
        stringBuilder.append(":ads\n");
        if (this.ads != null) {
            for (a obj : this.ads) {
                stringBuilder.append(obj.toString());
            }
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public static MarketResponse fromJson(String requestPosId, String json) {
        Exception e;
        MarketResponse marketResponse = null;
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            MarketResponse marketResponse2 = new MarketResponse();
            try {
                JSONObject jSONObject = new JSONObject(json);
                marketResponse2.setHeader((a) new a().a(jSONObject));
                JSONArray optJSONArray = jSONObject.optJSONArray(MASTNativeAdConstants.RESPONSE_ADS);
                if (optJSONArray != null) {
                    for (int i = 0; i < optJSONArray.length(); i++) {
                        JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                        if (optJSONObject != null) {
                            marketResponse2.addAd(newAd(requestPosId).fromJSONObject(optJSONObject));
                        }
                    }
                }
                if (MarketRequestBuilder.isAppRequest(requestPosId)) {
                    c.a(marketResponse2.ads());
                }
                return marketResponse2;
            } catch (Exception e2) {
                Exception exception = e2;
                marketResponse = marketResponse2;
                e = exception;
                e.printStackTrace();
                return marketResponse;
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            return marketResponse;
        }
    }

    public void setHeader(a header) {
        if (header == null) {
            header = new a();
        }
        this.mHeader = header;
    }

    public static MarketResponse fromJson2(String json) {
        JSONException e;
        MarketResponse marketResponse = null;
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            MarketResponse marketResponse2 = new MarketResponse();
            try {
                JSONObject jSONObject = new JSONObject(json);
                marketResponse2.setHeader((a) new a().a(jSONObject));
                JSONArray jSONArray = jSONObject.getJSONArray(MASTNativeAdConstants.RESPONSE_ADS);
                if (jSONArray != null) {
                    for (int i = 0; i < jSONArray.length(); i++) {
                        marketResponse2.addAd(new Ad().fromJSONObject(jSONArray.getJSONObject(i)));
                    }
                }
                return marketResponse2;
            } catch (JSONException e2) {
                JSONException jSONException = e2;
                marketResponse = marketResponse2;
                e = jSONException;
                e.printStackTrace();
                return marketResponse;
            }
        } catch (JSONException e3) {
            e = e3;
            e.printStackTrace();
            return marketResponse;
        }
    }

    public void addAds(List ads) {
        if (ads != null) {
            this.ads.addAll(ads);
        }
    }

    public List extAds() {
        return this.ads;
    }

    public a getHeader() {
        return this.mHeader;
    }

    public boolean success() {
        return this.mHeader.a == 0;
    }

    private static Ad newAd(String requestPosId) {
        if (MarketRequestBuilder.REQUEST_GAME_TIP.equals(requestPosId)) {
            return new AdEx();
        }
        return new Ad();
    }

    public void filter(com.cleanmaster.ui.app.market.data.filter.a env) {
        Ad ad;
        ArrayList<Ad> arrayList = new ArrayList();
        for (Ad ad2 : this.ads) {
            if (!ad2.match(env)) {
                arrayList.add(ad2);
            }
        }
        for (Ad ad22 : arrayList) {
            this.ads.remove(ad22);
        }
        Iterator it = this.ads.iterator();
        while (it.hasNext()) {
            ad22 = (Ad) it.next();
            if (!ad22.isDeepLink() && com.cleanmaster.common.a.b(com.picksinit.c.getInstance().getContext(), ad22.getPkg())) {
                it.remove();
            }
        }
    }
}
