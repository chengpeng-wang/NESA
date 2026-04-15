package com.androidquery.auth;

import android.net.Uri;
import android.support.v4.view.MotionEventCompat;
import com.androidquery.callback.AbstractAjaxCallback;
import com.androidquery.callback.AjaxStatus;
import java.net.HttpURLConnection;
import org.apache.http.HttpRequest;

public class BasicHandle extends AccountHandle {
    private static final char[] map1 = new char[64];
    private static final byte[] map2 = new byte[128];
    private String password;
    private String username;

    public BasicHandle(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean authenticated() {
        return true;
    }

    /* access modifiers changed from: protected */
    public void auth() {
    }

    public boolean expired(AbstractAjaxCallback<?, ?> abstractAjaxCallback, AjaxStatus status) {
        return false;
    }

    public boolean reauth(AbstractAjaxCallback<?, ?> abstractAjaxCallback) {
        return false;
    }

    public void applyToken(AbstractAjaxCallback<?, ?> cb, HttpRequest request) {
        byte[] data = (this.username + ":" + this.password).getBytes();
        String auth = "Basic " + new String(encode(data, 0, data.length));
        request.addHeader("Host", Uri.parse(cb.getUrl()).getHost());
        request.addHeader("Authorization", auth);
    }

    public void applyToken(AbstractAjaxCallback<?, ?> cb, HttpURLConnection conn) {
        byte[] data = (this.username + ":" + this.password).getBytes();
        String auth = "Basic " + new String(encode(data, 0, data.length));
        conn.setRequestProperty("Host", Uri.parse(cb.getUrl()).getHost());
        conn.setRequestProperty("Authorization", auth);
    }

    static {
        int i;
        int i2 = 0;
        char c = 'A';
        while (true) {
            i = i2;
            if (c > 'Z') {
                break;
            }
            i2 = i + 1;
            map1[i] = c;
            c = (char) (c + 1);
        }
        c = 'a';
        while (c <= 'z') {
            i2 = i + 1;
            map1[i] = c;
            c = (char) (c + 1);
            i = i2;
        }
        c = '0';
        while (c <= '9') {
            i2 = i + 1;
            map1[i] = c;
            c = (char) (c + 1);
            i = i2;
        }
        i2 = i + 1;
        map1[i] = '+';
        i = i2 + 1;
        map1[i2] = '/';
        for (i2 = 0; i2 < map2.length; i2++) {
            map2[i2] = (byte) -1;
        }
        for (i2 = 0; i2 < 64; i2++) {
            map2[map1[i2]] = (byte) i2;
        }
    }

    public static char[] encode(byte[] in, int iOff, int iLen) {
        int oDataLen = ((iLen * 4) + 2) / 3;
        char[] out = new char[(((iLen + 2) / 3) * 4)];
        int iEnd = iOff + iLen;
        int op = 0;
        int ip = iOff;
        while (ip < iEnd) {
            int i1;
            int i2;
            int ip2 = ip + 1;
            int i0 = in[ip] & MotionEventCompat.ACTION_MASK;
            if (ip2 < iEnd) {
                ip = ip2 + 1;
                i1 = in[ip2] & MotionEventCompat.ACTION_MASK;
            } else {
                i1 = 0;
                ip = ip2;
            }
            if (ip < iEnd) {
                ip2 = ip + 1;
                i2 = in[ip] & MotionEventCompat.ACTION_MASK;
            } else {
                i2 = 0;
                ip2 = ip;
            }
            int o1 = ((i0 & 3) << 4) | (i1 >>> 4);
            int o2 = ((i1 & 15) << 2) | (i2 >>> 6);
            int o3 = i2 & 63;
            int i = op + 1;
            out[op] = map1[i0 >>> 2];
            op = i + 1;
            out[i] = map1[o1];
            out[op] = op < oDataLen ? map1[o2] : '=';
            i = op + 1;
            out[i] = i < oDataLen ? map1[o3] : '=';
            op = i + 1;
            ip = ip2;
        }
        return out;
    }
}
