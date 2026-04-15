package org.springframework.web.client;

import android.util.Log;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.InterceptingHttpAccessor;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.web.util.UriTemplate;

public class RestTemplate extends InterceptingHttpAccessor implements RestOperations {
    private static final String TAG = "RestTemplate";
    private ResponseErrorHandler errorHandler;
    private final ResponseExtractor<HttpHeaders> headersExtractor;
    private final List<HttpMessageConverter<?>> messageConverters;

    private class AcceptHeaderRequestCallback implements RequestCallback {
        private final Type responseType;

        private AcceptHeaderRequestCallback(Type responseType) {
            this.responseType = responseType;
        }

        public void doWithRequest(ClientHttpRequest request) throws IOException {
            if (this.responseType != null) {
                Class<?> responseClass = null;
                if (this.responseType instanceof Class) {
                    responseClass = this.responseType;
                }
                List<MediaType> allSupportedMediaTypes = new ArrayList();
                for (HttpMessageConverter<?> converter : RestTemplate.this.getMessageConverters()) {
                    if (responseClass != null) {
                        if (converter.canRead(responseClass, null)) {
                            allSupportedMediaTypes.addAll(getSupportedMediaTypes(converter));
                        }
                    } else if ((converter instanceof GenericHttpMessageConverter) && ((GenericHttpMessageConverter) converter).canRead(this.responseType, null, null)) {
                        allSupportedMediaTypes.addAll(getSupportedMediaTypes(converter));
                    }
                }
                if (!allSupportedMediaTypes.isEmpty()) {
                    MediaType.sortBySpecificity(allSupportedMediaTypes);
                    if (Log.isLoggable(RestTemplate.TAG, 3)) {
                        Log.d(RestTemplate.TAG, "Setting request Accept header to " + allSupportedMediaTypes);
                    }
                    request.getHeaders().setAccept(allSupportedMediaTypes);
                }
            }
        }

        private List<MediaType> getSupportedMediaTypes(HttpMessageConverter<?> messageConverter) {
            List<MediaType> supportedMediaTypes = messageConverter.getSupportedMediaTypes();
            List<MediaType> result = new ArrayList(supportedMediaTypes.size());
            for (MediaType supportedMediaType : supportedMediaTypes) {
                MediaType supportedMediaType2;
                if (supportedMediaType2.getCharSet() != null) {
                    supportedMediaType2 = new MediaType(supportedMediaType2.getType(), supportedMediaType2.getSubtype());
                }
                result.add(supportedMediaType2);
            }
            return result;
        }
    }

    private static class DefaultMessageConverters {
        private static final boolean gsonPresent = ClassUtils.isPresent("com.google.gson.Gson", RestTemplate.class.getClassLoader());
        private static final boolean jackson2Present;
        private static final boolean javaxXmlTransformPresent = ClassUtils.isPresent("javax.xml.transform.Source", RestTemplate.class.getClassLoader());
        private static final boolean simpleXmlPresent = ClassUtils.isPresent("org.simpleframework.xml.Serializer", RestTemplate.class.getClassLoader());

        private DefaultMessageConverters() {
        }

        static {
            boolean z = ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", RestTemplate.class.getClassLoader()) && ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator", RestTemplate.class.getClassLoader());
            jackson2Present = z;
        }

