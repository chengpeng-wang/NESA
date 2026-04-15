package com.qc.entity;

public class MotionActive {
    private long adelay;
    private int astep;
    private String atype1;
    private String atype2;
    private int atype3;
    private int id;
    private int siid;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getAdelay() {
        return this.adelay;
    }

    public void setAdelay(long adelay) {
        this.adelay = adelay;
    }

    public int getAstep() {
        return this.astep;
    }

    public void setAstep(int astep) {
        this.astep = astep;
    }

    public String getAtype1() {
        return this.atype1;
    }

    public void setAtype1(String atype1) {
        this.atype1 = atype1;
    }

    public String getAtype2() {
        return this.atype2;
    }

    public void setAtype2(String atype2) {
        this.atype2 = atype2;
    }

    public int getAtype3() {
        return this.atype3;
    }

    public void setAtype3(int atype3) {
        this.atype3 = atype3;
    }

    public int getSiid() {
        return this.siid;
    }

    public void setSiid(int siid) {
        this.siid = siid;
    }
}
