package com.loopj.android.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

public class RequestParams {
    private static String ENCODING = "UTF-8";
    protected ConcurrentHashMap<String, FileWrapper> fileParams;
    protected ConcurrentHashMap<String, String> urlParams;

    private static class FileWrapper {
        public String contentType;
        public String fileName;
        public InputStream inputStream;

        public FileWrapper(InputStream inputStream, String str, String str2) {
            this.inputStream = inputStream;
            this.fileName = str;
            this.contentType = str2;
        }

        public String getFileName() {
            if (this.fileName != null) {
                return this.fileName;
            }
            return "nofilename";
        }
    }

    public RequestParams() {
        init();
    }

    public RequestParams(Map<String, String> map) {
        init();
        for (Entry entry : map.entrySet()) {
            put((String) entry.getKey(), (String) entry.getValue());
        }
    }

    public RequestParams(String str, String str2) {
        init();
        put(str, str2);
    }

    public void put(String str, String str2) {
        if (str != null && str2 != null) {
            this.urlParams.put(str, str2);
        }
    }

    public void put(String str, File file) throws FileNotFoundException {
        put(str, new FileInputStream(file), file.getName());
    }

    public void put(String str, InputStream inputStream) {
        put(str, inputStream, null);
    }

    public void put(String str, InputStream inputStream, String str2) {
        put(str, inputStream, str2, null);
    }

    public void put(String str, InputStream inputStream, String str2, String str3) {
        if (str != null && inputStream != null) {
            this.fileParams.put(str, new FileWrapper(inputStream, str2, str3));
        }
    }

    public void remove(String str) {
        this.urlParams.remove(str);
        this.fileParams.remove(str);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Entry entry : this.urlParams.entrySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("&");
            }
            stringBuilder.append((String) entry.getKey());
            stringBuilder.append("=");
            stringBuilder.append((String) entry.getValue());
        }
        for (Entry entry2 : this.fileParams.entrySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("&");
            }
            stringBuilder.append((String) entry2.getKey());
            stringBuilder.append("=");
            stringBuilder.append("FILE");
        }
        return stringBuilder.toString();
    }

    public HttpEntity getEntity() {
        if (this.fileParams.isEmpty()) {
            try {
                return new UrlEncodedFormEntity(getParamsList(), ENCODING);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }
        HttpEntity simpleMultipartEntity = new SimpleMultipartEntity();
        for (Entry entry : this.urlParams.entrySet()) {
            simpleMultipartEntity.addPart((String) entry.getKey(), (String) entry.getValue());
        }
        int size = this.fileParams.entrySet().size() - 1;
        int i = 0;
        for (Entry entry2 : this.fileParams.entrySet()) {
            FileWrapper fileWrapper = (FileWrapper) entry2.getValue();
            if (fileWrapper.inputStream != null) {
                boolean z = i == size;
                if (fileWrapper.contentType != null) {
                    simpleMultipartEntity.addPart((String) entry2.getKey(), fileWrapper.getFileName(), fileWrapper.inputStream, fileWrapper.contentType, z);
                } else {
                    simpleMultipartEntity.addPart((String) entry2.getKey(), fileWrapper.getFileName(), fileWrapper.inputStream, z);
                }
            }
            i++;
        }
        return simpleMultipartEntity;
    }

    private void init() {
        this.urlParams = new ConcurrentHashMap();
        this.fileParams = new ConcurrentHashMap();
    }

    /* access modifiers changed from: protected */
    public List<BasicNameValuePair> getParamsList() {
        LinkedList linkedList = new LinkedList();
        for (Entry entry : this.urlParams.entrySet()) {
            linkedList.add(new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue()));
        }
        return linkedList;
    }

    /* access modifiers changed from: protected */
    public String getParamString() {
        return URLEncodedUtils.format(getParamsList(), ENCODING);
    }
}
