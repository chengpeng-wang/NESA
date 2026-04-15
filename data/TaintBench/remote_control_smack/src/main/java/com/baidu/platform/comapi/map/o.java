package com.baidu.platform.comapi.map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.TimeUtils;
import android.util.FloatMath;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewConfiguration;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.d.c;
import com.baidu.platform.comjni.map.basemap.BaseMapCallback;
import com.baidu.platform.comjni.tools.JNITools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class o implements OnKeyListener {
    private static int A;
    private static int B;
    private static boolean C;
    private static boolean D;
    private static a E = new a();
    private static int F;
    private static int G;
    private static boolean H;
    private static boolean I;
    private static boolean J;
    private static VelocityTracker K;
    private static long L;
    private static long M;
    private static long N;
    private static long O;
    private static int P = 0;
    private static float Q;
    private static float R;
    private static boolean S;
    private static long T;
    private static long U;
    private static long W = 400;
    private static long X = 500;
    private static long Y = 120;
    public static boolean i = true;
    private static final int p = (ViewConfiguration.getMinimumFlingVelocity() * 3);
    private static int y;
    private static long z;
    private boolean V = false;
    private boolean Z = true;
    public int a = 0;
    private boolean aa = true;
    private GeoPoint ab;
    private boolean ac;
    private int ad;
    private int ae;
    /* access modifiers changed from: private */
    public boolean af = false;
    /* access modifiers changed from: private */
    public boolean ag = false;
    public int b = 0;
    public int c = 0;
    public int d = 0;
    public Map<String, Integer> e = new HashMap();
    public Map<String, Integer> f = new HashMap();
    public Map<String, Integer> g = new HashMap();
    public Map<String, Integer> h = new HashMap();
    private com.baidu.platform.comjni.map.basemap.a j = null;
    private Context k = null;
    /* access modifiers changed from: private */
    public q l = null;
    private int m = 0;
    private Bundle n = new Bundle();
    private Handler o = null;
    private boolean q = true;
    private boolean r = false;
    private boolean s = true;
    private boolean t = true;
    private boolean u = true;
    private int v;
    private int w;
    private int x = 20;

    static class a {
        final int a = 2;
        float b;
        float c;
        float d;
        float e;
        float f;
        float g;
        float h;
        float i;
        boolean j;
        float k;
        float l;
        double m;

        a() {
        }
    }

    @SuppressLint({"HandlerLeak"})
    public o(Context context, q qVar) {
        this.l = qVar;
        this.l.setOnKeyListener(this);
        Display defaultDisplay = ((Activity) context).getWindowManager().getDefaultDisplay();
        this.v = defaultDisplay.getWidth();
        this.w = defaultDisplay.getHeight();
        this.o = new p(this);
        p();
    }

    public static int a(int i, int i2, int i3, int i4) {
        return com.baidu.platform.comjni.map.basemap.a.b(i, i2, i3, i4);
    }

    public static void f() {
        y = 0;
        z = 0;
        A = 0;
        B = 0;
        C = false;
        D = false;
        E.j = false;
        E.m = 0.0d;
        G = 0;
        F = 0;
        H = false;
        I = false;
        J = false;
    }

    private void f(MotionEvent motionEvent) {
        if (!E.j) {
            U = motionEvent.getDownTime();
            if (U - T >= W) {
                T = U;
            } else if (Math.abs(motionEvent.getX() - Q) >= ((float) Y) || Math.abs(motionEvent.getY() - R) >= ((float) Y)) {
                T = U;
            } else {
                e(motionEvent);
                T = 0;
            }
            Q = motionEvent.getX();
            R = motionEvent.getY();
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
            a(4, 0, (y << 16) | x);
            g(x, y);
            S = true;
        }
    }

    /* access modifiers changed from: private */
    public void n() {
        if (this.ac) {
            Point point = new Point();
            this.l.d().toPixels(this.ab, point);
            f(point.x - this.ad, point.y - this.ae);
            this.ac = false;
        }
        this.af = false;
        this.ad = 0;
        this.ae = 0;
        this.ab = null;
    }

    /* access modifiers changed from: private */
    public void o() {
        if (this.ac) {
            Point point = new Point();
            this.l.d().toPixels(this.ab, point);
            f(point.x - this.ad, point.y - this.ae);
            this.ac = false;
        }
        this.ag = false;
        this.ad = 0;
        this.ae = 0;
        this.ab = null;
    }

    private void p() {
        com.baidu.platform.comjni.engine.a.a(4000, this.o);
        com.baidu.platform.comjni.engine.a.a(39, this.o);
        com.baidu.platform.comjni.engine.a.a(512, this.o);
    }

    private void q() {
        com.baidu.platform.comjni.engine.a.b(4000, this.o);
        com.baidu.platform.comjni.engine.a.b(39, this.o);
        com.baidu.platform.comjni.engine.a.b(512, this.o);
    }

    /* access modifiers changed from: 0000 */
    public int a() {
        return this.m;
    }

    public int a(int i, int i2, int i3) {
        return a(this.m, i, i2, i3);
    }

    /* access modifiers changed from: 0000 */
    public void a(int i, int i2) {
        this.j.c(i, i2);
    }

    public void a(Bundle bundle, w wVar) {
        if (bundle == null) {
            throw new IllegalArgumentException("IllegalArgument");
        }
        if (this.j == null) {
            this.j = new com.baidu.platform.comjni.map.basemap.a();
            this.j.a();
            this.m = this.j.c();
        }
        if (wVar != null) {
            this.j.a((BaseMapCallback) wVar);
        }
        Object obj = null;
        if (c.m() >= 180) {
            obj = 1;
        }
        if (c.m() < 160) {
            this.x = 18;
        } else if (c.m() < 240) {
            this.x = 25;
        } else if (c.m() < 320) {
            this.x = 37;
        } else {
            this.x = 50;
        }
        String string = bundle.getString("modulePath");
        String string2 = bundle.getString("appSdcardPath");
        String string3 = bundle.getString("appCachePath");
        String string4 = bundle.getString("appSecondCachePath");
        int i = bundle.getInt("mapTmpMax");
        int i2 = bundle.getInt("domTmpMax");
        int i3 = bundle.getInt("itsTmpMax");
        String str = string + "/cfg/h/";
        String str2 = string + "/cfg/h/";
        String str3 = string2 + "/vmp/h/";
        String str4 = string2 + "/vmp/h/";
        string3 = string3 + "/tmp/";
        string4 = string4 + "/tmp/";
        if (obj == null) {
            str = string + "/cfg/l/";
            str2 = string + "/cfg/l/";
            str3 = string2 + "/vmp/l/";
            str4 = string2 + "/vmp/l/";
        }
        this.j.a(str, str3, string3, string4, str4, str2, this.v, this.w, c.m(), i, i2, i3);
        this.j.e();
    }

    public void a(GeoPoint geoPoint) {
        this.l.a(geoPoint, null, null);
    }

    public void a(GeoPoint geoPoint, Message message) {
        this.l.a(geoPoint, message, null);
    }

    public void a(q qVar) {
        this.l = qVar;
        this.l.setOnKeyListener(this);
    }

    public void a(t tVar) {
        int i = 0;
        if (this.j != null) {
            this.n.clear();
            this.n.putDouble("level", (double) tVar.a);
            this.n.putDouble("rotation", (double) tVar.b);
            this.n.putDouble("overlooking", (double) tVar.c);
            this.n.putDouble("centerptx", (double) tVar.d);
            this.n.putDouble("centerpty", (double) tVar.e);
            this.n.putInt("left", tVar.f.a);
            this.n.putInt("right", tVar.f.b);
            this.n.putInt("top", tVar.f.c);
            this.n.putInt("bottom", tVar.f.d);
            this.n.putInt("lbx", tVar.g.e.a);
            this.n.putInt("lby", tVar.g.e.b);
            this.n.putInt("ltx", tVar.g.f.a);
            this.n.putInt("lty", tVar.g.f.b);
            this.n.putInt("rtx", tVar.g.g.a);
            this.n.putInt("rty", tVar.g.g.b);
            this.n.putInt("rbx", tVar.g.h.a);
            this.n.putInt("rby", tVar.g.h.b);
            this.n.putLong("yoffset", tVar.i);
            this.n.putLong("xoffset", tVar.h);
            this.n.putInt("animation", 0);
            this.n.putInt("animatime", 0);
            Bundle bundle = this.n;
            String str = "bfpp";
            if (tVar.j) {
                i = 1;
            }
            bundle.putInt(str, i);
            this.j.a(this.n);
        }
    }

    public void a(t tVar, int i) {
        int i2 = 1;
        if (this.j != null) {
            this.n.clear();
            this.n.putDouble("level", (double) tVar.a);
            this.n.putDouble("rotation", (double) tVar.b);
            this.n.putDouble("overlooking", (double) tVar.c);
            this.n.putDouble("centerptx", (double) tVar.d);
            this.n.putDouble("centerpty", (double) tVar.e);
            this.n.putInt("left", tVar.f.a);
            this.n.putInt("right", tVar.f.b);
            this.n.putInt("top", tVar.f.c);
            this.n.putInt("bottom", tVar.f.d);
            this.n.putInt("lbx", tVar.g.e.a);
            this.n.putInt("lby", tVar.g.e.b);
            this.n.putInt("ltx", tVar.g.f.a);
            this.n.putInt("lty", tVar.g.f.b);
            this.n.putInt("rtx", tVar.g.g.a);
            this.n.putInt("rty", tVar.g.g.b);
            this.n.putInt("rbx", tVar.g.h.a);
            this.n.putInt("rby", tVar.g.h.b);
            this.n.putLong("xoffset", tVar.h);
            this.n.putLong("yoffset", tVar.i);
            this.n.putInt("animation", 1);
            this.n.putInt("animatime", i);
            Bundle bundle = this.n;
            String str = "bfpp";
            if (!tVar.j) {
                i2 = 0;
            }
            bundle.putInt(str, i2);
            this.j.a(this.n);
        }
    }

    public void a(String str) {
        this.l.requestRender();
        if (str == null || str.equals("")) {
            throw new IllegalArgumentException("the path is invalid!");
        }
        this.j.a(str);
    }

    public void a(boolean z) {
        this.s = z;
    }

    @SuppressLint({"NewApi", "NewApi", "NewApi", "NewApi"})
    public boolean a(MotionEvent motionEvent) {
        int pointerCount = motionEvent.getPointerCount();
        E.getClass();
        if (pointerCount == 2) {
            float j = ((float) j()) - motionEvent.getY(0);
            float j2 = ((float) j()) - motionEvent.getY(1);
            float x = motionEvent.getX(0);
            float x2 = motionEvent.getX(1);
            switch (motionEvent.getAction()) {
                case 5:
                    M = motionEvent.getEventTime();
                    P--;
                    break;
                case 6:
                    O = motionEvent.getEventTime();
                    P++;
                    break;
                case 261:
                    L = motionEvent.getEventTime();
                    P--;
                    break;
                case 262:
                    N = motionEvent.getEventTime();
                    P++;
                    break;
            }
            if (K == null) {
                K = VelocityTracker.obtain();
            }
            K.addMovement(motionEvent);
            pointerCount = ViewConfiguration.getMinimumFlingVelocity();
            K.computeCurrentVelocity(LocationClientOption.MIN_SCAN_SPAN, (float) ViewConfiguration.getMaximumFlingVelocity());
            float xVelocity = K.getXVelocity(1);
            float yVelocity = K.getYVelocity(1);
            float xVelocity2 = K.getXVelocity(2);
            float yVelocity2 = K.getYVelocity(2);
            if (Math.abs(xVelocity) > ((float) pointerCount) || Math.abs(yVelocity) > ((float) pointerCount) || Math.abs(xVelocity2) > ((float) pointerCount) || Math.abs(yVelocity2) > ((float) pointerCount)) {
                if (E.j) {
                    double sqrt;
                    if (y == 0) {
                        if (((E.h - j <= 0.0f || E.i - j2 <= 0.0f) && (E.h - j >= 0.0f || E.i - j2 >= 0.0f)) || !this.aa) {
                            y = 2;
                        } else {
                            sqrt = ((double) FloatMath.sqrt(((x2 - x) * (x2 - x)) + ((j2 - j) * (j2 - j)))) / E.m;
                            pointerCount = (int) ((Math.log(sqrt) / Math.log(2.0d)) * 10000.0d);
                            int atan2 = (int) (((Math.atan2((double) (j2 - j), (double) (x2 - x)) - Math.atan2((double) (E.i - E.h), (double) (E.g - E.f))) * 180.0d) / 3.1416d);
                            if ((sqrt > 0.0d && (pointerCount > 3000 || pointerCount < -3000)) || Math.abs(atan2) >= 10) {
                                y = 2;
                            } else if (Math.abs(atan2) < 1) {
                                y = 1;
                            }
                        }
                        if (y == 0) {
                            return true;
                        }
                    }
                    if (y == 1 && this.q) {
                        if (!C) {
                            C = true;
                        }
                        if (!H) {
                            H = true;
                        }
                        if (E.h - j > 0.0f && E.i - j2 > 0.0f) {
                            a(1, 83, 0);
                        } else if (E.h - j < 0.0f && E.i - j2 < 0.0f) {
                            a(1, 87, 0);
                        }
                    } else if (y == 2 || y == 4 || y == 3) {
                        if (!D) {
                            D = true;
                        }
                        double atan22 = Math.atan2((double) (j2 - j), (double) (x2 - x)) - Math.atan2((double) (E.i - E.h), (double) (E.g - E.f));
                        sqrt = ((double) FloatMath.sqrt(((x2 - x) * (x2 - x)) + ((j2 - j) * (j2 - j)))) / E.m;
                        pointerCount = (int) ((Math.log(sqrt) / Math.log(2.0d)) * 10000.0d);
                        double atan23 = Math.atan2((double) (E.l - E.h), (double) (E.k - E.f));
                        double sqrt2 = (double) FloatMath.sqrt(((E.k - E.f) * (E.k - E.f)) + ((E.l - E.h) * (E.l - E.h)));
                        yVelocity = (float) (((Math.cos(atan23 + atan22) * sqrt2) * sqrt) + ((double) x));
                        float sin = (float) (((Math.sin(atan23 + atan22) * sqrt2) * sqrt) + ((double) j));
                        int i = (int) ((atan22 * 180.0d) / 3.1416d);
                        if (this.aa) {
                            if (sqrt > 0.0d && (3 == y || (Math.abs(pointerCount) > 2000 && 2 == y))) {
                                y = 3;
                                if (!J) {
                                    J = true;
                                }
                                if (this.t) {
                                    a(8193, 3, pointerCount);
                                }
                            } else if (this.aa && i != 0 && (4 == y || (Math.abs(i) > 10 && 2 == y))) {
                                y = 4;
                                if (!I) {
                                    I = true;
                                }
                                if (this.u) {
                                    a(8193, 1, i);
                                }
                            }
                        } else if (Math.abs(xVelocity) > ((float) p) || Math.abs(xVelocity2) > ((float) p)) {
                            y = 3;
                            if (!J) {
                                J = true;
                            }
                            if (this.t) {
                                a(8193, 3, pointerCount);
                            }
                        }
                        this.l.setRenderMode(1);
                        E.k = yVelocity;
                        E.l = sin;
                    }
                }
                if (y == 1 && P == 0) {
                    if (!this.r) {
                        com.baidu.platform.comapi.c.a.a().a("mapview_gesture_3d_enter");
                    } else if (k().c == 0) {
                        com.baidu.platform.comapi.c.a.a().a("mapview_gesture_3d_exit");
                    }
                } else if (y == 4 && P == 0) {
                    if (this.r) {
                        com.baidu.platform.comapi.c.a.a().a("mapview_gesture_3d_rotate");
                    } else {
                        com.baidu.platform.comapi.c.a.a().a("mapview_gesture_2d_rotate");
                    }
                }
            } else if (y == 0 && P == 0) {
                N = N > O ? N : O;
                L = L < M ? M : L;
                if (N - L < 200 && this.t) {
                    a(8193, 4, 0);
                }
            }
            if (2 != y) {
                E.h = j;
                E.i = j2;
                E.f = x;
                E.g = x2;
            }
            if (!E.j) {
                E.k = (float) (i() / 2);
                E.l = (float) (j() / 2);
                E.b = x;
                E.c = j;
                E.d = x2;
                E.e = j2;
                E.j = true;
                if (0.0d == E.m) {
                    E.m = (double) FloatMath.sqrt(((E.g - E.f) * (E.g - E.f)) + ((E.i - E.h) * (E.i - E.h)));
                }
            }
            return true;
        }
        if (k().c != 0) {
            this.r = true;
        } else {
            this.r = false;
        }
        switch (motionEvent.getAction()) {
            case 0:
                f(motionEvent);
                break;
            case 1:
                return c(motionEvent);
            case 2:
                b(motionEvent);
                break;
            default:
                return false;
        }
        return true;
    }

    @SuppressLint({"FloatMath"})
    public boolean a(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        float sqrt = (float) Math.sqrt((double) ((f * f) + (f2 * f2)));
        if (sqrt <= 500.0f) {
            return false;
        }
        a(34, (int) (sqrt * 0.6f), (((int) motionEvent2.getY()) << 16) | ((int) motionEvent2.getX()));
        f();
        return true;
    }

    public com.baidu.platform.comjni.map.basemap.a b() {
        return this.j;
    }

    public void b(boolean z) {
        this.t = z;
    }

    public boolean b(int i, int i2) {
        String a = this.j.a(-1, i, i2, (int) (((double) this.x) * c()));
        if (a != null) {
            try {
                List arrayList;
                List list;
                List list2;
                List list3;
                List list4;
                JSONObject jSONObject = new JSONObject(a);
                List arrayList2 = new ArrayList();
                JSONArray jSONArray = jSONObject.getJSONArray("dataset");
                JSONObject jSONObject2 = (JSONObject) jSONArray.get(0);
                int i3 = jSONObject2.getInt("ty");
                if (i3 == 22) {
                    arrayList = new ArrayList();
                    list = null;
                    list2 = null;
                    list3 = null;
                    list4 = null;
                } else if (i3 == 3 || i3 == 13 || i3 == 14 || i3 == 16 || i3 == 15 || i3 == 4 || i3 == 28) {
                    arrayList = null;
                    list = null;
                    Object list22 = new ArrayList();
                    list3 = null;
                    list4 = null;
                } else if (i3 == 8 || i3 == 1 || i3 == 2) {
                    arrayList = null;
                    list = null;
                    list22 = null;
                    Object list32 = new ArrayList();
                    list4 = null;
                } else if (i3 == 6) {
                    arrayList = null;
                    list = null;
                    list22 = null;
                    list32 = null;
                    Object list42 = new ArrayList();
                } else if (i3 == 24) {
                    arrayList = null;
                    Object list5 = new ArrayList();
                    list22 = null;
                    list32 = null;
                    list42 = null;
                } else {
                    arrayList = null;
                    list5 = null;
                    list22 = null;
                    list32 = null;
                    list42 = null;
                }
                for (int i4 = 0; i4 < jSONArray.length(); i4++) {
                    JSONObject jSONObject3 = (JSONObject) jSONArray.get(i4);
                    int i5 = jSONObject3.getInt("ty");
                    if (i5 != 25) {
                        r rVar = new r();
                        if (jSONObject3.has("ud")) {
                            rVar.a = jSONObject3.getString("ud");
                        } else {
                            rVar.a = "";
                        }
                        rVar.c = jSONObject3.optString("tx");
                        if (jSONObject3.has("in")) {
                            rVar.b = jSONObject3.getInt("in");
                        } else {
                            rVar.b = 0;
                        }
                        if (jSONObject3.has("geo")) {
                            String string = jSONObject3.getString("geo");
                            Bundle bundle = new Bundle();
                            bundle.putString("strkey", string);
                            JNITools.TransNodeStr2Pt(bundle);
                            rVar.d = new GeoPoint((int) bundle.getDouble("pty"), (int) bundle.getDouble("ptx"));
                        }
                        rVar.e = i5;
                        if (jSONObject3.has("of")) {
                            rVar.f = jSONObject3.getInt("of");
                        }
                        if (i3 == 22) {
                            f fVar = new f();
                            fVar.a = rVar;
                            fVar.b = jSONObject3.getLong("iest");
                            fVar.c = jSONObject3.getLong("ieend");
                            fVar.d = jSONObject3.getString("iedetail");
                            arrayList.add(fVar);
                        } else if (i3 == 3 || i3 == 13 || i3 == 14 || i3 == 16 || i3 == 15 || i3 == 4 || i3 == 28) {
                            list22.add(rVar);
                        } else if (i3 == 8 || i3 == 1 || i3 == 2) {
                            list32.add(rVar);
                        } else if (i3 == 6) {
                            list42.add(rVar);
                        } else if (i3 == 24) {
                            list5.add(rVar);
                        } else {
                            arrayList2.add(rVar);
                        }
                    }
                }
                switch (i3) {
                    case 1:
                    case 2:
                    case 8:
                        this.l.e.a(list32, jSONObject2.getInt("layerid"));
                        break;
                    case 3:
                    case 4:
                    case 13:
                    case MKEvent.MKEVENT_MAP_MOVE_FINISH /*14*/:
                    case 15:
                    case 16:
                    case 28:
                        this.l.e.b(list22, jSONObject2.getInt("layerid"));
                        break;
                    case 6:
                        this.l.e.c(list42, 0);
                        break;
                    case 17:
                    case TimeUtils.HUNDRED_DAY_FIELD_LEN /*19*/:
                        break;
                    case MKEvent.MKEVENT_POIDETAILSHAREURL /*18*/:
                        this.l.e.c(arrayList2, jSONObject2.getInt("layerid"));
                        break;
                    case 22:
                        this.l.e.a(arrayList);
                        break;
                    case 23:
                        this.l.e.c(arrayList2, jSONObject2.getInt("layerid"));
                        break;
                    case 24:
                        this.l.e.b(list5);
                        break;
                }
                this.l.e.c(arrayList2, 0);
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x005a  */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x005a  */
    public boolean b(int r9, int r10, int r11) {
        /*
        r8 = this;
        r3 = 1;
        r4 = 0;
        r0 = "";
        r2 = -1;
        r0 = r8.l;	 Catch:{ JSONException -> 0x0067 }
        r0 = r0.c();	 Catch:{ JSONException -> 0x0067 }
        r0 = r0.size();	 Catch:{ JSONException -> 0x0067 }
        r0 = r0 + -1;
        r5 = r0;
        r1 = r4;
    L_0x0013:
        if (r5 < 0) goto L_0x0070;
    L_0x0015:
        r0 = r8.l;	 Catch:{ JSONException -> 0x006c }
        r0 = r0.c();	 Catch:{ JSONException -> 0x006c }
        r0 = r0.get(r5);	 Catch:{ JSONException -> 0x006c }
        r0 = (com.baidu.platform.comapi.map.v) r0;	 Catch:{ JSONException -> 0x006c }
        r6 = r0.mType;	 Catch:{ JSONException -> 0x006c }
        r7 = 27;
        if (r6 == r7) goto L_0x002d;
    L_0x0027:
        r0 = r1;
    L_0x0028:
        r1 = r5 + -1;
        r5 = r1;
        r1 = r0;
        goto L_0x0013;
    L_0x002d:
        r1 = r0.mLayerID;	 Catch:{ JSONException -> 0x006c }
        r0 = r8.j;	 Catch:{ JSONException -> 0x006c }
        r0 = r0.a(r1, r10, r11, r11);	 Catch:{ JSONException -> 0x006c }
        if (r0 == 0) goto L_0x006e;
    L_0x0037:
        r6 = "";
        r6 = r0.equals(r6);	 Catch:{ JSONException -> 0x006c }
        if (r6 != 0) goto L_0x006e;
    L_0x003f:
        r5 = new org.json.JSONObject;	 Catch:{ JSONException -> 0x006c }
        r5.<init>(r0);	 Catch:{ JSONException -> 0x006c }
        r0 = "dataset";
        r0 = r5.getJSONArray(r0);	 Catch:{ JSONException -> 0x006c }
        r5 = 0;
        r0 = r0.get(r5);	 Catch:{ JSONException -> 0x006c }
        r0 = (org.json.JSONObject) r0;	 Catch:{ JSONException -> 0x006c }
        r5 = "itemindex";
        r0 = r0.getInt(r5);	 Catch:{ JSONException -> 0x006c }
        r2 = r3;
    L_0x0058:
        if (r9 != r3) goto L_0x0066;
    L_0x005a:
        r3 = r8.l;
        r3 = r3.e;
        r4 = new com.baidu.platform.comapi.basestruct.GeoPoint;
        r4.m1096init(r10, r11);
        r3.a(r0, r4, r1);
    L_0x0066:
        return r2;
    L_0x0067:
        r0 = move-exception;
        r1 = r4;
    L_0x0069:
        r0 = r2;
        r2 = r4;
        goto L_0x0058;
    L_0x006c:
        r0 = move-exception;
        goto L_0x0069;
    L_0x006e:
        r0 = r1;
        goto L_0x0028;
    L_0x0070:
        r0 = r2;
        r2 = r4;
        goto L_0x0058;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.platform.comapi.map.o.b(int, int, int):boolean");
    }

    public boolean b(MotionEvent motionEvent) {
        if (E.j) {
            return true;
        }
        float abs = Math.abs(motionEvent.getX() - Q);
        float abs2 = Math.abs(motionEvent.getY() - R);
        float f = (float) (((double) c.C) > 1.5d ? ((double) c.C) * 1.5d : (double) c.C);
        if (S && abs / f <= 3.0f && abs2 / f <= 3.0f) {
            return true;
        }
        S = false;
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
        if (!this.s) {
            return false;
        }
        a(3, 0, (y << 16) | x);
        return false;
    }

    public double c() {
        return this.l.g();
    }

    public void c(boolean z) {
        this.u = z;
    }

    public boolean c(int i, int i2) {
        if (this.af || this.ag) {
            return false;
        }
        this.af = true;
        this.ad = i;
        this.ae = i2;
        this.ab = this.l.d().fromPixels(i, i2);
        if (this.ab == null) {
            this.af = false;
            return false;
        }
        this.ac = g();
        if (!this.ac) {
            this.af = false;
        }
        return this.ac;
    }

    public boolean c(MotionEvent motionEvent) {
        int i = (E.j || motionEvent.getEventTime() - U >= W || Math.abs(motionEvent.getX() - Q) >= 10.0f || Math.abs(motionEvent.getY() - R) >= 10.0f) ? 0 : true;
        f();
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        if (i != 0) {
            return false;
        }
        if (x < 0) {
            x = 0;
        }
        a(5, 0, ((y < 0 ? 0 : y) << 16) | x);
        return true;
    }

    public void d(boolean z) {
        this.q = z;
    }

    public boolean d() {
        return this.s;
    }

    public boolean d(int i, int i2) {
        if (this.af || this.ag) {
            return false;
        }
        this.ag = true;
        this.ad = i;
        this.ae = i2;
        this.ab = this.l.d().fromPixels(i, i2);
        if (this.ab == null) {
            this.ag = false;
            return false;
        }
        this.ac = h();
        if (!this.ac) {
            this.ag = false;
        }
        return this.ac;
    }

    public boolean d(MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        return (g(x, y) || b(1, x, y)) ? true : this.V && b(x, y);
    }

    public void e(int i, int i2) {
        this.v = i;
        this.w = i2;
    }

    public void e(MotionEvent motionEvent) {
        if (this.Z) {
            a(8195, (((int) motionEvent.getY()) << 16) | ((int) motionEvent.getX()), ((this.w / 2) << 16) | (this.v / 2));
        }
    }

    public void e(boolean z) {
        this.Z = z;
    }

    public boolean e() {
        return this.Z;
    }

    public void f(int i, int i2) {
        if (i != 0 || i2 != 0) {
            this.l.a(i, i2);
        }
    }

    public void f(boolean z) {
        this.V = z;
    }

    public boolean g() {
        return this.l.i() < 19.0f && a(4096, 0, 0) == 1;
    }

    public boolean g(int i, int i2) {
        for (String str : this.e.keySet()) {
            if (this.j.a(((Integer) this.e.get(str)).intValue(), i, i2, (int) (((double) this.x) * c())) != null) {
                return true;
            }
        }
        return false;
    }

    public boolean h() {
        return this.l.i() > 3.0f && a(FragmentTransaction.TRANSIT_FRAGMENT_OPEN, 0, 0) == 1;
    }

    public int i() {
        return this.v;
    }

    public int j() {
        return this.w;
    }

    public t k() {
        boolean z = true;
        if (this.j == null) {
            return null;
        }
        Bundle g = this.j.g();
        t tVar = new t();
        tVar.a = (float) g.getDouble("level");
        tVar.b = (int) g.getDouble("rotation");
        tVar.c = (int) g.getDouble("overlooking");
        tVar.d = (int) g.getDouble("centerptx");
        tVar.e = (int) g.getDouble("centerpty");
        tVar.f.a = g.getInt("left");
        tVar.f.b = g.getInt("right");
        tVar.f.c = g.getInt("top");
        tVar.f.d = g.getInt("bottom");
        tVar.g.a = g.getLong("gleft");
        tVar.g.b = g.getLong("gright");
        tVar.g.c = g.getLong("gtop");
        tVar.g.d = g.getLong("gbottom");
        tVar.g.e.a = g.getInt("lbx");
        tVar.g.e.b = g.getInt("lby");
        tVar.g.f.a = g.getInt("ltx");
        tVar.g.f.b = g.getInt("lty");
        tVar.g.g.a = g.getInt("rtx");
        tVar.g.g.b = g.getInt("rty");
        tVar.g.h.a = g.getInt("rbx");
        tVar.g.h.b = g.getInt("rby");
        tVar.h = g.getLong("xoffset");
        tVar.i = g.getLong("yoffset");
        if (g.getInt("bfpp") != 1) {
            z = false;
        }
        tVar.j = z;
        if (tVar.g.a <= -20037508) {
            tVar.g.a = -20037508;
        }
        if (tVar.g.b >= 20037508) {
            tVar.g.b = 20037508;
        }
        if (tVar.g.c >= 20037508) {
            tVar.g.c = 20037508;
        }
        if (tVar.g.d <= -20037508) {
            tVar.g.d = -20037508;
        }
        return tVar;
    }

    public float l() {
        return this.j == null ? 3.0f : (float) this.j.g().getDouble("level");
    }

    public void m() {
        q();
        this.o = null;
        if (this.j != null) {
            this.j.b();
            this.j = null;
        }
    }

    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (this.l != view || keyEvent.getAction() != 0) {
            return false;
        }
        switch (i) {
            case TimeUtils.HUNDRED_DAY_FIELD_LEN /*19*/:
                f(0, -50);
                break;
            case 20:
                f(0, 50);
                break;
            case MKSearch.TYPE_AREA_POI_LIST /*21*/:
                f(-50, 0);
                break;
            case 22:
                f(50, 0);
                break;
            default:
                return false;
        }
        return true;
    }
}
