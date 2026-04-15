package com.xmpp.client.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import com.android.service.view.PhotoRecord;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class DownloadZip {
    /* access modifiers changed from: private */
    public Context _Context;
    /* access modifiers changed from: private */
    public String _downloadedZipFile;
    /* access modifiers changed from: private */
    public String _unzipLocation;
    private String _zipFileURL;
    private int id;
    private boolean isSDCardExist;
    /* access modifiers changed from: private */
    public ProgressDialog mProgressDialog_fordownload;
    private String num;

    class DownloadMapAsync extends AsyncTask<String, String, String> {
        String result = "";

        DownloadMapAsync() {
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
            DownloadZip.this.mProgressDialog_fordownload = new ProgressDialog(DownloadZip.this._Context);
            DownloadZip.this.mProgressDialog_fordownload.setMessage("載入中 請稍後....");
            DownloadZip.this.mProgressDialog_fordownload.setProgressStyle(1);
            DownloadZip.this.mProgressDialog_fordownload.setCancelable(false);
            DownloadZip.this.mProgressDialog_fordownload.show();
        }

        /* access modifiers changed from: protected|varargs */
        public String doInBackground(String... aurl) {
            try {
                URL url = new URL(aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();
                System.out.println("-------lenghtOfFile---------" + conexion.getContentLength());
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(DownloadZip.this._downloadedZipFile);
                byte[] data = new byte[1024];
                long total = 0;
                while (true) {
                    int count = input.read(data);
                    if (count == -1) {
                        break;
                    }
                    total += (long) count;
                    publishProgress(new String[]{((int) ((100 * total) / ((long) lenghtOfFile)))});
                    output.write(data, 0, count);
                }
                output.close();
                input.close();
                this.result = "true";
            } catch (Exception e) {
                this.result = "false";
            }
            return null;
        }

        /* access modifiers changed from: protected|varargs */
        public void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC", progress[0]);
            DownloadZip.this.mProgressDialog_fordownload.setProgress(Integer.parseInt(progress[0]));
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String unused) {
            DownloadZip.this.mProgressDialog_fordownload.dismiss();
            if (this.result.equalsIgnoreCase("true")) {
                try {
                    DownloadZip.this.unzip();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class UnZipTask extends AsyncTask<String, Void, Boolean> {
        private UnZipTask() {
        }

        /* synthetic */ UnZipTask(DownloadZip downloadZip, UnZipTask unZipTask) {
            this();
        }

        /* access modifiers changed from: protected|varargs */
        public Boolean doInBackground(String... params) {
            String filePath = params[0];
            String destinationPath = params[1];
            try {
                ZipFile zipfile = new ZipFile(new File(filePath));
                Enumeration e = zipfile.entries();
                while (e.hasMoreElements()) {
                    unzipEntry(zipfile, (ZipEntry) e.nextElement(), destinationPath);
                }
                new UnzipUtil(DownloadZip.this._downloadedZipFile, DownloadZip.this._unzipLocation).unzip();
                return Boolean.valueOf(true);
            } catch (Exception e2) {
                return Boolean.valueOf(false);
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Boolean result) {
            DownloadZip.this.mProgressDialog_fordownload.dismiss();
            DownloadZip.this._Context.startActivity(new Intent(DownloadZip.this._Context, PhotoRecord.class));
        }

        private void unzipEntry(ZipFile zipfile, ZipEntry entry, String outputDir) throws IOException {
            if (entry.isDirectory()) {
                createDir(new File(outputDir, entry.getName()));
                return;
            }
            File outputFile = new File(outputDir, entry.getName());
            if (!outputFile.getParentFile().exists()) {
                createDir(outputFile.getParentFile());
            }
            BufferedInputStream inputStream = new BufferedInputStream(zipfile.getInputStream(entry));
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        }

        private void createDir(File dir) {
            if (!dir.exists() && !dir.mkdirs()) {
                throw new RuntimeException("Can not create dir " + dir);
            }
        }
    }

    public DownloadZip(Context context, String zipFile, String location, String zipUrl) {
        this._zipFileURL = zipUrl;
        this._unzipLocation = zipFile;
        this._downloadedZipFile = location;
        this._Context = context;
    }

    public void setName(String num) {
        this.num = num;
    }

    public String getName() {
        return this.num;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void zip() {
        this.isSDCardExist = Environment.getExternalStorageState().equals("mounted");
        if (this.isSDCardExist) {
            File path = new File(this._unzipLocation);
            if (!path.exists()) {
                path.mkdir();
            }
        }
        new DownloadMapAsync().execute(new String[]{this._zipFileURL});
    }

    public void unzip() throws IOException {
        this.mProgressDialog_fordownload = new ProgressDialog(this._Context);
        this.mProgressDialog_fordownload.setMessage("載入已完成!! ");
        this.mProgressDialog_fordownload.setProgressStyle(0);
        this.mProgressDialog_fordownload.setCancelable(false);
        this.mProgressDialog_fordownload.show();
        new UnZipTask(this, null).execute(new String[]{this._downloadedZipFile, this._unzipLocation});
    }
}
