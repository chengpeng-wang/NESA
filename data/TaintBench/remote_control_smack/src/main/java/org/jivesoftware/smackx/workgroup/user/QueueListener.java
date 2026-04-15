package org.jivesoftware.smackx.workgroup.user;

public interface QueueListener {
    void departedQueue();

    void joinedQueue();

    void queuePositionUpdated(int i);

    void queueWaitTimeUpdated(int i);
}
