package com.google.zxing.oned;

public abstract class UPCEANWriter extends OneDimensionalCodeWriter {
    protected UPCEANWriter() {
        super(UPCEANReader.START_END_PATTERN.length << 1);
    }
}
