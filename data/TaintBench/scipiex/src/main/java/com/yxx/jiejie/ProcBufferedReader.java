package com.yxx.jiejie;

import java.io.BufferedReader;
import java.io.PrintWriter;

public interface ProcBufferedReader {
    void proc(BufferedReader bufferedReader) throws Exception;

    void writeToFile(PrintWriter printWriter) throws Exception;
}
