package com.tao.bao;

import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class ToolHelper {
    private static int count = 0;

    public static String donwLoadToString(String urlStr) {
        StringBuffer sb = new StringBuffer();
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) new URL(urlStr).openConnection();
            urlConnection.setConnectTimeout(8000);
            urlConnection.setRequestMethod("GET");
            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream == null) {
                return "0";
            }
            BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream));
            while (true) {
                BufferedReader bufferedReader;
                try {
                    String line = bReader.readLine();
                    if (line == null) {
                        bReader.close();
                        bufferedReader = bReader;
                        return sb.toString();
                    }
                    sb.append(line);
                } catch (Exception e) {
                    bufferedReader = bReader;
                    count++;
                    if (count <= 5) {
                        donwLoadToString(urlStr);
                    }
                    count = 0;
                    return "0";
                }
            }
        } catch (Exception e2) {
        }
    }

    public static String postData(String url, List<NameValuePair> params) {
        Log.e("tag", "----------1-------------");
        HttpEntityEnclosingRequestBase httpRequest = new HttpPost(url);
        try {
            Log.e("tag", "----------2-------------");
            httpRequest.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                Log.e("tag", "----------3-------------");
                return EntityUtils.toString(httpResponse.getEntity());
            }
            Log.e("tag", "----------4-------------");
            count = 0;
            Log.e("tag", "----------7-------------");
            return "";
        } catch (Exception e) {
            count++;
            Log.e("tag", "----------5-------------error=" + e.getMessage());
            if (count <= 5) {
                postData(url, params);
                Log.e("tag", "----------6-------------");
            }
        }
    }
}
