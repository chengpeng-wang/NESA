package com.mopub.nativeads;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mopub.common.logging.MoPubLog;
import com.mopub.nativeads.MoPubNativeAdPositioning.MoPubClientPositioning;
import java.util.List;

class PlacementData {
    private static final int MAX_ADS = 200;
    public static final int NOT_FOUND = -1;
    @NonNull
    private final int[] mAdjustedAdPositions = new int[200];
    private int mDesiredCount = 0;
    @NonNull
    private final int[] mDesiredInsertionPositions = new int[200];
    @NonNull
    private final int[] mDesiredOriginalPositions = new int[200];
    @NonNull
    private final NativeAd[] mNativeAds = new NativeAd[200];
    @NonNull
    private final int[] mOriginalAdPositions = new int[200];
    private int mPlacedCount = 0;

    private PlacementData(@NonNull int[] desiredInsertionPositions) {
        this.mDesiredCount = Math.min(desiredInsertionPositions.length, 200);
        System.arraycopy(desiredInsertionPositions, 0, this.mDesiredInsertionPositions, 0, this.mDesiredCount);
        System.arraycopy(desiredInsertionPositions, 0, this.mDesiredOriginalPositions, 0, this.mDesiredCount);
    }

    @NonNull
    static PlacementData fromAdPositioning(@NonNull MoPubClientPositioning adPositioning) {
        int size;
        int i;
        int i2 = 0;
        List<Integer> fixedPositions = adPositioning.getFixedPositions();
        int repeatingInterval = adPositioning.getRepeatingInterval();
        if (repeatingInterval == MoPubClientPositioning.NO_REPEAT) {
            size = fixedPositions.size();
        } else {
            size = 200;
        }
        int[] iArr = new int[size];
        int i3 = 0;
        for (Integer intValue : fixedPositions) {
            i3 = intValue.intValue() - i2;
            i = i2 + 1;
            iArr[i2] = i3;
            i2 = i;
        }
        while (i2 < size) {
            i3 = (i3 + repeatingInterval) - 1;
            i = i2 + 1;
            iArr[i2] = i3;
            i2 = i;
        }
        return new PlacementData(iArr);
    }

    @NonNull
    static PlacementData empty() {
        return new PlacementData(new int[0]);
    }

