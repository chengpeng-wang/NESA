package com.flurry.sdk;

import android.util.Pair;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.nio.ByteBuffer;

public class fd extends jx {
    /* access modifiers changed from: private|static|final */
    public static final String f = fd.class.getSimpleName();
    public final ja a;
    /* access modifiers changed from: private|final */
    public final ja g;

    public fd() {
        super(fd.class.getSimpleName());
        this.a = new ja("sdk log request", new fh());
        this.g = new ja("sdk log response", new fj());
        this.e = "AdData_";
        b();
    }

    public static byte[] a(byte[] bArr, String str) {
        byte[] bytes = str.getBytes();
        byte[] array = ByteBuffer.allocate(4).putInt(bytes.length).array();
        byte[] bArr2 = new byte[((array.length + bytes.length) + bArr.length)];
        int i = 0;
        while (i < bArr2.length) {
            if (i < array.length) {
                bArr2[i] = array[i];
            } else if (i < array.length || i >= bytes.length + array.length) {
                bArr2[i] = bArr[(i - 4) - bytes.length];
            } else {
                bArr2[i] = bytes[i - 4];
            }
            i++;
        }
        return bArr2;
    }

    /* access modifiers changed from: protected|final */
    public final void a(byte[] bArr, String str, String str2) {
        int i = 0;
        try {
            int i2;
            byte[] bArr2 = new byte[4];
            byte[] bArr3 = new byte[(bArr.length - 4)];
            for (i2 = 0; i2 < bArr.length; i2++) {
                if (i2 < 4) {
                    bArr2[i2] = bArr[i2];
                } else {
                    bArr3[i2 - 4] = bArr[i2];
                }
            }
            i2 = ByteBuffer.wrap(bArr2).getInt();
            bArr2 = new byte[i2];
            byte[] bArr4 = new byte[(bArr3.length - i2)];
            while (i < bArr3.length) {
                if (i < i2) {
                    bArr2[i] = bArr3[i];
                } else {
                    bArr4[i - i2] = bArr3[i];
                }
                i++;
            }
            Pair pair = new Pair(new String(bArr2), bArr4);
            String str3 = (String) pair.first;
            byte[] bArr5 = (byte[]) pair.second;
            iw.a(4, this.b, "FlurryAdLogsManager: start upload data with id = " + str + " to " + str3);
            jc jcVar = new jc();
            jcVar.e = str3;
            jcVar.j = 100000;
            jcVar.f = jj.kPost;
            jcVar.a(MASTNativeAdConstants.REQUEST_HEADER_CONTENT_TYPE, "application/x-flurry");
            jcVar.a("Accept", "application/x-flurry");
            jcVar.a("FM-Checksum", Integer.toString(ja.a(bArr5)));
            jcVar.c = new kw();
            jcVar.d = new kw();
            jcVar.b = bArr5;
            jcVar.a = new fe(this, str, str2);
            hy.a().a((Object) this, (ma) jcVar);
        } catch (Exception e) {
            iw.a(6, this.b, "Internal ERROR! Report is corrupt!");
            if (!this.d.a(str, str2)) {
                iw.a(6, this.b, "Internal error. Block wasn't deleted with id = " + str);
            }
            if (!this.c.remove(str)) {
                iw.a(6, this.b, "Internal error. Block with id = " + str + " was not in progress state");
            }
        }
    }
}
