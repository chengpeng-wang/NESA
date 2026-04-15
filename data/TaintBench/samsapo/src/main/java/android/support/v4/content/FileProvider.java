package android.support.v4.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import org.xmlpull.v1.XmlPullParserException;

public class FileProvider extends ContentProvider {
    private static final String ATTR_NAME = "name";
    private static final String ATTR_PATH = "path";
    private static final String[] COLUMNS;
    private static final File DEVICE_ROOT;
    private static final String META_DATA_FILE_PROVIDER_PATHS = "android.support.FILE_PROVIDER_PATHS";
    private static final String TAG_CACHE_PATH = "cache-path";
    private static final String TAG_EXTERNAL = "external-path";
    private static final String TAG_FILES_PATH = "files-path";
    private static final String TAG_ROOT_PATH = "root-path";
    private static HashMap<String, PathStrategy> sCache;
    private PathStrategy mStrategy;

    interface PathStrategy {
        File getFileForUri(Uri uri);

        Uri getUriForFile(File file);
    }

    static class SimplePathStrategy implements PathStrategy {
        private final String mAuthority;
        private final HashMap<String, File> mRoots;

        public SimplePathStrategy(String str) {
            String str2 = str;
            HashMap hashMap = r5;
            HashMap hashMap2 = new HashMap();
            this.mRoots = hashMap;
            this.mAuthority = str2;
        }

        public void addRoot(String str, File file) {
            String str2 = str;
            File file2 = file;
            IllegalArgumentException illegalArgumentException;
            IllegalArgumentException illegalArgumentException2;
            if (TextUtils.isEmpty(str2)) {
                illegalArgumentException = r8;
                illegalArgumentException2 = new IllegalArgumentException("Name must not be empty");
                throw illegalArgumentException;
            }
            try {
                Object put = this.mRoots.put(str2, file2.getCanonicalFile());
            } catch (IOException e) {
                Throwable th = e;
                illegalArgumentException = r8;
                StringBuilder stringBuilder = r8;
                StringBuilder stringBuilder2 = new StringBuilder();
                illegalArgumentException2 = new IllegalArgumentException(stringBuilder.append("Failed to resolve canonical path for ").append(file2).toString(), th);
                throw illegalArgumentException;
            }
        }

        public Uri getUriForFile(File file) {
            File file2 = file;
            IllegalArgumentException illegalArgumentException;
            StringBuilder stringBuilder;
            StringBuilder stringBuilder2;
            IllegalArgumentException illegalArgumentException2;
            try {
                String canonicalPath = file2.getCanonicalPath();
                Entry entry = null;
                for (Entry entry2 : this.mRoots.entrySet()) {
                    String path = ((File) entry2.getValue()).getPath();
                    if (canonicalPath.startsWith(path) && (entry == null || path.length() > ((File) entry.getValue()).getPath().length())) {
                        entry = entry2;
                    }
                }
                if (entry == null) {
                    illegalArgumentException = r11;
                    stringBuilder = r11;
                    stringBuilder2 = new StringBuilder();
                    illegalArgumentException2 = new IllegalArgumentException(stringBuilder.append("Failed to find configured root that contains ").append(canonicalPath).toString());
                    throw illegalArgumentException;
                }
                String path2 = ((File) entry.getValue()).getPath();
                if (path2.endsWith("/")) {
                    canonicalPath = canonicalPath.substring(path2.length());
                } else {
                    canonicalPath = canonicalPath.substring(path2.length() + 1);
                }
                StringBuilder stringBuilder3 = r11;
                StringBuilder stringBuilder4 = new StringBuilder();
                canonicalPath = stringBuilder3.append(Uri.encode((String) entry.getKey())).append('/').append(Uri.encode(canonicalPath, "/")).toString();
                Builder builder = r11;
                Builder builder2 = new Builder();
                return builder.scheme("content").authority(this.mAuthority).encodedPath(canonicalPath).build();
            } catch (IOException e) {
                IOException iOException = e;
                illegalArgumentException = r11;
                stringBuilder = r11;
                stringBuilder2 = new StringBuilder();
                illegalArgumentException2 = new IllegalArgumentException(stringBuilder.append("Failed to resolve canonical path for ").append(file2).toString());
                throw illegalArgumentException;
            }
        }

