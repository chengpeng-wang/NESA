package com.baidu.location;

public final class LocationClientOption {
    public static final int GpsFirst = 1;
    public static final int MIN_SCAN_SPAN = 1000;
    public static final int NetWorkFirst = 2;
    protected int a = 3;
    /* renamed from: byte */
    protected String f41byte = "com.baidu.location.service_v2.9";
    /* renamed from: case */
    protected boolean f42case = false;
    /* renamed from: char */
    protected String f43char = "detail";
    /* renamed from: do */
    protected float f44do = 500.0f;
    /* renamed from: else */
    protected boolean f45else = false;
    /* renamed from: for */
    protected boolean f46for = true;
    /* renamed from: goto */
    protected int f47goto = 1;
    /* renamed from: if */
    protected boolean f48if = false;
    /* renamed from: int */
    protected int f49int = 0;
    /* renamed from: long */
    protected int f50long = 12000;
    /* renamed from: new */
    protected String f51new = "SDK2.0";
    /* renamed from: try */
    protected String f52try = "gcj02";
    /* renamed from: void */
    protected boolean f53void = false;

    public LocationClientOption(LocationClientOption locationClientOption) {
        this.f52try = locationClientOption.f52try;
        this.f43char = locationClientOption.f43char;
        this.f42case = locationClientOption.f42case;
        this.f49int = locationClientOption.f49int;
        this.f50long = locationClientOption.f50long;
        this.f51new = locationClientOption.f51new;
        this.f47goto = locationClientOption.f47goto;
        this.f53void = locationClientOption.f53void;
        this.f48if = locationClientOption.f48if;
        this.f44do = locationClientOption.f44do;
        this.a = locationClientOption.a;
        this.f41byte = locationClientOption.f41byte;
        this.f46for = locationClientOption.f46for;
    }

    public void disableCache(boolean z) {
        this.f46for = z;
    }

    public boolean equals(LocationClientOption locationClientOption) {
        return this.f52try.equals(locationClientOption.f52try) && this.f43char.equals(locationClientOption.f43char) && this.f42case == locationClientOption.f42case && this.f49int == locationClientOption.f49int && this.f50long == locationClientOption.f50long && this.f51new.equals(locationClientOption.f51new) && this.f53void == locationClientOption.f53void && this.f47goto == locationClientOption.f47goto && this.a == locationClientOption.a && this.f48if == locationClientOption.f48if && this.f44do == locationClientOption.f44do && this.f46for == locationClientOption.f46for;
    }

    public String getAddrType() {
        return this.f43char;
    }

    public String getCoorType() {
        return this.f52try;
    }

    public float getPoiDistance() {
        return this.f44do;
    }

    public boolean getPoiExtranInfo() {
        return this.f48if;
    }

    public int getPoiNumber() {
        return this.a;
    }

    public int getPriority() {
        return this.f47goto;
    }

    public String getProdName() {
        return this.f51new;
    }

    public int getScanSpan() {
        return this.f49int;
    }

    public String getServiceName() {
        return this.f41byte;
    }

    public int getTimeOut() {
        return this.f50long;
    }

    public boolean isDisableCache() {
        return this.f46for;
    }

    public boolean isLocationNotify() {
        return this.f53void;
    }

    public boolean isOpenGps() {
        return this.f42case;
    }

    public void setAddrType(String str) {
        if (str.length() > 32) {
            str = str.substring(0, 32);
        }
        this.f43char = str;
    }

    public void setCoorType(String str) {
        String toLowerCase = str.toLowerCase();
        if (toLowerCase.equals("gcj02") || toLowerCase.equals("bd09") || toLowerCase.equals("bd09ll")) {
            this.f52try = toLowerCase;
        }
    }

    public void setLocationNotify(boolean z) {
        this.f53void = z;
    }

    public void setOpenGps(boolean z) {
        this.f42case = z;
    }

    public void setPoiDistance(float f) {
        this.f44do = f;
    }

    public void setPoiExtraInfo(boolean z) {
        this.f48if = z;
    }

    public void setPoiNumber(int i) {
        if (i > 10) {
            i = 10;
        }
        this.a = i;
    }

    public void setPriority(int i) {
        if (i == 1 || i == 2) {
            this.f47goto = i;
        }
    }

    public void setProdName(String str) {
        if (str.length() > 64) {
            str = str.substring(0, 64);
        }
        this.f51new = str;
    }

    public void setScanSpan(int i) {
        this.f49int = i;
    }

    public void setServiceName(String str) {
        this.f41byte = str;
    }

    public void setTimeOut(int i) {
        this.f50long = i;
    }
}
