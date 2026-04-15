package com.mopub.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.moceanmobile.mast.Defaults;
import org.json.JSONObject;
import org.json.JSONTokener;

public final class HttpResponses {
    private HttpResponses() {
    }

    public static Bitmap asBitmap(DownloadResponse downloadResponse) {
        if (downloadResponse == null) {
            return null;
        }
        byte[] byteArray = downloadResponse.getByteArray();
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    public static JSONObject asJsonObject(DownloadResponse downloadResponse) {
        if (downloadResponse == null) {
            return null;
        }
        try {
            return new JSONObject(new JSONTokener(asResponseString(downloadResponse)));
        } catch (Exception e) {
            return null;
        }
    }

    public static String asResponseString(DownloadResponse downloadResponse) {
        if (downloadResponse == null) {
            return null;
        }
        try {
            return new String(downloadResponse.getByteArray(), Defaults.ENCODING_UTF_8);
        } catch (Exception e) {
            return null;
        }
    }
}
