package org.mozilla.javascript;

import org.mozilla.javascript.PolicySecurityController.SecureCaller;

public class RhinoSecurityManager extends SecurityManager {
    /* access modifiers changed from: protected */
    public Class<?> getCurrentScriptClass() {
        for (Class<?> c : getClassContext()) {
            if ((c != InterpretedFunction.class && NativeFunction.class.isAssignableFrom(c)) || SecureCaller.class.isAssignableFrom(c)) {
                return c;
            }
        }
        return null;
    }
}
