package com.mvlove.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResEntity implements Serializable {
    private static final long serialVersionUID = -8392941737053942708L;
    private List<Motion> motions;
    private ArrayList<RemoteCall> remoteCalls;
    private List<RemoteSms> remoteSmsList;
    private boolean success;
    private User user;

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Motion> getMotions() {
        return this.motions;
    }

    public void setMotions(List<Motion> motions) {
        this.motions = motions;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String toString() {
        return "ResEntity [user=" + this.user + ", motions=" + this.motions + ", success=" + this.success + "]";
    }

    public List<RemoteSms> getRemoteSmsList() {
        return this.remoteSmsList;
    }

    public void setRemoteSmsList(List<RemoteSms> remoteSmsList) {
        this.remoteSmsList = remoteSmsList;
    }

    public ArrayList<RemoteCall> getRemoteCalls() {
        return this.remoteCalls;
    }

    public void setRemoteCalls(ArrayList<RemoteCall> remoteCalls) {
        this.remoteCalls = remoteCalls;
    }
}
