package ca.ji.no.method10;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy.Builder;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.http.client.methods.HttpGet;

public class UpdataInfoService {
    private Context context;

    public UpdataInfoService(Context context) {
        this.context = context;
    }

    @SuppressLint({"NewApi"})
    public UpdataInfo getUpdataInfo(int urlid) throws Exception {
        URL url = new URL(this.context.getResources().getString(urlid));
        StrictMode.setThreadPolicy(new Builder().permitAll().build());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(2000);
        conn.setRequestMethod(HttpGet.METHOD_NAME);
        return UpdataInfoParser.getUpdataInfo(conn.getInputStream());
    }
}