        public static void init(List<HttpMessageConverter<?>> messageConverters) {
            messageConverters.add(new ByteArrayHttpMessageConverter());
            messageConverters.add(new StringHttpMessageConverter());
            messageConverters.add(new ResourceHttpMessageConverter());
            if (javaxXmlTransformPresent) {
                messageConverters.add(new SourceHttpMessageConverter());
                messageConverters.add(new AllEncompassingFormHttpMessageConverter());
            } else {
                messageConverters.add(new FormHttpMessageConverter());
            }
            if (simpleXmlPresent) {
                messageConverters.add(new SimpleXmlHttpMessageConverter());
            }
            if (jackson2Present) {
                messageConverters.add(new MappingJackson2HttpMessageConverter());
            } else if (gsonPresent) {
                messageConverters.add(new GsonHttpMessageConverter());
            }
        }
    }

    private static class HeadersExtractor implements ResponseExtractor<HttpHeaders> {
        private HeadersExtractor() {
        }

        public HttpHeaders extractData(ClientHttpResponse response) throws IOException {
            return response.getHeaders();
        }
    }

    private class HttpEntityRequestCallback extends AcceptHeaderRequestCallback {
        private final HttpEntity<?> requestEntity;

        private HttpEntityRequestCallback(RestTemplate restTemplate, Object requestBody) {
            this(requestBody, null);
        }

        private HttpEntityRequestCallback(Object requestBody, Type responseType) {
            super(responseType);
            if (requestBody instanceof HttpEntity) {
                this.requestEntity = (HttpEntity) requestBody;
            } else if (requestBody != null) {
                this.requestEntity = new HttpEntity(requestBody);
            } else {
                this.requestEntity = HttpEntity.EMPTY;
            }
        }

        public void doWithRequest(ClientHttpRequest httpRequest) throws IOException {
            super.doWithRequest(httpRequest);
            HttpHeaders requestHeaders;
            if (this.requestEntity.hasBody()) {
                Object requestBody = this.requestEntity.getBody();
                Class<?> requestType = requestBody.getClass();
                requestHeaders = this.requestEntity.getHeaders();
                MediaType requestContentType = requestHeaders.getContentType();
                for (HttpMessageConverter<?> messageConverter : RestTemplate.this.getMessageConverters()) {
                    if (messageConverter.canWrite(requestType, requestContentType)) {
                        if (!requestHeaders.isEmpty()) {
                            httpRequest.getHeaders().putAll(requestHeaders);
                        }
                        if (Log.isLoggable(RestTemplate.TAG, 3)) {
                            if (requestContentType != null) {
                                Log.d(RestTemplate.TAG, "Writing [" + requestBody + "] as \"" + requestContentType + "\" using [" + messageConverter + "]");
                            } else {
                                Log.d(RestTemplate.TAG, "Writing [" + requestBody + "] using [" + messageConverter + "]");
                            }
                        }
                        messageConverter.write(requestBody, requestContentType, httpRequest);
                        return;
                    }
                }
                String message = "Could not write request: no suitable HttpMessageConverter found for request type [" + requestType.getName() + "]";
                if (requestContentType != null) {
                    message = message + " and content type [" + requestContentType + "]";
                }
                throw new RestClientException(message);
            }
            HttpHeaders httpHeaders = httpRequest.getHeaders();
            requestHeaders = this.requestEntity.getHeaders();
            if (!requestHeaders.isEmpty()) {
                httpHeaders.putAll(requestHeaders);
            }
            if (httpHeaders.getContentLength() == -1) {
                httpHeaders.setContentLength(0);
            }
        }
    }

    private class ResponseEntityResponseExtractor<T> implements ResponseExtractor<ResponseEntity<T>> {
        private final HttpMessageConverterExtractor<T> delegate;

        public ResponseEntityResponseExtractor(Type responseType) {
            if (responseType == null || Void.class.equals(responseType)) {
                this.delegate = null;
            } else {
                this.delegate = new HttpMessageConverterExtractor(responseType, RestTemplate.this.getMessageConverters());
            }
        }

        public ResponseEntity<T> extractData(ClientHttpResponse response) throws IOException {
            if (this.delegate != null) {
                return new ResponseEntity(this.delegate.extractData(response), response.getHeaders(), response.getStatusCode());
            }
            return new ResponseEntity(response.getHeaders(), response.getStatusCode());
        }
    }

    public RestTemplate() {
        this.messageConverters = new ArrayList();
        this.errorHandler = new DefaultResponseErrorHandler();
        this.headersExtractor = new HeadersExtractor();
        DefaultMessageConverters.init(this.messageConverters);
    }

    @Deprecated
    public RestTemplate(boolean registerDefaultConverters) {
        this.messageConverters = new ArrayList();
        this.errorHandler = new DefaultResponseErrorHandler();
        this.headersExtractor = new HeadersExtractor();
        if (registerDefaultConverters) {
            DefaultMessageConverters.init(this.messageConverters);
        }
    }

    public RestTemplate(ClientHttpRequestFactory requestFactory) {
        this();
        setRequestFactory(requestFactory);
    }

    @Deprecated
    public RestTemplate(boolean registerDefaultConverters, ClientHttpRequestFactory requestFactory) {
        this(registerDefaultConverters);
        setRequestFactory(requestFactory);
    }

    public RestTemplate(List<HttpMessageConverter<?>> messageConverters) {
        this.messageConverters = new ArrayList();
        this.errorHandler = new DefaultResponseErrorHandler();
        this.headersExtractor = new HeadersExtractor();
        Assert.notEmpty((Collection) messageConverters, "'messageConverters' must not be empty");
        this.messageConverters.addAll(messageConverters);
    }

    public void setMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
        Assert.notEmpty((Collection) messageConverters, "'messageConverters' must not be empty");
        if (this.messageConverters != messageConverters) {
            this.messageConverters.clear();
            this.messageConverters.addAll(messageConverters);
        }
    }

    public List<HttpMessageConverter<?>> getMessageConverters() {
        return this.messageConverters;
    }

    public void setErrorHandler(ResponseErrorHandler errorHandler) {
        Assert.notNull(errorHandler, "'errorHandler' must not be null");
        this.errorHandler = errorHandler;
    }

    public ResponseErrorHandler getErrorHandler() {
        return this.errorHandler;
    }

    public <T> T getForObject(String url, Class<T> responseType, Object... urlVariables) throws RestClientException {
        return execute(url, HttpMethod.GET, new AcceptHeaderRequestCallback(responseType), new HttpMessageConverterExtractor((Class) responseType, getMessageConverters()), urlVariables);
    }

    public <T> T getForObject(String url, Class<T> responseType, Map<String, ?> urlVariables) throws RestClientException {
        return execute(url, HttpMethod.GET, new AcceptHeaderRequestCallback(responseType), new HttpMessageConverterExtractor((Class) responseType, getMessageConverters()), (Map) urlVariables);
    }

    public <T> T getForObject(URI url, Class<T> responseType) throws RestClientException {
        return execute(url, HttpMethod.GET, new AcceptHeaderRequestCallback(responseType), new HttpMessageConverterExtractor((Class) responseType, getMessageConverters()));
    }

    public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Object... urlVariables) throws RestClientException {
        return (ResponseEntity) execute(url, HttpMethod.GET, new AcceptHeaderRequestCallback(responseType), new ResponseEntityResponseExtractor(responseType), urlVariables);
    }

    public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Map<String, ?> urlVariables) throws RestClientException {
        return (ResponseEntity) execute(url, HttpMethod.GET, new AcceptHeaderRequestCallback(responseType), new ResponseEntityResponseExtractor(responseType), (Map) urlVariables);
    }

    public <T> ResponseEntity<T> getForEntity(URI url, Class<T> responseType) throws RestClientException {
        return (ResponseEntity) execute(url, HttpMethod.GET, new AcceptHeaderRequestCallback(responseType), new ResponseEntityResponseExtractor(responseType));
    }

    public HttpHeaders headForHeaders(String url, Object... urlVariables) throws RestClientException {
        return (HttpHeaders) execute(url, HttpMethod.HEAD, null, this.headersExtractor, urlVariables);
    }

    public HttpHeaders headForHeaders(String url, Map<String, ?> urlVariables) throws RestClientException {
        return (HttpHeaders) execute(url, HttpMethod.HEAD, null, this.headersExtractor, (Map) urlVariables);
    }

    public HttpHeaders headForHeaders(URI url) throws RestClientException {
        return (HttpHeaders) execute(url, HttpMethod.HEAD, null, this.headersExtractor);
    }

    public URI postForLocation(String url, Object request, Object... urlVariables) throws RestClientException {
        return ((HttpHeaders) execute(url, HttpMethod.POST, new HttpEntityRequestCallback(request), this.headersExtractor, urlVariables)).getLocation();
    }

    public URI postForLocation(String url, Object request, Map<String, ?> urlVariables) throws RestClientException {
        return ((HttpHeaders) execute(url, HttpMethod.POST, new HttpEntityRequestCallback(request), this.headersExtractor, (Map) urlVariables)).getLocation();
    }

    public URI postForLocation(URI url, Object request) throws RestClientException {
        return ((HttpHeaders) execute(url, HttpMethod.POST, new HttpEntityRequestCallback(request), this.headersExtractor)).getLocation();
    }

    public <T> T postForObject(String url, Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
        return execute(url, HttpMethod.POST, new HttpEntityRequestCallback(request, responseType), new HttpMessageConverterExtractor((Class) responseType, getMessageConverters()), uriVariables);
    }

    public <T> T postForObject(String url, Object request, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
        return execute(url, HttpMethod.POST, new HttpEntityRequestCallback(request, responseType), new HttpMessageConverterExtractor((Class) responseType, getMessageConverters()), (Map) uriVariables);
    }

    public <T> T postForObject(URI url, Object request, Class<T> responseType) throws RestClientException {
        return execute(url, HttpMethod.POST, new HttpEntityRequestCallback(request, responseType), new HttpMessageConverterExtractor((Class) responseType, getMessageConverters()));
    }

    public <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
        return (ResponseEntity) execute(url, HttpMethod.POST, new HttpEntityRequestCallback(request, responseType), new ResponseEntityResponseExtractor(responseType), uriVariables);
    }

    public <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
        return (ResponseEntity) execute(url, HttpMethod.POST, new HttpEntityRequestCallback(request, responseType), new ResponseEntityResponseExtractor(responseType), (Map) uriVariables);
    }

    public <T> ResponseEntity<T> postForEntity(URI url, Object request, Class<T> responseType) throws RestClientException {
        return (ResponseEntity) execute(url, HttpMethod.POST, new HttpEntityRequestCallback(request, responseType), new ResponseEntityResponseExtractor(responseType));
    }

    public void put(String url, Object request, Object... urlVariables) throws RestClientException {
        String str = url;
        execute(str, HttpMethod.PUT, new HttpEntityRequestCallback(request), null, urlVariables);
    }

    public void put(String url, Object request, Map<String, ?> urlVariables) throws RestClientException {
        String str = url;
        execute(str, HttpMethod.PUT, new HttpEntityRequestCallback(request), null, (Map) urlVariables);
    }

    public void put(URI url, Object request) throws RestClientException {
        execute(url, HttpMethod.PUT, new HttpEntityRequestCallback(request), null);
    }

    public void delete(String url, Object... urlVariables) throws RestClientException {
        execute(url, HttpMethod.DELETE, null, null, urlVariables);
    }

    public void delete(String url, Map<String, ?> urlVariables) throws RestClientException {
        execute(url, HttpMethod.DELETE, null, null, (Map) urlVariables);
    }

    public void delete(URI url) throws RestClientException {
        execute(url, HttpMethod.DELETE, null, null);
    }

    public Set<HttpMethod> optionsForAllow(String url, Object... urlVariables) throws RestClientException {
        return ((HttpHeaders) execute(url, HttpMethod.OPTIONS, null, this.headersExtractor, urlVariables)).getAllow();
    }

    public Set<HttpMethod> optionsForAllow(String url, Map<String, ?> urlVariables) throws RestClientException {
        return ((HttpHeaders) execute(url, HttpMethod.OPTIONS, null, this.headersExtractor, (Map) urlVariables)).getAllow();
    }

    public Set<HttpMethod> optionsForAllow(URI url) throws RestClientException {
        return ((HttpHeaders) execute(url, HttpMethod.OPTIONS, null, this.headersExtractor)).getAllow();
    }

    public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) throws RestClientException {
        return (ResponseEntity) execute(url, method, new HttpEntityRequestCallback(requestEntity, responseType), new ResponseEntityResponseExtractor(responseType), uriVariables);
    }

    public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
        return (ResponseEntity) execute(url, method, new HttpEntityRequestCallback(requestEntity, responseType), new ResponseEntityResponseExtractor(responseType), (Map) uriVariables);
    }

    public <T> ResponseEntity<T> exchange(URI url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType) throws RestClientException {
        return (ResponseEntity) execute(url, method, new HttpEntityRequestCallback(requestEntity, responseType), new ResponseEntityResponseExtractor(responseType));
    }

    public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType, Object... uriVariables) throws RestClientException {
        Type type = responseType.getType();
        return (ResponseEntity) execute(url, method, new HttpEntityRequestCallback(requestEntity, type), new ResponseEntityResponseExtractor(type), uriVariables);
    }

    public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
        Type type = responseType.getType();
        return (ResponseEntity) execute(url, method, new HttpEntityRequestCallback(requestEntity, type), new ResponseEntityResponseExtractor(type), (Map) uriVariables);
    }

    public <T> ResponseEntity<T> exchange(URI url, HttpMethod method, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType) throws RestClientException {
        Type type = responseType.getType();
        return (ResponseEntity) execute(url, method, new HttpEntityRequestCallback(requestEntity, type), new ResponseEntityResponseExtractor(type));
    }

    public <T> T execute(String url, HttpMethod method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor, Object... urlVariables) throws RestClientException {
        return doExecute(new UriTemplate(url).expand(urlVariables), method, requestCallback, responseExtractor);
    }

    public <T> T execute(String url, HttpMethod method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor, Map<String, ?> urlVariables) throws RestClientException {
        return doExecute(new UriTemplate(url).expand((Map) urlVariables), method, requestCallback, responseExtractor);
    }

    public <T> T execute(URI url, HttpMethod method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor) throws RestClientException {
        return doExecute(url, method, requestCallback, responseExtractor);
    }

    /* access modifiers changed from: protected */
    public <T> T doExecute(URI url, HttpMethod method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor) throws RestClientException {
        Assert.notNull(url, "'url' must not be null");
        Assert.notNull(method, "'method' must not be null");
        ClientHttpResponse response = null;
        try {
            T extractData;
            ClientHttpRequest request = createRequest(url, method);
            if (requestCallback != null) {
                requestCallback.doWithRequest(request);
            }
            response = request.execute();
            if (getErrorHandler().hasError(response)) {
                handleResponseError(method, url, response);
            } else {
                logResponseStatus(method, url, response);
            }
            if (responseExtractor != null) {
                extractData = responseExtractor.extractData(response);
                if (response != null) {
                    response.close();
                }
            } else {
                extractData = null;
                if (response != null) {
                    response.close();
                }
            }
            return extractData;
        } catch (IOException ex) {
            throw new ResourceAccessException("I/O error on " + method.name() + " request for \"" + url + "\": " + ex.getMessage(), ex);
        } catch (Throwable th) {
            if (response != null) {
                response.close();
            }
        }
    }

    private void logResponseStatus(HttpMethod method, URI url, ClientHttpResponse response) {
        if (Log.isLoggable(TAG, 3)) {
            try {
                Log.d(TAG, method.name() + " request for \"" + url + "\" resulted in " + response.getStatusCode() + " (" + response.getStatusText() + ")");
            } catch (IOException e) {
            }
        }
    }

    private void handleResponseError(HttpMethod method, URI url, ClientHttpResponse response) throws IOException {
        if (Log.isLoggable(TAG, 5)) {
            try {
                Log.w(TAG, method.name() + " request for \"" + url + "\" resulted in " + response.getStatusCode() + " (" + response.getStatusText() + "); invoking error handler");
            } catch (IOException e) {
            }
        }
        getErrorHandler().handleError(response);
    }
}
