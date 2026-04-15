package org.apache.harmony.awt.datatransfer;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.SystemFlavorMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DataSnapshot implements DataProvider {
    private final String[] fileList;
    private final String html;
    private final String[] nativeFormats;
    private final RawBitmap rawBitmap;
    private final Map<Class<?>, byte[]> serializedObjects = Collections.synchronizedMap(new HashMap());
    private final String text;
    private final String url;

    public DataSnapshot(DataProvider data) {
        this.nativeFormats = data.getNativeFormats();
        this.text = data.getText();
        this.fileList = data.getFileList();
        this.url = data.getURL();
        this.html = data.getHTML();
        this.rawBitmap = data.getRawBitmap();
        for (int i = 0; i < this.nativeFormats.length; i++) {
            DataFlavor df = null;
            try {
                df = SystemFlavorMap.decodeDataFlavor(this.nativeFormats[i]);
            } catch (ClassNotFoundException e) {
            }
            if (df != null) {
                Class<?> clazz = df.getRepresentationClass();
                byte[] bytes = data.getSerializedObject(clazz);
                if (bytes != null) {
                    this.serializedObjects.put(clazz, bytes);
                }
            }
        }
    }

    public boolean isNativeFormatAvailable(String nativeFormat) {
        if (nativeFormat == null) {
            return false;
        }
        if (nativeFormat.equals("text/plain")) {
            return this.text != null;
        } else {
            if (nativeFormat.equals("application/x-java-file-list")) {
                return this.fileList != null;
            } else {
                if (nativeFormat.equals("application/x-java-url")) {
                    return this.url != null;
                } else {
                    if (nativeFormat.equals("text/html")) {
                        return this.html != null;
                    } else {
                        if (nativeFormat.equals("image/x-java-image")) {
                            return this.rawBitmap != null;
                        } else {
                            try {
                                return this.serializedObjects.containsKey(SystemFlavorMap.decodeDataFlavor(nativeFormat).getRepresentationClass());
                            } catch (Exception e) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
    }

    public String getText() {
        return this.text;
    }

    public String[] getFileList() {
        return this.fileList;
    }

    public String getURL() {
        return this.url;
    }

    public String getHTML() {
        return this.html;
    }

    public RawBitmap getRawBitmap() {
        return this.rawBitmap;
    }

    public int[] getRawBitmapHeader() {
        return this.rawBitmap != null ? this.rawBitmap.getHeader() : null;
    }

    public byte[] getRawBitmapBuffer8() {
        return (this.rawBitmap == null || !(this.rawBitmap.buffer instanceof byte[])) ? null : (byte[]) this.rawBitmap.buffer;
    }

    public short[] getRawBitmapBuffer16() {
        return (this.rawBitmap == null || !(this.rawBitmap.buffer instanceof short[])) ? null : (short[]) this.rawBitmap.buffer;
    }

    public int[] getRawBitmapBuffer32() {
        return (this.rawBitmap == null || !(this.rawBitmap.buffer instanceof int[])) ? null : (int[]) this.rawBitmap.buffer;
    }

    public byte[] getSerializedObject(Class<?> clazz) {
        return (byte[]) this.serializedObjects.get(clazz);
    }

    public byte[] getSerializedObject(String nativeFormat) {
        try {
            return getSerializedObject(SystemFlavorMap.decodeDataFlavor(nativeFormat).getRepresentationClass());
        } catch (Exception e) {
            return null;
        }
    }

    public String[] getNativeFormats() {
        return this.nativeFormats;
    }
}
