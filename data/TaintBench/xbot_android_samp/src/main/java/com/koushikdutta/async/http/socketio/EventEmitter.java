package com.koushikdutta.async.http.socketio;

import com.koushikdutta.async.util.HashList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;

public class EventEmitter {
    HashList<EventCallback> callbacks = new HashList();

    interface OnceCallback extends EventCallback {
    }

    /* access modifiers changed from: 0000 */
    public void onEvent(String event, JSONArray arguments, Acknowledge acknowledge) {
        List<EventCallback> list = (List) this.callbacks.get(event);
        if (list != null) {
            Iterator<EventCallback> iter = list.iterator();
            while (iter.hasNext()) {
                EventCallback cb = (EventCallback) iter.next();
                cb.onEvent(event, arguments, acknowledge);
                if (cb instanceof OnceCallback) {
                    iter.remove();
                }
            }
        }
    }

    public void addListener(String event, EventCallback callback) {
        on(event, callback);
    }

    public void once(String event, final EventCallback callback) {
        on(event, new OnceCallback() {
            public void onEvent(String event, JSONArray arguments, Acknowledge acknowledge) {
                callback.onEvent(event, arguments, acknowledge);
            }
        });
    }

    public void on(String event, EventCallback callback) {
        this.callbacks.add(event, callback);
    }

    public void removeListener(String event, EventCallback callback) {
        List<EventCallback> list = (List) this.callbacks.get(event);
        if (list != null) {
            list.remove(callback);
        }
    }
}
