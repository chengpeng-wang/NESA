package com.flurry.sdk;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.zip.CRC32;

public final class ih extends MessageDigest {
    private final CRC32 a = new CRC32();

    public ih() {
        super("CRC");
    }

    public final int a() {
        return ByteBuffer.wrap(engineDigest()).getInt();
    }

    /* access modifiers changed from: protected|final */
    public final byte[] engineDigest() {
        long value = this.a.getValue();
        return new byte[]{(byte) ((int) ((-16777216 & value) >> 24)), (byte) ((int) ((16711680 & value) >> 16)), (byte) ((int) ((65280 & value) >> 8)), (byte) ((int) (value & 255))};
    }

    /* access modifiers changed from: protected|final */
    public final void engineReset() {
        this.a.reset();
    }

    /* access modifiers changed from: protected|final */
    public final void engineUpdate(byte b) {
        this.a.update(b);
    }

    /* access modifiers changed from: protected|final */
    public final void engineUpdate(byte[] bArr, int i, int i2) {
        this.a.update(bArr, i, i2);
    }
}
