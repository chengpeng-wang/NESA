package org.mozilla.javascript.serialize;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Undefined;
import org.mozilla.javascript.UniqueTag;

public class ScriptableInputStream extends ObjectInputStream {
    private ClassLoader classLoader;
    private Scriptable scope;

    public ScriptableInputStream(InputStream in, Scriptable scope) throws IOException {
        super(in);
        this.scope = scope;
        enableResolveObject(true);
        Context cx = Context.getCurrentContext();
        if (cx != null) {
            this.classLoader = cx.getApplicationClassLoader();
        }
    }

    /* access modifiers changed from: protected */
    public Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        String name = desc.getName();
        if (this.classLoader != null) {
            try {
                return this.classLoader.loadClass(name);
            } catch (ClassNotFoundException e) {
            }
        }
        return super.resolveClass(desc);
    }

    /* access modifiers changed from: protected */
    public Object resolveObject(Object obj) throws IOException {
        if (obj instanceof PendingLookup) {
            String name = ((PendingLookup) obj).getName();
            obj = ScriptableOutputStream.lookupQualifiedName(this.scope, name);
            if (obj != Scriptable.NOT_FOUND) {
                return obj;
            }
            throw new IOException("Object " + name + " not found upon " + "deserialization.");
        } else if (obj instanceof UniqueTag) {
            return ((UniqueTag) obj).readResolve();
        } else {
            if (obj instanceof Undefined) {
                return ((Undefined) obj).readResolve();
            }
            return obj;
        }
    }
}
