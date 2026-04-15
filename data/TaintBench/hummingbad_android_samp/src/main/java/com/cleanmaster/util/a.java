package com.cleanmaster.util;

import android.annotation.SuppressLint;
import android.util.Base64;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import u.aly.bf;

/* compiled from: AESUtils */
public class a {
    private static final byte[] d = new byte[]{(byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9, (byte) 10, (byte) 11, (byte) 12, bf.k, bf.l, bf.m, bf.n};
    protected IvParameterSpec a;
    protected SecretKeySpec b;
    private int c = 1;

    public byte[] a(byte[] bArr) {
        try {
            Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
            instance.init(1, this.b, this.a);
            return instance.doFinal(bArr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public a() {
        byte[] bArr = new byte[16];
        new SecureRandom().nextBytes(bArr);
        this.a = new IvParameterSpec(bArr);
        byte[] bArr2 = new byte[16];
        bArr = null;
        try {
            bArr = "liebao%^&()@Io=-".getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int i = 0;
        while (i < 16 && i < bArr.length && bArr != null) {
            bArr2[i] = bArr[i];
            i++;
        }
        this.b = new SecretKeySpec(bArr2, "AES");
    }

    @SuppressLint({"NewApi", "InlinedApi"})
    public String a(String str) {
        if (str != null) {
            try {
                String str2 = new String(new DecimalFormat("0000").format((long) this.c));
                int length = this.a.getIV().length;
                int length2 = str2.getBytes("utf-8").length;
                byte[] a = a(str.getBytes("utf-8"));
                int length3 = a.length;
                byte[] bArr = new byte[((length + length2) + length3)];
                System.arraycopy(this.a.getIV(), 0, bArr, 0, length);
                System.arraycopy(str2.getBytes("utf-8"), 0, bArr, length, length2);
                System.arraycopy(a, 0, bArr, length + length2, length3);
                return Base64.encodeToString(bArr, 2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
