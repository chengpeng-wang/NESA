package u.aly;

import com.moceanmobile.mast.Defaults;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.mopub.mobileads.resource.DrawableConstants.CtaButton;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: UClient */
public class ao {
    private static final String a = ao.class.getName();
    private Map<String, String> b;

    public <T extends aq> T a(ap apVar, Class<T> cls) {
        JSONObject a;
        String trim = apVar.c().trim();
        b(trim);
        if (ap.c.equals(trim)) {
            a = a(apVar.b());
        } else if (ap.b.equals(trim)) {
            a = a(apVar.d, apVar.a());
        } else {
            a = null;
        }
        if (a == null) {
            return null;
        }
        try {
            return (aq) cls.getConstructor(new Class[]{JSONObject.class}).newInstance(new Object[]{a});
        } catch (SecurityException e) {
            aj.b(a, "SecurityException", e);
        } catch (NoSuchMethodException e2) {
            aj.b(a, "NoSuchMethodException", e2);
        } catch (IllegalArgumentException e3) {
            aj.b(a, "IllegalArgumentException", e3);
        } catch (InstantiationException e4) {
            aj.b(a, "InstantiationException", e4);
        } catch (IllegalAccessException e5) {
            aj.b(a, "IllegalAccessException", e5);
        } catch (InvocationTargetException e6) {
            aj.b(a, "InvocationTargetException", e6);
        }
        return null;
    }

    private JSONObject a(String str, JSONObject jSONObject) {
        String jSONObject2 = jSONObject.toString();
        int nextInt = new Random().nextInt(1000);
        aj.c(a, new StringBuilder(String.valueOf(nextInt)).append(":\trequest: ").append(str).append(av.a).append(jSONObject2).toString());
        HttpPost httpPost = new HttpPost(str);
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient(b());
        try {
            if (a()) {
                byte[] a = au.a("content=" + jSONObject2, Charset.defaultCharset().toString());
                httpPost.addHeader("Content-Encoding", "deflate");
                httpPost.setEntity(new InputStreamEntity(new ByteArrayInputStream(a), (long) a.length));
            } else {
                ArrayList arrayList = new ArrayList(1);
                arrayList.add(new BasicNameValuePair("content", jSONObject2));
                httpPost.setEntity(new UrlEncodedFormEntity(arrayList, Defaults.ENCODING_UTF_8));
            }
            HttpResponse execute = defaultHttpClient.execute(httpPost);
            if (execute.getStatusLine().getStatusCode() == CtaButton.WIDTH_DIPS) {
                HttpEntity entity = execute.getEntity();
                if (entity == null) {
                    return null;
                }
                InputStream inputStream;
                InputStream content = entity.getContent();
                Header firstHeader = execute.getFirstHeader("Content-Encoding");
                if (firstHeader == null || !firstHeader.getValue().equalsIgnoreCase("deflate")) {
                    inputStream = content;
                } else {
                    inputStream = new InflaterInputStream(content);
                }
                String a2 = a(inputStream);
                aj.a(a, new StringBuilder(String.valueOf(nextInt)).append(":\tresponse: ").append(av.a).append(a2).toString());
                if (a2 == null) {
                    return null;
                }
                return new JSONObject(a2);
            }
            aj.c(a, new StringBuilder(String.valueOf(nextInt)).append(":\tFailed to send message. StatusCode = ").append(execute.getStatusLine().getStatusCode()).append(av.a).append(str).toString());
            return null;
        } catch (ClientProtocolException e) {
            aj.c(a, new StringBuilder(String.valueOf(nextInt)).append(":\tClientProtocolException,Failed to send message.").append(str).toString(), e);
            return null;
        } catch (IOException e2) {
            aj.c(a, new StringBuilder(String.valueOf(nextInt)).append(":\tIOException,Failed to send message.").append(str).toString(), e2);
            return null;
        } catch (JSONException e3) {
            aj.c(a, new StringBuilder(String.valueOf(nextInt)).append(":\tIOException,Failed to send message.").append(str).toString(), e3);
            return null;
        }
    }

