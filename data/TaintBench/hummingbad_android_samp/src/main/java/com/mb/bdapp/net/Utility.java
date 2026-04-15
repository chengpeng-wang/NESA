package com.mb.bdapp.net;

import com.mb.bdapp.util.LogUtil;
import com.moceanmobile.mast.Defaults;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Utility {
    public static String encodeUrl(HttpParameters parameters) {
        if (parameters == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (int loc = 0; loc < parameters.size(); loc++) {
            if (first) {
                first = false;
            } else {
                sb.append(MASTNativeAdConstants.AMPERSAND);
            }
            String _key = parameters.getKey(loc);
            if (parameters.getValue(_key) == null) {
                LogUtil.i("encodeUrl", "key:" + _key + " 's value is null");
            } else {
                try {
                    sb.append(new StringBuilder(String.valueOf(URLEncoder.encode(parameters.getKey(loc), Defaults.ENCODING_UTF_8))).append(MASTNativeAdConstants.EQUAL).append(URLEncoder.encode(parameters.getValue(loc), Defaults.ENCODING_UTF_8)).toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public static String encodeParameters(HttpParameters httpParams) {
        if (httpParams == null || isBundleEmpty(httpParams)) {
            return "";
        }
        StringBuilder buf = new StringBuilder();
        int j = 0;
        for (int loc = 0; loc < httpParams.size(); loc++) {
            String key = httpParams.getKey(loc);
            if (j != 0) {
                buf.append(MASTNativeAdConstants.AMPERSAND);
            }
            try {
                buf.append(URLEncoder.encode(key, Defaults.ENCODING_UTF_8)).append(MASTNativeAdConstants.EQUAL).append(URLEncoder.encode(httpParams.getValue(key), Defaults.ENCODING_UTF_8));
            } catch (UnsupportedEncodingException e) {
            }
            j++;
        }
        return buf.toString();
    }

    private static boolean isBundleEmpty(HttpParameters bundle) {
        if (bundle == null || bundle.size() == 0) {
            return true;
        }
        return false;
    }
}
