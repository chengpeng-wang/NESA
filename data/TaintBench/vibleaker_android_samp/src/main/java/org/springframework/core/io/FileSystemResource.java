package org.springframework.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class FileSystemResource extends AbstractResource implements WritableResource {
    private final File file;
    private final String path;

    public FileSystemResource(File file) {
        Assert.notNull(file, "File must not be null");
        this.file = file;
        this.path = StringUtils.cleanPath(file.getPath());
    }

    public FileSystemResource(String path) {
        Assert.notNull(path, "Path must not be null");
        this.file = new File(path);
        this.path = StringUtils.cleanPath(path);
    }

    public final String getPath() {
        return this.path;
    }

    public boolean exists() {
        return this.file.exists();
    }

    public boolean isReadable() {
        return this.file.canRead() && !this.file.isDirectory();
    }

    public InputStream getInputStream() throws IOException {
        return new FileInputStream(this.file);
    }

    public URL getURL() throws IOException {
        return this.file.toURI().toURL();
    }

    public URI getURI() throws IOException {
        return this.file.toURI();
    }

    public File getFile() {
        return this.file;
    }

    public long contentLength() throws IOException {
        return this.file.length();
    }

    public Resource createRelative(String relativePath) {
        return new FileSystemResource(StringUtils.applyRelativePath(this.path, relativePath));
    }

    public String getFilename() {
        return this.file.getName();
    }

    public String getDescription() {
        return "file [" + this.file.getAbsolutePath() + "]";
    }

    public boolean isWritable() {
        return this.file.canWrite() && !this.file.isDirectory();
    }

    public OutputStream getOutputStream() throws IOException {
        return new FileOutputStream(this.file);
    }

    public boolean equals(Object obj) {
        return obj == this || ((obj instanceof FileSystemResource) && this.path.equals(((FileSystemResource) obj).path));
    }

    public int hashCode() {
        return this.path.hashCode();
    }
}
