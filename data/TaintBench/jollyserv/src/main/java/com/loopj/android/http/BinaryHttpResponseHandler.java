package com.loopj.android.http;

import android.os.Message;
import java.io.IOException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.util.EntityUtils;

public class BinaryHttpResponseHandler extends AsyncHttpResponseHandler {
    private static String[] mAllowedContentTypes = new String[]{"image/jpeg", "image/png"};

    public BinaryHttpResponseHandler(String[] strArr) {
        this();
        mAllowedContentTypes = strArr;
    }

    public void onSuccess(byte[] bArr) {
    }

    public void onFailure(Throwable th, byte[] bArr) {
        onFailure(th);
    }

    /* access modifiers changed from: protected */
    public void sendSuccessMessage(byte[] bArr) {
        sendMessage(obtainMessage(0, bArr));
    }

    /* access modifiers changed from: protected */
    public void sendFailureMessage(Throwable th, byte[] bArr) {
        sendMessage(obtainMessage(1, new Object[]{th, bArr}));
    }

    /* access modifiers changed from: protected */
    public void handleSuccessMessage(byte[] bArr) {
        onSuccess(bArr);
    }

    /* access modifiers changed from: protected */
    public void handleFailureMessage(Throwable th, byte[] bArr) {
        onFailure(th, bArr);
    }

    /* access modifiers changed from: protected */
    public void handleMessage(Message message) {
        switch (message.what) {
            case 0:
                handleSuccessMessage((byte[]) message.obj);
                return;
            case 1:
                Object[] objArr = (Object[]) message.obj;
                handleFailureMessage((Throwable) objArr[0], (byte[]) objArr[1]);
                return;
            default:
                super.handleMessage(message);
                return;
        }
    }

    /* access modifiers changed from: 0000 */
    public void sendResponseMessage(HttpResponse httpResponse) {
        byte[] bArr = null;
        int i = 0;
        StatusLine statusLine = httpResponse.getStatusLine();
        Header[] headers = httpResponse.getHeaders("Content-Type");
        if (headers.length != 1) {
            sendFailureMessage(new HttpResponseException(statusLine.getStatusCode(), "None, or more than one, Content-Type Header found!"), null);
            return;
        }
        Header header = headers[0];
        for (String equals : mAllowedContentTypes) {
            if (equals.equals(header.getValue())) {
                i = 1;
            }
        }
        if (i == 0) {
            sendFailureMessage(new HttpResponseException(statusLine.getStatusCode(), "Content-Type not allowed!"), null);
            return;
        }
        try {
            HttpEntity bufferedHttpEntity;
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                bufferedHttpEntity = new BufferedHttpEntity(entity);
            } else {
                bufferedHttpEntity = null;
            }
            bArr = EntityUtils.toByteArray(bufferedHttpEntity);
        } catch (IOException e) {
            sendFailureMessage(e, null);
        }
        if (statusLine.getStatusCode() >= 300) {
            sendFailureMessage(new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase()), bArr);
        } else {
            sendSuccessMessage(bArr);
        }
    }
}
