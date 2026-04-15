package org.springframework.http;

import java.net.URI;

public interface HttpRequest extends HttpMessage {
    HttpMethod getMethod();

    URI getURI();
}
