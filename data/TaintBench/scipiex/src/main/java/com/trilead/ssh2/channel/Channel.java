package com.trilead.ssh2.channel;

public class Channel {
    static final int CHANNEL_BUFFER_SIZE = 30000;
    static final int STATE_CLOSED = 4;
    static final int STATE_OPEN = 2;
    static final int STATE_OPENING = 1;
    boolean EOF = false;
    final Object channelSendLock = new Object();
    boolean closeMessageRecv = false;
    boolean closeMessageSent = false;
    final ChannelManager cm;
    String exit_signal;
    Integer exit_status;
    int failedCounter = 0;
    String hexX11FakeCookie;
    int localID = -1;
    int localMaxPacketSize = -1;
    int localWindow = 0;
    final byte[] msgWindowAdjust = new byte[9];
    private String reasonClosed = null;
    private final Object reasonClosedLock = new Object();
    int remoteID = -1;
    int remoteMaxPacketSize = -1;
    long remoteWindow = 0;
    int state = 1;
    final byte[] stderrBuffer = new byte[CHANNEL_BUFFER_SIZE];
    int stderrReadpos = 0;
    final ChannelInputStream stderrStream;
    int stderrWritepos = 0;
    final ChannelOutputStream stdinStream;
    final byte[] stdoutBuffer = new byte[CHANNEL_BUFFER_SIZE];
    int stdoutReadpos = 0;
    final ChannelInputStream stdoutStream;
    int stdoutWritepos = 0;
    int successCounter = 0;

    public Channel(ChannelManager cm) {
        this.cm = cm;
        this.localWindow = CHANNEL_BUFFER_SIZE;
        this.localMaxPacketSize = 33976;
        this.stdinStream = new ChannelOutputStream(this);
        this.stdoutStream = new ChannelInputStream(this, false);
        this.stderrStream = new ChannelInputStream(this, true);
    }

    public ChannelInputStream getStderrStream() {
        return this.stderrStream;
    }

    public ChannelOutputStream getStdinStream() {
        return this.stdinStream;
    }

    public ChannelInputStream getStdoutStream() {
        return this.stdoutStream;
    }

    public String getExitSignal() {
        String str;
        synchronized (this) {
            str = this.exit_signal;
        }
        return str;
    }

    public Integer getExitStatus() {
        Integer num;
        synchronized (this) {
            num = this.exit_status;
        }
        return num;
    }

    public String getReasonClosed() {
        String str;
        synchronized (this.reasonClosedLock) {
            str = this.reasonClosed;
        }
        return str;
    }

    public void setReasonClosed(String reasonClosed) {
        synchronized (this.reasonClosedLock) {
            if (this.reasonClosed == null) {
                this.reasonClosed = reasonClosed;
            }
        }
    }
}
