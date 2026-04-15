package org.springframework.http;

import org.springframework.util.MultiValueMap;

public class ResponseEntity<T> extends HttpEntity<T> {
    private final HttpStatus statusCode;

    public ResponseEntity(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public ResponseEntity(T body, HttpStatus statusCode) {
        super((Object) body);
        this.statusCode = statusCode;
    }

    public ResponseEntity(MultiValueMap<String, String> headers, HttpStatus statusCode) {
        super((MultiValueMap) headers);
        this.statusCode = statusCode;
    }

    public ResponseEntity(T body, MultiValueMap<String, String> headers, HttpStatus statusCode) {
        super(body, headers);
        this.statusCode = statusCode;
    }

    public HttpStatus getStatusCode() {
        return this.statusCode;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("<");
        builder.append(this.statusCode.toString());
        builder.append(' ');
        builder.append(this.statusCode.getReasonPhrase());
        builder.append(',');
        T body = getBody();
        HttpHeaders headers = getHeaders();
        if (body != null) {
            builder.append(body);
            if (headers != null) {
                builder.append(',');
            }
        }
        if (headers != null) {
            builder.append(headers);
        }
        builder.append('>');
        return builder.toString();
    }
}
