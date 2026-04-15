package myjava.awt.datatransfer;

import com.googleprojects.mm.MMMailContentUtil;
import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.harmony.awt.datatransfer.DTK;
import org.apache.harmony.awt.datatransfer.DataProvider;
import org.apache.harmony.awt.internal.nls.Messages;

public class DataFlavor implements Externalizable, Cloneable {
    public static final DataFlavor javaFileListFlavor = new DataFlavor("application/x-java-file-list; class=java.util.List", "application/x-java-file-list");
    public static final String javaJVMLocalObjectMimeType = "application/x-java-jvm-local-objectref";
    public static final String javaRemoteObjectMimeType = "application/x-java-remote-object";
    public static final String javaSerializedObjectMimeType = "application/x-java-serialized-object";
    @Deprecated
    public static final DataFlavor plainTextFlavor = new DataFlavor("text/plain; charset=unicode; class=java.io.InputStream", "Plain Text");
    private static DataFlavor plainUnicodeFlavor = null;
    private static final long serialVersionUID = 8367026044764648243L;
    private static final String[] sortedTextFlavors = new String[]{"text/sgml", "text/xml", "text/html", "text/rtf", "text/enriched", "text/richtext", DataProvider.TYPE_URILIST, "text/tab-separated-values", "text/t140", "text/rfc822-headers", "text/parityfec", "text/directory", "text/css", "text/calendar", "application/x-java-serialized-object", "text/plain"};
    public static final DataFlavor stringFlavor = new DataFlavor("application/x-java-serialized-object; class=java.lang.String", "Unicode String");
    private String humanPresentableName;
    private MimeType mimeInfo;
    private Class<?> representationClass;

    public static final DataFlavor getTextPlainUnicodeFlavor() {
        if (plainUnicodeFlavor == null) {
            plainUnicodeFlavor = new DataFlavor("text/plain; charset=" + DTK.getDTK().getDefaultCharset() + "; class=java.io.InputStream", "Plain Text");
        }
        return plainUnicodeFlavor;
    }

