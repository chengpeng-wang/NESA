package com.qc.entity;

import java.util.List;

public class SilencePager {
    private List<MotionActive> activies;
    private String url;

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<MotionActive> getActivies() {
        return this.activies;
    }

    public void setActivies(List<MotionActive> activies) {
        this.activies = activies;
    }
}
