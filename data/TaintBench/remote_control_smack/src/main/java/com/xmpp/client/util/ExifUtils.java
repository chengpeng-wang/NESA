package com.xmpp.client.util;

import android.content.ContentProviderClient;
import android.content.Context;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import java.io.IOException;

public class ExifUtils {
    public static final String[] EXIF_TAGS = new String[]{"FNumber", "DateTime", "ExposureTime", "Flash", "FocalLength", "GPSAltitude", "GPSAltitudeRef", "GPSDateStamp", "GPSLatitude", "GPSLatitudeRef", "GPSLongitude", "GPSLongitudeRef", "GPSProcessingMethod", "GPSTimeStamp", "ImageLength", "ImageWidth", "ISOSpeedRatings", "Make", "Model", "WhiteBalance"};

    public static int getExifOrientation(String filepath) {
        if (filepath == null) {
            return 0;
        }
        try {
            return getExifOrientation(new ExifInterface(filepath));
        } catch (IOException e) {
            return 0;
        }
    }

    public static int getExifOrientation(ExifInterface exif) {
        if (exif == null) {
            return 0;
        }
        int orientation = exif.getAttributeInt("Orientation", -1);
        if (orientation == -1) {
            return 0;
        }
        switch (orientation) {
            case 3:
                return 180;
            case 6:
                return 90;
            case 8:
                return 270;
            default:
                return 0;
        }
    }

    public static boolean loadAttributes(String filepath, Bundle out) {
        int i = 0;
        try {
            ExifInterface e = new ExifInterface(filepath);
            String[] strArr = EXIF_TAGS;
            int length = strArr.length;
            while (i < length) {
                String tag = strArr[i];
                out.putString(tag, e.getAttribute(tag));
                i++;
            }
            return true;
        } catch (IOException e1) {
            e1.printStackTrace();
            return false;
        }
    }

    public static boolean saveAttributes(String filepath, Bundle bundle) {
        try {
            ExifInterface exif = new ExifInterface(filepath);
            for (String tag : EXIF_TAGS) {
                if (bundle.containsKey(tag)) {
                    exif.setAttribute(tag, bundle.getString(tag));
                }
            }
            try {
                exif.saveAttributes();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public static String getExifOrientation(int orientation) {
        switch (orientation) {
            case 0:
                return String.valueOf(1);
            case 90:
                return String.valueOf(6);
            case 180:
                return String.valueOf(3);
            case 270:
                return String.valueOf(8);
            default:
                throw new AssertionError("invalid: " + orientation);
        }
    }

    public static int getExifOrientation(Context context, Uri uri) {
        String scheme = uri.getScheme();
        if (scheme == null || "file".equals(scheme)) {
            return getExifOrientation(uri.getPath());
        }
        if (!scheme.equals("content")) {
            return 0;
        }
        try {
            ContentProviderClient provider = context.getContentResolver().acquireContentProviderClient(uri);
            if (provider == null) {
                return 0;
            }
            try {
                Cursor result = provider.query(uri, new String[]{"orientation", "_data"}, null, null, null);
                if (result == null) {
                    return 0;
                }
                int orientationColumnIndex = result.getColumnIndex("orientation");
                int dataColumnIndex = result.getColumnIndex("_data");
                try {
                    if (result.getCount() > 0) {
                        result.moveToFirst();
                        int rotation = 0;
                        if (orientationColumnIndex > -1) {
                            rotation = result.getInt(orientationColumnIndex);
                        }
                        if (dataColumnIndex > -1) {
                            rotation |= getExifOrientation(result.getString(dataColumnIndex));
                        }
                        result.close();
                        return rotation;
                    }
                    result.close();
                    return 0;
                } catch (Throwable th) {
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        } catch (SecurityException e2) {
            return 0;
        }
    }
}
