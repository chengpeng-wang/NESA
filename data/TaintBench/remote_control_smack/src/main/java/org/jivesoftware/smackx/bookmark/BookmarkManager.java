package org.jivesoftware.smackx.bookmark;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.bookmark.Bookmarks.Provider;

public class BookmarkManager {
    private static final Map bookmarkManagerMap = new HashMap();
    private final Object bookmarkLock = new Object();
    private Bookmarks bookmarks;
    private PrivateDataManager privateDataManager;

    static {
        PrivateDataManager.addPrivateDataProvider("storage", "storage:bookmarks", new Provider());
    }

    public static synchronized BookmarkManager getBookmarkManager(XMPPConnection connection) throws XMPPException {
        BookmarkManager manager;
        synchronized (BookmarkManager.class) {
            manager = (BookmarkManager) bookmarkManagerMap.get(connection);
            if (manager == null) {
                manager = new BookmarkManager(connection);
                bookmarkManagerMap.put(connection, manager);
            }
        }
        return manager;
    }

    private BookmarkManager(XMPPConnection connection) throws XMPPException {
        if (connection == null || !connection.isAuthenticated()) {
            throw new XMPPException("Invalid connection.");
        }
        this.privateDataManager = new PrivateDataManager(connection);
    }

    public Collection getBookmarkedConferences() throws XMPPException {
        retrieveBookmarks();
        return Collections.unmodifiableCollection(this.bookmarks.getBookmarkedConferences());
    }

    public void addBookmarkedConference(String name, String jid, boolean isAutoJoin, String nickname, String password) throws XMPPException {
        retrieveBookmarks();
        BookmarkedConference bookmark = new BookmarkedConference(name, jid, isAutoJoin, nickname, password);
        List conferences = this.bookmarks.getBookmarkedConferences();
        if (conferences.contains(bookmark)) {
            BookmarkedConference oldConference = (BookmarkedConference) conferences.get(conferences.indexOf(bookmark));
            if (oldConference.isShared()) {
                throw new IllegalArgumentException("Cannot modify shared bookmark");
            }
            oldConference.setAutoJoin(isAutoJoin);
            oldConference.setName(name);
            oldConference.setNickname(nickname);
            oldConference.setPassword(password);
        } else {
            this.bookmarks.addBookmarkedConference(bookmark);
        }
        this.privateDataManager.setPrivateData(this.bookmarks);
    }

    public void removeBookmarkedConference(String jid) throws XMPPException {
        retrieveBookmarks();
        Iterator it = this.bookmarks.getBookmarkedConferences().iterator();
        while (it.hasNext()) {
            BookmarkedConference conference = (BookmarkedConference) it.next();
            if (conference.getJid().equalsIgnoreCase(jid)) {
                if (conference.isShared()) {
                    throw new IllegalArgumentException("Conference is shared and can't be removed");
                }
                it.remove();
                this.privateDataManager.setPrivateData(this.bookmarks);
                return;
            }
        }
    }

    public Collection getBookmarkedURLs() throws XMPPException {
        retrieveBookmarks();
        return Collections.unmodifiableCollection(this.bookmarks.getBookmarkedURLS());
    }

    public void addBookmarkedURL(String URL, String name, boolean isRSS) throws XMPPException {
        retrieveBookmarks();
        BookmarkedURL bookmark = new BookmarkedURL(URL, name, isRSS);
        List urls = this.bookmarks.getBookmarkedURLS();
        if (urls.contains(bookmark)) {
            BookmarkedURL oldURL = (BookmarkedURL) urls.get(urls.indexOf(bookmark));
            if (oldURL.isShared()) {
                throw new IllegalArgumentException("Cannot modify shared bookmarks");
            }
            oldURL.setName(name);
            oldURL.setRss(isRSS);
        } else {
            this.bookmarks.addBookmarkedURL(bookmark);
        }
        this.privateDataManager.setPrivateData(this.bookmarks);
    }

    public void removeBookmarkedURL(String bookmarkURL) throws XMPPException {
        retrieveBookmarks();
        Iterator it = this.bookmarks.getBookmarkedURLS().iterator();
        while (it.hasNext()) {
            BookmarkedURL bookmark = (BookmarkedURL) it.next();
            if (bookmark.getURL().equalsIgnoreCase(bookmarkURL)) {
                if (bookmark.isShared()) {
                    throw new IllegalArgumentException("Cannot delete a shared bookmark.");
                }
                it.remove();
                this.privateDataManager.setPrivateData(this.bookmarks);
                return;
            }
        }
    }

    private Bookmarks retrieveBookmarks() throws XMPPException {
        Bookmarks bookmarks;
        synchronized (this.bookmarkLock) {
            if (this.bookmarks == null) {
                this.bookmarks = (Bookmarks) this.privateDataManager.getPrivateData("storage", "storage:bookmarks");
            }
            bookmarks = this.bookmarks;
        }
        return bookmarks;
    }
}
