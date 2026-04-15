package com.baidu.android.pushservice.b;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.provider.Settings.System;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.Log;
import com.baidu.android.common.util.CommonParam;
import com.baidu.android.pushservice.PushSettings;
import com.baidu.android.pushservice.a;
import com.baidu.android.pushservice.b;
import com.baidu.android.pushservice.d;
import com.baidu.android.pushservice.util.m;
import com.baidu.android.pushservice.w;
import com.baidu.loctp.str.BDLocManager;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class h extends k {
    private static int d = 4;
    private static h e = null;
    private String c;

    public h(Context context) {
        super(context);
        this.c = "LbsSender";
        this.b = w.h;
    }

    public static h a(Context context) {
        if (e == null) {
            e = new h(context);
        }
        return e;
    }

    private String h() {
        String bssid = ((WifiManager) this.a.getSystemService("wifi")).getConnectionInfo().getBSSID();
        if (!TextUtils.isEmpty(bssid)) {
            return bssid;
        }
        TelephonyManager telephonyManager = (TelephonyManager) this.a.getSystemService("phone");
        CellLocation cellLocation = telephonyManager.getCellLocation();
        if (cellLocation instanceof GsmCellLocation) {
            GsmCellLocation gsmCellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
            return gsmCellLocation.getCid() + "" + gsmCellLocation.getLac();
        } else if (!(cellLocation instanceof CdmaCellLocation)) {
            return "";
        } else {
            CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) telephonyManager.getCellLocation();
            cdmaCellLocation.getNetworkId();
            int baseStationId = cdmaCellLocation.getBaseStationId();
            int networkId = cdmaCellLocation.getNetworkId();
            return baseStationId + "" + networkId + "" + cdmaCellLocation.getSystemId();
        }
    }

    /* access modifiers changed from: 0000 */
    public void a(String str, List list) {
        list.add(new BasicNameValuePair("method", "uploadGeo"));
        if (b.a(this.a)) {
            Log.d(this.c, "Sending LBS data: " + str);
        }
        list.add(new BasicNameValuePair("data", str));
    }

    /* access modifiers changed from: 0000 */
    public boolean a() {
        return true;
    }

    /* access modifiers changed from: 0000 */
    public String b() {
        int i = 0;
        String a = PushSettings.a();
        if (!TextUtils.isEmpty(a)) {
            String locString = new BDLocManager(this.a.getApplicationContext()).getLocString(d);
            String h = h();
            if (!TextUtils.isEmpty(locString)) {
                Object obj;
                String string = System.getString(this.a.getContentResolver(), "com.baidu.android.pushservice.lac");
                if (!TextUtils.isEmpty(h)) {
                    if (TextUtils.equals(h, string)) {
                        if (b.a(this.a)) {
                            Log.i(this.c, "lbsinfo equals");
                        }
                        PushSettings.b(System.currentTimeMillis());
                        return null;
                    }
                    if (b.a(this.a)) {
                        Log.i(this.c, "lbsinfo not the same");
                    }
                    System.putString(this.a.getContentResolver(), "com.baidu.android.pushservice.lac", h);
                }
                h = f();
                int indexOf = h.indexOf(37);
                if (!(h == null || indexOf == -1)) {
                    h = h.substring(0, indexOf);
                }
                if (h == null) {
                    obj = "";
                } else {
                    string = h;
                }
                a a2 = a.a(this.a);
                ArrayList arrayList = new ArrayList();
                JSONArray jSONArray = new JSONArray();
                JSONObject jSONObject = new JSONObject();
                ArrayList arrayList2 = a2.b;
                for (int i2 = 0; i2 < arrayList2.size(); i2++) {
                    if (!TextUtils.isEmpty(((d) arrayList2.get(i2)).b)) {
                        JSONObject jSONObject2 = new JSONObject();
                        try {
                            jSONObject2.put("userid", m.b(((d) arrayList2.get(i2)).c));
                            jSONObject2.put("appid", ((d) arrayList2.get(i2)).b);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        jSONArray.put(jSONObject2);
                    }
                }
                ArrayList arrayList3 = a2.a;
                while (i < arrayList3.size()) {
                    h = ((d) arrayList3.get(i)).b;
                    if (!(TextUtils.isEmpty(h) || a2.c(h))) {
                        Log.d(this.c, ((d) arrayList3.get(i)).c + ":" + ((d) arrayList3.get(i)).b);
                        JSONObject jSONObject3 = new JSONObject();
                        try {
                            jSONObject3.put("userid", m.b(((d) arrayList3.get(i)).c));
                            jSONObject3.put("appid", ((d) arrayList3.get(i)).b);
                        } catch (JSONException e2) {
                            e2.printStackTrace();
                        }
                        jSONArray.put(jSONObject3);
                    }
                    i++;
                }
                if (jSONArray.length() > 0) {
                    try {
                        jSONObject.put("channelid", a);
                        jSONObject.put("cuid", CommonParam.getCUID(this.a));
                        jSONObject.put("nettype", m.r(this.a.getApplicationContext()));
                        jSONObject.put("clients", jSONArray);
                        jSONObject.put("apinfo", locString);
                        jSONObject.put("cip", obj);
                        jSONObject.put("model", Build.MODEL);
                        jSONObject.put(ClientCookie.VERSION_ATTR, VERSION.RELEASE);
                        jSONObject.put("sdkversion", 13);
                    } catch (JSONException e22) {
                        e22.printStackTrace();
                    }
                    return jSONObject.toString();
                }
            }
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public void c() {
        if (b.a(this.a)) {
            Log.i(this.c, "<<< Location info send result return OK!");
        }
        PushSettings.b(System.currentTimeMillis());
    }

    /* access modifiers changed from: 0000 */
    public void d() {
        if (b.a(this.a)) {
            Log.i(this.c, "<<< Location info send result failed!");
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean e() {
        return true;
    }

    public String f() {
        try {
            Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                Enumeration inetAddresses = ((NetworkInterface) networkInterfaces.nextElement()).getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
            Log.e(this.c, e.toString());
        }
        return "";
    }
}
