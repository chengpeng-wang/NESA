package exts.whats.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class Sender {
    public static JSONObject request(DefaultHttpClient httpClient, String serverURL, String data) throws Exception {
        HttpPost request = new HttpPost(serverURL);
        StringEntity se = new StringEntity(data, "UTF-8");
        se.setContentType("application/json");
        request.setEntity(se);
        HttpResponse response = httpClient.execute(request);
        if (response.getStatusLine().getStatusCode() == 200) {
            return new JSONObject(EntityUtils.toString(response.getEntity()));
        }
        throw new Exception();
    }
}
