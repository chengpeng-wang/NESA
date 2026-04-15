package org.mozilla.javascript.commonjs.module.provider;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.commonjs.module.ModuleScript;
import org.mozilla.javascript.commonjs.module.provider.CachingModuleScriptProviderBase.CachedModuleScript;

public class SoftCachingModuleScriptProvider extends CachingModuleScriptProviderBase {
    private static final long serialVersionUID = 1;
    private transient ReferenceQueue<Script> scriptRefQueue = new ReferenceQueue();
    private transient ConcurrentMap<String, ScriptReference> scripts = new ConcurrentHashMap(16, 0.75f, CachingModuleScriptProviderBase.getConcurrencyLevel());

    private static class ScriptReference extends SoftReference<Script> {
        private final URI base;
        private final String moduleId;
        private final URI uri;
        private final Object validator;

        ScriptReference(Script script, String moduleId, URI uri, URI base, Object validator, ReferenceQueue<Script> refQueue) {
            super(script, refQueue);
            this.moduleId = moduleId;
            this.uri = uri;
            this.base = base;
            this.validator = validator;
        }

        /* access modifiers changed from: 0000 */
        public CachedModuleScript getCachedModuleScript() {
            Script script = (Script) get();
            if (script == null) {
                return null;
            }
            return new CachedModuleScript(new ModuleScript(script, this.uri, this.base), this.validator);
        }

        /* access modifiers changed from: 0000 */
        public String getModuleId() {
            return this.moduleId;
        }
    }

    public SoftCachingModuleScriptProvider(ModuleSourceProvider moduleSourceProvider) {
        super(moduleSourceProvider);
    }

    public ModuleScript getModuleScript(Context cx, String moduleId, URI uri, URI base, Scriptable paths) throws Exception {
        while (true) {
            ScriptReference ref = (ScriptReference) this.scriptRefQueue.poll();
            if (ref == null) {
                return super.getModuleScript(cx, moduleId, uri, base, paths);
            }
            this.scripts.remove(ref.getModuleId(), ref);
        }
    }

    /* access modifiers changed from: protected */
    public CachedModuleScript getLoadedModule(String moduleId) {
        ScriptReference scriptRef = (ScriptReference) this.scripts.get(moduleId);
        return scriptRef != null ? scriptRef.getCachedModuleScript() : null;
    }

    /* access modifiers changed from: protected */
    public void putLoadedModule(String moduleId, ModuleScript moduleScript, Object validator) {
        this.scripts.put(moduleId, new ScriptReference(moduleScript.getScript(), moduleId, moduleScript.getUri(), moduleScript.getBase(), validator, this.scriptRefQueue));
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.scriptRefQueue = new ReferenceQueue();
        this.scripts = new ConcurrentHashMap();
        for (Entry<String, CachedModuleScript> entry : ((Map) in.readObject()).entrySet()) {
            CachedModuleScript cachedModuleScript = (CachedModuleScript) entry.getValue();
            putLoadedModule((String) entry.getKey(), cachedModuleScript.getModule(), cachedModuleScript.getValidator());
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        Map<String, CachedModuleScript> serScripts = new HashMap();
        for (Entry<String, ScriptReference> entry : this.scripts.entrySet()) {
            CachedModuleScript cachedModuleScript = ((ScriptReference) entry.getValue()).getCachedModuleScript();
            if (cachedModuleScript != null) {
                serScripts.put(entry.getKey(), cachedModuleScript);
            }
        }
        out.writeObject(serScripts);
    }
}
