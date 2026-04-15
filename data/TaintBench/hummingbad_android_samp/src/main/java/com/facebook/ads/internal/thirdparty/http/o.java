package com.facebook.ads.internal.thirdparty.http;

import com.moceanmobile.mast.Defaults;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class o implements Map<String, String> {
    private Map<String, String> a = new HashMap();

    public o a(Map<? extends String, ? extends String> map) {
        putAll(map);
        return this;
    }

    public String a() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : this.a.keySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(MASTNativeAdConstants.AMPERSAND);
            }
            stringBuilder.append(str);
            String str2 = (String) this.a.get(str2);
            if (str2 != null) {
                stringBuilder.append(MASTNativeAdConstants.EQUAL);
                try {
                    stringBuilder.append(URLEncoder.encode(str2, Defaults.ENCODING_UTF_8));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuilder.toString();
    }

    /* renamed from: a */
    public String get(Object obj) {
        return (String) this.a.get(obj);
    }

    /* renamed from: a */
    public String put(String str, String str2) {
        return (String) this.a.put(str, str2);
    }

    /* renamed from: b */
    public String remove(Object obj) {
        return (String) this.a.remove(obj);
    }

    public byte[] b() {
        byte[] bArr = null;
        try {
            return a().getBytes(Defaults.ENCODING_UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return bArr;
        }
    }

    public void clear() {
        this.a.clear();
    }

    public boolean containsKey(Object obj) {
        return this.a.containsKey(obj);
    }

    public boolean containsValue(Object obj) {
        return this.a.containsValue(obj);
    }

    public Set<Entry<String, String>> entrySet() {
        return this.a.entrySet();
    }

    public boolean isEmpty() {
        return this.a.isEmpty();
    }

    public Set<String> keySet() {
        return this.a.keySet();
    }

    public void putAll(Map<? extends String, ? extends String> map) {
        this.a.putAll(map);
    }

    public int size() {
        return this.a.size();
    }

    public Collection<String> values() {
        return this.a.values();
    }
}
