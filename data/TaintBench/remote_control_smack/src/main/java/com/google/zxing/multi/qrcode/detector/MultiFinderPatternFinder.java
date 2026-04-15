package com.google.zxing.multi.qrcode.detector;

import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.detector.FinderPattern;
import com.google.zxing.qrcode.detector.FinderPatternFinder;
import com.google.zxing.qrcode.detector.FinderPatternInfo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

final class MultiFinderPatternFinder extends FinderPatternFinder {
    private static final float DIFF_MODSIZE_CUTOFF = 0.5f;
    private static final float DIFF_MODSIZE_CUTOFF_PERCENT = 0.05f;
    private static final FinderPatternInfo[] EMPTY_RESULT_ARRAY = new FinderPatternInfo[0];
    private static final float MAX_MODULE_COUNT_PER_EDGE = 180.0f;
    private static final float MIN_MODULE_COUNT_PER_EDGE = 9.0f;

    private static class ModuleSizeComparator implements Comparator<FinderPattern>, Serializable {
        private ModuleSizeComparator() {
        }

        /* synthetic */ ModuleSizeComparator(ModuleSizeComparator moduleSizeComparator) {
            this();
        }

        public int compare(FinderPattern center1, FinderPattern center2) {
            float value = center2.getEstimatedModuleSize() - center1.getEstimatedModuleSize();
            if (((double) value) < 0.0d) {
                return -1;
            }
            return ((double) value) > 0.0d ? 1 : 0;
        }
    }

    MultiFinderPatternFinder(BitMatrix image) {
        super(image);
    }

    MultiFinderPatternFinder(BitMatrix image, ResultPointCallback resultPointCallback) {
        super(image, resultPointCallback);
    }

