package com.beita.contact;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class ContactUtil {
    public static void write(String filePath, String contentStr) throws Exception {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath, true), "utf-8"));
        bw.write(new StringBuilder(String.valueOf(contentStr)).append("\n").toString());
        bw.flush();
        bw.close();
    }
}
