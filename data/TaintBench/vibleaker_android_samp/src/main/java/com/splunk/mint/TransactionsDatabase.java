package com.splunk.mint;

import java.util.HashMap;

class TransactionsDatabase extends HashMap<String, Container> {
    protected static final String TransName = "TStart:name:";
    private static final long serialVersionUID = -3516111185615801729L;

    public class Container {
        public Long timestamp;
        public String transid;

        public Container(Long timestamp, String transid) {
            this.timestamp = timestamp;
            this.transid = transid;
        }
    }

    public synchronized boolean addStartedTransaction(ActionTransactionStart mTransactionStart) {
        boolean z = false;
        synchronized (this) {
            if (mTransactionStart != null) {
                try {
                    put(TransName + mTransactionStart.name, new Container(mTransactionStart.timestampMilis, mTransactionStart.transaction_id));
                    z = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return z;
    }

    public synchronized boolean closeStartedTransaction(String name) {
        boolean z = false;
        synchronized (this) {
            if (name != null) {
                if (containsKey(TransName + name)) {
                    put(TransName + name, new Container(Long.valueOf(-1), null));
                    z = true;
                }
            }
        }
        return z;
    }

    public synchronized Container getStartedTransactionContainer(String name) {
        Container container = null;
        synchronized (this) {
            if (name != null) {
                if (containsKey(TransName + name)) {
                    container = (Container) get(TransName + name);
                }
            }
        }
        return container;
    }
}
