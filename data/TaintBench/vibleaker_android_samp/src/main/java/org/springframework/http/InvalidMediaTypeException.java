package org.springframework.http;

public class InvalidMediaTypeException extends IllegalArgumentException {
    private String mediaType;

    public InvalidMediaTypeException(String mediaType, String msg) {
        super("Invalid media type \"" + mediaType + "\": " + msg);
        this.mediaType = mediaType;
    }

    public String getMediaType() {
        return this.mediaType;
    }
}
