package com.cleanmaster.ui.app.market.data.filter;

import com.cleanmaster.util.q;
import com.picksinit.c;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: time_condition */
public class g extends b {
    private static SimpleDateFormat g = null;
    long e = -1;
    long f = -1;

    /* access modifiers changed from: protected */
    public boolean b(a aVar) {
        long g = aVar.g();
        return g >= this.e && g <= this.f;
    }

    public static String a(long j) {
        if (j <= 0) {
            return "N/A";
        }
        if (new Date(j).getYear() == 70) {
            return c.getInstance().getContext().getString(q.a("market_unknown_app_install_date"));
        }
        Calendar.getInstance().setTime(new Date(j));
        if (g == null) {
            g = new SimpleDateFormat("yyyy-MM-dd:hh:mm");
        }
        return g.format(Long.valueOf(j));
    }

    public static g b(JSONObject jSONObject) {
        g gVar = new g();
        try {
            JSONObject jSONObject2 = jSONObject.getJSONObject("time_condition");
            if (jSONObject2 != null) {
                gVar.e = jSONObject2.optLong("start_time", -1);
                if (gVar.e > 0) {
                    gVar.e *= 1000;
                    gVar.e = b(gVar.e);
                }
                gVar.f = jSONObject2.optLong("end_time", -1);
                if (gVar.f > 0) {
                    gVar.f *= 1000;
                    gVar.f = b(gVar.f);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return gVar;
    }

    public static long b(long j) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = new Date(j);
        try {
            date = simpleDateFormat.parse(simpleDateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }
}
