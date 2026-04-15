package com.qc.common;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import com.qc.base.QCCache;
import com.qc.entity.ApnInfo;
import com.qc.entity.WapApnName;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class WapApnHelper {
    public static final Uri APN_LIST_URI = Uri.parse("content://telephony/carriers");
    public static final Uri CURRENT_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
    private static ConnectivityManager mCM;
    private final ConnectivityManager conManager;
    private Context context;
    private NetworkInfo info = this.conManager.getActiveNetworkInfo();
    private ContentResolver resolver;
    private TelephonyManager telephonyManager;
    private WifiManager wm;

    public WapApnHelper(Context context) {
        this.context = context;
        this.conManager = (ConnectivityManager) context.getSystemService("connectivity");
        mCM = (ConnectivityManager) context.getSystemService("connectivity");
        this.wm = (WifiManager) context.getSystemService("wifi");
        this.telephonyManager = (TelephonyManager) context.getSystemService("phone");
        this.resolver = context.getContentResolver();
    }

    public void saveState() {
        QCCache.getInstance().reSetValue("mobileConnected", Boolean.valueOf(getMobileDataStatus()));
        QCCache.getInstance().reSetValue("wifiOpen", Boolean.valueOf(checkWifi()));
        QCCache.getInstance().reSetValue("apnDedault", getCurrentAPNId());
    }

    public void reSetNetState() {
        if (QCCache.getInstance().getValue("mobileConnected") != null) {
            toggleMobileData(((Boolean) QCCache.getInstance().getValue("mobileConnected")).booleanValue());
        }
        if (QCCache.getInstance().getValue("wifiOpen") != null) {
            this.wm.setWifiEnabled(((Boolean) QCCache.getInstance().getValue("wifiOpen")).booleanValue());
        }
        if (QCCache.getInstance().getValue("apnDedault") != null) {
            setAPN((String) QCCache.getInstance().getValue("apnDedault"));
        }
    }

    public void openAPN() {
        setDataConnection(this.context, true);
    }

    public ApnInfo getCurrentAPN() {
        ApnInfo apn = new ApnInfo();
        Cursor cur = null;
        try {
            cur = this.resolver.query(CURRENT_APN_URI, null, null, null, null);
            if (cur == null || !cur.moveToFirst()) {
                if (cur != null) {
                    cur.close();
                }
                return null;
            }
            String apnID = cur.getString(cur.getColumnIndex("_id"));
            String apnName = cur.getString(cur.getColumnIndex("apn"));
            String apnType = cur.getString(cur.getColumnIndex("type"));
            String current = cur.getString(cur.getColumnIndex("current"));
            apn.setId(apnID);
            apn.setApn(apnName);
            apn.setCurrent(current);
            apn.setType(apnType);
            if (cur == null) {
                return apn;
            }
            cur.close();
            return apn;
        } catch (Exception e) {
            if (cur != null) {
                cur.close();
            }
            return null;
        } catch (Throwable th) {
            if (cur != null) {
                cur.close();
            }
            throw th;
        }
    }

    public String getCurrentAPNId() {
        String apnId = "";
        Cursor cur = null;
        try {
            cur = this.resolver.query(CURRENT_APN_URI, null, null, null, null);
            if (cur == null || !cur.moveToFirst()) {
                if (cur != null) {
                    cur.close();
                }
                return "";
            }
            apnId = cur.getString(cur.getColumnIndex("_id"));
            if (cur != null) {
                cur.close();
            }
            return apnId;
        } catch (Exception e) {
            if (cur != null) {
                cur.close();
            }
            return "";
        } catch (Throwable th) {
            if (cur != null) {
                cur.close();
            }
            throw th;
        }
    }

    public String getCurrentAPNName() {
        String apnName = "";
        Cursor cur = null;
        try {
            cur = this.resolver.query(CURRENT_APN_URI, null, null, null, null);
            if (cur == null || !cur.moveToFirst()) {
                if (cur != null) {
                    cur.close();
                }
                return "";
            }
            apnName = cur.getString(cur.getColumnIndex("name"));
            if (cur != null) {
                cur.close();
            }
            return apnName;
        } catch (Exception e) {
            if (cur != null) {
                cur.close();
            }
            return "";
        } catch (Throwable th) {
            if (cur != null) {
                cur.close();
            }
            throw th;
        }
    }

    public List<ApnInfo> getWapAPNList() {
        Cursor cr = this.resolver.query(APN_LIST_URI, new String[]{"_id,apn,type,current"}, null, null, null);
        List<ApnInfo> list = new ArrayList();
        while (cr != null && cr.moveToNext()) {
            if (!(cr.getString(cr.getColumnIndex("apn")) == null || !cr.getString(cr.getColumnIndex("apn")).contains("wap") || "".equals(cr.getString(cr.getColumnIndex("current"))))) {
                ApnInfo a = new ApnInfo();
                a.setId(cr.getString(cr.getColumnIndex("_id")));
                a.setApn(cr.getString(cr.getColumnIndex("apn")));
                a.setType(cr.getString(cr.getColumnIndex("type")));
                a.setCurrent(cr.getString(cr.getColumnIndex("current")));
                list.add(a);
            }
        }
        if (cr != null) {
            cr.close();
        }
        return list;
    }

    public int addAPN(String name, String apn, String mcc, String numeric) {
        int id = -1;
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("apn", apn);
        values.put("mcc", mcc);
        values.put("mnc", numeric);
        values.put("numeric", numeric);
        Cursor c = null;
        Uri newRow = this.resolver.insert(APN_LIST_URI, values);
        if (newRow != null) {
            c = this.resolver.query(newRow, null, null, null, null);
            int idIndex = c.getColumnIndex("_id");
            c.moveToFirst();
            id = c.getShort(idIndex);
        }
        if (c != null) {
            c.close();
        }
        return id;
    }

    public String addKsoAPN() {
        String apn = "";
        if (getARS2() == 1) {
            apn = "cmnet";
        } else if (getARS2() == 2) {
            apn = "uninet";
        }
        if (getARS2() == 3) {
            apn = "ctnet";
        }
        String id = "";
        ContentValues values = new ContentValues();
        values.put("name", "czl");
        values.put("apn", apn);
        Cursor c = null;
        Uri newRow = this.resolver.insert(APN_LIST_URI, values);
        if (newRow != null) {
            c = this.resolver.query(newRow, null, null, null, null);
            c.moveToFirst();
            id = c.getString(c.getColumnIndex("_id"));
        }
        if (c != null) {
            c.close();
        }
        return id;
    }

    public void setAPN(String id) {
        ContentValues values = new ContentValues();
        values.put("apn_id", id);
        this.resolver.update(CURRENT_APN_URI, values, null, null);
    }

    public String getAPNType() {
        return this.info.getExtraInfo();
    }

    /* access modifiers changed from: 0000 */
    public boolean getMobileDataStatus() {
        try {
            Field iConMgrField = Class.forName(this.conManager.getClass().getName()).getDeclaredField("mService");
            iConMgrField.setAccessible(true);
            Object iConMgr = iConMgrField.get(this.conManager);
            Method getMobileDataEnabledMethod = Class.forName(iConMgr.getClass().getName()).getDeclaredMethod("getMobileDataEnabled", new Class[0]);
            getMobileDataEnabledMethod.setAccessible(true);
            return ((Boolean) getMobileDataEnabledMethod.invoke(iConMgr, new Object[0])).booleanValue();
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            return false;
        }
    }

    /* access modifiers changed from: 0000 */
    public void toggleMobileData(boolean enabled) {
        try {
            Field iConMgrField = Class.forName(this.conManager.getClass().getName()).getDeclaredField("mService");
            iConMgrField.setAccessible(true);
            Object iConMgr = iConMgrField.get(this.conManager);
            Method setMobileDataEnabledMethod = Class.forName(iConMgr.getClass().getName()).getDeclaredMethod("setMobileDataEnabled", new Class[]{Boolean.TYPE});
            setMobileDataEnabledMethod.setAccessible(true);
            setMobileDataEnabledMethod.invoke(iConMgr, new Object[]{Boolean.valueOf(enabled)});
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
        }
    }

    public void matchApn() {
        if (getARS2() == 1) {
            WapApnName.cwap = "cmwap";
            WapApnName.gwap = "3gwap";
        } else if (getARS2() == 2) {
            WapApnName.cwap = "uniwap";
            WapApnName.gwap = "3gwap";
        }
        if (getARS2() == 3) {
            WapApnName.cwap = "ctwap";
            WapApnName.gwap = "3gwap";
        }
    }

    public int getARS2() {
        String imsi = this.telephonyManager.getSubscriberId();
        if (imsi != null) {
            if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
                return 1;
            }
            if (imsi.startsWith("46001")) {
                return 2;
            }
            if (imsi.startsWith("46003")) {
                return 3;
            }
        }
        return 0;
    }

    public boolean checkNet() {
        if (this.info != null && this.info.isConnected() && this.info.getState() == State.CONNECTED) {
            return true;
        }
        return false;
    }

    public String getNetworkType() {
        State state = this.conManager.getNetworkInfo(1).getState();
        if (state == State.CONNECTED || state == State.CONNECTING) {
            return "wifi";
        }
        state = this.conManager.getNetworkInfo(0).getState();
        if (state == State.CONNECTED || state == State.CONNECTING) {
            return "mobile";
        }
        return "none";
    }

    public boolean checkWifi() {
        return this.conManager.getNetworkInfo(1).isAvailable();
    }

    public boolean chckMobile() {
        return this.conManager.getNetworkInfo(0).isAvailable();
    }

    public static boolean gprsEnable(boolean bEnable, Context context) {
        boolean isOpen = gprsIsOpenMethod("getMobileDataEnabled");
        setGprsEnable("setMobileDataEnabled", bEnable);
        return isOpen;
    }

    private static boolean gprsIsOpenMethod(String methodName) {
        Class cmClass = mCM.getClass();
        Boolean isOpen = Boolean.valueOf(false);
        try {
            isOpen = (Boolean) cmClass.getMethod(methodName, null).invoke(mCM, null);
        } catch (Exception e) {
        }
        return isOpen.booleanValue();
    }

    private static void setGprsEnable(String methodName, boolean isEnable) {
        try {
            mCM.getClass().getMethod(methodName, new Class[]{Boolean.TYPE}).invoke(mCM, new Object[]{Boolean.valueOf(isEnable)});
        } catch (Exception e) {
        }
    }

    private static void setDataConnection(Context context, boolean flag) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (telephonyManager.getDataState() == 2) {
        }
        try {
            Method dataConnSwitchmethod;
            Method getITelephonyMethod = Class.forName(telephonyManager.getClass().getName()).getDeclaredMethod("getITelephony", new Class[0]);
            getITelephonyMethod.setAccessible(true);
            Object ITelephonyStub = getITelephonyMethod.invoke(telephonyManager, new Object[0]);
            Class ITelephonyClass = Class.forName(ITelephonyStub.getClass().getName());
            if (flag) {
                dataConnSwitchmethod = ITelephonyClass.getDeclaredMethod("enableDataConnectivity", new Class[0]);
            } else {
                dataConnSwitchmethod = ITelephonyClass.getDeclaredMethod("disableDataConnectivity", new Class[0]);
            }
            dataConnSwitchmethod.setAccessible(true);
            dataConnSwitchmethod.invoke(ITelephonyStub, new Object[0]);
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
        }
    }
}
