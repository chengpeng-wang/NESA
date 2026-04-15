package com.google.zxing.common.detector;

import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;

public final class MonochromeRectangleDetector {
    private static final int MAX_MODULES = 32;
    private final BitMatrix image;

    public MonochromeRectangleDetector(BitMatrix image) {
        this.image = image;
    }

    public ResultPoint[] detect() throws NotFoundException {
        int height = this.image.getHeight();
        int width = this.image.getWidth();
        int halfHeight = height >> 1;
        int halfWidth = width >> 1;
        int deltaY = Math.max(1, height / 256);
        int deltaX = Math.max(1, width / 256);
        int bottom = height;
        int right = width;
        int top = ((int) findCornerFromCenter(halfWidth, 0, 0, right, halfHeight, -deltaY, 0, bottom, halfWidth >> 1).getY()) - 1;
        int left = ((int) findCornerFromCenter(halfWidth, -deltaX, 0, right, halfHeight, 0, top, bottom, halfHeight >> 1).getX()) - 1;
        right = ((int) findCornerFromCenter(halfWidth, deltaX, left, right, halfHeight, 0, top, bottom, halfHeight >> 1).getX()) + 1;
        ResultPoint pointD = findCornerFromCenter(halfWidth, 0, left, right, halfHeight, deltaY, top, bottom, halfWidth >> 1);
        ResultPoint pointA = findCornerFromCenter(halfWidth, 0, left, right, halfHeight, -deltaY, top, ((int) pointD.getY()) + 1, halfWidth >> 2);
        return new ResultPoint[]{pointA, pointB, pointC, pointD};
    }

    private ResultPoint findCornerFromCenter(int centerX, int deltaX, int left, int right, int centerY, int deltaY, int top, int bottom, int maxWhiteRun) throws NotFoundException {
        int[] lastRange = null;
        int y = centerY;
        int x = centerX;
        while (y < bottom && y >= top && x < right && x >= left) {
            int[] range;
            if (deltaX == 0) {
                range = blackWhiteRange(y, maxWhiteRun, left, right, true);
            } else {
                range = blackWhiteRange(x, maxWhiteRun, top, bottom, false);
            }
            if (range != null) {
                lastRange = range;
                y += deltaY;
                x += deltaX;
            } else if (lastRange == null) {
                throw NotFoundException.getNotFoundInstance();
            } else if (deltaX == 0) {
                int lastY = y - deltaY;
                if (lastRange[0] >= centerX) {
                    return new ResultPoint((float) lastRange[1], (float) lastY);
                }
                if (lastRange[1] <= centerX) {
                    return new ResultPoint((float) lastRange[0], (float) lastY);
                }
                int i;
                if (deltaY > 0) {
                    i = lastRange[0];
                } else {
                    i = lastRange[1];
                }
                return new ResultPoint((float) i, (float) lastY);
            } else {
                int lastX = x - deltaX;
                if (lastRange[0] >= centerY) {
                    return new ResultPoint((float) lastX, (float) lastRange[1]);
                }
                if (lastRange[1] <= centerY) {
                    return new ResultPoint((float) lastX, (float) lastRange[0]);
                }
                return new ResultPoint((float) lastX, (float) (deltaX < 0 ? lastRange[0] : lastRange[1]));
            }
        }
        throw NotFoundException.getNotFoundInstance();
    }

