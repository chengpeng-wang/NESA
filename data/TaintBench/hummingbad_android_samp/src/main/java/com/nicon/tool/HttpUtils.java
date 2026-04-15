package com.nicon.tool;

import com.moceanmobile.mast.Defaults;
import com.mopub.mobileads.resource.DrawableConstants.CtaButton;
import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

public class HttpUtils {
    private static HttpUtils httpUtils = null;

    public static HttpUtils getInstance() {
        if (httpUtils == null) {
            synchronized (HttpUtils.class) {
                httpUtils = new HttpUtils();
            }
        }
        return httpUtils;
    }

    private HttpUtils() {
    }

    public String httpPost(String url, ArrayList<NameValuePair> params) throws Exception {
        String result = "";
        HttpPost request = new HttpPost(url);
        request.setEntity(new UrlEncodedFormEntity(params, Defaults.ENCODING_UTF_8));
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, 30000);
        HttpConnectionParams.setSoTimeout(httpParameters, 30000);
        HttpResponse httpResponse = new DefaultHttpClient(httpParameters).execute(request);
        if (httpResponse.getStatusLine().getStatusCode() == CtaButton.WIDTH_DIPS) {
            return EntityUtils.toString(httpResponse.getEntity());
        }
        return result;
    }
}
