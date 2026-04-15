package com.cleanmaster.util;

import android.text.TextUtils;
import android.util.Log;
import com.cleanmaster.model.a;
import com.cleanmaster.model.b;
import com.cleanmaster.ui.app.market.loader.AsyncTaskEx;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

/* compiled from: BuinessDataReporter */
public class c extends AsyncTaskEx {
    private b a;
    private List b;

    public void a(a aVar, b bVar) {
        this.a = bVar;
        this.b = new ArrayList();
        this.b.add(aVar);
    }

    private String a() {
        String testReportIp;
        if (com.picksinit.c.getInstance().isDebug()) {
            testReportIp = com.picksinit.c.getInstance().getTestReportIp();
            if (!TextUtils.isEmpty(testReportIp)) {
                return "http://" + testReportIp + "/rp/";
            }
        }
        if (com.picksinit.c.getInstance().getmAdResourceRp() == 1) {
            if (com.cleanmaster.ui.app.market.c.a()) {
                return "http://rcv.mobad.ijinshan.com/rp/";
            }
            testReportIp = com.cleanmaster.ui.app.market.b.e();
            if (TextUtils.isEmpty(testReportIp)) {
                return "https://ssdk.adkmob.com/rp/";
            }
            return testReportIp;
        } else if (com.cleanmaster.ui.app.market.c.a()) {
            return "http://unrcv.mobad.ijinshan.com/rp/";
        } else {
            return "http://unrcv.adkmob.com/rp/";
        }
    }

    /* access modifiers changed from: protected|varargs */
    public Void a(Void... voidArr) {
        if (this.a != null) {
            a(this.a.a(), g());
        }
        return null;
    }

    private void a(String str, String str2) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            a(a(), str + str2, new d(this));
        }
    }

    private static int b() {
        int b = com.cleanmaster.ui.app.market.b.b();
        return b > 0 ? b : 10000;
    }

    private void a(String str, String str2, e eVar) {
        com.picksinit.c.getInstance().getContext();
        if (!TextUtils.isEmpty(str2)) {
            HttpUriRequest httpPost;
            if (h.a) {
                Log.d("bdr", str2);
            }
            BasicHttpParams basicHttpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(basicHttpParams, b());
            HttpConnectionParams.setSoTimeout(basicHttpParams, b());
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient(basicHttpParams);
            defaultHttpClient.getParams().setParameter("http.useragent", com.cleanmaster.ui.app.market.transport.b.b());
            try {
                httpPost = new HttpPost(str);
            } catch (Exception e) {
                e.printStackTrace();
                httpPost = null;
            }
            if (httpPost != null) {
                HttpResponse execute;
                try {
                    httpPost.setEntity(new StringEntity(str2));
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                try {
                    execute = defaultHttpClient.execute(httpPost);
                } catch (Exception e3) {
                    execute = null;
                }
                if (execute != null && eVar != null) {
                    InputStream content;
                    try {
                        content = execute.getEntity().getContent();
                    } catch (Exception e4) {
                        content = null;
                    }
                    if (eVar != null) {
                        eVar.a(content);
                    }
                    if (content != null) {
                        try {
                            content.close();
                        } catch (Exception e22) {
                            e22.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private String g() {
        if (this.b == null || this.b.size() == 0) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("&attach=[");
        Object obj = 1;
        for (a aVar : this.b) {
            if (obj != null) {
                obj = null;
            } else {
                stringBuilder.append(",");
            }
            stringBuilder.append(aVar.a());
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
