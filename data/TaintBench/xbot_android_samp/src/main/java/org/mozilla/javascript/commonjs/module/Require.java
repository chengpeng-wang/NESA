package org.mozilla.javascript.commonjs.module;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class Require extends BaseFunction {
    private static final ThreadLocal<Map<String, Scriptable>> loadingModuleInterfaces = new ThreadLocal();
    private static final long serialVersionUID = 1;
    private final Map<String, Scriptable> exportedModuleInterfaces = new ConcurrentHashMap();
    private final Object loadLock = new Object();
    private Scriptable mainExports;
    private String mainModuleId = null;
    private final ModuleScriptProvider moduleScriptProvider;
    private final Scriptable nativeScope;
    private final Scriptable paths;
    private final Script postExec;
    private final Script preExec;
    private final boolean sandboxed;

    public Require(Context cx, Scriptable nativeScope, ModuleScriptProvider moduleScriptProvider, Script preExec, Script postExec, boolean sandboxed) {
        this.moduleScriptProvider = moduleScriptProvider;
        this.nativeScope = nativeScope;
        this.sandboxed = sandboxed;
        this.preExec = preExec;
        this.postExec = postExec;
        setPrototype(ScriptableObject.getFunctionPrototype(nativeScope));
        if (sandboxed) {
            this.paths = null;
            return;
        }
        this.paths = cx.newArray(nativeScope, 0);
        defineReadOnlyProperty(this, "paths", this.paths);
    }

    public Scriptable requireMain(Context cx, String mainModuleId) {
        if (this.mainModuleId == null) {
            try {
                if (this.moduleScriptProvider.getModuleScript(cx, mainModuleId, null, null, this.paths) != null) {
                    this.mainExports = getExportedModuleInterface(cx, mainModuleId, null, null, true);
                } else if (!this.sandboxed) {
                    URI mainUri = null;
                    try {
                        mainUri = new URI(mainModuleId);
                    } catch (URISyntaxException e) {
                    }
                    if (mainUri == null || !mainUri.isAbsolute()) {
                        File file = new File(mainModuleId);
                        if (file.isFile()) {
                            mainUri = file.toURI();
                        } else {
                            throw ScriptRuntime.throwError(cx, this.nativeScope, "Module \"" + mainModuleId + "\" not found.");
                        }
                    }
                    this.mainExports = getExportedModuleInterface(cx, mainUri.toString(), mainUri, null, true);
                }
                this.mainModuleId = mainModuleId;
                return this.mainExports;
            } catch (RuntimeException x) {
                throw x;
            } catch (Exception x2) {
                throw new RuntimeException(x2);
            }
        } else if (this.mainModuleId.equals(mainModuleId)) {
            return this.mainExports;
        } else {
            throw new IllegalStateException("Main module already set to " + this.mainModuleId);
        }
    }

    public void install(Scriptable scope) {
        ScriptableObject.putProperty(scope, "require", (Object) this);
    }

    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (args == null || args.length < 1) {
            throw ScriptRuntime.throwError(cx, scope, "require() needs one argument");
        }
        String id = (String) Context.jsToJava(args[0], String.class);
        URI uri = null;
        URI base = null;
        if (id.startsWith("./") || id.startsWith("../")) {
            if (thisObj instanceof ModuleScope) {
                ModuleScope moduleScope = (ModuleScope) thisObj;
                base = moduleScope.getBase();
                URI current = moduleScope.getUri();
                uri = current.resolve(id);
                if (base == null) {
                    id = uri.toString();
                } else {
                    id = base.relativize(current).resolve(id).toString();
                    if (id.charAt(0) == '.') {
                        if (this.sandboxed) {
                            throw ScriptRuntime.throwError(cx, scope, "Module \"" + id + "\" is not contained in sandbox.");
                        }
                        id = uri.toString();
                    }
                }
            } else {
                throw ScriptRuntime.throwError(cx, scope, "Can't resolve relative module ID \"" + id + "\" when require() is used outside of a module");
            }
        }
        return getExportedModuleInterface(cx, id, uri, base, false);
    }

    public Scriptable construct(Context cx, Scriptable scope, Object[] args) {
        throw ScriptRuntime.throwError(cx, scope, "require() can not be invoked as a constructor");
    }

    /* JADX WARNING: Missing block: B:64:?, code skipped:
            return r4;
     */
    private org.mozilla.javascript.Scriptable getExportedModuleInterface(org.mozilla.javascript.Context r15, java.lang.String r16, java.net.URI r17, java.net.URI r18, boolean r19) {
        /*
        r14 = this;
        r1 = r14.exportedModuleInterfaces;
        r0 = r16;
        r4 = r1.get(r0);
        r4 = (org.mozilla.javascript.Scriptable) r4;
        if (r4 == 0) goto L_0x0018;
    L_0x000c:
        if (r19 == 0) goto L_0x0016;
    L_0x000e:
        r1 = new java.lang.IllegalStateException;
        r2 = "Attempt to set main module after it was loaded";
        r1.<init>(r2);
        throw r1;
    L_0x0016:
        r8 = r4;
    L_0x0017:
        return r8;
    L_0x0018:
        r1 = loadingModuleInterfaces;
        r11 = r1.get();
        r11 = (java.util.Map) r11;
        if (r11 == 0) goto L_0x002e;
    L_0x0022:
        r0 = r16;
        r4 = r11.get(r0);
        r4 = (org.mozilla.javascript.Scriptable) r4;
        if (r4 == 0) goto L_0x002e;
    L_0x002c:
        r8 = r4;
        goto L_0x0017;
    L_0x002e:
        r13 = r14.loadLock;
        monitor-enter(r13);
        r1 = r14.exportedModuleInterfaces;	 Catch:{ all -> 0x0072 }
        r0 = r16;
        r1 = r1.get(r0);	 Catch:{ all -> 0x0072 }
        r0 = r1;
        r0 = (org.mozilla.javascript.Scriptable) r0;	 Catch:{ all -> 0x0072 }
        r4 = r0;
        if (r4 == 0) goto L_0x0042;
    L_0x003f:
        monitor-exit(r13);	 Catch:{ all -> 0x0072 }
        r8 = r4;
        goto L_0x0017;
    L_0x0042:
        r5 = r14.getModule(r15, r16, r17, r18);	 Catch:{ all -> 0x0072 }
        r1 = r14.sandboxed;	 Catch:{ all -> 0x0072 }
        if (r1 == 0) goto L_0x0075;
    L_0x004a:
        r1 = r5.isSandboxed();	 Catch:{ all -> 0x0072 }
        if (r1 != 0) goto L_0x0075;
    L_0x0050:
        r1 = r14.nativeScope;	 Catch:{ all -> 0x0072 }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0072 }
        r2.<init>();	 Catch:{ all -> 0x0072 }
        r3 = "Module \"";
        r2 = r2.append(r3);	 Catch:{ all -> 0x0072 }
        r0 = r16;
        r2 = r2.append(r0);	 Catch:{ all -> 0x0072 }
        r3 = "\" is not contained in sandbox.";
        r2 = r2.append(r3);	 Catch:{ all -> 0x0072 }
        r2 = r2.toString();	 Catch:{ all -> 0x0072 }
        r1 = org.mozilla.javascript.ScriptRuntime.throwError(r15, r1, r2);	 Catch:{ all -> 0x0072 }
        throw r1;	 Catch:{ all -> 0x0072 }
    L_0x0072:
        r1 = move-exception;
    L_0x0073:
        monitor-exit(r13);	 Catch:{ all -> 0x0072 }
        throw r1;
    L_0x0075:
        r1 = r14.nativeScope;	 Catch:{ all -> 0x0072 }
        r4 = r15.newObject(r1);	 Catch:{ all -> 0x0072 }
        if (r11 != 0) goto L_0x00b3;
    L_0x007d:
        r10 = 1;
    L_0x007e:
        if (r10 == 0) goto L_0x008b;
    L_0x0080:
        r12 = new java.util.HashMap;	 Catch:{ all -> 0x0072 }
        r12.<init>();	 Catch:{ all -> 0x0072 }
        r1 = loadingModuleInterfaces;	 Catch:{ all -> 0x00cb }
        r1.set(r12);	 Catch:{ all -> 0x00cb }
        r11 = r12;
    L_0x008b:
        r0 = r16;
        r11.put(r0, r4);	 Catch:{ all -> 0x0072 }
        r1 = r14;
        r2 = r15;
        r3 = r16;
        r6 = r19;
        r9 = r1.executeModuleScript(r2, r3, r4, r5, r6);	 Catch:{ RuntimeException -> 0x00b5 }
        if (r4 == r9) goto L_0x00a2;
    L_0x009c:
        r0 = r16;
        r11.put(r0, r9);	 Catch:{ RuntimeException -> 0x00b5 }
        r4 = r9;
    L_0x00a2:
        if (r10 == 0) goto L_0x00af;
    L_0x00a4:
        r1 = r14.exportedModuleInterfaces;	 Catch:{ all -> 0x0072 }
        r1.putAll(r11);	 Catch:{ all -> 0x0072 }
        r1 = loadingModuleInterfaces;	 Catch:{ all -> 0x0072 }
        r2 = 0;
        r1.set(r2);	 Catch:{ all -> 0x0072 }
    L_0x00af:
        monitor-exit(r13);	 Catch:{ all -> 0x0072 }
        r8 = r4;
        goto L_0x0017;
    L_0x00b3:
        r10 = 0;
        goto L_0x007e;
    L_0x00b5:
        r7 = move-exception;
        r0 = r16;
        r11.remove(r0);	 Catch:{ all -> 0x00bc }
        throw r7;	 Catch:{ all -> 0x00bc }
    L_0x00bc:
        r1 = move-exception;
        if (r10 == 0) goto L_0x00ca;
    L_0x00bf:
        r2 = r14.exportedModuleInterfaces;	 Catch:{ all -> 0x0072 }
        r2.putAll(r11);	 Catch:{ all -> 0x0072 }
        r2 = loadingModuleInterfaces;	 Catch:{ all -> 0x0072 }
        r3 = 0;
        r2.set(r3);	 Catch:{ all -> 0x0072 }
    L_0x00ca:
        throw r1;	 Catch:{ all -> 0x0072 }
    L_0x00cb:
        r1 = move-exception;
        r11 = r12;
        goto L_0x0073;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.javascript.commonjs.module.Require.getExportedModuleInterface(org.mozilla.javascript.Context, java.lang.String, java.net.URI, java.net.URI, boolean):org.mozilla.javascript.Scriptable");
    }

    private Scriptable executeModuleScript(Context cx, String id, Scriptable exports, ModuleScript moduleScript, boolean isMain) {
        Scriptable moduleObject = (ScriptableObject) cx.newObject(this.nativeScope);
        URI uri = moduleScript.getUri();
        URI base = moduleScript.getBase();
        defineReadOnlyProperty(moduleObject, "id", id);
        if (!this.sandboxed) {
            defineReadOnlyProperty(moduleObject, "uri", uri.toString());
        }
        Scriptable executionScope = new ModuleScope(this.nativeScope, uri, base);
        executionScope.put("exports", executionScope, (Object) exports);
        executionScope.put("module", executionScope, (Object) moduleObject);
        moduleObject.put("exports", moduleObject, (Object) exports);
        install(executionScope);
        if (isMain) {
            defineReadOnlyProperty(this, "main", moduleObject);
        }
        executeOptionalScript(this.preExec, cx, executionScope);
        moduleScript.getScript().exec(cx, executionScope);
        executeOptionalScript(this.postExec, cx, executionScope);
        return ScriptRuntime.toObject(cx, this.nativeScope, ScriptableObject.getProperty(moduleObject, "exports"));
    }

    private static void executeOptionalScript(Script script, Context cx, Scriptable executionScope) {
        if (script != null) {
            script.exec(cx, executionScope);
        }
    }

    private static void defineReadOnlyProperty(ScriptableObject obj, String name, Object value) {
        ScriptableObject.putProperty((Scriptable) obj, name, value);
        obj.setAttributes(name, 5);
    }

    private ModuleScript getModule(Context cx, String id, URI uri, URI base) {
        try {
            ModuleScript moduleScript = this.moduleScriptProvider.getModuleScript(cx, id, uri, base, this.paths);
            if (moduleScript != null) {
                return moduleScript;
            }
            throw ScriptRuntime.throwError(cx, this.nativeScope, "Module \"" + id + "\" not found.");
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e2) {
            throw Context.throwAsScriptRuntimeEx(e2);
        }
    }

    public String getFunctionName() {
        return "require";
    }

    public int getArity() {
        return 1;
    }

    public int getLength() {
        return 1;
    }
}
