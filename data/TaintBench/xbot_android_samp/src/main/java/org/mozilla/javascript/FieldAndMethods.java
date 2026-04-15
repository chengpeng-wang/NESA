package org.mozilla.javascript;

import java.lang.reflect.Field;

/* compiled from: JavaMembers */
class FieldAndMethods extends NativeJavaMethod {
    static final long serialVersionUID = -9222428244284796755L;
    Field field;
    Object javaObject;

    FieldAndMethods(Scriptable scope, MemberBox[] methods, Field field) {
        super(methods);
        this.field = field;
        setParentScope(scope);
        setPrototype(ScriptableObject.getFunctionPrototype(scope));
    }

    public Object getDefaultValue(Class<?> hint) {
        if (hint == ScriptRuntime.FunctionClass) {
            return this;
        }
        try {
            Object rval = this.field.get(this.javaObject);
            Class<?> type = this.field.getType();
            Context cx = Context.getContext();
            rval = cx.getWrapFactory().wrap(cx, this, rval, type);
            if (rval instanceof Scriptable) {
                rval = ((Scriptable) rval).getDefaultValue(hint);
            }
            return rval;
        } catch (IllegalAccessException e) {
            throw Context.reportRuntimeError1("msg.java.internal.private", this.field.getName());
        }
    }
}
