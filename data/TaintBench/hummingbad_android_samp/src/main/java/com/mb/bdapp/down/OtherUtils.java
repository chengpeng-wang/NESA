package com.mb.bdapp.down;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.mopub.volley.BuildConfig;
import java.util.Locale;
import org.apache.http.Header;
import org.apache.http.HttpResponse;

public class OtherUtils {
    private OtherUtils() {
    }

    public static String getUserAgent(Context context) {
        String webUserAgent = null;
        if (context != null) {
            try {
                webUserAgent = context.getString(((Integer) Class.forName("com.android.internal.R$string").getDeclaredField("web_user_agent").get(null)).intValue());
            } catch (Throwable th) {
            }
        }
        if (TextUtils.isEmpty(webUserAgent)) {
            webUserAgent = "Mozilla/5.0 (Linux; U; Android %s) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 %sSafari/533.1";
        }
        Locale locale = Locale.getDefault();
        StringBuffer buffer = new StringBuffer();
        String version = VERSION.RELEASE;
        if (version.length() > 0) {
            buffer.append(version);
        } else {
            buffer.append(BuildConfig.VERSION_NAME);
        }
        buffer.append("; ");
        String language = locale.getLanguage();
        if (language != null) {
            buffer.append(language.toLowerCase());
            String country = locale.getCountry();
            if (country != null) {
                buffer.append("-");
                buffer.append(country.toLowerCase());
            }
        } else {
            buffer.append("en");
        }
        if ("REL".equals(VERSION.CODENAME)) {
            String model = Build.MODEL;
            if (model.length() > 0) {
                buffer.append("; ");
                buffer.append(model);
            }
        }
        String id = Build.ID;
        if (id.length() > 0) {
            buffer.append(" Build/");
            buffer.append(id);
        }
        return String.format(webUserAgent, new Object[]{buffer, "Mobile "});
    }

    public static boolean isSupportRange(HttpResponse response) {
        if (response == null) {
            return false;
        }
        Header header = response.getFirstHeader("Accept-Ranges");
        if (header != null) {
            return "bytes".equals(header.getValue());
        }
        header = response.getFirstHeader("Content-Range");
        if (header == null) {
            return false;
        }
        String value = header.getValue();
        if (value == null || !value.startsWith("bytes")) {
            return false;
        }
        return true;
    }
}
