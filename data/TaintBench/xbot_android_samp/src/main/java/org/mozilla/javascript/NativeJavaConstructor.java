package org.mozilla.javascript;

public class NativeJavaConstructor extends BaseFunction {
    static final long serialVersionUID = -8149253217482668463L;
    MemberBox ctor;

    public NativeJavaConstructor(MemberBox ctor) {
        this.ctor = ctor;
    }

    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        return NativeJavaClass.constructSpecific(cx, scope, args, this.ctor);
    }

    public String getFunctionName() {
        return "<init>".concat(JavaMembers.liveConnectSignature(this.ctor.argTypes));
    }

    public String toString() {
        return "[JavaConstructor " + this.ctor.getName() + "]";
    }
}
