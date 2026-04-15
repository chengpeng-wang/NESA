package org.mozilla.javascript.regexp;

/* compiled from: NativeRegExp */
class REGlobalData {
    REBackTrackData backTrackStackTop;
    int cp;
    boolean multiline;
    long[] parens;
    RECompiled regexp;
    int skipped;
    REProgState stateStackTop;

    REGlobalData() {
    }

    /* access modifiers changed from: 0000 */
    public int parensIndex(int i) {
        return (int) this.parens[i];
    }

    /* access modifiers changed from: 0000 */
    public int parensLength(int i) {
        return (int) (this.parens[i] >>> 32);
    }

    /* access modifiers changed from: 0000 */
    public void setParens(int i, int index, int length) {
        if (this.backTrackStackTop != null && this.backTrackStackTop.parens == this.parens) {
            this.parens = (long[]) this.parens.clone();
        }
        this.parens[i] = (((long) index) & 4294967295L) | (((long) length) << 32);
    }
}
