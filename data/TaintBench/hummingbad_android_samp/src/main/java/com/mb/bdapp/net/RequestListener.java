package com.mb.bdapp.net;

import java.io.IOException;

public interface RequestListener {
    void onComplete(String str);

    void onError(Exception exception);

    void onIOException(IOException iOException);
}
