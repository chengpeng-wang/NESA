package com.baidu.location;

import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import java.util.ArrayList;
import java.util.Iterator;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class a {
    /* access modifiers changed from: private|static */
    /* renamed from: if */
    public static String f58if = f.v;
    private boolean a = false;
    /* renamed from: do */
    private ArrayList f59do = null;
    /* renamed from: for */
    private boolean f60for = false;
    /* renamed from: int */
    private Handler f61int = null;

    private class a {
        /* renamed from: do */
        public LocationClientOption f54do = new LocationClientOption();
        /* renamed from: for */
        public Messenger f55for = null;
        /* renamed from: if */
        public int f56if = 0;
        /* renamed from: int */
        public String f57int = null;

        public a(Message message) {
            this.f55for = message.replyTo;
            this.f57int = message.getData().getString("packName");
            this.f54do.f51new = message.getData().getString("prodName");
            this.f54do.f52try = message.getData().getString("coorType");
            this.f54do.f43char = message.getData().getString("addrType");
            j.j = this.f54do.f43char;
            this.f54do.f42case = message.getData().getBoolean("openGPS");
            this.f54do.f49int = message.getData().getInt("scanSpan");
            this.f54do.f50long = message.getData().getInt("timeOut");
            this.f54do.f47goto = message.getData().getInt("priority");
            this.f54do.f53void = message.getData().getBoolean("location_change_notify");
        }

        /* access modifiers changed from: private */
        public void a(int i) {
            Message obtain = Message.obtain(null, i);
            try {
                if (this.f55for != null) {
                    this.f55for.send(obtain);
                }
                this.f56if = 0;
            } catch (Exception e) {
                if (e instanceof DeadObjectException) {
                    this.f56if++;
                }
            }
        }

        private void a(int i, String str, String str2) {
            Bundle bundle = new Bundle();
            bundle.putString(str, str2);
            Message obtain = Message.obtain(null, i);
            obtain.setData(bundle);
            try {
                if (this.f55for != null) {
                    this.f55for.send(obtain);
                }
                this.f56if = 0;
            } catch (Exception e) {
                if (e instanceof DeadObjectException) {
                    this.f56if++;
                }
            }
        }

        public void a() {
            a(23);
        }

        public void a(String str) {
            if (this.f54do.f53void) {
                m714if(str);
            }
        }

        public void a(String str, int i) {
            int i2 = 0;
            j.a(a.f58if, "decode...");
            if (str != null) {
                if (j.v) {
                    a(i, "locStr", str);
                    return;
                }
                if (i == 21) {
                    a(27, "locStr", str);
                }
                if (!(this.f54do.f52try == null || this.f54do.f52try.equals("gcj02"))) {
                    double d = j.m969do(str, "x\":\"", "\"");
                    double d2 = j.m969do(str, "y\":\"", "\"");
                    j.a(a.f58if, "decode..." + d + ":" + d2);
                    if (!(d == Double.MIN_VALUE || d2 == Double.MIN_VALUE)) {
                        double[] dArr = Jni.m674if(d, d2, this.f54do.f52try);
                        str = j.a(j.a(str, "x\":\"", "\"", dArr[0]), "y\":\"", "\"", dArr[1]);
                        j.a(a.f58if, "decode2 ..." + dArr[0] + ":" + dArr[1]);
                        j.a(a.f58if, "decode3 ..." + str);
                    }
                    if (this.f54do.f45else) {
                        try {
                            JSONObject jSONObject = new JSONObject(str);
                            JSONObject jSONObject2 = jSONObject.getJSONObject(Form.TYPE_RESULT);
                            JSONObject jSONObject3 = jSONObject.getJSONObject("content");
                            if (Integer.parseInt(jSONObject2.getString("error")) == BDLocation.TypeNetWorkLocation) {
                                jSONObject2 = jSONObject3.getJSONObject("poi");
                                JSONArray jSONArray = jSONObject2.getJSONArray("p");
                                while (i2 < jSONArray.length()) {
                                    JSONObject jSONObject4 = jSONArray.getJSONObject(i2);
                                    double parseDouble = Double.parseDouble(jSONObject4.getString(GroupChatInvitation.ELEMENT_NAME));
                                    double parseDouble2 = Double.parseDouble(jSONObject4.getString("y"));
                                    if (!(parseDouble == Double.MIN_VALUE || parseDouble2 == Double.MIN_VALUE)) {
                                        double[] dArr2 = Jni.m674if(parseDouble, parseDouble2, this.f54do.f52try);
                                        jSONObject4.put(GroupChatInvitation.ELEMENT_NAME, String.valueOf(dArr2[0]));
                                        jSONObject4.put("y", String.valueOf(dArr2[1]));
                                        jSONArray.put(i2, jSONObject4);
                                        i2++;
                                    }
                                }
                                jSONObject2.put("p", jSONArray);
                                jSONObject3.put("poi", jSONObject2);
                                jSONObject.put("content", jSONObject3);
                                str = jSONObject.toString();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                a(i, "locStr", str);
            }
        }

        /* renamed from: if */
        public void m713if() {
            if (!this.f54do.f53void) {
                return;
            }
            if (j.b) {
                a(54);
            } else {
                a(55);
            }
        }

        /* renamed from: if */
        public void m714if(String str) {
            if (str != null) {
                if (j.v) {
                    a(21, "locStr", str);
                    return;
                }
                a(27, "locStr", str);
                if (!(this.f54do.f52try == null || this.f54do.f52try.equals("gcj02"))) {
                    double d = j.m969do(str, "x\":\"", "\"");
                    double d2 = j.m969do(str, "y\":\"", "\"");
                    if (!(d == Double.MIN_VALUE || d2 == Double.MIN_VALUE)) {
                        double[] dArr = Jni.m674if(d, d2, this.f54do.f52try);
                        if (dArr[0] > 0.0d || dArr[1] > 0.0d) {
                            str = j.a(j.a(str, "x\":\"", "\"", dArr[0]), "y\":\"", "\"", dArr[1]);
                        }
                    }
                }
                a(21, "locStr", str);
            }
        }
    }

    public a(Handler handler) {
        this.f61int = handler;
        this.f59do = new ArrayList();
    }

    private a a(Messenger messenger) {
        if (this.f59do == null) {
            return null;
        }
        Iterator it = this.f59do.iterator();
        while (it.hasNext()) {
            a aVar = (a) it.next();
            if (aVar.f55for.equals(messenger)) {
                return aVar;
            }
        }
        return null;
    }

    private void a() {
        boolean z;
        boolean z2 = false;
        Iterator it = this.f59do.iterator();
        while (true) {
            z = z2;
            if (!it.hasNext()) {
                break;
            }
            z2 = ((a) it.next()).f54do.f51new.equals("kuikedefancaiburudashahaochi") ? true : z;
        }
        if (this.a != z) {
            this.a = z;
            this.f61int.obtainMessage(81).sendToTarget();
        }
    }

    private void a(a aVar) {
        if (aVar != null) {
            if (a(aVar.f55for) != null) {
                aVar.a(14);
                return;
            }
            this.f59do.add(aVar);
            j.a(f58if, aVar.f57int + " registered ");
            aVar.a(13);
        }
    }

    /* renamed from: do */
    private void m717do() {
        m718int();
        a();
        m728new();
    }

    /* renamed from: int */
    private void m718int() {
        Iterator it = this.f59do.iterator();
        boolean z = false;
        boolean z2 = false;
        while (it.hasNext()) {
            a aVar = (a) it.next();
            if (aVar.f54do.f42case) {
                z2 = true;
            }
            z = aVar.f54do.f53void ? true : z;
        }
        j.I = z;
        if (this.f60for != z2) {
            this.f60for = z2;
            this.f61int.obtainMessage(52).sendToTarget();
        }
    }

    public String a(Message message) {
        if (message == null || message.replyTo == null) {
            j.a(f58if, "invalid Poirequest");
            return null;
        }
        a a = a(message.replyTo);
        if (a == null) {
            return null;
        }
        a.f54do.a = message.getData().getInt("num", a.f54do.a);
        a.f54do.f44do = message.getData().getFloat("distance", a.f54do.f44do);
        a.f54do.f48if = message.getData().getBoolean("extraInfo", a.f54do.f48if);
        a.f54do.f45else = true;
        String format = String.format("&poi=%.1f|%d", new Object[]{Float.valueOf(a.f54do.f44do), Integer.valueOf(a.f54do.a)});
        return a.f54do.f48if ? format + "|1" : format;
    }

    public void a(String str) {
        a aVar;
        ArrayList arrayList = new ArrayList();
        Iterator it = this.f59do.iterator();
        while (it.hasNext()) {
            aVar = (a) it.next();
            aVar.m714if(str);
            if (aVar.f56if > 4) {
                arrayList.add(aVar);
            }
        }
        if (arrayList != null && arrayList.size() > 0) {
            Iterator it2 = arrayList.iterator();
            while (it2.hasNext()) {
                aVar = (a) it2.next();
                j.a(f58if, "remove dead object...");
                this.f59do.remove(aVar);
            }
        }
    }

    public void a(String str, int i) {
        a aVar;
        ArrayList arrayList = new ArrayList();
        Iterator it = this.f59do.iterator();
        while (it.hasNext()) {
            aVar = (a) it.next();
            aVar.a(str, i);
            if (aVar.f56if > 4) {
                arrayList.add(aVar);
            }
        }
        if (arrayList != null && arrayList.size() > 0) {
            Iterator it2 = arrayList.iterator();
            while (it2.hasNext()) {
                aVar = (a) it2.next();
                j.a(f58if, "remove dead object...");
                this.f59do.remove(aVar);
            }
        }
    }

    public void a(String str, Message message) {
        if (str != null && message != null) {
            a a = a(message.replyTo);
            if (a != null) {
                a.m714if(str);
                if (a.f56if > 4) {
                    this.f59do.remove(a);
                    return;
                }
                return;
            }
            j.a(f58if, "not found the client messener...");
        }
    }

    /* renamed from: byte */
    public String m720byte() {
        StringBuffer stringBuffer = new StringBuffer(256);
        a aVar = (a) this.f59do.get(0);
        if (aVar.f54do.f51new != null) {
            stringBuffer.append(aVar.f54do.f51new);
        }
        if (aVar.f57int != null) {
            stringBuffer.append(":");
            stringBuffer.append(aVar.f57int);
            stringBuffer.append("|");
        }
        String stringBuffer2 = stringBuffer.toString();
        return (stringBuffer2 == null || stringBuffer2.equals("")) ? null : "&prod=" + stringBuffer2;
    }

    /* renamed from: do */
    public int m721do(Message message) {
        if (message == null || message.replyTo == null) {
            return 1;
        }
        a a = a(message.replyTo);
        return (a == null || a.f54do == null) ? 1 : a.f54do.f47goto;
    }

    /* renamed from: for */
    public boolean m722for() {
        return this.f60for;
    }

    /* renamed from: for */
    public boolean m723for(Message message) {
        boolean z = true;
        a a = a(message.replyTo);
        if (a == null) {
            return false;
        }
        int i = a.f54do.f49int;
        a.f54do.f49int = message.getData().getInt("scanSpan", a.f54do.f49int);
        if (a.f54do.f49int < LocationClientOption.MIN_SCAN_SPAN) {
            j.J = false;
        } else {
            j.J = true;
        }
        if (a.f54do.f49int <= 999 || i >= LocationClientOption.MIN_SCAN_SPAN) {
            z = false;
        }
        a.f54do.f42case = message.getData().getBoolean("openGPS", a.f54do.f42case);
        String string = message.getData().getString("coorType");
        LocationClientOption locationClientOption = a.f54do;
        if (string == null || string.equals("")) {
            string = a.f54do.f52try;
        }
        locationClientOption.f52try = string;
        string = message.getData().getString("addrType");
        locationClientOption = a.f54do;
        if (string == null || string.equals("")) {
            string = a.f54do.f43char;
        }
        locationClientOption.f43char = string;
        j.j = a.f54do.f43char;
        a.f54do.f50long = message.getData().getInt("timeOut", a.f54do.f50long);
        a.f54do.f53void = message.getData().getBoolean("location_change_notify", a.f54do.f53void);
        a.f54do.f47goto = message.getData().getInt("priority", a.f54do.f47goto);
        m717do();
        return z;
    }

    /* renamed from: if */
    public void m724if() {
        Iterator it = this.f59do.iterator();
        while (it.hasNext()) {
            ((a) it.next()).a();
        }
    }

    /* renamed from: if */
    public void m725if(Message message) {
        a a = a(message.replyTo);
        if (a != null) {
            j.a(f58if, a.f57int + " unregistered");
            this.f59do.remove(a);
        }
        m717do();
    }

    /* renamed from: if */
    public void m726if(String str) {
        Iterator it = this.f59do.iterator();
        while (it.hasNext()) {
            ((a) it.next()).a(str);
        }
    }

    /* renamed from: int */
    public void m727int(Message message) {
        if (message == null || message.replyTo == null) {
            j.a(f58if, "invalid regist client");
            return;
        }
        a(new a(message));
        m717do();
    }

    /* renamed from: new */
    public void m728new() {
        Iterator it = this.f59do.iterator();
        while (it.hasNext()) {
            ((a) it.next()).m713if();
        }
    }
}
