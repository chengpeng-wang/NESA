package com.baidu.location;

import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.json.JSONObject;

public final class BDLocation {
    public static final int TypeCacheLocation = 65;
    public static final int TypeCriteriaException = 62;
    public static final int TypeGpsLocation = 61;
    public static final int TypeNetWorkException = 63;
    public static final int TypeNetWorkLocation = 161;
    public static final int TypeNone = 0;
    public static final int TypeOffLineLocation = 66;
    public static final int TypeOffLineLocationFail = 67;
    public static final int TypeOffLineLocationNetworkFail = 68;
    public static final int TypeServerError = 167;
    private String a = null;
    private boolean b = false;
    /* renamed from: byte */
    private String f7byte = null;
    private boolean c = false;
    /* renamed from: case */
    private boolean f8case = false;
    /* renamed from: char */
    private float f9char = -1.0f;
    private String d = null;
    /* renamed from: do */
    private int f10do = -1;
    private boolean e = false;
    /* renamed from: else */
    private double f11else = Double.MIN_VALUE;
    private double f = Double.MIN_VALUE;
    /* renamed from: for */
    private double f12for = Double.MIN_VALUE;
    /* renamed from: goto */
    private String f13goto = null;
    /* renamed from: if */
    private int f14if = 0;
    /* renamed from: int */
    private boolean f15int = false;
    /* renamed from: long */
    private float f16long = 0.0f;
    public a mAddr = new a();
    public String mServerString = null;
    /* renamed from: new */
    private float f17new = 0.0f;
    /* renamed from: try */
    private boolean f18try = false;
    /* renamed from: void */
    private boolean f19void = false;

    public class a {
        /* renamed from: byte */
        public String f0byte = null;
        /* renamed from: do */
        public String f1do = null;
        /* renamed from: for */
        public String f2for = null;
        /* renamed from: if */
        public String f3if = null;
        /* renamed from: int */
        public String f4int = null;
        /* renamed from: new */
        public String f5new = null;
        /* renamed from: try */
        public String f6try = null;
    }

    public BDLocation(double d, double d2, float f) {
        this.f11else = d2;
        this.f12for = d;
        this.f16long = f;
        this.d = j.a();
    }

