package com.mopub.mraid;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import com.mopub.common.MoPubHttpUrlConnection;
import com.mopub.common.Preconditions;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.AsyncTasks;
import com.mopub.common.util.Intents;
import com.mopub.common.util.ResponseHeader;
import com.mopub.common.util.Streams;
import com.mopub.common.util.Utils;
import com.mopub.common.util.VersionCode;
import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MraidNativeCommandHandler {
    public static final String ANDROID_CALENDAR_CONTENT_TYPE = "vnd.android.cursor.item/event";
    private static final String[] DATE_FORMATS = new String[]{"yyyy-MM-dd'T'HH:mm:ssZZZZZ", "yyyy-MM-dd'T'HH:mmZZZZZ"};
    private static final int MAX_NUMBER_DAYS_IN_MONTH = 31;
    @VisibleForTesting
    static final String MIME_TYPE_HEADER = "Content-Type";

    @VisibleForTesting
    static class DownloadImageAsyncTask extends AsyncTask<String, Void, Boolean> {
        private final Context mContext;
        private final DownloadImageAsyncTaskListener mListener;

        interface DownloadImageAsyncTaskListener {
            void onFailure();

            void onSuccess();
        }

        public DownloadImageAsyncTask(@NonNull Context context, @NonNull DownloadImageAsyncTaskListener listener) {
            this.mContext = context.getApplicationContext();
            this.mListener = listener;
        }

        /* access modifiers changed from: protected */
        public Boolean doInBackground(@NonNull String[] params) {
            Closeable bufferedInputStream;
            Closeable fileOutputStream;
            Boolean valueOf;
            Throwable th;
            if (params == null || params.length == 0 || params[0] == null) {
                return Boolean.valueOf(false);
            }
            File pictureStoragePath = getPictureStoragePath();
            pictureStoragePath.mkdirs();
            String str = params[0];
            URI create = URI.create(str);
            try {
                File file;
                HttpURLConnection httpUrlConnection = MoPubHttpUrlConnection.getHttpUrlConnection(str);
                bufferedInputStream = new BufferedInputStream(httpUrlConnection.getInputStream());
                try {
                    String headerField = httpUrlConnection.getHeaderField(ResponseHeader.LOCATION.getKey());
                    if (!TextUtils.isEmpty(headerField)) {
                        create = URI.create(headerField);
                    }
                    file = new File(pictureStoragePath, getFileNameForUriAndHeaders(create, httpUrlConnection.getHeaderFields()));
                    fileOutputStream = new FileOutputStream(file);
                } catch (Exception e) {
                    fileOutputStream = null;
                    try {
                        valueOf = Boolean.valueOf(false);
                        Streams.closeStream(bufferedInputStream);
                        Streams.closeStream(fileOutputStream);
                        return valueOf;
                    } catch (Throwable th2) {
                        th = th2;
                        Streams.closeStream(bufferedInputStream);
                        Streams.closeStream(fileOutputStream);
                        throw th;
                    }
                } catch (Throwable th3) {
                    Throwable th4 = th3;
                    fileOutputStream = null;
                    th = th4;
                    Streams.closeStream(bufferedInputStream);
                    Streams.closeStream(fileOutputStream);
                    throw th;
                }
                try {
                    Streams.copyContent(bufferedInputStream, fileOutputStream);
                    loadPictureIntoGalleryApp(file.toString());
                    valueOf = Boolean.valueOf(true);
                    Streams.closeStream(bufferedInputStream);
                    Streams.closeStream(fileOutputStream);
                    return valueOf;
                } catch (Exception e2) {
                    valueOf = Boolean.valueOf(false);
                    Streams.closeStream(bufferedInputStream);
                    Streams.closeStream(fileOutputStream);
                    return valueOf;
                }
            } catch (Exception e3) {
                fileOutputStream = null;
                bufferedInputStream = null;
                valueOf = Boolean.valueOf(false);
                Streams.closeStream(bufferedInputStream);
                Streams.closeStream(fileOutputStream);
                return valueOf;
            } catch (Throwable th32) {
                bufferedInputStream = null;
                th = th32;
                fileOutputStream = null;
                Streams.closeStream(bufferedInputStream);
                Streams.closeStream(fileOutputStream);
                throw th;
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Boolean success) {
            if (success == null || !success.booleanValue()) {
                this.mListener.onFailure();
            } else {
                this.mListener.onSuccess();
            }
        }

        @Nullable
        private String getFileNameForUriAndHeaders(@NonNull URI uri, @Nullable Map<String, List<String>> headers) {
            Preconditions.checkNotNull(uri);
            String path = uri.getPath();
            if (path == null || headers == null) {
                return null;
            }
            String name = new File(path).getName();
            List list = (List) headers.get("Content-Type");
            if (list == null || list.isEmpty() || list.get(0) == null) {
                return name;
            }
            for (String str : ((String) list.get(0)).split(";")) {
                if (str.contains("image/")) {
                    path = "." + str.split("/")[1];
                    if (!name.endsWith(path)) {
                        return name + path;
                    }
                    return name;
                }
            }
            return name;
        }

        private File getPictureStoragePath() {
            return new File(Environment.getExternalStorageDirectory(), "Pictures");
        }

        private void loadPictureIntoGalleryApp(String filename) {
            MoPubMediaScannerConnectionClient moPubMediaScannerConnectionClient = new MoPubMediaScannerConnectionClient(filename, null, null);
            MediaScannerConnection mediaScannerConnection = new MediaScannerConnection(this.mContext, moPubMediaScannerConnectionClient);
            moPubMediaScannerConnectionClient.setMediaScannerConnection(mediaScannerConnection);
            mediaScannerConnection.connect();
        }

        /* access modifiers changed from: 0000 */
        @Deprecated
        @VisibleForTesting
        public DownloadImageAsyncTaskListener getListener() {
            return this.mListener;
        }
    }

    private static class MoPubMediaScannerConnectionClient implements MediaScannerConnectionClient {
        private final String mFilename;
        private MediaScannerConnection mMediaScannerConnection;
        private final String mMimeType;

        /* synthetic */ MoPubMediaScannerConnectionClient(String x0, String x1, AnonymousClass1 x2) {
            this(x0, x1);
        }

        private MoPubMediaScannerConnectionClient(String filename, String mimeType) {
            this.mFilename = filename;
            this.mMimeType = mimeType;
        }

        /* access modifiers changed from: private */
        public void setMediaScannerConnection(MediaScannerConnection connection) {
            this.mMediaScannerConnection = connection;
        }

        public void onMediaScannerConnected() {
            if (this.mMediaScannerConnection != null) {
                this.mMediaScannerConnection.scanFile(this.mFilename, this.mMimeType);
            }
        }

        public void onScanCompleted(String path, Uri uri) {
            if (this.mMediaScannerConnection != null) {
                this.mMediaScannerConnection.disconnect();
            }
        }
    }

    interface MraidCommandFailureListener {
        void onFailure(MraidCommandException mraidCommandException);
    }

    /* access modifiers changed from: 0000 */
    public void createCalendarEvent(Context context, Map<String, String> params) throws MraidCommandException {
        if (isCalendarAvailable(context)) {
            try {
                Map translateJSParamsToAndroidCalendarEventMapping = translateJSParamsToAndroidCalendarEventMapping(params);
                Intent type = new Intent("android.intent.action.INSERT").setType(ANDROID_CALENDAR_CONTENT_TYPE);
                for (String str : translateJSParamsToAndroidCalendarEventMapping.keySet()) {
                    Object obj = translateJSParamsToAndroidCalendarEventMapping.get(str);
                    if (obj instanceof Long) {
                        type.putExtra(str, ((Long) obj).longValue());
                    } else if (obj instanceof Integer) {
                        type.putExtra(str, ((Integer) obj).intValue());
                    } else {
                        type.putExtra(str, (String) obj);
                    }
                }
                type.setFlags(268435456);
                context.startActivity(type);
                return;
            } catch (ActivityNotFoundException e) {
                MoPubLog.d("no calendar app installed");
                throw new MraidCommandException("Action is unsupported on this device - no calendar app installed");
            } catch (IllegalArgumentException e2) {
                MoPubLog.d("create calendar: invalid parameters " + e2.getMessage());
                throw new MraidCommandException(e2);
            } catch (Exception e22) {
                MoPubLog.d("could not create calendar event");
                throw new MraidCommandException(e22);
            }
        }
        MoPubLog.d("unsupported action createCalendarEvent for devices pre-ICS");
        throw new MraidCommandException("Action is unsupported on this device (need Android version Ice Cream Sandwich or above)");
    }

    /* access modifiers changed from: 0000 */
    public void storePicture(@NonNull Context context, @NonNull String imageUrl, @NonNull MraidCommandFailureListener failureListener) throws MraidCommandException {
        if (!isStorePictureSupported(context)) {
            MoPubLog.d("Error downloading file - the device does not have an SD card mounted, or the Android permission is not granted.");
            throw new MraidCommandException("Error downloading file  - the device does not have an SD card mounted, or the Android permission is not granted.");
        } else if (context instanceof Activity) {
            showUserDialog(context, imageUrl, failureListener);
        } else {
            Toast.makeText(context, "Downloading image to Picture gallery...", 0).show();
            downloadImage(context, imageUrl, failureListener);
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean isTelAvailable(Context context) {
        Intent intent = new Intent("android.intent.action.DIAL");
        intent.setData(Uri.parse("tel:"));
        return Intents.deviceCanHandleIntent(context, intent);
    }

    /* access modifiers changed from: 0000 */
    public boolean isSmsAvailable(Context context) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse("sms:"));
        return Intents.deviceCanHandleIntent(context, intent);
    }

    public static boolean isStorePictureSupported(Context context) {
        return "mounted".equals(Environment.getExternalStorageState()) && context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    static boolean isCalendarAvailable(Context context) {
        return VersionCode.currentApiLevel().isAtLeast(VersionCode.ICE_CREAM_SANDWICH) && Intents.deviceCanHandleIntent(context, new Intent("android.intent.action.INSERT").setType(ANDROID_CALENDAR_CONTENT_TYPE));
    }

    /* access modifiers changed from: 0000 */
    @TargetApi(11)
    public boolean isInlineVideoAvailable(@NonNull Activity activity, @NonNull View view) {
        if (VersionCode.currentApiLevel().isBelow(VersionCode.HONEYCOMB_MR1)) {
            return false;
        }
        while (view.isHardwareAccelerated() && !Utils.bitMaskContainsFlag(view.getLayerType(), 1)) {
            if (view.getParent() instanceof View) {
                view = (View) view.getParent();
            } else {
                Window window = activity.getWindow();
                if (window == null || !Utils.bitMaskContainsFlag(window.getAttributes().flags, ViewCompat.MEASURED_STATE_TOO_SMALL)) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    @TargetApi(14)
    private Map<String, Object> translateJSParamsToAndroidCalendarEventMapping(Map<String, String> params) {
        HashMap hashMap = new HashMap();
        if (params.containsKey("description") && params.containsKey("start")) {
            hashMap.put("title", params.get("description"));
            if (!params.containsKey("start") || params.get("start") == null) {
                throw new IllegalArgumentException("Invalid calendar event: start is null.");
            }
            Date parseDate = parseDate((String) params.get("start"));
            if (parseDate != null) {
                hashMap.put("beginTime", Long.valueOf(parseDate.getTime()));
                if (params.containsKey("end") && params.get("end") != null) {
                    parseDate = parseDate((String) params.get("end"));
                    if (parseDate != null) {
                        hashMap.put("endTime", Long.valueOf(parseDate.getTime()));
                    } else {
                        throw new IllegalArgumentException("Invalid calendar event: end time is malformed. Date format expecting (yyyy-MM-DDTHH:MM:SS-xx:xx) or (yyyy-MM-DDTHH:MM-xx:xx) i.e. 2013-08-14T09:00:01-08:00");
                    }
                }
                if (params.containsKey("location")) {
                    hashMap.put("eventLocation", params.get("location"));
                }
                if (params.containsKey("summary")) {
                    hashMap.put("description", params.get("summary"));
                }
                if (params.containsKey("transparency")) {
                    hashMap.put("availability", Integer.valueOf(((String) params.get("transparency")).equals("transparent") ? 1 : 0));
                }
                hashMap.put("rrule", parseRecurrenceRule(params));
                return hashMap;
            }
            throw new IllegalArgumentException("Invalid calendar event: start time is malformed. Date format expecting (yyyy-MM-DDTHH:MM:SS-xx:xx) or (yyyy-MM-DDTHH:MM-xx:xx) i.e. 2013-08-14T09:00:01-08:00");
        }
        throw new IllegalArgumentException("Missing start and description fields");
    }

    private Date parseDate(String dateTime) {
        String[] strArr = DATE_FORMATS;
        int length = strArr.length;
        Date date = null;
        int i = 0;
        while (i < length) {
            try {
                date = new SimpleDateFormat(strArr[i], Locale.US).parse(dateTime);
                if (date != null) {
                    break;
                }
                i++;
            } catch (ParseException e) {
            }
        }
        return date;
    }

    private String parseRecurrenceRule(Map<String, String> params) throws IllegalArgumentException {
        StringBuilder stringBuilder = new StringBuilder();
        if (params.containsKey("frequency")) {
            int parseInt;
            String str = (String) params.get("frequency");
            if (params.containsKey("interval")) {
                parseInt = Integer.parseInt((String) params.get("interval"));
            } else {
                parseInt = -1;
            }
            if ("daily".equals(str)) {
                stringBuilder.append("FREQ=DAILY;");
                if (parseInt != -1) {
                    stringBuilder.append("INTERVAL=" + parseInt + ";");
                }
            } else if ("weekly".equals(str)) {
                stringBuilder.append("FREQ=WEEKLY;");
                if (parseInt != -1) {
                    stringBuilder.append("INTERVAL=" + parseInt + ";");
                }
                if (params.containsKey("daysInWeek")) {
                    str = translateWeekIntegersToDays((String) params.get("daysInWeek"));
                    if (str == null) {
                        throw new IllegalArgumentException("invalid ");
                    }
                    stringBuilder.append("BYDAY=" + str + ";");
                }
            } else if ("monthly".equals(str)) {
                stringBuilder.append("FREQ=MONTHLY;");
                if (parseInt != -1) {
                    stringBuilder.append("INTERVAL=" + parseInt + ";");
                }
                if (params.containsKey("daysInMonth")) {
                    str = translateMonthIntegersToDays((String) params.get("daysInMonth"));
                    if (str == null) {
                        throw new IllegalArgumentException();
                    }
                    stringBuilder.append("BYMONTHDAY=" + str + ";");
                }
            } else {
                throw new IllegalArgumentException("frequency is only supported for daily, weekly, and monthly.");
            }
        }
        return stringBuilder.toString();
    }

    private String translateWeekIntegersToDays(String expression) throws IllegalArgumentException {
        StringBuilder stringBuilder = new StringBuilder();
        boolean[] zArr = new boolean[7];
        String[] split = expression.split(",");
        for (String parseInt : split) {
            int parseInt2 = Integer.parseInt(parseInt);
            if (parseInt2 == 7) {
                parseInt2 = 0;
            }
            if (!zArr[parseInt2]) {
                stringBuilder.append(dayNumberToDayOfWeekString(parseInt2) + ",");
                zArr[parseInt2] = true;
            }
        }
        if (split.length == 0) {
            throw new IllegalArgumentException("must have at least 1 day of the week if specifying repeating weekly");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    private String translateMonthIntegersToDays(String expression) throws IllegalArgumentException {
        StringBuilder stringBuilder = new StringBuilder();
        boolean[] zArr = new boolean[63];
        String[] split = expression.split(",");
        for (String parseInt : split) {
            int parseInt2 = Integer.parseInt(parseInt);
            if (!zArr[parseInt2 + 31]) {
                stringBuilder.append(dayNumberToDayOfMonthString(parseInt2) + ",");
                zArr[parseInt2 + 31] = true;
            }
        }
        if (split.length == 0) {
            throw new IllegalArgumentException("must have at least 1 day of the month if specifying repeating weekly");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    private String dayNumberToDayOfWeekString(int number) throws IllegalArgumentException {
        switch (number) {
            case 0:
                return "SU";
            case 1:
                return "MO";
            case 2:
                return "TU";
            case 3:
                return "WE";
            case 4:
                return "TH";
            case 5:
                return "FR";
            case 6:
                return "SA";
            default:
                throw new IllegalArgumentException("invalid day of week " + number);
        }
    }

    private String dayNumberToDayOfMonthString(int number) throws IllegalArgumentException {
        if (number != 0 && number >= -31 && number <= 31) {
            return number;
        }
        throw new IllegalArgumentException("invalid day of month " + number);
    }

    /* access modifiers changed from: 0000 */
    public void downloadImage(final Context context, String uriString, final MraidCommandFailureListener failureListener) {
        AsyncTasks.safeExecuteOnExecutor(new DownloadImageAsyncTask(context, new DownloadImageAsyncTaskListener() {
            public void onSuccess() {
                MoPubLog.d("Image successfully saved.");
            }

            public void onFailure() {
                Toast.makeText(context, "Image failed to download.", 0).show();
                MoPubLog.d("Error downloading and saving image file.");
                failureListener.onFailure(new MraidCommandException("Error downloading and saving image file."));
            }
        }), uriString);
    }

    private void showUserDialog(final Context context, final String imageUrl, final MraidCommandFailureListener failureListener) {
        new Builder(context).setTitle("Save Image").setMessage("Download image to Picture gallery?").setNegativeButton("Cancel", null).setPositiveButton("Okay", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MraidNativeCommandHandler.this.downloadImage(context, imageUrl, failureListener);
            }
        }).setCancelable(true).show();
    }
}
