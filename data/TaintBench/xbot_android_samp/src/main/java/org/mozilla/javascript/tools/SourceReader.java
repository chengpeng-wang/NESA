package org.mozilla.javascript.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.mozilla.javascript.Kit;
import org.mozilla.javascript.commonjs.module.provider.ParsedContentType;
import org.objectweb.asm.Opcodes;

public class SourceReader {
    public static URL toUrl(String path) {
        if (path.indexOf(58) >= 2) {
            try {
                return new URL(path);
            } catch (MalformedURLException e) {
            }
        }
        return null;
    }

    public static Object readFileOrUrl(String path, boolean convertToString, String defaultEncoding) throws IOException {
        String encoding;
        String contentType;
        int capacityHint;
        URL url = toUrl(path);
        InputStream is = null;
        if (url == null) {
            try {
                File file = new File(path);
                encoding = null;
                contentType = null;
                capacityHint = (int) file.length();
                is = new FileInputStream(file);
            } catch (Throwable th) {
                if (is != null) {
                    is.close();
                }
            }
        } else {
            URLConnection uc = url.openConnection();
            is = uc.getInputStream();
            if (convertToString) {
                ParsedContentType pct = new ParsedContentType(uc.getContentType());
                contentType = pct.getContentType();
                encoding = pct.getEncoding();
            } else {
                encoding = null;
                contentType = null;
            }
            capacityHint = uc.getContentLength();
            if (capacityHint > 1048576) {
                capacityHint = -1;
            }
        }
        if (capacityHint <= 0) {
            capacityHint = Opcodes.ACC_SYNTHETIC;
        }
        Object data = Kit.readStream(is, capacityHint);
        if (is != null) {
            is.close();
        }
        if (!convertToString) {
            return data;
        }
        if (encoding == null) {
            if (data.length > 3 && data[0] == (byte) -1 && data[1] == (byte) -2 && data[2] == (byte) 0 && data[3] == (byte) 0) {
                encoding = "UTF-32LE";
            } else if (data.length > 3 && data[0] == (byte) 0 && data[1] == (byte) 0 && data[2] == (byte) -2 && data[3] == (byte) -1) {
                encoding = "UTF-32BE";
            } else if (data.length > 2 && data[0] == (byte) -17 && data[1] == (byte) -69 && data[2] == (byte) -65) {
                encoding = "UTF-8";
            } else if (data.length > 1 && data[0] == (byte) -1 && data[1] == (byte) -2) {
                encoding = "UTF-16LE";
            } else if (data.length > 1 && data[0] == (byte) -2 && data[1] == (byte) -1) {
                encoding = "UTF-16BE";
            } else {
                encoding = defaultEncoding;
                if (encoding == null) {
                    if (url == null) {
                        encoding = System.getProperty("file.encoding");
                    } else if (contentType == null || !contentType.startsWith("application/")) {
                        encoding = "US-ASCII";
                    } else {
                        encoding = "UTF-8";
                    }
                }
            }
        }
        String strResult = new String(data, encoding);
        if (strResult.length() > 0 && strResult.charAt(0) == 65279) {
            strResult = strResult.substring(1);
        }
        return strResult;
    }
}
