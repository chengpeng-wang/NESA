package com.androidquery.callback;

import com.androidquery.util.AQUtility;
import java.io.Closeable;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

public class AjaxStatus {
    public static final int AUTH_ERROR = -102;
    public static final int DATASTORE = 2;
    public static final int DEVICE = 5;
    public static final int FILE = 3;
    public static final int MEMORY = 4;
    public static final int NETWORK = 1;
    public static final int NETWORK_ERROR = -101;
    public static final int TRANSFORM_ERROR = -103;
    private DefaultHttpClient client;
    private Closeable close;
    private int code = 200;
    private HttpContext context;
    private byte[] data;
    private boolean done;
    private long duration;
    private String error;
    private File file;
    private Header[] headers;
    private boolean invalid;
    private String message = "OK";
    private boolean reauth;
    private String redirect;
    private boolean refresh;
    private int source = 1;
    private long start = System.currentTimeMillis();
    private Date time = new Date();

    public AjaxStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /* access modifiers changed from: protected */
    public AjaxStatus source(int source) {
        this.source = source;
        return this;
    }

    public AjaxStatus code(int code) {
        this.code = code;
        return this;
    }

    /* access modifiers changed from: protected */
    public AjaxStatus error(String error) {
        this.error = error;
        return this;
    }

    public AjaxStatus message(String message) {
        this.message = message;
        return this;
    }

    /* access modifiers changed from: protected */
    public AjaxStatus redirect(String redirect) {
        this.redirect = redirect;
        return this;
    }

    /* access modifiers changed from: protected */
    public AjaxStatus context(HttpContext context) {
        this.context = context;
        return this;
    }

    /* access modifiers changed from: protected */
    public AjaxStatus time(Date time) {
        this.time = time;
        return this;
    }

    /* access modifiers changed from: protected */
    public AjaxStatus refresh(boolean refresh) {
        this.refresh = refresh;
        return this;
    }

    /* access modifiers changed from: protected */
    public AjaxStatus reauth(boolean reauth) {
        this.reauth = reauth;
        return this;
    }

    /* access modifiers changed from: protected */
    public AjaxStatus client(DefaultHttpClient client) {
        this.client = client;
        return this;
    }

    /* access modifiers changed from: protected */
    public AjaxStatus headers(Header[] headers) {
        this.headers = headers;
        return this;
    }

    public AjaxStatus done() {
        this.duration = System.currentTimeMillis() - this.start;
        this.done = true;
        this.reauth = false;
        return this;
    }

    /* access modifiers changed from: protected */
    public AjaxStatus reset() {
        this.duration = System.currentTimeMillis() - this.start;
        this.done = false;
        close();
        return this;
    }

    /* access modifiers changed from: protected */
    public void closeLater(Closeable c) {
        this.close = c;
    }

    public void close() {
        AQUtility.close(this.close);
        this.close = null;
    }

    /* access modifiers changed from: protected */
    public AjaxStatus data(byte[] data) {
        this.data = data;
        return this;
    }

    /* access modifiers changed from: protected */
    public AjaxStatus file(File file) {
        this.file = file;
        return this;
    }

    public AjaxStatus invalidate() {
        this.invalid = true;
        return this;
    }

    /* access modifiers changed from: protected */
    public boolean getDone() {
        return this.done;
    }

    /* access modifiers changed from: protected */
    public boolean getReauth() {
        return this.reauth;
    }

    /* access modifiers changed from: protected */
    public boolean getInvalid() {
        return this.invalid;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getRedirect() {
        return this.redirect;
    }

    /* access modifiers changed from: protected */
    public byte[] getData() {
        return this.data;
    }

    /* access modifiers changed from: protected */
    public File getFile() {
        return this.file;
    }

    public Date getTime() {
        return this.time;
    }

    public boolean getRefresh() {
        return this.refresh;
    }

    public DefaultHttpClient getClient() {
        return this.client;
    }

    public long getDuration() {
        return this.duration;
    }

    public int getSource() {
        return this.source;
    }

    public String getError() {
        return this.error;
    }

    public boolean expired(long expire) {
        if (System.currentTimeMillis() - this.time.getTime() <= expire || getSource() == 1) {
            return false;
        }
        return true;
    }

    public List<Cookie> getCookies() {
        if (this.context == null) {
            return Collections.emptyList();
        }
        CookieStore store = (CookieStore) this.context.getAttribute("http.cookie-store");
        if (store == null) {
            return Collections.emptyList();
        }
        return store.getCookies();
    }

    public List<Header> getHeaders() {
        if (this.headers == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(this.headers);
    }

    public String getHeader(String name) {
        if (this.headers == null) {
            return null;
        }
        for (int i = 0; i < this.headers.length; i++) {
            if (name.equalsIgnoreCase(this.headers[i].getName())) {
                return this.headers[i].getValue();
            }
        }
        return null;
    }
}
