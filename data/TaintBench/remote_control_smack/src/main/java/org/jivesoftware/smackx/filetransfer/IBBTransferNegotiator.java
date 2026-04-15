package org.jivesoftware.smackx.filetransfer;

import android.support.v4.internal.view.SupportMenu;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromContainsFilter;
import org.jivesoftware.smack.filter.FromMatchesFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.XMPPError.Condition;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.packet.IBBExtensions.Close;
import org.jivesoftware.smackx.packet.IBBExtensions.Data;
import org.jivesoftware.smackx.packet.IBBExtensions.Open;
import org.jivesoftware.smackx.packet.StreamInitiation;

public class IBBTransferNegotiator extends StreamNegotiator {
    public static final int DEFAULT_BLOCK_SIZE = 4096;
    protected static final String NAMESPACE = "http://jabber.org/protocol/ibb";
    /* access modifiers changed from: private */
    public XMPPConnection connection;

    private class IBBOutputStream extends OutputStream {
        protected byte[] buffer;
        private final IQ closePacket;
        protected int count = 0;
        private String messageID;
        protected int seq = 0;
        private String sid;
        final String userID;

        IBBOutputStream(String userID, String sid, int blockSize) {
            if (blockSize <= 0) {
                throw new IllegalArgumentException("Buffer size <= 0");
            }
            this.buffer = new byte[blockSize];
            this.userID = userID;
            this.messageID = new Message(userID).getPacketID();
            this.sid = sid;
            this.closePacket = createClosePacket(userID, sid);
        }

        private IQ createClosePacket(String userID, String sid) {
            IQ packet = new Close(sid);
            packet.setTo(userID);
            packet.setType(Type.SET);
            return packet;
        }

        public void write(int b) throws IOException {
            if (this.count >= this.buffer.length) {
                flushBuffer();
            }
            byte[] bArr = this.buffer;
            int i = this.count;
            this.count = i + 1;
            bArr[i] = (byte) b;
        }

        public synchronized void write(byte[] b, int off, int len) throws IOException {
            if (len >= this.buffer.length) {
                writeOut(b, off, this.buffer.length);
                write(b, this.buffer.length + off, len - this.buffer.length);
            } else {
                writeOut(b, off, len);
            }
        }

        private void writeOut(byte[] b, int off, int len) {
            if (len > this.buffer.length - this.count) {
                flushBuffer();
            }
            System.arraycopy(b, off, this.buffer, this.count, len);
            this.count += len;
        }

        private void flushBuffer() {
            writeToXML(this.buffer, 0, this.count);
            this.count = 0;
        }

        private synchronized void writeToXML(byte[] buffer, int offset, int len) {
            Message template;
            int i = 0;
            synchronized (this) {
                template = createTemplate(this.messageID + "_" + this.seq);
                Data ext = new Data(this.sid);
                template.addExtension(ext);
                ext.setData(StringUtils.encodeBase64(buffer, offset, len, false));
                ext.setSeq((long) this.seq);
                synchronized (this) {
                    try {
                        wait(100);
                    } catch (InterruptedException e) {
                    }
                }
            }
            IBBTransferNegotiator.this.connection.sendPacket(template);
            if (this.seq + 1 != SupportMenu.USER_MASK) {
                i = this.seq + 1;
            }
            this.seq = i;
        }

        public void close() throws IOException {
            IBBTransferNegotiator.this.connection.sendPacket(this.closePacket);
        }

        public void flush() throws IOException {
            flushBuffer();
        }

        public void write(byte[] b) throws IOException {
            write(b, 0, b.length);
        }

        public Message createTemplate(String messageID) {
            Message template = new Message(this.userID);
            template.setPacketID(messageID);
            return template;
        }
    }

    private class IBBInputStream extends InputStream implements PacketListener {
        private byte[] buffer;
        private int bufferPointer;
        private IQ closeConfirmation;
        private PacketCollector dataCollector;
        private boolean isClosed;
        private boolean isDone;
        private boolean isEOF;
        private Message lastMess;
        private int seq;
        private String streamID;

