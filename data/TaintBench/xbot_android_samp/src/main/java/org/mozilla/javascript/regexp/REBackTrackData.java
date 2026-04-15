package org.mozilla.javascript.regexp;

/* compiled from: NativeRegExp */
class REBackTrackData {
    final int continuationOp;
    final int continuationPc;
    final int cp;
    final int op;
    final long[] parens;
    final int pc;
    final REBackTrackData previous;
    final REProgState stateStackTop;

    REBackTrackData(REGlobalData gData, int op, int pc, int cp, int continuationOp, int continuationPc) {
        this.previous = gData.backTrackStackTop;
        this.op = op;
        this.pc = pc;
        this.cp = cp;
        this.continuationOp = continuationOp;
        this.continuationPc = continuationPc;
        this.parens = gData.parens;
        this.stateStackTop = gData.stateStackTop;
    }
}
