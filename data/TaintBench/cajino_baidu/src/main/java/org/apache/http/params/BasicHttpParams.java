package org.apache.http.params;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;

public class BasicHttpParams extends AbstractHttpParams implements Serializable, Cloneable {
    private static final long serialVersionUID = -7086398485908701455L;
    private final HashMap parameters = new HashMap();

    public Object getParameter(String name) {
        return this.parameters.get(name);
    }

    public HttpParams setParameter(String name, Object value) {
        this.parameters.put(name, value);
        return this;
    }

    public boolean removeParameter(String name) {
        if (!this.parameters.containsKey(name)) {
            return false;
        }
        this.parameters.remove(name);
        return true;
    }

    public void setParameters(String[] names, Object value) {
        for (String parameter : names) {
            setParameter(parameter, value);
        }
    }

    public boolean isParameterSet(String name) {
        return getParameter(name) != null;
    }

    public boolean isParameterSetLocally(String name) {
        return this.parameters.get(name) != null;
    }

    public void clear() {
        this.parameters.clear();
    }

    public HttpParams copy() {
        try {
            return (HttpParams) clone();
        } catch (CloneNotSupportedException e) {
            throw new UnsupportedOperationException("Cloning not supported");
        }
    }

    public Object clone() throws CloneNotSupportedException {
        BasicHttpParams clone = (BasicHttpParams) super.clone();
        copyParams(clone);
        return clone;
    }

    /* access modifiers changed from: protected */
    public void copyParams(HttpParams target) {
        for (Entry me : this.parameters.entrySet()) {
            if (me.getKey() instanceof String) {
                target.setParameter((String) me.getKey(), me.getValue());
            }
        }
    }
}
