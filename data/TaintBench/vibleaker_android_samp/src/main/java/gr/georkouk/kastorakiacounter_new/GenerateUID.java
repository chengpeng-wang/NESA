package gr.georkouk.kastorakiacounter_new;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GenerateUID {
    Context context;

    public GenerateUID(Context c_) {
        this.context = c_;
    }

    public String getSavedUID() {
        return this.context.getSharedPreferences("Settings", 0).getString("UID", "");
    }

    public void updateSavedUID(String UID_) {
        Editor settings = this.context.getSharedPreferences("Settings", 0).edit();
        settings.putString("UID", UID_);
        settings.apply();
    }

    public String getDeviceDetails() {
        return (Build.MANUFACTURER + " - " + Build.MODEL + " - " + Build.DEVICE + " - " + Build.DISPLAY).replaceAll(" ", "%20");
    }

    public String generateUID() {
        String m_szImei;
        int i = 0;
        try {
            m_szImei = ((TelephonyManager) this.context.getSystemService("phone")).getDeviceId();
        } catch (Exception e) {
            m_szImei = "1234567890";
        }
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.DISPLAY.length() % 10) + (Build.HOST.length() % 10) + (Build.ID.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10) + (Build.TAGS.length() % 10) + (Build.TYPE.length() % 10) + (Build.USER.length() % 10);
        String m_szLongID = m_szImei + m_szDevIDShort + Secure.getString(this.context.getContentResolver(), "android_id");
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e2) {
            e2.printStackTrace();
        }
        m.update(m_szLongID.getBytes(), 0, m_szLongID.length());
        byte[] p_md5Data = m.digest();
        String m_szUniqueID = "";
        int length = p_md5Data.length;
        while (i < length) {
            int b = p_md5Data[i] & 255;
            if (b <= 15) {
                m_szUniqueID = m_szUniqueID + "0";
            }
            m_szUniqueID = m_szUniqueID + Integer.toHexString(b);
            i++;
        }
        return m_szUniqueID.toUpperCase();
    }

    public String generate() {
        String uid = generateUID();
        String savedUID = getSavedUID();
        if (savedUID.equalsIgnoreCase("") || savedUID.equalsIgnoreCase("0")) {
            updateSavedUID(uid);
        }
        return uid;
    }
}
