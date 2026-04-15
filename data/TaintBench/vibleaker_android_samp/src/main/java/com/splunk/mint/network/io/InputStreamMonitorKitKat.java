package com.splunk.mint.network.io;

import com.splunk.mint.network.Counter;
import com.splunk.mint.network.MonitorRegistry;
import com.splunk.mint.network.socket.MonitoringSocketImpl;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.springframework.http.HttpHeaders;

public final class InputStreamMonitorKitKat extends InputStream {
    private static final String IN_POSTFIX = "-bytes-in";
    private static final int MAX_POSSIBLE_HEADER_LENGTH = 50;
    private StringBuffer body;
    private List<Byte> chars;
    boolean contentLengthFound = false;
    private final Counter counter;
    private boolean finishedReadingHeaders = false;
    HashMap<String, List<String>> headers = new HashMap(2);
    MonitoringSocketImpl monSocIm;
    private final InputStream original;
    boolean statusCodeFound = false;

    public InputStreamMonitorKitKat(String name, MonitorRegistry registry, InputStream original, MonitoringSocketImpl monSocIm) {
        this.original = original;
        this.counter = new Counter(name + IN_POSTFIX);
        this.chars = new ArrayList();
        this.body = new StringBuffer();
        this.monSocIm = monSocIm;
        this.finishedReadingHeaders = false;
        registry.add(this.counter);
    }

    public int read() throws IOException {
        int value = this.original.read();
        if (value > -1) {
            this.counter.inc();
        }
        if (!this.finishedReadingHeaders) {
            this.chars.add(Byte.valueOf((byte) value));
            updateBody();
        }
        return value;
    }

    public int read(byte[] buffer) throws IOException {
        int value = this.original.read(buffer);
        if (value > -1) {
            this.counter.inc((long) value);
        }
        if (!this.finishedReadingHeaders) {
            for (byte valueOf : buffer) {
                this.chars.add(Byte.valueOf(valueOf));
            }
            updateBody();
        }
        return value;
    }

    public int read(byte[] buffer, int byteOffset, int byteCount) throws IOException {
        int value = this.original.read(buffer, byteOffset, byteCount);
        if (value > -1) {
            this.counter.inc((long) value);
        }
        if (!this.finishedReadingHeaders) {
            for (int i = byteOffset; i < byteCount; i++) {
                this.chars.add(Byte.valueOf(buffer[i]));
            }
            updateBody();
        }
        return value;
    }

    private void updateBody() {
        byte[] data = new byte[this.chars.size()];
        for (int i = 0; i < data.length; i++) {
            data[i] = ((Byte) this.chars.get(i)).byteValue();
        }
        this.chars.clear();
        this.body.append(new String(data));
        if (this.body.toString().contains("\r\n\r\n")) {
            this.finishedReadingHeaders = true;
            tryToReadHeaders();
        }
    }

    public void tryToReadHeaders() {
        BufferedReader in = new BufferedReader(new StringReader(this.body.toString()));
        while (true) {
            try {
                String line = in.readLine();
                if (line != null) {
                    if (!this.statusCodeFound && line.contains("HTTP/") && line.length() < 50) {
                        try {
                            this.headers.put("splk-statuscode", Arrays.asList(new String[]{line.split(" ")[1].trim()}));
                            this.statusCodeFound = true;
                        } catch (ArrayIndexOutOfBoundsException e) {
                        }
                    }
                    if (!this.contentLengthFound && line.contains(":") && line.length() < 50) {
                        int start = line.indexOf(":");
                        if (start > -1) {
                            String name = line.substring(0, start).trim();
                            if (name.equals(HttpHeaders.CONTENT_LENGTH)) {
                                try {
                                    this.headers.put(name, Arrays.asList(new String[]{line.substring(start + 1, line.length()).trim()}));
                                    this.contentLengthFound = true;
                                } catch (ArrayIndexOutOfBoundsException e2) {
                                }
                            }
                        }
                    }
                    if (this.statusCodeFound && this.contentLengthFound) {
                        break;
                    }
                } else {
                    break;
                }
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
        if (this.monSocIm != null) {
            this.monSocIm.readingDone();
        }
    }

    public void close() throws IOException {
        this.original.close();
    }

    public HashMap<String, List<String>> getHeaders() {
        return this.headers;
    }
}
