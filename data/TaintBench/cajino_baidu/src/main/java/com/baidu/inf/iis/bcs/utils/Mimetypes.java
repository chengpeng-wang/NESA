package com.baidu.inf.iis.bcs.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Mimetypes {
    public static final String MIMETYPE_GZIP = "application/x-gzip";
    public static final String MIMETYPE_HTML = "text/html";
    public static final String MIMETYPE_OCTET_STREAM = "application/octet-stream";
    public static final String MIMETYPE_XML = "application/xml";
    private static final Log log = LogFactory.getLog(Mimetypes.class);
    private static Mimetypes mimetypes = null;
    private HashMap<String, String> extensionToMimetypeMap = new HashMap();

    public static synchronized Mimetypes getInstance() {
        Mimetypes mimetypes;
        synchronized (Mimetypes.class) {
            if (mimetypes != null) {
                mimetypes = mimetypes;
            } else {
                mimetypes = new Mimetypes();
                InputStream resourceAsStream = mimetypes.getClass().getResourceAsStream("/mime.types");
                if (resourceAsStream != null) {
                    if (log.isDebugEnabled()) {
                        log.info("Loading mime types from file in the classpath: mime.types");
                    }
                    try {
                        mimetypes.loadAndReplaceMimetypes(resourceAsStream);
                    } catch (IOException e) {
                        if (log.isErrorEnabled()) {
                            log.error("Failed to load mime types from file in the classpath: mime.types", e);
                        }
                    }
                } else if (log.isWarnEnabled()) {
                    log.warn("Unable to find 'mime.types' file in classpath");
                }
                mimetypes = mimetypes;
            }
        }
        return mimetypes;
    }

    public String getMimetype(File file) {
        return getMimetype(file.getName());
    }

    public String getMimetype(String str) {
        int lastIndexOf = str.lastIndexOf(".");
        if (lastIndexOf > 0 && lastIndexOf + 1 < str.length()) {
            String substring = str.substring(lastIndexOf + 1);
            if (this.extensionToMimetypeMap.keySet().contains(substring)) {
                String str2 = (String) this.extensionToMimetypeMap.get(substring);
                if (!log.isDebugEnabled()) {
                    return str2;
                }
                log.info("Recognised extension '" + substring + "', mimetype is: '" + str2 + "'");
                return str2;
            } else if (log.isDebugEnabled()) {
                log.info("Extension '" + substring + "' is unrecognized in mime type listing" + ", using default mime type: '" + "application/octet-stream" + "'");
            }
        } else if (log.isDebugEnabled()) {
            log.info("File name has no extension, mime type cannot be recognised for: " + str);
        }
        return "application/octet-stream";
    }

    public void loadAndReplaceMimetypes(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine != null) {
                readLine = readLine.trim();
                if (!(readLine.startsWith("#") || readLine.length() == 0)) {
                    StringTokenizer stringTokenizer = new StringTokenizer(readLine, " \t");
                    if (stringTokenizer.countTokens() > 1) {
                        readLine = stringTokenizer.nextToken();
                        while (stringTokenizer.hasMoreTokens()) {
                            String nextToken = stringTokenizer.nextToken();
                            this.extensionToMimetypeMap.put(nextToken, readLine);
                            if (log.isDebugEnabled()) {
                                log.info("Setting mime type for extension '" + nextToken + "' to '" + readLine + "'");
                            }
                        }
                    } else if (log.isDebugEnabled()) {
                        log.info("Ignoring mimetype with no associated file extensions: '" + readLine + "'");
                    }
                }
            } else {
                return;
            }
        }
    }
}
