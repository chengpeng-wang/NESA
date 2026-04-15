package com.googleprojects.mmsp;

public interface GCMListener {
    void GCMListener_MessageReceived(String str);

    void GCMListener_Registered(String str);
}
