package com.xmpp.client.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DecodeUtils {
    public static Bitmap decode(Context context, Uri uri, int maxW, int maxH) {
        Closeable stream = null;
        try {
            stream = context.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (stream == null) {
            System.out.println("----------null == stream------------");
            return null;
        }
        int orientation = ExifUtils.getExifOrientation(context, uri);
        System.out.println("----------orientation------------" + orientation);
        int[] imageSize = new int[2];
        boolean decoded = decodeImageBounds(stream, imageSize);
        IOUtils.closeSilently(stream);
        if (!decoded) {
            return null;
        }
        int sampleSize;
        System.out.println("----------decoded------------");
        if (maxW < 0 || maxH < 0) {
            sampleSize = 1;
        } else {
            sampleSize = computeSampleSize(imageSize[0], imageSize[1], (int) (((double) maxW) * 1.2d), (int) (((double) maxH) * 1.2d), orientation);
        }
        Options options = getDefaultOptions();
        options.inSampleSize = sampleSize;
        return decodeBitmap(context, uri, options, maxW, maxH, orientation, 0);
    }

    static Bitmap decodeBitmap(Context context, Uri uri, Options options, int maxW, int maxH, int orientation, int pass) {
        Bitmap bitmap = null;
        if (pass > 20) {
            System.out.println("----------decodeBitmap: pass > 20 ------------");
            return null;
        }
        Closeable stream = null;
        try {
            stream = context.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (stream == null) {
            System.out.println("---------decodeBitmap:-null == stream------------");
            return null;
        }
        try {
            bitmap = BitmapFactory.decodeStream(stream, null, options);
            IOUtils.closeSilently(stream);
            if (bitmap != null && maxW > 0 && maxH > 0) {
                Bitmap newBitmap = BitmapUtils.resizeBitmap(bitmap, maxW, maxH, orientation);
                if (bitmap != newBitmap) {
                    bitmap.recycle();
                }
                bitmap = newBitmap;
            }
        } catch (OutOfMemoryError e2) {
            IOUtils.closeSilently(stream);
            if (bitmap != null) {
                bitmap.recycle();
            }
            options.inSampleSize++;
            bitmap = decodeBitmap(context, uri, options, maxW, maxH, orientation, pass + 1);
        }
        return bitmap;
    }

    public static InputStream openInputStream(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        String scheme = uri.getScheme();
        if (scheme == null || "file".equals(scheme)) {
            return openFileInputStream(uri.getPath());
        }
        if ("content".equals(scheme)) {
            return openContentInputStream(context, uri);
        }
        if ("http".equals(scheme) || "https".equals(scheme)) {
            return openRemoteInputStream(uri);
        }
        return null;
    }

    public static boolean decodeImageBounds(InputStream stream, int[] outSize) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(stream, null, options);
        if (options.outHeight <= 0 || options.outWidth <= 0) {
            return false;
        }
        outSize[0] = options.outWidth;
        outSize[1] = options.outHeight;
        return true;
    }

    private static int computeSampleSize(int bitmapW, int bitmapH, int maxW, int maxH, int orientation) {
        double w;
        double h;
        if (orientation == 0 || orientation == 180) {
            w = (double) bitmapW;
            h = (double) bitmapH;
        } else {
            w = (double) bitmapH;
            h = (double) bitmapW;
        }
        return (int) Math.ceil(Math.max(w / ((double) maxW), h / ((double) maxH)));
    }

    static InputStream openFileInputStream(String path) {
        try {
            return new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    static InputStream openContentInputStream(Context context, Uri uri) {
        try {
            return context.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    static InputStream openRemoteInputStream(Uri uri) {
        try {
            URL finalUrl = new URL(uri.toString());
            try {
                HttpURLConnection connection = (HttpURLConnection) finalUrl.openConnection();
                connection.setInstanceFollowRedirects(false);
                try {
                    int code = connection.getResponseCode();
                    if (code == 301 || code == 302 || code == 303) {
                        return openRemoteInputStream(Uri.parse(connection.getHeaderField("Location")));
                    }
                    try {
                        return (InputStream) finalUrl.getContent();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                    return null;
                }
            } catch (IOException e22) {
                e22.printStackTrace();
                return null;
            }
        } catch (MalformedURLException e3) {
            e3.printStackTrace();
            return null;
        }
    }

    static Options getDefaultOptions() {
        Options options = new Options();
        options.inScaled = false;
        options.inPreferredConfig = Config.ARGB_8888;
        options.inDither = false;
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16384];
        return options;
    }
}
