package com.feedback.c;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.feedback.a.a;
import com.mobclick.android.UmengConstants;
import com.mobclick.android.l;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

public class c extends Thread {
    static final String a = c.class.getSimpleName();
    Context b;
    String c;
    String d;
    String e;
    int f;
    Handler g;

    public c(Context context) {
        this.c = "http://feedback.whalecloud.com/feedback/reply";
        this.f = 0;
        this.b = context;
        this.d = l.b(context);
        this.e = l.c(context);
    }

    public c(Context context, int i) {
        this.c = "http://feedback.whalecloud.com/feedback/reply";
        this.f = 0;
        this.b = context;
        this.f = i;
        this.d = l.b(context);
        this.e = l.c(context);
    }

    public c(Context context, Handler handler) {
        this(context);
        this.g = handler;
    }

    public void run() {
        String str;
        String str2 = "";
        Iterator it = this.b.getSharedPreferences(UmengConstants.FeedbackPreName, 0).getAll().keySet().iterator();
        while (true) {
            str = str2;
            if (!it.hasNext()) {
                break;
            }
            str2 = (String) it.next();
            if (str.length() != 0) {
                str2 = new StringBuilder(String.valueOf(str)).append(",").append(str2).toString();
            }
        }
        str2 = this.b.getSharedPreferences(UmengConstants.PreName_Trivial, 0).getString(UmengConstants.TrivialPreKey_MaxReplyID, "RP0");
        this.c += "?appkey=" + this.d + "&feedback_id=" + str;
        if (!str2.equals("RP0")) {
            this.c += "&startkey=" + str2;
        }
        Log.i("urlGetDevReply", this.c);
        HttpResponse httpResponse = null;
        try {
            httpResponse = new DefaultHttpClient().execute(new HttpGet(this.c));
        } catch (ClientProtocolException e) {
            Log.w(a, e.getMessage());
        } catch (IOException e2) {
            Log.w(a, e2.getMessage());
        }
        Intent intent = new Intent();
        intent.setAction(UmengConstants.RetrieveReplyBroadcastAction);
        if (httpResponse == null || httpResponse.getStatusLine().getStatusCode() != 200) {
            intent.putExtra(UmengConstants.RetrieveReplyBroadcastAction, -1);
        } else {
            HttpEntity entity = httpResponse.getEntity();
            StringBuffer stringBuffer = new StringBuffer();
            try {
                InputStream content = entity.getContent();
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = content.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    stringBuffer.append(new String(bArr, 0, read, "UTF-8"));
                }
                str2 = stringBuffer.toString();
                Log.i(a, "JSON RECEIVED :" + str2);
                try {
                    str2 = com.feedback.b.c.a(this.b, new JSONArray(str2));
                    Log.i(a, "newReplyIds :" + str2);
                    if (str2.length() == 0 || str2.split(",").length == 0) {
                        intent.putExtra(UmengConstants.RetrieveReplyBroadcastAction, 0);
                    } else {
                        intent.putExtra(UmengConstants.RetrieveReplyBroadcastAction, 1);
                        if (this.g != null) {
                            String[] split = str2.split(",");
                            a aVar = com.feedback.b.c.b(this.b, split[split.length - 1]).e;
                            Message message = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putString("newReplyContent", aVar.a());
                            message.setData(bundle);
                            this.g.sendMessage(message);
                        }
                    }
                } catch (JSONException e3) {
                    intent.putExtra(UmengConstants.RetrieveReplyBroadcastAction, -1);
                    Log.w(a, e3.getMessage());
                }
            } catch (IllegalStateException e4) {
                intent.putExtra(UmengConstants.RetrieveReplyBroadcastAction, -1);
                Log.w(a, e4.getMessage());
            } catch (IOException e5) {
                intent.putExtra(UmengConstants.RetrieveReplyBroadcastAction, -1);
                Log.w(a, e5.getMessage());
            }
        }
        this.b.sendBroadcast(intent);
    }
}
