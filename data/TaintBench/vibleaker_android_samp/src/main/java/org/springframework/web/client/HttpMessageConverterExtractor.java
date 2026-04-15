package org.springframework.web.client;

import android.util.Log;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.Assert;

public class HttpMessageConverterExtractor<T> implements ResponseExtractor<T> {
    private static final String TAG = "RestTemplate";
    private final List<HttpMessageConverter<?>> messageConverters;
    private final Class<T> responseClass;
    private final Type responseType;

    public HttpMessageConverterExtractor(Class<T> responseType, List<HttpMessageConverter<?>> messageConverters) {
        this((Type) responseType, (List) messageConverters);
    }

    public HttpMessageConverterExtractor(Type responseType, List<HttpMessageConverter<?>> messageConverters) {
        Assert.notNull(responseType, "'responseType' must not be null");
        Assert.notEmpty((Collection) messageConverters, "'messageConverters' must not be empty");
        this.responseType = responseType;
        this.responseClass = responseType instanceof Class ? (Class) responseType : null;
        this.messageConverters = messageConverters;
    }

    public T extractData(ClientHttpResponse response) throws IOException {
        if (!hasMessageBody(response)) {
            return null;
        }
        MediaType contentType = getContentType(response);
        for (HttpMessageConverter messageConverter : this.messageConverters) {
            if (messageConverter instanceof GenericHttpMessageConverter) {
                GenericHttpMessageConverter genericMessageConverter = (GenericHttpMessageConverter) messageConverter;
                if (genericMessageConverter.canRead(this.responseType, null, contentType)) {
                    if (Log.isLoggable(TAG, 3)) {
                        Log.d(TAG, "Reading [" + this.responseType + "] as \"" + contentType + "\" using [" + messageConverter + "]");
                    }
                    return genericMessageConverter.read(this.responseType, null, response);
                }
            }
            if (this.responseClass != null && messageConverter.canRead(this.responseClass, contentType)) {
                if (Log.isLoggable(TAG, 3)) {
                    Log.d(TAG, "Reading [" + this.responseClass.getName() + "] as \"" + contentType + "\" using [" + messageConverter + "]");
                }
                return messageConverter.read(this.responseClass, response);
            }
        }
        throw new RestClientException("Could not extract response: no suitable HttpMessageConverter found for response type [" + this.responseType + "] and content type [" + contentType + "]");
    }

    private MediaType getContentType(ClientHttpResponse response) {
        MediaType contentType = response.getHeaders().getContentType();
        if (contentType != null) {
            return contentType;
        }
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, "No Content-Type header found, defaulting to application/octet-stream");
        }
        return MediaType.APPLICATION_OCTET_STREAM;
    }

    /* access modifiers changed from: protected */
    public boolean hasMessageBody(ClientHttpResponse response) throws IOException {
        HttpStatus responseStatus = response.getStatusCode();
        if (responseStatus == HttpStatus.NO_CONTENT || responseStatus == HttpStatus.NOT_MODIFIED || response.getHeaders().getContentLength() == 0) {
            return false;
        }
        return true;
    }
}
