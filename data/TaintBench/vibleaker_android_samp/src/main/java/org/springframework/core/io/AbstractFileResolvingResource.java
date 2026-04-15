package org.springframework.core.io;

import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import org.springframework.util.ResourceUtils;

public abstract class AbstractFileResolvingResource extends AbstractResource {
    public File getFile() throws IOException {
        return ResourceUtils.getFile(getURL(), getDescription());
    }

    /* access modifiers changed from: protected */
    public File getFileForLastModifiedCheck() throws IOException {
        URL url = getURL();
        if (ResourceUtils.isJarURL(url)) {
            return ResourceUtils.getFile(ResourceUtils.extractJarFileURL(url), "Jar URL");
        }
        return getFile();
    }

    /* access modifiers changed from: protected */
    public File getFile(URI uri) throws IOException {
        return ResourceUtils.getFile(uri, getDescription());
    }

    public boolean exists() {
        try {
            URL url = getURL();
            if (ResourceUtils.isFileURL(url)) {
                return getFile().exists();
            }
            URLConnection con = url.openConnection();
            customizeConnection(con);
            HttpURLConnection httpCon = con instanceof HttpURLConnection ? (HttpURLConnection) con : null;
            if (httpCon != null) {
                int code = httpCon.getResponseCode();
                if (code == Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
                    return true;
                }
                if (code == 404) {
                    return false;
                }
            }
            if (con.getContentLength() >= 0) {
                return true;
            }
            if (httpCon != null) {
                httpCon.disconnect();
                return false;
            }
            getInputStream().close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean isReadable() {
        try {
            if (!ResourceUtils.isFileURL(getURL())) {
                return true;
            }
            File file = getFile();
            if (!file.canRead() || file.isDirectory()) {
                return false;
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public long contentLength() throws IOException {
        URL url = getURL();
        if (ResourceUtils.isFileURL(url)) {
            return getFile().length();
        }
        URLConnection con = url.openConnection();
        customizeConnection(con);
        return (long) con.getContentLength();
    }

    public long lastModified() throws IOException {
        URL url = getURL();
        if (ResourceUtils.isFileURL(url) || ResourceUtils.isJarURL(url)) {
            return super.lastModified();
        }
        URLConnection con = url.openConnection();
        customizeConnection(con);
        return con.getLastModified();
    }

    /* access modifiers changed from: protected */
    public void customizeConnection(URLConnection con) throws IOException {
        ResourceUtils.useCachesIfNecessary(con);
        if (con instanceof HttpURLConnection) {
            customizeConnection((HttpURLConnection) con);
        }
    }

    /* access modifiers changed from: protected */
    public void customizeConnection(HttpURLConnection con) throws IOException {
        con.setRequestMethod("HEAD");
    }
}
