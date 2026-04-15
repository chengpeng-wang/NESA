package com.feedback.b;

import android.content.Context;
import android.content.SharedPreferences;
import com.mobclick.android.UmengConstants;
import com.mobclick.android.l;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

public class b {
    public static int a(JSONObject jSONObject) {
        if (jSONObject.has(UmengConstants.AtomKey_Type)) {
            try {
                String string = jSONObject.getString(UmengConstants.AtomKey_Type);
                if (string.equals(UmengConstants.Atom_Type_DevReply)) {
                    return 0;
                }
                if (string.equals(UmengConstants.Atom_Type_UserReply)) {
                    return 1;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public static synchronized String a(JSONObject jSONObject, String str) {
        String optString;
        synchronized (b.class) {
            optString = jSONObject.optString(str);
        }
        return optString;
    }

    public static synchronized JSONObject a(Context context, String str, int i, int i2) {
        JSONObject e;
        synchronized (b.class) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(UmengConstants.PreName_Trivial, 0);
            int i3 = sharedPreferences.getInt(UmengConstants.TrivialPreKey_MaxSeqenceNum, -1) + 1;
            sharedPreferences.edit().putInt(UmengConstants.TrivialPreKey_MaxSeqenceNum, i3).commit();
            e = l.e(context);
            try {
                e.put(UmengConstants.AtomKey_User_ID, e.getString("idmd5"));
                e.put(UmengConstants.AtomKey_Thread_Title, str);
                e.put(UmengConstants.AtomKey_Content, "Not supported on client yet");
                e.put(UmengConstants.AtomKey_Date, l.a(new Date()));
                e.put(UmengConstants.AtomKey_SequenceNum, i3);
                e.put(UmengConstants.AtomKey_Type, UmengConstants.Atom_Type_NewFeedback);
                JSONObject jSONObject = new JSONObject();
                jSONObject.put(UmengConstants.AtomKey_AgeGroup, i);
                switch (i2) {
                    case 1:
                        jSONObject.put(UmengConstants.AtomKey_Sex, "male");
                        break;
                    case 2:
                        jSONObject.put(UmengConstants.AtomKey_Sex, "female");
                        break;
                }
                e.put("userinfo", jSONObject);
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
        return e;
    }

    public static synchronized JSONObject a(Context context, String str, String str2) {
        JSONObject jSONObject;
        synchronized (b.class) {
            String b = l.b(context);
            String c = l.c(context);
            jSONObject = new JSONObject();
            SharedPreferences sharedPreferences = context.getSharedPreferences(UmengConstants.PreName_Trivial, 0);
            int i = sharedPreferences.getInt(UmengConstants.TrivialPreKey_MaxSeqenceNum, -1) + 1;
            sharedPreferences.edit().putInt(UmengConstants.TrivialPreKey_MaxSeqenceNum, i).commit();
            try {
                jSONObject.put(UmengConstants.AtomKey_Type, UmengConstants.Atom_Type_UserReply);
                jSONObject.put(UmengConstants.AtomKey_AppKey, b);
                jSONObject.put(UmengConstants.AtomKey_FeedbackID, str2);
                jSONObject.put(UmengConstants.AtomKey_Content, str);
                jSONObject.put(UmengConstants.AtomKey_User_ID, c);
                jSONObject.put(UmengConstants.AtomKey_Date, l.a(new Date()));
                jSONObject.put(UmengConstants.AtomKey_SequenceNum, i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jSONObject;
    }

    public static synchronized boolean a(JSONObject jSONObject, String str, String str2) {
        boolean z;
        synchronized (b.class) {
            try {
                jSONObject.put(str, str2);
                z = true;
            } catch (JSONException e) {
                e.printStackTrace();
                z = false;
            }
        }
        return z;
    }

    public static boolean b(JSONObject jSONObject) {
        if (jSONObject == null) {
            return false;
        }
        try {
            if (jSONObject.has(UmengConstants.AtomKey_State)) {
                if ("ok".equals(jSONObject.getString(UmengConstants.AtomKey_State))) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static synchronized boolean c(JSONObject jSONObject) {
        boolean z;
        synchronized (b.class) {
            if (jSONObject.has(UmengConstants.AtomKey_State)) {
                jSONObject.remove(UmengConstants.AtomKey_State);
                z = true;
            } else {
                z = false;
            }
        }
        return z;
    }

    public static synchronized boolean d(JSONObject jSONObject) {
        boolean a;
        synchronized (b.class) {
            a = a(jSONObject, UmengConstants.AtomKey_State, "fail");
        }
        return a;
    }

    public static synchronized boolean e(JSONObject jSONObject) {
        boolean a;
        synchronized (b.class) {
            a = a(jSONObject, UmengConstants.AtomKey_State, UmengConstants.TempState);
        }
        return a;
    }

    public static synchronized boolean f(JSONObject jSONObject) {
        boolean a;
        synchronized (b.class) {
            a = a(jSONObject, UmengConstants.AtomKey_State, "ok");
        }
        return a;
    }
}
