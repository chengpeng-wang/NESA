package org.apache.harmony.awt.datatransfer;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.SystemFlavorMap;
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

public class TextFlavor {
    public static final Class[] charsetTextClasses = new Class[]{InputStream.class, ByteBuffer.class, byte[].class};
    public static final Class[] unicodeTextClasses = new Class[]{String.class, Reader.class, CharBuffer.class, char[].class};

    public static void addUnicodeClasses(SystemFlavorMap fm, String nat, String subType) {
        for (Class name : unicodeTextClasses) {
            String type = "text/" + subType;
            DataFlavor f = new DataFlavor(new StringBuilder(String.valueOf(type)).append(";class=\"" + name.getName() + "\"").toString(), type);
            fm.addFlavorForUnencodedNative(nat, f);
            fm.addUnencodedNativeForFlavor(f, nat);
        }
    }

    public static void addCharsetClasses(SystemFlavorMap fm, String nat, String subType, String charset) {
        for (Class name : charsetTextClasses) {
            String type = "text/" + subType;
            DataFlavor f = new DataFlavor(new StringBuilder(String.valueOf(type)).append(";class=\"" + name.getName() + "\"" + ";charset=\"" + charset + "\"").toString(), type);
            fm.addFlavorForUnencodedNative(nat, f);
            fm.addUnencodedNativeForFlavor(f, nat);
        }
    }
}
