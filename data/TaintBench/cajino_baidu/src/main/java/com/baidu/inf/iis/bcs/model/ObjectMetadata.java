package com.baidu.inf.iis.bcs.model;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpHeaders;

public class ObjectMetadata {
    private Map<String, Object> metadata = new HashMap();
    private Map<String, String> userMetadata = new HashMap();

    public void addUserMetadata(String str, String str2) {
        this.userMetadata.put(str, str2);
    }

    public String getCacheControl() {
        return (String) this.metadata.get(HttpHeaders.CACHE_CONTROL);
    }

    public String getContentDisposition() {
        return (String) this.metadata.get("Content-Disposition");
    }

    public String getContentEncoding() {
        return (String) this.metadata.get("Content-Encoding");
    }

    public long getContentLength() {
        if (this.metadata.get("Content-Length") == null || Long.decode((String) this.metadata.get("Content-Length")) == null) {
            return -1;
        }
        return Long.decode((String) this.metadata.get("Content-Length")).longValue();
    }

    public Date getLastModified() {
        return (Date) this.metadata.get(HttpHeaders.LAST_MODIFIED);
    }

    public void setLastModified(Date date) {
        this.metadata.put(HttpHeaders.LAST_MODIFIED, date);
    }

    public String getContentMD5() {
        return (String) this.metadata.get(HttpHeaders.CONTENT_MD5);
    }

    public String getContentType() {
        return (String) this.metadata.get("Content-Type");
    }

    public String getETag() {
        return (String) this.metadata.get(HttpHeaders.ETAG);
    }

    public Map<String, Object> getRawMetadata() {
        return Collections.unmodifiableMap(this.metadata);
    }

    public Map<String, String> getUserMetadata() {
        return this.userMetadata;
    }

    public String getVersionId() {
        return (String) this.metadata.get("x-bs-version");
    }

    public void setCacheControl(String str) {
        this.metadata.put(HttpHeaders.CACHE_CONTROL, str);
    }

    public void setContentDisposition(String str) {
        this.metadata.put("Content-Disposition", str);
    }

    public void setContentEncoding(String str) {
        this.metadata.put("Content-Encoding", str);
    }

    public void setContentLength(long j) {
        this.metadata.put("Content-Length", String.valueOf(j));
    }

    public void setContentMD5(String str) {
        this.metadata.put(HttpHeaders.CONTENT_MD5, str);
    }

    public void setContentType(String str) {
        this.metadata.put("Content-Type", str);
    }

    public void setHeader(String str, Object obj) {
        this.metadata.put(str, obj);
    }

    public void setUserMetadata(Map<String, String> map) {
        this.userMetadata = map;
    }

    public String toString() {
        return "ObjectMetadata [userMetadata=" + this.userMetadata + ", metadata=" + this.metadata + "]";
    }
}
