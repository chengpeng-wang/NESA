package com.flurry.sdk;

public enum df {
    kNoNetworkConnectivity(1),
    kMissingAdController(2),
    kNoContext(3),
    kInvalidAdUnit(4),
    kPrecachingDownloadFailed(6),
    kPrecachingCopyFailed(7),
    kPrecachingMissingAssets(8),
    kPrerenderDownloadFailed(9),
    kPrerenderDownloadTimeout(13),
    kUnfilled(20),
    kIncorrectClassForAdSpace(21),
    kUnknown(100);
    
    public final int m;

    private df(int i) {
        this.m = i;
    }
}
