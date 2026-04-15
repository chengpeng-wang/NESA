package com.google.zxing.pdf417.decoder;

import com.google.zxing.FormatException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DecoderResult;

public final class Decoder {
    private static final int MAX_EC_CODEWORDS = 512;
    private static final int MAX_ERRORS = 3;

    public DecoderResult decode(boolean[][] image) throws FormatException {
        int dimension = image.length;
        BitMatrix bits = new BitMatrix(dimension);
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (image[j][i]) {
                    bits.set(j, i);
                }
            }
        }
        return decode(bits);
    }

    public DecoderResult decode(BitMatrix bits) throws FormatException {
        BitMatrixParser parser = new BitMatrixParser(bits);
        int[] codewords = parser.readCodewords();
        if (codewords.length == 0) {
            throw FormatException.getFormatInstance();
        }
        int numECCodewords = 1 << (parser.getECLevel() + 1);
        correctErrors(codewords, parser.getErasures(), numECCodewords);
        verifyCodewordCount(codewords, numECCodewords);
        return DecodedBitStreamParser.decode(codewords);
    }

    private static void verifyCodewordCount(int[] codewords, int numECCodewords) throws FormatException {
        if (codewords.length < 4) {
            throw FormatException.getFormatInstance();
        }
        int numberOfCodewords = codewords[0];
        if (numberOfCodewords > codewords.length) {
            throw FormatException.getFormatInstance();
        } else if (numberOfCodewords != 0) {
        } else {
            if (numECCodewords < codewords.length) {
                codewords[0] = codewords.length - numECCodewords;
                return;
            }
            throw FormatException.getFormatInstance();
        }
    }

    private static int correctErrors(int[] codewords, int[] erasures, int numECCodewords) throws FormatException {
        if (erasures.length > (numECCodewords / 2) + 3 || numECCodewords < 0 || numECCodewords > 512) {
            throw FormatException.getFormatInstance();
        }
        int numErasures = erasures.length;
        if (null > null) {
            numErasures -= 0;
        }
        if (numErasures <= 3) {
            return 0;
        }
        throw FormatException.getFormatInstance();
    }
}
