package com.qc.model;

import android.content.Context;
import android.content.Intent;
import com.qc.base.BitmapCache;
import com.qc.base.QCMainCourse;
import com.qc.common.Constant;
import com.qc.entity.AdsInfo;
import com.qc.util.IsNetOpen;

public class AdsTask implements Runnable {
    private AdsInfo adsInfo;
    private int lastIndex = 0;
    private Context mContext;
    private int offSet = 0;

    public AdsTask(Context mContext, AdsInfo adsInfo, int offSet, int lastIndex) {
        this.mContext = mContext;
        this.adsInfo = adsInfo;
        this.offSet = offSet;
        this.lastIndex = lastIndex;
    }

    public void run() {
        if (new IsNetOpen(this.mContext).checkNet()) {
            int id = this.adsInfo.getId();
            String urlStr = this.adsInfo.getPathimage();
            int descriptType = this.adsInfo.getDescriptype();
            String icon = this.adsInfo.getIcon();
            if (descriptType == 2) {
                if (!(urlStr == null || "".equals(urlStr))) {
                    BitmapCache.getInstance().downLoadImage(this.mContext, urlStr, id);
                }
            } else if (!(descriptType != 1 || icon == null || "".equals(icon))) {
                BitmapCache.getInstance().downLoadImage(this.mContext, icon, id);
            }
            if (this.offSet == 1) {
                QCMainCourse.startAdsAlertHandler(this.mContext, this.adsInfo.getId(), this.adsInfo.getTimeout());
            } else if (this.offSet == this.lastIndex) {
                this.mContext.sendBroadcast(new Intent(Constant.ADS_TASK_FINISHED));
            }
        }
    }
}
