package com.google.zxing.oned.rss.expanded.decoders;

final class DecodedInformation extends DecodedObject {
    private final String newString;
    private final boolean remaining;
    private final int remainingValue;

    DecodedInformation(int newPosition, String newString) {
        super(newPosition);
        this.newString = newString;
        this.remaining = false;
        this.remainingValue = 0;
    }

    DecodedInformation(int newPosition, String newString, int remainingValue) {
        super(newPosition);
        this.remaining = true;
        this.remainingValue = remainingValue;
        this.newString = newString;
    }

    /* access modifiers changed from: 0000 */
    public String getNewString() {
        return this.newString;
    }

    /* access modifiers changed from: 0000 */
    public boolean isRemaining() {
        return this.remaining;
    }

    /* access modifiers changed from: 0000 */
    public int getRemainingValue() {
        return this.remainingValue;
    }
}
