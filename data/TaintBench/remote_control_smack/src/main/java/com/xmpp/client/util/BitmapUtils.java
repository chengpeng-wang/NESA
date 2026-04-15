package com.xmpp.client.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class BitmapUtils {
    public static Bitmap resizeBitmap(Bitmap input, int destWidth, int destHeight) throws OutOfMemoryError {
        return resizeBitmap(input, destWidth, destHeight, 0);
    }

    public static Bitmap resizeBitmap(Bitmap input, int destWidth, int destHeight, int rotation) throws OutOfMemoryError {
        int dstWidth = destWidth;
        int dstHeight = destHeight;
        int srcWidth = input.getWidth();
        int srcHeight = input.getHeight();
        if (rotation == 90 || rotation == 270) {
            dstWidth = destHeight;
            dstHeight = destWidth;
        }
        boolean needsResize = false;
        if (srcWidth > dstWidth || srcHeight > dstHeight) {
            needsResize = true;
            if (srcWidth <= srcHeight || srcWidth <= dstWidth) {
                dstWidth = (int) (((float) srcWidth) * (((float) dstHeight) / ((float) srcHeight)));
            } else {
                dstHeight = (int) (((float) srcHeight) * (((float) dstWidth) / ((float) srcWidth)));
            }
        } else {
            dstWidth = srcWidth;
            dstHeight = srcHeight;
        }
        if (!needsResize && rotation == 0) {
            return input;
        }
        Bitmap output;
        if (rotation == 0) {
            output = Bitmap.createScaledBitmap(input, dstWidth, dstHeight, true);
        } else {
            Matrix matrix = new Matrix();
            matrix.postScale(((float) dstWidth) / ((float) srcWidth), ((float) dstHeight) / ((float) srcHeight));
            matrix.postRotate((float) rotation);
            output = Bitmap.createBitmap(input, 0, 0, srcWidth, srcHeight, matrix, true);
        }
        return output;
    }
}
