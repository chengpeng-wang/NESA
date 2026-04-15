package com.google.zxing.common;

import com.google.zxing.ResultPoint;

public class DetectorResult {
    private final BitMatrix bits;
    private final ResultPoint[] points;

    public DetectorResult(BitMatrix bits, ResultPoint[] points) {
        this.bits = bits;
        this.points = points;
    }

    public BitMatrix getBits() {
        return this.bits;
    }

    public ResultPoint[] getPoints() {
        return this.points;
    }
}
