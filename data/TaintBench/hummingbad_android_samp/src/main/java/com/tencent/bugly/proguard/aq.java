package com.tencent.bugly.proguard;

import com.moceanmobile.mast.Defaults;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/* compiled from: BUGLY */
public class aq implements as {
    private String a = null;

    public byte[] a(byte[] bArr) throws Exception {
        if (this.a == null || bArr == null) {
            return null;
        }
        Cipher instance = Cipher.getInstance("DES/CBC/PKCS5Padding");
        instance.init(2, SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(this.a.getBytes(Defaults.ENCODING_UTF_8))), new IvParameterSpec(this.a.getBytes(Defaults.ENCODING_UTF_8)));
        return instance.doFinal(bArr);
    }

    public byte[] b(byte[] bArr) throws Exception, NoSuchAlgorithmException {
        if (this.a == null || bArr == null) {
            return null;
        }
        Cipher instance = Cipher.getInstance("DES/CBC/PKCS5Padding");
        instance.init(1, SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(this.a.getBytes(Defaults.ENCODING_UTF_8))), new IvParameterSpec(this.a.getBytes(Defaults.ENCODING_UTF_8)));
        return instance.doFinal(bArr);
    }

    public void a(String str) {
        if (str != null) {
            this.a = str;
        }
    }
}
