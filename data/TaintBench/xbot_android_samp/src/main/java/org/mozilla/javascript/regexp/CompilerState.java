package org.mozilla.javascript.regexp;

import org.mozilla.javascript.Context;

/* compiled from: NativeRegExp */
class CompilerState {
    int backReferenceLimit;
    int classCount;
    int cp = 0;
    char[] cpbegin;
    int cpend;
    Context cx;
    int flags;
    int maxBackReference;
    int parenCount;
    int parenNesting;
    int progLength;
    RENode result;

    CompilerState(Context cx, char[] source, int length, int flags) {
        this.cx = cx;
        this.cpbegin = source;
        this.cpend = length;
        this.flags = flags;
        this.backReferenceLimit = Integer.MAX_VALUE;
        this.maxBackReference = 0;
        this.parenCount = 0;
        this.classCount = 0;
        this.progLength = 0;
    }
}
