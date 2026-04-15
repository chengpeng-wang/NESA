package org.springframework.core.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

public class ClassPathResource extends AbstractFileResolvingResource {
    private ClassLoader classLoader;
    private Class<?> clazz;
    private final String path;

    public ClassPathResource(String path) {
        this(path, (ClassLoader) null);
    }

    public ClassPathResource(String path, ClassLoader classLoader) {
        Assert.notNull(path, "Path must not be null");
        String pathToUse = StringUtils.cleanPath(path);
        if (pathToUse.startsWith("/")) {
            pathToUse = pathToUse.substring(1);
        }
        this.path = pathToUse;
        if (classLoader == null) {
            classLoader = ClassUtils.getDefaultClassLoader();
        }
        this.classLoader = classLoader;
    }

    public ClassPathResource(String path, Class<?> clazz) {
        Assert.notNull(path, "Path must not be null");
        this.path = StringUtils.cleanPath(path);
        this.clazz = clazz;
    }

    protected ClassPathResource(String path, ClassLoader classLoader, Class<?> clazz) {
        this.path = StringUtils.cleanPath(path);
        this.classLoader = classLoader;
        this.clazz = clazz;
    }

    public final String getPath() {
        return this.path;
    }

    public final ClassLoader getClassLoader() {
        return this.clazz != null ? this.clazz.getClassLoader() : this.classLoader;
    }

    public boolean exists() {
        return resolveURL() != null;
    }

    /* access modifiers changed from: protected */
    public URL resolveURL() {
        if (this.clazz != null) {
            return this.clazz.getResource(this.path);
        }
        if (this.classLoader != null) {
            return this.classLoader.getResource(this.path);
        }
        return ClassLoader.getSystemResource(this.path);
    }

    public InputStream getInputStream() throws IOException {
        InputStream is;
        if (this.clazz != null) {
            is = this.clazz.getResourceAsStream(this.path);
        } else if (this.classLoader != null) {
            is = this.classLoader.getResourceAsStream(this.path);
        } else {
            is = ClassLoader.getSystemResourceAsStream(this.path);
        }
        if (is != null) {
            return is;
        }
        throw new FileNotFoundException(getDescription() + " cannot be opened because it does not exist");
    }

    public URL getURL() throws IOException {
        URL url = resolveURL();
        if (url != null) {
            return url;
        }
        throw new FileNotFoundException(getDescription() + " cannot be resolved to URL because it does not exist");
    }

    public Resource createRelative(String relativePath) {
        return new ClassPathResource(StringUtils.applyRelativePath(this.path, relativePath), this.classLoader, this.clazz);
    }

    public String getFilename() {
        return StringUtils.getFilename(this.path);
    }

    public String getDescription() {
        StringBuilder builder = new StringBuilder("class path resource [");
        String pathToUse = this.path;
        if (!(this.clazz == null || pathToUse.startsWith("/"))) {
            builder.append(ClassUtils.classPackageAsResourcePath(this.clazz));
            builder.append('/');
        }
        if (pathToUse.startsWith("/")) {
            pathToUse = pathToUse.substring(1);
        }
        builder.append(pathToUse);
        builder.append(']');
        return builder.toString();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ClassPathResource)) {
            return false;
        }
        ClassPathResource otherRes = (ClassPathResource) obj;
        if (this.path.equals(otherRes.path) && ObjectUtils.nullSafeEquals(this.classLoader, otherRes.classLoader) && ObjectUtils.nullSafeEquals(this.clazz, otherRes.clazz)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.path.hashCode();
    }
}
