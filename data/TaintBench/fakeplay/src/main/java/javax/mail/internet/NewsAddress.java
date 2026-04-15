package javax.mail.internet;

import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.mail.Address;

public class NewsAddress extends Address {
    private static final long serialVersionUID = -4203797299824684143L;
    protected String host;
    protected String newsgroup;

    public NewsAddress(String newsgroup) {
        this(newsgroup, null);
    }

    public NewsAddress(String newsgroup, String host) {
        this.newsgroup = newsgroup;
        this.host = host;
    }

    public String getType() {
        return "news";
    }

    public void setNewsgroup(String newsgroup) {
        this.newsgroup = newsgroup;
    }

    public String getNewsgroup() {
        return this.newsgroup;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return this.host;
    }

    public String toString() {
        return this.newsgroup;
    }

    public boolean equals(Object a) {
        if (!(a instanceof NewsAddress)) {
            return false;
        }
        NewsAddress s = (NewsAddress) a;
        if (!this.newsgroup.equals(s.newsgroup)) {
            return false;
        }
        if ((this.host != null || s.host != null) && (this.host == null || s.host == null || !this.host.equalsIgnoreCase(s.host))) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 0;
        if (this.newsgroup != null) {
            hash = 0 + this.newsgroup.hashCode();
        }
        if (this.host != null) {
            return hash + this.host.toLowerCase(Locale.ENGLISH).hashCode();
        }
        return hash;
    }

    public static String toString(Address[] addresses) {
        if (addresses == null || addresses.length == 0) {
            return null;
        }
        StringBuffer s = new StringBuffer(((NewsAddress) addresses[0]).toString());
        for (int i = 1; i < addresses.length; i++) {
            s.append(",").append(((NewsAddress) addresses[i]).toString());
        }
        return s.toString();
    }

    public static NewsAddress[] parse(String newsgroups) throws AddressException {
        StringTokenizer st = new StringTokenizer(newsgroups, ",");
        Vector nglist = new Vector();
        while (st.hasMoreTokens()) {
            nglist.addElement(new NewsAddress(st.nextToken()));
        }
        int size = nglist.size();
        NewsAddress[] na = new NewsAddress[size];
        if (size > 0) {
            nglist.copyInto(na);
        }
        return na;
    }
}
