package com.cleanmaster.util;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/* compiled from: DESUtil */
public class g {
    public static byte[] a(byte[] bArr, byte[] bArr2) {
        Key a = a(new String(bArr));
        Cipher instance = Cipher.getInstance("DES/ECB/PKCS5Padding");
        instance.init(1, a);
        return instance.doFinal(bArr2);
    }

    private static Key a(String str) {
        return SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(j.a(str)));
    }
}
