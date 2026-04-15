package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.util.Map;

public abstract class OneDimensionalCodeWriter implements Writer {
    private final int sidesMargin;

    public abstract byte[] encode(String str);

    protected OneDimensionalCodeWriter(int sidesMargin) {
        this.sidesMargin = sidesMargin;
    }

    public BitMatrix encode(String contents, BarcodeFormat format, int width, int height) throws WriterException {
        return encode(contents, format, width, height, null);
    }

    public BitMatrix encode(String contents, BarcodeFormat format, int width, int height, Map<EncodeHintType, ?> map) throws WriterException {
        if (contents.length() == 0) {
            throw new IllegalArgumentException("Found empty contents");
        } else if (width >= 0 && height >= 0) {
            return renderResult(encode(contents), width, height);
        } else {
            throw new IllegalArgumentException("Negative size is not allowed. Input: " + width + 'x' + height);
        }
    }

    private BitMatrix renderResult(byte[] code, int width, int height) {
        int inputWidth = code.length;
        int fullWidth = inputWidth + this.sidesMargin;
        int outputWidth = Math.max(width, fullWidth);
        int outputHeight = Math.max(1, height);
        int multiple = outputWidth / fullWidth;
        int leftPadding = (outputWidth - (inputWidth * multiple)) / 2;
        BitMatrix output = new BitMatrix(outputWidth, outputHeight);
        int inputX = 0;
        int outputX = leftPadding;
        while (inputX < inputWidth) {
            if (code[inputX] == (byte) 1) {
                output.setRegion(outputX, 0, multiple, outputHeight);
            }
            inputX++;
            outputX += multiple;
        }
        return output;
    }

    protected static int appendPattern(byte[] target, int pos, int[] pattern, int startColor) {
        if (startColor == 0 || startColor == 1) {
            byte color = (byte) startColor;
            int numAdded = 0;
            for (int len : pattern) {
                for (int j = 0; j < len; j++) {
                    target[pos] = color;
                    pos++;
                    numAdded++;
                }
                color = (byte) (color ^ 1);
            }
            return numAdded;
        }
        throw new IllegalArgumentException("startColor must be either 0 or 1, but got: " + startColor);
    }
}
