package com.dsifakf.aoakmnq;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class Connect3 {
    private HttpURLConnection httpConnection;
    private Progress progress;
    private URL url;

    interface Progress {
        void progressUpdate(int i);
    }

    public Connect3(String link) {
        try {
            this.url = new URL(link);
        } catch (Exception e) {
            this.url = null;
        }
    }

    public JSONArray getJSONData() {
        try {
            return new JSONArray(dataGet());
        } catch (Exception e) {
            return null;
        }
    }

    public JSONObject getJSONObject() {
        try {
            return new JSONObject(dataGet());
        } catch (Exception e) {
            return null;
        }
    }

    public String dataGet() {
        connect();
        StringBuilder resp = new StringBuilder();
        String uncryptResult = null;
        Secure parse = new Secure();
        this.httpConnection.setConnectTimeout(100000);
        try {
            if (this.httpConnection.getResponseCode() == 200) {
                if (this.progress != null) {
                    this.progress.progressUpdate(10);
                }
                BufferedReader inp = new BufferedReader(new InputStreamReader(this.httpConnection.getInputStream()));
                int contL = this.httpConnection.getContentLength();
                int readBytes = 0;
                while (true) {
                    String stL = inp.readLine();
                    if (stL == null) {
                        break;
                    }
                    resp.append(stL);
                    readBytes += stL.getBytes("ISO-8859-2").length + 2;
                    if (this.progress != null) {
                        if ((readBytes / contL) * 100 > 10) {
                            this.progress.progressUpdate((readBytes / contL) * 100);
                        }
                        this.progress.progressUpdate(100);
                    }
                }
                inp.close();
            }
            disconnect();
            try {
                uncryptResult = new String(parse.decrypt(resp.toString()));
            } catch (Exception e) {
            }
            return uncryptResult;
        } catch (Exception e2) {
            return null;
        }
    }

    public void disconnect() {
        this.httpConnection.disconnect();
    }

    public void connect() {
        try {
            this.httpConnection = (HttpURLConnection) this.url.openConnection();
        } catch (Exception e) {
            this.httpConnection = null;
        }
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }
}
