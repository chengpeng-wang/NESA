package com.baidu.mapapi.utils;

import android.os.Bundle;
import android.util.Log;
import com.baidu.platform.comapi.d.c;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jivesoftware.smack.packet.PrivacyItem.PrivacyRule;
import org.json.JSONException;
import org.json.JSONObject;

public class PermissionCheck {
    private static Map<String, String> a;

    public static void InitParam(String str, String str2, String str3) {
        Object str4;
        if (a == null) {
            a = new HashMap();
        }
        Bundle c = c.c();
        if (str4 == null || str4.length() == 0) {
            str4 = "-1";
        }
        a.put("ak", str4);
        a.put(PrivacyRule.SUBSCRIPTION_FROM, "lbs_androidsdk");
        a.put("mcode", str3);
        a.put("mb", c.getString("mb"));
        a.put("os", c.getString("os"));
        a.put("sv", c.getString("sv"));
        a.put("imt", "1");
        a.put("im", c.getString("im"));
        a.put("imrand", c.getString("imrand"));
        a.put("net", c.getString("net"));
        a.put("cpu", c.getString("cpu"));
        a.put("glr", c.getString("glr"));
        a.put("glv", c.getString("glv"));
        a.put("resid", c.getString("resid"));
        a.put("appid", "-1");
        a.put("ver", "1");
        a.put("screen", String.format("(%d,%d)", new Object[]{Integer.valueOf(c.getInt("screen_x")), Integer.valueOf(c.getInt("screen_y"))}));
        a.put("dpi", String.format("(%d,%d)", new Object[]{Integer.valueOf(c.getInt("dpi_x")), Integer.valueOf(c.getInt("dpi_y"))}));
        a.put("pcn", c.getString("mb"));
        a.put("name", str2);
    }

    public static boolean check() {
        return com.baidu.platform.comjni.permissioncheck.PermissionCheck.check();
    }

    public static int permissionCheck() {
        if (a == null) {
            return 1;
        }
        int praseResult;
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        HttpClientParams.setCookiePolicy(defaultHttpClient.getParams(), "compatibility");
        String str = "";
        try {
            String entityUtils;
            KeyStore instance = KeyStore.getInstance(KeyStore.getDefaultType());
            instance.load(null, null);
            f fVar = new f(instance);
            fVar.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            defaultHttpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", fVar, 443));
            HttpPost httpPost = new HttpPost("https://sapi.map.baidu.com/sdkcs/verify");
            ArrayList arrayList = new ArrayList();
            for (Entry entry : a.entrySet()) {
                arrayList.add(new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(arrayList, StringEncodings.UTF8));
            HttpEntity entity = defaultHttpClient.execute(httpPost).getEntity();
            if (entity != null) {
                entity.getContentLength();
                entityUtils = EntityUtils.toString(entity, StringEncodings.UTF8);
            } else {
                entityUtils = str;
            }
            praseResult = praseResult(entityUtils);
        } catch (UnsupportedEncodingException e) {
            return 3;
        } catch (ClientProtocolException e2) {
            return 3;
        } catch (ParseException e3) {
            return 3;
        } catch (IOException e4) {
            return 4;
        } catch (NoSuchAlgorithmException e5) {
            return 3;
        } catch (CertificateException e6) {
            return 3;
        } catch (KeyStoreException e7) {
            return 3;
        } catch (KeyManagementException e8) {
            return 3;
        } catch (UnrecoverableKeyException e9) {
            return 3;
        } finally {
            defaultHttpClient.getConnectionManager().shutdown();
            return 3;
        }
        return praseResult;
    }

    public static int praseResult(String str) {
        int i = -1;
        if (str == null || str.length() == 0) {
            return 3;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            int optInt = jSONObject.optInt("status");
            if (optInt == 1 || optInt == 2 || optInt == 4) {
                optInt = 100;
            }
            if (optInt == 0) {
                int optInt2 = jSONObject.has("uid") ? jSONObject.optInt("uid") : -1;
                if (jSONObject.has("appid")) {
                    i = jSONObject.optInt("appid");
                }
                c.b("" + optInt2, "" + i);
                return optInt;
            }
            Log.e("baidumapsdk", "Authentication Error,status: " + optInt + " message: " + jSONObject.optString("message"));
            return optInt;
        } catch (JSONException e) {
            return 3;
        } catch (NumberFormatException e2) {
            return 3;
        }
    }

    public boolean check(String str) {
        return true;
    }
}
