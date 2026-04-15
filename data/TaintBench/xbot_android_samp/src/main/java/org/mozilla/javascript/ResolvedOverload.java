package org.mozilla.javascript;

import java.util.Arrays;

/* compiled from: NativeJavaMethod */
class ResolvedOverload {
    final int index;
    final Class<?>[] types;

    ResolvedOverload(Object[] args, int index) {
        this.index = index;
        this.types = new Class[args.length];
        int l = args.length;
        for (int i = 0; i < l; i++) {
            Object arg = args[i];
            if (arg instanceof Wrapper) {
                arg = ((Wrapper) arg).unwrap();
            }
            this.types[i] = arg == null ? null : arg.getClass();
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean matches(Object[] args) {
        if (args.length != this.types.length) {
            return false;
        }
        int l = args.length;
        for (int i = 0; i < l; i++) {
            Object arg = args[i];
            if (arg instanceof Wrapper) {
                arg = ((Wrapper) arg).unwrap();
            }
            if (arg == null) {
                if (this.types[i] != null) {
                    return false;
                }
            } else if (arg.getClass() != this.types[i]) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object other) {
        if (!(other instanceof ResolvedOverload)) {
            return false;
        }
        ResolvedOverload ovl = (ResolvedOverload) other;
        if (Arrays.equals(this.types, ovl.types) && this.index == ovl.index) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Arrays.hashCode(this.types);
    }
}
