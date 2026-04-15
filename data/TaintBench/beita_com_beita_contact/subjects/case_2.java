package com.beita.contact;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UploadUtil {
    private static String srcPath = (Application.sdcardPathString + "/contact_backup.txt");

    public static void uploadFile() {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL("http://192.168.2.105:8080/upload_file_service/UploadServlet").openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
            dos.writeBytes(new StringBuilder(String.valueOf(twoHyphens)).append(boundary).append(end).toString());
            dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + srcPath.substring(srcPath.lastIndexOf("/") + 1) + "\"" + end);
            dos.writeBytes(end);
            FileInputStream fis = new FileInputStream(srcPath);
            byte[] buffer = new byte[8192];
            while (true) {
                int count = fis.read(buffer);
                if (count == -1) {
                    fis.close();
                    dos.writeBytes(end);
                    dos.writeBytes(new StringBuilder(String.valueOf(twoHyphens)).append(boundary).append(twoHyphens).append(end).toString());
                    dos.flush();
                    buffer = httpURLConnection.getInputStream();
                    boundary = new BufferedReader(new InputStreamReader(buffer, "utf-8")).readLine();
                    dos.close();
                    buffer.close();
                    return;
                }
                dos.write(buffer, 0, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
