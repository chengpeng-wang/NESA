package com.splunk.mint.network.io;

import com.splunk.mint.network.Counter;
import com.splunk.mint.network.MonitorRegistry;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.springframework.http.HttpHeaders;

public final class OutputStreamMonitor extends OutputStream {
    private static final int MAX_POSSIBLE_HEADER_LENGTH = 50;
    public static final String OUT_POSTFIX = "-bytes-out";
    private StringBuffer body = new StringBuffer();
    private List<Byte> chars = new ArrayList();
    private final Counter counter;
    private final OutputStream original;

    public OutputStreamMonitor(String name, MonitorRegistry registry, OutputStream original) {
        this.original = original;
        this.counter = new Counter(name + OUT_POSTFIX);
        registry.add(this.counter);
    }

    public void write(int oneByte) throws IOException {
        this.counter.inc();
        this.original.write(oneByte);
        this.chars.add(Byte.valueOf((byte) oneByte));
        updateBody();
    }

    public void write(byte[] buffer) throws IOException {
        this.counter.inc((long) buffer.length);
        this.original.write(buffer);
        for (byte valueOf : buffer) {
            this.chars.add(Byte.valueOf(valueOf));
        }
        updateBody();
    }

    public void write(byte[] buffer, int offset, int count) throws IOException {
        this.counter.inc((long) count);
        this.original.write(buffer, offset, count);
        for (int i = offset; i < count; i++) {
            this.chars.add(Byte.valueOf(buffer[i]));
        }
        updateBody();
    }

    private void updateBody() {
        byte[] data = new byte[this.chars.size()];
        for (int i = 0; i < data.length; i++) {
            data[i] = ((Byte) this.chars.get(i)).byteValue();
        }
        this.chars.clear();
        this.body.append(new String(data));
    }

    public void close() throws IOException {
        this.original.close();
    }

    public HashMap<String, List<String>> getHeaders() {
        HashMap<String, List<String>> headers = new HashMap(1);
        if (this.body != null && this.body.toString() != null && this.body.toString().length() > 50) {
            try {
                BufferedReader in = new BufferedReader(new StringReader(this.body.toString()));
                boolean host1Found = false;
                boolean host2Found = false;
                while (true) {
                    String line = in.readLine();
                    if (line != null) {
                        if (!host1Found && line.contains(":") && line.length() < 50) {
                            int start = line.indexOf(":");
                            if (start > -1) {
                                String name = line.substring(0, start).trim();
                                if (name.equals(HttpHeaders.HOST)) {
                                    headers.put(name, Arrays.asList(new String[]{line.substring(start + 1, line.length()).trim()}));
                                    host1Found = true;
                                }
                            }
                        }
                        if (!host2Found && (line.contains("POST") || line.contains("GET"))) {
                            headers.put("splk-host2", Arrays.asList(new String[]{line.split(" ")[1].trim()}));
                            host2Found = true;
                        }
                        if (host1Found && host2Found) {
                            break;
                        }
                    } else {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return headers;
    }
}
