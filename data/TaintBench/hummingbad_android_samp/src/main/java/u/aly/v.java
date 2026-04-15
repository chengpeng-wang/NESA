package u.aly;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.a;
import java.util.Arrays;
import java.util.List;

/* compiled from: SessionTracker */
public class v {
    private static final String a = "session_start_time";
    private static final String b = "session_end_time";
    private static final String c = "session_id";
    private static final String f = "activities";
    private final String d = "a_start_time";
    private final String e = "a_end_time";

    public F a(Context context) {
        SharedPreferences a = u.a(context);
        String string = a.getString(c, null);
        if (string == null) {
            return null;
        }
        long j = a.getLong(a, 0);
        long j2 = a.getLong(b, 0);
        long j3 = 0;
        if (j2 != 0) {
            j3 = j2 - j;
            if (Math.abs(j3) > a.m) {
                j3 = 0;
            }
        }
        F f = new F();
        f.a(string);
        f.a(j);
        f.b(j2);
        f.c(j3);
        double[] location = AnalyticsConfig.getLocation();
        if (location != null) {
            W w = new W(location[0], location[1], System.currentTimeMillis());
            if (f.y()) {
                f.a(w);
            } else {
                f.b(Arrays.asList(new W[]{w}));
            }
        }
        ae a2 = y.a(context);
        if (a2 != null) {
            f.a(a2);
        }
        List a3 = z.a(a);
        if (a3 != null && a3.size() > 0) {
            f.a(a3);
        }
        a(a);
        return f;
    }

    private void a(SharedPreferences sharedPreferences) {
        Editor edit = sharedPreferences.edit();
        edit.remove(a);
        edit.remove(b);
        edit.remove(c);
        edit.remove("a_start_time");
        edit.remove("a_end_time");
        edit.putString(f, "");
        edit.commit();
    }

    public String b(Context context) {
        String f = ai.f(context);
        String appkey = AnalyticsConfig.getAppkey(context);
        long currentTimeMillis = System.currentTimeMillis();
        if (appkey == null) {
            throw new RuntimeException("Appkey is null or empty, Please check AndroidManifest.xml");
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(currentTimeMillis).append(appkey).append(f);
        return av.a(stringBuilder.toString());
    }

    public void c(Context context) {
        SharedPreferences a = u.a(context);
        if (a != null) {
            if (b(a)) {
                aj.a(a.e, "Start new session: " + a(context, a));
            } else {
                aj.a(a.e, "Extend current session: " + a.getString(c, null));
            }
            Editor edit = a.edit();
            edit.putLong("a_start_time", System.currentTimeMillis());
            edit.putLong("a_end_time", 0);
            edit.commit();
        }
    }

    public void d(Context context) {
        SharedPreferences a = u.a(context);
        if (a != null) {
            if (a.getLong("a_start_time", 0) == 0 && AnalyticsConfig.ACTIVITY_DURATION_OPEN) {
                aj.b(a.e, "onPause called before onResume");
                return;
            }
            long currentTimeMillis = System.currentTimeMillis();
            Editor edit = a.edit();
            edit.putLong("a_start_time", 0);
            edit.putLong("a_end_time", currentTimeMillis);
            edit.putLong(b, currentTimeMillis);
            edit.commit();
        }
    }

    private boolean b(SharedPreferences sharedPreferences) {
        long j = sharedPreferences.getLong("a_start_time", 0);
        long j2 = sharedPreferences.getLong("a_end_time", 0);
        long currentTimeMillis = System.currentTimeMillis();
        if (j != 0 && currentTimeMillis - j < AnalyticsConfig.kContinueSessionMillis) {
            aj.b(a.e, "onResume called before onPause");
            return false;
        } else if (currentTimeMillis - j2 > AnalyticsConfig.kContinueSessionMillis) {
            return true;
        } else {
            return false;
        }
    }

    private String a(Context context, SharedPreferences sharedPreferences) {
        k a = k.a(context);
        String b = b(context);
        p a2 = a(context);
        Editor edit = sharedPreferences.edit();
        edit.putString(c, b);
        edit.putLong(a, System.currentTimeMillis());
        edit.putLong(b, 0);
        edit.commit();
        if (a2 != null) {
            a.a(a2);
        } else {
            a.a(null);
        }
        return b;
    }
}
