package com.google.tagmanager;

abstract class ServiceManager {
    public abstract void dispatch();

    public abstract void onRadioPowered();

    public abstract void setDispatchPeriod(int i);

    public abstract void updateConnectivityStatus(boolean z);

    ServiceManager() {
    }
}
