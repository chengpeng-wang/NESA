package org.jivesoftware.smackx.filetransfer;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.OrFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.packet.StreamInitiation;

public class FaultTolerantNegotiator extends StreamNegotiator {
    private XMPPConnection connection;
    private PacketFilter primaryFilter;
    private StreamNegotiator primaryNegotiator;
    private PacketFilter secondaryFilter;
    private StreamNegotiator secondaryNegotiator;

    private class NegotiatorService implements Callable<InputStream> {
        private PacketCollector collector;

        NegotiatorService(PacketCollector collector) {
            this.collector = collector;
        }

        public InputStream call() throws Exception {
            Packet streamInitiation = this.collector.nextResult((long) (SmackConfiguration.getPacketReplyTimeout() * 2));
            if (streamInitiation != null) {
                return FaultTolerantNegotiator.this.determineNegotiator(streamInitiation).negotiateIncomingStream(streamInitiation);
            }
            throw new XMPPException("No response from remote client");
        }
    }

    public FaultTolerantNegotiator(XMPPConnection connection, StreamNegotiator primary, StreamNegotiator secondary) {
        this.primaryNegotiator = primary;
        this.secondaryNegotiator = secondary;
        this.connection = connection;
    }

    public PacketFilter getInitiationPacketFilter(String from, String streamID) {
        if (this.primaryFilter == null || this.secondaryFilter == null) {
            this.primaryFilter = this.primaryNegotiator.getInitiationPacketFilter(from, streamID);
            this.secondaryFilter = this.secondaryNegotiator.getInitiationPacketFilter(from, streamID);
        }
        return new OrFilter(this.primaryFilter, this.secondaryFilter);
    }

    /* access modifiers changed from: 0000 */
    public InputStream negotiateIncomingStream(Packet streamInitiation) throws XMPPException {
        throw new UnsupportedOperationException("Negotiation only handled by create incoming stream method.");
    }

    /* access modifiers changed from: final */
    public final Packet initiateIncomingStream(XMPPConnection connection, StreamInitiation initiation) {
        throw new UnsupportedOperationException("Initiation handled by createIncomingStream method");
    }

    public InputStream createIncomingStream(StreamInitiation initiation) throws XMPPException {
        Throwable th;
        PacketCollector collector = this.connection.createPacketCollector(getInitiationPacketFilter(initiation.getFrom(), initiation.getSessionID()));
        this.connection.sendPacket(super.createInitiationAccept(initiation, getNamespaces()));
        CompletionService<InputStream> service = new ExecutorCompletionService(Executors.newFixedThreadPool(2));
        List<Future<InputStream>> futures = new ArrayList();
        InputStream stream = null;
        Future<InputStream> future;
        try {
            futures.add(service.submit(new NegotiatorService(collector)));
            futures.add(service.submit(new NegotiatorService(collector)));
            int i = 0;
            XMPPException exception = null;
            while (stream == null) {
                XMPPException exception2;
                try {
                    if (i >= futures.size()) {
                        break;
                    }
                    i++;
                    try {
                        future = service.poll(10, TimeUnit.SECONDS);
                        if (future != null) {
                            stream = (InputStream) future.get();
                            exception2 = exception;
                            exception = exception2;
                        }
                    } catch (InterruptedException e) {
                    }
                } catch (InterruptedException e2) {
                    exception2 = exception;
                } catch (ExecutionException e3) {
                    exception2 = new XMPPException(e3.getCause());
                } catch (Throwable th2) {
                    th = th2;
                    exception2 = exception;
                }
            }
            for (Future<InputStream> future2 : futures) {
                future2.cancel(true);
            }
            collector.cancel();
            if (stream != null) {
                return stream;
            }
            if (exception != null) {
                throw exception;
            }
            throw new XMPPException("File transfer negotiation failed.");
        } catch (Throwable th3) {
            th = th3;
            for (Future<InputStream> future22 : futures) {
                future22.cancel(true);
            }
            collector.cancel();
            throw th;
        }
    }

    /* access modifiers changed from: private */
    public StreamNegotiator determineNegotiator(Packet streamInitiation) {
        return this.primaryFilter.accept(streamInitiation) ? this.primaryNegotiator : this.secondaryNegotiator;
    }

    public OutputStream createOutgoingStream(String streamID, String initiator, String target) throws XMPPException {
        try {
            return this.primaryNegotiator.createOutgoingStream(streamID, initiator, target);
        } catch (XMPPException e) {
            return this.secondaryNegotiator.createOutgoingStream(streamID, initiator, target);
        }
    }

    public String[] getNamespaces() {
        String[] primary = this.primaryNegotiator.getNamespaces();
        String[] secondary = this.secondaryNegotiator.getNamespaces();
        String[] namespaces = new String[(primary.length + secondary.length)];
        System.arraycopy(primary, 0, namespaces, 0, primary.length);
        System.arraycopy(secondary, 0, namespaces, primary.length, secondary.length);
        return namespaces;
    }

    public void cleanup() {
    }
}
