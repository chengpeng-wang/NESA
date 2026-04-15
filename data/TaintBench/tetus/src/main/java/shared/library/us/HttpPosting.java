package shared.library.us;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public final class HttpPosting {
    public static String BASE_URL = "http://android.tetulus.com/";
    public static String appurl;
    public static String error;
    public static String query;

    public static String postData(String args) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(appurl + "?" + args).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.connect();
            if (connection.getResponseCode() == 200) {
                StringBuffer sb = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while (true) {
                    String line = reader.readLine();
                    if (line == null) {
                        return sb.toString();
                    }
                    sb.append(line);
                }
            }
        } catch (MalformedURLException e) {
            error = e.getMessage();
        } catch (IOException e2) {
            error = e2.getMessage();
        }
        return "";
    }

    public static String postData2(String args) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(args).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.connect();
            if (connection.getResponseCode() == 200) {
                StringBuffer sb = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while (true) {
                    String line = reader.readLine();
                    if (line == null) {
                        return sb.toString();
                    }
                    sb.append(line);
                }
            }
        } catch (MalformedURLException e) {
            error = e.getMessage();
        } catch (IOException e2) {
            error = e2.getMessage();
        }
        return "";
    }

    public String postData3(String args) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(args).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.connect();
            if (connection.getResponseCode() == 200) {
                StringBuffer sb = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while (true) {
                    String line = reader.readLine();
                    if (line == null) {
                        return sb.toString();
                    }
                    sb.append(line);
                }
            }
        } catch (MalformedURLException e) {
            error = e.getMessage();
        } catch (IOException e2) {
            error = e2.getMessage();
        }
        return "";
    }

    public static boolean sendRegistration(String accountName, String registrationId, String appid, String imei) {
        List values = new ArrayList();
        values.add(new BasicNameValuePair("accountName", accountName));
        values.add(new BasicNameValuePair("pid", appid));
        values.add(new BasicNameValuePair("imei", imei));
        values.add(new BasicNameValuePair("registrationid", registrationId));
        BASE_URL += "c2dm-registration.php";
        HttpResponse response = postData(values);
        if (response.getStatusLine().getStatusCode() == 200) {
            try {
                InputStream is = response.getEntity().getContent();
                StringBuffer b = new StringBuffer();
                while (true) {
                    int ch = is.read();
                    if (ch == -1) {
                        break;
                    }
                    b.append((char) ch);
                }
                if (b.toString().contains("ok")) {
                    return true;
                }
                return false;
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return false;
    }

    public static HttpResponse postData(List<NameValuePair> values) {
        HttpResponse response = null;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(BASE_URL);
            post.setEntity(new UrlEncodedFormEntity(values));
            return client.execute(post);
        } catch (Exception e) {
            error = e.getMessage();
            return response;
        }
    }
}
