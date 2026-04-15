package org.jivesoftware.smackx.bookmark;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smackx.packet.PrivateData;
import org.jivesoftware.smackx.provider.PrivateDataProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class Bookmarks implements PrivateData {
    private List bookmarkedConferences = new ArrayList();
    private List bookmarkedURLS = new ArrayList();

    public static class Provider implements PrivateDataProvider {
        public PrivateData parsePrivateData(XmlPullParser parser) throws Exception {
            Bookmarks storage = new Bookmarks();
            boolean done = false;
            while (!done) {
                int eventType = parser.next();
                if (eventType == 2 && "url".equals(parser.getName())) {
                    BookmarkedURL urlStorage = Bookmarks.getURLStorage(parser);
                    if (urlStorage != null) {
                        storage.addBookmarkedURL(urlStorage);
                    }
                } else if (eventType == 2 && "conference".equals(parser.getName())) {
                    storage.addBookmarkedConference(Bookmarks.getConferenceStorage(parser));
                } else if (eventType == 3 && "storage".equals(parser.getName())) {
                    done = true;
                }
            }
            return storage;
        }
    }

    public void addBookmarkedURL(BookmarkedURL bookmarkedURL) {
        this.bookmarkedURLS.add(bookmarkedURL);
    }

    public void removeBookmarkedURL(BookmarkedURL bookmarkedURL) {
        this.bookmarkedURLS.remove(bookmarkedURL);
    }

    public void clearBookmarkedURLS() {
        this.bookmarkedURLS.clear();
    }

    public void addBookmarkedConference(BookmarkedConference bookmarkedConference) {
        this.bookmarkedConferences.add(bookmarkedConference);
    }

    public void removeBookmarkedConference(BookmarkedConference bookmarkedConference) {
        this.bookmarkedConferences.remove(bookmarkedConference);
    }

    public void clearBookmarkedConferences() {
        this.bookmarkedConferences.clear();
    }

    public List getBookmarkedURLS() {
        return this.bookmarkedURLS;
    }

    public List getBookmarkedConferences() {
        return this.bookmarkedConferences;
    }

    public String getElementName() {
        return "storage";
    }

    public String getNamespace() {
        return "storage:bookmarks";
    }

    public String toXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<storage xmlns=\"storage:bookmarks\">");
        for (BookmarkedURL urlStorage : getBookmarkedURLS()) {
            if (!urlStorage.isShared()) {
                buf.append("<url name=\"").append(urlStorage.getName()).append("\" url=\"").append(urlStorage.getURL()).append("\"");
                if (urlStorage.isRss()) {
                    buf.append(" rss=\"").append(true).append("\"");
                }
                buf.append(" />");
            }
        }
        for (BookmarkedConference conference : getBookmarkedConferences()) {
            if (!conference.isShared()) {
                buf.append("<conference ");
                buf.append("name=\"").append(conference.getName()).append("\" ");
                buf.append("autojoin=\"").append(conference.isAutoJoin()).append("\" ");
                buf.append("jid=\"").append(conference.getJid()).append("\" ");
                buf.append(">");
                if (conference.getNickname() != null) {
                    buf.append("<nick>").append(conference.getNickname()).append("</nick>");
                }
                if (conference.getPassword() != null) {
                    buf.append("<password>").append(conference.getPassword()).append("</password>");
                }
                buf.append("</conference>");
            }
        }
        buf.append("</storage>");
        return buf.toString();
    }

    /* access modifiers changed from: private|static */
    public static BookmarkedURL getURLStorage(XmlPullParser parser) throws IOException, XmlPullParserException {
        String name = parser.getAttributeValue("", "name");
        String url = parser.getAttributeValue("", "url");
        String rssString = parser.getAttributeValue("", "rss");
        boolean rss = rssString != null && "true".equals(rssString);
        BookmarkedURL urlStore = new BookmarkedURL(url, name, rss);
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2 && "shared_bookmark".equals(parser.getName())) {
                urlStore.setShared(true);
            } else if (eventType == 3 && "url".equals(parser.getName())) {
                done = true;
            }
        }
        return urlStore;
    }

    /* access modifiers changed from: private|static */
    public static BookmarkedConference getConferenceStorage(XmlPullParser parser) throws Exception {
        String name = parser.getAttributeValue("", "name");
        String autojoin = parser.getAttributeValue("", "autojoin");
        BookmarkedConference conf = new BookmarkedConference(parser.getAttributeValue("", "jid"));
        conf.setName(name);
        conf.setAutoJoin(Boolean.valueOf(autojoin).booleanValue());
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2 && "nick".equals(parser.getName())) {
                conf.setNickname(parser.nextText());
            } else if (eventType == 2 && "password".equals(parser.getName())) {
                conf.setPassword(parser.nextText());
            } else if (eventType == 2 && "shared_bookmark".equals(parser.getName())) {
                conf.setShared(true);
            } else if (eventType == 3 && "conference".equals(parser.getName())) {
                done = true;
            }
        }
        return conf;
    }
}
