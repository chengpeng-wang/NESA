package com.labado.lulaoshi;

public class ImageAndText {
    private String imageUrl;
    private String text;
    private String vodname;

    public ImageAndText(String imageUrl, String vodname, String text) {
        this.imageUrl = imageUrl;
        this.text = text;
        this.vodname = vodname;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public String getText() {
        return this.text;
    }

    public String getvodname() {
        return this.vodname;
    }
}
