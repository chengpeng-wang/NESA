package org.jivesoftware.smackx.bookmark;

public class BookmarkedURL implements SharedBookmark {
    private final String URL;
    private boolean isRss;
    private boolean isShared;
    private String name;

    protected BookmarkedURL(String URL) {
        this.URL = URL;
    }

    protected BookmarkedURL(String URL, String name, boolean isRss) {
        this.URL = URL;
        this.name = name;
        this.isRss = isRss;
    }

    public String getName() {
        return this.name;
    }

    /* access modifiers changed from: protected */
    public void setName(String name) {
        this.name = name;
    }

    public String getURL() {
        return this.URL;
    }

    /* access modifiers changed from: protected */
    public void setRss(boolean isRss) {
        this.isRss = isRss;
    }

    public boolean isRss() {
        return this.isRss;
    }

    public boolean equals(Object obj) {
        if (obj instanceof BookmarkedURL) {
            return ((BookmarkedURL) obj).getURL().equalsIgnoreCase(this.URL);
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void setShared(boolean shared) {
        this.isShared = shared;
    }

    public boolean isShared() {
        return this.isShared;
    }
}