    public boolean a() {
        return false;
    }

    private static String a(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream), 8192);
        StringBuilder stringBuilder = new StringBuilder();
        while (true) {
            try {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                stringBuilder.append(new StringBuilder(String.valueOf(readLine)).append(MASTNativeAdConstants.NEWLINE).toString());
            } catch (IOException e) {
                stringBuilder = a;
                aj.b(stringBuilder, "Caught IOException in convertStreamToString()", e);
                return null;
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e2) {
                    aj.b(a, "Caught IOException in convertStreamToString()", e2);
                    return null;
                }
            }
        }
        return stringBuilder.toString();
    }

    private JSONObject a(String str) {
        int nextInt = new Random().nextInt(1000);
        try {
            String property = System.getProperty("line.separator");
            if (str.length() <= 1) {
                aj.b(a, new StringBuilder(String.valueOf(nextInt)).append(":\tInvalid baseUrl.").toString());
                return null;
            }
            aj.a(a, new StringBuilder(String.valueOf(nextInt)).append(":\tget: ").append(str).toString());
            HttpGet httpGet = new HttpGet(str);
            if (this.b != null && this.b.size() > 0) {
                for (String str2 : this.b.keySet()) {
                    httpGet.addHeader(str2, (String) this.b.get(str2));
                }
            }
            HttpResponse execute = new DefaultHttpClient(b()).execute(httpGet);
            if (execute.getStatusLine().getStatusCode() == CtaButton.WIDTH_DIPS) {
                HttpEntity entity = execute.getEntity();
                if (entity != null) {
                    InputStream inflaterInputStream;
                    InputStream content = entity.getContent();
                    Header firstHeader = execute.getFirstHeader("Content-Encoding");
                    if (firstHeader == null || !firstHeader.getValue().equalsIgnoreCase("gzip")) {
                        if (firstHeader != null) {
                            if (firstHeader.getValue().equalsIgnoreCase("deflate")) {
                                aj.a(a, new StringBuilder(String.valueOf(nextInt)).append("  Use InflaterInputStream get data....").toString());
                                inflaterInputStream = new InflaterInputStream(content);
                            }
                        }
                        inflaterInputStream = content;
                    } else {
                        aj.a(a, new StringBuilder(String.valueOf(nextInt)).append("  Use GZIPInputStream get data....").toString());
                        inflaterInputStream = new GZIPInputStream(content);
                    }
                    String a = a(inflaterInputStream);
                    aj.a(a, new StringBuilder(String.valueOf(nextInt)).append(":\tresponse: ").append(property).append(a).toString());
                    if (a == null) {
                        return null;
                    }
                    return new JSONObject(a);
                }
            }
            aj.c(a, new StringBuilder(String.valueOf(nextInt)).append(":\tFailed to send message. StatusCode = ").append(execute.getStatusLine().getStatusCode()).append(av.a).append(str).toString());
            return null;
        } catch (ClientProtocolException e) {
            aj.c(a, new StringBuilder(String.valueOf(nextInt)).append(":\tClientProtocolException,Failed to send message.").append(str).toString(), e);
            return null;
        } catch (Exception e2) {
            aj.c(a, new StringBuilder(String.valueOf(nextInt)).append(":\tIOException,Failed to send message.").append(str).toString(), e2);
            return null;
        }
    }

    private HttpParams b() {
        BasicHttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, 10000);
        HttpConnectionParams.setSoTimeout(basicHttpParams, BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT);
        HttpProtocolParams.setUserAgent(basicHttpParams, System.getProperty("http.agent"));
        return basicHttpParams;
    }

    public ao a(Map<String, String> map) {
        this.b = map;
        return this;
    }

    private void b(String str) {
        if (av.d(str) || (ap.c.equals(str.trim()) ^ ap.b.equals(str.trim())) == 0) {
            throw new RuntimeException("验证请求方式失败[" + str + "]");
        }
    }
}
