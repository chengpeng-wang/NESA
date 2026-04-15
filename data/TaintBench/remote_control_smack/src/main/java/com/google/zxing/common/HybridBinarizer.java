package com.google.zxing.common;

import android.support.v4.view.MotionEventCompat;
import com.google.zxing.Binarizer;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import java.lang.reflect.Array;

public final class HybridBinarizer extends GlobalHistogramBinarizer {
    private static final int BLOCK_SIZE = 8;
    private static final int BLOCK_SIZE_MASK = 7;
    private static final int BLOCK_SIZE_POWER = 3;
    private static final int MINIMUM_DIMENSION = 40;
    private BitMatrix matrix;

    public HybridBinarizer(LuminanceSource source) {
        super(source);
    }

    public BitMatrix getBlackMatrix() throws NotFoundException {
        if (this.matrix != null) {
            return this.matrix;
        }
        LuminanceSource source = getLuminanceSource();
        if (source.getWidth() < MINIMUM_DIMENSION || source.getHeight() < MINIMUM_DIMENSION) {
            this.matrix = super.getBlackMatrix();
        } else {
            byte[] luminances = source.getMatrix();
            int width = source.getWidth();
            int height = source.getHeight();
            int subWidth = width >> 3;
            if ((width & 7) != 0) {
                subWidth++;
            }
            int subHeight = height >> 3;
            if ((height & 7) != 0) {
                subHeight++;
            }
            int[][] blackPoints = calculateBlackPoints(luminances, subWidth, subHeight, width, height);
            BitMatrix newMatrix = new BitMatrix(width, height);
            calculateThresholdForBlock(luminances, subWidth, subHeight, width, height, blackPoints, newMatrix);
            this.matrix = newMatrix;
        }
        return this.matrix;
    }

    public Binarizer createBinarizer(LuminanceSource source) {
        return new HybridBinarizer(source);
    }

    private static void calculateThresholdForBlock(byte[] luminances, int subWidth, int subHeight, int width, int height, int[][] blackPoints, BitMatrix matrix) {
        int y = 0;
        while (y < subHeight) {
            int yoffset = y << 3;
            if (yoffset + 8 >= height) {
                yoffset = height - 8;
            }
            int x = 0;
            while (x < subWidth) {
                int xoffset = x << 3;
                if (xoffset + 8 >= width) {
                    xoffset = width - 8;
                }
                int left = x > 1 ? x : 2;
                if (left >= subWidth - 2) {
                    left = subWidth - 3;
                }
                int top = y > 1 ? y : 2;
                if (top >= subHeight - 2) {
                    top = subHeight - 3;
                }
                int sum = 0;
                for (int z = -2; z <= 2; z++) {
                    int[] blackRow = blackPoints[top + z];
                    sum += (((blackRow[left - 2] + blackRow[left - 1]) + blackRow[left]) + blackRow[left + 1]) + blackRow[left + 2];
                }
                threshold8x8Block(luminances, xoffset, yoffset, sum / 25, width, matrix);
                x++;
            }
            y++;
        }
    }

    private static void threshold8x8Block(byte[] luminances, int xoffset, int yoffset, int threshold, int stride, BitMatrix matrix) {
        int y = 0;
        int offset = (yoffset * stride) + xoffset;
        while (y < 8) {
            for (int x = 0; x < 8; x++) {
                if ((luminances[offset + x] & MotionEventCompat.ACTION_MASK) <= threshold) {
                    matrix.set(xoffset + x, yoffset + y);
                }
            }
            y++;
            offset += stride;
        }
    }

    private static int[][] calculateBlackPoints(byte[] luminances, int subWidth, int subHeight, int width, int height) {
        int[][] blackPoints = (int[][]) Array.newInstance(Integer.TYPE, new int[]{subHeight, subWidth});
        for (int y = 0; y < subHeight; y++) {
            int yoffset = y << 3;
            if (yoffset + 8 >= height) {
                yoffset = height - 8;
            }
            int x = 0;
            while (x < subWidth) {
                int xoffset = x << 3;
                if (xoffset + 8 >= width) {
                    xoffset = width - 8;
                }
                int sum = 0;
                int min = MotionEventCompat.ACTION_MASK;
                int max = 0;
                int yy = 0;
                int offset = (yoffset * width) + xoffset;
                while (yy < 8) {
                    for (int xx = 0; xx < 8; xx++) {
                        int pixel = luminances[offset + xx] & MotionEventCompat.ACTION_MASK;
                        sum += pixel;
                        if (pixel < min) {
                            min = pixel;
                        }
                        if (pixel > max) {
                            max = pixel;
                        }
                    }
                    yy++;
                    offset += width;
                }
                int average = sum >> 6;
                if (max - min <= 24) {
                    average = min >> 1;
                    if (y > 0 && x > 0) {
                        int averageNeighborBlackPoint = ((blackPoints[y - 1][x] + (blackPoints[y][x - 1] * 2)) + blackPoints[y - 1][x - 1]) >> 2;
                        if (min < averageNeighborBlackPoint) {
                            average = averageNeighborBlackPoint;
                        }
                    }
                }
                blackPoints[y][x] = average;
                x++;
            }
        }
        return blackPoints;
    }
}
