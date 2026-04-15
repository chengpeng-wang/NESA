package org.springframework.http.client;

import java.io.IOException;
import org.springframework.http.HttpRequest;

public interface ClientHttpRequestExecution {
    ClientHttpResponse execute(HttpRequest httpRequest, byte[] bArr) throws IOException;
}
