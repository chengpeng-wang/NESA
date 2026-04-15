package org.springframework.web.client;

import java.io.IOException;
import org.springframework.http.client.ClientHttpResponse;

public interface ResponseErrorHandler {
    void handleError(ClientHttpResponse clientHttpResponse) throws IOException;

    boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException;
}
