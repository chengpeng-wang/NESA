package com.mopub.volley;

public interface Network {
    NetworkResponse performRequest(Request<?> request) throws VolleyError;
}
