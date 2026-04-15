package com.cmcm.adsdk.requestconfig.request;

import android.os.AsyncTask;
import com.cmcm.adsdk.requestconfig.log.a;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.mopub.mobileads.resource.DrawableConstants.CtaButton;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public final class RequestTask extends AsyncTask<Void, Void, byte[]> {
    private String a;
    private String b;
    private ResultListener c;

    public interface ResultListener {
        void result(byte[] bArr);
    }

    /* access modifiers changed from: protected|final|synthetic */
    public final /* synthetic */ void onPostExecute(Object x0) {
        byte[] bArr = (byte[]) x0;
        super.onPostExecute(bArr);
        if (this.c != null) {
            this.c.result(bArr);
        }
    }

    public RequestTask(String url, String param, ResultListener listener) {
        this.a = url;
        this.b = param;
        this.c = listener;
    }

    /* access modifiers changed from: protected|final */
    public final void onPreExecute() {
        super.onPreExecute();
    }

    private static byte[] a(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[1024];
        while (true) {
            int read = inputStream.read(bArr);
            if (read != -1) {
                byteArrayOutputStream.write(bArr, 0, read);
            } else {
                inputStream.close();
                return byteArrayOutputStream.toByteArray();
            }
        }
    }

    private byte[] a(String str, String str2) {
        Object obj;
        if (str2 == null || "".equals(str2)) {
            obj = null;
        } else {
            obj = 1;
        }
        if (obj != null) {
            str = str + MASTNativeAdConstants.QUESTIONMARK + str2;
        }
        a.a("RequestTask", "Get Url:" + str);
        try {
            HttpGet httpGet = new HttpGet(str);
            HttpParams params = httpGet.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 5000);
            HttpConnectionParams.setSoTimeout(params, 5000);
            HttpResponse execute = new DefaultHttpClient().execute(httpGet);
            if (execute.getStatusLine().getStatusCode() == CtaButton.WIDTH_DIPS) {
                return a(execute.getEntity().getContent());
            }
        } catch (Exception e) {
            a.d("RequestTask", "get response error..." + e.getMessage());
        }
        return null;
    }

    /* access modifiers changed from: protected|final|synthetic */
    public final /* synthetic */ Object doInBackground(Object[] x0) {
        byte[] a = a(this.a, this.b);
        a.a("RequestTask", "result:" + a);
        return a;
    }
}
