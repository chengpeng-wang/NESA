package com.mopub.volley.toolbox;

import com.mopub.volley.AuthFailureError;

public interface Authenticator {
    String getAuthToken() throws AuthFailureError;

    void invalidateAuthToken(String str);
}
