package org.apache.http;

import java.io.Serializable;
import org.apache.http.util.CharArrayBuffer;

public class ProtocolVersion implements Serializable, Cloneable {
    private static final long serialVersionUID = 8950662842175091068L;
    protected final int major;
    protected final int minor;
    protected final String protocol;

    public ProtocolVersion(String protocol, int major, int minor) {
        if (protocol == null) {
            throw new IllegalArgumentException("Protocol name must not be null.");
        } else if (major < 0) {
            throw new IllegalArgumentException("Protocol major version number must not be negative.");
        } else if (minor < 0) {
            throw new IllegalArgumentException("Protocol minor version number may not be negative");
        } else {
            this.protocol = protocol;
            this.major = major;
            this.minor = minor;
        }
    }

    public final String getProtocol() {
        return this.protocol;
    }

    public final int getMajor() {
        return this.major;
    }

    public final int getMinor() {
        return this.minor;
    }

    public ProtocolVersion forVersion(int major, int minor) {
        return (major == this.major && minor == this.minor) ? this : new ProtocolVersion(this.protocol, major, minor);
    }

    public final int hashCode() {
        return (this.protocol.hashCode() ^ (this.major * 100000)) ^ this.minor;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ProtocolVersion)) {
            return false;
        }
        ProtocolVersion that = (ProtocolVersion) obj;
        if (this.protocol.equals(that.protocol) && this.major == that.major && this.minor == that.minor) {
            return true;
        }
        return false;
    }

    public boolean isComparable(ProtocolVersion that) {
        return that != null && this.protocol.equals(that.protocol);
    }

    public int compareToVersion(ProtocolVersion that) {
        if (that == null) {
            throw new IllegalArgumentException("Protocol version must not be null.");
        } else if (this.protocol.equals(that.protocol)) {
            int delta = getMajor() - that.getMajor();
            if (delta == 0) {
                return getMinor() - that.getMinor();
            }
            return delta;
        } else {
            throw new IllegalArgumentException(new StringBuffer().append("Versions for different protocols cannot be compared. ").append(this).append(" ").append(that).toString());
        }
    }

    public final boolean greaterEquals(ProtocolVersion version) {
        return isComparable(version) && compareToVersion(version) >= 0;
    }

    public final boolean lessEquals(ProtocolVersion version) {
        return isComparable(version) && compareToVersion(version) <= 0;
    }

    public String toString() {
        CharArrayBuffer buffer = new CharArrayBuffer(16);
        buffer.append(this.protocol);
        buffer.append('/');
        buffer.append(Integer.toString(this.major));
        buffer.append('.');
        buffer.append(Integer.toString(this.minor));
        return buffer.toString();
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
