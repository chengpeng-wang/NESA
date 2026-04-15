package org.apache.james.mime4j.storage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TempFileStorageProvider extends AbstractStorageProvider {
    private static final String DEFAULT_PREFIX = "m4j";
    private final File directory;
    private final String prefix;
    private final String suffix;

    private static final class TempFileStorage implements Storage {
        private static final Set<File> filesToDelete = new HashSet();
        private File file;

        public TempFileStorage(File file) {
            this.file = file;
        }

        public void delete() {
            synchronized (filesToDelete) {
                if (this.file != null) {
                    filesToDelete.add(this.file);
                    this.file = null;
                }
                Iterator<File> iterator = filesToDelete.iterator();
                while (iterator.hasNext()) {
                    if (((File) iterator.next()).delete()) {
                        iterator.remove();
                    }
                }
            }
        }

        public InputStream getInputStream() throws IOException {
            if (this.file != null) {
                return new BufferedInputStream(new FileInputStream(this.file));
            }
            throw new IllegalStateException("storage has been deleted");
        }
    }

    private static final class TempFileStorageOutputStream extends StorageOutputStream {
        private File file;
        private OutputStream out;

        public TempFileStorageOutputStream(File file) throws IOException {
            this.file = file;
            this.out = new FileOutputStream(file);
        }

        public void close() throws IOException {
            super.close();
            this.out.close();
        }

        /* access modifiers changed from: protected */
        public void write0(byte[] buffer, int offset, int length) throws IOException {
            this.out.write(buffer, offset, length);
        }

        /* access modifiers changed from: protected */
        public Storage toStorage0() throws IOException {
            return new TempFileStorage(this.file);
        }
    }

    public TempFileStorageProvider() {
        this(DEFAULT_PREFIX, null, null);
    }

    public TempFileStorageProvider(File directory) {
        this(DEFAULT_PREFIX, null, directory);
    }

    public TempFileStorageProvider(String prefix, String suffix, File directory) {
        if (prefix == null || prefix.length() < 3) {
            throw new IllegalArgumentException("invalid prefix");
        } else if (directory == null || directory.isDirectory() || directory.mkdirs()) {
            this.prefix = prefix;
            this.suffix = suffix;
            this.directory = directory;
        } else {
            throw new IllegalArgumentException("invalid directory");
        }
    }

    public StorageOutputStream createStorageOutputStream() throws IOException {
        File file = File.createTempFile(this.prefix, this.suffix, this.directory);
        file.deleteOnExit();
        return new TempFileStorageOutputStream(file);
    }
}