    /* JADX WARNING: Missing block: B:13:0x002f, code skipped:
            r4 = r2;
     */
    /* JADX WARNING: Missing block: B:14:0x0030, code skipped:
            r2 = r2 - 1;
     */
    /* JADX WARNING: Missing block: B:15:0x0032, code skipped:
            if (r2 < r10) goto L_0x003e;
     */
    /* JADX WARNING: Missing block: B:16:0x0034, code skipped:
            if (r12 == false) goto L_0x0046;
     */
    /* JADX WARNING: Missing block: B:18:0x003c, code skipped:
            if (r7.image.get(r2, r8) == false) goto L_0x0030;
     */
    /* JADX WARNING: Missing block: B:19:0x003e, code skipped:
            r3 = r4 - r2;
     */
    /* JADX WARNING: Missing block: B:20:0x0040, code skipped:
            if (r2 < r10) goto L_0x0044;
     */
    /* JADX WARNING: Missing block: B:21:0x0042, code skipped:
            if (r3 <= r9) goto L_0x0005;
     */
    /* JADX WARNING: Missing block: B:22:0x0044, code skipped:
            r2 = r4;
     */
    /* JADX WARNING: Missing block: B:24:0x004c, code skipped:
            if (r7.image.get(r8, r2) == false) goto L_0x0030;
     */
    /* JADX WARNING: Missing block: B:31:0x0064, code skipped:
            r4 = r1;
     */
    /* JADX WARNING: Missing block: B:32:0x0065, code skipped:
            r1 = r1 + 1;
     */
    /* JADX WARNING: Missing block: B:33:0x0067, code skipped:
            if (r1 >= r11) goto L_0x0073;
     */
    /* JADX WARNING: Missing block: B:34:0x0069, code skipped:
            if (r12 == false) goto L_0x007b;
     */
    /* JADX WARNING: Missing block: B:36:0x0071, code skipped:
            if (r7.image.get(r1, r8) == false) goto L_0x0065;
     */
    /* JADX WARNING: Missing block: B:37:0x0073, code skipped:
            r3 = r1 - r4;
     */
    /* JADX WARNING: Missing block: B:38:0x0075, code skipped:
            if (r1 >= r11) goto L_0x0079;
     */
    /* JADX WARNING: Missing block: B:39:0x0077, code skipped:
            if (r3 <= r9) goto L_0x000a;
     */
    /* JADX WARNING: Missing block: B:40:0x0079, code skipped:
            r1 = r4;
     */
    /* JADX WARNING: Missing block: B:42:0x0081, code skipped:
            if (r7.image.get(r8, r1) == false) goto L_0x0065;
     */
    private int[] blackWhiteRange(int r8, int r9, int r10, int r11, boolean r12) {
        /*
        r7 = this;
        r5 = r10 + r11;
        r0 = r5 >> 1;
        r2 = r0;
    L_0x0005:
        if (r2 >= r10) goto L_0x001a;
    L_0x0007:
        r2 = r2 + 1;
        r1 = r0;
    L_0x000a:
        if (r1 < r11) goto L_0x004f;
    L_0x000c:
        r1 = r1 + -1;
        if (r1 <= r2) goto L_0x0084;
    L_0x0010:
        r5 = 2;
        r5 = new int[r5];
        r6 = 0;
        r5[r6] = r2;
        r6 = 1;
        r5[r6] = r1;
    L_0x0019:
        return r5;
    L_0x001a:
        if (r12 == 0) goto L_0x0027;
    L_0x001c:
        r5 = r7.image;
        r5 = r5.get(r2, r8);
        if (r5 == 0) goto L_0x002f;
    L_0x0024:
        r2 = r2 + -1;
        goto L_0x0005;
    L_0x0027:
        r5 = r7.image;
        r5 = r5.get(r8, r2);
        if (r5 != 0) goto L_0x0024;
    L_0x002f:
        r4 = r2;
    L_0x0030:
        r2 = r2 + -1;
        if (r2 < r10) goto L_0x003e;
    L_0x0034:
        if (r12 == 0) goto L_0x0046;
    L_0x0036:
        r5 = r7.image;
        r5 = r5.get(r2, r8);
        if (r5 == 0) goto L_0x0030;
    L_0x003e:
        r3 = r4 - r2;
        if (r2 < r10) goto L_0x0044;
    L_0x0042:
        if (r3 <= r9) goto L_0x0005;
    L_0x0044:
        r2 = r4;
        goto L_0x0007;
    L_0x0046:
        r5 = r7.image;
        r5 = r5.get(r8, r2);
        if (r5 == 0) goto L_0x0030;
    L_0x004e:
        goto L_0x003e;
    L_0x004f:
        if (r12 == 0) goto L_0x005c;
    L_0x0051:
        r5 = r7.image;
        r5 = r5.get(r1, r8);
        if (r5 == 0) goto L_0x0064;
    L_0x0059:
        r1 = r1 + 1;
        goto L_0x000a;
    L_0x005c:
        r5 = r7.image;
        r5 = r5.get(r8, r1);
        if (r5 != 0) goto L_0x0059;
    L_0x0064:
        r4 = r1;
    L_0x0065:
        r1 = r1 + 1;
        if (r1 >= r11) goto L_0x0073;
    L_0x0069:
        if (r12 == 0) goto L_0x007b;
    L_0x006b:
        r5 = r7.image;
        r5 = r5.get(r1, r8);
        if (r5 == 0) goto L_0x0065;
    L_0x0073:
        r3 = r1 - r4;
        if (r1 >= r11) goto L_0x0079;
    L_0x0077:
        if (r3 <= r9) goto L_0x000a;
    L_0x0079:
        r1 = r4;
        goto L_0x000c;
    L_0x007b:
        r5 = r7.image;
        r5 = r5.get(r8, r1);
        if (r5 == 0) goto L_0x0065;
    L_0x0083:
        goto L_0x0073;
    L_0x0084:
        r5 = 0;
        goto L_0x0019;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.common.detector.MonochromeRectangleDetector.blackWhiteRange(int, int, int, int, boolean):int[]");
    }
}
