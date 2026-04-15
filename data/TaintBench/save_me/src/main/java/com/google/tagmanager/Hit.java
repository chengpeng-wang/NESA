package com.google.tagmanager;

import android.text.TextUtils;

class Hit {
    private final long mHitFirstDispatchTime;
    private final long mHitId;
    private final long mHitTime;
    private String mHitUrl;

    /* access modifiers changed from: 0000 */
    public long getHitId() {
        return this.mHitId;
    }

    /* access modifiers changed from: 0000 */
    public long getHitTime() {
        return this.mHitTime;
    }

    /* access modifiers changed from: 0000 */
    public long getHitFirstDispatchTime() {
        return this.mHitFirstDispatchTime;
    }

    Hit(long hitId, long hitTime, long firstDispatchTime) {
        this.mHitId = hitId;
        this.mHitTime = hitTime;
        this.mHitFirstDispatchTime = firstDispatchTime;
    }

    Hit(long hitId, long hitTime) {
        this.mHitId = hitId;
        this.mHitTime = hitTime;
        this.mHitFirstDispatchTime = 0;
    }

    /* access modifiers changed from: 0000 */
    public String getHitUrl() {
        return this.mHitUrl;
    }

    /* access modifiers changed from: 0000 */
    public void setHitUrl(String hitUrl) {
        if (hitUrl != null && !TextUtils.isEmpty(hitUrl.trim())) {
            this.mHitUrl = hitUrl;
        }
    }
}
