package com.androidquery.callback;

public interface Transformer {
    <T> T transform(String str, Class<T> cls, String str2, byte[] bArr, AjaxStatus ajaxStatus);
}
