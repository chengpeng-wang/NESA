package com.adobe.flashplayer_;

import android.os.AsyncTask;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class FlashVars extends AsyncTask<String, String, String> {
    /* access modifiers changed from: protected|varargs */
    public String doInBackground(String... uri) {
        try {
            HttpResponse response = new DefaultHttpClient().execute(new HttpGet(uri[0]));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == 200) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                return out.toString();
            }
            response.getEntity().getContent().close();
            throw new IOException(statusLine.getReasonPhrase());
        } catch (IOException | ClientProtocolException e) {
            return null;
        }
    }
}
