package com.android.tools.system;

import android.content.Context;
import android.os.AsyncTask;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class MyPostRequest extends AsyncTask<ArrayList<String[]>, Void, String> {
    public Context context;

    /* access modifiers changed from: protected|bridge */
    public /* bridge */ Object doInBackground(Object[] objArr) {
        return doInBackground((ArrayList[]) objArr);
    }

    /* access modifiers changed from: protected|bridge */
    public /* bridge */ void onPostExecute(Object obj) {
        onPostExecute((String) obj);
    }

    public MyPostRequest(Context context) {
        this.context = context;
    }

    /* access modifiers changed from: protected|varargs */
    @Override
    public String doInBackground(ArrayList<String[]>... arrayListArr) {
        ArrayList<String[]>[] arrayListArr2 = arrayListArr;
        String str = "";
        Object obj = null;
        Object obj2 = null;
        DefaultHttpClient defaultHttpClient = null;
        try {
            ArrayList arrayList = r16;
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = arrayList;
            for (int i = 1; i < arrayListArr2[0].size(); i++) {
                String[] strArr = (String[]) arrayListArr2[0].get(i);
                arrayList = arrayList3;
                BasicNameValuePair basicNameValuePair = r16;
                BasicNameValuePair basicNameValuePair2 = new BasicNameValuePair(strArr[0], Translit.toTranslit(strArr[1]));
                boolean add = arrayList.add(basicNameValuePair);
            }
            if (defaultHttpClient == null) {
                DefaultHttpClient defaultHttpClient2 = r16;
                DefaultHttpClient defaultHttpClient3 = new DefaultHttpClient();
                defaultHttpClient = defaultHttpClient2;
            }
            HttpPost httpPost = r16;
            HttpPost httpPost2 = new HttpPost(((String[]) arrayListArr2[0].get(0))[1]);
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
        return str;
    }

    /* access modifiers changed from: protected */
    @Override
    public void onPostExecute(String str) {
        super.onPostExecute(str);
    }
}
