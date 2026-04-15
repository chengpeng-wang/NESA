package org.springframework.web.client;

import java.net.URI;
import java.util.Map;
import java.util.Set;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public interface RestOperations {
    void delete(String str, Map<String, ?> map) throws RestClientException;

    void delete(String str, Object... objArr) throws RestClientException;

    void delete(URI uri) throws RestClientException;

    <T> ResponseEntity<T> exchange(String str, HttpMethod httpMethod, HttpEntity<?> httpEntity, Class<T> cls, Map<String, ?> map) throws RestClientException;

    <T> ResponseEntity<T> exchange(String str, HttpMethod httpMethod, HttpEntity<?> httpEntity, Class<T> cls, Object... objArr) throws RestClientException;

    <T> ResponseEntity<T> exchange(String str, HttpMethod httpMethod, HttpEntity<?> httpEntity, ParameterizedTypeReference<T> parameterizedTypeReference, Map<String, ?> map) throws RestClientException;

    <T> ResponseEntity<T> exchange(String str, HttpMethod httpMethod, HttpEntity<?> httpEntity, ParameterizedTypeReference<T> parameterizedTypeReference, Object... objArr) throws RestClientException;

    <T> ResponseEntity<T> exchange(URI uri, HttpMethod httpMethod, HttpEntity<?> httpEntity, Class<T> cls) throws RestClientException;

    <T> ResponseEntity<T> exchange(URI uri, HttpMethod httpMethod, HttpEntity<?> httpEntity, ParameterizedTypeReference<T> parameterizedTypeReference) throws RestClientException;

    <T> T execute(String str, HttpMethod httpMethod, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor, Map<String, ?> map) throws RestClientException;

    <T> T execute(String str, HttpMethod httpMethod, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor, Object... objArr) throws RestClientException;

    <T> T execute(URI uri, HttpMethod httpMethod, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor) throws RestClientException;

    <T> ResponseEntity<T> getForEntity(String str, Class<T> cls, Map<String, ?> map) throws RestClientException;

    <T> ResponseEntity<T> getForEntity(String str, Class<T> cls, Object... objArr) throws RestClientException;

    <T> ResponseEntity<T> getForEntity(URI uri, Class<T> cls) throws RestClientException;

    <T> T getForObject(String str, Class<T> cls, Map<String, ?> map) throws RestClientException;

    <T> T getForObject(String str, Class<T> cls, Object... objArr) throws RestClientException;

    <T> T getForObject(URI uri, Class<T> cls) throws RestClientException;

    HttpHeaders headForHeaders(String str, Map<String, ?> map) throws RestClientException;

    HttpHeaders headForHeaders(String str, Object... objArr) throws RestClientException;

    HttpHeaders headForHeaders(URI uri) throws RestClientException;

    Set<HttpMethod> optionsForAllow(String str, Map<String, ?> map) throws RestClientException;

    Set<HttpMethod> optionsForAllow(String str, Object... objArr) throws RestClientException;

    Set<HttpMethod> optionsForAllow(URI uri) throws RestClientException;

    <T> ResponseEntity<T> postForEntity(String str, Object obj, Class<T> cls, Map<String, ?> map) throws RestClientException;

    <T> ResponseEntity<T> postForEntity(String str, Object obj, Class<T> cls, Object... objArr) throws RestClientException;

    <T> ResponseEntity<T> postForEntity(URI uri, Object obj, Class<T> cls) throws RestClientException;

    URI postForLocation(String str, Object obj, Map<String, ?> map) throws RestClientException;

    URI postForLocation(String str, Object obj, Object... objArr) throws RestClientException;

    URI postForLocation(URI uri, Object obj) throws RestClientException;

    <T> T postForObject(String str, Object obj, Class<T> cls, Map<String, ?> map) throws RestClientException;

    <T> T postForObject(String str, Object obj, Class<T> cls, Object... objArr) throws RestClientException;

    <T> T postForObject(URI uri, Object obj, Class<T> cls) throws RestClientException;

    void put(String str, Object obj, Map<String, ?> map) throws RestClientException;

    void put(String str, Object obj, Object... objArr) throws RestClientException;

    void put(URI uri, Object obj) throws RestClientException;
}
