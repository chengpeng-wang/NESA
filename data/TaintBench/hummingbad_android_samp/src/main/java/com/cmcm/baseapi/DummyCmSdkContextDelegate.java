package com.cmcm.baseapi;

import android.content.Context;
import com.cmcm.baseapi.CmSdkContext.CmSdkContextDelegate;
import com.cmcm.baseapi.ads.INativeAdLoader;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class DummyCmSdkContextDelegate implements CmSdkContextDelegate {
    static final String CMSDK_BASE_NAME = "cmsdk_base";
    IEventReport mEventReport;
    Map<String, WeakReference<ILogger>> mLoggerMap;
    ILogger mSdkLogger;
    ISharedConfig mSharedConfig;

    DummyCmSdkContextDelegate(Context context) {
        this.mLoggerMap = new HashMap();
        this.mEventReport = null;
        this.mSharedConfig = null;
        this.mSdkLogger = null;
        this.mSdkLogger = createLogger(CMSDK_BASE_NAME);
        this.mEventReport = new DummyEventReport(this.mSdkLogger);
        this.mSharedConfig = new SharedPreferencesAdapter(context.getSharedPreferences(CMSDK_BASE_NAME, 0));
    }

    public INativeAdLoader createNativeAdLoader(String adPosName) {
        return null;
    }

    public ILogger createLogger(String loggerTagName) {
        ILogger iLogger = null;
        if (this.mLoggerMap.containsKey(loggerTagName)) {
            iLogger = (ILogger) ((WeakReference) this.mLoggerMap.get(loggerTagName)).get();
        }
        if (iLogger != null) {
            return iLogger;
        }
        DummyLogger dummyLogger = new DummyLogger(loggerTagName);
        this.mLoggerMap.put(loggerTagName, new WeakReference(dummyLogger));
        return dummyLogger;
    }

    public IEventReport getEventReport() {
        return this.mEventReport;
    }

    public ISharedConfig getSharedConfig(String configName) {
        return this.mSharedConfig;
    }
}
