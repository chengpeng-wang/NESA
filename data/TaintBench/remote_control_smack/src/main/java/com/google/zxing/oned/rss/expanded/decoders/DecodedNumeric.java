package com.google.zxing.oned.rss.expanded.decoders;

final class DecodedNumeric extends DecodedObject {
    static final int FNC1 = 10;
    private final int firstDigit;
    private final int secondDigit;

    DecodedNumeric(int newPosition, int firstDigit, int secondDigit) {
        super(newPosition);
        this.firstDigit = firstDigit;
        this.secondDigit = secondDigit;
        if (this.firstDigit < 0 || this.firstDigit > 10) {
            throw new IllegalArgumentException("Invalid firstDigit: " + firstDigit);
        } else if (this.secondDigit < 0 || this.secondDigit > 10) {
            throw new IllegalArgumentException("Invalid secondDigit: " + secondDigit);
        }
    }

    /* access modifiers changed from: 0000 */
    public int getFirstDigit() {
        return this.firstDigit;
    }

    /* access modifiers changed from: 0000 */
    public int getSecondDigit() {
        return this.secondDigit;
    }

    /* access modifiers changed from: 0000 */
    public int getValue() {
        return (this.firstDigit * 10) + this.secondDigit;
    }

    /* access modifiers changed from: 0000 */
    public boolean isFirstDigitFNC1() {
        return this.firstDigit == 10;
    }

    /* access modifiers changed from: 0000 */
    public boolean isSecondDigitFNC1() {
        return this.secondDigit == 10;
    }

    /* access modifiers changed from: 0000 */
    public boolean isAnyFNC1() {
        return this.firstDigit == 10 || this.secondDigit == 10;
    }
}
