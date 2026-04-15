package com.google.analytics.tracking.android;

import java.util.Map;
import java.util.Random;

class AdMobInfo {
    private static final AdMobInfo INSTANCE = new AdMobInfo();
    private int mAdHitId;
    private Random mRandom = new Random();

    enum AdMobKey {
        CLIENT_ID_KEY("ga_cid"),
        HIT_ID_KEY("ga_hid"),
        PROPERTY_ID_KEY("ga_wpids"),
        VISITOR_ID_KEY("ga_uid");
        
        private String mBowParameter;

        private AdMobKey(String bowParameter) {
            this.mBowParameter = bowParameter;
        }

        /* access modifiers changed from: 0000 */
        public String getBowParameter() {
            return this.mBowParameter;
        }
    }

    private AdMobInfo() {
    }

    static AdMobInfo getInstance() {
        return INSTANCE;
    }

    /* access modifiers changed from: 0000 */
    public Map<String, String> getJoinIds() {
        return null;
    }

    /* access modifiers changed from: 0000 */
    public int generateAdHitId() {
        this.mAdHitId = this.mRandom.nextInt(2147483646) + 1;
        return this.mAdHitId;
    }

    /* access modifiers changed from: 0000 */
    public void setAdHitId(int adHitId) {
        this.mAdHitId = adHitId;
    }

    /* access modifiers changed from: 0000 */
    public int getAdHitId() {
        return this.mAdHitId;
    }
}
