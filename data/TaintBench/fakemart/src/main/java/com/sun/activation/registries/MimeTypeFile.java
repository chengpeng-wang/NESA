package com.sun.activation.registries;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class MimeTypeFile {
    private String fname = null;
    private Hashtable type_hash = new Hashtable();

    public MimeTypeFile(String new_fname) throws IOException {
        this.fname = new_fname;
        FileReader fr = new FileReader(new File(this.fname));
        try {
            parse(new BufferedReader(fr));
        } finally {
            try {
                fr.close();
            } catch (IOException e) {
            }
        }
    }

    public MimeTypeFile(InputStream is) throws IOException {
        parse(new BufferedReader(new InputStreamReader(is, "iso-8859-1")));
    }

    public MimeTypeEntry getMimeTypeEntry(String file_ext) {
        return (MimeTypeEntry) this.type_hash.get(file_ext);
    }

    public String getMIMETypeString(String file_ext) {
        MimeTypeEntry entry = getMimeTypeEntry(file_ext);
        if (entry != null) {
            return entry.getMIMEType();
        }
        return null;
    }

    public void appendToRegistry(String mime_types) {
        try {
            parse(new BufferedReader(new StringReader(mime_types)));
        } catch (IOException e) {
        }
    }

    private void parse(BufferedReader buf_reader) throws IOException {
        String prev = null;
        while (true) {
            String line = buf_reader.readLine();
            if (line == null) {
                break;
            }
            if (prev == null) {
                prev = line;
            } else {
                prev = new StringBuilder(String.valueOf(prev)).append(line).toString();
            }
            int end = prev.length();
            if (prev.length() <= 0 || prev.charAt(end - 1) != '\\') {
                parseEntry(prev);
                prev = null;
            } else {
                prev = prev.substring(0, end - 1);
            }
        }
        if (prev != null) {
            parseEntry(prev);
        }
    }

    private void parseEntry(String line) {
        String mime_type = null;
        line = line.trim();
        if (line.length() != 0 && line.charAt(0) != '#') {
            String file_ext;
            MimeTypeEntry entry;
            if (line.indexOf(61) > 0) {
                LineTokenizer lt = new LineTokenizer(line);
                while (lt.hasMoreTokens()) {
                    String name = lt.nextToken();
                    String value = null;
                    if (lt.hasMoreTokens() && lt.nextToken().equals("=") && lt.hasMoreTokens()) {
                        value = lt.nextToken();
                    }
                    if (value == null) {
                        if (LogSupport.isLoggable()) {
                            LogSupport.log("Bad .mime.types entry: " + line);
                            return;
                        }
                        return;
                    } else if (name.equals("type")) {
                        mime_type = value;
                    } else if (name.equals("exts")) {
                        StringTokenizer st = new StringTokenizer(value, ",");
                        while (st.hasMoreTokens()) {
                            file_ext = st.nextToken();
                            entry = new MimeTypeEntry(mime_type, file_ext);
                            this.type_hash.put(file_ext, entry);
                            if (LogSupport.isLoggable()) {
                                LogSupport.log("Added: " + entry.toString());
                            }
                        }
                    }
                }
                return;
            }
            StringTokenizer strtok = new StringTokenizer(line);
            if (strtok.countTokens() != 0) {
                mime_type = strtok.nextToken();
                while (strtok.hasMoreTokens()) {
                    file_ext = strtok.nextToken();
                    entry = new MimeTypeEntry(mime_type, file_ext);
                    this.type_hash.put(file_ext, entry);
                    if (LogSupport.isLoggable()) {
                        LogSupport.log("Added: " + entry.toString());
                    }
                }
            }
        }
    }
}
