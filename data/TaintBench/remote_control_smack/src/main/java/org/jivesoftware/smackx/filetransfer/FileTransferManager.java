package org.jivesoftware.smackx.filetransfer;

import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.IQTypeFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.XMPPError.Condition;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.packet.StreamInitiation;

public class FileTransferManager {
    private XMPPConnection connection;
    private final FileTransferNegotiator fileTransferNegotiator;
    private List listeners;

    public FileTransferManager(XMPPConnection connection) {
        this.connection = connection;
        this.fileTransferNegotiator = FileTransferNegotiator.getInstanceFor(connection);
    }

    public void addFileTransferListener(FileTransferListener li) {
        if (this.listeners == null) {
            initListeners();
        }
        synchronized (this.listeners) {
            this.listeners.add(li);
        }
    }

    private void initListeners() {
        this.listeners = new ArrayList();
        this.connection.addPacketListener(new PacketListener() {
            public void processPacket(Packet packet) {
                FileTransferManager.this.fireNewRequest((StreamInitiation) packet);
            }
        }, new AndFilter(new PacketTypeFilter(StreamInitiation.class), new IQTypeFilter(Type.SET)));
    }

    /* access modifiers changed from: protected */
    public void fireNewRequest(StreamInitiation initiation) {
        FileTransferListener[] listeners;
        synchronized (this.listeners) {
            listeners = new FileTransferListener[this.listeners.size()];
            this.listeners.toArray(listeners);
        }
        FileTransferRequest request = new FileTransferRequest(this, initiation);
        for (FileTransferListener fileTransferRequest : listeners) {
            fileTransferRequest.fileTransferRequest(request);
        }
    }

    public void removeFileTransferListener(FileTransferListener li) {
        if (this.listeners != null) {
            synchronized (this.listeners) {
                this.listeners.remove(li);
            }
        }
    }

    public OutgoingFileTransfer createOutgoingFileTransfer(String userID) {
        if (userID != null && StringUtils.parseName(userID).length() > 0 && StringUtils.parseServer(userID).length() > 0 && StringUtils.parseResource(userID).length() > 0) {
            return new OutgoingFileTransfer(this.connection.getUser(), userID, this.fileTransferNegotiator.getNextStreamID(), this.fileTransferNegotiator);
        }
        throw new IllegalArgumentException("The provided user id was not fully qualified");
    }

    /* access modifiers changed from: protected */
    public IncomingFileTransfer createIncomingFileTransfer(FileTransferRequest request) {
        if (request == null) {
            throw new NullPointerException("RecieveRequest cannot be null");
        }
        IncomingFileTransfer transfer = new IncomingFileTransfer(request, this.fileTransferNegotiator);
        transfer.setFileInfo(request.getFileName(), request.getFileSize());
        return transfer;
    }

    /* access modifiers changed from: protected */
    public void rejectIncomingFileTransfer(FileTransferRequest request) {
        StreamInitiation initiation = request.getStreamInitiation();
        IQ rejection = FileTransferNegotiator.createIQ(initiation.getPacketID(), initiation.getFrom(), initiation.getTo(), Type.ERROR);
        rejection.setError(new XMPPError(Condition.forbidden));
        this.connection.sendPacket(rejection);
    }
}
