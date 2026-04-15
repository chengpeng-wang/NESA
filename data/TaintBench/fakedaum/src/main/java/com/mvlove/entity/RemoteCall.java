package com.mvlove.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Date;

public class RemoteCall implements Parcelable {
    public static final Creator<RemoteCall> CREATOR = new Creator<RemoteCall>() {
        public RemoteCall[] newArray(int size) {
            return new RemoteCall[size];
        }

        public RemoteCall createFromParcel(Parcel source) {
            return new RemoteCall(source);
        }
    };
    private static final long serialVersionUID = 1784334858070733531L;
    private long addTime = System.currentTimeMillis();
    private Date callAfter;
    private String callNumber;
    private boolean deleteCallLog = false;
    private Long duration;
    private boolean excuted = false;
    private Long id;
    private String ownerPhone;

    public RemoteCall(Parcel source) {
        boolean z = false;
        this.id = Long.valueOf(source.readLong());
        this.duration = Long.valueOf(source.readLong());
        this.callNumber = source.readString();
        this.callAfter = new Date(source.readLong());
        this.ownerPhone = source.readString();
        if (source.readInt() != 0) {
            z = true;
        }
        this.deleteCallLog = z;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id.longValue());
        dest.writeLong(this.duration.longValue());
        dest.writeString(this.callNumber);
        dest.writeLong(this.callAfter == null ? System.currentTimeMillis() : this.callAfter.getTime());
        dest.writeString(this.ownerPhone);
        dest.writeInt(this.deleteCallLog ? 1 : 0);
    }

    public int describeContents() {
        return 0;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDuration() {
        return this.duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getCallNumber() {
        return this.callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public boolean isExcuted() {
        return this.excuted;
    }

    public void setExcuted(boolean excuted) {
        this.excuted = excuted;
    }

    public long getAddTime() {
        return this.addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public Date getCallAfter() {
        return this.callAfter;
    }

    public void setCallAfter(Date callAfter) {
        this.callAfter = callAfter;
    }

    public String getOwnerPhone() {
        return this.ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public boolean isDeleteCallLog() {
        return this.deleteCallLog;
    }

    public void setDeleteCallLog(boolean deleteCallLog) {
        this.deleteCallLog = deleteCallLog;
    }

    public boolean equals(Object o) {
        if (o != null && (o instanceof RemoteCall) && this.id == ((RemoteCall) o).id) {
            return true;
        }
        return false;
    }
}