        private IBBInputStream(String streamID, PacketFilter dataFilter, PacketFilter closeFilter) {
            this.seq = -1;
            this.streamID = streamID;
            this.dataCollector = IBBTransferNegotiator.this.connection.createPacketCollector(dataFilter);
            IBBTransferNegotiator.this.connection.addPacketListener(this, closeFilter);
            this.bufferPointer = -1;
        }

        public synchronized int read() throws IOException {
            int i = -1;
            synchronized (this) {
                if (!(this.isEOF || this.isClosed)) {
                    if (this.bufferPointer == -1 || this.bufferPointer >= this.buffer.length) {
                        loadBufferWait();
                    }
                    byte[] bArr = this.buffer;
                    int i2 = this.bufferPointer;
                    this.bufferPointer = i2 + 1;
                    i = bArr[i2];
                }
            }
            return i;
        }

        public synchronized int read(byte[] b) throws IOException {
            return read(b, 0, b.length);
        }

        public synchronized int read(byte[] b, int off, int len) throws IOException {
            int i = -1;
            synchronized (this) {
                if (!(this.isEOF || this.isClosed)) {
                    if ((this.bufferPointer == -1 || this.bufferPointer >= this.buffer.length) && !loadBufferWait()) {
                        this.isEOF = true;
                    } else {
                        if (len - off > this.buffer.length - this.bufferPointer) {
                            len = this.buffer.length - this.bufferPointer;
                        }
                        System.arraycopy(this.buffer, this.bufferPointer, b, off, len);
                        this.bufferPointer += len;
                        i = len;
                    }
                }
            }
            return i;
        }

        private boolean loadBufferWait() throws IOException {
            Message mess = null;
            while (mess == null) {
                if (this.isDone) {
                    mess = (Message) this.dataCollector.pollResult();
                    if (mess == null) {
                        return false;
                    }
                } else {
                    mess = (Message) this.dataCollector.nextResult(1000);
                }
            }
            this.lastMess = mess;
            Data data = (Data) mess.getExtension(Data.ELEMENT_NAME, "http://jabber.org/protocol/ibb");
            checkSequence(mess, (int) data.getSeq());
            this.buffer = StringUtils.decodeBase64(data.getData());
            this.bufferPointer = 0;
            return true;
        }

        private void checkSequence(Message mess, int seq) throws IOException {
            if (this.seq == SupportMenu.USER_MASK) {
                this.seq = -1;
            }
            if (seq - 1 != this.seq) {
                cancelTransfer(mess);
                throw new IOException("Packets out of sequence");
            } else {
                this.seq = seq;
            }
        }

        private void cancelTransfer(Message mess) {
            cleanup();
            sendCancelMessage(mess);
        }

        private void cleanup() {
            this.dataCollector.cancel();
            IBBTransferNegotiator.this.connection.removePacketListener(this);
        }

        private void sendCancelMessage(Message message) {
            IQ error = FileTransferNegotiator.createIQ(message.getPacketID(), message.getFrom(), message.getTo(), Type.ERROR);
            error.setError(new XMPPError(Condition.remote_server_timeout, "Cancel Message Transfer"));
            IBBTransferNegotiator.this.connection.sendPacket(error);
        }

        public boolean markSupported() {
            return false;
        }

        public void processPacket(Packet packet) {
            if (((Close) packet).getSessionID().equals(this.streamID)) {
                this.isDone = true;
                this.closeConfirmation = FileTransferNegotiator.createIQ(packet.getPacketID(), packet.getFrom(), packet.getTo(), Type.RESULT);
            }
        }

        public synchronized void close() throws IOException {
            if (!this.isClosed) {
                cleanup();
                if (this.isEOF) {
                    sendCloseConfirmation();
                } else if (this.lastMess != null) {
                    sendCancelMessage(this.lastMess);
                }
                this.isClosed = true;
            }
        }

