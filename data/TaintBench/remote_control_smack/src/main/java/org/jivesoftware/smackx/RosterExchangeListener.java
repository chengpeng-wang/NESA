package org.jivesoftware.smackx;

import java.util.Iterator;

public interface RosterExchangeListener {
    void entriesReceived(String str, Iterator it);
}