        public File getFileForUri(Uri uri) {
            Uri uri2 = uri;
            String encodedPath = uri2.getEncodedPath();
            int indexOf = encodedPath.indexOf(47, 1);
            String decode = Uri.decode(encodedPath.substring(1, indexOf));
            encodedPath = Uri.decode(encodedPath.substring(indexOf + 1));
            File file = (File) this.mRoots.get(decode);
            IllegalArgumentException illegalArgumentException;
            StringBuilder stringBuilder;
            StringBuilder stringBuilder2;
            IllegalArgumentException illegalArgumentException2;
            if (file == null) {
                illegalArgumentException = r12;
                stringBuilder = r12;
                stringBuilder2 = new StringBuilder();
                illegalArgumentException2 = new IllegalArgumentException(stringBuilder.append("Unable to find configured root for ").append(uri2).toString());
                throw illegalArgumentException;
            }
            File file2 = r12;
            File file3 = new File(file, encodedPath);
            File file4 = file2;
            try {
                file4 = file4.getCanonicalFile();
                if (file4.getPath().startsWith(file.getPath())) {
                    return file4;
                }
                SecurityException securityException = r12;
                SecurityException securityException2 = new SecurityException("Resolved path jumped beyond configured root");
                throw securityException;
            } catch (IOException e) {
                IOException iOException = e;
                illegalArgumentException = r12;
                stringBuilder = r12;
                stringBuilder2 = new StringBuilder();
                illegalArgumentException2 = new IllegalArgumentException(stringBuilder.append("Failed to resolve canonical path for ").append(file4).toString());
                throw illegalArgumentException;
            }
        }
    }

    public FileProvider() {
    }

    static {
        r4 = new String[2];
        String[] strArr = r4;
        r4[0] = "_display_name";
        r4 = strArr;
        strArr = r4;
        r4[1] = "_size";
        COLUMNS = strArr;
        File file = r4;
        File file2 = new File("/");
        DEVICE_ROOT = file;
        HashMap hashMap = r4;
        HashMap hashMap2 = new HashMap();
        sCache = hashMap;
    }

    public boolean onCreate() {
        return true;
    }

    public void attachInfo(Context context, ProviderInfo providerInfo) {
        Context context2 = context;
        ProviderInfo providerInfo2 = providerInfo;
        super.attachInfo(context2, providerInfo2);
        SecurityException securityException;
        SecurityException securityException2;
        if (providerInfo2.exported) {
            securityException = r6;
            securityException2 = new SecurityException("Provider must not be exported");
            throw securityException;
        } else if (providerInfo2.grantUriPermissions) {
            this.mStrategy = getPathStrategy(context2, providerInfo2.authority);
        } else {
            securityException = r6;
            securityException2 = new SecurityException("Provider must grant uri permissions");
            throw securityException;
        }
    }

