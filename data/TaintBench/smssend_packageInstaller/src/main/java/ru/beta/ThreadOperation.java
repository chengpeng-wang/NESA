package ru.beta;

public class ThreadOperation implements Runnable {
    private int _id = 0;
    private ThreadOperationListener _listener = null;
    private Object _obj;

    ThreadOperation(ThreadOperationListener listener, int id, Object obj) {
        this._listener = listener;
        this._id = id;
        this._obj = obj;
    }

    public void run() {
        this._listener.threadOperationRun(this._id, this._obj);
    }
}