    /* access modifiers changed from: 0000 */
    public boolean shouldPlaceAd(int position) {
        if (binarySearch(this.mDesiredInsertionPositions, 0, this.mDesiredCount, position) >= 0) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: 0000 */
    public int nextInsertionPosition(int position) {
        int binarySearchGreaterThan = binarySearchGreaterThan(this.mDesiredInsertionPositions, this.mDesiredCount, position);
        if (binarySearchGreaterThan == this.mDesiredCount) {
            return -1;
        }
        return this.mDesiredInsertionPositions[binarySearchGreaterThan];
    }

    /* access modifiers changed from: 0000 */
    public int previousInsertionPosition(int position) {
        int binarySearchFirstEquals = binarySearchFirstEquals(this.mDesiredInsertionPositions, this.mDesiredCount, position);
        if (binarySearchFirstEquals == 0) {
            return -1;
        }
        return this.mDesiredInsertionPositions[binarySearchFirstEquals - 1];
    }

    /* access modifiers changed from: 0000 */
    public void placeAd(int adjustedPosition, NativeAd nativeAd) {
        int binarySearchFirstEquals = binarySearchFirstEquals(this.mDesiredInsertionPositions, this.mDesiredCount, adjustedPosition);
        if (binarySearchFirstEquals == this.mDesiredCount || this.mDesiredInsertionPositions[binarySearchFirstEquals] != adjustedPosition) {
            MoPubLog.w("Attempted to insert an ad at an invalid position");
            return;
        }
        int[] iArr;
        int i = this.mDesiredOriginalPositions[binarySearchFirstEquals];
        int binarySearchGreaterThan = binarySearchGreaterThan(this.mOriginalAdPositions, this.mPlacedCount, i);
        if (binarySearchGreaterThan < this.mPlacedCount) {
            int i2 = this.mPlacedCount - binarySearchGreaterThan;
            System.arraycopy(this.mOriginalAdPositions, binarySearchGreaterThan, this.mOriginalAdPositions, binarySearchGreaterThan + 1, i2);
            System.arraycopy(this.mAdjustedAdPositions, binarySearchGreaterThan, this.mAdjustedAdPositions, binarySearchGreaterThan + 1, i2);
            System.arraycopy(this.mNativeAds, binarySearchGreaterThan, this.mNativeAds, binarySearchGreaterThan + 1, i2);
        }
        this.mOriginalAdPositions[binarySearchGreaterThan] = i;
        this.mAdjustedAdPositions[binarySearchGreaterThan] = adjustedPosition;
        this.mNativeAds[binarySearchGreaterThan] = nativeAd;
        this.mPlacedCount++;
        i = (this.mDesiredCount - binarySearchFirstEquals) - 1;
        System.arraycopy(this.mDesiredInsertionPositions, binarySearchFirstEquals + 1, this.mDesiredInsertionPositions, binarySearchFirstEquals, i);
        System.arraycopy(this.mDesiredOriginalPositions, binarySearchFirstEquals + 1, this.mDesiredOriginalPositions, binarySearchFirstEquals, i);
        this.mDesiredCount--;
        while (binarySearchFirstEquals < this.mDesiredCount) {
            iArr = this.mDesiredInsertionPositions;
            iArr[binarySearchFirstEquals] = iArr[binarySearchFirstEquals] + 1;
            binarySearchFirstEquals++;
        }
        for (binarySearchFirstEquals = binarySearchGreaterThan + 1; binarySearchFirstEquals < this.mPlacedCount; binarySearchFirstEquals++) {
            iArr = this.mAdjustedAdPositions;
            iArr[binarySearchFirstEquals] = iArr[binarySearchFirstEquals] + 1;
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean isPlacedAd(int position) {
        if (binarySearch(this.mAdjustedAdPositions, 0, this.mPlacedCount, position) >= 0) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public NativeAd getPlacedAd(int position) {
        int binarySearch = binarySearch(this.mAdjustedAdPositions, 0, this.mPlacedCount, position);
        if (binarySearch < 0) {
            return null;
        }
        return this.mNativeAds[binarySearch];
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public int[] getPlacedAdPositions() {
        int[] iArr = new int[this.mPlacedCount];
        System.arraycopy(this.mAdjustedAdPositions, 0, iArr, 0, this.mPlacedCount);
        return iArr;
    }

    /* access modifiers changed from: 0000 */
    public int getOriginalPosition(int position) {
        int binarySearch = binarySearch(this.mAdjustedAdPositions, 0, this.mPlacedCount, position);
        if (binarySearch < 0) {
            return position - (binarySearch ^ -1);
        }
        return -1;
    }

    /* access modifiers changed from: 0000 */
    public int getAdjustedPosition(int originalPosition) {
        return binarySearchGreaterThan(this.mOriginalAdPositions, this.mPlacedCount, originalPosition) + originalPosition;
    }

    /* access modifiers changed from: 0000 */
    public int getOriginalCount(int count) {
        if (count == 0) {
            return 0;
        }
        int originalPosition = getOriginalPosition(count - 1);
        if (originalPosition != -1) {
            return originalPosition + 1;
        }
        return -1;
    }

    /* access modifiers changed from: 0000 */
    public int getAdjustedCount(int originalCount) {
        if (originalCount == 0) {
            return 0;
        }
        return getAdjustedPosition(originalCount - 1) + 1;
    }

    /* access modifiers changed from: 0000 */
    public int clearAdsInRange(int adjustedStartRange, int adjustedEndRange) {
        int i;
        int i2;
        int i3 = 0;
        int[] iArr = new int[this.mPlacedCount];
        int[] iArr2 = new int[this.mPlacedCount];
        int i4 = 0;
        for (i = 0; i < this.mPlacedCount; i++) {
            i2 = this.mOriginalAdPositions[i];
            int i5 = this.mAdjustedAdPositions[i];
            if (adjustedStartRange <= i5 && i5 < adjustedEndRange) {
                iArr[i4] = i2;
                iArr2[i4] = i5 - i4;
                this.mNativeAds[i].destroy();
                this.mNativeAds[i] = null;
                i4++;
            } else if (i4 > 0) {
                int i6 = i - i4;
                this.mOriginalAdPositions[i6] = i2;
                this.mAdjustedAdPositions[i6] = i5 - i4;
                this.mNativeAds[i6] = this.mNativeAds[i];
            }
        }
        if (i4 == 0) {
            return 0;
        }
        i2 = binarySearchFirstEquals(this.mDesiredInsertionPositions, this.mDesiredCount, iArr2[0]);
        for (i = this.mDesiredCount - 1; i >= i2; i--) {
            this.mDesiredOriginalPositions[i + i4] = this.mDesiredOriginalPositions[i];
            this.mDesiredInsertionPositions[i + i4] = this.mDesiredInsertionPositions[i] - i4;
        }
        while (i3 < i4) {
            this.mDesiredOriginalPositions[i2 + i3] = iArr[i3];
            this.mDesiredInsertionPositions[i2 + i3] = iArr2[i3];
            i3++;
        }
        this.mDesiredCount += i4;
        this.mPlacedCount -= i4;
        return i4;
    }

    /* access modifiers changed from: 0000 */
    public void clearAds() {
        if (this.mPlacedCount != 0) {
            clearAdsInRange(0, this.mAdjustedAdPositions[this.mPlacedCount - 1] + 1);
        }
    }

    /* access modifiers changed from: 0000 */
    public void insertItem(int originalPosition) {
        int binarySearchFirstEquals;
        int[] iArr;
        for (binarySearchFirstEquals = binarySearchFirstEquals(this.mDesiredOriginalPositions, this.mDesiredCount, originalPosition); binarySearchFirstEquals < this.mDesiredCount; binarySearchFirstEquals++) {
            iArr = this.mDesiredOriginalPositions;
            iArr[binarySearchFirstEquals] = iArr[binarySearchFirstEquals] + 1;
            iArr = this.mDesiredInsertionPositions;
            iArr[binarySearchFirstEquals] = iArr[binarySearchFirstEquals] + 1;
        }
        for (binarySearchFirstEquals = binarySearchFirstEquals(this.mOriginalAdPositions, this.mPlacedCount, originalPosition); binarySearchFirstEquals < this.mPlacedCount; binarySearchFirstEquals++) {
            iArr = this.mOriginalAdPositions;
            iArr[binarySearchFirstEquals] = iArr[binarySearchFirstEquals] + 1;
            iArr = this.mAdjustedAdPositions;
            iArr[binarySearchFirstEquals] = iArr[binarySearchFirstEquals] + 1;
        }
    }

    /* access modifiers changed from: 0000 */
    public void removeItem(int originalPosition) {
        int binarySearchGreaterThan;
        int[] iArr;
        for (binarySearchGreaterThan = binarySearchGreaterThan(this.mDesiredOriginalPositions, this.mDesiredCount, originalPosition); binarySearchGreaterThan < this.mDesiredCount; binarySearchGreaterThan++) {
            iArr = this.mDesiredOriginalPositions;
            iArr[binarySearchGreaterThan] = iArr[binarySearchGreaterThan] - 1;
            iArr = this.mDesiredInsertionPositions;
            iArr[binarySearchGreaterThan] = iArr[binarySearchGreaterThan] - 1;
        }
        for (binarySearchGreaterThan = binarySearchGreaterThan(this.mOriginalAdPositions, this.mPlacedCount, originalPosition); binarySearchGreaterThan < this.mPlacedCount; binarySearchGreaterThan++) {
            iArr = this.mOriginalAdPositions;
            iArr[binarySearchGreaterThan] = iArr[binarySearchGreaterThan] - 1;
            iArr = this.mAdjustedAdPositions;
            iArr[binarySearchGreaterThan] = iArr[binarySearchGreaterThan] - 1;
        }
    }

    /* access modifiers changed from: 0000 */
    public void moveItem(int originalPosition, int newPosition) {
        removeItem(originalPosition);
        insertItem(newPosition);
    }

    private static int binarySearchFirstEquals(int[] array, int count, int value) {
        int binarySearch = binarySearch(array, 0, count, value);
        if (binarySearch < 0) {
            return binarySearch ^ -1;
        }
        int i = array[binarySearch];
        while (binarySearch >= 0 && array[binarySearch] == i) {
            binarySearch--;
        }
        return binarySearch + 1;
    }

    private static int binarySearchGreaterThan(int[] array, int count, int value) {
        int binarySearch = binarySearch(array, 0, count, value);
        if (binarySearch < 0) {
            return binarySearch ^ -1;
        }
        int i = array[binarySearch];
        while (binarySearch < count && array[binarySearch] == i) {
            binarySearch++;
        }
        return binarySearch;
    }

    private static int binarySearch(int[] array, int startIndex, int endIndex, int value) {
        int i = endIndex - 1;
        while (startIndex <= i) {
            int i2 = (startIndex + i) >>> 1;
            int i3 = array[i2];
            if (i3 < value) {
                startIndex = i2 + 1;
            } else if (i3 <= value) {
                return i2;
            } else {
                i = i2 - 1;
            }
        }
        return startIndex ^ -1;
    }
}
