package com.splunk.mint;

import java.util.ArrayList;

public class ExcludedUrls extends ArrayList<String> {
    private static final int MIN_URL_LENGTH = 5;
    private static final long serialVersionUID = -4294368218440708551L;

    public ExcludedUrls(String[] defaultExcludedUrls) {
        for (String url : defaultExcludedUrls) {
            addValue(url);
        }
    }

    public void addValue(String url) {
        if (url != null && url.length() > 5) {
            add(url);
        }
    }
}
