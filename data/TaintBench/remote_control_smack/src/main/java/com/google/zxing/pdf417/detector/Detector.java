package com.google.zxing.pdf417.detector;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.common.GridSampler;
import java.util.Arrays;
import java.util.Map;

public final class Detector {
    private static final int MAX_AVG_VARIANCE = 107;
    private static final int MAX_INDIVIDUAL_VARIANCE = 204;
    private static final int SKEW_THRESHOLD = 2;
    private static final int[] START_PATTERN = new int[]{8, 1, 1, 1, 1, 1, 1, 3};
    private static final int[] START_PATTERN_REVERSE = new int[]{3, 1, 1, 1, 1, 1, 1, 8};
    private static final int[] STOP_PATTERN = new int[]{7, 1, 1, 3, 1, 1, 1, 2, 1};
    private static final int[] STOP_PATTERN_REVERSE = new int[]{1, 2, 1, 1, 1, 3, 1, 1, 7};
    private final BinaryBitmap image;

    public Detector(BinaryBitmap image) {
        this.image = image;
    }

    public DetectorResult detect() throws NotFoundException {
        return detect(null);
    }

    public DetectorResult detect(Map<DecodeHintType, ?> map) throws NotFoundException {
        BitMatrix matrix = this.image.getBlackMatrix();
        ResultPoint[] vertices = findVertices(matrix);
        if (vertices == null) {
            vertices = findVertices180(matrix);
            if (vertices != null) {
                correctCodeWordVertices(vertices, true);
            }
        } else {
            correctCodeWordVertices(vertices, false);
        }
        if (vertices == null) {
            throw NotFoundException.getNotFoundInstance();
        }
        float moduleWidth = computeModuleWidth(vertices);
        if (moduleWidth < 1.0f) {
            throw NotFoundException.getNotFoundInstance();
        }
        int dimension = computeDimension(vertices[4], vertices[6], vertices[5], vertices[7], moduleWidth);
        if (dimension < 1) {
            throw NotFoundException.getNotFoundInstance();
        }
        return new DetectorResult(sampleGrid(matrix, vertices[4], vertices[5], vertices[6], vertices[7], dimension), new ResultPoint[]{vertices[5], vertices[4], vertices[6], vertices[7]});
    }

    private static ResultPoint[] findVertices(BitMatrix matrix) {
        int i;
        int[] loc;
        int height = matrix.getHeight();
        int width = matrix.getWidth();
        ResultPoint[] result = new ResultPoint[8];
        boolean found = false;
        int[] counters = new int[START_PATTERN.length];
        for (i = 0; i < height; i++) {
            loc = findGuardPattern(matrix, 0, i, width, false, START_PATTERN, counters);
            if (loc != null) {
                result[0] = new ResultPoint((float) loc[0], (float) i);
                result[4] = new ResultPoint((float) loc[1], (float) i);
                found = true;
                break;
            }
        }
        if (found) {
            found = false;
            for (i = height - 1; i > 0; i--) {
                loc = findGuardPattern(matrix, 0, i, width, false, START_PATTERN, counters);
                if (loc != null) {
                    result[1] = new ResultPoint((float) loc[0], (float) i);
                    result[5] = new ResultPoint((float) loc[1], (float) i);
                    found = true;
                    break;
                }
            }
        }
        counters = new int[STOP_PATTERN.length];
        if (found) {
            found = false;
            for (i = 0; i < height; i++) {
                loc = findGuardPattern(matrix, 0, i, width, false, STOP_PATTERN, counters);
                if (loc != null) {
                    result[2] = new ResultPoint((float) loc[1], (float) i);
                    result[6] = new ResultPoint((float) loc[0], (float) i);
                    found = true;
                    break;
                }
            }
        }
        if (found) {
            found = false;
            for (i = height - 1; i > 0; i--) {
                loc = findGuardPattern(matrix, 0, i, width, false, STOP_PATTERN, counters);
                if (loc != null) {
                    result[3] = new ResultPoint((float) loc[1], (float) i);
                    result[7] = new ResultPoint((float) loc[0], (float) i);
                    found = true;
                    break;
                }
            }
        }
        return found ? result : null;
    }

    private static ResultPoint[] findVertices180(BitMatrix matrix) {
        int i;
        int[] loc;
        int height = matrix.getHeight();
        int halfWidth = matrix.getWidth() >> 1;
        ResultPoint[] result = new ResultPoint[8];
        boolean found = false;
        int[] counters = new int[START_PATTERN_REVERSE.length];
        for (i = height - 1; i > 0; i--) {
            loc = findGuardPattern(matrix, halfWidth, i, halfWidth, true, START_PATTERN_REVERSE, counters);
            if (loc != null) {
                result[0] = new ResultPoint((float) loc[1], (float) i);
                result[4] = new ResultPoint((float) loc[0], (float) i);
                found = true;
                break;
            }
        }
        if (found) {
            found = false;
            for (i = 0; i < height; i++) {
                loc = findGuardPattern(matrix, halfWidth, i, halfWidth, true, START_PATTERN_REVERSE, counters);
                if (loc != null) {
                    result[1] = new ResultPoint((float) loc[1], (float) i);
                    result[5] = new ResultPoint((float) loc[0], (float) i);
                    found = true;
                    break;
                }
            }
        }
        counters = new int[STOP_PATTERN_REVERSE.length];
        if (found) {
            found = false;
            for (i = height - 1; i > 0; i--) {
                loc = findGuardPattern(matrix, 0, i, halfWidth, false, STOP_PATTERN_REVERSE, counters);
                if (loc != null) {
                    result[2] = new ResultPoint((float) loc[0], (float) i);
                    result[6] = new ResultPoint((float) loc[1], (float) i);
                    found = true;
                    break;
                }
            }
        }
        if (found) {
            found = false;
            for (i = 0; i < height; i++) {
                loc = findGuardPattern(matrix, 0, i, halfWidth, false, STOP_PATTERN_REVERSE, counters);
                if (loc != null) {
                    result[3] = new ResultPoint((float) loc[0], (float) i);
                    result[7] = new ResultPoint((float) loc[1], (float) i);
                    found = true;
                    break;
                }
            }
        }
        return found ? result : null;
    }

