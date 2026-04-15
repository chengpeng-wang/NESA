package gr.georkouk.kastorakiacounter_new;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

public class MyServerPost extends AsyncTask<String, String, String> {
    Activity act;
    Context context;
    String executeType;

    public MyServerPost(Context context_, String type_, Activity act_) {
        this.context = context_;
        this.executeType = type_;
        this.act = act_;
    }

    /* access modifiers changed from: protected|varargs */
    public String doInBackground(String... params) {
        while (true) {
            if (this.executeType.equals("checkConnection")) {
                checkConnection();
            }
            try {
                Thread.sleep((long) (this.context.getSharedPreferences("Settings", 0).getInt("sec", 5) * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    /* access modifiers changed from: protected */
    public void onPreExecute() {
        super.onPreExecute();
    }

    /* access modifiers changed from: protected */
    public void onCancelled() {
        super.onCancelled();
    }

    public String checkConnection() {
        MyServerFunctions myServer = new MyServerFunctions(this.context);
        boolean ok = myServer.checkConnection();
        if (ok) {
            myServer.register();
        }
        return String.valueOf(ok);
    }
}
