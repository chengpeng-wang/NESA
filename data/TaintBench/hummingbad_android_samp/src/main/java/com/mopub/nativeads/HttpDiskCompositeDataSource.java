package com.mopub.nativeads;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DataSpec;
import com.google.android.exoplayer.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer.upstream.HttpDataSource;
import com.mopub.common.CacheService;
import com.mopub.common.Preconditions;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.event.BaseEvent.Category;
import com.mopub.common.event.BaseEvent.Name;
import com.mopub.common.event.BaseEvent.SamplingRate;
import com.mopub.common.event.Event;
import com.mopub.common.event.EventDetails;
import com.mopub.common.event.MoPubEvents;
import com.mopub.common.logging.MoPubLog;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeSet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpDiskCompositeDataSource implements DataSource {
    @VisibleForTesting
    static final int BLOCK_SIZE = 512000;
    @VisibleForTesting
    static final String EXPECTED_FILE_SIZE_KEY_PREFIX = "expectedsize-";
    private static final int HTTP_RESPONSE_REQUESTED_RANGE_NOT_SATISFIABLE = 416;
    @VisibleForTesting
    static final String INTERVALS_KEY_PREFIX = "intervals-sorted-";
    private static final String LENGTH = "length";
    @VisibleForTesting
    static final int LENGTH_UNBOUNDED = -1;
    private static final String START = "start";
    @Nullable
    private byte[] mCachedBytes;
    private int mDataBlockOffset;
    @Nullable
    private DataSpec mDataSpec;
    @Nullable
    private final EventDetails mEventDetails;
    @Nullable
    private Integer mExpectedFileLength;
    private boolean mHasLoggedDownloadStart;
    @NonNull
    private final HttpDataSource mHttpDataSource;
    @NonNull
    private final TreeSet<IntInterval> mIntervals;
    private boolean mIsDirty;
    private boolean mIsHttpSourceOpen;
    @Nullable
    private String mKey;
    private int mSegment;
    private int mStartInDataBlock;
    private int mStartInFile;

    public HttpDiskCompositeDataSource(@NonNull Context context, @NonNull String userAgent, @Nullable EventDetails eventDetails) {
        this(context, userAgent, eventDetails, new DefaultHttpDataSource(userAgent, null, null, 8000, 8000, false));
    }

    @VisibleForTesting
    HttpDiskCompositeDataSource(@NonNull Context context, @NonNull String userAgent, @Nullable EventDetails eventDetails, @NonNull HttpDataSource httpDataSource) {
        this.mExpectedFileLength = null;
        this.mHttpDataSource = httpDataSource;
        CacheService.initialize(context);
        this.mIntervals = new TreeSet();
        this.mEventDetails = eventDetails;
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x0104  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x00a4  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x00ef A:{Catch:{ InvalidResponseCodeException -> 0x0110 }} */
    public long open(@android.support.annotation.NonNull com.google.android.exoplayer.upstream.DataSpec r13) throws java.io.IOException {
        /*
        r12 = this;
        r2 = 512000; // 0x7d000 float:7.17465E-40 double:2.529616E-318;
        r11 = 0;
        r9 = -1;
        com.mopub.common.Preconditions.checkNotNull(r13);
        r0 = r13.uri;
        if (r0 != 0) goto L_0x000e;
    L_0x000d:
        return r9;
    L_0x000e:
        r12.mIsDirty = r11;
        r12.mDataSpec = r13;
        r0 = r13.uri;
        r0 = r0.toString();
        r12.mKey = r0;
        r0 = r12.mKey;
        if (r0 == 0) goto L_0x000d;
    L_0x001e:
        r0 = r13.absoluteStreamPosition;
        r0 = (int) r0;
        r12.mStartInFile = r0;
        r0 = r12.mStartInFile;
        r0 = r0 / r2;
        r12.mSegment = r0;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = r12.mSegment;
        r0 = r0.append(r1);
        r1 = r12.mKey;
        r0 = r0.append(r1);
        r0 = r0.toString();
        r0 = com.mopub.common.CacheService.getFromDiskCache(r0);
        r12.mCachedBytes = r0;
        r0 = r12.mStartInFile;
        r0 = r0 % r2;
        r12.mStartInDataBlock = r0;
        r12.mDataBlockOffset = r11;
        r0 = r12.mKey;
        r0 = getExpectedFileLengthFromDisk(r0);
        r12.mExpectedFileLength = r0;
        r0 = r12.mKey;
        r1 = r12.mIntervals;
        populateIntervalsFromDisk(r0, r1);
        r0 = r12.mStartInFile;
        r1 = r12.mIntervals;
        r0 = getFirstContiguousPointAfter(r0, r1);
        r1 = r12.mCachedBytes;
        if (r1 != 0) goto L_0x0143;
    L_0x0065:
        r1 = new byte[r2];
        r12.mCachedBytes = r1;
        r1 = r12.mStartInFile;
        if (r0 <= r1) goto L_0x0143;
    L_0x006d:
        r0 = new java.lang.StringBuilder;
        r1 = "Cache segment ";
        r0.<init>(r1);
        r1 = r12.mSegment;
        r0 = r0.append(r1);
        r1 = " was evicted. Invalidating cache";
        r0 = r0.append(r1);
        r0 = r0.toString();
        com.mopub.common.logging.MoPubLog.d(r0);
        r0 = r12.mIntervals;
        r0.clear();
        r0 = r13.absoluteStreamPosition;
        r0 = (int) r0;
        r8 = r0;
    L_0x0090:
        r0 = r12.mExpectedFileLength;
        if (r0 == 0) goto L_0x009c;
    L_0x0094:
        r0 = r12.mExpectedFileLength;
        r0 = r0.intValue();
        if (r8 == r0) goto L_0x012f;
    L_0x009c:
        r0 = r12.mDataSpec;
        r0 = r0.length;
        r0 = (r0 > r9 ? 1 : (r0 == r9 ? 0 : -1));
        if (r0 != 0) goto L_0x0104;
    L_0x00a4:
        r4 = r9;
    L_0x00a5:
        r0 = new com.google.android.exoplayer.upstream.DataSpec;
        r1 = r13.uri;
        r2 = (long) r8;
        r6 = r13.key;
        r7 = r13.flags;
        r0.<init>(r1, r2, r4, r6, r7);
        r1 = r12.mHttpDataSource;	 Catch:{ InvalidResponseCodeException -> 0x0110 }
        r0 = r1.open(r0);	 Catch:{ InvalidResponseCodeException -> 0x0110 }
        r2 = r12.mExpectedFileLength;	 Catch:{ InvalidResponseCodeException -> 0x0110 }
        if (r2 != 0) goto L_0x00e8;
    L_0x00bb:
        r2 = (r4 > r9 ? 1 : (r4 == r9 ? 0 : -1));
        if (r2 != 0) goto L_0x00e8;
    L_0x00bf:
        r2 = r12.mStartInFile;	 Catch:{ InvalidResponseCodeException -> 0x0110 }
        r2 = (long) r2;	 Catch:{ InvalidResponseCodeException -> 0x0110 }
        r2 = r2 + r0;
        r2 = (int) r2;	 Catch:{ InvalidResponseCodeException -> 0x0110 }
        r2 = java.lang.Integer.valueOf(r2);	 Catch:{ InvalidResponseCodeException -> 0x0110 }
        r12.mExpectedFileLength = r2;	 Catch:{ InvalidResponseCodeException -> 0x0110 }
        r2 = new java.lang.StringBuilder;	 Catch:{ InvalidResponseCodeException -> 0x0110 }
        r3 = "expectedsize-";
        r2.<init>(r3);	 Catch:{ InvalidResponseCodeException -> 0x0110 }
        r3 = r12.mKey;	 Catch:{ InvalidResponseCodeException -> 0x0110 }
        r2 = r2.append(r3);	 Catch:{ InvalidResponseCodeException -> 0x0110 }
        r2 = r2.toString();	 Catch:{ InvalidResponseCodeException -> 0x0110 }
        r3 = r12.mExpectedFileLength;	 Catch:{ InvalidResponseCodeException -> 0x0110 }
        r3 = java.lang.String.valueOf(r3);	 Catch:{ InvalidResponseCodeException -> 0x0110 }
        r3 = r3.getBytes();	 Catch:{ InvalidResponseCodeException -> 0x0110 }
        com.mopub.common.CacheService.putToDiskCache(r2, r3);	 Catch:{ InvalidResponseCodeException -> 0x0110 }
    L_0x00e8:
        r2 = 1;
        r12.mIsHttpSourceOpen = r2;	 Catch:{ InvalidResponseCodeException -> 0x0110 }
        r2 = r12.mHasLoggedDownloadStart;	 Catch:{ InvalidResponseCodeException -> 0x0110 }
        if (r2 != 0) goto L_0x0101;
    L_0x00ef:
        r2 = com.mopub.common.event.BaseEvent.Name.DOWNLOAD_START;	 Catch:{ InvalidResponseCodeException -> 0x0110 }
        r3 = com.mopub.common.event.BaseEvent.Category.NATIVE_VIDEO;	 Catch:{ InvalidResponseCodeException -> 0x0110 }
        r4 = com.mopub.common.event.BaseEvent.SamplingRate.NATIVE_VIDEO;	 Catch:{ InvalidResponseCodeException -> 0x0110 }
        r5 = r12.mEventDetails;	 Catch:{ InvalidResponseCodeException -> 0x0110 }
        r2 = com.mopub.common.event.Event.createEventFromDetails(r2, r3, r4, r5);	 Catch:{ InvalidResponseCodeException -> 0x0110 }
        com.mopub.common.event.MoPubEvents.log(r2);	 Catch:{ InvalidResponseCodeException -> 0x0110 }
        r2 = 1;
        r12.mHasLoggedDownloadStart = r2;	 Catch:{ InvalidResponseCodeException -> 0x0110 }
    L_0x0101:
        r9 = r0;
        goto L_0x000d;
    L_0x0104:
        r0 = r12.mDataSpec;
        r0 = r0.length;
        r2 = r12.mStartInFile;
        r2 = r8 - r2;
        r2 = (long) r2;
        r4 = r0 - r2;
        goto L_0x00a5;
    L_0x0110:
        r0 = move-exception;
        r1 = r0.responseCode;
        r2 = 416; // 0x1a0 float:5.83E-43 double:2.055E-321;
        if (r1 != r2) goto L_0x012e;
    L_0x0117:
        r0 = r12.mExpectedFileLength;
        if (r0 != 0) goto L_0x0123;
    L_0x011b:
        r0 = r12.mStartInFile;
        r0 = r8 - r0;
        r0 = (long) r0;
    L_0x0120:
        r12.mIsHttpSourceOpen = r11;
        goto L_0x0101;
    L_0x0123:
        r0 = r12.mExpectedFileLength;
        r0 = r0.intValue();
        r1 = r12.mStartInFile;
        r0 = r0 - r1;
        r0 = (long) r0;
        goto L_0x0120;
    L_0x012e:
        throw r0;
    L_0x012f:
        r0 = r13.length;
        r0 = (r0 > r9 ? 1 : (r0 == r9 ? 0 : -1));
        if (r0 != 0) goto L_0x0140;
    L_0x0135:
        r0 = r12.mExpectedFileLength;
        r0 = r0.intValue();
        r1 = r12.mStartInFile;
        r0 = r0 - r1;
        r0 = (long) r0;
        goto L_0x0101;
    L_0x0140:
        r0 = r13.length;
        goto L_0x0101;
    L_0x0143:
        r8 = r0;
        goto L_0x0090;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mopub.nativeads.HttpDiskCompositeDataSource.open(com.google.android.exoplayer.upstream.DataSpec):long");
    }

    private static void populateIntervalsFromDisk(@NonNull String key, @NonNull TreeSet<IntInterval> intervals) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(intervals);
        intervals.clear();
        byte[] fromDiskCache = CacheService.getFromDiskCache(new StringBuilder(INTERVALS_KEY_PREFIX).append(key).toString());
        if (fromDiskCache != null) {
            try {
                JSONArray jSONArray = new JSONArray(new String(fromDiskCache));
                int i = 0;
                while (true) {
                    int i2 = i;
                    if (i2 < jSONArray.length()) {
                        JSONObject jSONObject = new JSONObject((String) jSONArray.get(i2));
                        intervals.add(new IntInterval(jSONObject.getInt(START), jSONObject.getInt(LENGTH)));
                        i = i2 + 1;
                    } else {
                        return;
                    }
                }
            } catch (JSONException e) {
                MoPubLog.d("clearing cache since invalid json intervals found", e);
                intervals.clear();
            } catch (ClassCastException e2) {
                MoPubLog.d("clearing cache since unable to read json data");
                intervals.clear();
            }
        }
    }

    private static Integer getExpectedFileLengthFromDisk(@NonNull String key) {
        Integer num = null;
        Preconditions.checkNotNull(key);
        byte[] fromDiskCache = CacheService.getFromDiskCache(new StringBuilder(EXPECTED_FILE_SIZE_KEY_PREFIX).append(key).toString());
        if (fromDiskCache == null) {
            return num;
        }
        try {
            return Integer.valueOf(Integer.parseInt(new String(fromDiskCache)));
        } catch (NumberFormatException e) {
            return num;
        }
    }

    public void close() throws IOException {
        if (!(TextUtils.isEmpty(this.mKey) || this.mCachedBytes == null)) {
            CacheService.putToDiskCache(this.mSegment + this.mKey, this.mCachedBytes);
            addNewInterval(this.mIntervals, this.mStartInFile, this.mDataBlockOffset);
            writeIntervalsToDisk(this.mIntervals, this.mKey);
            if (this.mIsDirty && this.mExpectedFileLength != null && getFirstContiguousPointAfter(0, this.mIntervals) == this.mExpectedFileLength.intValue()) {
                MoPubEvents.log(Event.createEventFromDetails(Name.DOWNLOAD_FINISHED, Category.NATIVE_VIDEO, SamplingRate.NATIVE_VIDEO, this.mEventDetails));
            }
        }
        this.mCachedBytes = null;
        this.mHttpDataSource.close();
        this.mIsHttpSourceOpen = false;
        this.mStartInFile = 0;
        this.mDataBlockOffset = 0;
        this.mStartInDataBlock = 0;
        this.mExpectedFileLength = null;
        this.mIsDirty = false;
    }

    private static void writeIntervalsToDisk(@NonNull TreeSet<IntInterval> intervals, @NonNull String key) {
        Preconditions.checkNotNull(intervals);
        Preconditions.checkNotNull(key);
        JSONArray jSONArray = new JSONArray();
        Iterator it = intervals.iterator();
        while (it.hasNext()) {
            jSONArray.put((IntInterval) it.next());
        }
        CacheService.putToDiskCache(new StringBuilder(INTERVALS_KEY_PREFIX).append(key).toString(), jSONArray.toString().getBytes());
    }

    public int read(byte[] buffer, int offset, int length) throws IOException {
        if (length > 512000) {
            MoPubLog.d("Reading more than the block size (512000 bytes) at once is not possible. length = " + length);
            return -1;
        } else if (this.mDataSpec == null) {
            MoPubLog.d("Unable to read from data source when no spec provided");
            return -1;
        } else if (this.mCachedBytes == null) {
            MoPubLog.d("No cache set up. Call open before read.");
            return -1;
        } else {
            int i = (512000 - this.mStartInDataBlock) - this.mDataBlockOffset;
            int firstContiguousPointAfter = getFirstContiguousPointAfter(this.mStartInFile + this.mDataBlockOffset, this.mIntervals);
            int min = Math.min((firstContiguousPointAfter - this.mStartInFile) - this.mDataBlockOffset, length);
            if (!areBytesAvailableInCache(firstContiguousPointAfter, this.mStartInFile, this.mDataBlockOffset)) {
                min = 0;
            } else if (min <= i) {
                System.arraycopy(this.mCachedBytes, this.mStartInDataBlock + this.mDataBlockOffset, buffer, offset, min);
                this.mDataBlockOffset += min;
                min += 0;
            } else {
                System.arraycopy(this.mCachedBytes, this.mStartInDataBlock + this.mDataBlockOffset, buffer, offset, i);
                this.mDataBlockOffset += i;
                int i2 = i + 0;
                writeCacheToDiskAndClearVariables();
                this.mCachedBytes = CacheService.getFromDiskCache(this.mSegment + this.mKey);
                if (this.mCachedBytes == null) {
                    MoPubLog.d("Unexpected cache miss. Invalidating cache");
                    this.mIntervals.clear();
                    this.mCachedBytes = new byte[512000];
                    this.mHttpDataSource.close();
                    this.mHttpDataSource.open(new DataSpec(this.mDataSpec.uri, (long) (this.mStartInFile + this.mDataBlockOffset), -1, this.mDataSpec.key, this.mDataSpec.flags));
                    this.mIsHttpSourceOpen = true;
                    min = i2;
                } else {
                    System.arraycopy(this.mCachedBytes, this.mStartInDataBlock + this.mDataBlockOffset, buffer, offset + i2, min - i2);
                    this.mDataBlockOffset += min - i2;
                }
            }
            int i3 = length - min;
            if (i3 <= 0) {
                return min;
            }
            this.mIsDirty = true;
            if (this.mIsHttpSourceOpen) {
                i3 = this.mHttpDataSource.read(buffer, offset + min, i3);
                i = (512000 - this.mStartInDataBlock) - this.mDataBlockOffset;
                if (i < i3) {
                    System.arraycopy(buffer, offset + min, this.mCachedBytes, this.mStartInDataBlock + this.mDataBlockOffset, i);
                    this.mDataBlockOffset += i;
                    writeCacheToDiskAndClearVariables();
                    this.mCachedBytes = CacheService.getFromDiskCache(this.mSegment + this.mKey);
                    if (this.mCachedBytes == null) {
                        this.mCachedBytes = new byte[512000];
                    }
                    System.arraycopy(buffer, (offset + i) + min, this.mCachedBytes, this.mStartInDataBlock + this.mDataBlockOffset, i3 - i);
                    this.mDataBlockOffset = (i3 - i) + this.mDataBlockOffset;
                } else {
                    System.arraycopy(buffer, offset + min, this.mCachedBytes, this.mStartInDataBlock + this.mDataBlockOffset, i3);
                    this.mDataBlockOffset += i3;
                }
                return min + i3;
            }
            MoPubLog.d("end of cache reached. No http source open");
            return -1;
        }
    }

    private static boolean areBytesAvailableInCache(int farthestContiguousPoint, int startInFile, int dataBlockOffset) {
        return farthestContiguousPoint > startInFile + dataBlockOffset;
    }

    private void writeCacheToDiskAndClearVariables() {
        CacheService.putToDiskCache(this.mSegment + this.mKey, this.mCachedBytes);
        addNewInterval(this.mIntervals, this.mStartInFile, this.mDataBlockOffset);
        this.mStartInDataBlock = 0;
        this.mStartInFile += this.mDataBlockOffset;
        this.mDataBlockOffset = 0;
        this.mSegment = this.mStartInFile / 512000;
    }

    @VisibleForTesting
    static int getFirstContiguousPointAfter(int point, @NonNull TreeSet<IntInterval> intervals) {
        Preconditions.checkNotNull(intervals);
        Iterator it = intervals.iterator();
        while (it.hasNext()) {
            IntInterval intInterval = (IntInterval) it.next();
            if (intInterval.getStart() <= point) {
                point = Math.max(point, intInterval.getLength() + intInterval.getStart());
            }
        }
        return point;
    }

    @VisibleForTesting
    static void addNewInterval(@NonNull TreeSet<IntInterval> intervals, int start, int length) {
        Preconditions.checkNotNull(intervals);
        if (getFirstContiguousPointAfter(start, intervals) < start + length) {
            intervals.add(new IntInterval(start, length));
        }
    }
}
