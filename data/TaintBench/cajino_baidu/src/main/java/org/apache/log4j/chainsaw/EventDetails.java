package org.apache.log4j.chainsaw;

import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggingEvent;

class EventDetails {
    private final String mCategoryName;
    private final String mLocationDetails;
    private final String mMessage;
    private final String mNDC;
    private final Priority mPriority;
    private final String mThreadName;
    private final String[] mThrowableStrRep;
    private final long mTimeStamp;

    EventDetails(long aTimeStamp, Priority aPriority, String aCategoryName, String aNDC, String aThreadName, String aMessage, String[] aThrowableStrRep, String aLocationDetails) {
        this.mTimeStamp = aTimeStamp;
        this.mPriority = aPriority;
        this.mCategoryName = aCategoryName;
        this.mNDC = aNDC;
        this.mThreadName = aThreadName;
        this.mMessage = aMessage;
        this.mThrowableStrRep = aThrowableStrRep;
        this.mLocationDetails = aLocationDetails;
    }

    EventDetails(LoggingEvent aEvent) {
        this(aEvent.timeStamp, aEvent.getLevel(), aEvent.getLoggerName(), aEvent.getNDC(), aEvent.getThreadName(), aEvent.getRenderedMessage(), aEvent.getThrowableStrRep(), aEvent.getLocationInformation() == null ? null : aEvent.getLocationInformation().fullInfo);
    }

    /* access modifiers changed from: 0000 */
    public long getTimeStamp() {
        return this.mTimeStamp;
    }

    /* access modifiers changed from: 0000 */
    public Priority getPriority() {
        return this.mPriority;
    }

    /* access modifiers changed from: 0000 */
    public String getCategoryName() {
        return this.mCategoryName;
    }

    /* access modifiers changed from: 0000 */
    public String getNDC() {
        return this.mNDC;
    }

    /* access modifiers changed from: 0000 */
    public String getThreadName() {
        return this.mThreadName;
    }

    /* access modifiers changed from: 0000 */
    public String getMessage() {
        return this.mMessage;
    }

    /* access modifiers changed from: 0000 */
    public String getLocationDetails() {
        return this.mLocationDetails;
    }

    /* access modifiers changed from: 0000 */
    public String[] getThrowableStrRep() {
        return this.mThrowableStrRep;
    }
}
