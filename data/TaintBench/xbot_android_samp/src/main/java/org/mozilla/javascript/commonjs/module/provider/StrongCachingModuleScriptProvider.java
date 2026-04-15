package org.mozilla.javascript.commonjs.module.provider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.mozilla.javascript.commonjs.module.ModuleScript;
import org.mozilla.javascript.commonjs.module.provider.CachingModuleScriptProviderBase.CachedModuleScript;

public class StrongCachingModuleScriptProvider extends CachingModuleScriptProviderBase {
    private static final long serialVersionUID = 1;
    private final Map<String, CachedModuleScript> modules = new ConcurrentHashMap(16, 0.75f, CachingModuleScriptProviderBase.getConcurrencyLevel());

    public StrongCachingModuleScriptProvider(ModuleSourceProvider moduleSourceProvider) {
        super(moduleSourceProvider);
    }

    /* access modifiers changed from: protected */
    public CachedModuleScript getLoadedModule(String moduleId) {
        return (CachedModuleScript) this.modules.get(moduleId);
    }

    /* access modifiers changed from: protected */
    public void putLoadedModule(String moduleId, ModuleScript moduleScript, Object validator) {
        this.modules.put(moduleId, new CachedModuleScript(moduleScript, validator));
    }
}
