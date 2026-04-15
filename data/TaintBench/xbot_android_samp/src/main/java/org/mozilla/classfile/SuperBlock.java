package org.mozilla.classfile;

/* compiled from: ClassFileWriter */
final class SuperBlock {
    private int end;
    private int index;
    private boolean isInQueue = false;
    private boolean isInitialized = false;
    private int[] locals;
    private int[] stack = new int[0];
    private int start;

    SuperBlock(int index, int start, int end, int[] initialLocals) {
        this.index = index;
        this.start = start;
        this.end = end;
        this.locals = new int[initialLocals.length];
        System.arraycopy(initialLocals, 0, this.locals, 0, initialLocals.length);
    }

    /* access modifiers changed from: 0000 */
    public int getIndex() {
        return this.index;
    }

    /* access modifiers changed from: 0000 */
    public int[] getLocals() {
        int[] copy = new int[this.locals.length];
        System.arraycopy(this.locals, 0, copy, 0, this.locals.length);
        return copy;
    }

    /* access modifiers changed from: 0000 */
    public int[] getTrimmedLocals() {
        int i;
        int last = this.locals.length - 1;
        while (last >= 0 && this.locals[last] == 0 && !TypeInfo.isTwoWords(this.locals[last - 1])) {
            last--;
        }
        last++;
        int size = last;
        for (i = 0; i < last; i++) {
            if (TypeInfo.isTwoWords(this.locals[i])) {
                size--;
            }
        }
        int[] copy = new int[size];
        i = 0;
        int j = 0;
        while (i < size) {
            copy[i] = this.locals[j];
            if (TypeInfo.isTwoWords(this.locals[j])) {
                j++;
            }
            i++;
            j++;
        }
        return copy;
    }

    /* access modifiers changed from: 0000 */
    public int[] getStack() {
        int[] copy = new int[this.stack.length];
        System.arraycopy(this.stack, 0, copy, 0, this.stack.length);
        return copy;
    }

    /* access modifiers changed from: 0000 */
    public boolean merge(int[] locals, int localsTop, int[] stack, int stackTop, ConstantPool pool) {
        boolean z = false;
        if (!this.isInitialized) {
            System.arraycopy(locals, 0, this.locals, 0, localsTop);
            this.stack = new int[stackTop];
            System.arraycopy(stack, 0, this.stack, 0, stackTop);
            this.isInitialized = true;
            return true;
        } else if (this.locals.length == localsTop && this.stack.length == stackTop) {
            boolean localsChanged = mergeState(this.locals, locals, localsTop, pool);
            boolean stackChanged = mergeState(this.stack, stack, stackTop, pool);
            if (localsChanged || stackChanged) {
                z = true;
            }
            return z;
        } else {
            throw new IllegalArgumentException("bad merge attempt");
        }
    }

    private boolean mergeState(int[] current, int[] incoming, int size, ConstantPool pool) {
        boolean changed = false;
        for (int i = 0; i < size; i++) {
            int currentType = current[i];
            current[i] = TypeInfo.merge(current[i], incoming[i], pool);
            if (currentType != current[i]) {
                changed = true;
            }
        }
        return changed;
    }

    /* access modifiers changed from: 0000 */
    public int getStart() {
        return this.start;
    }

    /* access modifiers changed from: 0000 */
    public int getEnd() {
        return this.end;
    }

    public String toString() {
        return "sb " + this.index;
    }

    /* access modifiers changed from: 0000 */
    public boolean isInitialized() {
        return this.isInitialized;
    }

    /* access modifiers changed from: 0000 */
    public void setInitialized(boolean b) {
        this.isInitialized = b;
    }

    /* access modifiers changed from: 0000 */
    public boolean isInQueue() {
        return this.isInQueue;
    }

    /* access modifiers changed from: 0000 */
    public void setInQueue(boolean b) {
        this.isInQueue = b;
    }
}
