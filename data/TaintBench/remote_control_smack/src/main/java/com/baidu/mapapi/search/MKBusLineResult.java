package com.baidu.mapapi.search;

public class MKBusLineResult {
    private String a;
    private String b;
    private int c;
    private String d;
    private String e;
    private MKRoute f = new MKRoute();

    /* access modifiers changed from: 0000 */
    public void a(String str) {
        this.d = str;
    }

    /* access modifiers changed from: 0000 */
    public void a(String str, String str2, int i) {
        this.a = str;
        this.b = str2;
        this.c = i;
    }

    /* access modifiers changed from: 0000 */
    public void b(String str) {
        this.e = str;
    }

    public String getBusCompany() {
        return this.a;
    }

    public String getBusName() {
        return this.b;
    }

    public MKRoute getBusRoute() {
        return this.f;
    }

    public String getEndTime() {
        return this.e;
    }

    public String getStartTime() {
        return this.d;
    }

    public MKStep getStation(int i) {
        return (this.f == null || this.f.getNumSteps() <= 0 || i < 0 || i > this.f.getNumSteps() - 1) ? null : this.f.getStep(i);
    }

    public int isMonTicket() {
        return this.c;
    }
}
