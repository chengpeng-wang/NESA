package org.mozilla.javascript.commonjs.module.provider;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public abstract class ModuleSourceProviderBase implements ModuleSourceProvider, Serializable {
    private static final long serialVersionUID = 1;

    public abstract ModuleSource loadFromUri(URI uri, URI uri2, Object obj) throws IOException, URISyntaxException;

    public ModuleSource loadSource(String moduleId, Scriptable paths, Object validator) throws IOException, URISyntaxException {
        if (!entityNeedsRevalidation(validator)) {
            return NOT_MODIFIED;
        }
        ModuleSource moduleSource = loadFromPrivilegedLocations(moduleId, validator);
        if (moduleSource != null) {
            return moduleSource;
        }
        if (paths != null) {
            moduleSource = loadFromPathArray(moduleId, paths, validator);
            if (moduleSource != null) {
                return moduleSource;
            }
        }
        return loadFromFallbackLocations(moduleId, validator);
    }

    public ModuleSource loadSource(URI uri, URI base, Object validator) throws IOException, URISyntaxException {
        return loadFromUri(uri, base, validator);
    }

    private ModuleSource loadFromPathArray(String moduleId, Scriptable paths, Object validator) throws IOException {
        long llength = ScriptRuntime.toUint32(ScriptableObject.getProperty(paths, "length"));
        int ilength = llength > 2147483647L ? Integer.MAX_VALUE : (int) llength;
        int i = 0;
        while (i < ilength) {
            String path = ensureTrailingSlash((String) ScriptableObject.getTypedProperty(paths, i, String.class));
            try {
                URI uri = new URI(path);
                if (!uri.isAbsolute()) {
                    uri = new File(path).toURI().resolve("");
                }
                ModuleSource moduleSource = loadFromUri(uri.resolve(moduleId), uri, validator);
                if (moduleSource != null) {
                    return moduleSource;
                }
                i++;
            } catch (URISyntaxException e) {
                throw new MalformedURLException(e.getMessage());
            }
        }
        return null;
    }

    private static String ensureTrailingSlash(String path) {
        return path.endsWith("/") ? path : path.concat("/");
    }

    /* access modifiers changed from: protected */
    public boolean entityNeedsRevalidation(Object validator) {
        return true;
    }

    /* access modifiers changed from: protected */
    public ModuleSource loadFromPrivilegedLocations(String moduleId, Object validator) throws IOException, URISyntaxException {
        return null;
    }

    /* access modifiers changed from: protected */
    public ModuleSource loadFromFallbackLocations(String moduleId, Object validator) throws IOException, URISyntaxException {
        return null;
    }
}