    public static Uri getUriForFile(Context context, String str, File file) {
        return getPathStrategy(context, str).getUriForFile(file);
    }

    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        String[] strArr3 = strArr;
        String str3 = str;
        String[] strArr4 = strArr2;
        String str4 = str2;
        File fileForUri = this.mStrategy.getFileForUri(uri);
        if (strArr3 == null) {
            strArr3 = COLUMNS;
        }
        String[] strArr5 = new String[strArr3.length];
        Object[] objArr = new Object[strArr3.length];
        int i = 0;
        for (String str5 : strArr3) {
            int i2;
            if ("_display_name".equals(str5)) {
                strArr5[i] = "_display_name";
                i2 = i;
                i++;
                objArr[i2] = fileForUri.getName();
            } else if ("_size".equals(str5)) {
                strArr5[i] = "_size";
                i2 = i;
                i++;
                objArr[i2] = Long.valueOf(fileForUri.length());
            }
        }
        strArr5 = copyOf(strArr5, i);
        objArr = copyOf(objArr, i);
        MatrixCursor matrixCursor = r18;
        MatrixCursor matrixCursor2 = new MatrixCursor(strArr5, 1);
        MatrixCursor matrixCursor3 = matrixCursor;
        matrixCursor3.addRow(objArr);
        return matrixCursor3;
    }

    public String getType(Uri uri) {
        File fileForUri = this.mStrategy.getFileForUri(uri);
        int lastIndexOf = fileForUri.getName().lastIndexOf(46);
        if (lastIndexOf >= 0) {
            String mimeTypeFromExtension = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileForUri.getName().substring(lastIndexOf + 1));
            if (mimeTypeFromExtension != null) {
                return mimeTypeFromExtension;
            }
        }
        return "application/octet-stream";
    }

    public Uri insert(Uri uri, ContentValues contentValues) {
        Uri uri2 = uri;
        ContentValues contentValues2 = contentValues;
        UnsupportedOperationException unsupportedOperationException = r6;
        UnsupportedOperationException unsupportedOperationException2 = new UnsupportedOperationException("No external inserts");
        throw unsupportedOperationException;
    }

    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        Uri uri2 = uri;
        ContentValues contentValues2 = contentValues;
        String str2 = str;
        String[] strArr2 = strArr;
        UnsupportedOperationException unsupportedOperationException = r8;
        UnsupportedOperationException unsupportedOperationException2 = new UnsupportedOperationException("No external updates");
        throw unsupportedOperationException;
    }

    public int delete(Uri uri, String str, String[] strArr) {
        String str2 = str;
        String[] strArr2 = strArr;
        return this.mStrategy.getFileForUri(uri).delete() ? 1 : 0;
    }

    public ParcelFileDescriptor openFile(Uri uri, String str) throws FileNotFoundException {
        String str2 = str;
        return ParcelFileDescriptor.open(this.mStrategy.getFileForUri(uri), modeToMode(str2));
    }

    /* JADX WARNING: Missing block: B:20:0x003c, code skipped:
            r6 = r5;
     */
    private static android.support.v4.content.FileProvider.PathStrategy getPathStrategy(android.content.Context r11, java.lang.String r12) {
        /*
        r0 = r11;
        r1 = r12;
        r6 = sCache;
        r10 = r6;
        r6 = r10;
        r7 = r10;
        r3 = r7;
        monitor-enter(r6);
        r6 = sCache;	 Catch:{ all -> 0x0038 }
        r7 = r1;
        r6 = r6.get(r7);	 Catch:{ all -> 0x0038 }
        r6 = (android.support.v4.content.FileProvider.PathStrategy) r6;	 Catch:{ all -> 0x0038 }
        r2 = r6;
        r6 = r2;
        if (r6 != 0) goto L_0x0025;
    L_0x0016:
        r6 = r0;
        r7 = r1;
        r6 = parsePathStrategy(r6, r7);	 Catch:{ IOException -> 0x002a, XmlPullParserException -> 0x003e }
        r2 = r6;
        r6 = sCache;	 Catch:{ all -> 0x0038 }
        r7 = r1;
        r8 = r2;
        r6 = r6.put(r7, r8);	 Catch:{ all -> 0x0038 }
    L_0x0025:
        r6 = r3;
        monitor-exit(r6);	 Catch:{ all -> 0x0038 }
        r6 = r2;
        r0 = r6;
        return r0;
    L_0x002a:
        r6 = move-exception;
        r4 = r6;
        r6 = new java.lang.IllegalArgumentException;	 Catch:{ all -> 0x0038 }
        r10 = r6;
        r6 = r10;
        r7 = r10;
        r8 = "Failed to parse android.support.FILE_PROVIDER_PATHS meta-data";
        r9 = r4;
        r7.<init>(r8, r9);	 Catch:{ all -> 0x0038 }
        throw r6;	 Catch:{ all -> 0x0038 }
    L_0x0038:
        r6 = move-exception;
        r5 = r6;
        r6 = r3;
        monitor-exit(r6);	 Catch:{ all -> 0x0038 }
        r6 = r5;
        throw r6;
    L_0x003e:
        r6 = move-exception;
        r4 = r6;
        r6 = new java.lang.IllegalArgumentException;	 Catch:{ all -> 0x0038 }
        r10 = r6;
        r6 = r10;
        r7 = r10;
        r8 = "Failed to parse android.support.FILE_PROVIDER_PATHS meta-data";
        r9 = r4;
        r7.<init>(r8, r9);	 Catch:{ all -> 0x0038 }
        throw r6;	 Catch:{ all -> 0x0038 }
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.content.FileProvider.getPathStrategy(android.content.Context, java.lang.String):android.support.v4.content.FileProvider$PathStrategy");
    }

    private static PathStrategy parsePathStrategy(Context context, String str) throws IOException, XmlPullParserException {
        Context context2 = context;
        String str2 = str;
        SimplePathStrategy simplePathStrategy = r15;
        SimplePathStrategy simplePathStrategy2 = new SimplePathStrategy(str2);
        SimplePathStrategy simplePathStrategy3 = simplePathStrategy;
        XmlResourceParser loadXmlMetaData = context2.getPackageManager().resolveContentProvider(str2, 128).loadXmlMetaData(context2.getPackageManager(), META_DATA_FILE_PROVIDER_PATHS);
        if (loadXmlMetaData == null) {
            IllegalArgumentException illegalArgumentException = r15;
            IllegalArgumentException illegalArgumentException2 = new IllegalArgumentException("Missing android.support.FILE_PROVIDER_PATHS meta-data");
            throw illegalArgumentException;
        }
        while (true) {
            int next = loadXmlMetaData.next();
            int i = next;
            if (next == 1) {
                return simplePathStrategy3;
            }
            if (i == 2) {
                String name = loadXmlMetaData.getName();
                String attributeValue = loadXmlMetaData.getAttributeValue(null, ATTR_NAME);
                String attributeValue2 = loadXmlMetaData.getAttributeValue(null, ATTR_PATH);
                File file = null;
                File file2;
                String[] strArr;
                String[] strArr2;
                if (TAG_ROOT_PATH.equals(name)) {
                    file2 = DEVICE_ROOT;
                    strArr = new String[1];
                    strArr2 = strArr;
                    strArr[0] = attributeValue2;
                    file = buildPath(file2, strArr2);
                } else if (TAG_FILES_PATH.equals(name)) {
                    file2 = context2.getFilesDir();
                    strArr = new String[1];
                    strArr2 = strArr;
                    strArr[0] = attributeValue2;
                    file = buildPath(file2, strArr2);
                } else if (TAG_CACHE_PATH.equals(name)) {
                    file2 = context2.getCacheDir();
                    strArr = new String[1];
                    strArr2 = strArr;
                    strArr[0] = attributeValue2;
                    file = buildPath(file2, strArr2);
                } else if (TAG_EXTERNAL.equals(name)) {
                    file2 = Environment.getExternalStorageDirectory();
                    strArr = new String[1];
                    strArr2 = strArr;
                    strArr[0] = attributeValue2;
                    file = buildPath(file2, strArr2);
                }
                if (file != null) {
                    simplePathStrategy3.addRoot(attributeValue, file);
                }
            }
        }
    }

    private static int modeToMode(String str) {
        int i;
        String str2 = str;
        if ("r".equals(str2)) {
            i = 268435456;
        } else if ("w".equals(str2) || "wt".equals(str2)) {
            i = 738197504;
        } else if ("wa".equals(str2)) {
            i = 704643072;
        } else if ("rw".equals(str2)) {
            i = 939524096;
        } else if ("rwt".equals(str2)) {
            i = 1006632960;
        } else {
            IllegalArgumentException illegalArgumentException = r6;
            StringBuilder stringBuilder = r6;
            StringBuilder stringBuilder2 = new StringBuilder();
            IllegalArgumentException illegalArgumentException2 = new IllegalArgumentException(stringBuilder.append("Invalid mode: ").append(str2).toString());
            throw illegalArgumentException;
        }
        return i;
    }

    private static File buildPath(File file, String... strArr) {
        File file2 = file;
        for (String str : strArr) {
            if (str != null) {
                File file3 = r11;
                File file4 = new File(file2, str);
                file2 = file3;
            }
        }
        return file2;
    }

    private static String[] copyOf(String[] strArr, int i) {
        int i2 = i;
        Object obj = new String[i2];
        System.arraycopy(strArr, 0, obj, 0, i2);
        return obj;
    }

    private static Object[] copyOf(Object[] objArr, int i) {
        int i2 = i;
        Object obj = new Object[i2];
        System.arraycopy(objArr, 0, obj, 0, i2);
        return obj;
    }
}
