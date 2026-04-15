package com.cmcm.baseapi;

import android.content.Context;
import com.cmcm.baseapi.ads.INativeAdLoader;
import com.cmcm.baseapi.utils.Assure;

public class CmSdkContext {
    private static CmSdkContext sInstance = null;
    private Context mApplicationContext = null;
    private CmSdkContextDelegate mApplicationDelegate = null;

    public interface CmSdkContextDelegate {
        ILogger createLogger(String str);

        INativeAdLoader createNativeAdLoader(String str);

        IEventReport getEventReport();

        ISharedConfig getSharedConfig(String str);
    }

    private CmSdkContext() {
    }

    public static CmSdkContext getInstance() {
        if (sInstance == null) {
            synchronized (CmSdkContext.class) {
                if (sInstance == null) {
                    sInstance = new CmSdkContext();
                }
            }
        }
        return sInstance;
    }

    public void initSdkContext(Context context, CmSdkContextDelegate applicationDelegate) {
        Assure.checkNotNull(context);
        Assure.checkNull(this.mApplicationContext);
        this.mApplicationContext = context;
        if (applicationDelegate == null) {
            applicationDelegate = new DummyCmSdkContextDelegate(context);
        }
        this.mApplicationDelegate = applicationDelegate;
    }

    public Context getApplicationContext() {
        Assure.checkNotNull(this.mApplicationContext);
        return this.mApplicationContext;
    }

    public CmSdkContextDelegate getApplicationDelegate() {
        return this.mApplicationDelegate;
    }
}
