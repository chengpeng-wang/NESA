package org.apache.james.mime4j.field.address;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DomainList extends AbstractList<String> implements Serializable {
    private static final long serialVersionUID = 1;
    private final List<String> domains;

    public DomainList(List<String> domains, boolean dontCopy) {
        if (domains != null) {
            if (!dontCopy) {
                Object domains2 = new ArrayList(domains2);
            }
            this.domains = domains2;
            return;
        }
        this.domains = Collections.emptyList();
    }

    public int size() {
        return this.domains.size();
    }

    public String get(int index) {
        return (String) this.domains.get(index);
    }

    public String toRouteString() {
        StringBuilder sb = new StringBuilder();
        for (String domain : this.domains) {
            if (sb.length() > 0) {
                sb.append(',');
            }
            sb.append("@");
            sb.append(domain);
        }
        return sb.toString();
    }

    public String toString() {
        return toRouteString();
    }
}
