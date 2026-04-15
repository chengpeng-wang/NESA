package sx.jolly.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import sx.jolly.exceptions.NoConnectionException;

public class Post {
    private String data = "";
    private String url = "";

    public Post(Url url, String data) {
        url.setServer("http://partnerslab.com/-/");
        this.url = url.getStringUrl();
        this.data = data;
    }

    private String getUrl() {
        return this.url;
    }

    public void post() throws NoConnectionException {
        Utils.slog(Utils.class, "post to " + getUrl());
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(this.url);
        try {
            List<NameValuePair> nameValuePairs = new ArrayList(2);
            nameValuePairs.add(new BasicNameValuePair("body", this.data));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse execute = httpclient.execute(httppost);
        } catch (ClientProtocolException e) {
        } catch (IOException e2) {
            throw new NoConnectionException(e2);
        }
        Utils.slog(Utils.class, "post success");
    }
}
