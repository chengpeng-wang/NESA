package com.baidu.platform.comapi.basestruct;

public class GeoPoint {
    private int a;
    private int b;

    public GeoPoint(int i, int i2) {
        this.a = i;
        this.b = i2;
    }

    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        boolean z = this.a == ((GeoPoint) obj).a && this.b == ((GeoPoint) obj).b;
        return z;
    }

    public int getLatitudeE6() {
        return this.a;
    }

    public int getLongitudeE6() {
        return this.b;
    }

    public int hashCode() {
        return toString().hashCode();
    }

    public void setLatitudeE6(int i) {
        this.a = i;
    }

    public void setLongitudeE6(int i) {
        this.b = i;
    }

    public String toString() {
        return "GeoPoint: Latitude: " + this.a + ", Longitude: " + this.b;
    }
}
