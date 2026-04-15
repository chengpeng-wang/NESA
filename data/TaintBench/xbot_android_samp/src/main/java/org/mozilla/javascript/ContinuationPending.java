package org.mozilla.javascript;

public class ContinuationPending extends RuntimeException {
    private static final long serialVersionUID = 4956008116771118856L;
    private Object applicationState;
    private NativeContinuation continuationState;

    ContinuationPending(NativeContinuation continuationState) {
        this.continuationState = continuationState;
    }

    public Object getContinuation() {
        return this.continuationState;
    }

    /* access modifiers changed from: 0000 */
    public NativeContinuation getContinuationState() {
        return this.continuationState;
    }

    public void setApplicationState(Object applicationState) {
        this.applicationState = applicationState;
    }

    public Object getApplicationState() {
        return this.applicationState;
    }
}
