package com.google.zxing.qrcode.detector;

import com.google.zxing.ResultPoint;

public final class FinderPattern extends ResultPoint {
    private int count;
    private final float estimatedModuleSize;

    FinderPattern(float posX, float posY, float estimatedModuleSize) {
        this(posX, posY, estimatedModuleSize, 1);
    }

    FinderPattern(float posX, float posY, float estimatedModuleSize, int count) {
        super(posX, posY);
        this.estimatedModuleSize = estimatedModuleSize;
        this.count = count;
    }

    public float getEstimatedModuleSize() {
        return this.estimatedModuleSize;
    }

    /* access modifiers changed from: 0000 */
    public int getCount() {
        return this.count;
    }

    /* access modifiers changed from: 0000 */
    public void incrementCount() {
        this.count++;
    }

    /* access modifiers changed from: 0000 */
    public boolean aboutEquals(float moduleSize, float i, float j) {
        if (Math.abs(i - getY()) > moduleSize || Math.abs(j - getX()) > moduleSize) {
            return false;
        }
        float moduleSizeDiff = Math.abs(moduleSize - this.estimatedModuleSize);
        if (moduleSizeDiff <= 1.0f || moduleSizeDiff <= this.estimatedModuleSize) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: 0000 */
    public FinderPattern combineEstimate(float i, float j, float newModuleSize) {
        int combinedCount = this.count + 1;
        return new FinderPattern(((((float) this.count) * getX()) + j) / ((float) combinedCount), ((((float) this.count) * getY()) + i) / ((float) combinedCount), ((((float) this.count) * this.estimatedModuleSize) + newModuleSize) / ((float) combinedCount), combinedCount);
    }
}
