package org.jivesoftware.smackx.bookmark;

public class BookmarkedConference implements SharedBookmark {
    private boolean autoJoin;
    private boolean isShared;
    private final String jid;
    private String name;
    private String nickname;
    private String password;

    protected BookmarkedConference(String jid) {
        this.jid = jid;
    }

    protected BookmarkedConference(String name, String jid, boolean autoJoin, String nickname, String password) {
        this.name = name;
        this.jid = jid;
        this.autoJoin = autoJoin;
        this.nickname = nickname;
        this.password = password;
    }

    public String getName() {
        return this.name;
    }

    /* access modifiers changed from: protected */
    public void setName(String name) {
        this.name = name;
    }

    public boolean isAutoJoin() {
        return this.autoJoin;
    }

    /* access modifiers changed from: protected */
    public void setAutoJoin(boolean autoJoin) {
        this.autoJoin = autoJoin;
    }

    public String getJid() {
        return this.jid;
    }

    public String getNickname() {
        return this.nickname;
    }

    /* access modifiers changed from: protected */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return this.password;
    }

    /* access modifiers changed from: protected */
    public void setPassword(String password) {
        this.password = password;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof BookmarkedConference)) {
            return false;
        }
        return ((BookmarkedConference) obj).getJid().equalsIgnoreCase(this.jid);
    }

    /* access modifiers changed from: protected */
    public void setShared(boolean isShared) {
        this.isShared = isShared;
    }

    public boolean isShared() {
        return this.isShared;
    }
}
