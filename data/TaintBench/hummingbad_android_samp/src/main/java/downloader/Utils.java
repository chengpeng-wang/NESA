package downloader;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.text.TextUtils;
import com.moceanmobile.mast.Defaults;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.io.File;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.UUID;

public class Utils {
    private static String getFilenameFromUrl(String url) {
        String filename = md5(url) + ".down";
        int index = url.lastIndexOf("/");
        if (index <= 0 || index >= url.length()) {
            return filename;
        }
        String tmpFilename = url.substring(index + 1);
        int qmarkIndex = tmpFilename.indexOf(MASTNativeAdConstants.QUESTIONMARK);
        if (qmarkIndex > 0) {
            tmpFilename = tmpFilename.substring(0, qmarkIndex - 1);
        }
        if (tmpFilename.contains(".")) {
            return tmpFilename;
        }
        return filename;
    }

    public static String parseFileName(HttpURLConnection conn) {
        String filename = "";
        String contentDispos = conn.getHeaderField("Content-Disposition");
        if (TextUtils.isEmpty(contentDispos)) {
            return getFilenameFromUrl(conn.getURL().toString());
        }
        int index = contentDispos.indexOf("filename");
        if (index > 0) {
            return contentDispos.substring(index + 10, contentDispos.length() - 1);
        }
        return getFilenameFromUrl(conn.getURL().toString());
    }

    public static String md5(String origin) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(origin.getBytes(Defaults.ENCODING_UTF_8));
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            return getUuid();
        }
    }

    private static String getUuid() {
        return UUID.randomUUID().toString().trim().replaceAll("-", "");
    }

    public static String getLocation(HttpURLConnection conn) {
        String location = conn.getHeaderField("location");
        if (TextUtils.isEmpty(location)) {
            location = conn.getHeaderField("Location");
        }
        try {
            URL url = new URL(location);
            return location;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return new StringBuilder(String.valueOf(conn.getURL().getProtocol())).append("://").append(conn.getURL().getHost()).append(location).toString();
        }
    }

    public static boolean checkApkFileExist(Context ctx, String filePath) {
        try {
            PackageInfo info = ctx.getPackageManager().getPackageArchiveInfo(filePath, 1);
            if (info == null) {
                new File(filePath).delete();
            }
            if (info != null) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
