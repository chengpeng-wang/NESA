package org.mozilla.javascript;

import java.util.Iterator;

public final class NativeIterator extends IdScriptableObject {
    public static final String ITERATOR_PROPERTY_NAME = "__iterator__";
    private static final Object ITERATOR_TAG = "Iterator";
    private static final int Id___iterator__ = 3;
    private static final int Id_constructor = 1;
    private static final int Id_next = 2;
    private static final int MAX_PROTOTYPE_ID = 3;
    private static final String STOP_ITERATION = "StopIteration";
    private static final long serialVersionUID = -4136968203581667681L;
    private Object objectIterator;

    public static class WrappedJavaIterator {
        private Iterator<?> iterator;
        private Scriptable scope;

        WrappedJavaIterator(Iterator<?> iterator, Scriptable scope) {
            this.iterator = iterator;
            this.scope = scope;
        }

        public Object next() {
            if (this.iterator.hasNext()) {
                return this.iterator.next();
            }
            throw new JavaScriptException(NativeIterator.getStopIterationObject(this.scope), null, 0);
        }

        public Object __iterator__(boolean b) {
            return this;
        }
    }

    static class StopIteration extends NativeObject {
        private static final long serialVersionUID = 2485151085722377663L;

        StopIteration() {
        }

        public String getClassName() {
            return NativeIterator.STOP_ITERATION;
        }

        public boolean hasInstance(Scriptable instance) {
            return instance instanceof StopIteration;
        }
    }

    static void init(ScriptableObject scope, boolean sealed) {
        new NativeIterator().exportAsJSClass(3, scope, sealed);
        NativeGenerator.init(scope, sealed);
        NativeObject obj = new StopIteration();
        obj.setPrototype(ScriptableObject.getObjectPrototype(scope));
        obj.setParentScope(scope);
        if (sealed) {
            obj.sealObject();
        }
        ScriptableObject.defineProperty(scope, STOP_ITERATION, obj, 2);
        scope.associateValue(ITERATOR_TAG, obj);
    }

    private NativeIterator() {
    }

    private NativeIterator(Object objectIterator) {
        this.objectIterator = objectIterator;
    }

    public static Object getStopIterationObject(Scriptable scope) {
        return ScriptableObject.getTopScopeValue(ScriptableObject.getTopLevelScope(scope), ITERATOR_TAG);
    }

    public String getClassName() {
        return "Iterator";
    }

    /* access modifiers changed from: protected */
    public void initPrototypeId(int id) {
        int arity;
        String s;
        switch (id) {
            case 1:
                arity = 2;
                s = "constructor";
                break;
            case 2:
                arity = 0;
                s = "next";
                break;
            case 3:
                arity = 1;
                s = ITERATOR_PROPERTY_NAME;
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(id));
        }
        initPrototypeMethod(ITERATOR_TAG, id, s, arity);
    }

    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (!f.hasTag(ITERATOR_TAG)) {
            return super.execIdCall(f, cx, scope, thisObj, args);
        }
        int id = f.methodId();
        if (id == 1) {
            return jsConstructor(cx, scope, thisObj, args);
        }
        if (thisObj instanceof NativeIterator) {
            NativeIterator iterator = (NativeIterator) thisObj;
            switch (id) {
                case 2:
                    return iterator.next(cx, scope);
                case 3:
                    return thisObj;
                default:
                    throw new IllegalArgumentException(String.valueOf(id));
            }
        }
        throw IdScriptableObject.incompatibleCallError(f);
    }

    private static Object jsConstructor(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        boolean keyOnly = false;
        if (args.length == 0 || args[0] == null || args[0] == Undefined.instance) {
            throw ScriptRuntime.typeError1("msg.no.properties", ScriptRuntime.toString(args.length == 0 ? Undefined.instance : args[0]));
        }
        Scriptable obj = ScriptRuntime.toObject(cx, scope, args[0]);
        if (args.length > 1 && ScriptRuntime.toBoolean(args[1])) {
            keyOnly = true;
        }
        if (thisObj != null) {
            Iterator<?> iterator = VMBridge.instance.getJavaIterator(cx, scope, obj);
            if (iterator != null) {
                scope = ScriptableObject.getTopLevelScope(scope);
                return cx.getWrapFactory().wrap(cx, scope, new WrappedJavaIterator(iterator, scope), WrappedJavaIterator.class);
            }
            Object jsIterator = ScriptRuntime.toIterator(cx, scope, obj, keyOnly);
            if (jsIterator != null) {
                return jsIterator;
            }
        }
        Object objectIterator = ScriptRuntime.enumInit(obj, cx, scope, keyOnly ? 3 : 5);
        ScriptRuntime.setEnumNumbers(objectIterator, true);
        NativeIterator result = new NativeIterator(objectIterator);
        result.setPrototype(ScriptableObject.getClassPrototype(scope, result.getClassName()));
        result.setParentScope(scope);
        return result;
    }

    private Object next(Context cx, Scriptable scope) {
        if (ScriptRuntime.enumNext(this.objectIterator).booleanValue()) {
            return ScriptRuntime.enumId(this.objectIterator, cx);
        }
        throw new JavaScriptException(getStopIterationObject(scope), null, 0);
    }

    /* access modifiers changed from: protected */
    public int findPrototypeId(String s) {
        int id = 0;
        String X = null;
        int s_length = s.length();
        if (s_length == 4) {
            X = "next";
            id = 2;
        } else if (s_length == 11) {
            X = "constructor";
            id = 1;
        } else if (s_length == 12) {
            X = ITERATOR_PROPERTY_NAME;
            id = 3;
        }
        if (X == null || X == s || X.equals(s)) {
            return id;
        }
        return 0;
    }
}
