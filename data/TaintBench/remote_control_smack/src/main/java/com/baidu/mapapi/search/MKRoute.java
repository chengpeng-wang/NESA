package com.baidu.mapapi.search;

import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.mapapi.utils.e;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import java.util.ArrayList;
import java.util.Iterator;

public class MKRoute {
    public static final int ROUTE_TYPE_BUS_LINE = 3;
    public static final int ROUTE_TYPE_DRIVING = 1;
    public static final int ROUTE_TYPE_UNKNOW = 0;
    public static final int ROUTE_TYPE_WALKING = 2;
    ArrayList<ArrayList<GeoPoint>> a;
    private int b;
    private int c;
    private int d;
    private int e;
    private GeoPoint f;
    private GeoPoint g;
    private ArrayList<ArrayList<GeoPoint>> h;
    private ArrayList<MKStep> i;
    private String j;

    /* access modifiers changed from: 0000 */
    public void a(int i) {
        this.c = i;
    }

    /* access modifiers changed from: 0000 */
    public void a(GeoPoint geoPoint) {
        this.f = geoPoint;
    }

    /* access modifiers changed from: 0000 */
    public void a(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        char[] toCharArray = str.toCharArray();
        Object obj = null;
        for (int i = 0; i < toCharArray.length; i++) {
            if (toCharArray[i] == '<') {
                obj = 1;
            } else if (toCharArray[i] == '>') {
                obj = null;
            } else if (obj == null) {
                stringBuilder.append(toCharArray[i]);
            }
        }
        this.j = stringBuilder.toString();
    }

    /* access modifiers changed from: 0000 */
    public void a(ArrayList<MKStep> arrayList) {
        this.i = arrayList;
    }

    /* access modifiers changed from: 0000 */
    public void b(int i) {
        this.b = i;
    }

    /* access modifiers changed from: 0000 */
    public void b(GeoPoint geoPoint) {
        this.g = geoPoint;
    }

    /* access modifiers changed from: 0000 */
    public void b(ArrayList<ArrayList<GeoPoint>> arrayList) {
        this.h = arrayList;
    }

    /* access modifiers changed from: 0000 */
    public void c(int i) {
        this.e = i;
    }

    public void customizeRoute(GeoPoint geoPoint, GeoPoint geoPoint2, GeoPoint[] geoPointArr) {
        if (geoPoint != null && geoPoint2 != null && geoPointArr != null) {
            GeoPoint[][] geoPointArr2 = (GeoPoint[][]) null;
            customizeRoute(geoPoint, geoPoint2, new GeoPoint[][]{geoPointArr});
        }
    }

    public void customizeRoute(GeoPoint geoPoint, GeoPoint geoPoint2, GeoPoint[][] geoPointArr) {
        if (geoPoint != null && geoPoint2 != null && geoPointArr != null) {
            if (geoPoint != null) {
                this.f = geoPoint;
            }
            if (geoPoint2 != null) {
                this.g = geoPoint2;
            }
            this.e = 3;
            double d = 0.0d;
            GeoPoint geoPoint3 = null;
            if (geoPointArr != null && geoPointArr.length > 0) {
                this.h = new ArrayList();
                for (GeoPoint[] geoPointArr2 : geoPointArr) {
                    if (geoPointArr2 != null) {
                        ArrayList arrayList = new ArrayList();
                        for (int i = 0; i < geoPointArr2.length; i++) {
                            if (geoPointArr2[i] != null) {
                                arrayList.add(geoPointArr2[i]);
                            }
                        }
                        this.h.add(arrayList);
                    }
                }
                this.a = new ArrayList();
                this.i = new ArrayList();
                int i2 = 0;
                while (i2 < this.h.size()) {
                    ArrayList arrayList2 = (ArrayList) this.h.get(i2);
                    ArrayList arrayList3 = new ArrayList();
                    MKStep mKStep = new MKStep();
                    GeoPoint geoPoint4 = geoPoint3;
                    int i3 = 0;
                    double d2 = d;
                    GeoPoint geoPoint5 = geoPoint4;
                    while (i3 < arrayList2.size()) {
                        if (i2 == 0 && i3 == 0 && arrayList2.size() > 1) {
                            MKStep mKStep2 = new MKStep();
                            mKStep2.a((GeoPoint) arrayList2.get(i3));
                            mKStep2.a(String.valueOf(this.i.size()));
                            this.i.add(mKStep2);
                        }
                        arrayList3.add(e.b((GeoPoint) arrayList2.get(i3)));
                        d = geoPoint5 != null ? d2 + DistanceUtil.getDistance((GeoPoint) arrayList2.get(i3), geoPoint5) : d2;
                        if (i3 == arrayList2.size() - 1) {
                            mKStep.a((GeoPoint) arrayList2.get(i3));
                            mKStep.a(String.valueOf(this.i.size()));
                        }
                        i3++;
                        d2 = d;
                        geoPoint5 = (GeoPoint) arrayList2.get(i3);
                    }
                    this.a.add(arrayList3);
                    this.i.add(mKStep);
                    i2++;
                    geoPoint3 = geoPoint5;
                    d = d2;
                }
                this.c = (int) d;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void d(int i) {
        this.d = i;
    }

    public ArrayList<ArrayList<GeoPoint>> getArrayPoints() {
        if (this.h.size() == 0 && this.e == 1) {
            Iterator it = this.i.iterator();
            while (it.hasNext()) {
                c.a(((MKStep) it.next()).b(), this.h, this.a);
            }
        }
        return this.h;
    }

    public int getDistance() {
        return this.c;
    }

    public GeoPoint getEnd() {
        return this.g;
    }

    public int getIndex() {
        return this.b;
    }

    public int getNumSteps() {
        return this.i != null ? this.i.size() : 0;
    }

    public int getRouteType() {
        return this.e;
    }

    public GeoPoint getStart() {
        return this.f;
    }

    public MKStep getStep(int i) {
        return (this.i == null || i < 0 || i > this.i.size() - 1) ? null : (MKStep) this.i.get(i);
    }

    public int getTime() {
        return this.d;
    }

    public String getTip() {
        return this.j;
    }
}
