package com.cleanmaster.ui.app.market;

import com.cleanmaster.ui.app.market.transport.b;
import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.BasicHttpContext;

/* compiled from: ParseUrlUtils */
public class h {
    private j a;
    private m b = null;

    public void a(m mVar) {
        this.b = mVar;
    }

    public j a() {
        return this.a;
    }

    public HttpResponse a(String str) {
        BasicHttpParams basicHttpParams = new BasicHttpParams();
        basicHttpParams.setParameter("http.useragent", b.b());
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, 10000);
        HttpConnectionParams.setSoTimeout(basicHttpParams, 10000);
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient(basicHttpParams);
        defaultHttpClient.setRedirectHandler(new i(this));
        HttpResponse httpResponse = null;
        try {
            return defaultHttpClient.execute(new HttpGet(str), new BasicHttpContext());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return httpResponse;
        } catch (SocketTimeoutException e2) {
            e2.printStackTrace();
            return httpResponse;
        } catch (ConnectException e3) {
            e3.printStackTrace();
            return httpResponse;
        } catch (SocketException e4) {
            e4.printStackTrace();
            return httpResponse;
        } catch (MalformedURLException e5) {
            e5.printStackTrace();
            return httpResponse;
        } catch (ConnectionPoolTimeoutException e6) {
            e6.printStackTrace();
            return httpResponse;
        } catch (ConnectTimeoutException e7) {
            e7.printStackTrace();
            return httpResponse;
        } catch (NoHttpResponseException e8) {
            e8.printStackTrace();
            return httpResponse;
        } catch (IOException e9) {
            e9.printStackTrace();
            return httpResponse;
        } catch (NullPointerException e10) {
            e10.printStackTrace();
            return httpResponse;
        } catch (Exception e11) {
            e11.printStackTrace();
            return httpResponse;
        }
    }

    /* access modifiers changed from: private */
    public void c(String str) {
        if (this.b != null) {
            this.b.a(str);
        }
    }

    public j b(String str) {
        j jVar = new j(this);
        jVar.c((Object[]) new String[]{str});
        this.a = jVar;
        return jVar;
    }

    public j a(String str, String str2, String str3, String str4) {
        j jVar = new j(this);
        jVar.c((Object[]) new String[]{str, str2, str3, str4});
        this.a = jVar;
        return jVar;
    }
}
