package android.support.v4.util;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AtomicFile {
    private final File mBackupName;
    private final File mBaseName;

    public AtomicFile(File file) {
        File file2 = file;
        this.mBaseName = file2;
        File file3 = r7;
        StringBuilder stringBuilder = r7;
        StringBuilder stringBuilder2 = new StringBuilder();
        File file4 = new File(stringBuilder.append(file2.getPath()).append(".bak").toString());
        this.mBackupName = file3;
    }

    public File getBaseFile() {
        return this.mBaseName;
    }

    public void delete() {
        boolean delete = this.mBaseName.delete();
        delete = this.mBackupName.delete();
    }

    public FileOutputStream startWrite() throws IOException {
        StringBuilder stringBuilder;
        FileOutputStream fileOutputStream;
        IOException iOException;
        StringBuilder stringBuilder2;
        IOException iOException2;
        if (this.mBaseName.exists()) {
            if (this.mBackupName.exists()) {
                boolean delete = this.mBaseName.delete();
            } else if (!this.mBaseName.renameTo(this.mBackupName)) {
                StringBuilder stringBuilder3 = r9;
                stringBuilder = new StringBuilder();
                int w = Log.w("AtomicFile", stringBuilder3.append("Couldn't rename file ").append(this.mBaseName).append(" to backup file ").append(this.mBackupName).toString());
            }
        }
        Object obj = null;
        FileOutputStream fileOutputStream2;
        FileOutputStream fileOutputStream3;
        try {
            fileOutputStream2 = r9;
            fileOutputStream3 = new FileOutputStream(this.mBaseName);
            fileOutputStream = fileOutputStream2;
        } catch (FileNotFoundException e) {
            FileNotFoundException fileNotFoundException = e;
            if (this.mBaseName.getParentFile().mkdir()) {
                try {
                    fileOutputStream2 = r9;
                    fileOutputStream3 = new FileOutputStream(this.mBaseName);
                    fileOutputStream = fileOutputStream2;
                } catch (FileNotFoundException e2) {
                    FileNotFoundException fileNotFoundException2 = e2;
                    iOException = r9;
                    stringBuilder = r9;
                    stringBuilder2 = new StringBuilder();
                    iOException2 = new IOException(stringBuilder.append("Couldn't create ").append(this.mBaseName).toString());
                    throw iOException;
                }
            }
            iOException = r9;
            stringBuilder = r9;
            stringBuilder2 = new StringBuilder();
            iOException2 = new IOException(stringBuilder.append("Couldn't create directory ").append(this.mBaseName).toString());
            throw iOException;
        }
        return fileOutputStream;
    }

    public void finishWrite(FileOutputStream fileOutputStream) {
        FileOutputStream fileOutputStream2 = fileOutputStream;
        if (fileOutputStream2 != null) {
            boolean sync = sync(fileOutputStream2);
            try {
                fileOutputStream2.close();
                sync = this.mBackupName.delete();
            } catch (IOException e) {
                int w = Log.w("AtomicFile", "finishWrite: Got exception:", e);
            }
        }
    }

    public void failWrite(FileOutputStream fileOutputStream) {
        FileOutputStream fileOutputStream2 = fileOutputStream;
        if (fileOutputStream2 != null) {
            boolean sync = sync(fileOutputStream2);
            try {
                fileOutputStream2.close();
                sync = this.mBaseName.delete();
                sync = this.mBackupName.renameTo(this.mBaseName);
            } catch (IOException e) {
                int w = Log.w("AtomicFile", "failWrite: Got exception:", e);
            }
        }
    }

    public FileInputStream openRead() throws FileNotFoundException {
        if (this.mBackupName.exists()) {
            boolean delete = this.mBaseName.delete();
            delete = this.mBackupName.renameTo(this.mBaseName);
        }
        FileInputStream fileInputStream = r4;
        FileInputStream fileInputStream2 = new FileInputStream(this.mBaseName);
        return fileInputStream;
    }

    public byte[] readFully() throws IOException {
        byte[] bArr;
        FileInputStream openRead = openRead();
        int i = 0;
        try {
            byte[] bArr2 = new byte[openRead.available()];
            while (true) {
                int read = openRead.read(bArr2, i, bArr2.length - i);
                if (read <= 0) {
                    break;
                }
                i += read;
                int available = openRead.available();
                if (available > bArr2.length - i) {
                    Object obj = new byte[(i + available)];
                    System.arraycopy(bArr2, 0, obj, 0, i);
                    bArr2 = obj;
                }
            }
            bArr = bArr2;
            return this;
        } finally {
            byte[] bArr3 = bArr;
            openRead.close();
            bArr = bArr3;
        }
    }

    static boolean sync(FileOutputStream fileOutputStream) {
        FileOutputStream fileOutputStream2 = fileOutputStream;
        if (fileOutputStream2 != null) {
            try {
                fileOutputStream2.getFD().sync();
            } catch (IOException e) {
                IOException iOException = e;
                return false;
            }
        }
        return true;
    }
}