        private void sendCloseConfirmation() {
            IBBTransferNegotiator.this.connection.sendPacket(this.closeConfirmation);
        }
    }

    private static class IBBMessageSidFilter implements PacketFilter {
        private String from;
        private final String sessionID;

        public IBBMessageSidFilter(String from, String sessionID) {
            this.from = from;
            this.sessionID = sessionID;
        }

        public boolean accept(Packet packet) {
            if (!(packet instanceof Message) || !packet.getFrom().equalsIgnoreCase(this.from)) {
                return false;
            }
            Data data = (Data) packet.getExtension(Data.ELEMENT_NAME, "http://jabber.org/protocol/ibb");
            if (data == null || data.getSessionID() == null || !data.getSessionID().equalsIgnoreCase(this.sessionID)) {
                return false;
            }
            return true;
        }
    }

    private static class IBBOpenSidFilter implements PacketFilter {
        private String sessionID;

        public IBBOpenSidFilter(String sessionID) {
            if (sessionID == null) {
                throw new IllegalArgumentException("StreamID cannot be null");
            }
            this.sessionID = sessionID;
        }

        public boolean accept(Packet packet) {
            if (!Open.class.isInstance(packet)) {
                return false;
            }
            String sessionID = ((Open) packet).getSessionID();
            if (sessionID == null || !sessionID.equals(this.sessionID)) {
                return false;
            }
            return true;
        }
    }

    protected IBBTransferNegotiator(XMPPConnection connection) {
        this.connection = connection;
    }

    public PacketFilter getInitiationPacketFilter(String from, String streamID) {
        return new AndFilter(new FromContainsFilter(from), new IBBOpenSidFilter(streamID));
    }

    /* access modifiers changed from: 0000 */
    public InputStream negotiateIncomingStream(Packet streamInitiation) throws XMPPException {
        Open openRequest = (Open) streamInitiation;
        if (openRequest.getType().equals(Type.ERROR)) {
            throw new XMPPException(openRequest.getError());
        }
        PacketFilter dataFilter = new IBBMessageSidFilter(openRequest.getFrom(), openRequest.getSessionID());
        PacketFilter closeFilter = new AndFilter(new PacketTypeFilter(Close.class), new FromMatchesFilter(openRequest.getFrom()));
        InputStream stream = new IBBInputStream(openRequest.getSessionID(), dataFilter, closeFilter);
        initInBandTransfer(openRequest);
        return stream;
    }

    public InputStream createIncomingStream(StreamInitiation initiation) throws XMPPException {
        return negotiateIncomingStream(initiateIncomingStream(this.connection, initiation));
    }

    private void initInBandTransfer(Open openRequest) {
        this.connection.sendPacket(FileTransferNegotiator.createIQ(openRequest.getPacketID(), openRequest.getFrom(), openRequest.getTo(), Type.RESULT));
    }

    public OutputStream createOutgoingStream(String streamID, String initiator, String target) throws XMPPException {
        Open openIQ = new Open(streamID, 4096);
        openIQ.setTo(target);
        openIQ.setType(Type.SET);
        PacketCollector collector = this.connection.createPacketCollector(new PacketIDFilter(openIQ.getPacketID()));
        this.connection.sendPacket(openIQ);
        IQ openResponse = (IQ) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (openResponse == null) {
            throw new XMPPException("No response from peer on IBB open");
        }
        Type type = openResponse.getType();
        if (type.equals(Type.RESULT)) {
            return new IBBOutputStream(target, streamID, 4096);
        }
        if (type.equals(Type.ERROR)) {
            throw new XMPPException("Target returned an error", openResponse.getError());
        }
        throw new XMPPException("Target returned unknown response");
    }

    public String[] getNamespaces() {
        return new String[]{"http://jabber.org/protocol/ibb"};
    }

    public void cleanup() {
    }
}
