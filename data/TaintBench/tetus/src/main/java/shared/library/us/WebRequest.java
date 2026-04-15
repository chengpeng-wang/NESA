package shared.library.us;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class WebRequest {
    public String error;
    public InputStream inputStream = null;
    public Context parent = null;
    public String uri;

    public WebRequest(Context Parent, String Uri) {
        String str = "";
        String str2 = "";
        this.error = str;
        str2 = "";
        this.uri = str;
        this.uri = Uri;
        this.parent = Parent;
    }

    public boolean Download() {
        try {
            URLConnection urlConn = new URL(this.uri).openConnection();
            if (urlConn instanceof HttpURLConnection) {
                HttpURLConnection httpConn = (HttpURLConnection) urlConn;
                httpConn.setAllowUserInteraction(false);
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setRequestMethod("GET");
                httpConn.connect();
                if (httpConn.getResponseCode() != 200) {
                    return true;
                }
                this.inputStream = httpConn.getInputStream();
                return true;
            }
            throw new IOException("URL is not an Http URL");
        } catch (MalformedURLException e) {
            this.error = e.getMessage();
            return false;
        } catch (IOException e2) {
            this.error = e2.getMessage();
            return false;
        }
    }

    public String InputStreamToText() {
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(this.inputStream), 4096);
            StringBuilder sb = new StringBuilder();
            while (true) {
                String line = rd.readLine();
                if (line == null) {
                    rd.close();
                    this.inputStream.close();
                    return sb.toString();
                }
                sb.append(line);
            }
        } catch (Exception e) {
            this.error = e.getMessage();
            return "";
        }
    }

    public BitmapDrawable InputStreamToBitmap() {
        try {
            Bitmap bitmapOrg = BitmapFactory.decodeStream(this.inputStream);
            int width = bitmapOrg.getWidth();
            int height = bitmapOrg.getHeight();
            float scaleWidth = ((float) 80) / ((float) width);
            float scaleHeight = ((float) 80) / ((float) height);
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            return new BitmapDrawable(Bitmap.createBitmap(bitmapOrg, 0, 0, width, height, matrix, true));
        } catch (Exception e) {
            this.error = e.getMessage();
            return null;
        }
    }
}
