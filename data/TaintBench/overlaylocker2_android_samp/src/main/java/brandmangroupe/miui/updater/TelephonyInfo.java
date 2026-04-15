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
