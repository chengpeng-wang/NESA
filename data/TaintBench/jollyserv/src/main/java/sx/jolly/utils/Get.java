package sx.jolly.utils;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.acra.ACRAConstants;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class Get {
    private Context context = null;
    private List<String> response = null;
    private Url url = null;

    public Get(Url url, Context context) {
        this.url = url;
        this.context = context;
    }

    private List<String> getResponse() {
        return this.response;
    }

    private Url getUrl() {
        return this.url;
    }

    /* access modifiers changed from: protected */
    public List<String> tryGet(Url url) {
        Utils.slog(Utils.class, "get " + getUrl());
        List<String> lines = new ArrayList();
        HttpGet get = new HttpGet(url.getStringUrl());
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, ACRAConstants.DEFAULT_CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParameters, ACRAConstants.DEFAULT_CONNECTION_TIMEOUT);
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(new DefaultHttpClient(httpParameters).execute(get).getEntity().getContent()));
            while (true) {
                String line = r.readLine();
                if (line == null) {
                    break;
                }
                lines.add(line);
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e2) {
            return null;
        }
        Utils.slog(Utils.class, "response received ok: " + lines.toString());
        return lines;
    }

    public List<String> get() {
        Url url = getUrl();
        List<String> servers = new ServerManager(this.context).getServers();
        List<String> res = new ArrayList();
        for (String server : servers) {
            url.setServer(server);
            res = tryGet(url);
            if (res != null) {
                return res;
            }
        }
        return res;
    }
}
