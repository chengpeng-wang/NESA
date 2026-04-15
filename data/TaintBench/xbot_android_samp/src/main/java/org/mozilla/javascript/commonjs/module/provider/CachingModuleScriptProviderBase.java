package org.mozilla.javascript.commonjs.module.provider;

import java.io.Reader;
import java.io.Serializable;
import java.net.URI;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.commonjs.module.ModuleScript;
import org.mozilla.javascript.commonjs.module.ModuleScriptProvider;

public abstract class CachingModuleScriptProviderBase implements ModuleScriptProvider, Serializable {
    private static final int loadConcurrencyLevel = (Runtime.getRuntime().availableProcessors() * 8);
    private static final int loadLockCount;
    private static final int loadLockMask;
    private static final int loadLockShift;
    private static final long serialVersionUID = 1;
    private final Object[] loadLocks = new Object[loadLockCount];
    private final ModuleSourceProvider moduleSourceProvider;

    public static class CachedModuleScript {
        private final ModuleScript moduleScript;
        private final Object validator;

        public CachedModuleScript(ModuleScript moduleScript, Object validator) {
            this.moduleScript = moduleScript;
            this.validator = validator;
        }

        /* access modifiers changed from: 0000 */
        public ModuleScript getModule() {
            return this.moduleScript;
        }

        /* access modifiers changed from: 0000 */
        public Object getValidator() {
            return this.validator;
        }
    }

    public abstract CachedModuleScript getLoadedModule(String str);

    public abstract void putLoadedModule(String str, ModuleScript moduleScript, Object obj);

    static {
        int sshift = 0;
        int ssize = 1;
        while (ssize < loadConcurrencyLevel) {
            sshift++;
            ssize <<= 1;
        }
        loadLockShift = 32 - sshift;
        loadLockMask = ssize - 1;
        loadLockCount = ssize;
    }

    protected CachingModuleScriptProviderBase(ModuleSourceProvider moduleSourceProvider) {
        for (int i = 0; i < this.loadLocks.length; i++) {
            this.loadLocks[i] = new Object();
        }
        this.moduleSourceProvider = moduleSourceProvider;
    }

    public ModuleScript getModuleScript(Context cx, String moduleId, URI moduleUri, URI baseUri, Scriptable paths) throws Exception {
        ModuleSource moduleSource;
        ModuleScript moduleScript;
        CachedModuleScript cachedModule1 = getLoadedModule(moduleId);
        Object validator1 = getValidator(cachedModule1);
        if (moduleUri == null) {
            moduleSource = this.moduleSourceProvider.loadSource(moduleId, paths, validator1);
        } else {
            moduleSource = this.moduleSourceProvider.loadSource(moduleUri, baseUri, validator1);
        }
        if (moduleSource == ModuleSourceProvider.NOT_MODIFIED) {
            return cachedModule1.getModule();
        }
        if (moduleSource == null) {
            return null;
        }
        Reader reader = moduleSource.getReader();
        try {
            synchronized (this.loadLocks[(moduleId.hashCode() >>> loadLockShift) & loadLockMask]) {
                CachedModuleScript cachedModule2 = getLoadedModule(moduleId);
                if (cachedModule2 == null || equal(validator1, getValidator(cachedModule2))) {
                    URI sourceUri = moduleSource.getUri();
                    moduleScript = new ModuleScript(cx.compileReader(reader, sourceUri.toString(), 1, moduleSource.getSecurityDomain()), sourceUri, moduleSource.getBase());
                    putLoadedModule(moduleId, moduleScript, moduleSource.getValidator());
                    reader.close();
                    return moduleScript;
                }
                moduleScript = cachedModule2.getModule();
            }
        } finally {
            reader.close();
        }
        return moduleScript;
    }

    private static Object getValidator(CachedModuleScript cachedModule) {
        return cachedModule == null ? null : cachedModule.getValidator();
    }

    private static boolean equal(Object o1, Object o2) {
        if (o1 == null) {
            return o2 == null;
        } else {
            return o1.equals(o2);
        }
    }

    protected static int getConcurrencyLevel() {
        return loadLockCount;
    }
}
