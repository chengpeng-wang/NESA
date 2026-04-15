package com.google.android.gms.internal;

import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class zzaq {
    private final zzap zznF;
    private final SecureRandom zzox;

    public class zza extends Exception {
        public zza(Throwable th) {
            super(th);
        }
    }

    public zzaq(zzap zzap, SecureRandom secureRandom) {
        this.zznF = zzap;
        this.zzox = secureRandom;
    }

    static void zze(byte[] bArr) {
        for (int i = 0; i < bArr.length; i++) {
            bArr[i] = (byte) (bArr[i] ^ 68);
        }
    }

    public byte[] zzc(byte[] bArr, String str) throws zza {
        if (bArr.length != 16) {
            throw new zza();
        }
        try {
            byte[] zza = this.zznF.zza(str, false);
            if (zza.length <= 16) {
                throw new zza();
            }
            ByteBuffer allocate = ByteBuffer.allocate(zza.length);
            allocate.put(zza);
            allocate.flip();
            byte[] bArr2 = new byte[16];
            zza = new byte[(zza.length - 16)];
            allocate.get(bArr2);
            allocate.get(zza);
            SecretKeySpec secretKeySpec = new SecretKeySpec(bArr, "AES");
            Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
            instance.init(2, secretKeySpec, new IvParameterSpec(bArr2));
            return instance.doFinal(zza);
        } catch (NoSuchAlgorithmException e) {
            throw new zza(e);
        } catch (InvalidKeyException e2) {
            throw new zza(e2);
        } catch (IllegalBlockSizeException e3) {
            throw new zza(e3);
        } catch (NoSuchPaddingException e4) {
            throw new zza(e4);
        } catch (BadPaddingException e5) {
            throw new zza(e5);
        } catch (InvalidAlgorithmParameterException e6) {
            throw new zza(e6);
        } catch (IllegalArgumentException e7) {
            throw new zza(e7);
        }
    }

    public byte[] zzl(String str) throws zza {
        try {
            byte[] zza = this.zznF.zza(str, false);
            if (zza.length != 32) {
                throw new zza();
            }
            byte[] bArr = new byte[16];
            ByteBuffer.wrap(zza, 4, 16).get(bArr);
            zze(bArr);
            return bArr;
        } catch (IllegalArgumentException e) {
            throw new zza(e);
        }
    }
}
