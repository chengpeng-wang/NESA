package com.cleanmaster.util;

import android.text.TextUtils;
import android.util.Log;
import com.cleanmaster.model.a;
import com.cleanmaster.model.b;
import com.cleanmaster.ui.app.market.loader.AsyncTaskEx;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.picksinit.c;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

/* compiled from: SearchDataReporter */
public class r extends AsyncTaskEx {
    private b a;
    private List b;

    public void a(a aVar, b bVar) {
        this.a = bVar;
        this.b = new ArrayList();
        this.b.add(aVar);
    }

    private String a() {
        if (c.getInstance().isDebug()) {
            return "http://10.33.21.138/render";
        }
        if (c.getInstance().getmAdResourceRp() == 1) {
            if (com.cleanmaster.ui.app.market.c.a()) {
                return "http://rcv.mobad.ijinshan.com/render/";
            }
            return "https://ssdk.adkmob.com/render/";
        } else if (com.cleanmaster.ui.app.market.c.a()) {
            return "http://rcv.mobad.ijinshan.com/render/";
        } else {
            return "https://ssdk.adkmob.com/render/";
        }
    }

    /* access modifiers changed from: protected|varargs */
    public Void a(Void... voidArr) {
        if (this.a != null) {
            a(this.a.b());
        }
        return null;
    }

    private void a(String str) {
        if (!TextUtils.isEmpty(str)) {
            a(a(), str, new s(this));
        }
    }

    private static int b() {
        int b = com.cleanmaster.ui.app.market.b.b();
        return b > 0 ? b : 10000;
    }

    private void a(String str, String str2, t tVar) {
        if (!TextUtils.isEmpty(str2)) {
            HttpUriRequest httpGet;
            if (h.a) {
                Log.d("bdr", str2);
            }
            BasicHttpParams basicHttpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(basicHttpParams, b());
            HttpConnectionParams.setSoTimeout(basicHttpParams, b());
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient(basicHttpParams);
            defaultHttpClient.getParams().setParameter("http.useragent", com.cleanmaster.ui.app.market.transport.b.b());
            try {
                httpGet = new HttpGet(str + MASTNativeAdConstants.QUESTIONMARK + str2);
            } catch (Exception e) {
                e.printStackTrace();
                httpGet = null;
            }
            if (httpGet != null) {
                HttpResponse execute;
                try {
                    execute = defaultHttpClient.execute(httpGet);
                } catch (ClientProtocolException e2) {
                    e2.printStackTrace();
                    execute = null;
                } catch (IOException e3) {
                    e3.printStackTrace();
                    execute = null;
                } catch (Exception e4) {
                    e4.printStackTrace();
                    execute = null;
                }
                if (execute != null && tVar != null) {
                    InputStream content;
                    try {
                        content = execute.getEntity().getContent();
                    } catch (IllegalStateException e5) {
                        e5.printStackTrace();
                        content = null;
                    } catch (IOException e32) {
                        e32.printStackTrace();
                        content = null;
                    } catch (Exception e42) {
                        e42.printStackTrace();
                        content = null;
                    }
                    if (tVar != null) {
                        tVar.a(content);
                    }
                    if (content != null) {
                        try {
                            content.close();
                        } catch (Exception e422) {
                            e422.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
