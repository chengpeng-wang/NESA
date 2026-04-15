package com.baidu.mapapi.map;

class a {
    private MKOfflineMapListener a;

    public a(MKOfflineMapListener mKOfflineMapListener) {
        this.a = mKOfflineMapListener;
    }

    public void a(MKEvent mKEvent) {
        if (this.a != null && com.baidu.platform.comapi.a.a) {
            switch (mKEvent.a) {
                case 0:
                    this.a.onGetOfflineMapState(mKEvent.a, mKEvent.c);
                    return;
                case 4:
                    this.a.onGetOfflineMapState(mKEvent.a, mKEvent.c);
                    return;
                case 6:
                    this.a.onGetOfflineMapState(mKEvent.a, mKEvent.c);
                    return;
                default:
                    return;
            }
        }
    }
}
