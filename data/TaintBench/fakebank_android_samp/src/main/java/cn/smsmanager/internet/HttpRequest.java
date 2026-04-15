package cn.smsmanager.internet;

import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class HttpRequest {
    String TAG = "HttpRequest";

    public static boolean sendXML(String path, String xml) throws Exception {
        Log.d("HttpRequest sendxml", "strictmode");
        byte[] data = xml.getBytes();
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(5000);
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
        conn.setRequestProperty("Content-Length", String.valueOf(data.length));
        OutputStream outStream = conn.getOutputStream();
        outStream.write(data);
        outStream.flush();
        outStream.close();
        if (conn.getResponseCode() == 200) {
            return true;
        }
        return false;
    }

    public static byte[] sendGetRequest(String path, Map<String, String> params, String enc) throws Exception {
        StringBuilder sb = new StringBuilder(path);
        sb.append('?');
        for (Entry<String, String> entry : params.entrySet()) {
            sb.append((String) entry.getKey()).append('=').append(URLEncoder.encode((String) entry.getValue(), enc)).append('&');
        }
        sb.deleteCharAt(sb.length() - 1);
        URL url = new URL(sb.toString());
        Log.d("HttpRequest url", sb.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        System.out.println("HttpRequest_>conn.getResponseCode(): " + conn.getResponseCode());
        if (conn.getResponseCode() != 200) {
            return null;
        }
        Log.d("Httpequest getStream string", conn.getInputStream().toString());
        return readStream(conn.getInputStream());
    }

    public static boolean sendPostRequest(String path, Map<String, String> params, String enc) throws Exception {
        StringBuilder sb = new StringBuilder();
        if (!(params == null || params.isEmpty())) {
            for (Entry<String, String> entry : params.entrySet()) {
                sb.append((String) entry.getKey()).append('=').append(URLEncoder.encode((String) entry.getValue(), enc)).append('&');
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        byte[] entitydata = sb.toString().getBytes();
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        Log.d("HttpRequest", "strictMode");
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(5000);
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(entitydata.length));
        OutputStream outStream = conn.getOutputStream();
        outStream.write(entitydata);
        outStream.flush();
        outStream.close();
        if (conn.getResponseCode() == 200) {
            return true;
        }
        return false;
    }

    public static boolean sendRequestFromHttpClient(String path, Map<String, String> params, String enc) throws Exception {
        List<NameValuePair> paramPairs = new ArrayList();
        if (!(params == null || params.isEmpty())) {
            for (Entry<String, String> entry : params.entrySet()) {
                paramPairs.add(new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue()));
            }
        }
        UrlEncodedFormEntity entitydata = new UrlEncodedFormEntity(paramPairs, enc);
        HttpPost post = new HttpPost(path);
        post.setEntity(entitydata);
        if (new DefaultHttpClient().execute(post).getStatusLine().getStatusCode() == 200) {
            return true;
        }
        return false;
    }

    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (true) {
            int len = inStream.read(buffer);
            if (len == -1) {
                outSteam.close();
                inStream.close();
                return outSteam.toByteArray();
            }
            outSteam.write(buffer, 0, len);
        }
    }
}