    public BDLocation(String str) {
        if (str != null && !str.equals("")) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                JSONObject jSONObject2 = jSONObject.getJSONObject(Form.TYPE_RESULT);
                int parseInt = Integer.parseInt(jSONObject2.getString("error"));
                setLocType(parseInt);
                setTime(jSONObject2.getString("time"));
                if (parseInt == 61) {
                    jSONObject = jSONObject.getJSONObject("content");
                    jSONObject2 = jSONObject.getJSONObject("point");
                    setLatitude(Double.parseDouble(jSONObject2.getString("y")));
                    setLongitude(Double.parseDouble(jSONObject2.getString(GroupChatInvitation.ELEMENT_NAME)));
                    setRadius(Float.parseFloat(jSONObject.getString("radius")));
                    setSpeed(Float.parseFloat(jSONObject.getString("s")));
                    setDerect(Float.parseFloat(jSONObject.getString("d")));
                    setSatelliteNumber(Integer.parseInt(jSONObject.getString("n")));
                } else if (parseInt == TypeNetWorkLocation) {
                    jSONObject2 = jSONObject.getJSONObject("content");
                    jSONObject = jSONObject2.getJSONObject("point");
                    setLatitude(Double.parseDouble(jSONObject.getString("y")));
                    setLongitude(Double.parseDouble(jSONObject.getString(GroupChatInvitation.ELEMENT_NAME)));
                    setRadius(Float.parseFloat(jSONObject2.getString("radius")));
                    if (jSONObject2.has("addr")) {
                        String string = jSONObject2.getString("addr");
                        this.mAddr.f6try = string;
                        j.a(f.v, string);
                        String[] split = string.split(",");
                        this.mAddr.f3if = split[0];
                        this.mAddr.f5new = split[1];
                        this.mAddr.f4int = split[2];
                        this.mAddr.f0byte = split[3];
                        this.mAddr.f1do = split[4];
                        this.mAddr.f2for = split[5];
                        if ((this.mAddr.f3if.contains("北京") && this.mAddr.f5new.contains("北京")) || ((this.mAddr.f3if.contains("上海") && this.mAddr.f5new.contains("上海")) || ((this.mAddr.f3if.contains("天津") && this.mAddr.f5new.contains("天津")) || (this.mAddr.f3if.contains("重庆") && this.mAddr.f5new.contains("重庆"))))) {
                            j.a(f.v, "true,beijing");
                            string = this.mAddr.f3if;
                        } else {
                            string = this.mAddr.f3if + this.mAddr.f5new;
                        }
                        this.mAddr.f6try = string + this.mAddr.f4int + this.mAddr.f0byte + this.mAddr.f1do;
                        this.f19void = true;
                    } else {
                        this.f19void = false;
                        setAddrStr(null);
                    }
                    if (jSONObject2.has("poi")) {
                        this.f8case = true;
                        this.f7byte = jSONObject2.getJSONObject("poi").toString();
                    }
                } else if (parseInt == 66 || parseInt == 68) {
                    jSONObject = jSONObject.getJSONObject("content");
                    jSONObject2 = jSONObject.getJSONObject("point");
                    setLatitude(Double.parseDouble(jSONObject2.getString("y")));
                    setLongitude(Double.parseDouble(jSONObject2.getString(GroupChatInvitation.ELEMENT_NAME)));
                    setRadius(Float.parseFloat(jSONObject.getString("radius")));
                    a(Boolean.valueOf(Boolean.parseBoolean(jSONObject.getString("isCellChanged"))));
                }
            } catch (Exception e) {
                e.printStackTrace();
                this.f14if = 0;
                this.f19void = false;
            }
        }
    }

    public BDLocation(String str, double d, double d2, float f, String str2, String str3) {
        this.d = str;
        this.f11else = d;
        this.f12for = d2;
        this.f16long = f;
        this.f13goto = str2;
        this.a = str3;
        this.d = j.a();
    }

    private void a(Boolean bool) {
        this.e = bool.booleanValue();
    }

    public String getAddrStr() {
        return this.mAddr.f6try;
    }

    public double getAltitude() {
        return this.f;
    }

    public String getCity() {
        return this.mAddr.f5new;
    }

    public String getCityCode() {
        return this.mAddr.f2for;
    }

    public String getCoorType() {
        return this.f13goto;
    }

    public float getDerect() {
        return this.f9char;
    }

    public String getDistrict() {
        return this.mAddr.f4int;
    }

    public double getLatitude() {
        return this.f11else;
    }

    public int getLocType() {
        return this.f14if;
    }

    public double getLongitude() {
        return this.f12for;
    }

    public String getPoi() {
        return this.f7byte;
    }

    public String getProvince() {
        return this.mAddr.f3if;
    }

    public float getRadius() {
        return this.f16long;
    }

    public int getSatelliteNumber() {
        this.b = true;
        return this.f10do;
    }

    public float getSpeed() {
        return this.f17new;
    }

    public String getStreet() {
        return this.mAddr.f0byte;
    }

    public String getStreetNumber() {
        return this.mAddr.f1do;
    }

    public String getTime() {
        return this.d;
    }

    public boolean hasAddr() {
        return this.f19void;
    }

    public boolean hasAltitude() {
        return this.c;
    }

    public boolean hasPoi() {
        return this.f8case;
    }

    public boolean hasRadius() {
        return this.f18try;
    }

    public boolean hasSateNumber() {
        return this.b;
    }

    public boolean hasSpeed() {
        return this.f15int;
    }

    public boolean isCellChangeFlag() {
        return this.e;
    }

    public void setAddrStr(String str) {
        this.a = str;
        this.f19void = true;
    }

    public void setAltitude(double d) {
        this.f = d;
        this.c = true;
    }

    public void setCoorType(String str) {
        this.f13goto = str;
    }

    public void setDerect(float f) {
        this.f9char = f;
    }

    public void setLatitude(double d) {
        this.f11else = d;
    }

    public void setLocType(int i) {
        this.f14if = i;
    }

    public void setLongitude(double d) {
        this.f12for = d;
    }

    public void setRadius(float f) {
        this.f16long = f;
        this.f18try = true;
    }

    public void setSatelliteNumber(int i) {
        this.f10do = i;
    }

    public void setSpeed(float f) {
        this.f17new = f;
        this.f15int = true;
    }

    public void setTime(String str) {
        this.d = str;
    }

    public String toJsonString() {
        return null;
    }

    public BDLocation toNewLocation(String str) {
        return null;
    }
}
