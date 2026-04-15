package com.cleanmaster.ui.app.market.transport;

import android.os.Build.VERSION;
import com.moceanmobile.mast.Defaults;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.mopub.common.Constants;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

/* compiled from: BaseRequestBuilder */
public class a {
    protected String mHost = b.c();
    protected int mPort = b.a;
    protected String mQuery = "";
    protected List qparams = new ArrayList();

    public a() {
        v(18);
    }

    public void setHost(String host) {
        this.mHost = host;
    }

    public void setPort(int port) {
        this.mPort = port;
    }

    public void setQuery(String query) {
        this.mQuery = query;
    }

    public a v(int v) {
        this.qparams.add(new BasicNameValuePair("v", String.valueOf(v)));
        return this;
    }

    public a android_id() {
        this.qparams.add(new BasicNameValuePair("android_id", com.cleanmaster.common.a.a()));
        return this;
    }

    public a model() {
        this.qparams.add(new BasicNameValuePair("model", com.cleanmaster.functionevent.report.a.a("ro.product.model", "unknow")));
        return this;
    }

    public a mcc(String mcc) {
        this.qparams.add(new BasicNameValuePair(MASTNativeAdConstants.TELEPHONY_MCC, mcc));
        return this;
    }

    public a brand() {
        this.qparams.add(new BasicNameValuePair("brand", com.cleanmaster.functionevent.report.a.a("ro.product.brand", "unknow")));
        return this;
    }

    public a ch() {
        this.qparams.add(new BasicNameValuePair("ch", String.valueOf(com.cleanmaster.common.a.b())));
        return this;
    }

    public a os_version() {
        this.qparams.add(new BasicNameValuePair("os_version", VERSION.SDK_INT));
        return this;
    }

    public a cver(int cver) {
        this.qparams.add(new BasicNameValuePair("cver", String.valueOf(cver)));
        return this;
    }

    public a resolution(String resolution) {
        this.qparams.add(new BasicNameValuePair("resolution", String.valueOf(resolution)));
        return this;
    }

    public a net(int net) {
        this.qparams.add(new BasicNameValuePair("net", String.valueOf(net)));
        return this;
    }

    public a lan(String lan) {
        this.qparams.add(new BasicNameValuePair("lan", String.valueOf(lan)));
        return this;
    }

    public a k(int i) {
        this.qparams.add(new BasicNameValuePair("k", String.valueOf(i)));
        return this;
    }

    public a country(String country) {
        this.qparams.add(new BasicNameValuePair("country", String.valueOf(country)));
        return this;
    }

    public URI toURI() {
        try {
            return URIUtils.createURI(Constants.HTTP, this.mHost, this.mPort, this.mQuery, URLEncodedUtils.format(this.qparams, Defaults.ENCODING_UTF_8), null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}
