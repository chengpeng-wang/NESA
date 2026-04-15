package com.trilead.ssh2;

import com.trilead.ssh2.sftp.AttribFlags;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SCPClient {
    Connection conn;

    class LenNamePair {
        String filename;
        long length;

        LenNamePair() {
        }
    }

    public SCPClient(Connection conn) {
        if (conn == null) {
            throw new IllegalArgumentException("Cannot accept null argument!");
        }
        this.conn = conn;
    }

    private void readResponse(InputStream is) throws IOException {
        int c = is.read();
        if (c != 0) {
            if (c == -1) {
                throw new IOException("Remote scp terminated unexpectedly.");
            } else if (c != 1 && c != 2) {
                throw new IOException("Remote scp sent illegal error code.");
            } else if (c == 2) {
                throw new IOException("Remote scp terminated with error.");
            } else {
                throw new IOException("Remote scp terminated with error (" + receiveLine(is) + ").");
            }
        }
    }

    private String receiveLine(InputStream is) throws IOException {
        StringBuffer sb = new StringBuffer(30);
        while (sb.length() <= AttribFlags.SSH_FILEXFER_ATTR_LINK_COUNT) {
            int c = is.read();
            if (c < 0) {
                throw new IOException("Remote scp terminated unexpectedly.");
            } else if (c == 10) {
                return sb.toString();
            } else {
                sb.append((char) c);
            }
        }
        throw new IOException("Remote scp sent a too long line");
    }

    private LenNamePair parseCLine(String line) throws IOException {
        if (line.length() < 8) {
            throw new IOException("Malformed C line sent by remote SCP binary, line too short.");
        } else if (line.charAt(4) != ' ' || line.charAt(5) == ' ') {
            throw new IOException("Malformed C line sent by remote SCP binary.");
        } else {
            int length_name_sep = line.indexOf(32, 5);
            if (length_name_sep == -1) {
                throw new IOException("Malformed C line sent by remote SCP binary.");
            }
            String length_substring = line.substring(5, length_name_sep);
            String name_substring = line.substring(length_name_sep + 1);
            if (length_substring.length() <= 0 || name_substring.length() <= 0) {
                throw new IOException("Malformed C line sent by remote SCP binary.");
            } else if ((length_substring.length() + 6) + name_substring.length() != line.length()) {
                throw new IOException("Malformed C line sent by remote SCP binary.");
            } else {
                try {
                    long len = Long.parseLong(length_substring);
                    if (len < 0) {
                        throw new IOException("Malformed C line sent by remote SCP binary, illegal file length.");
                    }
                    LenNamePair lnp = new LenNamePair();
                    lnp.length = len;
                    lnp.filename = name_substring;
                    return lnp;
                } catch (NumberFormatException e) {
                    throw new IOException("Malformed C line sent by remote SCP binary, cannot parse file length.");
                }
            }
        }
    }

    private void sendBytes(Session sess, byte[] data, String fileName, String mode) throws IOException {
        OutputStream os = sess.getStdin();
        InputStream is = new BufferedInputStream(sess.getStdout(), 512);
        readResponse(is);
        os.write(("C" + mode + " " + data.length + " " + fileName + "\n").getBytes("ISO-8859-1"));
        os.flush();
        readResponse(is);
        os.write(data, 0, data.length);
        os.write(0);
        os.flush();
        readResponse(is);
        os.write("E\n".getBytes("ISO-8859-1"));
        os.flush();
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x00d8  */
    private void sendFiles(com.trilead.ssh2.Session r17, java.lang.String[] r18, java.lang.String[] r19, java.lang.String r20) throws java.io.IOException {
        /*
        r16 = this;
        r13 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
        r1 = new byte[r13];
        r8 = new java.io.BufferedOutputStream;
        r13 = r17.getStdin();
        r14 = 40000; // 0x9c40 float:5.6052E-41 double:1.97626E-319;
        r8.<init>(r13, r14);
        r7 = new java.io.BufferedInputStream;
        r13 = r17.getStdout();
        r14 = 512; // 0x200 float:7.175E-43 double:2.53E-321;
        r7.<init>(r13, r14);
        r0 = r16;
        r0.readResponse(r7);
        r6 = 0;
    L_0x0021:
        r0 = r18;
        r13 = r0.length;
        if (r6 < r13) goto L_0x0035;
    L_0x0026:
        r13 = "E\n";
        r14 = "ISO-8859-1";
        r13 = r13.getBytes(r14);
        r8.write(r13);
        r8.flush();
        return;
    L_0x0035:
        r3 = new java.io.File;
        r13 = r18[r6];
        r3.<init>(r13);
        r9 = r3.length();
        if (r19 == 0) goto L_0x00aa;
    L_0x0042:
        r0 = r19;
        r13 = r0.length;
        if (r13 <= r6) goto L_0x00aa;
    L_0x0047:
        r13 = r19[r6];
        if (r13 == 0) goto L_0x00aa;
    L_0x004b:
        r11 = r19[r6];
    L_0x004d:
        r13 = new java.lang.StringBuilder;
        r14 = "C";
        r13.<init>(r14);
        r0 = r20;
        r13 = r13.append(r0);
        r14 = " ";
        r13 = r13.append(r14);
        r13 = r13.append(r9);
        r14 = " ";
        r13 = r13.append(r14);
        r13 = r13.append(r11);
        r14 = "\n";
        r13 = r13.append(r14);
        r2 = r13.toString();
        r13 = "ISO-8859-1";
        r13 = r2.getBytes(r13);
        r8.write(r13);
        r8.flush();
        r0 = r16;
        r0.readResponse(r7);
        r4 = 0;
        r5 = new java.io.FileInputStream;	 Catch:{ all -> 0x00e5 }
        r5.<init>(r3);	 Catch:{ all -> 0x00e5 }
    L_0x008f:
        r13 = 0;
        r13 = (r9 > r13 ? 1 : (r9 == r13 ? 0 : -1));
        if (r13 > 0) goto L_0x00af;
    L_0x0095:
        if (r5 == 0) goto L_0x009a;
    L_0x0097:
        r5.close();
    L_0x009a:
        r13 = 0;
        r8.write(r13);
        r8.flush();
        r0 = r16;
        r0.readResponse(r7);
        r6 = r6 + 1;
        goto L_0x0021;
    L_0x00aa:
        r11 = r3.getName();
        goto L_0x004d;
    L_0x00af:
        r13 = r1.length;	 Catch:{ all -> 0x00d4 }
        r13 = (long) r13;	 Catch:{ all -> 0x00d4 }
        r13 = (r9 > r13 ? 1 : (r9 == r13 ? 0 : -1));
        if (r13 <= 0) goto L_0x00dc;
    L_0x00b5:
        r12 = r1.length;	 Catch:{ all -> 0x00d4 }
    L_0x00b6:
        r13 = 0;
        r13 = r5.read(r1, r13, r12);	 Catch:{ all -> 0x00d4 }
        if (r13 == r12) goto L_0x00de;
    L_0x00bd:
        r13 = new java.io.IOException;	 Catch:{ all -> 0x00d4 }
        r14 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00d4 }
        r15 = "Cannot read enough from local file ";
        r14.<init>(r15);	 Catch:{ all -> 0x00d4 }
        r15 = r18[r6];	 Catch:{ all -> 0x00d4 }
        r14 = r14.append(r15);	 Catch:{ all -> 0x00d4 }
        r14 = r14.toString();	 Catch:{ all -> 0x00d4 }
        r13.<init>(r14);	 Catch:{ all -> 0x00d4 }
        throw r13;	 Catch:{ all -> 0x00d4 }
    L_0x00d4:
        r13 = move-exception;
        r4 = r5;
    L_0x00d6:
        if (r4 == 0) goto L_0x00db;
    L_0x00d8:
        r4.close();
    L_0x00db:
        throw r13;
    L_0x00dc:
        r12 = (int) r9;
        goto L_0x00b6;
    L_0x00de:
        r13 = 0;
        r8.write(r1, r13, r12);	 Catch:{ all -> 0x00d4 }
        r13 = (long) r12;
        r9 = r9 - r13;
        goto L_0x008f;
    L_0x00e5:
        r13 = move-exception;
        goto L_0x00d6;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trilead.ssh2.SCPClient.sendFiles(com.trilead.ssh2.Session, java.lang.String[], java.lang.String[], java.lang.String):void");
    }

    private void receiveFiles(Session sess, OutputStream[] targets) throws IOException {
        byte[] buffer = new byte[AttribFlags.SSH_FILEXFER_ATTR_LINK_COUNT];
        OutputStream os = new BufferedOutputStream(sess.getStdin(), 512);
        InputStream is = new BufferedInputStream(sess.getStdout(), 40000);
        os.write(0);
        os.flush();
        int i = 0;
        while (i < targets.length) {
            int c;
            String line;
            do {
                c = is.read();
                if (c < 0) {
                    throw new IOException("Remote scp terminated unexpectedly.");
                }
                line = receiveLine(is);
            } while (c == 84);
            if (c == 1 || c == 2) {
                throw new IOException("Remote SCP error: " + line);
            } else if (c == 67) {
                LenNamePair lnp = parseCLine(line);
                os.write(0);
                os.flush();
                long remain = lnp.length;
                while (remain > 0) {
                    int trans;
                    if (remain > ((long) buffer.length)) {
                        trans = buffer.length;
                    } else {
                        trans = (int) remain;
                    }
                    int this_time_received = is.read(buffer, 0, trans);
                    if (this_time_received < 0) {
                        throw new IOException("Remote scp terminated connection unexpectedly");
                    }
                    targets[i].write(buffer, 0, this_time_received);
                    remain -= (long) this_time_received;
                }
                readResponse(is);
                os.write(0);
                os.flush();
                i++;
            } else {
                throw new IOException("Remote SCP error: " + ((char) c) + line);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:42:0x00ef  */
    private void receiveFiles(com.trilead.ssh2.Session r19, java.lang.String[] r20, java.lang.String r21) throws java.io.IOException {
        /*
        r18 = this;
        r15 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
        r1 = new byte[r15];
        r10 = new java.io.BufferedOutputStream;
        r15 = r19.getStdin();
        r16 = 512; // 0x200 float:7.175E-43 double:2.53E-321;
        r0 = r16;
        r10.<init>(r15, r0);
        r7 = new java.io.BufferedInputStream;
        r15 = r19.getStdout();
        r16 = 40000; // 0x9c40 float:5.6052E-41 double:1.97626E-319;
        r0 = r16;
        r7.<init>(r15, r0);
        r15 = 0;
        r10.write(r15);
        r10.flush();
        r6 = 0;
    L_0x0027:
        r0 = r20;
        r15 = r0.length;
        if (r6 < r15) goto L_0x002d;
    L_0x002c:
        return;
    L_0x002d:
        r9 = 0;
    L_0x002e:
        r2 = r7.read();
        if (r2 >= 0) goto L_0x003c;
    L_0x0034:
        r15 = new java.io.IOException;
        r16 = "Remote scp terminated unexpectedly.";
        r15.<init>(r16);
        throw r15;
    L_0x003c:
        r0 = r18;
        r8 = r0.receiveLine(r7);
        r15 = 84;
        if (r2 == r15) goto L_0x002e;
    L_0x0046:
        r15 = 1;
        if (r2 == r15) goto L_0x004c;
    L_0x0049:
        r15 = 2;
        if (r2 != r15) goto L_0x0063;
    L_0x004c:
        r15 = new java.io.IOException;
        r16 = new java.lang.StringBuilder;
        r17 = "Remote SCP error: ";
        r16.<init>(r17);
        r0 = r16;
        r16 = r0.append(r8);
        r16 = r16.toString();
        r15.<init>(r16);
        throw r15;
    L_0x0063:
        r15 = 67;
        if (r2 != r15) goto L_0x00b7;
    L_0x0067:
        r0 = r18;
        r9 = r0.parseCLine(r8);
        r15 = 0;
        r10.write(r15);
        r10.flush();
        r3 = new java.io.File;
        r15 = new java.lang.StringBuilder;
        r16 = java.lang.String.valueOf(r21);
        r15.<init>(r16);
        r16 = java.io.File.separatorChar;
        r15 = r15.append(r16);
        r0 = r9.filename;
        r16 = r0;
        r15 = r15.append(r16);
        r15 = r15.toString();
        r3.<init>(r15);
        r4 = 0;
        r5 = new java.io.FileOutputStream;	 Catch:{ all -> 0x00fc }
        r5.<init>(r3);	 Catch:{ all -> 0x00fc }
        r11 = r9.length;	 Catch:{ all -> 0x00eb }
    L_0x009c:
        r15 = 0;
        r15 = (r11 > r15 ? 1 : (r11 == r15 ? 0 : -1));
        if (r15 > 0) goto L_0x00d5;
    L_0x00a2:
        if (r5 == 0) goto L_0x00a7;
    L_0x00a4:
        r5.close();
    L_0x00a7:
        r0 = r18;
        r0.readResponse(r7);
        r15 = 0;
        r10.write(r15);
        r10.flush();
        r6 = r6 + 1;
        goto L_0x0027;
    L_0x00b7:
        r15 = new java.io.IOException;
        r16 = new java.lang.StringBuilder;
        r17 = "Remote SCP error: ";
        r16.<init>(r17);
        r0 = (char) r2;
        r17 = r0;
        r16 = r16.append(r17);
        r0 = r16;
        r16 = r0.append(r8);
        r16 = r16.toString();
        r15.<init>(r16);
        throw r15;
    L_0x00d5:
        r15 = r1.length;	 Catch:{ all -> 0x00eb }
        r15 = (long) r15;	 Catch:{ all -> 0x00eb }
        r15 = (r11 > r15 ? 1 : (r11 == r15 ? 0 : -1));
        if (r15 <= 0) goto L_0x00f3;
    L_0x00db:
        r14 = r1.length;	 Catch:{ all -> 0x00eb }
    L_0x00dc:
        r15 = 0;
        r13 = r7.read(r1, r15, r14);	 Catch:{ all -> 0x00eb }
        if (r13 >= 0) goto L_0x00f5;
    L_0x00e3:
        r15 = new java.io.IOException;	 Catch:{ all -> 0x00eb }
        r16 = "Remote scp terminated connection unexpectedly";
        r15.<init>(r16);	 Catch:{ all -> 0x00eb }
        throw r15;	 Catch:{ all -> 0x00eb }
    L_0x00eb:
        r15 = move-exception;
        r4 = r5;
    L_0x00ed:
        if (r4 == 0) goto L_0x00f2;
    L_0x00ef:
        r4.close();
    L_0x00f2:
        throw r15;
    L_0x00f3:
        r14 = (int) r11;
        goto L_0x00dc;
    L_0x00f5:
        r15 = 0;
        r5.write(r1, r15, r13);	 Catch:{ all -> 0x00eb }
        r15 = (long) r13;
        r11 = r11 - r15;
        goto L_0x009c;
    L_0x00fc:
        r15 = move-exception;
        goto L_0x00ed;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trilead.ssh2.SCPClient.receiveFiles(com.trilead.ssh2.Session, java.lang.String[], java.lang.String):void");
    }

    public void put(String localFile, String remoteTargetDirectory) throws IOException {
        put(new String[]{localFile}, remoteTargetDirectory, "0600");
    }

    public void put(String[] localFiles, String remoteTargetDirectory) throws IOException {
        put(localFiles, remoteTargetDirectory, "0600");
    }

    public void put(String localFile, String remoteTargetDirectory, String mode) throws IOException {
        put(new String[]{localFile}, remoteTargetDirectory, mode);
    }

    public void put(String localFile, String remoteFileName, String remoteTargetDirectory, String mode) throws IOException {
        put(new String[]{localFile}, new String[]{remoteFileName}, remoteTargetDirectory, mode);
    }

    public void put(byte[] data, String remoteFileName, String remoteTargetDirectory) throws IOException {
        put(data, remoteFileName, remoteTargetDirectory, "0600");
    }

    public void put(byte[] data, String remoteFileName, String remoteTargetDirectory, String mode) throws IOException {
        Session sess = null;
        if (remoteFileName == null || remoteTargetDirectory == null || mode == null) {
            throw new IllegalArgumentException("Null argument.");
        } else if (mode.length() != 4) {
            throw new IllegalArgumentException("Invalid mode.");
        } else {
            int i = 0;
            while (i < mode.length()) {
                if (Character.isDigit(mode.charAt(i))) {
                    i++;
                } else {
                    throw new IllegalArgumentException("Invalid mode.");
                }
            }
            remoteTargetDirectory = remoteTargetDirectory.trim();
            if (remoteTargetDirectory.length() <= 0) {
                remoteTargetDirectory = ".";
            }
            String cmd = "scp -t -d " + remoteTargetDirectory;
            try {
                sess = this.conn.openSession();
                sess.execCommand(cmd);
                sendBytes(sess, data, remoteFileName, mode);
                if (sess != null) {
                    sess.close();
                }
            } catch (IOException e) {
                throw ((IOException) new IOException("Error during SCP transfer.").initCause(e));
            } catch (Throwable th) {
                if (sess != null) {
                    sess.close();
                }
            }
        }
    }

    public void put(String[] localFiles, String remoteTargetDirectory, String mode) throws IOException {
        put(localFiles, null, remoteTargetDirectory, mode);
    }

    public void put(String[] localFiles, String[] remoteFiles, String remoteTargetDirectory, String mode) throws IOException {
        Session sess = null;
        if (localFiles == null || remoteTargetDirectory == null || mode == null) {
            throw new IllegalArgumentException("Null argument.");
        } else if (mode.length() != 4) {
            throw new IllegalArgumentException("Invalid mode.");
        } else {
            int i = 0;
            while (i < mode.length()) {
                if (Character.isDigit(mode.charAt(i))) {
                    i++;
                } else {
                    throw new IllegalArgumentException("Invalid mode.");
                }
            }
            if (localFiles.length != 0) {
                remoteTargetDirectory = remoteTargetDirectory.trim();
                if (remoteTargetDirectory.length() <= 0) {
                    remoteTargetDirectory = ".";
                }
                String cmd = "scp -t -d " + remoteTargetDirectory;
                for (String str : localFiles) {
                    if (str == null) {
                        throw new IllegalArgumentException("Cannot accept null filename.");
                    }
                }
                try {
                    sess = this.conn.openSession();
                    sess.execCommand(cmd);
                    sendFiles(sess, localFiles, remoteFiles, mode);
                    if (sess != null) {
                        sess.close();
                    }
                } catch (IOException e) {
                    throw ((IOException) new IOException("Error during SCP transfer.").initCause(e));
                } catch (Throwable th) {
                    if (sess != null) {
                        sess.close();
                    }
                }
            }
        }
    }

    public void get(String remoteFile, String localTargetDirectory) throws IOException {
        get(new String[]{remoteFile}, localTargetDirectory);
    }

    public void get(String remoteFile, OutputStream target) throws IOException {
        get(new String[]{remoteFile}, new OutputStream[]{target});
    }

    private void get(String[] remoteFiles, OutputStream[] targets) throws IOException {
        Session sess = null;
        if (remoteFiles == null || targets == null) {
            throw new IllegalArgumentException("Null argument.");
        } else if (remoteFiles.length != targets.length) {
            throw new IllegalArgumentException("Length of arguments does not match.");
        } else if (remoteFiles.length != 0) {
            String cmd = "scp -f";
            for (int i = 0; i < remoteFiles.length; i++) {
                if (remoteFiles[i] == null) {
                    throw new IllegalArgumentException("Cannot accept null filename.");
                }
                String tmp = remoteFiles[i].trim();
                if (tmp.length() == 0) {
                    throw new IllegalArgumentException("Cannot accept empty filename.");
                }
                cmd = new StringBuilder(String.valueOf(cmd)).append(" ").append(tmp).toString();
            }
            try {
                sess = this.conn.openSession();
                sess.execCommand(cmd);
                receiveFiles(sess, targets);
                if (sess != null) {
                    sess.close();
                }
            } catch (IOException e) {
                throw ((IOException) new IOException("Error during SCP transfer.").initCause(e));
            } catch (Throwable th) {
                if (sess != null) {
                    sess.close();
                }
            }
        }
    }

    public void get(String[] remoteFiles, String localTargetDirectory) throws IOException {
        Session sess = null;
        if (remoteFiles == null || localTargetDirectory == null) {
            throw new IllegalArgumentException("Null argument.");
        } else if (remoteFiles.length != 0) {
            String cmd = "scp -f";
            for (int i = 0; i < remoteFiles.length; i++) {
                if (remoteFiles[i] == null) {
                    throw new IllegalArgumentException("Cannot accept null filename.");
                }
                String tmp = remoteFiles[i].trim();
                if (tmp.length() == 0) {
                    throw new IllegalArgumentException("Cannot accept empty filename.");
                }
                cmd = new StringBuilder(String.valueOf(cmd)).append(" ").append(tmp).toString();
            }
            try {
                sess = this.conn.openSession();
                sess.execCommand(cmd);
                receiveFiles(sess, remoteFiles, localTargetDirectory);
                if (sess != null) {
                    sess.close();
                }
            } catch (IOException e) {
                throw ((IOException) new IOException("Error during SCP transfer.").initCause(e));
            } catch (Throwable th) {
                if (sess != null) {
                    sess.close();
                }
            }
        }
    }
}
