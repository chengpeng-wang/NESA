package com.google.android.apps.analytics;

class Event {
    static final String INSTALL_EVENT_CATEGORY = "__##GOOGLEINSTALL##__";
    static final String PAGEVIEW_EVENT_CATEGORY = "__##GOOGLEPAGEVIEW##__";
    final String accountId;
    final String action;
    final String category;
    final long eventId;
    final String label;
    final int randomVal;
    final int screenHeight;
    final int screenWidth;
    final int timestampCurrent;
    final int timestampFirst;
    final int timestampPrevious;
    final int userId;
    final int value;
    final int visits;

    Event(int i, String str, String str2, String str3, String str4, int i2, int i3, int i4) {
        this(-1, i, str, -1, -1, -1, -1, -1, str2, str3, str4, i2, i3, i4);
    }

    Event(long j, int i, String str, int i2, int i3, int i4, int i5, int i6, String str2, String str3, String str4, int i7, int i8, int i9) {
        this.eventId = j;
        this.userId = i;
        this.accountId = str;
        this.randomVal = i2;
        this.timestampFirst = i3;
        this.timestampPrevious = i4;
        this.timestampCurrent = i5;
        this.visits = i6;
        this.category = str2;
        this.action = str3;
        this.label = str4;
        this.value = i7;
        this.screenHeight = i9;
        this.screenWidth = i8;
    }

    public String toString() {
        String str = " ";
        String str2 = " ";
        str2 = " ";
        str2 = " ";
        str2 = " ";
        str2 = " ";
        str2 = " ";
        str2 = " ";
        str2 = " ";
        str2 = " ";
        str2 = " ";
        str2 = " ";
        return "id:" + this.eventId + str + "random:" + this.randomVal + str + "timestampCurrent:" + this.timestampCurrent + str + "timestampPrevious:" + this.timestampPrevious + str + "timestampFirst:" + this.timestampFirst + str + "visits:" + this.visits + str + "value:" + this.value + str + "category:" + this.category + str + "action:" + this.action + str + "label:" + this.label + str + "width:" + this.screenWidth + str + "height:" + this.screenHeight;
    }
}
