package com.google.zxing.oned.rss.expanded.decoders;

abstract class DecodedObject {
    private final int newPosition;

    DecodedObject(int newPosition) {
        this.newPosition = newPosition;
    }

    /* access modifiers changed from: 0000 */
    public int getNewPosition() {
        return this.newPosition;
    }
}
