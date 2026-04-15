package org.springframework.http.client;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.http.HttpHeaders;

final class HttpComponentsClientHttpResponse extends AbstractClientHttpResponse {
    private HttpHeaders headers;
    private final CloseableHttpResponse httpResponse;

    HttpComponentsClientHttpResponse(CloseableHttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }

    public int getRawStatusCode() throws IOException {
        return this.httpResponse.getStatusLine().getStatusCode();
    }

    public String getStatusText() throws IOException {
        return this.httpResponse.getStatusLine().getReasonPhrase();
    }

    public HttpHeaders getHeaders() {
        if (this.headers == null) {
            this.headers = new HttpHeaders();
            for (Header header : this.httpResponse.getAllHeaders()) {
                this.headers.add(header.getName(), header.getValue());
            }
        }
        return this.headers;
    }

    public InputStream getBodyInternal() throws IOException {
        HttpEntity entity = this.httpResponse.getEntity();
        return entity != null ? entity.getContent() : null;
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    public void closeInternal() {
        /*
        r2 = this;
        r0 = r2.httpResponse;	 Catch:{ all -> 0x000f }
        r0 = r0.getEntity();	 Catch:{ all -> 0x000f }
        org.apache.http.util.EntityUtilsHC4.consume(r0);	 Catch:{ all -> 0x000f }
        r0 = r2.httpResponse;	 Catch:{ IOException -> 0x0016 }
        r0.close();	 Catch:{ IOException -> 0x0016 }
    L_0x000e:
        return;
    L_0x000f:
        r0 = move-exception;
        r1 = r2.httpResponse;	 Catch:{ IOException -> 0x0016 }
        r1.close();	 Catch:{ IOException -> 0x0016 }
        throw r0;	 Catch:{ IOException -> 0x0016 }
    L_0x0016:
        r0 = move-exception;
        goto L_0x000e;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.springframework.http.client.HttpComponentsClientHttpResponse.closeInternal():void");
    }
}
