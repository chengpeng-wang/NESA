package com.mopub.mraid;

class MraidCommandException extends Exception {
    MraidCommandException() {
    }

    MraidCommandException(String detailMessage) {
        super(detailMessage);
    }

    MraidCommandException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    MraidCommandException(Throwable throwable) {
        super(throwable);
    }
}
