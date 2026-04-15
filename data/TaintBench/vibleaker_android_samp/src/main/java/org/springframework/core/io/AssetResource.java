package org.springframework.core.io;

import android.content.res.AssetManager;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.util.Assert;

public class AssetResource extends AbstractResource {
    private final AssetManager assetManager;
    private final String fileName;

    public AssetResource(AssetManager assetManager, String fileName) {
        Assert.notNull(assetManager, "assetManager must not be null");
        Assert.notNull(fileName, "fileName must not be null");
        this.assetManager = assetManager;
        this.fileName = fileName;
    }

    public boolean exists() {
        try {
            if (this.assetManager.open(this.fileName) != null) {
                return true;
            }
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    public long contentLength() throws IOException {
        return this.assetManager.openFd(this.fileName).getLength();
    }

    public String getDescription() {
        return "asset [" + this.fileName + "]";
    }

    public InputStream getInputStream() throws IOException {
        InputStream inputStream = this.assetManager.open(this.fileName);
        if (inputStream != null) {
            return inputStream;
        }
        throw new FileNotFoundException(getDescription() + " cannot be opened because it does not exist");
    }

    public boolean equals(Object obj) {
        return obj == this || ((obj instanceof AssetResource) && this.fileName.equals(((AssetResource) obj).fileName));
    }

    public int hashCode() {
        return this.fileName.hashCode();
    }
}
