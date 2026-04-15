package com.feedback.b;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.feedback.a.d;
import com.feedback.a.e;
import com.mobclick.android.UmengConstants;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class c {
    public static synchronized String a(Context context, JSONArray jSONArray) {
        String str;
        Exception e;
        String str2;
        synchronized (c.class) {
            if (jSONArray.length() == 0) {
                str = "";
            } else {
                String str3 = "";
                for (int i = 0; i < jSONArray.length(); i++) {
                    try {
                        JSONArray jSONArray2 = jSONArray.getJSONArray(i);
                        String str4 = str3;
                        int i2 = 0;
                        while (i2 < jSONArray2.length()) {
                            try {
                                if (!jSONArray2.getString(i2).equals("end")) {
                                    JSONObject jSONObject = jSONArray2.getJSONObject(i2);
                                    if (UmengConstants.Atom_Type_DevReply.equalsIgnoreCase(jSONObject.optString(UmengConstants.AtomKey_Type)) && a(context, jSONObject)) {
                                        str4 = c(context, b.a(jSONObject, UmengConstants.AtomKey_FeedbackID));
                                    }
                                }
                                i2++;
                            } catch (Exception e2) {
                                e = e2;
                                str2 = str4;
                                e.printStackTrace();
                                str3 = str2;
                            }
                        }
                        str3 = str4;
                    } catch (Exception e3) {
                        Exception exception = e3;
                        str2 = str3;
                        e = exception;
                        e.printStackTrace();
                        str3 = str2;
                    }
                }
                str = str3;
            }
        }
        return str;
    }

    public static synchronized List a(Context context) {
        ArrayList arrayList;
        synchronized (c.class) {
            arrayList = new ArrayList();
            try {
                SharedPreferences sharedPreferences = context.getSharedPreferences(UmengConstants.FeedbackPreName, 0);
                SharedPreferences sharedPreferences2 = context.getSharedPreferences(UmengConstants.TempPreName, 0);
                SharedPreferences sharedPreferences3 = context.getSharedPreferences("fail", 0);
                for (String jSONArray : sharedPreferences.getAll().values()) {
                    arrayList.add(new d(new JSONArray(jSONArray)));
                }
                for (String jSONArray2 : sharedPreferences2.getAll().values()) {
                    arrayList.add(new d(new JSONObject(jSONArray2)));
                }
                for (String jSONArray22 : sharedPreferences3.getAll().values()) {
                    arrayList.add(new d(new JSONObject(jSONArray22)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }

    public static synchronized void a(Context context, d dVar, int i) {
        synchronized (c.class) {
            if (dVar.b == e.Other) {
                SharedPreferences sharedPreferences = context.getSharedPreferences(UmengConstants.FeedbackPreName, 0);
                Editor edit = sharedPreferences.edit();
                String str = dVar.c;
                String string = sharedPreferences.getString(str, null);
                try {
                    JSONArray jSONArray = new JSONArray();
                    JSONArray jSONArray2 = new JSONArray(string);
                    if (jSONArray2.length() == 1) {
                        edit.remove(dVar.c);
                    } else {
                        for (int i2 = 0; i2 <= jSONArray2.length() - 1; i2++) {
                            if (i2 != i) {
                                jSONArray.put(jSONArray2.getJSONObject(i2));
                            }
                        }
                        edit.putString(str, jSONArray.toString());
                    }
                    edit.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                (dVar.b == e.PureFail ? context.getSharedPreferences("fail", 0) : context.getSharedPreferences(UmengConstants.TempPreName, 0)).edit().remove(dVar.c).commit();
            }
            dVar.b(i);
        }
        return;
    }

    public static synchronized void a(Context context, String str) {
        synchronized (c.class) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(UmengConstants.PreName_Trivial, 0);
            if (!d.a(str)) {
                a(sharedPreferences, UmengConstants.TrivialPreKey_newreplyIds, sharedPreferences.getString(UmengConstants.TrivialPreKey_newreplyIds, "").replaceFirst(str, ""));
            }
        }
    }

    public static synchronized void a(Context context, String str, String str2) {
        synchronized (c.class) {
            context.getSharedPreferences(str, 0).edit().remove(str2).commit();
        }
    }

    public static synchronized void a(Context context, String str, boolean z) {
        synchronized (c.class) {
            if (z) {
                context.getSharedPreferences(UmengConstants.FeedbackPreName, 0).edit().remove(str).commit();
            } else {
                context.getSharedPreferences(UmengConstants.TempPreName, 0).edit().remove(str).commit();
                context.getSharedPreferences("fail", 0).edit().remove(str).commit();
            }
        }
    }

    static void a(SharedPreferences sharedPreferences, String str, String str2) {
        sharedPreferences.edit().putString(str, str2).commit();
    }

    public static boolean a(Context context, d dVar) {
        return context.getSharedPreferences(UmengConstants.PreName_Trivial, 0).getString(UmengConstants.TrivialPreKey_newreplyIds, "").contains(dVar.c);
    }

    public static synchronized boolean a(Context context, JSONObject jSONObject) {
        boolean z;
        synchronized (c.class) {
            String a = b.a(jSONObject, UmengConstants.AtomKey_FeedbackID);
            SharedPreferences sharedPreferences = context.getSharedPreferences(UmengConstants.FeedbackPreName, 0);
            if (sharedPreferences.contains(a)) {
                try {
                    JSONArray jSONArray = new JSONArray(sharedPreferences.getString(a, null));
                    String a2;
                    if (UmengConstants.Atom_Type_UserReply.equals(b.a(jSONObject, UmengConstants.AtomKey_Type))) {
                        int i;
                        String a3 = b.a(jSONObject, UmengConstants.AtomKey_SequenceNum);
                        for (int length = jSONArray.length() - 1; length >= 0; length--) {
                            a2 = b.a(jSONArray.getJSONObject(length), UmengConstants.AtomKey_SequenceNum);
                            if (!d.a(a2) && a3.equals(a2)) {
                                jSONArray.put(length, jSONObject);
                                i = 1;
                                break;
                            }
                        }
                        i = 0;
                        if (i == 0) {
                            jSONArray.put(jSONObject);
                        }
                        a(sharedPreferences, a, jSONArray.toString());
                        z = i == 0;
                    } else {
                        SharedPreferences sharedPreferences2 = context.getSharedPreferences(UmengConstants.PreName_ReplyId, 0);
                        String string = sharedPreferences2.getString(a, "RP0");
                        a2 = b.a(jSONObject, "reply_id");
                        if (!d.a(string, a2)) {
                            jSONArray.put(jSONObject);
                            a(sharedPreferences, a, jSONArray.toString());
                            a(sharedPreferences2, a, a2);
                            a(context.getSharedPreferences(UmengConstants.PreName_Trivial, 0), UmengConstants.TrivialPreKey_MaxReplyID, a2);
                            z = true;
                        }
                        z = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                a(sharedPreferences, a, "[" + jSONObject.toString() + "]");
                z = true;
            }
        }
        return z;
    }

    public static synchronized d b(Context context, String str) {
        d dVar;
        synchronized (c.class) {
            try {
                dVar = new d(new JSONArray(context.getSharedPreferences(UmengConstants.FeedbackPreName, 0).getString(str, null)));
            } catch (Exception e) {
                e.printStackTrace();
                dVar = null;
            }
        }
        return dVar;
    }

    public static synchronized d b(Context context, String str, String str2) {
        d dVar;
        synchronized (c.class) {
            try {
                dVar = new d(new JSONObject(context.getSharedPreferences(str2, 0).getString(str, null)));
            } catch (Exception e) {
                e.printStackTrace();
                dVar = null;
            }
        }
        return dVar;
    }

    public static synchronized void b(Context context, JSONObject jSONObject) {
        synchronized (c.class) {
            b.e(jSONObject);
            if (UmengConstants.Atom_Type_NewFeedback.equals(jSONObject.optString(UmengConstants.AtomKey_Type))) {
                a(context.getSharedPreferences(UmengConstants.TempPreName, 0), b.a(jSONObject, UmengConstants.AtomKey_SequenceNum), jSONObject.toString());
            } else {
                String a = b.a(jSONObject, UmengConstants.AtomKey_FeedbackID);
                SharedPreferences sharedPreferences = context.getSharedPreferences(UmengConstants.FeedbackPreName, 0);
                try {
                    JSONArray jSONArray = new JSONArray(sharedPreferences.getString(a, null));
                    jSONArray.put(jSONObject);
                    a(sharedPreferences, a, jSONArray.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return;
    }

    private static synchronized String c(Context context, String str) {
        String str2;
        synchronized (c.class) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(UmengConstants.PreName_Trivial, 0);
            String string = sharedPreferences.getString(UmengConstants.TrivialPreKey_newreplyIds, "");
            if (string.contains(str)) {
                str2 = string;
            } else {
                string = new StringBuilder(String.valueOf(string)).append(",").append(str).toString();
                a(sharedPreferences, UmengConstants.TrivialPreKey_newreplyIds, string);
                str2 = string;
            }
        }
        return str2;
    }

    public static void c(Context context, JSONObject jSONObject) {
        b.d(jSONObject);
        String a = b.a(jSONObject, UmengConstants.AtomKey_SequenceNum);
        if (UmengConstants.Atom_Type_NewFeedback.equals(jSONObject.optString(UmengConstants.AtomKey_Type))) {
            a(context.getSharedPreferences("fail", 0), a, jSONObject.toString());
            return;
        }
        a = b.a(jSONObject, UmengConstants.AtomKey_FeedbackID);
        SharedPreferences sharedPreferences = context.getSharedPreferences(UmengConstants.FeedbackPreName, 0);
        try {
            JSONArray jSONArray = new JSONArray(sharedPreferences.getString(a, null));
            jSONArray.put(jSONObject);
            a(sharedPreferences, a, jSONArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
