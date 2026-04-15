package org.jivesoftware.smackx.workgroup.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.ListIterator;

public class ListenerEventDispatcher implements Runnable {
    protected transient boolean hasFinishedDispatching = false;
    protected transient boolean isRunning = false;
    protected transient ArrayList triplets = new ArrayList();

    protected class TripletContainer {
        protected Object listenerInstance;
        protected Method listenerMethod;
        protected Object[] methodArguments;

        protected TripletContainer(Object inst, Method meth, Object[] args) {
            this.listenerInstance = inst;
            this.listenerMethod = meth;
            this.methodArguments = args;
        }

        /* access modifiers changed from: protected */
        public Object getListenerInstance() {
            return this.listenerInstance;
        }

        /* access modifiers changed from: protected */
        public Method getListenerMethod() {
            return this.listenerMethod;
        }

        /* access modifiers changed from: protected */
        public Object[] getMethodArguments() {
            return this.methodArguments;
        }
    }

    public void addListenerTriplet(Object listenerInstance, Method listenerMethod, Object[] methodArguments) {
        if (!this.isRunning) {
            this.triplets.add(new TripletContainer(listenerInstance, listenerMethod, methodArguments));
        }
    }

    public boolean hasFinished() {
        return this.hasFinishedDispatching;
    }

    public void run() {
        this.isRunning = true;
        ListIterator li = this.triplets.listIterator();
        while (li.hasNext()) {
            TripletContainer tc = (TripletContainer) li.next();
            try {
                tc.getListenerMethod().invoke(tc.getListenerInstance(), tc.getMethodArguments());
            } catch (Exception e) {
                System.err.println("Exception dispatching an event: " + e);
                e.printStackTrace();
            }
        }
        this.hasFinishedDispatching = true;
    }
}
