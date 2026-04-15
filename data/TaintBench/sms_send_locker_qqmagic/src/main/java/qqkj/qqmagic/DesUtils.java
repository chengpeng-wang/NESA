package qqkj.qqmagic;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DesUtils {
    private static String strDefaultKey = "national";
    private Cipher decryptCipher;
    private Cipher encryptCipher;

    public static String byteArr2HexStr(byte[] bArr) throws Exception {
        StringBuffer stringBuffer = r10;
        StringBuffer stringBuffer2 = new StringBuffer(r1 * 2);
        StringBuffer stringBuffer3 = stringBuffer;
        for (byte b : bArr) {
            byte b2;
            while (true) {
                byte b3;
                b2 = b3;
                if (b2 >= (byte) 0) {
                    break;
                }
                b3 = b2 + 256;
            }
            if (b2 < (byte) 16) {
                stringBuffer = stringBuffer3.append('0');
            }
            stringBuffer = stringBuffer3.append(Integer.toString(b2, 16));
        }
        return stringBuffer3.toString();
    }

    public static byte[] hexStr2ByteArr(String str) throws Exception {
        byte[] bytes = str.getBytes();
        int length = bytes.length;
        byte[] bArr = new byte[(length / 2)];
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= length) {
                return bArr;
            }
            String str2 = r12;
            String str3 = new String(bytes, i2, 2);
            bArr[i2 / 2] = (byte) Integer.parseInt(str2, 16);
            i = i2 + 2;
        }
    }

    public DesUtils() throws Exception {
        this(strDefaultKey);
    }

    public DesUtils(String str) {
        String str2 = str;
        this.encryptCipher = (Cipher) null;
        this.decryptCipher = (Cipher) null;
        try {
            Key key = getKey(str2.getBytes());
            this.encryptCipher = Cipher.getInstance("DES");
            this.encryptCipher.init(1, key);
            this.decryptCipher = Cipher.getInstance("DES");
            this.decryptCipher.init(2, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] encrypt(byte[] bArr) throws Exception {
        return this.encryptCipher.doFinal(bArr);
    }

    public String encrypt(String str) throws Exception {
        return byteArr2HexStr(encrypt(str.getBytes()));
    }

    public byte[] decrypt(byte[] bArr) throws Exception {
        return this.decryptCipher.doFinal(bArr);
    }

    public String decrypt(String str) throws Exception {
        String str2 = r7;
        String str3 = new String(decrypt(hexStr2ByteArr(str)));
        return str2;
    }

    private Key getKey(byte[] bArr) throws Exception {
        byte[] bArr2 = bArr;
        byte[] bArr3 = new byte[8];
        int i = 0;
        while (i < bArr2.length && i < bArr3.length) {
            bArr3[i] = bArr2[i];
            i++;
        }
        SecretKeySpec secretKeySpec = r9;
        SecretKeySpec secretKeySpec2 = new SecretKeySpec(bArr3, "DES");
        return secretKeySpec;
    }
}
