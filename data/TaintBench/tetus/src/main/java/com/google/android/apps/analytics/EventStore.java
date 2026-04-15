package com.google.android.apps.analytics;

interface EventStore {
    void deleteEvent(long j);

    int getNumStoredEvents();

    String getReferrer();

    int getStoreId();

    Event[] peekEvents();

    Event[] peekEvents(int i);

    void putEvent(Event event);

    void setReferrer(String str);

    void startNewVisit();
}
