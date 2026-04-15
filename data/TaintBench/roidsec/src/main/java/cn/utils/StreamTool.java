package cn.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

public class StreamTool {
    public static void save(File file, byte[] data) throws Exception {
        FileOutputStream outStream = new FileOutputStream(file);
        outStream.write(data);
        outStream.close();
    }

    public static String readLine(PushbackInputStream in) throws IOException {
        int c;
        char[] buf = new char[128];
        int room = buf.length;
        int offset = 0;
        while (true) {
            c = in.read();
            switch (c) {
                case -1:
                case 10:
                    break;
                case 13:
                    int c2 = in.read();
                    if (!(c2 == 10 || c2 == -1)) {
                        in.unread(c2);
                        break;
                    }
                default:
                    room--;
                    if (room < 0) {
                        char[] lineBuffer = buf;
                        buf = new char[(offset + 128)];
                        room = (buf.length - offset) - 1;
                        System.arraycopy(lineBuffer, 0, buf, 0, offset);
                    }
                    int offset2 = offset + 1;
                    buf[offset] = (char) c;
                    offset = offset2;
            }
        }
        if (c == -1 && offset == 0) {
            return null;
        }
        return String.copyValueOf(buf, 0, offset);
    }

    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (true) {
            int len = inStream.read(buffer);
            if (len == -1) {
                outSteam.close();
                inStream.close();
                return outSteam.toByteArray();
            }
            outSteam.write(buffer, 0, len);
        }
    }
}
