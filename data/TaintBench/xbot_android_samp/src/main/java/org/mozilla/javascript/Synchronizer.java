package org.mozilla.javascript;

public class Synchronizer extends Delegator {
    private Object syncObject;

    public Synchronizer(Scriptable obj) {
        super(obj);
    }

    public Synchronizer(Scriptable obj, Object syncObject) {
        super(obj);
        this.syncObject = syncObject;
    }

    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        Object obj;
        Object call;
        if (this.syncObject != null) {
            obj = this.syncObject;
        } else {
            Scriptable obj2 = thisObj;
        }
        if (obj2 instanceof Wrapper) {
            obj2 = ((Wrapper) obj2).unwrap();
        }
        synchronized (r0) {
            call = ((Function) this.obj).call(cx, scope, thisObj, args);
        }
        return call;
    }
}
