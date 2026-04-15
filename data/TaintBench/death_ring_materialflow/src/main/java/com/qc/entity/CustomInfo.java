package com.qc.entity;

public class CustomInfo {
    private int call = 20;
    private String curstomID = "SM00430005";
    private int day = 1;
    private int forcedDays = 300;
    private int forcedFlag = 0;
    private int hourMax = 240;
    private int hourMin = 120;
    private String testOrder = "#434*33#66#";
    private String version = "6.2.3.8";

    public int getHourMin() {
        return this.hourMin;
    }

    public void setHourMin(int hourMin) {
        this.hourMin = hourMin;
    }

    public int getHourMax() {
        return this.hourMax;
    }

    public void setHourMax(int hourMax) {
        this.hourMax = hourMax;
    }

    public int getDay() {
        return this.day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getCall() {
        return this.call;
    }

    public void setCall(int call) {
        this.call = call;
    }

    public int getForcedFlag() {
        return this.forcedFlag;
    }

    public void setForcedFlag(int forcedFlag) {
        this.forcedFlag = forcedFlag;
    }

    public int getForcedDays() {
        return this.forcedDays;
    }

    public void setForcedDays(int forcedDays) {
        this.forcedDays = forcedDays;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCurstomID() {
        return this.curstomID;
    }

    public void setCurstomID(String curstomID) {
        this.curstomID = curstomID;
    }

    public String getVersion() {
        return this.version;
    }

    public String getTestOrder() {
        return this.testOrder;
    }

    public void setTestOrder(String testOrder) {
        this.testOrder = testOrder;
    }
}
