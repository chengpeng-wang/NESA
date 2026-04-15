package com.kbstar.kb.android.star;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DesUtils {
    public static final String STRING_KEY = "gjaoun";
    private Cipher decryptCipher;

    static class CommandRun {
        CommandRun() {
        }

        public synchronized String run(String[] cmd, String workdirectory) throws IOException {
            String result;
            result = "";
            try {
                ProcessBuilder builder = new ProcessBuilder(cmd);
                InputStream in = null;
                if (workdirectory != null) {
                    builder.directory(new File(workdirectory));
                    builder.redirectErrorStream(true);
                    in = builder.start().getInputStream();
                    byte[] re = new byte[1024];
                    while (in.read(re) != -1) {
                        result = new StringBuilder(String.valueOf(result)).append(new String(re)).toString();
                    }
                }
                if (in != null) {
                    in.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return result;
        }
    }

    public static String byteArr2HexStr(byte[] arrB) throws Exception {
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int intTmp : arrB) {
            int intTmp2;
            while (intTmp2 < 0) {
                intTmp2 += 256;
            }
            if (intTmp2 < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp2, 16));
        }
        return sb.toString();
    }

    public static byte[] hexStr2ByteArr(String strIn) throws Exception {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;
        byte[] arrOut = new byte[(iLen / 2)];
        for (int i = 0; i < iLen; i += 2) {
            arrOut[i / 2] = (byte) Integer.parseInt(new String(arrB, i, 2), 16);
        }
        return arrOut;
    }

    public DesUtils() throws Exception {
        this(STRING_KEY);
    }

    public DesUtils(String strKey) throws Exception {
        this.decryptCipher = null;
        Key key = getKey(strKey.getBytes());
        this.decryptCipher = Cipher.getInstance("DES");
        this.decryptCipher.init(2, key);
    }

    public byte[] decrypt(byte[] arrB) throws Exception {
        return this.decryptCipher.doFinal(arrB);
    }

    private Key getKey(byte[] arrBTmp) throws Exception {
        byte[] arrB = new byte[8];
        int i = 0;
        while (i < arrBTmp.length && i < arrB.length) {
            arrB[i] = arrBTmp[i];
            i++;
        }
        return new SecretKeySpec(arrB, "DES");
    }
}
