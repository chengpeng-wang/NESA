package org.mozilla.javascript;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashSet;
import java.util.Set;

public class NativeJavaPackage extends ScriptableObject {
    static final long serialVersionUID = 7445054382212031523L;
    private transient ClassLoader classLoader;
    private Set<String> negativeCache;
    private String packageName;

    NativeJavaPackage(boolean internalUsage, String packageName, ClassLoader classLoader) {
        this.negativeCache = null;
        this.packageName = packageName;
        this.classLoader = classLoader;
    }

    @Deprecated
    public NativeJavaPackage(String packageName, ClassLoader classLoader) {
        this(false, packageName, classLoader);
    }

    @Deprecated
    public NativeJavaPackage(String packageName) {
        this(false, packageName, Context.getCurrentContext().getApplicationClassLoader());
    }

    public String getClassName() {
        return "JavaPackage";
    }

    public boolean has(String id, Scriptable start) {
        return true;
    }

    public boolean has(int index, Scriptable start) {
        return false;
    }

    public void put(String id, Scriptable start, Object value) {
    }

    public void put(int index, Scriptable start, Object value) {
        throw Context.reportRuntimeError0("msg.pkg.int");
    }

    public Object get(String id, Scriptable start) {
        return getPkgProperty(id, start, true);
    }

    public Object get(int index, Scriptable start) {
        return NOT_FOUND;
    }

    /* access modifiers changed from: 0000 */
    public NativeJavaPackage forcePackage(String name, Scriptable scope) {
        Object cached = super.get(name, (Scriptable) this);
        if (cached != null && (cached instanceof NativeJavaPackage)) {
            return (NativeJavaPackage) cached;
        }
        Object pkg = new NativeJavaPackage(true, this.packageName.length() == 0 ? name : this.packageName + "." + name, this.classLoader);
        ScriptRuntime.setObjectProtoAndParent(pkg, scope);
        super.put(name, (Scriptable) this, pkg);
        return pkg;
    }

    /* access modifiers changed from: declared_synchronized */
    public synchronized Object getPkgProperty(String name, Scriptable start, boolean createPkg) {
        Object cached;
        cached = super.get(name, start);
        if (cached == NOT_FOUND) {
            if (this.negativeCache == null || !this.negativeCache.contains(name)) {
                String className = this.packageName.length() == 0 ? name : this.packageName + '.' + name;
                Context cx = Context.getContext();
                ClassShutter shutter = cx.getClassShutter();
                Object newValue = null;
                if (shutter == null || shutter.visibleToScripts(className)) {
                    Class<?> cl;
                    if (this.classLoader != null) {
                        cl = Kit.classOrNull(this.classLoader, className);
                    } else {
                        cl = Kit.classOrNull(className);
                    }
                    if (cl != null) {
                        newValue = cx.getWrapFactory().wrapJavaClass(cx, ScriptableObject.getTopLevelScope(this), cl);
                        newValue.setPrototype(getPrototype());
                    }
                }
                if (newValue == null) {
                    if (createPkg) {
                        NativeJavaPackage pkg = new NativeJavaPackage(true, className, this.classLoader);
                        ScriptRuntime.setObjectProtoAndParent(pkg, getParentScope());
                        newValue = pkg;
                    } else {
                        if (this.negativeCache == null) {
                            this.negativeCache = new HashSet();
                        }
                        this.negativeCache.add(name);
                    }
                }
                if (newValue != null) {
                    super.put(name, start, newValue);
                }
                cached = newValue;
            } else {
                cached = null;
            }
        }
        return cached;
    }

    public Object getDefaultValue(Class<?> cls) {
        return toString();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.classLoader = Context.getCurrentContext().getApplicationClassLoader();
    }

    public String toString() {
        return "[JavaPackage " + this.packageName + "]";
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof NativeJavaPackage)) {
            return false;
        }
        NativeJavaPackage njp = (NativeJavaPackage) obj;
        if (this.packageName.equals(njp.packageName) && this.classLoader == njp.classLoader) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int i;
        int hashCode = this.packageName.hashCode();
        if (this.classLoader == null) {
            i = 0;
        } else {
            i = this.classLoader.hashCode();
        }
        return i ^ hashCode;
    }
}
