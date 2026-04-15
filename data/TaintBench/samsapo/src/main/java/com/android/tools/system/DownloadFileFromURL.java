package com.android.tools.system;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class DownloadFileFromURL extends AsyncTask<String, Void, File> {
    public Activity activity;
    public Context context;

    /* access modifiers changed from: protected|bridge */
    public /* bridge */ Object doInBackground(Object[] objArr) {
        return doInBackground((String[]) objArr);
    }

    /* access modifiers changed from: protected|bridge */
    public /* bridge */ void onPostExecute(Object obj) {
        onPostExecute((File) obj);
    }

    public DownloadFileFromURL(Context context) {
        this.context = context;
    }

    /* access modifiers changed from: protected */
    @Override
    public void onPreExecute() {
        super.onPreExecute();
    }

    /* access modifiers changed from: protected|varargs */
    @Override
    public File doInBackground(String... strArr) {
        String[] strArr2 = strArr;
        File file = null;
        Object obj = null;
        try {
            URL url = r22;
            URL url2 = new URL(strArr2[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.connect();
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            String lastPathSegment = Uri.parse(strArr2[0]).getLastPathSegment();
            File file2 = r22;
            StringBuffer stringBuffer = r22;
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2 = r22;
            StringBuffer stringBuffer3 = new StringBuffer();
            File file3 = new File(stringBuffer.append(stringBuffer2.append(externalStorageDirectory.getAbsolutePath()).append("/").toString()).append(lastPathSegment).toString());
            file = file2;
            FileOutputStream fileOutputStream = r22;
            FileOutputStream fileOutputStream2 = new FileOutputStream(file);
            FileOutputStream fileOutputStream3 = fileOutputStream;
            InputStream inputStream = httpURLConnection.getInputStream();
            int contentLength = httpURLConnection.getContentLength();
            int i = 0;
            byte[] bArr = new byte[1024];
            Object obj2 = null;
            while (true) {
                int read = inputStream.read(bArr);
                int i2 = read;
                if (read <= 0) {
                    fileOutputStream3.close();
                    inputStream.close();
                    return file;
                }
                fileOutputStream3.write(bArr, 0, i2);
                i += i2;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return file;
        } catch (IOException e2) {
            e2.printStackTrace();
            return file;
        }
    }

    /* access modifiers changed from: protected */
    @Override
    public void onPostExecute(File file) {
        super.onPostExecute(file);
    }
}
