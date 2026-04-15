package com.mopub.common;

import android.support.annotation.NonNull;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public final class DiskLruCache implements Closeable {
    static final long ANY_SEQUENCE_NUMBER = -1;
    private static final String CLEAN = "CLEAN";
    private static final String DIRTY = "DIRTY";
    static final String JOURNAL_FILE = "journal";
    static final String JOURNAL_FILE_BACKUP = "journal.bkp";
    static final String JOURNAL_FILE_TEMP = "journal.tmp";
    static final Pattern LEGAL_KEY_PATTERN = Pattern.compile("[a-z0-9_-]{1,64}");
    static final String MAGIC = "libcore.io.DiskLruCache";
    /* access modifiers changed from: private|static|final */
    public static final OutputStream NULL_OUTPUT_STREAM = new OutputStream() {
        public final void write(int b) throws IOException {
        }
    };
    private static final String READ = "READ";
    private static final String REMOVE = "REMOVE";
    static final String VERSION_1 = "1";
    private final int appVersion;
    private final Callable<Void> cleanupCallable = new Callable<Void>() {
        public Void call() throws Exception {
            synchronized (DiskLruCache.this) {
                if (DiskLruCache.this.journalWriter == null) {
                } else {
                    DiskLruCache.this.trimToSize();
                    if (DiskLruCache.this.journalRebuildRequired()) {
                        DiskLruCache.this.rebuildJournal();
                        DiskLruCache.this.redundantOpCount = 0;
                    }
                }
            }
            return null;
        }
    };
    /* access modifiers changed from: private|final */
    public final File directory;
    final ThreadPoolExecutor executorService = new ThreadPoolExecutor(0, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue());
    private final File journalFile;
    private final File journalFileBackup;
    private final File journalFileTmp;
    /* access modifiers changed from: private */
    public Writer journalWriter;
    private final LinkedHashMap<String, Entry> lruEntries = new LinkedHashMap(0, 0.75f, true);
    private long maxSize;
    private long nextSequenceNumber = 0;
    /* access modifiers changed from: private */
    public int redundantOpCount;
    private long size = 0;
    /* access modifiers changed from: private|final */
    public final int valueCount;

    public final class Editor {
        private boolean committed;
        /* access modifiers changed from: private|final */
        public final Entry entry;
        /* access modifiers changed from: private */
        public boolean hasErrors;
        /* access modifiers changed from: private|final */
        public final boolean[] written;

        private class FaultHidingOutputStream extends FilterOutputStream {
            /* synthetic */ FaultHidingOutputStream(Editor x0, OutputStream x1, AnonymousClass1 x2) {
                this(x1);
            }

            private FaultHidingOutputStream(OutputStream out) {
                super(out);
            }

            public void write(int oneByte) {
                try {
                    this.out.write(oneByte);
                } catch (IOException e) {
                    Editor.this.hasErrors = true;
                }
            }

            public void write(@NonNull byte[] buffer, int offset, int length) {
                try {
                    this.out.write(buffer, offset, length);
                } catch (IOException e) {
                    Editor.this.hasErrors = true;
                }
            }

            public void close() {
                try {
                    this.out.close();
                } catch (IOException e) {
                    Editor.this.hasErrors = true;
                }
            }

            public void flush() {
                try {
                    this.out.flush();
                } catch (IOException e) {
                    Editor.this.hasErrors = true;
                }
            }
        }

        /* synthetic */ Editor(DiskLruCache x0, Entry x1, AnonymousClass1 x2) {
            this(x1);
        }

        private Editor(Entry entry) {
            this.entry = entry;
            this.written = entry.readable ? null : new boolean[DiskLruCache.this.valueCount];
        }

        public final InputStream newInputStream(int index) throws IOException {
            synchronized (DiskLruCache.this) {
                if (this.entry.currentEditor != this) {
                    throw new IllegalStateException();
                } else if (this.entry.readable) {
                    try {
                        InputStream fileInputStream = new FileInputStream(this.entry.getCleanFile(index));
                        return fileInputStream;
                    } catch (FileNotFoundException e) {
                        return null;
                    }
                } else {
                    return null;
                }
            }
        }

        public final String getString(int index) throws IOException {
            InputStream newInputStream = newInputStream(index);
            return newInputStream != null ? DiskLruCache.inputStreamToString(newInputStream) : null;
        }

        public final OutputStream newOutputStream(int index) throws IOException {
            OutputStream access$2000;
            synchronized (DiskLruCache.this) {
                if (this.entry.currentEditor != this) {
                    throw new IllegalStateException();
                }
                OutputStream fileOutputStream;
                if (!this.entry.readable) {
                    this.written[index] = true;
                }
                File dirtyFile = this.entry.getDirtyFile(index);
                try {
                    fileOutputStream = new FileOutputStream(dirtyFile);
                } catch (FileNotFoundException e) {
                    DiskLruCache.this.directory.mkdirs();
                    try {
                        fileOutputStream = new FileOutputStream(dirtyFile);
                    } catch (FileNotFoundException e2) {
                        access$2000 = DiskLruCache.NULL_OUTPUT_STREAM;
                    }
                }
                access$2000 = new FaultHidingOutputStream(this, fileOutputStream, null);
            }
            return access$2000;
        }

        public final void set(int index, String value) throws IOException {
            Throwable th;
            Closeable outputStreamWriter;
            try {
                outputStreamWriter = new OutputStreamWriter(newOutputStream(index), DiskLruCacheUtil.UTF_8);
                try {
                    outputStreamWriter.write(value);
                    DiskLruCacheUtil.closeQuietly(outputStreamWriter);
                } catch (Throwable th2) {
                    th = th2;
                    DiskLruCacheUtil.closeQuietly(outputStreamWriter);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                outputStreamWriter = null;
                DiskLruCacheUtil.closeQuietly(outputStreamWriter);
                throw th;
            }
        }

        public final void commit() throws IOException {
            if (this.hasErrors) {
                DiskLruCache.this.completeEdit(this, false);
                DiskLruCache.this.remove(this.entry.key);
            } else {
                DiskLruCache.this.completeEdit(this, true);
            }
            this.committed = true;
        }

        public final void abort() throws IOException {
            DiskLruCache.this.completeEdit(this, false);
        }

        public final void abortUnlessCommitted() {
            if (!this.committed) {
                try {
                    abort();
                } catch (IOException e) {
                }
            }
        }
    }

    private final class Entry {
        /* access modifiers changed from: private */
        public Editor currentEditor;
        /* access modifiers changed from: private|final */
        public final String key;
        /* access modifiers changed from: private|final */
        public final long[] lengths;
        /* access modifiers changed from: private */
        public boolean readable;
        /* access modifiers changed from: private */
        public long sequenceNumber;

        /* synthetic */ Entry(DiskLruCache x0, String x1, AnonymousClass1 x2) {
            this(x1);
        }

        private Entry(String key) {
            this.key = key;
            this.lengths = new long[DiskLruCache.this.valueCount];
        }

        public final String getLengths() throws IOException {
            StringBuilder stringBuilder = new StringBuilder();
            for (long append : this.lengths) {
                stringBuilder.append(' ').append(append);
            }
            return stringBuilder.toString();
        }

        /* access modifiers changed from: private */
        public void setLengths(String[] strings) throws IOException {
            if (strings.length != DiskLruCache.this.valueCount) {
                throw invalidLengths(strings);
            }
            int i = 0;
            while (i < strings.length) {
                try {
                    this.lengths[i] = Long.parseLong(strings[i]);
                    i++;
                } catch (NumberFormatException e) {
                    throw invalidLengths(strings);
                }
            }
        }

        private IOException invalidLengths(String[] strings) throws IOException {
            throw new IOException("unexpected journal line: " + Arrays.toString(strings));
        }

        public final File getCleanFile(int i) {
            return new File(DiskLruCache.this.directory, this.key + "." + i);
        }

        public final File getDirtyFile(int i) {
            return new File(DiskLruCache.this.directory, this.key + "." + i + ".tmp");
        }
    }

    public final class Snapshot implements Closeable {
        private final InputStream[] ins;
        private final String key;
        private final long[] lengths;
        private final long sequenceNumber;

        /* synthetic */ Snapshot(DiskLruCache x0, String x1, long x2, InputStream[] x3, long[] x4, AnonymousClass1 x5) {
            this(x1, x2, x3, x4);
        }

        private Snapshot(String key, long sequenceNumber, InputStream[] ins, long[] lengths) {
            this.key = key;
            this.sequenceNumber = sequenceNumber;
            this.ins = ins;
            this.lengths = lengths;
        }

        public final Editor edit() throws IOException {
            return DiskLruCache.this.edit(this.key, this.sequenceNumber);
        }

        public final InputStream getInputStream(int index) {
            return this.ins[index];
        }

        public final String getString(int index) throws IOException {
            return DiskLruCache.inputStreamToString(getInputStream(index));
        }

        public final long getLength(int index) {
            return this.lengths[index];
        }

        public final void close() {
            for (Closeable closeQuietly : this.ins) {
                DiskLruCacheUtil.closeQuietly(closeQuietly);
            }
        }
    }

    private DiskLruCache(File directory, int appVersion, int valueCount, long maxSize) {
        this.directory = directory;
        this.appVersion = appVersion;
        this.journalFile = new File(directory, JOURNAL_FILE);
        this.journalFileTmp = new File(directory, JOURNAL_FILE_TEMP);
        this.journalFileBackup = new File(directory, JOURNAL_FILE_BACKUP);
        this.valueCount = valueCount;
        this.maxSize = maxSize;
    }

    public static DiskLruCache open(File directory, int appVersion, int valueCount, long maxSize) throws IOException {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        } else if (valueCount <= 0) {
            throw new IllegalArgumentException("valueCount <= 0");
        } else {
            File file = new File(directory, JOURNAL_FILE_BACKUP);
            if (file.exists()) {
                File file2 = new File(directory, JOURNAL_FILE);
                if (file2.exists()) {
                    file.delete();
                } else {
                    renameTo(file, file2, false);
                }
            }
            DiskLruCache diskLruCache = new DiskLruCache(directory, appVersion, valueCount, maxSize);
            if (diskLruCache.journalFile.exists()) {
                try {
                    diskLruCache.readJournal();
                    diskLruCache.processJournal();
                    diskLruCache.journalWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(diskLruCache.journalFile, true), DiskLruCacheUtil.US_ASCII));
                    return diskLruCache;
                } catch (IOException e) {
                    System.out.println("DiskLruCache " + directory + " is corrupt: " + e.getMessage() + ", removing");
                    diskLruCache.delete();
                }
            }
            directory.mkdirs();
            diskLruCache = new DiskLruCache(directory, appVersion, valueCount, maxSize);
            diskLruCache.rebuildJournal();
            return diskLruCache;
        }
    }

    private void readJournal() throws IOException {
        DiskLruCacheStrictLineReader diskLruCacheStrictLineReader = new DiskLruCacheStrictLineReader(new FileInputStream(this.journalFile), DiskLruCacheUtil.US_ASCII);
        int i;
        try {
            String readLine = diskLruCacheStrictLineReader.readLine();
            String readLine2 = diskLruCacheStrictLineReader.readLine();
            String readLine3 = diskLruCacheStrictLineReader.readLine();
            String readLine4 = diskLruCacheStrictLineReader.readLine();
            String readLine5 = diskLruCacheStrictLineReader.readLine();
            if (MAGIC.equals(readLine) && "1".equals(readLine2) && Integer.toString(this.appVersion).equals(readLine3) && Integer.toString(this.valueCount).equals(readLine4) && "".equals(readLine5)) {
                i = 0;
                while (true) {
                    readJournalLine(diskLruCacheStrictLineReader.readLine());
                    i++;
                }
            } else {
                throw new IOException("unexpected journal header: [" + readLine + ", " + readLine2 + ", " + readLine4 + ", " + readLine5 + "]");
            }
        } catch (EOFException e) {
            this.redundantOpCount = i - this.lruEntries.size();
            DiskLruCacheUtil.closeQuietly(diskLruCacheStrictLineReader);
        } catch (Throwable th) {
            DiskLruCacheUtil.closeQuietly(diskLruCacheStrictLineReader);
        }
    }

    private void readJournalLine(String line) throws IOException {
        int indexOf = line.indexOf(32);
        if (indexOf == -1) {
            throw new IOException("unexpected journal line: " + line);
        }
        Object substring;
        int i = indexOf + 1;
        int indexOf2 = line.indexOf(32, i);
        if (indexOf2 == -1) {
            String substring2 = line.substring(i);
            if (indexOf == 6 && line.startsWith(REMOVE)) {
                this.lruEntries.remove(substring2);
                return;
            }
            String substring3 = substring2;
        } else {
            substring3 = line.substring(i, indexOf2);
        }
        Entry entry = (Entry) this.lruEntries.get(substring3);
        if (entry == null) {
            entry = new Entry(this, substring3, null);
            this.lruEntries.put(substring3, entry);
        }
        if (indexOf2 != -1 && indexOf == 5 && line.startsWith(CLEAN)) {
            String[] split = line.substring(indexOf2 + 1).split(" ");
            entry.readable = true;
            entry.currentEditor = null;
            entry.setLengths(split);
        } else if (indexOf2 == -1 && indexOf == 5 && line.startsWith(DIRTY)) {
            entry.currentEditor = new Editor(this, entry, null);
        } else if (indexOf2 != -1 || indexOf != 4 || !line.startsWith(READ)) {
            throw new IOException("unexpected journal line: " + line);
        }
    }

    private void processJournal() throws IOException {
        deleteIfExists(this.journalFileTmp);
        Iterator it = this.lruEntries.values().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            int i;
            if (entry.currentEditor == null) {
                for (i = 0; i < this.valueCount; i++) {
                    this.size += entry.lengths[i];
                }
            } else {
                entry.currentEditor = null;
                for (i = 0; i < this.valueCount; i++) {
                    deleteIfExists(entry.getCleanFile(i));
                    deleteIfExists(entry.getDirtyFile(i));
                }
                it.remove();
            }
        }
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void rebuildJournal() throws IOException {
        if (this.journalWriter != null) {
            this.journalWriter.close();
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.journalFileTmp), DiskLruCacheUtil.US_ASCII));
        try {
            bufferedWriter.write(MAGIC);
            bufferedWriter.write(MASTNativeAdConstants.NEWLINE);
            bufferedWriter.write("1");
            bufferedWriter.write(MASTNativeAdConstants.NEWLINE);
            bufferedWriter.write(Integer.toString(this.appVersion));
            bufferedWriter.write(MASTNativeAdConstants.NEWLINE);
            bufferedWriter.write(Integer.toString(this.valueCount));
            bufferedWriter.write(MASTNativeAdConstants.NEWLINE);
            bufferedWriter.write(MASTNativeAdConstants.NEWLINE);
            for (Entry entry : this.lruEntries.values()) {
                if (entry.currentEditor != null) {
                    bufferedWriter.write("DIRTY " + entry.key + 10);
                } else {
                    bufferedWriter.write("CLEAN " + entry.key + entry.getLengths() + 10);
                }
            }
            bufferedWriter.close();
            if (this.journalFile.exists()) {
                renameTo(this.journalFile, this.journalFileBackup, true);
            }
            renameTo(this.journalFileTmp, this.journalFile, false);
            this.journalFileBackup.delete();
            this.journalWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.journalFile, true), DiskLruCacheUtil.US_ASCII));
        } catch (Throwable th) {
            bufferedWriter.close();
        }
    }

    private static void deleteIfExists(File file) throws IOException {
        if (file.exists() && !file.delete()) {
            throw new IOException();
        }
    }

    private static void renameTo(File from, File to, boolean deleteDestination) throws IOException {
        if (deleteDestination) {
            deleteIfExists(to);
        }
        if (!from.renameTo(to)) {
            throw new IOException();
        }
    }

    public final synchronized Snapshot get(String key) throws IOException {
        Snapshot snapshot;
        checkNotClosed();
        validateKey(key);
        Entry entry = (Entry) this.lruEntries.get(key);
        if (entry == null) {
            snapshot = null;
        } else if (entry.readable) {
            InputStream[] inputStreamArr = new InputStream[this.valueCount];
            int i = 0;
            while (i < this.valueCount) {
                try {
                    inputStreamArr[i] = new FileInputStream(entry.getCleanFile(i));
                    i++;
                } catch (FileNotFoundException e) {
                    i = 0;
                    while (i < this.valueCount && inputStreamArr[i] != null) {
                        DiskLruCacheUtil.closeQuietly(inputStreamArr[i]);
                        i++;
                    }
                    snapshot = null;
                }
            }
            this.redundantOpCount++;
            this.journalWriter.append("READ " + key + 10);
            if (journalRebuildRequired()) {
                this.executorService.submit(this.cleanupCallable);
            }
            snapshot = new Snapshot(this, key, entry.sequenceNumber, inputStreamArr, entry.lengths, null);
        } else {
            snapshot = null;
        }
        return snapshot;
    }

    public final Editor edit(String key) throws IOException {
        return edit(key, ANY_SEQUENCE_NUMBER);
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized Editor edit(String key, long expectedSequenceNumber) throws IOException {
        Editor editor;
        checkNotClosed();
        validateKey(key);
        Entry entry = (Entry) this.lruEntries.get(key);
        if (expectedSequenceNumber == ANY_SEQUENCE_NUMBER || (entry != null && entry.sequenceNumber == expectedSequenceNumber)) {
            Entry entry2;
            if (entry == null) {
                entry = new Entry(this, key, null);
                this.lruEntries.put(key, entry);
                entry2 = entry;
            } else if (entry.currentEditor != null) {
                editor = null;
            } else {
                entry2 = entry;
            }
            editor = new Editor(this, entry2, null);
            entry2.currentEditor = editor;
            this.journalWriter.write("DIRTY " + key + 10);
            this.journalWriter.flush();
        } else {
            editor = null;
        }
        return editor;
    }

    public final File getDirectory() {
        return this.directory;
    }

    public final synchronized long getMaxSize() {
        return this.maxSize;
    }

    public final synchronized void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
        this.executorService.submit(this.cleanupCallable);
    }

    public final synchronized long size() {
        return this.size;
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void completeEdit(Editor editor, boolean success) throws IOException {
        int i = 0;
        synchronized (this) {
            Entry access$1400 = editor.entry;
            if (access$1400.currentEditor != editor) {
                throw new IllegalStateException();
            }
            if (success) {
                if (!access$1400.readable) {
                    int i2 = 0;
                    while (i2 < this.valueCount) {
                        if (!editor.written[i2]) {
                            editor.abort();
                            throw new IllegalStateException("Newly created entry didn't create value for index " + i2);
                        } else if (!access$1400.getDirtyFile(i2).exists()) {
                            editor.abort();
                            break;
                        } else {
                            i2++;
                        }
                    }
                }
            }
            while (i < this.valueCount) {
                File dirtyFile = access$1400.getDirtyFile(i);
                if (!success) {
                    deleteIfExists(dirtyFile);
                } else if (dirtyFile.exists()) {
                    File cleanFile = access$1400.getCleanFile(i);
                    dirtyFile.renameTo(cleanFile);
                    long j = access$1400.lengths[i];
                    long length = cleanFile.length();
                    access$1400.lengths[i] = length;
                    this.size = (this.size - j) + length;
                }
                i++;
            }
            this.redundantOpCount++;
            access$1400.currentEditor = null;
            if ((access$1400.readable | success) != 0) {
                access$1400.readable = true;
                this.journalWriter.write("CLEAN " + access$1400.key + access$1400.getLengths() + 10);
                if (success) {
                    long j2 = this.nextSequenceNumber;
                    this.nextSequenceNumber = 1 + j2;
                    access$1400.sequenceNumber = j2;
                }
            } else {
                this.lruEntries.remove(access$1400.key);
                this.journalWriter.write("REMOVE " + access$1400.key + 10);
            }
            this.journalWriter.flush();
            if (this.size > this.maxSize || journalRebuildRequired()) {
                this.executorService.submit(this.cleanupCallable);
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean journalRebuildRequired() {
        return this.redundantOpCount >= 2000 && this.redundantOpCount >= this.lruEntries.size();
    }

    public final synchronized boolean remove(String key) throws IOException {
        boolean z;
        int i = 0;
        synchronized (this) {
            checkNotClosed();
            validateKey(key);
            Entry entry = (Entry) this.lruEntries.get(key);
            if (entry == null || entry.currentEditor != null) {
                z = false;
            } else {
                while (i < this.valueCount) {
                    File cleanFile = entry.getCleanFile(i);
                    if (!cleanFile.exists() || cleanFile.delete()) {
                        this.size -= entry.lengths[i];
                        entry.lengths[i] = 0;
                        i++;
                    } else {
                        throw new IOException("failed to delete " + cleanFile);
                    }
                }
                this.redundantOpCount++;
                this.journalWriter.append("REMOVE " + key + 10);
                this.lruEntries.remove(key);
                if (journalRebuildRequired()) {
                    this.executorService.submit(this.cleanupCallable);
                }
                z = true;
            }
        }
        return z;
    }

    public final synchronized boolean isClosed() {
        return this.journalWriter == null;
    }

    private void checkNotClosed() {
        if (this.journalWriter == null) {
            throw new IllegalStateException("cache is closed");
        }
    }

    public final synchronized void flush() throws IOException {
        checkNotClosed();
        trimToSize();
        this.journalWriter.flush();
    }

    public final synchronized void close() throws IOException {
        if (this.journalWriter != null) {
            Iterator it = new ArrayList(this.lruEntries.values()).iterator();
            while (it.hasNext()) {
                Entry entry = (Entry) it.next();
                if (entry.currentEditor != null) {
                    entry.currentEditor.abort();
                }
            }
            trimToSize();
            this.journalWriter.close();
            this.journalWriter = null;
        }
    }

    /* access modifiers changed from: private */
    public void trimToSize() throws IOException {
        while (this.size > this.maxSize) {
            remove((String) ((java.util.Map.Entry) this.lruEntries.entrySet().iterator().next()).getKey());
        }
    }

    public final void delete() throws IOException {
        close();
        DiskLruCacheUtil.deleteContents(this.directory);
    }

    private void validateKey(String key) {
        if (!LEGAL_KEY_PATTERN.matcher(key).matches()) {
            throw new IllegalArgumentException("keys must match regex [a-z0-9_-]{1,64}: \"" + key + "\"");
        }
    }

    /* access modifiers changed from: private|static */
    public static String inputStreamToString(InputStream in) throws IOException {
        return DiskLruCacheUtil.readFully(new InputStreamReader(in, DiskLruCacheUtil.UTF_8));
    }
}
