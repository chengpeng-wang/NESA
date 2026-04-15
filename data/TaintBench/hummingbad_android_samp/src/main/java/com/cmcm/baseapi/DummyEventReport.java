package com.cmcm.baseapi;

import com.cmcm.baseapi.utils.Assure;

public class DummyEventReport implements IEventReport {
    ILogger mLogger = null;

    public DummyEventReport(ILogger logger) {
        Assure.checkNotNull(logger);
        this.mLogger = logger;
    }

    public void reportShow(String posId, String adType) {
    }

    public void reportShow(String posId, String adType, String extra) {
    }

    public void reportClick(String posId, String adType) {
    }

    public void reportClick(String posId, String adType, String extra) {
    }

    public void reportOther(String posId, int type) {
    }

    public void reportOther(String posId, int type, String extra) {
    }
}
