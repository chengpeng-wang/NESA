package org.apache.james.mime4j.field.address;

import java.io.StringReader;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.apache.james.mime4j.codec.EncoderUtil;
import org.apache.james.mime4j.field.address.parser.AddressListParser;
import org.apache.james.mime4j.field.address.parser.ParseException;

public class Mailbox extends Address {
    private static final DomainList EMPTY_ROUTE_LIST = new DomainList(Collections.emptyList(), true);
    private static final long serialVersionUID = 1;
    private final String domain;
    private final String localPart;
    private final String name;
    private final DomainList route;

    public Mailbox(String localPart, String domain) {
        this(null, null, localPart, domain);
    }

    public Mailbox(DomainList route, String localPart, String domain) {
        this(null, route, localPart, domain);
    }

    public Mailbox(String name, String localPart, String domain) {
        this(name, null, localPart, domain);
    }

    public Mailbox(String name, DomainList route, String localPart, String domain) {
        if (localPart == null || localPart.length() == 0) {
            throw new IllegalArgumentException();
        }
        if (name == null || name.length() == 0) {
            name = null;
        }
        this.name = name;
        if (route == null) {
            route = EMPTY_ROUTE_LIST;
        }
        this.route = route;
        this.localPart = localPart;
        if (domain == null || domain.length() == 0) {
            domain = null;
        }
        this.domain = domain;
    }

    Mailbox(String name, Mailbox baseMailbox) {
        this(name, baseMailbox.getRoute(), baseMailbox.getLocalPart(), baseMailbox.getDomain());
    }

    public static Mailbox parse(String rawMailboxString) {
        try {
            return Builder.getInstance().buildMailbox(new AddressListParser(new StringReader(rawMailboxString)).parseMailbox());
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public String getName() {
        return this.name;
    }

    public DomainList getRoute() {
        return this.route;
    }

    public String getLocalPart() {
        return this.localPart;
    }

    public String getDomain() {
        return this.domain;
    }

    public String getAddress() {
        if (this.domain == null) {
            return this.localPart;
        }
        return this.localPart + '@' + this.domain;
    }

    public String getDisplayString(boolean includeRoute) {
        int i;
        boolean includeAngleBrackets;
        if (this.route != null) {
            i = 1;
        } else {
            i = 0;
        }
        includeRoute &= i;
        if (this.name != null || includeRoute) {
            includeAngleBrackets = true;
        } else {
            includeAngleBrackets = false;
        }
        StringBuilder sb = new StringBuilder();
        if (this.name != null) {
            sb.append(this.name);
            sb.append(' ');
        }
        if (includeAngleBrackets) {
            sb.append('<');
        }
        if (includeRoute) {
            sb.append(this.route.toRouteString());
            sb.append(':');
        }
        sb.append(this.localPart);
        if (this.domain != null) {
            sb.append('@');
            sb.append(this.domain);
        }
        if (includeAngleBrackets) {
            sb.append('>');
        }
        return sb.toString();
    }

    public String getEncodedString() {
        StringBuilder sb = new StringBuilder();
        if (this.name != null) {
            sb.append(EncoderUtil.encodeAddressDisplayName(this.name));
            sb.append(" <");
        }
        sb.append(EncoderUtil.encodeAddressLocalPart(this.localPart));
        if (this.domain != null) {
            sb.append('@');
            sb.append(this.domain);
        }
        if (this.name != null) {
            sb.append('>');
        }
        return sb.toString();
    }

    public int hashCode() {
        return getCanonicalizedAddress().hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Mailbox)) {
            return false;
        }
        return getCanonicalizedAddress().equals(((Mailbox) obj).getCanonicalizedAddress());
    }

    /* access modifiers changed from: protected|final */
    public final void doAddMailboxesTo(List<Mailbox> results) {
        results.add(this);
    }

    private Object getCanonicalizedAddress() {
        if (this.domain == null) {
            return this.localPart;
        }
        return this.localPart + '@' + this.domain.toLowerCase(Locale.US);
    }
}