    protected static final Class<?> tryToLoadClass(String className, ClassLoader fallback) throws ClassNotFoundException {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            try {
                return ClassLoader.getSystemClassLoader().loadClass(className);
            } catch (ClassNotFoundException e2) {
                ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
                if (contextLoader != null) {
                    try {
                        return contextLoader.loadClass(className);
                    } catch (ClassNotFoundException e3) {
                    }
                }
                return fallback.loadClass(className);
            }
        }
    }

    private static boolean isCharsetSupported(String charset) {
        try {
            return Charset.isSupported(charset);
        } catch (IllegalCharsetNameException e) {
            return false;
        }
    }

    public DataFlavor() {
        this.mimeInfo = null;
        this.humanPresentableName = null;
        this.representationClass = null;
    }

    public DataFlavor(Class<?> representationClass, String humanPresentableName) {
        this.mimeInfo = new MimeType("application", "x-java-serialized-object");
        if (humanPresentableName != null) {
            this.humanPresentableName = humanPresentableName;
        } else {
            this.humanPresentableName = "application/x-java-serialized-object";
        }
        this.mimeInfo.addParameter("class", representationClass.getName());
        this.representationClass = representationClass;
    }

    public DataFlavor(String mimeType, String humanPresentableName) {
        try {
            init(mimeType, humanPresentableName, null);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(Messages.getString("awt.16C", this.mimeInfo.getParameter("class")), e);
        }
    }

    public DataFlavor(String mimeType) throws ClassNotFoundException {
        init(mimeType, null, null);
    }

    public DataFlavor(String mimeType, String humanPresentableName, ClassLoader classLoader) throws ClassNotFoundException {
        init(mimeType, humanPresentableName, classLoader);
    }

    private void init(String mimeType, String humanPresentableName, ClassLoader classLoader) throws ClassNotFoundException {
        try {
            Class cls;
            this.mimeInfo = MimeTypeProcessor.parse(mimeType);
            if (humanPresentableName != null) {
                this.humanPresentableName = humanPresentableName;
            } else {
                this.humanPresentableName = new StringBuilder(String.valueOf(this.mimeInfo.getPrimaryType())).append('/').append(this.mimeInfo.getSubType()).toString();
            }
            String className = this.mimeInfo.getParameter("class");
            if (className == null) {
                className = "java.io.InputStream";
                this.mimeInfo.addParameter("class", className);
            }
            if (classLoader == null) {
                cls = Class.forName(className);
            } else {
                cls = classLoader.loadClass(className);
            }
            this.representationClass = cls;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(Messages.getString("awt.16D", (Object) mimeType));
        }
    }

    private String getCharset() {
        if (this.mimeInfo == null || isCharsetRedundant()) {
            return MMMailContentUtil.MM_MESSAGE_SUBJECT;
        }
        String charset = this.mimeInfo.getParameter("charset");
        if (isCharsetRequired() && (charset == null || charset.length() == 0)) {
            return DTK.getDTK().getDefaultCharset();
        }
        if (charset == null) {
            return MMMailContentUtil.MM_MESSAGE_SUBJECT;
        }
        return charset;
    }

    private boolean isCharsetRequired() {
        String type = this.mimeInfo.getFullType();
        return type.equals("text/sgml") || type.equals("text/xml") || type.equals("text/html") || type.equals("text/enriched") || type.equals("text/richtext") || type.equals(DataProvider.TYPE_URILIST) || type.equals("text/directory") || type.equals("text/css") || type.equals("text/calendar") || type.equals("application/x-java-serialized-object") || type.equals("text/plain");
    }

    private boolean isCharsetRedundant() {
        String type = this.mimeInfo.getFullType();
        return type.equals("text/rtf") || type.equals("text/tab-separated-values") || type.equals("text/t140") || type.equals("text/rfc822-headers") || type.equals("text/parityfec");
    }

    /* access modifiers changed from: 0000 */
    public MimeType getMimeInfo() {
        return this.mimeInfo;
    }

    public String getPrimaryType() {
        return this.mimeInfo != null ? this.mimeInfo.getPrimaryType() : null;
    }

    public String getSubType() {
        return this.mimeInfo != null ? this.mimeInfo.getSubType() : null;
    }

    public String getMimeType() {
        return this.mimeInfo != null ? MimeTypeProcessor.assemble(this.mimeInfo) : null;
    }

    public String getParameter(String paramName) {
        String lowerName = paramName.toLowerCase();
        if (lowerName.equals("humanpresentablename")) {
            return this.humanPresentableName;
        }
        return this.mimeInfo != null ? this.mimeInfo.getParameter(lowerName) : null;
    }

    public String getHumanPresentableName() {
        return this.humanPresentableName;
    }

    public void setHumanPresentableName(String humanPresentableName) {
        this.humanPresentableName = humanPresentableName;
    }

    public Class<?> getRepresentationClass() {
        return this.representationClass;
    }

    public final Class<?> getDefaultRepresentationClass() {
        return InputStream.class;
    }

    public final String getDefaultRepresentationClassAsString() {
        return getDefaultRepresentationClass().getName();
    }

    public boolean isRepresentationClassSerializable() {
        return Serializable.class.isAssignableFrom(this.representationClass);
    }

    public boolean isRepresentationClassRemote() {
        return false;
    }

    public boolean isRepresentationClassReader() {
        return Reader.class.isAssignableFrom(this.representationClass);
    }

    public boolean isRepresentationClassInputStream() {
        return InputStream.class.isAssignableFrom(this.representationClass);
    }

    public boolean isRepresentationClassCharBuffer() {
        return CharBuffer.class.isAssignableFrom(this.representationClass);
    }

    public boolean isRepresentationClassByteBuffer() {
        return ByteBuffer.class.isAssignableFrom(this.representationClass);
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public String normalizeMimeTypeParameter(String parameterName, String parameterValue) {
        return parameterValue;
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public String normalizeMimeType(String mimeType) {
        return mimeType;
    }

    public final boolean isMimeTypeEqual(DataFlavor dataFlavor) {
        if (this.mimeInfo != null) {
            return this.mimeInfo.equals(dataFlavor.mimeInfo);
        }
        return dataFlavor.mimeInfo == null;
    }

    public boolean isMimeTypeEqual(String mimeType) {
        try {
            return this.mimeInfo.equals(MimeTypeProcessor.parse(mimeType));
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public synchronized void writeExternal(ObjectOutput os) throws IOException {
        os.writeObject(this.humanPresentableName);
        os.writeObject(this.mimeInfo);
    }

    public synchronized void readExternal(ObjectInput is) throws IOException, ClassNotFoundException {
        this.humanPresentableName = (String) is.readObject();
        this.mimeInfo = (MimeType) is.readObject();
        this.representationClass = this.mimeInfo != null ? Class.forName(this.mimeInfo.getParameter("class")) : null;
    }

    public Object clone() throws CloneNotSupportedException {
        MimeType mimeType;
        DataFlavor clone = new DataFlavor();
        clone.humanPresentableName = this.humanPresentableName;
        clone.representationClass = this.representationClass;
        if (this.mimeInfo != null) {
            mimeType = (MimeType) this.mimeInfo.clone();
        } else {
            mimeType = null;
        }
        clone.mimeInfo = mimeType;
        return clone;
    }

    public String toString() {
        return new StringBuilder(String.valueOf(getClass().getName())).append("[MimeType=(").append(getMimeType()).append(");humanPresentableName=").append(this.humanPresentableName).append("]").toString();
    }

    public boolean isMimeTypeSerializedObject() {
        return isMimeTypeEqual("application/x-java-serialized-object");
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof DataFlavor)) {
            return false;
        }
        return equals((DataFlavor) o);
    }

    public boolean equals(DataFlavor that) {
        if (that == this) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (this.mimeInfo == null) {
            if (that.mimeInfo != null) {
                return false;
            }
            return true;
        } else if (!this.mimeInfo.equals(that.mimeInfo) || !this.representationClass.equals(that.representationClass)) {
            return false;
        } else {
            if (!this.mimeInfo.getPrimaryType().equals("text") || isUnicodeFlavor()) {
                return true;
            }
            String charset1 = getCharset();
            String charset2 = that.getCharset();
            if (isCharsetSupported(charset1) && isCharsetSupported(charset2)) {
                return Charset.forName(charset1).equals(Charset.forName(charset2));
            }
            return charset1.equalsIgnoreCase(charset2);
        }
    }

    @Deprecated
    public boolean equals(String s) {
        if (s == null) {
            return false;
        }
        return isMimeTypeEqual(s);
    }

    public boolean match(DataFlavor that) {
        return equals(that);
    }

    public int hashCode() {
        return getKeyInfo().hashCode();
    }

    private String getKeyInfo() {
        String key = new StringBuilder(String.valueOf(this.mimeInfo.getFullType())).append(";class=").append(this.representationClass.getName()).toString();
        return (!this.mimeInfo.getPrimaryType().equals("text") || isUnicodeFlavor()) ? key : new StringBuilder(String.valueOf(key)).append(";charset=").append(getCharset().toLowerCase()).toString();
    }

    public boolean isFlavorSerializedObjectType() {
        return isMimeTypeSerializedObject() && isRepresentationClassSerializable();
    }

    public boolean isFlavorRemoteObjectType() {
        return isMimeTypeEqual(javaRemoteObjectMimeType) && isRepresentationClassRemote();
    }

    public boolean isFlavorJavaFileListType() {
        return List.class.isAssignableFrom(this.representationClass) && isMimeTypeEqual(javaFileListFlavor);
    }

    public boolean isFlavorTextType() {
        if (equals(stringFlavor) || equals(plainTextFlavor)) {
            return true;
        }
        if (this.mimeInfo != null && !this.mimeInfo.getPrimaryType().equals("text")) {
            return false;
        }
        String charset = getCharset();
        if (!isByteCodeFlavor()) {
            return isUnicodeFlavor();
        }
        if (charset.length() != 0) {
            return isCharsetSupported(charset);
        }
        return true;
    }

    public Reader getReaderForText(Transferable transferable) throws UnsupportedFlavorException, IOException {
        Reader data = transferable.getTransferData(this);
        if (data == null) {
            throw new IllegalArgumentException(Messages.getString("awt.16E"));
        } else if (data instanceof Reader) {
            Reader reader = data;
            reader.reset();
            return reader;
        } else if (data instanceof String) {
            return new StringReader((String) data);
        } else {
            if (data instanceof CharBuffer) {
                return new CharArrayReader(((CharBuffer) data).array());
            }
            if (data instanceof char[]) {
                return new CharArrayReader((char[]) data);
            }
            InputStream stream;
            String charset = getCharset();
            if (data instanceof InputStream) {
                stream = (InputStream) data;
                stream.reset();
            } else if (data instanceof ByteBuffer) {
                stream = new ByteArrayInputStream(((ByteBuffer) data).array());
            } else if (data instanceof byte[]) {
                stream = new ByteArrayInputStream((byte[]) data);
            } else {
                throw new IllegalArgumentException(Messages.getString("awt.16F"));
            }
            if (charset.length() == 0) {
                return new InputStreamReader(stream);
            }
            return new InputStreamReader(stream, charset);
        }
    }

    public static final DataFlavor selectBestTextFlavor(DataFlavor[] availableFlavors) {
        if (availableFlavors == null) {
            return null;
        }
        List<List<DataFlavor>> sorted = sortTextFlavorsByType(new LinkedList(Arrays.asList(availableFlavors)));
        if (sorted.isEmpty()) {
            return null;
        }
        List<DataFlavor> bestSorted = (List) sorted.get(0);
        if (bestSorted.size() == 1) {
            return (DataFlavor) bestSorted.get(0);
        }
        if (((DataFlavor) bestSorted.get(0)).getCharset().length() == 0) {
            return selectBestFlavorWOCharset(bestSorted);
        }
        return selectBestFlavorWCharset(bestSorted);
    }

    private static DataFlavor selectBestFlavorWCharset(List<DataFlavor> list) {
        List<DataFlavor> best = getFlavors((List) list, Reader.class);
        if (best != null) {
            return (DataFlavor) best.get(0);
        }
        best = getFlavors((List) list, String.class);
        if (best != null) {
            return (DataFlavor) best.get(0);
        }
        best = getFlavors((List) list, CharBuffer.class);
        if (best != null) {
            return (DataFlavor) best.get(0);
        }
        best = getFlavors((List) list, char[].class);
        if (best != null) {
            return (DataFlavor) best.get(0);
        }
        return selectBestByCharset(list);
    }

    private static DataFlavor selectBestByCharset(List<DataFlavor> list) {
        List<DataFlavor> best = getFlavors((List) list, new String[]{"UTF-16", "UTF-8", "UTF-16BE", "UTF-16LE"});
        if (best == null) {
            best = getFlavors((List) list, new String[]{DTK.getDTK().getDefaultCharset()});
            if (best == null) {
                best = getFlavors((List) list, new String[]{"US-ASCII"});
                if (best == null) {
                    best = selectBestByAlphabet(list);
                }
            }
        }
        if (best == null) {
            return null;
        }
        if (best.size() == 1) {
            return (DataFlavor) best.get(0);
        }
        return selectBestFlavorWOCharset(best);
    }

    private static List<DataFlavor> selectBestByAlphabet(List<DataFlavor> list) {
        String[] charsets = new String[list.size()];
        LinkedList<DataFlavor> best = new LinkedList();
        for (int i = 0; i < charsets.length; i++) {
            charsets[i] = ((DataFlavor) list.get(i)).getCharset();
        }
        Arrays.sort(charsets, String.CASE_INSENSITIVE_ORDER);
        for (DataFlavor flavor : list) {
            if (charsets[0].equalsIgnoreCase(flavor.getCharset())) {
                best.add(flavor);
            }
        }
        return best.isEmpty() ? null : best;
    }

    private static List<DataFlavor> getFlavors(List<DataFlavor> list, String[] charset) {
        LinkedList<DataFlavor> sublist = new LinkedList();
        Iterator<DataFlavor> i = list.iterator();
        while (i.hasNext()) {
            DataFlavor flavor = (DataFlavor) i.next();
            if (isCharsetSupported(flavor.getCharset())) {
                for (String element : charset) {
                    if (Charset.forName(element).equals(Charset.forName(flavor.getCharset()))) {
                        sublist.add(flavor);
                    }
                }
            } else {
                i.remove();
            }
        }
        return sublist.isEmpty() ? null : list;
    }

    private static DataFlavor selectBestFlavorWOCharset(List<DataFlavor> list) {
        List<DataFlavor> best = getFlavors((List) list, InputStream.class);
        if (best != null) {
            return (DataFlavor) best.get(0);
        }
        best = getFlavors((List) list, ByteBuffer.class);
        if (best != null) {
            return (DataFlavor) best.get(0);
        }
        best = getFlavors((List) list, byte[].class);
        if (best != null) {
            return (DataFlavor) best.get(0);
        }
        return (DataFlavor) list.get(0);
    }

    private static List<DataFlavor> getFlavors(List<DataFlavor> list, Class<?> klass) {
        LinkedList<DataFlavor> sublist = new LinkedList();
        for (DataFlavor flavor : list) {
            if (flavor.representationClass.equals(klass)) {
                sublist.add(flavor);
            }
        }
        return sublist.isEmpty() ? null : list;
    }

    private static List<List<DataFlavor>> sortTextFlavorsByType(List<DataFlavor> availableFlavors) {
        LinkedList<List<DataFlavor>> list = new LinkedList();
        for (String element : sortedTextFlavors) {
            List<DataFlavor> subList = fetchTextFlavors(availableFlavors, element);
            if (subList != null) {
                list.addLast(subList);
            }
        }
        if (!availableFlavors.isEmpty()) {
            list.addLast(availableFlavors);
        }
        return list;
    }

    private static List<DataFlavor> fetchTextFlavors(List<DataFlavor> availableFlavors, String mimeType) {
        LinkedList<DataFlavor> list = new LinkedList();
        Iterator<DataFlavor> i = availableFlavors.iterator();
        while (i.hasNext()) {
            DataFlavor flavor = (DataFlavor) i.next();
            if (!flavor.isFlavorTextType()) {
                i.remove();
            } else if (flavor.mimeInfo.getFullType().equals(mimeType)) {
                if (!list.contains(flavor)) {
                    list.add(flavor);
                }
                i.remove();
            }
        }
        return list.isEmpty() ? null : list;
    }

    private boolean isUnicodeFlavor() {
        return this.representationClass != null && (this.representationClass.equals(Reader.class) || this.representationClass.equals(String.class) || this.representationClass.equals(CharBuffer.class) || this.representationClass.equals(char[].class));
    }

    private boolean isByteCodeFlavor() {
        return this.representationClass != null && (this.representationClass.equals(InputStream.class) || this.representationClass.equals(ByteBuffer.class) || this.representationClass.equals(byte[].class));
    }
}
