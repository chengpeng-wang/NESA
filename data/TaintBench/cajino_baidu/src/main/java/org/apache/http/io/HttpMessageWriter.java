package org.apache.http.io;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;

public interface HttpMessageWriter {
    void write(HttpMessage httpMessage) throws IOException, HttpException;
}
