package org.apache.harmony.awt.datatransfer;

import android.support.v4.view.MotionEventCompat;
import com.sun.mail.imap.IMAPStore;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.SystemFlavorMap;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferUShort;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.harmony.awt.internal.nls.Messages;

public final class DataProxy implements Transferable {
    public static final Class[] charsetTextClasses = new Class[]{byte[].class, ByteBuffer.class, InputStream.class};
    public static final Class[] unicodeTextClasses = new Class[]{String.class, Reader.class, CharBuffer.class, char[].class};
    private final DataProvider data;
    private final SystemFlavorMap flavorMap = ((SystemFlavorMap) SystemFlavorMap.getDefaultFlavorMap());

    public DataProxy(DataProvider data) {
        this.data = data;
    }

    public DataProvider getDataProvider() {
        return this.data;
    }

    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        String mimeType = flavor.getPrimaryType() + "/" + flavor.getSubType();
        if (flavor.isFlavorTextType()) {
            if (mimeType.equalsIgnoreCase("text/html")) {
                return getHTML(flavor);
            }
            if (mimeType.equalsIgnoreCase(DataProvider.TYPE_URILIST)) {
                return getURL(flavor);
            }
            return getPlainText(flavor);
        } else if (flavor.isFlavorJavaFileListType()) {
            return getFileList(flavor);
        } else {
            if (flavor.isFlavorSerializedObjectType()) {
                return getSerializedObject(flavor);
            }
            if (flavor.equals(DataProvider.urlFlavor)) {
                return getURL(flavor);
            }
            if (mimeType.equalsIgnoreCase("image/x-java-image") && Image.class.isAssignableFrom(flavor.getRepresentationClass())) {
                return getImage(flavor);
            }
            throw new UnsupportedFlavorException(flavor);
        }
    }

    public DataFlavor[] getTransferDataFlavors() {
        ArrayList<DataFlavor> result = new ArrayList();
        String[] natives = this.data.getNativeFormats();
        for (String flavorsForNative : natives) {
            for (DataFlavor f : this.flavorMap.getFlavorsForNative(flavorsForNative)) {
                if (!result.contains(f)) {
                    result.add(f);
                }
            }
        }
        return (DataFlavor[]) result.toArray(new DataFlavor[result.size()]);
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        DataFlavor[] flavors = getTransferDataFlavors();
        for (DataFlavor equals : flavors) {
            if (equals.equals(flavor)) {
                return true;
            }
        }
        return false;
    }

    private Object getPlainText(DataFlavor f) throws IOException, UnsupportedFlavorException {
        if (this.data.isNativeFormatAvailable("text/plain")) {
            String str = this.data.getText();
            if (str != null) {
                return getTextRepresentation(str, f);
            }
            throw new IOException(Messages.getString("awt.4F"));
        }
        throw new UnsupportedFlavorException(f);
    }

    private Object getFileList(DataFlavor f) throws IOException, UnsupportedFlavorException {
        if (this.data.isNativeFormatAvailable("application/x-java-file-list")) {
            String[] files = this.data.getFileList();
            if (files != null) {
                return Arrays.asList(files);
            }
            throw new IOException(Messages.getString("awt.4F"));
        }
        throw new UnsupportedFlavorException(f);
    }

    private Object getHTML(DataFlavor f) throws IOException, UnsupportedFlavorException {
        if (this.data.isNativeFormatAvailable("text/html")) {
            String str = this.data.getHTML();
            if (str != null) {
                return getTextRepresentation(str, f);
            }
            throw new IOException(Messages.getString("awt.4F"));
        }
        throw new UnsupportedFlavorException(f);
    }

    private Object getURL(DataFlavor f) throws IOException, UnsupportedFlavorException {
        if (this.data.isNativeFormatAvailable("application/x-java-url")) {
            String str = this.data.getURL();
            if (str == null) {
                throw new IOException(Messages.getString("awt.4F"));
            }
            URL url = new URL(str);
            if (f.getRepresentationClass().isAssignableFrom(URL.class)) {
                return url;
            }
            if (f.isFlavorTextType()) {
                return getTextRepresentation(url.toString(), f);
            }
            throw new UnsupportedFlavorException(f);
        }
        throw new UnsupportedFlavorException(f);
    }

    private Object getSerializedObject(DataFlavor f) throws IOException, UnsupportedFlavorException {
        String nativeFormat = SystemFlavorMap.encodeDataFlavor(f);
        if (nativeFormat == null || !this.data.isNativeFormatAvailable(nativeFormat)) {
            throw new UnsupportedFlavorException(f);
        }
        byte[] bytes = this.data.getSerializedObject(f.getRepresentationClass());
        if (bytes == null) {
            throw new IOException(Messages.getString("awt.4F"));
        }
        try {
            return new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
        } catch (ClassNotFoundException ex) {
            throw new IOException(ex.getMessage());
        }
    }

    private String getCharset(DataFlavor f) {
        return f.getParameter("charset");
    }

    private Object getTextRepresentation(String text, DataFlavor f) throws UnsupportedFlavorException, IOException {
        if (f.getRepresentationClass() == String.class) {
            return text;
        }
        if (f.isRepresentationClassReader()) {
            return new StringReader(text);
        }
        if (f.isRepresentationClassCharBuffer()) {
            return CharBuffer.wrap(text);
        }
        if (f.getRepresentationClass() == char[].class) {
            char[] chars = new char[text.length()];
            text.getChars(0, text.length(), chars, 0);
            return chars;
        }
        String charset = getCharset(f);
        if (f.getRepresentationClass() == byte[].class) {
            return text.getBytes(charset);
        }
        if (f.isRepresentationClassByteBuffer()) {
            return ByteBuffer.wrap(text.getBytes(charset));
        }
        if (f.isRepresentationClassInputStream()) {
            return new ByteArrayInputStream(text.getBytes(charset));
        }
        throw new UnsupportedFlavorException(f);
    }

    private Image getImage(DataFlavor f) throws IOException, UnsupportedFlavorException {
        if (this.data.isNativeFormatAvailable("image/x-java-image")) {
            RawBitmap bitmap = this.data.getRawBitmap();
            if (bitmap != null) {
                return createBufferedImage(bitmap);
            }
            throw new IOException(Messages.getString("awt.4F"));
        }
        throw new UnsupportedFlavorException(f);
    }

    private boolean isRGB(RawBitmap b) {
        return b.rMask == 16711680 && b.gMask == MotionEventCompat.ACTION_POINTER_INDEX_MASK && b.bMask == MotionEventCompat.ACTION_MASK;
    }

    private boolean isBGR(RawBitmap b) {
        return b.rMask == MotionEventCompat.ACTION_MASK && b.gMask == MotionEventCompat.ACTION_POINTER_INDEX_MASK && b.bMask == 16711680;
    }

    private BufferedImage createBufferedImage(RawBitmap b) {
        if (b == null || b.buffer == null || b.width <= 0 || b.height <= 0) {
            return null;
        }
        ColorModel cm = null;
        WritableRaster wr = null;
        int[] masks;
        if (b.bits == 32 && (b.buffer instanceof int[])) {
            if (!isRGB(b) && !isBGR(b)) {
                return null;
            }
            masks = new int[]{b.rMask, b.gMask, b.bMask};
            int[] buffer = (int[]) b.buffer;
            DirectColorModel directColorModel = new DirectColorModel(24, b.rMask, b.gMask, b.bMask);
            wr = Raster.createPackedRaster(new DataBufferInt(buffer, buffer.length), b.width, b.height, b.stride, masks, null);
            cm = directColorModel;
        } else if (b.bits == 24 && (b.buffer instanceof byte[])) {
            int[] offsets;
            int[] bits = new int[]{8, 8, 8};
            if (isRGB(b)) {
                offsets = new int[3];
                offsets[1] = 1;
                offsets[2] = 2;
            } else if (!isBGR(b)) {
                return null;
            } else {
                offsets = new int[3];
                offsets[0] = 2;
                offsets[1] = 1;
            }
            byte[] buffer2 = (byte[]) b.buffer;
            cm = new ComponentColorModel(ColorSpace.getInstance(IMAPStore.RESPONSE), bits, false, false, 1, 0);
            wr = Raster.createInterleavedRaster(new DataBufferByte(buffer2, buffer2.length), b.width, b.height, b.stride, 3, offsets, null);
        } else if ((b.bits == 16 || b.bits == 15) && (b.buffer instanceof short[])) {
            masks = new int[]{b.rMask, b.gMask, b.bMask};
            short[] buffer3 = (short[]) b.buffer;
            cm = new DirectColorModel(b.bits, b.rMask, b.gMask, b.bMask);
            wr = Raster.createPackedRaster(new DataBufferUShort(buffer3, buffer3.length), b.width, b.height, b.stride, masks, null);
        }
        if (cm == null || wr == null) {
            return null;
        }
        return new BufferedImage(cm, wr, false, null);
    }
}
