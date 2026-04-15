package com.baidu.android.pushservice.b;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import com.baidu.android.common.logging.Log;
import com.baidu.android.pushservice.a;
import com.baidu.android.pushservice.b;
import com.baidu.android.pushservice.d;
import com.baidu.android.pushservice.util.PushDatabase;
import com.baidu.android.pushservice.util.k;
import com.baidu.android.pushservice.w;
import com.baidu.android.pushservice.y;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class e extends k {
    private static e e = null;
    private SQLiteDatabase c;
    private c d;
    private int f;
    private Location g;
    private int h;
    private String i;
    private String j;
    private String k;
    private TelephonyManager l;
    private WifiManager m;
    private LocationManager n;

    private e(Context context) {
        super(context);
        this.c = null;
        this.d = null;
        this.f = 0;
        this.h = 0;
        this.l = null;
        this.m = null;
        this.n = null;
        this.b = w.f;
        this.l = (TelephonyManager) context.getSystemService("phone");
        this.m = (WifiManager) context.getSystemService("wifi");
        this.k = y.a().c();
        this.n = (LocationManager) context.getSystemService("location");
    }

    public static e a(Context context) {
        if (e == null) {
            e = new e(context);
        }
        return e;
    }

    private SQLiteDatabase f() {
        try {
            if (this.c == null) {
                this.c = PushDatabase.getDb(this.a);
            }
        } catch (Exception e) {
        }
        return this.c;
    }

    private void h() {
        if (TextUtils.isEmpty(this.i)) {
            this.i = this.l.getDeviceId();
        }
        if (TextUtils.isEmpty(this.k)) {
            this.k = y.a().c();
        }
        CellLocation cellLocation = this.l.getCellLocation();
        int phoneType = this.l.getPhoneType();
        int cid = phoneType == 1 ? ((GsmCellLocation) cellLocation).getCid() : phoneType == 2 ? ((CdmaCellLocation) cellLocation).getNetworkId() : 0;
        if (cid <= 0) {
            cid = this.h;
        }
        this.h = cid;
        String macAddress = this.m.getConnectionInfo().getMacAddress();
        if (TextUtils.isEmpty(macAddress)) {
            macAddress = this.j;
        }
        this.j = macAddress;
        Location lastKnownLocation = this.n.getLastKnownLocation("gps");
        if (lastKnownLocation == null) {
            lastKnownLocation = this.g;
        }
        this.g = lastKnownLocation;
        if (b.a(this.a)) {
            Log.d("AppStatisticsSender", ">>> Completed update client info: ");
            Log.d("AppStatisticsSender", "    imei=" + this.i);
            Log.d("AppStatisticsSender", "    channelid=" + this.k);
            Log.d("AppStatisticsSender", "    cellid=" + this.h);
            Log.d("AppStatisticsSender", "    wifi=" + this.j);
            Log.d("AppStatisticsSender", "    location=" + this.g);
        }
    }

    private void i() {
        if (this.d != null && this.d.a.size() != 0) {
            for (d dVar : this.d.a) {
                PushDatabase.deleteStatisticsInfo(f(), dVar.a);
            }
            this.d = null;
        } else if (b.a(this.a)) {
            Log.d("AppStatisticsSender", "stat info has no record, cancel sync database mStatInfo=" + this.d);
        }
    }

    /* access modifiers changed from: protected */
    public void a(String str, List list) {
        com.baidu.android.pushservice.a.b.a(list);
        list.add(new BasicNameValuePair("method", "appusestat"));
        list.add(new BasicNameValuePair("channel_token", y.a().d()));
        if (b.a(this.a)) {
            Log.d("AppStatisticsSender", "Sending statistics data: " + str);
        }
        list.add(new BasicNameValuePair("data", str));
    }

    /* access modifiers changed from: 0000 */
    public boolean a() {
        return PushDatabase.getStatisticsInfoCounts(f()) != 0;
    }

    /* access modifiers changed from: protected */
    public String b() {
        if (b.a(this.a)) {
            Log.d("AppStatisticsSender", "start productSendData");
        }
        h();
        this.d = new c();
        c cVar = this.d;
        int i = this.f;
        this.f = i + 1;
        cVar.a(i, this.g, this.h, this.j);
        List<k> selectStatisticsInfo = PushDatabase.selectStatisticsInfo(f(), 10);
        if (selectStatisticsInfo == null || selectStatisticsInfo.size() == 0) {
            if (b.a(this.a)) {
                Log.d("AppStatisticsSender", "feedbackList is null, return null.");
            }
            return null;
        }
        for (k kVar : selectStatisticsInfo) {
            d dVar = new d();
            d a = a.a(this.a).a(kVar.b);
            if (a != null) {
                dVar.b = a.b;
                dVar.c = a.c;
                dVar.a = kVar.a;
                dVar.d = kVar.c;
                dVar.e = kVar.d;
                dVar.f = kVar.e;
                dVar.g = kVar.f;
                dVar.h = kVar.g;
                dVar.i = kVar.h;
                if (b.a(this.a)) {
                    try {
                        Log.d("AppStatisticsSender", ">>> Get one App statistics record: " + dVar.a().toString());
                    } catch (Exception e) {
                        Log.d("AppStatisticsSender", ">>> Get one App statistics Exception!");
                    }
                }
                this.d.a(dVar);
            }
        }
        if (this.d.a.size() != 0) {
            return this.d.a();
        }
        if (b.a(this.a)) {
            Log.d("AppStatisticsSender", "recordList num is 0.");
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void c() {
        i();
    }

    /* access modifiers changed from: protected */
    public void d() {
        if (b.a(this.a)) {
            Log.d("AppStatisticsSender", "The last send if fail, maybe has network problem now. Abort task, try later.");
        }
        this.d = null;
    }

    /* access modifiers changed from: 0000 */
    public boolean e() {
        return false;
    }
}
