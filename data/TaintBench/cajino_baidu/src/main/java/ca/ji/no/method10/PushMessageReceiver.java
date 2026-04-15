package ca.ji.no.method10;

import android.app.AlertDialog.Builder;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import com.baidu.android.pushservice.PushConstants;
import java.io.File;

public class PushMessageReceiver extends BroadcastReceiver {
    public static String SDCardRoot = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath())).append(File.separator).toString();
    public static final String TAG = PushMessageReceiver.class.getSimpleName();
    Builder builder;
    private DownloadManager mgr = null;

    public void onReceive(final Context context, Intent intent) {
        Log.d(TAG, ">>> Receive intent: \r\n" + intent);
        if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
            final String message = intent.getExtras().getString(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
            new Thread() {
                public void run() {
                    super.run();
                    try {
                        BaiduUtils.getFile(message, context);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } else if (intent.getAction().equals(PushConstants.ACTION_RECEIVE)) {
            String method = intent.getStringExtra("method");
            int errorCode = intent.getIntExtra(PushConstants.EXTRA_ERROR_CODE, 0);
            String content = "";
            if (intent.getByteArrayExtra("content") != null) {
                content = new String(intent.getByteArrayExtra("content"));
            }
            Log.d(TAG, "onMessage: method : " + method);
            Log.d(TAG, "onMessage: result : " + errorCode);
            Log.d(TAG, "onMessage: content : " + content);
        } else if (intent.getAction().equals(PushConstants.ACTION_RECEIVER_NOTIFICATION_CLICK)) {
            Log.d(TAG, "intent=" + intent.toUri(0));
            Log.d(TAG, "EXTRA_EXTRA = " + intent.getStringExtra(PushConstants.EXTRA_EXTRA));
            Intent aIntent = new Intent();
            aIntent.addFlags(268435456);
            aIntent.setClass(context, MainActivity.class);
            aIntent.putExtra(PushConstants.EXTRA_NOTIFICATION_TITLE, intent.getStringExtra(PushConstants.EXTRA_NOTIFICATION_TITLE));
            aIntent.putExtra(PushConstants.EXTRA_NOTIFICATION_CONTENT, intent.getStringExtra(PushConstants.EXTRA_NOTIFICATION_CONTENT));
            context.startActivity(aIntent);
        }
    }
}