    private static void correctCodeWordVertices(ResultPoint[] vertices, boolean upsideDown) {
        float deltax;
        float skew = vertices[4].getY() - vertices[6].getY();
        if (upsideDown) {
            skew = -skew;
        }
        if (skew > 2.0f) {
            deltax = vertices[6].getX() - vertices[0].getX();
            vertices[4] = new ResultPoint(vertices[4].getX(), vertices[4].getY() + (((vertices[4].getX() - vertices[0].getX()) * (vertices[6].getY() - vertices[0].getY())) / deltax));
        } else if ((-skew) > 2.0f) {
            deltax = vertices[2].getX() - vertices[4].getX();
            vertices[6] = new ResultPoint(vertices[6].getX(), vertices[6].getY() - (((vertices[2].getX() - vertices[6].getX()) * (vertices[2].getY() - vertices[4].getY())) / deltax));
        }
        skew = vertices[7].getY() - vertices[5].getY();
        if (upsideDown) {
            skew = -skew;
        }
        if (skew > 2.0f) {
            deltax = vertices[7].getX() - vertices[1].getX();
            vertices[5] = new ResultPoint(vertices[5].getX(), vertices[5].getY() + (((vertices[5].getX() - vertices[1].getX()) * (vertices[7].getY() - vertices[1].getY())) / deltax));
        } else if ((-skew) > 2.0f) {
            deltax = vertices[3].getX() - vertices[5].getX();
            vertices[7] = new ResultPoint(vertices[7].getX(), vertices[7].getY() - (((vertices[3].getX() - vertices[7].getX()) * (vertices[3].getY() - vertices[5].getY())) / deltax));
        }
    }

    private static float computeModuleWidth(ResultPoint[] vertices) {
        return (((ResultPoint.distance(vertices[0], vertices[4]) + ResultPoint.distance(vertices[1], vertices[5])) / 34.0f) + ((ResultPoint.distance(vertices[6], vertices[2]) + ResultPoint.distance(vertices[7], vertices[3])) / 36.0f)) / 2.0f;
    }

    private static int computeDimension(ResultPoint topLeft, ResultPoint topRight, ResultPoint bottomLeft, ResultPoint bottomRight, float moduleWidth) {
        return ((((round(ResultPoint.distance(topLeft, topRight) / moduleWidth) + round(ResultPoint.distance(bottomLeft, bottomRight) / moduleWidth)) >> 1) + 8) / 17) * 17;
    }

    private static BitMatrix sampleGrid(BitMatrix matrix, ResultPoint topLeft, ResultPoint bottomLeft, ResultPoint topRight, ResultPoint bottomRight, int dimension) throws NotFoundException {
        return GridSampler.getInstance().sampleGrid(matrix, dimension, dimension, 0.0f, 0.0f, (float) dimension, 0.0f, (float) dimension, (float) dimension, 0.0f, (float) dimension, topLeft.getX(), topLeft.getY(), topRight.getX(), topRight.getY(), bottomRight.getX(), bottomRight.getY(), bottomLeft.getX(), bottomLeft.getY());
    }

    private static int round(float d) {
        return (int) (0.5f + d);
    }

    private static int[] findGuardPattern(BitMatrix matrix, int column, int row, int width, boolean whiteFirst, int[] pattern, int[] counters) {
        Arrays.fill(counters, 0, counters.length, 0);
        int patternLength = pattern.length;
        boolean isWhite = whiteFirst;
        int counterPosition = 0;
        int patternStart = column;
        for (int x = column; x < column + width; x++) {
            if ((matrix.get(x, row) ^ isWhite) != 0) {
                counters[counterPosition] = counters[counterPosition] + 1;
            } else {
                if (counterPosition != patternLength - 1) {
                    counterPosition++;
                } else if (patternMatchVariance(counters, pattern, MAX_INDIVIDUAL_VARIANCE) < MAX_AVG_VARIANCE) {
                    return new int[]{patternStart, x};
                } else {
                    patternStart += counters[0] + counters[1];
                    System.arraycopy(counters, 2, counters, 0, patternLength - 2);
                    counters[patternLength - 2] = 0;
                    counters[patternLength - 1] = 0;
                    counterPosition--;
                }
                counters[counterPosition] = 1;
                isWhite = !isWhite;
            }
        }
        return null;
    }

    private static int patternMatchVariance(int[] counters, int[] pattern, int maxIndividualVariance) {
        int numCounters = counters.length;
        int total = 0;
        int patternLength = 0;
        for (int i = 0; i < numCounters; i++) {
            total += counters[i];
            patternLength += pattern[i];
        }
        if (total < patternLength) {
            return Integer.MAX_VALUE;
        }
        int unitBarWidth = (total << 8) / patternLength;
        maxIndividualVariance = (maxIndividualVariance * unitBarWidth) >> 8;
        int totalVariance = 0;
        for (int x = 0; x < numCounters; x++) {
            int counter = counters[x] << 8;
            int scaledPattern = pattern[x] * unitBarWidth;
            int variance = counter > scaledPattern ? counter - scaledPattern : scaledPattern - counter;
            if (variance > maxIndividualVariance) {
                return Integer.MAX_VALUE;
            }
            totalVariance += variance;
        }
        return totalVariance / total;
    }
}