    private FinderPattern[][] selectMutipleBestPatterns() throws NotFoundException {
        List<FinderPattern> possibleCenters = getPossibleCenters();
        int size = possibleCenters.size();
        if (size < 3) {
            throw NotFoundException.getNotFoundInstance();
        } else if (size == 3) {
            FinderPattern[][] finderPatternArr = new FinderPattern[1][];
            finderPatternArr[0] = new FinderPattern[]{(FinderPattern) possibleCenters.get(0), (FinderPattern) possibleCenters.get(1), (FinderPattern) possibleCenters.get(2)};
            return finderPatternArr;
        } else {
            Collections.sort(possibleCenters, new ModuleSizeComparator());
            List<FinderPattern[]> results = new ArrayList();
            for (int i1 = 0; i1 < size - 2; i1++) {
                FinderPattern p1 = (FinderPattern) possibleCenters.get(i1);
                if (p1 != null) {
                    for (int i2 = i1 + 1; i2 < size - 1; i2++) {
                        FinderPattern p2 = (FinderPattern) possibleCenters.get(i2);
                        if (p2 != null) {
                            float vModSize12 = (p1.getEstimatedModuleSize() - p2.getEstimatedModuleSize()) / Math.min(p1.getEstimatedModuleSize(), p2.getEstimatedModuleSize());
                            if (Math.abs(p1.getEstimatedModuleSize() - p2.getEstimatedModuleSize()) > DIFF_MODSIZE_CUTOFF && vModSize12 >= DIFF_MODSIZE_CUTOFF_PERCENT) {
                                break;
                            }
                            for (int i3 = i2 + 1; i3 < size; i3++) {
                                FinderPattern p3 = (FinderPattern) possibleCenters.get(i3);
                                if (p3 != null) {
                                    float vModSize23 = (p2.getEstimatedModuleSize() - p3.getEstimatedModuleSize()) / Math.min(p2.getEstimatedModuleSize(), p3.getEstimatedModuleSize());
                                    if (Math.abs(p2.getEstimatedModuleSize() - p3.getEstimatedModuleSize()) > DIFF_MODSIZE_CUTOFF && vModSize23 >= DIFF_MODSIZE_CUTOFF_PERCENT) {
                                        break;
                                    }
                                    Object test = new FinderPattern[]{p1, p2, p3};
                                    ResultPoint.orderBestPatterns(test);
                                    FinderPatternInfo info = new FinderPatternInfo(test);
                                    float dA = ResultPoint.distance(info.getTopLeft(), info.getBottomLeft());
                                    float dC = ResultPoint.distance(info.getTopRight(), info.getBottomLeft());
                                    float dB = ResultPoint.distance(info.getTopLeft(), info.getTopRight());
                                    float estimatedModuleCount = (dA + dB) / (p1.getEstimatedModuleSize() * 2.0f);
                                    if (estimatedModuleCount <= MAX_MODULE_COUNT_PER_EDGE && estimatedModuleCount >= MIN_MODULE_COUNT_PER_EDGE && Math.abs((dA - dB) / Math.min(dA, dB)) < 0.1f) {
                                        float dCpy = (float) Math.sqrt((double) ((dA * dA) + (dB * dB)));
                                        if (Math.abs((dC - dCpy) / Math.min(dC, dCpy)) < 0.1f) {
                                            results.add(test);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (!results.isEmpty()) {
                return (FinderPattern[][]) results.toArray(new FinderPattern[results.size()][]);
            }
            throw NotFoundException.getNotFoundInstance();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0046  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00e4 A:{LOOP_END, LOOP:1: B:11:0x0039->B:46:0x00e4} */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00f9  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0041  */
    public com.google.zxing.qrcode.detector.FinderPatternInfo[] findMulti(java.util.Map<com.google.zxing.DecodeHintType, ?> r18) throws com.google.zxing.NotFoundException {
        /*
        r17 = this;
        if (r18 == 0) goto L_0x0044;
    L_0x0002:
        r14 = com.google.zxing.DecodeHintType.TRY_HARDER;
        r0 = r18;
        r14 = r0.containsKey(r14);
        if (r14 == 0) goto L_0x0044;
    L_0x000c:
        r13 = 1;
    L_0x000d:
        r5 = r17.getImage();
        r7 = r5.getHeight();
        r8 = r5.getWidth();
        r14 = (float) r7;
        r15 = 1130627072; // 0x43640000 float:228.0 double:5.586039945E-315;
        r14 = r14 / r15;
        r15 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r14 = r14 * r15;
        r4 = (int) r14;
        r14 = 3;
        if (r4 < r14) goto L_0x0026;
    L_0x0024:
        if (r13 == 0) goto L_0x0027;
    L_0x0026:
        r4 = 3;
    L_0x0027:
        r14 = 5;
        r12 = new int[r14];
        r3 = r4 + -1;
    L_0x002c:
        if (r3 < r7) goto L_0x0046;
    L_0x002e:
        r10 = r17.selectMutipleBestPatterns();
        r11 = new java.util.ArrayList;
        r11.<init>();
        r15 = r10.length;
        r14 = 0;
    L_0x0039:
        if (r14 < r15) goto L_0x00e4;
    L_0x003b:
        r14 = r11.isEmpty();
        if (r14 == 0) goto L_0x00f9;
    L_0x0041:
        r14 = EMPTY_RESULT_ARRAY;
    L_0x0043:
        return r14;
    L_0x0044:
        r13 = 0;
        goto L_0x000d;
    L_0x0046:
        r14 = 0;
        r15 = 0;
        r12[r14] = r15;
        r14 = 1;
        r15 = 0;
        r12[r14] = r15;
        r14 = 2;
        r15 = 0;
        r12[r14] = r15;
        r14 = 3;
        r15 = 0;
        r12[r14] = r15;
        r14 = 4;
        r15 = 0;
        r12[r14] = r15;
        r2 = 0;
        r6 = 0;
    L_0x005c:
        if (r6 < r8) goto L_0x006b;
    L_0x005e:
        r14 = com.google.zxing.qrcode.detector.FinderPatternFinder.foundPatternCross(r12);
        if (r14 == 0) goto L_0x0069;
    L_0x0064:
        r0 = r17;
        r0.handlePossibleCenter(r12, r3, r8);
    L_0x0069:
        r3 = r3 + r4;
        goto L_0x002c;
    L_0x006b:
        r14 = r5.get(r6, r3);
        if (r14 == 0) goto L_0x0081;
    L_0x0071:
        r14 = r2 & 1;
        r15 = 1;
        if (r14 != r15) goto L_0x0078;
    L_0x0076:
        r2 = r2 + 1;
    L_0x0078:
        r14 = r12[r2];
        r14 = r14 + 1;
        r12[r2] = r14;
    L_0x007e:
        r6 = r6 + 1;
        goto L_0x005c;
    L_0x0081:
        r14 = r2 & 1;
        if (r14 != 0) goto L_0x00dd;
    L_0x0085:
        r14 = 4;
        if (r2 != r14) goto L_0x00d4;
    L_0x0088:
        r14 = com.google.zxing.qrcode.detector.FinderPatternFinder.foundPatternCross(r12);
        if (r14 == 0) goto L_0x00b8;
    L_0x008e:
        r0 = r17;
        r1 = r0.handlePossibleCenter(r12, r3, r6);
        if (r1 != 0) goto L_0x00a2;
    L_0x0096:
        r6 = r6 + 1;
        if (r6 >= r8) goto L_0x00a0;
    L_0x009a:
        r14 = r5.get(r6, r3);
        if (r14 == 0) goto L_0x0096;
    L_0x00a0:
        r6 = r6 + -1;
    L_0x00a2:
        r2 = 0;
        r14 = 0;
        r15 = 0;
        r12[r14] = r15;
        r14 = 1;
        r15 = 0;
        r12[r14] = r15;
        r14 = 2;
        r15 = 0;
        r12[r14] = r15;
        r14 = 3;
        r15 = 0;
        r12[r14] = r15;
        r14 = 4;
        r15 = 0;
        r12[r14] = r15;
        goto L_0x007e;
    L_0x00b8:
        r14 = 0;
        r15 = 2;
        r15 = r12[r15];
        r12[r14] = r15;
        r14 = 1;
        r15 = 3;
        r15 = r12[r15];
        r12[r14] = r15;
        r14 = 2;
        r15 = 4;
        r15 = r12[r15];
        r12[r14] = r15;
        r14 = 3;
        r15 = 1;
        r12[r14] = r15;
        r14 = 4;
        r15 = 0;
        r12[r14] = r15;
        r2 = 3;
        goto L_0x007e;
    L_0x00d4:
        r2 = r2 + 1;
        r14 = r12[r2];
        r14 = r14 + 1;
        r12[r2] = r14;
        goto L_0x007e;
    L_0x00dd:
        r14 = r12[r2];
        r14 = r14 + 1;
        r12[r2] = r14;
        goto L_0x007e;
    L_0x00e4:
        r9 = r10[r14];
        com.google.zxing.ResultPoint.orderBestPatterns(r9);
        r16 = new com.google.zxing.qrcode.detector.FinderPatternInfo;
        r0 = r16;
        r0.m1310init(r9);
        r0 = r16;
        r11.add(r0);
        r14 = r14 + 1;
        goto L_0x0039;
    L_0x00f9:
        r14 = r11.size();
        r14 = new com.google.zxing.qrcode.detector.FinderPatternInfo[r14];
        r14 = r11.toArray(r14);
        r14 = (com.google.zxing.qrcode.detector.FinderPatternInfo[]) r14;
        goto L_0x0043;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.multi.qrcode.detector.MultiFinderPatternFinder.findMulti(java.util.Map):com.google.zxing.qrcode.detector.FinderPatternInfo[]");
    }
}
