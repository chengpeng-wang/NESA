package u.aly;

import android.content.Context;
import android.content.res.Resources;
import com.moceanmobile.mast.MASTNativeAdConstants;

/* compiled from: Res */
public class ak {
    private static final String a = ak.class.getName();
    private static ak b = null;
    private Resources c;
    private final String d;
    private final String e = "drawable";
    private final String f = MASTNativeAdConstants.ID_STRING;
    private final String g = "layout";
    private final String h = "anim";
    private final String i = "style";
    private final String j = "string";
    private final String k = "array";

    private ak(Context context) {
        this.c = context.getResources();
        this.d = context.getPackageName();
    }

    public static synchronized ak a(Context context) {
        ak akVar;
        synchronized (ak.class) {
            if (b == null) {
                b = new ak(context.getApplicationContext());
            }
            akVar = b;
        }
        return akVar;
    }

    public int a(String str) {
        return a(str, "anim");
    }

    public int b(String str) {
        return a(str, MASTNativeAdConstants.ID_STRING);
    }

    public int c(String str) {
        return a(str, "drawable");
    }

    public int d(String str) {
        return a(str, "layout");
    }

    public int e(String str) {
        return a(str, "style");
    }

    public int f(String str) {
        return a(str, "string");
    }

    public int g(String str) {
        return a(str, "array");
    }

    private int a(String str, String str2) {
        int identifier = this.c.getIdentifier(str, str2, this.d);
        if (identifier != 0) {
            return identifier;
        }
        aj.b(a, "getRes(" + str2 + "/ " + str + ")");
        aj.b(a, "Error getting resource. Make sure you have copied all resources (res/) from SDK to your project. ");
        return 0;
    }
}
