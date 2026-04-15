package com.baidu.android.pushservice.jni;

import android.support.v4.view.MotionEventCompat;
import com.baidu.android.common.logging.Log;
import com.baidu.android.pushservice.b;
import com.baidu.android.pushservice.message.a;
import com.baidu.android.pushservice.util.m;

public class PushSocket {
    public static boolean a;
    private static byte[] b = null;
    private static int c = 0;
    private static String d = "socket";
    private static int e = 36;
    private static int f = 32;

    static {
        a = false;
        try {
            System.loadLibrary("bdpush_V1_0");
            a = true;
        } catch (UnsatisfiedLinkError e) {
            try {
                System.loadLibrary("push-socket");
                a = true;
            } catch (UnsatisfiedLinkError e2) {
                Log.d(d, "native library not found!");
            }
        }
    }

    public static short a(byte[] bArr, int i) {
        return (short) ((bArr[i + 1] << 8) | (bArr[i + 0] & MotionEventCompat.ACTION_MASK));
    }

    private static boolean a(int i) {
        byte[] rcvMsg = rcvMsg(i);
        if (rcvMsg == null || rcvMsg.length == 0) {
            return false;
        }
        if (b == null) {
            b = rcvMsg;
        } else {
            byte[] bArr = new byte[(b.length + rcvMsg.length)];
            System.arraycopy(b, c, bArr, 0, b.length - c);
            System.arraycopy(rcvMsg, 0, bArr, b.length, rcvMsg.length);
            b = bArr;
        }
        return true;
    }

    public static byte[] a(int i, a aVar) {
        byte[] bArr;
        while (true) {
            if (b != null) {
                int length = b.length;
                if (length == c) {
                    b = null;
                    c = 0;
                } else if (length - c > 1) {
                    short a = a(b, c);
                    Log.i(d, "msgid:" + a);
                    if (a == (short) 5 || a == (short) 6) {
                        bArr = new byte[2];
                        System.arraycopy(b, c, bArr, 0, bArr.length);
                    } else if (length - c < e && !a(i)) {
                        return null;
                    } else {
                        int b = b(b, c + f);
                        if ((c + b) + e <= length - c) {
                            bArr = new byte[(e + b)];
                            System.arraycopy(b, c, bArr, 0, bArr.length);
                            c += b + e;
                            return bArr;
                        } else if (!a(i)) {
                            return null;
                        }
                    }
                } else if (!a(i)) {
                    return null;
                }
            } else if (!a(i)) {
                return null;
            }
        }
        bArr = new byte[2];
        System.arraycopy(b, c, bArr, 0, bArr.length);
        if (b.a() && a == (short) 6) {
            Log.i(d, "MSG_ID_TINY_HEARTBEAT_SERVER");
            m.a("MSG_ID_TINY_HEARTBEAT_SERVER");
        }
        c += 2;
        return bArr;
    }

    public static int b(byte[] bArr, int i) {
        return ((((bArr[i + 3] & MotionEventCompat.ACTION_MASK) << 24) | ((bArr[i + 2] & MotionEventCompat.ACTION_MASK) << 16)) | ((bArr[i + 1] & MotionEventCompat.ACTION_MASK) << 8)) | ((bArr[i + 0] & MotionEventCompat.ACTION_MASK) << 0);
    }

    public static native int closeSocket(int i);

    public static native int createSocket(String str, int i);

    public static native int getLastSocketError();

    private static native byte[] rcvMsg(int i);

    public static native int sendMsg(int i, byte[] bArr, int i2);
}
