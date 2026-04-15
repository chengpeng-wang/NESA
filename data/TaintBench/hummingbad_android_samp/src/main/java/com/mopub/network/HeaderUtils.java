package com.mopub.network;

import android.support.annotation.Nullable;
import com.mopub.common.util.ResponseHeader;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpResponse;

public class HeaderUtils {
    @Nullable
    public static String extractHeader(Map<String, String> headers, ResponseHeader responseHeader) {
        return (String) headers.get(responseHeader.getKey());
    }

    public static Integer extractIntegerHeader(Map<String, String> headers, ResponseHeader responseHeader) {
        return formatIntHeader(extractHeader((Map) headers, responseHeader));
    }

    public static boolean extractBooleanHeader(Map<String, String> headers, ResponseHeader responseHeader, boolean defaultValue) {
        return formatBooleanHeader(extractHeader((Map) headers, responseHeader), defaultValue);
    }

    public static Integer extractPercentHeader(Map<String, String> headers, ResponseHeader responseHeader) {
        return formatPercentHeader(extractHeader((Map) headers, responseHeader));
    }

    @Nullable
    public static String extractPercentHeaderString(Map<String, String> headers, ResponseHeader responseHeader) {
        Integer extractPercentHeader = extractPercentHeader(headers, responseHeader);
        return extractPercentHeader != null ? extractPercentHeader.toString() : null;
    }

    public static String extractHeader(HttpResponse response, ResponseHeader responseHeader) {
        Header firstHeader = response.getFirstHeader(responseHeader.getKey());
        return firstHeader != null ? firstHeader.getValue() : null;
    }

    public static boolean extractBooleanHeader(HttpResponse response, ResponseHeader responseHeader, boolean defaultValue) {
        return formatBooleanHeader(extractHeader(response, responseHeader), defaultValue);
    }

    public static Integer extractIntegerHeader(HttpResponse response, ResponseHeader responseHeader) {
        return formatIntHeader(extractHeader(response, responseHeader));
    }

    public static int extractIntHeader(HttpResponse response, ResponseHeader responseHeader, int defaultValue) {
        Integer extractIntegerHeader = extractIntegerHeader(response, responseHeader);
        return extractIntegerHeader == null ? defaultValue : extractIntegerHeader.intValue();
    }

    private static boolean formatBooleanHeader(@Nullable String headerValue, boolean defaultValue) {
        return headerValue == null ? defaultValue : headerValue.equals("1");
    }

    private static Integer formatIntHeader(String headerValue) {
        NumberFormat instance = NumberFormat.getInstance(Locale.US);
        instance.setParseIntegerOnly(true);
        try {
            return Integer.valueOf(instance.parse(headerValue.trim()).intValue());
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    private static Integer formatPercentHeader(@Nullable String headerValue) {
        if (headerValue == null) {
            return null;
        }
        Integer formatIntHeader = formatIntHeader(headerValue.replace("%", ""));
        if (formatIntHeader == null || formatIntHeader.intValue() < 0 || formatIntHeader.intValue() > 100) {
            return null;
        }
        return formatIntHeader;
    }
}
