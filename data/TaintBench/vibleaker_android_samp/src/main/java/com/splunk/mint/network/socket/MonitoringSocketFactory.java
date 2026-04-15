package com.splunk.mint.network.socket;

import com.splunk.mint.Logger;
import com.splunk.mint.network.MonitorRegistry;
import java.net.SocketImpl;
import java.net.SocketImplFactory;

public class MonitoringSocketFactory implements SocketImplFactory {
    private final MonitorRegistry registry;

    public MonitoringSocketFactory(MonitorRegistry registry) {
        this.registry = registry;
    }

    public SocketImpl createSocketImpl() {
        try {
            return new MonitoringSocketImpl(this.registry);
        } catch (Exception e) {
            Logger.logError("Could not create the Network Monitoring implementation, Network monitoring will be disabled.");
            throw new RuntimeException("Could not create the Network Monitoring implementation, Network monitoring will be disabled.");
        }
    }
}
