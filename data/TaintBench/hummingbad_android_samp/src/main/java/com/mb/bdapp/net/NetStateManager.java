package com.mb.bdapp.net;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import org.apache.http.HttpHost;

public class NetStateManager {
    private static Context mContext;

    public static void setContext(Context context) {
        mContext = context.getApplicationContext();
    }

    public static HttpHost getAPN() {
        HttpHost proxy = null;
        Uri uri = Uri.parse("content://telephony/carriers/preferapn");
        Cursor mCursor = null;
        if (mContext != null) {
            mCursor = mContext.getContentResolver().query(uri, null, null, null, null);
        }
        if (mCursor != null && mCursor.moveToFirst()) {
            String proxyStr = mCursor.getString(mCursor.getColumnIndex("proxy"));
            if (proxyStr != null && proxyStr.trim().length() > 0) {
                proxy = new HttpHost(proxyStr, 80);
            }
            mCursor.close();
        }
        return proxy;
    }
}
