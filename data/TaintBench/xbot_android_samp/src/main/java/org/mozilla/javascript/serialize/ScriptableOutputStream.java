package org.mozilla.javascript.serialize;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.UniqueTag;

public class ScriptableOutputStream extends ObjectOutputStream {
    private Scriptable scope;
    private Map<Object, String> table = new HashMap();

    static class PendingLookup implements Serializable {
        static final long serialVersionUID = -2692990309789917727L;
        private String name;

        PendingLookup(String name) {
            this.name = name;
        }

        /* access modifiers changed from: 0000 */
        public String getName() {
            return this.name;
        }
    }

    public ScriptableOutputStream(OutputStream out, Scriptable scope) throws IOException {
        super(out);
        this.scope = scope;
        this.table.put(scope, "");
        enableReplaceObject(true);
        excludeStandardObjectNames();
    }

    public void excludeAllIds(Object[] ids) {
        for (Object id : ids) {
            if ((id instanceof String) && (this.scope.get((String) id, this.scope) instanceof Scriptable)) {
                addExcludedName((String) id);
            }
        }
    }

    public void addOptionalExcludedName(String name) {
        UniqueTag obj = lookupQualifiedName(this.scope, name);
        if (obj != null && obj != UniqueTag.NOT_FOUND) {
            if (obj instanceof Scriptable) {
                this.table.put(obj, name);
                return;
            }
            throw new IllegalArgumentException("Object for excluded name " + name + " is not a Scriptable, it is " + obj.getClass().getName());
        }
    }

    public void addExcludedName(String name) {
        Object obj = lookupQualifiedName(this.scope, name);
        if (obj instanceof Scriptable) {
            this.table.put(obj, name);
            return;
        }
        throw new IllegalArgumentException("Object for excluded name " + name + " not found.");
    }

    public boolean hasExcludedName(String name) {
        return this.table.get(name) != null;
    }

    public void removeExcludedName(String name) {
        this.table.remove(name);
    }

    public void excludeStandardObjectNames() {
        String[] names = new String[]{"Object", "Object.prototype", "Function", "Function.prototype", "String", "String.prototype", "Math", "Array", "Array.prototype", "Error", "Error.prototype", "Number", "Number.prototype", "Date", "Date.prototype", "RegExp", "RegExp.prototype", "Script", "Script.prototype", "Continuation", "Continuation.prototype"};
        for (String addExcludedName : names) {
            addExcludedName(addExcludedName);
        }
        String[] optionalNames = new String[]{"XML", "XML.prototype", "XMLList", "XMLList.prototype"};
        for (String addExcludedName2 : optionalNames) {
            addOptionalExcludedName(addExcludedName2);
        }
    }

    static Object lookupQualifiedName(Scriptable scope, String qualifiedName) {
        StringTokenizer st = new StringTokenizer(qualifiedName, ".");
        Object result = scope;
        while (st.hasMoreTokens()) {
            result = ScriptableObject.getProperty((Scriptable) result, st.nextToken());
            if (result != null) {
                if (!(result instanceof Scriptable)) {
                    break;
                }
            }
            break;
        }
        return result;
    }

    /* access modifiers changed from: protected */
    public Object replaceObject(Object obj) throws IOException {
        String name = (String) this.table.get(obj);
        return name == null ? obj : new PendingLookup(name);
    }
}
