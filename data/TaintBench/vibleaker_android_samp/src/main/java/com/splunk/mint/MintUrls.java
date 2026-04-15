package com.splunk.mint;

class MintUrls {
    private static volatile StringBuffer URL;

    public MintUrls(String baseUrl, String apiKey) {
        URL = new StringBuffer();
        createUrl(baseUrl, apiKey);
    }

    private synchronized void createUrl(String baseUrl, String apiKey) {
        if (baseUrl == null) {
            URL.append("https://");
            URL.append(apiKey);
            URL.append(".api.splkmobile.com/");
            URL.append(Properties.REST_VERSION);
            URL.append("/");
            URL.append(apiKey);
            URL.append("/");
            URL.append(Properties.UID);
            URL.append("/");
        } else if (baseUrl.length() > 0 && baseUrl.startsWith("http")) {
            URL.append(removeLastSlashFromEnd(baseUrl));
            URL.append("/");
            URL.append(Properties.REST_VERSION);
            URL.append("/");
            URL.append(apiKey);
            URL.append("/");
            URL.append(Properties.UID);
            URL.append("/");
        }
    }

    public static synchronized String getURL() {
        String stringBuffer;
        synchronized (MintUrls.class) {
            if (URL != null) {
                stringBuffer = URL.toString();
            } else {
                stringBuffer = "";
            }
        }
        return stringBuffer;
    }

    public static synchronized String getURL(int numOfErrors, int numOfActions) {
        String str;
        synchronized (MintUrls.class) {
            if (URL != null) {
                str = URL.toString() + String.valueOf(numOfErrors) + "/" + String.valueOf(numOfActions);
            } else {
                str = "";
            }
        }
        return str;
    }

    private static final synchronized String removeLastSlashFromEnd(String url) {
        synchronized (MintUrls.class) {
            if (url == null) {
                url = null;
            } else if (url.endsWith("/")) {
                url = url.substring(0, url.lastIndexOf("/"));
            }
        }
        return url;
    }
}
