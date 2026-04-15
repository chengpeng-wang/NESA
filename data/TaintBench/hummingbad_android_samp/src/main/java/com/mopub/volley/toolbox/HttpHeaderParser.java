package com.mopub.volley.toolbox;

import com.moceanmobile.mast.MASTNativeAdConstants;
import com.mopub.volley.Cache.Entry;
import com.mopub.volley.NetworkResponse;
import java.util.Map;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;

public class HttpHeaderParser {
    public static Entry parseCacheHeaders(NetworkResponse response) {
        long currentTimeMillis = System.currentTimeMillis();
        Map map = response.headers;
        long j = 0;
        long j2 = 0;
        long j3 = 0;
        Object obj = null;
        String str = (String) map.get("Date");
        if (str != null) {
            j = parseDateAsEpoch(str);
        }
        str = (String) map.get("Cache-Control");
        if (str != null) {
            obj = 1;
            String[] split = str.split(",");
            for (String trim : split) {
                String trim2 = trim2.trim();
                if (trim2.equals("no-cache") || trim2.equals("no-store")) {
                    return null;
                }
                if (trim2.startsWith("max-age=")) {
                    try {
                        j3 = Long.parseLong(trim2.substring(8));
                    } catch (Exception e) {
                    }
                } else if (trim2.equals("must-revalidate") || trim2.equals("proxy-revalidate")) {
                    j3 = 0;
                }
            }
        }
        long j4 = j3;
        Object obj2 = obj;
        str = (String) map.get("Expires");
        if (str != null) {
            j2 = parseDateAsEpoch(str);
        }
        str = (String) map.get("ETag");
        j3 = obj2 != null ? (j4 * 1000) + currentTimeMillis : (j <= 0 || j2 < j) ? 0 : (j2 - j) + currentTimeMillis;
        Entry entry = new Entry();
        entry.data = response.data;
        entry.etag = str;
        entry.softTtl = j3;
        entry.ttl = entry.softTtl;
        entry.serverDate = j;
        entry.responseHeaders = map;
        return entry;
    }

    public static long parseDateAsEpoch(String dateStr) {
        try {
            return DateUtils.parseDate(dateStr).getTime();
        } catch (DateParseException e) {
            return 0;
        }
    }

    public static String parseCharset(Map<String, String> headers) {
        String str = (String) headers.get(MASTNativeAdConstants.REQUEST_HEADER_CONTENT_TYPE);
        if (str != null) {
            String[] split = str.split(";");
            for (int i = 1; i < split.length; i++) {
                String[] split2 = split[i].trim().split(MASTNativeAdConstants.EQUAL);
                if (split2.length == 2 && split2[0].equals("charset")) {
                    return split2[1];
                }
            }
        }
        return "ISO-8859-1";
    }
}
