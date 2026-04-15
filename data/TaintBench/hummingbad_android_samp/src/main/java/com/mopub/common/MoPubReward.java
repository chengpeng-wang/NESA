package com.mopub.common;

import android.support.annotation.NonNull;

public final class MoPubReward {
    public static final int NO_REWARD_AMOUNT = -123;
    public static final String NO_REWARD_LABEL = "";
    private final int mAmount;
    @NonNull
    private final String mLabel;
    private final boolean mSuccess;

    private MoPubReward(boolean success, @NonNull String label, int amount) {
        this.mSuccess = success;
        this.mLabel = label;
        this.mAmount = amount;
    }

    @NonNull
    public static MoPubReward failure() {
        return new MoPubReward(false, "", 0);
    }

    @NonNull
    public static MoPubReward success(@NonNull String rewardLabel, int amount) {
        return new MoPubReward(true, rewardLabel, amount);
    }

    public final boolean isSuccessful() {
        return this.mSuccess;
    }

    @NonNull
    public final String getLabel() {
        return this.mLabel;
    }

    public final int getAmount() {
        return this.mAmount;
    }
}
